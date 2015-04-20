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
		url : "../rest/secure/person/reducedData/getAllActive"
	}).done(
			function(data) {
				var contactPersons = data;	// return data is already JSON
				
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
	
	// call the sorting function when a contact person checkbox change its status
	$('.boxElement_person > input:checkbox').change( sortContactPersonBoxElements );
	$('.boxElement_category > input:checkbox').change( sortCategoryBoxElements );
	
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
				c = data;	// return data is already JSON
				
				for (var e in c) {
					var c_divRow = "<div class='boxElement_category'>" + "<input type='hidden' value="+ c[e].categoryId +">" + "<span>" + c[e].category + " "
					+ "</span><input class='pull-right' type='checkbox'></div>";
					
					$("#categoryDiv").append(c_divRow);
				}
	});
};


/**
 * call modal for a new organisation
 */
$("#btn_new").click(function() {
	
	// show loading spinner
	showLoadingSpinner(true);
	
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
	
	// hide loading spinner
	showLoadingSpinner(false);
});



/**
 * call modal for editing an organisation
 */
$("#btn_edit").click(function() {
	
	// show loading spinner
	showLoadingSpinner(true);
	
	$("#modal_title_text").text("Bearbeite Organisation");
	
	// clear filter boxes
	$('#filter_modal1').val("");
	$('#filter_modal2').val("");
	
	// hide alert messsage
	$("#newAlertForm").hide();
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Keine Organisation auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
	$.ajax({
		type : "POST",
		url : "../rest/secure/organisation/getOrganisationById/" + id
	}).done(function(data) {
		
		var org = data;		// return data is already JSON
		
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
		
		
		// call the sorting functions for contact persons and categories one time to initially sort them
		sortContactPersonBoxElements();
		sortCategoryBoxElements();
		
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
		
		// hide loading spinner
		showLoadingSpinner(false);
		
		// show modal
		$('#new').modal('show');
		
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
				contactPerson = data;	// return data is already JSON
				nameString = contactPerson.lastName + " " + contactPerson.firstName;
			});
	return nameString;
};

//load organisation table
var o;
var c;
function loadTableContent() {
	
	// show loading spinner
	showLoadingSpinner(true);
	
	$('#lieferanten_cbx').prop('checked', true);
	$('#kunden_cbx').prop('checked', true);
	$('#sponsoren_cbx').prop('checked', true);
	$('#keinTyp_cbx').prop('checked', true);
	
	// load all Categories
	var category;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/category/getAllCategories"
	}).done(
			function(data) {
				c = data;	// already JSON
	});
	
	// load all Persons for ContactPersons
	var allPersons;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/person/reducedData/getAllActive"
	}).done(
			function(data) {
				allPersons = data;	// already JSON
	});
	
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getAllActiveOrganisations"
	}).done(
			function(data) {
				o = data;	// already JSON
				
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
								personIdString = personIdString + allPersons[i].salutation + " " + allPersons[i].lastName + " " + allPersons[i].firstName;
								if (k < personIds.length - 1) {
									personIdString = personIdString + "," + "<br/>";
								}
							}
						}
					}
					
					// create addressString according to available address variables
					var addressString = "";
					if (o[e].address.length > 0)
						addressString += o[e].address;
					if (o[e].city.length > 0)
					{
						if (addressString.length > 0)
							addressString += ", ";
						addressString += o[e].zip + " " + o[e].city;
					}
					if (o[e].country.length > 0)
					{
						if (addressString.length > 0)
							addressString += ", ";
						addressString += o[e].country;
					}
					
					var tableRow = "<tr>" + 
								"<td class='hidden'>" + o[e].id + "</td>"
								+ "<td>" + o[e].name + "</td>"
								+ "<td>" + personIdString + "</td>" 
								+ "<td>" + addressString + "</td>"
								+ "<td>" + typeString + "</td>" 
								+ "<td>" + categoryString + "</td>"
								+ "<td>" + o[e].comment + "</td>" +	"</tr>";
								
					$("#organisationTableBody").append(tableRow);
				}
				
				// hide loading spinner
				showLoadingSpinner(false);
				
				// update organisation count label
				updateOrganisationCountLabel();
			});
};



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
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Keine Organisation auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
	$.ajax({
		type : "POST",
		url : "../rest/secure/organisation/getOrganisationById/" + id
	}).done(function(data) {
		
		var org = data;	// already JSON
		
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
					allCategories = data;	// already JSON
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
			$("#label_personIds_details").text("");
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
		
		// show modal
		$('#details').modal('show');
		
	});
	
	
});



