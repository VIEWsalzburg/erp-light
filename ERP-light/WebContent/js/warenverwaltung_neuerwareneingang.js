function loadAllDeliverers() {
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getAllOrganisations"
	}).done(
			function(data) {
				var o = eval(data);
				
				for (var e in o) {
					var o_divRow = "<div class='boxElement_deliverer'>" + "<input type='hidden' value="+ o[e].id +">" + "<span>" + o[e].name + " "
					+ "</span><input class='pull-right' value="+ o[e].id +" name='delivererRadio' type='radio'></div>";
					
					$("#delivererDiv").append(o_divRow);
				}
	});
};

$("#btn_addDeliverer").click(function() {
	$(".boxElement_deliverer").remove();
	loadAllDeliverers();
});

function createTableRow(count){
	var tableRow = "<tr id="+ count +">" + "<td>" + count
	+ "</td>" + "<td>" + $("#tbx_description").val()
	+ "</td>" + "<td>" + $("#tbx_numberofpackagingunits").val()
	+ "</td>" + "<td>" + $("#tbx_packagingunit").val()
	+ "</td>" + "<td>" + $("#tbx_weightpackagingunit").val()
	+ "</td>" + "<td>" + $("#tbx_mdd").val()
	+ "</td>" + "<td>" + $("#tbx_pricepackagingunit").val()
	+ "</td>" + "<td>" + "10€" //TODO Gesamtpreis
	+ "</td>" + "</tr>";
	
	return tableRow;
}

var articleCount = 1;
$("#btn_savearticle").click(function() {
	if($("#modal_title_text").text() == "Neue Position"){
		var rowTemplate = createTableRow(articleCount);
		$("#newIncomingDeliveryTableBody").append(rowTemplate);
		articleCount++;
	}
	else{
		var id = tableData[0];
		var rowTemplate = createTableRow(id);
		$("#" + id).remove();
		$("#newIncomingDeliveryTableBody").append(rowTemplate);
	}
	
	$('#new').modal('hide');
});

$("#btn_saveDeliverer").click(function() {
	//get Id of checked radiobox of deliverer div
	var id = $("#delivererDiv input[name='delivererRadio']:checked").val();
	
	var delivererString;
	$.ajax({
		type : "POST",
		async : false,
		url : "../rest/secure/organisation/getOrganisationById/" + id
	}).done(
			function(data) {
				var o = eval(data);
				delivererString = o.name + ", " + o.country + ", " + o.zip + " " + o.city;
	});
	
	$("#tbx_deliverer").val(delivererString);
	$('#chooseDelivererModal').modal('hide');
});

$("#btn_new").click(function() {
	$("#modal_title_text").text("Neue Position");
});

//Load selected article to modal
$("#btn_edit").click(function() {
	$("#modal_title_text").text("Bearbeite Position");
	
	$("#tbx_description").val(tableData[1]);
	$("#tbx_numberofpackagingunits").val(tableData[2]);
	$("#tbx_packagingunit").val(tableData[3]);
	$("#tbx_weightpackagingunit").val(tableData[4]);
	$("#tbx_mdd").val(tableData[5]);
	$("#tbx_pricepackagingunit").val(tableData[6]);
});

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
				$('#btn_edit').prop('disabled', false);
				$('#btn_deleteModal').prop('disabled', false);
			} 
			else {
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