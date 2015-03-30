//设备类别配置--新增
var mainFrame = $.getMainFrame();

var deviceTypeSel;
var areaSel;//所属地区
var classSelect;//设备类别
var dimissionFlowSel;//离职
var useApplyFlowSel;//设备领用
var allocateFlowSel;//设备调拨
var discardFlowSel;//设备报废
var mainDevSel;//是否主设备
$(initDeviceClassConfigEdit);

function initDeviceClassConfigEdit() {
	initDeviceTypeSel($.trim($("#deviceTypeCode").val()), $.trim($("#deviceClassId").val()));
	viewInfoNo();
  	initArea();//所属地区
//  	initClassDiv();//设备类别
// 	initPurchaseFlowDiv() //离职设备处理流程
 	initUseApplyFlowDiv() //设备领用
 	initAllocateFlowDiv() //设备调拨
 	initDiscardFlowDiv() //设备报废
 	initDimissionFlowDiv();
    mainDevSel= $("#zsb").ySelect({width:99  });
    if ($("#mainDevFlag").val() != null && $("#mainDevFlag").val() != "") {
		mainDevSel.select($("#mainDevFlag").val());
	}
 	//保存
   $("#saveBtn").click(function(){
        saveDeviceNo();
    });
    //取消
    $("#closeBtn").click(function(){
       //关闭
		mainFrame.getCurrentTab().close();
    });
}

/**
 * 初始化设备类型下拉列表
 * @param {} deviceTypeCode 设备类型代码
 */
function initDeviceTypeSel(deviceTypeCode, deviceClassCode) {
	deviceTypeSel = $("#deviceTypeSel").ySelect({
		width : 99, 
		url:BASE_PATH+"m/data_dict?act=selectdevtype",
		afterLoad : function() {
			deviceTypeSel.addOption("", "请选择...", 0);
			if (deviceTypeCode != null && deviceTypeCode != "") {
				deviceTypeSel.select(deviceTypeCode);
			} else {
				
				deviceTypeSel.select(0);
			}
			
		},
		onChange : function(value, text) {
			
			initDeviceClassSel(value, deviceClassCode);
//			if(value!=""){
//				initDevUseType(value);
//			}
			
		}
	});     
}

/**
 * 初始化设备类别下拉列表
 * @param {} deviceClassCode 设备类别代码
 */
function initDeviceClassSel(deviceTypeCode, deviceClassCode) {
	if(deviceTypeCode==""){
		classSelect = $("#deviceClassSel").ySelect({
		width : 99
		});   
		classSelect.addOption("", "请选择...", 0);
		return;
	}
	classSelect = $("#deviceClassSel").ySelect({
		width : 99, 
		url:BASE_PATH+"m/device_class?act=classselect&deviceType=" + deviceTypeCode,
		afterLoad : function() {
			classSelect.addOption("", "请选择...", 0);
			if (deviceClassCode != null && deviceClassCode != "") {
				classSelect.select(deviceClassCode);
			} else {
				classSelect.select(0);
			}
		}, 
		onChange : function(value, text) {
		}
	});     
}
function initArea(){
	    areaSel = $("#areaCodeDiv").ySelect({width:99, url:BASE_PATH+"m/data_dict?act=ereasel",
		afterLoad:function(){
			areaSel.addOption("", "请选择...", 0);
			if($("#areaCode").val()!=""){
				areaSel.select($("#areaCode").val());
			}			
		}
	});
}

/**
 * 设备类别
 */
function initClassDiv() {
	classSelect = $("#deviceClassDiv").ySelect({width:99, url:"m/device_class?act=classselect",
		afterLoad:function(){
			classSelect.addOption("", "请选择...", 0);
			if($("#deviceClassId").val()!=""){
				classSelect.select($("#deviceClassId").val());
			}
			
		}
	});
}
/**
 * 离职设备处理流程
 */
function initDimissionFlowDiv() {
	dimissionFlowSel = $("#lzsb").ySelect({width:99, url:"m/flow_man?act=getByKey&flowClass=SBLC", 
		afterLoad:function(){
			dimissionFlowSel.addOption("", "请选择...", 0);
			if($("#discardFlowKey").val()!=""){
				dimissionFlowSel.select($("#dimissionFlowKey").val());
			}
		}
	});
}

/**
 * 设备领用
 */
function initUseApplyFlowDiv() {
	useApplyFlowSel = $("#sblylc").ySelect({width:99, url:"m/flow_man?act=getByKey&flowClass=SBLC",
		afterLoad:function(){
			useApplyFlowSel.addOption("", "请选择...", 0);
			if($("#useApplyFlowKey").val()!=""){
				useApplyFlowSel.select($("#useApplyFlowKey").val());
			}
		}
	});
}

/**
 * 设备调拨
 */
function initAllocateFlowDiv() {
	allocateFlowSel = $("#sbdb").ySelect({width:99, url:"m/flow_man?act=getByKey&flowClass=SBLC",
		afterLoad:function(){
			allocateFlowSel.addOption("", "请选择...", 0);
			if($("#allocateFlowKey").val()!=""){
				allocateFlowSel.select($("#allocateFlowKey").val());
			}
		}
	});
}

