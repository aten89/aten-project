var mainFrame = $.getMainFrame();

$(initPage);
var flowSel;
var fileSel;
//var isAssingSel;
	//生成文件类别下拉框
	fileSel = $("#fileClassSel").ySelect({width: 70});
//当前是否为编辑状态
var edit=false;
function initPage(){
	
	//添加权限约束
	$.handleRights({
	   "btnSort" : $.SysConstants.ORDER,
	   "addDocclassFlow" : $.SysConstants.ADD
	});
	
	
	$("#addDocclassFlow").click(function(){
		addDocclassFlow();
	});
	$("#reflash").click(function(){
	 	$("#docclassTab > tbody").html("");
		queryList();
		$("#addDocclassFlow").removeAttr("disabled","true").removeClass("icoNone");
		edit=false;
	})
	//查询
	queryList();
	
	/*排序*/
	$("#btnSort").click(
		function(){
			mainFrame.addTab({
				id:"oa_DocclassLayout_sort",
				title:"公文配置排序",
				url:BASE_PATH +"/m/doc_class?act=initsort",
				callback:queryList
			});
		}
	);
};


//查询
function queryList(){
	
	//提交到后台
	$.ajax({
	    type : "POST",
		cache: false,
		url  : "m/doc_class",
		data : {act:"query"},
	       success : function(xml){
	     		//解析XML中的返回代码
				var messageCode = $("message",xml).attr("code");
				if(messageCode == "1"){
					var bodyHTML = "";
					$("doc-class",xml).each(
	                    function(index){
	                        var docclassLayout = $(this);
	                        bodyHTML += createTR(docclassLayout.attr("id"), $("name",docclassLayout).text(), $("file-class",docclassLayout).text(),$("flow-class-name",docclassLayout).text(),$("description",docclassLayout).text(), $("has-template",docclassLayout).text());
	                });
	                $("#docclassTab > tbody").html(bodyHTML);
	                $("#addDocclassFlow").removeAttr("disabled","true").removeClass("icoNone");
					edit=false;
				}else{
               		$.alert($("message",xml).text());
           		};
	       },
	       error : $.ermpAjaxError
	});	
}

