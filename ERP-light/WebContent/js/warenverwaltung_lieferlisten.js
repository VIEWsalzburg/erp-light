//Get all delivery lists and load into table
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
		loadArchivedEntries = "";
	}
	else{
		loadArchivedEntries = "Unarchived";
	}
	
			$.ajax({
				type : "POST",
				url : "../rest/secure/deliveryList/getAll" + loadArchivedEntries
			}).done(
					function(data) {
						var deliverylists = data;	// already JSON
						
						// iterate over all lists
						for (var e in deliverylists) {
							
							var list = deliverylists[e];
							
							var delivererArray = [];
							var receiverArray = [];
							
							// sort outgoingDeliveryDTOs by deliveryNr
							list.outgoingDeliveryDTOs.sort(function(a, b){
								return a.deliveryNr - b.deliveryNr;
							});
							
							for(var i=0; i < list.outgoingDeliveryDTOs.length; i++){
								
								// get organisation by id
								var orgId = list.outgoingDeliveryDTOs[i].organisationId;
								var org;
								
								for (o in organisations)
								{
									if (organisations[o].id == orgId)
										org = organisations[o];
								}
								
								// get receivers 
								receiverArray.push(org.id);
								
								// iterate over outgoingArticleDTOS
								for (var indexArticle in list.outgoingDeliveryDTOs[i].outgoingArticleDTOs)
								{
									// iterate over all articles
									var article = list.outgoingDeliveryDTOs[i].outgoingArticleDTOs[indexArticle].articleDTO;
									delivererArray.push(article.delivererId);
								}
								
							}
							
							// remove duplicates from delivererArray
							var unique = [];
							delivererArray.forEach(function(item) {
								if (unique.indexOf(item) < 0)		// if element does not exist in unique array
									unique.push(item);
							});
							
							// create delivererString
							var delivererString = "";
							for (i in unique)
							{
								var orgId = unique[i];
								for (o in organisations)
								{
									if (organisations[o].id == orgId)
										delivererString = delivererString + organisations[o].name;
								}
								
								if (i < (unique.length - 1) )
									delivererString = delivererString + ", ";
							}
							
							// remove duplicates from receiverArray
							var unique = [];
							receiverArray.forEach(function(item) {
								if (unique.indexOf(item) < 0)		// if element does not exist in unique array
									unique.push(item);
							});
							
							// create receiverString
							var receiverString = "";
							for (i in unique)
							{
								var orgId = unique[i];
								for (o in organisations)
								{
									if (organisations[o].id == orgId)
										receiverString = receiverString + organisations[o].name;
								}
								
								if (i < (unique.length - 1) )
									receiverString = receiverString + ", ";
							}
							
							//create driverString
							var driverString = "";
							if(list.passenger == ""){
								driverString = list.driver;
							}
							else{
								driverString = list.driver + "," + "<br>" + list.passenger;
							}
							
							//check archived flag
							var archivedCheckboxState = "";
							if(list.archived > 0){
								archivedCheckboxState = "checked";
							}
							
							var tableRow = "<tr class='"+ archivedCheckboxState +"'>" + "<td class='hidden'>" + list.deliveryListId
									+ "</td>" + "<td>" + list.date
									+ "</td>" + "<td>" + delivererString
									+ "</td>" + "<td>" + receiverString
									+ "</td>" + "<td>" + driverString
									+ "</td>" + "<td>" + list.comment
									+ "</td>" + "<td>" + "<input type='checkbox' id='cbx_archived' disabled "+ archivedCheckboxState +">"
									+ "</td>" + "</tr>";

							$("#deliveryListTableBody").append(tableRow);
						}
						
						// hide loading spinner
						showLoadingSpinner(false);
						
						// update iteml count label
						updateItemCountLabel();
						
			});
};

//Get all delivery lists and load into table
$(document).ready(loadTableContent(loadArchivedEntries));

//init collapse
$(function () {
	$('#details .collapse').collapse();		// collapse all accordions of the details modal
});

