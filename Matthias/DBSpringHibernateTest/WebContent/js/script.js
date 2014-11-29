
function sendData() {
	
	var querystring = "id="+$("#Id").val()+ 
						"&salutation="+$("#salutation").val()+
						"&title="+$("#title").val()+
						"&firstname="+$("#firstname").val()+
						"&lastname="+$("#lastname").val()+
						"&comment="+$("#comment").val()+
						"&active="+$("#active").val();
	
	$.ajax({
		type: "GET",
		url: "/Person/set/",
		data: querystring,
		contentType: "application/x-www-form-urlencoded; charset=UTF-8"
	})
	.done(function(msg){
		// getPerson();
		alert("super!");
	});
	
}