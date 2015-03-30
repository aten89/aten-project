var mainFrame = $.getMainFrame();
var url;
$(initPage);
//var typeSel;
function initPage(){

  	$("#refresh").click(function(){
  		$("#informationTab > tbody").html("");
  		loadList();
  	});
	
	//生成类别下拉框
//	typeSel = $("#typeSel").ySelect({width:70});
	
	//搜索提交
	$("#searchInfo").click(function(){
		$("#informationTab > tbody").html("");
  		gotoPage(1);
  	});
  	//回车搜索
	$.EnterKeyPress($("#subject"),$("#searchInfo"));
  	
  	gotoPage(1);
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
    $("#hidNumPage").val(parseInt(pageNo));
    loadList();
};

//-------------------加载查询列表-------------------
function loadList(){
	
	 //分页
  	var pageno=$.trim($("#hidNumPage").val());
  	var pagecount=$.trim($("#hidPageCount").val());
  	var layoutName = $.trim($("#layoutName").val());
	var subject =  $("#subject").val();
//	var infoClass = typeSel.getValue();
	
	$.ajax({
        type : "POST",
		cache: false,
		url  : "page/portlet",
		data : {
			act:"getinfos",
			pageno:pageno,
			pagecount:pagecount,
			subject:subject,
//			infoclass:infoClass,
			layoutname:layoutName
		},
        success : function(xml){
            //解析XML中的返回代码
            var message = $("message",xml);
            if(message.attr("code") == "1"){
            	var tBodyHTML = "";
	            $("information",xml).each(
	                    function(index){
	                        var informationConfig = $(this);
	                        var color = "black";
				    		if ($("info-property",informationConfig).text() == "0") {
				    			color = "blue";
				    		} else if ($("is-new",informationConfig).text() == "true") {
				    			color = "red";
				    		}
//				    		var layoutName = $("#layoutName").val();
//				    		if(layoutName == "升级公告"){
//				    			tBodyHTML += createTRHtml(
//				    					(index + 1),
//				    					$(informationConfig).attr("id"),
//				    					$("subject",informationConfig).text(),
//				    					$("public-date",informationConfig).text(),
//				    					color
//				    			);
//				    		}else{
				    			tBodyHTML += createTR(
				    					(index + 1),
				    					$(informationConfig).attr("id"),
				    					$("subject",informationConfig).text(),
				    					$("group-name",informationConfig).text(),
				    					$("info-class",informationConfig).text(),
				    					$("public-date",informationConfig).text(),
				    					color
				    			);
//				    		}
	                    }
	             );
	            $("#informationTab > tbody").html(tBodyHTML);
                //------------------翻页数据--------------------------
	            $(".pageNext").html($.createTurnPage(xml));
	            $.EnterKeyPress($("#numPage"),$("#numPage").next());
            };
        },
        error : $.ermpAjaxError
    });
}

//创建TR
function createTR(index,id,subject,groupName,infoClass,publicDate,color){
	var html = "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\" style='color:" + color + "'>" 
		 + "<td>" + index + "</td>"
		 + "<td>" + subject + "</td>"
		 + "<td>" + publicDate + "</td>"
		 + "<td>" + groupName + "</td>"
//		 + "<td>" + infoClass + "</td>"
	     + "<td>" + "<a href=\"javascript:void(0);\" onclick=\"view('" + id + "');\"  class=\"opLink\">详情</a></td></tr>";
    return html;
}

////升级公告TR
//function createTRHtml(index,id,subject,publicDate,color){
//	var html = "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\" style='color:" + color + "'>" 
//	+ "<td>" + index + "</td>"
//	+ "<td>" + subject + "</td>"
//	+ "<td>" + publicDate + "</td>"
//	+ "<td>" + "<a href=\"javascript:void(0);\" onclick=\"view('" + id + "');\"  class=\"opLink\">详情</a></td></tr>";
//	return html;
//}

//查看详情
function view(id){
	mainFrame.addTab({
		id:"oa_InfoMan_view_"+id,
		title:"查看信息",
		url:BASE_PATH + "/page/portlet?act=viewinfo&id=" + id
	});
}
