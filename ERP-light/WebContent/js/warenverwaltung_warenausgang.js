//append alert message to modal
var pwdError = "<div id='pwdErrorAlert'> <div class='col-sm-5'> <div class='alert alert-danger custom-alert' style='text-align: left;'>Leere Felder vorhanden!</div> </div>  </div>";
	$("#newAlertForm").append(pwdError);

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
									+ "</td>" + "<td>" + "Helios Hallein" + ", " + "<br/>" + "Österreich" + "<br/>" + "5020 Salzburg" 
									+ "</td>" + "<td>" + "03.01.2014"
									+ "</td>" + "<td>" + "Äpfel, Birnen, Kekse, Sauce"
									+ "</td>" + "<td>" + "Bemerkung"
									+ "</td>" + "</tr>";

							$("#outgoingDeliveryTableBody").append(tableRow);
						}
					});
};

//Get all organisations and load into table
$(document).ready(loadTableContent());

// Get one category and load it to modal
$("#btn_edit").click(function() {
	$("#modal_title_text").text("Bearbeite Kategorie");

	//hide alert messsage
	$("#newAlertForm").hide();
	
	// load data to modal
	$('#tbx_categoryId').val(tableData[0]);
	$('#tbx_category').val(tableData[1]);
	$('#tbx_description').val(tableData[2]);
});

$("#btn_savecategory").click(function() {
	if($("#tbx_category").val() == "")
	{
			$("#newAlertForm").show();
			return;
	}
	
	var newcategory = new Object();
	
	newcategory.categoryId = $("#tbx_categoryId").val();
	newcategory.category = $("#tbx_category").val();
	newcategory.description = $("#tbx_description").val();
	
	$.ajax({
		headers : {
			'Accept' : 'application/json',
			'Content-Type' : 'application/json'
		},
		type : "POST",
		url : "../rest/secure/category/setCategory",	
		contentType: "application/json; charset=utf-8",
	    dataType: "json",
		data : JSON.stringify(newcategory)
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
	
	//hide alert messsage
	$("#newAlertForm").hide();
	
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
 * call the delete modal for the selected category
 */
$("#btn_deleteModal").click(function() {
	var id = tableData[0];
	 var name = tableData[1];
	 var description = tableData[2];
	 
	$("#label_name_delete").text(name);
	$("#label_description_delete").text(description);
	
	// Get category with id "id"
	$.ajax({
		type : "POST",
		url : "../rest/secure/category/getOrganisationsByCategoryId/" + id
	}).done(function(data) {

		var organisations = eval(data);
	
		if (organisations.length>0)
		{
			var organisationString = "";
			
			for (i in organisations)
			{
				organisationString += organisations[i].name;
				if (i < organisations.length-1)
				organisationString += ", "
			}
		
			$("#label_organisations_delete").text(organisationString);
		}
		else
			$("#label_organisations_delete").text("-");
		
	});
});


/**
 * call the delete url for the category
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
