/**********************
出差审批--跟踪列表
*********************/ 
var mainFrame = $.getMainFrame();
$(initApprovalTrack);
function initApprovalTrack(){
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
	$("#tripTrackList").empty(); 	
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	var applicant = $.trim($("#applicant").val());
		
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/trip_track",
	   data : "act=query&pageno="+pageno+"&pagecount="+pagecount+"&applicant="+applicant,          
	   success : function (xml){
	   		var message = $("message",xml);
			var listDate = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {	 
		    var sessionUser = $.trim($("#sessionUser").val());	                       	    	  
		    	$(xml).find('busTripApply').each(function(index){
		    		var curELe = $(this);
                    var id = curELe.attr("id");
                    listDate += "<tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
							+ "<td>" + id + "</td>"
							+ "<td>" + $("apply-account",curELe).text() + "</td>"
							+ "<td>" + $("dept",curELe).text() + "</td>"		
					//		+ "<td>" + $("trip-sche",curELe).text() + "</td>"					
							+ "<td>" + $("days",curELe).text() + "</td>"
							+ "<td>" + $("node-name",curELe).text() + "</td>"
							+ "<td>" + $("deal-man",curELe).text() + "</td>"
							+ "<td>" + $("create-time",curELe).text() + "</td>"
							+ "<td>" + ( $.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initView('" + id + "')\">详情</a> " : "");

							listDate += "</td></tr>";

				});
				$("#tripTrackList").html(listDate);
		        //------------------翻页数据----------------------------
		        $(".pageNext").html($.createTurnPage(xml));
		        //----------------------------------------------------		
		        $.EnterKeyPress($("#numPage"),$("#numPage").next());
		    } 
	   },
	   error : function(){
	        $.ermpAjaxError();
	    }
	});
}

function initView(id) {
	mainFrame.addTab({
		id:"oa_trip_track_view_"+id,
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
