var phoneCount = 0;
var phoneelement_template = "";

var emailCount = 0;
var emailelement_template = "";

var currentUser = "";
var currentUserRights = "";

function loadTableContent() {
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getAll"
	}).done(
			function(data) {
				var p = eval(data);
				
				for (var e in p) {
					var emailString = "";
					var phoneString = "";
					var typeString = "";

					var types = p[e].types;
					var emails = p[e].emails;
					var phoneNumbers = p[e].telephones;
					
					for (var i = 0; i < emails.length; i++) {
						emailString = emailString + emails[i].mail;
						if (i < emails.length - 1) {
							emailString = emailString + ", ";
						}
					}
					for (var j = 0; j < phoneNumbers.length; j++) {
						phoneString = phoneString + phoneNumbers[j].telephone;
						if (j < phoneNumbers.length - 1) {
							phoneString = phoneString + ", ";
						}
					}
					for (var k = 0; k < types.length; k++) {
						typeString = typeString + types[k];
						if (k < types.length - 1) {
							typeString = typeString + ", ";
						}
					}
					
					var tableRow = "<tr>" +
							"<td>" + p[e].personId + "</td>" +
							"<td>" + p[e].salutation + "</td>" +
							"<td>" + p[e].title + "</td>" +
							"<td>" + p[e].firstName + "</td>" +
							"<td>" + p[e].lastName + "</td>" +
							"<td>" + p[e].address + ", " + p[e].zip + " "
							+ p[e].city + ", " + p[e].country + "</td>" +
							"<td>" + emailString + "</td>" +
							"<td>" + phoneString + "</td>" +
							"<td>" + p[e].updateTimestamp + "</td>" +
							"<td>" + p[e].permission + "</td>" +
							"<td>" + typeString + "</td>" +
							"<td>" + p[e].lastEditor + "</td>" +
							"</tr>";
					
					$("#personTableBody").append(tableRow);
				}
			});
};

$("#select_loginEmail").focus(function() {
	$("#select_loginEmail").find('option')
    .remove()
    .end();
	$(".tbx_mailadress").each(function() {
		var emailValue = $(this).val();
		if(emailValue != ""){
			$("#select_loginEmail").append($("<option></option>")
			         .text(emailValue));
		}
	});
});

//Modal new
$("#btn_new").click(function() {
	$("#new").find('input')
    .val("")
    .end();
	
	$("#select_loginEmail").find('option')
    .remove()
    .end();
	
	//Set for DB notification of new User
	$("#tbx_id").val(0);
	$("#modal_title_text").text("Neue Person");
	
	//hide reset password button
	$("#btn_resetpasswordModal").hide();
	
	//hide admin type option, if user is no admin
	if(currentUserRights != "Admin"){
		$("#option_admin").remove();
	}
	
	//default select option is first option
	$("#select_permission").prop("selectedIndex", 0);
	
	//remove all phonenumber divs
	$(".btn_removephonenumber").closest('div[class^="phone_element"]').remove();
	phoneCount = 0;
	
	//remove all email divs
	$(".btn_removeemail").closest('div[class^="email_element"]').remove();
	emailCount = 0;
	
	//uncheck type checkboxes
	$('#cbx_mitarbeiter').prop('checked', false);
	$('#cbx_mitglied').prop('checked', false);
	$('#cbx_gast').prop('checked', false);
	$('#cbx_unterstuetzer').prop('checked', false);
});

