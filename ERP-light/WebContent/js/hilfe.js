
$(document).mousemove(function(event) {
	
	if ($.cookie('globalHelp')=='false')
	{
		$('#helpHover').hide();
		return;
	}
	
	var elem = document.elementFromPoint(event.pageX, event.pageY);
	
	var elem = $(elem).closest('.helpArea');
	
//	var id = $(elem).attr('id');
//	
//	var helpText = id;
	
	// var helpText = helpData[id];
	
	var helpText = $(elem).data('helptext');
	
	if (helpText == null)
	{
		$('#helpHover').hide();
		return;
	}
	
	$('#helpHover').show();
	
	$('#helpHover').text(helpText);
	
	$('#helpHover').css({top: event.pageY+10, left: event.pageX+10});
	
});


$(document).ready(function(){
	
	if ($.cookie('globalHelp')=='true')
	{
		$('#btn_activeHoverHelp').text(" Globale Hilfe deaktivieren");
	}
	else
	{
		$('#btn_activeHoverHelp').text(" Globale Hilfe aktivieren");
	}
	
	$('#btn_activeHoverHelp').click(function(){
		
		console.log($.cookie('globalHelp'));
		
		if ($.cookie('globalHelp')=='true')
		{
			$.cookie('globalHelp', 'false');
			$('#btn_activeHoverHelp').text(" Globale Hilfe aktivieren");
		}
		else
		{
			$.cookie('globalHelp', 'true');
			$('#btn_activeHoverHelp').text(" Globale Hilfe deaktivieren");
		}
		
		
	});
	
});