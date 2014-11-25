var user;

$("#btn_submit").click(function() {
	$.ajax({
		type : "POST",
		url : "rest/authenticate",
		data : {
			id : 1,
			username : $("#tbx_username").val(),
			password : $("#tbx_password").val()
		}
	}).done(function(data) {
		if (data != null) {
			user=data;
			window.location.replace("secure/home.html");
		} else {
			alert("whoops something went wrong");
		}
	});
});