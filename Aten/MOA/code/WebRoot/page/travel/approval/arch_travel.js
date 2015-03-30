/**********************
出差审批--跟踪列表
*********************/ 
var mainFrame = $.getMainFrame();
$(initApprovalTrack);
function initApprovalTrack(){
	//刷新
  	$("#refresh").click(function(){
  		queryPage();
  	});
  	
  	//查询
  	$("#query").click(function(){
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
  	$.EnterKeyPress($("#applicantName"),$("#query"));
  	
  	gotoPage(1);
};

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

	$("#archList").empty();
 	
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	var applicant = $.trim($("#applicant").val());
	
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/trip_arch",
	   data : "act=query&pageno="+pageno+"&pagecount="+pagecount+"&applicant="+applicant,             
	   success : function (xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listDate = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {                        	    	  
		    	$(xml).find('busTripApply').each(function(index){
		    		var curELe = $(this);
                    var id = curELe.attr("id");
                    listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
							+ "<td>" + id + "</td>"
							+ "<td>" + $("apply-account",curELe).text() + "</td>"
							+ "<td>" + $("dept",curELe).text() + "</td>"
							+ "<td>" + $("trip-sche",curELe).text() + "</td>"
							+ "<td>" + $("days",curELe).text() + "</td>"
							+ "<td>" + $("arch-date",curELe).text() + "</td>"
							+ "<td>" + $("pass",curELe).text() + "</td>"							
							+ "<td>" + ( $.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initView('" + id + "')\">详情</a>" : "");
							
					listDate+= "</td></tr>";

				});
				$("#archList").html(listDate);
				//----------------------------------------------------
		        //------------------翻页数据----------------------------
		        $(".pageNext").html($.createTurnPage(xml));
		        //----------------------------------------------------		
		        $.EnterKeyPress($("#numPage"),$("#numPage").next());
		    } else {
		        alert("没有查询结果");
		    };
	   		$("#searchReim").removeAttr("disabled");
	   },
	   error : function(){
	        $("#searchReim").removeAttr("disabled");
	        $.ermpAjaxError();
	    }
	});
}

function initView(id) {
	mainFrame.addTab({
		id:"oa_trip_arch_view_"+id,
		title:"查看出差申请单",
		url:BASE_PATH + "/m/trip_track?act=view&id="+id
	});
}

//取消出差
function trip_cancel(id){
	mainFrame.addTab({
		id:"oa_trip_cancel",
		title:"取消出差",
		url:BASE_PATH + "/m/trip_track?act=initcancel&id="+id,
		callback:queryPage
	});
}

//变更
function trip_change(id){
	mainFrame.addTab({
		id:"oa_trip_change",
		title:"变更出差",
		url:BASE_PATH + "/m/trip_arch?act=initchange&id="+id,
		callback:queryPage
	});
}