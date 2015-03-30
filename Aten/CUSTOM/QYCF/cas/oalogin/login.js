var allowLogin = true;
var tdheight;
$(document).ready(function() {
    getTdHeight();
    getBrowser();
    var now = new Date();
    var year = now.getFullYear(); 
    var month = now.getMonth() + 1;
    var date = now.getDate(); 
    var day;        //获取当前星期X(0-6,0代表星期天) 
    switch(now.getDay()) {
		case 0:
		  day="日";
		  break;
		case 1:
		  day="一";
		  break;
		case 2:
		  day="二";
		  break;
		case 3:
		  day="三";
		  break;
		case 4:
		  day="四";
		  break;
		case 5:
		  day="五";
		  break;
		case 6:
		  day="六";
		  break;
		}
    $("#curTime").html("今天是" + year + "年" + month + "月" + date + "日&nbsp;&nbsp;&nbsp;&nbsp;星期" + day);
});


function getBrowser() {
    if ($.browser.msie) {
        $(".browesPosition").css("position", "absolute");
    } else {
        $("div").removeClass("browesPosition");
    }
}

function getTdHeight() {
    tdheight = $(window).height();
    $("#toptd").css('height', tdheight * 0.01);
    $("#tableheight").css('height', tdheight);
}