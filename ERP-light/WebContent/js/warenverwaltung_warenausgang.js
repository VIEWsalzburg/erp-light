//append alert message to modal
var pwdError = "<div id='pwdErrorAlert'> <div class='col-sm-5'> <div class='alert alert-danger custom-alert' style='text-align: left;'>Leere Felder vorhanden!</div> </div>  </div>";
	$("#newAlertForm").append(pwdError);

//Get all outgoing deliveries and load into table
function loadTableContent(){
	
			// get all organisations
			var organisations;
			$.ajax({
				type : "POST",
				async : false,
				url : "../rest/secure/organisation/getAllOrganisations"
			}).done(function(data) {
				organisations = eval(data);
			});
	
			
			
			$.ajax({
				type : "POST",
				url : "../rest/secure/outgoingDelivery/getAll"
			}).done(function(data) {
						var out = eval(data);

						for (var e in out) {
							
							//get organisation by id
							var org = null;
							
							for (i in organisations)
							{
								if (organisations[i].id == out[e].organisationId)
								{
									org = organisations[i];
								}
							}
							
							var articles = out[e].outgoingArticleDTOs;
							// sort articles according to their articleNr
							articles.sort( function(a, b) { 
								return (a.articleNr - b.articleNr);
							} );
							
							//get article names
							var articleString = "";
							for(var i=0; i < articles.length; i++){
								articleString = articleString + articles[i].articleDTO.description;
								
								if(i < articles.length - 1){
									articleString = articleString + ", ";
								}
							}
							
							var tableRow = "<tr>" + "<td>" + out[e].outgoingDeliveryId
									+ "</td>" + "<td>" + org.name + ", " + "<br/>" + org.zip + " " + org.city + "," + "<br/>" + org.country 
									+ "</td>" + "<td>" + out[e].date
									+ "</td>" + "<td>" + articleString
									+ "</td>" + "<td>" + out[e].comment
									+ "</td>" + "</tr>";

							$("#outgoingDeliveryTableBody").append(tableRow);
						}
			});
};

//Get all outgoing deliveries and load into table
$(document).ready(loadTableContent());

//init collapse
$(function () {
	$('.collapse').collapse()
});

//switch to new outgoing deliveries tab
$("#btn_new").click(function() {
	//switch to new incoming deliveries with GET parameter mode=new and id=-1
	location.href="warenverwaltung_neuedisposition.html?mode=new&id=-1";
	return false;
});

//switch to edit ougoing deliveries tab
$("#btn_edit").click(function() {
	//switch to edit outgoing deliveries with GET parameter mode=edit and id=...
	location.href="warenverwaltung_neuedisposition.html?mode=edit&id=" + tableData[0];
	return false;
});

//load a single (contact)Person into a global variable
//function returns the name of a person
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

