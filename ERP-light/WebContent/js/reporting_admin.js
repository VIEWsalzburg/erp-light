$(document).ready(function(){
	
	// get data base size
	$('#btn_getDatabaseSize').click(function(){
		
		$.ajax({
			type : "POST",
			url : "../rest/secure/admin/getDatabaseSize"
		}).done( function(data){
			 $('#input_dbSize').val(data);
		});
		
	});
	
	
	// get transfer time
	$('#btn_getTransferTime').click(function(){
		
		// show loading spinner
		showLoadingSpinner(true);
		
		var startTime = new Date().getTime();
		$.ajax({
			type : "POST",
			url : "../rest/secure/person/getAllActive"
		}).done( function(data){
			var stopTime = new Date().getTime()-startTime;
			
			 $('#input_transferTime').val(stopTime+" ms");
			 
			// hide loading spinner
			showLoadingSpinner(false);
			 
		});
		
	});
	
	
	
});