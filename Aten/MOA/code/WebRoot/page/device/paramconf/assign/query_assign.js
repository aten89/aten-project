/**********************
参数设置--设备属性配置项--列表
*********************/ 
var mainFrame = $.getMainFrame();
var edit=false;
var deviceTypeSel;
var deviceClassFlag=false;
var areaCodeSel;
var areaCodeValue;
$(initInventoryAdminConfig);
function initInventoryAdminConfig(){
	  //添加权限约束
    $.handleRights({
   		"assignCon_add" : $.SysConstants.ADD
	});
  $("#ssqy").ySelect({width:36});
  queryList();
  //刷新
	$("#refresh").click(function(){
		$("#chargeTab > tbody").html("");
		$("#assignCon_add").removeAttr("disabled","true").removeClass("icoNone");
	    queryList();     
	});
	//批量删除
	$("#addDelete").click(function(){
		if (edit) {
			$.alert("请先保存");
			return;
		}
	    batchDelete();     
	});
  //新增
	$("#assignCon_add").click(function(){
		var edit=false;
	    addDeviceAssign();
	});
	
	//全选
	$("#assignCheckbox").click(function(){
	    checkAssignAll();     
	});
	
	$("#saveProblem").click(function(){
		if(!checkValues()){
			return false;
		}
		saveProblem("deviceClassDiv2");
		$("input[name=classId]").each(function() {
            $(this).attr("checked", false);
        });
        deviceClassFlag = false;
        $("#selectClass").removeAttr("checked","false");
        initArea(areaCodeSel.getValue());
         initDeviceTypeSel(deviceTypeSel.getValue());
	});
	$("#selClose").click(function(){
		$("#deviceClassDiv2").hide();
		$("input[name=classId]").each(function() {
            $(this).attr("checked", false);
        });
        deviceClassFlag = false;
        $("#selectClass").removeAttr("checked","false");
        initArea(areaCodeSel.getValue());
         initDeviceTypeSel(deviceTypeSel.getValue());
	});
};
/**
 * 初始化设备类型下拉列表
 * @param {} deviceTypeCode 设备类型代码
 */
function initDeviceTypeSel(deviceTypeCode) {
	deviceTypeSel = $("#deviceTypeSel").ySelect({
		width : 70, 
		url:BASE_PATH+"m/data_dict?act=selectdevtype",
		afterLoad : function() {
			deviceTypeSel.addOption("", "请选择...", 0);
			if (deviceTypeCode != null && deviceTypeCode != "") {
				deviceTypeSel.select(deviceTypeCode);
			} else {
				deviceTypeSel.select(0);
			}
			
		},
		onChange : function(value, text) {
			if(deviceTypeCode!=value){
					$("#classNames").val("");
				}
		}
	});     
}
//全选
function checkAssignAll(){
	if ($("#assignCheckbox").attr("checked")) {
        $("input[name=assignId]").each(function() {
            $(this).attr("checked", true);
        });
    } else {
        $("input[name=assignId]").each(function() {
            $(this).attr("checked", false);
        });
    }
	
}
function saveProblem(dcDiv){
	$("#" + dcDiv).hide();
}
//新增
function addDeviceAssign(){
	$("#assignCon_add").attr("disabled","true").addClass("icoNone");
	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}
    var chargeList="<tr id=\"reportDefine\">"
    				+"<td></td>"
					+"<td><div><div id=\"areaCodeDiv\" name=\"areaCode\"></div></div></td>"
					+"<td><div><div id=\"deviceTypeSel\" name=\"deviceTypeSel\"></div></div></td>"
	               	+"<td><textarea id=\"classNames\" readonly=\"true\" name=\"zyjd\" class=\"area01\" style=\"width:130px;height:30px;\" onclick=\"deviceClass('classNames','deviceClassDiv2','')\"></textarea><input id=\"classIds\" type=\"hidden\"></td>"
	               	+"<td><textarea id=\"remark\" name=\"zyjd\" class=\"area01\" style=\"width:70px;height:30px;\"></textarea></td>"
	               	+"<td><a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"saveAssginConfig('')\">确定</a> | <a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"cancelSave(this)\">取消</a></td>"
					+"</tr>";
	
	if($("#assignTab > tbody tr").length==0){
	  $("#assignTab > tbody").append(chargeList)
	}else{
	  $(chargeList).insertBefore($("#assignTab > tbody tr:eq(0)"));
	}
	initDeviceTypeSel("");
	initArea("");
}

