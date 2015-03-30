var mainFrame = $.getMainFrame();
var trunPageObj;//翻页组件对像
$(initPortletList);

function initPortletList(){
    //mainFrame.getCurrentTab().setTitle("列表-企业门户", "企业门户查询列表");
//    $.handleRights({
//		"addPortlet" : $.SysConstants.ADD,
//		"searchPortlet" : $.SysConstants.QUERY,
//		"defaultPortlet" : $.SysConstants.SET_DEFAULT
//	});
    
    //添加
    $("#addPortlet").click(
        function(e){
        	mainFrame.addTab({
				id:"ermp_portlet_" + Math.floor(Math.random() * 1000000),
				title:"新增门户",
				url:"m/portlet/initframe?action=initadd",
				callback:queryList
			});
        }
    );
    
    //刷新
    $("#refreshPortlet").click(
        function(e){
            $("#data_list").html("");
	    	queryList();
        }
    );
    
     //默认门户
    $("#defaultPortlet").click(
        function(e){
        	mainFrame.addTab({
				id:"ermp_portlet_defaultPortal",
				title:"设置默认门户",
				url:"m/portlet/showdefault",
				callback:queryList
			});
        }
    );
    
    //加载数据
    $("#searchPortlet").click(function(){
        	trunPageObj.gotoPage(1);//搜索第一页
        }
    );
    //回车搜索
    $.EnterKeyPress($("input[name=portletname]"),$("#searchPortlet"));
    //初始化翻页组件
    trunPageObj =  $(".pageNext").turnPage();
	trunPageObj.gotoPage(1);//搜索第一页
};

//加载数据
function queryList(){
    $("#searchPortlet").attr("disabled","true");
    //分页
	var pageNo=trunPageObj.getCurrentPageNo();//当前第几页
	var pageCount=trunPageObj.getPageCount();//一页多少条
	
    $.ajax({
        type : "POST",
        cache: false,
        async : true,
		url  : "m/portlet/query",
		dataType : "json",
		data: {
			pageNo: pageNo,
			pageSize : pageCount,
			portletName : $.trim($("#portletname").val())
		},
        success: function(data){
        	if ($.checkErrorMsg(data) ) {
				var fileList = "";
				if(data.portletPage && data.portletPage.dataList){
					var dataList = data.portletPage.dataList;
					$(dataList).each(function(i) {
						fileList += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" ;
						fileList +=  "<td>" + (i + 1) + "</td>";
						fileList += "<td>" + dataList[i].portletName + "</td>";
						fileList += "<td>" + dataList[i].subSystem.name + "</td>";
						fileList += "<td>" + (dataList[i].style ? dataList[i].style : "") + "</td>";
	                    
	                    //操作
						var op = $.wrapActionRight($.SysConstants.VIEW,"<a class=\"opLink\" href=\"javascript:void(0)\" onclick=\"viewPortlet('" + dataList[i].portletID + "');\">详情</a>&nbsp;|&nbsp;") 
                        		+ $.wrapActionRight($.SysConstants.MODIFY,"<a class=\"opLink\" href=\"javascript:void(0)\" onclick=\"modifyPortlet('" + dataList[i].portletID + "');\">修改</a>&nbsp;|&nbsp;") 
                             	+ $.wrapActionRight($.SysConstants.DELETE,"<a class=\"opLink\" href=\"javascript:void(0)\" onclick=\"deletePortlet('" + dataList[i].portletID + "');\">删除</a>&nbsp;|&nbsp;");
                        fileList += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td></tr>";
					});
				}
				
				$("#data_list").html(fileList);
				trunPageObj.setPageData(data.portletPage);
			}
			$("#searchPortlet").removeAttr("disabled");
        }
    });
};

//查看
function viewPortlet(porletId){
	mainFrame.addTab({
		id:"ermp_portlet_" + porletId,
		title:"查看门户",
		url:"m/portlet/initframe?action=view&portletid=" + porletId,
		callback:queryList
	});
};

//修改
function modifyPortlet(porletId){
	mainFrame.addTab({
		id:"ermp_portlet_" + porletId,
		title:"修改门户",
		url:"m/portlet/initframe?action=initmodify&portletid=" + porletId,
		callback:queryList
	});
};

//删除
function deletePortlet(porletId){
	if (!porletId) {
    	return false;
    }
		    
	$.confirm("是否删除该板块信息？", function(r){
		if (r) {
		    $.ajax({
		        type : "POST",
		        cache : false,
		        async : true,
				url  : "m/portlet/delete",
				dataType : "json",
				data : {portletID : porletId},
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