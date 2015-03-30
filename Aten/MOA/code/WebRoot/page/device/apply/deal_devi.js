//离职设备处理单--待办
var mainFrame = $.getMainFrame();
var columnSortObj;//表头列排序对像
var deviceFormTypeSel;
var deviceTypeSel;
$(initToDoEquipmentScrap);

function initToDoEquipmentScrap() {
	//初始化表头列排序
  columnSortObj = $("#tableTH").columnSort();
  initDeviceFormTypeSel();
  initDeviceTypeSel();
  //查询
	$("#queryDevice").click(function () {
		gotoPage(1);
	});
   //打开用户帐号
	$("#openUserSelect").click(
	   	function(e){
			var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(user){
				if (user != null) {
					$("#applicant").val(user.id);
				$("#applicantName").val(user.name);
				}
			});
			dialog.openDialog("single");
		}
	);
  gotoPage(1);
   $.EnterKeyPress($("#formNO"),$("#queryDevice"));
}
/**
 * 初始化设备类型下拉列表
 * @param {} deviceTypeCode 设备类型代码
 */
function initDeviceTypeSel() {
	deviceTypeSel = $("#deviceTypeSel").ySelect({
		width : 80, 
		url:BASE_PATH+"m/data_dict?act=selectdevtype",
		afterLoad : function() {
			deviceTypeSel.addOption("", "所有....", 0);
			deviceTypeSel.select(0);
		}
	});     
}
function initDeviceFormTypeSel() {
	deviceFormTypeSel = $("#deviceFormTypeSel").ySelect({width:80, url:"m/dev_deal?act=getformtypesel",
		afterLoad:function(){
			deviceFormTypeSel.addOption("", "所有....", 0);
			deviceFormTypeSel.select(0);
		}
	});
}
/**
 * 分页
 **/
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
    $("#hidNumPage").val(pageNo);
    queryList();
};

/**
 * 查询列表信息
 */
function queryList() {
	$("#FormList > tbody").empty();
	//分页
	var sortCol = columnSortObj.getSortColumn();
	var ascend = columnSortObj.getAscend();
  	var pageno = $.trim($("#hidNumPage").val());
  	var pagecount = $.trim($("#hidPageCount").val());
	
	var applicant = $.trim($("#applicant").val());
	var formNO = $.trim($("#formNO").val());
	var formType = deviceFormTypeSel.getValue();
    var startApplyTime = $.trim($("#startApplyTime").val());
    var endApplyTime = $.trim($("#endApplyTime").val());
  	var deviceType=deviceTypeSel.getValue();
    if ( $.compareDate(startApplyTime, endApplyTime)) {
		$.alert("申请开始日期不能大于结束日期！");
		return false;
	}

	$.ajax({type:"POST",
		 cache:false, 
		 url:"m/dev_deal", 
		 data:{
			 act:"query",
			 applicant : applicant,
			 formNO : formNO,
			 formType : formType,
			 deviceType:deviceType,
			 startApplyTime : startApplyTime,
			 endApplyTime:endApplyTime,
			 sortCol : sortCol,
			 ascend : ascend,
			 pageNo : pageno,
			 pageSize : pagecount
		 }, 
		 success:function (xml) {
            //解析XML中的返回代码
		var message = $("message", xml);
		if (message.attr("code") == "1") {
			var tBodyHTML = "";
			$("device-flow", xml).each(function (index) {
				var deviceFlow = $(this);
				tBodyHTML += createTR($.trim(deviceFlow.attr("id")), 
							 $.trim($("form-code", deviceFlow).text()), 
							 $.trim($("node-name", deviceFlow).text()), 
							 $.trim($("form-name", deviceFlow).text()),
							 $.trim($("device-typeName", deviceFlow).text()),
							 $.trim($("applicant-name", deviceFlow).text()),
							 $.trim($("applicant-group", deviceFlow).text()),
							 $.trim($("apply-date", deviceFlow).text()),
							 $.trim($("form-type", deviceFlow).text()),
							 $("task-id",deviceFlow).text(),
							 $("taskinstance-id",deviceFlow).text()) 
			});
			
			$("#FormList > tbody").html(tBodyHTML).find("td:empty").html("&nbsp;");   
			
	        //------------------翻页数据--------------------------
            $(".pageNext").html($.createTurnPage(xml));
            $.EnterKeyPress($("#numPage"),$("#numPage").next());
            
		} else {
			$.alert($("message", xml).text());
		}
	}, error:$.ermpAjaxError});
}

/**
 * HTML 构造器
 */
function createTR(formID,formCode, nodeName, formTypeName,deviceTypeName,userID, groupName ,aplyDate,formType,taskid, tiid) {
	var html = "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
	 + "<td>" + formCode+ "</td>" 
	 + "<td>" + formTypeName + "</td>" 
	 + "<td>" + deviceTypeName + "</td>" 
	 + "<td>" + userID + "</td>" 
	 + "<td>" + groupName + "</td>"
	 + "<td>" + aplyDate + "</td>"
	var op =  "";
		op += ( $.hasRight($.OaConstants.DISPOSE)?"<a href=\"javascript:void(0);\" onclick=\"openDetail('"+ taskid +"','"+tiid+"','"+ formType +"','"+ nodeName +"')\"  class=\"opLink\">处理</a> | ":"");
	html += "<td>" + op.substring(0, op.lastIndexOf(" | ")) + " </td> </tr>";
	return html;
}

//办理
function openDetail(taskid, tiid,formType,nodeName){
	var formName="";
	if(formType=="3"){
		formName="设备报废单审批"
	}else if(formType=="0"){
		formName="设备领用单审批"
	}else if(formType=="1"){
		formName="设备申购单审批"
	}else if(formType=="2"){
		formName="设备调拨单审批"
	}else if(formType=="4"){
		formName="设备离职处理单审批"
	}
	//设备报废审批
	mainFrame.addTab({
		id:"oa_equipmentHandle"+taskid,
		title:formName,
		url:BASE_PATH +"m/dev_deal?act=dispose&taskid="+taskid+"&tiid=" + tiid+"&formType="+formType,
		callback:queryList
	});
}