/**
 * 设备报废
 */
function initDiscardFlowDiv() {
	discardFlowSel = $("#sbbf").ySelect({width:99, url:"m/flow_man?act=getByKey&flowClass=SBLC",
		afterLoad:function(){
			discardFlowSel.addOption("", "请选择...", 0);
			if($("#discardFlowKey").val()!=""){
				discardFlowSel.select($("#discardFlowKey").val());
			}
		}
	});
}

function saveDeviceNo(){
	
	var areaCode=areaSel.getValue();//所属地区
	var classId=classSelect.getValue();//设备类别
	
	var useApplyFlow=useApplyFlowSel.getValue();//设备领用
	var allocateFlow=allocateFlowSel.getValue();//设备调拨
	var discardFlow=discardFlowSel.getValue();//设备报废
	var dimissionFlow=dimissionFlowSel.getValue();//设备离职流程
	var mainDevFlag=mainDevSel.getValue();//是否主设备
	if(areaCode==""){
		$.alert("请选择所属地区");
		return;
	}
	if(classId==""){
		$.alert("请选择设备类别");
		return;
	}
	var orderPrefix = $.trim($("#orderPrefix").val());
	var separator = $.trim($("#separator").val());
	var seqNum = $.trim($("#seqNum").val());
	result = $.validChar(orderPrefix);
	if (orderPrefix=="") {
		$.alert("前缀不能为空");
		$("#orderPrefix").focus();
		return;
	}
	if (result) {
		$.alert("前缀不能包含非法字符：" + result);
		$("#orderPrefix").focus();
		return;
	}
	if (orderPrefix.length >30) {
		$.alert("前缀长度不能大于30个字符");
		$("#orderPrefix").focus();
		return;
	}
	result = $.validChar(separator);
//	if (separator=="") {
//		$.alert("符号不能为空");
//		$("#separator").focus();
//		return;
//	}
	if (result) {
		$.alert("符号不能包含非法字符：" + result);
		$("#separator").focus();
		return;
	}
	if (separator.length >30) {
		$.alert("符号长度不能大于30个字符");
		$("#separator").focus();
		return;
	}
	if($.validNumber("seqNum","流水号位数",true,9)){
		return;
	}
	if(seqNum==0){
		$.alert("流水号位数必须大于0");
		$("#seqNum").focus();
		return;
	}
	if(useApplyFlow==""){
		$.alert("请选择设备领用流程");
		return;
	}
	if(allocateFlow==""){
		$.alert("请选择设备调拨流程");
		return;
	}
	if(discardFlow==""){
		$.alert("请选择设备报废流程");
		return;
	}
	if(dimissionFlow==""){
		$.alert("请选择设备离职流程");
		return;
	}
//	var devPuerposeIds = checkDeleteValue();
//	if(devPuerposeIds==""){
//		$.alert("请选择设备用途");
//		return;
//	}
	var remark = $.trim($("#remark").val());
	result = $.validChar(remark);
	if (result) {
		$.alert("备注不能包含非法字符：" + result);
		$("#remark").focus();
		return;
	}
	if (remark.length >300) {
		$.alert("备注长度不能大于300个字符");
		$("#remark").focus();
		return;
	}
	$("#saveBtn,#closeBtn").attr("disabled","true").addClass("icoNone");
	var id = $.trim($("#id").val());
	var act = "";
	if(id==""){
		act="add"
	}else{
		act="modify"
	}
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/device_areacfg",  //
		data : {
			act: act,
			id:id,
			areaCode:areaCode,
			classId:classId,
			useApplyFlow:useApplyFlow,
			allocateFlow:allocateFlow,
			discardFlow:discardFlow,
			dimissionFlow:dimissionFlow,
			orderPrefix:orderPrefix,
			separator:separator,
//			devPuerpose:devPuerposeIds,
			mainDevFlag:mainDevFlag,
			seqNum:seqNum,
			remark:remark
		},
        success : saveSuccess,
        error : $.ermpAjaxError
    });  
};

//保存成功回调函数
function saveSuccess(xml){
    var messageCode = $("message",xml).attr("code");
    var messageInfo = $("message",xml).text();
	
    if(messageCode == "1"){
    	$.alert("保存成功", function(){
	    	 //刷新父列表
			mainFrame.getCurrentTab().doCallback();
	       //关闭
			mainFrame.getCurrentTab().close();
    	});
    }else{
    	$.alert(messageInfo);
    	$("#saveBtn,#closeBtn").removeAttr("disabled","true").removeClass("icoNone");
    };
};
function getViewInfo(){
	var orderPrefix = $.trim($("#orderPrefix").val());
	var separator = $.trim($("#separator").val());
	var seqNum = $.trim($("#seqNum").val());
	$("#orderPrefix").onmousemove(function(){
       viewInfoNo();
    });
	$("#separator").onmousemove(function(){
       viewInfoNo();
    });
	$("#seqNum").onmousemove(function(){
       viewInfoNo();
    });
	
}
function  viewInfoNo(){
	var orderPrefix = $.trim($("#orderPrefix").val());
	var separator = $.trim($("#separator").val());
	var seqNum = $.trim($("#seqNum").val());
	
	if($.validNumber("seqNum","流水号位数",false,999999999)){
		return;
	}
	var index="";
	for(i=0;i<seqNum;i++){
		if(i==seqNum-1){
			index+="1";
		}else{
			index+="0";
		}
	}
	var devNo=orderPrefix+separator+index;
	if(orderPrefix!="" && seqNum!=""){
		$("#viewInfo").html(devNo)
	}else{
		$("#viewInfo").html("")
	}
	
}

