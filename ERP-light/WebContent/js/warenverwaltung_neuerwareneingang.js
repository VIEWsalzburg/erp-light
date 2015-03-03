//append alert message to modal
var modalError = "<div id='modalErrorAlert'> <div class='col-sm-7'> <div class='alert alert-danger custom-alert' style='text-align: left;'>Leere Felder vorhanden!</div> </div>  </div>";
$("#newAlertForm").append(modalError);
var modalErrorDeliverer = "<div id='modalErrorDelivererAlert'> <div class='col-sm-5'> <div class='alert alert-danger custom-alert' style='text-align: left;'>Kein Lieferant ausgewählt!</div> </div>  </div>";
$("#newAlertFormDeliverer").append(modalErrorDeliverer);

//load page in specific mode
var global_id;
var isBooked;
var globalIdMap = [];		// is used to map the IncomingArticleId to a ArticleId
							// => ArticleId is needed to call article distribution
							// this is only used when editing an existing IncomingDelivery
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
	
	// default value = true
	isBooked = true;
	
	// if bookedStatus = false, isBooked = false
	if (bookedStatus == 'false')
	{
		isBooked = false;
	}
	
	if(mode == "new"){
		$("#tabtext").text("Neuer Wareneingang");
		global_id = 0;	// set Id to 0
		isBooked = false;	// isBooked is always false for new incomingDeliveries
	}
	else if(mode == "edit"){
		$("#tabtext").text("Bearbeite Wareneingang");
		loadNewIncomingDelivery(global_id);
	}

});

//init popover
$(function () {
	$('[data-toggle="popover"]').popover()
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
				todayHighlight: true
			});


// load existing incomingDelivery
function loadNewIncomingDelivery(id){
	
	// show loading spinner
	showLoadingSpinner(true);
	
	var inc;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/incomingDelivery/getById/" + id
	}).done(function(data) {
			inc = data;	// already JSON
	});
	
	//get organisation by id
	var org;
	var delivererString;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getOrganisationById/" + inc.organisationId
	}).done(function(data) {
		org = data;	// already JSON

		// store the id to the global var
		$('#tbx_orgId').val(org.id);
		
		delivererString = org.name + ", " + org.zip + " " + org.city + ", " + org.country;
		$("#tbx_deliverer").val(delivererString);
	});
	
	//set deliverer popover
	$("#tbx_deliverer_popover").attr("data-content", org.name + ",<br>" + org.zip + " " + org.city + ",<br>" + org.country);
	
	$("#tbx_date").val(inc.date);
	$("#tbx_comment").val(inc.comment);
	
	//set comment popover
	$("#tbx_comment_popover").attr("data-content", $("#tbx_comment").val());
	
	
	//get articles, sort them by the articleNr and append them to the table
	var articles = inc.incomingArticleDTOs;
	
	// sort articles according to their articleNr
	articles.sort( function(a, b) { 
		return (a.articleNr - b.articleNr);
	} );
	
	for(var i=0; i < articles.length; i++){
		var incomingArticleId = articles[i].incomingArticleId;
		var description = articles[i].articleDTO.description;
		var numberpu = articles[i].numberpu;
		var packagingUnit = articles[i].articleDTO.packagingUnit;
		var weightpu = articles[i].articleDTO.weightpu;
		var mdd = articles[i].articleDTO.mdd;
		var pricepu = parseFloat(articles[i].articleDTO.pricepu);
		
		//calculate sum price
		var totalWeight = Math.round( weightpu * articles[i].numberpu*100)/100;
		var sum = Math.round( pricepu * articles[i].numberpu*100)/100;
		
		// adding the incomingArticleId is important to be able to update the according Article infos in the DB by using the Ids
		var tableRow = "<tr id='"+incomingArticleId+"'>" +
			"<td class='hidden'>" + incomingArticleId + "</td>" +
			"<td>" + description + "</td>" +
			"<td>" + numberpu + "</td>" +
			"<td>" + packagingUnit + "</td>" +
			"<td>" + weightpu + " kg" + "</td>" +
			"<td>" + mdd + "</td>" +
			"<td>" + pricepu + " €" + "</td>" +
			"<td>" + totalWeight + " kg" + "</td>" +
			"<td>" + sum + " €" + "</td>" +
		"</tr>";
		
		// insert entry to IdMap variable
		var entry = new Object();
		entry.incomingArticleId = incomingArticleId;
		entry.articleId = articles[i].articleDTO.articleId;
		globalIdMap.push(entry);	// insert entry to IdMap
		
		$("#newIncomingDeliveryTableBody").append(tableRow);
	}
	
	// hide loading spinner
	showLoadingSpinner(false);
	
}



