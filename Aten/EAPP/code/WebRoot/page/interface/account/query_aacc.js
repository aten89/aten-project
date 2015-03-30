var mainFrame = $.getMainFrame();
var trunPageObj;//翻页组件对像
var islockSel;
var isvalidSel;
$(initActorAccount);

//初始化页面
function initActorAccount(){
	//mainFrame.getCurrentTab().setTitle("列表-接口帐号", "接口帐号查询列表");
//    $.handleRights({
//            "addActorAcc" : $.SysConstants.ADD,
//            "delAllAcc" : $.SysConstants.DELETE,
//            "searchActorAcc" : $.SysConstants.QUERY
//        }
//    );
    
    
    islockSel = $("#islockLink").ySelect({width: 40});
    isvalidSel = $("#isvalidLink").ySelect({width: 40});

	$("#delAllAcc").attr("disabled","true").addClass("icoNone");
	
    	
	/*新增-用户帐号*/
	$("#addActorAcc").click(
		function(e){
			mainFrame.addTab({
				id:"ermp_actor_account_"+Math.floor(Math.random() * 1000000),
				title:"新增接口帐号",
				url:"m/actor_account/initframe?action=add",
				callback:queryList
			});
		}
	);
	/*批量删除*/
	$("#delAllAcc").click(
		function(e){
		    if ($("input:checkbox:checked","tbody").length==0){
	           $.alert('请选择要删除的接口帐号!');        
	        }
	        else{
	        	$.confirm("是否删除所选的所有接口帐号?", function(r){
	        		if (r) {
	        			var accountIdList="";
						$("input:checkbox:checked","tbody").each(function(e){
							accountIdList+="&accountIDs="+$(this).attr("name");
						});
						$.ajax({
							type : "POST",
							cache: false,
							url  : "m/actor_account/delete",
							dataType : "json",
							data : accountIdList,
							success : function(data){
					        	if ($.checkErrorMsg(data) ) {
			//		        		$.alert("删除成功！");
					                //重新加载数据
					                queryList();
					        	}
					        }
						});
	        		}
	        	});
            }
		}
	);		
    /*刷新*/
    $("#refresh").click(
        function(){
            $("#data_list").html("");
	    	queryList();
        }
    );
    /*查询*/	
	$("#searchActorAcc").click(function(){
		trunPageObj.gotoPage(1);//搜索第一页
	});
    //回车搜索
    $.EnterKeyPress($("#keyword"),$("#searchActorAcc"));
    //初始化翻页组件
    trunPageObj =  $(".pageNext").turnPage();
	trunPageObj.gotoPage(1);//搜索第一页
}

//全选 
function checkAll(){
	$("tbody > tr").each(function(){
		$("input").attr("checked",$("#checkbox")[0].checked);
	});
    if ($("input:checkbox:checked","tbody").length==0){
        $("#delAllAcc").attr("disabled","true").addClass("icoNone");
    } else{
       $("#delAllAcc").removeAttr("disabled").removeClass("icoNone");
    }	
}
//选择单个
function checkSingle(){
	if ($("input:checkbox:checked","tbody").length==0){
		$("#checkbox")[0].checked=false;
		$("#delAllAcc").attr("disabled","true").addClass("icoNone");
	} else{
	   $("#delAllAcc").removeAttr("disabled").removeClass("icoNone");
	}
}

function modifyActorAcc(accountId){
	mainFrame.addTab({
		id:"ermp_actor_account_" + accountId,
		title:"修改接口帐号",
		url:"m/actor_account/initframe?action=modify&accountId="+encodeURIComponent(accountId),
		callback:queryList
	});
}

function deleteActorAcc(accountId){
	$.confirm("是否删除该帐号?", function(r){
		if (r) {
			if(!accountId || $.trim(accountId) == ""){
				$.alert("参数传入出错！");
				return;
			};
			$.ajax({
				type : "POST",
				cache: false,
				url  : "m/actor_account/delete",
				dataType : "json",
				data : "accountIDs=" + accountId,
				success : function(data){
		        	if ($.checkErrorMsg(data) ) {
//		        		$.alert("删除成功！");
		                //重新加载数据
		                queryList();
		        	}
		        }
			});
		}
	});
}


//查询函数
function queryList(){
	//设置查询按钮
	$("#searchActorAcc").attr("disabled","true");
    //设置全选框
    $("#checkbox")[0].checked=false;
	//设置批量删除按钮
	$("#delAllAcc").attr("disabled","true").addClass("icoNone");
	
	 //分页
	var pageNo=trunPageObj.getCurrentPageNo();//当前第几页
	var pageCount=trunPageObj.getPageCount();//一页多少条
	
	var keyword=$.trim($("#keyword").val());
	var islock=$.trim(islockSel.getValue());
	var isvalid=$.trim(isvalidSel.getValue());
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/actor_account/query",
	   dataType : "json",
	   data: {
			pageNo: pageNo,
			pageSize : pageCount,
			keyword : keyword,
			isLock : islock,
			isValid : isvalid
		},       
	   success : function (data){
		   	if ($.checkErrorMsg(data) ) {
		   		var fileList = "";
				if(data.actorAccountPage && data.actorAccountPage.dataList){
					var dataList = data.actorAccountPage.dataList;
					$(dataList).each(function(i) {
						fileList += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" ;
						fileList += "<td>" + (i + 1) + "</td>";
						fileList += "<td> <input id=\"checkbox"+(i + 1)+"\" type=\"checkbox\" name=\""+dataList[i].accountID+"\" onclick=\"checkSingle();\"/></td>";
						fileList += "<td class='titLeft'>"+dataList[i].accountID+"</td>";
						fileList += "<td>" + dataList[i].displayName + "</td>";
						fileList += "<td>" + (dataList[i].isLock ? "锁定" : "未锁定") + "</td>";
//						fileList += "<td>" + (dataList[i].changePasswordFlag =="Y" ? "可修改" : "不可修改") + "</td>";
						fileList += "<td>" + dataList[i].createDate + "</td>";
						fileList += "<td>" + (dataList[i].invalidDate ?dataList[i].invalidDate : "") + "</td>";
	                    
	                    //操作
						var op = $.wrapActionRight($.SysConstants.VIEW,"<a class=\"opLink\" href=\"javascript:viewActorAcc('" + dataList[i].accountID + "');void(0);\">详情</a>&nbsp;|&nbsp;") 
					        + $.wrapActionRight($.SysConstants.MODIFY,"<a class=\"opLink\" href=\"javascript:modifyActorAcc('"+dataList[i].accountID+"');void(0);\">修改</a>&nbsp;|&nbsp;")
							+ $.wrapActionRight($.SysConstants.DELETE,"<a class=\"opLink\" href=\"javascript:deleteActorAcc('"+dataList[i].accountID+"');\">删除</a>&nbsp;|&nbsp;");
                        fileList += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td></tr>";
					});
				}
				$("#data_list").html(fileList);
				trunPageObj.setPageData(data.actorAccountPage);
		   	}
			$("#searchActorAcc").removeAttr("disabled");
		}
	});
}

function viewActorAcc(accountId){
	mainFrame.addTab({
		id:"ermp_actor_account_" + accountId,
		title:"查看接口帐号",
		url:"m/actor_account/initframe?action=view&accountId="+encodeURIComponent(accountId),
		callback:queryList
	});
}