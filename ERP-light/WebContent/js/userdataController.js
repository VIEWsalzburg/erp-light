$(document).ready(function() {
	$.ajax({
		type : "POST",
		url : "/ERP-light/rest/secure/userdata"
	}).done(function(data) {
		$("#username").text(data);
	});
});