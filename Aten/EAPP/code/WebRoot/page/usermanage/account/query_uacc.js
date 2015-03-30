var mainFrame = $.getMainFrame();
var trunPageObj;//翻页组件对像
var islockSel;
var isvalidSel;
$(initUserAccount);

//初始化页面
function initUserAccount(){
	//mainFrame.getCurrentTab().setTitle("列表-用户帐号", "用户帐号查询列表");
//    $.handleRights({
//        "addUserAcc" : $.SysConstants.ADD,
//        "delAllAcc" : $.SysConstants.DELETE,
//        "searchUserAcc" : $.SysConstants.QUERY
//    });
//    
    
    islockSel = $("#islockList").ySelect({width: 40});
    isvalidSel = $("#isvalidList").ySelect({width: 40});

	$("#delAllAcc").attr("disabled","true").addClass("icoNone");

	 //部门
    $('#showGroup').click(function(){
    	var selector = new DeptDialog(BASE_PATH);
    	selector.setCallbackFun(function(retVal){
			if (retVal != null) {
				$("#groupid").val(retVal.id);
				$("#groupname").val(retVal.name);
			}
		});
		selector.openDialog("single");
		
    });     
    
	/*新增-用户帐号*/
	$("#addUserAcc").click(
		function(e){
			mainFrame.addTab({
				id:"ermp_user_account_"+Math.floor(Math.random() * 1000000),
				title:"新增用户帐号",
				url:"m/user_account/initframe?action=add",
				callback:queryList
			});
		}
	);
	/*批量删除*/
	$("#delAllAcc").click(
		function(e){       
		    if ($("input:checkbox:checked","tbody").length==0){
		       $.alert('请选择要删除的用户帐号!');	       
		    }
		    else{
		    	$.confirm("是否删除所选的所有用户帐号?", function(r){
					if (r) {
						var accountIdList="";
						$("input:checkbox:checked","tbody").each(function(e){
							accountIdList+="&accountIDs="+$(this).attr("name");
						});
						$.ajax({
							type : "POST",
							cache: false,
							url  : "m/user_account/delete",
							dataType : "json",
							data : accountIdList,
							success : function(data) {
								if ($.checkErrorMsg(data) ) {
//									$.alert("删除用户帐号成功!");
								    queryList();
								}
							}
						});
					}
				});
			}
        }			
	);		
    /*刷新*/
    $("#refresh").click(
        function(){
            $("#data_list").html("");
	    	queryList();
        }
    );
    /*查询*/	
	$("#searchUserAcc").click(function(){
	   trunPageObj.gotoPage(1);//搜索第一页
	}); 
	//回车搜索
	$.EnterKeyPress($("#keyword"),$("#searchUserAcc"));
	//初始化翻页组件
    trunPageObj =  $(".pageNext").turnPage();
	trunPageObj.gotoPage(1);//搜索第一页
}

//全选 
function checkAll(){
	$("tbody > tr").each(function(){
		$("input").attr("checked",$("#checkbox")[0].checked);
	});
    if ($("input:checkbox:checked","tbody").length==0){
        $("#delAllAcc").attr("disabled","true").addClass("icoNone");
    } else{
       $("#delAllAcc").removeAttr("disabled").removeClass("icoNone");
    }	
}
//选择单个
function checkSingle(){
    if ($("input:checkbox:checked","tbody").length==0){
        $("#checkbox")[0].checked=false;
        $("#delAllAcc").attr("disabled","true").addClass("icoNone");
    } else{
       $("#delAllAcc").removeAttr("disabled").removeClass("icoNone");
    }	
}

function modifyUserAcc(accountId){
	mainFrame.addTab({
		id:"ermp_user_account_"+ accountId,
		title:"修改用户帐号",
		url:"m/user_account/initframe?action=modify&accountId="+encodeURIComponent(accountId),
		callback:queryList
	});
}

