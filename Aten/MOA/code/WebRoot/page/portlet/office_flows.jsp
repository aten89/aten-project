<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <head>
	<jsp:include page="../base_path.jsp"></jsp:include>
	<meta http-equiv="Content-Language" content="en" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>企业资源管理系统</title>
	<script>
		var mainFrame = $.getMainFrame();
		function openUrl(url, title) {
			mainFrame.addTab({
				id:"office_flows_"+Math.floor(Math.random() * 1000000),
				title:title,
				url:url
			});
		}
		
		$(function() {
			$.handleRights({
		        "addHoli" : $.SysConstants.ADD
		    },"holi_startRights");
		    
			$.handleRights({
		        "addEntry" : "entry",
		        "addResign" : "resign",
		        "addMyResign" : "myresign"
		    },"staff_startRights");
		    
		    $.handleRights({
		        "addTran" : $.SysConstants.ADD
		    },"tran_startRights");
		    
		    $.handleRights({
		        "addPosi" : $.SysConstants.ADD
		    },"posi_startRights");
		    
		    $.handleRights({
				"btnDeviceUse" : $.OaConstants.RECIPIENTS,//领用
				"equipAllot" : $.OaConstants.ALLOCATION,//调拨
				"equipScrap" : $.OaConstants.SCRAP,//报废
				"equipmentLeaveDealAdd" : $.OaConstants.SCRAP//离职处理
			},"device_myuseRights");
	
			$.handleRights({
		        "addRei" : $.SysConstants.ADD
		    },"rei_startRights");
		    
		    $.handleRights({
		        "addTra" : $.SysConstants.ADD
		    },"tra_startRights");
		    
		    $.ajax({
			    type : "POST",
			    cache: false,
			    async : true,
			    url  : "m/info_param?act=getassignlayout",
			    success : function(xml){
			        var message = $("message",xml);
			        if(message.attr("code") == "1"){
			            var listDate = "";
			            $("info-layout",xml).each(
			                function(index){
			                	var name = $("name",$(this)).text();
			                	listDate += "<li><a href=\"javascript:openUrl('/oa/m/info_draft?act=initadd&layout=' + encodeURI('" + name + "'),'发布" + name + "');\">" + name + "发布</a></li>";
			                }
			            );
			            $("#infoLayoutList").append(listDate);
			        }
			    },
			    error : $.ermpAjaxError
			});	
	
		});
	</script>
    <style>
    .divLayout{ border:1px solid #eee;width:24%; margin-right:10px}
    </style>
  </head>
  <body class="bdNone">
  	<input id="holi_startRights" type="hidden" value="<oa:right key='holi_start'/>"/>
  	<input id="staff_startRights" type="hidden" value="<oa:right key='staff_start'/>"/>
  	<input id="tran_startRights" type="hidden" value="<oa:right key='tran_start'/>"/>
  	<input id="posi_startRights" type="hidden" value="<oa:right key='posi_start'/>"/>
  	<input id="device_myuseRights" type="hidden" value="<oa:right key='device_myuse'/>"/>
  	<input id="rei_startRights" type="hidden" value="<oa:right key='rei_start'/>" />
  	<input id="tra_startRights" type="hidden" value="<oa:right key='trip_start'/>"/>
  	
 	<div style="width:100%;font-size:12px">
		<div class="divLayout" style="height:250px;">
			<div class="divPanelSub" style="padding:5px;">
				<ul id="taskList" style="padding:5px;background:#fff">
					<b>人事类流程</b>
					<li id="addHoli"><a href="javascript:openUrl('/oa/m/holi_start?act=initadd','填写请假单');">请假申请</a></li>
					<li id="addEntry"><a href="javascript:openUrl('/oa/m/staff_start?act=initadd&applyType=1','新增入职申请单');">员工入职申请</a></li>
					<li id="addResign"><a href="javascript:openUrl('/oa/m/staff_start?act=initadd&applyType=2','新增离职申请单');">员工离职申请</a></li>
					<li id="addMyResign"><a href="javascript:openUrl('/oa/m/staff_start?act=initadd&applyType=3','新增离职申请单');">个人离职申请</a></li>
					<li id="addTran"><a href="javascript:openUrl('/oa/m/tran_start?act=initadd','填写异动单');">异动申请</a></li>
					<li id="addPosi"><a href="javascript:openUrl('/oa/m/posi_start?act=initadd','填写转正单');">转正申请</a></li>
				</ul>
			</div>
		</div>
		
		<div class="divLayout" style="width:24%;height:250px;">
			<div class="divPanelSub" style="padding:5px;">
				<ul id="taskList" style="padding:5px;background:#fff">
					<b>设备审批流程</b>
					<li id="btnDeviceUse"><a href="javascript:openUrl('/oa/m/dev_apply_start?act=initadd','领用申购申请');">设备领用申购申请</a></li>
					<li id="equipAllot"><a href="javascript:openUrl('/oa/m/dev_alc_start?act=initadd','调拨申请');">设备调拨申请</a></li>
					<li id="equipScrap"><a href="javascript:openUrl('/oa/m/dev_discard_start?act=initadd&formType=0','报废申请');">设备报废申请</a></li>
					<li id="equipmentLeaveDealAdd"><a href="javascript:openUrl('/oa/m/dev_discard_start?act=initadd&formType=1','离职处理申请');">设备离职处理申请</a></li>
				</ul>
			</div>
		</div>
		<div class="divLayout" style="height:250px;">
			<div class="divPanelSub" style="padding:5px;">
				<ul id="infoLayoutList" style="padding:5px;background:#fff">
					<b>信息发布流程</b>
			<!-- 		<li><a href="javascript:openUrl('/oa/m/info_draft?act=initadd&layout=' + encodeURI('新闻公告'),'发布新闻公告');">新闻公告发布</a></li>
					<li><a href="javascript:openUrl('/oa/m/info_draft?act=initadd&layout=' + encodeURI('规章制度'),'发布规章制度');">规章制度发布</a></li>
					<li><a href="javascript:openUrl('/oa/m/info_draft?act=initadd&layout=' + encodeURI('客服消息'),'发布客服消息');">客服消息发布</a></li>
					<li><a href="javascript:openUrl('/oa/m/info_draft?act=initadd&layout=' + encodeURI('运营消息'),'发布运营消息');">运营消息发布</a></li>
					<li><a href="javascript:openUrl('/oa/m/info_draft?act=initadd&layout=' + encodeURI('产品消息'),'发布产品消息');">产品消息发布</a></li>
					<li><a href="javascript:openUrl('/oa/m/info_draft?act=initadd&layout=' + encodeURI('合规与信用消息'),'发布合规与信用消息');">合规与信用消息发布</a></li>
			 -->
				</ul>
			</div>
		</div>
		<div class="divLayout" style="height:250px;margin-right:0">
			<div class="divPanelSub" style="padding:5px;">
				<ul id="taskList" style="padding:5px;background:#fff">
					<b>其它流程</b>
					<li id="addRei"><a href="javascript:openUrl('/oa/m/rei_start?act=initadd','填写报销单');">费用报销申请</a></li>
					<li id="addTra"><a href="javascript:openUrl('/oa/m/trip_start?act=initadd','填写出差申请单');">出差申请</a></li>
				</ul>
			</div>
		</div>
	</div>
  </body>
</html>