var mainFrame = $.getMainFrame();

$(initView);

function initView() {
	var d=new Date(); 
	var year = d.getFullYear();
	    
	var curMonth = $("#curMonth").val();
	if(curMonth) {
		var syear = curMonth.substr(0, 4);
		var smonth = curMonth.substr(4, 2);
		
		$("#monthSel").val(smonth);
    
	    var yearHTML = "";
	    for (var i = 0; i <10; i++){
	    	yearHTML += "<option value='" + (year - i) + "'>" + (year - i) + "年</option>";
	    }
	    
	    $("#yearSel").html(yearHTML).val(syear);
	} else {
		
	    var month = d.getMonth() + 1;
	    month =( month < 10 ? "0" : "") + month;
	    
	    changeSalary(year + month);
	}
}

function changeSalary(month) {
	if (!month) {
		month = $("#yearSel").val() + $("#monthSel").val();
	}
	mainFrame.getCurrentTab().loadURL(BASE_PATH + "m/my_salary?act=view&month=" + month);
}