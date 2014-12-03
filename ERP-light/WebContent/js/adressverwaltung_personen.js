
		var telnumelement_template = "<div class='row'> 		<div class='form-group'>															 			<div class='col-sm-5'> 				<input type='text' class='form-control' id='tbx_telnr2' 					placeholder='Telefonnr.'> 			</div> 			<div class='col-sm-4'> 				<select class='form-control'> 					<option>Privat</option> 					<option>Gesch&auml;ftlich</option> 				</select> 			</div> 			<div class='col-sm-3'> 				<button type='button' class='btn btn-default removephonenumber_btn' 					id='btn_delete' >L&ouml;schen</button> 			</div> 		</div> 	</div>";
		var telnumCount=0;
		
		$("#pageheader").load("../partials/header.html");
		$("#adressverwaltung_nav").addClass("active");
	

	
		$("#neuanlegen_btn").click(function() {
			$("#modal_title_text").text("Neue Person");
		});

		$("#edit").click(function() {
			$("#modal_title_text").text("Bearbeite Person");
		});
	
		$("body").on('click', '.removephonenumber_btn',function() {
			$(this).closest('div[class^="telnum-element"]').remove();
				});
	
		$(document)
				.ready(
						function() {
							$("#addphonenumber_btn")
									.click(
											function() {
												
												var newElement = $("<div/>",{id:"telnum-elemt"+telnumCount++, "class":"telnum-element"}).append(telnumelement_template);
												
												$("#telnum_container").append(newElement);
																								 
											});
						});
	

//	<!-- Filterfunktion -->
	
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
	

//	<!-- Typfilter -->
	
		$(document)
				.ready(
						function() {
							$('#mitarbeiter_cbx').prop('checked', true);
							$('#unterstuetzer_cbx').prop('checked', true);
							$('#mitglieder_cbx').prop('checked', true);
							$('#gaeste_cbx').prop('checked', true);

							(function($) {
								$('#mitarbeiter_cbx')
										.on(
												'change',
												function() {
													if (this.checked) {
														$('.searchable tr')
																.filter(
																		function() {
																			return $(
																					this)
																					.find(
																							'td')
																					.eq(
																							10)
																					.text() == "MA"
																		})
																.show();
													} else {
														$('.searchable tr')
																.filter(
																		function() {
																			return $(
																					this)
																					.find(
																							'td')
																					.eq(
																							10)
																					.text() == "MA"
																		})
																.hide();
													}
												});
								$('#unterstuetzer_cbx')
										.on(
												'change',
												function() {
													if (this.checked) {
														$('.searchable tr')
																.filter(
																		function() {
																			return $(
																					this)
																					.find(
																							'td')
																					.eq(
																							10)
																					.text() == "Unterst."
																		})
																.show();
													} else {
														$('.searchable tr')
																.filter(
																		function() {
																			return $(
																					this)
																					.find(
																							'td')
																					.eq(
																							10)
																					.text() == "Unterst."
																		})
																.hide();
													}
												});
								$('#mitglieder_cbx')
										.on(
												'change',
												function() {
													if (this.checked) {
														$('.searchable tr')
																.filter(
																		function() {
																			return $(
																					this)
																					.find(
																							'td')
																					.eq(
																							10)
																					.text() == "MG"
																		})
																.show();
													} else {
														$('.searchable tr')
																.filter(
																		function() {
																			return $(
																					this)
																					.find(
																							'td')
																					.eq(
																							10)
																					.text() == "MG"
																		})
																.hide();
													}
												});
								$('#gaeste_cbx')
										.on(
												'change',
												function() {
													if (this.checked) {
														$('.searchable tr')
																.filter(
																		function() {
																			return $(
																					this)
																					.find(
																							'td')
																					.eq(
																							10)
																					.text() == "Gast"
																		})
																.show();
													} else {
														$('.searchable tr')
																.filter(
																		function() {
																			return $(
																					this)
																					.find(
																							'td')
																					.eq(
																							10)
																					.text() == "Gast"
																		})
																.hide();
													}
												});

							}(jQuery));
						});
	

//	<!-- Tabellenzeile markieren -->
	
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
	

//	<!-- L�schen einer Tabellenzeile -->
	
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
	