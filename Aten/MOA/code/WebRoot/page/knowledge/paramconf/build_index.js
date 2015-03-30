$(initParameterSet);

function initParameterSet(){
	//添加权限约束
	$.handleRights({
	   "updateIndex" : $.SysConstants.MODIFY,
	   "rebuildIndex" : $.SysConstants.MODIFY
	});
	$("#updateIndex").click(function(){
		submitAction("modify_update");
	});
	
	$("#rebuildIndex").click(function(){
		submitAction("modify_rebuild");
	});
}

function submitAction(act) {
	$.confirm("确认操作索引吗？",function(r){
		if (r) {
			$("#updateIndex,#rebuildIndex").attr("disabled","true").addClass("icoNone");
			$.ajax({
		       type : "POST",
		       cache: false,
		       url  : "m/kb_index",
		       data : {act:act},
		       success : function(xml){
		           var messageCode = $("message",xml).attr("code");
		           	$.alert($("message",xml).text());
		           	$("#updateIndex,#rebuildIndex").removeAttr("disabled","true").removeClass("icoNone");
		       },
		       error : $.ermpAjaxError
		   	});	
		}
	});
}