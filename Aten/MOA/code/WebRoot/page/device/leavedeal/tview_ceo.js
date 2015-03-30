var mainPanel = $.getMainFrame();
//离职设备处理单--办理
$(initEquipmentInfoLeaveDealCEOHandle);
function initEquipmentInfoLeaveDealCEOHandle() {
	//快速处理意见
	$("div.feesSp").each(function(i){
		$("span[name='orderNum']", this).text(i+1);
	});
	
	$(".addTool2 .allBtn").each(function(){
		$(this).click(function(){
		  if ($(this).val().indexOf("打印") == -1) {
			if($.validInput("comment", "处理意见", true, "\<\>\'\"", 1000)){
		  		return false;
		  	}
		  	var id = $("#id").val();
			var tiid=$("#taskInstanceID").val();
			var transition = $(this).val();
			var comment =$("#comment").val();
					
		  	$.confirm("确认是否" + $(this).val() + "？", function(r){
				if (r) {
					saveTaskComm(id, tiid,transition,comment);
				} else {
					return;
				}
			});
		  }
		});
	});
		
	//打印回购单
	$("input[name='btnPrintBuyBackForm']").click(function(){
	  if($.validInput("comment", "处理意见", true, "\<\>\'\"", 1000)){
		  return false;
	   }	
	  var comment =$("#comment").val();
	  var buyBackDeviceID = $(this).attr("id").split("_")[1];
//	  printBuyBackForm(buyBackDeviceID,comment);
	});
}

/**
 * 保存处理意见
 * @param {} tiid
 * @param {} transition
 * @param {} comment
 * @return {Boolean}
 */
function saveTaskComm(id, tiid,transition,comment){
	var act = "";
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/dev_deal?act=deal_scrap_task_by_backbuyflag",
		data : {
			id : id,
   			tiid : tiid,
   			transition : transition,
   			comment : comment
		},
        success : function(xml){
            //解析XML中的返回代码
            var message = $("message",xml);
            if(message.attr("code") == "1"){
            	$.alert("操作成功", function(){
	            	//刷新父列表
					mainPanel.getCurrentTab().doCallback();
					//关闭
					mainPanel.getCurrentTab().close();
            	});
            } 
            else{
            	$.alert($("message",xml).text());
            }
        },
        error : $.ermpAjaxError
    }); 
}

///**
// * 打印回购单
// */
//function printBuyBackForm(id,comment) {
//	$.showModalDialog(BASE_PATH + "m/dev_deal?act=print_buy_back_form&id=" + id + "&comment=" + encodeURI(comment), "", "dialogHeight:600px;dialogWidth:793px;status:no;scroll:auto;help:no");
//}