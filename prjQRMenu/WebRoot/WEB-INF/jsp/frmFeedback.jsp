
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width">
	<meta name="viewport" content="initial-scale=1.0">
	<meta name="viewport" content="width=590">
	<meta name="viewport" content="width=device-width, user-scalable=no">
	
	 
   	<%-- Started Default Script For Page  --%>
    
		<script type="text/javascript" src="<spring:url value="/resources/js/jQuery.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery-ui.min.js"/>"></script>	
		<script type="text/javascript" src="<spring:url value="/resources/js/validations.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery.ui-jalert.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/pagination.js"/>"></script>
		<script type="text/javascript" src="<spring:url value="/resources/js/jquery-ui.js"/>"></script>
		
	<%-- End Default Script For Page  --%>
	
	<%-- Started Default CSS For Page  --%>

	    <link rel="icon" href="${pageContext.request.contextPath}/resources/images/favicon.ico" type="image/x-icon" sizes="16x16">
	 	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/jquery-ui.css"/>" />
	 	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/main.css"/>" />
	 	
 	
 	<%-- End Default CSS For Page  --%>
 	
 	
	<!-- Latest compiled and minified CSS -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
	<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+TC:wght@100;300;400;500;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.0.0/animate.min.css"/>
 	
<title>
</title>
<style type="text/css">

  body{line-height:0.5;color: #f1a61e;}
.firstpage{background: #111;height: 200vh;padding:40px 40px;}
.firstpage .head h2{color: #ffffff;margin: 0px;padding-top: 30px;font-size: 27px;}
.firstpage .head p{margin: 16px 0px;font-size: 14px;color:#ffffff;margin-bottom: 15px;}
.sect1{border:4px solid #fff;text-align: center;margin: 20px auto;width: 257px;}
.sect1:hover{background:#615a5ad4;}
.sect1 p{color:#fff;margin: 8px 0px;font-size: 20px;}

.time{border-radius: 1.25rem;margin:10px 10px;}
.list-group-item{border:none;background-color:#111;}
.list-group .active{background-color: #131212;}
.list-group-item .active{color: #000;background-color: #ffffff;border:1px solid #ffffff;}
.btn{border-radius: 1.25rem;}
.btn-outline-primary {color: #ffffff;border-color: #ffffff;padding:5px 100px;}
.btn-outline-primary hover{color: #000;background-color: #ffffff;}

.pagination {display: inline-block;}
.pagination a {color: #ffffff;float: left;padding: 8px 16px;text-decoration: none;}
.list-item{display:inline-block;}
.pagination a.active {background-color: #4CAF50;color: white;border-radius: 5px;}
.pagination a:hover:not(.active) {background-color: #ddd;border-radius: 5px;}
.star-rating {display:flex;
  flex-direction: row-reverse;
  font-size:2.8em;
  justify-content:space-around;
  
  text-align:center;
  }
.star-rating input {display:none;}
.star-rating label {color:#ccc;cursor:pointer;padding: 0px 10px;}
.star-rating :checked ~ label {color:#f90;}
.star-rating label:hover,
.star-rating label:hover ~ label {color:#fc0;}

</style>



<script type="text/javascript">

var mapQuestion=new Map();
var listQustion=[];
$(document).ready(function() 
{
     funGetQuestionsForFeedback();
				
});

function getContextPath() 
{
return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
}



function funGetQuestionsForFeedback(){

	var count=0;
	$.ajax({
		type : "GET",
		url : getContextPath()+ "/getQuestionForFeedback.html",
		dataType : "json",
		success : function(response)
		{ 
			$.each(response, function(i,item)
         {
				// a.strDocNo,a.strDocName,a.dblDocRate,a.strVendorCode, b.strPName
				
				funfillQustionForFeedback(response[i][0],response[i][1],response[i][2],response[i][3],count);
			       // (vendorName,vendorCode,ServiceName,serviceCode,dblRate)
				count++;
		    });
     	
		},
		
		error : function(e){
		 if (jqXHR.status === 0) {
             alert('Not connect.n Verify Network.');
         } else if (jqXHR.status == 404) {
             alert('Requested page not found. [404]');
         } else if (jqXHR.status == 500) {
             alert('Internal Server Error [500].');
         } else if (exception === 'parsererror') {
             alert('Requested JSON parse failed.');
         } else if (exception === 'timeout') {
             alert('Time out error.');
         } else if (exception === 'abort') {
             alert('Ajax request aborted.');
         } else {
             alert('Uncaught Error.n' + jqXHR.responseText);
         } 
		}
	});
}



function funfillQustionForFeedback(strQueCode,Strque,strType,rating,count)
{
	var tblSettleItemDtl=document.getElementById('tblFeedback');

var rowCount = tblSettleItemDtl.rows.length;
var insertRow = tblSettleItemDtl.insertRow(rowCount);
		     	
var col1=insertRow.insertCell(0);
//col1.style.width ='10';
rowCount++;
 insertRow = tblSettleItemDtl.insertRow(rowCount);
var col2=insertRow.insertCell(0);
var col3=insertRow.insertCell(1);


	col1.innerHTML = "<input readonly=\"readonly\" size=\"33px\"  class=\"head\" style=\"text-align: left; color:black; height:34px;width:73%;border:none; padding-left:5px;font-size:20px;font-family:'trebuchet ms';margin:22px 0px;\"   id=\"strQuestion."+(count)+"\" value='"+Strque+"' />";
   
	
	 col2.innerHTML = "<div class=\"star-rating\" > "
	+ " <input type=\"radio\" id=\"5-stars\" name=\"rating\" value=\"5\"><label for=\"5-stars\" class=\"star\" onclick=\"funGetQuestionRating(5,"+count+")\">&#9733;</label> "
	+ " <input type=\"radio\" id=\"4-stars\" name=\"rating\" value=\"4\"><label for=\"4-stars\" class=\"star\" onclick=\"funGetQuestionRating(4,"+count+")\">&#9733;</label> "
	+ " <input type=\"radio\" id=\"3-stars\" name=\"rating\" value=\"3\"><label for=\"3-stars\" class=\"star\" onclick=\"funGetQuestionRating(3,"+count+")\">&#9733;</label> "
	+ " <input type=\"radio\" id=\"2-stars\" name=\"rating\" value=\"2\"><label for=\"2-stars\" class=\"star\" onclick=\"funGetQuestionRating(2,"+count+")\" >&#9733;</label> "
	+ "  <input type=\"radio\" id=\"1-stars\" name=\"rating\" value=\"1\"><label for=\"1-stars\" class=\"star\" onclick=\"funGetQuestionRating(1,"+count+")\" >&#9733;</label> "
	+ " </div>" 

	col3.innerHTML = "<input type=\"hidden\" size=\"0px\"     id=\"strQuestionCode."+(count)+"\"  value='"+strQueCode+"' />";

	 
}

function funGetQuestionRating(rating,index)
{
	
    mapQuestion.set(document.getElementById("strQuestionCode."+ index).value,rating);
    
    var flag=true;
 	var ObjQue = {}
 	$.each(listQustion, function(i, obj) 
	 {
 		
			if(obj.quecode == document.getElementById("strQuestionCode."+ index).value)
		    {
				obj.rating=rating;
				flag=false;
		    }
				
	 })
	if(flag)
	{
		ObjQue['quecode'] =document.getElementById("strQuestionCode."+ index).value;
		ObjQue['rating'] =rating;
	 	listQustion.push(ObjQue);
		
	}	
 	/* alert(	document.getElementById("strQuestionCode."+ index).value)
	alert(mapQuestion)

	alert(	document.getElementById("strQuestion."+ index).value)
 */
   <%session.setAttribute("allQuestion", "abc");%>
}


function funValidate()
{
	 var strAllQueRating="";
	 $.each(listQustion, function(i, obj) 
	 {
			if(strAllQueRating.length > 0)
		    {
				strAllQueRating =strAllQueRating + "#"+obj.quecode +"!"+obj.rating;
		    }
			else
			{
				strAllQueRating=obj.quecode +"!"+obj.rating;
			}	
	 })
	 
	 $("#hidQuestion").val(strAllQueRating);
	
}
</script>





</head>
<body>

<s:form name="frmFeedback" method="GET" action="saveFeedback.html? saddr=${urlHits}"  target="_blank" style="width: 100%; height: 100%;">
	<div class="container-fuild"> 
		<div class="firstpage">
   			<h1 style="text-align:center;color:#ffffff;font-size:29px;">About our food </h1>
	   			<div style="overflow: auto;padding-left: 4px;">
				 <table class="order" id="tblFeedback">
					 
					</table> 
					<!-- <div class="star-rating">
						 <input type="radio" id="5-stars" name="rating" value="5" />
						  <label for="5-stars" class="star">&#9733;</label>
						  <input type="radio" id="4-stars" name="rating" value="4" />
						  <label for="4-stars" class="star">&#9733;</label>
						  <input type="radio" id="3-stars" name="rating" value="3" />
						  <label for="3-stars" class="star">&#9733;</label>
						  <input type="radio" id="2-stars" name="rating" value="2" />
						  <label for="2-stars" class="star">&#9733;</label>
						  <input type="radio" id="1-star" name="rating" value="1" />
						  <label for="1-star" class="star">&#9733;</label>
					</div> -->
				</div>
				
         		<div class="head">
                 	<h3 style="margin-top:30px;font-size34px;">Remark<span style="color:red;">*</span></h3>
           		</div>
           		<div class="form-group">
            		<s:input type="text" class="form-control" id="idRemark" name="remark" path="strRemark" style="height: 38px;"/>
           		</div>
				
				<div style="text-align:center;margin-top: 50px">
       				<button type="submit" class="btn btn-outline-primary" onclick="return funValidate()" style="font-size:22px;padding:5px 70px;" >CONTINUE</button>
	 			</div>
				
				
				
 			<!-- <div class="head">
				<h2>Taste</h2>
			</div>

       <ul class="list-group list-group-horizontal " >
          <li class="list-group-item"><i class="fa fa-star" aria-hidden="true" width="30px" height="30px" font-size="100px"></i><br><br><span style="color:#ffffff;">poor</span></li>
          <li class="list-group-item"><i class="fa fa-star" aria-hidden="true"></i></li>
          <li class="list-group-item"><i class="fa fa-star" aria-hidden="true"></i></li>
          <li class="list-group-item"><i class="fa fa-star" aria-hidden="true"></i></li>
          <li class="list-group-item active"><i class="fa fa-star" aria-hidden="true"></i><br><br>excellent</li>
        </ul>


				<div class="head">
					<h2>Portion Size</h2>
				</div>

	       <ul class="list-group list-group-horizontal " >
	          <li class="list-group-item"><i class="fa fa-star" aria-hidden="true"></i><br><br><span style="color:#ffffff;">poor</span></li>
	          <li class="list-group-item"><i class="fa fa-star" aria-hidden="true"></i></li>
	          <li class="list-group-item"><i class="fa fa-star" aria-hidden="true"></i></li>
	          <li class="list-group-item"><i class="fa fa-star" aria-hidden="true"></i></li>
	          <li class="list-group-item active"><i class="fa fa-star" aria-hidden="true"></i><br><br>excellent</li>
	        </ul>

					<div class="head">
						<h2>Menu Variety</h2>
					</div>

					 <ul class="list-group list-group-horizontal " >
							<li class="list-group-item"><i class="fa fa-star" aria-hidden="true"></i><br><br><span style="color:#ffffff;">poor</span></li>
							<li class="list-group-item"><i class="fa fa-star" aria-hidden="true"></i></li>
							<li class="list-group-item"><i class="fa fa-star" aria-hidden="true"></i></li>
							<li class="list-group-item"><i class="fa fa-star" aria-hidden="true"></i></li>
							<li class="list-group-item active"><i class="fa fa-star" aria-hidden="true"></i><br><br>excellent</li>
						</ul>
           <br><br></br>
	<div style="align:center;">
  <ul class="pagination">
    <li class="list-item page-item"><a  href="#">&laquo;</a></li>
    <li class="list-item page-item"><a  href="#">1</a></li>
    <li class="list-item page-item"><a  href="#" class="active">2</a></li>
    <li class="list-item page-item"><a  href="#">3</a></li>
    <li class="list-item page-item"><a  href="#">4</a></li>
    <li class="list-item page-item"><a  href="#">&raquo;</a></li>
  </ul>
  </div>
 -->	 </div>
	</div>

		

	<s:hidden id="hidQuestion" path="strAllQueRating"   /> 
</s:form>
</body>
</html>