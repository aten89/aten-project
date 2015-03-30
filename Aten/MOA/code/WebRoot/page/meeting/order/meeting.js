
//生成RGB颜色
function getRandColor(meetingID,startTime,endTime){
	startTime = startTime.replace(/(-|:| )/igm, "");
 	endTime = endTime.replace(/(-|:| )/igm, "");
  	return "rgb("+ parseInt(meetingID, 16) % 255 +"," + parseInt(startTime, 10) % 255 +"," +parseInt(endTime, 12) % 255+")";
}

//预订扇区
function drawAPic(obj,meetingID,startTime,endTime,title, vColor, desc){
  	var mX=Math.pow(2,16);// * 360;
  
  	var startAngle=0;
  	var endAngle=0;

  	var objLegendRect=document.createElement("v:rect");
  	obj.appendChild(objLegendRect);  
  

   	var vPieEl=document.createElement("v:shape");
   	var vPieId=meetingID;
   	vPieEl.style.width="15000px";
   	vPieEl.style.height="15000px";
   	vPieEl.style.top="1000px";
   	vPieEl.style.left="1000px";
   	vPieEl.coordsize="1500,1500";
   	vPieEl.strokecolor="white";   
   	vPieEl.id=vPieId;
   	vPieEl.attachEvent("onmouseover",function(){event.srcElement.firstChild.type='gradientradial'});
   	vPieEl.attachEvent("onmouseout",function(){event.srcElement.firstChild.type='solid'});
   	vPieEl.attachEvent("onclick",function(){
   		$.getMainFrame().addTab({
			id:"oa_meet_info_view_"+meetingID,
			title:"查看会议信息",
			url:BASE_PATH + "/m/meet_info?act=view&id="+meetingID,
			callback:showWeekTable
		});
   	});
   	var j = transAngle(startTime);
   	var k = transAngle(endTime);
   	startAngle = k;
   	endAngle = j - k;
   
   	vPieEl.path="M 750 750 AE 750 750 750 750 " + parseInt(mX * startAngle) +" " + parseInt(mX * endAngle) +" xe";
   	vPieEl.title= desc+"\n" + title;
   	var objFill=document.createElement("v:fill");
   	objFill.rotate="t";
   	objFill.focus="100%";
   	objFill.type="solid";
   	objFill.angle=parseInt( 360 * (startAngle + endAngle /2));
   	vPieEl.appendChild(objFill);
   
   	vPieEl.fillcolor=vColor; //设置颜色
   
   	obj.appendChild(vPieEl);
}


function drawOval(obj,w,h){ 
	
 	//画外框
  	var o=document.createElement("v:group");
  	o.style.width=w;
  	o.style.height=h;
  	o.coordsize="21600,21600";

 	//添加一个中心圆
  	obj.innerHTML = "";
  	obj.appendChild(o);
  
 	//开始画内部园
  	var oOval=document.createElement("v:oval");
  	oOval.style.width="15000px";
  	oOval.style.height="15000px";
  	oOval.style.top="1000px";
  	oOval.style.left="1000px";
  	oOval.fillcolor="#d5dbfb";
  	oOval.title = "点击预订";
  	oOval.onclick = function(){roomOrder();};
  	//圆点
  	var oOval2 =document.createElement("v:oval");
  	oOval2.style.zIndex="1";
  	oOval2.style.width="800px";
  	oOval2.style.height="800px";
  	oOval2.style.top ="8000px";
  	oOval2.style.left = "8000px";
  	oOval2.fillcolor="white";

	//本来计划加入3D的效果，后来感觉确实不好控制，就懒得动手了
	//var o3D=document.createElement("o:extrusion");
	var formatStyle=' <v:fill  rotate="t" angle="-135" focus="10%" type="gradient"/>';
	//formatStyle+='<o:extrusion v:ext="view" color="#9cf" on="t" rotationangle="-15"';
	//formatStyle+=' viewpoint="0,34.72222mm" viewpointorigin="0,.5" skewangle="105"';
	//formatStyle+=' lightposition="0,50000" lightposition2="0,-50000"/>';
	//formatStyle+='<o:extrusion v:ext="view" backdepth="1in" on="t" viewpoint="0,34.72222mm"   viewpointorigin="0,.5" skewangle="90" lightposition="-50000"   lightposition2="50000" type="perspective"/>';
	oOval.innerHTML=formatStyle;  
	o.appendChild(oOval);
	o.appendChild(oOval2);
}

