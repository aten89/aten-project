var mainFrame = $.getMainFrame();

$(initPage);
//当前是否为编辑状态
var edit=false;
function initPage(){

	//添加权限约束
	$.handleRights({
	   "addDocNo" : $.SysConstants.ADD
	});
		
	$("#addDocNo").click(function(){
		addDocNo();
	});
	$("#reflash").click(function(){
	 	$("#docNoTab > tbody").html("");
		queryList();
		$("#addDocNo").removeAttr("disabled","true").removeClass("icoNone");
		edit=false;
	})
	//查询
	queryList();
	
};

//查询
function queryList(){
	
	//提交到后台
	$.ajax({
	    type : "POST",
		cache: false,
		url  : "m/doc_no",
		data : {act:"query"},
	       success : function(xml){
	     		//解析XML中的返回代码
				var messageCode = $("message",xml).attr("code");
				if(messageCode == "1"){
					var bodyHTML = "";
					$("doc-no",xml).each(
	                    function(index){
	                        var docnoLayout = $(this);
	                        bodyHTML += createTR(docnoLayout.attr("id"), $("docWord",docnoLayout).text(), $("yearPrefix",docnoLayout).text(),
	                        					 $("currentYear",docnoLayout).text(), $("yearPostfix",docnoLayout).text(),
	                        					 $("orderPrefix",docnoLayout).text(), $("orderNumber",docnoLayout).text(),
	                        					 $("orderPostfix",docnoLayout).text(), $("description",docnoLayout).text(),
	                        					 $("has-template",docnoLayout).text());
	                });
	                $("#docNoTab > tbody").html(bodyHTML);
	                $("#addDocNo").removeAttr("disabled","true").removeClass("icoNone");
					edit=false;
				}else{
               		$.alert($("message",xml).text());
           		};
	       },
	       error : $.ermpAjaxError
	});	
}

