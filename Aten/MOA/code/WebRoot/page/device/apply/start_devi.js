//设备领用申购--起草
var mainFrame = $.getMainFrame();
var columnSortObj;//表头列排序对像
var deviceFormTypeSel;
$(initDraftEquipmentInfoList);

function initDraftEquipmentInfoList() {
	//初始化表头列排序
  columnSortObj = $("#tableTH").columnSort();
  initDeviceFormTypeSel();
  //查询
	$("#queryDevice").click(function () {
		gotoPage(1);
	});
  gotoPage(1);
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
	var formType = deviceFormTypeSel.getValue();
	$.ajax({type:"POST",
		 cache:false, 
		 url:"m/dev_start", 
		 data:{
			 act:"query",
			 formType : formType,
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
							 $.trim($("form-name", deviceFlow).text()),
							  $.trim($("device-typeName", deviceFlow).text()),
							 $.trim($("apply-date", deviceFlow).text()),
							 $.trim($("form-type", deviceFlow).text())) 
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
function createTR(formID,formTypeName,deviceTypeName,applyDate,formType) {
	var html = "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
	 + "<td>" + formTypeName + "</td>" 
	 + "<td>" + deviceTypeName + "</td>" 
	 + "<td>" + applyDate + "</td>"
	var op =  "";
		op += ( $.hasRight($.SysConstants.MODIFY)?"<a href=\"javascript:void(0);\" onclick=\"initModifyForm('"+ formID +"','"+ formType +"')\"  class=\"opLink\">修改</a> | ":"");
		op += ( $.hasRight($.SysConstants.MODIFY)?"<a href=\"javascript:void(0);\" onclick=\"deleteApply('"+ formID +"','"+ formType +"')\"  class=\"opLink\">删除</a> | ":"");
	html += "<td>" + op.substring(0, op.lastIndexOf(" | ")) + " </td> </tr>";
	return html;
}

function initModifyForm(formID, formType) {
	switch (formType) {
		case "0" :
			//领用
			equipmentUseAdd(formID);
			break;
		case "1" :
			//申购
			equipmentUseAdd(formID);
			break;
		case "2" :
			//调拨
			equipmentAllotAdd(formID);
			break;
		case "3" :
			//报废
		case "4" :
			//离职处理
			equipmentScrapModify(formID);
			break;
	}
}

//报废新增/修改
function equipmentScrapModify(id){
	mainFrame.addTab({
		id:"oa_device_apply_disc"+id,
		title:"报废申请", 
		url:BASE_PATH + "m/dev_discard_start?act=initmodify&id=" + id,
		callback:queryList
	});
}

//领用申购新增/修改
function equipmentUseAdd(id){
	mainFrame.addTab({
		id:"oa_device_apply_reci"+id,
		title:"领用申购申请", 
		url:BASE_PATH + "m/dev_apply_start?act=initmodify&id=" + id,
		callback:queryList
	});
}

//调拨新增/修改
function equipmentAllotAdd(id){
	mainFrame.addTab({
		id:"oa_device_apply_allo"+id,
		title:"调拨申请", 
		url:BASE_PATH + "m/dev_alc_start?act=initmodify&id=" + id,
		callback:queryList
	});
}

//删除
function deleteApply(formID, formType){
	if(""==formID || null==formID || formType==null || formType==""){
		return;
	}
	var formName="领用";
	if(formType=="0"){
		formName="领用";
	} else if(formType=="1"){
		formName="申购";
	} else if(formType=="2"){
		formName="调拨";
	} else if(formType=="3"){
		formName="报废";
	} else if(formType=="4"){
		formName="离职处理";
	}
	$.confirm("确认要删除该"+formName+"单吗？", function(r){
		if (r) {
			$.ajax({
		        type : "POST",
				cache: false,
				url  : "m/dev_start",  //
				data : {
					act: "delete",
					id:formID,
					formType:formType
				},
		        success : function(xml){
		            var message = $("message",xml);
		            if(message.attr("code") == "1"){
		            	$.alert("操作成功");
						queryList();
		            } 
		            else{
		            	$.alert($("message",xml).text());
		            }
		        },
		        error : $.ermpAjaxError
		    });  
		}
	});
}

