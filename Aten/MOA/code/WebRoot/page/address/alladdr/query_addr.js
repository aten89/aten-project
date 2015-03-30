var mainFrame = $.getMainFrame();

$(initAddrListListPage);

//初始化页面
function initAddrListListPage(){
	//添加权限约束
    $.handleRights({
   		"searchAddrListList" : $.SysConstants.QUERY,
   		"exportAsCSV" : $.SysConstants.EXPORT
	});

    //搜索
    $("#searchAddrListList").click(
        function(){
        	$("#hidNumPage").val(1);
            queryPage();
        }
    );   
	
	//刷新
  	$("#refreshAddrList").click(function(){
  		queryPage();
  	});
  	
  	//手工输入姓名进行搜索时,按回车直接搜索
  	$.EnterKeyPress($("#userAccountId"),$("#searchAddrListList"));
  	
  	//导出通讯录数据到CSV文件，用于foxmail导入
  	$("#exportAsCSV").click(function(){
  		exportAsCSV();
  	});
  	
  	$("#AllSelectState").click(function(){
		$("input[name='_doc_chk']", "#addrListList").attr("checked", this.checked);
	});

	$("#userAccountId").val("");
	$("#hidNumPage").val(1);
	$("#AllSelectState").attr("checked", false);
	
//	//初始化机构列表
//	var url =ERMP_PATH + "m/rbac_group/groupselect?jsoncallback=?";
//	$.getJSON(url,function(data){
//		$("#groupIdDiv").html(data.htmlValue);
//		groupSelect = $("#groupIdDiv").ySelect({width:100});
//		groupSelect.addOption("", "所有...", 0);
//		groupSelect.select(0);
//		
//		//默认搜索
//		queryPage();
//	});
  	
	//初始化机构
	//打开部门
    $('#openDeptSelect').click(
	   	function(e){
	   		var dialog = new DeptDialog(ERMP_PATH, BASE_PATH);
	   		dialog.setCallbackFun(function(user){
				if (user != null) {
	   				$("#deptName").val(user.name);
				}
			});
	   		dialog.openDialog();
		}
	);
	
	//默认搜索
	queryPage();
}

		
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
    queryPage();
}

function queryPage() {	
	//设置查询按钮
 	$("#addrListList").empty();
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	var userDeptId = $("#deptName").val();
	var userAccountId = $.trim($("#userAccountId").val());//用户帐号id
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : BASE_PATH + "/m/addrlist",
	   data : "act=query&pageno="+pageno+"&pagecount="+pagecount+"&userDeptName="+userDeptId
	            +"&userAccountId="+userAccountId,             
	   success : function (xml){
	   		//alert($(xml).get(0).xml);		
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listData = "";
			$("#addrListList").empty();     
			//是否有数据返回
		    if (message.attr("code") == "1") {	
		    	    	                         	    	  
		    	$(xml).find('address-list').each(function(index){
		    		var curELe = $(this);
                    var id = $.trim(curELe.attr("id"));
                    listData += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
                   	 		+ "<td class=\"listCBoxW\"><input name=\"_doc_chk\" type=\"checkbox\" value=\"" + id + "\" class=\"cBox\"/></td>"
			  				+ "<td>" + $("user-account-id",curELe).text() + "</td>"
							+ "<td>" + $("user-name",curELe).text() + "</td>"
							+ "<td>" + $("mobile",curELe).text() + "</td>"
							+ "<td>" + $("officeTel",curELe).text() + "</td>"
							+ "<td>" + $("email",curELe).text() + "</td>"
							+ "<td>" + $("seatNumber",curELe).text() + "</td>"
							+ "<td>" + ($.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initView('" + id + "','" + $("user-account-id",curELe).text() + "','" + $("user-name",curELe).text() + "')\">详情</a> " : "")//通过userAccountId绑定修改
							+ ($.hasRight($.SysConstants.MODIFY)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initModify('" + id + "','" + $("user-account-id",curELe).text() + "','" + $("user-name",curELe).text() +"')\">修改</a> " : "")
							+ "</td></tr>";

				});
				$("#addrListList").html(listData);
				//----------------------------------------------------
		        //------------------翻页数据----------------------------
		        $(".pageNext").html($.createTurnPage(xml));
		        //----------------------------------------------------		
		        $.EnterKeyPress($("#numPage"),$("#numPage").next());
		    } else {
		        $(".pageNext").html("");
		    };
	   		$("#searchAddrListList").removeAttr("disabled");
	   },
	   error : function(){
	        $("#searchAddrListList").removeAttr("disabled");
	        $.ermpAjaxError();
	    }
	});
}

