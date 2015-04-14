$(document).ready(function(){
	
	// get all current users logged in to the system
	
	$.ajax({
		type: "POST",
		url: "../rest/secure/getCurrentUsers"
	}).done(function(data){
		
		data = JSON.parse(data);
		
		for (var i in data.persons)
		{
			$('#ul_currentUsers').append("<li>"+data.persons[i]+"</li>");
		}
		
	});
	
});