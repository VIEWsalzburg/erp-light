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
	else if (document.URL.indexOf("hilfe") > -1)
	{
		$("#hilfe_nav").addClass("active");
	}
	else
	{
		$("#home_nav").addClass("active");
	}
	
	var phoneCount = 0;
	var phoneelement_template = "";
	
	var emailCount = 0;
	var emailelement_template = "";
	
	
	// function for showing an alert message
	function showAlertElement(success, text, timeOut)
	{
			$('.myAlert').remove();
		
			var successDiv = "";
			var alertClass = "";
		
			//set class
			if (success == 1 || success == true)
			{
				alertClass = "alert alert-success";
			}
			else if (success == 2 || success == false)
			{
				alertClass = "alert alert-danger";
			}
			
			successDiv = "	<div class='myAlert' style='position: absolute; width: 100%; z-index: 10; top: 60px;'> " +
							 " <div style='width: 220px; margin: auto; text-align: center; padding-top:10px;'>" +
							 " <div class='row'> " + 
							 " <div class='"+alertClass+"' role='alert'>"+text+"</div>" +
							 " </div>	</div>	<div>";
			
			var domElement = $.parseHTML(successDiv);
				
			$("body").append(domElement);
			
			$('.myAlert').fadeIn("slow").delay(timeOut).fadeOut("slow",
				function() {
					$('.myAlert').remove();
				}
			);
	}
	
	
	
	// update option entries when seleciton box gains focus
	$('#select_loginEmail_mydata').focus(updateLoginEmailSelectMyData);
	
	// update the entries within the selection for the loginEmail
	function updateLoginEmailSelectMyData()
	{
		// selected Email
		var selectedEmail = $('#select_loginEmail_mydata').val();
		
		// remove all Elements
		$("#select_loginEmail_mydata").find('option').remove().end();
		
		// add all Elements within modal mydata
		$("#mydata .tbx_mailadress_mydata").each(function() {
			var emailValue = $(this).val();
			var inputId = $(this).attr('id');
			$("#select_loginEmail_mydata").append($("<option id='"+inputId+"_option'></option>")
				         .text(emailValue));
		});
		
		// reselect the selectedEmail
		$("#select_loginEmail_mydata").val(selectedEmail);
	}
	
	// update email addresses within selection box when editing a email
	$(document).on('keyup','#email_container_mydata input',
			function()
			{
				// new email address
				var newText = $(this).val();
				var inputId = $(this).attr('id');
				
				// update email addresses in option box
				$('#select_loginEmail_mydata').find('#'+inputId+'_option').text(newText);
	});
	
	
	// Get one person and load it to modal
	$("#btn_mydata").click(function() {
		// remove alert message which have been there before
		$('#pwdErrorAlert').remove();
		
		//load and hide alert messsage
		var pwdError = "<div id='pwdErrorAlert'> <div class='col-sm-7'> <div id='ErrorAlertMessageMyData' class='alert alert-danger custom-alert' style='text-align: left;'>Es sind nicht alle Felder ausgefüllt!</div> </div>  </div>"
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
			$("#select_loginEmail_mydata").find('option').remove().end();
			
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
							id : "phone_element_mydata" + phoneCount,
							"class" : "phone_element_mydata"
						}).append(phoneelement_template);
						$("#phone_container_mydata").append(newElement);
				
				help = "#tbx_phoneNumber_mydata" + phoneCount;
				$(help).val(p.telephones[i].telephone);
				
				help = p.telephones[i].type; 
				help1 = "select#select_phoneNumber_mydata" + phoneCount + " option";
				$(help1).each(function() { 
					this.selected = (this.text == help);
				});
				
				phoneCount++;
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
						id : "email_element_mydata" + emailCount,
						"class" : "email_element_mydata"
					}).append(emailelement_template);
					$("#email_container_mydata").append(newElement);
					
				help = "#tbx_email_mydata" + emailCount;
				$(help).val(p.emails[i].mail);
				
				help = p.emails[i].type;
				help1 = "select#select_email_mydata" + emailCount + " option";
				$(help1).each(function() { 
					this.selected = (this.text == help);
				});
				
				//add emails to loginEmail select
				loginEmail_template = "<option id='tbx_email_mydata"+emailCount+"_option'>" + p.emails[i].mail + "</option>";
				$("#select_loginEmail_mydata").append(loginEmail_template);
				
				emailCount++;
			}
			
			// select correct email in loginEmailSelect
			$("#select_loginEmail_mydata").val(p.loginEmail);
			
			
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
			$('#ErrorAlertMessageMyData').text("Passwörter stimmen nicht überein!");
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
			
			// update loginEmail options
			updateLoginEmailSelectMyData();
			
		});
	});
	
	//remove email div
	$("body").on('click', '.btn_removeemail_mydata', function() {
		$(this).closest('div[class^="email_element"]').remove();
		emailCount--;
		
		// update loginEmail options
		updateLoginEmailSelectMyData();
	});

	//Save my data
	$("#btn_savemydata").click(function() {
		if(  $("#tbx_lastName_mydata").val() == "" )
		{
			$('#ErrorAlertMessageMyData').text("Kein Nachname!");
			$("#myDataAlertForm").show();
			return;
		}
		
		if( $('#select_loginEmail_mydata').val() == "" || $('#select_loginEmail_mydata option').length == 0 )
		{
			$('#ErrorAlertMessageMyData').text("Keine Login-Email!");
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

		newperson.loginEmail = $("#select_loginEmail_mydata").val();
		
		// missing types are not persisted in the backend
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
				$('#mydata').modal('hide');
				
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
		return false;
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
			
			if( (currentUserRights != "ReadWrite") && (currentUserRights != "Admin") ){
				$("#warenverwaltung_nav").attr("class", "disabled");
				$("#warenverwaltung_nav_a").attr("href", "#");
			}
		});
	});
	
});


