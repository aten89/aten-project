var mainFrame = $.getMainFrame();

$(initFlowList);
//当前是否为编辑状态
var edit=false;
function initFlowList() {
	$.handleRights({
        "addFlow" : $.SysConstants.ADD//新增
    });
    
    //新增请假流程配置
	$("#addFlow").click(function(){
		addInfoFlow();
	});
	//刷新
  	$("#refresh").click(function(){
  	    $("#holidayFlowList").empty();
  		queryList();
  		$("#addFlow").removeAttr("disabled","true").removeClass("icoNone");
		edit=false;
  	});
	//查询
	queryList();
}

function queryList(){
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/holi_type",
		data : "act=query",
        success : function(xml){
            //解析XML中的返回代码
            var message = $("message",xml);
            if(message.attr("code") == "1"){
                var tBodyHTML = "";
                $("holiday-type",xml).each(
                    function(index){
                        var holidayFlow = $(this);
                         tBodyHTML += createHtml(holidayFlow.attr("id"), $("holiday-name",holidayFlow).text(), $("max-days",holidayFlow).text(), 
                         		$("expression",holidayFlow).text(), $("desc",holidayFlow).text());
                    
                    }
                );
                $("#holidayFlowList").html(tBodyHTML);
                $("#addFlow").removeAttr("disabled","true").removeClass("icoNone");
				edit=false;
            } else{
               $.alert($("message",xml).text());
            };
        },
        error : $.ermpAjaxError
    });
}
//新增流程配置
function addInfoFlow(){
	$("#addFlow").attr("disabled","true").addClass("icoNone");
	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}
	var infoFlowList = createEditHtml("","","","","");
	if($("#holidayFlowList tr").length==0){
	  $("#holidayFlowList").append(infoFlowList);
	}else{
	  $(infoFlowList).insertBefore($("#holidayFlowList tr:eq(0)"));
	}
	cancelEnter($("#description"));
}

//保存
function saveInfoFlow(self) {
	
	var holidayName =$.trim($("#holidayName").val());
	//判断
	if (!holidayName) {
		$.alert("假期名称不能为空");
		return;
	}
	var maxDays = $.trim($("#maxDays").val());
	if ($.validNumber("maxDays","单次最长天数",false)){
		return;
	}
	var expression = $.trim($("#expression").val());
	var desc = $.trim($("#description").val());

	var result = $.validChar(desc, "'\"<>");
	if (result) {
		$.alert("说明不能输入非法字符：" + result);
		$("#description").focus();
		return;
	}

	// 提交到后台
	$.ajax({
		type : "POST",
		cache : false,
		async : true,
		url : "m/holi_type",
		data : {
			act : "add",
			holidayname : holidayName,
			maxdays : maxDays,
			exp : expression,
			desc : desc
		},
		success : function(xml) {
			// 解析XML中的返回代码
			var messageCode = $("message", xml).attr("code");
			if (messageCode == "1") {
				$.alert("保存成功");
				queryList();
				$("#addFlow").removeAttr("disabled", "true").removeClass("icoNone");
			} else {
				$.alert($("message", xml).text());
			}
			edit = false;
		},
		error : $.ermpAjaxError
	});
}

function cancelInsert(self) {
	$(self).parent().parent().remove();
	$("#addFlow").removeAttr("disabled","true").removeClass("icoNone");
	edit=false;
}


//修改
function modifyInfoLayout(self,id){
	$("#addFlow").attr("disabled","true").addClass("icoNone");
	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}
	var TR = $(self).parent().parent();
//	var id = TR.attr("id");
	var selfObj=$(self).parent().parent().find("td");
	var holidayName = selfObj.eq(0).text();
	var maxDays = selfObj.eq(1).text();
	var expression = selfObj.eq(2).text();
	var description = selfObj.eq(3).text();
	var html =createEditHtml(id, holidayName, maxDays, expression, description);
	TR.replaceWith(html);
	
	cancelEnter($("#description"));
}

