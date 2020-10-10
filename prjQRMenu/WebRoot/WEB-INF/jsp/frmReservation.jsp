<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List"%>
<html>
<head>
	<meta name="viewport" content="width=device-width, user-scalable=no">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Insert title here</title>

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
	<%-- <link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/bootstrap.grid.min.css"/>" />
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
	
	<!-- Latest compiled and minified CSS -->
  		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
 		<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
		<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+TC:wght@100;300;400;500;700&display=swap" rel="stylesheet">
  		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.0.0/animate.min.css"/>
	
	
<style type="text/css">
	 body{line-height:0.5;color: #ffffff;}
	.firstpage{background: #111;height: 100vh;padding:0px 30px;}
	.firstpage .head h2{color: #ffffff;margin: 0px;padding-top: 14px;font-size: 21px;}
	.firstpage .head p{margin: 7px 0px;font-size: 13px;color:#ffffff;margin-bottom: 15px;}
	#outer {float: left;width: 241px;overflow: hidden;white-space: nowrap;display: inline-block;}
	#left-button {float: left;width: 30px;text-align: center;}
	#right-button {float: left;width: 30px;text-align: center;}
	a {text-decoration: none;font-weight: bolder;color: red;}
	#inner:first-child {margin-left: 0;}
	label {margin-left: 10px;}
	.hide {display: none;}
	.list-group-horizontal{;display: inline-block;margin: 2px 5px;border:none;background-color:#111;border-radius: 0.25rem;color: #fff;margin-bottom:24px}
	.list-group-horizontal:hover{background: #fff;color: #111;}
	.list-group{padding: 7px;font-size: 17px;margin: 0px;}
	                                                                                                                                         
	#left-button{margin-top: 29px;font-size: 22px;color: #fff;width:27px;}
	#left-button a{color:#fff;font-size:23px;font:weight:600;}
	#right-button{margin-top: 29px;font-size: 22px;color: #fff;width:27px;}
	#right-button a{color:#fff;font-size:23px;font:weight:600;}
	.list-group-time{display: inline-block;margin: 8px 0px;border:none;padding:8px 4px;background-color:#111;border-radius: 0.25rem;color: #fff;}
	.list-group-time:hover{background: #fff;color: #111;}


 </style>

<script type="text/javascript">
var date="";
var dateCount=0;
var year="";
$(document).ready(function() 
   {
	
		/* var Date = "${posDate}";
		var arr = Date.split("-");
		Dat = arr[2] + "-" + arr[1] + "-" + arr[0];
		$("#txtFromDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		       $("#txtFromDate").datepicker('setDate', Dat);
		   */    
			$("#txtFromDate").datepicker({ dateFormat: 'dd-mm-yy' });
			$("#txtFromDate").datepicker('setDate','todate');
			date=$("#txtFromDate").val();
			
				
	});
	
   var setInvisible = function(elem) {
    elem.css('visibility', 'hidden');
  };
  
  $(function() {
      var print = function(msg) {
        alert(msg);
      };

      
      var setVisible = function(elem) {
        elem.css('visibility', 'visible');
      };

      var elem = $("#elem");
      var items = elem.children();
      funSetForwardDate()
      
          // Inserting Buttons
      elem.prepend('<div id="right-button" style="visibility: hidden;" ><a href="#" onclick=\"funSetBackwardDate()\" ><</a></div>');
      elem.append('  <div id="left-button" ><a href="#" onclick=\"funSetForwardDate()\">></a></div>');

      // Inserting Inner
      items.wrapAll('<div id="inner"></div>');

      // Inserting Outer
      debugger;
      elem.find('#inner').wrap('<div id="outer"/>');

      var outer = $('#outer');

      var updateUI = function() {
        var maxWidth = outer.outerWidth(true);
        var actualWidth = 0;
        $.each($('#inner >'), function(i, item) {
          actualWidth += $(item).outerWidth(true);
        });

        if (actualWidth <= maxWidth) {
          setVisible($('#left-button'));
        }
      };
      updateUI();



      $('#right-button').click(function() {
        var leftPos = outer.scrollLeft();
        outer.animate({
          scrollLeft: leftPos - 200
        }, 800, function() {
          debugger;
          if ($('#outer').scrollLeft() <= 0) {
         
          }
        });
      });

      $('#left-button').click(function() {
        setVisible($('#right-button'));

        var leftPos = outer.scrollLeft();
        outer.animate({
          scrollLeft: leftPos + 200
        }, 800);
      });

      $(window).resize(function() {
        updateUI();
      });
    });

function getContextPath() 
{
	return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
}


	function funSetForwardDate()
	{

		  var today = new Date();
	  	  var todaydate=today.toString().split(" ");
	  	var tomorrow = "";
	       
		  if(dateCount>0)
		  {
			   tomorrow = new Date(today);
		       tomorrow.setDate(tomorrow.getDate() + dateCount);
		       todaydate=tomorrow.toString().split(" ");
		       
		  }
		    
		   document.getElementById("month1").innerHTML = todaydate[1];
	 	   document.getElementById("date1").innerHTML = todaydate[2];
	 	   document.getElementById("day1").innerHTML = todaydate[0];
	 	  dateCount++;
	 	  tomorrow = new Date(today);
	       tomorrow.setDate(tomorrow.getDate() + dateCount);
	       todaydate=tomorrow.toString().split(" ");
	       
	 	   document.getElementById("month2").innerHTML = todaydate[1];
		   document.getElementById("date2").innerHTML = todaydate[2];
		   document.getElementById("day2").innerHTML = todaydate[0];
		   dateCount++;
		   tomorrow = new Date(today);
	       tomorrow.setDate(tomorrow.getDate() + dateCount);
	       todaydate=tomorrow.toString().split(" ");
	       
		   document.getElementById("month3").innerHTML = todaydate[1];
	 	   document.getElementById("date3").innerHTML = todaydate[2];
	 	   document.getElementById("day3").innerHTML = todaydate[0];
	 	  dateCount++;
	 	  tomorrow = new Date(today);
	      tomorrow.setDate(tomorrow.getDate() + dateCount);
	      todaydate=tomorrow.toString().split(" ");
	    
	 	   document.getElementById("month4").innerHTML = todaydate[1];
		   document.getElementById("date4").innerHTML = todaydate[2];
		   document.getElementById("day4").innerHTML = todaydate[0];

		   year=todaydate[3];
	}
	function funSetBackwardDate()
	{

		dateCount=dateCount-6;
		  if(dateCount==0)
      	   {
            setInvisible($('#right-button'));

      	   }

		  var today = new Date();
	  	  var todaydate=today.toString().split(" ");
	  	var tomorrow = "";
	       
		  if(dateCount>0)
		  {
			   tomorrow = new Date(today);
		       tomorrow.setDate(tomorrow.getDate() + dateCount);
		       todaydate=tomorrow.toString().split(" ");
		       
		  }
		    
		   document.getElementById("month1").innerHTML = todaydate[1];
	 	   document.getElementById("date1").innerHTML = todaydate[2];
	 	   document.getElementById("day1").innerHTML = todaydate[0];
	 	  dateCount++;
	 	  tomorrow = new Date(today);
	       tomorrow.setDate(tomorrow.getDate() + dateCount);
	       todaydate=tomorrow.toString().split(" ");
	       
	 	   document.getElementById("month2").innerHTML = todaydate[1];
		   document.getElementById("date2").innerHTML = todaydate[2];
		   document.getElementById("day2").innerHTML = todaydate[0];
		   dateCount++;
		   tomorrow = new Date(today);
	       tomorrow.setDate(tomorrow.getDate() + dateCount);
	       todaydate=tomorrow.toString().split(" ");
	       
		   document.getElementById("month3").innerHTML = todaydate[1];
	 	   document.getElementById("date3").innerHTML = todaydate[2];
	 	   document.getElementById("day3").innerHTML = todaydate[0];
	 	  dateCount++;
	 	  tomorrow = new Date(today);
	      tomorrow.setDate(tomorrow.getDate() + dateCount);
	      todaydate=tomorrow.toString().split(" ");
	    
	 	   document.getElementById("month4").innerHTML = todaydate[1];
		   document.getElementById("date4").innerHTML = todaydate[2];
		   document.getElementById("day4").innerHTML = todaydate[0];


	}
	
function funSetForwardTime()
{
	  var minutes =00;
	  var hr= document.getElementById("time1").innerHTML;
	  var hrMinAmPm=hr.split(":")[1].split(" ")[1];
	  if(parseFloat(hr.split(":")[0])==11)
	  {
	      var hm=hr.split(":")[1].split(" ")[1];
	      if(hm=='PM')
    	  {
	    	  hrMinAmPm='AM';
    	  }
	      else
	      {
	    	  hrMinAmPm='PM';

	      }	  
		  
	  }
      
		 for(var i=1;i<=4;i++)
		 {
			  var hours = parseFloat(hr.split(":")[0]) + parseFloat(1);
			  var ampm = hours <= 12 ? hrMinAmPm : hrMinAmPm;
			  hours = hours % 12;
			  hours = hours ? hours : 12; // the hour '0' should be '12'
			  minutes = minutes < 10 ? '0'+minutes : minutes;
			  var strTime = hours + ':' + minutes + ' ' + ampm;
			  minutes=parseFloat(minutes) + parseFloat(15);
			document.getElementById("time"+i).innerHTML = strTime;

			 
		 }
    


	}

function funSetBackwordTime()
{
    /* var date1 = new Date();
    date1.setMinutes(date1.getMinutes()+15);
    alert(date1);
	 */
	  var minutes =00;

    var hr= document.getElementById("time1").innerHTML;
	  var hrMinAmPm=hr.split(":")[1].split(" ")[1];
	  if(parseFloat(hr.split(":")[0])==12)
	  {
	      var hm=hr.split(":")[1].split(" ")[1];
	      if(hm=='PM')
    	  {
	    	  hrMinAmPm='AM';
    	  }
	      else
	      {
	    	  hrMinAmPm='PM';

	      }	  
		  
	  }
      

	 for(var i=1;i<=4;i++)
	 {
		  var hours = parseFloat(hr.split(":")[0]) - parseFloat(1);
		  var ampm = hours <= 12 ? hrMinAmPm : hrMinAmPm;
		  hours = hours % 12;
		  hours = hours ? hours : 12; // the hour '0' should be '12'
		  minutes = minutes < 10 ? '0'+minutes : minutes;
		  var strTime = hours + ':' + minutes + ' ' + ampm;
		  minutes=parseFloat(minutes) + parseFloat(15);
		document.getElementById("time"+i).innerHTML = strTime;

		 
	 }

		 
	 }
	 

	function funIncreaseGuestNo() {
	var guestNo = parseInt($("#txtAddGuest").text());
	var newGuest = guestNo + 1;
	$("#txtAddGuest").text(newGuest);

	

}

function funDecreaseGuestNo() {
	if (parseInt($("#txtAddGuest").text()) == 0) {
        
	}
	else
	{
		 var NewGuestNo = parseInt($("#txtAddGuest").text()) - 1;
		 $("#txtAddGuest").text(NewGuestNo);
	}	
	
}
function funSetBookingDate(dateCount)
{
	var month=document.getElementById("month"+dateCount).innerHTML;
	var date=document.getElementById("date"+dateCount).innerHTML;

	$("#hidBookingDate").val(date +" "+month);
	
}
function funSetBookingTime(timeCount)
{
	var strTime=document.getElementById("time"+timeCount).innerHTML;
	$("#hidstrBookingTimeHour").val(strTime);
}

function funValidateForm(){
	$("#hidYear").val(year);
    $("#hidNoOfGuests").val($("#txtAddGuest").text());
    return true;
}
 </script>

</head>
<body>
	<s:form name="frmReservation" method="GET" action="saveReservation.html?
		saddr=${urlHits}"  target="_blank" style="width: 100%; height: 100%;">

	<div class="container-fuild">
		<div class="firstpage">
 			<div class="head">
				<h2>Date</h2>
				<p>Choose an available date</p>
				<div id="elem">
 	 				<div class="list-group-horizontal">
 						<div class="list-group" id="month1" onclick="funSetBookingDate('1')"></div>
 						<div class="list-group" id="date1" onclick="funSetBookingDate('1')"></div>
 						<div class="list-group" id="day1" onclick="funSetBookingDate('1')" ></div>
 					</div>
 					<div class="list-group-horizontal">
 						<div class="list-group" id="month2" onclick="funSetBookingDate('2')" ></div>
 						<div class="list-group" id="date2" onclick="funSetBookingDate('2')"></div>
 						<div class="list-group" id="day2" onclick="funSetBookingDate('2')"></div>
 					</div>
 					<div class="list-group-horizontal">
			 			<div class="list-group" id="month3" onclick="funSetBookingDate('3')"></div>
			 			<div class="list-group" id="date3" onclick="funSetBookingDate('3')"></div>
			 			<div class="list-group" id="day3" onclick="funSetBookingDate('3')"></div>
			 		</div>
			 		<div class="list-group-horizontal">
			 			<div class="list-group" id="month4" onclick="funSetBookingDate('4')"></div>
			 			<div class="list-group" id="date4" onclick="funSetBookingDate('4')"></div>
			 			<div class="list-group" id="day4" onclick="funSetBookingDate('4')"></div>
			 		</div>
			 		
  				</div>
  			</div>
  			<div class="head">
        		<h2>Time</h2>
        		<p>Choose an available time</p>
        		 <span class="" style="font-size: 24px;color: white;margin-left: -5%; " onclick="funSetBackwordTime()"><</span>
				 <span class="" style="font-size: 24px;color: white;float:right; margin-right: -6%;" onclick="funSetForwardTime()">></span>
								
	     		<div class="" style="margin-top: -8%;margin-left:1%">
		            <div class="list-group-time" id="time1" onclick="funSetBookingTime('1')">12:00 PM</div>
		            <div class="list-group-time" id="time2" onclick="funSetBookingTime('2')">12:15 PM</div>
		            <div class="list-group-time" id="time3" onclick="funSetBookingTime('3')">12:30 PM</div>
		            <div class="list-group-time" id="time4" onclick="funSetBookingTime('4')">12:45 PM</div>
		         </div>
	    	
  			</div>
  			<div class="head">
      			<h2>Select Guests</h2>
      			<p>Choose the number of guests going</p>
       		<div>    
       		
				 <div><h2 style="font-size: 14px;">No of guests</h2></div>&nbsp;&nbsp;
			   <div> <i class="fa fa-minus-square" onclick="funDecreaseGuestNo()"></i>&nbsp;&nbsp;&nbsp;&nbsp;
			   <span style="font-size: 14px;" id="txtAddGuest">1</span>
			   &nbsp;&nbsp;&nbsp;&nbsp;<i class="fa fa-plus-square" onclick="funIncreaseGuestNo()"></i></div>
       		</div>
    </div>

		 <div style="text-align:center;">
       <button type="submit" class="btn btn-outline-primary" style="padding:9px 3px;" onclick="funValidateForm()">CONTINUE</button>
	  </div> 
  		</div>
  		</div>
  			
  		<s:hidden id="hidBookingDate" path="strBookingDate"   />
  		<s:hidden id="hidYear" path="strYear"   />
  		<s:hidden id="hidstrBookingTimeHour" path="strBookingTimeHour"   />
  		<s:hidden id="hidNoOfGuests" path="intNoOfGuests"   />
  		
  		
  			
  			
  			
  			
  			
  			
  			
  			
  			
<%-- <div class="element-input col-lg-6"
					style="margin-bottom: 10px; width: 20%">
					<s:input id="txtFromDate" required="required" path="strBookingDate"
						pattern="\d{1,2}-\d{1,2}-\d{4}" style="width: 100%;" />
				</div> --%>
      <!-- <div class="head">
        <h2>Time</h2>
        <p>Choose an available time</p>
      </div> --><!-- 
     <ul class="list-group list-group-horizontal">
            <li class="list-group-item time">6:42PM</li>
            <li class="list-group-item  time active">6:57PM</li>
            <li class="list-group-item  time">7:12PM</li>
            <li class="list-group-item  time">7:27PM</li>
    </ul>
 -->
 
 
 
 <%-- <div class="row">
 <div class="element-input col-lg-6"
									style="margin-bottom: 10px; width: 10%;">
									<s:select id="cmbHH" path="strBookingTimeHour">
									<option value="HH">HH</option>
										<option value="1">1</option>
										<option value="2">2</option>
										<option value="3">3</option>
										<option value="4">4</option>
										<option value="5">5</option>
										<option value="6">6</option>
										<option value="7">7</option>
										<option value="8">8</option>
										<option value="9">9</option>
										<option value="10">10</option>
										<option value="11">11</option>
										<option value="12">12</option>
									</s:select> 
								</div>
								<div class="element-input col-lg-6"
									style="margin-bottom: 10px; width: 10%;">
									<s:select id="cmbMM" path="strBookingTimeMin" >
										 <option value="MM">MM</option>
										<option value="00">00</option>
										<option value="15">15</option>
										<option value="30">30</option>
										<option value="45">45</option>
										</s:select>
								</div>
								<div class="element-input col-lg-6"
									style="margin-bottom: 10px; width: 10%;">
									<s:select id="cmbAMPM" path="strAMPM">
										<option value="AM">AM</option>
										<option value="PM">PM</option>
									</s:select>
								</div>
</div>  --%>
    <!-- <div class="head">
      <h2>Select Guests</h2>
      <p>Choose the number of guests going</p>
       <div> -->
				 <%-- <div><h2 style="font-size: 14px;">No of guests</h2></div>&nbsp;&nbsp;
<div class="element-input col-lg-6"
										style="margin-bottom: 10px; width: 30%;">
										<s:input class="large" colspan="3" type="text"
											id="txtNoOfGuest" path="intNoOfGuests" />
									</div>
								       </div>
    </div>

		<div style="align:center;margin: 50px 210px;">
       <button type="submit" class="btn btn-outline-primary" onclick="return funValidateForm();">CONTINUE</button>
	  </div>
		</div> --%>

</s:form>
</body>
</html>