//loads all deliverers
function loadAllDeliverers() {
	
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
		url : "../rest/secure/organisation/getAllActiveOrganisations"
	}).done(
			function(data) {
				var o = data;	// already JSON
				
				for (var e in o) {
					for(var i=0; i< o[e].types.length; i++){
						if(o[e].types[i] == "Lieferant"){
							
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
							
							var o_divRow = "<div class='boxElement_deliverer'>" +
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
														"<input class='pull-right' value="+ o[e].id +" id="+ o[e].id +" name='delivererRadio' type='radio'>" +
													"</div>" +
												"</div>" +
											"</div>";
							
							
							$("#delivererDiv").append(o_divRow);
						}
					}
				}
				
				// hide loading spinner
				showLoadingSpinner(false);
	});
};



//load deliverer modal
$("#btn_addDeliverer").click(function() {
	$(".boxElement_deliverer").remove();
	$("#filter_modal").val("");
	$("#newAlertFormDeliverer").hide();
	loadAllDeliverers();
});



// variable count is shown as Id in the table, but the second parameter Id determins if its a new Article (according to the DB)
// or a updated existing one
function createTableRow(count, id){
	
	var weightpu = parseFloat($("#tbx_weightpackagingunit").val());
	var totalWeight = Math.round(weightpu * parseInt($("#tbx_numberofpackagingunits").val()) * 100) / 100;
	weightpu = weightpu + " kg";
	totalWeight = totalWeight + " kg";
	
	var pricepu = $("#tbx_pricepackagingunit").val();
	var sum;
	if(pricepu == ""){
		pricepu = "-";
		sum = "-";
	}
	else{
		pricepu = parseFloat($("#tbx_pricepackagingunit").val());
		sum = Math.round(pricepu * parseInt($("#tbx_numberofpackagingunits").val()) * 100) / 100;
		
		pricepu = pricepu + " €";
		sum = sum + " €";
	}
	
	
	// if the passed id == 0 => set the class 'newArticleId' for the new row, so the mapping function in the function
	// 'submit to depot' can determine if it is an existing IncomingArticle or a new IncomingArticle
	if (id == 0)
		newArticleClass = "newArticleId";
	else
		newArticleClass = "";
	
	// add class newArticleId to sign the new Row as a new Article for the DB (set Id to 0 when saving the Delivery, by checking the class)
	// otherwise if the class is not assigned to the row, the IncomingArticle already exists in the DB and this is a updated row
	var tableRow = "<tr id="+ count +">" +
		"<td class='"+newArticleClass+" hidden'>" + count + "</td>" +
		"<td>" + $("#tbx_description").val() + "</td>" +
		"<td>" + $("#tbx_numberofpackagingunits").val() + "</td>" +
		"<td>" + $("#tbx_packagingunit").val() + "</td>" +
		"<td>" + weightpu + "</td>" +
		"<td>" + $("#tbx_mdd").val() + "</td>" +
		"<td>" + pricepu + "</td>" +
		"<td>" + totalWeight + "</td>" +
		"<td>" + sum + "</td>" +
	"</tr>";
	
	return tableRow;
}