function initArea(areaCode){
		areaCodeValue=areaCode;
	    areaCodeSel = $("#areaCodeDiv").ySelect({width:56, url:"m/data_dict?act=ereasel",
		afterLoad:function(){
			areaCodeSel.addOption("", "请选择...", 0);
			areaCodeSel.select(areaCode);
			
		},onChange:function() {
				if(areaCodeValue!=areaCodeSel.getValue()){
					$("#classNames").val("");
				}
		}
	});
}

function initIsdisabledArea(areaCode){
	    areaCodeSel = $("#areaCodeDiv").ySelect({width:56,isdisabled: true, url:"m/data_dict?act=ereasel",
		afterLoad:function(){
			areaCodeSel.addOption("", "请选择...", 0);
			areaCodeSel.select(areaCode);
			areaCodeValue=areaCodeSel.getValue();
		}
	});
}
function initdisabledDeviceTypeSel(deviceTypeCode) {
	deviceTypeSel = $("#deviceTypeSel").ySelect({
		width : 55, 
		isdisabled: true,
		url:BASE_PATH+"m/data_dict?act=selectdevtype",
		afterLoad : function() {
			deviceTypeSel.addOption("", "请选择...", 0);
			if (deviceTypeCode != null && deviceTypeCode != "") {
				deviceTypeSel.select(deviceTypeCode);
			} else {
				deviceTypeSel.select(0);
			}
			
		}
	});     
}
//取消新增
function cancelSave(self){
	edit = false;
	$(self).parent().parent().remove();
	$("#assignCon_add").removeAttr("disabled","true").removeClass("icoNone");
}
//设备类别--弹出层
function deviceClass(parent,divId,classIds){
	if(areaCodeSel.getValue()==""){
		$.alert("请选择授权地区");
		return;
	}
	if(deviceTypeSel.getValue()==""){
		$.alert("请选择资产类别");
		return;
	}
	getClassInfo(areaCodeSel.getValue(),deviceTypeSel.getValue(),classIds);
	deviceClassFlag = true;
	initIsdisabledArea(areaCodeSel.getValue());
	initdisabledDeviceTypeSel(deviceTypeSel.getValue());
	
	divObj = $("#" + divId);
	parentObj = $("#" + parent);
	if(divObj.is(":hidden")){
			var expTop = parentObj.offset().top + 24;
			var expLeft = parentObj.offset().left + 5;
			divObj.show().css({position:"absolute","top":expTop+"px" ,"left":expLeft+"px"});
		}else{
			deviceClassFlag = false;
			divObj.hide();
		}
	
	
}

