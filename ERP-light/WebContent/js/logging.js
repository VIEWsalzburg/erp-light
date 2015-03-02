
// load latest logging data into table
$(function()
{
	$('#select_logcount').val('25');
	loadLogEntries(25);
	
});

function loadLogEntries(count) {
	$.ajax({
		type : "POST",
		url : "../rest/secure/logging/getLatestLogs/"+count
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
}


$('#select_logcount').change(function(){
	
	// remove all entries from the table
	$('#loggingTableBody').empty();
	
	// load entries according to the selected values
	var selectedCount = $(this).val();
	if (selectedCount == 'Alles')
		selectedCount = -1;
	loadLogEntries(selectedCount);
	
});
