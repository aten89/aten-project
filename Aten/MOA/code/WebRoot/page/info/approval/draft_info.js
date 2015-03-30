var mainFrame = $.getMainFrame();
var isIE = "ActiveXObject" in window;
var useWordEdit;//是否使用Word编辑
$(initInfoManage);
var NTKO_ATTACH_OCX,NTKO_OFFICE_OCX;
var flag = true;
var inforId;
var flowKey;
var copyInfoFormId;
function initInfoManage(){

	$("#saveDraft").click(function(){
		submitInfo(true);
	});
	inforId = $("#infoFormId").val();
	copyInfoFormId = $("#copyInfoFormId").val();
	$("#selectProblem").click(function(){
		if (!vaildateParam()) {
			return;
		}
		flowKey = window.showModalDialog(BASE_PATH + "/m/info_draft?act=initflowselect&layout="+ encodeURI($("#infoLayout").text()),"","dialogHeight:300px;dialogWidth:300px;status:no;scroll:auto;help:no");
  		if(!flowKey){
  			return;
  		}
  		submitInfo(false);
  	});	
  	
  	//初始化附件
  	initNTKOAttach();
  	
  	var displayMode = $("#displayMode").val();
  	if (displayMode) {
  		//不为空说明是修改，根据显示模式决定编辑模式
  		useWordEdit = (displayMode == "0");//0是内容地址形式，即用Word编辑
  	} else {
  		//新增时，是IE时默认使用Office控件
  		useWordEdit = isIE;
  	}
  	
  	if (useWordEdit) {
  		initNTKOWrod();
  	} else {
  		initHTMLEdit();
  	}
  	
  	//切换链接
  	$("#htmlEdit").click(function(){
  		initHTMLEdit();
  	});	
  	$("#wordEdit").click(function(){
  		if (!isIE) {
  			showAlert("只有IE浏览器才能使用Word编辑");
  			return;
  		}
  		initNTKOWrod();
  	});	
  	
	$("#fullScrean").click(function(){
		NTKO_OFFICE_OCX.showFullWindow();	
	});
}

//初始化附件控件
function initNTKOAttach(){
	//附件控件
  	NTKO_ATTACH_OCX = $.getNewAttachmentControl("NTKO_AttachmentCtrl");
	NTKO_ATTACH_OCX.setPermission({view:true,add:true,update:false,del:true,saveas:false});
	NTKO_ATTACH_OCX.setSaveURL(BASE_PATH + "m/info_draft?act=upload");
	NTKO_ATTACH_OCX.setBasePath(BASE_PATH);
	NTKO_ATTACH_OCX.init();
	if (inforId != "") {
		//加载文件列表
		$.ajax({
	       type : "POST",
	       cache: false,
	       async: false,
	       url  : "m/info_draft",
	       data : "act=getfiles&referid=" + inforId,
	       success : function(xml){
	           var messageCode = $("message",xml).attr("code");
	           var msg = $("message",xml).text();
	           if(messageCode == "1"){
	               NTKO_ATTACH_OCX.setFileList($("content",xml)[0]);
	           } else {
	               showAlert($("message",xml).text());
	           }
	       }
	   	});
	}
	
}

//初始化正文控件
function initNTKOWrod(){
	$("#NTKO_OfficeCtrl").html("");
	//OFFIC控件
	NTKO_OFFICE_OCX = new NTKOOfficeControl();
	NTKO_OFFICE_OCX.setSaveURL(BASE_PATH + "m/info_draft?act=uploadcontent");
	NTKO_OFFICE_OCX.setPublishURL(BASE_PATH + "m/info_draft?act=uploadhtml");
	NTKO_OFFICE_OCX.setBasePath(BASE_PATH);
	NTKO_OFFICE_OCX.setOperator($("#userId").val());
	NTKO_OFFICE_OCX.init("NTKO_OfficeCtrl");

	if( inforId != ""){
		var contentDocUrl = $("#contentDocUrl").val();
		if (contentDocUrl == "") {
			contentDocUrl = "page/template/emptyDocTemplate.doc";
		}
		//如果是归档修改的那么必须留痕
		if(copyInfoFormId != ""){
			NTKO_OFFICE_OCX.openOfficeFile(BASE_PATH + contentDocUrl,"approve");
		}else{
			NTKO_OFFICE_OCX.openOfficeFile(BASE_PATH + contentDocUrl,"draft");
		}
	}else{
		NTKO_OFFICE_OCX.openOfficeFile(BASE_PATH + "page/template/emptyDocTemplate.doc","draft");
	}
	
	$("#wordEdit").hide();
  	$("#htmlEdit,#fullScrean").show();
  	useWordEdit = true;
}

//初始化HTML编辑器
function initHTMLEdit() {
	$("#NTKO_OfficeCtrl").html("<textarea rows='57' id='wysiwyg' style='width:100%'></textarea>");
	var conVal = $.trim($("#contentVal").html());
	if (conVal != "") {
		$("#wysiwyg").val(conVal);
	}
	$('#wysiwyg').wysiwyg({
		getImg:getContentImg,
		html:'<html><head><base href="'+ BASE_PATH +'" /><meta http-equiv="Content-Type" content="text/html; charset=utf-8" />STYLE_SHEET</head><body>INITIAL_CONTENT</body></html>',
	    controls : {
	        separator06 : { visible : false }
	    }
	});
	
	$("#htmlEdit,#fullScrean").hide();
  	$("#wordEdit").show();
  	useWordEdit = false;
}

