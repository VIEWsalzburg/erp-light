var phoneCount = 0;
var phoneelement_template = "";

var emailCount = 0;
var emailelement_template = "";

// Get one person and load it to modal
$(document).ready(function() {
	//remove all phonenumber divs
//	$(".btn_removephonenumber").closest('div[class^="phone_element"]').remove();
//	phoneCount = 0;
	
	//remove all email divs
//	$(".btn_removeemail").closest('div[class^="email_element"]').remove();
//	emailCount = 0;
	
	//get current user
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getCurrentUser" //TODO not working
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
			$(help).val(p.telephones[i]);
			
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
			$(help).val(p.emails[i]);
			
			help = 'geschäftlich';	//test
			help1 = "select#select_email" + (emailCount-1) + " option";
			$(help1).each(function() { 
				this.selected = (this.text == help);
			});
		}
	});
});

//Save
$("#btn_savemydata").click(function() {
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

	newperson.types = typesArray;

	newperson.emails = [];
	$(".tbx_mailadress").each(function (){
		newperson.emails.push($(this).val());
	});
	newperson.telephones = [];
	$(".tbx_phoneNumber").each(function (){
		newperson.telephones.push($(this).val());
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
			loadTableContent();
		} else {
			alert("Verbindungsproblem mit dem Server");
		}
	});
	return false;
});



