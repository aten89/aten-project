/*******************
 * 客户资料列表字段设置
 *******************/ 
var columns;
$(fieldSetPage)

function fieldSetPage(){
	
	columns = parent.window.dialogParams; 
	initFields();
	
	$("#saveFlieds").click(function(){
		if($("[type='checkbox'][checked]").length == 0){
			$.alert("请至少选择一个字段");
			return;
		}
		
		for (var i=0; i<columns.length; i++) {
			if($("#" + columns[i].name).attr("checked") == true){
				columns[i].defaultShow = true;
			} else {
				columns[i].defaultShow = false;
			}
		}
		var re = new RegExp("^([-]){0,1}([0-9]){1,}([.]){0,1}([0-9]){0,}$","igm");
		if(!re.test($.trim($("#pageNo").val()))){
	   		$.alert("每页显示记录数不是有效的数字类型");
	   		$("#pageNo").focus();
	   		return false;
	   	}
	   	if (Number($.trim($("#pageNo").val()))<=0) {
			$.alert("每页显示记录数不能小于0");
	   		$("#pageNo").focus();
	   		return false;
		}
		columns.pageNo= $("#pageNo").val();
		
		parent.closeDialog();
	});
	
	$("#cancelFlieds").click(function(){
		parent.closeDialog();
	});
	
}
//初始化字段
function initFields(){
	var html="";
	for(var i=0; i<columns.length; i++){
		var column = columns[i];
		html +="<li style='width:133px'>";
		html +="<input id=\"" + column.name + "\" type=\"checkbox\" class=\"cBox\"";
		if(column.defaultShow){
			html +=" checked";
		}
		html +="/>";
		html +=column.name;
		if (column.request) {
			html +="<span class='cRed'>*</span>";
		}
		html += "</li>";
	}
	$("#pageNo").val(columns.pageNo);
	$("#columnList").html(html);
}

function selectAll(flag) {
	$("input", "#columnList").each(function(){
		$(this).attr("checked", flag);
	});
}