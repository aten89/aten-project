var mainFrame = $.getMainFrame();
var trunPageObj;//翻页组件对像
$(initRBACRoleList);

function initRBACRoleList(){
//    $.handleRights({
//        "addRBACRole" : $.SysConstants.ADD,
//        "bindRBACRoleGroup" : $.SysConstants.BIND_GROUP,
//        "bindRBACRoleUser" : $.SysConstants.BIND_USER,
//        "bindRBACRoleModule" : $.SysConstants.BIND_RIGHT,
//        "searchRole" : $.SysConstants.QUERY
//    });
	
    //添加
    $("#addRBACRole").click(
        function(e){
        	mainFrame.addTab({
				id:"ermp_rbac_role_"+Math.floor(Math.random() * 1000000),
				title:"新增角色",
				url:"m/rbac_role/initframe?action=add",
				callback:queryList
			});
        }
    );
    
    //绑定群组
    $("#bindRBACRoleGroup").click(
        function(e){
        	mainFrame.addTab({
				id:"ermp_rbac_role_BindGroup",
				title:"绑定机构",
				url:"m/rbac_role/initframe?action=bindgroup",
				callback:queryList
			});
        }
    );
    
    //绑定用户
    $("#bindRBACRoleUser").click(
        function(e){
        	mainFrame.addTab({
				id:"ermp_rbac_role_BindUser",
				title:"绑定用户",
				url:"m/rbac_role/initframe?action=binduser",
				callback:queryList
			});
        }
    );
    
    //绑定模块动作
    $("#bindRBACRoleModule").click(
        function(e){
        	mainFrame.addTab({
				id:"ermp_rbac_role_BindModuleAction",
				title:"绑定动作",
				url:"m/rbac_role/initframe?action=bindright",
				callback:queryList
			});
        }
    );
    
    //菜单栏［刷新］
    $("#refreshRBACRole").click(
        function(){
            $("#data_list").html("");
	    	queryList();
        }
    );
    
    //搜索
    $("#searchRole").click(
        function(e){
            trunPageObj.gotoPage(1);//搜索第一页
        }
    );
    //回车搜索
    $.EnterKeyPress($("input[name=rolename]"),$("#searchRole"));  
      //初始化翻页组件
    trunPageObj =  $(".pageNext").turnPage();
	trunPageObj.gotoPage(1);//搜索第一页
}

function queryList(){
    $("#searchRole").attr("disabled","true");
     //分页
	var pageNo=trunPageObj.getCurrentPageNo();//当前第几页
	var pageCount=trunPageObj.getPageCount();//一页多少条
	
    $.ajax({
        type : "POST",
        cache: false,
        async : true,
		url  : "m/rbac_role/query",
		dataType : "json",
		 data: {
			pageNo: pageNo,
			pageSize : pageCount,
			roleName : $("#rolename").val()
		}, 
        success: function(data){
        	if ($.checkErrorMsg(data) ) {
		   		var fileList = "";
				if(data.rolePage && data.rolePage.dataList){
					var dataList = data.rolePage.dataList;
					$(dataList).each(function(i) {
						fileList += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" ;
						fileList += "<td>" + (i + 1) + "</td>";
						fileList +="<td class='clip'>" + dataList[i].roleName+"&nbsp;</td>";
						fileList += "<td>" + (dataList[i].isValid ? "有效" : "无效") + "</td>";
						fileList +="<td class='clip'>" + (dataList[i].description ?dataList[i].description : "")+"</td>";
                                           
	                    //操作
						var op = $.wrapActionRight($.SysConstants.VIEW, "<a class=opLink href=\"javascript:void(0)\" onclick=\"viewRole('" + dataList[i].roleID + "');\">详情</a>&nbsp;|&nbsp;") 
                                           + $.wrapActionRight($.SysConstants.MODIFY, "<a class=opLink href=\"javascript:void(0)\" onclick=\"modifyRole('" + dataList[i].roleID+ "');\">修改</a>&nbsp;|&nbsp;") 
                                           + $.wrapActionRight($.SysConstants.DELETE, "<a class=opLink href=\"javascript:void(0)\" onclick=\"deleteRole('" + dataList[i].roleID + "');\">删除</a>&nbsp;|&nbsp;</td>");
                        fileList += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td></tr>";
					});
				}
				$("#data_list").html(fileList);
				trunPageObj.setPageData(data.rolePage);
		   	}
		   	 $("#searchRole").removeAttr("disabled");
        }
    });
};

function deleteRole(roleId){
	$.confirm("确认要删除该角色吗？", function(r){
		if (r) {
 			if (!roleId) {
		    	return false;
		    }
		    $.ajax({
		        type : "POST",
		        cache : false,
		        async : true,
				url  : "m/rbac_role/delete",
				dataType : "json",
				data : "roleIDs=" + roleId,
		        success: function(data){
		        	if ($.checkErrorMsg(data) ) {
//		        		$.alert("删除成功！");
		                //重新加载数据
		                queryList();
		        	}
		        }
		    });
		}
	});
};

function modifyRole(roleId){
	mainFrame.addTab({
		id:"ermp_rbac_role_"+roleId,
		title:"修改角色",
		url:"m/rbac_role/initframe?action=modify&roleid=" + roleId,
		callback:queryList
	});
};

function viewRole(roleId){
	mainFrame.addTab({
		id:"ermp_rbac_role_"+roleId,
		title:"查看角色",
		url:"m/rbac_role/initframe?action=view&roleid=" + roleId,
		callback:queryList
	});
};