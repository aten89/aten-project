$(initView);
var NTKO_ATTACH_OCX,NTKO_OFFICE_OCX;

function initView(){
  
	//日志展开
	$("#costsLog a").toggle(function(){
		$("#costsLog").find(".mb").hide();
		$("#costsLog h1").removeClass("logArrow");
	},function(){
		$("#costsLog").find(".mb").show();
		$("#costsLog h1").addClass("logArrow");
		
	});
	
	//初始化附件
  	initNTKOAttach();
  	
	var useWordEdit = ($("#displayMode").val() == "0");//0是内容地址形式，即用Word编辑
	if (useWordEdit) {
  		initNTKOWrod();
  	} else {
  		initHTMLEdit();
  	}
  	
	$("#fullScrean").click(function(){
		NTKO_OFFICE_OCX.showFullWindow();	
	});

};

function showRevisions(obj){
	NTKO_OFFICE_OCX.showRevisions(obj.checked);
}

//初始化附件控件
function initNTKOAttach(){
	var inforId = $("#infoFormId").val();
	//附件控件
  	NTKO_ATTACH_OCX = $.getNewAttachmentControl("NTKO_AttachmentCtrl");
	NTKO_ATTACH_OCX.setPermission({view:true,add:false,update:false,del:false,saveas:true});
	NTKO_ATTACH_OCX.setSaveURL(BASE_PATH + "m/info_draft?act=upload");
	NTKO_ATTACH_OCX.setBasePath(BASE_PATH);
	NTKO_ATTACH_OCX.init();
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
               alert($("message",xml).text());
           }
       }
   	});
}
//初始化正文控件
function initNTKOWrod(){
	var inforId = $("#infoFormId").val();
	//OFFIC控件
	NTKO_OFFICE_OCX = new NTKOOfficeControl();
	NTKO_OFFICE_OCX.setSaveURL(BASE_PATH + "m/info_draft?act=uploadcontent");
	NTKO_OFFICE_OCX.setPublishURL(BASE_PATH + "m/info_draft?act=uploadhtml");
	NTKO_OFFICE_OCX.setBasePath(BASE_PATH);
	NTKO_OFFICE_OCX.setOperator($("#userId").val());
	NTKO_OFFICE_OCX.init("NTKO_OfficeCtrl");
	if( inforId != ""){
		NTKO_OFFICE_OCX.openOfficeFile(BASE_PATH + $("#contentDocUrl").val(),"readonly");
	}else{
		NTKO_OFFICE_OCX.openOfficeFile(BASE_PATH + "page/template/emptyDocTemplate.doc","readonly");
	}
};

//初始化HTML编辑器
function initHTMLEdit() {
	$("#NTKO_OfficeCtrl").removeAttr("style");
	$("#contentSet").html("");
	$("#contentVal").show();
}
