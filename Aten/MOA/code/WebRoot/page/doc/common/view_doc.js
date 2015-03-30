$(initView);
var NTKO_ATTACH_OCX,NTKO_OFFICE_OCX;
var docFormId;
var flowKey;
function initView(){
  
	initControl();
	
	$("#fullScrean").click(function(){
		NTKO_OFFICE_OCX.showFullWindow();	
	});
	
	NTKO_OFFICE_OCX.setAfterOpenFromURLEvent("updateCommentBookmark()");
	
	//在处理意见旁边，显示流程名称
//	var flowName = $("#flowName").val();
//	var index = flowName.lastIndexOf("-");
//	if (index == -1) index = flowName.length;
//	$("#flowNamePanel").html(flowName.substring(0,index));
	
	//当前用户为发起人时，可以强制结束流程
	if ($("#allowForceEnd").val() == "1"){
		$("#btnForceEnd").click(function(){
			var comment = $.trim($("#comment").val());
			if (comment == ""){
				alert("请填写强制结束本流程的原因。");
				return false;
			}
			$.ajax({
				type : "POST",
				cache: false,
				async: false,
				url  : "m/nor_track",
				data : "act=forceEnd&id=" + $("#docFormId").val()+"&comment=" + comment,
				success : function(xml){
					var messageCode = $("message",xml).attr("code");
					var msg = $("message",xml).text();
					if(messageCode == "1"){
						alert(msg);
						var mainFrame = $.getMainFrame();
						mainFrame.getCurrentTab().doCallback();
						mainFrame.getCurrentTab().close();
					}else{
						alert(msg);
					}
				}
		   	});
		});
		$("#forceEndPanel").show();
	}
};

function updateCommentBookmark(){
	//读取意见，更新WORD中所有的意见框
//	NTKO_OFFICE_OCX.setAllCommentBookmark("updateSignComment");
}
//function updateSignComment(bookmark){
//	var bookmarks = bookmark.split("：");
//	if (bookmarks.length<2) return false;
//	
//	$.ajax({
//		type : "POST",
//		cache: false,
//		async: false,
//		url  : "m/nor_deal",
//		data : "act=get_comment&id=" + $("#docFormId").val()+"&nodeName=" + bookmarks[1],
//		success : function(xml){
//			var messageCode = $("message",xml).attr("code");
//			var msg = $("message",xml).text();
//			if(messageCode == "1"){
//				var content = $("content",xml);
//				var comments = "";
//				$("task",content).each(function(){
//					var comment = $("comment",this).text();
//					
//					var index = comment.indexOf("，");
//					if (index>=0){
//						//说明写了意见
//						comment = comment.substring(index+1,comment.length);
//						var user = $("user",this).text();
//						var time = $("time",this).text();
//						comments = comments + "\n" + comment + "\n" + "　　　　　　　　　　签名人：" + user + "\n" + "　　　　　　　　　　" + time + "\n";
//					}
//					
//				});
//				//插入内容之前，先把修订关掉
//				if (NTKO_OFFICE_OCX.element.ActiveDocument.TrackRevisions==true){
//					NTKO_OFFICE_OCX.element.ActiveDocument.TrackRevisions = false;
//					NTKO_OFFICE_OCX.setBookmarkValue(bookmark,comments);
//					NTKO_OFFICE_OCX.element.ActiveDocument.TrackRevisions = true;
//				}else{
//					NTKO_OFFICE_OCX.setBookmarkValue(bookmark,comments);
//				}
//			}
//		}
//   	});
//}
//初始化控件
function initControl(){
	docFormId = $("#docFormId").val();
	
	//附件控件
  	NTKO_ATTACH_OCX = $.getNewAttachmentControl("NTKO_AttachmentCtrl");
	NTKO_ATTACH_OCX.setPermission({view:true,add:false,update:false,del:false,saveas:true});
	NTKO_ATTACH_OCX.setSaveURL(BASE_PATH + "m/dis_start?act=upload");
	NTKO_ATTACH_OCX.setBasePath(BASE_PATH);
	NTKO_ATTACH_OCX.init();
	//加载文件列表
	$.ajax({
       type : "POST",
       cache: false,
       async: false,
       url  : "m/nor_start",
       data : "act=getfiles&referid=" + docFormId,
       success : function(xml){
           var messageCode = $("message",xml).attr("code");
           var msg = $("message",xml).text();
           if(messageCode == "1"){
               NTKO_ATTACH_OCX.setFileList($("content",xml)[0]);
           } else {
               alert($("message",xml).text());
           }
       }
   	});
	//OFFIC控件
	NTKO_OFFICE_OCX = new NTKOOfficeControl();
	NTKO_OFFICE_OCX.setSaveURL(BASE_PATH + "m/dis_start?act=uploadcontent");
	NTKO_OFFICE_OCX.setPublishURL(BASE_PATH + "m/dis_start?act=uploadhtml");
	NTKO_OFFICE_OCX.setBasePath(BASE_PATH);
	NTKO_OFFICE_OCX.setOperator($("#userId").val());
	NTKO_OFFICE_OCX.init("NTKO_OfficeCtrl");
	NTKO_OFFICE_OCX.openOfficeFile(BASE_PATH + $("#draftDocUrl").val(),"readonly");
};
