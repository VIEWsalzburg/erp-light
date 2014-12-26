//Load pageheader
$("#pageheader").load("../partials/header.html", function() {
	$("#adressverwaltung_nav").addClass("active");
});

//append alert message to modal
var pwdError = "<div id='pwdErrorAlert'> <div class='col-sm-4'> <div class='alert alert-danger custom-alert' style='text-align: left;'>Leere Felder vorhanden!</div> </div>  </div>";
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
				var contactPersons = eval(data);
				
				for (var e in contactPersons) {
					var p_divRow = "<div class='boxElement_person'>" + "<input type='hidden' value="+
						contactPersons[e].personId +">" + "<span>" + contactPersons[e].lastName + " " + contactPersons[e].firstName
						+ " " + "</span><input class='pull-right' type='checkbox' ></div>";
					
					$("#contactPersonDiv").append(p_divRow);
				}
			});
};

// remove all contactPerson-checkboxes and all category-checkboxes from the new/edit-modal
// and load them again from the database
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
					var c_divRow = "<div class='boxElement_category'>" + "<input type='hidden' value="+ c[e].categoryId +">" + "<span>" + c[e].category + " "
					+ "</span><input class='pull-right' type='checkbox'></div>";
					
					$("#categoryDiv").append(c_divRow);
				}
	});
};

$("#btn_new").click(function() {
	$("#modal_title_text").text("Neue Organisation");
	
	// hide alert messsage
	$("#newAlertForm").hide();
	
	// clear textboxes
	$("#tbx_id").val("");
	$("#tbx_type").val("");
	$("#tbx_name").val("");
	$("#tbx_description").val("");
	$("#tbx_address").val("");
	$("#tbx_zip").val("");
	$("#tbx_city").val("");
	$("#tbx_country").val("");
	$("#tbx_comment").val("");
	
	// clear filter boxes
	$('#filter_modal1').val("");
	$('#filter_modal2').val("");
	
	// untick Checkboxes
	$('#cbx_lieferant').prop('checked',false);
	$('#cbx_kunde').prop('checked',false);
	$('#cbx_sponsor').prop('checked',false);
	
	// load all available categories and all available persons to modal
	clearAndLoadDivContainer();
});



$("#btn_edit").click(function() {
	$("#modal_title_text").text("Bearbeite Organisation");
	
	// clear filter boxes
	$('#filter_modal1').val("");
	$('#filter_modal2').val("");
	
	// hide alert messsage
	$("#newAlertForm").hide();
	
	var id = tableData[0];
	
	$.ajax({
		type : "POST",
		url : "../rest/secure/organisation/getOrganisationById/" + id
	}).done(function(data) {
		
		var org = eval(data);
		
		// load textboxes
		$("#tbx_id").val(org.id);
		$("#tbx_name").val(org.name);
		$("#tbx_address").val(org.address);
		$("#tbx_zip").val(org.zip);
		$("#tbx_city").val(org.city);
		$("#tbx_country").val(org.country);
		$("#tbx_comment").val(org.comment);
		
		// load all available categories and all available persons to modal
		clearAndLoadDivContainer();
	
		// check all persons, which are currently contactPersons
		$('#contactPersonDiv').children().each(
				function(){
					var inputs = $(this).children('input');
					var id = $(inputs).first().val();
					
					// iterate over all assigned contactPersons and check those with matching Ids
					var personIds = org.personIds;
					for (var i in personIds)
					{
						// check if Ids match, when they match => check checkbox
						if (id == personIds[i])
						{
							$(inputs).last().prop('checked', true);
						}
					}
					
				}
		);
	
	
		// check all categories, which are currently assigned
		$('#categoryDiv').children().each(
				function(){
					var inputs = $(this).children('input');
					var id = $(inputs).first().val();
					
					// iterate over all assigned contactPersons and check those with matching Ids
					var categoryIds = org.categoryIds;
					for (var i in categoryIds)
					{
						// check if Ids match, when they match => check checkbox
						if (id == categoryIds[i])
						{
							$(inputs).last().prop('checked', true);
						}
					}
					
				}
		);
		
		
		// uncheck all checkboxes first
		$('#cbx_lieferant').prop('checked',false);
		$('#cbx_kunde').prop('checked',false);
		$('#cbx_sponsor').prop('checked',false);
		
		
		var types = org.types;
		for (i in types)
		{
			if (types[i] == "Lieferant")
			{
				$('#cbx_lieferant').prop('checked',true);
			}
			
			if (types[i] == "Kunde")
			{
				$('#cbx_kunde').prop('checked',true);
			}
			
			if (types[i] == "Sponsor")
			{
				$('#cbx_sponsor').prop('checked',true);
			}
			
		}
	
	});	// end of ajax.done()
	
	
});	// end of click function





