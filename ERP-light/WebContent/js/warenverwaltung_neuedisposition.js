//load page in specific mode
var global_id;
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
	global_id = $.urlParam('id');
	
	if(mode == "new"){
		$("#tabtext").text("Neue Disposition");
		loadAllAvailableArticlesInDepot();
	}
	else if(mode == "edit"){
		$("#tabtext").text("Bearbeite Disposition");
		loadTableContent(global_id);
	}
});

//init popover
$(function () {
	$('[data-toggle="popover"]').popover();
});

// checked
function loadAllAvailableArticlesInDepot(){
	var availArticles;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/articles/getAvailableArticles"
	}).done(function(data) {
			availArticles = eval(data);
	});
	
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
		
		var tableRow = "<tr id='aId_"+articleId+"_dep'>" + "<td>" + articleId
		+ "</td>" + "<td class='article_description'>" + description
		+ "</td>" + "<td class='article_packagingunits'>" + availNumberPUs
		+ "</td>" + "<td class='article_packaging_unit'>" + packagingUnit
		+ "</td>" + "<td class='article_mdd'>" + mdd
		+ "</td>" + "</tr>";
		
		$("#leftDepotTableBody").append(tableRow);
	}
	
}


// unchecked
function loadTableContent(id){
	var out;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/outgoingDelivery/getById/" + id
	}).done(function(data) {
			out = eval(data);
	});
	
	//get organisation by id
	var org;
	var receiverString;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getOrganisationById/" + out.organisationId
	}).done(function(data) {
		org = eval(data);
		receiverString = org.name + ", " + org.country + ", " + org.zip + " " + org.city;
		$("#tbx_receiver").val(receiverString);
	});
	
	//set receiver popover
	$("#tbx_receiver_popover").attr("data-content", $("#tbx_receiver").val());
	
	$("#tbx_date").val(out.date);
	$("#tbx_comment").val(out.comment);
					
	//get articles (disposition)
	var article = out.outgoingArticleDTOs;
	for(var i=0; i < article.length; i++){
		var articleId = article[i].articleDTO.articleId;
		var description = article[i].articleDTO.description;
		var numberpu = article[i].numberpu;
		var packagingUnit = article[i].articleDTO.packagingUnit;
		var weightpu = article[i].articleDTO.weightpu;
		var mdd = article[i].articleDTO.mdd;
		
		var tableRow = "<tr id='aId_"+articleId + "_disp" +"'>" + "<td>" + articleId
		+ "</td>" + "<td class='article_description'>" + description
		+ "</td>" + "<td class='article_packagingunits'>" + numberpu
		+ "</td>" + "<td class='article_packaging_unit'>" + packagingUnit
		+ "</td>" + "<td class='article_mdd'>" + mdd
		+ "</td>" + "</tr>";
		
		$("#rightDepotTableBody").append(tableRow);
	}
	
	var inc;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/incomingDelivery/getAll"
	}).done(function(data) {
			inc = eval(data);
	});
	
	for (var e in inc) {
		//get articles (depot)
		var article = inc[e].incomingArticleDTOs;
		for(var i=0; i < article.length; i++){
			var articleId = article[i].articleDTO.articleId;
			var description = article[i].articleDTO.description;
			var numberpu = article[i].numberpu;
			var packagingUnit = article[i].articleDTO.packagingUnit;
			var weightpu = article[i].articleDTO.weightpu;
			var mdd = article[i].articleDTO.mdd;
			
			var tableRow = "<tr id='aId_"+articleId+"_dep'>" + "<td>" + articleId
			+ "</td>" + "<td class='article_description'>" + description
			+ "</td>" + "<td class='article_packagingunits'>" + numberpu
			+ "</td>" + "<td class='article_packaging_unit'>" + packagingUnit
			+ "</td>" + "<td class='article_mdd'>" + mdd
			+ "</td>" + "</tr>";
			
			$("#leftDepotTableBody").append(tableRow);
		}
	}
}




/**
 * load all organisations
 * TODO load only organisations of type KUNDE
 */