////查询
//function initDevUseType(deviceType){
//	 var devPuerpose = $("#devPerpose").val();
//	$.ajax({
//	    type : "POST",
//		cache: false,
//		url  : "m/data_dict",
//		data : {act:"getdevusetype",
//			dataValue:deviceType
//		},
//	       success : function(xml){
//	     		//解析XML中的返回代码
//				var messageCode = $("message",xml).attr("code");
//				if(messageCode == "1"){
//					var html = "";
//					var i=0;
//					var k=1;
//					$("dataDict",xml).each(
//						
//	                    function(index){
//	                       k=k+2;
//	                       i=i+2;
//	                       var dateDict = $(this);
//	                       html += "<div>";
//	                       if(devPuerpose.indexOf($("data-value",dateDict).text()+";")!=-1){
//	                       	html +="<input type=\"checkbox\" id=\"dt0"+i+"\" class=\"cBox\" checked=\"true\" name=\"devPuerpose\" value=\""+$("data-value",dateDict).text()+"\" onClick=\"showTimesFlag(this)\"/><label for=\"dt0"+i+"\">" + $("data-key",dateDict).text()+ "</label> ";
//		                       if(devPuerpose.indexOf($("data-value",dateDict).text()+"true;")!=-1){
//		                       		html +=		"<span id=\""+$("data-value",dateDict).text()+"\"> 是否领用多次：<input type=\"radio\" name=\""+$("data-value",dateDict).text()+"\" id=\"r0"+i+"\" class=\"cBox\"  value=\"true\"/ checked=\"true\"><label for=\"r0"+i+"\">是</label>&nbsp;" ;
//		                       		html +=		"<input type=\"radio\" name=\""+$("data-value",dateDict).text()+"\" id=\"r0"+k+"\" class=\"cBox\"  value=\"false\" /><label for=\"r0"+k+"\">否</label></span>";
//		                       }else{
//		                       		html +=		"<span id=\""+$("data-value",dateDict).text()+"\"> 是否领用多次：<input type=\"radio\" name=\""+$("data-value",dateDict).text()+"\" id=\"r0"+i+"\" class=\"cBox\"  value=\"true\"/><label for=\"r0"+i+"\">是</label>&nbsp;" ;
//		                       		html +=		"<input type=\"radio\" name=\""+$("data-value",dateDict).text()+"\" id=\"r0"+k+"\" class=\"cBox\"  value=\"false\" checked=\"true\"/><label for=\"r0"+k+"\">否</label></span>";
//		                       
//		                       }
//	                       }else{
//	                       	html +="<input type=\"checkbox\" id=\"dt0"+i+"\" class=\"cBox\" onClick=\"showTimesFlag(this)\" name=\"devPuerpose\" value=\""+$("data-value",dateDict).text()+"\"/><label for=\"dt0"+i+"\">" + $("data-key",dateDict).text()+ "</label>" ;
//	                       	html +=	"<span id=\""+$("data-value",dateDict).text()+"\" style=\"display:none\"> 是否领用多次：<input type=\"radio\" name=\""+$("data-value",dateDict).text()+"\" id=\"r0"+i+"\" class=\"cBox\" value=\"true\"/ checked=\"true\" ><label for=\"r0"+i+"\">是</label>&nbsp;" ;
//	                       	html +=	"<input type=\"radio\" name=\""+$("data-value",dateDict).text()+"\" id=\"r0"+k+"\" class=\"cBox\" value=\"false\"/><label for=\"r0"+k+"\">否</label></span>";
//	                       }
//          					html +="</div>"
//	                    });		        
//	                $("#devUseType").html(html);
//				}else{
//               		$.alert($("message",xml).text());
//           		}
//	       },
//	       error : $.ermpAjaxError
//	});	
//}
//获取选中的值
//function checkDeleteValue(){
//	//获得参数
//	var devPuerposeIds = "";//
//	var flag=true;
//	$("input[name='devPuerpose'][checked]", "#devUseType").each(function(){
//		if( this.checked == true ){
//			if(flag){
//				devPuerposeIds = this.value+";"+$("input[@type=radio][name="+this.value+"][@checked]").val();
//				flag=false;
//			}else{
//				devPuerposeIds += ","+this.value+";"+$("input[@type=radio][name="+this.value+"][@checked]").val();
//			}
//		}
//	});
//	return devPuerposeIds;
//}

function showTimesFlag(objet){
	var id=objet.value;
	if(objet.checked){
		$("#"+id).show();
	}else{
		$("#"+id).hide();
	}
	
}

