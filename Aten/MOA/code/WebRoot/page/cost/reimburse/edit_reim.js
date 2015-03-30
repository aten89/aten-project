/*
	group.modifyType用于控制修改模式
		1：新建报销单
		2：驳回修改
		3：部门领导修改
*/
$(initReimburseDraftEdit);
var outlayItemHtml = "";

//当前是否为编辑状态
var edit=false;
var reimItemDraft = null;

function initReimburseDraftEdit(){
	
	reimItemDraft = parent.reimItemDraft;
	
	$("#close").click(function(){
		parent.closeDialog();
	});
	
	$("#openSaveInfo").click(function(){
		saveGroup();
	});
               
    //添加费用明细
    $("#costsAdd").click(function(){
		addItem();
	});
	
	//打开用户帐号
	
   $("#openSelect").click(function(e){
			var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
			var userNames = $("#coterielList").val().split(",");
			for (var i=0 ; i<userNames.length ; i++){
				if (userNames[i]!=""){
					dialog.appendSelected({name:userNames[i],id:""});
				}
			}
			dialog.setCallbackFun(function(selected){
				if (selected != null) {
					var selectedName = "";
					for (var i=0 ; i<selected.length ; i++){
						if (selectedName==""){
							selectedName += selected[i].name;
						}else{
							selectedName += "," + selected[i].name;
						}
					}
					$("#coterielList").val(selectedName);
					$("#coterielList").focus();
				}
			});
			dialog.openDialog("multi");
	});

	initReimItemDraft();
	
};

function initReimItemDraft(){
	$("#travelPlace").val(reimItemDraft.travelPlace);
	$("#travelBeginDate").val(reimItemDraft.travelBeginDate);
	$("#travelEndDate").val(reimItemDraft.travelEndDate);
	$("#customName").val(reimItemDraft.customName);
	$("#regionName").val(reimItemDraft.regionName);
	$("#coterielList").val(reimItemDraft.coterielList);
	if(reimItemDraft.outlayLists) {
		var html = "";
		for(var i=0 ; i < reimItemDraft.outlayLists.length; i++ ){
			var outlays = reimItemDraft.outlayLists[i];
			html += createHtml(outlays.id, outlays.outlayCategory, outlays.outlayName, outlays.documetNum, outlays.outlaySum, outlays.description);
		}
		$("#outlayListDrafts").append(html);
	}
};

/**
 * 添加费用明细
 */
function addItem() {
	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}
	
	var addItemHtml = createEditHtml("", "", "", "", "", "");
	$("#outlayListDrafts").append(addItemHtml);
	$("#outlayCategory").removeAttr("disabled").html(createOption("m/rei_start?act=costclass",{}));
	//添加不可用
    $("#costsAdd").attr("disabled","true").addClass("icoNone");
    $("#openSaveInfo").attr("disabled","true").addClass("icoNone");
}

/**
 * 修改
 * @param obj
 */
function modifyItem(obj){
	if (edit) {
		$.alert("请先保存");
		return;
	} else {
		edit = true;
	}
	//添加不可用
    $("#costsAdd").attr("disabled","true").addClass("icoNone");
    $("#openSaveInfo").attr("disabled","true").addClass("icoNone");
    
    var TR = $(obj).parent().parent();
	var id = TR.attr("id");
	// 编辑前html
	outlayItemHtml = "<tr id='"+id+"'>" + TR.html() +"</tr>";
	var outlayCategory = TR.find("td").eq(0).html();
	var outlayName = TR.find("td").eq(1).html();
	var documetNum = TR.find("td").eq(2).html();
	var outlaySum = TR.find("td").eq(3).html();
	var description = TR.find("td").eq(4).html();
	var editItemHtml = createEditHtml(id, outlayCategory, outlayName, documetNum, outlaySum, description);
	TR.replaceWith(editItemHtml);
	$("#outlayCategory").removeAttr("disabled").html(createOption("m/rei_start?act=costclass",{},outlayCategory));
	$("#outlayName").removeAttr("disabled").html(createOption("m/rei_start?act=costitem",{costclass:outlayCategory}, outlayName));
}

/**
 * 保存
 * @param Obj
 * @returns {Boolean}
 */
