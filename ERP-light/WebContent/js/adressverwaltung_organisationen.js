//Load pageheader
$("#pageheader").load("../partials/header.html", function() {
	$("#adressverwaltung_nav").addClass("active");
});

//append alert message to modal
var pwdError = "<div id='pwdErrorAlert'> <div class='col-sm-4'> <div class='alert alert-danger custom-alert' style='text-align: left;'>Leere Felder vorhanden!</div> </div>  </div>"
	$("#newAlertForm").append(pwdError);

var template = "<div class='row'><div class='col-md-6'><label>Login Email</label></div><div class='col-md-6'><label id='label_loginEmail_details'>Login Email</label>" +
	"</div></div><div class='row'><div class='col-md-6'><label>Rechte</label></div><div class='col-md-6'><label id='label_permission_details'>Rechte</label>" +
	"</div></div>";
$("#loginEmailPermission_container_details").append(template);

//load contact persons to modal
var p;
function loadAllContactPersons() {
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/person/getAll"
	}).done(
			function(data) {
				p = eval(data);
				
				for (var e in p) {
					var p_divRow = "<div class='boxElement_person'>" + "<span>" + p[e].lastName + " " + p[e].firstName
								+ " " + "</span><input class='pull-right' type='checkbox' id='cbx_contactperson" + p[e].personId + "'></div>";
					
					$("#contactPersonDiv").append(p_divRow);
				}
			});
};

function clearAndLoadDivContainer(){
	//clear div container
	$(".boxElement_person").remove();
	$(".boxElement_category").remove();
	
	//load div container
	loadAllContactPersons();
	loadAllCategories();
};

//load categories to modal
var c;
function loadAllCategories() {
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/category/getAllCategories"
	}).done(
			function(data) {
				c = eval(data);
				
				for (var e in c) {
					var c_divRow = "<div class='boxElement_category'>" + "<span>" + c[e].category + " "
					+ "</span><input class='pull-right' type='checkbox' id='cbx_category" + c[e].categoryId + "'></div>";
					
					$("#categoryDiv").append(c_divRow);
				}
	});
};

$("#btn_new").click(function() {
	$("#modal_title_text").text("Neue Organisation");
	
	//hide alert messsage
	$("#newAlertForm").hide();
	
	//clear textboxes
	$("#tbx_id").val("");
	$("#tbx_type").val("");
	$("#tbx_name").val("");
	$("#tbx_description").val("");
	$("#tbx_address").val("");
	$("#tbx_zip").val("");
	$("#tbx_city").val("");
	$("#tbx_country").val("");
	
	clearAndLoadDivContainer();
});

$("#btn_edit").click(function() {
	$("#modal_title_text").text("Bearbeite Organisation");
	
	//hide alert messsage
	$("#newAlertForm").hide();
	
	var id = tableData[0];
	
	//load textboxes
	$("#tbx_id").val(o[id].id);
	$("#tbx_name").val(o[id].name);
	$("#tbx_address").val(o[id].address);
	$("#tbx_zip").val(o[id].zip);
	$("#tbx_city").val(o[id].city);
	$("#tbx_country").val("Österreich");
	$("#tbx_description").val(o[id].comment);
	
	clearAndLoadDivContainer();
	
	//set contact person checkboxes
	var personIds = o[id].personIds
	for(var i=0; i<personIds.length; i++){
		var help = "#cbx_contactperson" + personIds[i];
		$(help).prop('checked', true);
	}
	
	//set category checkboxes
	var categoryIds = o[id].categoryIds;
	for(var i=0; i<categoryIds.length; i++){
		var help = "#cbx_category" + categoryIds[i];
		$(help).prop('checked', true);
	}
});

//load contact persons for table
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