// Modal Neu anlegen -> speichern
$("#btn_saveperson").click(function() {
	var newperson = new Object();

	newperson.personId = $("#tbx_id").val();
	newperson.salutation = $("#tbx_salutation").val();
	newperson.title = $("#tbx_title").val();
	newperson.firstName = $("#tbx_firstName").val();
	newperson.lastName = $("#tbx_lastName").val();
	//Not used yet
	newperson.comment = "";
	//Set by server
	newperson.updateTimestamp = "";
	newperson.active = 1;

	newperson.address = $("#tbx_address").val();
	newperson.city = $("#tbx_city").val();
	newperson.zip = $("#tbx_zip").val();
	newperson.country = $("#tbx_country").val();

	newperson.loginEmail = $("#select_loginEmail").val();
	newperson.permission = $("#select_permission").val();
	var typesArray = [];
	
	//checking if type checkboxes checked
	if($('#cbx_mitarbeiter').prop('checked')){
		typesArray.push("Mitarbeiter");
	}
	if($('#cbx_mitglied').prop('checked')){
		typesArray.push("Mitglied");
	}
	if($('#cbx_gast').prop('checked')){
		typesArray.push("Gast");
	}
	if($('#cbx_unterstuetzer').prop('checked')){
		typesArray.push("Unterstützer");
	}
	
	newperson.types = typesArray;

	newperson.emails = [];
	$(".email_element").each(function (){
		newperson.emails.push({"mail":$(this).find(".tbx_mailadress").val(), "type":$(this).find(".select_email").val()});

	});
	newperson.telephones = [];
	$(".phone_element").each(function (){
		newperson.telephones.push({"telephone":$(this).find(".tbx_phoneNumber").val(), "type":$(this).find(".select_phoneNumber").val()});
	});
	
	var persondata = JSON.stringify(newperson);
	$.ajax({
		headers : {
			'Accept' : 'application/json',
			'Content-Type' : 'application/json'
		},
		type : "POST",
		url : "../rest/secure/person/setPerson",
		contentType: "application/json; charset=utf-8",
	    dataType: "json",
		data : JSON.stringify(newperson)
	}).done(function(data) {
		if (data) {
			$('#personTableBody').empty();
			$('#new').modal('hide');
			
			if (data.success == true)
			{
				showAlertElement(1, data.message, 5000);
			}
			else
			{
				showAlertElement(2, data.message, 5000);
			}
			
			loadTableContent();
		} else {
			alert("Verbindungsproblem mit dem Server");
		}
	});
	return false;
});

// Get all Persons and load into table
$(document).ready(loadTableContent());

