var mainFrame = $.getMainFrame();

//设备报废--办理
$(initEquipmentInfoCEOHandle);
function initEquipmentInfoCEOHandle() {
	$("div.feesSp").each(function(i){
		$("span[name='orderNum']", this).text(i+1);
	});
	var id = $("#id").val();
	
	$(".addTool2 .allBtn").each(function(){
		$(this).click(function(){
			if($.validInput("comment", "处理意见", true, "\<\>\'\"", 1000)){
		  		return false;
		  	}
		  	var tiid=$("#taskInstanceID").val();
			var transition = $(this).val();
			var comment =$("#comment").val();
		  	$.confirm("确认是否" + $(this).val() + "？", function(r){
				if (r) {
					saveTaskComm(tiid,transition,comment);
				} else {
					return;
				}
			});
		});
	});
}

/**
 * 保存处理意见
 * @param {} tiid
 * @param {} transition
 * @param {} comment
 * @return {Boolean}
 */
function saveTaskComm(tiid,transition,comment){
	var id = $("#formId").val();
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/dev_deal?act=deal_ceo",
		data : {
			id:id,
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
					mainFrame.getCurrentTab().doCallback();
					//关闭
					mainFrame.getCurrentTab().close();
            	});
            } 
            else{
            	$.alert($("message",xml).text());
            }
        },
        error : $.ermpAjaxError
    }); 
}










