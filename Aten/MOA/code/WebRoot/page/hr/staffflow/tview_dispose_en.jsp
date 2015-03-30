<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title></title>
<script type="text/javascript" src="page/hr/staffflow/tview_dispose_en.js"></script>
<script type="text/javascript" src="page/system/flownotify/comm_notify.js"></script>
</head>
<body class="bdNone">
<div class="tabMid">
<!--请假单-->
  <div class="addCon">
  <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
  	  <tr>
        <th colspan="6" onclick="hideTbody('showFlag1','staffInfo1');"><b id="showFlag1">-</b> 基本信息</th>
      </tr>
	  <tbody id="staffInfo1">
		  <tr> 	
		 	<td class="tit">申请单号</td>
	        <td id="staffid">${staffFlowApply.id}</td>
	        <td class="tit">申 请 人</td>
	        <td>${staffFlowApply.applicantName}</td>
	        <td class="tit"><span class="cRed" valid="userName">*</span>入职者姓名</td>
	        <td>${staffFlowApply.userName}</td>	
		  </tr>
		  <tr>
	        <td class="tit">人员隶属</td>
		    <td>${staffFlowApply.companyAreaName}</td>
		    <td class="tit"><span class="cRed" valid="groupName">*</span>入职部门</td>
		    <td colspan="3">${staffFlowApply.groupName} &nbsp;<span style="color:#9E9E9E">${staffFlowApply.groupFullName}</span></td>
	      </tr>
	      <tr>
	      	<td class="tit"><span class="cRed" valid="post">*</span>岗 位</td>
	        <td>${staffFlowApply.post}</td>
	        <td class="tit"><span class="cRed" valid="level">*</span>级 别</td>
	        <td>${staffFlowApply.level}</td>
	    	<td class="tit"><span class="cRed" valid="staffStatus">*</span>员工状态</td>
	        <td>${staffFlowApply.staffStatus}</td>
	      </tr>
	      <tr>
	        <td class="tit"><span class="cRed" valid="entryDate">*</span>入职时间</td>
	        <td><fmt:formatDate value="${staffFlowApply.entryDate}" pattern="yyyy-MM-dd"/></td>
	        <td class="tit"><span class="cRed" valid="formalDate">*</span>转正时间</td>
	        <td><fmt:formatDate value="${staffFlowApply.formalDate}" pattern="yyyy-MM-dd"/></td>
	    	<td class="tit"><span class="cRed" valid="employeeNumber">*</span>分配工号</td>
	        <td>${staffFlowApply.employeeNumber}</td>
	      </tr>
	      <tr>
	        <td class="tit"><span class="cRed" valid="idCard">*</span>身份证号</td>
	        <td>${staffFlowApply.idCard}</td>
	    	<td class="tit"><span class="cRed" valid="birthdate">*</span>出生日期</td>
	        <td><fmt:formatDate value="${staffFlowApply.birthdate}" pattern="yyyy-MM-dd"/></td>
	    	<td class="tit"><span class="cRed">*</span>年 龄</td>
	        <td>${staffFlowApply.age}</td>
	      </tr>
	      <tr>
	        <td class="tit"><span class="cRed">*</span>性 别</td>
	        <td>${staffFlowApply.sex == '1' ? '女':'男'}</td>
	    	<td class="tit"><span class="cRed" valid="mobile">*</span>手机号码</td>
	        <td>${staffFlowApply.mobile}</td>
	        <td class="tit">办公电话</td>
	        <td>${staffFlowApply.officeTel}</td>
	      </tr>
	      <tr>
	    	<td class="tit"><span class="cRed" valid="email">*</span>电子邮箱</td>
	        <td>${staffFlowApply.email}</td>
	    	<td class="tit"><span class="cRed" valid="politicalStatus">*</span>政治面貌</td>
	        <td>${staffFlowApply.politicalStatus}</td>
	        <td class="tit"><span class="cRed" valid="nation">*</span>民 族</td>
	        <td>${staffFlowApply.nation}</td>
	      </tr>
	      <tr>
	    	<td class="tit"><span class="cRed">*</span>是否有销售经验</td>
	        <td>${staffFlowApply.salesExperience == '1' ? '是':'否'}</td>
	        <td class="tit"><span class="cRed">*</span>是否有金融从业经验</td>
	        <td>${staffFlowApply.financialExperience == '1' ? '是':'否'}</td>
	    	<td class="tit"><span class="cRed">*</span>是否有金融从业资格</td>
	        <td>${staffFlowApply.financialQualification == '1' ? '是':'否'}</td>
	      </tr>
	      <tr>
	    	<td class="tit"><span class="cRed" valid="workStartDate">*</span>开始工作日期</td>
	        <td><fmt:formatDate value="${staffFlowApply.workStartDate}" pattern="yyyy-MM-dd"/></td>
	        <td class="tit"><span class="cRed" valid="recruitmentType">*</span>招聘方式</td>
	        <td>
	          ${staffFlowApply.recruitmentType}<input id="hidRecruitmentType" type="hidden" value="${staffFlowApply.recruitmentType}"/>
	        </td>
	    	<td class="tit" id="recommendedTD"></td>
	        <td>${staffFlowApply.recommended}</td>
	      </tr>
	      <tr>
	        <td class="tit"><span class="cRed" valid="supervisor">*</span>上级主管</td>
	        <td>${staffFlowApply.supervisor}</td>
	        <td class="tit">司 龄</td>
	        <td>${staffFlowApply.countSeniority}</td>
	    	<td class="tit"><span class="cRed" valid="emergencyContact">*</span>紧急联系人</td>
	        <td>${staffFlowApply.emergencyContact}</td>
	      </tr>
	      <tr>
	    	<td class="tit"><span class="cRed" valid="emergencyContactTel">*</span>紧急联系人电话</td>
	        <td>${staffFlowApply.emergencyContactTel}</td>
	    	<td class="tit"><span class="cRed" valid="bankCardNO">*</span>开户行</td>
	        <td>${staffFlowApply.bankCardNO}</td>
	    	<td class="tit"><span class="cRed" valid="bankType">*</span>银行账号</td>
	        <td>${staffFlowApply.bankType}</td>
	      </tr>
	  </tbody>
      
      <tr>
        <th colspan="6" onclick="hideTbody('showFlag2','staffInfo2');"><b id="showFlag2">-</b> 家庭信息</th>
      </tr>
      <tbody id="staffInfo2">
      	<tr>
	    	<td class="tit"><span class="cRed" valid="nativePlace">*</span>籍 贯</td>
	        <td>${staffFlowApply.nativePlace}</td>
	    	<td class="tit"><span class="cRed" valid="homeAddr">*</span>住 址</td>
	        <td>${staffFlowApply.homeAddr}</td>
	        <td class="tit">邮 编</td>
	        <td>${staffFlowApply.zipCode}</td>
	      </tr>
	      <tr>
	    	<td class="tit"><span class="cRed" valid="domicilePlace">*</span>户口所在地</td>
	        <td>${staffFlowApply.domicilePlace}</td>
	    	<td class="tit"><span class="cRed" valid="domicileType">*</span>户口性质</td>
	        <td>${staffFlowApply.domicileType}</td>
	        <td class="tit"><span class="cRed" valid="maritalStatus">*</span>婚姻状况</td>
	        <td>${staffFlowApply.maritalStatus}</td>
	      </tr>
	      <tr>
	    	<td class="tit"><span class="cRed">*</span>是否生育</td>
	        <td>${staffFlowApply.hadKids == '1' ? '是':'否'}</td>
	        <td colspan="4">
	    	</td>
	      </tr>
      </tbody>
      
      <tr>
        <th colspan="6" onclick="hideTbody('showFlag3','staffInfo3');"><b id="showFlag3">-</b> 教育信息</th>
      </tr>
      <tbody id="staffInfo3">
      	<tr>
	    	<td class="tit"><span class="cRed" valid="education">*</span>学 历</td>
	        <td>${staffFlowApply.education}</td>
	    	<td class="tit"><span class="cRed" valid="finishSchool">*</span>毕业院校</td>
	        <td>${staffFlowApply.finishSchool}</td>
	        <td class="tit"><span class="cRed" valid="professional">*</span>专 业</td>
	        <td>${staffFlowApply.professional}</td>
		  </tr>
		  <tr>
	    	<td class="tit"><span class="cRed" valid="degree">*</span>学 位</td>
	        <td>${staffFlowApply.degree}</td>
	        <td colspan="4">
	    	</td>
	      </tr>
      </tbody>
      
      <tr>
        <th colspan="6" onclick="hideTbody('showFlag4','staffInfo4');"><b id="showFlag4">-</b> 培训情况</th>
      </tr>
      <tbody id="staffInfo4">
		<tr>
	        <td colspan="6"><textarea id="trainInfo" readonly class="area01">${staffFlowApply.trainInfo}</textarea></td>
		</tr>
      </tbody>
      
      <tr>
        <th colspan="6" onclick="hideTbody('showFlag5','staffInfo5');"><b id="showFlag5">-</b> 专业技能信息</th>
      </tr>
      <tbody id="staffInfo5">
		<tr>
	        <td colspan="6"><textarea id="skillsInfo" readonly class="area01">${staffFlowApply.skillsInfo}</textarea></td>
		</tr>
      </tbody>
      
	  <tr>
        <th colspan="6" onclick="hideTbody('showFlag6','staffInfo6');"><b id="showFlag6">-</b> 合同信息</th>
      </tr>
      <tbody id="staffInfo6">
	      <tr>
	    	<td class="tit"><span class="cRed" valid="contractType">*</span>合同类型</td>
	        <td>${staffFlowApply.contractType}</td>
	        <td class="tit"><span class="cRed" valid="contractStartDate">*</span>合同开始时间</td>
	        <td><fmt:formatDate value="${staffFlowApply.contractStartDate}" pattern="yyyy-MM-dd"/></td>
	    	<td class="tit"><span class="cRed" valid="contractEndDate">*</span>合同终止日</td>
	        <td><fmt:formatDate value="${staffFlowApply.contractEndDate}" pattern="yyyy-MM-dd"/></td>
	      </tr>
      </tbody>
      
      <tr>
        <th colspan="6" onclick="hideTbody('showFlag7','staffInfo7');"><b id="showFlag7">-</b> 工作经历</th>
      </tr>
      <tbody id="staffInfo7">
	      <tr>
	        <td colspan="6" class="tipShow" style="padding:0 6px 20px 6px">
	          <div class="costsListHead">
			  	<div style="width:200px;padding:0 0px 20px 0px">明细信息</div>
			  </div>
	      
	          <div class="boxShadow" style="left:5px">
	            <div class="shadow01">
	              <div class="shadow02" >
	                <div class="shadow03" >
	                  <div class="shadow04" >
	                    <div class="shadowCon">
	                      <!--预算信息-->
	                      <div class="ywjgList">
	                        <table width="100%" border="0" cellspacing="1" cellpadding="0">
	                          <tr>
	                            <th width="16%">起始日期</th>
	                            <th width="16%">结束日期</th>
	                            <th width="34%">公司名称</th>
	                            <th width="17%">岗位名称</th>
	                            <th width="17%">岗位职责</th>
	                          </tr>
	                          <tbody id="workList">
					<c:forEach var="work" items="${staffFlowApply.workExperiences}">
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
	                      <!--预算信息 end-->
	                    </div>
	                  </div>
	                </div>
	              </div>
	            </div>
	          </div>
	         </td>
	      </tr>
	  </tbody>
  </table>
  
  <div class="blank"></div>
  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
	<tr>
      <th colspan="2">审批</th>
	</tr>
	<tr>
       <td  class="tit">审批意见</td>
       <td style="position:relative;">
     	<input type="hidden" id="taskInstanceID" value="${param.tiid }"/>
     	<textarea class="area01" id="comment" style="width:630px" ></textarea>
       </td>
	</tr>
    <tr>
	<td  class="tit">操&nbsp;&nbsp;&nbsp;&nbsp;作</td>
	  <td>
		<input type="hidden" id="transitionName"/>
		<span id="commitBut">
	<c:forEach var="transition" items="${transitions}" >
		  <input class="allBtn" type="button" value="${transition}"/>&nbsp;&nbsp;
	</c:forEach>
		</span>
	<c:if test="${not empty param.notify}">   
			<input type="button" value="添加知会人" class="allBtn" onclick="addFlowNotify('${sessionUser.displayName }','审批的入职申请', 'RZLC', '/m/staff_arch?act=toview&id=');" style="margin-left:50px"/>&nbsp;&nbsp;
			<span id="notifyUserNames" style="display:none;color:blue;"></span>
	</c:if>
	  </td>
	</tr>
  </table>
