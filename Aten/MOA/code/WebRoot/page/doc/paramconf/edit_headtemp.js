var mainFrame = $.getMainFrame();

$(initHeadTemplate);
var NTKO_ATTACH_OCX,NTKO_OFFICE_OCX;
var docNumberId;
function initHeadTemplate(){
	$("#saveTemplate").click(function(){
		submitTemplate();
	});
	
	$("#cancelTemplate").click(function(){
		if(window.confirm("确认取消")) {
			mainFrame.getCurrentTab().close();
		}
	});
	
	docNumberId = $("#docNumberId").val();
	initControl();
}

//初始化控件
function initControl(){
	
	//OFFIC控件
	NTKO_OFFICE_OCX = new NTKOOfficeControl();
	NTKO_OFFICE_OCX.setSaveURL(BASE_PATH + "m/doc_no?act=uploadtemplate&referid="+docNumberId);
//	NTKO_OFFICE_OCX.setPublishURL(BASE_PATH + "m/info_draft?act=uploadhtml");
	NTKO_OFFICE_OCX.setBasePath(BASE_PATH);
//	NTKO_OFFICE_OCX.setOperator($("#userId").val());
	NTKO_OFFICE_OCX.init("NTKO_OfficeCtrl");
	
	//判断：无url-->创建新Office; 有url-->打开
	//NTKO_OFFICE_OCX.openOfficeFile(BASE_PATH + $("#headTemplateUrl").val());
	if("" == $("#headTemplateUrl").val()) {
		NTKO_OFFICE_OCX.openOfficeFile(BASE_PATH + "page/template/emptyDocTemplate.doc","draft");
	}
	else {
		NTKO_OFFICE_OCX.openOfficeFile(BASE_PATH + $("#headTemplateUrl").val(),"draft");
	}
	
}

function insertBookmark(sText,sName){
	NTKO_OFFICE_OCX.insertBookmark(sText,sName);
}

function submitTemplate() {
	//保存OFFIC内容
	saveOfficContent();
	alert("操作成功");
	//执行回调函数，刷新父列表
    mainFrame.getCurrentTab().doCallback();
    //关闭自己
    mainFrame.getCurrentTab().close();
}



function saveOfficContent(){
	var obj = NTKO_OFFICE_OCX.saveOffice(docNumberId);
}