function getContentImg() {
}

function submitInfo(isDraft) {

	$("#selectProblem,#saveDraft").attr("disabled","true").addClass("icoNone");
	flag = true;
	//保存主体
	saveDraftInfo();
	if (!flag) {
		$("#selectProblem,#saveDraft").removeAttr("disabled","true").removeClass("icoNone");
		return;
	}
	if (useWordEdit) {//使用Word编辑才要保存内容文件
		//保存OFFIC内容
		saveOfficContent();
		if (!flag) {
			$("#selectProblem,#saveDraft").removeAttr("disabled","true").removeClass("icoNone");
			return;
		}
	}
	if (!isDraft) {
		//如果flowKey等于直接发布保存HTML
		if(flowKey == "DEFAULT_VALUE_ATONCE" && useWordEdit) {
			saveContentHtml();
		}
		if (!flag) {
			$("#selectProblem,#saveDraft").removeAttr("disabled","true").removeClass("icoNone");
			return;
		}
		//TODO 不知为什么，IE下附件非控件时，附件有修改时，这个回调方法调不到（暂未找到原因，临时注释以下三行）
//		NTKO_ATTACH_OCX.setAfterSaveEvent("saveAttachmentEvent");
//	} else {
//		NTKO_ATTACH_OCX.setAfterSaveEvent("saveDraftAttachmentEvent");
	}
	
	//保存附件
	saveAttachment();
	
	/***START***/
	//TODO 不知为什么，IE下附件非控件时，附件有修改时，这个回调方法调不到（暂时改为直接调，临时增加以下五行）
	if (!isDraft) {
		saveAttachmentEvent(1);
	} else {
		saveDraftAttachmentEvent(1);
	}
	/***END***/
	
	if (!flag) {
		$("#selectProblem,#saveDraft").removeAttr("disabled","true").removeClass("icoNone");
		return;
	}
	showAlert("操作成功",function(){
		//执行回调函数，刷新父列表
		mainFrame.getCurrentTab().doCallback();
		//关闭自己
		mainFrame.getCurrentTab().close();
	});
	
}

function vaildateParam() {
	var subject = $.trim($("#subject").val());
	var result = $.validChar(subject);
	if (result) {
		showAlert("标题不能包含非法字符：" + result);
		$("#subject").focus();
		return false;
	}
	if (subject == "") {
		showAlert("标题不能为空");
		$("#subject").focus();
		return false;
	}
	return true;
}

//保存主体信息
function saveDraftInfo(){
	if (!vaildateParam()) {
		flag = false ;
		return;
	}
	var subject = $("#subject").val();
	var layout = $("#infoLayout").text();
	var infoclass = "";
	var groupname = $("#groupname").val();
	var subjectcolor = $("#titleColor").val();
 	var displaymode = useWordEdit ?"0": "1";
 	var content =$.trim($("#wysiwyg").val());
	
	$.ajax({
        type : "POST",
        cache: false,
        async: false,
		url  : "m/info_draft",
        data : {act:"savedraftinfo",
        		id:inforId,
        		subject: subject,
        		layout:layout,
        		infoclass:infoclass,
        		groupname:groupname ? groupname : "",
        		subjectcolor:subjectcolor,
        		displaymode: displaymode,
        		content:content
        		},
        success : function(xml){
            var message = $("message",xml);
            if (message.attr("code") == "1") {
            	inforId = $("message",xml).text();
            }else{
                flag = false ;
                showAlert(message.text());
            } 
        },
        error : $.ermpAjaxError
	});
}

//保存内容Word
function saveOfficContent(){
	var obj = NTKO_OFFICE_OCX.saveOffice(inforId);
	if(obj != null && obj.code == 0){
		flag = false ;
		showAlert(obj.message);
	}
};

//转成HTML后提交保存
function saveContentHtml(){
	var obj = NTKO_OFFICE_OCX.saveAsHTML(inforId);
	if(obj == null){
		showAlert("OFFICE控件未正确安装，文件发布失败！");
		flag = false;
	}else{
		if (obj.code == 0){
			flag = false ;
			showAlert(obj.message);
		}
	}
}

//提交保存附件
function saveAttachment(){
	NTKO_ATTACH_OCX.saveAttachments(inforId);
}

function saveDraftAttachmentEvent(code,message){
	if(code == 0){
		flag = false ;
		showAlert(message);
	}
}

function saveAttachmentEvent(code,message){
	var isPublish = true;
	if(code == 0){
		flag = false ;
		showAlert(message);
	}else{
		if(flowKey == "DEFAULT_VALUE_ATONCE"){
			isPublish = false;
		}
		$.ajax({
	        type : "POST",
	        cache: false,
	        async: false,
			url  : "m/info_draft",
	        data : {act:"add",id: inforId,flag:isPublish,flowkey:flowKey},
	        success : function(xml){
	            var message = $("message",xml);
	            if (message.attr("code") == "0") {
	            	flag = false ;
	                showAlert($("message",xml).text());
	            } 
	        },
	        error : $.ermpAjaxError
		});
	}
}

function showAlert(info, fun) {
	if (isIE) {
		alert(info);//控件只能作系统提示，否则会被挡住
		if (fun) {
			fun();
		}
	} else {
		$.alert(info, fun);
	}
}