</div>
<!--请假单 end-->
<div class="blank"></div>
<div class="costsLog" id="costsLog">
  <h1 class="logArrow"><a  href="javascript:void(0)">各级审批意见显示</a></h1>
  <div class="mb">
<c:forEach var="task" items="${tasks}" >
	<div class="processBk">
	  <div class="clgcTit">
		<ul class="clearfix">
		  <li><b>处理人：</b>${task.transactorDisplayName }</li>
		  <li><b>收到时间：</b><fmt:formatDate value="${task.createTime}" pattern="yyyy-MM-dd HH:mm"/></li>
		  <li><b>完成时间：</b><fmt:formatDate value="${task.endTime}" pattern="yyyy-MM-dd HH:mm"/></li>
		  <li><b>处理状态：</b>
		<c:if test="${task.taskState=='ps_create'}">
			<img src="themes/comm/images/stateIcoNone.gif"/>[未查看]
		</c:if>
		<c:if test="${task.taskState=='ps_start'}">
			<img src="themes/comm/images/stateIco02.gif"/>[已查看,未处理]
		</c:if>
		<c:if test="${task.taskState=='ps_end'}">
			<img src="themes/comm/images/stateIco01.gif"/>[已处理]
		</c:if>
		  </li>
		</ul>
 	  </div>
	  <div class="taskCon nonebb">
	  <div class="handleCon"> ${task.comment } </div>
	  </div>
	</div>
</c:forEach>
  </div>
</div>
<div class="blank"></div>
</body>
