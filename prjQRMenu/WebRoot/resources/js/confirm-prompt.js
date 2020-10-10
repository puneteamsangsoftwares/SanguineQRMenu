/*
   Author : Vinayak Borwal
   License: Sanguine Software Solutions
*/

var value="";
var textValue="";

function confirmDialog(title,content)
{
	 $.confirm({
		    title: title,
		    content: content,
		    useBootstrap: false,
		    boxWidth: '30%',
		    keys: ['enter', 'a'],
		    buttons: {
			    ok: function()
			    {
			    	text: 'OK'
			    }
		    }
	});
}

function promptDialog(title,content)
{
	var obj = $.confirm({
					title: title,
			        content: content +
			        '<form action="" class="formName">' +
			        '<div class="form-group">' +
			        '<label></label>' +
			        '<input type="text" placeholder="Enter something" class="name form-control" required />' +
			        '</div>' +
			        '</form>',
			        useBootstrap: false,
			        boxWidth: '30%',
			        buttons: {
			            formSubmit: {
			                text: 'Submit',
			                btnClass: 'btn-blue'
			            },
			            
			            cancel: function () {
			            
			            }
			        },
			    
			        onContentReady: function () {
			            // bind to events
			            var jc = this;
			            this.$content.find('form').on('submit', function (e) {
			                // if the user submits the form by pressing enter in the field.
			                e.preventDefault();
			                jc.$$formSubmit.trigger('click'); // reference the button and click it
			            });
			        },
			        
			        onAction: function(formSubmit){
			       	 	 var value = this.$content.find('.name').val();
				       	 textValue=value;
				            if(!value){
				               return false;
				            }
				            window.close();
				          //funPreviousForm1(textValue);
				    }
			        
			        onAction: function(cancel){
			       	 	    window.close();
				         
				    }
				    
    	});
	
	return obj;
}		 

function funPreviousForm1(value) {
	window.funSetData2(value);
	window.close();
}


function alertDialog(title,content)
{
	$.alert({
	    title: 'Alert!',
	    content: 'Simple alert!',
	});
}

/* For Setting Data of New Dialog 
function funSetData2(value) {
	alert(value);
}*/
