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

//TODO load organisation table
function loadTableContent() {
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getAll"
	}).done(
			function(data) {
				
				var p = eval(data);
				
				for (var e in p) {
					var types = p[e].types;
					var typeString = "";
					
					for (var k = 0; k < types.length; k++) {
						typeString = typeString + types[k];
						if (k < types.length - 1) {
							typeString = typeString + ", ";
						}
					}
					
					var tableRow = "<tr>" + 
								"<td>" + p[e].personId + "</td>" 
								+ "<td>" + "Lieferant" + "</td>" 
								+ "<td>" + "Musterfirma" + "</td>"
								+ "<td>" + "Nimmt kein Brot ab" + "</td>"
								+ "<td>" + p[e].address + "</td>" 
								+ "<td>" + p[e].zip + "</td>"
								+ "<td>" + p[e].city + "</td>"
								+ "<td>" + p[e].firstName + " " + p[e].lastName + "</td>" 
								+ "<td>" + "Kategorie1" + "</td>"
								+ "<td>" + p[e].updateTimestamp + "</td>"
								+ "<td>" + p[e].firstName + " " + p[e].lastName + "</td>" +	"</tr>";
							
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
$('#btn_new').prop('disabled', true);
$('#btn_edit').prop('disabled', true);
$('#btn_deleteModal').prop('disabled', true);

//get current user rights
$(document).ready(function() {
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getCurrentUser"
	}).done(function(data) {
		currentUser = eval(data);
		currentUserRights = currentUser.permission;
		
		//only when user has readwrite/admin rights
		if(currentUserRights != "Read" && currentUserRights != ""){
			$("#btn_new").prop('disabled', false);
		}
	});
});

var tableData;
$('#personen').on('click', 'tbody tr', function(event) {
	tableData = $(this).children("td").map(function() {
		return $(this).text();
	}).get();

	$(this).addClass('highlight').siblings().removeClass('highlight');
	
		//only when user has readwrite/admin rights
		if(currentUserRights != "Read" && currentUserRights != ""){
			$('#btn_edit').prop('disabled', false);
			$('#btn_deleteModal').prop('disabled', false);
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
