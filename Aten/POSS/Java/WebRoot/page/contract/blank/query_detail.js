var mainFrame = $.getMainFrame();


$(initPage);
function initPage(){
	//快捷工具栏中没有任何按钮，则清除该DIV
    if($("#divAddTool input").length == 0){
    	$("#divAddTool").remove();
    }

	$("#add").click(function(){
		addContractDetail();
	});
	queryList();
}

function queryList(){
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/cont_blank/query_detail",
			dataType : "json",
			data:{
				contractId : $.trim($("#contractId").val())
			},
	        success : function(data,i){
	           	var html="";
	        	if ($.checkErrorMsg(data)) {
	        		if(data.contractRegDetails){
					var dataList = data.contractRegDetails;
			    	$(dataList).each(function(i) {
				           	var id = dataList[i].id;
				           	html +="<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
//		           			"<td class=\"listData\"><a href='javascript:void(0);'  onclick=\"viewProdInfo('" + dataList[i].blankContract.prodId + "')\">"+dataList[i].blankContract.prodName+"</a></td>" +
		           			"<td class=\"listData\">"+(dataList[i].contractNums==null ? "":dataList[i].contractNums)+"</td>" +
		           			"<td class=\"listData\">"+dataList[i].regUserName+"</td>" +
		           			"<td class=\"listData\">"+dataList[i].regDate+"</td>";
		           			
		           			 //操作
							var op = $.wrapActionRight($.SysConstants.DELETE, "<a href=\"javascript:void(0);\" onclick=\"deleteDetail('" + id + "');\" class=\"opLink\">删除</a>&nbsp;|&nbsp;");
	                        html += "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;")) + "</td></tr>";
			           });
			        }
		            $("#data_list").html(html);
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
	var url = BASE_PATH + "/m/cont_blank/initadd?contractId=" + $("#contractId").val();
	var width = 560;
	var height = 200;
	var dialogWin = $.showModalDialog(title, url, width, height);
}

function deleteDetail(id) {
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
				url : BASE_PATH + "/m/cont_blank/delete_detail",
				dataType : "json",
				data:{
					detailId : id
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
