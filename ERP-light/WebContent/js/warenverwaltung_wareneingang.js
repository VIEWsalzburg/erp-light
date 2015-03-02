//Get all incoming deliveries and load into table
function loadTableContent(loadArchivedEntries){
	
	// show loading spinner
	showLoadingSpinner(true);
	
	// get all organisations and save it in variable to search for when loading all incoming deliveries
	var organisations;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getAllOrganisations"
	}).done(function(data) {
			organisations = data;	// already JSON
	});
	
	//check if only non archived entries should be loaded to table
	if(loadArchivedEntries == 1){
		loadArchivedEntries = "";
	}
	else{
		loadArchivedEntries = "Unarchived";
	}
	
	$.ajax({
		type : "POST",
		url : "../rest/secure/incomingDelivery/getAll" + loadArchivedEntries
	}).done(function(data) {
		var inc = data;	// already JSON

		for (var e in inc) {
			
			//get organisation by id
			var org;

			// search organisation in variable
			for (i in organisations)
			{
				if (organisations[i].id == inc[e].organisationId)
				{
					org = organisations[i];
				}
			}
			
			//get articles
			var articleString = "";
			var articles = inc[e].incomingArticleDTOs;
			
			// sort articles according to their articleNr
			articles.sort( function(a, b) { 
				return (a.articleNr - b.articleNr);
			} );
			
			for(var i=0; i < articles.length; i++){
				articleString = articleString + articles[i].articleDTO.description;
				
				if(i < articles.length - 1){
					articleString = articleString + ", ";
				}
			}
			
			var bookedClass = "";
			if (inc[e].booked > 0)
				bookedClass = "booked-entry";	// set the class to display as booked
			
			//check archived flag
			var archivedCheckboxState = "";
			if(inc[e].archived > 0){
				archivedCheckboxState = "checked";
			}
			
			var tableRow = "<tr class='"+ bookedClass + " " + archivedCheckboxState +"'>" + "<td class='hidden'>" + inc[e].incomingDeliveryId
					+ "</td>" + "<td>" + org.name + ", " + "<br/>" + org.zip + " " + org.city + "," + "<br/>" + org.country 
					+ "</td>" + "<td>" + inc[e].date
					+ "</td>" + "<td>" + articleString
					+ "</td>" + "<td>" + inc[e].comment
					+ "</td>" + "<td>" + "<input type='checkbox' id='cbx_archived' disabled "+ archivedCheckboxState +">"
					+ "</td>" + "</tr>";

			$("#incomingDeliveryTableBody").append(tableRow);
		}
		
		// hide loading spinner
		showLoadingSpinner(false);
	});
	
};

//Get all non archived incoming deliveries and load into table
$(document).ready(loadTableContent(loadArchivedEntries));

//init collapse
$(function () {
	$('#details .collapse').collapse();		// collapse all accordions of the details modal
});

//switch to new incoming deliveries tab
$("#btn_new").click(function() {
	//switch to new incoming deliveries with GET parameter mode=new and id=-1
	location.href="warenverwaltung_neuerwareneingang.html?mode=new&id=-1";
	return false;
});

//switch to edit incoming deliveries tab
$("#btn_edit").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Kein Wareneingang auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
	//switch to edit incoming deliveries with GET parameter mode=edit and id=...
	location.href="warenverwaltung_neuerwareneingang.html?mode=edit&booked="+ isBooked +"&id=" + id;
	return false;
});

//load a single (contact)Person into a global variable
//function returns the name of a person
var contactPerson;
function loadContactPerson(id) {
	var nameString="";
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/person/getPersonById/" + id
	}).done(function(data) {
				contactPerson = data;	// already JSON
				nameString = contactPerson.lastName + " " + contactPerson.firstName;
			});
	return nameString;
};

