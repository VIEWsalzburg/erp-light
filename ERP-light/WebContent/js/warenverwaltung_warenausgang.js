//append alert message to modal
var pwdError = "<div id='pwdErrorAlert'> <div class='col-sm-5'> <div class='alert alert-danger custom-alert' style='text-align: left;'>Leere Felder vorhanden!</div> </div>  </div>";
	$("#newAlertForm").append(pwdError);

//Get all outgoing deliveries and load into table
function loadTableContent(loadArchivedEntries){
	
	// show loading spinner
	showLoadingSpinner(true);
	
			// get all organisations
			var organisations;
			$.ajax({
				type : "POST",
				async : false,
				url : "../rest/secure/organisation/reducedData/getAllOrganisations"
			}).done(function(data) {
				organisations = data;	// already JSON
			});
	
			//check if only non archived entries should be loaded to table
			if(loadArchivedEntries == 1){
				
				var y = $( "#sel_year option:selected" ).text();
				console.log(y);
				//load all outgoing deliveries
				if(y == 'Alle'){			
					loadArchivedEntries = "";
				}
				//load outgoing deliveries from a specific year
				else{
					loadArchivedEntries = "ByYear/" + y;
				}
			}
			else{
				loadArchivedEntries = "Unarchived";
			}
			
			$.ajax({
				type : "POST",
				url : "../rest/secure/outgoingDelivery/getAll" + loadArchivedEntries
			}).done(function(data) {
						var out = data;	// already JSON

						for (var e in out) {
							
							//get organisation by id
							var org = null;
							
							for (i in organisations)
							{
								if (organisations[i].id == out[e].organisationId)
								{
									org = organisations[i];
								}
							}
							
							var articles = out[e].outgoingArticleDTOs;
							// sort articles according to their articleNr
							articles.sort( function(a, b) { 
								return (a.articleNr - b.articleNr);
							} );
							
							//get article names
							var articleString = "";
							for(var i=0; i < articles.length; i++){
								articleString = articleString + articles[i].numberpu + " " + 
									articles[i].articleDTO.packagingUnit + " " + articles[i].articleDTO.description;
								
								
								if(i < articles.length - 1){
									articleString = articleString + ", ";
								}
							}
							
							var bookedClass = "";
							if (out[e].booked > 0)
								bookedClass = "booked-entry";
							
							//check archived flag
							var archivedCheckboxState = "";
							if(out[e].archived > 0){
								archivedCheckboxState = "checked";
							}
							
							var tableRow = "<tr class='"+bookedClass + " " + archivedCheckboxState +"'>" + "<td class='hidden'>" + out[e].outgoingDeliveryId
									+ "</td>" + "<td>" + org.name + ", " + "<br/>" + org.zip + " " + org.city + ", " + "<br/>" + org.country 
									+ "</td>" + "<td>" + out[e].date
									+ "</td>" + "<td>" + articleString
									+ "</td>" + "<td>" + out[e].comment
									+ "</td>" + "<td>" + "<input type='checkbox' id='cbx_archived' disabled "+ archivedCheckboxState +">"
									+ "</td>" + "</tr>";

							$("#outgoingDeliveryTableBody").append(tableRow);
						}
						
						// hide loading spinner
						showLoadingSpinner(false);
						
						// update item count label
						updateItemCountLabel();
			});
};

//fill the select-option-values for archieved entries
function fillSelectArchive(){
	
	//add date to select-field for archived entries
	//get date
	var d = new Date();
	//get year
    var y = d.getFullYear();
    //first entries appears in 2014
    var start = 2014;    
    while(y >= start){
    	$('#sel_year').append($('<option>', {
    		value: y,
    		text: y--
    	}));
    }
}

//Get all outgoing deliveries and load into table
$(document).ready(loadTableContent(loadArchivedEntries),fillSelectArchive());

//init collapse
$(function () {
	$('#details .collapse').collapse();		// collapse all accordions of the details modal
});

//switch to new outgoing deliveries tab
$("#btn_new").click(function() {
	//switch to new incoming deliveries with GET parameter mode=new and id=-1
	location.href="warenverwaltung_neuedisposition.html?mode=new&id=-1";
	return false;
});

