var dialogWin;
var reimItemDrafts = [];
var reimItemDraft;
var editReimItem = true;

function getJsonStr(reim) {
	//	要拷贝后才能正常转为JSON，可能是对象传到弹出框导致的
	reim.reimItems = [];
	for (var j=0; j<reimItemDrafts.length;j++) {
		var reimItemDraft = {};
		
		if (reimItemDrafts[j].id.substr(0, 5) !="NEWID") {
			reimItemDraft.id=reimItemDrafts[j].id;
		} 
		reimItemDraft.regionName=reimItemDrafts[j].regionName;
		reimItemDraft.customName=reimItemDrafts[j].customName;
		reimItemDraft.travelBeginDate=reimItemDrafts[j].travelBeginDate;
		reimItemDraft.travelEndDate=reimItemDrafts[j].travelEndDate;
		reimItemDraft.travelPlace=reimItemDrafts[j].travelPlace;
		reimItemDraft.coterielList=reimItemDrafts[j].coterielList;
		
		reimItemDraft.outlayLists = [];
		for (var i =0;i<reimItemDrafts[j].outlayLists.length;i++){
			var o = reimItemDrafts[j].outlayLists[i];
			outlay = {};
			outlay.id = o.id;
			outlay.outlayCategory =o.outlayCategory;
			outlay.outlayName = o.outlayName;
			outlay.documetNum =o.documetNum;
			outlay.outlaySum = o.outlaySum;
			outlay.description = o.description;
			
			reimItemDraft.outlayLists.push(outlay);
		}
		reim.reimItems.push(reimItemDraft);
	}
	return $.toJSON(reim);
}

function closeDialog() {
	if (dialogWin) {
		dialogWin.close();
	}
}

function addGroup(){
	reimItemDraft={};
	reimItemDraft.id="NEWID_"+Math.floor(Math.random() * 1000000);
	dialogWin = $.showModalDialog("费用明细", BASE_PATH + "page/cost/reimburse/edit_reim.jsp?req="+(new Date()).getTime(), 845, 440, function(){
		if (window.returnValue) {
			reimItemDrafts.push(reimItemDraft);
		draw();
		}
	});
	
//	var win = window.showModalDialog(,reimItemDraft,"dialogHeight:440px;dialogWidth:860px;status:no;scroll:auto;help:no");
//	if (win){
//		reimItemDrafts.push(reimItemDraft);
//		draw();
//	}
}

function modifyGroup(id){
	reimItemDraft = null;
	//查找对象
	for (var i=0; i<reimItemDrafts.length; i++){
		if (reimItemDrafts[i].id == id){
			reimItemDraft = reimItemDrafts[i];
		}
	}
	if (reimItemDraft != null){
		dialogWin = $.showModalDialog("费用明细", BASE_PATH + "page/cost/reimburse/edit_reim.jsp?req="+(new Date()).getTime(), 845, 440, function(){
			if (window.returnValue) {
				draw();
			}
		});
	
//		var win = window.showModalDialog(BASE_PATH + "page/cost/reimburse/edit_reim.jsp?req="+(new Date()).getTime(),reimItemDraft,"dialogHeight:440px;dialogWidth:860px;status:no;scroll:auto;help:no");
//		if (win){
//			draw();
//		}
	}else{
		$.alert("未找到要修改的组。");
	}
}

function deleteGroup(id){
	$.confirm("此操作将删除整组数据，您确认要删除吗？", function(r){
		if (r) {
			for (var i=0; i<this.reimItemDrafts.length; i++){
				if (reimItemDrafts[i].id == id){
					reimItemDrafts.splice(i,1);
					break;
				}
			}
			draw();
		}
	});
}

