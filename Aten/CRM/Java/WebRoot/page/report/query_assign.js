var mainFrame = $.getMainFrame();
var groupSelect = null;
var trunPageObj;//翻页组件对像
var rptID;
$(initAddrListListPage);

//初始化页面
function initAddrListListPage(){
	rptID = $("#rptId").val();
    //搜索
    $("#searchAddrListList").click(
        function(){
        	trunPageObj.gotoPage(1);//搜索第一页
        }
    );   
	
	//刷新
  	$("#refreshAddrList").click(function(){
  		$("#data_list").html("");
  		queryPage();
  	});
  	
  	//手工输入姓名进行搜索时,按回车直接搜索
  	$.EnterKeyPress($("#userAccountId"),$("#searchAddrListList"));

	
	//初始化机构列表
	var url =ERMP_PATH + "m/rbac_group/groupselect?jsoncallback=?";
	$.getJSON(url,function(data){
		$("#groupIdDiv").html(data.htmlValue);
		groupSelect = $("#groupIdDiv").ySelect({width:100});
		groupSelect.addOption("", "所有...", 0);
		groupSelect.select(0);
		trunPageObj.gotoPage(1);//搜索第一页
	});
  	
	//初始化翻页组件
    trunPageObj =  $(".pageNext").turnPage();
	
}

function queryList() {	
	//设置查询按钮
	$("#searchAddrListList").attr("disabled","true");
	//分页
	var pageNo=trunPageObj.getCurrentPageNo();//当前第几页
	var pageCount=trunPageObj.getPageCount();//一页多少条
	var userDeptId = groupSelect.getValue() ? groupSelect.getDisplayValue() : "";
	var userAccountId = $.trim($("#userAccountId").val());//用户帐号id
	
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : BASE_PATH + "/m/report/query_assign",
	   data: {
			pageNo: pageNo,
			pageSize : pageCount,
			rptID : rptID,
			userDeptName : userDeptId,
			userAccountId : userAccountId
		},
		dataType : "json",
		success : function(data){
			if ($.checkErrorMsg(data) ) {
				var fileList = "";
				if(data.assignInfoPage && data.assignInfoPage.dataList){
					var dataList = data.assignInfoPage.dataList;
					$(dataList).each(function(i) {
						var id = dataList[i].user.accountID;
						var assignValue = dataList[i].assignValue;
						fileList += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" ;
						fileList +=  "<td>" + id + "</td>";
						fileList += "<td>" + dataList[i].user.displayName + "</td>";
						fileList += "<td>" + assignValue+ "</td>";
						fileList += "<td><a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initAssign('" + id + "','" + assignValue + "')\">授权</a> ";
						fileList += "</td></tr>";
					});
				}
				
				$("#data_list").html(fileList);
				trunPageObj.setPageData(data.assignInfoPage);
			}
	   		$("#searchAddrListList").removeAttr("disabled");
	   }
	});
}

//弹出授权页面
function initAssign(userAccountId, assignValue) {
    var selector = new DeptDialog(ERMP_PATH, BASE_PATH);
//			var selectedIds = $("#deptids").text().split(",");
	var selectedNames = assignValue.split(",");
	for (var i=0 ; i<selectedNames.length ; i++){
		if ($.trim(selectedNames[i]) != ""){
			selector.appendSelected({id:"", name:selectedNames[i]});
		}
	}
	
	selector.setCallbackFun(function(retVal){
		if (retVal != null) {
//					var ids = "";
			var names = "";
			for (var i=0 ; i<retVal.length ; i++){
//						ids += retVal[i].id + ",";
				names += retVal[i].name + ",";
			}
			saveAssign(userAccountId, names);
		}
	});
	selector.openDialog("multi");
}

//保存授权信息
function saveAssign(userAccountId, names) {
	$.ajax({
	    type : "POST",
	    cache: false,
	    async : true,
	    dataType : "json",
	    url  : "m/report/add_assign",
	    data :{
	    	rptID : rptID,
	    	userAccountId:userAccountId,
	    	groupNameStr:names
	    	},
	    success : function(data){
	    	if ($.checkErrorMsg(data) ) {
					$.alert("保存授权成功！");
	                queryList();
				}
	    }
	});
}