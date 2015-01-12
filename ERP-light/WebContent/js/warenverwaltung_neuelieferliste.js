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
		$("#tabtext").text("Neue Lieferliste");
		loadAllOutgoingDeliveries();
	}
	else if(mode == "edit"){
		$("#tabtext").text("Bearbeite Lieferliste");
		loadAllOutgoingDeliveries();
		loadDeliveryList(global_id);
	}
});

//init popover
$(function () {
	$('[data-toggle="popover"]').popover()
});

//Get all outgoing delivery entries and load into table
function loadAllOutgoingDeliveries(){
	$.ajax({
		type : "POST",
		url : "../rest/secure/outgoingDelivery/getAll"
	}).done(function(data) {
				var out = eval(data);

				for (var e in out) {
					
					//get organisation by id
					var org;
					$.ajax({
						type : "POST",
						async : false,
						url : "../rest/secure/organisation/getOrganisationById/" + out[e].organisationId
					}).done(function(data) {
						
						org = eval(data);
					});
					
					//get articles
					var articleString = "";
					var article = out[e].outgoingArticleDTOs;
					for(var i=0; i < out[e].outgoingArticleDTOs.length; i++){
						articleString = articleString + article[i].articleDTO.description;
						
						if(i < out[e].outgoingArticleDTOs.length - 1){
							articleString = articleString + ", ";
						}
					}
					
					var tableRow = "<tr id='"+ out[e].outgoingDeliveryId +"'>" + "<td>" + out[e].outgoingDeliveryId
							+ "</td>" + "<td class='receiver'>" + org.name
							+ "</td>" + "<td class='article'>" + articleString
							+ "</td>" + "<td class='comment'>" + out[e].comment
							+ "</td>" + "</tr>";

					$("#outgoingDeliveryTableBody").append(tableRow);
				}
	});
};

//TODO Load delivery list with specific id
function loadDeliveryList(){
	
}

//load driver modal
$("#btn_addDriver").click(function() {
	$("#tbx_driver_modal").val("");
	$("#tbx_codriver_modal").val("");
});

//save driver to textbox
$("#btn_saveDriver").click(function() {
	var driver = $("#tbx_driver_modal").val();
	var codriver = $("#tbx_codriver_modal").val();
	
	if(driver == "" && codriver == ""){
		$('#chooseDriverModal').modal('hide');
	}
	else{
		if(codriver == ""){
			$("#tbx_driver").val("F: " + driver);
			$("#tbx_driver").attr("title", "F: " + driver);
		}
		else if(driver == ""){
			$("#tbx_driver").val("B: " + codriver);
			$("#tbx_driver").attr("title", "B: " + codriver);
		}
		else{
			$("#tbx_driver").val("F: " + driver + ", " + "B: " + codriver);
			$("#tbx_driver").attr("title", "F: " + driver + ", " + "B: " + codriver);
		}
	}
	$("#tbx_driver_popover").attr("data-content", $("#tbx_driver").val());
	$('#chooseDriverModal').modal('hide');
});

$("#tbx_driver").hover(function() {
	//alert("test");	
	//$("#tbx_driver").removeAttr("disabled");
});

//add outgoing delivery to delivery list
$("#btn_addtodeliverylist").click(function() {
	var id = tableData[0];
	
	var thisRow = $("#" + id).closest('tr');
	$("#deliveryListTableBody").append(thisRow);
	$(thisRow).removeClass("highlight");
});

//remove outgoing delivery from delivery list
$("#btn_removefromdeliverylist").click(function() {
	var id = tableData[0];
	
	var thisRow = $("#" + id).closest('tr');
	$("#outgoingDeliveryTableBody").append(thisRow);
	$(thisRow).removeClass("highlight");
});

//TODO save delivery list
$("#btn_savedeliverylist").click(function() {
	
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

//disable new, edit and delete buttons
$('#btn_addtodeliverylist').hide();
$('#btn_removefromdeliverylist').hide();

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
			$('#btn_addtodeliverylist').show();
			$('#btn_removefromdeliverylist').show();

			$('#btn_up').prop('disabled', true);
			$('#btn_down').prop('disabled', true);
			$('#btn_addtodeliverylist').prop('disabled', true);
			$('#btn_removefromdeliverylist').prop('disabled', true);
		}
	});
});

var tableData;
$('#TableHeadOutgoingDelivery').on('click','tbody tr', function(event) {
			tableData = $(this).children("td").map(function() {
				return $(this).text();
			}).get();

			$(this).addClass('highlight').siblings().removeClass('highlight');

			// only when user has admin rights
			if (currentUserRights == "Admin" && currentUserRights != "") {
				$('#btn_addtodeliverylist').prop('disabled', false);
				$('#btn_removefromdeliverylist').prop('disabled', true);
			} 
			else {
				$('#btn_addtodeliverylist').prop('disabled', true);
				$('#btn_removefromdeliverylist').prop('disabled', true);
			}
});

$('#TableHeadDeliveryList').on('click','tbody tr', function(event) {
	tableData = $(this).children("td").map(function() {
		return $(this).text();
	}).get();

	$(this).addClass('highlight').siblings().removeClass('highlight');

	// only when user has admin rights
	if (currentUserRights == "Admin" && currentUserRights != "") {
		$('#btn_up').prop('disabled', false);
		$('#btn_down').prop('disabled', false);
		$('#btn_addtodeliverylist').prop('disabled', true);
		$('#btn_removefromdeliverylist').prop('disabled', false);
	} 
	else {
		$('#btn_up').prop('disabled', true);
		$('#btn_down').prop('disabled', true);
		$('#btn_addtodeliverylist').prop('disabled', true);
		$('#btn_removefromdeliverylist').prop('disabled', true);
	}
});

