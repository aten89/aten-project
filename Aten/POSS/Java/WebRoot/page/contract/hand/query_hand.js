var INPUT_ID_USER_ACTION_KEYS = "r_userActionKeys1";	//登录用户在当前模块下有权限的动作列表
var mainFrame = $.getMainFrame();
/**
 * 翻页组件对像
 */
var trunPageObj;

$(initPage);
function initPage(){
	$.removeNoRightEles();
	
	//快捷工具栏中没有任何按钮，则清除该DIV
    if($("#divAddTool input").length == 0){
    	$("#divAddTool").remove();
    }
	
	//放在这里不会报错
	trunPageObj =  $(".pageNext").turnPage();
	trunPageObj.gotoPage(1);//进入页面先进行一次搜索
	
	//支持回车
	$.EnterKeyPress($("#prodName"),$("#searchInfo"));
	
	//搜索
	$("#searchInfo").click(function(){
		 trunPageObj.gotoPage(1);
	});
	
	$("#add").click(function(){
		addContractDetail();
	});
}

function queryList(){
	
	var pageno = trunPageObj.getCurrentPageNo();//当前第几页
	var pagecount = trunPageObj.getPageCount();//一页多少条
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/cont_hand/query",
			dataType : "json",
			data:{
				pageNo : pageno,
				pageSize : pagecount,
				prodName : $.trim($("#prodName").val())
			},
	        success : function(data,i){
	           	var html="";
	        	if ($.checkErrorMsg(data)) {
	        		if(data.contractHandStatPage && data.contractHandStatPage.dataList){
					var dataList = data.contractHandStatPage.dataList;
			    	$(dataList).each(function(i) {
				           	var prodId = dataList[i].prodId;
				           	html +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
		           			"<td class=\"listData\"><a href='javascript:void(0);'  onclick=\"viewProdInfo('" + prodId + "')\">"+dataList[i].prodName+"</a></td>" +
		           			"<td class=\"listData\">"+dataList[i].orgName+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].regNums==null ? "":dataList[i].regNums)+"</td>" +
		           			"<td class=\"listData\">"+dataList[i].handNums+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].signNums==null ?"":dataList[i].signNums)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].blankNums==null ?"":dataList[i].blankNums)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].invalidNums==null ?"":dataList[i].invalidNums)+"</td>";
		           			
		           			 //操作
							var op = $.wrapActionRight($.SysConstants.VIEW, "<a href=\"javascript:void(0);\" onclick=\"viewRegDetail('" + prodId + "','" + dataList[i].prodName + "','" + dataList[i].orgName + "');\" id=\"editAction" + dataList[i].actionID + "\" class=\"opLink\">查看登记详情</a>&nbsp;|&nbsp;") 
	                                   + $.wrapActionRight($.SysConstants.DELETE, "<a href=\"javascript:void(0);\" onclick=\"deleteAllContract('" + prodId + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;");
	                        html += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td></tr>";
			           });
			        }
		            $("#data_list").html(html);
		            trunPageObj.setPageData(data.contractHandStatPage);
	        	}
	        }
	    });
}

function viewProdInfo(id) {
	mainFrame.addTab({
		id:"view_prodInfo_"+id,
		title:"查看产品详情",
		url:BASE_PATH + "/m/prod_info/init_prodInfoFrame?prodInfoId=" + id
	});
}

function addContractDetail(){
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
	var title = "登记上交合同";
	var url = BASE_PATH + "/m/cont_hand/initedit";
	var width = 560;
	var height = 380;
	var dialogWin = $.showModalDialog(title, url, width, height);
}

function viewRegDetail(prodId, prodName, orgName){
	var title = prodName + "（" + orgName + "）";
	var url = BASE_PATH + "/m/cont_hand/initquerydetail?prodId="+prodId;
	var width = 1000;
	var height = 460;
	var dialogWin = $.showModalDialog(title, url, width, height,function(){
		queryList();//搜索
	});
}

function deleteAllContract(prodId) {
	if(!prodId){
		$.alert("请选择要删除的记录");
		return;
	}
	$.confirm("是否确认删除?",function(result) {
		if (result) {
			$.ajax({
		        type : "POST",
				cache: false,
				async: true,
				url : BASE_PATH + "/m/cont_hand/delete_all",
				dataType : "json",
				data:{
					prodId : prodId
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