function createEditHtml(id, holidayName, maxDays, expression, description){
	var html ="<tr id=\""+id+"\">" +
	"<td class=\"listData\"><input id=\"holidayName\" type=\"text\" style=\"width:120px\" class=\"ipt01\" value=\""+holidayName+"\"/></td>" +
	"<td class=\"listData\"><input id=\"maxDays\" type=\"text\" style=\"width:60px\" class=\"ipt01\" value=\""+maxDays+"\"/></td>" +
	"<td class=\"listData\"><input id=\"expression\" type=\"text\" style=\"width:150px\" class=\"ipt01\" value=\""+expression+"\"/></td>" +
	"<td class=\"listData\"><input id=\"description\" type=\"text\"  maxlength=\"50\" style=\"width:170px\" class=\"ipt01\" value=\""+description+"\"/></td> ";
	if(id == null || id == "") {
		html += "<td class=\"oprateImg\"><a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"saveInfoFlow(this)\">保存</a> | <a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"cancelInsert(this)\">取消</a></td>";
		
	} else {
		html += "<td class=\"oprateImg\">"+"<a href=\"javascript:void(0);\" class=\"opLink\" onclick=\"modifySuccess(this,'"+id+"')\">保存</a>" +"&nbsp;|&nbsp;<a href=\"javascript:void(0);\"  class=\"opLink\" onclick=\"cancelModify(this,'"+id+"','"+holidayName+"','"+maxDays+"','"+expression+"','"+description+"')\">取消</a>" +"</td>";
	}
	html += "</tr>";
	return html;
}

function createHtml(id, holidayName, maxDays, expression, description){
	var html = "";
	html += "<tr id=\""+id+"\" onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
         + "<td>" + holidayName + "</td>" 
         + "<td>" + maxDays + "</td>"
         + "<td>" + expression + "</td>"
         + "<td>" + description + "</td>"; 
         //操作
	var op = ($.hasRight($.SysConstants.MODIFY)?("<a href=\"javascript:void(0);\" onclick=\"modifyInfoLayout(this,'" + id + "');\"  class=\"opLink\">修改</a>&nbsp;|&nbsp;") : "") 
               + ($.hasRight($.SysConstants.DELETE)?("<a href=\"javascript:void(0);\" onclick=\"deleteInfoLayout('" + id + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;") : "");
	html += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"));                   	           
    html += "</td></tr>";
    return html;
}

//修改成功
function modifySuccess(self, id) {
	var holidayName =$.trim($("#holidayName").val());
	//判断
	if (!holidayName) {
		$.alert("假期名称不能为空");
		return;
	}
	var maxDays = $.trim($("#maxDays").val());
	if ($.validNumber("maxDays","单次最长天数",false)){
		return;
	}
	var expression = $.trim($("#expression").val());
	var desc = $.trim($("#description").val());

	var result = $.validChar(desc, "'\"<>");
	if (result) {
		$.alert("说明不能输入非法字符：" + result);
		$("#description").focus();
		return;
	}

	$.ajax({
		type : "POST",
		cache : false,
		url : "m/holi_type",
		data : {
			act : "modify",
			id : id,
			holidayname : holidayName,
			maxdays : maxDays,
			exp : expression,
			desc : desc
		},
		success : function(xml) {
			// 解析XML中的返回代码
			var messageCode = $("message", xml).attr("code");
			if (messageCode == "1") {
				$.alert("修改成功");
				queryList();
			} else {
				$.alert($("message", xml).text());
			}
		},
		error : $.ermpAjaxError
	});
}

// 取消修改
function cancelModify(self,id,holidayName, maxDays, expression,desc){
	$(createHtml(id,holidayName,maxDays,expression,desc)).insertBefore($(self).parent().parent());
	$(self).parent().parent().remove();
	$("#addFlow").removeAttr("disabled","true").removeClass("icoNone");
	edit=false;
}

function deleteInfoLayout(id){
	if (edit) {
		$.alert("请先保存");
		return;
	}
	$.confirm("确认要删除吗?", function(r){
		if (r) {
			$.ajax({
		       type : "POST",
		       cache: false,
		       url  : "m/holi_type",
		       data : {act:"delete",id:id},
		       success : function(xml){
		           var messageCode = $("message",xml).attr("code");
		           if(messageCode == "1"){
		           		$.alert("删除成功!");
		           		queryList();
		           }else{
		           		$.alert($("message",xml).text());
		           }
		       },
		       error : $.ermpAjaxError
		   	});
		}
	});	
}

//解决IE6下按回车转到其它页面
function cancelEnter(obj) {
	obj.keypress(function(e) {
		if (e.keyCode == 13) {
			return false;
		}
	});
}