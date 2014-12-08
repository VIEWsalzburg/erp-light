$("#pageheader").load("../partials/header.html");
$("#adressverwaltung_nav").addClass("active");

$("#neuanlegen_btn").click(function() {
	$("#modal_title_text").text("Neue Organisation");
});

$("#edit").click(function() {
	$("#modal_title_text").text("Bearbeite Organisation");
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

// <!-- Filterfunktion Neu anlegen/Bearbeiten -->

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

// <!-- Typfilter -->
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

$('#delete').on('click', function() {
	document.getElementById('index_delete').innerHTML = $.trim(tableData[0]);
	document.getElementById('typ_delete').innerHTML = $.trim(tableData[1]);
	document.getElementById('name_delete').innerHTML = $.trim(tableData[2]);
	document.getElementById('adresse_delete').innerHTML = $.trim(tableData[4]);
});
