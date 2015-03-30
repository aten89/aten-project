var mainFrame = $.getMainFrame();
var dialogWin;
var yearSelect;
$(initInventoryEquipment);
//-----------------------全局变量------------------------
var columnSortObj;//表头列排序对像
var trunPageObj;//翻页组件对像
var gsType;
var ssPianQu;
var ssHangYe;
/**
 * 库存设备查询
 */
function initInventoryEquipment() {
	//添加权限约束
	$.handleRights({
		"queryEquipment":$.SysConstants.QUERY,
		"exportExcel" : $.SysConstants.EXPORT//登记
	});
	getDeviceTypeList();
	getDeviceClassDiv();
	getStatusDiv();
  makeDateSelect();
  //初始化表头列排序
  columnSortObj = $("#tableTH").columnSort();
  initListHead(columns);//调用FieldSetComm.js 里的方法
  
  gotoPage(1);
  //刷新
	$("#queryEquipment").click(function(){
		$("#deviceClassDiv").hide();
		$("#deviceClassDiv0").hide();
		$("#deviceClassDiv2").hide();
		$("#deviceClassDiv3").hide();
		 gotoPage(1);
	});
	//导出excel
	$("#exportExcel").click(function(){
		$("#deviceClassDiv").hide();
		$("#deviceClassDiv0").hide();
		$("#deviceClassDiv2").hide();
		$("#deviceClassDiv3").hide();
		exportExcle();
  	});
}
//导出EXCLE
function exportExcle(){
	//对排序参数的处理
	var sortCol = columnSortObj.getSortColumn();
	var ascend =  columnSortObj.getAscend();
  	
	var deviceTypeIds = $("#deviceClassDiv_ids").val();
	var areaCodeIds = $("#deviceClassDiv0_ids").val();
	var deviceClassIds = $("#deviceClassDiv2_ids").val();
	var statuses = $("#deviceClassDiv3_ids").val();
	var year= yearSelect.getValue();
	var stratBuyTime=  $("#stratBuyTime").val();
	var endBuyTime=  $("#endBuyTime").val();
	var expNameAndValue = $("#expNameAndValue").val(); 
	$.ajax({type:"POST",
		 cache:false, 
		 url:"m/query_statistic", 
		 data:{
			 act:"export",
			 deviceTypeIds : deviceTypeIds,
			 areaCodeIds:areaCodeIds,
			 deviceClassIds:deviceClassIds,
			 statuses : statuses,
			 year : year,
			 stratBuyTime : stratBuyTime,
			 endBuyTime : endBuyTime,
			 sortCol : sortCol,
			 ascend : ascend,
			 expNameAndValue:expNameAndValue
		 },
        success : function(xml){
            //解析XML中的返回代码
            var message = $("message",xml);
            if(message.attr("code") == "1"){
            	$.openDownloadDialog(BASE_PATH + $("message",xml).text());
            }
            else{
               $.alert($("message",xml).text());
            };
        },
        error : $.ermpAjaxError
    });
}
function saveInfo(){
	window.close();	
}

function doclose(){
	window.close();	
}
function makeDateSelect() {
	var	d = new Date();
	var year=d.getFullYear();
	
	var html="<div>**所有...</div>";
	for(i=-1; i<10; i++) {
		html+="<div " + (i==0?"isselected='true'":"") + ">"+ (year-i)+"**"+(year-i)+"</div>";	
	}
	$("#yearSelectDiv").html(html);
	yearSelect=$("#yearSelectDiv").ySelect({width : 80,name:"yearSelectDiv"});
};
/**
 * 分页
 **/
function gotoPage(pageNo, totalPage){
    if(isNaN(pageNo)){
        $.alert("页索引只能为数字！");
        return;
    } else if(pageNo < 1){
        pageNo = 1;
    }
    if (totalPage && !isNaN(totalPage) && pageNo > totalPage) {
    	pageNo = totalPage;
    }
    $("#hidNumPage").val(pageNo);
    queryList();
};

function closeDialog() {
	if (dialogWin) {
		dialogWin.close();
	}
}

