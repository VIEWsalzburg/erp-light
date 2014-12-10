//Load pageheader
$("#pageheader").load("../partials/header.html", function() {
	$("#adressverwaltung_nav").addClass("active");
});

//load contact persons to modal
function loadAllContactPersons() {
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getAll"
	}).done(
			function(data) {
				var p = eval(data);
				
				for (var e in p) {
					var phoneString = "";
					var phoneNumbers = p[e].telephones;
					
					for (var j = 0; j < phoneNumbers.length; j++) {
						phoneString = phoneString + phoneNumbers[j].telephone;
						if (j < phoneNumbers.length - 1) {
							phoneString = phoneString + ", ";
						}
					}
					
					var divRow = "<div class='boxElement_person'>" + "<span>" + p[e].personId + " " + p[e].firstName + " " + p[e].lastName
								+ " " + phoneString + " " + "</span><input type='checkbox' id='cbx_contactperson" + p[e].personId + "'></div>";
					
					$("#contactPersonDiv").append(divRow);
				}
			});
};

//load categories to modal (unfinished)
function loadAllCategories() {
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getAll"
	}).done(
			function(data) {
				var p = eval(data);
				
				for (var e in p) {
					
					var divRow = "<div class='boxElement_category'>" + "<span>" + p[e].personId + " " + p[e].firstName  + " " 
					+ "</span><input type='checkbox' id='cbx_contactperson" + p[e].personId + "'></div>";
					
					$("#categoryDiv").append(divRow);
				}
			});
};

$("#btn_new").click(function() {
	$("#modal_title_text").text("Neue Organisation");
	
	//clear textboxes
	$("#tbx_type").val("");
	$("#tbx_name").val("");
	$("#tbx_description").val("");
	$("#tbx_address").val("");
	$("#tbx_zip").val("");
	$("#tbx_city").val("");
	$("#tbx_country").val("");
	
	loadAllContactPersons();
	//loadAllCategories();
});

$("#btn_edit").click(function() {
	$("#modal_title_text").text("Bearbeite Organisation");
	
	//load textboxes
	$("#tbx_type").val(tableData[1]);
	$("#tbx_name").val(tableData[2]);
	$("#tbx_description").val(tableData[3]);
	$("#tbx_address").val(tableData[4]);
	$("#tbx_zip").val(tableData[5]);
	$("#tbx_city").val(tableData[6]);
	$("#tbx_country").val("Österreich");
	
	loadAllContactPersons();
	//loadAllCategories();
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
				nameString = p.firstName + " " + p.lastName;
			});
	return nameString;

};

//TODO load organisation table
function loadTableContent() {
	$.ajax({
		type : "POST",
		url : "../rest/secure/organisation/getAllOrganisations"
	}).done(
			function(data) {
				var o = eval(data);
				
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
							categoryString = categoryString + ", ";
						}
					}
					
					for (var k = 0; k < personIds.length; k++) {
						var help = loadContactPerson(personIds[k]);
						personIdString = personIdString + help;
						if (k < personIds.length - 1) {
							personIdString = personIdString + ", ";
						}
					}
					
					var tableRow = "<tr>" + 
								"<td>" + o[e].id + "</td>"
								+ "<td>" + typeString + "</td>" 
								+ "<td>" + o[e].name + "</td>"
								+ "<td>" + o[e].comment + "</td>"
								+ "<td>" + o[e].address + "</td>" 
								+ "<td>" + o[e].zip + "</td>"
								+ "<td>" + o[e].city + "</td>"
								+ "<td>" + personIdString + "</td>" 
								+ "<td>" + categoryString + "</td>"
								+ "<td>" + o[e].updateTimestamp + "</td>"
								+ "<td>" + o[e].lastEditor + "</td>" +	"</tr>";
							
					$("#organisationTableBody").append(tableRow);
				}
			});
};

//Get all organisations and load into table
$(document).ready(loadTableContent());

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
	if(currentUserRights == "Admin" && currentUserRights != "" && currentUser.personId != tableData[0]){
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
	/*
	var id = tableData[0];
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/deletePersonById/" + id
	}).done(function(data) {
		$('#personTableBody').empty();
		$('#deleteModal').modal('hide');
		loadTableContent();
	});
	*/
});
