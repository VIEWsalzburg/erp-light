//append alert message to modal
var modalError = "<div id='modalErrorReceiverAlert'> <div class='col-sm-5'> <div class='alert alert-danger custom-alert' style='text-align: left;'>Kein Empfänger ausgewählt!</div> </div>  </div>";
$("#newAlertFormReceiver").append(modalError);

//load page in specific mode
var global_id;
var isBooked;
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
	
	var mode = $.urlParam('mode');
	var bookedStatus = $.urlParam('booked');
	global_id = $.urlParam('id');
	
	//default value = false
	isBooked = true;
	if (bookedStatus == 'false')
	{
		isBooked = false;
	}
	
	// show loading spinner
	showLoadingSpinner(true);
	
	// load all organisations into global variable to search them for the deliverer property of each article
	loadAllOrganisations();		// first load all organisaitons synchron to use the global variable when loading the articles
	
	
	if(mode == "new"){
		$("#tabtext").text("Neuer Warenausgang");
		loadAllAvailableArticlesInDepot();
		global_id = 0;	// set Id to 0
		isBooked = false;
	}
	else if(mode == "edit"){
		$("#tabtext").text("Bearbeite Warenausgang");
		loadTableContent(global_id);
	}

});

//init popover
$(function () {
	$('[data-toggle="popover"]').popover();
});

//set comment popover on tbx_comment lost focus event
$("#tbx_comment").focusout(function() {
	$("#tbx_comment_popover").attr("data-content", $("#tbx_comment").val());
});


// init datepicker on load
$('.datepicker').datepicker({
				format: "dd.mm.yyyy",
				weekStart: 1,
				todayBtn: "linked",
				language: "de",
				todayHighlight: true,
				autoclose: true
			});


//restrict input of PUs to integers
$(document).ready(function(){
	$('#tbx_packagingunit').keypress(function(eventData) {
		switch (eventData.keyCode)
		{
			case 8: return true;	// backspace key
			case 9: return true;	// tabulator key
			case 46: return true;	// delete key
			case 37: return true;	// left arrow key
			case 39: return true;	// right arrow key
			default: return /\d/.test(String.fromCharCode(eventData.charCode));
		}
	});
});


// global variable to keep organisation data
var gOrganisations;
function loadAllOrganisations() {
	
	var availArticles;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/reducedData/getAllOrganisations"
	}).done(function(data) {
			gOrganisations = data;	// already JSON
	});
	
}

// checked
function loadAllAvailableArticlesInDepot(){
	
	// show loading spinner
	showLoadingSpinner(true);
	
	var availArticles;
	$.ajax({
		type : "POST",
		async : true,
		url : "../rest/secure/articles/getAvailableArticles"
	}).done(function(data) {
			availArticles = data;	// already JSON
			
			
			// if no articles are available
			if (availArticles.length==0)
			{
				// show alert
				showAlertElement(2, "Keine Waren verfügbar!", 5000);
			}
			
			for (var i in availArticles) {
				var article = availArticles[i].articleDTO;
				var articleId = article.articleId;
				var description = article.description;
				var packagingUnit = article.packagingUnit;
				var weightpu = article.wieghtpu;
				var mdd = article.mdd;
				var pricepu = article.pricepu;
				var availNumberPUs = availArticles[i].availNumberOfPUs;
				
				// get Deliverer (first 20 characters)
				var deliverer = "";
				var delivererId = article.delivererId;
				
				for (var o in gOrganisations)
				{
					if (gOrganisations[o].id == delivererId)
					{
						deliverer = gOrganisations[o].name;
						if (deliverer.length > 15)
						{ deliverer = deliverer.substr(0, 15)+"...";	}
					}
				}
				
				var tableRow = "<tr id='aId_"+articleId+"_dep'>"
					+ "<td class='hidden'>" + articleId + "</td>"
					+ "<td class='article_description'>" + description + "</td>"
					+ "<td class='article_packagingunits'>" + availNumberPUs	+ "</td>"
					+ "<td class='article_packaging_unit'>" + packagingUnit	+ "</td>"
					+ "<td class='article_mdd'>" + mdd	+ "</td>"
					+ "<td class='article_deliverer'>" + deliverer + "</td>"
					+ "</tr>";
				
				$("#leftDepotTableBody").append(tableRow);
			}
			
			// hide loading spinner
			showLoadingSpinner(false);
	
	});
	
}