// Get one person and load it to modal
$("#btn_edit").click(function() {
	$("#modal_title_text").text("Bearbeite Person");
	
	//remove all phonenumber divs
	$(".btn_removephonenumber").closest('div[class^="phone_element"]').remove();
	phoneCount = 0;
	
	//remove all email divs
	$(".btn_removeemail").closest('div[class^="email_element"]').remove();
	emailCount = 0;
	
	//show reset password button
	$("#btn_resetpasswordModal").show();
	
	//hide admin type option, if user is no admin
	if(currentUserRights != "Admin"){
		$("#option_admin").remove();
	}
	
	var id = tableData[0];

	// Get person with id "id"
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getPersonById/" + id
	}).done(function(data) {

		var p = eval(data);

		//load data to modal
		$("#tbx_id").val(p.personId);
		$("#tbx_salutation").val(p.salutation);
		$("#tbx_title").val(p.title);
		$("#tbx_firstName").val(p.firstName);
		$("#tbx_lastName").val(p.lastName);
		$("#tbx_address").val(p.address);
		$("#tbx_zip").val(p.zip);
		$("#tbx_city").val(p.city);
		$("#tbx_country").val(p.country);

		//load phoneNumber divs
		var newElement;
		var loginEmail_template;
		var help;
		var help1;
		$("#select_loginEmail").find('option')
	    .remove()
	    .end();
		for (var i = 0; i<p.telephones.length; i++) {
			phoneelement_template = "<div class='row'> <div class='form-group'> <div class='col-sm-5'> <input type='text' id='tbx_phoneNumber" 
				+ phoneCount + "' class='form-control tbx_phoneNumber' placeholder='Telefonnr.'> </div> <div class='col-sm-4'>" +
				"<select class='form-control select_phoneNumber' id='select_phoneNumber"+ phoneCount +"'> <option>privat</option> <option>gesch&auml;ftlich</option> </select>" +
				"</div> <div class='col-sm-3'> <button type='button' class='btn btn-danger btn_removephonenumber' id='btn_delete' >L&ouml;schen</button> </div> </div> </div>";
				
				newElement = $(
					"<div/>",
					{
						id : "phone_element" + phoneCount++,
						"class" : "phone_element"
					}).append(phoneelement_template);
					$("#phone_container").append(newElement);
			
			help = "#tbx_phoneNumber" + (phoneCount-1);
			$(help).val(p.telephones[i].telephone);
			
			help = p.telephones[i].type; 
			help1 = "select#select_phoneNumber" + (phoneCount-1) + " option";
			$(help1).each(function() { 
				this.selected = (this.text == help);
			});
		}
		
		//load email divs
		for (var i = 0; i<p.emails.length; i++) {
			emailelement_template = "<div class='row'> <div class='form-group'> <div class='col-sm-5'> <input type='text' id='tbx_email" + emailCount + "' " +
				"class='form-control tbx_mailadress' placeholder='Email'> </div> <div class='col-sm-4'> <select class='form-control select_email' id='select_email"+ emailCount +"'>" +
				"<option>privat</option> <option>gesch&auml;ftlich</option> </select> </div> <div class='col-sm-3'><button type='button' class='btn btn-danger btn_removeemail'" +
				"id='btn_delete' >L&ouml;schen</button> </div> </div> </div>";
			
				newElement = $("<div/>", {
					id : "email_element" + emailCount++,
					"class" : "email_element"
				}).append(emailelement_template);
				$("#email_container").append(newElement);
				
			help = "#tbx_email" + (emailCount-1);
			$(help).val(p.emails[i].mail);
			
			help = p.emails[i].type;	//test
			help1 = "select#select_email" + (emailCount-1) + " option";
			$(help1).each(function() { 
				this.selected = (this.text == help);
			});
			
			//add emails to loginEmail select
			loginEmail_template = "<option>" + p.emails[i].mail + "</option>";
			$("#select_loginEmail").append(loginEmail_template);
		}
		
		//load selects
		$("select#select_loginEmail option").each(function() { 
			this.selected = (this.text == p.loginEmail);
		});
		
		$("select#select_permission option").each(function() { 
			this.selected = (this.text == p.permission);
		});
		
		$("select#select_types option").each(function() { 
			this.selected = (this.text == p.types);
		});
		
		//load type checkboxes
		$('#cbx_mitarbeiter').prop('checked', false);
		$('#cbx_mitglied').prop('checked', false);
		$('#cbx_gast').prop('checked', false);
		$('#cbx_unterstuetzer').prop('checked', false);
		
		for (var i = 0; i<p.types.length; i++) {
			if(p.types[i] == "Mitarbeiter"){
				$('#cbx_mitarbeiter').prop('checked', true);
			}
			if(p.types[i] == "Mitglied"){
				$('#cbx_mitglied').prop('checked', true);
			}
			if(p.types[i] == "Gast"){
				$('#cbx_gast').prop('checked', true);
			}
			if(p.types[i] == "Unterstützer"){
				$('#cbx_unterstuetzer').prop('checked', true);
			}
		}
	});
});

//add phonenumber div
$(document).ready(function() {
	$("#btn_addphonenumber").click(function() {
			phoneelement_template = "<div class='row'> <div class='form-group'> <div class='col-sm-5'> <input type='text' id='tbx_phoneNumber" 
									+ phoneCount + "' class='form-control tbx_phoneNumber' placeholder='Telefonnr.'> </div> <div class='col-sm-4'>" +
									"<select class='form-control select_phoneNumber' id='select_phoneNumber"+ phoneCount +"'> <option>privat</option> <option>gesch&auml;ftlich</option> </select>" +
									"</div> <div class='col-sm-3'> <button type='button' class='btn btn-danger btn_removephonenumber' id='btn_delete' >L&ouml;schen</button> </div> </div> </div>";
									
									var newElement = $(
										"<div/>",
										{
											id : "phone_element" + phoneCount++,
											"class" : "phone_element"
										}).append(phoneelement_template);
										$("#phone_container").append(newElement);
	});
});

//remove phonenumber div
$("body").on('click', '.btn_removephonenumber', function() {
	$(this).closest('div[class^="phone_element"]').remove();
	phoneCount--;
});