//load organisation table
var o;
var c;
function loadTableContent() {
	var category;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/category/getAllCategories"
	}).done(
			function(data) {
				c = eval(data);
	});
	
	$.ajax({
		type : "POST",
		url : "../rest/secure/organisation/getAllOrganisations"
	}).done(
			function(data) {
				o = eval(data);
				
				for (var e in o) {
					var types = o[e].types;
					var typeString = "";
					
					var categories = o[e].categoryIds;
					var categoryString = "";
					
					var personIds = o[e].personIds;
					var personIdString = "";
					
					for (var k = 0; k < types.length; k++) {
						typeString = typeString + types[k];
						if (k < types.length - 1) {
							typeString = typeString + ", ";
						}
					}
					
					for (var k = 0; k < categories.length; k++) {
						categoryString = categoryString + c[categories[k]-1].category;
						if (k < categories.length - 1) {
							categoryString = categoryString + "," + "<br/>";
						}
					}
					
					for (var k = 0; k < personIds.length; k++) {
						var help = loadContactPerson(personIds[k]);
						personIdString = personIdString + help;
						if (k < personIds.length - 1) {
							personIdString = personIdString + "," + "<br/>";
						}
					}
					
					var tableRow = "<tr>" + 
								"<td>" + o[e].id + "</td>"
								+ "<td>" + o[e].name + "</td>"
								+ "<td>" + personIdString + "</td>" 
								+ "<td>" + o[e].address + "," + "<br/>" + o[e].zip + " "
								+ o[e].city + "," + "<br/>" + "Österreich" + "</td>"	//TODO country missing in json object
								+ "<td>" + typeString + "</td>" 
								+ "<td>" + categoryString + "</td>"
								+ "<td>" + o[e].comment + "</td>" +	"</tr>";
								
					$("#organisationTableBody").append(tableRow);
				}
			});
};

//Get all organisations and load into table
$(document).ready(loadTableContent());