//switch to new delivery list tab
$("#btn_new").click(function() {
	//switch to new delivery list with GET parameter mode=new and id=-1
	location.href="warenverwaltung_neuelieferliste.html?mode=new&id=-1";
	return false;
});

//switch to edit new delivery list tab
$("#btn_edit").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Keine Lieferliste auswählt!", 2500);
		return;
	}
	
	//switch to edit delivery list with GET parameter mode=edit and id=...
	location.href="warenverwaltung_neuelieferliste.html?mode=edit&id=" + rowData[0];
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
				contactPerson = data;	// alredy JSON
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
		showAlertElement(false, "Keine Lieferliste auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
	//get delivery list by id
	var list;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/deliveryList/getById/" + id
	}).done(function(data) {
		list = data;	// already JSON
	});
	
	$("#label_description_details").text(list.comment);
	$("#label_dateofdeliverylist_details").text(list.date);
	
	//load outgoingDeliveryDTOs
	
	// sort outgoingDeliveryDTOs by deliveryNr
	list.outgoingDeliveryDTOs.sort(function(a,b){
		return a.deliveryNr - b.deliveryNr;
	});
	
	for (var i = 0; i < list.outgoingDeliveryDTOs.length; i++) {
		var template = "<div class='panel-group details' id='accordion_deliverylist"+ i +"' role='tablist' aria-multiselectable='true'>" +
						"<div class='panel panel-default'><div class='panel-heading' role='tab' id='heading"+ i +"'><h4 class='panel-title'>" +
						"<a data-toggle='collapse' data-parent='#accordion_deliverylist"+ i +"' href='#collapse"+ i +"' aria-expanded='true' aria-controls='collapseOne'>" +
						"Lieferstation "+ (i+1) +"</a></h4></div><div id='collapse"+ i +"' class='panel-collapse collapse in' role='tabpanel' aria-labelledby='heading"+ i +"'>" +
						"<div class='panel-body'><div id='organisation_container_details"+ i +"'></div></div></div></div></div>";
		$("#accordion_container_details").append(template);
		
		//close accordion
		$("#collapse" + i).collapse("hide");
		
		// iterate over outgoingArticleDTOS
		var delivererArray = [];
		for (var indexArticle in list.outgoingDeliveryDTOs[i].outgoingArticleDTOs)
		{
			//push all deliverer ids to delivererArray
			var article = list.outgoingDeliveryDTOs[i].outgoingArticleDTOs[indexArticle].articleDTO;
			delivererArray.push(article.delivererId);
		}
		
		// remove duplicates from delivererArray
		var unique = [];
		delivererArray.forEach(function(item) {
			if (unique.indexOf(item) < 0)		// if element does not exist in unique array
				unique.push(item);
		});
		
		for (j in unique)
		{
			//get deliverer organisation by id
			var org;
			$.ajax({
				type : "POST",
				async : false,
				url : "../rest/secure/organisation/getOrganisationById/" + unique[j]
			}).done(function(data) {
				org = data;	// already JSON
			
				//deliverer
				var template = "<div class='row details' style='background-color: #F0F0F0;'><div class='col-md-6'><label>Lieferant "+ (parseInt(j) + 1) +"</label></div><div class='col-md-6'><label>" + org.name + "</label></div></div>";
				$("#organisation_container_details" + i).append(template);
				
				var template = "<div class='row details'><div class='col-md-6'><label>Adresse</label></div><div class='col-md-6'><label>" + org.address + "</label></div></div>";
				$("#organisation_container_details" + i).append(template);
			
				var template = "<div class='row details'><div class='col-md-6'><label>Postleitzahl</label></div><div class='col-md-6'><label>" + org.zip + "</label></div></div>";
				$("#organisation_container_details" + i).append(template);
				
				var template = "<div class='row details'><div class='col-md-6'><label>Stadt</label></div><div class='col-md-6'><label>" + org.city + "</label></div></div>";
				$("#organisation_container_details" + i).append(template);
				
				var template = "<div class='row details'><div class='col-md-6'><label>Land</label></div><div class='col-md-6'><label>" + org.country + "</label></div></div>";
				$("#organisation_container_details" + i).append(template);
			
				if(org.comment == ""){
					var template = "<div class='row details'><div class='col-md-6'><label>Bemerkung</label></div><div class='col-md-6'><label>-</label></div></div>";
					$("#organisation_container_details" + i).append(template);
				}
				else{
					var template = "<div class='row details'><div class='col-md-6'><label>Bemerkung</label></div><div class='col-md-6'><label>" + org.comment + "</label></div></div>";
					$("#organisation_container_details" + i).append(template);
				}
				
				//append divider
				$("#organisation_container_details" + i).append("<div class='row divider-horizontal persondivider'></div>");
			});
			
			// iterate over outgoingArticleDTOS
			for (var indexArticle in list.outgoingDeliveryDTOs[i].outgoingArticleDTOs)
			{
				var article = list.outgoingDeliveryDTOs[i].outgoingArticleDTOs[indexArticle].articleDTO;
				var article_details = list.outgoingDeliveryDTOs[i].outgoingArticleDTOs[indexArticle];
				
				//append only articles from same deliverer
				if(article.delivererId == org.id){
					var template = "<div class='row details'><div class='col-md-6'><label>Ware "+ (parseInt(indexArticle) + 1) +"</label></div><div class='col-md-6'><label>" + article.description + "</label></div></div>";
					$("#organisation_container_details" + i).append(template);
					
					var template = "<div class='row details'><div class='col-md-6'><label>Anzahl der VE</label></div><div class='col-md-6'><label>" + article_details.numberpu + "</label></div></div>";
					$("#organisation_container_details" + i).append(template);
					
					var template = "<div class='row details'><div class='col-md-6'><label>Art der VE</label></div><div class='col-md-6'><label>" + article.packagingUnit + "</label></div></div>";
					$("#organisation_container_details" + i).append(template);
					
					//append divider
					$("#organisation_container_details" + i).append("<div class='row divider-horizontal persondivider'></div>");
				}
			}
		}
		
		//get receiver organisation by id
		var org;
		$.ajax({
			type : "POST",
			async : false,
			url : "../rest/secure/organisation/getOrganisationById/" + list.outgoingDeliveryDTOs[i].organisationId
		}).done(function(data) {
			org = data;	// already JSON
		
			//receiver
			var template = "<div class='row details' style='background-color: #F0F0F0;'><div class='col-md-6'><label>Kunde</label></div><div class='col-md-6'><label>" + org.name + "</label></div></div>";
			$("#organisation_container_details" + i).append(template);
			
			var template = "<div class='row details'><div class='col-md-6'><label>Adresse</label></div><div class='col-md-6'><label>" + org.address + "</label></div></div>";
			$("#organisation_container_details" + i).append(template);
		
			var template = "<div class='row details'><div class='col-md-6'><label>Postleitzahl</label></div><div class='col-md-6'><label>" + org.zip + "</label></div></div>";
			$("#organisation_container_details" + i).append(template);
			
			var template = "<div class='row details'><div class='col-md-6'><label>Stadt</label></div><div class='col-md-6'><label>" + org.city + "</label></div></div>";
			$("#organisation_container_details" + i).append(template);
			
			var template = "<div class='row details'><div class='col-md-6'><label>Land</label></div><div class='col-md-6'><label>" + org.country + "</label></div></div>";
			$("#organisation_container_details" + i).append(template);
		
			if(org.comment == ""){
				var template = "<div class='row details'><div class='col-md-6'><label>Bemerkung</label></div><div class='col-md-6'><label>-</label></div></div>";
				$("#organisation_container_details" + i).append(template);
			}
			else{
				var template = "<div class='row details'><div class='col-md-6'><label>Bemerkung</label></div><div class='col-md-6'><label>" + org.comment + "</label></div></div>";
				$("#organisation_container_details" + i).append(template);
			}
		});
	}
	
	//load driver and codriver
	$("#label_driver_details").text(list.driver);
	
	if(list.passenger == ""){
		$("#label_codriver_details").text("-");
	}
	else{
		$("#label_codriver_details").text(list.passenger);
	}
	
	//load last editor and updateTimeStamp
	$("#label_lastEditor_details").text(loadContactPerson(list.lastEditorId));
	$("#label_updateTimestamp_details").text(list.updateTimestamp);
	
	
	// show modal
	$('#details').modal('show');
	
});