//add email div
$(document).ready(function() {
	$("#btn_addemail").click(function() {
		emailelement_template = "<div class='row'> <div class='form-group'> <div class='col-sm-5'> <input type='text' id='tbx_email" + emailCount + "' " +
			"class='form-control tbx_mailadress' placeholder='Email'> </div> <div class='col-sm-4'> <select class='form-control select_email' id='select_email"+ emailCount +"'>" +
			"<option>privat</option> <option>gesch&auml;ftlich</option> </select> </div> <div class='col-sm-3'><button type='button' class='btn btn-danger btn_removeemail'" +
			"id='btn_delete' >L&ouml;schen</button> </div> </div> </div>";
		
		var newElement = $("<div/>", {
			id : "email_element" + emailCount++,
			"class" : "email_element"
		}).append(emailelement_template);
		$("#email_container").append(newElement);
		
	});
});

//remove email div
$("body").on('click', '.btn_removeemail', function() {
	$(this).closest('div[class^="email_element"]').remove();
	emailCount--;
});

//reset password
$("#btn_resetpassword").click(function() {
	var id = tableData[0];
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/resetPasswordForId/" + id
	}).done(function(data) {
		$('#resetpasswordModal').modal('hide');
	});
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

//typfilter
$(document).ready(function() {
	$('#mitarbeiter_cbx').prop('checked', true);
	$('#unterstuetzer_cbx').prop('checked', true);
	$('#mitglieder_cbx').prop('checked', true);
	$('#gaeste_cbx').prop('checked', true);

	(function($) {
		$('#mitarbeiter_cbx').on('change', function() {
			if (this.checked) {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "Mitarbeiter"
				}).show();
			} else {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "Mitarbeiter"
				}).hide();
			}
		});
		$('#unterstuetzer_cbx').on('change', function() {
			if (this.checked) {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "Unterstützer"
				}).show();
			} else {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "Unterstützer"
				}).hide();
			}
		});
		$('#mitglieder_cbx').on('change', function() {
			if (this.checked) {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "Mitglied"
				}).show();
			} else {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "Mitglied"
				}).hide();
			}
		});
		$('#gaeste_cbx').on('change', function() {
			if (this.checked) {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "Gast"
				}).show();
			} else {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "Gast"
				}).hide();
			}
		});
	}(jQuery));
});

//disable new, edit and delete buttons
$('#btn_new').hide();
$(".suchfilter").css("margin-left", "0px");
$('#btn_edit').hide();
$('#btn_deleteModal').hide();

//get current user rights
$(document).ready(function() {
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getCurrentUser"
	}).done(function(data) {
		currentUser = eval(data);
		currentUserRights = currentUser.permission;
		
		//only when user has admin rights
		if(currentUserRights == "Admin" && currentUserRights != ""){
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
$('#personen').on('click', 'tbody tr', function(event) {
	tableData = $(this).children("td").map(function() {
		return $(this).text();
	}).get();

	$(this).addClass('highlight').siblings().removeClass('highlight');
	
		//only when user has admin rights
		if(currentUserRights == "Admin" && currentUserRights != "" && currentUser.personId != tableData[0]){
			if(currentUserRights == "Admin"){
				$('#btn_edit').prop('disabled', false);
				$('#btn_deleteModal').prop('disabled', false);
			}
			else{
				if(tableData[9] != "Admin"){
					$('#btn_edit').prop('disabled', false);
					$('#btn_deleteModal').prop('disabled', false);
				}
			}
		}
		else{
			$('#btn_edit').prop('disabled', true);
			$('#btn_deleteModal').prop('disabled', true);
		}
});

//remove table row modal
$("#btn_deleteModal").click(function() {
	$("#label_id").text(tableData[0]);
	$("#label_salutation").text(tableData[1]);
	$("#label_title").text(tableData[2]);
	$("#label_firstName").text(tableData[3]);
	$("#label_lastName").text(tableData[4]);
});

$("#btn_deletePerson").click(function() {
	var id = tableData[0];
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/deletePersonById/" + id
	}).done(function(data) {
		$('#personTableBody').empty();
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
