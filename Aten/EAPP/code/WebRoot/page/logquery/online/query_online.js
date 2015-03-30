var mainFrame = $.getMainFrame();
var trunPageObj;//翻页组件对像

$(initIngerFaceLogList);

function initIngerFaceLogList(){
    $("#searchLog").click(function(){
        trunPageObj.gotoPage(1);//搜索第一页
    });
    
    //回车搜索
    $.EnterKeyPress($("input[name=accountname]"),$("#searchLog"));
    $.EnterKeyPress($("input[name=accountid]"),$("#searchLog"));
    
    //初始化翻页组件
    trunPageObj =  $(".pageNext").turnPage();
    trunPageObj.gotoPage(1);//搜索第一页
};

function queryList(){
	$(this).attr("disabled","true");
	 //分页
	var pageNo=trunPageObj.getCurrentPageNo();//当前第几页
	var pageCount=trunPageObj.getPageCount();//一页多少条

	$.ajax({
	    type : "POST",
	    cache: false,
	    async : true,
	    url  : "m/online_user/query",
	    dataType : "json",
	    data: {
			pageNo: pageNo,
			pageSize : pageCount,
			accountID:$("#accountid").val(),
			accountName:$("#accountname").val(),
			beginTime:$("#begintime").val(),
			endTime:$("#endtime").val()
		},
	    success : function(data){
	        if ($.checkErrorMsg(data) ) {
	        	$("#onlineCount").html(data.totalOnlineCount);
	        	
				var fileList = "";
				if(data.loginLogPage && data.loginLogPage.dataList){
					var dataList = data.loginLogPage.dataList;
					$(dataList).each(function(i) {
						fileList += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" ;
						fileList +=  "<td>" + (i + 1) + "</td>";
						fileList += "<td>" + dataList[i].accountID + "</td>";
						fileList += "<td>" + dataList[i].accountName + "</td>";
						fileList += "<td>" + dataList[i].ipAddress + "</td>";
						fileList += "<td>" + (dataList[i].loginTime) + "</td>";
                        fileList += "</tr>";
					});
				}
				
				$("#data_list").html(fileList);
				trunPageObj.setPageData(data.loginLogPage);
			}
	        $("#searchLog").removeAttr("disabled");
	    }
	});
}