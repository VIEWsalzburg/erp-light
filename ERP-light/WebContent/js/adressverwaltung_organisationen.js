//Load pageheader
$("#pageheader").load("../partials/header.html", function() {
	$("#adressverwaltung_nav").addClass("active");
});

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
	
	//set category checkboxes	TODO category id
	var categories = o[id].categories;
	for(var i=0; i<categories.length; i++){
		var help = "#cbx_category" + 1;	//categories[i];
		$(help).prop('checked', true);
	}
});

//load contact persons for table
function loadContactPerson(id) {
	var nameString="";
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/person/getPersonById/" + id
	}).done(function(data) {
				var p = eval(data);
				nameString = p.lastName + " " + p.firstName;
			});
	return nameString;
};

//load organisation table
var o;
function loadTableContent() {
	$.ajax({
		type : "POST",
		url : "../rest/secure/organisation/getAllOrganisations"
	}).done(
			function(data) {
				o = eval(data);
				
				for (var e in o) {
					var types = o[e].types;
					var typeString = "";
					
					var categories = o[e].categories;
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
						categoryString = categoryString + categories[k];
						if (k < categories.length - 1) {
							categoryString = categoryString + ", " + "<br/>";
						}
					}
					
					for (var k = 0; k < personIds.length; k++) {
						var help = loadContactPerson(personIds[k]);
						personIdString = personIdString + help;
						if (k < personIds.length - 1) {
							personIdString = personIdString + ", " + "<br/>";
						}
					}
					
					var tableRow = "<tr>" + 
								"<td>" + o[e].id + "</td>"
								+ "<td>" + o[e].name + "</td>"
								+ "<td>" + personIdString + "</td>" 
								+ "<td>" + "Bundestraßeeeee 102" + ", " + "<br/>" + "5020" + " "
								+ "Salzburg" + ", " + "<br/>" + "Österreich" + "</td>"	//TOD country missing in json object
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
			categoryArray.push(c[i-1].category);
		}
	}
	neworganisation.categories = categoryArray;
	
	//checking if type checkboxes checked
	var typesArray = [];
//	if($('#cbx_lieferant').prop('checked')){
//		typesArray.push("Lieferant");
//	}
//	if($('#cbx_kunde').prop('checked')){
//		typesArray.push("Kunde");
//	}
//	if($('#cbx_sponsor').prop('checked')){
//		typesArray.push("Sponsor");
//	}
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
					return $(this).find('td').eq(1).text() >= "Lieferant"
				}).show();
			} else {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(1).text() >= "Lieferant"
				}).hide();
			}
		});
		$('#kunden_cbx').on('change', function() {
			if (this.checked) {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(1).text() == "Kunde"
				}).show();
			} else {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(1).text() == "Kunde"
				}).hide();
			}
		});
		$('#sponsoren_cbx').on('change', function() {
			if (this.checked) {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(1).text() == "Sponsor"
				}).show();
			} else {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(1).text() == "Sponsor"
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
$('#personen').on('click', 'tbody tr', function(event) {
	tableData = $(this).children("td").map(function() {
		return $(this).text();
	}).get();

	$(this).addClass('highlight').siblings().removeClass('highlight');
	
	//only when user has admin rights
	if(currentUserRights == "Admin" && currentUserRights != ""){
		if(currentUserRights == "Admin"){
			$('#btn_edit').prop('disabled', false);
			$('#btn_deleteModal').prop('disabled', false);
		}
		else{
			if(tableData[9] != "Admin"){
				$('#btn_edit').prop('disabled', false);
				$('#btn_deleteModal').prop('disabled', false);
			}
		}
	}
	else{
		$('#btn_edit').prop('disabled', true);
		$('#btn_deleteModal').prop('disabled', true);
	}
});

//remove table row modal
$("#btn_deleteModal").click(function() {
	$("#label_id").text(tableData[0]);
	$("#label_type").text(tableData[1]);
	$("#label_name").text(tableData[2]);
	$("#label_address").text(tableData[4]);
});

//TODO delete organisation
$("#btn_deleteOrganisation").click(function() {
//	var id = tableData[0];
//	
//	$.ajax({
//		type : "POST",
//		url : "../rest/secure/organisation/deleteOrganisationById/" + id
//	}).done(function(data) {
//		$('#personTableBody').empty();
//		$('#deleteModal').modal('hide');
//		
//		if (data.success == true)
//			{
//				showAlertElement(1, data.message, 5000);
//			}
//		 else
//			{
//				showAlertElement(2, data.message, 5000);
//			}
//		 
//		 loadTableContent();
//	});
});