//创建行
function createTR(id,name,fileClass,flowClass,desc, hasTem){
	var html = "";
	html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
         + "<td>" + name + "</td>" 
         + "<td>" + flowClass + "</td>"
         + "<td>" + fileClass + "</td>"
//         + "<td>" + isAssign + "</td>"
         + "<td>" + desc + "</td>"
         + "<td>" + hasTem + "</td>"; 
         //操作
	var op = ($.hasRight($.SysConstants.MODIFY)?("<a href=\"javascript:void(0);\" onclick=\"editTemplate(this,'" + id + "');\"  class=\"opLink\">编辑模板</a>&nbsp;|&nbsp;") : "")
			   + ($.hasRight($.SysConstants.MODIFY)?("<a href=\"javascript:void(0);\" onclick=\"modifyDocclass(this,'" + id + "');\"  class=\"opLink\">修改</a>&nbsp;|&nbsp;") : "") 
               + ($.hasRight($.SysConstants.DELETE)?("<a href=\"javascript:void(0);\" onclick=\"deleteDocclass('" + id + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;") : "")
               + (($.hasRight($.SysConstants.BIND_USER) || $.hasRight($.SysConstants.BIND_GROUP) || $.hasRight($.SysConstants.BIND_POST))?("<a href=\"javascript:void(0);\" onclick=\"impowerDraft('" + id + "','"+name+"')\"  class=\"opLink\">起草授权</a>&nbsp;|&nbsp;") : "");
    html += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"));   
    html += "</td></tr>";
    return html;
}

//-----------------------------新增公文流程-----------------------------
function addDocclassFlow(){
	$("#addDocclassFlow").attr("disabled","true").addClass("icoNone");
	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}
	var docclassFlowList="<tr id=\"docclassFlow\">"
					  +"<td><input id=\"docclassName\" name=\"docclassName\" type=\"text\" maxlength=\"50\" class=\"ipt01\"  style=\"width:90px\"/></td>"
	                  +"<td><div id=\"flowClassSel\" type=\"single\" overflow=\"true\" name=\"flowClass\"></div></td>"
	                  +"<td><div id=\"fileClassSel\" type=\"single\" overflow=\"true\" name=\"fileClass\"><div>1**普通文件</div><div>0**公文</div></div></td>"
//	                  +"<td><div id=\"isAssignSel\" type=\"single\" overflow=\"true\" name=\"isAssign\"><div>0**无需指定审批</div><div>1**必选指定审批</div><div>2**可选指定审批</div></div></td>"
	                  +"<td style=\"vertical-align:top\"><input id=\"desc\" name=\"docclassDesc\" type=\"text\" maxlength=\"50\" class=\"ipt01\"  style=\"width:115px\"/></td>"
	                  +"<td style=\"vertical-align:top\">无</td>"
	                  +"<td style=\"vertical-align:top\"><a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"saveDocclass()\">保存</a> | <a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"cancelInsert()\">取消</a></td>"
	                  +"</tr>";
	if($("#docclassTab > tbody tr").length==0){
	  $("#docclassTab > tbody").append(docclassFlowList)
	}else{
	  $(docclassFlowList).insertBefore($("#docclassTab > tbody tr:eq(0)"));
	}
	cancelEnter($("#desc"));
	//生成流程类别下拉框
	flowSel = $("#flowClassSel").ySelect({width: 55,url : "m/flow_draft?act=flow_class",afterLoad:flowSelAddOption});
	//生成文件类别下拉框
	fileSel = $("#fileClassSel").ySelect({width: 55});
//	//生指定审批
//	isAssingSel = $("#isAssignSel").ySelect({width: 55});
	
	function flowSelAddOption() {
		flowSel.addOption("", "请选择...", 0);
		flowSel.select(0);
	}
    $("#docclassCode").focus();
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

function cancelInsert() {
	$("#docclassFlow").remove();
	$("#addDocclassFlow").removeAttr("disabled","true").removeClass("icoNone");
	edit=false;
}

//保存
function saveDocclass(){
	var docclassName = $.trim($("#docclassName").val());
	if(docclassName == ""){
		$.alert("请选择模版名称");
		return;
	}
	if(fileSel.getValue() == ""){
		$.alert("请选择文件类别");
		return;
	}
//	if(isAssingSel.getValue() == ""){
//		$.alert("请选择可否指定");
//		return;
//	}
	if(fileSel.getValue() == "1" && flowSel.getValue() == ""){
		$.alert("普通文件的流程类别不能为空！");
		return;
	}
	
	var flowClass = flowSel.getValue();
	var fileClass = fileSel.getValue();
	var desc = $.trim($("#desc").val());
//	var isAssign = isAssingSel.getValue();
	var result = $.validChar(desc,"'\"<>");
	if (result){
		$.alert("说明不能输入非法字符：" + result);
		$("#desc" + docclassNum).focus();
		return;
	}

	//提交到后台
	$.ajax({
	    type : "POST",
		cache: false,
		async : true,
		url  : "m/doc_class",
		data : {act:"add",
				docclassname:docclassName,
				fileClass:fileClass,
				flowclass:flowClass,
//				isAssign:isAssign,
				desc:desc},
	       success : function(xml){
	     		//解析XML中的返回代码
				var messageCode = $("message",xml).attr("code");
				if(messageCode == "1"){
					queryList();
					$("#addDocclassFlow").removeAttr("disabled","true").removeClass("icoNone");
				}else {
					$.alert($("message",xml).text());
				}
				edit = false;
	       },
	       error : $.ermpAjaxError
	});	
}

//编辑模板
function editTemplate(self,id){
	mainFrame.addTab({
		id:"oa_doc_temp_edit" + id,
		title:"公文模板编辑",
		url:BASE_PATH + "m/doc_class?act=edittemp&id=" + id,
		callback:queryList
	});
}

//删除
function deleteDocclass(id){
	
	if (edit) {
		$.alert("请先保存");
		return;
	}
	$.confirm("确认要删除吗?", function(r){
		if (r) {
			$.ajax({
		       type : "POST",
		       cache: false,
		       url  : "m/doc_class",
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
function modifyDocclass(self,id){
	$("#addDocclassFlow").attr("disabled","true").addClass("icoNone");

	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}

	var selfObj=$(self).parent().parent().find("td");
	var docclassName = selfObj.eq(0).text();
	var flowClass = selfObj.eq(1).text();
	var fileClass = selfObj.eq(2).text();
//	var isAssign = selfObj.eq(3).text();
	var desc = selfObj.eq(3).text();
	var flag = selfObj.eq(4).text();
	var newOperate="<a href=\"javascript:void(0);\" onclick=\"modifySuccess(this,'" + id + "')\" class=\"opLink\">保存</a>&nbsp;|&nbsp"
	              +"<a href=\"javascript:void(0);\" onclick=\"cancelModify(this,'" + id + "','" + docclassName + "','" + flowClass + "','" + fileClass + "','" + desc + "','" + flag + "')\" class=\"opLink\">取消</a>";

	selfObj.eq(0).html("<input id='docclassName' maxlength='50' type='text' style='width:90px' class='ipt01' value=\""+docclassName+"\">");

	selfObj.eq(1).html("<div id=\"flowClassSel\" type=\"single\" overflow=\"true\" name=\"flowClass\"></div>");
	flowSel = $("#flowClassSel").ySelect({width: 55,url : "m/flow_draft?act=flow_class",afterLoad:selectFlowClassOption});
	//加载后选中
	function selectFlowClassOption(){
		flowSel.addOption("", "请选择...", 0);
		flowSel.selectByName(flowClass);
	}
	selfObj.eq(2).html("<div id=\"fileClassSel\" type=\"single\" overflow=\"true\" name=\"fileClass\"><div>1**普通文件</div><div>0**公文</div></div>");
	fileSel = $("#fileClassSel").ySelect({width: 55});
	fileSel.selectByName(fileClass);
	
//	selfObj.eq(3).html("<div id=\"isAssignSel\" type=\"single\" overflow=\"true\" name=\"isAssign\"><div>0**无需指定审批</div><div>1**必选指定审批</div><div>2**可选指定审批</div></div>");
//	isAssingSel = $("#isAssignSel").ySelect({width: 55});
//	isAssingSel.selectByName(isAssign);
	
	selfObj.eq(3).html("<input id='modifyDesc' maxlength='50' type='text' style='width:115px' class='ipt01' value=\""+desc+"\">");
	selfObj.eq(4).html("无");
	selfObj.eq(5).html(newOperate);
	cancelEnter($("#modifyDesc"));
}
//修改成功
function modifySuccess(self,id){
	var selfObj=$(self).parent().parent().find("td");
	var docclassName = $.trim($("#docclassName").val());
	var flowClass = flowSel.getValue();
	var fileClass = fileSel.getValue();
//	var isAssign = isAssingSel.getValue();
	var desc = $.trim($("#modifyDesc").val());
	var result = $.validChar(desc,"'\"<>");
	if (result){
		$.alert("说明不能输入非法字符：" + result);
		selfObj.eq(2).find("input").focus();
		return;
	}
	
	$.ajax({
	  type : "POST",
	  cache: false,
	  url  : "m/doc_class",
	  data : {act:"modify",
	 			id:id,
	  			docclassname:docclassName,
				flowclass:flowClass,
				fileClass:fileClass,
//				isAssign:isAssign,
				desc:desc},
	     success : function(xml){
		      //解析XML中的返回代码
		      var messageCode = $("message",xml).attr("code");
		      if(messageCode == "1"){
		         $.alert("修改成功");
		         queryList();
		        }else {
			        $.alert( $("message",xml).text());
			    }
	       },
	     error : $.ermpAjaxError
	});
}

//取消修改
function cancelModify(self,id,name,flow,file,desc,hasTem){
	$(createTR(id,name,file,flow,desc,hasTem)).insertBefore($(self).parent().parent());
	$(self).parent().parent().remove();
	$("#addDocclassFlow").removeAttr("disabled","true").removeClass("icoNone");
					edit=false;
}
function impowerDraft(id,name){
	mainFrame.addTab({
		id:"oa_impower_draft_doc" + id,
		title:"起草授权",
		url:BASE_PATH+"m/doc_class?act=init_assign&id=" + id +"&title="+encodeURI(name+"的起草授权")
	});
}