/**
 * sorting function for contact persons in organisation modal, 
 * sorts the contact persons according to their checkbox status and their name
 */
function sortContactPersonBoxElements()
{

	var sortedElements = $('#contactPersonDiv').children().sort( function(a, b) {
		
				if( $(a).find('input').last().prop('checked') == false && $(b).find('input').last().prop('checked') == true )
				{
					return 1;	// switch
				}
				
				if( $(a).find('input').last().prop('checked') == true && $(b).find('input').last().prop('checked') == false )
				{
					return -1;	// don't switch
				}
				
				// if both have the same status
				if( $(a).find('input').last().prop('checked') == $(b).find('input').last().prop('checked') )
				{
					
					// if first text is greater than second text
					if( $(a).text().toLowerCase() > $(b).text().toLowerCase() )
					{
						return 1;	// switch
					}
					
					// if first text is less than second text
					if( $(a).text().toLowerCase() < $(b).text().toLowerCase() )
					{
						return -1;	// don't switch
					}
					
					return 0;
					
					
				}
				
				return 0;
				
		
			} );

	// rearrange the elements in the container
	for( i=0; i<sortedElements.length; i++)
	{
		$('#contactPersonDiv').append( $(sortedElements[i]));
	}

}


/**
 * sorting function for categories in organisation modal, 
 * sorts the categories according to their checkbox status and their name
 */
function sortCategoryBoxElements()
{

	var sortedElements = $('#categoryDiv').children().sort( function(a, b) {
		
				if( $(a).find('input').last().prop('checked') == false && $(b).find('input').last().prop('checked') == true )
				{
					return 1;	// switch
				}
				
				if( $(a).find('input').last().prop('checked') == true && $(b).find('input').last().prop('checked') == false )
				{
					return -1;	// don't switch
				}
				
				// if both have the same status
				if( $(a).find('input').last().prop('checked') == $(b).find('input').last().prop('checked') )
				{
					
					// if first text is greater than second text
					if( $(a).text().toLowerCase() > $(b).text().toLowerCase() )
					{
						return 1;	// switch
					}
					
					// if first text is less than second text
					if( $(a).text().toLowerCase() < $(b).text().toLowerCase() )
					{
						return -1;	// don't switch
					}
					
					return 0;
					
					
				}
				
				return 0;
				
		
			} );

	// rearrange the elements in the container
	for( i=0; i<sortedElements.length; i++)
	{
		$('#categoryDiv').append( $(sortedElements[i]));
	}

}


// function for updating the organisation count label right above the table
function updateOrganisationCountLabel()
{
	var count = $('#organisationTableBody').children(':visible').length;
	
	$('#lbl_organisation_count').text(count+" Organisationen");

}


/**
 * search filter for the table of all organisations
 */
$(document).ready(function() {
	(function($) {
		$('#filter').keyup(function() {

			// check all checkboxes
			$('#lieferanten_cbx').prop('checked', true);
			$('#kunden_cbx').prop('checked', true);
			$('#sponsoren_cbx').prop('checked', true);
			$('#keinTyp_cbx').prop('checked', true);
			
			var rex = new RegExp($(this).val(), 'i');
			$('.searchable tr').hide();
			$('.searchable tr').filter(function() {
				var text = $(this).children().map(function(i,opt) {
					return $(opt).text();
				}).toArray().join(',');
				
				return rex.test(text);
			}).show();
			
			// update organisation count label
			updateOrganisationCountLabel();
		})
	}(jQuery));
});



/**
 * search filter for contactPersons in organisation modal
 */
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



/**
 * search filter for categories in organisation modal
 */
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




/**
 * filter function, which updates the table according to the checkbox status
 */
function updateTableTypeFilter() {
	
	// remove textfilter
	$('#filter').val('');
	
	// get checkbox states
	var lieferantenChecked = $('#lieferanten_cbx').prop('checked');
	var kundenChecked = $('#kunden_cbx').prop('checked'); 
	var sponsorenChecked = $('#sponsoren_cbx').prop('checked');
	var keinTypChecked = $('#keinTyp_cbx').prop('checked');
	
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
	
	// update organisation count label
	updateOrganisationCountLabel();

}



