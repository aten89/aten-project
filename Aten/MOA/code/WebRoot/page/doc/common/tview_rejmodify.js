var mainFrame = $.getMainFrame();

$(initModify);
var NTKO_ATTACH_OCX,NTKO_OFFICE_OCX;
var flag = true;
var docFormId;
var flowKey;
var apprDept="";

//var actions = [];
//var appointTo = "";
//var endTime= "";

function initModify(){
	$("#saveActorAcc22").attr("disabled","true").addClass("icoNone");
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
  				submitDoc();
//  				var type = "A";
//  				if($("#args").val()!=""){
//  					for(var i=0; i<actions.length; i++){
//  						if(actions[i].action == $(this).val()){
//  							type = actions[i].type;
//  						}
//  					}
//  				}
//  				//如果为a,说明无需选择人员则直接进行提交.
//  				if(type.toUpperCase() == "A"){
//  					submitDoc();
//  				}else{
//  					var arg = new Object();
//  					arg.type = type.toUpperCase();
//  					var ok = window.showModalDialog(BASE_PATH + "page/ArchivesApprove/CommonFileManage/PersonDialog.jsp", arg, "dialogHeight:270px;dialogWidth:360px;status:no;scroll:auto;help:no");
//  					if(ok){
//  						appointTo = arg.appointTo;
//  						endTime = arg.endTime;
//  						submitDoc();
//  					}
//  				}
  			} else {
  				return ;
  			}
  		});
  	});
  

	initControl();
	$("#fullScrean").click(function(){
		NTKO_OFFICE_OCX.showFullWindow();	
	});
	
	$("#subject").change(function(){
		NTKO_OFFICE_OCX.setBookmarkValue("subject",$(this).val());
	});
	$("#groupname").change(function(){
		NTKO_OFFICE_OCX.setBookmarkValue("groupName",$(this).val());
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
	NTKO_ATTACH_OCX.setPermission({view:true,add:true,update:false,del:true,saveas:false});
	NTKO_ATTACH_OCX.setSaveURL(BASE_PATH + "m/nor_start?act=upload");
	NTKO_ATTACH_OCX.setBasePath(BASE_PATH);
	NTKO_ATTACH_OCX.init();
	if (docFormId != "") {
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
	}
	//OFFIC控件
	NTKO_OFFICE_OCX = new NTKOOfficeControl();
	NTKO_OFFICE_OCX.setSaveURL(BASE_PATH + "m/nor_start?act=uploadcontent");
	NTKO_OFFICE_OCX.setPublishURL(BASE_PATH + "m/nor_start?act=uploadhtml");
	NTKO_OFFICE_OCX.setBasePath(BASE_PATH);
	NTKO_OFFICE_OCX.setOperator($("#userId").val());
	NTKO_OFFICE_OCX.init("NTKO_OfficeCtrl");
	NTKO_OFFICE_OCX.openOfficeFile(BASE_PATH + $("#draftDocUrl").val(),"approve");
};


function submitDoc() {
	var comment = $("#comment").val();//填写的意见
	//驳回修改 一定要写意见
	if ($("#transitionName").val() == "驳回修改" && comment=="") {
		alert("请填写驳回意见");
		$("#comment").focus();
		return;
	}
	var result = $.validChar(comment,"<>");
	if (result){
		alert("意见审批中不能输入非法字符："+ result);
		return ;
	}
	//设置按钮不可用
	$(".allBtn").attr("disabled","true").addClass("icoNone");
	flag = true;
	//保存主体
	saveDraftDoc();
	if (!flag) {
		$(".allBtn").removeAttr("disabled","true").removeClass("icoNone");
		return;
	}
	//保存OFFIC内容
	saveOfficContent();
	if (!flag) {
		$(".allBtn").removeAttr("disabled","true").removeClass("icoNone");
		return;
	}
	NTKO_ATTACH_OCX.setAfterSaveEvent("afterSaveAttachment");
	//保存附件
	saveAttachment();
	if (!flag) {
		$(".allBtn").removeAttr("disabled","true").removeClass("icoNone");
		return;
	}
}

function vaildateParam() {
	var subject = $.trim($("#subject").val());
	var result = $.validChar(subject);
	if (result) {
		alert("标题不能包含非法字符：" + result);
		$("#subject").focus();
		return false;
	}
	if (subject == "") {
		alert("标题不能为空");
		$("#subject").focus();
		return false;
	}
	return true;
}

function saveDraftDoc(){
	if (!vaildateParam()) {
		flag = false ;
		return;
	}
	var subject = $("#subject").val();
	var groupname = $("#groupname").val();
 	var toDept = $("#todept").val();
 	var ccDept = $("#ccdept").val();
	var docClassName = $("#docclass").val();
	var docSecurity = $("#securityclass").val();
	var docUrgency = $("#urgency").val();
	var signGroupNames=$("#apprDept").val();
	$.ajax({
         type : "POST",
        cache: false,
        async: false,
		url  : "m/nor_start",
        data : {act:"savedraftdoc",
        		docFormId:docFormId,
        		subject: subject,
        		groupname:groupname,
        		docClassName:docClassName
        		},
        success : function(xml){
            var message = $("message",xml);
            if (message.attr("code") == "1") {
            	docFormId = $("message",xml).text();
            }else{
                flag = false ;
                alert("公文保存失败");
            } 
        },
        error : $.ermpAjaxError
	});
}

function saveOfficContent(){
	var obj = NTKO_OFFICE_OCX.saveOffice(docFormId);
	if(obj != null && obj.code == 0){
		flag = false ;
		alert(obj.message);
	}
};

function saveAttachment(){
	NTKO_ATTACH_OCX.saveAttachments(docFormId);
}


function afterSaveAttachment(code,message){
	if(code == 0){
		flag = false ;
		alert(message);
	}else{
		commitDispose();
	}
}

function commitDispose(){
	$.ajax({
       type : "POST",
       cache: false,
       async: false,
       url  : "m/nor_deal",
       data : {act:"rejected_approve",
       			tiid:$("#taskInstanceID").val(),
       			transition:$("#transitionName").val(),
       			comment:$("#comment").val(),
       			docFormId:docFormId
//       			appointTo:appointTo,
//       			endTime:endTime
       			},
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
};

function afterTempSave(code,message){
	if(code == 1){
		alert("临时保存成功！");
	}
}