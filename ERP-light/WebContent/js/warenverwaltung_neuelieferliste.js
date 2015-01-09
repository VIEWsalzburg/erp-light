//TODO Get outgoing delivery entries and load into table
function loadTableContent(){
			$.ajax({
				type : "POST",
				url : "../rest/secure/category/getAllCategories"
			}).done(
					function(data) {
						var c = eval(data);

						var count = 1;
						for ( var e in c) {
							var tableRow = "<tr id='"+ count +"'>" + "<td>" + count
									+ "</td>" + "<td class='receiver'>" + "Wärmestube" 
									+ "</td>" + "<td class='article'>" + "Kekse, Saft"
									+ "</td>" + "<td class='comment'>" + "Bemerkung"
									+ "</td>" + "</tr>";

							$("#outcomingdeliveriesTableBody").append(tableRow);
							count++;
						}
					});
};

//Get all outgoing deliveries and load into table
$(document).ready(loadTableContent());

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
	$("#outcomingdeliveriesTableBody").append(thisRow);
	$(thisRow).removeClass("highlight");
});

//close new delivery list and show delivery list tab
$("#btn_close").click(function() {
	location.href="warenverwaltung_lieferlisten.html";
	return false;
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
$('#TableHeadOutcomingDeliveries').on('click','tbody tr', function(event) {
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

