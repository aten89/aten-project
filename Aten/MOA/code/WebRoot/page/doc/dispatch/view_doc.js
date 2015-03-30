$(initView);
var NTKO_ATTACH_OCX,NTKO_OFFICE_OCX;
var docFormId;
var flowKey;
function initView(){
  
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
	NTKO_ATTACH_OCX.setPermission({view:true,add:false,update:false,del:false,saveas:true});
	NTKO_ATTACH_OCX.setSaveURL(BASE_PATH + "m/dis_start?act=upload");
	NTKO_ATTACH_OCX.setBasePath(BASE_PATH);
	NTKO_ATTACH_OCX.init();
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
	
	//OFFIC控件
	NTKO_OFFICE_OCX = new NTKOOfficeControl();
	NTKO_OFFICE_OCX.setSaveURL(BASE_PATH + "m/dis_start?act=uploadcontent");
	NTKO_OFFICE_OCX.setPublishURL(BASE_PATH + "m/dis_start?act=uploadhtml");
	NTKO_OFFICE_OCX.setBasePath(BASE_PATH);
	NTKO_OFFICE_OCX.setOperator($("#userId").val());
	NTKO_OFFICE_OCX.init("NTKO_OfficeCtrl");
	NTKO_OFFICE_OCX.openOfficeFile(BASE_PATH + $("#draftDocUrl").val(),"readonly");
};
