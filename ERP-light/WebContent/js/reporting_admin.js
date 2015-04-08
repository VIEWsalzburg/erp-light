$(document).ready(function(){
	
	$('#btn_getDatabaseSize').click(function(){
		
		$.ajax({
			type : "POST",
			url : "../rest/secure/admin/getDatabaseSize"
		}).done( function(data){
			 $('#input_dbSize').val(data);
		});
		
	});
	
});