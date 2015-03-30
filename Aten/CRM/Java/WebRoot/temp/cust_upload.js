var mainPanel = $.getMainFrame();
$(initPage);
function initPage() {
		var code =$("#code").val();
		var msg = $("#msg").val();
		var customerManage = $("#customerManage").val();
		if (msg!="") {
			alert(msg);
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