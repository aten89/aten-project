var mainFrame = $.getMainFrame();
var groupSelect;
$(initVacationApprovalArch);

function initVacationApprovalArch() {
	initListHead(columns);
	
	$.handleRights({
        "query" : $.SysConstants.QUERY,
        "addEntry" : $.SysConstants.ADD,
        "exportExcel" : $.SysConstants.EXPORT,
        "queryAssign" : $.OaConstants.ASSIGN
    });
    //新增
  	$("#addEntry").click(function(){
  		mainFrame.addTab({
			id:"oa_staffflow_draft"+Math.floor(Math.random() * 1000000),
			title:"新增员工信息",
			url:BASE_PATH + "m/staff_query?act=initadd",
			callback:queryPage
		});
  	});
  	//导出excel
	$("#exportExcel").click(function(){
		exportExcle();
  	});
  	//授权
	$("#queryAssign").click(function(){
		mainFrame.addTab({
			id:"oa_staffflow_assign",
			title:"员工查询授权",
			url:BASE_PATH + "page/hr/staffflow/query/query_assign.jsp",
			callback:queryPage
		});
  	});
    
	//刷新
  	$("#refresh").click(function(){
  		queryPage();
  	});
  	
  	//查询
  	$("#query").click(function(){
  		gotoPage(1);
  	});
  //初始化机构列表
	var url =ERMP_PATH + "m/rbac_group/groupselect?jsoncallback=?";
	$.getJSON(url,function(data){
		$("#groupIdDiv").html(data.htmlValue);
		groupSelect = $("#groupIdDiv").ySelect({width:100});
		groupSelect.addOption("", "所有...", 0);
		groupSelect.select(0);
		
		//默认搜索
		gotoPage(1);
	});
  	$.EnterKeyPress($("#userAccountId"),$("#query"));
}

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
    queryPage();
};

function queryPage() {

	$("#dataList > tbody").empty();
 	
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	var userDeptName = groupSelect.getValue() ? groupSelect.getDisplayValue() : "";
	var userAccountId = $.trim($("#userAccountId").val());
	var type = $("#type").val();
	var act = (type == "3") ? "query_my" : "query";
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/staff_query",
	   data : "act="+act+"&pageno="+pageno+"&pagecount="+pagecount+"&userDeptName="+userDeptName+"&userAccountId="+userAccountId+"&type="+type,             
	   success : function (xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listDate = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {
		    	var tBodyHTML = "";
		    	$(xml).find('staff-flow').each(function(index){
//		    		var curELe = $(this);
//                    var id = curELe.attr("id");
//                     var applyType = $("apply-type",curELe).text();
//                     var op = ( $.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initView('" + id + "','" + applyType + "')\">详情</a>&nbsp;|&nbsp;" : "")
//                     		+ ( $.hasRight($.SysConstants.MODIFY)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initModiyf('" + id + "','" + applyType + "')\">修改</a>&nbsp;|&nbsp;" : "");
//                    	   
//                    listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
//							+ "<td>" + $("user-account",curELe).text() + "</td>"
//							+ "<td>" + $("user-name",curELe).text() + "</td>"
//							+ "<td>" + $("employee-number",curELe).text() + "</td>"
//							+ "<td>" + $("company-area-name",curELe).text() + "</td>"
//							+ "<td>" + $("dept",curELe).text() + "</td>"
//							+ "<td>" +  $("post",curELe).text() + "</td>"
//							+ "<td>" + $("entry-date",curELe).text() + "</td>"
//							+ "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"))
//							+ "</td></tr>";
		    		var deviceConfig = $(this);
					var id = deviceConfig.attr("id");
					var applyType = $("applyType",deviceConfig).text();
					tBodyHTML += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
					for(var j=0; j< columns.length; j++){
						if(columns[j].defaultShow == true){
							var cn = columns[j].column;
							tBodyHTML +="<td class=\"listData\">" +$.trim($(cn, deviceConfig).text()), +"</td>";
						}
					}
					 var op = ( $.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initView('" + id + "','" + applyType + "')\">详情</a>&nbsp;|&nbsp;" : "")
                     		+ ( $.hasRight($.SysConstants.MODIFY)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initModiyf('" + id + "','" + applyType + "')\">修改</a>&nbsp;|&nbsp;" : "");
                    	   
					tBodyHTML += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + " </td> </tr>";
					

				});
				$("#dataList > tbody").html(tBodyHTML)
				//----------------------------------------------------
		        //------------------翻页数据----------------------------
		        $(".pageNext").html($.createTurnPage(xml));
		        //----------------------------------------------------		
		        $.EnterKeyPress($("#numPage"),$("#numPage").next());
		    } else {
		       // $.alert("没有查询结果");
		    };
	   		$("#searchReim").removeAttr("disabled");
	   },
	   error : function(){
	        $("#searchReim").removeAttr("disabled");
	        $.ermpAjaxError();
	    }
	});
}