//定制字段
function fieldDisplaySet(){
	$("#deviceClassDiv").hide();
	$("#deviceClassDiv0").hide();
	$("#deviceClassDiv2").hide();
	$("#deviceClassDiv3").hide();
	columns.pageNo=$("#hidPageCount").val();
//	var sFeature = "dialogHeight:330px;dialogWidth:508px;status:no;scroll:auto;help:no";
//	if($.browser.msie && $.browser.version==6.0){
//		sFeature = "dialogHeight:385px;dialogWidth:516px;status:no;scroll:auto;help:no";
//	}
	window.dialogParams = columns;
	dialogWin = $.showModalDialog("定制字段显示", BASE_PATH + "page/device/statistics/set_field.jsp", 508, 330, function(){
		$("#hidPageCount").val(columns.pageNo);
		initListHead(columns);
		gotoPage(1)
	});
	
//	window.showModalDialog(BASE_PATH + "page/device/statistics/set_field.jsp",columns,sFeature);
//	$("#hidPageCount").val(columns.pageNo);
//	initListHead(columns);
//	gotoPage(1)
}
function initListHead(columns) {
	var expNameAndValue = "";
	var html="<table id=\"DeviceList\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" ><thead id=\"custHeadTr\"><tr>";
	for(var i=0; i<columns.length; i++){
		var column = columns[i];
		if(column.defaultShow){
			html +="<th ";
			if (column.columnSort) {//要列排序
				html += "sort='" + column.value + "' ";
			}
			html += "width=\""+column.width+"\">"+column.name+"</th>";
			expNameAndValue+=column.name+","+column.column+";"
		}
	}
	html +="<th width=\"25\"><div class=\"oprateW\">操作</div></th></tr></thead><tbody></tbody></table>";
	$("#divTable").html(html);
     //初始化表头列排序
    columnSortObj = $("#custHeadTr").columnSort();
   $("#expNameAndValue").val(expNameAndValue); 
}

//设备类别
function deviceClass(parent,divId){
	if(divId!="deviceClassDiv"){
		$("#deviceClassDiv").hide();
	}
	if(divId!="deviceClassDiv0"){
		$("#deviceClassDiv0").hide();
	}
	if(divId!="deviceClassDiv2"){
		$("#deviceClassDiv2").hide();
	}
	if(divId!="deviceClassDiv3"){
		$("#deviceClassDiv3").hide();
	}
	if($("#"+divId+"_name").val()=="所有..."){
		 $("input[name=exp]","#"+divId+"_span").each(function() {
       		 $(this).attr("checked", true);
    	});
    	$("input[name=exp1]","#"+divId+"_span").each(function() {
       		 $(this).attr("checked", true);
    	});
	}else{
		var flag = true;
		 $("input[name=exp]","#"+divId+"_span").each(function() {
		 	var ids = $("#"+divId+"_ids").val();
		 	if (ids.indexOf(","+this.value+",")!=-1){
		 		 $(this).attr("checked", true);
		 	}else{
		 		flag=false;
		 		$(this).attr("checked", false);
		 	}
    	});
    	if(flag){
    		$("input[name=exp1]","#"+divId+"_span").each(function() {
	       		 $(this).attr("checked", true);
	    	});
    	}else{
    		$("input[name=exp1]","#"+divId+"_span").each(function() {
	       		 $(this).attr("checked", false);
	    	});
    	}
    	
	}
   
	divObj = $("#" + divId);
	parentObj = $("#" + parent);
	if(divObj.is(":hidden")){
			var expTop = parentObj.offset().top + 24;
			var expLeft = parentObj.offset().left + 5;
			divObj.show().css({position:"absolute","top":expTop+"px" ,"left":expLeft+"px"});
		}else{
			divObj.hide();
		}
	
}

