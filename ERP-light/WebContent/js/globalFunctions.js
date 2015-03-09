function showAlertElement(success, text, timeOut)
{
		$('.myAlert').remove();
	
		var successDiv = "";
		var alertClass = "";
	
		//set class
		if (success == 1 || success == true)
		{
			alertClass = "alert alert-success";
		}
		else if (success == 2 || success == false)
		{
			alertClass = "alert alert-danger";
		}
		
		successDiv = "<div class='"+alertClass+" myAlert' role='alert'>"+text+"</div>";
		
		var domElement = $.parseHTML(successDiv);
			
		$("body").append(domElement);
		
		$('.myAlert').fadeIn("slow").delay(timeOut).fadeOut("slow",
			function() {
				$('.myAlert').remove();
			}
		);
}

/* shows the loading spinner on the site; status: false => hide; status: true => show */
function showLoadingSpinner(status) {
	
	if (status==true)
	{
		 $('.spinner-container').show();
//		$('.spinner-container').css('visibility','visible');
	}
	else
	{
		 $('.spinner-container').hide();
//		$('.spinner-container').css('visibility', 'hidden');
	}
	
}