//转到详情查看页面
function initView(id, userAccountId, userName) {
	mainFrame.addTab({
		id:"oa_addrlist_view_"+userAccountId,
		title:userName, 
		url:BASE_PATH + "/m/addrlist?act=view&id=" + id + "&userAccountId="+userAccountId,
		callback:queryPage
	});
}

//转到修改页面
function initModify(id, userAccountId, userName) {
	mainFrame.addTab({
		id:"oa_addrlist_modify_" + userAccountId,
		title:userName,
		url:BASE_PATH + "/m/addrlist?act=initmodify&id=" + id + "&userAccountId=" + userAccountId,
		callback:queryPage
	});
}

//导出成CSV文件用于foxmail导入
function exportAsCSV(){
	var pageno = "1";
	var pagecount = "0";
	var userDeptId = $("#deptName").val();
	var userAccountId = $.trim($("#userAccountId").val());//用户帐号id
	var promptStr = "";
	
	var users = getSelectedUsers();
	var selectedNames = "";
	var selectedAccountIds = "";
	if(users && users[0] && users[1]){
		selectedAccountIds = users[0];
		selectedNames = users[1];
		promptStr = "帐户：" + selectedNames;
	} else {
		if(userDeptId == "" && userAccountId == ""){
			promptStr += "所有";
		} else {
			if(userDeptId != ""){
				promptStr += "上级部门:“" + userDeptId + "”";
			}
			if(userAccountId != ""){
				if(promptStr != ""){
					promptStr += ",";
				}
				promptStr += "帐户姓名或帐户id含:“" + userAccountId + "”字符";
			}
		}
	}
	
	$.confirm("正要导出符合以下条件的通讯录数据：\n" + promptStr + "\n\n是否继续？", function(r){
		if (r) {
			$.ajax({
		        type : "POST",
				url  : BASE_PATH + "m/addrlist",
				data : "act=export&pageno="+pageno+"&pagecount="+pagecount+"&userDeptName="+userDeptId
			            +"&userAccountId="+userAccountId+"&selectedAccountIds=" + selectedAccountIds, 
		        success : function(xml){
		            //解析XML中的返回代码
		            var message = $("message",xml);
		            if(message.attr("code") == "1"){
		            	$.openDownloadDialog(BASE_PATH + $("message",xml).text());
		            }
		            else{
		               $.alert($("message",xml).text());
		            };
		        },
		        error : $.ermpAjaxError
		    });
		}
	});
}

//取得选中的用户
function getSelectedUsers(){
	var strSelectedNames = "";//选中的姓名串
	var strSelectedIds = "";//选中的id串	
	
	$("input[name='_doc_chk'][checked]", "#addrListList").each(function(){
		if( this.checked == true ){
			var name_ = $(this).parent().next().next().html();
			var accountId_ = $(this).parent().next().html();
			if($.trim(name_) != ""){
				if(strSelectedNames != ""){
					strSelectedNames += ",";
				}
				strSelectedNames += "“" + name_ + "”";
			}
			
			if($.trim(accountId_) != ""){
				if(strSelectedIds != ""){
					strSelectedIds += ",";
				}
				strSelectedIds += accountId_;
			}
		}
	});
	return new Array(strSelectedIds, strSelectedNames);
}

//左右模块拖拉,如果鼠标移动太快的话，最小宽度会有偏差；
function MouseDownToResize(e,obj){ 
	e = e || event;
	obj.mouseDownX=e.clientX; 	
	obj.pareneTdW=obj.parentElement.offsetWidth; 
	obj.pareneTableW=theObjTable.offsetWidth; 
	obj.setCapture(); 
} 
function MouseMoveToResize(e,obj){ 
	e = e || event;
	if(!obj.mouseDownX) return false; 
		var newWidth=obj.pareneTdW + e.clientX-obj.mouseDownX; 
	if(newWidth>=185) 
	{ 
		obj.parentElement.style.width = newWidth; 
		theObjTable.style.width="100%"; 
	} else{return}
} 
function MouseUpToResize(e,obj){ 
	obj.releaseCapture(); 
	obj.mouseDownX=0; 
} 