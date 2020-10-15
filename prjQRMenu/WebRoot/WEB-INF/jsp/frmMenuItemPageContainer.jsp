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
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
	<meta name="theme-color" content="#111">
	<meta charset="UTF-8" />
	

  	<meta name="viewport" content="width=590">
	
	<script type="text/javascript" src="<spring:url value="/resources/js/jquery-confirm.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/confirm-prompt.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/jquery.autocomplete.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/jquery.autocomplete.min.js"/>"></script>
	<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
 	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
 
 
	<script type="text/javascript" src="<spring:url value="/resources/js/jquery-1.11.1.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.min.js"/>"></script>
	
	
	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/bootstrap.grid.min.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/bootstrap.min.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/style.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/materialdesignicons.min.css"/>" />
	<link rel="stylesheet" type="text/css" media="screen" href="<spring:url value="/resources/css/mdb.min.css"/>" />
	
	<script type="text/javascript" src="<spring:url value="/resources/js/bootstrap.bundle.min.js"/>"></script>
	<script type="text/javascript" src="<spring:url value="/resources/js/mdb.min.js"/>"></script>

	<script type="text/javascript" src="<spring:url value="/resources/js/print.min.js"/>"></script>

	<script type="text/javascript" src="<spring:url value="/resources/js/easy-numpad.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/jquery-confirm.min.css"/>"/>
 
 	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

	<!-- Popper JS -->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>

	<!-- Latest compiled JavaScript -->
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
 
 
  	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
 	<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/easy-numpad.css"/>"/>
 
 


<title>
</title>
<style type="text/css">

.mdi mdi-currency-inr{
    font-size: 20px;
    visibility:visible;
    display:block;
    line-height:2px;
}
</style>

<script type="text/javascript">

var mapMenuHead=new Map();
var mapMenuHeadWithNameKey=new Map();


var hmGroupMap=new Map();
var hmSubGroupMap=new Map();
var hmItempMap=new Map();
var listBillItem=[];
var finalNetTotal=0.0;
var finalGrandTotal=0.0;
var finalSubTotal=0,finalDiscountAmt=0;
var finalDelCharges=0.0;
var listOfCompItem=[];
var listPunchedItmeDtl=[];

//var gPopUpToApplyPromotionsOnBill="${gPopUpToApplyPromotionsOnBill}";




/* This space is use to define only global variables 
 * coz form frmWebPOSBillingContainer is use in all billing related forms 
 */

 var gTableNo="";
 var gTableName="";
 var gWaiterName="";
 var gWaiterNo="";
 var gPAX=0;
 var gLastKOTNo="";
 var gAreaCode="";

var gMobileNo="";
var fieldName;
var selectedRowIndex=0;
var gDebitCardPayment="";
var tblMenuItemDtl_MAX_ROW_SIZE=100;
var tblMenuItemDtl_MAX_COL_SIZE=6;
var itemPriceDtlList=new Array();	
var hmHappyHourItems = new Map();
var gCustomerCode="${custCode}" ,gCustomerName="";
var currentDate="";
var currentTime="";
var dayForPricing="",flagPopular="",menucode="",homeDeliveryForTax="N",gTakeAway="No",cmsMemCode="",cmsMemName="";
var arrListHomeDelDetails= new Array();
var arrKOTItemDtlList=new Array();
var arrDirectBilleritems=new Array();
var gCustAddressSelectionForBill="${gCustAddressSelectionForBill}";
var gCMSIntegrationYN="${gCMSIntegrationYN}";
var gCRMInterface="${gCRMInterface}";sublim
var gDelBoyCompulsoryOnDirectBiller="${gDelBoyCompulsoryOnDirectBiller}";
var gRemarksOnTakeAway="${gRemarksOnTakeAway}";
var gNewCustomerForHomeDel=false;
var gTotalBillAmount,gNewCustomerMobileNo;
var gBuildingCodeForHD="",gDeliveryBoyCode="";
var isNCKOT=false;
var ncKot="N",reasonCode="",cmsMemCode="",cmsMemName="",globalTableNo="",globalDebitCardNo="",taxAmt=0,homeDeliveryForTax="N";
var operationType="DineIn",transactionType="Make KOT";
/* operationType must be DineIn,HomeDelivery and TakeAway */
/* transactionType would be Make KOT,Direct Biller,Modify Bill,Make Bill,UnSettle Bill,etc */
var gMenuItemSortingOn="${gMenuItemSortingOn}";
var gSkipPax="${gSkipPax}";
var gSkipWaiter="${gSkipWaiter}";
var gPrintType="${gPrintType}";
var gMultiWaiterSelOnMakeKOT="${gMultiWaiterSelOnMakeKOT}";
var gSelectWaiterFromCardSwipe="${gSelectWaiterFromCardSwipe}";

var gPOSCode="${gPOSCode}";
var gClientCode="${gClientCode}";
var gBillDate="${billDate}";
var gEnableSettleBtnForDirectBiller="${gEnableSettleBtnForDirectBiller}";
var fieldName="";

var operationType="${operationMode}";
var gTableNo="${tableOp}";

var tempVal="";

var OrderNO=0;
$(document).ready(function() 
{
	
    funShowItem();
    $("#Closer").click(function () {
        $("#dialog").dialog("destroy");
        return false;
    });	
	$(".tab_content").hide();
	$(".tab_content:first").show();
	$("ul.tabs li").click(function() {
		$("ul.tabs li").removeClass("active");
		$(this).addClass("active");
		$(".tab_content").hide();
		var activeTab = $(this).attr("data-state");
		$("#" + activeTab).fadeIn();
		
	}); 
    	
});
function abc()
{
  alert("aaa");	
}

/* gDayEnd = "${gDayEnd}";  
gShiftEnd = "${gShiftEnd}";
if(gShiftEnd=="" && gDayEnd=="N")1
{
	alert("Please Start Day");
	funPOSHome();
} */
<%-- var voucherNo="${voucherNo}";
if(voucherNo!=''){
	funOpenBillPrint(voucherNo);
	<%
	if (session.getAttribute("success") != null) {
		if(session.getAttribute("OrderNo") != null){%>
		OrderNO='<%=session.getAttribute("OrderNo").toString()%>';
			<%
			session.removeAttribute("voucherNo");
		}
		boolean test = ((Boolean) session.getAttribute("success")).booleanValue();
		session.removeAttribute("success");
		if (test) {
			%>	
			<%
		}
	}%>
	
	
	
} --%>


