var telnumCount = 0;
var telnumelement_template = "<div class='row'> 		<div class='form-group'>															 			<div class='col-sm-5'> 				<input type='text' id='tbx_phoneNumber"
		+ telnumCount
		+ "' class='form-control tbx_telnr' 					placeholder='Telefonnr.'> 			</div> 			<div class='col-sm-4'> 				<select class='form-control' id='select_phoneNumber'> 					<option>Privat</option> 					<option>Gesch&auml;ftlich</option> 				</select> 			</div> 			<div class='col-sm-3'> 				<button type='button' class='btn btn-default removephonenumber_btn' 					id='btn_delete' >L&ouml;schen</button> 			</div> 		</div> 	</div>";

var mailadressCount = 0;
var mailadress_template = "<div class='row'> 		<div class='form-group'>															 			<div class='col-sm-5'> 				<input type='text'  class='form-control tbx_mailadress'					placeholder='Email'> 			</div> 			<div class='col-sm-4'> 				<select class='form-control' id='select_email'> 					<option>Privat</option> 					<option>Gesch&auml;ftlich</option> 				</select> 			</div> 			<div class='col-sm-3'> 				<button type='button' class='btn btn-default removemailadress_btn' 					id='btn_delete' >L&ouml;schen</button> 			</div> 		</div> 	</div>";

// Load pageheader
$("#pageheader").load("../partials/header.html", function() {
	$("#adressverwaltung_nav").addClass("active");
});

// $('ul.nav
// a[href="adressverwaltung_personen.html"]').parent().addClass('active');

$("#neuanlegen_btn").click(function() {
	$("#modal_title_text").text("Neue Person");
});

// Modal Neu anlegen -> speichern
$("#saveperson_btn").click(function() {
	var newperson = new Object();
	newperson.personId = -1;
	newperson.salutation = $("#tbx_salutation").val();
	newperson.title = $("#tbx_title").val();
	newperson.firstName = $("#tbx_firstName").val();
	newperson.lastName = $("#tbx_lastName").val();
	newperson.comment = "";
	newperson.updateTimestamp = "";
	newperson.active = 1;

	newperson.address = $("#tbx_address").val();
	newperson.city = $("#tbx_city").val();
	newperson.zip = $("#tbx_zip").val();
	newperson.country = $("#tbx_country").val();

	newperson.loginEmail = $("#select_loginEmail").val();
	newperson.password = "";
	newperson.permission = $("#select_permission").val();
	newperson.types = $("#select_types").val();

	newperson.emails = [];
	newperson.phoneNumbers = [];

	var persondata = JSON.stringify(newperson);
	$.ajax({
		headers : {
			'Accept' : 'application/json',
			'Content-Type' : 'application/json'
		},
		type : "POST",
		url : "../rest/secure/person/setPerson",
		data : {
			"personId" : "1",
			"salutation" : "Frau",
			"title" : "BSc",
			"firstName" : "Maria",
			"lastName" : "Schmidt",
			"comment" : "Kommt aus der Stadt",
			"updateTimestamp" : "12.9.2013",
			"active" : "1",
			"address" : "Vogelweiderstraße 7",
			"city" : "Obertrum",
			"zip" : "5070",
			"country" : "Deutschland",
			"loginEmail" : "maria@test.com",
			"password" : "muhaha",
			"permission" : "admin",
			"types" : [ "Mitglied", "User" ],
			"emails" : [ "f.sdfhj@doo.com", "huber@gmail.at" ],
			"phoneNumbers" : [ "293847239423", "032423423432", "293427394799" ]
		}
	}).done(function(data) {
		if (data) {
			alert("Speichern erfolgreich! Hurra!");
		} else {
			alert("whoops something went wrong");
		}
	});
	return false;
});

// Get all Persons and load into table
$(document).ready(
		function() {
			$.ajax({
				type : "POST",
				url : "../rest/secure/person/getAll"
			}).done(
					function(data) {
						
						var p = eval(data);
						var personsString = "";

						for ( var e in p) {
							var emailString = "";
							var phoneString = "";
							var typeString = "";

							var types = p[e].types;
							var emails = p[e].emails;
							var phoneNumbers = p[e].phoneNumbers;
							
							for (var i = 0; i < emails.length; i++) {
								emailString = emailString + emails[i];
								if (i < emails.length - 1) {
									emailString = emailString + ", ";
								}
							}
							for (var j = 0; j < phoneNumbers.length; j++) {
								phoneString = phoneString + phoneNumbers[j];
								if (j < phoneNumbers.length - 1) {
									phoneString = phoneString + ", ";
								}
							}
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
									+ typeString + "</td>" + "<td>"
									+ "[LastUpdate]" + "</td>" + "</tr>";
							
							$("#personTableBody").append(tableRow);
							
						}
					});
		});