//load table content
function loadTableContent(id){
	
	// show loading spinner
	showLoadingSpinner(true);
	
	var out;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/outgoingDelivery/getById/" + id
	}).done(function(data) {
			out = data;	// already JSON
	});
	
	//get organisation by id
	var org;
	var receiverString;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getOrganisationById/" + out.organisationId
	}).done(function(data) {
		org = data;	// already JSON
		
		// store the id to the global var
		$('#tbx_orgId').val(org.id);
		
		receiverString = org.name + ", " + org.zip + " " + org.city + ", " + org.country;
		$("#tbx_receiver").val(receiverString);
	});
	
	//set receiver popover
	$("#tbx_receiver_popover").attr("data-content", org.name + ",<br>" + org.zip + " " + org.city + ",<br>" + org.country);
	
	$("#tbx_date").val(out.date);
	$("#tbx_comment").val(out.comment);
	
	//set comment popover
	$("#tbx_comment_popover").attr("data-content", $("#tbx_comment").val());
	
	
	//get articles (disposition)
	var oArticles = out.outgoingArticleDTOs;
	
	// sort articles by article_nr
	oArticles.sort(function(a, b){
		return a.articleNr - b.articleNr;
	});
	
	for(var i=0; i < oArticles.length; i++){
		var article = oArticles[i].articleDTO;
		var articleId = article.articleId;
		var description = article.description;
		var packagingUnit = article.packagingUnit;
		var weightpu = article.weightpu;
		var mdd = article.mdd;
		var numberpu = oArticles[i].numberpu;
		
		// get Deliverer (first 20 characters)
		var deliverer = "";
		var delivererId = article.delivererId;
		
		for (var o in gOrganisations)
		{
			if (gOrganisations[o].id == delivererId)
			{
				deliverer = gOrganisations[o].name;
				if (deliverer.length > 15)
				{ deliverer = deliverer.substr(0, 15)+"...";	}
			}
		}
		
		var tableRow = "<tr id='aId_"+articleId + "_disp" +"'>"
			+ "<td class='hidden'>" + articleId	+ "</td>"
			+ "<td class='article_description'>" + description	+ "</td>"
			+ "<td class='article_packagingunits'>" + numberpu	+ "</td>"
			+ "<td class='article_packaging_unit'>" + packagingUnit	+ "</td>"
			+ "<td class='article_mdd'>" + mdd + "</td>"
			+ "<td class='article_deliverer'>" + deliverer + "</td>"
			+ "</tr>";
		
		$("#rightDepotTableBody").append(tableRow);
	}
	
	
	
	// get available articles and place them into
	var availArticles;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/articles/getAvailableArticles"
	}).done(function(data) {
			availArticles = data;	// already JSON
	});
	
	for (var i in availArticles) {
		var article = availArticles[i].articleDTO;
		var articleId = article.articleId;
		var description = article.description;
		var packagingUnit = article.packagingUnit;
		var weightpu = article.wieghtpu;
		var mdd = article.mdd;
		var pricepu = article.pricepu;
		var availNumberPUs = availArticles[i].availNumberOfPUs;
		
		// get Deliverer (first 20 characters)
		var deliverer = "";
		var delivererId = article.delivererId;
		
		for (var o in gOrganisations)
		{
			if (gOrganisations[o].id == delivererId)
			{
				deliverer = gOrganisations[o].name;
				if (deliverer.length > 15)
				{ deliverer = deliverer.substr(0, 15)+"...";	}
			}
		}
		
		var tableRow = "<tr id='aId_"+articleId+"_dep'>"
		+ "<td class='hidden'>" + articleId + "</td>"
		+ "<td class='article_description'>" + description + "</td>"
		+ "<td class='article_packagingunits'>" + availNumberPUs + "</td>"
		+ "<td class='article_packaging_unit'>" + packagingUnit	+ "</td>"
		+ "<td class='article_mdd'>" + mdd + "</td>"
		+ "<td class='article_deliverer'>" + deliverer + "</td>"
		+ "</tr>";
		
		$("#leftDepotTableBody").append(tableRow);
	}
	
	// hide loading spinner
	showLoadingSpinner(false);
}