//load details modal
$("#btn_details").click(function() {
	//remove container
	$(".details").remove();
	$(".persondivider").remove();
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Kein Wareneingang auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
	//get incoming delivery by id
	var inc;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/incomingDelivery/getById/" + id
	}).done(function(data) {
		inc = data;	// already JSON
	});
				
	//get organisation by id
	var org;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getOrganisationById/" + inc.organisationId
	}).done(function(data) {
		org = data;	// already JSON
	});
	
	$("#label_name_details").text(org.name);
	$("#label_address_details").text(org.address);
	$("#label_zip_details").text(org.zip);
	$("#label_city_details").text(org.city);
	$("#label_country_details").text(org.country);
		
	if(org.comment == ""){
		$("#label_comment_details").text("-");
	}
	else{
		$("#label_comment_details").text(org.comment);
	}
	
	//load personIds
	var contactPersonIds = org.personIds;
	if(contactPersonIds.length == 0){
		$("#label_personIds_details").text("-");
	}
	else{
		var personString = "";
			for (var j = 0; j < contactPersonIds.length; j++) {
				loadContactPerson(contactPersonIds[j]);
				
				//load contact person name
				personString = contactPerson.lastName + " " + contactPerson.firstName;
				var template = "<div class='row details'><div class='col-md-6'><label>Name</label></div><div class='col-md-6'><label>" + personString + "</label></div></div>";
				$("#person_container_details").append(template);
				
				//load contact person phone numbers
				var phoneNumbers = contactPerson.telephones;
				if(phoneNumbers.length == 0){
					var template = "<div class='row details'><div class='col-md-6'><label>Telefonnummer</label></div><div class='col-md-6'><label>-</label></div></div>";
					$("#person_container_details").append(template);
				}
				else{
					var phoneString = phoneNumbers[0].telephone + " (" + phoneNumbers[0].type.toLowerCase() + ")";
					var template = "<div class='row details'><div class='col-md-6'><label>Telefonnummer</label></div><div class='col-md-6'><label>" + phoneString + "</label></div></div>";
					$("#person_container_details").append(template);
					
					for (var k = 1; k < phoneNumbers.length; k++) {
						phoneString = phoneNumbers[k].telephone + " (" + phoneNumbers[k].type.toLowerCase() + ")";
						var template = "<div class='row details'><div class='col-md-6'></div><div class='col-md-6'><label>" + phoneString + "</label></div></div>";
						$("#person_container_details").append(template);
					}
				}
				
				//load contact person emails
				var emails = contactPerson.emails;
				if(emails.length == 0){
					var template = "<div class='row details'><div class='col-md-6'><label>Email-Adresse</label></div><div class='col-md-6'><label>-</label></div></div>";
					$("#person_container_details").append(template);
				}
				else{
					var emailString = emails[0].mail + " (" + emails[0].type.toLowerCase() + ")";
					var template = "<div class='row details'><div class='col-md-6'><label>Email-Adresse</label></div><div class='col-md-6'><label>" + emailString + "</label></div></div>";
					$("#person_container_details").append(template);
					
					for (var l = 1; l < emails.length; l++) {
						emailString = emails[l].mail + " (" + emails[l].type.toLowerCase() + ")";
						var template = "<div class='row details'><div class='col-md-6'></div><div class='col-md-6'><label>" + emailString + "</label></div></div>";
						$("#person_container_details").append(template);
					}
				}
				
				if(j < contactPersonIds.length-1){
					//append divider
					$("#person_container_details").append("<div class='row divider-horizontal persondivider'></div>");
				}
			}
		} 	// end else
		
	
	//load date and comment
	$("#label_dateofdelivery_details").text(inc.date);
	$("#label_description_details").text(inc.comment);
	
	//load last editor and updateTimeStamp
	$("#label_lastEditor_details").text(loadContactPerson(inc.lastEditorId));
	$("#label_updateTimestamp_details").text(inc.updateTimestamp);
	
	
	var articles = inc.incomingArticleDTOs;
	// sort articles according to their articleNr
	articles.sort( function(a, b) { 
		return (a.articleNr - b.articleNr);
	} );
	
	//load articles
	var articleString = "";
	for(var i=0; i < inc.incomingArticleDTOs.length; i++){
		articleString = articles[i].articleDTO.description;
		createAndAppendArticleTemplate("Beschreibung", articleString);
		
		articleString = articles[i].numberpu;
		createAndAppendArticleTemplate("Anzahl der VE", articleString);
		
		articleString = articles[i].articleDTO.packagingUnit;
		createAndAppendArticleTemplate("Art der VE", articleString);
		
		articleString = articles[i].articleDTO.weightpu + " kg";
		createAndAppendArticleTemplate("Einzelgewicht der VE", articleString);
		
		var weightPU = parseFloat(articles[i].articleDTO.weightpu);
		var sum = Math.round( (weightPU * articles[i].numberpu) *100)/100;
		createAndAppendArticleTemplate("Gesamtgewicht", sum + " kg");
		
		articleString = articles[i].articleDTO.mdd;
		createAndAppendArticleTemplate("Mindesthaltbarkeitsdatum", articleString);
		
		articleString = articles[i].articleDTO.pricepu + " €";
		createAndAppendArticleTemplate("Einzelpreis der VE", articleString);
		
		var pricepu = parseFloat(articles[i].articleDTO.pricepu);
		var sum = Math.round( (pricepu * articles[i].numberpu) *100)/100;
		createAndAppendArticleTemplate("Gesamtpreis", sum + " €");
		
		if(i < inc.incomingArticleDTOs.length-1){
			//append divider
			$("#article_container_details").append("<div class='row divider-horizontal persondivider'></div>");
		}
	}
	
	// show modal
	$('#details').modal('show');
	
});

