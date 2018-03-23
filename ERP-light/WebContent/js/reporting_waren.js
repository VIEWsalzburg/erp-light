//append alert message accordion and to organisation search modal
var accordionError = "<div id='accordionErrorAlert'> <div class='col-sm-2'> <div class='alert alert-danger custom-alert' style='text-align: left;'>Leere Felder vorhanden!</div> </div>  </div>";
$(".newAlertFormAccordion").append(accordionError);
var modalErrorOrganisation = "<div id='modalErrorOrganisationAlert'> <div class='col-sm-6'> <div class='alert alert-danger custom-alert' style='text-align: left;'>Keine Organisation ausgewählt!</div> </div>  </div>";
$("#newAlertFormOrganisation").append(modalErrorOrganisation);

$(".btn_export").prop('disabled', true);

//init popover
$(function () {
	$('[data-toggle="popover"]').popover()
});

//init datepicker on load
$('.datepicker').datepicker({
				format: "dd.mm.yyyy",
				weekStart: 1,
				todayBtn: "linked",
				language: "de",
				todayHighlight: true,
				autoclose: true
			});

//clear inputs on accordion load
$(".accordion_heading").click(function() {
	$(".btn_export").prop('disabled', true);
	$(".tbx_organisation_popover").attr("data-content", "");
	
	$('.tbx_organisation').val("");
	$('.tbx_orgId').val("");
	
	$('.tbx_datefrom').val("");
	$('.tbx_dateto').val("");
	
	$('.tbx_totalweight').val("");
	$('.tbx_totalprice').val("");
});

//loads all deliverers
function loadAllOrganisations(type) {
	
	var allCategories;
	
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/category/getAllCategories"
	}).done( function(data){
		allCategories = data;
	});
	
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/reducedData/getAllOrganisations"
	}).done(
			function(data) {
				var o = data;	// already JSON
				
				for (var e in o) {
					
					// variable for checking if the organisation has the requested type
					var hasType = false;
					
					// iterate over all types
					for (var i in o[e].types)
					{
						// if organisation has the requested type
						if (o[e].types[i] == type)
							hasType = true;
					}
					
					// if checkvariable is not true (organisation has not the requested type
					if (hasType == false)
					{
						// continue with next organisation
						continue;
					}
					
					nameString = o[e].name;
				
					/*
					if(o[e].name.length > 22){
						nameString = o[e].name.substring(0, 22) + "...";
					}
					*/
					
					
					var categoryString = "";
					
					var categoryIds = o[e].categoryIds;
					for (var a in categoryIds)
					{
						for (var c in allCategories)
						{
							if (categoryIds[a] == allCategories[c].categoryId)
							{
								categoryString = categoryString + allCategories[c].category;
							}
						}
						
						if (a < categoryIds.length - 1)
							categoryString = categoryString + ", ";
					}
					
					var o_divRow = "<div class='boxElement_organisation'>" +
										"<div class='row'>" +
											"<div class='col-sm-5'>" +
												"<input type='hidden' value="+ o[e].id +">" +
												"<span>" + nameString + "</span>"+
											"</div>" +
											"<div class='col-sm-3'>" +
												"<span>" + o[e].city + "</span>" +
											"</div>" +
											"<div class='col-sm-3'>" +
												"<span>" + categoryString + "</span>" +
											"</div>" +
											"<div class='col-sm-1'>" +
												"<input class='pull-right' value="+ o[e].id +" id="+ o[e].id +" name='organisationRadio' type='radio'>" +
											"</div>" +
										"</div>" +
									"</div>";
					
					
					
					$("#organisationDiv").append(o_divRow);
						
					
				}
	});
};

//load organisation modal with lieferanten
$(".btn_addLieferanten,#collapseOne .tbx_organisation_popover").click(function() {
	$(".boxElement_organisation").remove();
	$("#filter_modal").val("");
	$("#newAlertFormOrganisation").hide();
	loadAllOrganisations('Lieferant');
	
	$('#chooseOrganisationModal .modal-title').text("Lieferant auswählen");
	
	$('#chooseOrganisationModal').modal('show');
	
	// set focus when modal is loaded
	$('#chooseOrganisationModal').on('shown.bs.modal', function(){
		$('#filter_modal').focus();
	});
});