//load details modal
$("#btn_details").click(function() {
	//remove container
	$(".details").remove();
	$(".persondivider").remove();
	
	var id = tableData[0];
	
	//get outgoing delivery by id
	var out;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/outgoingDelivery/getById/" + id
	}).done(function(data) {
		out = eval(data);
	});
				
	//get organisation by id
	var org;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getOrganisationById/" + out.organisationId
	}).done(function(data) {
		org = eval(data);
	});
	
	$("#label_name_details").text(org.name);
	$("#label_address_details").text(org.address);
	$("#label_zip_details").text(org.zip);
	$("#label_city_details").text(org.city);
	$("#label_country_details").text(org.country);
		
	if(org.comment == ""){
		$("#label_comment_details").text("-");
	}
	else{
		$("#label_comment_details").text(org.comment);
	}
	
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
				
				if(j < contactPersonIds.length-1){
					//append divider
					$("#person_container_details").append("<div class='row divider-horizontal persondivider'></div>");
				}
			}
		} 	// end else
		
	
	//load date and comment
	$("#label_dateofdelivery_details").text(out.date);
	$("#label_description_details").text(out.comment);
	
	//load last editor and updateTimeStamp
	$("#label_lastEditor_details").text(loadContactPerson(out.lastEditorId));
	$("#label_updateTimestamp_details").text(out.updateTimestamp);
	

	
	var articles = out.outgoingArticleDTOs;
	// sort articles according to their articleNr
	articles.sort( function(a, b) { 
		return (a.articleNr - b.articleNr);
	} );
	
	//load articles
	var articleString = "";
	for(var i=0; i < out.outgoingArticleDTOs.length; i++){
		articleString = articles[i].articleDTO.description;
		createAndAppendArticleTemplate("Beschreibung", articleString);
		
		articleString = articles[i].numberpu;
		createAndAppendArticleTemplate("Anzahl der VE", articleString);
		
		articleString = articles[i].articleDTO.packagingUnit;
		createAndAppendArticleTemplate("VE", articleString);
		
		articleString = articles[i].articleDTO.weightpu; + " kg";
		createAndAppendArticleTemplate("Einzelgewicht der VE", articleString);
		
		articleString = articles[i].articleDTO.mdd;
		createAndAppendArticleTemplate("Mindesthaltbarkeitsdatum", articleString);
		
		articleString = articles[i].articleDTO.pricepu + " €";
		createAndAppendArticleTemplate("Einzelpreis der VE", articleString);
		
		var pricepu = parseFloat(articles[i].articleDTO.pricepu);
		var sum = Math.round((pricepu * articles[i].numberpu)*100)/100;
		createAndAppendArticleTemplate("Gesamtpreis", sum + " €");
		
		if(i < out.outgoingArticleDTOs.length-1){
			//append divider
			$("#article_container_details").append("<div class='row divider-horizontal persondivider'></div>");
		}
	}
	
});

function createAndAppendArticleTemplate(name, value){
	var template = "<div class='row details'><div class='col-md-6'><label>"+ name +"</label></div><div class='col-md-6'><label>" + value + "</label></div></div>";
	$("#article_container_details").append(template);
}

// search filter
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

// disable new, edit and delete buttons
$('#btn_new').hide();
$(".suchfilter").css("margin-left", "0px");
$('#btn_edit').hide();
$('#btn_deleteModal').hide();

// get current user rights
$(document).ready(function() {
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getCurrentUser"
	}).done(function(data) {
		currentUser = eval(data);
		currentUserRights = currentUser.permission;

		// only when user has admin rights
		if (currentUserRights == "Admin" && currentUserRights != "") {
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
$('#TableHead').on('click','tbody tr', function(event) {
			tableData = $(this).children("td").map(function() {
				return $(this).text();
			}).get();

			$(this).addClass('highlight').siblings().removeClass('highlight');

			// only when user has admin rights
			if (currentUserRights == "Admin" && currentUserRights != "") {
				$('#btn_edit').prop('disabled', false);
				$('#btn_deleteModal').prop('disabled', false);
			} 
			else {
				$('#btn_edit').prop('disabled', true);
				$('#btn_deleteModal').prop('disabled', true);
			}
});

/**
 * Call the delete modal for the selected outgoing delivery
 */
$("#btn_deleteModal").click(function() {
	var id = tableData[0];
	
	//get incoming delivery
	var out;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/outgoingDelivery/getById/" + id
	}).done(function(data) {
			out = eval(data);
	});
				
	//get organisation by id
	var org;
	var organisationString;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getOrganisationById/" + out.organisationId
	}).done(function(data) {
		org = eval(data);
		organisationString = org.name + ", " + org.zip + " " + org.city + ", " + org.country
	});
					
	//get articles
	var articleString = "";
	var article = out.outgoingArticleDTOs;
	for(var i=0; i < out.outgoingArticleDTOs.length; i++){
		articleString = articleString + article[i].articleDTO.description;
				
		if(i < out.outgoingArticleDTOs.length - 1){
			articleString = articleString + ", ";
		}
	}
	
	$("#label_receiver_delete").text(organisationString); 
	$("#label_article_delete").text(articleString);
});


/**
 * TODO call the delete url for the outgoing delivery
 */
$("#btn_deleteOutgoingDelivery").click(function() {
	var id = tableData[0];
	
	 $.ajax({
		 type : "POST",
		 url : "../rest/secure/outgoingDelivery/deleteById/" + id
	 }).done(function(data) {
		 $('#outgoingDeliveryTableBody').empty();
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