//submit to depot
$(document).ready(function() {
$("#btn_submittodepot").click(function() {
	var orgId = $('#tbx_orgId').val();
	var date = $('#tbx_date').val();
	var comment = $('#tbx_comment').val();
					
	// check if all Fields are filled
	if ( (orgId==0) || (date=="") )
	{
		showAlertElement(2, "Lieferant und Datum sind Pflichtfelder!", 5000);
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
	$('#newIncomingDeliveryTableBody tr').each(function(){
		var rowData = $(this).children('td').map(
				function(){
					// if the Id field of the row has the class 'newArticleId', which signs it as a new IncomingArticle for the DB
					// return the Id 0
					// if it does not has the newArticleId class, which occurs when the IncomingArticle exists in the DB, it returns it Id
					// the class acts like a flag, because setting Id fiels to 0 would break the sorting option
					if ( $(this).hasClass("newArticleId") )
					{	
						return "0";
					}
					
					// return the text of the current table field
					return $(this).text();
			});
		articles.push(rowData);
	});
	
	if (articles.length == 0)
	{
		showAlertElement(2, "Keine Waren verfügbar!", 5000);
		return;
	}
	
	var incomingDelivery = new Object();
	incomingDelivery.incomingDeliveryId = global_id;	// either Id = 0 or it is the Id of the incoming deivery, which is editted
	incomingDelivery.organisationId = orgId;
	incomingDelivery.lastEditorId = 0;
	incomingDelivery.deliveryNr = 0;
	incomingDelivery.date = date;
	incomingDelivery.comment = comment;
	
	var incomingArticleDTOs = [];
	for (e in articles)
	{
		// current article of the mapped article array from the disposition
		var art = articles[e];
		
		var incomingArticle = new Object();
		incomingArticle.incomingArticleId = art[0];		// incomingArticleId (when updating an existing IncomingArticle: the Id of it
														// 		when saving a new IncomingArticle: 0 (originally 1...x added by the modal),
														//		but returned in the mapping function when containing class 'newArticleId'
		incomingArticle.articleNr = e;					// assign the index of the element
		incomingArticle.numberpu = art[2];				// get the number of the PUs
		
		var articleDTO = new Object();
		articleDTO.articleId = null;					// id of the article
		articleDTO.description = art[1];				// description of the article
		articleDTO.packagingUnit = art[3];				// packaging unit
		articleDTO.weightpu = art[4].substring(0, art[4].length-2);
		articleDTO.mdd = art[5];						// mdd of the article
		articleDTO.pricepu = art[6].substring(0, art[6].length-2);
		
		incomingArticle.articleDTO = articleDTO;
		
		// insert current incomingArticle in the array
		incomingArticleDTOs.push(incomingArticle);
	}
	
	// insert incomingArticles array in the incomingDelivery
	incomingDelivery.incomingArticleDTOs = incomingArticleDTOs;
	
	$.ajax({
		headers : {
			'Accept' : 'application/json',
			'Content-Type' : 'application/json'
		},
		type : "POST",
		url : "../rest/secure/incomingDelivery/set",
		contentType: "application/json; charset=utf-8",
	    dataType: "json",
		data : JSON.stringify(incomingDelivery)
	}).done(function(data) {
		if (data) {
			
			if (data.success == true)
			{
				showAlertElement(1, data.message, 5000);
				
				// return to incomingDelivery overview
				location.href="warenverwaltung_wareneingang.html";
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


// restrict input of PUs to integers
$(document).ready(function(){
	$('#tbx_numberofpackagingunits').keypress(function(eventData) {
		switch (eventData.keyCode)
		{
			case 8: return true;	// backspace key
			case 9: return true;	// tab key
			case 46: return true;	// delete key
			case 37: return true;	// left arrow key
			case 39: return true;	// right arrow key
			default: return /\d/.test(String.fromCharCode(eventData.charCode));
		}
	});
});


//saves an article to the table
var articleCount = 1;
$("#btn_savearticle").click(function() {
	//validate input fields
	if($("#tbx_description").val() == ""){
		$(".alert").text("Keine Beschreibung Vorhanden!");
		$("#newAlertForm").show();
		return;
	}
	
	// check number for integers
	if ($("#tbx_numberofpackagingunits").val().indexOf(".") != -1 || $("#tbx_numberofpackagingunits").val().indexOf(",") != -1)
	{
		$(".alert").text("Anzahl der VE darf nur eine ganze Zahl sein!");
		$("#newAlertForm").show();
		return;
	}
		
	if($("#tbx_numberofpackagingunits").val() == 0 || isNaN($("#tbx_numberofpackagingunits").val()) == true){
		$(".alert").text("Anzahl der VE ist leer oder keine Zahl!");
		$("#newAlertForm").show();
		return;
	}
	
	if($("#tbx_packagingunit").val() == ""){
		$(".alert").text("Art der VE ist leer!");
		$("#newAlertForm").show();
		return;
	}
	
	// Start - Einzelgewicht
	if($("#tbx_weightpackagingunit").val().length == 0 ){
		$(".alert").text("Das Einzelgewicht darf nicht leer sein!");
		$("#newAlertForm").show();
		return;
	}
	
	if ($("#tbx_weightpackagingunit").val().indexOf(",") != -1)
	{
		$(".alert").text("Dezimalpunkt als Dezimaltrennzeichen verwenden!");
		$("#newAlertForm").show();
		return;
	}
	
	if (isNaN(parseInt($("#tbx_weightpackagingunit").val()))==true || isNaN($("#tbx_weightpackagingunit").val()) ){
		$(".alert").text("Einzelgewicht der VE ist keine Zahl!");
		$("#newAlertForm").show();
		return;
	}
	
	if(parseInt($("#tbx_weightpackagingunit").val()) <= 0 ){
		$(".alert").text("Das Einzelgewicht der VE muss größer als 0 sein!");
		$("#newAlertForm").show();
		return;
	}
	// Ende - Einzelgewicht
	
	
	var date = $("#tbx_mdd").val();
	var regEx = /^(\d{1,2})\.(\d{1,2})\.(\d{4})$/;
	var dateArray = date.match(regEx);
	
	if (dateArray == null)
	{
		$(".alert").text("MDD ist kein gültiges Datumsformat!");
		$("#newAlertForm").show();
		return;
	}
	
	
	// Start - Einzelpreis
	if ($("#tbx_pricepackagingunit").val().length != 0)
	{
		if ($("#tbx_pricepackagingunit").val().indexOf(",") != -1)
		{
			$(".alert").text("Dezimalpunkt als Dezimaltrennzeichen verwenden!");
			$("#newAlertForm").show();
			return;
		}
		
		if (isNaN(parseInt($("#tbx_pricepackagingunit").val()))==true || isNaN($("#tbx_pricepackagingunit").val()) ){
			$(".alert").text("Einzelpreis der VE ist keine Zahl!");
			$("#newAlertForm").show();
			return;
		}
		
	}
	
	// Ende - Einzelpreis
	
	
	if($("#modal_title_text").text() == "Neue Position"){
		var rowTemplate = createTableRow(articleCount, 0);	// add Id (concerning the DB, 0 if new Article) for the new Article,
															// so the function knows if it needs to set the class 'newArticleId'
															// or if its an existing IncomingArticle
		$("#newIncomingDeliveryTableBody").append(rowTemplate);
		articleCount++;
	}
	else{
		var rowData = getSelectedRow();
		if (rowData.length == 0)
		{
			showAlertElement(false, "Kein Artikel auswählt!", 2500);
			return;
		}
		
		// get selected entry TableEntryId
		var id = rowData[0];
		var rowTemplate;
		// check selected element for having the class newArticleId, to insert 0 or the id if the incomingArticle already exists in the DB
		if ($("tr#"+id).find('td.newArticleId').length > 0)
		{
			rowTemplate = createTableRow(id, 0);
		}
		else
		{
			rowTemplate = createTableRow(id, id);			// add Id (concerning the DB, existing Id if existing Artilce) for the Article
															// so the function knows if it needs to set the class 'newArticleId'
															// or if its an existing IncomingArticle
		}
		
		$("#" + id).remove();
		$("#newIncomingDeliveryTableBody").append(rowTemplate);
	}
	
	$('#new').modal('hide');
});

//save deliverer to textbox
$("#btn_saveDeliverer").click(function() {
	//get Id of checked radiobox of deliverer div
	var id = $("#delivererDiv input[name='delivererRadio']:checked").val();
	
	//check if a checkbox is selected
	if(id == null){
		$("#newAlertFormDeliverer").show();
		return;
	}
	
	var delivererString;
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
				
				delivererString = o.name + ", " + o.zip + " " + o.city + ", " + o.country;
	});
	
	$("#tbx_deliverer").val(delivererString);
	$("#tbx_deliverer_popover").attr("data-content", o.name + ",<br>" + o.zip + " " + o.city + ",<br>" + o.country);
	$('#chooseDelivererModal').modal('hide');
});

//new position modal
$("#btn_new").click(function() {
	$("#modal_title_text").text("Neue Position");
	$("#newAlertForm").hide();
	clearPositionModal();
});

//move marked row one row upwards
$("#btn_up").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Kein Artikel auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
	var thisRow = $("#" + id).closest('tr');
    var prevRow = thisRow.prev();
    if (prevRow.length) {
        prevRow.before(thisRow);
    }
});

//move marked row one row downwards
$("#btn_down").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Kein Artikel auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	
	var thisRow = $("#" + id).closest('tr');
    var nextRow = thisRow.next();
    if (nextRow.length) {
        nextRow.after(thisRow);
    }
});

//Load selected article to modal
$("#btn_edit").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Kein Artikel auswählt!", 2500);
		return;
	}
	
	$("#modal_title_text").text("Bearbeite Position");
	$("#newAlertForm").hide();
	clearPositionModal();
	
	//disable every textbox beside pricepackagingunit and mdd if isBooked == true
	if(isBooked == true){
		$("#tbx_description").prop('disabled', true);
		$("#tbx_numberofpackagingunits").prop('disabled', true);
		$("#tbx_packagingunit").prop('disabled', true);
		$("#tbx_weightpackagingunit").prop('disabled', true);
	}
	
	$("#tbx_description").val(rowData[1]);
	$("#tbx_numberofpackagingunits").val(rowData[2]);
	
	$("#tbx_packagingunit").val(rowData[3]);
	
	var Stringlength = rowData[4].length;
	$("#tbx_weightpackagingunit").val(rowData[4].substring(0, Stringlength-3));		// remove ' kg' from the end
	
	$("#tbx_mdd").val(rowData[5]);
	
	if(rowData[6] != "-"){
		var Stringlength = rowData[6].length;
		$("#tbx_pricepackagingunit").val(rowData[6].substring(0, Stringlength-2));	// remove ' €' from the end
	}
	
	// show modal
	$('#new').modal('show');
});