//查询
function queryList(){
	edit=false;
	$("#assignTab > tbody").empty();
	 //分页
	//提交到后台
	$.ajax({
	    type : "POST",
		cache: false,
		url  : "m/device_assgin",
		data : {
				act:"query"
			},
	       success : function(xml){
	     		//解析XML中的返回代码
				var messageCode = $("message",xml).attr("code");
				if(messageCode == "1"){
					var bodyHTML = "";
					$("class-assign",xml).each(
	                    function(index){
	                        var option = $(this);
	                        var mamagerFlag=$("mamager-flag",option).text()=="0"?"已授权":"未授权";
	                        var selectFlag=$("select-flag",option).text()=="1"?"已授权":"未授权";
	                        bodyHTML += createTR(option.attr("id"),$("areaCode",option).text(),$("areaName",option).text(),$("deviceType",option).text(),$("deviceTypeName",option).text(), $("class-names",option).text(),$("class-ids",option).text(),$("remark",option).text());
	                });
	                $("#assignTab > tbody").html(bodyHTML);
					 $("assignTab > tbody").html(bodyHTML).find("td:empty").html("&nbsp;");
				}else{
               		$.alert($("message",xml).text());
           		};
	       },
	       error : $.ermpAjaxError
	});	
}
//授权
function mamagerDeviceAreaAssgin(id,type){
	if(deviceClassFlag){
		$.alert("请关闭设备类别选择框");
		return;
	}
	if (edit) {
		$.alert("请先保存");
		return;
	}
	mainFrame.addTab({
		id:"oa_device_class_assign"+id,
		title:"设备类别授权", 
		url:BASE_PATH + "page/device/paramconf/assign/frame_assign.jsp?id="+id+"&type="+type,
		callback:queryList
	});
	
//	var ok;
//	if($.browser.msie && $.browser.version==6.0){
//		ok=$.showModalDialog(BASE_PATH + "page/EquipmentManagement/SystemManagement/ParameterSetting/InventoryAdminConfig/ImpowerFrame.jsp?id="+id+"&type="+type,"","dialogHeight:613px;dialogWidth:858px;status:no;scroll:auto;help:no");
//	}else{
//		ok=$.showModalDialog(BASE_PATH + "page/EquipmentManagement/SystemManagement/ParameterSetting/InventoryAdminConfig/ImpowerFrame.jsp?id="+id+"&type="+type,"","dialogHeight:558px;dialogWidth:850px;status:no;scroll:auto;help:no");
//	} 
//	if (ok){
//		queryList();
//	}

}
//查询
function getClassInfo(areaCode,deviceType,classIds){
	//提交到后台
	$.ajax({
	    type : "POST",
		cache: false,
		url  : "m/device_areacfg",
		data : {act:"getClass",
			areaCode:areaCode,
			deviceType:deviceType
		},
	       success : function(xml){
	     		//解析XML中的返回代码
				var messageCode = $("message",xml).attr("code");
				if(messageCode == "1"){
					var html = "<ul id=\"classType\"><li><input type=\"checkbox\" id=\"selectClass\" class=\"cBox\" name=\"exp\" onclick=\"checkAll()\"/><label for=\"selectClass\">全部</label></li>";
					$("area-config",xml).each(
	                    function(index){
	                        var deviceclassLayout = $(this);
	                        var id=$("class-id",deviceclassLayout).text();
	                        
	                        var name=$("class-name",deviceclassLayout).text();
							    
							if(classIds.indexOf(id)!=-1 && $("#classNames").val()!=""){
	                        	html+="<li style='clear:both;'><input type=\"checkbox\" class=\"cBox\" name=\"classIds\" id=\"dt"+id+"\" checked=\"true\" value=\""+id+"\" onclick=\"deleteCheckAll('dt"+id+"')\"/><label for=\"dt"+id+"\" id=\""+id+"\">"+name+"</label></li>"
	                        }else{
	                        	html+="<li style='clear:both;'><input type=\"checkbox\" class=\"cBox\" name=\"classIds\" value=\""+id+"\" id=\"dt"+id+"\" onclick=\"deleteCheckAll('dt"+id+"')\"/><label for=\"dt"+id+"\" id=\""+id+"\">"+name+"</label></li>"
	                        }
						            
					  });
					  html += "</ul>";
					  $("#classDiv").html(html);
				}
	       },
	       error : $.ermpAjaxError
	});	
}

//全选
function checkAll(){
	if ($("#selectClass").attr("checked")) {
        $("input[name=classIds]").each(function() {
            $(this).attr("checked", true);
        });
    } else {
        $("input[name=classIds]").each(function() {
            $(this).attr("checked", false);
        });
    }
	
}
function deleteCheckAll(id){
	if (!$("#"+id).attr("checked")) {
         $("input[name=exp]").each(function() {
            $(this).attr("checked", false);
        });
    }
	
}
//获取选中的只
function checkValues(){
	//获得参数
	var classIds = "";//选中的设备类别
	var classNames = "";//选中的设备类别
	var flag=true;
	$("input[name='classIds'][checked]", "#classType").each(function(){
		if( this.checked == true ){
			classIds +=";"+this.value;
			if(flag){
				classNames = $("#"+this.value).html();
				flag=false;
			}else{
				classNames += "；"+$("#"+this.value).html();
			}
		}
	});
	if(classIds==""){
		$.alert("请选择设备类别");
		return false;
	}
	
	$("#classNames").val(classNames);
	$("#classIds").val(classIds);
	return true;
}

