var mainFrame = $.getMainFrame();
var holidayApply = null;

$(initVacationApprovalAdd);
function initVacationApprovalAdd(){
	
	//打开用户帐号
	$("#openDeptSelect").click(
	   	function(e){
			var dialog = new DeptDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(retVal){
				if (retVal != null) {
//					$("#deptids").val(retVal.id);
					$("#groupName").val(retVal.name);
					$("#groupFullName").text(retVal.path);
				}
			});
			dialog.openDialog("single");
		}
	);
	
	//提交开始流程
    $("#startFlow").click(function(){
    	saveDraft(true);
    });
    //保存为草稿
    $("#saveAsDraft").click(function(){
    	saveDraft(false);
    });
    
    var selVal = $("#hidLevel").val();
    if (selVal) {
    	$("#level").val(selVal);
    }
    selVal = $("#hidStaffStatus").val();
    if (selVal) {
    	$("#staffStatus").val(selVal);
    }
    selVal = $("#contractType").val();
    if (selVal) {
    	$("#contractTypeSel").val(selVal);
    	if (!$("#contractTypeSel").val()) {
    		$("#contractTypeSel").val("ELSE");
    		$("#contractTypeSpan").show();
    	}
    }
    selVal = $("#hidRecruitmentType").val();
    if (selVal) {
    	$("#recruitmentType").val(selVal);
    	onChangerecruitmentType(selVal);
    }
    
    selVal = $("#hidMaritalStatus").val();
    if (selVal) {
    	$("#maritalStatus").val(selVal);
    }
    selVal = $("#hidPoliticalStatus").val();
    if (selVal) {
    	$("#politicalStatus").val(selVal);
    }
    selVal = $("#hidDomicileType").val();
    if (selVal) {
    	$("#domicileType").val(selVal);
    }
    
    
    
    /**工作经历**/
	//新增工作经历
	$("#workAdd").click(function(){
		addWorkExp();
	});
}

function hideTbody(flagId, dataId) {
	var flag = $("#" + flagId).text();
	if (flag == "-") {
		$("#" + flagId).text("+");
		$("#" + dataId).hide();
	} else {
		$("#" + flagId).text("-");
		$("#" + dataId).show();
	}
}

function onChangecontractType(val) {
	if (val == "ELSE") {
		$("#contractTypeSpan").show();
		$("#contractType").val("");
	} else {
		$("#contractTypeSpan").hide();
		$("#contractType").val(val);
	}
	
}

function onChangerecruitmentType(val) {
	if (val == "网络招聘" || val == "内部推荐") {
//		$("#recommended").show();
//		$("#recommendedTD").text("招聘人");
//	} else if (val == "内部推荐") {
		$("#recommended").show();
		$("#recommendedTD").text("推荐人");
	} else {
		$("#recommended").hide();
		$("#recommendedTD").text("");
	}
}

function onChangeIdCard(iIdNo) {
	iIdNo = $.trim(iIdNo);
	if ((iIdNo.length != 15) && (iIdNo.length != 18)) {
		$.alert("输入的身份证号位数错误");
		return;
	}
	var tmpStr = "";
	if (iIdNo.length == 15) {
		tmpStr = iIdNo.substring(6, 12);
		tmpStr = "19" + tmpStr;
		tmpStr = tmpStr.substring(0, 4) + "-" + tmpStr.substring(4, 6) + "-" + tmpStr.substring(6)
	} else {
		tmpStr = iIdNo.substring(6, 14);
		tmpStr = tmpStr.substring(0, 4) + "-" + tmpStr.substring(4, 6) + "-" + tmpStr.substring(6)
	}
	$("#birthdate").val(tmpStr);
	onChangeBirthdate(tmpStr);
}

function onChangeBirthdate(val) {
	if(!val) {
		return;
	}
	var birYear = parseInt(val.substring(0, 4));
	
	var d=new Date(); 
    var year = d.getFullYear();
	$("#age").val(year - birYear);
}

