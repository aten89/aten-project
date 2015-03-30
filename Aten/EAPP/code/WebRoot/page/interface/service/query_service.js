var mainFrame = $.getMainFrame();
var trunPageObj;//翻页组件对像
$(initServiceConfigList);

function initServiceConfigList() {
    //mainFrame.getCurrentTab().setTitle("列表-服务配置", "服务配置查询列表");
//    $.handleRights({
//            "addFwpz" : $.SysConstants.ADD,
//            "bindJrzh" : $.SysConstants.BIND_ACTOR,
//            "bindMkdz" : $.SysConstants.BIND_RIGHT,
//            "searchService" : $.SysConstants.QUERY
//        }
//    );
    
	/*新增-服务配置*/
	$("#addFwpz").click(
	function(e){
			mainFrame.addTab({
				id:"ermp_service_"+Math.floor(Math.random() * 1000000),
				title:"新增服务配置",
				url:"m/service/initframe?action=add",
				callback:queryList
			});
		}
	);
	/*绑定接入帐号-服务配置*/
	$("#bindJrzh").click(
	function(e){
			mainFrame.addTab({
				id:"ermp_service_BindAccount",
				title:"绑定接入帐号",
				url:"m/service/initframe?action=bindactor",
				callback:queryList
			});
		}
	);
	/*绑定模块操作-服务配置*/
	$("#bindMkdz").click(
	function(e){
			mainFrame.addTab({
				id:"ermp_service_BindModuleAction",
				title:"绑定动作",
				url:"m/service/initframe?action=bindright",
				callback:queryList
			});
		}
	);

    $("#refresh").click(
        function(){
 			$("#data_list").html("");
	    	queryList();
        }
    );	

    /*搜索*/
	$("#searchService").click(
		function() {
			trunPageObj.gotoPage(1);//搜索第一页
		}
	);
	
    //回车搜索
    $.EnterKeyPress($("#serviceName"),$("#searchService"));
    //初始化翻页组件
    trunPageObj =  $(".pageNext").turnPage();
	trunPageObj.gotoPage(1);//搜索第一页
}

function queryList() {
	$("#searchService").attr("disabled","true");
	 //分页
	var pageNo=trunPageObj.getCurrentPageNo();//当前第几页
	var pageCount=trunPageObj.getPageCount();//一页多少条
	

	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
   	   url  : "m/service/query",
   	   dataType : "json",
   	   data: {
			pageNo: pageNo,
			pageSize : pageCount,
			serviceName : $("#serviceName").val()
		}, 
       success : function(data) {
       		if ($.checkErrorMsg(data) ) {
		   		var fileList = "";
				if(data.servicePage && data.servicePage.dataList){
					var dataList = data.servicePage.dataList;
					$(dataList).each(function(i) {
						fileList += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" ;
						fileList += "<td>" + (i + 1) + "</td>";
						fileList +="<td class='titLeft'>" + dataList[i].serviceName+"&nbsp;</td>";
						fileList += "<td>" + (dataList[i].isValid ? "有效" : "无效") + "</td>";
						fileList +="<td class='titLeft'>" + (dataList[i].description ?dataList[i].description : "")+"</td>";
	                    
	                    //操作
						var op = $.wrapActionRight($.SysConstants.VIEW,"<a class=\"opLink\" href=\"javascript:view('" + dataList[i].serviceID + "');void(0);\">详情</a>&nbsp;|&nbsp;")
					        + $.wrapActionRight($.SysConstants.MODIFY,"<a class=\"opLink\" href=\"javascript:modifyService('" + dataList[i].serviceID + "');void(0);\">修改</a>&nbsp;|&nbsp;")
							+ $.wrapActionRight($.SysConstants.DELETE,"<a class=\"opLink\" href=\"javascript:deleteService('" + dataList[i].serviceID + "');void(0);\">删除</a>&nbsp;|&nbsp;");
                        fileList += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td></tr>";
					});
				}
				$("#data_list").html(fileList);
				trunPageObj.setPageData(data.servicePage);
		   	}
			$("#searchService").removeAttr("disabled");
       }
	});
}

function view(serviceId){
	mainFrame.addTab({
		id:"ermp_service_"+ serviceId,
		title:"查看服务配置",
		url:"m/service/initframe?action=view&serviceid=" + serviceId,
		callback:queryList
	});
};

function modifyService(serviceId){
	mainFrame.addTab({
		id:"ermp_service_"+ serviceId,
		title:"修改服务配置",
		url:"m/service/initframe?action=modify&serviceid=" + serviceId,
		callback:queryList
	});
};

function deleteService(serviceId){
	$.confirm("确认要删除该服务吗？", function(r){
		if (r) {
			if(!serviceId || $.trim(serviceId) == ""){
				$.alert("参数传入出错!");
				return;
			};
			$.ajax({
				type : "POST",
				cache: false,
				url  : "m/service/delete",
				dataType : "json",
				data : "serviceIDs=" + serviceId,
				success : function(data) {
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