function funOpenBillPrint(voucherNo){
	//alert(voucherNo);
	window.open(getContextPath()+"/getBillPrint.html?voucherNo="+voucherNo+"#toolbar=1");
	window.open(getContextPath()+"/getKotPrint.html?voucherNo="+voucherNo+"#toolbar=1");

   /* var tableNo="1",kotNO="2",reprint="N";	
   window.open(getContextPath()+"/getKotPrint.html?tableNo='"+tableNo+"'&KOTNo='"+kotNO+"'&voucherNo="+voucherNo+"&reprint='"+reprint+"'&type='DirectBiller'&printYN='Y' ");
	 */
	
	
	//var url=window.location.origin+getContextPath()+"/getBillPrint.html?voucherNo="+voucherNo+"#toolbar=1";
	//alert(url);
	 /* $("#plugin").attr("src", url);
	 $("#dialog").dialog({
		 	autoOpen: false,
	        maxWidth:600,
	        maxHeight: 500,
	        width: 600,
	        height: 500,
	        modal: false,
	        buttons: {
	          
	            Cancel: function() {
	                $(this).dialog("destroy");
	                $('#dialog').dialog('destroy');
	            }
	        },
	        close: function() {
	        	 $(this).dialog("destroy");
	        }
		});
	 
	 $("#dialog").dialog('open');
	 */
	
	

	//window.open(getContextPath()+"/getBillPrint.html?voucherNo="+voucherNo,"","dialogHeight:500px;dialogWidth:50px;dialogLeft:50px;")
	
	
	//<embed width="100%" height="100%" name="plugin" id="plugin" src="http://localhost:8080/prjWebPOS/getBillPrint.html?voucherNo=P0131947" type="application/pdf" internalinstanceid="62">
	
	//window.showModalDialog(getContextPath()+"/getBillPrint.html?voucherNo="+voucherNo); 
	
	/* var searchurl=getContextPath()+"/getBillPrint.html?voucherNo="+voucherNo;
	$.ajax({
		 type: "GET",
	        url: searchurl,
	        dataType: "json",
	        async: false,
       success: function (response)
       {
    	   var myResponse = eval(response);
    	   window.open('data:application/pdf;base64,' + myResponse.base64EncodedResponse);
    	   
           $("<iframe />") // create an iframe
             // add the source
             .attr('src', 'data:application/pdf;base64,' + myResponse.base64EncodedResponse)
             .appendTo('.modal-body');
           
           
       },
       error: function(jqXHR, exception)
       {
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
	
	 */
	 
}
function funMakeBillBtnClicked(TableNo)
{

	 $("#hidTableNo").val(TableNo);
	 listPunchedItmeDtl=[];
     var searchurl=getContextPath()+"/funGetItemsForTable.html?bussyTableNo="+TableNo;
	 $.ajax({
		        type: "GET",
		        url: searchurl,
		        dataType: "json",
		        async:false,
		        success: function(response)
		        {
		        	$.each(response.itemList, function(i,item)
    				{			
		        		
		        		var itemName=item.strItemName;									
						var itemQty=item.dblQuantity;									
						var itemAmt=item.dblAmount;
						var rate=item.dblRate;
						var itemCode=item.strItemCode;
		        		
    		    		
		        		var isModifier=false;
		  		    	if(itemName.startsWith("-->"))
		  				{
		  		    		isModifier=true;
		  				}
		  		    	
		  		    	
		  		    	var subgroupcode=item.strSubGroupCode;
				 		var subgroupName=item.strSubGroupName;
		  		    	var groupcode=item.strGroupCode;
				 		var groupName=item.strGroupName;
									 							
						
		  		    	
						
						var singleObj = {}

					    singleObj['itemCode'] =itemCode;
						singleObj['itemName'] =itemName;				
						singleObj['quantity'] =itemQty;																	    								   
					    singleObj['rate'] =rate;				   								    
					    singleObj['amount'] = itemAmt;								    
					    singleObj['isModifier'] =isModifier;								    
					    singleObj['discountPer'] = 0.0;
					    singleObj['discountAmt'] =0.0;
					    singleObj['strSubGroupCode'] =subgroupcode;
					    singleObj['strGroupcode'] =groupcode;
					    singleObj['dblCompQty'] ='0';
						singleObj['modItemCode'] ="";
						singleObj['added'] ="abc";

					   
					    
						listPunchedItmeDtl.push(singleObj);
		  		    	
		  		    	
		  		    	
		  		    	
    		    		
    			  	});					        	  
				},
				error: function(jqXHR, exception) {
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
	 if(listPunchedItmeDtl.length>0)
	 {
		 funDoneBtnDirectBiller();

	 }
	 else
     {
		   
		 $('#idYourOrderHeader').text("");
		 $('#idHeaderWithCompanyName').text("");

			var $rows = $('#tblSettleItemTable').empty();
 		var $rows = $('#tblSettleItemTableHead').empty();
 		var tblSettleItemDtl=document.getElementById('tblSettleItemTable');
 		var rowCount = tblSettleItemDtl.rows.length;
 		var insertRow = tblSettleItemDtl.insertRow(rowCount);
 				     	
 	    var col1=insertRow.insertCell(0);

 	    //col1.style.width ='10';
 	   
 	    /* col1.style.backgroundColor="lavenderblush";
 	    col2.style.backgroundColor="lavenderblush";
 	    col3.style
 	    .backgroundColor="lavenderblush"; */
 	    
 	    
 	    
 	    
 	    col1.innerHTML = "<input readonly=\"readonly\" size=\"33px\"   style=\"text-align:center;font-size:28px;color:#a31717; margin-top:9%;margin-left:4%;width:90%;\"  value='No Orders are present to Generate Bill' />";
 	   document.getElementById("idBtnProceed").style.display='none';
     }		 
  }
			
function funDoneBtnDirectBiller()
{

    var op="${operationMode}";

	
		var $rows = $('#tblSettleItemTable').empty();
		if(op =="Dine In And Make Bill")
		{
		  document.getElementById("tab1").style.display='block';

		}
		else
		{
		   document.getElementById("tab1").style.display='none';
		   document.getElementById("tab2").style.display='block'; 
		}	
	
		if(op == 'Take Away')
   	   {
			operationType="TakeAway";
			transactionType="Direct Biller";
			 gTakeAway="Yes";

	   }
	   else if(op == 'Home Delivery')
	   {
			operationType="HomeDelivery";
			transactionType="Direct Biller";		
			gTakeAway="No";

			
	   }
   	   else if(op =="Dine In And Make Bill")
   	   {
	  		operationType="DineIn";
		    transactionType="Make Bill";
		    gTakeAway="No";
     	}
		
		
		 homeDeliveryForTax = "N";		 		
		 
		
		 finalSubTotal=0;
		 finalNetTotal=0;
		 finalGrandTotal=0;
		funNoPromtionCalculation(listPunchedItmeDtl);
		funRefreshSettlementItemGrid();

		
		
}

function funRefreshSettlementItemGrid()
{
	



    finalNetTotal=finalSubTotal;
    finalGrandTotal=finalNetTotal;
    funFillTableFooterDtl("","","");  
    funFillTableFooterDtl(" SUB TOTAL",finalSubTotal,"bold");
   // funFillTableFooterDtl(" Discount",finalDiscountAmt,"");
   // funFillTableFooterDtl(" NetTotal",finalNetTotal,"");
    
	var taxTotal= funCalculateTaxForItemTbl();

    finalGrandTotal=taxTotal+finalNetTotal;
    finalGrandTotal=funCalculateRoundOffAmt(finalGrandTotal);
    funFillTableFooterDtl("","","");  

    funFillTableFooterDtl(" GRAND TOTAL",finalGrandTotal,"bold");
 	$('#hidSubTotal').val(finalSubTotal);
 	$('#hidNetTotal').val(finalNetTotal);
 	$('#hidGrandTotal').val(finalGrandTotal);
 	
 	$("#hidTakeAway").val(gTakeAway);
	$("#hidCustomerCode").val(gCustomerCode);
	//$("#hidCustomerName").val(gCustomerName);
	
	$("#hidOperationType").val(operationType);
	$("#hidTransactionType").val(transactionType);
	
	/* $("#hidAreaCode").val(gAreaCode);
	
	$("#hidTableNo").val(gTableNo);
	$("#hidWaiterNo").val(gWaiterNo);
	 */
}

var settleItemRow=0;
function funNoPromtionCalculation(listItmeDtl)
{
	listBillItem=[];
	settleItemRow=0;
	//$('#tblmodalDataTable tbody').empty();
	$.each(listItmeDtl,function(i,item)
	{
		
		if(item.quantity >0 || item.amount >0)
		{
			funFillSettleTable(item.itemName,item.quantity,item.amount,item.discountPer,item.discountAmt,item.strGroupcode,item.strSubGroupCode,item.itemCode,item.rate,item.dblCompQty,item.added);

		}
		//funFillModalTable(item.itemName,item.quantity,item.amount,item.dblCompQty,item.itemCode,item.rate);
	});

}

function funFillSettleTable(strItemName,dblQuantity,dblAmount,dblDiscountPer1,dblDiscountAmt1,strGroupCode,strSubGroupCode,strItemCode,dblRate,dblCompQty,isModifier)
{
	var tblSettleItemDtl=document.getElementById('tblSettleItemTable');
	var rowCount = tblSettleItemDtl.rows.length;
	var insertRow = tblSettleItemDtl.insertRow(rowCount);
			     	
    var col1=insertRow.insertCell(0);
    //col1.style.width ='10';
    var col2=insertRow.insertCell(1);
    var col3=insertRow.insertCell(2);
    var col4=insertRow.insertCell(3);
    var col5=insertRow.insertCell(4);
    var col6=insertRow.insertCell(5);
    var col7=insertRow.insertCell(6);
    var col8=insertRow.insertCell(7);
    var col9=insertRow.insertCell(8);
    var col10=insertRow.insertCell(9);

    /* col1.style.backgroundColor="lavenderblush";
    col2.style.backgroundColor="lavenderblush";
    col3.style.backgroundColor="lavenderblush"; */
    
    
    if(!(isModifier=="No"))
    {
    	 col1.innerHTML = "<input readonly=\"readonly\" size=\"33px\"  class=\"itemName\" style=\"text-align:left;color:black;height:30px;width:380px;border:none; padding-left:5px;font-size:21px;font-weight:600;font-family:'calibri';margin-bottom:8px;\"   name=\"listOfBillItemDtl["+(rowCount)+"].itemName\" id=\"strItemName."+(rowCount)+"\" value='"+strItemName+"' />";
   	    if(dblAmount == 0 && (strItemName.startsWith('-->')))
   	    {
   	    	col2.innerHTML = "<input  type=\"hidden\" readonly=\"readonly\" size=\"0px\"   class=\"itemQty\" style=\"text-align: right; color:black; height:30px;width:50px;border:none;font-family:'calibri';\"  name=\"listOfBillItemDtl["+(rowCount)+"].quantity\" id=\"dblQuantity."+(rowCount)+"\" value='"+dblQuantity+"' />";
   	        col3.innerHTML = "<span style=\"font-size:17px;\" class=\"mdi mdi-currency-inr\"></span><input type=\"hidden\" readonly=\"readonly\" size=\"0px\"  class=\"itemAmt\" style=\"text-align: left; color:black;font-size:21px; height:30px;width:53px;border:none;margin-top:-6px;font-family:'calibri';\"  name=\"listOfBillItemDtl["+(rowCount)+"].amount\" id=\"dblAmount."+(rowCount)+"\" value='"+dblAmount+"'/>";
   	        
   	    }	
   	    else
   	    {
   	    	//col2.innerHTML = "<input readonly=\"readonly\" size=\"6px\"   class=\"itemQty\" style=\"text-align: right; color:black; height:30px;width:50px;border:none;font-family:'calibri';\"  name=\"listOfBillItemDtl["+(rowCount)+"].quantity\" id=\"dblQuantity."+(rowCount)+"\" value='"+dblQuantity+"' />";
   	        
   	    	col2.innerHTML = "	<div class=\"\" id=\"qtysettle\" style=\"width: 90%;height:28px;margin-top:-8px; border:1px solid gray;font-weight:100;\" >"
	 							+" <span class=\"minus\"  onclick=\"funSettleDecreaseQty(this,"+rowCount+")\" style=\"font-size: 45px;margin-top:-53%;\">-</span>"
	 							+"<input type=\"text\" style=\" font-size: 23px;color: black; width:37%;height:56%;text-align: left\" name=\"listOfBillItemDtl["+(rowCount)+"].quantity\" id=\"dblQuantity."+(rowCount)+"\" value='"+dblQuantity+"' />"
	 							+" <span class=\"plus\"  onclick=\"funSettleIncreaseQty(this,"+rowCount+")\" style=\"font-size: 22px;margin-top: -98%;float:right;display:contents;font-weight:800;\">+</span></div></div>"
    
   	    	col3.innerHTML = "<span style=\"font-size:17px;\" class=\"mdi mdi-currency-inr\"></span><input readonly=\"readonly\" size=\"20px\" class=\"itemAmt\" style=\"text-align: left; color:black; height:30px;width:53px;font-size:21px;margin-top:-6px;border:none;font-family:'calibri';\"  name=\"listOfBillItemDtl["+(rowCount)+"].amount\" id=\"dblAmount."+(rowCount)+"\" value='"+dblAmount+"'/>";
   	        	
   	    }	
   	        
   	        col4.innerHTML = "<input type=\"hidden\" size=\"0px\" class=\"discountPer\"      name=\"listOfBillItemDtl["+(rowCount)+"].discountPer\" id=\"tblDiscountPer."+(rowCount)+"\" value='"+dblDiscountPer1+"' />";
    	    col5.innerHTML = "<input type=\"hidden\" size=\"0px\"   class=\"discountAmt\"    name=\"listOfBillItemDtl["+(rowCount)+"].discountAmt\" id=\"tblDiscountAmt."+(rowCount-1)+"\" value='"+dblDiscountAmt1+"' />"; 
    	    col6.innerHTML = "<input type=\"hidden\"  size=\"0px\"   class=\"groupcode\"    name=\"listOfBillItemDtl["+(rowCount)+"].strGroupcode\" id=\"strGroupcode."+(rowCount)+"\" value='"+strGroupCode+"' />";	    
    	    col7.innerHTML = "<input type=\"hidden\" size=\"0px\"   class=\"subGroupCode\"  name=\"listOfBillItemDtl["+(rowCount)+"].strSubGroupCode\" id=\"strSubGroupCode."+(rowCount)+"\" value='"+strSubGroupCode+"' />";
    	   	col8.innerHTML = "<input type=\"hidden\" size=\"0px\"   class=\"itemCode\"  name=\"listOfBillItemDtl["+(rowCount)+"].itemCode\" id=\"itemCode."+(rowCount)+"\" value='"+strItemCode+"' />";
    	    col9.innerHTML = "<input type=\"hidden\" size=\"0px\"   class=\"rate\"  name=\"listOfBillItemDtl["+(rowCount)+"].rate\" id=\"rate."+(rowCount)+"\" value='"+dblRate+"' />";
    	    col10.innerHTML = "<input type=\"hidden\" size=\"0px\"    name=\"listOfBillItemDtl["+(rowCount)+"].dblCompQty\" id=\"dblCompQuantity."+(rowCount)+"\"  value='"+dblCompQty+"' />";

    	  //For Calculaing Discount Fill the list with item Dtl
    	    var singleObj = {}
    	    singleObj['itemName'] =strItemName;
    	    singleObj['quantity'] =dblQuantity;
    	    singleObj['amount'] = dblAmount;
    	    singleObj['discountPer'] = dblDiscountPer1;
    	    singleObj['discountAmt'] =dblDiscountAmt1;
    	    singleObj['strSubGroupCode'] =strSubGroupCode;
    	    singleObj['strGroupcode'] =strGroupCode;
    	    singleObj['itemCode'] =strItemCode;
    	    singleObj['rate'] =dblRate;
    	    singleObj['dblCompQty'] =dblCompQty;

    	    listBillItem.push(singleObj);itemCode
    	    
    	    finalSubTotal=finalSubTotal+parseFloat(dblAmount);
    	    settleItemRow++;

    }	
   	//finalDiscountAmt=finalDiscountAmt+parseFloat(dblDiscountAmt1);//(itemDiscAmt);
// 			  })	
}

function funCalculatePromotion(listItmeDtl)
{
	
	
	listBillItem=[];
	
	
	var searchurl=getContextPath()+"/promotionCalculate.html?";
	$.ajax({
		 type: "POST",
	        url: searchurl,
	        data : JSON.stringify(listItmeDtl),
	        contentType: 'application/json',
	        async: false,
        success: function (response)
        {
                   if(response.checkPromotion=="Y")
                	 {	
                	   if(gPopUpToApplyPromotionsOnBill=="Y")
                		{
                			var isOk=confirm("Do want to Calculate Promotions for this Bill?");
                			
                			if(isOk)
                			{
                				$.each(response.listOfPromotionItem,function(i,item)
                				{
                    	    		funFillSettleTable(item.strItemName,item.dblQuantity,item.dblAmount,item.dblDiscountPer,item.dblDiscountAmt,item.strGroupCode,item.strSubGroupCode,item.strItemCode,item.dblRate,'0');
                    	    		funFillModalTable(item.itemName,item.quantity,item.amount,'0',item.itemCode,item.rate);
                				});
                			}
                			else
                			{
                				funNoPromtionCalculation(listItmeDtl)                				 	
                			}
                		}
                	   else
                	   {
                			
                			$.each(response.listOfPromotionItem,function(i,item)
                			{
                	    		funFillSettleTable(item.strItemName,item.dblQuantity,item.dblAmount,item.dblDiscountPer,item.dblDiscountAmt,item.strGroupCode,item.strSubGroupCode,item.strItemCode,item.dblRate,'0');
                	    		funFillModalTable(item.itemName,item.quantity,item.amount,'0',item.itemCode,item.rate);
                			});
                			
                		}
        	    	
                	 }
                     else
                     {                	  
                	   funNoPromtionCalculation(listItmeDtl)
                     }
        
        },
        error: function(jqXHR, exception)
        {
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

	function funCalculateTaxForItemTbl()
	{

		var taxTotal=0;
		 var rowCountTax=0;
		var searchurl=getContextPath()+"/funCalculateTaxInSettlement.html?operationTypeForTax="+operationType;
		$.ajax({
			 type: "POST",
		        url: searchurl,
		        data : JSON.stringify(listBillItem),
		        contentType: 'application/json',
		        async: false,
	        success: function (response)
	        {
	        	    	$.each(response,function(i,item)
	        	    	{
			        		taxTotal=taxTotal+response[i].taxAmount;
			        		
			        		funFillTableTaxDetials(response[i].taxName,response[i].taxAmount,response[i].taxCode,response[i].taxCalculationType,response[i].taxableAmount ,rowCountTax);
			        		
			        		rowCountTax++;
			        	});
             
	        	    	finalGrandTotal=finalGrandTotal+taxTotal;
	        	    	 $('#hidTaxTotal').val(taxTotal);
	        },
	        error: function(jqXHR, exception)
	        {
	            if (jqXHR.status === 0) 
	            {
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
		return taxTotal;
	}

	
	
      function funFillTableTaxDetials(taxName,taxAmount,taxCode,taxCalculationType,taxableAmount,rowCountTax)
        {
		var tblSettleItemDtl=document.getElementById('tblSettleItemTable');
		var rowCount = tblSettleItemDtl.rows.length;
		var insertRow = tblSettleItemDtl.insertRow(rowCount);
		
	    var col1=insertRow.insertCell(0);
	    var col2=insertRow.insertCell(1);
	    var col3=insertRow.insertCell(2);
	   	var col4=insertRow.insertCell(3);
	    var col5=insertRow.insertCell(4);
	    var col6=insertRow.insertCell(5);
	    var col7=insertRow.insertCell(6);
	    var col8=insertRow.insertCell(7);
	    
	    /* col1.style.backgroundColor="white";
	    col2.style.backgroundColor="#F5F5F5";
	    col3.style.backgroundColor="#F5F5F5"; */
	    
	    col1.innerHTML = "<input readonly=\"readonly\" size=\"30px\"  name=\"  listTaxDtlOnBill["+(rowCountTax)+"].taxName\" id=\"taxName."+(rowCountTax)+"\" style=\"text-align: left; color:black; width:275px;font-size:21px;height:30px;border:none;padding-left: 4px; font-family:'calibri';\"  value='"+taxName+"' />";
	    col2.innerHTML = "<input readonly=\"readonly\" size=\"6px\"  style=\"text-align: right; color:black; height:30px; border:none;\"   />";
	    col3.innerHTML = "<input readonly=\"readonly\" size=\"20px\"  name=\"  listTaxDtlOnBill["+(rowCountTax)+"].taxAmount\" id=\"taxAmount."+(rowCountTax)+"\"  style=\"text-align: right; color:black; width:75px;font-size:21px; height:30px;border:none;padding-right:20px;font-family:'calibri';\"  value='"+taxAmount+"'  />";
	    /* col4.innerHTML = "<input readonly=\"readonly\" size=\"1px\"   style=\"text-align: right; color:blue; height:20px;\"  />";
	    col5.innerHTML = "<input readonly=\"readonly\" size=\"1px\"   style=\"text-align: right; color:blue; height:20px;\"  />"; */
	    col6.innerHTML = "<input type=\"hidden\" size=\"0px\"   class=\"taxCode\"  name=\"listTaxDtlOnBill["+(rowCountTax)+"].taxCode\" id=\"taxCode."+(rowCountTax)+"\" value='"+taxCode+"' />";
	    col7.innerHTML = "<input type=\"hidden\" size=\"0px\"   class=\"taxCalculationType\"  name=\"listTaxDtlOnBill["+(rowCountTax)+"].taxCalculationType\" id=\"taxCalculationType."+(rowCountTax)+"\" value='"+taxCalculationType+"' />";
	    col8.innerHTML = "<input type=\"hidden\" size=\"0px\"   class=\"taxableAmount\"  name=\"listTaxDtlOnBill["+(rowCountTax)+"].taxableAmount\" id=\"taxableAmount."+(rowCountTax)+"\" value='"+taxableAmount+"' />";
	   
   }


      
   function funFillTableFooterDtl(column1,column2,font){
	   

		var tblSettleItemDtl=document.getElementById('tblSettleItemTable');
		var rowCount = tblSettleItemDtl.rows.length;
		var insertRow = tblSettleItemDtl.insertRow(rowCount);
		totalItemRow=rowCount;
	    var col1=insertRow.insertCell(0);
	    var col2=insertRow.insertCell(1);
	    var col3=insertRow.insertCell(2);
	    var col4=insertRow.insertCell(3);
	    var col5=insertRow.insertCell(4);
	    
	   	var styleLeft="style=\"text-align: left; color:black; height:30px; border:none;widht:240px;font-family:'calibri';\""; 
	    var styleRight="style=\"text-align: right; color:black; height:30px; padding-right:20px;border:none;width: 70px;font-family:'calibri';\"";
	    if(column1=="" && column2=="")
	    {
	    	styleLeft="style=\"text-align: left; color:blue; width:28%; height:30px; border:none;font-family:'calibri'; \" "; 
		    styleRight="style=\"text-align: right; color:blue; height:30px;border:none;width: 70px;font-family:'calibri';\" ";
	    }else if(font.includes("bold")){
	    	styleLeft="style=\"text-align: left; color:green; height:30px;width:95%; border:none;font-weight:600;font-size:21px;font-family:'calibri'; \" ";
	    	styleRight="style=\"text-align: right; color:green; height:30px; padding-right:20px;border:none;width: 70px;font-weight:600;font-size:21px; font-family:'calibri';\"";
	    }
	    
	    
	    col1.innerHTML = "<input readonly=\"readonly\" size=\"30px\"  "+styleLeft+" id=\"column1."+(rowCount)+"\" value='"+column1+"'  />";
	    col2.innerHTML = "<input readonly=\"readonly\" size=\"6px\" "+styleLeft+"  />";
	    col3.innerHTML = "<input readonly=\"readonly\" size=\"20px\"   "+styleRight+" id=\"column2."+(rowCount)+"\" value='"+column2+"' />";
	    
	    
   }

$(document).ready(function()
{
	
	var operationFrom="${operationFrom}";
	
    document.getElementById("tab2").style.display='none';	
    document.getElementById("Bill").style.display='none';	

});

function setTwoDecimal(el) {
    el.value = parseFloat(el.value).toFixed(2);
};



function funFillModalTable(strItemName,dblQuantity,dblAmount,dblCompQty,strItemCode,dblRate)
{
	var tblmodalDataTable=document.getElementById('tblmodalDataTable');
	var rowCount = tblmodalDataTable.rows.length;
	var insertRow = tblmodalDataTable.insertRow(rowCount);
			     	
    var col1=insertRow.insertCell(0);
    var col2=insertRow.insertCell(1);

    var col3=insertRow.insertCell(2);
    var col4=insertRow.insertCell(3);
    var col5=insertRow.insertCell(4);
    var col6=insertRow.insertCell(5);
   	
    col1.innerHTML = "<input readonly=\"readonly\" size=\"33px\" style=\"text-align: left; color:black; height:30px;border:none;\"   name=\"listOfBillItemDtl["+(rowCount)+"].itemName\" id=\"strItemName."+(rowCount)+"\" value='"+strItemName+"' />";
    col2.innerHTML = "<input readonly=\"readonly\" size=\"6px\"    style=\"text-align: right; color:black; height:30px;border:none;\"  name=\"listOfBillItemDtl["+(rowCount)+"].dblCompQty\" id=\"dblCompQuantity."+(rowCount)+"\" onclick=\"funOpenCompNumDialog(this,"+(rowCount)+")\" value='"+dblCompQty+"' />";
    col3.innerHTML = "<input readonly=\"readonly\" size=\"6px\"    style=\"text-align: right; color:black; height:30px;border:none;\"  name=\"listOfBillItemDtl["+(rowCount)+"].quantity\" id=\"dblQuantity."+(rowCount)+"\" value='"+dblQuantity+"' />";
    col4.innerHTML = "<input readonly=\"readonly\" size=\"20px\"    style=\"text-align: right; color:black; height:30px;border:none;padding-right:20px;\"  name=\"listOfBillItemDtl["+(rowCount)+"].amount\" id=\"dblAmount."+(rowCount)+"\" value='"+dblAmount+"'/>";
 	col5.innerHTML = "<input type=\"hidden\" size=\"0px\"   class=\"itemCode\"  name=\"listOfBillItemDtl["+(rowCount)+"].itemCode\" id=\"itemCode."+(rowCount)+"\" value='"+strItemCode+"' />";
    col6.innerHTML = "<input type=\"hidden\" size=\"0px\"   class=\"rate\"  name=\"listOfBillItemDtl["+(rowCount)+"].rate\" id=\"rate."+(rowCount)+"\" value='"+dblRate+"' />";

}
var listOfCompItem=[];
function funOpenCompNumDialog(obj,gridHelpRow)
{
	 listOfCompItem=[];
	 var compQty =prompt("Enter Comp Quantity", "");
	 document.getElementById("dblCompQuantity."+gridHelpRow).value=compQty;	
	 var tblBillCompItemDtl=document.getElementById('tblmodalDataTable');
	 var rowCompCount = tblBillCompItemDtl.rows.length;
		
	for(var k=0;k<rowCompCount;k++)
	{
		 var singleObj = {}
	     
         singleObj['itemCode'] =	 document.getElementById("itemCode."+k).value;
	     singleObj['itemName'] =	 document.getElementById("strItemName."+k).value;
	     singleObj['Compquantity'] =document.getElementById("dblCompQuantity."+k).value;
	     singleObj['rate'] =	 document.getElementById("rate."+k).value;	     
	     listOfCompItem.push(singleObj);
 
	}
	
}
function funCalculationForCompItem()
{
	  var listItmeDtl=[];	   

	 $.each(listBillItem,function(i,obj)
			 { 

	        	    hmItempMap.set(obj.itemCode,obj.itemName);
	        	    
	        	    var discAmt=0,discPer=0;
	        	    var rate=0,comp=0;
	        	    $.each(listOfCompItem,function(i,objCompItem)
	        		{
	        	    	if(obj.itemCode==objCompItem.itemCode)
	        	    	{
	        	    		comp=objCompItem.Compquantity;
	        	    		rate=objCompItem.rate;
	        	    	}
	        		})
					
	        	    var singleObj = {}
	        	    var amount=0;
	        	    amount=(obj.rate * obj.quantity) - (comp * rate);
	        	    
				    singleObj['itemName'] =obj.itemName;
				    singleObj['quantity'] =obj.quantity; 
				    singleObj['amount'] = amount;
				    singleObj['discountPer'] = discPer;
	        	    singleObj['discountAmt'] = discAmt;				    
				    singleObj['itemCode'] = obj.itemCode;
				    singleObj['rate'] =obj.rate;
				    singleObj['strSubGroupCode'] =obj.strSubGroupCode;
				    singleObj['strGroupcode'] =obj.strGroupcode;
				    singleObj['dblCompQty'] =comp;

				   
				    
				    
				    listItmeDtl.push(singleObj);
				        	
			 })		
			 var oTable = document.getElementById('tblSettleItemTable');
			var rowLength = oTable.rows.length;
			
			var $rows = $('#tblSettleItemTable').empty();
			listBillItem=[];
			

			
			finalSubTotal=0.00;
			finalDiscountAmt=0.00;
			finalNetTotal=0.00;
			taxTotal=0.00;
			taxAmt=0.00;
			finalGrandTotal=0.00;	

			 funNoPromtionCalculation(listItmeDtl);
		
	    
		    funRefreshSettlementItemGrid();	
	

}
	function funCalculateRoundOffAmt(settlementAmt)
    {

	var roundOffTo = "${roundoff}";

	if (roundOffTo == 0.00)
	{
	    roundOffTo = 1.00;
	}

	var roundOffSettleAmt = settlementAmt;
	var remainderAmt = (settlementAmt % roundOffTo);
	var roundOffToBy2 = roundOffTo / 2;
	var x = 0.00;

	if (remainderAmt <= roundOffToBy2)
	{
	    x = (-1) * remainderAmt;

	    roundOffSettleAmt = (Math.floor(settlementAmt / roundOffTo) * roundOffTo);

	    //System.out.println(settleAmt + " " + roundOffSettleAmt + " " + x);
	}
	else
	{
	    x = roundOffTo - remainderAmt;

	    roundOffSettleAmt = (Math.ceil(settlementAmt / roundOffTo) * roundOffTo);

	    // System.out.println(settleAmt + " " + roundOffSettleAmt + " " + x);
	}

	return roundOffSettleAmt;

    }

		
function funOpenSingleItem(itemCode,onClickName){
	
	//alert(itemCode.id);
	var gCompanyCode='<%=session.getAttribute("gClientCode").toString()%>';
	
	if(!(gCompanyCode=='240.001'))
	{
		var title='Single Item Page';
	     document.title=title;
	     var OnliclicktemCode="";
	    if(onClickName=='image')
	    {
	    	
	        OnliclicktemCode=itemCode.id.substring(9,itemCode.id.length);

	    }
	    else
	    {
	      OnliclicktemCode=itemCode.id;

	    }	
		document.getElementById("tab1").style.display='none';
		document.getElementById("tab3").style.display='block';	
		
		var qty=0;
		$.each(listPunchedItmeDtl, function(i, obj) 
		{		
			qty= qty + obj.quantity; 		 
		})
		
		if(qty==0)
		{
			document.getElementById("itemCountInSinglePage").style.display='none'; 
		}
		var objMenuItemPricingDtl;
		var jsonArrForMenuItemPricing=${command.jsonArrForDirectBillerMenuItemPricing};	

		$.each(jsonArrForMenuItemPricing, function(i, obj) 
		{									
			if(OnliclicktemCode==obj.strItemCode)
			{
				objMenuItemPricingDtl=obj;
			}	
		});

		var oldQty=0;
		var amount=0;
		$.each(listPunchedItmeDtl, function(i, obj) 
		{		
			if (OnliclicktemCode == obj.itemCode)
			{
				oldQty=obj.quantity; 

			}
			if (OnliclicktemCode == obj.itemCode || obj.modItemCode==itemCode.id) {
				amount=parseFloat(amount) + parseFloat(obj.amount);
			}
			
		})
		var amt= parseFloat(objMenuItemPricingDtl.strPriceSunday).toFixed(2);
		$("#idPrice").text(amt);

	    if(oldQty >0)
	   	{
	   	    $("#txtAddQuantity").val(oldQty);
	   		$("#idPrice").text(amount);
	   	}
	    else
	    {
	    	funDisplayCart();
	    }	
		//$("#lblItemDesc").text(itemDel);
		$("#idItemName").text(objMenuItemPricingDtl.strItemName);
	    $("#idDetails").text(objMenuItemPricingDtl.strItemDetails);
		
	    funLoadModifiers(OnliclicktemCode,amt,onClickName);	
	}	
}

function funBackToDashboard(){
	
	//alert(itemCode);
	var title='Menu Item';
    document.title=title;

	document.getElementById("tab3").style.display='none';
	document.getElementById("tab2").style.display='none';
	document.getElementById("tab1").style.display='block';
	funUnCheckAlltextBox();

}

function funUpdateCartQty()
{
	var quantity=0;
	var amount=0;
	 $.each(listPunchedItmeDtl, function(i, obj) 
	{		
		 
		if(!(obj.itemName.startsWith("-->")))
		{
	        quantity= quantity + obj.quantity; 		 
	
		}   	
	})
	$("#itemCountInSinglePage").val(quantity);
	$("#itemCountInMainPage").val(quantity);		  


	
}
function funShowItem()
{
	 document.getElementById("tab1").style.display='block';
	document.getElementById("tab3").style.display='none';
	document.getElementById("tab2").style.display='none';

}
function funOpenSettlement()
{
	document.getElementById("idBtnProceed").style.display='block';

	var oldQty=0;
	var oldAmount=0;
	$.each(listPunchedItmeDtl, function(i, obj) 
	{		
		
			oldQty=parseFloat(oldQty)  + parseFloat(obj.quantity); 
			oldAmount=parseFloat(oldAmount) + parseFloat(obj.amount);
		
		
	})
	 if(oldQty > 0 && oldAmount > 0 )
	 {
		 var title='Bill Settlement';
	     document.title=title;
		 document.getElementById("tab1").style.display='none';
		 document.getElementById("tab3").style.display='none';
		 document.getElementById("tab2").style.display='block';
		 funUnCheckAlltextBox();
		 funDoneBtnDirectBiller();

	 }	 
	 
}



function funSettleDecreaseQty(obj,index){
	
	var qtyDec=	document.getElementById("dblQuantity."+ index).value;
	var qty= parseInt( qtyDec) - parseInt(1);

	if(!(parseInt(qty)==0))
	{
		   document.getElementById("dblQuantity."+ index).value=qty;
		   var amount= document.getElementById("dblAmount."+ index).value;
		   var rate=amount / qtyDec; 
		   var total=parseInt( rate) * qty;

		   document.getElementById("dblAmount."+ index).value=total;
		   finalSubTotal =finalSubTotal -parseInt(  rate);
		   var itemCode= document.getElementById("itemCode."+ index).value;
		   funFillList(qty,total,itemCode);
		   funDeleteSettlementRow();
		   funRefreshSettlementItemGrid();	


	}

}
function funSettleIncreaseQty(obj,index){
	
	var qtyInc=	document.getElementById("dblQuantity."+ index).value;
	var qty= parseInt( qtyInc) + parseInt(1);

    document.getElementById("dblQuantity."+ index).value=qty;
    var amount= document.getElementById("dblAmount."+ index).value;
    var rate=amount / qtyInc; 
	var total=parseInt( rate) * qty;

    document.getElementById("dblAmount."+ index).value=total;
	finalSubTotal =finalSubTotal + parseInt( rate);
    var itemCode= document.getElementById("itemCode."+ index).value;

	funFillList(qty,total,itemCode);
    funDeleteSettlementRow();
    funRefreshSettlementItemGrid();	

    
	
}

function funDeleteSettlementRow() 
{
	var tblSettleItemDtl=document.getElementById('tblSettleItemTable');
	var rowCount = tblSettleItemDtl.rows.length;
	var row=tblSettleItemDtl.rows.length -1;
	for (var i=settleItemRow;i<rowCount - 1;i++)
	{
		tblSettleItemDtl.deleteRow(row);
		row--;

	}	
	//funCalSettlementTotal();
}

function funFillList(newQty,newAmt,itemCode){

	
	 $.each(listBillItem, function(i, obj) 
	{
		  if(itemCode==obj.itemCode)
		  {
			  obj.quantity=newQty;
	    	  obj.amount=newAmt;
	    	  
	    	
		  } 
	 })
}
</script>





</head>
<body  onload="">

	<s:form name="Billing" method="GET" action=""  style="width: 100%; height: 100%;">	
	
	
	<div class="modal fade" id="myModalReason" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  
  <div class="modal-dialog">
  <div class="modal-content">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    <h3 id="myModalLabel">Select  Reason</h3>
  </div>
  <div class="modal-body">
  <s:select id="cmbReason" name="cmbReason" path=""  items="${reason}" style="height:20px;" />	
  </div>
  <div class="modal-footer">
    <button class="btn" id ="btnOKReason" class="close" data-dismiss="modal" aria-hidden="true" >OK</button>
  </div>
  </div>
  </div>
</div>
	
	<div id="myModalShowBillItems" class="modal">
		<div class="modal-content" >
			  <div class="modal-header">
			      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
			  
			    <h4>Make Items Complimentary</h4>
			  </div>
			  <div class="modal-body" >
			    <table id="modalTable" class=" table" style="border:1px border #ccc; width:100%; height:100px;;">
			    	<thead>
			    	<tr style="border:1px border #ccc;">
			    		<th style="text-align: left;">Select</th>
			    	    <th style="width:17.5%;">Mod Name</th>
			    		<th style="width:17.5%;">Rate</th>
			    	</tr>
					</thead>    
			    </table>
			    
			    <table id="tblmodalDataTable" class=" table" style="border:0px border #ccc; width:100%; height:100px; margin-top:0px;">
			    	
			    </table>
			  </div>
			  <div class="modal-footer">
             <button class="btn" id ="btnOKCompItems" class="close" data-dismiss="modal" aria-hidden="true" onclick="funCalculationForCompItem()" >OK</button>
  </div>
  
		</div>
	</div>
		<table>
			<tr>
				<td>
				
				
				
				
				<div id="tab_container" style="width: 100%; height: 100%; overflow:hidden ">
						<ul class="tabs">
							<li id ="DirectBiller" class="active" data-state="tab1" style="display:  none;" ></li>
							<li id ="Bill" data-state="tab2" style="width: 6%; padding-left: 2%"></li>
						</ul>
						
						<!--This is tab1 which is use to show the main form which we want to show -->
						<!--This depends on the form name which is passed from controller  -->
						<div id="tab1" class="tab_content" style="width: 100%;height: 100%;">
							
							<!-- Include the jsp form in first tab based on the form name which is passed from contoller -->	
													
							<c:choose>
							
						      <c:when test="${formToBeOpen == 'Billing'}">
						     	<jsp:include page="frmMenuItemPage.jsp" />
						      </c:when>
						     
						 <%--       <c:when test="${formToBeOpen == 'Make Bill'}">
						     	<jsp:include page="frmPOSMakeBill.jsp" />
						      </c:when> --%>
							  
						       <c:when test="${formToBeOpen == 'Single Item Page'}">
						     	<jsp:include page="frmSingleItemPage.jsp" />
						      </c:when>
							 
							 <c:when test="${formToBeOpen == 'Bill Settlement'}">
				   			     <jsp:include page="frmPOSBillSettlement.jsp" /> 
				   			</c:when>
						       
						     <%--   <c:otherwise>
						      	<jsp:include page="frmMenuItemPage.jsp" />
						      </c:otherwise>
						  --%>
						      <%-- 
						
						  	  <c:when test="${formToBeOpen == 'Modify Bill'}">
						     	<jsp:include page="frmPOSModifyBill.jsp" />
						      </c:when>
						      
						      
						      
						       <c:when test="${formToBeOpen == 'Make Bill'}">
						     	<jsp:include page="frmPOSMakeBill.jsp" />
						      </c:when>
													
						      <c:otherwise>
						      	<jsp:include page="frmBilling.jsp" />
						      </c:otherwise>
						 --%>  
						 
						     
						    </c:choose>
																			
			   	 		</div>
			   	 
			   	 
						
			    <!-- This is a tab2  -->
			    <!-- This tab is use to show only bill settlement window on second tab which is invisible by default -->
			    <div id="tab2" class="tab_content" style="width:100%;">
			   			
			   			<jsp:include page="frmPOSBillSettlement.jsp" /> 
			    </div>
			    
			    <div id="tab3" class="tab_content" style="width: 100%;height: 100%;">
			   			     	<jsp:include page="frmSingleItemPage.jsp" />
				 
			   	 </div>
			   	 
			   	 
			   
			    <!-- Modal -->
			    

			   
	  <div id="dialog" style="width: 0px; min-height: 0px; max-height: 0%; height: 0px;">
	      
	       <embed width="100%" height="100%" name="plugin" id="plugin"  type="application/pdf" internalinstanceid="100">
	   	   <!-- <button id="Closer">Close Dialog</button> -->
	 </div>
			    </div>
			    </td>
			   </tr>
			    </table>
			
<div id="wait"
					style="display: none; width: 60px; height: 60px; border: 0px solid black; position: absolute; top: 45%; left: 45%; padding: 2px;">
					<img
						src="../${pageContext.request.contextPath}/resources/images/ajax-loader-light.gif"
						width="60px" height="60px" />
				</div>
			
			   
			    </s:form>
			   
</body>
</html>