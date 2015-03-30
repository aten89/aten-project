var mainPanel = $.getMainFrame();
var dialogWin = null;
var param = null;

$(initCustomerInfomation);
function initCustomerInfomation(){
	//添加权限约束-问题管理
	$.handleRights({
        "modify" : $.SysConstants.MODIFY, 
        "commit" : $.SysConstants.MODIFY, 
        "revisit" : $.CrmConstants.RETURNVISIT, 
        "sendmessage" : $.CrmConstants.MESSAGE, 
        "request" : $.CrmConstants.REQUEST,
        "appoint" : $.CrmConstants.APPOINT 
    });

	//快捷工具栏中没有任何按钮，则清除该DIV
    if($("#divAddTool input").length == 0){
    	$("#divAddTool").remove();
    }
	
	$("#modify").click(function(){
		modify();
	});
	
	$("#revisit").click(function(){
		revisit();
	});
	
	$("#sendmessage").click(function(){
		sendmessage();
	});
	
	$("#request").click(function(){
		request();
	});
	
	$("#appoint").click(function(){
		appoint();
	});
	
	if($("#commit").size() > 0){
		$("#commit").click(function(){
			$.confirm("是否提交回访？", function(r){
				if (r) {
					commitCustomer();
				}
			});
		});
	}
	
}

//修改
function modify(){
	var customerId = $("#customerId").val();
	
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/customer/initmodify_div?customerId=" + customerId,
        success : function(txt){
        	$("#costsCon").html(txt);
     	}
	});
	
//	window.location.href = BASE_PATH + "/m/customer/initmodify?customerId=" + customerId;    	
	
//	$.getMainFrame().getCurrentTab().tabContent.loadURL(BASE_PATH + "/m/customer/initmodify?customerId=" + customerId);
	
//	mainPanel.addTab({
//		id:"view_customer_"+id,
//		title:"查看客户资料",
//		url:BASE_PATH + "/m/customer/initmodify?customerId=" + customerId
//	});
	
//	var customerId = $("#customerId").val();
//	param = new Object();
//	param.callback = function(){
//		if (param.returnValue){
//			$.alert("操作成功",function(){
//				dialogWin.close();
//				refresh();
//			});
//		}else{
//			dialogWin.close();
//			refresh();
//		}
//	};
//	window.dialogParam = param;
//	var title = "";
//	var url = BASE_PATH + "/m/customer/initmodify?customerId=" + customerId;
//	var width = 800;
//	var height = 480;
//	var onClose = "";
//	var position = "";
//	dialogWin = $.showModalDialog(title, url, width, height, onClose, position);
}

function revisit(){
	var id = $("#customerId").val();
	mainPanel.addTab({
		id:"initAddVisitRecord" + id,
		title:"添加回访",
		url:BASE_PATH + "m/customer/initAddReturnVist?customerId=" + id,
		callback:refresh
	});
}

function sendmessage(){
	var custStr = $("#tel").val() + "(" + $("#custName").val() + ")";
	custStr = encodeURI(custStr);   

	param = new Object();
	param.callback = function(){
		if (param.returnValue){
			$.alert("操作成功",function(){
				dialogWin.close();
			});
		}else{
			dialogWin.close();
		}
	};
	window.dialogParam = param;
	var title = "发送短信";
	var url = POSS_PATH + "/m/message/initAdd?custStr=" + custStr ;
	var width = 600;
	var height = 380;
	var onClose = "";
	dialogWin = $.showModalDialog(title, url, width, height, onClose);
}

function request(){
	var customerId = $("#customerId").val();
	//参数
	param = new Object();
	param.customerId = customerId;
	param.callback = closeWin;
	window.dialogParam = param;
	
	var title = "新增咨询记录";
	var url = BASE_PATH + "page/MyCustomer/CustomerInfo/addConsultRecord.jsp";
	var width = 585;
	var height = 280;
	var onClose = "";
	var position = "";
	dialogWin = $.showModalDialog(title, url, width, height, onClose, position);
}

function closeWin(){
	if (param.returnValue){
		$.alert("操作成功",function(){
			dialogWin.close();
			refresh();
		});
	}else{
		dialogWin.close();
		refresh();
	}
}

function appoint(){
	var customerId = $("#customerId").val();
	param = new Object();
	param.callback = function(){
		if (param.returnValue){
			$.alert("操作成功",function(){
				dialogWin.close();
				refresh();
			});
		}else{
			dialogWin.close();
			refresh();
		}
	};
	
	window.dialogParam = param;
	var title = "增加预约";
	var url = BASE_PATH + "page/MyCustomer/CustomerInfo/editAppointmentRecord.jsp?customerId=" + customerId;
	var width = 450;
	var height = 200;
	var onClose = "";
	var position = "";
	dialogWin = $.showModalDialog(title, url, width, height, onClose, position);
}

/**
 * 提交
 */
function commitCustomer(){
	var customerId = $("#customerId").val();
	$.ajax({
		   type : "POST",
		   cache: false, 
		   url  : BASE_PATH + "/m/customer/modify_commitFromView", 
		   dataType : "json",
		   data : {
			    customerId : customerId
		   },
		   success : function(data,i){
		    	if ($.checkErrorMsg(data)) {
		    		$.alert("操作成功",function(){
			    		//刷新父列表		
			    		mainPanel.getCurrentTab().doCallback();
			    		mainPanel.getCurrentTab().close();
		    		});
		    	} 
		    },
		    error : $.ermpAjaxError
		});
}

function refresh(){
	$("#custInfo").removeClass("current").click();
}