//load a single (contact)Person into a global variable
// function returns the name of a person
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
	
	
	// load all Categories
	var category;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/category/getAllCategories"
	}).done(
			function(data) {
				c = eval(data);
	});
	
	// load all Persons for ContactPersons
	var allPersons;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/person/getAll"
	}).done(
			function(data) {
				allPersons = eval(data);
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
					
					var categoryIds = o[e].categoryIds;
					var categoryString = "";
					
					var personIds = o[e].personIds;
					var personIdString = "";
					
					// append all Types to one String
					for (var k = 0; k < types.length; k++) {
						typeString = typeString + types[k];
						if (k < types.length - 1) {
							typeString = typeString + ", ";
						}
					}
					
					
					// read assigned categories from allCategories
					// IMPORTANT !!! CHECK FOR CORRECT CategoryID
					for (var k = 0; k < categoryIds.length; k++) {
						
						// iterate over all categories and append only assigned categories with the correct IDs
						// IMPORTANT for (i in c) => i is only the current index
						for (i in c)
						{
							
							// if assigned categoryId matches with categoryId from all Categories then append CategoryName to String
							if (categoryIds[k] == c[i].categoryId)
							{
								categoryString = categoryString + c[i].category;
								if (k < categoryIds.length - 1) {
									categoryString = categoryString + "," + "<br/>";
								}
							}	
						}
					}
					
					
					
					// read assigned contactPersons from allPersons
					// IMPORTANT !!! CHECK FOR CORRECT PersonID
					for (var k = 0; k < personIds.length; k++) {
						
						// iterate over all persons and append only assigned contactPersons with the correct IDs
						// IMPORTANT for (i in p) => i is only the current index
						for (i in allPersons)
						{
							if (personIds[k] == allPersons[i].personId)
							{
								personIdString = personIdString + allPersons[i].lastName + " " + allPersons[i].firstName;
								if (k < personIds.length - 1) {
									personIdString = personIdString + "," + "<br/>";
								}
							}
						}
					}
					
					var tableRow = "<tr>" + 
								"<td>" + o[e].id + "</td>"
								+ "<td>" + o[e].name + "</td>"
								+ "<td>" + personIdString + "</td>" 
								+ "<td>" + o[e].address + "," + "<br/>" + o[e].zip + " "
								+ o[e].city + "," + "<br/>" + o[e].country + "</td>"
								+ "<td>" + typeString + "</td>" 
								+ "<td>" + categoryString + "</td>"
								+ "<td>" + o[e].comment + "</td>" +	"</tr>";
								
					$("#organisationTableBody").append(tableRow);
				}
			});
};



//Get all organisations and load into table
$(document).ready(loadTableContent());



