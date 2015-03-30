var mainFrame = $.getMainFrame();
$(initPage);
function initPage(){

	//关闭
  	$("#closeTab").click(function(){
  		 mainFrame.getCurrentTab().close();
  	});
  	
}