// button distributionReport
$('#btn_distributionReport').click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Kein Wareneingang auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
	window.location.href = "../rest/secure/reports/articles/generateDistributionReportByIncomingDeliveryId/"+id;
	
});

function createAndAppendArticleTemplate(name, value){
	var template = "<div class='row details'><div class='col-md-6'><label>"+ name +"</label></div><div class='col-md-6'><label>" + value + "</label></div></div>";
	$("#article_container_details").append(template);
}

// search filter
$(document).ready(function() {
	(function($) {
		$('#filter').keyup(function() {

			var rex = new RegExp($(this).val(), 'i');
			$('.searchable tr').hide();
			$('.searchable tr').filter(function() {
				return rex.test($(this).text());
			}).show();
		})
	}(jQuery));
});

//set loadArchivedEntries variable to 0 (default)
var loadArchivedEntries = 0;

//check if archive checkbox is checked
$("#cbx_archive").on('change', function(){
	if($("#cbx_archive").prop('checked')){
		//load all archived entries
		loadArchivedEntries = 1;
		$('#incomingDeliveryTableBody').empty();
		loadTableContent(loadArchivedEntries);
	}
	else{
		//load all non archived entries
		loadArchivedEntries = 0;
		$('#incomingDeliveryTableBody').empty();
		loadTableContent(loadArchivedEntries);
	}
});

//set entry archived or non archived depending on button value
$("#btn_archive").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Kein Wareneingang auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
	if($(this).val() == "archive"){
		//set entry archived
		$.ajax({
			type : "POST",
			async : false,
			url : "../rest/secure/incomingDelivery/setArchivedState/"+ id +"/1"
		}).done(function(data) {
			$('#incomingDeliveryTableBody').empty();
			loadTableContent(loadArchivedEntries);
		});
	}
	else if($(this).val() == "dearchive"){
		//set entry non archived
		$.ajax({
			type : "POST",
			async : false,
			url : "../rest/secure/incomingDelivery/setArchivedState/"+ id +"/0"
		}).done(function(data) {
			$('#incomingDeliveryTableBody').empty();
			loadTableContent(loadArchivedEntries);
		});
	}
});

// hide new, edit and delete buttons
$('#btn_new').hide();
$(".suchfilter").css("margin-left", "0px");
$('#btn_edit').hide();
$('#btn_deleteModal').hide();

// disable details, distribution and archive buttons
$('#btn_details').prop('disabled', true);
$('#btn_distributionReport').prop('disabled', true);
$('#btn_archive').prop('disabled', true);
$('#cbx_archive').prop('checked', false);

