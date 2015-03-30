/**
* 类型：公用组件
* 名称：列表选择框
* 用途：用于生成列表选择框（单项选择、范围选择、多项选择三种类型）
*	   用户可以自定义候选项，以及初始化时的默认值
*	   选择后自动将选择结果赋于指定的结果域内
*
* 用法：1)在页面的特定位置设计一个<div>容器,用于摆放生成的列表选择框;
*	   2)为容器设置属性,如:<div id="selector" type="single" width="100" overflow="true" popheight="160" popwidth="160" isdisabled="true">
*		 属性说明:
*		 	type:列表选择框类型,可选项:single/multiple,默认值:single;
*		   	width:选择框宽度,默认值:100;
*			overflow:是否出错滚动条,可选项:true/false,默认值:true;
*		   	popheight:选择框高度,仅当overflow=false时有效,默认值:160;
*		   	popwidth:选择框宽度,为空时垂直对齐(即与显示框保持相同宽度);
*		   	isdisabled:是否不可用（即不允许选择），可选项：true/false，默认值：false;
*		   	url:列表数据地址,不为空时data，json，async才要设置
*			data:JQuery AJAX参数一样
*			json:JQuery AJAX参数一样
*			async:JQuery AJAX参数一样
*			afterLoad:组件初始化完后回调方法
*		   	onChange:选择值时触发回调方法，并传入两个参数（value,name）
*			onClick:点击下拉列表时回调方法，该方法返回false时停止弹出下拉
*
*		 以上参数也可以在初始化列表选择框时进行设置,方法见4).
*
*	   3)设置默认选择结果:
*		 方法一:在选择结果域中填写默认选择结果
*		 方法二:在需要设置为选择结果的候选项中添加属性:isselected="true"(优先生效)
*	   
*	   4)初始化方法:
*	     $("#selector").ySelect({type:"single",width:100,popheight:160,url:"",onChange:null});
*
*	   5)选择结果值格式: 
*		 单项选择："abc";
*		 多项选择："a,b,c";
*
* 创建者：zhuoshiyao
* 修改者：richfans	 	2008-06-26
* 修改者：zhuoshiyao		2008-07-16		添加addOption与select方法
* 修改者：zhuoshiyao		2008-07-23		添加setValue与disable方法,代码优化
* 修改者：lintianri		2010-09-01		解决隐藏时的滚动条不出现问题
* 修改者：zhuoshiyao		2014-11-24		重构部分代码，去除范围选择类型（无用）
*/