//load organisation modal with kunden
$(".btn_addKunden,#collapseTwo .tbx_organisation_popover").click(function() {
	$(".boxElement_organisation").remove();
	$("#filter_modal").val("");
	$("#newAlertFormOrganisation").hide();
	loadAllOrganisations('Kunde');
	
	$('#chooseOrganisationModal .modal-title').text("Kunde auswählen");
	
	$('#chooseOrganisationModal').modal('show');
	
	// set focus when modal is loaded
	$('#chooseOrganisationModal').on('shown.bs.modal', function(){
		$('#filter_modal').focus();
	});
});


//select organisation if row is clicked
$(document).on("click", ".boxElement_organisation", function(){
	// select radio button
	$(this).find('input:radio[name=organisationRadio]').prop('checked', true);
});



//save organisation to textbox
$("#btn_saveOrganisation").click(function() {
	//get Id of checked radiobox of organisation div
	var id = $("#organisationDiv input[name='organisationRadio']:checked").val();
	
	//check if a checkbox is selected
	if(id == null){
		$("#newAlertFormOrganisation").show();
		return;
	}
	
	var organisationString;
	var o;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getOrganisationById/" + id
	}).done(
			function(data) {
				o = data;	// already JSON
				
				// store the id to the global var
				$('.tbx_orgId').val(o.id);
				
				organisationString = o.name + ", " + o.zip + " " + o.city + ", " + o.country;
	});
	
	$(".tbx_organisation").val(organisationString);
	$(".tbx_organisation_popover").attr("data-content", o.name + ",<br>" + o.zip + " " + o.city + ",<br>" + o.country);
	$('#chooseOrganisationModal').modal('hide');
});

//generate report (incomingReportByOrganisation)
$("#btn_generateIncomingReportByOrg").click(function() {
	reportCommand_global = generateReport(0, true, false, false, false, false, false);
});

//generate report (outgoingReportByOrganisation)
$("#btn_generateOutgoingReportByOrg").click(function() {
	reportCommand_global = generateReport(2, false, false, true, false, false, false);
});

//generate report (totalSumOfAllIncomingDeliveries)
$("#btn_generateTotalSumOfAllIncomingDeliveries").click(function() {
	reportCommand_global = generateReport(4, false, false, false, false, true, false);
});

//generate report (totalSumOfAllOutgoingDeliveries)
$("#btn_generateTotalSumOfAllOutgoingDeliveries").click(function() {
	reportCommand_global = generateReport(5, false, false, false, false, false, true);
});

//generate report export
$(".btn_export").click(function() {
	generateReportExport();
});

//generate report export for all organisations (incoming)
$("#btn_exportIncomingReportForAllOrg").click(function() {
	reportCommand_global = generateReport(1, false, true, false, false, false, false);
	generateReportExport();
});

//generate report export for all organisations (outgoing)
$("#btn_exportOutgoingReportForAllOrg").click(function() {
	reportCommand_global = generateReport(3, false, false, false, true, false, false);
	generateReportExport();
});

function validateInputFields(mode){
	var orgId = $('.tbx_orgId').val();
	var dateFrom = $("#inputDateGroup" + mode).find('.tbx_datefrom').val();
	var dateTo = $("#inputDateGroup" + mode).find('.tbx_dateto').val();
	
	// check if all Fields are filled
	if(mode == 0 || mode == 2){
		if (orgId == 0)
		{
			showAlertElement(2, "Leere Felder vorhanden!", 5000);
			return null;
		}
	}
	
	if( (dateFrom == "") || (dateTo == "") )
	{
		showAlertElement(2, "Leere Felder vorhanden!", 5000);
		return null;
	}
	
	// check date for validity
	var regEx = /^(\d{1,2})\.(\d{1,2})\.(\d{4})$/;
	
	var dateFromArray = dateFrom.match(regEx);
	var dateToArray = dateTo.match(regEx);
	
	if (dateFromArray == null || dateToArray == null)
	{
		showAlertElement(2, "Falsches Datumsformat!", 5000);
		return null;
	}
	
	return true;
}

