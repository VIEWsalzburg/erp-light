//Load pageheader
$("#pageheader").load("../partials/header.html", function() {
	$("#adressverwaltung_nav").addClass("active");
});

$("#btn_new").click(function() {
	$("#modal_title_text").text("Neue Organisation");
});

$("#btn_edit").click(function() {
	$("#modal_title_text").text("Bearbeite Organisation");
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
					
					for (var k = 0; k < types.length; k++) {
						typeString = typeString + types[k];
						if (k < types.length - 1) {
							typeString = typeString + ", ";
						}
					}
					
					var tableRow = "<tr>" + "<td>" + p[e].personId
							+ "</td>" + "<td>" + p[e].salutation
							+ "</td>" + "<td>" + p[e].title + "</td>"
							+ "<td>" + p[e].firstName + "</td>"
							+ "<td>" + p[e].lastName + "</td>" + "<td>"
							+ p[e].address + ", " + p[e].zip + " "
							+ p[e].city + ", " + p[e].country + "</td>"
							+ "<td>" + emailString + "</td>" + "<td>"
							+ phoneString + "</td>" + "<td>"
							+ p[e].updateTimestamp + "</td>" + "<td>"
							+ p[e].permission + "</td>" + "<td>"
							+ typeString + "</td>" + "<td>"	+ "</tr>";
					
					$("#organisationTableBody").append(tableRow);
				}
			});
};

//Get all organisations and load into table
//$(document).ready(loadTableContent());

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
