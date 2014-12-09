//Load pageheader
$("#pageheader").load("../partials/header.html", function() {
	$("#adressverwaltung_nav").addClass("active");
});

//Get all categories and load into table
$(document).ready(
		
		function() {
			$.ajax({
				type : "POST",
				url : "../rest/secure/person/getAll"	//TODO unfinished
			}).done(
					function(data) {
						var p = eval(data);
						
						for (var e in p) {
							var tableRow = "<tr>" + "<td>" + "p[e].personId"
									+ "</td>" + "<td>" + "p[e].salutation"
									+ "</td>" + "<td>" + "p[e].title" + "</td>"
									+ "</tr>";
							
							$("#categoryTableBody").append(tableRow);
							
						}
					});
		});

//Get one person and load it to modal
$("#btn_edit").click(function() {
	$("#modal_title_text").text("Bearbeite Kategorie");
	
	var id = tableData[0];
	
	//get person with id "id"
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getPersonById/" + id	//TODO unfinished
	}).done(function(data) {
		
		var p = eval(data);

		//load data to modal
		$('#tbx_name').val(tableData[1]);
		$('#tbx_description').val(tableData[2]);
	});
});

$("#btn_savecategory").click(function() {
	//TODO unfinished
});

$("#btn_new").click(function() {
	$("#modal_title_text").text("Neue Kategorie");
});

//search filter
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

//select table row
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

//disable new, edit and delete buttons
$('#btn_new').hide();
$(".suchfilter").css("margin-left", "0px");
$('#btn_edit').hide();
$('#btn_deleteModal').hide();

//get current user rights
$(document).ready(function() {
	$.ajax({
		type : "POST",
		url : "../rest/secure/person/getCurrentUser"
	}).done(function(data) {
		currentUser = eval(data);
		currentUserRights = currentUser.permission;
		
		//only when user has admin rights
		if(currentUserRights == "Admin" && currentUserRights != ""){
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
$('#personen').on('click', 'tbody tr', function(event) {
	tableData = $(this).children("td").map(function() {
		return $(this).text();
	}).get();

	$(this).addClass('highlight').siblings().removeClass('highlight');
	
	//only when user has admin rights
	if(currentUserRights == "Admin" && currentUserRights != "" && currentUser.personId != tableData[0]){
		if(currentUserRights == "Admin"){
			$('#btn_edit').prop('disabled', false);
			$('#btn_deleteModal').prop('disabled', false);
		}
		else{
			if(tableData[9] != "Admin"){
				$('#btn_edit').prop('disabled', false);
				$('#btn_deleteModal').prop('disabled', false);
			}
		}
	}
	else{
		$('#btn_edit').prop('disabled', true);
		$('#btn_deleteModal').prop('disabled', true);
	}
});

//remove table row Modal
$("#btn_deleteModal").click(function() {
	$("#label_id").text(tableData[0]);
	$("#label_name").text(tableData[1]);
	$("#label_description").text(tableData[2]);
});

$("#btn_deleteCategory").click(function() {
//	var id = tableData[0];
//	$.ajax({
//		type : "POST",
//		url : "../rest/secure/person/deletePersonById/" + id
//	}).done(function(data) {
//		$('#personTableBody').empty();
//		$('#deleteModal').modal('hide');
//		loadTableContent();
//	});
});
