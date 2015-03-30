var mainFrame = $.getMainFrame();
var trunPageObj;//翻页组件对像

var sysSelect;
var actionSelect;
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
    
    sysSelect = $("#subSystemList").ySelect({width: 110,json:true,url : "m/subsystem/allsystem",onChange : subSystemList_change,afterLoad:addSysOption});
   	function addSysOption() {
		sysSelect.addOption("", "所有系统", 0);
	}
   actionSelect =	$("#actionList").ySelect({width: 80,isdisabled:true});
   
   $("#openModuleBtn").click(function(){
        openModuleList();
    });
    
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
                url  : "m/action_log/export",
                dataType : "json",
                data: {
					pageNo: 1,
					pageSize : 0,
					systemID: sysSelect.getValue(),
					moduleKey:$("#hidModuleKey").val(),
					actionKey:actionSelect ? actionSelect.getValue(): "",
					accountID:$("#accountid").val(),
					accountName:$("#accountname").val(),
					beginTime:$("#begintime").val(),
					endTime:$("#endtime").val(),
					serviceLog: false
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

function subSystemList_change(value){
    //清空模块数据
    $("#moduleName").val("");
    $("#hidModuleId").val("");
    $("#hidModuleKey").val("");
    //清空动作数据
    actionSelect.setValue("","请选择...").disable(true);
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

function viewLog(logid){
    mainFrame.addTab({
    	id:"ermp_action_log_" + logid,
    	title:"查看系统日志",
    	url:"m/action_log/view?logID=" + logid
    });
};

function openModuleList(){
	var systemID = sysSelect.getValue();
    if(!systemID){
        $.alert("请先选择子系统！");
        return false;
    };
    var selector = new ModuleDialog(BASE_PATH);
	selector.setSubSystemId(systemID);
	selector.setCallbackFun(function(retVal){
			if (retVal) {
				$("#hidModuleId").val(retVal.id);
				$("#moduleName").val(retVal.name);
		        $("#hidModuleKey").val(retVal.key);
				if (retVal.id) {
					 actionSelect = $("#actionList").ySelect({
		                    width: 80,
		                    json:true,
		                    url: "m/action/actions?moduleID=" + retVal.id,
		                    afterLoad:addAcOption
		                });
				} else {//清除
					actionSelect.setValue("","请选择...").disable(true);
				}
			}
		});
	selector.openDialog("single");
    return true;
};

//在动作选择列表中添加选项
function addAcOption() {
	actionSelect.addOption("", "所有动作", 0);
}

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
	    url  : "m/action_log/query",
	    dataType : "json",
	    data: {
			pageNo: pageNo,
			pageSize : pageCount,
			systemID: sysSelect.getValue(),
			moduleKey:$("#hidModuleKey").val(),
			actionKey:actionSelect ? actionSelect.getValue(): "",
			accountID:$("#accountid").val(),
			accountName:$("#accountname").val(),
			beginTime:$("#begintime").val(),
			endTime:$("#endtime").val(),
			serviceLog: false
		},
	    success : function(data){
	        if ($.checkErrorMsg(data) ) {
				var fileList = "";
				if(data.actionLogPage && data.actionLogPage.dataList){
					var dataList = data.actionLogPage.dataList;
					$(dataList).each(function(i) {
						fileList += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" ;
						fileList +=  "<td>" + (i + 1) + "</td>";
						fileList += "<td>" + dataList[i].systemName + "</td>";
						fileList += "<td>" + (dataList[i].moduleName ? dataList[i].moduleName : dataList[i].moduleKey) + "</td>";
						fileList += "<td>" + (dataList[i].actionName ? dataList[i].actionName : dataList[i].actionKey) + "</td>";
						fileList += "<td>" + dataList[i].accountID + "</td>";
						fileList += "<td>" + dataList[i].accountName + "</td>";
						fileList += "<td>" + (dataList[i].ipAddress ? dataList[i].ipAddress : "") + "</td>";
						fileList += "<td>" + (dataList[i].operateTime) + "</td>";
	                    
	                    //操作
						var op = $.wrapActionRight($.SysConstants.VIEW, "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"viewLog('" + dataList[i].logID + "');\">详情</a>");
                        fileList += "<td>" + op + "</td></tr>";
					});
				}
				
				$("#data_list").html(fileList);
				trunPageObj.setPageData(data.actionLogPage);
			}
	        $("#searchLog").removeAttr("disabled");
	    }
	});
}