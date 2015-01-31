$("#btn_submit").click(function() {
	
	// show the loading spinner when trying to login
	showLoadingSpinner(true);
	
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
				$("#error-notification-message").html(response.message);
				$("#error-notification-message").show();
			}
		} else {
			alert("Verbindungsproblem");
		}
		
		// hide the spinner after response
		showLoadingSpinner(false);
		
	});
	return false;
});