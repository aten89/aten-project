var mainFrame = $.getMainFrame();
var docFormId;
var copyDocFormId;
var flowKey;
var layouts;
var endTime;
var appointTo="";
var NTKO_OFFICE_OCX,NTKO_ATTACH_OCX;

$(initArchivesManage);

function initArchivesManage(){
	
	//保存为草稿
 	$("#saveDraft").click(function(){
		submitDoc(true);
	});
	docFormId = $('#docFormId').val();
	copyDocFormId = $('#copyDocFormId').val();
	
	//提交
	$("#saveActorAcc").click(function(){
		if (!vaildateParam()) {
			return;
		}
		var arg = new Object();
		var ok = window.showModalDialog(BASE_PATH + "/m/nor_start?act=initflowselect&docclass="+ encodeURI($("#docclass").val()),arg,"dialogHeight:400px;dialogWidth:360px;status:no;scroll:auto;help:yes");
  		if(ok){
  			flowKey = arg.flowKey;
  			appointTo = arg.appointTo;
  			endTime = arg.endTime;
  			submitDoc(false);
  		}
  		
  	});	  	
  	
	initControl();

	$("#fullScrean").click(function(){
		NTKO_OFFICE_OCX.showFullWindow();	
	});
	
	$("#subject").change(function(){
		NTKO_OFFICE_OCX.setBookmarkValue("subject",$(this).val());
	})
	$("#groupname").change(function(){
		NTKO_OFFICE_OCX.setBookmarkValue("groupName",$(this).val());
	})
};

function setAllBookMark(){
	NTKO_OFFICE_OCX.setBookmarkValue("draftsman",$("#draftsman").html());
//	NTKO_OFFICE_OCX.setBookmarkValue("docClassName",$("#docclass").val());
	NTKO_OFFICE_OCX.setBookmarkValue("draftDate",$.trim($("#draftDate").html()).toUpperDate());
	NTKO_OFFICE_OCX.setBookmarkValue("groupName",$("#groupname").val());
}

function initControl(){
	//附件控件
  	NTKO_ATTACH_OCX = $.getNewAttachmentControl("NTKO_AttachmentCtrl");
	NTKO_ATTACH_OCX.setPermission({view:true,add:true,update:false,del:true,saveas:true});
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
	NTKO_OFFICE_OCX.setAfterOpenFromURLEvent("setAllBookMark()");
	NTKO_OFFICE_OCX.init("NTKO_OfficeCtrl");
	if( docFormId != ""){
		//如果是归档修改的那么必须留痕
		if(copyDocFormId != ""){
			NTKO_OFFICE_OCX.openOfficeFile(BASE_PATH + $("#draftDocUrl").val(),"approve");
		}else{
			NTKO_OFFICE_OCX.openOfficeFile(BASE_PATH + $("#draftDocUrl").val(),"draft");
		}
	}else{
		var docClassUrl = $.trim($("#docClassUrl").val());
		if(docClassUrl!=null && docClassUrl.length>0){
			NTKO_OFFICE_OCX.openOfficeFile(BASE_PATH + $("#docClassUrl").val(),"draft");
		}else{
			NTKO_OFFICE_OCX.openOfficeFile(BASE_PATH + "page/template/emptyDocTemplate.doc","draft");
		}
	}
}

function submitDoc(isDraft) {
	$("#saveActorAcc,#saveDraft").attr("disabled","true").addClass("icoNone");
	flag = true;
	//保存主体
	saveDraftDoc();
	if (!flag) {
		$("#saveActorAcc,#saveDraft").removeAttr("disabled","true").removeClass("icoNone");
		return;
	}
	
	//保存OFFIC内容
	saveOfficContent();
	if (!flag) {
		$("#saveActorAcc,#saveDraft").removeAttr("disabled","true").removeClass("icoNone");
		return;
	}
	if (!isDraft) {
		NTKO_ATTACH_OCX.setAfterSaveEvent("saveAttachmentEvent");
	} else {
		NTKO_ATTACH_OCX.setAfterSaveEvent("saveDraftAttachmentEvent");
	}
	//保存附件
	saveAttachment(flowKey);
	if (!flag) {
		$("#saveActorAcc,#saveDraft").removeAttr("disabled","true").removeClass("icoNone");
		return;
	}
	alert("操作成功");
	//刷新父列表
	mainFrame.getCurrentTab().doCallback();
	//关闭
	mainFrame.getCurrentTab().close();
};


function saveDraftDoc(){
	if (!vaildateParam()) {
		flag = false ;
		return;
	}
 	var subject = $("#subject").val();
	var groupname = $("#groupname").val();
	var docClassName = $("#docclass").val();
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
                alert(message.text());
            } 
        },
        error : $.ermpAjaxError
	});
};
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
};
function saveOfficContent(){
	var obj = NTKO_OFFICE_OCX.saveOffice(docFormId);
	if(obj == null){
		alert("OFFICE控件未正确安装，正文保存失败！");
		flag = false;
	}else{
		if (obj.code == 0){
			flag = false ;
			alert(obj.message);
		}
	}
};


function saveAttachment(){
	NTKO_ATTACH_OCX.saveAttachments(docFormId);
}

function saveDraftAttachmentEvent(code,message){
	if(code == 0){
		flag = false ;
		alert(message);
	}
}

function saveAttachmentEvent(code,message){
	var isPublish = true;
	if(code == 0){
		flag = false ;
		alert(message);
	}else{
		if(flowKey == "DEFAULT_VALUE_ATONCE"){
			isPublish = false;
		}
		$.ajax({
	        type : "POST",
	        cache: false,
	        async: false,
			url  : "m/nor_start",
	        data : {act:"add",id: docFormId,flag:isPublish,flowkey:flowKey,users:appointTo,endTime:endTime},
	        success : function(xml){
	            var message = $("message",xml);
	            if (message.attr("code") == "0") {
	            	flag = false ;
	                alert($("message",xml).text());
	            } 
	        },
	        error : $.ermpAjaxError
		});
	}
}