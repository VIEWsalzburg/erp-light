var phoneCount = 0;
var phoneelement_template = "";

var emailCount = 0;
var emailelement_template = "";

var currentUser = "";
var currentUserRights = "";

var pwdError = "<div id='pwdErrorAlert'> <div class='col-sm-4'> <div id='ErrorAlertMessage' class='alert alert-danger custom-alert' style='text-align: left;'>Leere Felder vorhanden!</div> </div>  </div>"
	$("#newAlertForm").append(pwdError);


var p;
function loadTableContent() {
	
	// show loading spinner
	showLoadingSpinner(true);
	
	$('#personTableBody').empty();
	
	// check all Checkboxes
	$('#mitarbeiter_cbx').prop('checked', true);
	$('#unterstuetzer_cbx').prop('checked', true);
	$('#mitglieder_cbx').prop('checked', true);
	$('#gaeste_cbx').prop('checked', true);
	
	// personList for appending the persons
	var personList;
	
	// determine the loading option
	
	var selectedPage = $('#select_page').val();
	
	
	if (selectedPage == "Alle")
	{
		// load all Persons
		$.ajax({
			type : "POST",
			async: false,
			url : "../rest/secure/person/getAllActive"
		}).done(
				function(data) {
					personList = data;	// already JSON
		});
	}
	
	if (isNaN(selectedPage)==false)
	{
		var count = 200;
		var offset = (selectedPage-1)*200;
		// load Persons
		$.ajax({
			type : "POST",
			async: false,
			url : "../rest/secure/person/getActive?count="+count+"&offset="+offset
		}).done(
				function(data) {
					personList = data;	// already JSON
		});
	}
	
	
	
	// insert Persons
	{
		p = personList;
		
		for (var e in p) {
			var emailString = "";
			var phoneString = "";
			var typeString = "";

			var types = p[e].types;
			var emails = p[e].emails;
			var phoneNumbers = p[e].telephones;
			
			for (var i = 0; i < emails.length; i++) {
				emailString = emailString + emails[i].type.substring(0,1).toLowerCase()+": "+ emails[i].mail;
				if (i < emails.length - 1) {
					emailString = emailString + "," + "<br/>";
				}
			}
			for (var j = 0; j < phoneNumbers.length; j++) {
				phoneString = phoneString + phoneNumbers[j].type.substring(0,1).toLowerCase()+": "+phoneNumbers[j].telephone;
				if (j < phoneNumbers.length - 1) {
					phoneString = phoneString + "," + "<br/>";
				}
			}
			for (var k = 0; k < types.length; k++) {
				typeString = typeString + types[k];
				if (k < types.length - 1) {
					typeString = typeString + "," + "<br/>";
				}
			}
			
			// create addressString according to available address variables
			var addressString = "";
			if (p[e].address.length > 0)
				addressString += p[e].address;
			if (p[e].city.length > 0)
			{
				if (addressString.length > 0)
					addressString += ", ";
				addressString += p[e].zip + " " + p[e].city;
			}
			if (p[e].country.length > 0)
			{
				if (addressString.length > 0)
					addressString += ", ";
				addressString += p[e].country;
			}
			
			var tableRow = "<tr>" +
					"<td class='hidden'>" + p[e].personId + "</td>" +
					"<td>" + p[e].salutation + " " + p[e].title + " " + p[e].lastName + " " + p[e].firstName + "</td>" +
					"<td>" + addressString + "</td>" +
					"<td>" + phoneString + "</td>" +
					"<td>" + emailString + "</td>" +
					"<td>" + typeString + "</td>" +
					"<td>" + p[e].comment + "</td>" +
					"</tr>";
			
			$("#personTableBody").append(tableRow);
		}
		
		// hide loading spinner
		showLoadingSpinner(false);
		
		// update person count label
		updatePersonCountLabel();
		
	}
	
	
	
//	// load all Persons
//	$.ajax({
//		type : "POST",
//		url : "../rest/secure/person/getAllActive"
//	}).done(
//			function(data) {
//				p = data;	// already JSON
//				
//				
//				
//			});

};

