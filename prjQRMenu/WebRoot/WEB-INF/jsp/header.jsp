
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    
   	<%-- Started Default Script For Page  --%>
    	<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.bundle.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.bundle.min.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jQuery.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery-ui.min.js"/>"></script>	
		<script type="text/javascript" src="<spring:url value="/resources/js/validations.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/TreeMenu.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/main.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery.fancytree.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery.numeric.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery.ui-jalert.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/pagination.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery-ui.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery.excelexport.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/hindiTyping.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/checkNetworkConnection.js"/>"></script>
	
	<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.bundle.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.bundle.min.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
	 	
	<%-- End Default Script For Page  --%>
	
	<%-- Started Default CSS For Page  --%>
		
	    <link rel="icon" href="${pageContext.request.contextPath}/resources/images/favicon.ico" type="image/x-icon" sizes="16x16">
	    <link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/materialdesignicons.min.css"/>" />
	 	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/all.min.css"/>" />
	    <link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/bootstrap.grid.css"/>" />
<%-- 		<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/bootstrap.grid.min.css"/>" />
 --%>		
 <link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/bootstrap.css"/>" />
		<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/bootstrap.min.css"/>" />
	 	
	 	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/design.css"/>" />
	    <link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/tree.css"/>" /> 
	 	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/jquery-ui.css"/>" />
	 	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/main.css"/>" />
	 	<link rel="stylesheet"  href="<spring:url value="/resources/css/pagination.css"/>" />
	 	<link href="https://fonts.googleapis.com/css?family=Roboto&display=swap" rel="stylesheet">
	 	
 	
 	<%-- End Default CSS For Page  --%>
 	
 	<%--  Started Script and CSS For Select Time in textBox  --%>
	
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery.timepicker.min.js"/>"></script>
	  	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/jquery.timepicker.css"/>" />
	
	<%-- End Script and CSS For Select Time in textBox  --%>
	
 	  
  	<title>Online Order</title>
	
<script type="text/javascript">
	function getContextPath() 
   	{
	  	return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
	}
  
  	
  		var debugFlag = false;
  		function debug(value)
   	{
   		if(debugFlag)
   		{
   			alert(value);
   		}   		
   	} 
  	
  	
</script>
<script  type="text/JavaScript">
document.onkeypress = stopRKey;
function stopRKey(evt) {+
              var evt = (evt) ? evt : ((event) ? event : null);
              var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
              if (evt.keyCode == 13)  {
                           //disable form submission
                           return false;
              }
}
</script>
<!-- code for banner -->
<script type="text/javascript">

function getContextPath() 
{
	return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
}
</script>

</head>
		<body>
			<%-- <div id="pageTop"> 	
	 			<header class="app-header">
      				<nav class="app-nav">
        				<div class="left-menu">
         			 		 <div class="navaction app-header-main">
            					<a href="#" id="one" style="float:left; width:30%;"><img src="../${pageContext.request.contextPath}/resources/images/Sanguine_Logo.png" style="height:auto;width:79%;"></a>
         			 	
           			 			<a style="float:left;width:33%; font-size:16px; font-weight: 600; color: #4a4a4a;margin-top: 3px;">
           			 			${gCompanyName}</a>
           			 			
         			 			<a href="#" id="one" style="float:left;width:30%;"><img src="../${pageContext.request.contextPath}/resources/images/Restaurant.png" style="height:31px;width:100px;margin-left:70px;"></a>
         			 
         			 		
         		 			</div> 
        				</div>
        				<!-- <div class="right-menu" id="page_top_banner">
          					<ul>
          					 	<li><a href="logout.html" class="mdi mdi mdi-power menu-link" aria-hidden="true" title="LOGOUT"></a></li>
					         </ul>
					      </div>
 -->					
				</nav>  
			</header>
		</div>
		</div>
 --%>	</body>
</html>