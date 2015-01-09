//TODO Get all incoming deliveries and load into table
function loadTableContent(){
			$.ajax({
				type : "POST",
				url : "../rest/secure/category/getAllCategories"
			}).done(
					function(data) {
						var c = eval(data);

						for ( var e in c) {
							var tableRow = "<tr>" + "<td>" + "1"
									+ "</td>" + "<td>" + "Nannerl" + ", " + "<br/>" + "Österreich" + "<br/>" + "5020 Salzburg" 
									+ "</td>" + "<td>" + "05.01.2014"
									+ "</td>" + "<td>" + "Beschreibung"
									+ "</td>" + "<td>" + "Äpfel, Birnen, Kekse, Sauce"
									+ "</td>" + "</tr>";

							$("#incomingDeliveryTableBody").append(tableRow);
						}
					});
};

//Get all incoming deliveries and load into table
$(document).ready(loadTableContent());

//switch to new incoming deliveries tab
$("#btn_new").click(function() {
	location.href="warenverwaltung_neuerwareneingang.html";
	return false;
});

//switch to edit incoming deliveries tab
$("#btn_edit").click(function() {
	location.href="warenverwaltung_neuerwareneingang.html";
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

//TODO load details modal
$("#btn_details").click(function() {
	//remove container
	$(".details").remove();
	$(".persondivider").remove();
	
	var id = tableData[0];
	
	$.ajax({
		type : "POST",
		url : "../rest/secure/organisation/getOrganisationById/" + "33"
	}).done(function(data) {
		
		var org = eval(data);
		
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
				
				//append divider
				$("#person_container_details").append("<div class='row divider-horizontal persondivider'></div>");
			}
		} 	// end else
		
	});
	
	
	//Lieferdatum und Beschreibung
	$("#label_dateofdelivery_details").text("Lieferdatum");
	$("#label_description_details").text("Beschreibung");
	
	//Letzter Bearbeiter und Aktualiserungsdatum von Wareneingangseintrag
	$("#label_lastEditor_details").text("Letzter Bearbeiter");
	$("#label_updateTimestamp_details").text("Aktualisierungsdatum");
	
	//Ware
	$("#label_article_details").text("Ware");
});

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
});

/**
 * TODO call the delete modal for the selected incoming delivery
 */
$("#btn_deleteModal").click(function() {

});


/**
 * TODO call the delete url for the category
 */
$("#btn_deleteCategory").click(function() {
	 var id = tableData[0];
	 
	 $.ajax({
		 type : "POST",
		 url : "../rest/secure/category/deleteCategoryById/" + id
	 }).done(function(data) {
		 $('#categoryTableBody').empty();
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