/**
 * load all organisations
 * load only organisations of type KUNDE
 */
function loadAllReceivers() {
	
	// show loading spinner
	showLoadingSpinner(true);
	
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
		url : "../rest/secure/organisation/reducedData/getAllActiveOrganisations"
	}).done(
			function(data) {
				var o = data;	// already JSON
				
				for (var e in o) {
					for(var i=0; i< o[e].types.length; i++){
						if(o[e].types[i] == "Kunde"){
							
							var nameString = "";
							
							if(o[e].name.length > 22){
								nameString = o[e].name.substring(0, 22) + "...";
							}
							else{
								nameString = o[e].name;
							}
							
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
							
							var o_divRow = "<div class='boxElement_receiver'>" +
												"<div class='row'>" +
													"<div class='col-sm-4'>" +
														"<input type='hidden' value="+ o[e].id +">" +
														"<span>" + nameString + "</span>"+
													"</div>" +
													"<div class='col-sm-2'>" +
														"<span>" + o[e].city + "</span>" +
													"</div>" +
													"<div class='col-sm-5'>" +
														"<span>" + categoryString + "</span>" +
													"</div>" +
													"<div class='col-sm-1'>" +
														"<input class='pull-right' value="+ o[e].id +" id="+ o[e].id +" name='receiverRadio' type='radio'>" +
													"</div>" +
												"</div>" +
											"</div>";
							
							
							$("#receiverDiv").append(o_divRow);
						}
					}
				}
				
			// hide loading spinner
			showLoadingSpinner(false);
	});
};

//create table row
function createTableRow(id, mode, article_description, article_packagingunits, article_packaging_unit, article_mdd, article_deliverer){
	var id_new;		// string for creating a row in the depot or in the disposition table
	if(mode == 0){
		id_new = id + "_disp";
	}
	else if(mode == 1){
		id_new = id + "_dep";
	}
	
	var tableRow = "<tr id='aId_"+ id_new +"'>"
		+ "<td class='hidden'>" + id + "</td>"
		+ "<td class='article_description'>" + article_description + "</td>"
		+ "<td class='article_packagingunits'>" + article_packagingunits + "</td>"
		+ "<td class='article_packaging_unit'>" + article_packaging_unit + "</td>"
		+ "<td class='article_mdd'>" + article_mdd + "</td>"
		+ "<td class='article_deliverer'>" + article_deliverer + "</td>"
		+ "</tr>";
	
	return tableRow;
}



//load receiver modal
$("#btn_addReceiver,#tbx_receiver_popover").click(function() {
	$(".boxElement_receiver").remove();
	$("#filter_modal").val("");
	$("#newAlertFormReceiver").hide();
	loadAllReceivers();
	
	$('#chooseReceiverModal').modal('show');
	
	// set focus when modal is loaded
	$('#chooseReceiverModal').on('shown.bs.modal', function(){
		$('#filter_modal').focus();
	});
});


//select receiver if row is clicked
$(document).on("click", ".boxElement_receiver", function(){
	// select radio button
	$(this).find('input:radio[name=receiverRadio]').prop('checked', true);
});



