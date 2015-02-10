
//load with the given ArticleId
var global_articleId;
$(document).ready(function() {
	$.urlParam = function(name){
	    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
	    if (results==null){
	       return null;
	    }
	    else{
	       return results[1] || 0;
	    }
	}
	
	global_articleId = $.urlParam('articleId');
});



// ein neues DistributionElement mit den gegebenen Parametern erstellen
function createDistributionElementString(orgName, inOutArticleId, articleDescription, numberPUs, packagingUnit, type) {
	
	var typeStr = "";
	
	if (type == 0)
	{
		typeStr = "incomingDistributionElement";
	}
	else if (type == 1)
	{
		typeStr = "outgoingDistributionElement";
	}
	else if (type == 2)
	{
		typeStr = "depotDistributionElement";
	}
	
	var distributionElementString = 
	"<div class='col-sm-12 col-md-12 col-lg-6 "+typeStr+"'>"+
		"<div class='panel panel-default'>"+
			"<div class='panel-heading'>"+orgName+"</div>"+
			"<div class='panel-body'>"+
				"<form class='form-inline'>"+
					"<div class='form-group'>"+
						"<input class='hidden' value='"+inOutArticleId+"' >"+
						"<label > "+articleDescription+" </label>"+
						"<input type='text' class='form-control numberPUs' value='"+numberPUs+"' style='width:100px;' >"+
						"<label> VE: "+packagingUnit+"</label>"+
					"</div>"+
				"</form>"+
			"</div>"+
		"</div>"+
	"</div>";
	
	return distributionElementString;
	
}


/***** load the article distribution for the given article id *****/
$(document).ready(
	function() {
		
		// show loading spinner
		showLoadingSpinner(true);
		
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
			type: "POST",
			url: "../rest/secure/articlePUDistribution/getListByArticleId/"+global_articleId
		}).done(function(data) {
			
			var articleDTO;
			
			for (var i in data)
			{
				
				articleDTO = data[i].articleDTO;
				
				// find Organisation for current element
				var orgName = "";
				for (o in organisations)
				{
					if (organisations[o].id == data[i].organisationId)
						orgName = organisations[o].name;
				}
				
				// is incomingDistributionElement
				if (data[i].type == 0)
				{
					var element = createDistributionElementString(orgName, data[i].inOutArticleId, data[i].articleDTO.description, data[i].numberPUs, data[i].articleDTO.packagingUnit, data[i].type);
					$('#incomingDistributionContainer').append(element);
				}
				
				// is outgoingDistributionElement
				if ( data[i].type == 1 )
				{
					var element = createDistributionElementString(orgName, data[i].inOutArticleId, data[i].articleDTO.description, data[i].numberPUs, data[i].articleDTO.packagingUnit, data[i].type);
					$('#outgoingDistributionContainer').append(element);
				}
				
				// is outgoingDistributionElement
				if ( data[i].type == 2 )
				{
					var element = createDistributionElementString("Depot", -1, data[i].articleDTO.description, data[i].numberPUs, data[i].articleDTO.packagingUnit, data[i].type);
					$('#outgoingDistributionContainer').append(element);
				}
				
			}
			
			
			$('#lblDescriptionIncoming').text(articleDTO.description);
			$('#lblPUIncoming').text("VE: "+articleDTO.packagingUnit);
			$('#lblDescriptionOutgoing').text(articleDTO.description);
			$('#lblPUOutgoing').text("VE: "+articleDTO.packagingUnit);
			
			
			$('input.numberPUs').keypress(validateInput);
			// assign the check to all inputs with class 'numberPUs'
			$('input.numberPUs').keyup(checkNumberPUs);
			
			// call check for the first time to init Sums
			checkNumberPUs();
			
			// hide loading spinner
			showLoadingSpinner(false);
			
		});
	
	}
);


// limit the input of the inputs for the updated PU number
//restrict input of PUs to integers
function validateInput(eventData) {
	switch (eventData.keyCode)
	{
		case 8: return true;	// backspace key
		case 9: return true;	// tabulator key
		case 46: return true;	// delete key
		case 37: return true;	// left arrow key
		case 39: return true;	// right arrow key
		default: return /\d/.test(String.fromCharCode(eventData.charCode));
	}
}


// check number of PUs and color the inputs accordingly

function checkNumberPUs() {
	
	var incomingPUs = 0;
	var outgoingPUs = 0;

	// calc all incoming
	$('#incomingDistributionContainer input.numberPUs').each(
			function() {
				if ($(this).val().length==0)	// does not count when input length == 0
				{return;}
				incomingPUs = incomingPUs + parseInt($(this).val()); }
			);
	
	// calc all outgoing incl. Depot
	$('#outgoingDistributionContainer input.numberPUs').each(
			function() {
				if ($(this).val().length==0)	// does not count when input length == 0
				{return;}
				outgoingPUs = outgoingPUs + parseInt($(this).val()); }		
			);
	
	// write numbers to SumPanel
	
	$('#lblSumIncoming').text(""+incomingPUs);
	$('#lblSumOutgoing').text(""+outgoingPUs);
	
	// color inputs accordingly
	if (incomingPUs == outgoingPUs)
	{
		// if number are the same => color input fields green
		$('.numberPUs').parent().removeClass('has-error');
		$('.numberPUs').parent().addClass('has-success');
		
		// color sumPanel accordingly
		$('.sumIncomingDistribution').find('.panel:first').removeClass('panel-danger');
		$('.sumIncomingDistribution').find('.panel:first').addClass('panel-success');
		$('.sumOutgoingDistribution').find('.panel:first').removeClass('panel-danger');
		$('.sumOutgoingDistribution').find('.panel:first').addClass('panel-success')
	}
	else
	{
		// if numbers are not the same => color input fields red
		$('.numberPUs').parent().removeClass('has-success');
		$('.numberPUs').parent().addClass('has-error');
		
		// color sumPanel accordingly
		$('.sumIncomingDistribution').find('.panel:first').removeClass('panel-success');
		$('.sumIncomingDistribution').find('.panel:first').addClass('panel-danger');
		$('.sumOutgoingDistribution').find('.panel:first').removeClass('panel-success');
		$('.sumOutgoingDistribution').find('.panel:first').addClass('panel-danger');
	}
	
	if (incomingPUs == outgoingPUs)
	{
		return true;
	}
	else
	{
		return false;
	}
	
}


