//append alert message accordion and to organisation search modal
var accordionError = "<div id='accordionErrorAlert'> <div class='col-sm-2'> <div class='alert alert-danger custom-alert' style='text-align: left;'>Leere Felder vorhanden!</div> </div>  </div>";
$(".newAlertFormAccordion").append(accordionError);
var modalErrorOrganisation = "<div id='modalErrorOrganisationAlert'> <div class='col-sm-6'> <div class='alert alert-danger custom-alert' style='text-align: left;'>Keine Organisation ausgewählt!</div> </div>  </div>";
$("#newAlertFormOrganisation").append(modalErrorOrganisation);

$('.collapse').on('show.bs.collapse', function () {
    $otherPanels = $(this).parents('.panel-group').siblings('.panel-group');
    $('.collapse',$otherPanels).removeClass('in');
});

$('.collapse').collapse('hide');
$("#collapseOne").collapse('show');

$(".btn_export").prop('disabled', true);

//init popover
$(function () {
	$('[data-toggle="popover"]').popover()
});

//clear inputs on accordion load
$(".accordion_heading").click(function() {
	$(".btn_export").prop('disabled', true);
	
	$('.tbx_organisation').val("");
	$('.tbx_orgId').val("");
	
	$('.tbx_datefrom').val("");
	$('.tbx_dateto').val("");
	
	$('.tbx_totalweight').val("");
	$('.tbx_totalprice').val("");
});

//loads all deliverers
function loadAllOrganisations() {
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getAllOrganisations"
	}).done(
			function(data) {
				var o = eval(data);
				
				var nameString = "";
				for (var e in o) {
					for(var i=0; i< o[e].types.length; i++){
							if(o[e].name.length > 22){
								nameString = o[e].name.substring(0, 22) + "...";
							}
							else{
								nameString = o[e].name;
							}
							
							var o_divRow = "<div class='boxElement_organisation'>" + "<input type='hidden' value="+ o[e].id +">" + "<span>" + nameString + " "
							+ "</span><input class='pull-right' value="+ o[e].id +" id="+ o[e].id +" name='organisationRadio' type='radio'></div>";
							
							nameString = "";
							$("#organisationDiv").append(o_divRow);
					}
				}
	});
};

//load organisation modal
$(".btn_addOrganisation").click(function() {
	$(".boxElement_organisation").remove();
	$("#filter_modal").val("");
	$("#newAlertFormOrganisation").hide();
	loadAllOrganisations();
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
				o = eval(data);
				
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
	reportCommand_global = generateReport(true, false, false, false, false, false);
});

//generate report (outgoingReportByOrganisation)
$("#btn_generateOutgoingReportByOrg").click(function() {
	reportCommand_global = generateReport(false, false, true, false, false, false);
});

//generate report (incomingReportForAllOrganisations)
$("#btn_generateIncomingReportForAllOrg").click(function() {
	reportCommand_global = generateReport(false, true, false, false, false, false);
});

//generate report (outgoingReportForAllOrganisations)
$("#btn_generateOutgoingReportForAllOrg").click(function() {
	reportCommand_global = generateReport(false, false, false, true, false, false);
});

//generate report (totalSumOfAllIncomingDeliveries)
$("#btn_generateTotalSumOfAllIncomingDeliveries").click(function() {
	reportCommand_global = generateReport(false, false, false, false, true, false);
});

//generate report (totalSumOfAllOutgoingDeliveries)
$("#btn_generateTotalSumOfAllOutgoingDeliveries").click(function() {
	reportCommand_global = generateReport(false, false, false, false, false, true);
});

//generate report export
$(".btn_export").click(function() {
	generateReportExport();
});

function validateInputFields(mode){
	var orgId = $('.tbx_orgId').val();
	var dateFrom = $('.tbx_datefrom').val();
	var dateTo = $('.tbx_dateto').val();
	
	// check if all Fields are filled
	if(mode == 0){
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
function generateReport(incomingReportByOrg, incomingReportForAllOrg, outgoingReportByOrg, outgoingReportForAllOrg, totalSumOfAllIncomingDeliveries, totalSumOfAllOutgoingDeliveries){
	//validate input fields
	if(incomingReportByOrg == true || outgoingReportByOrg == true){
		if(validateInputFields(0) == null){
			return;
		}
	}
	else{
		if(validateInputFields(1) == null){
			return;
		}
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
	
	reportCommand.dateFrom = $('.tbx_datefrom').val();
	reportCommand.dateTo = $('.tbx_dateto').val();
	
	reportCommand.incomingReportByOrganisationId = incomingReportByOrg;
	reportCommand.incomingReportForAllOrganisations = incomingReportForAllOrg;
	
	reportCommand.outgoingReportByOrganisationId = outgoingReportByOrg;
	reportCommand.outgoingReportForAllOrganisations = outgoingReportForAllOrg; 
	
	reportCommand.totalSumOfAllIncomingDeliveries = totalSumOfAllIncomingDeliveries;
	reportCommand.totalSumOfAllOutgoingDeliveries = totalSumOfAllOutgoingDeliveries;
	
	var report_data;
	$.ajax({
		headers : {
			'Accept' : 'application/json',
			'Content-Type' : 'application/json'
		},
		type : "POST",
		url : "../rest/secure/reports/getSingleData",
		contentType: "application/json; charset=utf-8",
	    dataType: "json",
		data : JSON.stringify(reportCommand)
	}).done(function(data) {
			report_data = eval(data);
			
			$('.tbx_totalweight').val(report_data.totalWeight + " kg");
			$('.tbx_totalprice').val(report_data.totalPrice + " €");
			
			$(".btn_export").prop('disabled', false);
	});
	
	return reportCommand;
}

//TODO not working
function generateReportExport(){
	if(reportCommand_global == null){
		return;
	}
	
	$.ajax({
		headers : {
			'Accept' : 'application/json',
			'Content-Type' : 'application/json'
		},
		type : "POST",
		url : "../rest/secure/reports/generateCSVReport",
		contentType: "application/json; charset=utf-8",
	    dataType: "json",
		data : JSON.stringify(reportCommand_global)
	}).done(function(data) {
	});
	
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