function loadAllReceivers() {
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getAllOrganisations"
	}).done(
			function(data) {
				var o = eval(data);
				
				for (var e in o) {
					for(var i=0; i< o[e].types.length; i++){
						if(o[e].types[i] == "Kunde"){
							var o_divRow = "<div class='boxElement_receiver'>" + "<input type='hidden' value="+ o[e].id +">" + "<span>" + o[e].name + " "
							+ "</span><input class='pull-right' value="+ o[e].id +" name='receiverRadio' type='radio'></div>";
							
							$("#receiverDiv").append(o_divRow);
						}
					}
				}
	});
};

//create table row
function createTableRow(id, mode, article_description, article_packagingunits, article_packaging_unit, article_mdd){
	var id_new;
	if(mode == 0){
		id_new = id + "_disp";
	}
	else if(mode == 1){
		id_new = id + "_dep";
	}
	
	var tableRow = "<tr id='aId_"+ id_new +"'>" + "<td>" + id
		+ "</td>" + "<td class='article_description'>" + article_description
		+ "</td>" + "<td class='article_packagingunits'>" + article_packagingunits
		+ "</td>" + "<td class='article_packaging_unit'>" + article_packaging_unit
		+ "</td>" + "<td class='article_mdd'>" + article_mdd
		+ "</td>" + "</tr>";
	
	return tableRow;
}



//load receiver modal
$("#btn_addReceiver").click(function() {
	$(".boxElement_receiver").remove();
	$("#filter_modal").val("");
	loadAllReceivers();
});


// global var for storing the selected id of the receiver
var orgReceiveId = 0;
//save receiver to textbox
$("#btn_saveReceiver").click(function() {
	//get Id of checked radiobox of receiver div
	var id = $("#receiverDiv input[name='receiverRadio']:checked").val();
	// store the id to the global var
	orgReceiveId = id;
	
	var receiverString;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getOrganisationById/" + id
	}).done(
			function(data) {
				var o = eval(data);
				receiverString = o.name + ", " + o.country + ", " + o.zip + " " + o.city;
	});
	
	$("#tbx_receiver").val(receiverString);
	$("#tbx_receiver_popover").attr("data-content", $("#tbx_receiver").val());
	$('#chooseReceiverModal').modal('hide');
});



//add article to disposition
$("#btn_addtodisposition").click(function() {
	// get the selected articleId
	var id = tableData[0];
	
	// get the selected row
	var thisRow = $("#aId_" + id + "_dep").closest('tr');
	var packagingunits_depot = $(thisRow).find(".article_packagingunits").html().trim();
	var packagingunits_tbx = $("#tbx_packagingunit").val().trim();
	
	var article_description = $(thisRow).find(".article_description").html();
	var article_packagingunits = parseFloat($(thisRow).find(".article_packagingunits").html()) - parseFloat(packagingunits_tbx);
	var article_packaging_unit = $(thisRow).find(".article_packaging_unit").html();
	var article_mdd = $(thisRow).find(".article_mdd").html();
	
	if($("#tbx_packagingunit").val() == ""){
		packagingunits_tbx = $(thisRow).find(".article_packagingunits").html();
		var newDispositionRow = createTableRow(id, 0, article_description, packagingunits_tbx, article_packaging_unit, article_mdd);
		
		$(thisRow).removeClass("highlight");
		$(thisRow).remove();
		$("#rightDepotTableBody").append(newDispositionRow);
	}
	else{
		if(parseFloat(packagingunits_tbx) > parseFloat(packagingunits_depot)){
			$("#tbx_packagingunit").val("");
			return false;
		}
		else{
			if(article_packagingunits == 0){
				$(thisRow).remove();
				$('#btn_addtodisposition').prop('disabled', true);
			}
			
			$(thisRow).find(".article_packagingunits").html(parseFloat(article_packagingunits));
			
			if($("#aId_" + id + "_disp").length != 0){
				var thisRow = $("#aId_" + id + "_disp").closest('tr');
				article_packagingunits = $(thisRow).find(".article_packagingunits").html();
				$(thisRow).find(".article_packagingunits").html(parseFloat(packagingunits_tbx) + parseFloat(article_packagingunits));
			}
			else{
				var newDispositionRow = createTableRow(id, 0, article_description, parseFloat(packagingunits_tbx), article_packaging_unit, article_mdd);
				$("#rightDepotTableBody").append(newDispositionRow);
			}
		}
	}
});

