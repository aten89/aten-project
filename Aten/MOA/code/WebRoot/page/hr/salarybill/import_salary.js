$(initPage);
function initPage(){
	var code = $("#code").val();
	var msg = $("#msg").val();
	if (msg) {//有错误信息，说明是提交后转向过来
		if (code == "1") {
			//是否导入
	//				var rel = confirm(msg);
	//			    $.ajax({
	//					type : "POST",
	//					cache: false,
	//					url  : "m/outlay_import",
	//					data : "act=confirm&confirm=" + rel,
	//					success : function(xml){
	//						if (rel) {
	//							alert($("message",xml).text());
	//						}
	//					},
	//					error : $.ermpAjaxError
	//				});
			
			$.alert("导入成功", function() {
				parent.window.returnValue = true;
				parent.closeDialog();
			});
			
		} else {
			$.alert(msg);
		}
	}
	
	//导入
	$("#submitBtn").click(function(){
  		submitFrm();
  	});
  	
	var d=new Date(); 
    var year = d.getFullYear();
    var month = d.getMonth() + 1;
    $("#monthSel").val((month < 10 ? "0" : "") + month);
    
    var yearHTML = "";
    for (var i = 0; i <10; i++){
    	yearHTML += "<option value='" + (year - i) + "'>" + (year - i) + "年</option>";
    }
    
    $("#yearSel").html(yearHTML);
}
		
function submitFrm() {
	var filepath=$("#mportFile").val();
	if (filepath == "") {
		$.alert("请选择导入的文件");
		return;
	}
	$.confirm("确认要导入" +  $("#yearSel").val() + "年" +  $("#monthSel").val() +  "月的数据吗?", function(r){
		if (r) {
			fileext=filepath.substring(filepath.lastIndexOf('.')+1,filepath.length).toLowerCase();
			if(fileext != 'xls' && fileext != 'xlsx') {
				$.alert("只能上传Excel文件");
				return;
			}
			var month = $("#yearSel").val() + $("#monthSel").val();
			$("#month").val(month);
			var count = getCountByMonth(month);
			if(count > 0) {
				$.confirm($("#yearSel").val() + "年" +  $("#monthSel").val() +  "月已有" + count + "条数据，确认要重新导入吗?", function(r1){
						if (r1) {
							$("#submitBtn").attr("disabled","true");
							$('#impForm').submit();
						}
				});
			} else {
				$("#submitBtn").attr("disabled","true");
				$('#impForm').submit();
			}
		} 
	});
}

function getCountByMonth(month) {
	var count = 0;
	$.ajax({
   		type : "POST",
   		cache: false,
    	async : false,
   		url  : "m/salary_bill",
   		data : {
   			act:"count",
			month:month
   		},
   		success : function(xml){
   			count = $("message",xml).text();
  		}
	});
	return count;
}