// get current user rights
$(document).ready(function() {
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getCurrentUser"
	}).done(function(data) {
		currentUser = data;	// already JSON
		currentUserRights = currentUser.permission;

		// only when user has admin rights
		if (currentUserRights != "Read" && currentUserRights != "") {
			$("#btn_new").show();
			$(".suchfilter").css("margin-left", "5px");

			$('#btn_edit').show();
			$('#btn_deleteModal').show();

			$('#btn_edit').prop('disabled', true);
			$('#btn_deleteModal').prop('disabled', true);
		}
	});
});


//this function is used to get the selected row
//the function is called when a button is pressed and the selected entry has to be determined
function getSelectedRow(){
	
	// find selected tr in the table and map it to the variable
	var currentRow = $('#TableHead').find('tr.highlight').first().children("td").map(function() {
		return $(this).text();
	}).get();
	
	return currentRow;
}


var isBooked;
$('#TableHead').on('click','tbody tr', function(event) {
//	tableData = $(this).children("td").map(function() {
//		return $(this).text();
//	}).get();

	$(this).addClass('highlight').siblings().removeClass('highlight');
	
	// only when user has admin rights
	if (currentUserRights != "Read" && currentUserRights != "") {
		//deleteModal disabled=false only if not booked 
		if($(this).closest("tr").hasClass("booked-entry") == true){
			$('#btn_edit').prop('disabled', false);
			$('#btn_deleteModal').prop('disabled', true);
			isBooked = true;
		}
		else{
			$('#btn_edit').prop('disabled', false);
			$('#btn_deleteModal').prop('disabled', false);
			isBooked = false;
		}
	} 
	else {
		$('#btn_edit').prop('disabled', true);
		$('#btn_deleteModal').prop('disabled', true);
	}
	
	// enable details button and distribution button on click onto an table entry
	$('#btn_details').prop('disabled', false);
	$('#btn_distributionReport').prop('disabled', false);
	
	//check if clicked table row entry is archived
	if($(this).closest("tr").hasClass("checked") == true){
		$('#btn_archive').html('<span class="glyphicon glyphicon-folder-close"></span> Abschluss aufheben');
		$('#btn_archive').val("dearchive");
		$('#btn_archive').prop('disabled', false);
	}
	else{
		$('#btn_archive').html('<span class="glyphicon glyphicon-folder-close"></span> Abschließen');
		$('#btn_archive').val("archive");
		$('#btn_archive').prop('disabled', false);
	}
});

/**
 * Call the delete modal for the selected incoming delivery
 */
$("#btn_deleteModal").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Kein Wareneingang auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
	//get incoming delivery
	var inc;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/incomingDelivery/getById/" + id
	}).done(function(data) {
			inc = data;	// already JSON
	});
				
	//get organisation by id
	var org;
	var organisationString;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getOrganisationById/" + inc.organisationId
	}).done(function(data) {
		org = data;	// already JSON
		organisationString = org.name + ", " + org.zip + " " + org.city + ", " + org.country
	});
					
	//get articles
	var articleString = "";
	var article = inc.incomingArticleDTOs;
	for(var i=0; i < inc.incomingArticleDTOs.length; i++){
		articleString = articleString + article[i].articleDTO.description;
				
		if(i < inc.incomingArticleDTOs.length - 1){
			articleString = articleString + ", ";
		}
	}
	
	$("#label_deliverer_delete").text(organisationString); 
	$("#label_article_delete").text(articleString);
	
	$('#deleteModal').modal('show');
});

/**
 * call the delete url for the incoming delivery
 */
$("#btn_deleteIncomingDelivery").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Kein Wareneingang auswählt!", 2500);
		return;
	}
	
	 var id = rowData[0];
	 
	 $.ajax({
		 type : "POST",
		 url : "../rest/secure/incomingDelivery/deleteById/" + id
	 }).done(function(data) {
		 $('#incomingDeliveryTableBody').empty();
		 $('#deleteModal').modal('hide');
		 
		 if (data.success == true)
			{
				showAlertElement(1, data.message, 5000);
			}
		 else
			{
				showAlertElement(2, data.message, 5000);
			}
		 
		 loadTableContent(loadArchivedEntries);
	 });
});
