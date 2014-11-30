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
								"&active="+$("#active").val()+
								"&address="+$("#address").val()+
								"&city="+$("#city").val()+
								"&zip="+$("#zip").val()+
								"&country="+$("#country").val();
			
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
	        	<th>Adresse</th>
	        	<th>addressId</th>
	        	<th>Ort</th>
	        	<th>PLZ</th>
	       		<th>cityId</th>
	        	<th>Land</th>
	        	<th>countryId</th>
	        	
				<c:forEach var="person" items="${allPersons}" >
	        	<tr>
	        		<td>${person.personId}</td>
	        		<td>${person.salutation}</td>
	        		<td>${person.title}</td>
					<td>${person.firstName}</td>
					<td>${person.lastName}</td>
					<td>${person.comment}</td>
					<td>${person.active}</td>
					<td>${person.address.address}</td>
					<td>${person.address.addressId}</td>
					<td>${person.city.city}</td>
					<td>${person.city.zip}</td>
					<td>${person.city.cityId}</td>
					<td>${person.country.country}</td>
					<td>${person.country.countryId}</td>
							
	        	</tr>
				</c:forEach>	        	
        	</table>
        	<br/>
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
        			
        			<tr>
        				<td><label>address</label></td>
        				<td><input id="address"></input></td>
        			</tr>
        			<tr>
        				<td><label>city</label></td>
        				<td><input id="city"></input></td>
        			</tr>
        			<tr>
        				<td><label>zip</label></td>
        				<td><input id="zip"></input></td>
        			</tr>
        			<tr>
        				<td><label>country</label></td>
        				<td><input id="country"></input></td>
        			</tr>
        			
        		</table>
        		<button onclick="sendData()">Speichern</button>
        	
        	</div>
        
        
        
        
        
        </div>
    </body>
</html>
