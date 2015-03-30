var mainFrame = $.getMainFrame();
var groupSelect = null;
var listGroupTree= null;

$(initAddrListListPage);

//初始化页面
function initAddrListListPage(){
    //搜索
    $("#searchAddrListList").click(
        function(){
        	$("#hidNumPage").val(1);
            queryPage();
        }
    );   
	
	//刷新
  	$("#refreshAddrList").click(function(){
  		queryPage();
  	});
  	
  	//手工输入姓名进行搜索时,按回车直接搜索
  	$.EnterKeyPress($("#userAccountId"),$("#searchAddrListList"));

	$("#userDeptId").val("");//作为　[在通过树型查找到用户通讯录时，翻页]的初始化条件值
	$("#userAccountId").val("");
	$("#hidNumPage").val(1);
	$("#AllSelectState").attr("checked", false);
	
	//初始化机构列表
	var url =ERMP_PATH + "m/rbac_group/groupselect?jsoncallback=?";
	$.getJSON(url,function(data){
		$("#groupIdDiv").html(data.htmlValue);
		groupSelect = $("#groupIdDiv").ySelect({width:100});
		groupSelect.addOption("", "所有...", 0);
		groupSelect.select(0);
		
		//默认搜索
		queryPage();
	});
  	
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
}

function queryPage() {	
	//设置查询按钮
 	$("#addrListList").empty();
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	var userDeptId = groupSelect.getValue() ? groupSelect.getDisplayValue() : "";
	var userAccountId = $.trim($("#userAccountId").val());//用户帐号id
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : BASE_PATH + "/m/staff_query",
	   data : "act=loadusers&pageno="+pageno+"&pagecount="+pagecount+"&userDeptName="+userDeptId
	            +"&userAccountId="+userAccountId,             
	   success : function (xml){
	   		//alert($(xml).get(0).xml);		
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listData = "";
			$("#addrListList").empty();     
			//是否有数据返回
		    if (message.attr("code") == "1") {	
		    	$(xml).find('user-account-info').each(function(index){
		    		var curELe = $(this);
                    var id = $.trim(curELe.attr("id"));
                    var assignValue = $("assign-value",curELe).text();
                    listData += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
			  				+ "<td>" + id + "</td>"
							+ "<td>" + $("display-name",curELe).text() + "</td>"
							+ "<td>" + assignValue + "</td>"
							+ "<td><a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initAssign('" + id + "','" + assignValue + "')\">授权</a> "
							+ "</td></tr>";

				});
				$("#addrListList").html(listData);
				//----------------------------------------------------
		        //------------------翻页数据----------------------------
		        $(".pageNext").html($.createTurnPage(xml));
		        //----------------------------------------------------		
		        $.EnterKeyPress($("#numPage"),$("#numPage").next());
		    } else {
		        $(".pageNext").html("");
		    };
	   		$("#searchAddrListList").removeAttr("disabled");
	   },
	   error : function(){
	        $("#searchAddrListList").removeAttr("disabled");
	        $.ermpAjaxError();
	    }
	});
}

//弹出授权页面
function initAssign(userAccountId, assignValue) {
    var selector = new DeptDialog(ERMP_PATH, BASE_PATH);
//			var selectedIds = $("#deptids").text().split(",");
	var selectedNames = assignValue.split(",");
	for (var i=0 ; i<selectedNames.length ; i++){
		if ($.trim(selectedNames[i]) != ""){
			selector.appendSelected({id:"", name:selectedNames[i]});
		}
	}
	
	selector.setCallbackFun(function(retVal){
		if (retVal != null) {
//					var ids = "";
			var names = "";
			for (var i=0 ; i<retVal.length ; i++){
//						ids += retVal[i].id + ",";
				names += retVal[i].name + ",";
			}
			saveAssign(userAccountId, names);
		}
	});
	selector.openDialog("multi");
}

//保存授权信息
function saveAssign(userAccountId, names) {
	$.ajax({
	    type : "POST",
	    cache: false,
	    async : true,
	    url  : "m/staff_query",
	    data :{act:"assign",
	    	userAccountId:userAccountId,
	    	groupNameStr:names},
	    success : function(xml){
			var message = $("message",xml);
	        if(message.attr("code") == "1"){
	        	$.alert("保存授权信息成功");
	        	queryPage();
	        }else{
	        	$.alert($("message",xml).text());
	        }
			
	    },
	    error : $.ermpAjaxError
	});
}