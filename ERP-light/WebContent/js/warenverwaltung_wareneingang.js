//TODO Get all incoming deliveries and load into table
function loadTableContent(){
			$.ajax({
				type : "POST",
				url : "../rest/secure/category/getAllCategories"
			}).done(
					function(data) {
						var c = eval(data);

						for ( var e in c) {
							var tableRow = "<tr>" + "<td>" + "1"
									+ "</td>" + "<td>" + "Nannerl" + ", " + "<br/>" + "Österreich" + "<br/>" + "5020 Salzburg" 
									+ "</td>" + "<td>" + "05.01.2014"
									+ "</td>" + "<td>" + "Beschreibung"
									+ "</td>" + "<td>" + "Äpfel, Birnen, Kekse, Sauce"
									+ "</td>" + "</tr>";

							$("#incomingDeliveryTableBody").append(tableRow);
						}
					});
};

//Get all incoming deliveries and load into table
$(document).ready(loadTableContent());

//switch to new incoming deliveries tab
$("#btn_new").click(function() {
	$("#incomingdelivery").removeClass("active");
	$("#newincomingdelivery").removeClass("hidden");
	$(".nav-tabs").tabs("select", "warenverwaltung_neuerwareneingang.html"); //TODO not working
});

//switch to edit incoming deliveries tab
$("#btn_edit").click(function() {
	$("#newincomingdelivery").removeClass("hidden");
});

// search filter
$(document).ready(function() {
	(function($) {
		$('#filter').keyup(function() {

			var rex = new RegExp($(this).val(), 'i');
			$('.searchable tr').hide();
			$('.searchable tr').filter(function() {
				return rex.test($(this).text());
			}).show();
		})
	}(jQuery));
});

// disable new, edit and delete buttons
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
 * TODO call the delete modal for the selected incoming delivery
 */
$("#btn_deleteModal").click(function() {

});


/**
 * TODO call the delete url for the category
 */
$("#btn_deleteCategory").click(function() {
	 var id = tableData[0];
	 
	 $.ajax({
		 type : "POST",
		 url : "../rest/secure/category/deleteCategoryById/" + id
	 }).done(function(data) {
		 $('#categoryTableBody').empty();
		 $('#deleteModal').modal('hide');
		 
		 if (data.success == true)
			{
				showAlertElement(1, data.message, 5000);
			}
		 else
			{
				showAlertElement(2, data.message, 5000);
			}
		 
		 loadTableContent();
	 });
});