// update option entries when selection box gains focus
$("#select_loginEmail").focus(updateLoginEmailSelect);

// updates the entries within the selection for the loginEmail
function updateLoginEmailSelect(){
	
	// selected Email
	var selectedEmail = $('#select_loginEmail').val();
	
	// remove all Elements
	$("#select_loginEmail").find('option').remove().end();
	
	// add all Elements
	$(".tbx_mailadress").each(function() {
		var emailValue = $(this).val();
		var inputId = $(this).attr('id');
		$("#select_loginEmail").append($("<option id='"+inputId+"_option'></option>")
			         .text(emailValue));
	});
	
	// reselect the selectedEmail
	$("#select_loginEmail").val(selectedEmail);
	
};

// update email addresses within selection box when editing a email
$(document).on('keyup','#email_container input',
		function()
		{
			// new email address
			var newText = $(this).val();
			var inputId = $(this).attr('id');
			
			// update email addresses in option box
			$('#select_loginEmail').find('#'+inputId+'_option').text(newText);
			
});




//Modal new
$("#btn_new").click(function() {
	//hide alert messsage
	$("#newAlertForm").hide();
	
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
	
	//systemuser default checked=false
	$('#cbx_systemuser').prop('checked', false);
	$('.divContainer').hide();
	
	//uncheck type checkboxes
	$('#cbx_mitarbeiter').prop('checked', false);
	$('#cbx_mitglied').prop('checked', false);
	$('#cbx_gast').prop('checked', false);
	$('#cbx_unterstuetzer').prop('checked', false);
});