function draw (){
	var html = "";
	var reimbursementSum = 0.0;
	if (reimItemDrafts.length == 0){
		html = '<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabLine">'
				+ '<tr>'
				+ '<td colspan="7" class="spDot"><span></span></td>'
				+ '</tr>'
				+ '<tr>'
				+ '<td><font color=red>&nbsp;&nbsp;&nbsp;&nbsp;请点击“新增”按钮，开始填写报销费用明细。</font></td>'
				+ '</tr></table>';
	}else{
		for (var i=0 ; i<reimItemDrafts.length; i++){
			var group = reimItemDrafts[i];

			html += '<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabLine">'
				+ '<tr>'
				+ '<td colspan="7" class="spDot"><span></span></td>'
				+ '</tr>'
				+ '<tr>'
				+ '<td class="spTit"><div>出差日期：</div></td>' 
     			+ '<td>' + (group.travelBeginDate==null || group.travelBeginDate==""?"":group.travelBeginDate) + '</td>'
				+ '<td class="spTit"><div>结束日期：</div></td>' 
				+ '<td>' + (group.travelEndDate==null || group.travelEndDate==""?"":group.travelEndDate) + '</td>'
				+ '<td class="spTit"><div>出差地点：</div></td>'
				+ '<td width="270">' + (group.travelPlace==null || group.travelPlace==""?"":group.travelPlace) + '</td>'
				+ '<td rowspan="3"  class="spOpW">';	
					//判断是否需要编辑按钮
				if (editReimItem){
					html += '<a href="javascript:void(0)" onclick="modifyGroup(\'' + group.id + '\');"><img src="themes/comm/images/spEdit.gif">修改</a>';
		//		}
		//		if (this.options.del){
					html += '<a href="javascript:void(0)" onclick="deleteGroup(\'' + group.id + '\');"><img src="themes/comm/images/spDel.gif"/>删除</a>';
				}
				
			html += '</td>'	
				+ '<tr>'
				+ '<td class="spTit"><div>所属区域：</div></td>' 
     			+ '<td width="15%">' + (group.regionName=="" || group.regionName==null?"无":group.regionName) + '</td>'
				+ '<td width="40" class="spTit"><div>客户名称：</div></td>'
				+ '<td width="15%">' + (group.customName=="" || group.customName==null?"无":group.customName) + '</td>'
				+ '<td class="spTit"><div>同行名单：</div></td>'
				+ '<td>' + (group.coterielList==null || group.coterielList==""?"":group.coterielList) + '</td>'
				+ '</tr>'		
				+ '<tr>'
				+ '<td colspan="7" class="feesList">'
				+ '<div class="boxShadow" ><div class="shadow01"><div class="shadow02" ><div class="shadow03" ><div class="shadow04" ><div class="shadowCon shadowPadd">'
				+ '<table width="100%" border="0" cellspacing="1" cellpadding="0">'
				+ '<tr><th width="15%">费用类别</th><th width="15%">费用名称</th><th width="15%">单据(补贴)数</th><th width="15%">费用金额</th><th width="40%">附加说明</th></tr>';
			
			for (var j=0; j<group.outlayLists.length ; j++){
				var outlay = group.outlayLists[j];
				html += '<tr>'
					+ '<td>' + (outlay.outlayCategory==null?"":outlay.outlayCategory) + '</td>'
					+ '<td>' + (outlay.outlayName==null?"":outlay.outlayName) + '</td>'
					+ '<td>' + (outlay.documetNum==null?"":outlay.documetNum) + '</td>'
					+ '<td>' + (outlay.outlaySum==null?"":outlay.outlaySum) + '</td>'
					+ '<td>' + (outlay.description==null?"":outlay.description) + '</td>'
					+ '</tr>';
				reimbursementSum += outlay.outlaySum;
			}

			html += '</table>'
				+ '</div></div></div></div></div></div>'
				+ '</td></tr></table>'
				+ '<div class="feesBlank"></div>';
		}
	}
	$(".feesSp").html(html);
	$("#outlaySumShow").html(reimbursementSum.toFixed(2));
}