<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../../base_path.jsp"></jsp:include>
<title></title>
<script type="text/javascript" src="page/hr/staffflow/query/view_resign.js"></script>
</head>
<body class="bdNone">
<div class="tabMid">
<!--请假单-->
  <div class="addCon">
  <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
	<thead>
	  <tr>
		<th colspan="6" onclick="hideTbody('resignShowFlag1','planInfo1');"><b id="resignShowFlag1">-</b> 离职员工信息</th>
	  </tr>
	</thead>
	<tbody id="planInfo1">
	  <tr> 
        <td class="tit"><span class="cRed" valid="userName">*</span>离职员工</td>
        <td>${staffFlowApply.userName}</td>	
        <td class="tit">项 目</td>
        <td>${staffFlowApply.project}</td>
        <td class="tit">培训开始时间</td>
        <td><fmt:formatDate value="${staffFlowApply.tranStartDate}" pattern="yyyy-MM-dd"/></td>
	  </tr>
	  <tr>
      	<td class="tit">培训结束时间</td>
        <td><fmt:formatDate value="${staffFlowApply.tranEndDate}" pattern="yyyy-MM-dd"/></td>
        <td class="tit">培训费用</td>
        <td>${staffFlowApply.tranCost}</td>
        <td class="tit">违约金</td>
        <td>${staffFlowApply.penalty}</td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="staffClass">*</span>员工分类</td>
        <td>${staffFlowApply.staffClass}</td>
        <td class="tit"><span class="cRed" valid="groupName">*</span>所属部门</td>
       	<td colspan="3">${staffFlowApply.groupName} &nbsp;<span style="color:#9E9E9E">${staffFlowApply.groupFullName}</span></td>
      </tr>
      <tr>
        <td class="tit"><span class="cRed" valid="post">*</span>员工岗位</td>
        <td>${staffFlowApply.post}</td>
      	<td class="tit"><span class="cRed" valid="entryDate">*</span>入职时间</td>
        <td><fmt:formatDate value="${staffFlowApply.entryDate}" pattern="yyyy-MM-dd"/></td>
        <td class="tit"><span class="cRed" valid="achievement">*</span>入职累计业绩</td>
        <td>${staffFlowApply.achievement}</td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="resignDate">*</span>申请离职日期</td>
        <td><fmt:formatDate value="${staffFlowApply.resignDate}" pattern="yyyy-MM-dd"/></td>
    	<td class="tit"><span class="cRed" valid="resignType">*</span>离职类型</td>
        <td>${staffFlowApply.resignType}</td>
    	<td class="tit"><span class="cRed" valid="resignReason">*</span>离职原因</td>
        <td>${staffFlowApply.resignReason}</td>
      </tr>
      <tr>
      	<td class="tit"><span class="cRed" valid="resignDesc">*</span>离职原因描述</td>
        <td colspan="5"><textarea readonly id="resignDesc" class="area01">${staffFlowApply.resignDesc}</textarea></td>
      </tr>
	</tbody>
  </table>
  
  <br/>
  <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
	<thead>
	  <tr>
      	<th colspan="6" onclick="hideTbody('resignShowFlag2','planInfo2');"><b id="resignShowFlag2">-</b> 对应入职信息</th>
      </tr>
	</thead>
	<tbody id="planInfo2">
      <tr>
        <td colspan="6" class="tipList">&nbsp;</td>
      </tr>
      <tr>
      	<td colspan="6"  class="tipShow">
		  <span class="crmSubTabsBk">
			<ul class="crmSubTabs" id="crmTab" style="width:700px">
	       	  <li class="current">
	        	<div>基本信息</div>
	       	  </li>
	      	  <li>
	       		<div>家庭信息</div>
			  </li>
			  <li>
				<div>教育信息</div>
			  </li>
			  <li>
				<div>培训情况</div>
			  </li>
			  <li>
				<div>工作经历</div>
			  </li>
			  <li>
				<div>专业技能信息</div>
			  </li>
			  <li>
				<div>合同信息</div>
			  </li>
          	</ul>
		  </span>
          <div class="boxShadow" >
            <div class="shadow01">
              <div class="shadow02" >
                <div class="shadow03" >
                  <div class="shadow04" >
                    <div class="shadowCon">
                      <div id="tab00"> 
                        <!--设备验收单-->
                        <div class="spyjConBk">
                          <div class="spyjCon">
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                              <tr> 	
							 	<td class="tit"><span class="cRed" valid="employeeNumber">*</span>工 号</td>
						        <td>${refStaffFlowApply.employeeNumber}</td>
						        <td class="tit"><span class="cRed" valid="userName">*</span>姓 名</td>
						        <td>${refStaffFlowApply.userName}</td>	
						        <td class="tit"><span class="cRed">*</span>性 别</td>
						        <td>${refStaffFlowApply.sex == '1' ? '女':'男'}</td>
							  </tr>
							  <tr>
							    <td class="tit"><span class="cRed" valid="staffStatus">*</span>员工状态</td>
						        <td>${refStaffFlowApply.staffStatus}</td>
							    <td class="tit"><span class="cRed" valid="groupName">*</span>入职部门</td>
							    <td colspan="3">${refStaffFlowApply.groupName} &nbsp;<span style="color:#9E9E9E">${refStaffFlowApply.groupFullName}</span></td>
						      </tr>
						      <tr>
						      	<td class="tit"><span class="cRed" valid="post">*</span>岗 位</td>
						        <td>${refStaffFlowApply.post}</td>
						        <td class="tit"><span class="cRed" valid="level">*</span>级 别</td>
						        <td>${refStaffFlowApply.level}</td>
						    	<td class="tit"><span class="cRed" valid="entryDate">*</span>入职时间</td>
						        <td><fmt:formatDate value="${refStaffFlowApply.entryDate}" pattern="yyyy-MM-dd"/></td>
						      </tr>
						      <tr>
						        <td class="tit"><span class="cRed" valid="formalDate">*</span>转正时间</td>
						        <td><fmt:formatDate value="${refStaffFlowApply.formalDate}" pattern="yyyy-MM-dd"/></td>
						    	<td class="tit"><span class="cRed" valid="idCard">*</span>身份证号</td>
						        <td>${refStaffFlowApply.idCard}</td>
						    	<td class="tit"><span class="cRed" valid="birthdate">*</span>出生日期</td>
						        <td><fmt:formatDate value="${refStaffFlowApply.birthdate}" pattern="yyyy-MM-dd"/></td>
						      </tr>
						      <tr>
						    	<td class="tit"><span class="cRed">*</span>年 龄</td>
						        <td>${refStaffFlowApply.age}</td>
						        <td class="tit"><span class="cRed" valid="education">*</span>学 历</td>
						        <td>${refStaffFlowApply.education}</td>
						    	<td class="tit"><span class="cRed" valid="finishSchool">*</span>毕业院校</td>
						        <td>${refStaffFlowApply.finishSchool}</td>
						      </tr>
							  <tr>
						        <td class="tit"><span class="cRed" valid="professional">*</span>专 业</td>
						        <td>${refStaffFlowApply.professional}</td>
						    	<td class="tit"><span class="cRed" valid="degree">*</span>学 位</td>
						        <td>${refStaffFlowApply.degree}</td>
						    	<td class="tit"><span class="cRed" valid="contractType">*</span>合同类型</td>
						        <td>${refStaffFlowApply.contractType}</td>
						      </tr>
						      <tr>
						        <td class="tit"><span class="cRed" valid="contractStartDate">*</span>合同开始时间</td>
						        <td><fmt:formatDate value="${refStaffFlowApply.contractStartDate}" pattern="yyyy-MM-dd"/></td>
						    	<td class="tit"><span class="cRed" valid="contractEndDate">*</span>合同终止日</td>
						        <td><fmt:formatDate value="${refStaffFlowApply.contractEndDate}" pattern="yyyy-MM-dd"/></td>
						    	<td class="tit"><span class="cRed" valid="mobile">*</span>手机号码</td>
						        <td>${refStaffFlowApply.mobile}</td>
						      </tr>
						      <tr>
						        <td class="tit">办公电话</td>
						        <td>${refStaffFlowApply.officeTel}</td>
						    	<td class="tit"><span class="cRed" valid="email">*</span>电子邮箱</td>
						        <td>${refStaffFlowApply.email}</td>
						    	<td class="tit"><span class="cRed" valid="politicalStatus">*</span>政治面貌</td>
						        <td>${refStaffFlowApply.politicalStatus}</td>
						      </tr>
						      <tr>
						        <td class="tit"><span class="cRed" valid="nation">*</span>民 族</td>
						        <td>${refStaffFlowApply.nation}</td>
						    	<td class="tit"><span class="cRed" valid="nativePlace">*</span>籍 贯</td>
						        <td>${refStaffFlowApply.nativePlace}</td>
						    	<td class="tit"><span class="cRed" valid="homeAddr">*</span>住 址</td>
						        <td>${refStaffFlowApply.homeAddr}</td>
						      </tr>
						      <tr>
						        <td class="tit">邮 编</td>
						        <td>${refStaffFlowApply.zipCode}</td>
						    	<td class="tit"><span class="cRed" valid="domicilePlace">*</span>户口所在地</td>
						        <td>${refStaffFlowApply.domicilePlace}</td>
						    	<td class="tit"><span class="cRed" valid="domicileType">*</span>户口性质</td>
						        <td>${refStaffFlowApply.domicileType}</td>
						      </tr>
						      <tr>
						        <td class="tit"><span class="cRed" valid="maritalStatus">*</span>婚姻状况</td>
						        <td>${refStaffFlowApply.maritalStatus}</td>
						    	<td class="tit"><span class="cRed">*</span>是否生育</td>
						        <td>${refStaffFlowApply.hadKids == '1' ? '是':'否'}</td>
						    	<td class="tit"><span class="cRed">*</span>是否有销售经验</td>
						        <td>${refStaffFlowApply.salesExperience == '1' ? '是':'否'}</td>
						      </tr>
						      <tr>
						        <td class="tit"><span class="cRed">*</span>是否有金融从业经验</td>
						        <td>${refStaffFlowApply.financialExperience == '1' ? '是':'否'}</td>
						    	<td class="tit"><span class="cRed">*</span>是否有金融从业资格</td>
						        <td>${refStaffFlowApply.financialQualification == '1' ? '是':'否'}</td>
						    	<td class="tit"><span class="cRed" valid="workStartDate">*</span>开始工作日期</td>
						        <td><fmt:formatDate value="${refStaffFlowApply.workStartDate}" pattern="yyyy-MM-dd"/></td>
						      </tr>
						      <tr>
						        <td class="tit"><span class="cRed" valid="supervisor">*</span>上级主管</td>
						        <td>${refStaffFlowApply.supervisor}</td>
						    	<td class="tit"><span class="cRed" valid="recruitmentType">*</span>招聘方式</td>
						        <td>
						          ${refStaffFlowApply.recruitmentType}<input id="hidRecruitmentType" type="hidden" value="${refStaffFlowApply.recruitmentType}"/>
						        </td>
						    	<td class="tit" id="recommendedTD"></td>
						        <td>${refStaffFlowApply.recommended}</td>
						      </tr>
						      <tr>
						        <td class="tit">司 龄</td>
						        <td>${staffFlowApply.countSeniority}年</td>
						    	<td class="tit"><span class="cRed" valid="emergencyContact">*</span>紧急联系人</td>
						        <td>${refStaffFlowApply.emergencyContact}</td>
						    	<td class="tit"><span class="cRed" valid="emergencyContactTel">*</span>紧急联系人电话</td>
						        <td>${refStaffFlowApply.emergencyContactTel}</td>
						      </tr>
						      <tr>
						    	<td class="tit"><span class="cRed" valid="bankCardNO">*</span>开户行</td>
						        <td>${refStaffFlowApply.bankCardNO}</td>
						    	<td class="tit"><span class="cRed" valid="bankType">*</span>银行账号</td>
						        <td colspan="3">${refStaffFlowApply.bankType}</td>
						      </tr>
                            </table>
                          </div>
                        </div>
                        <!--设备验收单 end--> 
                      </div>
                      <div id="tab01"  style="display:none"> 
                        <div class="spyjConBk">
                          <div class="spyjCon">
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                              <tr>
						      	<td class="tit"><span class="cRed" valid="nativePlace">*</span>籍 贯</td>
						        <td>${refStaffFlowApply.nativePlace}</td>
						    	<td class="tit"><span class="cRed" valid="homeAddr">*</span>住 址</td>
						        <td>${refStaffFlowApply.homeAddr}</td>
						        <td class="tit">邮 编</td>
						        <td>${refStaffFlowApply.zipCode}</td>
						      </tr>
						      <tr>
						      	<td class="tit"><span class="cRed" valid="domicilePlace">*</span>户口所在地</td>
						        <td>${refStaffFlowApply.domicilePlace}</td>
						    	<td class="tit"><span class="cRed" valid="domicileType">*</span>户口性质</td>
						        <td>${refStaffFlowApply.domicileType}</td>
						        <td class="tit"><span class="cRed" valid="maritalStatus">*</span>婚姻状况</td>
						        <td>${refStaffFlowApply.maritalStatus}</td>
						      </tr>
						      <tr>
						      	<td class="tit"><span class="cRed">*</span>是否生育</td>
						        <td colspan="5">${refStaffFlowApply.hadKids == '1' ? '是':'否'}</td>
						      </tr>
                            </table>
                          </div>
                        </div>
                      </div>
                      <div id="tab02"  style="display:none"> 
                        <div class="spyjConBk">
                          <div class="spyjCon">
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                              <tr>
						      	<td class="tit"><span class="cRed" valid="education">*</span>学 历</td>
						        <td>${refStaffFlowApply.education}</td>
						    	<td class="tit"><span class="cRed" valid="finishSchool">*</span>毕业院校</td>
						        <td>${refStaffFlowApply.finishSchool}</td>
						        <td class="tit"><span class="cRed" valid="professional">*</span>专 业</td>
						        <td>${refStaffFlowApply.professional}</td>
						      </tr>
						      <tr>
						      	<td class="tit"><span class="cRed" valid="degree">*</span>学 位</td>
						        <td colspan="5">${refStaffFlowApply.degree}</td>
						      </tr>
                            </table>
                          </div>
                        </div>
                      </div>
                      <div id="tab03"  style="display:none"> 
                        <div class="spyjConBk">
                          <div class="spyjCon">
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                              <tr>
						        <td>
						          <textarea id="trainInfo" readonly class="area01" style="height:250px">${refStaffFlowApply.trainInfo}</textarea>
						        </td>
						      </tr>
                            </table>
                          </div>
                        </div>
                      </div>
                      <div id="tab04"  style="display:none"> 
                        <!--设备流程记录-->
                        <div class="ywjgList">
                          <table width="100%" border="0" cellspacing="1" cellpadding="0">
                          <thead>
                          	<tr>
                              <th width="16%">起始日期</th>
                              <th width="16%">结束日期</th>
                              <th width="34%">公司名称</th>
                              <th width="17%">岗位名称</th>
                              <th width="17%">岗位职责</th>
                              </tr>
                          </thead>
                          <tbody id="workList">
				<c:forEach var="work" items="${refStaffFlowApply.workExperiences}">
			        		<tr onmouseover="$(this).addClass('over');" onmouseout="$(this).removeClass('over');">
							  <td><fmt:formatDate value="${work.startDate}" pattern="yyyy-MM-dd"/></td>
							  <td><fmt:formatDate value="${work.endDate}" pattern="yyyy-MM-dd"/></td>
							  <td>${work.companyName}</td>
							  <td>${work.postName}</td>
							  <td>${work.postDuty}</td>
							</tr>
				</c:forEach>                          
                          </tbody>
                          </table>
                        </div>
                        <!--设备流程记录--> 
                      </div>
                      <div id="tab05"  style="display:none"> 
                        <div class="spyjConBk">
                          <div class="spyjCon">
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                              <tr>
						        <td>
						          <textarea id="skillsInfo" readonly class="area01" style="height:250px">${refStaffFlowApply.skillsInfo}</textarea>
						        </td>
						      </tr>
                            </table>
                          </div>
                        </div>
                      </div>
                      <div id="tab06" style="display:none"> 
                        <!--设备信息更新记录-->
                        <div class="spyjConBk">
                          <div class="spyjCon">
                          	<table width="100%" border="0" cellspacing="1" cellpadding="0">
                              <tr>
						      	<td class="tit"><span class="cRed" valid="contractType">*</span>合同类型</td>
						        <td>${refStaffFlowApply.contractType}</td>
						        <td class="tit"><span class="cRed" valid="contractStartDate">*</span>合同开始时间</td>
						        <td><fmt:formatDate value="${refStaffFlowApply.contractStartDate}" pattern="yyyy-MM-dd"/></td>
						    	<td class="tit"><span class="cRed" valid="contractEndDate">*</span>合同终止日</td>
						        <td><fmt:formatDate value="${refStaffFlowApply.contractEndDate}" pattern="yyyy-MM-dd"/></td>
						      </tr>
                            </table>
                          </div>
                        </div>
                        <!--设备信息更新记录--> 
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div></td>
      </tr>
	</tbody>
  </table>
</div>
</body>
