var phoneCount = 0;
var phoneelement_template = "";

var emailCount = 0;
var emailelement_template = "";

// Load pageheader
$("#pageheader").load("../partials/header.html", function() {
	$("#adressverwaltung_nav").addClass("active");
});

//Modal new
$("#btn_new").click(function() {
	$("#modal_title_text").text("Neue Person");
	
	//remove all phonenumber divs
	$(".btn_removephonenumber").closest('div[class^="phone_element"]').remove();
	phoneCount = 0;
	
	//remove all email divs
	$(".btn_removeemail").closest('div[class^="email_element"]').remove();
	emailCount = 0;
});

// Modal Neu anlegen -> speichern
$("#btn_saveperson").click(function() {
	var newperson = new Object();
	newperson.personId = -1;
	newperson.salutation = $("#tbx_salutation").val();
	newperson.title = $("#tbx_title").val();
	newperson.firstName = $("#tbx_firstName").val();
	newperson.lastName = $("#tbx_lastName").val();
	newperson.comment = "";
	newperson.updateTimestamp = "";
	newperson.active = 1;

	newperson.address = $("#tbx_address").val();
	newperson.city = $("#tbx_city").val();
	newperson.zip = $("#tbx_zip").val();
	newperson.country = $("#tbx_country").val();

	newperson.loginEmail = $("#select_loginEmail").val();
	newperson.password = "";
	newperson.permission = $("#select_permission").val();
	var typesArray=[];
	typesArray.push($("#select_types").val());
	newperson.types = typesArray;

	newperson.emails = [];
	newperson.telephones = [];

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
			alert("Speichern erfolgreich! Hurra!");
		} else {
			alert("whoops something went wrong");
		}
	});
	return false;
});

// Get all Persons and load into table
$(document).ready(
		function() {
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
								emailString = emailString + emails[i];
								if (i < emails.length - 1) {
									emailString = emailString + ", ";
								}
							}
							for (var j = 0; j < phoneNumbers.length; j++) {
								phoneString = phoneString + phoneNumbers[j];
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
							
							var tableRow = "<tr>" + "<td>" + p[e].personId
									+ "</td>" + "<td>" + p[e].salutation
									+ "</td>" + "<td>" + p[e].title + "</td>"
									+ "<td>" + p[e].firstName + "</td>"
									+ "<td>" + p[e].lastName + "</td>" + "<td>"
									+ p[e].address + ", " + p[e].zip + " "
									+ p[e].city + ", " + p[e].country + "</td>"
									+ "<td>" + emailString + "</td>" + "<td>"
									+ phoneString + "</td>" + "<td>"
									+ p[e].updateTimestamp + "</td>" + "<td>"
									+ p[e].permission + "</td>" + "<td>"
									+ typeString + "</td>" + "<td>"
									+ "[LastUpdate]" + "</td>" + "</tr>";
							
							$("#personTableBody").append(tableRow);
							
						}
					});
		});

