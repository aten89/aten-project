var INPUT_ID_USER_ACTION_KEYS = "r_userActionKeys1";	//登录用户在当前模块下有权限的动作列表
var mainFrame = $.getMainFrame();
/**
 * 翻页组件对像
 */
var trunPageObj;

$(initPage);
function initPage(){
	$.removeNoRightEles();
	
	//放在这里不会报错
	trunPageObj =  $(".pageNext").turnPage();
	trunPageObj.gotoPage(1);//进入页面先进行一次搜索
	
	//支持回车
	$.EnterKeyPress($("#prodName"),$("#searchInfo"));
	
	//搜索
	$("#searchInfo").click(function(){
		 trunPageObj.gotoPage(1);
	});
	
}

function queryList(){
	
	var pageno = trunPageObj.getCurrentPageNo();//当前第几页
	var pagecount = trunPageObj.getPageCount();//一页多少条
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/cont_hand/query_detail",
			dataType : "json",
			data:{
				pageNo : pageno,
				pageSize : pagecount,
				checkStatus : 0,
				prodName : $.trim($("#prodName").val())
			},
	        success : function(data,i){
	           	var html="";
	        	if ($.checkErrorMsg(data)) {
	        		if(data.contractHandPage && data.contractHandPage.dataList){
					var dataList = data.contractHandPage.dataList;
			    	$(dataList).each(function(i) {
				           	var prodId = dataList[i].prodId;
				           	var id = dataList[i].id;
				           	html +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
		           			"<td class=\"listData\"><a href='javascript:void(0);'  onclick=\"viewProdInfo('" + prodId + "')\">"+dataList[i].prodName+"</a></td>" +
				           	"<td class=\"listData\">"+dataList[i].orgName+"</td>" +
		           			"<td class=\"listData\">"+dataList[i].regUserName+"</td>" +
		           			"<td class=\"listData\">"+dataList[i].regDate+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].handNums==null ? "":dataList[i].handNums)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].signNums==null ? "":dataList[i].signNums)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].blankNums==null ? "":dataList[i].blankNums)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].unPassNums==null ? "":dataList[i].unPassNums)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].expressName==null ? "":dataList[i].expressName)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].expressNo==null ? "":dataList[i].expressNo)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].handDate==null ? "":dataList[i].handDate)+"</td>" +
		           			"<td class=\"listData\">"+(dataList[i].handRemark==null ? "":dataList[i].handRemark)+"</td>";
		           			
		           			 //操作
							var op = $.wrapActionRight("check", "<a href=\"javascript:void(0);\" onclick=\"checkContract('" + id + "');\" class=\"opLink\">审核</a>&nbsp;|&nbsp;");
	                        html += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td></tr>";
			           });
			        }
		            $("#data_list").html(html);
		            trunPageObj.setPageData(data.contractHandPage);
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


function checkContract(id) {
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
	var title = "审核上交合同";
	var url = BASE_PATH + "/m/cont_hand/initcheck?id=" + id;
	var width = 640;
	var height = 280;
	var dialogWin = $.showModalDialog(title, url, width, height);
}
