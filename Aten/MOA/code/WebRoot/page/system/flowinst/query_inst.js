var mainFrame = $.getMainFrame();
$(initPage);

function initPage(){
	//刷新
	$.handleRights({
		"searchLog": $.SysConstants.QUERY
    });
    $("#searchLog").click(function(){
  		gotoPage(1);
  	});
  	$("#refresh").click(function(){
  		queryPage();
  	});
    gotoPage(1);
	
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
	
	if ( $.compareDate($("#beginCreateDate").val(), $("#endCreateDate").val())) {
		$.alert("开始时间不能大于结束时间！");
		return false;
	}
	
	$("#taskList").empty();
 	
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	var id =$("#formid").val();
	var desc =$("#desc").val();
	var beginCreateDate=$.trim($("#beginCreateDate").val());
	var endCreateDate=$.trim($("#endCreateDate").val());
	var applicant=$.trim($("#applicant").val());
	
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/flow_inst",
	   data : {
				act:"QUERY",
				id:id,
				desc:desc,
				beginCreateTimeDateStr:beginCreateDate,
				endCreateTimeDateStr:endCreateDate,
				transactor:applicant,
				pageno:pageno,
				pagecount:pagecount
		},
	   success : function (xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listDate = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {                      	    	  
		    	$(xml).find('task').each(function(index){
		    		var curELe = $(this);
		    		var viewFlag = $("view-flag",curELe).text();
                    var id = curELe.attr("id");
                    var op = ($.hasRight($.OaConstants.DISPOSE)?("<a href=\"javascript:void(0);\" onclick=\"dispose('"+ $("task-id",curELe).text() + "','"  + $("taskinstance-id",curELe).text() +"');\"  class=\"opLink\">处理</a>&nbsp;|&nbsp;") : "") 
               			   + ($.hasRight($.OaConstants.ASSIGN)?("<a href=\"javascript:void(0);\" onclick=\"assign('"+ $("taskinstance-id",curELe).text() +"');\" class=\"opLink\">授权</a>&nbsp;|&nbsp;") : "");
                    listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
                   			+ "<td>" + (index + 1) + "</td>"
							+ "<td>" + $("description",curELe).text() + "</td>"
							+ "<td>" + $("form-id",curELe).text() + "</td>"
							+ "<td>" + $("transactor",curELe).text() + "</td>"
							+ "<td>" + $("node-name",curELe).text() + "</td>"
							+ "<td>" + $("create-time",curELe).text() + "</td>";
							listDate += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"))+ "</td></tr>";      

				});
				$("#taskList").html(listDate);
				//----------------------------------------------------
		        //------------------翻页数据----------------------------
		        $(".pageNext").html($.createTurnPage(xml));
		        //----------------------------------------------------		
		        $.EnterKeyPress($("#numPage"),$("#numPage").next());
		    } else {
		        $.alert("没有查询结果");
		    };
	   },
	   error : function(){
	        $.ermpAjaxError();
	    }
	});
}

function assign(taskInstId){
	/*
	var selector = new UserDialog(ERMP_PATH,BASE_PATH);
	var user = selector.openDialog();
	if (user != null && user.id!=""){
		if (confirm("您确认要将这个文件授权给“" + user.name + "”吗？")){
			$.ajax({
		        type : "POST",
		        cache: false,
		        async : true,
				url  : "m/flow_inst",
		        data : {
		        	act:"assign",
		        	tiid:taskInstId,
		        	transactor:user.id
		        },
		        success : function(xml){
		            var message = $("message",xml);
		            if (message.attr("code") == "1") {
		          		 $.alert(message.text());
		          		 queryPage();
		            } else {
		                $.alert(message.text());
		            }
		        },
		        error : $.ermpAjaxError
			});
		}
	}
	*/

	var selector = new UserDialog(ERMP_PATH,BASE_PATH);
	selector.setCallbackFun(function(users){
		if (users != null) {
			var ids = "";
			var names = "";
			for (var i=0 ; i<users.length ; i++){
				if (ids != "") {
					ids += ",";
					names += ",";
				}
				ids += users[i].id;
				names += users[i].name;
			}
			$.confirm("您确认要将这个文件授权给“" + names + "”吗？", function(r){
				if (r) {
					$.ajax({
				        type : "POST",
				        cache: false,
				        async : true,
						url  : "m/flow_inst",
				        data : {
				        	act:"assign",
				        	tiid:taskInstId,
				        	transactor:ids
				        },
				        success : function(xml){
				            var message = $("message",xml);
				            if (message.attr("code") == "1") {
				          		 $.alert(message.text());
				          		 queryPage();
				            } else {
				                $.alert(message.text());
				            }
				        },
				        error : $.ermpAjaxError
					});
				}
			});
		}
	});
	
	selector.openDialog("multi");
}

//处理代办任务
function dispose(taskid,tiid) {
	mainFrame.addTab({
		id:"oa_deal_task"+taskid,
		title:"处理任务",
		url:BASE_PATH+"/m/flow_inst?act=dispose&taskid="+ taskid+ "&tiid=" + tiid,
		callback:queryPage
	});
}