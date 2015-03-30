var flowCategorySel = null;
var flowEditObj = null;
var flowInfoData = null;//流程基本信息

//窗口大小改变监听
window.onresize = reSizeEditArea;

$(initEditFlow);
function initEditFlow(){
	//加载流程类别
	flowCategorySel = $("#flowCategory").ySelect({width: 110, isdisabled:true,json:true,url : BASE_PATH + "m/datadict/dictsel?dictType=FLOW_CLASS",afterLoad: function(){
			//加载流程数据
			loadFlowJsonData();
		}
	});
}

///窗口大小改变时重载流程编辑区域
function reSizeEditArea() {
	var wHeight = $(window).height();
	flowEditObj.reinitSize($("#flowEditDiv").parent().width(), wHeight - 53);
}

//加载流程的JSON数据
function loadFlowJsonData() {
	// 通过flowKey从后台加载
	$.ajax({
		type : "POST",
		cache: false, 
		async : false,
		url  : BASE_PATH + "m/flow_pub/loadflowstr",
		dataType : "json",
		data : {flowInstanceId : $("#flowInstanceId").val()},
		success : function(data){
			if ($.checkErrorMsg(data) ) {
				flowInfoData = eval('('+data.flowJson+')');
				//初始化流程设计器
				initGooFlow();
			}
        }
	});
//	return {category : "BXLC", description : "DESC", metas : [], events : {}, title:"编辑流程", nodes:{START_NODE:{name:"开始",left:300,top:50,type:"start round",width:30,height:30},END_NODE:{name:"结束",left:300,top:300,type:"end round",width:30,height:30},NODE1:{name:"节点1",left:300,top:200,type:"task",width:90,height:40}},lines:{},areas:{}};
}

//初始化流程设计器
function initGooFlow(){
	//禁用流程类别选择框与按钮
	flowCategorySel.select(flowInfoData.category);
	
	var wHeight = $(window).height();
	
   	var property={
		width: $("#flowEditDiv").parent().width(),
		height: wHeight - 53,
		toolBtns:[],
		haveHead:true,
		headBtns:["expand","shrink"],
		haveTool:false,
		haveGroup:true,
		useOperStack:false
	};
	var remark={
		expand:"扩大工作区域",
		shrink:"缩小工作区域"
	};
	//创建对象
	flowEditObj=$.createGooFlow($("#flowEditDiv"),property);
	flowEditObj.setNodeRemarks(remark);
	//加载新增时默认数据
	flowEditObj.loadData(flowInfoData);
	////加载流程实例已执行过的节点或转向
	loadFlowTracePoint();
}

//加载流程实例已执行过的节点或转向
function loadFlowTracePoint() {
	// 通过flowKey从后台加载
	$.ajax({
		type : "POST",
		cache: false, 
		async : false,
		url  : BASE_PATH + "m/flow_pub/loadtrace",
		dataType : "json",
		data : {flowInstanceId : $("#flowInstanceId").val()},
		success : function(data){
			if ($.checkErrorMsg(data) ) {
				var traces = data.flowTracePoints;
                if (traces) {
                	$(traces).each(function(i) {
                		flowEditObj.markItem(traces[i].refGraphKey, traces[i].elementType, true);
                	});
                }
			}
        }
	});
}