//导出EXCLE
function exportExcle(){
	//对排序参数的处理
	var userDeptName = groupSelect.getValue() ? groupSelect.getDisplayValue() : "";
	var userAccountId = $.trim($("#userAccountId").val());
	var type = $("#type").val();
	var expNameAndValue = $("#expNameAndValue").val();
	$.ajax({type:"POST",
		 cache:false, 
		 url:"m/staff_query", 
		 data : {act:"export",
		 	userDeptName:userDeptName,
		 	userAccountId:userAccountId,
		 	type:type,
		 	expNameAndValue:expNameAndValue},
        success : function(xml){
            //解析XML中的返回代码
            var message = $("message",xml);
            if(message.attr("code") == "1"){
            	$.openDownloadDialog(BASE_PATH + $("message",xml).text());
            }
            else{
               $.alert($("message",xml).text());
            };
        },
        error : $.ermpAjaxError
    });
}

function initView(id, applyType) {
	mainFrame.addTab({
		id:"oa_staff_arch_viewarch_"+id,
		title:"查看员工信息",
		url:BASE_PATH + "/m/staff_query?act=view&id="+id
	});
}

function initModiyf(id, applyType){
	mainFrame.addTab({
		id:"oa_holiday_modify"+id,
		title:"修改员工信息",
		url:BASE_PATH + "m/staff_query?act=initmodify&id=" + id,
		callback:queryPage
	});
};

function initListHead(columns) {
	var expNameAndValue = "";
	var html="<table id=\"dataList\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" ><thead id=\"custHeadTr\"><tr>";
	for(var i=0; i<columns.length; i++){
		var column = columns[i];
		if(column.defaultShow){
			html +="<th ";
			if (column.columnSort) {//要列排序
				html += "sort='" + column.value + "' ";
			}
			html += "width=\""+column.width+"\">"+column.name+"</th>";
			expNameAndValue+=column.name+","+column.column+";"
		}
	}
	html +="<th width=\"65\"><div class=\"oprateW\">操作</div></th></tr></thead><tbody></tbody></table>";
	$("#divTable").html(html);
     //初始化表头列排序
//    columnSortObj = $("#custHeadTr").columnSort();
   $("#expNameAndValue").val(expNameAndValue); 
}

var dialogWin;
function closeDialog() {
	if (dialogWin) {
		dialogWin.close();
	}
}

//定制字段
function fieldDisplaySet(){
	columns.pageNo=$("#hidPageCount").val();
//	var sFeature = "dialogHeight:330px;dialogWidth:508px;status:no;scroll:auto;help:no";
//	if($.browser.msie && $.browser.version==6.0){
//		sFeature = "dialogHeight:385px;dialogWidth:516px;status:no;scroll:auto;help:no";
//	}
	window.dialogParams = columns;
	dialogWin = $.showModalDialog("定制字段显示", BASE_PATH + "page/hr/staffflow/query/set_field.jsp", 580, 360, function(){
		$("#hidPageCount").val(columns.pageNo);
		initListHead(columns);
		gotoPage(1)
	});
	
//	window.showModalDialog(BASE_PATH + "page/device/statistics/set_field.jsp",columns,sFeature);
//	$("#hidPageCount").val(columns.pageNo);
//	initListHead(columns);
//	gotoPage(1)
}