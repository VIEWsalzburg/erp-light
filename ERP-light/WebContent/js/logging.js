
// load latest logging data into table
$(function()
{
	$.ajax({
		type : "POST",
		url : "../rest/secure/logging/getLatestLogs/"+50
	}).done(
		function(data) {
			var logs = data;	// return data is already a JSON object

			for (var i in logs)
			{
				var logEntry = "<tr>" +
								"<td>" + logs[i].timestamp + "</td>" +
								"<td>" + logs[i].loggingText + "</td>" + 
								"<td>" + logs[i].personName + "</td>" + 
							"</tr>;"
				
				$('#loggingTableBody').append(logEntry);
			}
	});
	
	
	
	
	
	
});