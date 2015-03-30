/**
 * 父页面传过来的参数
 */
var args = parent.window.dialogParam;

/**
 * 附件对象
 */
var ocx;

$(initPage);
function initPage(){
	
	// 初始化文件上传控件
	initControl();
	
	// 保存
	$("#save").click(function() {
		saveRefuncNotice();
	});
	
	//取消
	$("#close").click(function() {
		args.returnValue = false;
		args.callback();
	});
	
	//用户选择弹出框
//    $('#openDispacher').click(
//	   	function(e){
//	   		dialog = new UserDialog(ERMP_PATH, BASE_PATH);
//	   		dialog.setCallbackFun(callbackFun);
//	   		var user = dialog.openDialog();
//		}
//	);
}

function callbackFun() {
	var selected = dialog.selected;
		if (selected!=null){
			$("#linkman").val(selected[0].id);
			$("#linkmanDis").val(selected[0].name);
		}
}


/**
 * 保存/修改
 */
function saveRefuncNotice(){
	if(!validateForm()){
		return;
	}
	var refuncNoticeJson = getRefuncNoticeJson();
	var act = $("#id").val() == ""  ? "addRefuncNotice" : "modifyRefuncNotice";
	// 保存主体
	$.ajax({
		   type : "POST",
		   cache: false, 
		   url  : BASE_PATH + "/m/operationsManage/" + act, 
		   dataType : "json",
		   data : {
			   refuncNoticeJson : refuncNoticeJson
		   },
		   success : function(data,i){
			   debugger;
			   if ($.checkErrorMsg(data)) {
				   // 保存主题成功，准备上传附件
				   ocx.refId = data.refuncNotice.id;
				   saveFileList();
			   } else {
				   args.returnValue = false;
				   args.callback();
			   }
		   },
		   error : $.ermpAjaxError
		});
}

/**
 * 封装数据
 * @return {Boolean}
 */
function getRefuncNoticeJson(){
	var refuncNotice = {};
	var refuncNoticeId = $("#id").val();
   	if (refuncNoticeId) {
   		refuncNotice.id = refuncNoticeId;
   	}
	
   	refuncNotice.trustCompany = $.trim($("#trustCompany").val());
   	refuncNotice.refundNotice = $.trim($("#refundNotice").val());
   	refuncNotice.linkman = $.trim($("#linkman").val());
   	
	var refuncNoticeJson = $.toJSON(refuncNotice);
	return refuncNoticeJson;
}

/**
 * 表单验证
 * @return {Boolean}
 */
function validateForm(){
	if(true)
		return true;
	
	if($.validInput("custName", "公司名称", true, "\<\>\'\"", 128)){
		return false;
	}
	if($.validInput("tel", "电话", true, "", 128)){
		return false;
	}
  	if(sexSel.getValue() == ""){
  		$.alert("性别不能为空");
  		return false;
  	}
  	if(customerNatureSel.getValue() == ""){
  		$.alert("客户性质不能为空");
  		return false;
  	}
	return true;
}

/**---------------------------附件上传相关----------------------------------*/
/**
 * 初始化附件上传控件
 */
function initControl() {
	// 获取控件实例,需要提供容器的ID
	ocx = $.getNewAttachmentControl("NTKO_AttachmentCtrl");
	ocx.setPermission({
		view : true,
		add : true,
		update : true,
		del : true,
		saveas : true
	});

	ocx.setBasePath(BASE_PATH);
	// 定义保存成功或失败后执行的回调函数，执行时将自动带上code和message
	ocx.setAfterSaveEvent("saveAttachmentEvent");
	ocx.setSaveURL(BASE_PATH + "m/operationsManage/uploadRefuncNoticeFile");
	ocx.init("NTKO_AttachmentCtrl");
	
	// 取得退款须知的ID
	var refuncNoticeId = $("#id").val();
	if (refuncNoticeId) {
		// 加载文件列表
		var sUrl = BASE_PATH + "m/operationsManage/loadRefuncNoticeAttachments";
		$.post(sUrl, {"referid" : refuncNoticeId}, callbackfun, "json");
	}
}

/**
 * 回调函数：把加载回来的数据展示在附件上传控件上
 * @param data
 */
function callbackfun(data) {
	if ($.checkErrorMsg(data)) {
		ocx.setFileList(data.refunNoticeAttachments);
	}
}

/**
 * 回调函数：上传完附件之后的操作
 */
function saveAttachmentEvent(code, message) {
	if (code == "1") {
		args.returnValue = true;
		args.callback();
	} else {
		args.returnValue = false;
	}
}

/**
 * 上传附件
 */
function saveFileList() {
	ocx.saveAttachments();
}