//save person
$("#btn_saveorganisation").click(function() {
	if($("#tbx_name").val() == "" || $("#tbx_address").val() == "" || $("#tbx_zip").val() == "" 
		|| $("#tbx_city").val() == "" || $("#tbx_country").val() == "")
	{
			$("#newAlertForm").show();
			return;
	}
	
	var neworganisation = new Object();
	
	neworganisation.id = $("#tbx_id").val();
	neworganisation.name = $("#tbx_name").val();
	neworganisation.comment = $("#tbx_comment").val();
	neworganisation.address = $("#tbx_address").val();
	neworganisation.zip = $("#tbx_zip").val();
	neworganisation.city = $("#tbx_city").val();
	
	//Set by server
	neworganisation.updateTimestamp = "";
	neworganisation.lastEditor = "";
	
	//check if contact person checkboxes are checked
	var contactPersonArray = [];
	for(var i=1; i<=p.length; i++){
		var help = "#cbx_contactperson" + i;
		if($(help).prop('checked')){
			contactPersonArray.push(i);
		}
	}
	neworganisation.personIds = contactPersonArray;
	
	//check if category checkboxes are checked
	var categoryArray = [];
	for(var i=1; i<=c.length; i++){
		var help = "#cbx_category" + i;
		if($(help).prop('checked')){
			categoryArray.push(c[i-1].categoryId);
		}
	}
	neworganisation.categories = categoryArray;
	
	//checking if type checkboxes checked
	var typesArray = [];
	if($('#cbx_lieferant').prop('checked')){
		typesArray.push("Lieferant");
	}
	if($('#cbx_kunde').prop('checked')){
		typesArray.push("Kunde");
	}
	if($('#cbx_sponsor').prop('checked')){
		typesArray.push("Sponsor");
	}
	neworganisation.types = typesArray;
	
	$.ajax({
		headers : {
			'Accept' : 'application/json',
			'Content-Type' : 'application/json'
		},
		type : "POST",
		url : "../rest/secure/organisation/setOrganisation", //TODO set organisation
		contentType: "application/json; charset=utf-8",
	    dataType: "json",
		data : JSON.stringify(neworganisation)
	}).done(function(data) {
		if (data) {
			$('#organisationTableBody').empty();
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

//load details modal
$("#btn_details").click(function() {
	//remove container
	$(".details").remove();
	$(".persondivider").remove();
	
	var id = tableData[0];
	
	$("#label_name_details").text(o[id].name);
	$("#label_address_details").text(o[id].address);
	$("#label_zip_details").text(o[id].zip);
	$("#label_city_details").text(o[id].city);
	$("#label_country_details").text(o[id].country);
	$("#label_lastEditor_details").text(o[id].lastEditor);
	$("#label_updateTimestamp_details").text(o[id].updateTimestamp);
	
	if(o[id].comment == ""){
		$("#label_comment_details").text("-");
	}
	else{
		$("#label_comment_details").text(o[id].comment);
	}
	
	//load types
	var types = o[id].types;
	if(types.length == 0){
		$("#label_types_details").text("-");
	}
	else{
		var typeString = "";
		$("#label_types_details").text(types[0]);
		for (var j = 1; j < types.length; j++) {
			typeString = types[j];
			var template = "<div class='row'><div class='col-md-6'></div><div class='col-md-6'><label>" + typeString + "</label></div></div>";
			$("#type_container_details").append(template);
		}
	}
	
	//load categories
	var categories = o[id].categoryIds;
	if(categories.length == 0){
		$("#label_categories_details").text("-");
	}
	else{
		var categoryString = "";
		$("#label_categories_details").text(c[categories[0]-1].category);
		$("#category_container_details").show();
		for (var k = 1; k < categories.length; k++) {
			categoryString = c[categories[k]-1].category;
			var template = "<div class='row details'><div class='col-md-6'></div><div class='col-md-6'><label>" + categoryString + "</label></div></div>";
			$("#category_container_details").append(template);
		}
	}
	
	//load personIds
	var persons = o[id].personIds;
	if(persons.length == 0){
		$("#label_personIds_details").text("-");
	}
	else{
		var personString = "";
		for (var j = 0; j < persons.length; j++) {
			loadContactPerson(persons[j]);
			
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
	}
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

//modal search filter 1
$(document).ready(function() {
	(function($) {
		$('#filter_modal1').keyup(function() {

			var rex = new RegExp($(this).val(), 'i');
			$('.searchable .boxElement_person').hide();
			$('.searchable .boxElement_person').filter(function() {
				return rex.test($(this).text());
			}).show();
		})
	}(jQuery));
});

//modal search filter 2
$(document).ready(function() {
	(function($) {
		$('#filter_modal2').keyup(function() {

			var rex = new RegExp($(this).val(), 'i');
			$('.searchable .boxElement_kategorie').hide();
			$('.searchable .boxElement_kategorie').filter(function() {
				return rex.test($(this).text());
			}).show();
		})
	}(jQuery));
});

//typefilter
$(document).ready(function() {
	$('#lieferanten_cbx').prop('checked', true);
	$('#kunden_cbx').prop('checked', true);
	$('#sponsoren_cbx').prop('checked', true);

	(function($) {
		$('#lieferanten_cbx').on('change', function() {
			if (this.checked) {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(4).text() >= "Lieferant"
				}).show();
			} else {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(4).text() >= "Lieferant"
				}).hide();
			}
		});
		$('#kunden_cbx').on('change', function() {
			if (this.checked) {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(4).text() == "Kunde"
				}).show();
			} else {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(4).text() == "Kunde"
				}).hide();
			}
		});
		$('#sponsoren_cbx').on('change', function() {
			if (this.checked) {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(4).text() == "Sponsor"
				}).show();
			} else {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(4).text() == "Sponsor"
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
$('#btn_details').prop('disabled', true);

//get current user rights
$(document).ready(function() {
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getCurrentUser"
	}).done(function(data) {
		currentUser = eval(data);
		currentUserRights = currentUser.permission;
		
		//only when user has readwrite/admin rights
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
$('#TableHead').on('click', 'tbody tr', function(event) {
	tableData = $(this).children("td").map(function() {
		return $(this).text();
	}).get();

	$(this).addClass('highlight').siblings().removeClass('highlight');
	
	//only when user has admin rights
	if(currentUserRights == "Admin" && currentUserRights != ""){
		$('#btn_edit').prop('disabled', false);
		$('#btn_deleteModal').prop('disabled', false);
	}
	else{
		$('#btn_edit').prop('disabled', true);
		$('#btn_deleteModal').prop('disabled', true);
	}
	$('#btn_details').prop('disabled', false);
});

//remove table row modal
$("#btn_deleteModal").click(function() {
	var id = tableData[0];
	
	$("#label_name").text(o[id].name);
	$("#label_address").text(o[id].address);
});

//TODO delete organisation
$("#btn_deleteOrganisation").click(function() {
	var id = tableData[0];
	
	$.ajax({
		type : "POST",
		url : "../rest/secure/organisation/deleteOrganisationById/" + id
	}).done(function(data) {
		$('#organisationTableBody').empty();
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