//save person
$("#btn_saveperson").click(function() {
	
	if( $("#tbx_lastName").val() == "")
	{
		$('#ErrorAlertMessage').text("Kein Nachname!");
		$("#newAlertForm").show();
		return;
	}
	
	// if person is a systemuser, then check the loginEmail
	if( $('#cbx_systemuser').prop('checked') )
	{
		if( $('#select_loginEmail').val() == "" || $('#select_loginEmail option').length == 0 )
		{
			$('#ErrorAlertMessage').text("Keine Login-Email!");
			$("#newAlertForm").show();
			return;
		}
	}
	
	var newperson = new Object();

	newperson.personId = $("#tbx_id").val();
	newperson.salutation = $("#tbx_salutation").val();
	newperson.title = $("#tbx_title").val();
	newperson.firstName = $("#tbx_firstName").val();
	newperson.lastName = $("#tbx_lastName").val();
	newperson.comment = $("#tbx_comment").val();
	// These values are set by the server
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
	
	// set SystemUser Property
	newperson.systemUser = $('#cbx_systemuser').prop('checked');
	
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



// Get one person and load it to modal
var p;
$("#btn_edit").click(function() {
	$("#modal_title_text").text("Bearbeite Person");
	
	//hide alert messsage
	$("#newAlertForm").hide();
	
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
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Keine Person auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];

	// Get person with id "id"
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getPersonById/" + id
	}).done(function(data) {

		p = data;	// already JSOn

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
		$("#tbx_comment").val(p.comment);

		//check if systemuser
		$('#cbx_systemuser').prop('checked', false);
		$('.divContainer').hide();
		
		// hide reset passwort button automatically
		$('#btn_resetpasswordModal').hide();
		
		if(p.systemUser){
			$('#cbx_systemuser').prop('checked', true);
			$(".divContainer").show();
			
			//load selects
			$("select#select_loginEmail option").each(function() { 
				this.selected = (this.text == p.loginEmail);
			});
			
			$("select#select_permission option").each(function() { 
				this.selected = (this.text == p.permission);
			});
			
			// show reset passwort button if person is a platformuser
			$('#btn_resetpasswordModal').show();
		}
		
		//load phoneNumber divs
		var newElement;
		var loginEmail_template;
		var help;
		var help1;
		$("#select_loginEmail").find('option')
	    .remove()
	    .end();
		for (var i = 0; i<p.telephones.length; i++) {
			phoneelement_template = "<div class='row'><div class='col-md-6'><div class='form-group'><div class='col-sm-4'></div><div class='col-sm-8'>" +
			"<input type='text' id='tbx_phoneNumber" + phoneCount + "' class='form-control tbx_phoneNumber' placeholder='Telefonnr.' maxlength='20'>" +
			"</div></div></div><div class='col-md-6'><div class='form-group'><div class='col-sm-6'><select class='form-control select_phoneNumber' id='select_phoneNumber"+ phoneCount +"'>" +
			"<option>privat</option> <option>gesch&auml;ftlich</option></select></div><div class='col-sm-6'>" + 
			"<button type='button' class='btn btn-danger btn_removephonenumber' id='btn_delete' ><span class='glyphicon glyphicon-trash'></span> L&ouml;schen</button>" +
			"</div></div></div></div>";
				
				newElement = $(
					"<div/>",
					{
						id : "phone_element" + phoneCount,
						"class" : "phone_element"
					}).append(phoneelement_template);
					$("#phone_container").append(newElement);
			
			help = "#tbx_phoneNumber" + (phoneCount);
			$(help).val(p.telephones[i].telephone);
			
			help = p.telephones[i].type; 
			help1 = "select#select_phoneNumber" + (phoneCount) + " option";
			$(help1).each(function() { 
				this.selected = (this.text == help);
			});
			
			phoneCount++;
		}
		
		//load email divs
		for (var i = 0; i<p.emails.length; i++) {
			emailelement_template = "<div class='row'><div class='col-md-6'><div class='form-group'><div class='col-sm-4'></div><div class='col-sm-8'>" +
				"<input type='text' id='tbx_email" + emailCount + "' class='form-control tbx_mailadress' placeholder='Email' maxlength='50'>" +
				"</div></div></div><div class='col-md-6'><div class='form-group'><div class='col-sm-6'><select class='form-control select_email' id='select_email"+ emailCount +"'>" +
				"<option>privat</option> <option>gesch&auml;ftlich</option></select></div><div class='col-sm-6'>" + 
				"<button type='button' class='btn btn-danger btn_removeemail' id='btn_delete' ><span class='glyphicon glyphicon-trash'></span> L&ouml;schen</button>" +
				"</div></div></div></div>";
			
				newElement = $("<div/>", {
					id : "email_element" + emailCount,
					"class" : "email_element"
				}).append(emailelement_template);
				$("#email_container").append(newElement);
				
			help = "#tbx_email" + (emailCount);
			$(help).val(p.emails[i].mail);
			
			help = p.emails[i].type;	//test
			help1 = "select#select_email" + (emailCount) + " option";
			$(help1).each(function() { 
				this.selected = (this.text == help);
			});
			
			//add emails to loginEmail select
			loginEmail_template = "<option id='tbx_email"+emailCount+"_option'>" + p.emails[i].mail + "</option>";
			$("#select_loginEmail").append(loginEmail_template);
			emailCount++;
		}
		
		// select correct email
		$("#select_loginEmail").val(p.loginEmail);
		
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
		
		// show modal when all data has been filled in
		$('#new').modal('show');
		
	});
});