//switch to edit ougoing deliveries tab
$("#btn_edit").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Keinen Warenausgang auswählt!", 2500);
		return;
	}
	
	//switch to edit outgoing deliveries with GET parameter mode=edit and id=...
	location.href="warenverwaltung_neuedisposition.html?mode=edit&booked="+ isBooked +"&id=" + rowData[0];
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
	$('#person_container_details').empty();
	
	// remove old article panels from accordion
	$('#accordion_articles').empty();
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Keinen Warenausgang auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
	//get outgoing delivery by id
	var out;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/outgoingDelivery/getById/" + id
	}).done(function(data) {
		out = data;	// already JSON
	});
				
	//get organisation by id
	var org;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getOrganisationById/" + out.organisationId
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
	$("#label_dateofdelivery_details").text(out.date);
	$("#label_description_details").text(out.comment);
	
	//load last editor and updateTimeStamp
	$("#label_lastEditor_details").text(loadContactPerson(out.lastEditorId));
	$("#label_updateTimestamp_details").text(out.updateTimestamp);
	

	
	var articles = out.outgoingArticleDTOs;
	// sort articles according to their articleNr
	articles.sort( function(a, b) { 
		return (a.articleNr - b.articleNr);
	} );
	
	//load articles
	var articleString = "";
	for(var i=0; i < articles.length; i++){
		
		var headingString = articles[i].numberpu + "x "+articles[i].articleDTO.description + " (" + articles[i].articleDTO.packagingUnit + ")";
		
		var articleTemplate = "";
		
		articleString = articles[i].articleDTO.description;
		articleTemplate += createArticleTemplate("Beschreibung", articleString);
		
		articleString = articles[i].numberpu;
		articleTemplate += createArticleTemplate("Anzahl der VE", articleString);
		
		articleString = articles[i].articleDTO.packagingUnit;
		articleTemplate += createArticleTemplate("VE", articleString);
		
		articleString = articles[i].articleDTO.weightpu; + " kg";
		articleTemplate += createArticleTemplate("Einzelgewicht der VE", articleString);
		
		var weightPU = parseFloat(articles[i].articleDTO.weightpu);
		var sum = Math.round( (weightPU * articles[i].numberpu) *100)/100;
		articleTemplate += createArticleTemplate("Gesamtgewicht", sum + " kg");
		
		articleString = articles[i].articleDTO.mdd;
		articleTemplate += createArticleTemplate("Mindesthaltbarkeitsdatum", articleString);
		
		articleString = articles[i].articleDTO.pricepu + " €";
		articleTemplate += createArticleTemplate("Einzelpreis der VE", articleString);
		
		var pricepu = parseFloat(articles[i].articleDTO.pricepu);
		var sum = Math.round((pricepu * articles[i].numberpu)*100)/100;
		articleTemplate += createArticleTemplate("Gesamtpreis", sum + " €");
		
		
		//get deliverer of the article
		var org;
		$.ajax({
			type : "POST",
			async : false,
			url : "../rest/secure/organisation/getOrganisationById/" + articles[i].articleDTO.delivererId
		}).done(function(data) {
			org = data;	// already JSON
		});
		
		articleString = org.name;
		articleTemplate += createArticleTemplate("Lieferant", articleString);
		
		
		// insert into panel and append to accordion
		var accordionTemplate = "<div class='panel panel-default'>"+
									 "<div class='panel-heading' role='tab' id='heading_art_"+i+"'>"+
									 	"<h5 class='panel-title'>"+
									 		"<a data-toggle='collapse' data-parent='#accordion_articles' href='#collapse_art_"+i+"' aria-expanded='true' aria-controls='collapse_art_"+i+"'>"+
									 			headingString+
									 		"</a>"+
									 	"</h5>"+
									 "</div>"+
									 "<div id='collapse_art_"+i+"' class='panel-collapse collapse' role='tabpanel' aria-labelledby='heading_art_"+i+"'>"+
									 	"<div class='panel-body'>"+
									 		articleTemplate +
										"</div>"+
								    "</div>"+
								 "</div>";
		
		$('#accordion_articles').append(accordionTemplate);
	}
	
	// show modal
	$('#details').modal('show');
	
});

function createArticleTemplate(name, value){
	var template = "<div class='row details'><div class='col-md-6'><label>"+ name +"</label></div><div class='col-md-6'><label>" + value + "</label></div></div>";
	return template;
}

