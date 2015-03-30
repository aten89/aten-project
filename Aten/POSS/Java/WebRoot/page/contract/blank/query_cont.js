var mainFrame = $.getMainFrame();
/**
 * 翻页组件对像
 */
var trunPageObj;

$(initPage);
function initPage(){
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
			url  : "m/cont_blank/query",
			dataType : "json",
			data:{
				pageNo : pageno,
				pageSize : pagecount,
				prodName : $.trim($("#prodName").val())
			},
	        success : function(data,i){
	           	var html="";
	        	if ($.checkErrorMsg(data)) {
	        		if(data.blankContractPage && data.blankContractPage.dataList){
					var dataList = data.blankContractPage.dataList;
			    	$(dataList).each(function(i) {
				           	var id = dataList[i].id;
				           	html +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
		           			"<td class=\"listData\"><a href='javascript:void(0);'  onclick=\"viewProdInfo('" + dataList[i].prodId + "')\">"+dataList[i].prodName+"</a></td>" +
		           			"<td class=\"listData\">"+(dataList[i].regNums==null ? "":dataList[i].regNums)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].contractNums==null ?"":dataList[i].contractNums)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].remainNums==null ?"":dataList[i].remainNums)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].latestDas==null ?"":dataList[i].latestDas)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].returnFlag?"是":"否")+"</td>";
		           			
		           			 //操作
							var op = $.wrapActionRight($.SysConstants.VIEW, "<a href=\"javascript:void(0);\" onclick=\"viewRegDetail('" + id + "','" + dataList[i].prodName + "');\" id=\"editAction" + dataList[i].actionID + "\" class=\"opLink\">查看登记详情</a>&nbsp;|&nbsp;") 
	                                   + $.wrapActionRight($.SysConstants.DELETE, "<a href=\"javascript:void(0);\" onclick=\"deleteBlankContract('" + id + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;");
	                        html += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td></tr>";
			           });
			        }
		            $("#data_list").html(html);
		            trunPageObj.setPageData(data.blankContractPage);
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
	var title = "登记空白合同";
	var url = BASE_PATH + "/m/cont_blank/initadd";
	var width = 560;
	var height = 220;
	var dialogWin = $.showModalDialog(title, url, width, height);
}

function viewRegDetail(id, prodName){
	var title = prodName;
	var url = BASE_PATH + "/m/cont_blank/initquerydetail?contractId="+id;
	var width = 640;
	var height = 360;
	var dialogWin = $.showModalDialog(title, url, width, height,function(){
		queryList();//搜索
	});
}

function deleteBlankContract(id) {
	if(!id){
		$.alert("请选择要删除的记录");
		return;
	}
	$.confirm("是否确认删除?",function(result) {
		if (result) {
			$.ajax({
		        type : "POST",
				cache: false,
				async: true,
				url : BASE_PATH + "/m/cont_blank/delete",
				dataType : "json",
				data:{
					contractId : id
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
