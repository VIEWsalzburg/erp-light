<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>allPersons</title>
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
        </div>
    </body>
</html>
