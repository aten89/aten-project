$(initInfoDialog);
function initInfoDialog(){
	$("#submit").click(function(){
		selectLayout();
		
  	});
  	$("#unsubmit").click(function(){
  		window.returnValue = "PUBLISH_NOTHING";
		window.close();
		
  	});
  	$("#cancel").click(function(){
		window.close();
  	});
};

function selectLayout(){
	
	var layoutStr="";
	var checkedObj = $("input[type='checkbox'][checked]"); 
	//获取当前checked的value值 如果选中多个则循环 
	checkedObj.each(function(index){ 
		var isCheck = this.value; 
	    if(index != 0){
	   		layoutStr +=",";
	    }
		layoutStr +=isCheck;
	}); 
	if (layoutStr=="") {
		$.alert("请选择要发布的版块");
		return;
	}
	window.returnValue = layoutStr;
	window.close();
}