// Get one person and load it to modal
$("#btn_edit").click(function() {
	$("#modal_title_text").text("Bearbeite Person");
	
	//remove all phonenumber divs
	$(".btn_removephonenumber").closest('div[class^="phone_element"]').remove();
	phoneCount = 0;
	
	//remove all email divs
	$(".btn_removeemail").closest('div[class^="email_element"]').remove();
	emailCount = 0;
	
	var id = tableData[0];

	// Get person with id "id"
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getPersonById/" + id
	}).done(function(data) {

		var p = eval(data);

		//load data to modal
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
		
		for (var i = 0; i<p.telephones.length; i++) {
			phoneelement_template = "<div class='row'> <div class='form-group'> <div class='col-sm-5'> <input type='text' id='tbx_phoneNumber" 
				+ phoneCount + "' class='form-control tbx_phoneNumber' placeholder='Telefonnr.'> </div> <div class='col-sm-4'>" +
				"<select class='form-control' id='select_phoneNumber"+ phoneCount +"'> <option>privat</option> <option>gesch&auml;ftlich</option> </select>" +
				"</div> <div class='col-sm-3'> <button type='button' class='btn btn-danger btn_removephonenumber' id='btn_delete' >L&ouml;schen</button> </div> </div> </div>";
				
				newElement = $(
					"<div/>",
					{
						id : "phone_element" + phoneCount++,
						"class" : "phone_element"
					}).append(phoneelement_template);
					$("#phone_container").append(newElement);
			
			help = "#tbx_phoneNumber" + (phoneCount-1);
			$(help).val(p.telephones);	//not working right
			
			help = 'geschäftlich'; //test
			help1 = "select#select_phoneNumber" + (phoneCount-1) + " option";
			$(help1).each(function() { 
				this.selected = (this.text == help);
			});
		}
		
		//load email divs
		for (var i = 0; i<p.emails.length; i++) {
			emailelement_template = "<div class='row'> <div class='form-group'> <div class='col-sm-5'> <input type='text' id='tbx_email" + emailCount + "' " +
				"class='form-control tbx_mailadress' placeholder='Email'> </div> <div class='col-sm-4'> <select class='form-control' id='select_email"+ emailCount +"'>" +
				"<option>privat</option> <option>gesch&auml;ftlich</option> </select> </div> <div class='col-sm-3'><button type='button' class='btn btn-danger btn_removeemail'" +
				"id='btn_delete' >L&ouml;schen</button> </div> </div> </div>";
			
				newElement = $("<div/>", {
					id : "email_element" + emailCount++,
					"class" : "email_element"
				}).append(emailelement_template);
				$("#email_container").append(newElement);
				
			help = "#tbx_email" + (emailCount-1);
			$(help).val(p.emails);	//not working right
			
			help = 'geschäftlich';	//test
			help1 = "select#select_email" + (emailCount-1) + " option";
			$(help1).each(function() { 
				this.selected = (this.text == help);
			});
			
			//add emails to loginEmail select
			loginEmail_template = "<option>" + p.emails + "</option>";
			$("#select_loginEmail").append(loginEmail_template);
		}
		
		//load selects
		$("select#select_loginEmail option").each(function() { 
			this.selected = (this.text == p.loginEmail);
		});
		
		$("select#select_types option").each(function() { 
			this.selected = (this.text == p.types);
		});
		
		$("select#select_permission option").each(function() { 
			this.selected = (this.text == p.permission);
		});
	});
});

//add phonenumber div
$(document).ready(function() {
	$("#btn_addphonenumber").click(function() {
			phoneelement_template = "<div class='row'> <div class='form-group'> <div class='col-sm-5'> <input type='text' id='tbx_phoneNumber" 
									+ phoneCount + "' class='form-control tbx_phoneNumber' placeholder='Telefonnr.'> </div> <div class='col-sm-4'>" +
									"<select class='form-control' id='select_phoneNumber"+ phoneCount +"'> <option>privat</option> <option>gesch&auml;ftlich</option> </select>" +
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
			"class='form-control tbx_mailadress' placeholder='Email'> </div> <div class='col-sm-4'> <select class='form-control' id='select_email"+ emailCount +"'>" +
			"<option>privat</option> <option>gesch&auml;ftlich</option> </select> </div> <div class='col-sm-3'><button type='button' class='btn btn-danger btn_removeemail'" +
			"id='btn_delete' >L&ouml;schen</button> </div> </div> </div>";
		
		var newElement = $("<div/>", {
			id : "email_element" + emailCount++,
			"class" : "email_element"
		}).append(emailelement_template);
		$("#email_container").append(newElement);
		
		//TODO finish select appending
		var help = $("#tbx_email0").val();
		//alert(help);
		loginEmail_template = "<option>" + help + "</option>";
		$("#select_loginEmail").append(loginEmail_template);
	});
});

//remove email div
$("body").on('click', '.btn_removeemail', function() {
	$(this).closest('div[class^="email_element"]').remove();
	emailCount--;
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
					return $(this).find('td').eq(10).text() == "MA"
				}).show();
			} else {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "MA"
				}).hide();
			}
		});
		$('#unterstuetzer_cbx').on('change', function() {
			if (this.checked) {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "Unterst."
				}).show();
			} else {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "Unterst."
				}).hide();
			}
		});
		$('#mitglieder_cbx').on('change', function() {
			if (this.checked) {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "MG"
				}).show();
			} else {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "MG"
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

//select table row
$('#btn_edit').prop('disabled', true);
$('#btn_deleteModal').prop('disabled', true);

var tableData;
$('#personen').on('click', 'tbody tr', function(event) {
	tableData = $(this).children("td").map(function() {
		return $(this).text();
	}).get();

	$(this).addClass('highlight').siblings().removeClass('highlight');
	$('#btn_edit').prop('disabled', false);
	$('#btn_deleteModal').prop('disabled', false);
});

//remove table row modal
$("#btn_deleteModal").click(function() {
			$("#label_id").text(tableData[0]);
			$("#label_salutation").text(tableData[1]);
			$("#label_title").text(tableData[2]);
			$("#label_firstName").text(tableData[3]);
			$("#label_lastName").text(tableData[4]);
});