//save organisation
$("#btn_saveorganisation").click(function() {
	if($("#tbx_name").val() == "" )
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
	neworganisation.country = $("#tbx_country").val();
	
	//Set by server
	neworganisation.updateTimestamp = "";
	neworganisation.lastEditor = "";
	
	// get Ids of all checked Checkboxes of contactPersons
	var contactPersonIds = [];
	$('#contactPersonDiv').children().each(
			function(){
				var inputs = $(this).children('input');
				if ($(inputs).last().prop('checked'))
				{
					var id = $(inputs).first().val();
					contactPersonIds.push(id);
				}
			}
	);
	neworganisation.personIds = contactPersonIds;
	
	
	// get Ids of all checked Checkboxes of categories
	var categoryIds = [];
	$('#categoryDiv').children().each(
			function(){
				var inputs = $(this).children('input');
				if ($(inputs).last().prop('checked'))
				{
					var id = $(inputs).first().val();
					categoryIds.push(id);
				}
			}
	);
	neworganisation.categoryIds = categoryIds;
	
	
	//checking if type checkboxes are checked
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
		url : "../rest/secure/organisation/setOrganisation",
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
			
			// reload Table content
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
	
	$.ajax({
		type : "POST",
		url : "../rest/secure/organisation/getOrganisationById/" + id
	}).done(function(data) {
		
		var org = eval(data);
		
		$("#label_name_details").text(org.name);
		$("#label_address_details").text(org.address);
		$("#label_zip_details").text(org.zip);
		$("#label_city_details").text(org.city);
		$("#label_country_details").text(org.country);
		$("#label_lastEditor_details").text(org.lastEditor);
		$("#label_updateTimestamp_details").text(org.updateTimestamp);
		
		if(org.comment == ""){
			$("#label_comment_details").text("-");
		}
		else{
			$("#label_comment_details").text(org.comment);
		}
		
		//load types
		var types = org.types;
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
		
		
		
		
		
		// load all Categories to iterate over them and search for used Ids
		var allCategories;
		$.ajax({
			type : "POST",
			async : false,
			url : "../rest/secure/category/getAllCategories"
		}).done(
				function(data) {
					allCategories = eval(data);
		});
		
		
		//load categories
		var categoryIds = org.categoryIds;		
		if(categoryIds.length == 0){
			$("#label_categories_details").text("-");
		}
		else{
			var categoryString = "";
			
			// find first categoryId in allCategories
			for (i in allCategories)
			{
				if (categoryIds[0] == allCategories[i].categoryId)
				{
					$("#label_categories_details").text(allCategories[i].category);
				}
			}
			
			$("#category_container_details").show();
			for (var k = 1; k < categoryIds.length; k++) {
				
				for (i in allCategories)
				{
					if (categoryIds[k] == allCategories[i].categoryId)
					{
						categoryString = allCategories[i].category;
						var template = "<div class='row details'><div class='col-md-6'></div><div class='col-md-6'><label>" + categoryString + "</label></div></div>";
						$("#category_container_details").append(template); 
					}
				}
			}
		}	// end of load categories
		
		
		
		
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
	
	
});

//search filter main table
$(document).ready(function() {
	(function($) {
		$('#filter').keyup(function() {

			// check all checkboxes
			$('#lieferanten_cbx').prop('checked', true);
			$('#kunden_cbx').prop('checked', true);
			$('#sponsoren_cbx').prop('checked', true);
			
			var rex = new RegExp($(this).val(), 'i');
			$('.searchable tr').hide();
			$('.searchable tr').filter(function() {
				return rex.test($(this).text());
			}).show();
		})
	}(jQuery));
});

//modal search filter Person
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

//modal search filter Kategorie
$(document).ready(function() {
	(function($) {
		$('#filter_modal2').keyup(function() {

			var rex = new RegExp($(this).val(), 'i');
			$('.searchable .boxElement_category').hide();
			$('.searchable .boxElement_category').filter(function() {
				return rex.test($(this).text());
			}).show();
		})
	}(jQuery));
});


//show/hide Persons by Types
function updateTableTypeFilter() {
	
	// remove textfilter
	$('#filter').val('');
	
	// get checkbox states
	var lieferantenChecked = $('#lieferanten_cbx').prop('checked');
	var kundenChecked = $('#kunden_cbx').prop('checked'); 
	var sponsorenChecked = $('#sponsoren_cbx').prop('checked');
	
	$('.searchable tr').each( function() {
		
		// hide all by default
		var show = false;
		
		// get typeText
		var typeText = $(this).find('td').eq(4).text();
		
		// show all Lieferanten
		if (lieferantenChecked)
		{
			if (typeText.indexOf('Lieferant')!=-1)
				show = true;
		}
		
		// show all Kunden
		if (kundenChecked)
		{
			if (typeText.indexOf('Kunde')!=-1)
				show = true;
		}
		
		// show all Sponsore
		if (sponsorenChecked)
		{
			if (typeText.indexOf('Sponsor')!=-1)
				show = true;
		}
		
		// show all empty types
		if (typeText == '')
		{
			show = true;
		}
		
		if (show) {
			$(this).show();
		} else {
			$(this).hide();
		}
		
	} );

}



//typefilter
$(document).ready(function() {
	$('#lieferanten_cbx').prop('checked', true);
	$('#kunden_cbx').prop('checked', true);
	$('#sponsoren_cbx').prop('checked', true);

	// call function updateTableTypeFilter when checkbox states are changed
	$('#lieferanten_cbx').on('change', updateTableTypeFilter);
	$('#kunden_cbx').on('change', updateTableTypeFilter);
	$('#sponsoren_cbx').on('change', updateTableTypeFilter);
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
	
	$.ajax({
		type : "POST",
		url : "../rest/secure/organisation/getOrganisationById/" + id
	}).done(function(data) {
	
		var o = eval(data); 
		
		var typeString = "";
		
		for (i in o.types)
		{
			typeString = typeString + o.types[i];
			if (i<o.types.length-1)
				typeString += ', ';
		}
		
		$("#label_name").text(o.name);
		$("#label_address").text(o.address + ", " + o.zip + " " + o.city + ", " + o.country);
		$("#label_type").text(typeString);
		
	});
	
	
	// Get organisation with id "id"
	$.ajax({
		type : "POST",
		url : "../rest/secure/organisation/getOrganisationById/" + id
	}).done(function(data) {

		var deleteOrganisation = eval(data);
	
		$("#label_name_delete").text(deleteOrganisation.name);
		$("#label_address_delete").text(deleteOrganisation.address + ", " + deleteOrganisation.zip + " " + deleteOrganisation.city + ", " + deleteOrganisation.country);
	});
});

// delete organisation
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