//generate report
var reportCommand_global;
function generateReport(mode, incomingReportByOrg, incomingReportForAllOrg, outgoingReportByOrg, outgoingReportForAllOrg, totalSumOfAllIncomingDeliveries, totalSumOfAllOutgoingDeliveries){
	//validate input fields
	if(validateInputFields(mode) == null){
			return;
	}
	
	//create new reportCommand object
	var reportCommand = new Object();
	
	//check if organisation id is necessary for report
	if(incomingReportByOrg == true || outgoingReportByOrg == true){
		reportCommand.organisationId = $('.tbx_orgId').val(); 
	}
	else{
		reportCommand.organisationId = 0; 
	}
	
	reportCommand.dateFrom = $("#inputDateGroup" + mode).find('.tbx_datefrom').val();
	reportCommand.dateTo = $("#inputDateGroup" + mode).find('.tbx_dateto').val();
	
	reportCommand.incomingReportByOrganisationId = incomingReportByOrg;
	reportCommand.incomingReportForAllOrganisations = incomingReportForAllOrg;
	
	reportCommand.outgoingReportByOrganisationId = outgoingReportByOrg;
	reportCommand.outgoingReportForAllOrganisations = outgoingReportForAllOrg; 
	
	reportCommand.totalSumOfAllIncomingDeliveries = totalSumOfAllIncomingDeliveries;
	reportCommand.totalSumOfAllOutgoingDeliveries = totalSumOfAllOutgoingDeliveries;
	
	var urlString;
	if(mode == 1 || mode == 3){
		urlString = "getListData";
	}
	else{
		urlString = "getSingleData";
	}
	
	var report_data;
	$.ajax({
		headers : {
			'Accept' : 'application/json',
			'Content-Type' : 'application/json'
		},
		type : "POST",
		url : "../rest/secure/reports/articles/" + urlString,
		contentType: "application/json; charset=utf-8",
	    dataType: "json",
		data : JSON.stringify(reportCommand)
	}).done(function(data) {
			report_data = data;		// already JSON
			
			if(mode == 0 || mode == 2 || mode == 4 || mode == 5){
				$('.tbx_totalweight').val(report_data.totalWeight + " kg");
				$('.tbx_totalprice').val(report_data.totalPrice + " €");
			}
			
			$(".btn_export").prop('disabled', false);
	});
	
	return reportCommand;
}

function generateReportExport(){
	if(reportCommand_global == null){
		return;
	}
	
	$('<form action="../rest/secure/reports/articles/generateCSVReport">'+ 
			'<input type="hidden" value="'+reportCommand_global.organisationId+'" name="organisationId">'+ 
			'<input type="hidden" value="'+reportCommand_global.dateFrom+'" name="dateFrom">'
			+ '<input type="hidden" value="'+reportCommand_global.dateTo+'" name="dateTo">'
			+ '<input type="hidden" value="'+reportCommand_global.incomingReportByOrganisationId+'" name="incomingReportByOrganisationId">'
			+ '<input type="hidden" value="'+reportCommand_global.incomingReportForAllOrganisations+'" name="incomingReportForAllOrganisations">'
			+ '<input type="hidden" value="'+reportCommand_global.outgoingReportByOrganisationId+'" name="outgoingReportByOrganisationId">'
			+ '<input type="hidden" value="'+reportCommand_global.outgoingReportForAllOrganisations+'" name="outgoingReportForAllOrganisations">'
			+ '<input type="hidden" value="'+reportCommand_global.totalSumOfAllIncomingDeliveries+'" name="totalSumOfAllIncomingDeliveries">'
			+ '<input type="hidden" value="'+reportCommand_global.totalSumOfAllOutgoingDeliveries+'" name="totalSumOfAllOutgoingDeliveries">'
			+ '</form>').appendTo('body').submit();
	
	$(".btn_export").prop('disabled', true);
}

/**
 * search filter for organisations
 */
$(document).ready(function() {
	(function($) {
		$('#filter_modal').keyup(function() {

			var rex = new RegExp($(this).val(), 'i');
			$('.searchable .boxElement_organisation').hide();
			$('.searchable .boxElement_organisation').filter(function() {
				return rex.test($(this).text());
			}).show();
		})
	}(jQuery));
});