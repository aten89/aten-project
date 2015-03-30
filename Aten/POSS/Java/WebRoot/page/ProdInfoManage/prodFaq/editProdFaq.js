/**
 * 父页面传过来的参数
 */
var args = parent.window.dialogParam;

var prodInfoSel;

$(initEditProdFaq);
function initEditProdFaq(){
	
	var prodInfoId = $("#prodInfoId").val();
	initProdInfoSel(prodInfoId);
	
	// 保存
	$("#save").click(function() {
		saveProdFaq();
	});
	
	//取消
	$("#cancel").click(function() {
		args.returnValue = false;
		args.callback();
	});
}

/**
 * 保存
 * @returns {Boolean}
 */
function saveProdFaq() {
	// 设置按钮
	setCommitButtonDisabled(true);
	var id = $("#id").val();
	var creator = $("#creator").val();
	var prodInfoId = "";
	if (prodInfoSel != null) {
		prodInfoId = prodInfoSel.getValue();
	}
	var prodInfo = new Object();
	prodInfo.id = prodInfoId;
	var title = $.trim($("#title").val());
	var content = $.trim($("#content").val());
	
	if (prodInfoId == "") {
		$.alert("请选择产品！");
		setCommitButtonDisabled(false);
		return;
	}
	if (title == "") {
		$.alert("问题概述不能为空！");
		setCommitButtonDisabled(false);
		return;
	}
	
	var prodFaqObj = new Object();
	prodFaqObj.id = id;
	prodFaqObj.creator = creator;
	prodFaqObj.prodInfo = prodInfo;
	prodFaqObj.title = title;
	prodFaqObj.content = content;
	
	var url = BASE_PATH + "m/prod_faq/add_prodFaq";
	if (id != "undefined" && id != "") {
		url = BASE_PATH + "m/prod_faq/modify_prodFaq";
	}
	// 
	$.ajax({
        type : "POST",
		cache: false,
		url  : url,
		dataType : "json",
		data : {
			prodFaqJSON : $.toJSON(prodFaqObj)
		},
	    success : function(data,i){
        	if ($.checkErrorMsg(data)) {
        		args.returnValue = true;
        		args.callback();
        	} else {
        		setCommitButtonDisabled(false);
        	}
        },
        error : $.ermpAjaxError
    });
}

function initProdInfoSel(prodInfoId) {
	if ($("#prodInfoSel").size() > 0) {
		prodInfoSel = $("#prodInfoSel").ySelect({
			width : 265,
			height: 150,
			url : BASE_PATH + "/m/prod_info/initProdInfoSel",
			afterLoad : function() {
				prodInfoSel.addOption("", "请选择...", 0);
				// 设置默认值
				if (prodInfoId != "") {
					prodInfoSel.select(prodInfoId);
				} else {
					prodInfoSel.select(0);
				}
			},
			onChange : function(value, name) {
				// null
			}
		});
	}
}

/**
 * 设置按钮状态
 * @param disabled true:不可用。false:可用。
 */
function setCommitButtonDisabled(disabled) {
	if (disabled) {
		$(".allBtn").attr("disabled","true");
	} else {
		$(".allBtn").removeAttr("disabled");
	}
}
