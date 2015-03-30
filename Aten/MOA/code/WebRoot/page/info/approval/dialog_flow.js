$(initInfoDialog);
function initInfoDialog(){
	
	$("#submit").click(function(){
		selectFlowKey();
		window.close();
  	});
  	$("#cancel").click(function(){
		window.close();
  	});
  	
  	//default select the first item
  	$("input[type='radio']").eq(0).attr("checked",true);
};

function selectFlowKey(){
	window.returnValue = $("input[type='radio'][checked]").val();
}