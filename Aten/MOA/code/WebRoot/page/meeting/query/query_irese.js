var mainFrame = $.getMainFrame();
var areaSel;
$(initPage);
var meetingRoomSelect;
function initPage(){
	//添加权限约束
	$.handleRights({
		"searchMeeting": $.SysConstants.QUERY
    });
    //初始化下拉
	initArea();
	meetingRoomSelect = $("#meetingRoomDiv").ySelect({width:80,isdisabled: true});
	$("#searchMeeting").click(function(){
  		gotoPage(1);
  	});
  	
	//刷新
	$("#refresh").click(function(){
		$("#meetingList").empty();
  		loadList();
	});
	
	$.EnterKeyPress($("#theme"),$("#searchMeeting"));

	
	gotoPage(1);
	
}

function initDiv() {
	var areaCode = "";
	if(typeof(areaSel)!="undefined"){
		areaCode=areaSel.getValue();
	}
	if(areaCode==null || areaCode==""){
		meetingRoomSelect = $("#meetingRoomDiv").ySelect({width:80,isdisabled: true});
		return;
	}
	meetingRoomSelect = $("#meetingRoomDiv").ySelect({width:80, url:"m/meet_param?act=meetingroomselect&areaCode="+areaCode,
		afterLoad:function(){
			meetingRoomSelect.addOption("", "请选择...", 0);
		}
	});
}

//-------------------加载查询列表-------------------
function loadList(){

	if ( $.compareDate($("#inputBeginDate").val(), $("#inputEndDate").val())) {
		$.alert("开始时间不能大于结束时间！");
		return false;
	}
	
	 //分页
  	var pageno=$.trim($("#hidNumPage").val());
  	var pagecount=$.trim($("#hidPageCount").val());
  	var areaCode = "";
	if(typeof(areaSel)!="undefined"){
		areaCode=areaSel.getValue();
	}
	//获得参数
	var roomId=meetingRoomSelect.getValue()==""?"":meetingRoomSelect.getValue();
	var inputBeginDate=$.trim($("#inputBeginDate").val());
	var inputEndDate=$.trim($("#inputEndDate").val());
	var theme=$.trim($("#theme").val());
	//提交查询
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/meet_query",
		data : {
				act:"QUERY",
				queryType:"ireserved",
				inputBeginDate:inputBeginDate,
				inputEndDate:inputEndDate,
				theme:theme,
				areaCode:areaCode,
				roomId:roomId,
				pageNo:pageno,
				pageSize:pagecount
		},
        success : function(xml){
            //解析XML中的返回代码
            var message = $("message",xml);
            if(message.attr("code") == "1"){
                var tBodyHTML = "";
                var pageNextHTML="";
                $("meet-info",xml).each(
                    function(index){
                        var meetInfo = $(this);
                        tBodyHTML += createTR(meetInfo.attr("id"),
                        $("begin-time",meetInfo).text(), 
                        $("end-time",meetInfo).text(),
                         $("area-name",meetInfo).text(),
                        $("room-name",meetInfo).text(),
                        $("theme",meetInfo).text(),
                        $("status",meetInfo).text());
                    }
                );
                $("#meetingList").html(tBodyHTML).find("td:empty").html("&nbsp;");
                enWrap(); //折行
                //------------------翻页数据--------------------------
	            $(".pageNext").html($.createTurnPage(xml));
	            $.EnterKeyPress($("#numPage"),$("#numPage").next());
            }
            else{
               $.alert($("message",xml).text());
            };
        },
        error : $.ermpAjaxError
    });
    	
}
//创建行
function createTR(id,beginTime,endTime,areaName,roomName,theme,status){
	var html = "";
	html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
         + "<td>" + beginTime + " 至 " + endTime + "</td>" 
         + "<td>" + areaName + "</td>" 
         + "<td>" + roomName + "</td>" 
         + "<td>" + theme + "</td>"
         + "<td>" + status + "</td>"
         + "<td>" + ( $.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initView('" + id + "')\">详情</a> " : "")
         + "</td></tr>";
    return html;
}

function initView(id) {
	mainFrame.addTab({
		id:"oa_meet_info_view_"+id,
		title:"查看会议信息",
		url:BASE_PATH + "/m/meet_query?act=view&id="+id,
		callback:loadList
	});
}

//转向第几页
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
    loadList();
};

function initArea(){
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/data_dict?act=ereasel",
			dataType : "html",
	        success : function(data){
	        	var html=" <div isselected='true'>**请选择...</div>";
	            html+=data;
	            $("#areaCode").html(html);
	            areaSel=$("#areaCode").ySelect({width :70,name:"areaCode",onChange : initDiv});
	        },
	        error : $.ermpAjaxError
	    });
}
//折行
function enWrap(){
	var D=document; F(D.body); function F(n){var u,r,c,x; if(n.nodeType==3){ u=n.data.search(/\S{10}/); if(u>=0) { r=n.splitText(u+10); n.parentNode.insertBefore(D.createElement("WBR"),r); } }else if(n.tagName!="STYLE" && n.tagName!="SCRIPT"){for (c=0;x=n.childNodes[c];++c){F(x);}} } 
}