function drawAllOval(){
	var objTab = document.getElementById("tabMain");
	for (var i=2;i<objTab.rows.length;i++){
		var objTr = objTab.rows[i];
		var roomID = objTr.cells;
		for (var j=1;j<objTr.cells.length;j++){
			var objTd = objTr.cells[j];
			objTd.id = "td_" + objTr.id + "_" + weekDatesEn[j-1];
			drawOval(objTd,"80px","80px");
		}
	}
}


//画出会议室的预订情况
function drawMeeting(meetingID,roomID,startTime,endTime,title,weekDatesEn){
	/([\d]*)\-([\d]*)\-([\d]*)[  | ]([\d]*):([\d]*)/.exec(startTime);  
	var y1=parseInt(RegExp.$1,10);
	var m1=parseInt(RegExp.$2,10);
	var d1=parseInt(RegExp.$3,10);
	var h1=RegExp.$4;
	var s1=RegExp.$5;

	/([\d]*)\-([\d]*)\-([\d]*)[  | ]([\d]*):([\d]*)/.exec(endTime)
	var y2=parseInt(RegExp.$1,10);
	var m2=parseInt(RegExp.$2,10);
	var d2=parseInt(RegExp.$3,10);
	var h2=RegExp.$4;
	var s2=RegExp.$5;
	var desc = formatTime(y1+"-"+m1+"-"+d1, h1+":"+s1,y2+"-"+m2+"-"+d2, h2+":"+s2);
	var sDate = y1;
	if(m1 < 10){
		sDate+="-0"+m1;
	}else{
		sDate+="-"+m1;
	}
	if(d1<10){
		sDate+="-0"+d1;
	}else{
		sDate+="-"+d1;
	}
	var eDate = y2+"-"+m2+"-"+d2;
	var days = daysBetween(sDate,eDate);
    var vColor=getRandColor(meetingID,startTime,endTime);

  	var sTime="";
	if(h1 < 8){
		sTime="08:00";
	}else{
		sTime=h1+":"+s1;
	}
	var eTime="";
	if(h2 > 20){
		eTime = "20:00";
	}else{
		eTime = h2+":"+s2;
	}
	if(days == 0){
		var obj =document.getElementById("td_" + roomID + "_" + (y1 + "-" + m1 + "-" + d1)).firstChild;
		if(h2 < 8){
			return;
		}
		drawAPic(obj,meetingID,sTime,eTime,title, vColor ,desc);
	}else{
		for(var i=0; i<=days; i++){
			var obj =null;
			var tmpDate = Date.parseString(sDate).DateAdd('d',i);
			var dateArray = tmpDate.toArray();
			if(i==0){
				if(document.getElementById("td_" + roomID + "_" + (y1 + "-" + m1 + "-" + d1))!=null){
					obj =document.getElementById("td_" + roomID + "_" + (y1 + "-" + m1 + "-" + d1)).firstChild;
					drawAPic(obj,meetingID,sTime,"20:00",title, vColor ,desc);				
				}
			}else if(i == days){
				if(document.getElementById("td_" + roomID + "_" +(dateArray[0] + "-" + dateArray[1] + "-" + dateArray[2]) )!=null){
					obj =document.getElementById("td_" + roomID + "_" +(dateArray[0] + "-" + dateArray[1] + "-" + dateArray[2]) ).firstChild;
					if((h2==0 && s2==0 )|| h2 < 8 ){
						return;
					}
					drawAPic(obj,meetingID,"08:00",eTime,title, vColor ,desc);
				}
			}else{
				if(document.getElementById("td_" + roomID + "_" +(dateArray[0] + "-" + dateArray[1] + "-" + dateArray[2]) ) !=null){
					obj =document.getElementById("td_" + roomID + "_" +(dateArray[0] + "-" + dateArray[1] + "-" + dateArray[2]) ).firstChild;
					drawAPic(obj,meetingID,"08:00","20:00",title, vColor ,desc);
				}
			}
		}
	}
}

//end画时间段饼图函数**************************************

//时间计算Start***********************************
function formatTime(bDate,bTime,eDate,eTime) {
	if (bDate == eDate) {
		return bDate + " " + bTime + "至" + eTime;
	} else {
		return bDate + " " + bTime + "至" + eDate + " " + eTime;
	}
}

