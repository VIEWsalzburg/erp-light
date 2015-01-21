//append alert message to modal
var modalError = "<div id='modalErrorAlert'> <div class='col-sm-7'> <div class='alert alert-danger custom-alert' style='text-align: left;'>Leere Felder vorhanden!</div> </div>  </div>";
$("#newAlertForm").append(modalError);
var modalErrorDeliverer = "<div id='modalErrorDelivererAlert'> <div class='col-sm-5'> <div class='alert alert-danger custom-alert' style='text-align: left;'>Kein Lieferant ausgewählt!</div> </div>  </div>";
$("#newAlertFormDeliverer").append(modalErrorDeliverer);

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
		$("#tabtext").text("Neuer Wareneingang");
		global_id = 0;	// set Id to 0
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

function loadNewIncomingDelivery(id){
	var inc;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/incomingDelivery/getById/" + id
	}).done(function(data) {
			inc = eval(data);
	});
	
	//get organisation by id
	var org;
	var delivererString;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getOrganisationById/" + inc.organisationId
	}).done(function(data) {
		org = eval(data);

		// store the id to the global var
		$('#tbx_orgId').val(org.id);
		
		delivererString = org.name + ", " + org.zip + " " + org.city + ", " + org.country;
		$("#tbx_deliverer").val(delivererString);
	});
	
	//set deliverer popover
	$("#tbx_deliverer_popover").attr("data-content", org.name + " <br> " + org.zip + " " + org.city + "<br>" + org.country);
	
	$("#tbx_date").val(inc.date);
	$("#tbx_comment").val(inc.comment);
	
	//get articles, sort them by the articleNr and append them to the table
	var article = inc.incomingArticleDTOs;
	for(var i=0; i < article.length; i++){
		for(var j=0; j < article.length; j++){
			if(article[j].articleNr == i){
				var incomingArticleId = article[j].incomingArticleId;
				var description = article[j].articleDTO.description;
				var numberpu = article[j].numberpu;
				var packagingUnit = article[j].articleDTO.packagingUnit;
				var weightpu = article[j].articleDTO.weightpu;
				var mdd = article[j].articleDTO.mdd;
				var pricepu = parseFloat(article[j].articleDTO.pricepu);
				
				//calculate sum price
				var sum = pricepu * article[j].numberpu;
				
				// adding the incomingArticleId is important to be able to update the according Article infos in the DB by using the Ids
				var tableRow = "<tr id='"+incomingArticleId+"'>" + "<td>" + incomingArticleId
				+ "</td>" + "<td>" + description
				+ "</td>" + "<td>" + numberpu
				+ "</td>" + "<td>" + packagingUnit
				+ "</td>" + "<td>" + weightpu + " kg"
				+ "</td>" + "<td>" + mdd
				+ "</td>" + "<td>" + pricepu + " €"
				+ "</td>" + "<td>" + sum + " €"
				+ "</td>" + "</tr>";
				
				$("#newIncomingDeliveryTableBody").append(tableRow);
			}
		}
	}
}