// call mask to edit the article distribution
$('#btn_editDistribution').click(function(){
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Kein Artikel auswählt!", 2500);
		return;
	}
	
	var incomingArticleId = rowData[0];		// get the incomingArticleId from the current row
	var articleId = 0;
	// find the incomingArticleId in the IdMap and call the distribution mask with the articleId
	for (i in globalIdMap)
	{
		if (globalIdMap[i].incomingArticleId == incomingArticleId)
			articleId = globalIdMap[i].articleId;
	}
	
	if (articleId == 0)
		return;
	
	location.href="warenverwaltung_warenverteilung.html?articleId="+articleId;
	
});







//clears position modal textboxes
function clearPositionModal(){
	$("#tbx_description").val("");
	$("#tbx_numberofpackagingunits").val("");
	$("#tbx_packagingunit").val("");
	$("#tbx_weightpackagingunit").val("");
	$("#tbx_mdd").val("");
	$("#tbx_pricepackagingunit").val("");
}

/**
 * search filter for deliverer
 */
$(document).ready(function() {
	(function($) {
		$('#filter_modal').keyup(function() {

			var rex = new RegExp($(this).val(), 'i');
			$('.searchable .boxElement_deliverer').hide();
			$('.searchable .boxElement_deliverer').filter(function() {
				return rex.test($(this).text());
			}).show();
		})
	}(jQuery));
});

