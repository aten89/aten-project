/**********************
参数设置--设备属性配置项--列表
*********************/ 
var edit=false;
$(initDeviceConfig);
function initDeviceConfig(){
	  //添加权限约束
    $.handleRights({
   		"DeviceCon_add" : $.SysConstants.ADD
	});
	//新增
	$("#DeviceCon_add").click(function(){
		var edit=false;
	    addDeviceOption();
	});
	gotoPage(1);
	//刷新
	$("#DeviceCon_flash").click(function(){
		$("#DeviceCon_add").removeAttr("disabled","true").removeClass("icoNone");
	    gotoPage(1);
	    var edit=false;
	});
};

/**
 * 转到第几页
 * @param {Number} pageNo 页码
 * @param {Number} totalPage 总页数
 */
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
}

//查询
function queryList(){
	$("#optionTab > tbody").empty();
	 //分页
  	var pageno=$.trim($("#hidNumPage").val());
  	var pagecount=$.trim($("#hidPageCount").val());
	//提交到后台
	$.ajax({
	    type : "POST",
		cache: false,
		url  : "m/dev_prop",
		data : {
				act:"query",
				pageNo:pageno,
				pageSize:pagecount
			},
	       success : function(xml){
	     		//解析XML中的返回代码
				var messageCode = $("message",xml).attr("code");
				if(messageCode == "1"){
					var bodyHTML = "";
					$("device_property",xml).each(
	                    function(index){
	                        var option = $(this);
	                        bodyHTML += createTR(option.attr("id"),$("property-name",option).text(), $("property-remark",option).text());
	                });
	                edit=false;
	                $("#optionTab > tbody").html(bodyHTML);
					 $("optionTab > tbody").html(bodyHTML).find("td:empty").html("&nbsp;");
	                //------------------翻页数据--------------------------
		            $(".pageNext").html($.createTurnPage(xml));
		            $.EnterKeyPress($("#numPage"),$("#numPage").next());
				}else{
               		$.alert($("message",xml).text());
           		};
	       },
	       error : $.ermpAjaxError
	});	
}
//新增
function addDeviceOption(){
	$("#DeviceCon_add").attr("disabled","true").addClass("icoNone");
	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}
    var chargeList="<tr>"
					+"<td><input id=\"sbpzx\" type=\"text\" maxlength=\"50\" class=\"ipt05\"/><input type=\"hidden\" maxlength=\"50\" class=\"ipt05\"/></td>"
	               	+"<td><textarea name=\"bz\" class=\"area01\" style=\"width:400px;height:15px\"></textarea></td>"
	               	+"<td><a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"saveDeviceOption(this)\">确定</a> | <a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"cancelSave(this)\">取消</a></td>"
					+"</tr>";
	
	if($("#optionTab > tbody tr").length==0){
	  $("#optionTab > tbody").append(chargeList)
	}else{
	  $(chargeList).insertBefore($("#optionTab > tbody tr:eq(0)"));
	}
	
}
//取消新增
function cancelSave(self){
	edit = false;
	$(self).parent().parent().remove();
	$("#DeviceCon_add").removeAttr("disabled","true").removeClass("icoNone");
}

//修改
function modifyDeviceOption(self,id){
	$("#DeviceCon_add").attr("disabled","true").addClass("icoNone");
	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}
	var selfObj=$(self).parent().parent().find("td");
	var name = selfObj.eq(0).text();
	var remark = selfObj.eq(1).text();
//	var chargeUser = $("input",selfObj.eq(1)).eq(0).val();
	var newOperate="<a href=\"javascript:void(0);\" onclick=\"saveDeviceOption(this)\" class=\"opLink\">确定</a>&nbsp;|&nbsp"
	              +"<a href=\"javascript:void(0);\" onclick=\"cancelModify(this,'" + id + "','" + name + "','"  + remark + "')\" class=\"opLink\">取消</a>";
	selfObj.eq(0).html("<input id=\"sbpzx\" type=\"text\" maxlength=\"50\" class=\"ipt05\" value=\""+name+"\"/><input type=\"hidden\" id=\"optionId\" value=\""+id+"\"/>");
	selfObj.eq(1).html("<textarea id=\"bz\" name=\"bz\" class=\"area01\" style=\"width:400px;height:15px\">"+remark+"</textarea>");
	selfObj.eq(2).html(newOperate);
}

//取消修改
function cancelModify(self,id,name,remark){
	$(createTR(id,name,remark)).insertBefore($(self).parent().parent());
	$(self).parent().parent().remove();
	$("#DeviceCon_add").removeAttr("disabled","true").removeClass("icoNone");
	edit=false;
}
//创建行
function createTR(id,name,remark){
	var html = "";
	html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
		+ "<td>"+name+"</td>" 
		+ "<td>" + remark + "</td>" 
	var op =  ($.hasRight($.SysConstants.MODIFY)?("<a href=\"javascript:void(0);\" onclick=\"modifyDeviceOption(this,'" + id + "');\" class=\"opLink\">修改</a>&nbsp;|&nbsp;") : "")
               + ($.hasRight($.SysConstants.DELETE)?("<a href=\"javascript:void(0);\" onclick=\"deleteDeviceOption('" + id + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;") : "")
    html += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"));   
    html += "</td></tr>";
    return html;
}

//保存
function saveDeviceOption(self){
	var selfObj=$(self).parent().parent().find("td");
	var id = $("input",selfObj.eq(0)).eq(1).val();
	var name = $.trim($("input",selfObj.eq(0)).eq(0).val());
	var remark = $.trim($("textarea",selfObj.eq(1)).eq(0).val());
	var result = $.validChar(name);
	if (name.length <=0) {
		$.alert("设备属性名称不能为空");
		$("#sbpzx").focus();
		return;
	}
	if (result) {
		$.alert("设备属性名称不能包含非法字符：" + result);
		$("#sbpzx").focus();
		return;
	}
	if (name.length >50) {
		$.alert("设备属性名称长度不能大于50个字符");
		$("#sbpzx").focus();
		return;
	}
	result = $.validChar(remark);
	if (result) {
		$.alert("备注不能包含非法字符：" + result);
		$("#bz").focus();
		return;
	}
	if (remark.length >300) {
		$.alert("备注长度不能大于300个字符");
		$("#bz").focus();
		return;
	}
	if(id==""){
		act="add"
	}else{
		act="modify"
	}
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/dev_prop",  //
		data : {
			act: act,
			id:id,
			name:name,
			remark:remark
		},
        success : function(xml){
            var message = $("message",xml);
            if(message.attr("code") == "1"){
            	$.alert("保存成功");
            	edit=false;
            	$("#DeviceCon_add").removeAttr("disabled","true").removeClass("icoNone");
				queryList();
            } 
            else{
            	$.alert($("message",xml).text());
            }
        },
        error : $.ermpAjaxError
    });  
}
//删除
function deleteDeviceOption(id){
	if (edit) {
		$.alert("请先保存");
		return;
	}
	if(""==id || null==id){
		$.alert("参数不能为空");
		return;
	}
	
	$.confirm("确认要删除该设备属性吗？",function(r){
		if (r) {
			$.ajax({
		        type : "POST",
				cache: false,
				url  : "m/dev_prop",  //
				data : {
					act: "delete",
					id:id
				},
		        success : function(xml){
		            var message = $("message",xml);
		            if(message.attr("code") == "1"){
//		            	$.alert("操作成功");
						queryList();
		            } 
		            else{
		            	$.alert($("message",xml).text());
		            }
		        },
		        error : $.ermpAjaxError
		    });  
		}
	});
}