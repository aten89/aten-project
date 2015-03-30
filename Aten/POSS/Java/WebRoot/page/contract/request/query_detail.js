var INPUT_ID_USER_ACTION_KEYS = "r_userActionKeys1";	//登录用户在当前模块下有权限的动作列表
var mainFrame = $.getMainFrame();


$(initPage);
function initPage(){
	//快捷工具栏中没有任何按钮，则清除该DIV
    if($("#divAddTool input").length == 0){
    	$("#divAddTool").remove();
    }

	$("#add").click(function(){
		editContractDetail();
	});
	
	queryList();
}

function queryList(){
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/cont_request/query_detail",
			dataType : "json",
			data:{
				prodId : $.trim($("#prodId").val()),
				orgName : $.trim($("#orgName").val()),
				pageSize : 0
			},
	        success : function(data){
	           	var html="";
	        	if ($.checkErrorMsg(data)) {
	        		if(data.contractRequestPage && data.contractRequestPage.dataList){
						var dataList = data.contractRequestPage.dataList;
				    	$(dataList).each(function(i) {
				           	var id = dataList[i].id;
				           	html +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
		           			"<td class=\"listData\">"+getRegStatusStr(dataList[i].regStatus)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].reqNums==null ? "":dataList[i].reqNums)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].extendNums==null ? "":dataList[i].extendNums)+"</td>" +
		           			"<td class=\"listData\">"+dataList[i].regUserName+"</td>" +
		           			"<td class=\"listData\">"+dataList[i].regDate+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].expressName==null ? "":dataList[i].expressName)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].expressNo==null ? "":dataList[i].expressNo)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].sendDate==null ? "":dataList[i].sendDate)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].reqRemark==null ? "":dataList[i].reqRemark)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].extendRemark==null ? "":dataList[i].extendRemark)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].firstFlag ? "是":"否")+"</td>";
		           			
		           			 //操作
							var op = $.wrapActionRight($.SysConstants.DELETE, "<a href=\"javascript:void(0);\" onclick=\"deleteDetail('" + id + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;");
							if (dataList[i].regStatus==1) {
								op += $.wrapActionRight($.SysConstants.MODIFY, "<a href=\"javascript:void(0);\" onclick=\"editContractDetail('" + id + "');\" class=\"opLink\">修改</a>&nbsp;|&nbsp;") ;
							}
							if (dataList[i].regStatus==2) {
								op += $.wrapActionRight("confirm", "<a href=\"javascript:void(0);\" onclick=\"confirmRecive('" + id + "');\" class=\"opLink\">确认</a>&nbsp;|&nbsp;") ;
							}
	                        html += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td></tr>";
			           });
			        }
		            $("#data_list").html(html);
	        	}
	        }
	    });
}

function getRegStatusStr(regStatus) {
	if (regStatus == 1) {
		return "未发放";
	} else if (regStatus == 2) {
		return "已发放";
		} else if (regStatus == 3) {
		return "已领取";
	}
}

function viewProdInfo(id) {
	mainFrame.addTab({
		id:"view_prodInfo_"+id,
		title:"查看产品详情",
		url:BASE_PATH + "/m/prod_info/init_prodInfoFrame?prodInfoId=" + id
	});
}

function editContractDetail(id){
	var param = new Object();
	param.callback = function(){
		if (param.returnValue){
			$.alert("操作成功",function(){
				queryList();//搜索
			});
		}
		dialogWin.close();
	};
	window.dialogParam = param;
	var title = "登记所需合同";
	var url = BASE_PATH + "/m/cont_request/initedit?prodId=" + $("#prodId").val();
	if (id) {
		url += "&id=" + id;
	}
	var width = 560;
	var height = 260;
	var dialogWin = $.showModalDialog(title, url, width, height);
}

function confirmRecive(id) {
	$.confirm("是否确认领取合同?",function(result) {
		if (result) {
			$.ajax({
		        type : "POST",
				cache: false,
				async: true,
				url : BASE_PATH + "/m/cont_request/confirm",
				dataType : "json",
				data:{
					id : id
				},
		        success : function(data){
		        	if ($.checkErrorMsg(data)) {
//		    			$.alert("操作成功!",function(){
		    				queryList();
//		    			});
			    	}
		        },
		        error : $.ermpAjaxError
		    });
		}
	} );
}

function deleteDetail(id) {
	$.confirm("是否确认删除?",function(result) {
		if (result) {
			$.ajax({
		        type : "POST",
				cache: false,
				async: true,
				url : BASE_PATH + "/m/cont_request/delete",
				dataType : "json",
				data:{
					id : id
				},
		        success : function(data){
		        	if ($.checkErrorMsg(data)) {
//		    			$.alert("操作成功!",function(){
		    				queryList();
//		    			});
			    	}
		        },
		        error : $.ermpAjaxError
		    });
		}
	} );
}
