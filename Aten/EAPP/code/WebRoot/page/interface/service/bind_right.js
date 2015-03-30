var isChange = false;
$(initBindRight);
function initBindRight(){
    //$.getMainFrame().getCurrentTab().setTitle("动作-服务配置", "接口服务绑定模块动作");
    
    $("#subSystemList").ySelect({width: 110,json:true,url : "m/subsystem/allsystem",onChange : subSystemList_OnChange});
    
    $(".addTool input").removeAttr("disabled").removeClass("icoNone");
    $("#bindMkdz").attr("disabled","true").addClass("icoNone");
    //保存按扭不可用
    $("#saveRightRoles").attr("disabled","true").addClass("icoNone");
    var selectService = $("#hidServiceId").val();
    /*如果ID为空则不能进行修改操作,不为空则可修改且列出唯一项*/
    if(selectService=='null' || selectService== ""){
    	$("#modFwpz").attr("disabled","true").addClass("icoNone");
    	$.ajax({
			type : "POST",
			async : true,
			cache: false,
			dataType : "json",
		   	url  : "m/service/xmlservices",
		    success : searchServiceBindSuccess
		});
    }else{
    	$("#txtSearchService").attr("disabled","true").addClass("icoNone");
    	$("#search_Service").attr("disabled","true").addClass("icoNone");
    	var serviceName = $("#hidServiceName").val();
    	var StrHtml ="";
    	StrHtml += "<div><input name=\"serviceid_1\" type=\"radio\" value=\"" + selectService + "\" id=\"" + selectService + "\" onclick=\"javascript:selectService('"+selectService+"','"+serviceName+"');\"/>" 
                                   + "<label>" + serviceName + "</label></div>";
		$(".jsdw").html(StrHtml); 
    	$("#"+selectService).click();
    };
    //服务列表筛选
    $("#search_Service").click(
        function(e){
        	$(".jsdw > div:hidden").show();
        	var tvalue = $.trim($("#txtSearchService").val());
            if(tvalue == ""){
                return;
            };
            $(".jsdw label:not(:contains('" + tvalue + "'))").parent().hide();
            //选中的不隐藏
            $(".jsdw input:checked").parent().show();
        }
    );
	$.EnterKeyPress($("#txtSearchService"),$("#search_Service"));
	
    //绑定用户动作
    $("#saveRightRoles").click(
        function(e){
            $.ajax({
                type : "POST",
                cache: false,
                async : true,
        		url  : "m/service/bindright",
        		dataType : "json",
                data : $("#mainForm").serialize(),
                success : function(data){
                	if ($.checkErrorMsg(data) ) {
                		 $.alert("绑定成功！");
                	}
                    $("#saveRightRoles").attr("disabled","true").addClass("icoNone");
                   	isChange = false;
                }
            });
        }
    );
};

//取得服务有效的权限列表
function searchServiceBindSuccess(data){
	if ($.checkErrorMsg(data) ) {
		var dataList = data.services;
		if (dataList) {
			var StrHtml = "";
			$(dataList).each(function(i) {
				var sid = dataList[i].serviceID;
	    		var sName = dataList[i].serviceName;
				StrHtml += "<div><input name=\"serviceid_1\" type=\"radio\" value=\"" + sid + "\"  onclick=\"javascript:selectService('"+sid+"','"+sName+"');\"/>" 
	            	+ "<label>" + sName + "</label></div>";
			});
			$(".jsdw").html(StrHtml); 
		}
	}
};

//左边的单选单击
function selectService(o, name){
	if (isChange) {
		$.confirm("是否先保存当前修改？", function(r){
    		if (r) {
    			$("#saveRightRoles").click();
    		}
    		loadBindedRight(o, name);
    	});
		isChange=false;
	} else {
		loadBindedRight(o, name);
	}
}

function loadBindedRight(o, name) {
  //  $("#hidServiceId").val(o);
  //  $("#hidServiceName").val(name);
    
  //  $("#modFwpz").removeAttr("disabled").removeClass("icoNone");
	$.ajax({
		type : "POST",
		async : true,
		cache: false,
	   	url  : "m/service/bindedrights",
	   	dataType : "json",
		data : {serviceID: o},
	    success : function(data){
	    	if ($.checkErrorMsg(data) ) {
	    		var dataList = data.moduleActions;
        		if (dataList) {
        			var rolesActionHTML = "";
        			$(dataList).each(function(i) {
        				rolesActionHTML += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
		                               + "<td><a class=opLink>删除</a><input type=\"hidden\" name=\"moduleActionIDs\" value=\"" + dataList[i].moduleActionID + "\"></td>" 
		                               + "<td>"+dataList[i].module.subSystem.name+"</td>" 
		                               + "<td>"+dataList[i].module.name+"</td>"
		                               + "<td>"+dataList[i].action.name+"</td>"
		                               + "</tr>";
        			});
        			$("#bindRightList > tbody").html(rolesActionHTML).find("td:empty").html("&nbsp;");
			        $("#serviceid").val(o);
			        //点击解绑定事件
			        $("#bindRightList > tbody a").click(
			            function(e){
			                $(this).parent().parent().remove();
			                $("#saveRightRoles").removeAttr("disabled").removeClass("icoNone");
			                isChange = true;
			                 $("#" +$(this).next().val(), "#moduleActionList > tbody").removeClass("over");//移除样式
			            }
			        );
			        $("tr", "#moduleActionList > tbody").each(function() {
			         	if ($("#bindRightList input[value='" + $(this).attr("id") + "']").length != 0) {//是否在已绑定列表
			         		$(this).addClass("over");
			         	} else {
			         		$(this).removeClass("over");
			         	}
			         });
        		} else {
        			$("#bindRightList > tbody").empty();
        		}
	    	}
		}
	});	
};

