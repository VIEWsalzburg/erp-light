<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>allPersons</title>
        
        <script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
		<script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>

		<!-- Latest compiled and minified CSS -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
		<!-- Optional theme -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
		<!-- Latest compiled and minified JavaScript -->
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

		<script>
		function sendData() {
			
			var querystring = "id="+$("#id").val()+ 
								"&salutation="+$("#salutation").val()+
								"&title="+$("#title").val()+
								"&firstname="+$("#firstname").val()+
								"&lastname="+$("#lastname").val()+
								"&comment="+$("#comment").val()+
								"&active="+$("#active").val();
			
			$.ajax({
				type: "GET",
				url: "/DBSpringHibernateTest/Person/set/",
				data: querystring,
				contentType: "application/x-www-form-urlencoded; charset=UTF-8"
			})
			.done(function(msg){
				// getPerson();
				location.reload();
			});
			
		}
		</script>
        
    </head>
    <body>
        <div align="center">
	        <h1>PersonList</h1>
        	<table border="1">
	        	<th>Id</th>
	        	<th>Anrede</th>
	        	<th>Titel</th>
	        	<th>FirstName</th>
	        	<th>LastName</th>
	        	<th>Kommentar</th>
	        	<th>active</th>
	        	
				<c:forEach var="person" items="${allPersons}" >
	        	<tr>
	        		<td>${person.personId}</td>
	        		<td>${person.salutation}</td>
	        		<td>${person.title}</td>
					<td>${person.firstName}</td>
					<td>${person.lastName}</td>
					<td>${person.comment}</td>
					<td>${person.active}</td>
							
	        	</tr>
				</c:forEach>	        	
        	</table>
			<p>localhost:8080/DBSpringHibernateTest/Person/set/?id=2&salutation=Herr&title=&firstname=Matthias&lastname=Schnöll&comment=Geht gerne Biken im Bikepark Leogang und Schladming!&active=1</p>
        
        	<div>
        		<table>
        			<tr>
        				<td><label>Id</label></td>
        				<td><input id="id"></input></td>
        			</tr>
        			<tr>
        				<td><label>Salutation</label></td>
        				<td><input id="salutation"></input></td>
        			</tr>
        			<tr>
        				<td><label>Title</label></td>
        				<td><input id="title"></input></td>
        			</tr>
        			<tr>
        				<td><label>FirstName</label></td>
        				<td><input id="firstname"></input></td>
        			</tr>
        			<tr>
        				<td><label>LastName</label></td>
        				<td><input id="lastname"></input></td>
        			</tr>
        			<tr>
        				<td><label>Comment</label></td>
        				<td><input id="comment"></input></td>
        			</tr>
        			<tr>
        				<td><label>active</label></td>
        				<td><input id="active"></input></td>
        			</tr>
        		</table>
        		<button onclick="sendData()">Speichern</button>
        	
        	</div>
        
        
        
        
        
        </div>
    </body>
</html>
