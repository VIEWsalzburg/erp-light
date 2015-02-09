
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
	
	var distributionElementString = "<div class='row "+typeStr+"'>"+
										"<div class='col-sm-12'>"+
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
			
			for (var i in data)
			{
				
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
		
		
		
		
		// get all incomingDistributions
		
		
		
		// get all outgoingDistributions
		
		
		
		// get Deopt Distribution
		
		showAlertElement(true, "Speichern", 5000);
		
		
	});
	
});