(function($) {
	/**
	* 初始化列表选择框控件
	* @param 用户自定义属性列表(见组件说明)
	* @return 无
	*/
	$.fn.ySelect = function(options) {
		//默认参数
		var defaults = {
				width: 100,//宽度
				overflow: true,//是否出现滚动条
				popheight: 160,//弹出高度，overflow=true时才要设置
				popwidth: null,//弹出宽度，不设时，默认与width垂直对齐
				isdisabled: false,//是否不可用
				type: "single",//多选：multiple，单选：single
				url: "",//列表数据地址,不为空时data，json，async才要设置
				data: "",//请求参数
				json: false,//是否加载JSON类型请求，如果为true,内容存在jsonData.htmlValue中
				async:true,//是否异步加载
				
				afterLoad : null,	//初始化完后回调方法
				onChange : null,	//模拟onchange事件,(补充：这里应该是onSelect才对，点击选择时调用并传入两个参数（value,name）)
				onClick : null	//点击下拉列表时回调方法，该方法返回false时停止弹出下拉
			};
		var p_this = $(this);//当前引用的DIV的jquery对像
		
		//=======设置用户自定义参数====================
		//1.合并参数：用传入的参数覆盖默认参数
		$.extend(defaults, options);
		//2.合并参数：用容器的属性值覆盖默认参数
		initOptionsFromAttributes(p_this, defaults);
		//==========================================
		
		//异步读取
		if (defaults.url != "") {
			try {
				if (defaults.json) {
					$.ajax({
						url: defaults.url,
						data: defaults.data,
						async: defaults.async,
						type: 'GET',
						dataType: 'json',
						success: function(json){
	                        p_this.html(json.htmlValue);
	                   		init(p_this, defaults);
	                    },
	                    error: function(){$.alert(arguments[0].responseText);$.alert("加载列表失败：" + defaults.url);}
					});
				} else {
					$.ajax({
						url: defaults.url,
						data: defaults.data,
						async: defaults.async,
						type: 'POST',
						success: function(xml){
	                        p_this.html(xml);
	                        init(p_this, defaults);
	                    },
	                    error: function(){$.alert(arguments[0].responseText);$.alert("加载列表失败：" + defaults.url);}
					});
				}
			} catch(e){}
		} else {
			init(p_this,defaults);
		}
		
		//设置列表组件的有效性
		//flag：true/false
		this.disable = function(flag) {
			defaults.isdisabled = flag;
			setDisabled(p_this, defaults);
			return this;
		};
		
		//设置列表组件的值
		//value:值
		//name:显示名称
		this.setValue = function(value, name) {
			if (name) {
				setSelectValue(p_this, value, defaults);
				p_this.find(".s_top_text").val(name);
			}
			return this;
		};
		
		//选中选项
		//index:选中到第几项，从0算起
		this.select = function(index) {
			var olists = p_this.find(".select_list");//.children(".s_list_i");
			if(typeof index == "string"){//通过值选择
				olists.find("input").each(function(){
					if ($(this).val() == index) {
						$(this).parent().click();
					}
				});
			} else {//通过索引选择
				if (index >= olists.children(".s_list_i").size()) {
					return;
				} else {
					olists.children(".s_list_i").eq(index).click();
				}
			}
			return this;
		};
		
		//通过显示名称选中
		this.selectByName = function(name) {
			p_this.find(".select_list").children(".s_list_i").each(function(){
				if ($(this).text() == name) {
					$(this).click();
				}
			});
			return this;
		};
		
		//取得选中的值
		this.getValue = function(){
			var val = $("#"+defaults.valueInputId, p_this).val();
			return val ? val : "";
		};
		
		//取得选中的显示名称
		this.getDisplayValue = function() {
			return p_this.find(".s_top_text").val();
		};
		
		//添加选项
		//index:添加到第几项，从0算起
		this.addOption = function(value, name, index) {
			if (name) {
				var inputtype = "hidden";
				if (defaults.type == "multiple") {
					inputtype = "checkbox";
				}
				var content = $("<div class='s_list_i'><input type='" + inputtype +"' value='" 
						+ value + "' />" + name + "</div>");
				var olists = p_this.find(".select_list");//.children(".s_list_i");
				var size = olists.children(".s_list_i").size();
				if(index == null) index = size;
				if (size == 0 || index >= size) {
					olists.append(content);
				} else {
					olists.children(".s_list_i").eq(index).before(content);
				}
				
				//为添加的选项添加点击事件
				addOptionClick(content, p_this, defaults);
			}
			return this;
		};
		
		//移除选项（如果移除的时当前选中的选项，默认选中每一个）
		this.removeOption = function(index) {
			var currentVal;
			if(typeof index == "string"){//通过值选择
				currentVal = index;
				$(".select_list input[value='" + index + "']", p_this).parent().remove();
			} else {//通过索引选择
				var items = $(".select_list", p_this).children(".s_list_i");
				if (index >= items.size()) {
					return;
				} else {
					var removeItem = items.eq(index);
					currentVal = $("input", removeItem).val();
					removeItem.remove();
				}
			}
			if (this.getValue() == currentVal) {
				this.select(0);
			}
		};

		//添加分隔符
		//index:添加到第几项，从0算起
		this.addSeparator = function(text, index) {
			var content = $("<div class='s_separator' align='center'>" + (text ? "<span>" + text + "</span>" : "") + "</div>");
			var olists = p_this.find(".select_list");//.children(".s_list_i");
			var size = olists.children(".s_list_i").size();
			if(index == null) index = size;
			if (size == 0 || index >= size) {
				olists.append(content);
			} else {
				olists.children(".s_list_i").eq(index).before(content);
			}
			return this;
		};
		
		//改变宽度
		this.resize = function(width){
			p_this.find(".s_top_text").css("width",width);
			return this;
		};
		
		return this;
	};

	/**
	* 读取DIV容器的属性值，覆盖默认参数
	* @param 选择框容器
	* @param 默认参数列表
	* @return 无
	*/
	function initOptionsFromAttributes(p_this, defaults) {
		var parm = p_this.attr("width");
		if (parm)  {
			defaults.width = parseInt(parm);
		}
		parm = p_this.attr("overflow");
		if (parm)  {
			defaults.overflow = parm == "true";
		}
		parm = p_this.attr("popheight");
		if (parm)  {
			defaults.popheight = parseInt(parm);
		}
		parm = p_this.attr("popwidth");
		if (parm)  {
			defaults.popwidth = parseInt(parm);
		}
		parm = p_this.attr("type");
		if (parm)  {
			defaults.type = parm;
		}
		parm = p_this.attr("url");
		if (parm)  {
			defaults.url = parm;
		}
		parm = p_this.attr("data");
		if (parm)  {
			defaults.data = parm;
		}
		parm = p_this.attr("json");
		if (parm)  {
			defaults.json = parm == "true";
		}
		parm = p_this.attr("async");
		if (parm)  {
			defaults.async = parm == "true";
		}
		parm = p_this.attr("name");
		if (parm)  {
			//为兼容旧代码中使用该组件，已指定name属性时valueInputId值以些为准
			//旧代码很多直接取name属性值为ID的INPUT值，作为该组件的当件选中值
			//推荐使用getValue()来取值，初始化组件时不要给定name属性值
			defaults.valueInputId = parm;
		} else {
			//未指定是默认规则命名
			//存储组件值的INPUT的ID
			defaults.valueInputId = p_this.attr("id") + "_val";
		}
	}
    
	/**
	* 初始化过程
	* @param 选择框容器
	* @param 参数选项
	* @return 无
	*/
	function init(p_this, defaults) {
		//默认选择值
		var defaultvalue = [{name:"请选择...",value:""}];
		//取得该div的ID
		var p_id = p_this.attr("id");
		//读取原始列表数据
		var getData = getListData();
		//设为默认选中值
		if (getData.d_values.length>0) {
			defaultvalue = getData.d_values;
		}
		
		//替换现有的DIV内容
		p_this.html(createListData());
		p_this.css("display","");
		
		var l_top = p_this.find(".select_top");//列表框部分外层DIV
		var l_bottom = p_this.find(".select_bottom");//下拉部分的外层DIV
		var s_value = p_this.find(".s_top_val");//显示选择结果的层－－提交值
		var sd_value = p_this.find(".s_top_text");//显示选择结果的层－－显示值
		
		//设置用户定制的属性
		sd_value.css("width", defaults.width).attr("readonly", "true");

		l_bottom.css("font-size", sd_value.css("font-size"));
		
		//设置默认值
		setDefaultValue();
		//设置有效性
		setDisabled(p_this, defaults);
		
		//鼠标在列表选项上移动时样式切换
		l_bottom.find(".s_list_i").mouseover(function() {
			$(this).addClass("s_list_io");
		}).mouseout(function() {
			$(this).removeClass("s_list_io");
		});
		
		//列表选项点击事件
		l_bottom.find(".s_list_i").each(function(){
			addOptionClick($(this), p_this, defaults);
		});
	
		//添加下拉列表内附加按扭事件
		if (defaults.type == "multiple") {//添加多选按扭事件
			//全选按扭
			l_bottom.find(".s_list_bt .ms_all").click(function(){
				if (l_bottom.find(".select_list input:not(:checked)").size() == 0) {
					l_bottom.find(".select_list input").attr("checked", false);
				} else {
					l_bottom.find(".select_list input").attr("checked", true);
				}
			});
			//确定按扭
			l_bottom.find(".s_list_bt .ms_ok").click(function(){
				//s_value.html("");
				//由于多项选择时采用追加选择结果的方式
				//所以多项选择中，点击确定按钮时必须先将原来的选择结果清空，再重新赋值
				if ($("#" + defaults.valueInputId, p_this)){
					$("#" + defaults.valueInputId, p_this).val("");
				}
				var d_v = "";
				l_bottom.find(".select_list input:checked").each(function() {
					if (d_v != "") {
						d_v += ",";
					}
					d_v += $(this).parent().text();
					setSelectValue(p_this, $(this).val(), defaults);
				});
				sd_value.val(d_v);
				showlist(l_bottom,false,p_id);
				//响应回调方法
				if($.isFunction(defaults.onChange)){
	                defaults.onChange($("#"+defaults.valueInputId, p_this).val(), p_this.find(".s_top_text").val());
	            };
			});
		} 
		
		//初始化完后回调方法
		if($.isFunction(defaults.afterLoad)){
            defaults.afterLoad();
        };
        
        
		/**
		* 读取选择框的候选项列表，以及读取选择框默认值
		* @param 候选项<div>
		* @param 选择框类型
		* @param 结果域ID
		* @return 候选项数组 + 默认值数组
		*/
		function getListData() {
			var rows = [];
			var d_values = [];
			//遍历各候选项，组装候选列表和默认值列表
			p_this.children("div").each(function(i){
				var data = $(this).text();
				if (data.indexOf("separator")==0){
					//分隔符
					var d = data.split(":");
					var row = {};
					row.type1 = "separator";
					row.name1 = d.length>1?d[1]:"";
					rows[i] = row;
				}else{
					var d = data.split("**");
					if (d.length < 2) return;
					if ($(this).attr("isselected") == "true") {//设为默认值
						d_values[(defaults.type == "multiple") ? d_values.length:0]= {name:d[1],value:d[0]};
					}
					var row = {};
					row.type1 = "option";
					row.value1 = d[0];
					row.name1 = d[1];
					rows[i] = row;
				}
			});
			//返回结果
			return {rows:rows,d_values:d_values};
		}
	
		/**
		* 生成列表选择框的HTML代码
		* @return HTML代码段
		*/
		function createListData() {
			var inputtype = "hidden";
			if (defaults.type == "multiple") {
					inputtype = "checkbox";
			}
			var list = "<div class='select_top'><input class='s_top_text' type='text' value='请选择...'/>";
					if(defaults.isdisabled==false){
						list += "<span class='s_top_img' title='请选择'></span>"
					}else{
						list += "<span class='s_top_img' title='不可选择'></span>"
					}
					list += "<div class='s_top_val'></div></div>"
					+ "<div class='select_bottom'><div class='select_list'>";
	
			for(i = 0; i < getData.rows.length; i++) {
				var row = getData.rows[i];
				if (row.type1=="separator"){
					list += "<div class='s_separator' align='center'>" + (row.name1 ? "<span>" +row.name1 + "</span>" : "") + "</div>";
				}else{
					list += "<div class='s_list_i'><input type='" + inputtype +"' value='" 
						+ row.value1 + "' />" + row.name1 + "</div>";
				}
			}
			list += "</div>";
			if (defaults.type == "multiple") {
				list += "<div class='s_list_bt'><input type='button' class='ms_all' alt='all'/>"
						+ "<input type='button' class='ms_ok' alt='ok'/></div>";
				
			} 
			list += "</div>";
			return list;
		}
	
		/**
		* 设置选择框默认值
		*/
		function setDefaultValue() {
			var ds_names = "";
			for (i = 0; i < defaultvalue.length; i++) {
				//默认值必须在候选项范围内，否则不生效
				for (var j=0 ; j < getData.rows.length; j++){
					var row = getData.rows[j];
					if (row.type1!="separator" && defaultvalue[i].value == row.value1){
						if (ds_names != "") {
							ds_names += ",";	
						}
						ds_names += getData.rows[j].name1;	//显示值
						setSelectValue(p_this, defaultvalue[i].value, defaults);	//选择值
						if (defaults.type=="multiple"){	//多选框选择
							l_bottom.find(".s_list_i input[value='" + defaultvalue[i].value + "']").attr("checked", true);
						}
					}
				}
			}
			if (ds_names!=""){
				sd_value.val(ds_names);
			}else{
				sd_value.val("请选择...");
			}
		}

	}
	
	//设置下拉列表的有失效状态
    function setDisabled(p_this, defaults) {
    	var l_top = p_this.find(".select_top");
    	var l_bt =  l_top.find("span");
    	if (defaults.isdisabled) {//添加不可用属性
			l_top.unbind();//解除所有事件
			l_top.find(".s_top_text").attr("disabled","true");//文本失效
			l_top.find(".s_top_img").attr("title","不可选择");
			l_bt.attr('class', 's_top_img');//失效时下拉按扭的样式
			
		} else {
			var p_id = p_this.attr("id");
			var l_bottom = p_this.find(".select_bottom");
		
			l_top.find(".s_top_text").removeAttr("disabled");//文本有效
			//改变下拉按扭的样式
			l_top.mouseover(function() {l_bt.attr('class', "s_top_img_over");})
				.mouseout(function() {l_bt.attr('class', 's_top_img');})
				.mousedown(function() {l_bt.attr('class', 's_top_img_down');})
				.mouseup(function() {l_bt.attr('class', 's_top_img_over');});
				
			//添加列表显示与隐藏事件
			l_top.click(function(){
				if($.isFunction(defaults.onClick) && !defaults.onClick()){
					//onClick方法返回false取消点击下拉事件
					return;
				}
				if (l_bottom.is(':visible')) {
		  			showlist(l_bottom,false,p_id);
		  		} else {
		  			positionList();	//一定要在弹出前定位，否则外层元素有滚动条时会错位
					showlist(l_bottom,true,p_id);
		  		}
			});
		}
		
		/**
		* 定位下拉列表框的位置
		*/
		function positionList(){
			//设置弹出宽度（要弹出时设置，否则在初始化本控件时，外层元素是隐藏的情况下，l_top.width()取到为0）
			if(defaults.popwidth) {
				l_bottom.find(".select_list").width(defaults.popwidth);
			} else {
				//与上部分一样宽
				l_bottom.find(".select_list").width(l_top.width()-2);
			}
			
			//是否显示滚动条与设置弹出高度
			if (defaults.overflow) {
				l_bottom.find(".select_list").css("overflow","auto");
				if (l_bottom.height() > defaults.popheight){
					//如果有设预设高度且实际高度大于预设高度时
					l_bottom.find(".select_list").height(defaults.popheight);
				}
			}
		
			//设置弹出框的位置
	//		var offset = getLTWH(l_top[0],getPositionEle(l_top[0]));
			var offset = l_top.offset();
			//超出屏幕范围的，要反方向呈现
			if (offset.top + l_top.height() + 1 + l_bottom.height() > $(document).height()//往下弹的高度不够
					&& offset.top - 1 - l_bottom.height() > 0){//且往上弹的高度够的情况，向上弹出
				l_bottom.css("top",offset.top - l_bottom.height() - 3);
			} else{
				l_bottom.css("top",offset.top + l_top.height()+1);
			}
			if (document.documentMode && document.documentMode < 8) {//兼容模式时要设置left
				l_bottom.css("left",offset.left);
			}
			
		}
//		
//		//取得元素在BODY中的绝对坐标及宽高
//	    //@RETURN {"left": o.offsetLeft,"top": o.offsetTop,"width": o.offsetWidth,"height": o.offsetHeight}
//		function getLTWH(o,zone){
//	        zone = zone || document.body;
//	
//	        function getCurrentStyle(style){
//	            var number = parseInt(o.currentStyle[style]);
//	            return isNaN(number) ? 0 : number;
//	        }
//	        function getComputedStyle(style){
//	            return parseInt(document.defaultView.getComputedStyle(o, null).getPropertyValue(style));
//	        }
//	        var oLTWH = {
//	            "left": o.offsetLeft,
//	            "top": o.offsetTop,
//	            "width": o.offsetWidth,
//	            "height": o.offsetHeight
//	        };
//	        while (true) {
//	            o = o.offsetParent;
//	            if (o == zone || o == null)
//	                break;
//	            oLTWH.left += o.offsetLeft - o.scrollLeft;//解决出现滚动条时，发生偏移现象
//	            oLTWH.top += o.offsetTop - o.scrollTop;
//	            if ($.browser.msie) {
//	                oLTWH.left += getCurrentStyle("borderLeftWidth");
//	                oLTWH.top += getCurrentStyle("borderTopWidth");
//	            }
//	            if ($.browser.mozilla) {
//	                oLTWH.left += getComputedStyle("border-left-width");
//	                oLTWH.top += getComputedStyle("border-top-width");
//	            }
//	        }
//	        return oLTWH;
//	    }
//		
//		/**
//	    * 寻找元素的顶级容器
//	    * 向源元素上级一直查找含有某些CSS属性的元素
//	    * @param 源元素，即起点函数
//	    * @return 元素的顶级容器
//	    */
//	    function getPositionEle(o){
//	        var curJq = null;
//	        while(o = o.offsetParent){
//	            curJq = $(o);
//	            if(curJq.css("position") == "absolute" || curJq.css("position") == "relative"){
//	                curJq = null;
//	                return o;
//	            };
//	        };
//	        curJq = null;
//	        o = null;
//	        
//	        return document.body;
//	    }

    }
    
	/**
	* 为每个选项添加点击事件
	* @param option 选项对像
	* @param p_this 下拉控件本身对像
	* @param defaults 控件属性列表
	* @return 无
	*/
	function addOptionClick(option, p_this, defaults) {
		if (defaults.type == "multiple") {//多选时点击事件
			option.click(function(e){
				if(!$(e.target).is("input")) {
					$(this).contents("input[type='checkbox']").click();
				}
			});
		} else {//单选及区间时点击事件
			option.click(function(e){
				//s_value.html("");
				setSelectValue(p_this, $(this).contents("input[type='hidden']").val(), defaults);
				p_this.find(".s_top_text").val($(this).text());
				showlist(p_this.find(".select_bottom"),false,p_this.attr("id"));
				//响应回调方法
				if($.isFunction(defaults.onChange)){
	                defaults.onChange($("input:hidden",this).val(),$(this).text());
	            };
			});
		}
	}
	
	/**
	* 设置列表框选择结果
	* @param 选择结果存储域的容器
	* @param 当前选择值
	* @param 控件属性列表
	* @return 无
	*/
	function setSelectValue(p_this, value, defaults) {
		var s_value = p_this.find(".s_top_val");//显示选择结果的层－－提交值
		switch(defaults.type) {
			case "single":
				//如果页面上已存在ID为defaults.valueInputId的域，则更新其值
				//否则添加一个隐藏域，用于存储选择值
				var obj = $("#" + defaults.valueInputId, p_this);
				if (obj.size() > 0){
					obj.val(value);
				}else{
					s_value.append("<input type='hidden' id='" + defaults.valueInputId + "' name='" + defaults.valueInputId + "' value='" + value +"'/>");
				}
				break;
			case "multiple":
				//如果页面上已存在ID为defaults.valueInputId的域，则更新其值
				//否则添加一个隐藏域，用于存储选择值
				var obj = $("#" + defaults.valueInputId, p_this);
				if (obj.size() > 0){
					if (obj.val()==""){
						obj.val(value);
					}else{
						obj.val(obj.val() + "," + value);//追加
					}
				}else{
					s_value.append("<input type='hidden' id='" + defaults.valueInputId + "' name='" + defaults.valueInputId + "' value='" + value +"'/>");
				}
				break;
		}
	}
	
	var c_open = "";	//全局变量，当前展开的列表ID
	/**
	* 显示或隐藏下拉选择框
	* @param 下拉选择框容器
	* @param 显示选项，true:显示，false:隐藏
	* @param 当前选择框的容器ID
	* @return 无
	*/
	function showlist(jo, flag, p_id) {
		if (flag) {
			//jo.slideDown();
			jo.show();
			c_open = p_id;
		} else {
			//jo.slideUp();
			jo.hide();
			c_open = "";
		}
	}	
	
	/**
	* 添加鼠标点击事件，点击组件外部时列表收缩
	*/
	$(document).mousedown(function(event) {
		if (c_open != "" && $(event.target).parents("#" + c_open).size() == 0) {
			//已打开点的位置不是弹出层时
			showlist($("#" + c_open + " .select_bottom"),false);
		}
	});	
})(jQuery);