//提交或保存草稿
function saveDraft(flag){
	if (edit) {
		$.alert("工作经历还在编辑中");
		return;
	} 
	
	var result = true;
	$(".cRed").each(function(){
		var obj = $(this);
		var valid = obj.attr("valid");
		if (valid) {
			if($.validInput(valid, obj.parent().text(), true)){
				result = false;
				return false;
			}
		}
	});
	if (!result) {
		return;
	}
	if($.validNumber("age", "*年 龄", true)){
		return;
	}
	if($.validNumber("seniority", "司 龄", false)){
		return;
	}
	
	var staff = {};
	var staffid = $("#staffid").val();
   	if (staffid) {
   		staff.id = staffid;
   	}
	//将表单信息填写到请假单对象属性
   	var employeeNumber = $("#employeeNumber").val();
   	
	staff.companyArea = $("#companyArea").val();
	staff.groupName = $("#groupName").val();
	staff.userAccountID = employeeNumber;
	staff.post = $("#post").val();
	staff.employeeNumber = employeeNumber;
	staff.userName = $("#userName").val();
	staff.sex = $("input[name='sexOption'][@checked]").val();
	staff.idCard = $("#idCard").val();
	staff.birthdate = $("#birthdate").val();
//	staff.description = $("#description").val();
	staff.applyType = $("#applyType").val();
	staff.entryDate = $("#entryDate").val();
	
	staff.groupFullName = $("#groupFullName").text();
	staff.level = $("#level").val();
	staff.staffStatus = $("#staffStatus").val();
	staff.formalDate = $("#formalDate").val();
	staff.age = $("#age").val();
	staff.education = $("#education").val();
	staff.finishSchool = $("#finishSchool").val();
	staff.professional = $("#professional").val();
	staff.degree = $("#degree").val();
	staff.contractType = $("#contractType").val();
	staff.contractStartDate = $("#contractStartDate").val();
	staff.contractEndDate = $("#contractEndDate").val();
	staff.mobile = $("#mobile").val();
	staff.officeTel = $("#officeTel").val();
	staff.email = $("#email").val();
	staff.politicalStatus = $("#politicalStatus").val();
	staff.nation = $("#nation").val();
	staff.nativePlace = $("#nativePlace").val();
	staff.homeAddr = $("#homeAddr").val();
	staff.zipCode = $("#zipCode").val();
	staff.domicilePlace = $("#domicilePlace").val();
	staff.domicileType = $("#domicileType").val();
	staff.maritalStatus = $("#maritalStatus").val();
	staff.hadKids = $("input[name='hadKidsOption'][@checked]").val();
	staff.salesExperience = $("input[name='salesExperienceOption'][@checked]").val();
	staff.financialExperience = $("input[name='financialExperienceOption'][@checked]").val();
	staff.financialQualification = $("input[name='financialQualificationOption'][@checked]").val();
	staff.workStartDate = $("#workStartDate").val();
	staff.supervisor = $("#supervisor").val();
	staff.recruitmentType = $("#recruitmentType").val();
	staff.recommended = $("#recommended").val();
	staff.seniority = $("#seniority").val();
	staff.emergencyContact = $("#emergencyContact").val();
	staff.emergencyContactTel = $("#emergencyContactTel").val();
	staff.bankCardNO = $("#bankCardNO").val();
	staff.bankType = $("#bankType").val();
	staff.trainInfo = $("#trainInfo").val();
	staff.skillsInfo = $("#skillsInfo").val();
	
	staff.workExperiences = [];
	$("tr","#workList").each(function(){
		var tds = $(this).find("td");

		var detail = {};
		detail.startDate = tds.eq(0).text();
		detail.endDate = tds.eq(1).text();
		detail.companyName = tds.eq(2).text();
		detail.postName = tds.eq(3).text();
		detail.postDuty = tds.eq(4).text();
		
		staff.workExperiences.push(detail);
	});
	
	var jsonStr = $.toJSON(staff);
	
	$("#startFlow,#saveAsDraft").attr("disabled","true");
	
	$.ajax({
       type : "POST",
       cache: false,
       url  : "m/staff_start?act=add" + (flag ? "&flag=Y" : ""),
       data : {json:jsonStr},
       success : function(xml){
           var messageCode = $("message",xml).attr("code");
           if(messageCode == "1"){
           		//添加知会人
           		saveFlowNotify($("message",xml).text());
           		
               $.alert("保存成功", function(){
	                //刷新父列表
	               mainFrame.getCurrentTab().doCallback();
	               //关闭
	               mainFrame.getCurrentTab().close();
               });
              
           } else {
               $.alert($("message",xml).text());
               //设置按钮不可用
           }
           $("#startFlow,#saveAsDraft").removeAttr("disabled");
       },
       error : $.ermpAjaxError
   	});
};



/**工作经历**/
var edit=false;
//新增工作经历
function addWorkExp() {
	$("#workAdd").attr("disabled","true").addClass("icoNone");
	edit = true;
	
	var html ="<tr>" +
	"<td class=\"listData\"><input id=\"startDate\" type=\"text\" readonly style=\"width:66%\" class=\"invokeBoth invokeBoth1\"/></td>" +
	"<td class=\"listData\"><input id=\"endDate\" type=\"text\" readonly style=\"width:66%\" class=\"invokeBoth invokeBoth1\"/></td>" +
	"<td class=\"listData\"><input id=\"companyName\" type=\"text\" style=\"width:95%\" class=\"ipt01\"/></td>" +
	"<td class=\"listData\"><input id=\"postName\" type=\"text\" style=\"width:90%\" class=\"ipt01\"/></td>" +
	"<td class=\"listData\"><input id=\"postDuty\" type=\"text\" style=\"width:90%\" class=\"ipt01\"/></td> ";

	html += "<td class=\"oprateImg\"><a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"saveWorkExp(this)\">确定</a> | <a href=\"javascript:void(0)\" class=\"opLink\" onclick=\"cancelInsert(this)\">取消</a></td>";
	html += "</tr>";
	
	$("#workList").append(html);
	$('.invokeBoth1').attachDatepicker({showOn: 'both', buttonImage: 'themes/comm/spacer.gif', buttonImageOnly: true});
}

//保存
function saveWorkExp(self) {
	var companyName =$.trim($("#companyName").val());
	//判断
	if (!companyName) {
		$.alert("公司名称不能为空");
		return;
	}
	var selfObj=$(self).parent().parent().find("input");
	var html = "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">";
	html +=  "<td>" + selfObj.eq(0).val() + "</td>" 
         + "<td>" + selfObj.eq(1).val() + "</td>"
         + "<td>" + selfObj.eq(2).val() + "</td>"
         + "<td>" + selfObj.eq(3).val() + "</td>"
         + "<td>" + selfObj.eq(4).val() + "</td>"; 
	html += "<td><a href=\"javascript:void(0);\" onclick=\"cancelInsert(this);\" class=\"opLink\">删除</a></td></tr>";
   $(self).parent().parent().replaceWith(html);
   $("#workAdd").removeAttr("disabled", "true").removeClass("icoNone");
   edit = false;
}

function cancelInsert(self) {
	$(self).parent().parent().remove();
	$("#workAdd").removeAttr("disabled","true").removeClass("icoNone");
	edit=false;
}