function saveItem(obj){
	var outlayCategory = $("#outlayCategory").val();
	if (outlayCategory == "") {
		$("#outlayCategory").focus();
		$.alert("请选择费用类别");
		return;
	} 
	var outlayName = $("#outlayName").val();
	if (outlayName == "") {
		$("#outlayName").focus();
		$.alert("请选择费用名称");
		return;
	}
	var documetNum = $("#documetNum").val();
	if(documetNum == ""){
        $.alert("单据数量不能为空！");
        $("#documetNum").focus();
        return;
    }
    var outlaySum = $("#outlaySum").val();
     if(outlaySum == ""){
        $.alert("费用金额不能为空！");
        $("#outlaySum").focus();
        return;
    }
    var desc = $("#desc").val();
    var result = $.validChar(desc,"<>&");
	if (result) {
		$.alert("附加说明不能包含非法字符：" + result);
		$("#desc").focus();
		return;
	}
    if(desc.length > 250) {
    	$.alert("附加说明不能超过250个字符");
    	$("#desc").focus();
    	return false;
    }
    
    var TR = $(obj).parent().parent();
	var id = TR.attr("id");
    
    var html = createHtml(id, outlayCategory, outlayName, documetNum, outlaySum, desc);
    
   	TR.replaceWith(html);
	$("#costsAdd").removeAttr("disabled","true").removeClass("icoNone");
	$("#openSaveInfo").removeAttr("disabled","true").removeClass("icoNone");
//	$("#budgetId").attr("disabled","true").addClass("icoNone");
	edit = false;
	outlayItemHtml = "";
}

/**
 * 取消新增或取消修改
 */
function cancelSaveItem(obj){
	edit = false;
	var TR = $(obj).parent().parent();
	//原行显示
	TR.after(outlayItemHtml);
	// 移除编辑行
	TR.remove();
	// 启用按钮
	outlayItemHtml = "";
	$("#costsAdd").removeAttr("disabled","true").removeClass("icoNone");
	$("#openSaveInfo").removeAttr("disabled","true").removeClass("icoNone");
}

/**
 * 删除
 */
function delItem(obj){
	if (edit) {
		$.alert("请先保存");
		return;
	}
	//将当前行移除.
	var TR = $(obj).parent().parent();
	TR.remove();
}

function saveGroup(){
	
	if($("#outlayListDrafts").find("tr").length==0){
		$.alert("请至少填写一项费用明细!");
		return;		
	}
	
	var travelBeginDate = $("#travelBeginDate").val();
	var travelEndDate = $("#travelEndDate").val();	
	
	if ( $.compareDate(travelBeginDate, travelEndDate)) {
		$.alert("出差开始时间不能大于出差结束时间！");
		return false;
	}
	
	var travelPlace = $("#travelPlace").val();
	var result = $.validChar(travelPlace,"'\"");
	if (result) {
		$.alert("出差地点不能包含非法字符：" + result);
		$("#travelPlace").focus();
		return;
	}
	var coterielList = $("#coterielList").val();
	result = $.validChar(coterielList,"'\"");
	if (result) {
		$.alert("同行名单不能包含非法字符：" + result);
		$("#coterielList").focus();
		return;
	}
	
	var customName = $("#customName").val();
	var regionName = $("#regionName").val();

	reimItemDraft.regionName=regionName;
	reimItemDraft.customName=customName;
	reimItemDraft.travelBeginDate=travelBeginDate;
	reimItemDraft.travelEndDate=travelEndDate;
	reimItemDraft.travelPlace=travelPlace;
	reimItemDraft.coterielList=coterielList;
	
	reimItemDraft.outlayLists = [];

	$("#outlayListDrafts").find("tr").each(function(i){
		
		var id = $.trim($(this).attr("id"));
		outlay = {};
		if (id) {
			outlay.id = id;
		}
		outlay.outlayCategory = $.trim($(this).find("td").eq(0).html());
		outlay.outlayName = $.trim($(this).find("td").eq(1).html());
		outlay.documetNum =parseFloat($.trim($(this).find("td").eq(2).html()),10);
		outlay.outlaySum = parseFloat($.trim($(this).find("td").eq(3).html()),10);
		outlay.description = $.trim($(this).find("td").eq(4).html());
		
		reimItemDraft.outlayLists.push(outlay);
	});

	parent.window.returnValue = true;
	parent.closeDialog();
}