// save the new distribution by clicking the save button
$(document).ready(function() {

	$('#btn_saveDistribution').click(function(){
		
		if (checkNumberPUs() == false)
		{
			showAlertElement(false, "Summen stimmen nicht überein!", 2500);
			return;
		}
		
		// check input fields for incoming and outgoing for not null
		
		
		// check incoming Fields for 0
		var inputsValid = true; 
		
		$('.incomingDistributionElement input.numberPUs').each(function(){
			
			if (isNaN($(this).val())==true)
			{
				inputsValid = false;
			}
			
			if ($(this).val().length == 0)
			{
				inputsValid = false;
			}
			
			if ( parseInt( $(this).val() ) <= 0)
			{
				inputsValid = false;
			}
			
		});
		
		if (inputsValid == false)
		{
			showAlertElement(false, "Die VE-Anzahl muss für Wareneingänge größer als 0 sein.", 5000);
			return;
		}
		
		
		
		// check outgoing Fields for 0
		var inputsValid = true; 
		
		$('.outgoingDistributionElement input.numberPUs').each(function(){
			
			if (isNaN($(this).val())==true)
			{
				inputsValid = false;
			}
			
			if ($(this).val().length == 0)
			{
				inputsValid = false;
			}
			
			if ( parseInt( $(this).val() ) <= 0)
			{
				inputsValid = false;
			}
			
		});
		
		if (inputsValid == false)
		{
			showAlertElement(false, "Die VE-Anzahl muss für Warenausgänge größer als 0 sein.", 5000);
			return;
		}
		
		
		// list which contains all elements
		var distributionList = [];
		
		// get all incomingDistributions
		$('.incomingDistributionElement').each( function() {
			var inOutArticleId = parseInt($(this).find('input').first().val());
			var numberPUs = parseInt($(this).find('input').last().val());
			
			// create object with all required fields
			var element = new Object();
			element.organisationId = 0;		// organisationId is not required for backend
			element.numberPUs = numberPUs;	// number of updated PUs is required for backend
			element.type = 0;				// type 0 => incoming
			element.articleDTO = null;		// articleDTO is not required
			element.inOutArticleId = inOutArticleId;	// Id of Incoming/OutgoingArticle is required for backend
			
			distributionList.push(element);
		} );
		
		
		// get all outgoingDistributions
		$('.outgoingDistributionElement').each( function() {
			var inOutArticleId = parseInt($(this).find('input').first().val());
			var numberPUs = parseInt($(this).find('input').last().val());
			
			// create object with all required fields
			var element = new Object();
			element.organisationId = 0;		// organisationId is not required for backend
			element.numberPUs = numberPUs;	// number of updated PUs is required for backend
			element.type = 1;				// type 1 => outgoing
			element.articleDTO = null;		// articleDTO is not required
			element.inOutArticleId = inOutArticleId;	// Id of Incoming/OutgoingArticle is required for backend
			
			distributionList.push(element);
		} );
		
		
		// get Deopt Distribution
		$('.depotDistributionElement').each( function() {
			var inOutArticleId = parseInt($(this).find('input').first().val());
			var numberPUs = parseInt($(this).find('input').last().val());
			
			// create object with all required fields
			var element = new Object();
			element.organisationId = -1;	// organisationId is not required for backend (Id: -1 for Depot)
			element.numberPUs = numberPUs;	// number of updated PUs is required for backend
			element.type = 2;				// type 2 => depot
			element.articleDTO = null;		// articleDTO is not required
			element.inOutArticleId = -1;	// Id of Incoming/OutgoingArticle is required for backend (Id: -1 for Depot)
			
			distributionList.push(element);
		} );
		
		
		// transmit updated DistributionList to Backend
		$.ajax({
			headers : {
				'Accept' : 'application/json',
				'Content-Type' : 'application/json'
			},
			type : "POST",
			url : "../rest/secure/articlePUDistribution/updateDistributionList",
			contentType: "application/json; charset=utf-8",
		    dataType: "json",
			data : JSON.stringify(distributionList)
		}).done(function(data) {
			if (data) {
				
				if (data.success == true)
				{
					showAlertElement(1, data.message, 5000);
					
					location.href="warenverwaltung_wareneingang.html";
					// location.href=document.referrer;	// goes back to the previous page
				}
				else
				{
					showAlertElement(2, data.message, 5000);
				}
				
			} else {
				alert("Verbindungsproblem mit dem Server");
			}
			
		});
		
	});
	
});



$('#btn_deleteArticleDistribution').click(function(){
	
	// call delete request
	$.ajax({
		type: "POST",
		url: "../rest/secure/articlePUDistribution/deleteArticleById/"+global_articleId
	}).done(function(data) {
		if (data)
		{
			if (data.success == true)
			{
				showAlertElement(1, data.message, 5000);
				
				location.href="warenverwaltung_wareneingang.html";
				// location.href=document.referrer;	// goes back to the previous page
			}
			else
			{
				showAlertElement(2, data.message, 5000);
			}
			
		} else {
			alert("Verbindungsproblem mit dem Server");
		}
		
	});
	
});