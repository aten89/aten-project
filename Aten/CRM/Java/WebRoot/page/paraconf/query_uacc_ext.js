var mainFrame = $.getMainFrame();
var trunPageObj;//翻页组件对像
$(initUserAccount);

//初始化页面
function initUserAccount(){
	//添加权限约束
	$.handleRights({
        "editAllAcc" : $.SysConstants.MODIFY
    },"hidModuleRights");
    
	$("#editAllAcc").attr("disabled","true").addClass("icoNone");

	/*批量编辑*/
	$("#editAllAcc").click(function() {
		if ($("input:checkbox:checked","tbody").length==0){
		    $.alert('请选择要批量修改“客服人员”的用户帐号!');	       
	    } else {
	    	var selector = new UserDialog(ERMP_PATH, BASE_PATH);
			selector.setCallbackFun(function(retVal){
				if (retVal != null) {
					modifyServiceAccountID(retVal.id);
					queryList();
				}
			});
			selector.openDialog("single");
		}
	});
	
    /*刷新*/	
	$("#refresh").click(function(){
	   trunPageObj.gotoPage(1);//搜索第一页
	}); 
	
	$("#saleDeportList").ySelect({width:117,json:true, url:"m/group_ext/dict_sale"});
	
	$("#searchUserAcc").click(function(){
		   trunPageObj.gotoPage(1);
	}); 
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
        $("#editAllAcc").attr("disabled","true").addClass("icoNone");
    } else{
       $("#editAllAcc").removeAttr("disabled").removeClass("icoNone");
    }	
}
//选择单个
function checkSingle(){
    if ($("input:checkbox:checked","tbody").length==0){
        $("#checkbox")[0].checked=false;
        $("#editAllAcc").attr("disabled","true").addClass("icoNone");
    } else{
       $("#editAllAcc").removeAttr("disabled").removeClass("icoNone");
    }
}

function modifyServiceAccountID(serviceAccountID) {
	var accountIdList="";
	$("input:checkbox:checked","tbody").each(function(e){
		accountIdList+="&accountIDs="+$(this).attr("name");
	});
	
	accountIdList +="&serviceAccountID="+serviceAccountID;
	
	$.ajax({
		type : "POST",
		cache: false,
		url  : "m/user_account_ext/modify",
		dataType : "json",
		data : accountIdList,
		success : function(data) {
			if ($.checkErrorMsg(data) ) {
			    queryList();
			}
		}
	});
}

function queryList(){
    //设置全选框
    $("#checkbox")[0].checked=false;
    //设置批量编辑按钮
    $("#editAllAcc").attr("disabled","true").addClass("icoNone");
	
    var saleDeptID = $('#saleDeportList').val();
    
     //分页
	var pageNo=trunPageObj.getCurrentPageNo();//当前第几页
	var pageCount=trunPageObj.getPageCount();//一页多少条
	
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/user_account_ext/query",
	   dataType : "json",
	   data: {
			saleDeptID: saleDeptID,
			pageNo: pageNo,
			pageSize : pageCount
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
						fileList += "<td>" + (dataList[i].serviceAccountId ?dataList[i].serviceAccountId : "") + "</td>";
						fileList += "<td>" + dataList[i].serviceAccountName + "</td></tr>";
					});
				}
				$("#data_list").html(fileList);
				trunPageObj.setPageData(data.userAccountPage);
		   	}
	   }
	});
}