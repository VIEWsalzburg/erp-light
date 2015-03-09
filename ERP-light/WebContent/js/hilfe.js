
$(document).mousemove(function(event) {
	
	if ($.cookie('globalHelp')=='false' || $.cookie('globalHelp')==null)
	{
		$('#helpHover').hide();
		return;
	}	
	
	var elem = document.elementFromPoint(event.pageX, event.pageY);
	
	var elem = $(elem).closest('.helpHotspot');
	
//	var id = $(elem).attr('id');
//	
//	var helpText = id;
	
	// var helpText = helpData[id];
	
	var helpText = $(elem).data('helptext');
	
	if (helpText == null || helpText.length=="")
	{
		$('#helpHover').hide();
		console.log("hide: "+helpText);
		return;
	}
	else
	{
		$('#helpHover').show();
	}
	
	$('#helpHover').html(helpText);
	
	var helpWidth = $('#helpHover').width();
	var helpHeight = $('#helpHover').height();
	var windowWidth = $(window).width();
	var windowHeight = $(window).height();
	
	var posX = event.pageX;
	var posY = event.pageY;
	
	if (windowWidth < (posX+30+helpWidth) )
	{
		posX = posX  - 20 - helpWidth;
	}
	else
	{
		posX = posX + 10;
	}
	
	if (windowHeight < (posY+30+helpHeight) )
	{
		posY = posY - 20 - helpHeight;
	}
	else
	{
		posY = posY + 10;
	}
	
	$('#helpHover').css({top: posY, left: posX});
	
});


$(document).ready(function(){
	
	// append helpHover to document
	var helpTextElement = "<div id='helpHover' class='popover'>	Ich bin ein Hilfetext! </div>";
	$('body').append(helpTextElement);
	
	console.log($.cookie('globalHelp'));
	
	if ($.cookie('globalHelp')=='true')
	{
		$('#btn_activeHoverHelp').html("<span class='glyphicon glyphicon-question-sign'></span> Interaktive Hilfe aktivieren");
	}
	else
	{
		$('#btn_activeHoverHelp').html("<span class='glyphicon glyphicon-question-sign'></span> Interaktive Hilfe deaktivieren");
	}
	
	$('#btn_activeHoverHelp').click(function(){
		
		console.log($.cookie('globalHelp'));
		
		if ($.cookie('globalHelp')=='true')
		{
			$.cookie('globalHelp', 'false', {expires: 30});
			$('#btn_activeHoverHelp').html("<span class='glyphicon glyphicon-question-sign'></span> Interaktive Hilfe aktivieren");
		}
		else
		{
			$.cookie('globalHelp', 'true', {expires: 30});
			$('#btn_activeHoverHelp').html("<span class='glyphicon glyphicon-question-sign'></span> Interaktive Hilfe deaktivieren");
		}
		
		
	});
	
});