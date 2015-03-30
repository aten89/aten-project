var mainFrame = $.getMainFrame();
$(initForm);

function initForm(){
    //mainFrame.getCurrentTab().setTitle("列表-快捷方式", "快捷方式查询列表");
//    $.handleRights(
//        {
//            "opAdd" : $.SysConstants.ADD,
//            "opSort" : $.SysConstants.ORDER
//        }
//    );
    
    
	/*新增*/
	$("#opAdd").click(
		function(){
			mainFrame.addTab({
				id:"ermp_shortcutmenu_"+Math.floor(Math.random() * 1000000),
				title:"新增-快捷方式",
				url:"/m/shortcutmenu/initframe?action=add",
				callback:queryList
			});
		}
	);
	
	/*排序*/
	$("#opSort").click(
		function(){
			mainFrame.addTab({
				id:"ermp_shortcutmenu_Sort",
				title:"排序-快捷方式",
				url:"m/shortcutmenu/initorder",
				callback:queryList
			});
		}
	);
	
	/*刷新*/
	$("#opRefresh").click(
        function(){
          	$("#data_list").html("");
	    	queryList();
        }
    );
	
	queryList();
}

function queryList(){
	$.ajax({
	   type : "POST",
	   async : true,
   	   url  : "m/shortcutmenu/query",
   	   dataType : "json",
       success : function(data){
       		if ($.checkErrorMsg(data) ) {
				var fileList = "";
				if(data.scms){
					var dataList = data.scms;
					$(dataList).each(function(i) {
						fileList += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" ;
						fileList +=  "<td>" + (i + 1) + "</td>";
						fileList += "<td><div class='clip'>" + dataList[i].menuTitle + "</div></td>";
						fileList += "<td><div class='clip'>" + dataList[i].url + "</div></td>";
						fileList += "<td><img border=0 src='" + dataList[i].logoURL +"'>&nbsp;</td>";
	                    
	                    //操作
						var op = $.wrapActionRight($.SysConstants.VIEW, "<a class=\"opLink\" href=\"javascript:viewShortCut('" + dataList[i].shortCutMenuID + "');void(0);\">详情</a>&nbsp;|&nbsp;")
                    			+ $.wrapActionRight($.SysConstants.MODIFY,"<a class=\"opLink\" href=\"javascript:modifyShortCut('" + dataList[i].shortCutMenuID + "');void(0);\">修改</a>&nbsp;|&nbsp;")
                    			+ $.wrapActionRight($.SysConstants.DELETE,"<a class=\"opLink\" href=\"javascript:deleteShortCut('" + dataList[i].shortCutMenuID + "');void(0);\">删除</a>&nbsp;|&nbsp;");
						if (dataList[i].isValid){
			                    op += $.wrapActionRight($.SysConstants.DISABLE,"<a class=\"opLink\" href=\"javascript:disableShortCut('" + dataList[i].shortCutMenuID + "');void(0);\">停用</a>&nbsp;|&nbsp;");
			            }else{
			                    op += $.wrapActionRight($.SysConstants.ENABLE,"<a class=\"opLink\" href=\"javascript:enableShortCut('" + dataList[i].shortCutMenuID + "');void(0);\">启用</a>&nbsp;|&nbsp;");
						}		
                        fileList += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td></tr>";
					});
				}
				$("#data_list").html(fileList);
        	}
       }
	});
};


function viewShortCut(id){
	mainFrame.addTab({
		id:"ermp_shortcutmenu_" + id,
		title:"查看快捷方式",
		url:"/m/shortcutmenu/initframe?action=view&shortCutMenuID=" + id,
		callback:queryList
	});
};

function modifyShortCut(id){
	mainFrame.addTab({
		id:"ermp_shortcutmenu_" + id,
		title:"修改快捷方式",
		url:"/m/shortcutmenu/initframe?action=modify&shortCutMenuID=" + id,
		callback:queryList
	});
};

function deleteShortCut(id){
	$.confirm("您确认要删除这个快捷方式吗？", function(r){
		if (r) {
 			$.ajax({
				type : "POST",
				async : true,
	   	 		url  : "m/shortcutmenu/delete",
	   	 		dataType : "json",
				data : {shortCutMenuID: id},
	    		success :  function(data){
					if ($.checkErrorMsg(data) ) {
//						 $.alert("子系统配置信息删除成功！");
		                queryList();
					}
				}
			});
		}
	});
};

function enableShortCut(id){
	$.confirm("您确认要启用这个快捷方式吗？", function(r){
		if (r) {
 			$.ajax({
				type : "POST",
				async : true,
	   	 		url  : "m/shortcutmenu/enable",
	   	 		dataType : "json",
				data : {shortCutMenuID: id},
	    		success : function(data){
					if ($.checkErrorMsg(data) ) {
//						 $.alert("子系统配置信息删除成功！");
		                queryList();
					}
	    		}
			});
		}
	});
};

function disableShortCut(id){
	$.confirm("您确认要停用这个快捷方式吗？", function(r){
		if (r) {
 			$.ajax({
				type : "POST",
				async : true,
	   	 		url  : "m/shortcutmenu/disable",
	   	 		dataType : "json",
				data : {shortCutMenuID: id},
	    		success :function(data){
					if ($.checkErrorMsg(data) ) {
//						 $.alert("子系统配置信息删除成功！");
		                queryList();
					}
	    		}
			});
		}
	});
};