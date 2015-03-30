<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script type="text/javascript" src="page/meeting/order/meeting.js"></script>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/meeting/order/order_meeting.js"></script>
<link href="themes/comm/meeting.css" rel="stylesheet" type="text/css"></link>
<script>
$(function(){
	$(".a01,.a02,.a03,.a04,.a05,.e01,.XM06,.XM07").hover(function(){
		  $(this).addClass("overCol")		 
	   },function(){
		   $(this).removeClass("overCol")
	   });
	$(".b01,.b02,.b03,.SH01,.SH02,.XM01,.XM02,.XM03,.XM04,.XM05").hover(function(){
		  $(this).addClass("overCol2");
	   },function(){
		   $(this).removeClass("overCol2")
	   });
})
</script>
<title></title>
<style type="text/css">
		*{ margin:0; padding:0}
		.meetingFZ{ width:666px; height:309px; margin:0 auto; position:relative}
		.meetingSH{ width:666px; height:309px; margin:0 auto; position:relative}
		.meetingXM{ width:870px; height:360px; margin:0 auto; position:relative}
		.temp{ border:1px solid #000; background:#fff}
		.a01{position:absolute;top:153px;left:317px;width:179px;height:61px; background:#fff;-moz-opacity:0;opacity:0;filter:alpha(opacity: 0);cursor:pointer;}
		.a02{position:absolute;top:153px;left:496px;width:59px;height:61px;background:#fff;-moz-opacity:0;opacity:0;filter:alpha(opacity: 0);cursor:pointer;}
		.a03{position:absolute;top:153px;left:176px;width:76px;height:30px;background:#fff;-moz-opacity:0;opacity:0;filter:alpha(opacity: 0);cursor:pointer;}
		.a04{position:absolute;top:185px;left:176px;width:76px;height:29px;background:#fff;-moz-opacity:0;opacity:0;filter:alpha(opacity: 0);cursor:pointer;}
		.a05{position:absolute;top:153px;left:98px;width:76px;height:60px;background:#fff;-moz-opacity:0;opacity:0;filter:alpha(opacity: 0);cursor:pointer;}
		.b01{position:absolute;top:28px;left:172px;width:80px;height:44px;background:#fff;-moz-opacity:0;opacity:0;filter:alpha(opacity: 0);cursor:pointer;}
		.b02{position:absolute;top:47px;left:318px;width:110px;height:28px;background:#fff;-moz-opacity:0;opacity:0;filter:alpha(opacity: 0);cursor:pointer;}
		.b03{position:absolute;top:28px;left:558px;width:85px;height:48px;background:#fff;-moz-opacity:0;opacity:0;filter:alpha(opacity: 0);cursor:pointer;}
		.e01{position:absolute;top:258px;left:23px;width:69px;height:31px;background:#fff;-moz-opacity:0;opacity:0;filter:alpha(opacity: 0);cursor:pointer;}
		
		
		.SH01{position:absolute;top:28px;left:20px;width:61px;height:42px; background:#fff;-moz-opacity:0;opacity:0;filter:alpha(opacity: 0);cursor:pointer;}
		.SH02{position:absolute;top:217px;left:574px;width:68px;height:72px;background:#fff;-moz-opacity:0;opacity:0;filter:alpha(opacity: 0);cursor:pointer;}
		
		.XM01{position:absolute;top:115px;left:579px;width:52px;height:31px; background:#fff;-moz-opacity:0;opacity:0;filter:alpha(opacity: 0);cursor:pointer;}
		.XM02{position:absolute;top:115px;left:724px;width:57px;height:31px; background:#fff;-moz-opacity:0;opacity:0;filter:alpha(opacity: 0);cursor:pointer;}
		.XM03{position:absolute;top:115px;left:782px;width:57px;height:31px; background:#fff;-moz-opacity:0;opacity:0;filter:alpha(opacity: 0);cursor:pointer;}
		
		.XM04{position:absolute;top:192px;left:286px;width:54px;height:21px;background:#fff;-moz-opacity:0;opacity:0;filter:alpha(opacity: 0);cursor:pointer;}
		.XM05{position:absolute;top:187px;left:766px;width:90px;height:50px; background:#fff;-moz-opacity:0;opacity:0;filter:alpha(opacity: 0);cursor:pointer;}
		
		.XM06{position:absolute;top:249px;left:21px;width:76px;height:48px; background:#fff;-moz-opacity:0;opacity:0;filter:alpha(opacity: 0);cursor:pointer;}
		.XM07{position:absolute;top:300px;left:21px;width:76px;height:47px; background:#fff;-moz-opacity:0;opacity:0;filter:alpha(opacity: 0);cursor:pointer;}
		
		.overCol{-moz-opacity:0.39;filter:alpha(opacity: 39); border:1px solid #960;opacity:.39}
		.overCol2{-moz-opacity:0.39;filter:alpha(opacity: 39); border:1px solid #1A679B;opacity:.39}
		v\:* { BEHAVIOR: url(#default#VML) }
		o\:* { BEHAVIOR: url(#default#VML) }
		.shape { BEHAVIOR: url(#default#VML) }
		#tabMain{ border-collapse:collapse}
		#tabMain th{border:1px solid #9EB6CE;height:23px}
		#tabMain td{border:1px solid #9EB6CE; text-align:center;}
		#tabMain .mTit{background:url(page/meeting/order/meetingImg/mTit.gif);text-align:center}
		#tabMain .mTitOver{background:url(page/meeting/order/meetingImg/mTitOver.gif); border-left:1px solid #fff;border-right:1px solid #fff;border-bottom:1px solid #fff;}
		.transparent { BORDER-RIGHT: 0px; BORDER-TOP: 0px; BORDER-LEFT: 0px; BORDER-BOTTOM: 0px; BACKGROUND-COLOR: transparent }
</style>
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr>
    <td>
        <!-- 查询条件 begin -->
        <div class="soso" style="height:auto">
          <div class="t01"  style="height:auto">
            <table border="0" cellpadding="0" cellspacing="0"  style="table-layout:fixed">
              <tr>
                <td width="100"></td>
                <td width="131"></td>
              </tr>
              <tr>
                <td class="soPaddLeft">请选择所属地区：</td>
                <td><div style="width:131px">
                    <div id="areaSel" name="areaSel">
                    </div>
                  </div></td>
              </tr>
            </table>
          </div>
        </div>
        <!-- 查询条件 end --> 
    </td>
  </tr>
  <tr>
  	<td style="text-align:center; border:1px solid #ccc; padding:10px;">
		<div align="center" style="background:#fff; width:686px; margin:0 auto;">
			<div>提示：点击会议室名称开始预订，在IE浏览器下还可以点击饼图直接预定!</div>
			<div class="blank" style="height:22px"></div>
			<div>
				<table border="0" align="center" cellpadding="0" cellspacing="0" id="tabMain">
					<tr>
						<th colspan="2" style="border-right:none">&nbsp;</th>
						<th style="border-right:none;border-left:none" ><input name="btnPrev" type="button" class="transparent" id="btnPrev" value="<<" onClick="showWeekTable(-1)"/></th>
						<th style="border-left:none;border-right:none;"  id="tdWeekTitle" height="25" colspan="3" >&nbsp;</th><!---colspan正常情况下为3,特殊节目为5------------临时添加，以便在（五一，十一）等节前的周末预定{张培坤 2007/09/26}--------------->
						<th height="25" style="border-left:none;border-right:none;"><input name="btnNext" type="button" class="transparent" id="btnNext" value=">>" onClick="showWeekTable(1)"/></th>
						<th style="border-left:none">&nbsp;</th>
					</tr>
					<tr height="20" style="CURSOR:default">
					    <td style="background:url(page/meeting/order/meetingImg/mTip.gif) no-repeat right bottom; height:23px;">&nbsp;</td>
						<td  class="mTit"   title="点击进入周一" onMouseOver="this.className='mTitOver'"	onMouseOut="this.className='mTit'">&nbsp;周一</td>
						<td  class="mTit"   onMouseOver="this.className='mTitOver'" onMouseOut="this.className='mTit'">&nbsp;周二</td>
						<td  class="mTit"   onMouseOver="this.className='mTitOver'" onMouseOut="this.className='mTit'">&nbsp;周三</td>
						<td  class="mTit"   onMouseOver="this.className='mTitOver'" onMouseOut="this.className='mTit'">&nbsp;周四</td>
						<td  class="mTit"  onMouseOver="this.className='mTitOver'" onMouseOut="this.className='mTit'">&nbsp;周五</td>
						<!---------------临时添加，以便在（五一，十一）等节前的周末预定{张培坤 2007/09/26}--------------->
						<td  class="mTit" onMouseOver="this.className='mTitOver'" onMouseOut="this.className='mTit'">&nbsp;周六</td>
						<td  class="mTit"  onMouseOver="this.className='mTitOver'" onMouseOut="this.className='mTit'">&nbsp;周日</td>
						<!----------------------------------------------------------------------------------------------->
					</tr>
				</table>
			</div>
		</div>
    </td>
  </tr>
</table>