/**
 * 创建编辑行
 * @param id
 * @param outlayCategory
 * @param outlayName
 * @param documetNum
 * @param outlaySum
 * @param description
 * @returns {String}
 */
function createEditHtml(id, outlayCategory, outlayName, documetNum, outlaySum, description) {
	var html = "<tr id='"+(id ? id : "")+"'>";
	html += "<td><select id='outlayCategory' style='width:140px;height:20px' onchange='onSelectoutlayCategory()'  class='sel02' disabled='true'><option value=''>请选择...</option></select></td>";
	html += "<td><select id='outlayName' style='width:140px;height:20px' onchange='addTitleAttr(this)' class='sel02' disabled='true'><option value=''>请选择...</option></select></td>";
	html += "<td><input id='documetNum' value='"+(documetNum ? documetNum : "")+"' onblur='formatDocumetNum(this)'type='text' class='ipt01' style='width:95%' maxlength='4'/></td>";
	html += "<td><input id='outlaySum' value='"+(outlaySum ? outlaySum : "")+"' onblur='formatOutlaySum(this)' type='text' class='ipt01' style='width:95%' maxlength='10'/></td>";
	html += "<td><textarea id='desc' class='area01' style='width:97%;height:38px'>"+(description ? description : "")+"</textarea></td>";
	html += "<td><span id='saveItemlLinkId' class='opLink' onclick='saveItem(this);'>确定</span>&nbsp;<span id='cancelLinkId' class='opLink' onclick='cancelSaveItem(this);' >取消</span></td>";
	html += "</tr>";
	return html;
}

/**
 * 创建只读行
 * @param id
 * @param outlayCategory
 * @param outlayName
 * @param documetNum
 * @param outlaySum
 * @param description
 * @param modifyType
 * @returns {String}
 */
function createHtml(id, outlayCategory, outlayName, documetNum, outlaySum, description) {
	var html = "";
	html += "<tr id=" + (id ? id : "") + ">";
	html += "<td>"+ outlayCategory +"</td>";
	html += "<td>"+ outlayName  +"</td>";
	html += "<td>"+ documetNum  +"</td>";
	html += "<td>"+ outlaySum  +"</td>";
	html += "<td>"+ (description ? description : "")  +"</td>";
   	html += "<td><span onclick='modifyItem(this);' class='opLink'>修改</span>&nbsp;";
   	html += "<span onclick='delItem(this);' class='opLink'>删除</span>";
	html += "</td>";
	html += "</tr>";
	return html;
}

//创建选择框选项
function createOption(url,data, defaultName) {
	var options = "<option value=''>请选择...</option>";
//	if (!defaultName) {
//		defaultName = "请选择...";
//	}
	
	$.ajax({
		type : "POST",
		cache : false,
		async : false,
		url : url,
		data : data,
		success : function(html){
				$("div", "<div>" + html + "</div>").each(function(){
					var str = $(this).html().split("**");
					var selected = defaultName == str[1];
					options += "<option value='" + str[1] + "' title='" + str[1] + "'" + (selected ? "selected" : "") + ">" + str[1] + "</option>";
				});
			},
		error : $.ermpAjaxError
	});
	return options;
}

function onSelectoutlayCategory() {
	var val = $("#outlayCategory").val();
	
	if (val == "") {
		$("#outlayName").val("").attr("disabled", true);
		return;
	}
	
	$("#outlayName").removeAttr("disabled").html(createOption("m/rei_start?act=costitem",{costclass:val}));
}

function formatDocumetNum(o) {
	var num = o.value;
	if (num == "") {
		return;
	} else if (isNaN(num)) {
		$.alert("请输入数字！");
		o.select();
		return;
	} else if (num < 0) {
		$.alert("单据数量不能为负数");
		o.select();
		return;
	} else {
		o.value=parseInt(Number(num));
	}
}

function formatOutlaySum(o) {
	var num = o.value;
	if (num == "") {
		return;
	} else if (isNaN(num)) {
		$.alert("请输入数字！");
		o.select();
		return;
	} else if (num > 10000000) {
		$.alert("费用金额不能超过10000000");
		o.select();
		return;
	} else {
		o.value=Number(num).toFixed(2);
	}
}

/**
 * 添加提示信息
 * @param obj
 */
function addTitleAttr(obj) {
	//添加提示信息
	$(obj).attr("title", obj.options[obj.selectedIndex].text);
}