//add phonenumber div
$(document).ready(function() {
	$("#btn_addphonenumber").click(function() {
			phoneelement_template = "<div class='row'><div class='col-md-6'><div class='form-group'><div class='col-sm-4'></div><div class='col-sm-8'>" +
									"<input type='text' id='tbx_phoneNumber" + phoneCount + "' class='form-control tbx_phoneNumber' placeholder='Telefonnr.' maxlength='20'>" +
									"</div></div></div><div class='col-md-6'><div class='form-group'><div class='col-sm-6'><select class='form-control select_phoneNumber' id='select_phoneNumber"+ phoneCount +"'>" +
									"<option>privat</option> <option>gesch&auml;ftlich</option></select></div><div class='col-sm-6'>" + 
									"<button type='button' class='btn btn-danger btn_removephonenumber' id='btn_delete' ><span class='glyphicon glyphicon-trash'></span> L&ouml;schen</button>" +
									"</div></div></div></div>";
			
			var newElement = $(
				"<div/>",
				{
					id : "phone_element" + phoneCount,
					"class" : "phone_element"
				}).append(phoneelement_template);
				$("#phone_container").append(newElement);
				
				phoneCount++;
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
		emailelement_template = "<div class='row'><div class='col-md-6'><div class='form-group'><div class='col-sm-4'></div><div class='col-sm-8'>" +
			"<input type='text' id='tbx_email" + emailCount + "' class='form-control tbx_mailadress' placeholder='Email' maxlength='50'>" +
			"</div></div></div><div class='col-md-6'><div class='form-group'><div class='col-sm-6'><select class='form-control select_email' id='select_email"+ emailCount +"'>" +
			"<option>privat</option> <option>gesch&auml;ftlich</option></select></div><div class='col-sm-6'>" + 
			"<button type='button' class='btn btn-danger btn_removeemail' id='btn_delete' ><span class='glyphicon glyphicon-trash'></span> L&ouml;schen</button>" +
			"</div></div></div></div>";
		
		var newElement = $("<div/>", {
			id : "email_element" + emailCount,
			"class" : "email_element"
		}).append(emailelement_template);
		$("#email_container").append(newElement);
		
		// update the elements in the loginEmail Selection
		updateLoginEmailSelect();
		
		emailCount++;
		
	});
});

//remove email div
$("body").on('click', '.btn_removeemail', function() {
	$(this).closest('div[class^="email_element"]').remove();
	emailCount--;
	
	// update the elements in the loginEmail Selection
	updateLoginEmailSelect();
});

//reset password
$("#btn_resetpassword").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Keine Person auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/resetPasswordForId/" + id
	}).done(function(data) {
		
		if (data) {
			$('#resetpasswordModal').modal('hide');
			
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
});


//showing the system user checkbox within the new / edit modal
$('#cbx_systemuser').on('change', function() {
	if($("#cbx_systemuser").prop('checked')){
		$(".divContainer").show();
	}
	else{
		$('.divContainer').hide();
	}
});



//load details modal
$("#btn_details").click(function() {
	//remove container
	$(".details").remove();
	$("#loginEmailPermission_container_details").hide();
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Keine Person auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getPersonById/" + id
	}).done(function(data) {
		
		var p = data;	// already JSON
		
		$("#label_salutation_details").text(p.salutation);
		
		if(p.title == ""){
			$("#label_title_details").text("-");
		}
		else{
			$("#label_title_details").text(p.title);
		}
		
		$("#label_firstName_details").text(p.firstName);
		$("#label_lastName_details").text(p.lastName);
		$("#label_address_details").text(p.address);
		$("#label_zip_details").text(p.zip);
		$("#label_city_details").text(p.city);
		$("#label_country_details").text(p.country);
		$("#label_lastEditor_details").text(p.lastEditor);
		$("#label_updateTimestamp_details").text(p.updateTimestamp);
		
		if(p.comment == ""){
			$("#label_comment_details").text("-");
		}
		else{
			$("#label_comment_details").text(p.comment);
		}
		
		//load phone numbers
		var phoneNumbers = p.telephones;
		if(phoneNumbers.length == 0){
			$("#label_phoneNumber_details").text("-");
		}
		else{
			var phoneString = "";
			// fill in first line
			$("#label_phoneNumber_details").text(phoneNumbers[0].telephone + " (" + phoneNumbers[0].type.toLowerCase() + ")");
			// add all extra lines beginning with index 1
			for (var j = 1; j < phoneNumbers.length; j++) {
				phoneString = phoneNumbers[j].telephone + " (" + phoneNumbers[j].type.toLowerCase() + ")";
				var template = "<div class='row details'><div class='col-md-6'></div><div class='col-md-6'><label>" + phoneString + "</label></div></div>";
				$("#phone_container_details").append(template);
			}
		}
		
		//load emails
		var emails = p.emails;
		if(emails.length == 0){
			$("#label_email_details").text("-");
		}
		else{
			var emailString = "";
			// fill in first line
			$("#label_email_details").text(emails[0].mail + " (" + emails[0].type.toLowerCase() + ")");
			// add all extra lines beginning with index 1
			for (var j = 1; j < emails.length; j++) {
				emailString = emails[j].mail + " (" + emails[j].type.toLowerCase() + ")";
				var template = "<div class='row details'><div class='col-md-6'></div><div class='col-md-6'><label>" + emailString + "</label></div></div>";
				$("#email_container_details").append(template);
			}
		}
		
		//load types
		var types = p.types;
		if(types.length == 0){
			$("#label_types_details").text("-");
		}
		else{
			var typeString = "";
			$("#label_types_details").text(types[0]);
			for (var j = 1; j < types.length; j++) {
				typeString = types[j];
				var template = "<div class='row details'><div class='col-md-6'></div><div class='col-md-6'><label>" + typeString + "</label></div></div>";
				$("#type_container_details").append(template);
			}
		}
		
		//check if systemuser
		if(p.systemUser){
			$("#cbx_systemuser_details").prop("checked", "true");
			$("#loginEmailPermission_container_details").show();
			$("#label_loginEmail_details").text(p.loginEmail);
			$("#label_permission_details").text(p.permission);
		}
		else{
			$("#cbx_systemuser_details").removeAttr("checked");
		}
		
		// show details modal after all data is filled in
		$('#details').modal('show');
		
	});	// end ajax
	
});

