<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<html>
<head>
<title><tiles:insertAttribute name="title" ignore="true"></tiles:insertAttribute></title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">


</style>
</head>
<body bgcolor="white">
	<div style="height: 200px">
	<%-- <tiles:insertAttribute name="loginheader"></tiles:insertAttribute> --%>
	</div>
	<div style="background-color: inherit; top: 200px; bottom: 0;">
			<tiles:insertAttribute name="body"></tiles:insertAttribute>
	</div>
	
	<div style="height: 25px">
		<tiles:insertAttribute name="footer"></tiles:insertAttribute>
	</div>

</body>
</html>