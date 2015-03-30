$(initParameterSet);
var currentSelectorId = "";
var infoSel;
//当前是否为编辑状态
var edit=false;
function initParameterSet(){
	//添加权限约束
	$.handleRights({
	   "add" : $.SysConstants.ADD,
	   "searchLog" : $.SysConstants.QUERY,
	   "del" : $.SysConstants.DELETE
	});
	
	$("#del").click(function(){
		delLabelByArgs();
	});
	
	$("#add").click(function(){
		addLabel();
	});
	
	$("#searchLog").click(function(){
		gotoPage(1);
	});
	
	$("#reflash").click(function(){
	 	$("#flowSet > tbody").html("");
		queryList();
	});
	
	$.EnterKeyPress($("#labelName"),$("#searchLog"));
	$.EnterKeyPress($("#startCount"),$("#searchLog"));
	$.EnterKeyPress($("#endCount"),$("#searchLog"));
	
	//查询
	gotoPage(1);
	
}

function delLabelByArgs(){
	
	var labelName = $.trim($("#labelName").val());
	var startCount = $.trim($("#startCount").val());
	var endCount = $.trim($("#endCount").val());
	
	var conMsg;
	if(labelName == "" && startCount=="" && endCount==""){
		conMsg = "是否要删除所有标签！";
	} else {
		if (startCount != ""){
			if(isNaN(startCount)){
				$.alert("最小点击数只能为数字!");
				$("#startCount").select();
				return;
			}
			startCount = parseInt(startCount);
		}
		if (endCount != ""){
			if(isNaN(endCount)){
				$.alert("最大点击数只能为数字!");
				$("#endCount").select();
				return;
			}
			endCount = parseInt(endCount);
		}
		if (startCount != "" && endCount != "" && startCount > endCount) {
			$.alert("请选择正确的点击数区间!");
			$("#endCount").select();
			return;
		}
		conMsg = "是否要删除所筛选出的标签！";
	}
	
	$.confirm(conMsg,function(r){
		if (r) {
			$.ajax({
		       type : "POST",
		       cache: false,
		       url  : "m/kb_label",
		       data : {act:"delete",
				       name:labelName,
				       startCount:startCount,
				       endCount:endCount
		       },
		       success : function(xml){
		           var messageCode = $("message",xml).attr("code");
		           if(messageCode == "1"){
		           		$.alert("删除成功!")
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


//-----------------------------新增标签信息-----------------------------
function addLabel(){
	$("#add").attr("disabled","true").addClass("icoNone");
	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}
	var infoFlowList="<tr id=\"infoFlow\">"
					  +"<td><input id=\"addName\" type=\"text\" maxlength=\"100\" class=\"ipt05\"  style=\"width:200px\"/></td>"
	                  +"<td><div id=\"propertySel\" type=\"single\" overflow=\"true\" name=\"property\"><div>0**普通</div></div></td>"
	                  +"<td>0</td>"
	                  +"<td>当前时间</td>"
	                  +"<td><a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"saveLabel(this)\">保存</a> | <a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"cancelInsert(this)\">取消</a></td>"
	                  +"</tr>";
	if($("#flowSet > tbody tr").length==0){
	  $("#flowSet > tbody").append(infoFlowList)
	}else{
	  $(infoFlowList).insertBefore($("#flowSet > tbody tr:eq(0)"));
	}
	cancelEnter($("#addName"));
	//生成流程类别下拉框
	infoSel = $("#propertySel").ySelect({width: 80});
	infoSel.select(0);
	$("#addName").focus();
}

//保存
function saveLabel(self) {
	var labelName = $.trim($("#addName").val());
	if(labelName==""){
		$.alert("标签名称不能为空");
		return;
	}
	var result = $.validChar(labelName,"'\"<>");
	if (result){
		$.alert("标签名称不能输入非法字符：" + result);
		$("#addName").focus();
		return;
	}
	var property = infoSel.getValue();
	//提交到后台
	$.ajax({
	    type : "POST",
		cache: false,
		async : true,
		url  : "m/kb_label",
		data : {act:"add",
				labelName:labelName,
				property:property},
	       success : function(xml){
	     		//解析XML中的返回代码
				var messageCode = $("message",xml).attr("code");
				if(messageCode == "1"){
					$.alert("保存成功!");
					queryList();
				}else {
					$.alert($("message",xml).text());
				}
	       },
	       error : $.ermpAjaxError
	});	
}


//解决IE6下按回车转到其它页面
function cancelEnter(obj) {
	obj.keypress(
	       function(e){
	           if(e.keyCode == 13){
	               return false;
	           }
	       }
	   );
}

function cancelInsert(self) {
	$(self).parent().parent().remove();
	$("#add").removeAttr("disabled","true").removeClass("icoNone");
	edit=false;
}

//取消
function cancelModify(self,id,infoCode,infoName,infoFlow) {
	//var td2 = "<input type=\"hidden\" value=\"" + principal + "\">" + principalName;
	$(createTR(id,infoCode,infoName,infoFlow)).insertBefore($(self).parent().parent());
	$(self).parent().parent().remove();
}

//查询
function queryList(){
	
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	
	var labelName = $.trim($("#labelName").val());
	var startCount = $.trim($("#startCount").val());
	var endCount = $.trim($("#endCount").val());
	
	if (startCount != ""){
		if(isNaN(startCount)){
			$.alert("最小点击数只能为数字!");
			$("#startCount").select();
			return;
		}
		startCount = parseInt(startCount);
	}
	if (endCount != ""){
		if(isNaN(endCount)){
			$.alert("最大点击数只能为数字!");
			$("#endCount").select();
			return;
		}
		endCount = parseInt(endCount);
	}
	if (startCount != "" && endCount != "" && startCount > endCount) {
		$.alert("请选择正确的点击数区间!");
		$("#endCount").select();
		return;
	}
	//提交到后台
	$.ajax({
	    type : "POST",
		cache: false,
		url  : "m/kb_label",
		data : {
			act:"query",
			name:labelName,
			startCount:startCount,
			endCount:endCount,
			pageno:pageno,
			pagecount:pagecount
		},
       success : function(xml){
			var messageCode = $("message",xml).attr("code");
			if(messageCode == "1"){
				var bodyHTML = "";
				$("labelLib",xml).each(
                    function(index){
                        var labelLib = $(this);
                        bodyHTML += createTR(labelLib.attr("id"), $("name",labelLib).text(), $("property",labelLib).text(), $("count",labelLib).text(),$("date",labelLib).text());
                });
                $("#flowSet > tbody").html(bodyHTML);
                $(".pageNext").html($.createTurnPage(xml));
	            $.EnterKeyPress($("#numPage"),$("#numPage").next());
                $("#add").removeAttr("disabled","true").removeClass("icoNone");
                enWrap();
				edit=false;
			}else{
           		$.alert($("message",xml).text());
       		}
       },
       error : $.ermpAjaxError
	});	
}

//创建行
function createTR(id,name,property,count,createDate){
	var html = "";
	html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
         + "<td>" + name + "</td>" 
         + "<td>" + property + "</td>"
         + "<td>" + count + "</td>"
         + "<td>" + createDate + "</td>";
         //操作
	var op = ($.hasRight($.SysConstants.MODIFY)?("<a href=\"javascript:void(0);\" onclick=\"modifyLabel(this,'" + id + "');\"  class=\"opLink\">修改</a>&nbsp;|&nbsp;") : "") 
               + ($.hasRight($.SysConstants.DELETE)?("<a href=\"javascript:void(0);\" onclick=\"deleteLabel('" + id + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;") : "");
    html += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"));                   	           
    html += "</td></tr>";
    return html;
}

//删除
function deleteLabel(id){
	if (edit) {
		$.alert("请先保存");
		return;
	}
	$.confirm("确认要删除此标签吗？",function(r){
		if (r) {
			$.ajax({
		       type : "POST",
		       cache: false,
		       url  : "m/kb_label",
		       data : {act:"delete",id:id},
		       success : function(xml){
		           var messageCode = $("message",xml).attr("code");
		           if(messageCode == "1"){
		           		$.alert("删除成功!")
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
//修改
function modifyLabel(self,id){
	$("#add").attr("disabled","true").addClass("icoNone");
	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}

	var selfObj=$(self).parent().parent().find("td");
	var infoName = selfObj.eq(0).text();
	var flowClass = selfObj.eq(1).text();
	var newOperate="<a href=\"javascript:void(0);\" onclick=\"saveModLabel('" + id + "')\" class=\"opLink\">保存</a>&nbsp;|&nbsp"
	              +"<a href=\"javascript:void(0);\" onclick=\"cancelModify(this,'" + id + "','" + infoName + "','" + flowClass + "','" + selfObj.eq(2).text() + "','" + selfObj.eq(3).text() + "')\" class=\"opLink\">取消</a>";
	
	selfObj.eq(0).html("<input id='addName' maxlength='100' type='text' style='width:200px' class='ipt01' value=\""+infoName+"\">");
	selfObj.eq(1).html("<div id=\"propertySel\" type=\"single\" overflow=\"true\" name=\"propertySel\"><div>0**普通</div></div>");
	flowSel = $("#propertySel").ySelect({width: 80});
	flowSel.selectByName(flowClass);
	selfObj.eq(4).html(newOperate);
	cancelEnter($("#addName"));
}


//修改成功
function saveModLabel(id){
	var labelName = $.trim($("#addName").val());
	if(labelName==""){
		$.alert("标签名称不能为空");
		return;
	}
	var result = $.validChar(labelName,"'\"<>");
	if (result){
		$.alert("标签名称不能输入非法字符：" + result);
		$("#addName").focus();
		return;
	}
	var poperty = flowSel.getValue();
	
	$.ajax({
	  type : "POST",
	  cache: false,
	  url  : "m/kb_label",
	  data : {act:"modify",
	 			id:id,
	  			labelName:labelName,
				property:poperty},
	     success : function(xml){
		      //解析XML中的返回代码
		      var messageCode = $("message",xml).attr("code");
		      if(messageCode == "1"){
		         $.alert("标签信息修改成功");
		         queryList();
		        }else {
			        $.alert( $("message",xml).text());
			    }
	       },
	     error : $.ermpAjaxError
	});
}
//取消修改
function cancelModify(self,id,name,property,count,createTime){
	$(createTR(id,name,property,count,createTime)).insertBefore($(self).parent().parent());
	$(self).parent().parent().remove();
	$("#add").removeAttr("disabled","true").removeClass("icoNone");
	edit=false;
}

//转向第几页
function gotoPage(pageNo, totalPage){
    if(isNaN(pageNo)){
        $.alert("页索引只能为数字！");
        return;
    } else if(pageNo < 1){
        pageNo = 1;
    }
    if (totalPage && !isNaN(totalPage) && pageNo > totalPage) {
    	pageNo = totalPage;
    }
    $("#hidNumPage").val(parseInt(pageNo));
    queryList();
};

//折行
function enWrap(){
	var D=document; F(D.body); function F(n){var u,r,c,x; if(n.nodeType==3){ u=n.data.search(/\S{10}/); if(u>=0) { r=n.splitText(u+10); n.parentNode.insertBefore(D.createElement("WBR"),r); } }else if(n.tagName!="STYLE" && n.tagName!="SCRIPT"){for (c=0;x=n.childNodes[c];++c){F(x);}} } 
}