var mainFrame = $.getMainFrame();
var trunPageObj;//翻页组件对像

$(initIngerFaceLogList);

function initIngerFaceLogList(){
	//mainFrame.getCurrentTab().setTitle("列表-系统日志", "系统动作日志查询列表");
//    $.handleRights({
//            "searchLog" : $.SysConstants.QUERY,
//            "exportLog" : $.SysConstants.EXPORT
//        }
//    );
    var d=new Date(); 
    var year = d.getFullYear();
    var month = d.getMonth() + 1;
    var date = d.getDate();
	var curDate=year+ (month < 10 ? "-0" : "-") + month + (date < 10 ? "-0" : "-") + date;
	$("#endtime").val(curDate);
    if (month == 1) {
    	//如果是1月，不能跨年查询，从1月1日查起
    	date = 1;
    } else {
    	month = month - 1;
    }
    curDate=year+ (month < 10 ? "-0" : "-") + month + (date < 10 ? "-0" : "-") + date;
    $("#begintime").val(curDate);

   
    $("#searchLog").click(function(){
        trunPageObj.gotoPage(1);//搜索第一页
    });
    
    $("#exportLog").click(
        function(){
            if(!checkFormArguments()){
                return;
            };
            $(this).attr("disabled","true");
            $.ajax({
                type : "POST",
                cache: false,
                url  : "m/login_log/export",
                dataType : "json",
                data: {
					pageNo: 1,
					pageSize : 0,
					accountID:$("#accountid").val(),
					accountName:$("#accountname").val(),
					beginTime:$("#begintime").val(),
					endTime:$("#endtime").val()
				},
                success : function(data){
                	if ($.checkErrorMsg(data) ) {
                		window.open(BASE_PATH + data.msg.text);
                	}
                    $("#exportLog").removeAttr("disabled");
                }
            });
        }
    );
    
    //回车搜索
    $.EnterKeyPress($("input[name=accountname]"),$("#searchLog"));
    $.EnterKeyPress($("input[name=accountid]"),$("#searchLog"));
    
    //初始化翻页组件
    trunPageObj =  $(".pageNext").turnPage();
    trunPageObj.gotoPage(1);//搜索第一页
};


//提交前的检查
function checkFormArguments(){
    $("input[name=accountname]").val($.trim($("input[name=accountname]").val()));
    $("input[name=accountid]").val($.trim($("input[name=accountid]").val()));  
	if($("input[name=begintime]").val() == "" || $("input[name=endtime]").val() == ""){
		$.alert("查询时间不能为空！");
		return false;
	};

	if(Date.parse($("input[name=begintime]").val()) - Date.parse($("input[name=endtime]").val()) > 0){
		$.alert("开始时间不能大于结束时间！");
		return false;
	};
	
	return true;
};

function queryList(){
	if(!checkFormArguments()){
	    return;
	};
	
	$(this).attr("disabled","true");
	 //分页
	var pageNo=trunPageObj.getCurrentPageNo();//当前第几页
	var pageCount=trunPageObj.getPageCount();//一页多少条

	$.ajax({
	    type : "POST",
	    cache: false,
	    async : true,
	    url  : "m/login_log/query",
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
				var fileList = "";
				if(data.loginLogPage && data.loginLogPage.dataList){
					var dataList = data.loginLogPage.dataList;
					$(dataList).each(function(i) {
						fileList += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" ;
						fileList +=  "<td>" + (i + 1) + "</td>";
						fileList += "<td>" + dataList[i].accountID + "</td>";
						fileList += "<td>" + dataList[i].accountName + "</td>";
						fileList += "<td>" + dataList[i].ipAddress + "</td>";
						fileList += "<td>" + (dataList[i].isSuccess ? "成功" : "失败") + "</td>";
						fileList += "<td>" + (dataList[i].loginInfo ? dataList[i].loginInfo : "") + "</td>";
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