//创建行
function createTR(id,docWord,yearPrefix,currentYear,yearPostfix,orderPrefix,orderNumber,orderPostfix,preview,hasTem){
	var html = "";
	html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
         + "<td>" + docWord + "</td>" 
         + "<td>" + yearPrefix + "</td>" 
         + "<td>" + currentYear + "</td>" 
         + "<td>" + yearPostfix + "</td>" 
         + "<td>" + orderPrefix + "</td>" 
         + "<td>" + orderNumber + "</td>"
         + "<td>" + orderPostfix + "</td>"
         + "<td>" + preview + "</td>"
         + "<td>" + hasTem + "</td>"; 
         //操作
	var op = ($.hasRight($.SysConstants.MODIFY)?("<a href=\"javascript:void(0);\" onclick=\"editTemplate(this,'" + id + "');\"  class=\"opLink\">编辑模板</a>&nbsp;|&nbsp;") : "")
	           + ($.hasRight($.SysConstants.MODIFY)?("<a href=\"javascript:void(0);\" onclick=\"modifyDocno(this,'" + id + "');\"  class=\"opLink\">修改</a>&nbsp;|&nbsp;") : "") 
               + ($.hasRight($.SysConstants.DELETE)?("<a href=\"javascript:void(0);\" onclick=\"deleteDocno('" + id + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;") : "");
    html += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"));   
    html += "</td></tr>";
    return html;
}

//-----------------------------新增公文编号流程-----------------------------
function addDocNo(){
	$("#addDocNo").attr("disabled","true").addClass("icoNone");
	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}
	
	var docNoList="<tr id=\"addDocNoTr\">"
		    +"<td><input id=\"docWord\" name=\"docWord\" type=\"text\" maxlength=\"10\" class=\"ipt05\" style=\"width:60px\"/></td>" 
		    +"<td><input id=\"yearPrefix\" name=\"yearPrefix\" type=\"text\" maxlength=\"4\" class=\"ipt05\" style=\"width:25px\"/></td>"
		    +"<td></td>"
		    +"<td><input id=\"yearPostfix\" name=\"yearPostfix\" type=\"text\" maxlength=\"4\" class=\"ipt05\" style=\"width:25px\"/></td>"
		    +"<td><input id=\"orderPrefix\" name=\"orderPrefix\" type=\"text\" maxlength=\"4\" class=\"ipt05\" style=\"width:25px\"/></td>"
		    +"<td></td>"
		    +"<td><input id=\"orderPostfix\" name=\"orderPostfix\" type=\"text\" maxlength=\"4\" class=\"ipt05\" style=\"width:25px\"/></td>"
		    +"<td><input id=\"preview\" name=\"preview\" type=\"hidden\" maxlength=\"50\" class=\"ipt05\"></td>"
		    +"<td>无</td>"
		    +"<td><a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"saveDocNo()\">保存</a> | <a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"cancelInsert()\">取消</a></td>"
	        +"</tr>";
	if($("#docNoTab > tbody tr").length==0){
		$("#docNoTab > tbody").append(docNoList);
	}else{
	  	$(docNoList).insertBefore($("#docNoTab > tbody tr:eq(0)"));
	}
	cancelEnter($("#docWord"));
	cancelEnter($("#yearPrefix"));
	cancelEnter($("#yearPostfix"));
	cancelEnter($("#orderPrefix"));
	cancelEnter($("#orderPostfix"));
	cancelEnter($("#preview"));
    $("#docNoCode").focus();
}

//解决IE6下按回车转到其它页面
function cancelEnter(obj) {
	obj.keypress(function(e){
		if(e.keyCode == 13){
		    return false;
		}
	});
}

function cancelInsert() {
	$("#addDocNoTr").remove();
	$("#addDocNo").removeAttr("disabled","true").removeClass("icoNone");
	edit=false;
}

function checkParam(docWord, yearPrefix, yearPostfix, orderPrefix, orderPostfix) {
	//验证输入数据
	if(docWord=="") {
		$.alert("文件字不能为空");
		$("#docWord").focus();
		return false;
	}
	var result = $.validChar(docWord,"'\"<>");
	if (result){
		$.alert("文件字不能输入非法字符：" + result);
		$("#docWord").focus();
		return false;
	}
	
	if(yearPrefix=="") {
		$.alert("年份前缀不能为空");
		$("#yearPrefix").focus();
		return false;
	}
	result = $.validChar(yearPrefix,"'\"<>");
	if (result){
		$.alert("年份前缀不能输入非法字符：" + result);
		$("#yearPrefix").focus();
		return false;
	}
	
	if(yearPostfix=="") {
		$.alert("年份后缀不能为空");
		$("#yearPostfix").focus();
		return false;
	}
	result = $.validChar(yearPostfix,"'\"<>");
	if (result){
		$.alert("年份后缀不能输入非法字符：" + result);
		$("#yearPostfix").focus();
		return false;
	}
	
	if(orderPrefix=="") {
		$.alert("编号前缀不能为空");
		$("#orderPrefix").focus();
		return false;
	}
	result = $.validChar(orderPrefix,"'\"<>");
	if (result){
		$.alert("编号前缀不能输入非法字符：" + result);
		$("#orderPrefix").focus();
		return false;
	}
	
	if(orderPostfix=="") {
		$.alert("编号后缀不能为空");
		$("#orderPostfix").focus();
		return false;
	}
	result = $.validChar(orderPostfix,"'\"<>");
	if (result){
		$.alert("编号后缀不能输入非法字符：" + result);
		$("#orderPostfix").focus();
		return false;
	}
	return true;
}

//保存
function saveDocNo(){
	var docWord = $.trim($("#docWord").val());
	var yearPrefix = $.trim($("#yearPrefix").val());
	var yearPostfix = $.trim($("#yearPostfix").val());
	var orderPrefix = $.trim($("#orderPrefix").val());
	var orderPostfix = $.trim($("#orderPostfix").val());
	if (!checkParam(docWord, yearPrefix, yearPostfix, orderPrefix, orderPostfix)) {
		return;
	}
	//提交到后台
	$.ajax({
	    type : "POST",
		cache: false,
		async : true,
		url  : "m/doc_no",
		data : {act:"add",
				docWord:docWord,
				yearPrefix:yearPrefix,
				yearPostfix:yearPostfix,
				orderPrefix:orderPrefix,
				orderPostfix:orderPostfix},
	       success : function(xml){
	     		//解析XML中的返回代码
				var messageCode = $("message",xml).attr("code");
				if(messageCode == "1"){
					queryList();
					$("#addDocNo").removeAttr("disabled","true").removeClass("icoNone");
				}else {
					$.alert($("message",xml).text());
				}
				edit = false;
	       },
	       error : $.ermpAjaxError
	});	
//	$("#preview" + docNoNum).val(preview);
}

//编辑模板
function editTemplate(self,id){
	mainFrame.addTab({
		id:"oa_docno_temp_edit" + id,
		title:"红头模板编辑",
		url:BASE_PATH + "m/doc_no?act=edittemp&id=" + id,
		callback:queryList
	});
}

//删除
function deleteDocno(id){
	
	if (edit) {
		$.alert("请先保存");
		return;
	}
	$.confirm("确认要删除吗?", function(r){
		if (r) {
			$.ajax({
		       type : "POST",
		       cache: false,
		       url  : "m/doc_no",
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
function modifyDocno(self,id){
	
	$("#addDocNo").attr("disabled","true").addClass("icoNone");

	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}
	var selfObj=$(self).parent().parent().find("td");
	
	var docWord = selfObj.eq(0).text();
	var yearPrefix = selfObj.eq(1).text();
	var year = selfObj.eq(2).text();
	var yearPostfix = selfObj.eq(3).text();
	var orderPrefix = selfObj.eq(4).text();
	var order = selfObj.eq(5).text();
	var orderPostfix = selfObj.eq(6).text();
	var preview = selfObj.eq(7).text();
	var flag = selfObj.eq(8).text();
	
	var newOperate="<a href=\"javascript:void(0);\" onclick=\"modifySuccess(this,'" + id + "')\" class=\"opLink\">保存</a>&nbsp;|&nbsp"
	              +"<a href=\"javascript:void(0);\" onclick=\"cancelModify(this,'" + id + "','" 
	              + docWord + "','" + yearPrefix + "','" + year + "','"
	              + yearPostfix + "','" + orderPrefix + "','" + order + "','"
	              + orderPostfix + "','" + preview + "','" + flag + "')\" class=\"opLink\">取消</a>";

	selfObj.eq(0).html("<input id='docWord' type=\"text\" maxlength=\"10\" class=\"ipt05\" style=\"width:60px\" value=\""+docWord+"\">");
	selfObj.eq(1).html("<input id='yearPrefix' type=\"text\" maxlength=\"4\" class=\"ipt05\" style=\"width:25px\" value=\""+yearPrefix+"\">");
	selfObj.eq(3).html("<input id='yearPostfix' type=\"text\" maxlength=\"4\" class=\"ipt05\" style=\"width:25px\" value=\""+yearPostfix+"\">");
	selfObj.eq(4).html("<input id='orderPrefix' type=\"text\" maxlength=\"4\" class=\"ipt05\" style=\"width:25px\" value=\""+orderPrefix+"\">");
	selfObj.eq(6).html("<input id='orderPostfix' type=\"text\" maxlength=\"4\" class=\"ipt05\" style=\"width:25px\" value=\""+orderPostfix+"\">");
	selfObj.eq(9).html(newOperate);
}


//修改成功
function modifySuccess(self,id){
	var selfObj=$(self).parent().parent().find("td");
	
	var docWord = $.trim($("#docWord").val());
	var yearPrefix = $.trim($("#yearPrefix").val());
	var yearPostfix = $.trim($("#yearPostfix").val());
	var orderPrefix = $.trim($("#orderPrefix").val());
	var orderPostfix = $.trim($("#orderPostfix").val());
	if (!checkParam(docWord, yearPrefix, yearPostfix, orderPrefix, orderPostfix)) {
		return;
	}
	var currentYear = $.trim(selfObj.eq(2).find("input").val());
	var orderNumber = $.trim(selfObj.eq(5).find("input").val());

	$.ajax({
	  type : "POST",
	  cache: false,
	  url  : "m/doc_no",
	  data : {act:"modify",
	 			id:id,
	  			docWord:docWord,
				yearPrefix:yearPrefix,
				currentYear:currentYear,
				yearPostfix:yearPostfix,
				orderPrefix:orderPrefix,
				orderNumber:orderNumber,
				orderPostfix:orderPostfix},
	     success : function(xml){
		      //解析XML中的返回代码
		      var messageCode = $("message",xml).attr("code");
		      if(messageCode == "1"){
		         $.alert("公文编号修改成功");
		         queryList();
		        }else {
			        $.alert( $("message",xml).text());
			    }
	       },
	     error : $.ermpAjaxError
	});
}

//取消修改
function cancelModify(self,id,docWord,yearPrefix,currentYear,yearPostfix,orderPrefix,orderNumber,orderPostfix,preview,hasTem){
	$(createTR(id,docWord,yearPrefix,currentYear,yearPostfix,orderPrefix,orderNumber,orderPostfix,preview,hasTem)).insertBefore($(self).parent().parent());
	$(self).parent().parent().remove();
	$("#addDocNo").removeAttr("disabled","true").removeClass("icoNone");
	edit=false;
}
