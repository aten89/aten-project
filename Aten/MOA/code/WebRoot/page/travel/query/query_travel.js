var mainFrame = $.getMainFrame();
var groupSelect;
$(initTripApprovalTrack);
function initTripApprovalTrack() {
	initGroupSel();
	//打开用户帐号
   $("#openUserSelect").click(
	   	function(e){
			var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(user){
				if (user != null) {
					$("#userId").val(user.id);
					$("#applicantName").val(user.name);
				}
			});
			dialog.openDialog("single");
		}
	);
	
  	//查询
  	$("#query").click(function(){
  		gotoPage(1);
  	});
	
  	$.EnterKeyPress($("#id"),$("#query"));
  	gotoPage(1);
}

function gotoPage(pageNo, totalPage){

    if(isNaN(pageNo)){
        alert("页索引只能为数字！");
        return;
    } else if(pageNo < 1){
        pageNo = 1;
    }
    if (totalPage && !isNaN(totalPage) && pageNo > totalPage) {
    	pageNo = totalPage;
    }
    $("#hidNumPage").val(parseInt(pageNo));
    queryPage();
};

function queryPage() {
	
	$("#list").empty();
 	
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	var applicant = $.trim($("#userId").val());
	var id = $.trim($("#id").val());
	var startDate = $.trim($("#startDate").val());
	var endDate = $.trim($("#endDate").val());
   	var groupName = "";	
	if(typeof(groupSelect)!="undefined"){
   		groupName = groupSelect.getDisplayValue();
   	}
  	if(groupName=="请选择..."||groupName==undefined||groupName=="所有...") groupName="";
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/trip_admin_track",
	   data : {
		   "act":"query",
		   "pageno":pageno,
		   "pagecount":pagecount,
		   "applicant":applicant,
		   "id":id,
		   "startDate":startDate,
		   "endDate":endDate,
		   "applyDept":groupName
	   },       
	   success : function (xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listDate = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {	                              	    	  
		    	$(xml).find('busTripApply').each(function(index){
		    		var curELe = $(this);
                    var id = curELe.attr("id");
                    listDate += "<tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
							+ "<td>" + id + "</td>"
							+ "<td>" + $("apply-account",curELe).text() + "</td>"
							+ "<td>" + $("apply-date",curELe).text() + "</td>"
							+ "<td>" + $("dept",curELe).text() + "</td>"
							+ "<td>" + $("trip-sche",curELe).text() + "</td>"
							+ "<td>" + $("days",curELe).text() + "</td>"
							+ "<td>" + $("term-type",curELe).text() + "</td>"							
							+ "<td>" + getStatusStr($("status",curELe).text()) + "</td>"
							+ "<td>" + ( $.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initView('" + id + "')\">详情</a>" : "");
							+ "</td></tr>";

				});
				$("#list").html(listDate);
				//----------------------------------------------------
		        //------------------翻页数据----------------------------
		        $(".pageNext").html($.createTurnPage(xml));
		        //----------------------------------------------------		
		        $.EnterKeyPress($("#numPage"),$("#numPage").next());
		    } else {
		       
		    };
	   		$("#searchReim").removeAttr("disabled");
	   },
	   error : function(){
	        $("#searchReim").removeAttr("disabled");
	        $.ermpAjaxError();
	    }
	});
}

function getStatusStr(status) {
	if (status == "1") {
		return "审批中";
	} else if (status == "2") {
		return "归档";
	} else if (status == "3") {
		return "作废";
	}
}

function initView(id) {
	mainFrame.addTab({
		id:"oa_trip_arch_view_"+id,
		title:"查看出差申请单",
		url:BASE_PATH + "/m/trip_track?act=view&id="+id
	});
}

//所属部门
function initGroupSel(){
	//初始化机构列表
	var url =ERMP_PATH + "m/rbac_group/groupselect?jsoncallback=?";
	$.getJSON(url,function(data){
		$("#groupIdDiv").html(data.htmlValue);
		groupSelect = $("#groupIdDiv").ySelect({width:80});
		groupSelect.addOption("", "所有...", 0);
		groupSelect.select(0);
		
		//默认搜索
		queryPage();
	});
}