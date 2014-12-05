$("#btn_submit").click(function() {
	$.ajax({
		type : "POST",
		url : "rest/authenticate",
		data : {
			id : 1,
			username : $("#tbx_username").val(),
			password : $("#tbx_password").val()
		}
	}).done(function(response) {
		if (response != null) {
			if(response.success)
			{
				window.location.replace("secure/home.html");
			}
			else
			{
				$("#error-notification").html(response.message);
				$("#error-notification").show();
			}
		} else {
			alert("Verbindungsproblem");
		}
	});
	return false;
});