//remove article from disposition
$("#btn_removefromdisposition").click(function() {
	var id = tableData[0];
	
	var thisRow = $("#aId_" + id + "_disp").closest('tr');
	var packagingunits_depot = $(thisRow).find(".article_packagingunits").html().trim();
	var packagingunits_tbx = $("#tbx_packagingunit").val().trim();
	
	var article_description = $(thisRow).find(".article_description").html();
	var article_packagingunits = parseFloat($(thisRow).find(".article_packagingunits").html()) - parseFloat(packagingunits_tbx);
	var article_packaging_unit = $(thisRow).find(".article_packaging_unit").html();
	var article_mdd = $(thisRow).find(".article_mdd").html();
	
	if($("#tbx_packagingunit").val() == ""){
		packagingunits_tbx = $(thisRow).find(".article_packagingunits").html();
		var newDepotRow = createTableRow(id, 1, article_description, packagingunits_tbx, article_packaging_unit, article_mdd);
		
		$(thisRow).removeClass("highlight");
		$(thisRow).remove();
		$("#leftDepotTableBody").append(newDepotRow);
	}
	else{
		if(parseFloat(packagingunits_tbx) > parseFloat(packagingunits_depot)){
			$("#tbx_packagingunit").val("");
			return false;
		}
		else{
			if(article_packagingunits == 0){
				$(thisRow).remove();
				$('#btn_removefromdisposition').prop('disabled', true);
			}
			
			$(thisRow).find(".article_packagingunits").html(parseFloat(article_packagingunits));
			
			if($("#aId_" + id + "_dep").length != 0){
				var thisRow = $("#aId_" + id + "_dep").closest('tr');
				article_packagingunits = $(thisRow).find(".article_packagingunits").html();
				$(thisRow).find(".article_packagingunits").html(parseFloat(packagingunits_tbx) + parseFloat(article_packagingunits));
			}
			else{
				var newDepotRow = createTableRow(id, 1, article_description, parseFloat(packagingunits_tbx), article_packaging_unit, article_mdd);
				$("#leftDepotTableBody").append(newDepotRow);
			}
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
			
				var orgId = orgReceiveId;
				var date = $('#tbx_date').val();
				var comment = $('#tbx_comment').val();
								
				// check if all Fields are filled
				if ( (orgId==0) || (date=="") || (comment=="") )
				{
					showAlertElement(2, "Leere Felder vorhanden!", 5000);
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
				outgoingDelivery.outgoingDeliveryId = 0;
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
							
							// return to incomingDelivery overview
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
$('#btn_addtodisposition').hide();
$('#btn_removefromdisposition').hide();


// get current user rights
$(document).ready(function() {
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getCurrentUser"
	}).done(function(data) {
		currentUser = eval(data);
		currentUserRights = currentUser.permission;

		// only when user has admin rights
		if (currentUserRights == "Admin" && currentUserRights != "") {
			$('#btn_addtodisposition').show();
			$('#btn_removefromdisposition').show();

			$('#btn_up').prop('disabled', true);
			$('#btn_down').prop('disabled', true);
			$('#btn_addtodisposition').prop('disabled', true);
			$('#btn_removefromdisposition').prop('disabled', true);
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

			// only when user has admin rights
			if (currentUserRights == "Admin" && currentUserRights != "") {
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
});

$('#TableHeadRightDepot').on('click','tbody tr', function(event) {
	tableData = $(this).children("td").map(function() {
		return $(this).text();
	}).get();

	$('tr.highlight').removeClass('highlight');
	$(this).addClass('highlight');

	// only when user has admin rights
	if (currentUserRights == "Admin" && currentUserRights != "") {
		// enable buttons: 'up, down, to disposition, from disposition'
		$('#btn_up').prop('disabled', false);
		$('#btn_down').prop('disabled', false);
		$('#btn_addtodisposition').prop('disabled', true);
		$('#btn_removefromdisposition').prop('disabled', false);
		// set the maximum packaging units for the selected article
		$('#tbx_packagingunit').val(parseFloat(tableData[2]));
	} 
	else {
		$('#btn_up').prop('disabled', true);
		$('#btn_down').prop('disabled', true);
		$('#btn_addtodisposition').prop('disabled', true);
		$('#btn_removefromdisposition').prop('disabled', true);
	}
});

