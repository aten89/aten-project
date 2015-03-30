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
	$.EnterKeyPress($("#orgName"),$("#searchInfo"));
	$.EnterKeyPress($("#prodName"),$("#searchInfo"));
	
	//搜索
	$("#searchInfo").click(function(){
		 trunPageObj.gotoPage(1);
	});
	
	$("#add").click(function(){
		editConfirmExtendl("");
	});
}

function queryList(){
	
	var pageno = trunPageObj.getCurrentPageNo();//当前第几页
	var pagecount = trunPageObj.getPageCount();//一页多少条
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/confirm_ext/query",
			dataType : "json",
			data:{
				pageNo : pageno,
				pageSize : pagecount,
				orgName : $.trim($("#orgName").val()),
				prodName : $.trim($("#prodName").val())
			},
	        success : function(data,i){
	           	var html="";
	        	if ($.checkErrorMsg(data)) {
	        		if(data.confirmExtendPage && data.confirmExtendPage.dataList){
					var dataList = data.confirmExtendPage.dataList;
			    	$(dataList).each(function(i) {
				           	var id = dataList[i].id;
				           	html +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
		           			"<td class=\"listData\"><a href='javascript:void(0);'  onclick=\"viewProdInfo('" + dataList[i].prodId + "')\">"+dataList[i].prodName+"</a></td>" +
		           			"<td class=\"listData\">"+dataList[i].orgName+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].custNums==null ?"":dataList[i].custNums)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].confirmNums==null ?"":dataList[i].confirmNums)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].expressName==null ?"":dataList[i].expressName)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].expressNo==null ?"":dataList[i].expressNo)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].remark==null ?"":dataList[i].remark)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].regUser==null ?"":dataList[i].regUser)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].regDate==null ?"":dataList[i].regDate)+"</td>";
		           			
		           			 //操作
							var op = $.wrapActionRight($.SysConstants.MODIFY, "<a href=\"javascript:void(0);\" onclick=\"editConfirmExtendl('" + id + "');\"  class=\"opLink\">修改</a>&nbsp;|&nbsp;") 
	                                   + $.wrapActionRight($.SysConstants.DELETE, "<a href=\"javascript:void(0);\" onclick=\"delConfirmExtendl('" + id + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;");
	                        html += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td></tr>";
			           });
			        }
		            $("#data_list").html(html);
		            trunPageObj.setPageData(data.confirmExtendPage);
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

function editConfirmExtendl(id){
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
	var title = "确认书发放";
	var url = BASE_PATH + "m/confirm_ext/initedit?id=" +id;
	var width = 560;
	var height = 320;
	var dialogWin = $.showModalDialog(title, url, width, height);
}

function delConfirmExtendl(id) {
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
				url : BASE_PATH + "/m/confirm_ext/delete",
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
