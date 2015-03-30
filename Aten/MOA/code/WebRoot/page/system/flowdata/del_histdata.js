var mainFrame = $.getMainFrame();
$(initPage);

function initPage(){
		// 初始化权限
	$.handleRights({
		"delData": $.SysConstants.DELETE 
    });
	// 初始化日期
	initDefaultDate();
	// 启动定时器,调用方法
	autoByTime();
	
 	// 删除操作
    $("#delData").click(function(){
    	// 按钮不可用
    	deleteFunction();
    });
    
}

/**
 * 初始化日期
 */
function initDefaultDate() {
	var time = new Date();
	var timeDate = new Date(time.getFullYear(),time.getMonth(),time.getDate());
	var date2 = new Date(timeDate.getTime () - 365*24*60*60*1000);
	$("#startDate").val(getForMatTime(date2));
}

/**
 * 每隔30秒调用该方法
 */
function autoByTime() {
		// 每隔30秒调用该函数-->
		window.setTimeout('autoByTime()', 30000);
		// 检查操作是否完成
		checkDBFinish();
 }
 
 /**
  * 检查操作是否完成
  */
function checkDBFinish() {
	if ($("#datepicker_div") != null && $("#datepicker_div").next("div") != null) {
		$("#datepicker_div").next("div").hide();
	}
	$.ajax({
	       type : "POST",
	       cache: false,
	       url  : "m/flow_data",
	       data : "act=checkflowdata",
	       success : function(xml) {
	       	 	var messageCode = $("message",xml).attr("code");
	           	var msg = $("message",xml).text();
          		if(messageCode == "1") {
          			if (msg == "1") {
          				//执行成功
	               		$("#show").hide();
	               		// 按钮可用
						disableButtion(false);
						$("#importantShow").html("<span style='color: red;'>上次删除操作执行成功!</span>");
						$("#importantShow").show();
          			} else if (msg == "-1") {
          				// 执行失败
          				$("#show").hide();
	               		// 按钮可用
						disableButtion(false);
						$("#importantShow").html("<span style='color: red;'>上次删除操作执行失败!</span>");
						$("#importantShow").show();
          			} else if(msg == "0"){
          				// 运行中
          				$("#show").show();
	               		// 按钮不可用
						disableButtion(true);
						$("#importantShow").hide();
          			}
           		} 
	       }
	   	});
}

/**
 * 删除操作
 */
function deleteFunction() {
	// 获取时间
	var time = $("#startDate").val();
	if (time == "") {
		$.alert("请选择时间.");
		// 按钮可用
		return false;
	}
	
	// 判断选择的日期是否在一年之内
//	var array = time.split("-");
//	var timeDate = new Date(array[0],array[1]-1,array[2]);
//	var date2 = new Date(timeDate.getTime () + 365*24*60*60*1000);
//	// 现在日期
//	var timeNow = new Date();
//	var date = getForMatTime(timeNow);
//	var array2 = date.split("-");
//	var timeDate = new Date(array2[0],array2[1]-1,array2[2]);
//	if (date2 > timeDate) {
//		alert("选取的日期应该在当前日期一年之前.");
//		// 填写默认时间
//		initDefaultDate();
//		// 按钮可用
//		disableButtion(false);
//		return false;
//	}
	$.confirm("确认删除 "+time+" 之前的流程数据?", function(r){
		if (r) {
			disableButtion(true);
			$.ajax({
		       type : "POST",
		       cache: false,
		       url  : "m/flow_data",
		       data :  {act : "delete", date : time},
		       success : function(xml) {
		       	 	var messageCode = $("message",xml).attr("code");
	          		if(messageCode == "0") {
	          			$.alert($("message",xml).text());
	          			disableButtion(false);
	           		} else {
	           			checkDBFinish();
	           		}
		       }
		   	});
			
		   	if ($("#datepicker_div") != null && $("#datepicker_div").next("div") != null) {
				$("#datepicker_div").next("div").hide();
				$("#importantShow").hide();
			} else {
				$("#show").show();
			}
		}
	});
}

/**
 * 获取格式如 2012-12-12 所示的时间
 */
function getForMatTime(d) {
	var defaultDate = d.getFullYear();
	defaultDate += "-";
	//月份
	var month = "";
	if (d.getMonth() + 1 < 10) {
		month = "0"+ (d.getMonth()+1);
	} else {
		month = d.getMonth()+1;
	}
	defaultDate += month;
	defaultDate += "-";
	//日期          
	var date = "";
	if (d.getDate() < 10) {
		date = "0"+ d.getDate();
	} else {
		date = d.getDate();
	}
	defaultDate += date;
	return defaultDate;
}

/**
 * 控制按钮是否可用
 * true 不可用 , fasle 可用
 */
function disableButtion(bool) {
	if (bool) {
		$("#delData").attr("disabled","true");
	} else {
		$("#delData").removeAttr("disabled");
	}
}