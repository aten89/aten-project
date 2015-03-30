var mainFrame = $.getMainFrame();

$(initPublish);
var NTKO_ATTACH_OCX,NTKO_OFFICE_OCX;
var flag = true;
var docFormId;
var layouts;
var redHeaderArgs = new Object();
redHeaderArgs.redHeader="";
redHeaderArgs.redHeaderUrl="";
redHeaderArgs.docTmpId=""
redHeaderArgs.docOrder="";
redHeaderArgs.year="";
function initPublish(){
	//添加权限约束
	$.handleRights({
		 "commitBut" : $.OaConstants.DISPOSE,
		 "redHeaderBtn" : $.OaConstants.DISPOSE 
	});
	//提交
  	$("#commitBut").find(".allBtn").each(function(){
  		$(this).click(function(){
  			if(!redHeaderArgs.redHeader && $.trim($("#docNum").text()) == ""){
  				alert("请先进行套红头操作");
  				return;
  			}
			var comment = $("#comment").val();//填写的意见
			var result = $.validChar(comment,"<>");
			if (result){
				alert("意见审批中不能输入非法字符："+ result);
				return ;
			}
	
  			if (confirm("确认是否" + $(this).val() + "？")) {
  				var rValue = window.showModalDialog(BASE_PATH + "/m/dis_start?act=initinfolayoutselect","","dialogHeight:240px;dialogWidth:380px;status:no;scroll:auto;help:no");
  				if (!rValue) {
  					return;
  				}
  				if (rValue != "PUBLISH_NOTHING") {
  					layouts = rValue;
  				} else {
  					layouts = "";
  				}
  				$("#transitionName").val($(this).val());
  				submitDoc();
  			} else {
  				return ;
  			}
  		});
  	});
  	
  	$("#redHead").click(function(){ 		
  		var returnStr = window.showModalDialog(BASE_PATH + "/m/doc_no?act=initredheaderselect",redHeaderArgs,"dialogHeight:280px;dialogWidth:600px;status:no;scroll:auto;help:no");
  		if(returnStr =="submit"){
  			var url = "";
  			if (redHeaderArgs.redHeaderUrl != "") {
  				url = BASE_PATH+redHeaderArgs.redHeaderUrl;
  			}
  			NTKO_OFFICE_OCX.setRedHeader(url);
  			NTKO_OFFICE_OCX.setBookmarkValue("docNumber",redHeaderArgs.redHeader);//把红头字串代替书签
  		} else{
  			redHeaderArgs.redHeader = "";
  			$("#docNum").html("");
  			return;
  		} 		
  		$("#docNum").html(redHeaderArgs.redHeader);
  	});
  
	//日志展开
	$("#costsLog a").toggle(function(){
		$("#costsLog").find(".mb").hide();
		$("#costsLog h1").removeClass("logArrow");
	},function(){
		$("#costsLog").find(".mb").show();
		$("#costsLog h1").addClass("logArrow");
		
	});
	
	//取消FIXME
  
	initControl();
	$("#fullScrean").click(function(){
		NTKO_OFFICE_OCX.showFullWindow();	
	});

};
//初始化控件
function initControl(){
	
	docFormId = $("#docFormId").val();
	//附件控件
  	NTKO_ATTACH_OCX = $.getNewAttachmentControl("NTKO_AttachmentCtrl");
	NTKO_ATTACH_OCX.setPermission({view:true,add:false,update:false,del:false,saveas:false});
	NTKO_ATTACH_OCX.setSaveURL(BASE_PATH + "m/dis_start?act=upload");
	NTKO_ATTACH_OCX.setBasePath(BASE_PATH);
	NTKO_ATTACH_OCX.init();
	if (docFormId != "") {
		//加载文件列表
		$.ajax({
	       type : "POST",
	       cache: false,
	       async: false,
	       url  : "m/dis_start",
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
	NTKO_OFFICE_OCX.setSaveURL(BASE_PATH + "m/dis_start?act=uploadcontent");
	NTKO_OFFICE_OCX.setPublishURL(BASE_PATH + "m/dis_start?act=uploadhtml");
	NTKO_OFFICE_OCX.setSaveFinalURL(BASE_PATH + "m/dis_start?act=savefinal");
	NTKO_OFFICE_OCX.setBasePath(BASE_PATH);
	NTKO_OFFICE_OCX.setOperator($("#userId").val());
	NTKO_OFFICE_OCX.init("NTKO_OfficeCtrl");
	NTKO_OFFICE_OCX.openOfficeFile(BASE_PATH + $("#draftDocUrl").val(),"approve");
};


function submitDoc() {
	$(".allBtn").attr("disabled","true").addClass("icoNone");
	flag = true;
	//保存OFFIC内容
	saveOfficContent();
	if (!flag) {
		$(".allBtn").removeAttr("disabled","true").removeClass("icoNone");
		return;
	}
	//套了红头的正文文档
	saveRedHeaderDoc();
	if(!flag){
		$(".allBtn").removeAttr("disabled","true").removeClass("icoNone");
		return;
	}

	flag = true;
	saveContentHtml();
	if (!flag) {
		$(".allBtn").removeAttr("disabled","true").removeClass("icoNone");
		return;
	}
	
//	NTKO_ATTACH_OCX.setAfterSaveEvent("saveAttachmentEvent");
	//保存附件
//	saveAttachment();
//	if (!flag) {
//		$(".allBtn").removeAttr("disabled","true").removeClass("icoNone");
//		return;
//	}

	commitDispose();
}

function saveOfficContent(){
	var obj = NTKO_OFFICE_OCX.saveOffice(docFormId);
	if(obj != null && obj.code == 0){
		flag = false ;
		alert(obj.message);
	}
};

function saveContentHtml(){
	var params = encodeURI("&infoLayout="+layouts);
	var obj = NTKO_OFFICE_OCX.saveAsHTML(docFormId,'',params);
	if(obj == null){
		alert("OFFICE控件未正确安装，文件发布失败！");
		flag = false;
	}else{
		if (obj.code == 0){
			flag = false ;
			alert(obj.message);
		}
	}
}

function saveOfficContent(){
	var obj = NTKO_OFFICE_OCX.saveOffice(docFormId);
	if(obj != null && obj.code == 0){
		flag = false ;
		alert(obj.message);
	}
};

//function saveAttachment(){
//	NTKO_ATTACH_OCX.saveAttachments(docFormId);
//}


//function saveAttachmentEvent(code,message){
//	if(code == 0){
//		flag = false ;
//		alert(message);
//	}else{
//		commitDispose();
//	}
//}

function commitDispose(){
	var docFormId = $("#docFormId").val();
	$.ajax({
       type : "POST",
       cache: false,
       async: false,
       url  : "m/dis_deal",
       data : {act:"deal_approve",
       			tiid:$("#taskInstanceID").val(),
       			transition:$("#transitionName").val(),
       			comment:$("#comment").val(),
       			docFormId: docFormId
       			},
       success : function(xml){
           var messageCode = $("message",xml).attr("code");
           var msg = $("message",xml).text();
           if(messageCode == "1"){
               alert($("message",xml).text());
               //执行回调函数，刷新父列表
               mainFrame.getCurrentTab().doCallback();
               //关闭自己
               mainFrame.getCurrentTab().close();
           } else {
               alert($("message",xml).text());
           }
       },
       error : $.ermpAjaxError
   	});
};

function saveRedHeaderDoc(){	
	var params = "&headerTmpId="+redHeaderArgs.docTmpId+"&docNum="+redHeaderArgs.docOrder+"&year="+redHeaderArgs.year+"&headerStr="+encodeURI(redHeaderArgs.redHeader);
	var obj = NTKO_OFFICE_OCX.saveFinalOffice(docFormId,params);
	if(obj == null){
		alert("OFFICE控件未正确安装，红头文件保存失败！");
		flag = false;
	}else{
		if (obj.code == 0){
			flag = false ;
			alert(obj.message);
		}
	}
}