//保存
function saveAssginConfig(id){
	if(deviceClassFlag){
		$.alert("请关闭设备类别选择框");
		return;
	}
	var classIds = $.trim($("#classIds").val());
	var remark = $.trim($("#remark").val());
	var areaCode = areaCodeSel.getValue();
	if(areaCode==""){
		$.alert("请选择所属地区");
		return;
	}
	var deviceType = deviceTypeSel.getValue();
	if(deviceType==""){
		$.alert("请选资产类别");
		return;
	}
	if(classIds==""){
		$.alert("请选择设备类别");
		return;
	}
	result = $.validChar(remark);
	if (result) {
		$.alert("备注不能包含非法字符：" + result);
		$("#remark").focus();
		return;
	}
	if (remark.length >300) {
		$.alert("备注长度不能大于300个字符");
		$("#remark").focus();
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
		url  : "m/device_assgin",  //
		data : {
			id:id,
			act: act,
			classIds:classIds,
			deviceType:deviceType,
			areaCode:areaCode,
			remark : remark
		},
        success : function(xml){
            var message = $("message",xml);
            if(message.attr("code") == "1"){
            	$.alert("保存成功");
            	edit=false;
            	$("#assignCon_add").removeAttr("disabled","true").removeClass("icoNone");
				queryList();
            } 
            else{
            	$.alert($("message",xml).text());
            }
        },
        error : $.ermpAjaxError
    });  
}

