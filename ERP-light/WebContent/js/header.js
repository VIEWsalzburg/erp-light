//load pageheader
$("#pageheader").load("../partials/header.html", function() {
	
	$(document).ready(function() {
		$.ajax({
			type : "POST",
			url : "../rest/secure/userdata"
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
	$("#btn_mydata").click(function() {
		//load and hide alert messsage
		var pwdError = "<div id='pwdErrorAlert'> <div class='col-sm-7'> <div class='alert alert-danger custom-alert' style='text-align: left;'>Es sind nicht alle Felder ausgefüllt!</div> </div>  </div>"
			$("#myDataAlertForm").append(pwdError);
			$("#myDataAlertForm").hide();
		
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

			var p = data;	// already JSON
			
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
			$("#tbx_comment_mydata").val(p.comment);

			//load phoneNumber divs
			var newElement;
			var loginEmail_template;
			var help;
			var help1;
			$("#select_loginEmail").find('option').remove().end();
			
			for (var i = 0; i<p.telephones.length; i++) {
				phoneelement_template = "<div class='row'><div class='col-md-6'><div class='form-group'><div class='col-sm-4'></div><div class='col-sm-8'>" +
					"<input type='text' id='tbx_phoneNumber_mydata" + phoneCount + "' class='form-control tbx_phoneNumber_mydata' placeholder='Telefonnr.' maxlength='20'>" +
					"</div></div></div><div class='col-md-6'><div class='form-group'><div class='col-sm-6'><select class='form-control select_phoneNumber_mydata' id='select_phoneNumber_mydata"+ phoneCount +"'>" +
					"<option>privat</option> <option>gesch&auml;ftlich</option></select></div><div class='col-sm-6'>" + 
					"<button type='button' class='btn btn-danger btn_removephonenumber_mydata' id='btn_delete_mydata' ><span class='glyphicon glyphicon-trash'></span> L&ouml;schen</button>" +
					"</div></div></div></div>";
				
					newElement = $(
						"<div/>",
						{
							id : "phone_element_mydata" + phoneCount++,
							"class" : "phone_element_mydata"
						}).append(phoneelement_template);
						$("#phone_container_mydata").append(newElement);
				
				help = "#tbx_phoneNumber_mydata" + (phoneCount-1);
				$(help).val(p.telephones[i].telephone);
				
				help = p.telephones[i].type; 
				help1 = "select#select_phoneNumber_mydata" + (phoneCount-1) + " option";
				$(help1).each(function() { 
					this.selected = (this.text == help);
				});
			}
			
			//load email divs
			for (var i = 0; i<p.emails.length; i++) {
				emailelement_template = "<div class='row'><div class='col-md-6'><div class='form-group'><div class='col-sm-4'></div><div class='col-sm-8'>" +
					"<input type='text' id='tbx_email_mydata" + emailCount + "' class='form-control tbx_mailadress_mydata' placeholder='Email' maxlength='50'>" +
					"</div></div></div><div class='col-md-6'><div class='form-group'><div class='col-sm-6'><select class='form-control select_email_mydata' id='select_email_mydata"+ emailCount +"'>" +
					"<option>privat</option> <option>gesch&auml;ftlich</option></select></div><div class='col-sm-6'>" + 
					"<button type='button' class='btn btn-danger btn_removeemail_mydata' id='btn_delete_mydata' ><span class='glyphicon glyphicon-trash'></span> L&ouml;schen</button>" +
					"</div></div></div></div>";
				
					newElement = $("<div/>", {
						id : "email_element_mydata" + emailCount++,
						"class" : "email_element_mydata"
					}).append(emailelement_template);
					$("#email_container_mydata").append(newElement);
					
				help = "#tbx_email_mydata" + (emailCount-1);
				$(help).val(p.emails[i].mail);
				
				help = p.emails[i].type;
				help1 = "select#select_email_mydata" + (emailCount-1) + " option";
				$(help1).each(function() { 
					this.selected = (this.text == help);
				});
			}
		});
	});
	
	$("#btn_mypassword").click(function() {
		var pwdError = "<div id='pwdErrorAlert'> <div class='col-sm-7'> <div class='alert alert-danger custom-alert' style='text-align: left;'>Passwörter stimmen nicht überein.</div> </div>  </div>"
			$("#passwordAlertForm").append(pwdError);
			$("#passwordAlertForm").hide();
	});
	
	//my password modal
	$("#btn_savemypassword").click(function() {
		
		// check if both new passwords are equal
		var oldPwd = $("#tbx_oldpassword").val();
		var newPwd = $("#tbx_newpassword").val();
		var newPwdAck = $("#tbx_acknewpassword").val();
		
		if (newPwd != newPwdAck)
		{
			$("#passwordAlertForm").show();
			
			$("#tbx_oldpassword").val("");
			$("#tbx_newpassword").val("");
			$("#tbx_acknewpassword").val("");	
			
			return;
		}		
		
		var newpassword = new Object();
		newpassword.oldPassword = oldPwd;
		newpassword.newPassword = newPwd;
		
		$.ajax({
			headers : {
				'Accept' : 'application/json',
				'Content-Type' : 'application/json'
			},
			type : "POST",
			url : "../rest/secure/person/changeCurrentUserPassword",
			contentType: "application/json; charset=utf-8",
		    dataType: "json",
			data : JSON.stringify(newpassword)
		}).done(function(data) {
			if (data) {
				$('#mypassword').modal('hide');
				
				if (data.success == true)
				{
					showAlertElement(1, data.message, 5000);
				}
				else
				{
					showAlertElement(2, data.message, 5000);
				}				
				
			} else {
				alert("Verbindungsproblem mit dem Server");
			}
			
			
		});
		
		// clear passwords, so they don't show up the next time the user wants to change password
		$("#tbx_oldpassword").val("");
		$("#tbx_newpassword").val("");
		$("#tbx_acknewpassword").val("");
		$("#pwdErrorAlert").remove();
		
		return false;
	});
	
	$(document).ready(function() {
		//get current user
		$.ajax({
			type : "POST",
			url : "../rest/secure/person/getCurrentUser"
		}).done(function(data) {

			var p = data;	// already JSON
			
			//load data to modal
			$("#username").text(p.firstName +" " + p.lastName);
		});
	});

	//add phonenumber div
	$(document).ready(function() {
		$("#btn_addphonenumber_mydata").click(function() {
				phoneelement_template = "<div class='row'><div class='col-md-6'><div class='form-group'><div class='col-sm-4'></div><div class='col-sm-8'>" +
					"<input type='text' id='tbx_phoneNumber_mydata" + phoneCount + "' class='form-control tbx_phoneNumber_mydata' placeholder='Telefonnr.' maxlength='20'>" +
					"</div></div></div><div class='col-md-6'><div class='form-group'><div class='col-sm-6'><select class='form-control select_phoneNumber_mydata' id='select_phoneNumber_mydata"+ phoneCount +"'>" +
					"<option>privat</option> <option>gesch&auml;ftlich</option></select></div><div class='col-sm-6'>" + 
					"<button type='button' class='btn btn-danger btn_removephonenumber_mydata' id='btn_delete_mydata' ><span class='glyphicon glyphicon-trash'></span> L&ouml;schen</button>" +
					"</div></div></div></div>";
				
										var newElement = $(
											"<div/>",
											{
												id : "phone_element_mydata" + phoneCount++,
												"class" : "phone_element_mydata"
											}).append(phoneelement_template);
											$("#phone_container_mydata").append(newElement);
		});
	});
	
	//remove phonenumber div
	$("body").on('click', '.btn_removephonenumber_mydata', function() {
		$(this).closest('div[class^="phone_element"]').remove();
		phoneCount--;
	});

	//add email div
	$(document).ready(function() {
		$("#btn_addemail_mydata").click(function() {
			emailelement_template = "<div class='row'><div class='col-md-6'><div class='form-group'><div class='col-sm-4'></div><div class='col-sm-8'>" +
			"<input type='text' id='tbx_email_mydata" + emailCount + "' class='form-control tbx_mailadress_mydata' placeholder='Email' maxlength='50'>" +
			"</div></div></div><div class='col-md-6'><div class='form-group'><div class='col-sm-6'><select class='form-control select_email_mydata' id='select_email_mydata"+ emailCount +"'>" +
			"<option>privat</option> <option>gesch&auml;ftlich</option></select></div><div class='col-sm-6'>" + 
			"<button type='button' class='btn btn-danger btn_removeemail_mydata' id='btn_delete_mydata' ><span class='glyphicon glyphicon-trash'></span> L&ouml;schen</button>" +
			"</div></div></div></div>";
			
			var newElement = $("<div/>", {
				id : "email_element_mydata" + emailCount++,
				"class" : "email_element_mydata"
			}).append(emailelement_template);
			$("#email_container_mydata").append(newElement);
			
		});
	});
	
	//remove email div
	$("body").on('click', '.btn_removeemail_mydata', function() {
		$(this).closest('div[class^="email_element"]').remove();
		emailCount--;
	});

	//Save my data
	$("#btn_savemydata").click(function() {
		if($("#tbx_salutation_mydata").val() == "" || $("#tbx_firstName_mydata").val() == "" || $("#tbx_lastName_mydata").val() == "" 
			|| $("#tbx_address_mydata").val() == "" || $("#tbx_city_mydata").val() == "" || $("#tbx_zip_mydata").val() == "" || $("#tbx_country_mydata").val() == "")
		{
				$("#myDataAlertForm").show();
				return;
		}
		
		var newperson = new Object();
			
		newperson.personId = $("#tbx_id_mydata").val();
		newperson.salutation = $("#tbx_salutation_mydata").val();
		newperson.title = $("#tbx_title_mydata").val();
		newperson.firstName = $("#tbx_firstName_mydata").val();
		newperson.lastName = $("#tbx_lastName_mydata").val();
		newperson.comment = $("#tbx_comment_mydata").val();
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
			url : "../rest/secure/person/changeMyData",
			contentType: "application/json; charset=utf-8",
		    dataType: "json",
			data : persondata
		}).done(function(data) {
			if (data) {
				$('#personTableBody').empty();
				$('#mydata').modal('hide');
				
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
});

//disable warenverwaltung href, if user has not the permission
var currentUserRights = "";
$(document).ready(function() {
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getCurrentUser"
	}).done(function(data) {
		var p = data;	// already JSON 
		currentUserRights = p.permission;
		
		if(currentUserRights == "Read"){
			$("#warenverwaltung_nav").attr("class", "disabled");
			$("#warenverwaltung_nav_a").attr("href", "#");
		}
	});
});