//function for updating the item count label right above the table
function updateItemCountLabel()
{
	var count = $('#deliveryListTableBody').children(':visible').length;
	
	$('#lbl_item_count').text(count+" Lieferlisten");
}

//search filter
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
		$('#deliveryListTableBody').empty();
		loadTableContent(loadArchivedEntries);
	}
	else{
		//load all non archived entries
		loadArchivedEntries = 0;
		$('#deliveryListTableBody').empty();
		loadTableContent(loadArchivedEntries);
	}
});

//set entry archived or non archived depending on button value
$("#btn_archive").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Keine Lieferliste auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
	if($(this).val() == "archive"){
		//set entry archived
		$.ajax({
			type : "POST",
			async : false,
			url : "../rest/secure/deliveryList/setArchivedState/"+ id +"/1"
		}).done(function(data) {
			$('#deliveryListTableBody').empty();
			loadTableContent(loadArchivedEntries);
		});
	}
	else if($(this).val() == "dearchive"){
		//set entry non archived
		$.ajax({
			type : "POST",
			async : false,
			url : "../rest/secure/deliveryList/setArchivedState/"+ id +"/0"
		}).done(function(data) {
			$('#deliveryListTableBody').empty();
			loadTableContent(loadArchivedEntries);
		});
	}
});

