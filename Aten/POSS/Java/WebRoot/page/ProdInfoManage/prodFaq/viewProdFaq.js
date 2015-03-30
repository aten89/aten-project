var mainFrame = $.getMainFrame();

$(initViewProdFaqPage);
function initViewProdFaqPage(){
	
	// add answer
	$("#save").click(function(){
		saveProdFaqAnswer();
	});
	
	$("#cancel").click(function(){
		if (mainFrame != null && mainFrame.getCurrentTab()!= null){
			mainFrame.getCurrentTab().doCallback();
			mainFrame.getCurrentTab().close();
   		}
	});
	
}

function saveProdFaqAnswer() {
	// 
	setCommitButtonDisabled(true);
	var perentId = $("#id").val();
	var content = $.trim($("#content").val());
	if (content == "") {
		$.alert("请填写问题解答！");
		return;
	}
	
	var prodFaqObj = new Object();
	var parentFrodFaqObj = new Object();
	parentFrodFaqObj.id = perentId;
	prodFaqObj.parentProdFaq = parentFrodFaqObj;
	prodFaqObj.content = content;
	
	$.ajax({
        type : "POST",
		cache: false,
		url  : BASE_PATH + "m/prod_faq/add_prodFaq",
		dataType : "json",
		data : {
			prodFaqJSON : $.toJSON(prodFaqObj)
		},
	    success : function(data,i){
        	if ($.checkErrorMsg(data)) {
        		$.alert("操作成功!", function() {
        			window.location.reload();
        		});
        	} else {
        		setCommitButtonDisabled(false);
        	}
        },
        error : $.ermpAjaxError
    });
}

function setCommitButtonDisabled(disabled) {
	if (disabled) {
		$(".allBtn").attr("disabled","true");
	} else {
		$(".allBtn").removeAttr("disabled");
	}
}