// Get one person and load it to modal
$("#edit").click(function() {
	$("#modal_title_text").text("Bearbeite Person");
	var id = tableData[0];

	// Get person with id "id"
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getPersonById/" + id
	}).done(function(data) {

		var p = eval(data);

		// Load data to modal
		$("#tbx_salutation").val(p.salutation);
		$("#tbx_title").val(p.title);
		$("#tbx_firstName").val(p.firstName);
		$("#tbx_lastName").val(p.lastName);
		$("#tbx_address").val(p.address);
		$("#tbx_zip").val(p.zip);
		$("#tbx_city").val(p.city);
		$("#tbx_country").val(p.country);

		// test
		for (var i = 0; i < p.phoneNumbers.length; i++) {
			var newElement = $("<div/>", {
				id : "telnum-elemt" + telnumCount++,
				"class" : "telnum-element"
			}).append(telnumelement_template);
			$("#telnum_container").append(newElement);
			// $("#tbx_phoneNu").attr("id", "tbx_phoneNumber" + i);
		}

		$("#select_phoneNumber").each(function(a, b) {
			if ($(this).html() == "gesch&auml;ftlich")
				$(this).attr("selected", "selected");
		});

		for (var i = 0; i < p.emails.length; i++) {
			var newElement = $("<div/>", {
				id : "mailadress-element" + mailadressCount++,
				"class" : "mailadress-element"
			}).append(mailadress_template);
			$("#mailadress_container").append(newElement);
		}

	});
});

// Phonenumber handler
$("body").on('click', '.removephonenumber_btn', function() {
	$(this).closest('div[class^="telnum-element"]').remove();
});

$(document)
		.ready(
				function() {
					$("#addphonenumber_btn")
							.click(
									function() {
										telnumelement_template = "<div class='row'> 		<div class='form-group'>															 			<div class='col-sm-5'> 				<input type='text' id='tbx_phoneNumber"
												+ telnumCount
												+ "' class='form-control tbx_telnr' 					placeholder='Telefonnr.'> 			</div> 			<div class='col-sm-4'> 				<select class='form-control' id='select_phoneNumber'> 					<option>Privat</option> 					<option>Gesch&auml;ftlich</option> 				</select> 			</div> 			<div class='col-sm-3'> 				<button type='button' class='btn btn-default removephonenumber_btn' 					id='btn_delete' >L&ouml;schen</button> 			</div> 		</div> 	</div>";
										var newElement = $(
												"<div/>",
												{
													id : "telnum-elemt"
															+ telnumCount++,
													"class" : "telnum-element"
												}).append(
												telnumelement_template);
										$("#telnum_container").append(
												newElement);
									});
				});

// Mailadress handler
$("body").on('click', '.removemailadress_btn', function() {
	$(this).closest('div[class^="mailadress-element"]').remove();
});

$(document).ready(function() {
	$("#addmailadress_btn").click(function() {
		var newElement = $("<div/>", {
			id : "mailadress-element" + mailadressCount++,
			"class" : "mailadress-element"
		}).append(mailadress_template);
		$("#mailadress_container").append(newElement);
	});
});

// <!-- Filterfunktion -->

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

// <!-- Typfilter -->

$(document).ready(function() {
	$('#mitarbeiter_cbx').prop('checked', true);
	$('#unterstuetzer_cbx').prop('checked', true);
	$('#mitglieder_cbx').prop('checked', true);
	$('#gaeste_cbx').prop('checked', true);

	(function($) {
		$('#mitarbeiter_cbx').on('change', function() {
			if (this.checked) {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "MA"
				}).show();
			} else {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "MA"
				}).hide();
			}
		});
		$('#unterstuetzer_cbx').on('change', function() {
			if (this.checked) {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "Unterst."
				}).show();
			} else {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "Unterst."
				}).hide();
			}
		});
		$('#mitglieder_cbx').on('change', function() {
			if (this.checked) {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "MG"
				}).show();
			} else {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "MG"
				}).hide();
			}
		});
		$('#gaeste_cbx').on('change', function() {
			if (this.checked) {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "Gast"
				}).show();
			} else {
				$('.searchable tr').filter(function() {
					return $(this).find('td').eq(10).text() == "Gast"
				}).hide();
			}
		});

	}(jQuery));
});

// <!-- Tabellenzeile markieren -->

$('#edit').prop('disabled', true);
$('#delete').prop('disabled', true);
var tableData;

$('#personen').on('click', 'tbody tr', function(event) {
	tableData = $(this).children("td").map(function() {
		return $(this).text();
	}).get();

	$(this).addClass('highlight').siblings().removeClass('highlight');
	$('#edit').prop('disabled', false);
	$('#delete').prop('disabled', false);
});

// <!-- L�schen einer Tabellenzeile -->

$('#delete').on(
		'click',
		function() {
			document.getElementById('index_delete').innerHTML = $
					.trim(tableData[0]);
			document.getElementById('anrede_delete').innerHTML = $
					.trim(tableData[1]);
			document.getElementById('titel_delete').innerHTML = $
					.trim(tableData[2]);
			document.getElementById('vorname_delete').innerHTML = $
					.trim(tableData[3]);
			document.getElementById('nachname_delete').innerHTML = $
					.trim(tableData[4]);
		});