//loads all deliverers
function loadAllDeliverers(id) {
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getAllOrganisations"
	}).done(
			function(data) {
				var o = eval(data);
				
				for (var e in o) {
					for(var i=0; i< o[e].types.length; i++){
						if(o[e].types[i] == "Lieferant"){
							var o_divRow = "<div class='boxElement_deliverer'>" + "<input type='hidden' value="+ o[e].id +">" + "<span>" + o[e].name + " "
							+ "</span><input class='pull-right' value="+ o[e].id +" id="+ o[e].id +" name='delivererRadio' type='radio'></div>";
							
							$("#delivererDiv").append(o_divRow);
						}
					}
				}
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
	
	var pricepu = $("#tbx_pricepackagingunit").val();
	var sum = $("#tbx_numberofpackagingunits").val();
	if(pricepu == ""){
		pricepu = "-";
		sum = "-";
	}
	else{
		pricepu = parseFloat($("#tbx_pricepackagingunit").val());
		sum = pricepu * $("#tbx_numberofpackagingunits").val();
		
		pricepu = pricepu + " €";
		sum = sum + " €";
	}
	
	
	// if the passed id == 0 => set the class 'newArticleId' for the new row, so the mapping funciton in the funciton
	// 'submit to depot' can determine if it is an existing IncomingArticle or a new IncomingArticle
	if (id == 0)
		newArticleClass = "newArticleId";
	else
		newArticleClass = "";
	
	// add class newArticleId to sign the new Row as a new Article for the DB (set Id to 0 when saving the Delivery, by checking the class)
	// otherwise if the class is not assigned to the row, the IncomingArticle already exists in the DB and this is a updated row
	var tableRow = "<tr id="+ count +">" + "<td class='"+newArticleClass+"'>" + count
	+ "</td>" + "<td>" + $("#tbx_description").val()
	+ "</td>" + "<td>" + $("#tbx_numberofpackagingunits").val()
	+ "</td>" + "<td>" + $("#tbx_packagingunit").val()
	+ "</td>" + "<td>" + $("#tbx_weightpackagingunit").val() + " kg"
	+ "</td>" + "<td>" + $("#tbx_mdd").val()
	+ "</td>" + "<td>" + pricepu
	+ "</td>" + "<td>" + sum
	+ "</td>" + "</tr>";
	
	return tableRow;
}

//submit to depot
$(document).ready(function() {
$("#btn_submittodepot").click(function() {
	var orgId = $('#tbx_orgId').val();
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

//saves an article to the table
var articleCount = 1;
$("#btn_savearticle").click(function() {
	//validate input fields
	if($("#tbx_description").val() == ""){
		$(".alert").text("Keine Beschreibung Vorhanden!");
		$("#newAlertForm").show();
		return;
	}
	if($("#tbx_numberofpackagingunits").val() == 0 || isNaN($("#tbx_numberofpackagingunits").val()) == true){
		$(".alert").text("Anzahl der VE ist leer oder keine Zahl!");
		$("#newAlertForm").show();
		return;
	}
	if($("#tbx_packagingunit").val() == ""){
		$(".alert").text("VE ist leer!");
		$("#newAlertForm").show();
		return;
	}
	if($("#tbx_weightpackagingunit").val() == 0 || isNaN($("#tbx_weightpackagingunit").val()) == true){
		$(".alert").text("Einzelgewicht der VE ist leer oder keine Zahl!");
		$("#newAlertForm").show();
		return;
	}
	
	var date = $("#tbx_mdd").val();
	var regEx = /^(\d{1,2})\.(\d{1,2})\.(\d{4})$/;
	var dateArray = date.match(regEx);
	
	if (dateArray == null)
	{
		$(".alert").text("MDD ist kein valides Datumsformat!");
		$("#newAlertForm").show();
		return;
	}
	
	if($("#tbx_pricepackagingunit").val() != ""){
		if(isNaN($("#tbx_pricepackagingunit").val()) == true){
			$(".alert").text("Einzelpreis ist keine Zahl!");
			$("#newAlertForm").show();
			return;
		}
	}
	//end validation
	
	
	if($("#modal_title_text").text() == "Neue Position"){
		var rowTemplate = createTableRow(articleCount, 0);	// add Id (concerning the DB, 0 if new Article) for the new Article,
															// so the function knows if it needs to set the class 'newArticleId'
															// or if its an existing IncomingArticle
		$("#newIncomingDeliveryTableBody").append(rowTemplate);
		articleCount++;
	}
	else{
		var id = tableData[0];
		var rowTemplate = createTableRow(id, id);			// add Id (concerning the DB, existing Id if existing Artilce) for the Article
															// so the function knows if it needs to set the class 'newArticleId'
															// or if its an existing IncomingArticle
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
				o = eval(data);
				
				// store the id to the global var
				$('#tbx_orgId').val(o.id);
				
				delivererString = o.name + ", " + o.zip + " " + o.city + ", " + o.country;
	});
	
	$("#tbx_deliverer").val(delivererString);
	$("#tbx_deliverer_popover").attr("data-content", o.name + " <br> " + o.zip + " " + o.city + "<br>" + o.country);
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
	var id = tableData[0];
	
	var thisRow = $("#" + id).closest('tr');
    var prevRow = thisRow.prev();
    if (prevRow.length) {
        prevRow.before(thisRow);
    }
});

//move marked row one row downwards
$("#btn_down").click(function() {
	var id = tableData[0];
	
	var thisRow = $("#" + id).closest('tr');
    var nextRow = thisRow.next();
    if (nextRow.length) {
        nextRow.after(thisRow);
    }
});

//Load selected article to modal
$("#btn_edit").click(function() {
	$("#modal_title_text").text("Bearbeite Position");
	$("#newAlertForm").hide();
	clearPositionModal();
	
	$("#tbx_description").val(tableData[1]);
	$("#tbx_numberofpackagingunits").val(tableData[2]);
	
	//var packagingUnit = tableData[3];
	//packagingUnit.match(/\d+/g);
	$("#tbx_packagingunit").val(tableData[3]);
	
	var Stringlength = tableData[4].length;
	$("#tbx_weightpackagingunit").val(tableData[4].substring(0, Stringlength-2));
	
	$("#tbx_mdd").val(tableData[5]);
	
	if(tableData[6] != "-"){
		var Stringlength = tableData[6].length;
		$("#tbx_pricepackagingunit").val(tableData[6].substring(0, Stringlength-2));
	}
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

//disable new, edit and delete buttons
$('#btn_new').hide();
$(".suchfilter").css("margin-left", "0px");
$('#btn_edit').hide();
$('#btn_deleteModal').hide();

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
			$("#btn_new").show();
			$(".suchfilter").css("margin-left", "5px");

			$('#btn_edit').show();
			$('#btn_deleteModal').show();

			$('#btn_up').prop('disabled', true);
			$('#btn_down').prop('disabled', true);
			$('#btn_edit').prop('disabled', true);
			$('#btn_deleteModal').prop('disabled', true);
		}
	});
});

var tableData;
$('#TableHead').on('click','tbody tr', function(event) {
			tableData = $(this).children("td").map(function() {
				return $(this).text();
			}).get();

			$(this).addClass('highlight').siblings().removeClass('highlight');

			// only when user has admin rights
			if (currentUserRights == "Admin" && currentUserRights != "") {
				$('#btn_up').prop('disabled', false);
				$('#btn_down').prop('disabled', false);
				$('#btn_edit').prop('disabled', false);
				$('#btn_deleteModal').prop('disabled', false);
			} 
			else {
				$('#btn_up').prop('disabled', true);
				$('#btn_down').prop('disabled', true);
				$('#btn_edit').prop('disabled', true);
				$('#btn_deleteModal').prop('disabled', true);
			}
});

/**
 * call the delete modal for the selected article
 */
$("#btn_deleteModal").click(function() {
	var name = tableData[1];
	$("#label_name_delete").text(name);
});


/**
 * remove selected table row
 */
$("#btn_deleteArticle").click(function() {
	var id = tableData[0];
	$("#" + id).remove();
	$('#deleteModal').modal('hide');
});