//function for updating the item count label right above the table
function updateItemCountLabel()
{
	var count = $('#outgoingDeliveryTableBody').children(':visible').length;
	
	$('#lbl_item_count').text(count+" Warenausgänge");
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
			
			updateItemCountLabel();
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
		$('#outgoingDeliveryTableBody').empty();
		loadTableContent(loadArchivedEntries);
	}
	else{
		//load all non archived entries
		loadArchivedEntries = 0;
		$('#outgoingDeliveryTableBody').empty();
		loadTableContent(loadArchivedEntries);
	}
});

//change-eventhandler for select-years for finished deliveries
$("#sel_year").on('change', function() {
	if($("#cbx_archive").prop('checked')){
		loadArchivedEntries = 1;
		$('#outgoingDeliveryTableBody').empty();
		loadTableContent(loadArchivedEntries);
	}	  
});

//set entry archived or non archived depending on button value
$("#btn_archive").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Keinen Warenausgang auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
	if($(this).val() == "archive"){
		//set entry archived
		$.ajax({
			type : "POST",
			async : false,
			url : "../rest/secure/outgoingDelivery/setArchivedState/"+ id +"/1"
		}).done(function(data) {
			$('#outgoingDeliveryTableBody').empty();
			loadTableContent(loadArchivedEntries);
		});
	}
	else if($(this).val() == "dearchive"){
		//set entry non archived
		$.ajax({
			type : "POST",
			async : false,
			url : "../rest/secure/outgoingDelivery/setArchivedState/"+ id +"/0"
		}).done(function(data) {
			$('#outgoingDeliveryTableBody').empty();
			loadTableContent(loadArchivedEntries);
		});
	}
});

// disable new, edit and delete buttons
$('#btn_new').hide();
$(".suchfilter").css("margin-left", "0px");
$('#btn_edit').hide();
$('#btn_deleteModal').hide();

$('#btn_details').prop('disabled', true);
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
			$('#btn_details').prop('disabled', false);
			
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
 * Call the delete modal for the selected outgoing delivery
 */
$("#btn_deleteModal").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Keinen Warenausgang auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
	//get incoming delivery
	var out;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/outgoingDelivery/getById/" + id
	}).done(function(data) {
			out = data;	// already JSON
	});
				
	//get organisation by id
	var org;
	var organisationString;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getOrganisationById/" + out.organisationId
	}).done(function(data) {
		org = data;	// already JSON
		organisationString = org.name + ", " + org.zip + " " + org.city + ", " + org.country
	});
					
	//get articles
	var articleString = "";
	var article = out.outgoingArticleDTOs;
	for(var i=0; i < out.outgoingArticleDTOs.length; i++){
		articleString = articleString + article[i].articleDTO.description;
				
		if(i < out.outgoingArticleDTOs.length - 1){
			articleString = articleString + ", ";
		}
	}
	
	$("#label_receiver_delete").text(organisationString); 
	$("#label_article_delete").text(articleString);
	
	$('#deleteModal').modal('show');
});


/**
 * Call the delete url for the outgoing delivery
 */
$("#btn_deleteOutgoingDelivery").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Keinen Warenausgang auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
	 $.ajax({
		 type : "POST",
		 url : "../rest/secure/outgoingDelivery/deleteById/" + id
	 }).done(function(data) {
		 $('#outgoingDeliveryTableBody').empty();
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

//export current view as CSV
$('#btn_exportCurrentView').click(function(){
	
	var tableData = [];
	
	var headerString = $('#TableHead .TableHead tr').children().map(
			function(){
				return $(this).text();
			}).get().join(';');
	tableData.push(headerString);
	
	// only visible rows
	// concat columns with separator ';' for each row and push it into tableData
	$('#outgoingDeliveryTableBody tr:visible').each(function(){
		var string = $(this).children().map(function(){
				var text = $(this).text();
				text = text.replace(/\;/g,',');
				text = text.replace(/(\r\n|\n|\r)/g,' ');
				return text;
			}).get().join(';');
		
		tableData.push(string);
	});
	
	// merge rows
	var csvString = tableData.join('\n');
	
	// start encoding and download
	// code by https://github.com/b4stien/js-csv-encoding
	var csvContent = csvString,
    	textEncoder = new CustomTextEncoder('windows-1252', {NONSTANDARD_allowLegacyEncoding: true}),
    	fileName = 'Warenausgang-Export.csv';

	// encode
	var csvContentEncoded = textEncoder.encode([csvContent]);
	// start download
	var blob = new Blob([csvContentEncoded], {type: 'text/csv;charset=windows-1252;'});
	saveAs(blob, fileName);
	
	// end download
	
});