//创建行
function createTR(id,areaCode,areaname,deviceType,deviceTypeName,className,classIds,remark){
	var html = "";
	html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
		+"<td><input type=\"checkbox\" name=\"assignId\" id=\"\" value=\""+id+"\"/></td>"
		+ "<td>"+areaname+"<input type=\"hidden\" name=\"\" value=\""+areaCode+"\"/></td>"
		+ "<td>"+deviceTypeName+"<input type=\"hidden\" name=\"\" value=\""+deviceType+"\"/></td>"
		+ "<td>"+className+"<input type=\"hidden\" name=\"\" value=\""+classIds+"\"/><input type=\"hidden\" name=\"\" value=\""+className+"\"/></td>" 
		+ "<td>" + remark + "</td>" ;

	var op =  ($.hasRight($.SysConstants.MODIFY)?("<a href=\"javascript:void(0);\" onclick=\"modifyDeviceAssgin(this,'" + id + "');\" class=\"opLink\">修改</a>&nbsp;|&nbsp;") : "")
               + ($.hasRight($.SysConstants.DELETE)?("<a href=\"javascript:void(0);\" onclick=\"deleteDeviceAssgin('" + id + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;") : "")
                + ($.hasRight($.SysConstants.MODIFY)?("<a href=\"javascript:void(0);\" onclick=\"mamagerDeviceAreaAssgin('" + id + "','0');\" class=\"opLink\">管理授权</a>&nbsp;|&nbsp;") : "")
                 + ($.hasRight($.SysConstants.MODIFY)?("<a href=\"javascript:void(0);\" onclick=\"mamagerDeviceAreaAssgin('" + id + "','1');\" class=\"opLink\">查询授权</a>&nbsp;|&nbsp;") : "")
    html += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"));   
    html += "</td></tr>";
    return html;
}

//删除
function deleteDeviceAssgin(id){
	if(deviceClassFlag){
		$.alert("请关闭设备类别选择框");
		return;
	}
	if (edit) {
		$.alert("请先保存");
		return;
	}
	if(""==id || null==id){
		$.alert("参数不能为空");
		return;
	}
	
	$.confirm("确认要删除该区域授权配置吗？",function(r){
		if (r) {
			$.ajax({
		        type : "POST",
				cache: false,
				url  : "m/device_assgin",  //
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

//修改
function modifyDeviceAssgin(self,id){
	$("#assignCon_add").attr("disabled","true").addClass("icoNone");
	if(deviceClassFlag){
		$.alert("请关闭设备类别选择框");
		return;
	}
	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}
	var selfObj=$(self).parent().parent().find("td");
	var areaName = selfObj.eq(1).text();
	var areaCode = $("input",selfObj.eq(1)).eq(0).val();
	var deviceTypeName = selfObj.eq(2).text();
	var deviceType = $("input",selfObj.eq(2)).eq(0).val();
	var classIds = $("input",selfObj.eq(3)).eq(0).val();
	var classNames = $("input",selfObj.eq(3)).eq(1).val();
	var classNameTexts = selfObj.eq(3).text();
	var remark = selfObj.eq(4).text();
	var newOperate="<a href=\"javascript:void(0);\" onclick=\"saveAssginConfig('"+id+"')\" class=\"opLink\">确定</a>&nbsp;|&nbsp"
	              +"<a href=\"javascript:void(0);\" onclick=\"cancelModify(this,'" + id + "','" + areaCode + "','"  + areaName + "','" + deviceType + "','"  + deviceTypeName + "','" + classNames + "','" + classIds + "','"  +remark+ "')\" class=\"opLink\">取消</a>";
	selfObj.eq(0).html("");
	selfObj.eq(1).html("<div><div id=\"areaCodeDiv\" name=\"areaCode\"></div></div>");
	selfObj.eq(2).html("<div><div id=\"deviceTypeSel\" name=\"deviceTypeSel\"></div></div>");
	selfObj.eq(3).html("<textarea id=\"classNames\" readonly=\"true\" name=\"zyjd\" class=\"area01\" style=\"width:130px;height:30px;\" onclick=\"deviceClass('classNames','deviceClassDiv2','"+classIds+"')\">"+classNameTexts+"</textarea><input id=\"classIds\" type=\"hidden\" value=\""+classIds+"\">");
	selfObj.eq(4).html("<textarea id=\"remark\" name=\"zyjd\" class=\"area01\" style=\"width:70px;height:30px;\">"+remark+"</textarea>");
	selfObj.eq(5).html(newOperate);
//	selfObj.eq(4).html("");
//	selfObj.eq(5).html("");
	initArea(areaCode);
	initDeviceTypeSel(deviceType);
	
	
	
}

//取消修改
function cancelModify(self,id,areaCode,areaName,deviceType,deviceTypeName,className,classIds,remark){
	if(deviceClassFlag){
		$.alert("请关闭设备类别选择框");
		return;
	}
	$(createTR(id,areaCode,areaName,deviceType,deviceTypeName,className,classIds,remark)).insertBefore($(self).parent().parent());
	$(self).parent().parent().remove();
	$("#assignCon_add").removeAttr("disabled","true").removeClass("icoNone");
	edit=false;
}
//获取选中的值
function checkDeleteValue(){
	//获得参数
	var assignIds = "";//
	var flag=true;
	$("input[name='assignId'][checked]", "#assignTab").each(function(){
		if( this.checked == true ){
			if(flag){
				assignIds = this.value;
				flag=false;
			}else{
				assignIds += ","+this.value;
			}
		}
	});
	return assignIds;
}
//批量删除工作报告计划
function batchDelete(){
	var assignIds=checkDeleteValue();
	if(assignIds==""){
		$.alert("请选择要操作的数据！")
		return;
	}
	$.confirm("确认要删除该区域授权信息吗？",function(r){
		if(r) {
			$.ajax({
	       type : "POST",
	       cache: false,
	       url  : "m/device_assgin",
	       data : {act:"batchdelete",assignIds:assignIds},
	       success : function(xml){
	           var messageCode = $("message",xml).attr("code");
	           if(messageCode == "1"){
//	           		$.alert("操作成功!")
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
