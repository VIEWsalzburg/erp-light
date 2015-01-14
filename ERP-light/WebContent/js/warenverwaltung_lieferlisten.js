//Get all delivery lists and load into table
function loadTableContent(){
			$.ajax({
				type : "POST",
				url : "../rest/secure/deliveryList/getAll"
			}).done(
					function(data) {
						var list = eval(data);
						var receiverString;
						
						for (var e in list) {
							
							receiverString = "";
							for(var i=0; i < list[e].outgoingDeliverieDTOs.length; i++){
								
								//get organisation by id
								var org;
								$.ajax({
									type : "POST",
									async : false,
									url : "../rest/secure/organisation/getOrganisationById/" + list[e].outgoingDeliverieDTOs[i].organisationId
								}).done(function(data) {
									org = eval(data);
									
									receiverString = receiverString + org.name;
									if(i < list[e].outgoingDeliverieDTOs.length - 1){
										receiverString = receiverString + ", ";
									}
								});
							}
						
							var tableRow = "<tr>" + "<td>" + list[e].deliveryListId
									+ "</td>" + "<td>" + list[e].name
									+ "</td>" + "<td>" + list[e].date
									+ "</td>" + "<td>" + receiverString
									+ "</td>" + "<td>" + list[e].driver
									+ "</td>" + "<td>" + list[e].passenger
									+ "</td>" + "</tr>";

							$("#deliveryListTableBody").append(tableRow);
						}
			});
};

//Get all delivery lists and load into table
$(document).ready(loadTableContent());

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
	//switch to edit delivery list with GET parameter mode=edit and id=...
	location.href="warenverwaltung_neuelieferliste.html?mode=edit&id=" + tableData[0];
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
				contactPerson = eval(data);
				nameString = contactPerson.lastName + " " + contactPerson.firstName;
			});
	return nameString;
};

//load details modal
$("#btn_details").click(function() {
	//remove container
	$(".details").remove();
	$(".persondivider").remove();
	
	var id = tableData[0];
	
	//get delivery list by id
	var list;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/deliveryList/getById/" + id
	}).done(function(data) {
		list = eval(data);
	});
	
	$("#label_description_details").text(list.name);
	$("#label_dateofdeliverylist_details").text(list.date);
		
	for (var i = 0; i < list.outgoingDeliverieDTOs.length; i++) {
		//get organisation by id
		var org;
		$.ajax({
			type : "POST",
			async : false,
			url : "../rest/secure/organisation/getOrganisationById/" + list.outgoingDeliverieDTOs[i].organisationId
		}).done(function(data) {
			org = eval(data);
		
			var template = "<div class='row details'><div class='col-md-6'><label>Name</label></div><div class='col-md-6'><label>" + org.name + "</label></div></div>";
			$("#organisation_container_details").append(template);
			
			var template = "<div class='row details'><div class='col-md-6'><label>Adresse</label></div><div class='col-md-6'><label>" + org.address + "</label></div></div>";
			$("#organisation_container_details").append(template);
		
			var template = "<div class='row details'><div class='col-md-6'><label>Postleitzahl</label></div><div class='col-md-6'><label>" + org.zip + "</label></div></div>";
			$("#organisation_container_details").append(template);
			
			var template = "<div class='row details'><div class='col-md-6'><label>Stadt</label></div><div class='col-md-6'><label>" + org.city + "</label></div></div>";
			$("#organisation_container_details").append(template);
			
			var template = "<div class='row details'><div class='col-md-6'><label>Land</label></div><div class='col-md-6'><label>" + org.country + "</label></div></div>";
			$("#organisation_container_details").append(template);
		
			if(org.comment == ""){
				var template = "<div class='row details'><div class='col-md-6'><label>Bemerkung</label></div><div class='col-md-6'><label>-</label></div></div>";
				$("#organisation_container_details").append(template);
			}
			else{
				var template = "<div class='row details'><div class='col-md-6'><label>Bemerkung</label></div><div class='col-md-6'><label>" + org.comment + "</label></div></div>";
				$("#organisation_container_details").append(template);
			}
		});
		
		if(i < list.outgoingDeliverieDTOs.length-1){
			//append divider
			$("#organisation_container_details").append("<div class='row divider-horizontal persondivider'></div>");
		}
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
});

//search filter
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

// disable new, edit and delete buttons
$('#btn_new').hide();
$(".suchfilter").css("margin-left", "0px");
$('#btn_edit').hide();
$('#btn_deleteModal').hide();

// get current user rights
$(document).ready(function() {
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getCurrentUser"
	}).done(function(data) {
		currentUser = eval(data);
		currentUserRights = currentUser.permission;

		// only when user has admin rights
		if (currentUserRights == "Admin" && currentUserRights != "") {
			$("#btn_new").show();
			$(".suchfilter").css("margin-left", "5px");

			$('#btn_edit').show();
			$('#btn_deleteModal').show();

			$('#btn_edit').prop('disabled', true);
			$('#btn_deleteModal').prop('disabled', true);
			$('#btn_details').prop('disabled', true);
		}
	});
});

var tableData;
$('#TableHead').on('click','tbody tr', function(event) {
			tableData = $(this).children("td").map(function() {
				return $(this).text();
			}).get();

			$(this).addClass('highlight').siblings().removeClass('highlight');

			// only when user has admin rights
			if (currentUserRights == "Admin" && currentUserRights != "") {
				$('#btn_edit').prop('disabled', false);
				$('#btn_deleteModal').prop('disabled', false);
			} 
			else {
				$('#btn_edit').prop('disabled', true);
				$('#btn_deleteModal').prop('disabled', true);
			}
			$('#btn_details').prop('disabled', false);
});

/**
 * Call the delete modal for the selected delivery list
 */
$("#btn_deleteModal").click(function() {
	var id = tableData[0];
	 
	//get delivery list by id
	var inc;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/deliveryList/getById/" + id
	}).done(function(data) {
			inc = eval(data);
	});
	
	$("#label_name_delete").text(inc.name); 
	$("#label_date_delete").text(inc.date);
});


/**
 * Call the delete url for the delivery list
 */
$("#btn_deleteDeliveryList").click(function() {
	var id = tableData[0];

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
		 
		 loadTableContent();
	 });
});