function saveDevice(dcDiv){
	if(!checkDeleteValue(dcDiv)){
		return;
	}
	$("#" + dcDiv).hide();
	if(!(dcDiv=="deviceClassDiv2" || dcDiv=="deviceClassDiv3")){
		getDeviceClassDiv();
	}
	
}
function closeDevice(dcDiv){
	$("#" + dcDiv).hide();
}
//获取选中的值
function checkDeleteValue(dcDiv){
	//获得参数
	var flag=true;
	var name="";
	var ids=",";
	$("input[name='exp'][checked]", "#"+dcDiv).each(function(){
		if( this.checked == true ){
			ids+=this.value+",";
			if(flag){
				name = $.trim($("#"+dcDiv+this.value).text());
				flag=false;
			}else{
				name += "；"+$.trim($("#"+dcDiv+this.value).text());
			}
		}
	});
	
	if(ids==","){
		if(dcDiv=="deviceClassDiv0"){
			 $.alert("请选择设备财务隶属");
			 return false;
		}
		if(dcDiv=="deviceClassDiv"){
			 $.alert("请选择设备资产类别");
			return false;
		}
		if(dcDiv=="deviceClassDiv2"){
			 $.alert("请选择设备类别");
			 return false;
		}
		if(dcDiv=="deviceClassDiv3"){
			  $.alert("请选择设备状态");
			 return false;
		}
	}
	if(!(dcDiv=="deviceClassDiv2" || dcDiv=="deviceClassDiv3")){
		$("#deviceClassDiv2_name").val("所有...");
		$("#deviceClassDiv2_ids").val("");
	}
	
	$("#"+dcDiv+"_name").val(name);
	$("#"+dcDiv+"_ids").val(ids);
	return true;
}
/**
 * 查询资产类别
 */
function getDeviceTypeList() {
	$.ajax({
		type : "post",
		cache : false,
		url : BASE_PATH + "m/data_dict?act=divdevicetype",
		success : function(xml){
			var message = $("message",xml);
			var content = $("content",xml);
			var listData = "<input type=\"checkbox\" id=\"dttype\" class=\"cBox\" name=\"exp1\" onclick=\"checkAll('dttype','deviceClassDiv_span') \"/><label for=\"dttype\"><span>全部</span></label><br />";
			//是否有数据返回
		    if (message.attr("code") == "1") {
		    	
		    	$(xml).find('dataDict').each(function(index){
		    		var curELe = $(this);
                    var id =  $("data-value",curELe).text() ;
                    listData += "<input type=\"checkbox\" id=\"dt01"+id+"\" class=\"cBox\" name=\"exp\" value=\""+id+"\" onclick=\"deletecheckAll('dc01"+id+"','deviceClassDiv_span')\"/><label for=\"dt01"+id+"\"><span id=\"deviceClassDiv"+id+"\">"+ $("data-key",curELe).text() +"</span></label><br />";

				});
				$("#deviceClassDiv_span").html(listData);
		    }
		},
		error : $.ermpAjaxError
	});
}
/**
 * 查询设备类别
 */
function getDeviceClassDiv() {
	var deviceTypeIds = $("#deviceClassDiv_ids").val();
	var areaCodeIds = $("#deviceClassDiv0_ids").val();
	$.ajax({
		type : "post",
		cache : false,
		url : BASE_PATH + "m/device_class?act=classsqueryassign",
		data : {
			deviceTypeIds : deviceTypeIds,
			areaCodeIds : areaCodeIds,
			assignType:"1"
		},
		success : function(xml){
			var message = $("message",xml);
			var content = $("content",xml);
			var listData = "<input type=\"checkbox\" id=\"dcAll\" class=\"cBox\" name=\"exp1\" onclick=\"checkAll('dcAll','deviceClassDiv2_span') \"/><label for=\"dcAll\"><span>全部</span></label><br />";
			//是否有数据返回
		    if (message.attr("code") == "1") {
		    	
		    	$(xml).find('device-class').each(function(index){
		    		var curELe = $(this);
		    		var id=curELe.attr("id");
                    listData += "<input type=\"checkbox\" id=\"dc01"+id+"\" class=\"cBox\" name=\"exp\" value=\""+id+"\" onclick=\"deletecheckAll('dc01"+id+"','deviceClassDiv2_span')\"/><label for=\"dc01"+id+"\"><span id=\"deviceClassDiv2"+id+"\">"+ $("class-name",curELe).text() +"</span></label><br />";

				});
				$("#deviceClassDiv2_span").html(listData);
		    }
		},
		error : $.ermpAjaxError
	});
}

/**
 * 设备状态
 */
