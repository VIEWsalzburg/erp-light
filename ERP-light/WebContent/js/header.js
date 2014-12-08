//load pageheader
$("#pageheader").load("../partials/header.html", function() {
	
	$(document).ready(function() {
		$.ajax({
			type : "POST",
			url : "/ERP-light/rest/secure/userdata"
		}).done(function(data) {
		});
	});
	
	if(document.URL.indexOf("adressverwaltung") > -1)
	{
		$("#adressverwaltung_nav").addClass("active");
	}
	else if(document.URL.indexOf("warenverwaltung") >-1)
	{
		$("#warenverwaltung_nav").addClass("active");

	}
	else if(document.URL.indexOf("reporting") >-1)
	{
		$("#reporting_nav").addClass("active");

	}
	else
	{
		$("#home_nav").addClass("active");
	}
	
	var phoneCount = 0;
	var phoneelement_template = "";
	
	var emailCount = 0;
	var emailelement_template = "";

	// Get one person and load it to modal
	$(document).ready(function() {
		//remove all phonenumber divs
		$(".btn_removephonenumber_mydata").closest('div[class^="phone_element_mydata"]').remove();
		phoneCount = 0;
		
		//remove all email divs
		$(".btn_removeemail_mydata").closest('div[class^="email_element_mydata"]').remove();
		emailCount = 0;
		
		//get current user
		$.ajax({
			type : "POST",
			url : "../rest/secure/person/getCurrentUser"
		}).done(function(data) {

			var p = eval(data);
			//load data to modal
			$("#username").text(p.firstName +" " + p.lastName);

			$("#tbx_id_mydata").val(p.personId);
			$("#tbx_salutation_mydata").val(p.salutation);
			$("#tbx_title_mydata").val(p.title);
			$("#tbx_firstName_mydata").val(p.firstName);
			$("#tbx_lastName_mydata").val(p.lastName);
			$("#tbx_address_mydata").val(p.address);
			$("#tbx_zip_mydata").val(p.zip);
			$("#tbx_city_mydata").val(p.city);
			$("#tbx_country_mydata").val(p.country);

			//load phoneNumber divs
			var newElement;
			var loginEmail_template;
			var help;
			var help1;
			$("#select_loginEmail").find('option').remove().end();
			
			for (var i = 0; i<p.telephones.length; i++) {
				phoneelement_template = "<div class='row'> <div class='form-group'> <div class='col-sm-5'> <input type='text' id='tbx_phoneNumber_mydata" 
					+ phoneCount + "' class='form-control tbx_phoneNumber_mydata' placeholder='Telefonnr.'> </div> <div class='col-sm-4'>" +
					"<select class='form-control select_phoneNumber_mydata' id='select_phoneNumber_mydata"+ phoneCount +"'> <option>privat</option> <option>gesch&auml;ftlich</option> </select>" +
					"</div> <div class='col-sm-3'> <button type='button' class='btn btn-danger btn_removephonenumber_mydata' id='btn_delete_mydata' >L&ouml;schen</button> </div> </div> </div>";
					
					newElement = $(
						"<div/>",
						{
							id : "phone_element_mydata" + phoneCount++,
							"class" : "phone_element_mydata"
						}).append(phoneelement_template);
						$("#phone_container_mydata").append(newElement);
				
				help = "#tbx_phoneNumber_mydata" + (phoneCount-1);
				$(help).val(p.telephones[i].telephone);
				
				help = p.telephones[i].type; //test
				help1 = "select#select_phoneNumber_mydata" + (phoneCount-1) + " option";
				$(help1).each(function() { 
					this.selected = (this.text == help);
				});
			}
			
			//load email divs
			for (var i = 0; i<p.emails.length; i++) {
				emailelement_template = "<div class='row'> <div class='form-group'> <div class='col-sm-5'> <input type='text' id='tbx_email_mydata" + emailCount + "' " +
					"class='form-control tbx_mailadress_mydata' placeholder='Email'> </div> <div class='col-sm-4'> <select class='form-control select_email_mydata' id='select_email_mydata"+ emailCount +"'>" +
					"<option>privat</option> <option>gesch&auml;ftlich</option> </select> </div> <div class='col-sm-3'><button type='button' class='btn btn-danger btn_removeemail_mydata'" +
					"id='btn_delete_mydata' >L&ouml;schen</button> </div> </div> </div>";
				
					newElement = $("<div/>", {
						id : "email_element_mydata" + emailCount++,
						"class" : "email_element_mydata"
					}).append(emailelement_template);
					$("#email_container_mydata").append(newElement);
					
				help = "#tbx_email_mydata" + (emailCount-1);
				$(help).val(p.emails[i].mail);
				
				help = p.emails[i].type;	//test
				help1 = "select#select_email_mydata" + (emailCount-1) + " option";
				$(help1).each(function() { 
					this.selected = (this.text == help);
				});
			}
		});
	});

	//add phonenumber div
	$(document).ready(function() {
		$("#btn_addphonenumber_mydata").click(function() {
				phoneelement_template = "<div class='row'> <div class='form-group'> <div class='col-sm-5'> <input type='text' id='tbx_phoneNumber_mydata" 
										+ phoneCount + "' class='form-control tbx_phoneNumber_mydata' placeholder='Telefonnr.'> </div> <div class='col-sm-4'>" +
										"<select class='form-control select_phoneNumber_mydata' id='select_phoneNumber_mydata"+ phoneCount +"'> <option>privat</option> <option>gesch&auml;ftlich</option> </select>" +
										"</div> <div class='col-sm-3'> <button type='button' class='btn btn-danger btn_removephonenumber_mydata' id='btn_delete_mydata' >L&ouml;schen</button> </div> </div> </div>";
										
										var newElement = $(
											"<div/>",
											{
												id : "phone_element_mydata" + phoneCount++,
												"class" : "phone_element_mydata"
											}).append(phoneelement_template);
											$("#phone_container_mydata").append(newElement);
		});
	});

	//add email div
	$(document).ready(function() {
		$("#btn_addemail_mydata").click(function() {
			emailelement_template = "<div class='row'> <div class='form-group'> <div class='col-sm-5'> <input type='text' id='tbx_email_mydata" + emailCount + "' " +
				"class='form-control tbx_mailadress_mydata' placeholder='Email'> </div> <div class='col-sm-4'> <select class='form-control select_email_mydata' id='select_email_mydata"+ emailCount +"'>" +
				"<option>privat</option> <option>gesch&auml;ftlich</option> </select> </div> <div class='col-sm-3'><button type='button' class='btn btn-danger btn_removeemail_mydata'" +
				"id='btn_delete_mydata' >L&ouml;schen</button> </div> </div> </div>";
			
			var newElement = $("<div/>", {
				id : "email_element_mydata" + emailCount++,
				"class" : "email_element_mydata"
			}).append(emailelement_template);
			$("#email_container_mydata").append(newElement);
			
		});
	});

	//Save my data
	$("#btn_savemydata").click(function() {
		
		var newperson = new Object();

		newperson.personId = $("#tbx_id_mydata").val();
		newperson.salutation = $("#tbx_salutation_mydata").val();
		newperson.title = $("#tbx_title_mydata").val();
		newperson.firstName = $("#tbx_firstName_mydata").val();
		newperson.lastName = $("#tbx_lastName_mydata").val();
		//Not used yet
		newperson.comment = "";
		//Set by server
		newperson.updateTimestamp = "";
		newperson.active = 1;

		newperson.address = $("#tbx_address_mydata").val();
		newperson.city = $("#tbx_city_mydata").val();
		newperson.zip = $("#tbx_zip_mydata").val();
		newperson.country = $("#tbx_country_mydata").val();

		var typesArray = [];
		newperson.types = typesArray;

		newperson.emails = [];
		$(".email_element_mydata").each(function (){
			newperson.emails.push({"mail":$(this).find(".tbx_mailadress_mydata").val(), "type":$(this).find(".select_email_mydata").val()});

		});
		newperson.telephones = [];
		$(".phone_element_mydata").each(function (){
			newperson.telephones.push({"telephone":$(this).find(".tbx_phoneNumber_mydata").val(), "type":$(this).find(".select_phoneNumber_mydata").val()});
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
				$('#mydata').modal('hide');
			} else {
				alert("Verbindungsproblem mit dem Server");
			}
		});
		return false;
	});

});