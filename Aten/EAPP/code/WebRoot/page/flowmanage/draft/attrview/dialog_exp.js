
//初始化页面
$(initPage);

function initPage(){
	var param = parent.window.dialogParam;
	var varHtml = "";
	if (param.metas) {
		$(param.metas).each(function(i) {
			varHtml += "<div>" + param.metas[i].name + "**" + param.metas[i].displayName + "(" + param.metas[i].type + ")</div>";
		});
	}
	var flowVarsSel = $("#flowVarsDiv").html(varHtml).ySelect({width: 150});
	flowVarsSel.select(0);
	var varOptSel = $("#VarOptDiv").ySelect({width: 25});
	
	$("#expression").val($("#" + param.expInputId, window.parent.document).val()).focus();
	$("#cancelBtn").click(function(){
		param.callback();
	});
	$("#comitBtn").click(function(){
		//验证表达式
		$.ajax({
			type : "POST",
			cache: false, 
			url  : BASE_PATH + "m/flow_var/validexp",
			dataType : "json",
			data : {expression : $("#expression").val(),
				returnType : param.returnType,
				flowJson : $.toJSON(param.metas)},
			success : function(data){
				if ($.checkErrorMsg(data)) {
	//				$.alert("保存存成功！");
					param.callback($("#expression").val(), param.expInputId);
				}
	        }
		});
	});
	
	$("#addVar").click(function(){
		$("#expression").val($("#expression").val() + " " + flowVarsSel.getValue() + " " + varOptSel.getValue());
	});
};
