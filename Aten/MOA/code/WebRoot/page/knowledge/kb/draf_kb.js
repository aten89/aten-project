var mainFrame = $.getMainFrame();
var NTKO_ATTACH_OCX;
var firstTypeSel; // 一级分类下拉框
var secondTypeSel; // 二级分类下拉框
$(initArchivesManage);
function initArchivesManage(){
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
	$("#addKnow").click(function(){
		submitKnowledge();
	});
	initControl();

	// 一级分类
	firstTypeSel = $("#firstTypeSel").ySelect({
		width: 110,
		url  : "m/knowledge", 
		data : {act:"firstType"},
		afterLoad : function() {
			firstTypeSel.select($("#firstTypeForShow").val());
		}
	});
	
	// 二级分类
	secondTypeSel = $("#secondTypeSel").ySelect({
		width: 110,
		url  : "m/knowledge", 
		data : {act:"secondType"},
		afterLoad : function() {
			secondTypeSel.select($("#secondTypeForShow").val());
		}
	});
    // 关键字
	initLabel();
}

function getContentImg() {
	var params = new Object();
	params.szURL ="";
	params.id = $("#id").val();
	var returnStatus = window.showModalDialog(BASE_PATH + "page/knowledge/kb/dialog_addimg.jsp",params,"dialogHeight:400px;dialogWidth:565px;status:no;scroll:auto;help:no");
 	if(returnStatus){
 		 if ( params.szURL && params.szURL.length > 0 ){
           return params.szURL;
 		 }
 	}	
	return null;
}

function initControl(){
	//附件控件
	var id = $("#id").val();
  	NTKO_ATTACH_OCX = $.getNewAttachmentControl("NTKO_AttachmentCtrl");
	NTKO_ATTACH_OCX.setPermission({view:true,add:true,update:true,del:true,saveas:true});
	NTKO_ATTACH_OCX.setSaveURL(BASE_PATH + "m/knowledge?act=uploadattachments");
	NTKO_ATTACH_OCX.setBasePath(BASE_PATH);
	NTKO_ATTACH_OCX.setAfterSaveEvent("saveAttachmentEvent");
	NTKO_ATTACH_OCX.init();
	if(id !=null && id.length>0){
		$.ajax({
	       type : "POST",
	       cache: false,
	       url  : "m/knowledge",
	       data : "act=getattachments&referid=" + id,
	       success : function(xml){
	           var messageCode = $("message",xml).attr("code");
	           var msg = $("message",xml).text();
	           if(messageCode == "1"){
	               NTKO_ATTACH_OCX.setFileList($("content",xml)[0]);
	           } else {
	               $.alert(msg);
	           }
	       }
	   	});
	}
}

function saveAttachmentEvent(code,message){
	if(code == 0){
		$.alert(message);
	} else {
		$.alert("保存成功",function(){
			//执行回调函数，刷新父列表
			mainFrame.getCurrentTab().doCallback();
			//关闭自己
			mainFrame.getCurrentTab().close();
		});
	}
}

function saveAttachment(id){
	NTKO_ATTACH_OCX.saveAttachments(id);
}

/**
 * 保存知识点信息
 */
function submitKnowledge(){
	var subject = $.trim($("#subject").val());
	var result = $.validChar(subject);
	if (result) {
		$.alert("标题不能包含非法字符：" + result);
		$("#subject").focus();
		return false;
	}
	if (subject == "") {
		$.alert("标题不能为空");
		$("#subject").focus();
		return false;
	}
	var label =$.trim($("#label").val());
	result = $.validChar(label);
	if (result) {
		$.alert("标签不能包含非法字符：" + result);
		$("#label").focus();
		return false;
	}

	var remark =$.trim($("#remark").val());
	result = $.validChar(remark);
	if (result) {
		$.alert("概述不能包含非法字符：" + result);
		$("#remark").focus();
		return false;
	}
	if(remark.length >4000){
		$.alert("概述长度不能超过4000");
		$("#remark").focus();
		return false;
	}
	
	// 知识内容 -> 转码html格式
	var content =$.trim($("#wysiwyg").val());
//	content = $.trim(content.replaceAll("&nbsp;",""));
	// 去除html元素,保留img 和 p 标签
//	content = $.trim(content.replaceAll("<(?!img|p|/p).*?>",""));
	if (content == "") {
		$.alert("请填写知识内容!");
		$("#wysiwyg").focus();
		return false;
	}
	var status = $("#status").val();
//	var content =encodeURIComponent($.trim($("#wysiwyg").val()));//转码html格式
	var knowClassId = $("#knowClassId").val();
	var id = $("#id").val();
    var firstType = firstTypeSel.getValue();
    var secondType = secondTypeSel.getValue();
    
    $("#addKnow").attr("disabled",true);
    $.ajax({
		type : "POST",
				cache: false,
		      	url : "m/knowledge",
		      	dataType : "xml",
		      	data : {
		      		act : "saveknowledge",
		      		id : id,
		      		classId: knowClassId,
					subject : subject,
					label : label,
					remark : remark,
					content : content,
					firstType: firstType,
					secondType:secondType,
					status : status
		      	},
		        success : function(xml){
		        	var message = $("message",xml);
		        	if(message.attr("code") == "1") {
		        		var knowId = $("message",xml).text();
						saveAttachment(knowId);
		        	}else{
		        		$("#addKnow").attr("disabled",false);
		        		$.alert(message.text());
		        	}
		        },
	        	error : $.ermpAjaxError
	});
	
}

function initLabel(){
	$.getJSON("m/kb_label?act=autocomplete",function(data){
		$("#label").autocomplete(data, {
			minChars: 0,
			width: 150,
			matchContains: true,
			autoFill: false,
			multiple: true,
			multipleSeparator: " ",
			max:10
//			formatItem: function(row, i, max) {
//				return row.keyword;
//			},
//			formatResult: function(row) {
//				return row.keyword;
//			}
		});
	});	
}