//save receiver to textbox
$("#btn_saveReceiver").click(function() {
	//get Id of checked radiobox of receiver div
	var id = $("#receiverDiv input[name='receiverRadio']:checked").val();
	
	//check if a checkbox is selected
	if(id == null){
		$("#newAlertFormReceiver").show();
		return;
	}
	
	var receiverString;
	var o;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getOrganisationById/" + id
	}).done(
			function(data) {
				o = data;	// already JSON
				
				// store the id to the global var
				$('#tbx_orgId').val(o.id);
				
				receiverString = o.name + ", " + o.zip + " " + o.city + ", " + o.country;
	});
	
	$("#tbx_receiver").val(receiverString);
	$("#tbx_receiver_popover").attr("data-content", o.name + ",<br>" + o.zip + " " + o.city + ",<br>" + o.country);
	$('#chooseReceiverModal').modal('hide');
});



//add article to disposition
$("#btn_addtodisposition").click(function() {
	// get the selected articleId
	var id = tableData[0];
	
	// get the selected row
	var thisRow = $("#aId_" + id + "_dep").closest('tr');
	// get the current number of PUs
	var packagingunits_depot = $(thisRow).find(".article_packagingunits").html().trim();
	// get the number of PUs to move to the right table
	var packagingunits_tbx = $("#tbx_packagingunit").val().trim();
	
/* INTEGERS */
	// check numbers for integers
	if ( (packagingunits_tbx.indexOf(".") != -1) || (packagingunits_tbx.indexOf(",") != -1) )
	{
		showAlertElement(false, "Nur ganze Zahlen erlaubt!", 2500);
		return false;
	}
	
	// round the number of PUs
/*  DOUBLES */
//	packagingunits_depot = Math.round(packagingunits_depot * 100) / 100;
//	packagingunits_tbx = Math.round(packagingunits_tbx * 100) / 100;
	
	// checks if the number is valid and is 
	if(isNaN(packagingunits_tbx) == true){
		$("#tbx_packagingunit").val("");
		return false;
	}
	
	// convert the texts to numbers
	packagingunits_tbx = parseInt(packagingunits_tbx);
	packagingunits_depot = parseInt(packagingunits_depot);
	
	// get informations from the row
	var article_description = $(thisRow).find(".article_description").html();
	// calc the remaining PUs and round them
	var article_packagingunits = packagingunits_depot - packagingunits_tbx;
	article_packagingunits = Math.round(article_packagingunits * 100) / 100;
	
	// text, describing the details
	var article_packaging_unit = $(thisRow).find(".article_packaging_unit").html();	
	var article_mdd = $(thisRow).find(".article_mdd").html();
	var article_deliverer = $(thisRow).find(".article_deliverer").html();
	
	
	if($("#tbx_packagingunit").val() == ""){
		showAlertElement(2, "Keine Anzahl ausgewählt!", 2500);
		return false;
	}
	
	// if number of PUs, which should be moved, is greater than number of PUs, which are available
	if(packagingunits_tbx > packagingunits_depot){
		$("#tbx_packagingunit").val(packagingunits_depot);
		showAlertElement(2, "Nummer der VE zu groß!", 2500);
		return false;	// return from function
	}
	else{
		// if no PUs will remain in the depot ...
		if(article_packagingunits == 0){
			$(thisRow).remove();	// ... remove the row from the depot
			$('#btn_addtodisposition').prop('disabled', true);
		}
		else {
			// set the remaining PUs for the remaining row in the left table
			$(thisRow).find(".article_packagingunits").html(article_packagingunits);
		}
		
		// if an entry with the given id already exists in the right table
		if($("#aId_" + id + "_disp").length != 0){
			// get the desired row in the right table
			var thisRow = $("#aId_" + id + "_disp").closest('tr');
			// get the number of PUs already in the right table
			article_packagingunits = $(thisRow).find(".article_packagingunits").html();
			// round the existing number
			article_packagingunits = Math.round(article_packagingunits * 100) / 100;
			
			// calc the new total number, round it and set it for the existing row
			var sum = Math.round((packagingunits_tbx + article_packagingunits) * 100) / 100;
			$(thisRow).find(".article_packagingunits").html(sum);
			return;
		}
		else 
		{
			// create a new row with the number of PUs, which should be moved
			var newDispositionRow = createTableRow(id, 0, article_description, packagingunits_tbx, article_packaging_unit, article_mdd, article_deliverer);
			// add the row to the right table
			$("#rightDepotTableBody").append(newDispositionRow);
		}
	}
	
});



