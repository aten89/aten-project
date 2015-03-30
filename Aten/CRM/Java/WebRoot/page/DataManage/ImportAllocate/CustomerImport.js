var mainPanel = $.getMainFrame();
$(initPage);
function initPage() {
		var code =$("#code").val();
		var msg = $("#msg").val();
		var customerManage = $("#customerManage").val();
		if (msg!="") {
			if (code == "1") {
				//是否导入
				var rel = confirm(msg);
				if(rel){
					if (customerManage != "" && customerManage != "undefined") {
						$.post("m/customer/import_myCustomer","", callbackfile,"json");
					} else {
						$.post("m/importCustomer/importCustomer","",
								callbackfile,"json");
					}
					}
			} else {
				alert(msg);
			}
		}
}

function callbackfile(data) {
	if ($.checkErrorMsg(data)) {
		mainPanel.getCurrentTab().doCallback(data.batchNumber);
		alert("导入成功");
		mainPanel.getCurrentTab().close();
		}
}	

function submitFrm() {
	var filepath=$("#mportFile").val();
	if (filepath == "") {
		alert("请选择导入的文件");
		return false;
	}
	fileext=filepath.substring(filepath.lastIndexOf('.')+1,filepath.length);
	if(fileext != 'xls') {
		alert("只能上传Excel2003文件");
		return false;
	}
		return true;
}	