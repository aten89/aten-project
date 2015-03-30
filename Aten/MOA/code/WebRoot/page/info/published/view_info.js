$(initPage);

function initPage(){
	initAttachment();
}

function SetWinHeight(obj)
{
	var win=obj;
	if (document.getElementById)
	{
		if (win && !window.opera){
			if (win.contentDocument && win.contentDocument.body.offsetWidth){
						//$.alert(win.contentDocument.body.scrollWidth);
						if(win.contentDocument.body.scrollWidth < 800){
							win.width = document.documentElement.scrollWidth - 100;
						}else{
							win.width = win.contentDocument.body.scrollWidth;
							}
				}else if(win.Document && win.Document.body.scrollWidth){
						if(win.Document.body.scrollWidth < 800){
							win.width = document.body.offsetWidth - 100;
						}else{
							 win.width = win.Document.body.scrollWidth;
							}
				}
			if (win.contentDocument && win.contentDocument.body.offsetHeight){
				win.height = win.contentDocument.body.scrollHeight;
				
			}else if(win.Document && win.Document.body.scrollHeight){
				win.height = win.Document.body.scrollHeight;
				
			}
		}
	}
}


function closeInfoWin() {
	if ($.getMainFrame() != null && $.getMainFrame().getCurrentTab() != null) {
		$.getMainFrame().getCurrentTab().close();
	} else {
		window.close();
	}
}


function initAttachment(){
	//附件控件
  	var NTKO_ATTACH_OCX = $.getNewAttachmentControl("NTKO_AttachmentCtrl");
	NTKO_ATTACH_OCX.setPermission({view:true,add:false,update:false,del:false,saveas:true});
	NTKO_ATTACH_OCX.setSaveURL(BASE_PATH + "m/info_draft?act=upload");
	NTKO_ATTACH_OCX.setBasePath(BASE_PATH);
	NTKO_ATTACH_OCX.init();
	//加载文件列表
	$.ajax({
	      type : "POST",
	      cache: false,
	      async: false,
	      url  : "m/info_man",
	      data : "act=getfiles&referid=" + $("#infoFormId").val(),
	      success : function(xml){
	          var messageCode = $("message",xml).attr("code");
	          var msg = $("message",xml).text();
	          if(messageCode == "1"){
	              NTKO_ATTACH_OCX.setFileList($("content",xml)[0]);
	          } else {
	              $.alert($("message",xml).text());
	          }
	      }
	 });
}