// function for updating the person count label right above the table
function updatePersonCountLabel()
{
	var count = $('#personTableBody').children(':visible').length;
	
	$('#lbl_person_count').text(count+" Personen");

}


//search filter
$(document).ready(function() {
	(function($) {
		$('#filter').keyup(function() {

			// check all Checkboxes
			$('#mitarbeiter_cbx').prop('checked', true);
			$('#unterstuetzer_cbx').prop('checked', true);
			$('#mitglieder_cbx').prop('checked', true);
			$('#gaeste_cbx').prop('checked', true);
			$('#keinTyp_cbx').prop('checked', true);
			
			var rex = new RegExp($(this).val(), 'i');
			// hide all Persons
			$('.searchable tr').hide();
			// show all Persons, which contain the given text
			$('.searchable tr').filter(function() {
				
				var text = $(this).children().map(function(i,opt) {
					return $(opt).text();
				}).toArray().join(',');
				
				return rex.test(text);
			}).show();
			
			// update person count label
			updatePersonCountLabel();
		})
	}(jQuery));
});


// show/hide Persons by Types
function updateTableTypeFilter() {
	
	// remove textfilter
	$('#filter').val('');
	
	// get checkbox states
	var mitarbeiterChecked = $('#mitarbeiter_cbx').prop('checked'); 
	var unterstuetzerChecked = $('#unterstuetzer_cbx').prop('checked');
	var mitgliederChecked = $('#mitglieder_cbx').prop('checked');
	var gaesteChecked = $('#gaeste_cbx').prop('checked');
	var keinTypChecked = $('#keinTyp_cbx').prop('checked');
	
	$('.searchable tr').each( function() {
		
		// hide all by default
		var show = false;
		
		// get typeText
		var typeText = $(this).find('td').eq(5).text();
		
		// show all Mitarbeiter
		if (mitarbeiterChecked)
		{
			if (typeText.indexOf('Mitarbeiter')!=-1)
				show = true;
		}
		
		// show all Unterstützer
		if (unterstuetzerChecked)
		{
			if (typeText.indexOf('Unterstützer')!=-1)
				show = true;
		}
		
		// show all Mitglieder
		if (mitgliederChecked)
		{
			if (typeText.indexOf('Mitglied')!=-1)
				show = true;
		}
		
		// show all Gäste
		if (gaesteChecked)
		{
			if (typeText.indexOf('Gast')!=-1)
				show = true;
		}
		
		// show all empty types
		if (keinTypChecked)
		{
			if (typeText == '')
				show = true;
		}
		
		if (show) {
			$(this).show();
		} else {
			$(this).hide();
		}
		
	} );
	
	// update person count label
	updatePersonCountLabel();

}