Date.parseString = function(dateString,formatter){
    var today = new Date();
    if(!dateString || dateString == "")
    {
        return today;
    }
    if(!formatter || formatter == "")
    {
        formatter = "yyyy-MM-dd";
    }  
    var yearMarker = formatter.replace(/[^y|Y]/g,'');   
    var monthMarker = formatter.replace(/[^m|M]/g,'');   
    var dayMarker = formatter.replace(/[^d]/g,'');
    var yearPosition = formatter.indexOf(yearMarker);
    var yearLength = yearMarker.length;
    var year =  dateString.substring(yearPosition ,yearPosition + yearLength) * 1;  
    if( yearLength == 2)
    {
        if(year < 50 )
        {
            year += 2000;
        }
        else
        {
            year += 1900;
        }
    }
    var monthPosition = formatter.indexOf(monthMarker);
    var month = dateString.substring(monthPosition,monthPosition + monthMarker.length) * 1 - 1;
    var dayPosition = formatter.indexOf(dayMarker);
    var day = dateString.substring( dayPosition,dayPosition + dayMarker.length )* 1;
    
    return new Date(year,month,day);
}
function daysBetween(DateOne,DateTwo)   
{    
    var OneMonth = DateOne.substring(5,DateOne.lastIndexOf ('-'));   
    var OneDay = DateOne.substring(DateOne.length,DateOne.lastIndexOf ('-')+1);   
    var OneYear = DateOne.substring(0,DateOne.indexOf ('-'));   
    var TwoMonth = DateTwo.substring(5,DateTwo.lastIndexOf ('-'));   
    var TwoDay = DateTwo.substring(DateTwo.length,DateTwo.lastIndexOf ('-')+1);   
    var TwoYear = DateTwo.substring(0,DateTwo.indexOf ('-'));   
    var cha=((Date.parse(OneMonth+'/'+OneDay+'/'+OneYear)- Date.parse(TwoMonth+'/'+TwoDay+'/'+TwoYear))/86400000);    
    return Math.abs(cha);   
}   
Date.prototype.DateAdd = function(strInterval, Number) {    
    var dtTmp = this;   
    switch (strInterval) {    
        case 's' :return new Date(Date.parse(dtTmp) + (1000 * Number));   
        case 'n' :return new Date(Date.parse(dtTmp) + (60000 * Number));   
        case 'h' :return new Date(Date.parse(dtTmp) + (3600000 * Number));   
        case 'd' :return new Date(Date.parse(dtTmp) + (86400000 * Number));   
        case 'w' :return new Date(Date.parse(dtTmp) + ((86400000 * 7) * Number));   
        case 'q' :return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number*3, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());   
        case 'm' :return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());   
        case 'y' :return new Date((dtTmp.getFullYear() + Number), dtTmp.getMonth(), dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());   
    }   
}   

//+---------------------------------------------------   
//| 把日期分割成数组   
//+---------------------------------------------------   
Date.prototype.toArray = function()   
{    
    var myDate = this;   
    var myArray = Array();   
    myArray[0] = myDate.getFullYear();   
    myArray[1] = myDate.getMonth()+1;   
    myArray[2] = myDate.getDate();   
    myArray[3] = myDate.getHours();   
    myArray[4] = myDate.getMinutes();   
    myArray[5] = myDate.getSeconds();   
    return myArray;   
}   
//时间计算end***********************************
//鼠标事件*************************************************
function onRowOver(row){
  if(row.cells[0].firstChild.checked!=true||row.cells[0].firstChild.disabled){
	row.bgColor=row.style.backgroundColor;
	row.style.backgroundColor='#CCFFCC';
  }
}
function onRowOut(row){
  if(row.cells[0].firstChild.checked!=true||row.cells[0].firstChild.disabled){
	row.style.backgroundColor=row.bgColor;
	row.bgColor='';
  }
}
function checkRow(obj){
  if (obj.checked)
	obj.parentElement.parentElement.style.backgroundColor='#FFCC99';
}
//end鼠标事件***********************************************


function transAngle(times){
	var timeArr = times.split(":");
	var iTimes = parseInt(timeArr[0],10) + parseInt(timeArr[1],10)/60;
	var angles = 0;
	if (iTimes>=6 && iTimes<=12) angles = 90 + ((12-iTimes) *30);
	if (iTimes>12 && iTimes<=15) angles = 90 - ((iTimes-12) * 30);
	if (iTimes>15) angles =  -((iTimes-15)*30);

	return angles;
}

//function openMeeting(url,title,iWidth,iHeight,iLeft,iTop){
//	url = escape(url)
//	var str = window.showModalDialog("ModalDialog.aspx?url=" + url + "&title=" + title,window,"dialogHeight: " + iHeight + "px; dialogWidth:" + iWidth + "px;dialogLeft:" + iLeft + "px;dialogTop:" + iTop + "px; edge: Raised; center: Yes; help: No; resizable: Yes; status: No;");
//	if (str=="reload"){
//		showWeekTable(0);
//	}
//}
//
//function openUrl(url,title,iWidth,iHeight,iLeft,iTop){
//	url = escape(url)
//	var str = window.showModalDialog("ModalDialog.aspx?url=" + url + "&title=" + title,window,"dialogHeight: " + iHeight + "px; dialogWidth:" + iWidth + "px;dialogLeft:" + iLeft + "px;dialogTop:" + iTop + "px; edge: Raised; center: Yes; help: No; resizable: Yes; status: No;");
//	if (str=="reload"){
//		window.location.href=location.href;
//	}
//}

