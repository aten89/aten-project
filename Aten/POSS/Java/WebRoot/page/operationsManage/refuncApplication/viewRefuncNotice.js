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
		add : false,
		update : false,
		del : false,
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