// assign typefilter to checkboxes
$(document).ready(function() {
	$('#mitarbeiter_cbx').prop('checked', true);
	$('#unterstuetzer_cbx').prop('checked', true);
	$('#mitglieder_cbx').prop('checked', true);
	$('#gaeste_cbx').prop('checked', true);
	$('#keinTyp_cbx').prop('checked', true);

	$('#mitarbeiter_cbx').on('change', updateTableTypeFilter);
	$('#unterstuetzer_cbx').on('change', updateTableTypeFilter);
	$('#mitglieder_cbx').on('change', updateTableTypeFilter);
	$('#gaeste_cbx').on('change', updateTableTypeFilter);
	$('#keinTyp_cbx').on('change', updateTableTypeFilter);
});

//disable new, edit, delete and details buttons
$('#btn_new').hide();
$(".suchfilter").css("margin-left", "0px");
$('#btn_edit').hide();
$('#btn_deleteModal').hide();
$('#btn_details').prop('disabled', true);

//get current user rights
$(document).ready(function() {
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getCurrentUser"
	}).done(function(data) {
		currentUser = data;		// already JSON
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

//Get all Persons and load into table
$(document).ready(
		function(){
			
			$('#select_page').append("<option>Alle</option>");
			
			// determine number of active persons in the system
			var countActive = -1;
			
			$.ajax({
				type : "POST",
				async: false,
				url : "../rest/secure/person/countActive"
			}).done(function(data) {
				countActive = data;
			});
			
			if(countActive != -1)
			{
				// maximum number per page = 200
				var numberPages = Math.ceil(countActive/200);
				
				for (var i=0; i<numberPages; i++)
				{
					// add options to select box
					$('#select_page').append("<option>"+(i+1)+"</option>");
				}
			}
			
			// select option Alle
			$('#select_page :nth-child(0)').prop('selected',true);
			
			loadTableContent();
			
			$('#select_page').change(loadTableContent);
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

$('#TableHead').on('click', 'tbody tr', function(event) {
	var rowData = $(this).children("td").map(function() {
		return $(this).text();
	}).get();

	$(this).addClass('highlight').siblings().removeClass('highlight');
	
		//only when user has admin rights
		if(currentUserRights == "Admin" && currentUserRights != "" && currentUser.personId != rowData[0]){
			$('#btn_edit').prop('disabled', false);
			$('#btn_deleteModal').prop('disabled', false);
		}
		else{
			$('#btn_edit').prop('disabled', true);
			$('#btn_deleteModal').prop('disabled', true);
		}
		$('#btn_details').prop('disabled', false);
});

// call delete modal and fill in according data
$("#btn_deleteModal").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Keine Person auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getPersonById/" + id
	}).done(function(data) {
	
		var p = data;	// already JSON
	
		$("#label_name_delete").text(p.title + " " + p.lastName + " " + p.firstName);
		$("#label_address_delete").text(p.address + ", " + p.zip + " " + p.city + ", " + p.country);
	
		// show modal after all data is filled in
		$('#deleteModal').modal('show');
		
	});
	
});

// delete Action
$("#btn_deletePerson").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Keine Person auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
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


// export current view as CSV
$('#btn_exportCurrentView').click(function(){
	
	var tableData = [];
	
	var headerString = $('#TableHead .TableHead tr').children().map(
			function(){
				return $(this).text();
			}).get().join(';');
	tableData.push(headerString);
	
	// only visible rows
	// concat columns with separator ';' for each row and push it into tableData
	$('#personTableBody tr:visible').each(function(){
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
	
	// MS IE option
	if (window.navigator.userAgent.indexOf('MSIE ') != -1)
	{
		var blob = new Blob([csvString]);
		window.navigator.msSaveOrOpenBlob(blob, 'Personen-Export.csv');
	}
	else	// other browsers
	{
		var csvContent = encodeURIComponent(csvString);
		
		var csvFile = "data:application/csv;charset=utf-8,"+csvContent;
		
		$('body').append($('<a id="csvTableDownload" href="'+csvFile+'" target="_blank" download="Personen-Export.csv"/>'));
		$('#csvTableDownload').ready(function(){
			$('#csvTableDownload').get(0).click();
			$('#csvTableDownload').remove();
		});
	}
	
});