function roomOrder(){
	var obj = event.srcElement.parentElement.parentElement;
	var roomID = obj.id.split("_")[1];
	var orderDate = obj.id.split("_")[2];
	var objDate = new Date(orderDate.split("-")[0],parseInt(orderDate.split("-")[1])-1,orderDate.split("-")[2]);
	if (today.getTime()>objDate.getTime()){
		alert("今天之前的时间段不能预订");
		return false;	
	} 
	var status = $("#status_"+roomID).val();
	var remark = $("#remark_"+roomID).val();
	
	if(status =="否")
	{
		var strRemark="";
		if(remark !="")
		{
		    strRemark=remark+"，";
		}
		alert(strRemark+'该会议室不能预订！');
		return false;
	}
	if(remark !="")
	{
	    alert(remark);
	}
	$.getMainFrame().addTab({
		id:"oa_meet_manage_"+Math.floor(Math.random() * 1000000),
		title:"预约会议室",
		url:BASE_PATH + "/m/meet_info?act=toReservePage&roomId="+roomID+"&orderDate="+orderDate+"&areaCode="+__areaCode,
		callback:showWeekTable
	});
}

function addDays(myDate,days)
{
	return new Date(myDate.getTime()+days*24*60*60*1000)
}

function meetingSearchResult(weekDatesEn, areaCode){
	var startDate = weekDatesEn[0];
	var endDate = weekDatesEn[6]+" 23:59";
	$.ajax({
			type : "POST",
			cache: false,
	   		async : false,
	      	url : "m/meet_query",
	      	dataType : "xml",
	      	data : {
	      		act : "meetingorderinfo",
	      		areaCode : areaCode,
	      		inputBeginDate : startDate,
	      		inputEndDate : endDate 
	      	},
	        success : function(xml){
	        	var message = $("message",xml);
	        	if(message.attr("code") == "1") {
	        		$("meet-info",xml).each(function(index){
	        			var meeting = $(this);
	        			var meetingID = meeting.attr("id");
						var roomID = $("room-id",meeting).text();
						var startTime =  $("begin-time",meeting).text();
						var endTime =  $("end-time",meeting).text();
						var title =  $("theme",meeting).text();
						try{
							drawMeeting(meetingID,roomID,startTime,endTime,title,weekDatesEn);
						}catch(e){
						}
	        		});	
	        	}
	        },
        	error : $.ermpAjaxError
		});

}
function showWeekTable(n){
	if(n==null){
		n=0;
	}
	currDate = addDays(currDate,7*n);
	for (var d=0;d<7 ;d++ )/*====d在正常情况下为5,特殊节假日为7==[以后补充功能，让程序自动控制和手动控制]==临时添加，以便在（五一，十一）等节前的周末预定{张培坤 2007/09/26}=====*/
	{
		weekDates[d] = addDays(currDate,-currDate.getDay()+d+1);
		weekDatesEn[d] = weekDates[d].getFullYear() + "-" + (weekDates[d].getMonth()+1) + "-" + weekDates[d].getDate();
		weekDatesCn[d] = weekDates[d].getFullYear() + "年" + (weekDates[d].getMonth()+1) + "月" + weekDates[d].getDate() + "日";
		document.getElementById("tabMain").rows[1].cells[d].bgColor=(weekDates[d].getTime()==today.getTime())?"#3399EE":"buttonface";
		for (var l=2; l<document.getElementById("tabMain").rows.length; l++)
		{
			document.getElementById("tabMain").rows[l].cells[d+1].bgColor=(weekDates[d].getTime()==today.getTime())?"#D9E6F4":"";
		}
	}
	//document.getElementById("tdWeekTitle").innerText = weekDatesCn[0] + " ── " + weekDatesCn[6];/*====在正常情况下为4,特殊节假日为6==[以后补充功能，让程序自动控制和手动控制]==临时添加，以便在（五一，十一）等节前的周末预定{张培坤 2007/09/26}=====*/	
	$("#tdWeekTitle").html( weekDatesCn[0] + " ── " + weekDatesCn[6]);
	drawAllOval();
	meetingSearchResult(weekDatesEn, __areaCode);
}
var today = new Date();
today = new Date(today.getFullYear(),today.getMonth(),today.getDate());
var currDate = today;
var weekDates = new Array(4);	//日期型
var weekDatesEn = new Array(4);	//简写型
var weekDatesCn = new Array(4);	//中文型
