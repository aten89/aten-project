//添加设备验收单
var args = parent.window.dialogParams;
$(initEquipmentReceivingReport);

function initEquipmentReceivingReport() {
	initCheckFormTiem();
	$("#remark").val(args.remark);
}
function saveInfo(){
	savevalidateForm();
}

function doclose(){
	parent.closeDialog();	
}

function displayOrhide(vv){
	if(vv == "yk" && $("#" + vv)[0].checked){
			$("#je").show();
	}
	if(vv == "wk" && $("#" + vv)[0].checked){
			$("#je").hide();
	}
}	

/**
 * 提交保存
 */
function savevalidateForm(){   
	var html =checkFormTiemInfo();
	if(!html){
		return;
	}
	args.valiHtml = html;//
	
	parent.window.returnValue1 = true;
   parent.closeDialog();	
}

//组装验收单明细
function checkFormTiemInfo(){
	var html="";
	var applicant = $.trim($("#applicant").val());
	var valiDate = $.trim($("#valiDate").text());
	var remark = $.trim($("#remark").val());
	var flag=false;
	
	$("#valiTab").find("tr").each(function(){
		html+="<span name=\"valiSpan\"> "
		var tdobj=$("td",this);
		var itemName = $("td",this).eq(0).text();
//		var itemRemark = $("td",this).eq(2).text();
		var itemRemark = $("input",tdobj.eq(2)).eq(0).val();
		var isEligibility=0;
		if($("input",$("td",this).eq(1)).eq(0).attr('checked')){
			isEligibility=1;
		}
		result = $.validChar(itemRemark,"\"<>");
		if (result){
			$.alert("检查项“"+itemName+"”的备注不能输入非法字符：" + result+"！");
			flag=true;
			return false;
		}
		if(itemRemark.length>300){
			$.alert("检查项”"+itemName+"“的备注不能超过100个字符！");
			flag=true;
			return false;
		}
		
		html+="<span name=\"itemName\">"+itemName+"</span>";
		html+="<span name=\"isEligibility\">"+isEligibility+"</span>";
		html+="<span name=\"itemRemark\">"+itemRemark+"</span>";
		html+="</span> "
	});
	html+="<span name=\"applicant\">"+applicant+"</span>";
	html+="<span name=\"valiDate\">"+valiDate+"</span>";
	if(flag){
		return false;
	}
	result = $.validChar(remark,"\"<>");
	if (result){
		$.alert("检查项”备注“不能输入非法字符：" + result+"！");
		$("#remark").focus();
		return false;
	}
	if(remark.length>300){
		$.alert("检查项”备注“不能超过300个字符！");
		$("#remark").focus();
		return false;
	}
	html+="<span name=\"remark\">"+remark+"</span>";
	return html;
}
//组装验收单明细
function initCheckFormTiem(){
	if(typeof(args.valiTiems)=="undefined"){
		return;
	}
	if ($("#valiTab").text() == "") {
		for (var i=0 ; i<args.valiTiems.length ; i++){
			var itemName = args.valiTiems[i].itemName;
			var itemRemark = args.valiTiems[i].itemRemark;
			var isEligibility = args.valiTiems[i].isEligibility;
			var html="<tr class=\"valilist\"><td>"+itemName+"</td>";
			if(isEligibility=="1"){
					html+="<td><input type=\"checkbox\" class=\"cBox\" checked=\"checked\"/></td>";
			}else{
					html+="<td><input type=\"checkbox\" class=\"cBox\"/></td>";
			}
			html+="<td><input type=\"text\"  class=\'ipt05\' style=\"width:386px\"/></td></tr>";
              
			$("#valiTab").append(html) ;
		}
	}else{
		for (var i=0 ; i<args.valiTiems.length ; i++){
			var itemName = args.valiTiems[i].itemName;
			var itemRemark = args.valiTiems[i].itemRemark;
			var isEligibility = args.valiTiems[i].isEligibility;
			var flag = true;
			$("#valiTab").find("tr").each(function(){
				var tdobj=$("td",this).eq(0);
				var itemName_ = $("td",this).eq(0).text();
				if(itemName==itemName_){
					$("td",this).eq(2).html("<input type=\"text\" value=\""+itemRemark+"\"  class=\'ipt05\' style=\"width:386px\"/></td>")
					if(isEligibility=="1"){
						$("td",this).eq(1).html("<input type=\"checkbox\" class=\"cBox\" checked=\"checked\"/>")

					}
					flag = false;
					return false;
				}
			});
			if(flag){
				var html="<tr class=\"valilist\"><td>"+itemName+"</td>";
				if(isEligibility=="1"){
						html+="<td><input type=\"checkbox\" class=\"cBox\" checked=\"checked\"/></td>";
				}else{
						html+="<td><input type=\"checkbox\" class=\"cBox\"/></td>";
				}
				html+="<td><input type=\"text\"  class=\'ipt05\' style=\"width:386px\"/></td></tr>";
                  
				$("#valiTab").append(html) ;
			}
		}
		
	}
	
}