//disable new, edit, delete and distribution buttons
$('#btn_new').hide();
$('#btn_edit').hide();
$('#btn_deleteModal').hide();
$('#btn_editDistribution').hide();

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
			// show btn_new
			$("#btn_new").show();
			
			// if delivery is not booked
			if(isBooked == false){
				// enable delivery selection
				$('#btn_addDeliverer').prop('disabled', false);
			}
			else{	// else if delivery is booked
				// disable the button and the delivery selection
				$("#btn_new").prop('disabled', true);
				$('#btn_addDeliverer').prop('disabled', true);
			}
			
			$('#btn_edit').show();
			$('#btn_deleteModal').show();
			
			// show button for editing distribution if editing an existing delivery
			if(global_id > 0)
				$('#btn_editDistribution').show();

			// disable buttons on first load => activate/deactivate them accordingly to the selected row
			$('#btn_up').prop('disabled', true);
			$('#btn_down').prop('disabled', true);
			$('#btn_edit').prop('disabled', true);
			$('#btn_deleteModal').prop('disabled', true);
			$('#btn_editDistribution').prop('disabled', true);	// disable the button; enable it when clicking an entry
		}
	});
});


//this function is used to get the selected row
//the function is called when a button is pressed and the selected entry has to be determined
function getSelectedRow(){
	
	// find selected tr in the table and map it to the variable
	var currentRow = $('#TableHead').find('tr.highlight').first().children("td").map(function() {
		return $(this).text();
	}).get();
	
	return currentRow;
}