//remove article from disposition
$("#btn_removefromdisposition").click(function() {
	// get the current id
	var id = tableData[0];
	
	// get the selected row from the disposition
	var thisRow = $("#aId_" + id + "_disp").closest('tr');
	// get the number of PUs available
	var packagingunits_disposition = $(thisRow).find(".article_packagingunits").html().trim();
	// get the number of PUs to be moved
	var packagingunits_tbx = $("#tbx_packagingunit").val().trim();
	
	/* INTEGERS */
	// check numbers for integers
	if ( (packagingunits_tbx.indexOf(".") != -1) || (packagingunits_tbx.indexOf(",") != -1) )
	{
		showAlertElement(false, "Nur ganze Zahlen erlaubt!", 2500);
		return false;
	}
	
	// round the number of PUs
/*  DOUBLES */
//	packagingunits_disposition = Math.round(packagingunits_disposition * 100) / 100;
//	packagingunits_tbx = Math.round(packagingunits_tbx * 100) / 100;
	
	// check the number for validity
	if(isNaN(packagingunits_tbx) == true){
		$("#tbx_packagingunit").val("");
		return false;
	}
	
	// convert the texts to numbers
	packagingunits_tbx = parseInt(packagingunits_tbx);
	packagingunits_disposition = parseInt(packagingunits_disposition);
	
	
	// get informations
	var article_description = $(thisRow).find(".article_description").html();
	// calc the remaining PUs and round it
	var article_packagingunits = packagingunits_disposition - packagingunits_tbx;
	article_packagingunits = Math.round(article_packagingunits * 100) / 100;
	
	var article_packaging_unit = $(thisRow).find(".article_packaging_unit").html();
	var article_mdd = $(thisRow).find(".article_mdd").html();
	var article_deliverer = $(thisRow).find(".article_deliverer").html();
	
	
	if($("#tbx_packagingunit").val() == ""){
		showAlertElement(2, "Keine Anzahl ausgewählt!", 2500);
		return false;
	}
	
	// if number of PUs, which should be moved, is greater than number of PUs, which are available
	if(packagingunits_tbx > packagingunits_disposition){
		$("#tbx_packagingunit").val(packagingunits_disposition);
		showAlertElement(2, "Nummer der VE zu groß!", 2500);
		return false;
	}
	else{
		// if no PUs will remain in the disposition
		if(article_packagingunits == 0){
			$(thisRow).remove();	// remove the row
			$('#btn_removefromdisposition').prop('disabled', true);
		}
		else{
			// set the remaining PUs for the remaining row in the right table
			$(thisRow).find(".article_packagingunits").html(article_packagingunits);
		}
		
		// if an entry with the given id already exists in the left table
		if($("#aId_" + id + "_dep").length != 0){
			// get the row with the id in the left table
			var thisRow = $("#aId_" + id + "_dep").closest('tr');
			// get the number of PUs, which are already in the left table
			article_packagingunits = $(thisRow).find(".article_packagingunits").html();
			article_packagingunits = Math.round(article_packagingunits * 100) / 100;
			
			// calc the new total number of PUs and set it
			var sum = Math.round((packagingunits_tbx + article_packagingunits) * 100) / 100;
			$(thisRow).find(".article_packagingunits").html(sum);
			return;
		}
		else{
			// create a new depot row with the number of PUs which should be moved
			var newDepotRow = createTableRow(id, 1, article_description, packagingunits_tbx, article_packaging_unit, article_mdd, article_deliverer);
			$("#leftDepotTableBody").append(newDepotRow);
		}
	}
	
	
});



