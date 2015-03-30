//离职设备处理--归档
var mainFrame = $.getMainFrame();
var columnSortObj;//表头列排序对像
var deviceFormTypeSel;
var deviceTypeSel;
$(initArchivedEquipmentInfoLeaveDeal);

function initArchivedEquipmentInfoLeaveDeal() {
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
	$("#DeviceList > tbody").empty();
	//分页
	var sortCol = columnSortObj.getSortColumn();
	var ascend = columnSortObj.getAscend();
  	var pageno = $.trim($("#hidNumPage").val());
  	var pagecount = $.trim($("#hidPageCount").val());
	
	var applicant = $.trim($("#applicant").val());
	var formNO = $.trim($("#formNO").val());
	var formType = deviceFormTypeSel.getValue();
    var startArchTime = $.trim($("#startArchTime").val());
    var endArchTime = $.trim($("#endArchTime").val());
    var deviceType=deviceTypeSel.getValue();
    if ( $.compareDate(startArchTime, endArchTime)) {
		$.alert("申请开始日期不能大于结束日期！");
		return false;
	}

	$.ajax({type:"POST",
		 cache:false, 
		 url:"m/dev_arch", 
		 data:{
			 act:"query",
			 applicant : applicant,
			 formNO : formNO,
			 formType : formType,
			 deviceType:deviceType,
			 startArchTime : startArchTime,
			 endArchTime:endArchTime,
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
							 $.trim($("form-name", deviceFlow).text()),
							 $.trim($("device-typeName", deviceFlow).text()),
							 $.trim($("applicant-name", deviceFlow).text()),
							 $.trim($("applicant-group", deviceFlow).text()),
							 $.trim($("apply-date", deviceFlow).text()),
							 $.trim($("form-type", deviceFlow).text()),
							 $.trim($("archive-date", deviceFlow).text()),
							 $.trim($("form-status", deviceFlow).text())) 
			});
			
			$("#DeviceList > tbody").html(tBodyHTML).find("td:empty").html("&nbsp;");   
			
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
function createTR(formID,formCode, formTypeName,deviceTypeName,userID, groupName ,aplyDate,formType,archiveDate,formStatus) {
	var html = "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
	 + "<td>" + formCode+"</td>" 
	 + "<td>" + formTypeName + "</td>" 
	 + "<td>" + deviceTypeName + "</td>" 
	 + "<td>" + userID + "</td>" 
	 + "<td>" + groupName + "</td>"
	 + "<td>" + aplyDate + "</td>"
	 + "<td>" + archiveDate + "</td>"
	 + "<td>" + formStatus + "</td>"
	var op =  "";
		op += ( $.hasRight($.SysConstants.VIEW)?"<a href=\"javascript:void(0);\" onclick=\"openDetail('"+ formID +"','"+ formType +"','"+formCode+"')\"  class=\"opLink\">查看</a> | ":"");
	html += "<td>" + op.substring(0, op.lastIndexOf(" | ")) + " </td> </tr>";
	return html;
}

//查看
function openDetail(formID,formType,formCode){
	var formName="";
	if(formType=="3"){
		formName="设备报废单"
	} else if(formType=="0"){
		formName="设备领用单"
	} else if(formType=="1"){
		formName="设备申购单"
	} else if(formType=="2"){
		formName="设备调拨单"
	} else if(formType=="4"){
		formName="设备离职处理单"
	}
	mainFrame.addTab({
		id:"oa_equipmentScrapArchHandle"+formType+formID,
		title:formName+formCode,
		url:BASE_PATH +"m/dev_arch?act=view&formId=" + formID+"&formType="+formType,
		callback:queryList
	});
}

//设备报废审批详情
function openScrapDetail(formID, formName){
	
}