function deleteUserAcc(accountId){
	$.confirm("是否删除该用户?", function(r){
		if (r) {
			if(!accountId || $.trim(accountId) == ""){
				$.alert("参数传入出错！");
				return;
			}
			$.ajax({
				type : "POST",
				cache: false,
				url  : "m/user_account/delete",
				dataType : "json",
				data : "accountIDs=" + accountId,
				success : function(data){
		        	if ($.checkErrorMsg(data) ) {
//		        		$.alert("删除成功！");
		                //重新加载数据
		                queryList();
		        	}
		        }
			});
		}
	});
}

function queryList(){
	//设置查询按钮
	$("#searchUserAcc").attr("disabled","true");
    //设置全选框
    $("#checkbox")[0].checked=false;
    //设置批量删除按钮
    $("#delAllAcc").attr("disabled","true").addClass("icoNone");
	
     //分页
	var pageNo=trunPageObj.getCurrentPageNo();//当前第几页
	var pageCount=trunPageObj.getPageCount();//一页多少条
	
	var groupid=$("#groupid").val();
	var keyword=$.trim($("#keyword").val());
	var islock=$.trim(islockSel.getValue());
	var isvalid=$.trim(isvalidSel.getValue());
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/user_account/query",
	   dataType : "json",
	   data: {
			pageNo: pageNo,
			pageSize : pageCount,
			groupID : groupid,
			keyword : keyword,
			isLock : islock,
			isValid : isvalid
		},         
	   success : function (data){
	   		if ($.checkErrorMsg(data) ) {
		   		var fileList = "";
				if(data.userAccountPage && data.userAccountPage.dataList){
					var dataList = data.userAccountPage.dataList;
					$(dataList).each(function(i) {
						fileList += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" ;
						fileList += "<td>" + (i + 1) + "</td>";
						fileList += "<td> <input id=\"checkbox"+(i + 1)+"\" type=\"checkbox\" name=\""+dataList[i].accountID+"\" onclick=\"checkSingle();\"/></td>";
						fileList += "<td class='titLeft'>"+dataList[i].accountID+"</td>";
						fileList += "<td>" + dataList[i].displayName + "</td>";
						fileList += "<td>" + (dataList[i].isLock ? "锁定" : "未锁定") + "</td>";
						fileList += "<td>" + (dataList[i].changePasswordFlag =="Y" ? "登录时" : "不约束") + "</td>";
						fileList += "<td>" + dataList[i].createDate + "</td>";
						fileList += "<td>" + (dataList[i].invalidDate ?dataList[i].invalidDate : "") + "</td>";

					
	                    //操作
						var op = $.wrapActionRight($.SysConstants.VIEW,"<a class=\"opLink\" href=\"javascript:viewUserAcc('" + dataList[i].accountID + "');void(0);\">详情</a>&nbsp;|&nbsp;")
		                    + $.wrapActionRight($.SysConstants.MODIFY,"<a class=\"opLink\" href=\"javascript:modifyUserAcc('"+dataList[i].accountID+"');void(0);\">修改</a>&nbsp;|&nbsp;") 
		                    + $.wrapActionRight($.SysConstants.DELETE,"<a class=\"opLink\" href=\"javascript:deleteUserAcc('"+dataList[i].accountID+"');\">删除</a>&nbsp;|&nbsp;") 
		                    + $.wrapActionRight($.SysConstants.SET_DEFAULT,"<a class=\"opLink\" href=\"javascript:setPortlet('"+dataList[i].accountID+"');\">默认门户</a>&nbsp;|&nbsp;") ;
                        fileList += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td></tr>";
					});
				}
				$("#data_list").html(fileList);
				trunPageObj.setPageData(data.userAccountPage);
		   	}
			
	   		$("#searchUserAcc").removeAttr("disabled");
	   }
	});
}

function viewUserAcc(accountId){
	mainFrame.addTab({
		id:"ermp_user_account_" + accountId,
		title:"查看用户帐号",
		url:"m/user_account/initframe?action=view&accountId="+encodeURIComponent(accountId),
		callback:queryList
	});
}

function setPortlet(accountId) {
	mainFrame.addTab({
		id:"ermp_user_account_portlet_" + accountId,
		title:"设置默认门户",
		url:"m/user_account/initsetportlet?accountId="+encodeURIComponent(accountId)
	});
}