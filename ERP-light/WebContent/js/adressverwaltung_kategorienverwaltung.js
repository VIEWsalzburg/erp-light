//Load pageheader
$("#pageheader").load("../partials/header.html", function() {
	$("#adressverwaltung_nav").addClass("active");
});

// Get all categories and load into table
function loadTableContent(){
			$.ajax({
				type : "POST",
				url : "../rest/secure/category/getAllCategories" // TODO
																	// unfinished
			}).done(
					function(data) {
						var c = eval(data);

						for ( var e in c) {
							var tableRow = "<tr>" + "<td>" + c[e].categoryId
									+ "</td>" + "<td>" + c[e].category
									+ "</td>" + "<td>" + c[e].description
									+ "</td>" + "</tr>";

							$("#categoryTableBody").append(tableRow);
						}
					});
};

//Get all organisations and load into table
$(document).ready(loadTableContent());

// Get one category and load it to modal
$("#btn_edit").click(function() {
	$("#modal_title_text").text("Bearbeite Kategorie");

	// load data to modal
	$('#tbx_categoryId').val(tableData[0]);
	$('#tbx_category').val(tableData[1]);
	$('#tbx_description').val(tableData[2]);
});

$("#btn_savecategory").click(function() {
	var neworganisation = new Object();
	
	neworganisation.categoryId = $("#tbx_categoryId").val();
	neworganisation.category = $("#tbx_category").val();
	neworganisation.description = $("#tbx_description").val();
	
	$.ajax({
		headers : {
			'Accept' : 'application/json',
			'Content-Type' : 'application/json'
		},
		type : "POST",
		url : "../rest/secure/category/setCategory",
		contentType: "application/json; charset=utf-8",
	    dataType: "json",
		data : JSON.stringify(newperson)
	}).done(function(data) {
		if (data) {
			$('#categoryTableBody').empty();
			$('#new').modal('hide');
			
			if (data.success == true)
			{
				showAlertElement(1, data.message, 5000);
			}
			else
			{
				showAlertElement(2, data.message, 5000);
			}
			
			loadTableContent();
		} else {
			alert("Verbindungsproblem mit dem Server");
		}
	});
	return false;
});

$("#btn_new").click(function() {
	$("#modal_title_text").text("Neue Kategorie");
	
	//clear modal
	$('#tbx_categoryId').val("");
	$('#tbx_category').val("");
	$('#tbx_description').val("");
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

// select table row
$('#btn_edit').prop('disabled', true);
$('#btn_deleteModal').prop('disabled', true);

var tableData;
$('#personen').on('click', 'tbody tr', function(event) {
	tableData = $(this).children("td").map(function() {
		return $(this).text();
	}).get();

	$(this).addClass('highlight').siblings().removeClass('highlight');
	$('#btn_edit').prop('disabled', false);
	$('#btn_deleteModal').prop('disabled', false);
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
$('#personen').on(
		'click',
		'tbody tr',
		function(event) {
			tableData = $(this).children("td").map(function() {
				return $(this).text();
			}).get();

			$(this).addClass('highlight').siblings().removeClass('highlight');

			// only when user has admin rights
			if (currentUserRights == "Admin" && currentUserRights != "") {
				if (currentUserRights == "Admin") {
					$('#btn_edit').prop('disabled', false);
					$('#btn_deleteModal').prop('disabled', false);
				} else {
					if (tableData[9] != "Admin") {
						$('#btn_edit').prop('disabled', false);
						$('#btn_deleteModal').prop('disabled', false);
					}
				}
			} else {
				$('#btn_edit').prop('disabled', true);
				$('#btn_deleteModal').prop('disabled', true);
			}
		});

// remove table row Modal
$("#btn_deleteModal").click(function() {
	$("#label_id").text(tableData[0]);
	$("#label_name").text(tableData[1]);
	$("#label_description").text(tableData[2]);
});

$("#btn_deleteCategory").click(function() { //TODO
//	 var id = tableData[0];
//	 
//	 $.ajax({
//		 type : "POST",
//		 url : "../rest/secure/category/deleteCategoryById/" + id
//	 }).done(function(data) {
//		 $('#personTableBody').empty();
//		 $('#deleteModal').modal('hide');
//		 
//		 if (data.success == true)
//			{
//				showAlertElement(1, data.message, 5000);
//			}
//		 else
//			{
//				showAlertElement(2, data.message, 5000);
//			}
//		 
//		 loadTableContent();
//	 });
});