// global variable containing the content of the selected row
$('#TableHead').on('click','tbody tr', function(event) {
//			tableData = $(this).children("td").map(function() {
//				return $(this).text();
//			}).get();

			$(this).addClass('highlight').siblings().removeClass('highlight');

			// only when user has admin rights
			if (currentUserRights == "Admin" || currentUserRights == "ReadWrite") {
				$('#btn_up').prop('disabled', false);
				$('#btn_down').prop('disabled', false);
				
				if(isBooked == false){
					$('#btn_deleteModal').prop('disabled', false);
				}
				else{
					$('#btn_deleteModal').prop('disabled', true);
				}
				$('#btn_edit').prop('disabled', false);
				$('#btn_editDistribution').prop('disabled', false);		// enable the button for editing the distribution
			} 
			else {
				$('#btn_up').prop('disabled', true);
				$('#btn_down').prop('disabled', true);
				$('#btn_edit').prop('disabled', true);
				$('#btn_deleteModal').prop('disabled', true);
				$('#btn_editDistribution').prop('disabled', true);		// disable the button for editing the distribution
			}
});

/**
 * call the delete modal for the selected article
 */
$("#btn_deleteModal").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Kein Artikel auswählt!", 2500);
		return;
	}
	
	var name = rowData[1];
	$("#label_name_delete").text(name);
	
	$('#deleteModal').modal('show');
});


/**
 * remove selected table row
 */
$("#btn_deleteArticle").click(function() {
	
	var rowData = getSelectedRow();
	if (rowData.length == 0)
	{
		showAlertElement(false, "Kein Artikel auswählt!", 2500);
		return;
	}
	
	var id = rowData[0];
	$("#" + id).remove();
	$('#deleteModal').modal('hide');
});