function subSystemList_OnChange(value){
	$.ajax({
		type : "POST",
		cache: false, 
        async : true,
		url  : "m/module/subsystemtree",
		dataType : "json",
		data : {subSystemID : value},
		success : function(data){
	        $("#jkpzTree").html(data.htmlValue);
	        $("#jkpzTree").simpleTree({animate: true,json:true,afterClick : systemModulesClick});
            //清空权限列表
            $("#moduleActionList > tbody").empty();
		}
	});
};

function systemModulesClick(o){
    var moduleId = $(o).attr("moduleId");
    
    $.ajax({
        type : "POST",
        cache: false,
        async : true,
		url  : "m/module_action/findmas",
		dataType : "json",
		data : "moduleID=" + moduleId + "&isRPC=Y",
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		if(data.moduleActionPage && data.moduleActionPage.dataList){
					var dataList = data.moduleActionPage.dataList;
					var moduleActionHTML = "";
					$(dataList).each(function(i) {
						var maid = dataList[i].moduleActionID;
                    	var inList = $("#bindRightList input[value=" + maid + "]").length != 0;//是否在已绑定列表
                    	
                    	 moduleActionHTML += "<tr id='" + maid + "' " + (inList ? "class='over'" : "") + ">" 
                                         + "<td><a class='opLink bindopt'>绑定</a> <a class='opLink unbindopt'>解除</a><input type=\"hidden\" value=\"" + maid + "\"></td>" 
                                         + "<td>" + dataList[i].action.actionKey  + "</td>" 
                                         + "<td>" + dataList[i].action.name  + "</td>" 
                                         + "</tr>";
					});
					$("#moduleActionList > tbody").html(moduleActionHTML).find("td:empty").html("&nbsp;");
	                //点击绑定事件
	                $("#moduleActionList .bindopt").click(function(e){
                        if($(".jsdw input:checked").length == 0){
                            $.alert("请选择要绑定的服务！");
                            return;
                        };
                        
                        var c_tr = $(this).parent().parent();
                        var actionID = c_tr.find("input").val();
                        
                        //判断是否已被添加
                        if($("#bindRightList input[value=" + actionID + "]").length != 0){
                            $.alert("不能重复绑定");
                            return;
                        };
						
					//	var actionKey = c_tr.find("td:eq(1)").html();
						var actionName = c_tr.find("td:eq(2)").html();
						var systemName = $(".root > span").text();
						var moduleName = $("> span",o).text();
						
                        $("#bindRightList > tbody").append(
                        		"<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
                        		+ "<td><a class=opLink>删除</a><input type=\"hidden\" name=\"moduleActionIDs\" value=\"" + actionID + "\"></td><td>" 
                        		+ systemName + "</td><td>" + moduleName + "</td><td>" + actionName + "</td></tr>")
                        .find("a:last").click(function(e){
                                $(this).parent().parent().remove();
                                $("#saveRightRoles").removeAttr("disabled").removeClass("icoNone");
                        		isChange = true;
                        		
                        		$("#" +$(this).next().val(), "#moduleActionList > tbody").removeClass("over");//移除样式
                        });
                        $("#saveRightRoles").removeAttr("disabled").removeClass("icoNone");
                        isChange = true;
                        
                        c_tr.addClass('over');//添加已绑定样式
	            	});
	            	
					//点击解除绑定事件
	                $("#moduleActionList .unbindopt").click(function(e){
                       if($(".jsdw input:checked").length == 0){
                            $.alert("请选择要绑定的服务！");
                            return;
                        };
                        
                        var c_tr = $(this).parent().parent();
                        var actionID = c_tr.find("input").val();
						
						$("#bindRightList input[value=" + actionID + "]").parent().parent().remove();
						$("#saveRightRoles").removeAttr("disabled").removeClass("icoNone");
						isChange = true;
						$("#" +actionID, "#moduleActionList > tbody").removeClass("over");//移除样式
					});
        		} else {
        			$("#moduleActionList > tbody").empty();
        		}
        	}
        }
    });
}