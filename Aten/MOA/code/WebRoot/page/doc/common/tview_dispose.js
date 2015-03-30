var mainFrame = $.getMainFrame();

$(initArchivesManage);
var NTKO_ATTACH_OCX,NTKO_OFFICE_OCX;
var docFormId;
var flowKey;

//var actions = [];
//var appointTo = "";
//var endTime="";
function initArchivesManage(){
	//添加权限约束
	$.handleRights({
		 "commitBut" : $.OaConstants.DISPOSE
	});

	
//	if($("#args").val()!=""){
//		var args = $("#args").val().split(",");
//		for(var i=0; i<args.length; i++){
//			var temps = args[i].split("-");
//				if (temps[0] != ""){
//				actions.push({action:temps[0],type:(temps.length<2?"A":temps[1])});
//  			}
//		}
//	}
	
	
	//提交
  	$("#commitBut").find(".allBtn").each(function(){
  		$(this).click(function(){
  			if (confirm("确认是否" + $(this).val() + "？")) {
  		  
				$("#transitionName").val($(this).val());
				commitDispose();
//				var type = "A";
//				
//				if($("#args").val()!=""){
//					for(var i=0; i<actions.length; i++){
//						if(actions[i].action == $(this).val()){
//							type = actions[i].type;
//						}
//					}
//				}
//				//如果为a,说明无需选择人员则直接进行提交.
//				if(type.toUpperCase() == "A"){
//					commitDispose();
//				}else{
//					var comment = $("#comment").val();//填写的意见
//					if (comment=="") {
//						alert("请填写处理意见");
//						$("#comment").focus();
//						return;
//					}
//					var arg = new Object();
//					arg.type = type.toUpperCase();
//					var ok = window.showModalDialog(BASE_PATH + "page/ArchivesApprove/CommonFileManage/PersonDialog.jsp", arg, "dialogHeight:270px;dialogWidth:360px;status:no;scroll:auto;help:no");
//					if(ok){
//						appointTo = arg.appointTo;
//						endTime = arg.endTime;
//						commitDispose();
//					}
//				}
			} else {
				return ;
			}
  		});
  	});
  
	//取消FIXME
  
	initControl();
	
	
	$("#fullScrean").click(function(){
		NTKO_OFFICE_OCX.showFullWindow();	
	});
	
	NTKO_OFFICE_OCX.setAfterOpenFromURLEvent("updateCommentBookmark()");
	
	$("#tempSave").click(function(){
		var obj = NTKO_OFFICE_OCX.saveOffice(docFormId);
		if(obj != null && obj.code == 0){
			alert(obj.message);
			return false;
		}
		NTKO_ATTACH_OCX.setAfterSaveEvent("afterTempSave");
		NTKO_ATTACH_OCX.saveAttachments(docFormId);	
	});
	
	if($("#message").val()!= ""){
		alert($("#message").val());
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
  	//根据流程配置的处理类型控制控件的操作权限.
  	//以d开头的都是此处的权限
  	//后四位数字分别表示“增加附件”“修改附件”“删除福建”“修改正文”，1表示有，0表示没有
//  	var flag = $("#disposeType").val();
//  	var f0 = flag.length>=1?flag.substring(0,1):"";		//权限类型，此处只认d
//  	var f1 = (flag.length>=2 && f0=="d")?flag.substring(1,2):"0";	//是否可以增加附件
//  	var f2 = (flag.length>=3 && f0=="d")?flag.substring(2,3):"0";	//是否可以修改附件
//  	var f3 = (flag.length>=4 && f0=="d")?flag.substring(3,4):"0";	//是否可以删除附件
//  	var f4 = (flag.length>=5 && f0=="d")?flag.substring(4,5):"0";	//是否可以修改正文
  	
  	NTKO_ATTACH_OCX.setPermission({view:true,add:false,update:false,del:false,saveas:true});
	NTKO_ATTACH_OCX.setSaveURL(BASE_PATH + "m/nor_start?act=upload");
	NTKO_ATTACH_OCX.setBasePath(BASE_PATH);
	NTKO_ATTACH_OCX.setAfterSaveEvent("afterSaveAttachment");
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
	NTKO_OFFICE_OCX.setSaveURL(BASE_PATH + "m/nor_start?act=uploadcontent");
	NTKO_OFFICE_OCX.setPublishURL(BASE_PATH + "m/nor_start?act=uploadhtml");
	NTKO_OFFICE_OCX.setBasePath(BASE_PATH);
	NTKO_OFFICE_OCX.setOperator($("#userId").val());
	NTKO_OFFICE_OCX.init("NTKO_OfficeCtrl");
	NTKO_OFFICE_OCX.openOfficeFile(BASE_PATH + $("#draftDocUrl").val(),"readonly");
};

function afterTempSave(code,message){
	if(code == 1){
		alert("临时保存成功！");
	}
}
function afterSaveAttachment(){
	var transitionName = $("#transitionName").val();//获得转向的名称
	var taskid = $("#taskInstanceID").val();//任务ID,未测试有没有值
	var docFormId = $("#docFormId").val();//文件ID
	var comment = $("#comment").val();//填写的意见
	
	$.ajax({
       type : "POST",
       cache: false,
       async: false,
       url  : "m/nor_deal",
       data : {act:"deal_approve",
       			tiid:taskid,
       			transition:transitionName,
       			comment:comment,
//       			appointTo:appointTo,
//       			endTime:endTime,
       			docFormId:docFormId},
       success : function(xml){
           var messageCode = $("message",xml).attr("code");
           var msg = $("message",xml).text();
           if(messageCode == "1"){
               alert($("message",xml).text());
                //执行回调函数，刷新父列表
               mainFrame.getCurrentTab().doCallback();
               //关闭
               mainFrame.getCurrentTab().close();
           } else {
               alert($("message",xml).text());
           }
       },
       error : $.ermpAjaxError
   	});
}

function commitDispose(){
	
	var transitionName = $("#transitionName").val();//获得转向的名称
	if (transitionName == "") {
		alert("请选择流程操作");
		return;
	}
	var taskid = $("#taskInstanceID").val();//任务ID,未测试有没有值
	var comment = $("#comment").val();//填写的意见
	//驳回修改 一定要写意见
	if (comment=="") {
		alert("请填写处理意见");
		$("#comment").focus();
		return;
	}
	var result = $.validChar(comment,"<>");
	if (result){
		alert("意见审批中不能输入非法字符："+ result);
		return ;
	}
	//设置按钮不可用
	$("#commitBut").find(".allBtn").attr("disabled","true");
	
	var docFormId = $("#docFormId").val();
	var obj = NTKO_OFFICE_OCX.saveOffice(docFormId);
	if(obj != null && obj.code == 0){
		flag = false ;
		alert(obj.message);
	}
	NTKO_ATTACH_OCX.setAfterSaveEvent("afterSaveAttachment");
	NTKO_ATTACH_OCX.saveAttachments(docFormId);
};