//close newoutgoing delivery and show outgoing delivery tab
$("#btn_close").click(function() {
	location.href="warenverwaltung_warenausgang.html";
	return false;
});



//move marked row one row upwards
$("#btn_up").click(function() {
	// get the selected id
	var id = tableData[0];
	
	// get the selected row element
	var thisRow = $("#aId_" + id + "_disp");
	// get the previous element of the selected row
    var prevRow = thisRow.prev();
    // insert the selected row before the previous element, if it exists
    if (prevRow.length) {
        prevRow.before(thisRow);
    }
});



//move marked row one row downwards
$("#btn_down").click(function() {
	// get the selected id
	var id = tableData[0];
	
	// get the selected row element
	var thisRow = $("#aId_" + id + "_disp");
	// get the next element of the selected row
    var nextRow = thisRow.next();
    // insert the selected row after the next element, if it exists
    if (nextRow.length) {
        nextRow.after(thisRow);
    }
});


// save disposition
$(document).ready(function() {
	$('#btn_submittodepot').click(
			function() {
			
				var orgId = $('#tbx_orgId').val();
				var date = $('#tbx_date').val();
				var comment = $('#tbx_comment').val();
								
				// check if all Fields are filled
				if ( (orgId==0) || (date=="") )
				{
					showAlertElement(2, "Empfänger und Datum sind Pflichtfelder!", 5000);
					return;
				}
				
				// check date for validity
				var regEx = /^(\d{1,2})\.(\d{1,2})\.(\d{4})$/;
				var dateArray = date.match(regEx);
				
				if (dateArray == null)
				{
					showAlertElement(2, "Falsches Datumsformat!", 5000);
					return;
				}
				
				// articles in disposition
				var articles = [];
				// get all articles in the new disposition
				$('#rightDepotTableBody tr').each(function(){
					var rowData = $(this).children('td').map(function(){return $(this).text();});
					articles.push(rowData);
				});
				
				if (articles.length == 0)
				{
					showAlertElement(2, "Keine Waren verfügbar!", 5000);
					return;
				}
				
				var outgoingDelivery = new Object();
				outgoingDelivery.outgoingDeliveryId = global_id;
				outgoingDelivery.organisationId = orgId;
				outgoingDelivery.lastEditorId = 0;
				outgoingDelivery.deliveryNr = 0;
				outgoingDelivery.date = date;
				outgoingDelivery.comment = comment;
				
				var outgoingArticleDTOs = [];
				for (e in articles)
				{
					// current article of the mapped article array from the disposition
					var art = articles[e];
					
					var outgoingArticle = new Object();
					outgoingArticle.outgoingArticleId = 0;
					outgoingArticle.articleNr = e;	// assign the index of the element
					outgoingArticle.numberpu = art[2];		// get the number of the PUs
					
					var articleDTO = new Object();
					articleDTO.articleId = art[0];		// id of the article
					articleDTO.description = art[1];	// description of the article
					articleDTO.packagingUnit = art[3];	// packaging unit
					articleDTO.weightpu = null;			// don't assign weight
					articleDTO.mdd = art[4];			// mdd of the article
					articleDTO.pricepu = null;			// don't assign pricepu
														// don't assign article_deliverer, it is generated by DB
					
					outgoingArticle.articleDTO = articleDTO;
					
					// insert current outgoingArticle in the array
					outgoingArticleDTOs.push(outgoingArticle);
				}
				
				// insert outgoingArticles array in the outgoingDelivery
				outgoingDelivery.outgoingArticleDTOs = outgoingArticleDTOs;
				
				$.ajax({
					headers : {
						'Accept' : 'application/json',
						'Content-Type' : 'application/json'
					},
					type : "POST",
					url : "../rest/secure/outgoingDelivery/set",
					contentType: "application/json; charset=utf-8",
				    dataType: "json",
					data : JSON.stringify(outgoingDelivery)
				}).done(function(data) {
					if (data) {
						
						if (data.success == true)
						{
							showAlertElement(1, data.message, 5000);
							
							// return to outgoingDelivery overview
							location.href="warenverwaltung_warenausgang.html";
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



/**
 * search filter for receiver
 */
$(document).ready(function() {
	(function($) {
		$('#filter_modal').keyup(function() {

			var rex = new RegExp($(this).val(), 'i');
			$('.searchable .boxElement_receiver').hide();
			$('.searchable .boxElement_receiver').filter(function() {
				return rex.test($(this).text());
			}).show();
		})
	}(jQuery));
});


//disable new, edit and delete buttons
$('#label_packagingunits').hide();
$('#tbx_packagingunit_group').hide();
$('#btn_addtodisposition').hide();
$('#btn_removefromdisposition').hide();


// get current user rights
$(document).ready(function() {
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getCurrentUser"
	}).done(function(data) {
		currentUser = data;	// already JSON
		currentUserRights = currentUser.permission;

		// only when user has admin rights
		if (currentUserRights == "Admin" || currentUserRights == "ReadWrite") {
//			if(isBooked == false){
				$('#label_packagingunits').show();
				$('#tbx_packagingunit_group').show();
				$('#btn_addtodisposition').show();
				$('#btn_removefromdisposition').show();
	
				$('#btn_addtodisposition').prop('disabled', true);
				$('#btn_removefromdisposition').prop('disabled', true);
//			}
			
			// deactivate on start
			$('#btn_up').prop('disabled', true);
			$('#btn_down').prop('disabled', true);
		}
	});
});

// tableData of the selected Row (either depot or disposition table)
var tableData;
$('#TableHeadLeftDepot').on('click','tbody tr', function(event) {
	tableData = $(this).children("td").map(function() {
		return $(this).text();
	}).get();

	$('tr.highlight').removeClass('highlight');
	$(this).addClass('highlight');
	
	// $("*").removeClass("highlight");	
	// $(this).addClass('highlight').siblings().removeClass('highlight');

	$('#btn_up').prop('disabled', true);
	$('#btn_down').prop('disabled', true);
	
//	if(isBooked == false){
		// only when user has admin rights
		if (currentUserRights == "Admin" || currentUserRights == "ReadWrite") {
			// enable 'button to disposition'
			$('#btn_addtodisposition').prop('disabled', false);
			// disable 'button from disposition'
			$('#btn_removefromdisposition').prop('disabled', true);
			// set the maximum packaging units for the selected article
			$('#tbx_packagingunit').val(parseFloat(tableData[2]));
		} 
		else {
			$('#btn_addtodisposition').prop('disabled', true);
			$('#btn_removefromdisposition').prop('disabled', true);
		}
//	}
});

$('#TableHeadRightDepot').on('click','tbody tr', function(event) {
	tableData = $(this).children("td").map(function() {
		return $(this).text();
	}).get();

	$('tr.highlight').removeClass('highlight');
	$(this).addClass('highlight');
	
//	$("*").removeClass("highlight");
//	$(this).addClass('highlight').siblings().removeClass('highlight');
	
	// only when user has admin rights
	if ( currentUserRights == "Admin" || currentUserRights == "ReadWrite" ) {
		
		// disable 'button to disposition'
		$('#btn_addtodisposition').prop('disabled', true);
		// enable 'button from disposition'
		$('#btn_removefromdisposition').prop('disabled', false);
		// set the maximum packaging units for the selected article
		$('#tbx_packagingunit').val(parseFloat(tableData[2]));
		
		// enable buttons up, down
		$('#btn_up').prop('disabled', false);
		$('#btn_down').prop('disabled', false);
		
	} else {
		// disable buttons
		$('#btn_addtodisposition').prop('disabled', true);
		$('#btn_removefromdisposition').prop('disabled', true);
		
		$('#btn_up').prop('disabled', true);
		$('#btn_down').prop('disabled', true);
	}
	
});