// disable new, edit and delete buttons
$('#btn_new').hide();
$(".suchfilter").css("margin-left", "0px");
$('#btn_edit').hide();
$('#btn_deleteModal').hide();
$('#btn_export').hide();

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
			$('#btn_export').show();

			$('#btn_edit').prop('disabled', true);
			$('#btn_deleteModal').prop('disabled', true);
			$('#btn_export').prop('disabled', true);
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


$('#TableHead').on('click','tbody tr', function(event) {
	var rowData = $(this).children("td").map(function() {
		return $(this).text();
	}).get();

	$(this).addClass('highlight').siblings().removeClass('highlight');

	// set link to word generation of the current selected deliveryList
	var hrefString = "../rest/secure/deliveryList/exportAsWord/" + rowData[0];
	$("#href_export").attr("href", hrefString);
	
	// only when user has admin rights
	if (currentUserRights == "Admin" || currentUserRights == "ReadWrite") {
		$('#btn_edit').prop('disabled', false);
		$('#btn_deleteModal').prop('disabled', false);
		$('#btn_export').prop('disabled', false);
	} 
	else {
		$('#btn_edit').prop('disabled', true);
		$('#btn_deleteModal').prop('disabled', true);
		$('#btn_export').prop('disabled', true);
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
 * Call the delete modal for the selected delivery list
 */
$("#btn_deleteModal").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Keine Lieferliste auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	 
	//get delivery list by id
	var inc;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/deliveryList/getById/" + id
	}).done(function(data) {
			inc = data;	// already JSON
	});
	
	$("#label_date_delete").text(inc.date);
	$("#label_name_delete").text(inc.comment); 
	
	// show modal
	$('#deleteModal').modal('show');
});


/**
 * Call the delete url for the delivery list
 */
$("#btn_deleteDeliveryList").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Keine Lieferliste auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];

	$.ajax({
		 type : "POST",
		 url : "../rest/secure/deliveryList/deleteById/" + id
	 }).done(function(data) {
		 $('#deliveryListTableBody').empty();
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
	$('#deliveryListTableBody tr:visible').each(function(){
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
    	fileName = 'Lieferlisten-Export.csv';

	// encode
	var csvContentEncoded = textEncoder.encode([csvContent]);
	// start download
	var blob = new Blob([csvContentEncoded], {type: 'text/csv;charset=windows-1252;'});
	saveAs(blob, fileName);
	
	// end download
	
});