/**
 * assign the filter function calls to the checkboxes
 */
$(document).ready(function() {
	$('#lieferanten_cbx').prop('checked', true);
	$('#kunden_cbx').prop('checked', true);
	$('#sponsoren_cbx').prop('checked', true);
	$('#keinTyp_cbx').prop('checked', true);

	// call function updateTableTypeFilter when checkbox states are changed
	$('#lieferanten_cbx').on('change', updateTableTypeFilter);
	$('#kunden_cbx').on('change', updateTableTypeFilter);
	$('#sponsoren_cbx').on('change', updateTableTypeFilter);
	$('#keinTyp_cbx').on('change', updateTableTypeFilter);
});


/**
 * disable new, edit and delete buttons, and activate them according to the user permissions
 */
$('#btn_new').hide();
$(".suchfilter").css("margin-left", "0px");
$('#btn_edit').hide();
$('#btn_deleteModal').hide();

// deactivate button on start
$('#btn_details').prop('disabled', true);

/**
 * Get current user permissions
 */
$(document).ready(function() {
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getCurrentUser"
	}).done(function(data) {
		currentUser = data;	// already JSON
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
		else
		{
			/**
			 * disable new, edit and delete buttons, and activate them according to the user permissions
			 */
			$('#btn_new').hide();
			$(".suchfilter").css("margin-left", "0px");
			$('#btn_edit').hide();
			$('#btn_deleteModal').hide();
			
			// deactivate button on start
			$('#btn_details').prop('disabled', true);
		}
		
	});
});



//Get all organisations and load into table
$(document).ready(function(){loadTableContent();});



//this function is used to get the selected row
//the function is called when a button is pressed and the selected entry has to be determined
function getSelectedRow(){
	
	// find selected tr in the table and map it to the variable
	var currentRow = $('#TableHead').find('tr.highlight').first().children("td").map(function() {
		return $(this).text();
	}).get();
	
	return currentRow;
}

/**
 * write the selected row data to a global variable 
 */
$('#TableHead').on('click', 'tbody tr', function(event) {
//	tableData = $(this).children("td").map(function() {
//		return $(this).text();
//	}).get();

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


/**
 * Call the delete modal
 */
$("#btn_deleteModal").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Keine Organisation auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
	$.ajax({
		type : "POST",
		url : "../rest/secure/organisation/getOrganisationById/" + id
	}).done(function(data) {
	
		var o = data;	// already JSON 
		
		var typeString = "";
		
		for (i in o.types)
		{
			typeString = typeString + o.types[i];
			if (i<o.types.length-1)
				typeString += ', ';
		}
		
		$("#label_name_delete").text(o.name);
		$("#label_address_delete").text(o.address + ", " + o.zip + " " + o.city + ", " + o.country);
		$("#label_types_delete").text(typeString);
		
		// show the modal
		$('#deleteModal').modal('show');
	});
	
});

/**
 * AJAX call to delete an organisation
 */
$("#btn_deleteOrganisation").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Keine Organisation auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
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


//export all persons as csv
$('#btn_export').click(function(){
	// submit form on click
	$('#formOrganisationExport').submit();
});


//export current view as CSV
$('#btn_exportCurrentView').click(function(){
	
	var tableData = [];
	
	var headerString = $('#TableHead .TableHead tr').children().map(
			function(){
				return $(this).text();
			}).get().join(';');
	tableData.push(headerString);
	
	// only visible rows
	// concat columns with separator ';' for each row and push it into tableData
	$('#organisationTableBody tr:visible').each(function(){
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
	
	// start encoding and download
	// code by https://github.com/b4stien/js-csv-encoding
	var csvContent = csvString,
    	textEncoder = new CustomTextEncoder('windows-1252', {NONSTANDARD_allowLegacyEncoding: true}),
    	fileName = 'Organisations-Export.csv';

	// encode
	var csvContentEncoded = textEncoder.encode([csvContent]);
	// start download
	var blob = new Blob([csvContentEncoded], {type: 'text/csv;charset=windows-1252;'});
	saveAs(blob, fileName);
	
	// end download
	
});