function getStatusDiv() {
	$.ajax({
		type : "post",
		cache : false,
		url : BASE_PATH + "m/it_dev_man?act=getstatuslist",
		success : function(xml){
			var message = $("message",xml);
			var content = $("content",xml);
			var listData = "<input type=\"checkbox\" id=\"dcStatus\" class=\"cBox\" name=\"exp1\" onclick=\"checkAll('dcStatus','deviceClassDiv3_span') \"/><label for=\"dcStatus\"><span>全部</span></label><br />";
			//是否有数据返回
		    if (message.attr("code") == "1") {
		    	
		    	$(xml).find('status').each(function(index){
		    		var curELe = $(this);
		    		var name= $("status-name",curELe).text();
		    		var value= $("status-value",curELe).text()
                    listData += "<input type=\"checkbox\" id=\"ds01"+value+"\" class=\"cBox\" name=\"exp\" value=\""+value+"\" onclick=\"deletecheckAll('ds01"+value+"','deviceClassDiv3_span')\"/><label for=\"ds01"+value+"\"><span id=\"deviceClassDiv3"+value+"\">"+ name +"</span></label><br />";

				});
				$("#deviceClassDiv3_span").html(listData);
		    }
		},
		error : $.ermpAjaxError
	});
}
function queryList(){
	$("#DeviceList > tbody").empty();
	//对排序参数的处理
	var sortCol = columnSortObj.getSortColumn();
	var ascend =  columnSortObj.getAscend();
	var pageno = $.trim($("#hidNumPage").val());
  	var pagecount = $.trim($("#hidPageCount").val());
  	
	var deviceTypeIds = $("#deviceClassDiv_ids").val();
	var areaCodeIds = $("#deviceClassDiv0_ids").val();
	var deviceClassIds = $("#deviceClassDiv2_ids").val();
	var statuses = $("#deviceClassDiv3_ids").val();
	var year= yearSelect.getValue();
	var stratBuyTime=  $("#stratBuyTime").val();
	var endBuyTime=  $("#endBuyTime").val();
	
	$.ajax({type:"POST",
		 cache:false, 
		 url:"m/query_statistic", 
		 data:{
			 act:"query",
			 deviceTypeIds : deviceTypeIds,
			 areaCodeIds:areaCodeIds,
			 deviceClassIds:deviceClassIds,
			 statuses : statuses,
			 year : year,
			 stratBuyTime : stratBuyTime,
			 endBuyTime : endBuyTime,
			 sortCol : sortCol,
			 ascend : ascend,
			 pageNo : pageno,
			 pageSize : pagecount
		 },
		 success:function (xml) {
            //解析XML中的返回代码
			var message = $("message", xml);
			if (message.attr("code") == "1") {
				var tBodyHTML = "";
				$("device", xml).each(function (index) {
					var deviceConfig = $(this);
					var id = deviceConfig.attr("id");
					tBodyHTML += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
					for(var j=0; j< columns.length; j++){
						if(columns[j].defaultShow == true){
							tel = "";
							cls = "listData";
							var cn = columns[j].column
							tBodyHTML +="<td class=\""+cls+"\">" +$.trim($(cn, deviceConfig).text()), +"</td>";
						}
					}
					var op =  "";
						op += ( $.hasRight($.SysConstants.VIEW)?"<a href=\"javascript:void(0);\" onclick=\"openDetail('"+ id +"')\"  class=\"opLink\">查看</a>":"");
					tBodyHTML += "<td>" + op + " </td> </tr>";
				});
				$("#DeviceList > tbody").html(tBodyHTML)
				
		        //------------------翻页数据--------------------------
	            $(".pageNext").html($.createTurnPage(xml));
	            $.EnterKeyPress($("#numPage"),$("#numPage").next());
	            
			} else {
				$.alert($("message", xml).text());
			}
		}, error:$.ermpAjaxError});
}
//详情
function openDetail(id){
	mainFrame.addTab({
			id:"oa_equipmentInfo"+id,
			title:"查看设备信息",
			url:BASE_PATH +"m/it_dev_man?act=view&id="+id
		});
}


//全选
function checkAll(name,dcDiv){
	if ($("#"+name).attr("checked")) {
        $("input[name=exp]","#"+dcDiv).each(function() {
            $(this).attr("checked", true);
        });
    } else {
        $("input[name=exp]","#"+dcDiv).each(function() {
            $(this).attr("checked", false);
        });
    }
	
}

//全选
function deletecheckAll(name,dcDiv){
	if (!$("#"+name).attr("checked")) {
         $("input[name=exp1]","#"+dcDiv).each(function() {
            $(this).attr("checked", false);
        });
    }
	
}

