/*
* jQuery SimpleTree Drag&Drop plugin
* Update on 22th May 2008
* Version 0.3
*
* Licensed under BSD <http://en.wikipedia.org/wiki/BSD_License>
* Copyright (c) 2008, Peter Panov <panov@elcat.kg>, IKEEN Group http://www.ikeen.com
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*     * Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright
*       notice, this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the name of the Peter Panov, IKEEN Group nor the
*       names of its contributors may be used to endorse or promote products
*       derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY Peter Panov, IKEEN Group ``AS IS'' AND ANY
* EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL Peter Panov, IKEEN Group BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/


$.fn.simpleTree = function(opt){
	return this.each(function(){
		var TREE = this;
		var ROOT = $('.root',this);
		var mousePressed = false;
		var mouseMoved = false;
		var dragMoveType = false;
		var dragNode_destination = false;
		var dragNode_source = false;
		var dragDropTimer = false;
		var ajaxCache = Array();

		/** added by zhengchao  2010-03-31
		 * beforeAjax : 展开AJAX之前的事件接口
		 */
		var beforeAjax = false;
		
		/** added by zhengchao  2010-04-05
		 * nodeCheckBox : 多选框启用与否
		 */
		var nodeCheckBox = false;
		
		/** added by zhengchao  2010-04-05
		 * checkBoxClick : 多选框事件复写接口
		 */
		var checkBoxClick = false;
		
		TREE.option = {
			drag:		true,
			animate:	false,
			autoclose:	false,
			speed:		'fast',
			afterAjax:	false,
			afterMove:	false,
			afterClick:	false,
			afterDblClick:	false,
			// added by Erik Dohmen (2BinBusiness.nl) to make context menu cliks available
			afterContextMenu:	false,
			docToFolderConvert:false,
			selectRoot : false,
			dataSrcPath : "",//用于跨域：数据来源
			basePath : "",//用于跨域：当前系统路径
			
			beforeAjax : false, //beforeAjax : 展开AJAX之前事件
			
			/** 
			 * 复选框属性及状态标志
			 * 默认属性，支持重新定义 
			 * by zhengchao 2010-04-05
			 * 例： <span ck='a'>表示全选</span>
			 */
			nodeCheckBox : false,
			/**
			 * ck
			 */
			ckFlag : 'ck',          //标签中的元素
			/**
			 * a
			 */
			allCheckFlag  : 'a',    //全选
			/**
			 * h
			 */
			halfCheckFlag : 'h',    //半选
			/**
			 * n
			 */
			noCheckFlag   : 'n',    //没选
			checkBoxClick : false,   //多选框事件
			
			treeBiz : ""//CHILD_NOT_SELECTED:当节点选中时其下的子节点不能再次被选
		};
		TREE.option = $.extend(TREE.option,opt);
		$.extend(this, {getSelected: function(){
			return $('span.active', this).parent();
		}});
		TREE.closeNearby = function(obj)
		{
			$(obj).siblings().filter('.folder-open, .folder-open-last').each(function(){
				var childUl = $('>ul',this);
				var className = this.className;
				this.className = className.replace('open','close');
				if(TREE.option.animate)
				{
					childUl.animate({height:"toggle"},TREE.option.speed);
				}else{
					childUl.hide();
				}
			});
		};
		TREE.nodeToggle = function(obj)
		{
			var childUl = $('>ul',obj);
			if(childUl.is(':visible')){
				obj.className = obj.className.replace('open','close');

				if(TREE.option.animate)
				{
					childUl.animate({height:"toggle"},TREE.option.speed);
				}else{
					childUl.hide();
				}
			}else{
				obj.className = obj.className.replace('close','open');
				if(TREE.option.animate)
				{
					childUl.animate({height:"toggle"},TREE.option.speed, function(){
						if(TREE.option.autoclose)TREE.closeNearby(obj);
						if(childUl.is('.ajax'))TREE.setAjaxNodes(childUl, obj.id);
					});
				}else{
					childUl.show();
					if(TREE.option.autoclose)TREE.closeNearby(obj);
					if(childUl.is('.ajax'))TREE.setAjaxNodes(childUl, obj.id);
				}
			}
		};
		TREE.setAjaxNodes = function(node, parentId, callback)
		{
			if($.inArray(parentId,ajaxCache) == -1){
				ajaxCache[ajaxCache.length]=parentId;
				var url = $.trim($('>li', node).text());
				if(url && url.indexOf('url:'))
				{
					url=$.trim(url.replace(/.*\{url:(.*)\}/i ,'$1'));
					
					if (TREE.option.dataSrcPath==null || TREE.option.dataSrcPath == ""){
						//兼容原来的方式
						$.ajax({
							type: "GET",
							url: url,
							contentType:'html',
							cache:false,
							success: function(response){
								aft_setAjaxNodes(response);
							}
						});
					}else{
						//处理跨域访问
						if (TREE.option.dataSrcPath != TREE.option.basePath){
							//跨域
							url = TREE.option.dataSrcPath + url + "&format=json&jsoncallback=?";
						}else{
							//非跨域
							url = TREE.option.dataSrcPath + url + "&format=json";
						}
						$.getJSON(url,function(response){
							aft_setAjaxNodes(response.htmlValue);
						});
					}
					
					function aft_setAjaxNodes(data){
						if (typeof TREE.option.beforeAjax == 'function'){
							//AJAX节点展开前动作
				            TREE.option.beforeAjax(node);
				        }
						node.removeAttr('class');
						node.html(data);
						$.extend(node,{url:url});
						TREE.setTreeNodes(node, false);
						//异步方式时执行
						//add by shenyinjie 2011-09-15;如果父节点下的所有子节点都为disabled，则父节点应设为disabled
						if ($("ul :checkbox:enabled", node.parent()).size() == 0) {
							$(">:checkbox",node.parent()).attr("disabled", "disabled");
						}
						//如果父节点下的所有子节点都为unchecked，则父节点应设为unchecked
						if ($("ul :checkbox:checked", node.parent()).size() == 0) {
							$(">:checkbox",node.parent()).attr("checked", false);
						}
						//TREE.changeParentNodes($(">:checkbox",node.parent()));
						//add by shenyinjie;2009-12-8;增加对复选框的处理,父节点被选中,子节点不能再选	
						if(TREE.option.treeBiz == "CHILD_NOT_SELECTED"){
							//在进行Ajax加载下一级时要将下级节点置为不可选
							if($(">:checkbox",node.parent()).attr("checked") == true){
								//父节点被选中,子节点不能再选
								$(":checkbox",node).attr("checked", false);
								$(":checkbox",node).attr("disabled", true);
							}
												
							$(":checkbox").click(function(){
								var oChk = this;
								if(this.checked){
									if($(this).parent().attr("class").indexOf("doc") == -1){//只有文件夹才有级联置子节点为不可选
										$(":checkbox",($(this).parent().children("ul").get(0))).each(function(){//li ul li ">:checkbox"
											$(this).attr("checked", false);
											$(this).attr("disabled", true);
										});
									}
								}
								else{
									if($(this).parent().attr("class").indexOf("doc") == -1){//只有文件夹才有级联置子节点为可选
										$(":checkbox",($(this).parent().children("ul").get(0))).each(function(){//li ul li ">:checkbox"
											$(this).attr("disabled", false);
										});
									}
								}
							});
						}
						//add by shenyinjie;2009-12-8;增加对复选框的处理　end
						
						if(typeof TREE.option.afterAjax == 'function')
						{
							TREE.option.afterAjax(node);
						}
						if(typeof callback == 'function')
						{
							callback(node);
						}
					}
				}//end of if(url && url.indexOf('url:'))
				
			}
		};
		TREE.setTreeNodes = function(obj, useParent){
			obj = useParent? obj.parent():obj;
			var findexp = TREE.option.selectRoot ? '>span,li>span' : 'li>span';
			$(findexp, obj).addClass('text')
			.bind('selectstart', function() {
				return false;
			}).click(function(){
				//tim modify 在原来样式基础上加入active
				$('.active',TREE).removeClass("active");
				this.className='active '+this.className;
				if(typeof TREE.option.afterClick == 'function')
				{
					TREE.option.afterClick($(this).parent());
				}
				return false;
			}).dblclick(function(){
				mousePressed = false;
				TREE.nodeToggle($(this).parent().get(0));
				if(typeof TREE.option.afterDblClick == 'function')
				{
					TREE.option.afterDblClick($(this).parent());
				}
				return false;
				// added by Erik Dohmen (2BinBusiness.nl) to make context menu actions
				// available
			}).bind("contextmenu",function(){
				$('.active',TREE).attr('class','text');
				if(this.className=='text')
				{
					this.className='active';
				}
				if(typeof TREE.option.afterContextMenu == 'function')
				{
					TREE.option.afterContextMenu($(this).parent());
				}
				return false;
			}).mousedown(function(event){
				mousePressed = true;
				cloneNode = $(this).parent().clone();
				var LI = $(this).parent();
				if(TREE.option.drag)
				{
					$('>ul', cloneNode).hide();
					$('body').append('<div id="drag_container"><ul></ul></div>');
					$('#drag_container').hide().css({opacity:'0.8'});
					$('#drag_container >ul').append(cloneNode);
					$("<img>").attr({id	: "tree_plus",src	: "images/plus.gif"}).css({width: "7px",display: "block",position: "absolute",left	: "5px",top: "5px", display:'none'}).appendTo("body");
					$(document).bind("mousemove", {LI:LI}, TREE.dragStart).bind("mouseup",TREE.dragEnd);
				}
				return false;
			}).mouseup(function(){
				if(mousePressed && mouseMoved && dragNode_source)
				{
					TREE.moveNodeToFolder($(this).parent());
				}
				TREE.eventDestroy();
			});
			
			/**
			 * 生长出多选框 
			 * @author zhengchao
			 */
			if(TREE.option.nodeCheckBox){
				/**
				 * 1没选;2:全选
				 */
				var parentChecked_ = 0 ; // 全选框默认 undefined状态
				/**
				 * 1:没选;2:半选
				 */
				var parentIndeterminate_ = 0; //半选框默认 undefined状态
				
				if($("input:first",obj.parent()).attr("checked")==true){
					//if ($("ul :checkbox", obj.parent()).size() != 0 && 
							//$("ul :checkbox:checked", obj.parent()).size() == 0 && 
							//$("ul :checkbox:enabled", obj.parent()).size() == 0) {
						//如果子节点都不选
						//parentChecked_ = 1;//没选
					//} else {
						parentChecked_ = 2; //全选
					//}
					
				}//else if($("input:first",obj.parent()).attr("checked")==false){
				 else if($("input:first:enabled",obj.parent()).attr("checked")==false){
					parentChecked_ = 1; //没选
				}
				
				//if($("input:first",obj.parent()).attr("indeterminate")==true){
				if($("input:first:enabled",obj.parent()).attr("indeterminate")==true){
					parentIndeterminate_ = 2; //半选
				}//else if($("input:first",obj.parent()).attr("indeterminate")==false){
				 else if($("input:first:enabled",obj.parent()).attr("indeterminate")==false){
					parentIndeterminate_ = 1; //没选
				}
				
				$('li', obj).each(function (){
					//纯净标签
					//var input_ = $("input[type=checkbox]",$(this)).eq(0);
					//var input_ = $("input[type=checkbox]",$(this)).eq(0);
					var input_ = $("input[type=checkbox]:enabled",$(this)).eq(0);
					//如果父节点全选，则将所有下级子节点全选
					if(parentChecked_ == 2)$(this).attr(TREE.option.ckFlag,TREE.option.allCheckFlag);
					if(parentChecked_ == 1 && parentIndeterminate_ == 1)$(this).attr(TREE.option.ckFlag,TREE.option.noCheckFlag);
					//状态构造
					if($(this).attr(TREE.option.ckFlag) == undefined || //没有设置标志，默认认为没选
					   	$(this).attr(TREE.option.ckFlag) == TREE.option.noCheckFlag){//没选
					   	$(this).attr(TREE.option.ckFlag,TREE.option.noCheckFlag);//修正 undefined 设置
					   	TREE.checkBoxStatus(input_,false,false);
					}else if($(this).attr(TREE.option.ckFlag) == TREE.option.halfCheckFlag){//半选
					   	TREE.checkBoxStatus(input_,true,false);
					}else if($(this).attr(TREE.option.ckFlag) == TREE.option.allCheckFlag){//全选
					   	TREE.checkBoxStatus(input_,false,true);
					}
					if(typeof TREE.option.checkBoxClick == 'function'){//如果事件被重写，则利用重写的事件规则
						TREE.option.checkBoxClick(input_);
					}else{// 否则为复选框绑定默认事件规则
						input_.click(function (e){
							TREE.checkBoxClickDefault(input_);
						});
					}
				});
			}
			
			
			$('li', obj).each(function(i){
				var className = this.className;
				var open = false;
				var cloneNode=false;
				var LI = this;
				var childNode = $('>ul',this);
				if(childNode.size()>0){
					var setClassName = 'folder-';
					if(className && className.indexOf('open')>=0){
						setClassName=setClassName+'open';
						open=true;
					}else{
						setClassName=setClassName+'close';
					}
					this.className = setClassName + ($(this).is(':last-child')? '-last':'');

					if(!open || className.indexOf('ajax')>=0)childNode.hide();

					TREE.setTrigger(this);
				}else{
					var setClassName = 'doc';
					this.className = setClassName + ($(this).is(':last-child')? '-last':'');
				}
			}).before('<li class="line">&nbsp;</li>')
			.filter(':last-child').after('<li class="line-last"></li>');
			TREE.setEventLine($('.line, .line-last', obj));
		};
		
		/**
		 * 复选框状态控制 by zhengchao
		 * 给inputCheck对象的indeterminate、checked属性赋值
		 */
		TREE.checkBoxStatus = function(inputCheck,indeterminate,checked){
			//判断类型，这里必须是BOOLEAN类型
			if(typeof(indeterminate)=="boolean" && typeof(checked)=="boolean"){
				$(inputCheck).attr("indeterminate",indeterminate);
				$(inputCheck).attr("checked",checked);
			}else{
				alert("出现参数类型异常jquery.simple.tree.js==> 318行");
			}
		}
		
		// 复选框默认事件 by zhengchao
		TREE.checkBoxClickDefault = function(input){
			if($(input).attr("checked")){//如果选中
				$("input[type=checkbox]:enabled",$(input).parent()).each(function (index){//对于disabled的checkbox不计入。modify by shenyinjie 2011/09/15
		            TREE.checkBoxStatus(this,false,true);//构造当前所操作的复选框的状态
			    });
			}else{//没选中
				$("input[type=checkbox]:enabled",$(input).parent()).each(function (index){//对于disabled的checkbox不计入。modify by shenyinjie 2011/09/15
		            TREE.checkBoxStatus(this,false,false);//构造当前所操作的复选框的状态
			    });
			}
			TREE.changeParentNodes($(input));//构造当前所操作的复选框其所有父节点的复选框状态
			
		};
		
		/**
		 * 改变父节点状态(递归)
		 * @param {} node
		 * by zhengchao
		 */
		TREE.changeParentNodes = function(input){
			var input_ = $(input).parent().parent().parent();
			//根据根节点没有复选框的要求，来跳出当前递归
			if($(input_).hasClass("root")) return ;
		    var parentInput = $("input:first",$(input_)).eq(0);//找到父节点的选择框
		    var callbackValue = TREE.decideBrotherChecked(input) ; //搜捕同级兄弟节点状态
		    //父节点状态
		    if(callbackValue==2){
		    	TREE.checkBoxStatus(parentInput,false,true);//选中
		    }else if(callbackValue==1){
		    	TREE.checkBoxStatus(parentInput,true,false);//半选
		    }else if(callbackValue==0){
		    	TREE.checkBoxStatus(parentInput,false,false);//未选
		    }
		    TREE.changeParentNodes($(parentInput));
		}

		/**
		 * 判断同级节点状态
		 * return ：
		 *      全选上了： 2
		 *      半选上了： 1
		 *      都没选上： 0
		 * @author zhengchao
		 */
		TREE.decideBrotherChecked = function (input){
		    
			/**
			 * ul
			 */
		    var nowThis = $(input).parent().parent();
		    
		    var flagInteger = 0 ; //返回值变量
		    var temp_ = 0; //临时变量
		    //var countBrother = $("input[type=checkbox]" ,nowThis).size();//同级兄弟节点数
   			var countBrother = $("input[type=checkbox]:enabled" ,nowThis).size();//同级兄弟节点数//对于disabled的checkbox不计入。modify by shenyinjie 2011/09/15
   			
		    //$("input[type=checkbox]" ,nowThis).each(function (index){
		   	  $("input[type=checkbox]:enabled" ,nowThis).each(function (index){//对于disabled的checkbox不计入。modify by shenyinjie 2011/09/15
		        if(this.checked) temp_++;//选中
		        if(this.indeterminate){//发现半选，直接判定结果，并跳出循环
		            flagInteger = 1;
		            return false ;
		        }
		    });
		    if(flagInteger != 1){
		        if(temp_ == 0) return 0;
		        if(temp_ != countBrother) return 1;
		        if(temp_ == countBrother) return 2;
		    } 
		    return flagInteger;
		}
		
		
		TREE.setTrigger = function(node){
			var htmlImg = '<img class="trigger" src="images/spacer.gif" border=0>';
			if($(">:checkbox",node).length > 0){//有checkbox在checkbox前加htmlImg,这样才可以做到点击+,-符号展开/收缩节点　modify by shenyinjie;2009-12-08
				$('>:checkbox',node).before(htmlImg);
			} else{
				$('span:first',node).before(htmlImg);
			}
			
			var trigger = $('>.trigger', node);
			trigger.click(function(event){
				TREE.nodeToggle(node);
			});
			if(!$.browser.msie)
			{
				trigger.css('float','left');
			}
		};
		TREE.dragStart = function(event){
			var LI = $(event.data.LI);
			if(mousePressed)
			{
				mouseMoved = true;
				if(dragDropTimer) clearTimeout(dragDropTimer);
				if($('#drag_container:not(:visible)')){
					$('#drag_container').show();
					LI.prev('.line').hide();
					dragNode_source = LI;
				}
				$('#drag_container').css({position:'absolute', "left" : (event.pageX + 5), "top": (event.pageY + 15) });
				if(LI.is(':visible'))LI.hide();
				var temp_move = false;
				if(event.target.tagName.toLowerCase()=='span' && $.inArray(event.target.className, Array('text','active','trigger'))!= -1)
				{
					var parent = event.target.parentNode;
					var offs = $(parent).offset({scroll:false});
					var screenScroll = {x : (offs.left - 3),y : event.pageY - offs.top};
					var isrc = $("#tree_plus").attr('src');
					var ajaxChildSize = $('>ul.ajax',parent).size();
					var ajaxChild = $('>ul.ajax',parent);
					screenScroll.x += 19;
					screenScroll.y = event.pageY - screenScroll.y + 5;

					if(parent.className.indexOf('folder-close')>=0 && ajaxChildSize==0)
					{
						if(isrc.indexOf('minus')!=-1)$("#tree_plus").attr('src','images/plus.gif');
						$("#tree_plus").css({"left": screenScroll.x, "top": screenScroll.y}).show();
						dragDropTimer = setTimeout(function(){
							parent.className = parent.className.replace('close','open');
							$('>ul',parent).show();
						}, 700);
					}else if(parent.className.indexOf('folder')>=0 && ajaxChildSize==0){
						if(isrc.indexOf('minus')!=-1)$("#tree_plus").attr('src','images/plus.gif');
						$("#tree_plus").css({"left": screenScroll.x, "top": screenScroll.y}).show();
					}else if(parent.className.indexOf('folder-close')>=0 && ajaxChildSize>0)
					{
						mouseMoved = false;
						$("#tree_plus").attr('src','images/minus.gif');
						$("#tree_plus").css({"left": screenScroll.x, "top": screenScroll.y}).show();

						$('>ul',parent).show();
						/*
							Thanks for the idea of Erik Dohmen
						*/
						TREE.setAjaxNodes(ajaxChild,parent.id, function(){
							parent.className = parent.className.replace('close','open');
							mouseMoved = true;
							$("#tree_plus").attr('src','images/plus.gif');
							$("#tree_plus").css({"left": screenScroll.x, "top": screenScroll.y}).show();
						});

					}else{
						if(TREE.option.docToFolderConvert)
						{
							$("#tree_plus").css({"left": screenScroll.x, "top": screenScroll.y}).show();
						}else{
							$("#tree_plus").hide();
						}
					}
				}else{
					$("#tree_plus").hide();
				}
				return false;
			}
			return true;
		};
		TREE.dragEnd = function(){
			if(dragDropTimer) clearTimeout(dragDropTimer);
			TREE.eventDestroy();
		};
		TREE.setEventLine = function(obj){
			obj.mouseover(function(){
				if(this.className.indexOf('over')<0 && mousePressed && mouseMoved)
				{
					this.className = this.className.replace('line','line-over');
				}
			}).mouseout(function(){
				if(this.className.indexOf('over')>=0)
				{
					this.className = this.className.replace('-over','');
				}
			}).mouseup(function(){
				if(mousePressed && dragNode_source && mouseMoved)
				{
					dragNode_destination = $(this).parents('li:first');
					TREE.moveNodeToLine(this);
					TREE.eventDestroy();
				}
			});
		};
		TREE.checkNodeIsLast = function(node)
		{
			if(node.className.indexOf('last')>=0)
			{
				var prev_source = dragNode_source.prev().prev();
				if(prev_source.size()>0)
				{
					prev_source[0].className+='-last';
				}
				node.className = node.className.replace('-last','');
			}
		};
		TREE.checkLineIsLast = function(line)
		{
			if(line.className.indexOf('last')>=0)
			{
				var prev = $(line).prev();
				if(prev.size()>0)
				{
					prev[0].className = prev[0].className.replace('-last','');
				}
				dragNode_source[0].className+='-last';
			}
		};
		TREE.eventDestroy = function()
		{
			// added by Erik Dohmen (2BinBusiness.nl), the unbind mousemove TREE.dragStart action
			// like this other mousemove actions binded through other actions ain't removed (use it myself
			// to determine location for context menu)
			$(document).unbind('mousemove',TREE.dragStart).unbind('mouseup').unbind('mousedown');
			$('#drag_container, #tree_plus').remove();
			if(dragNode_source)
			{
				$(dragNode_source).show().prev('.line').show();
			}
			dragNode_destination = dragNode_source = mousePressed = mouseMoved = false;
			//ajaxCache = Array();
		};
		TREE.convertToFolder = function(node){
			node[0].className = node[0].className.replace('doc','folder-open');
			node.append('<ul><li class="line-last"></li></ul>');
			TREE.setTrigger(node[0]);
			TREE.setEventLine($('.line, .line-last', node));
		};
		TREE.convertToDoc = function(node){
			$('>ul', node).remove();
			$('img', node).remove();
			node[0].className = node[0].className.replace(/folder-(open|close)/gi , 'doc');
		};
		/**
		 * node:当前选择节点的li
		 */
		TREE.moveNodeToFolder = function(node)
		{
			//alert(node.text())
			//alert(node[0].className == 'root' && $('>ul >li', node).size() == 0);
			if(!TREE.option.docToFolderConvert && node[0].className.indexOf('doc')!=-1)
			{
				return true;
			}else if(TREE.option.docToFolderConvert && 
				(node[0].className.indexOf('doc')!=-1) ||//根节点添加第一个节点
				node[0].className == 'root' && $('>ul >li', node).size() == 0){
				TREE.convertToFolder(node);
			}
			TREE.checkNodeIsLast(dragNode_source[0]);
			var lastLine = $('>ul >.line-last', node);
			if(lastLine.size()>0)
			{
				TREE.moveNodeToLine(lastLine[0]);
			}
		};
		TREE.moveNodeToLine = function(node){
			TREE.checkNodeIsLast(dragNode_source[0]);
			TREE.checkLineIsLast(node);
			var parent = $(dragNode_source).parents('li:first');
			var line = $(dragNode_source).prev('.line');
			$(node).before(dragNode_source);
			$(dragNode_source).before(line);
			node.className = node.className.replace('-over','');
			var nodeSize = $('>ul >li', parent).not('.line, .line-last').filter(':visible').size();
			if(TREE.option.docToFolderConvert && nodeSize==0)
			{
				TREE.convertToDoc(parent);
			}else if(nodeSize==0)
			{
				parent[0].className=parent[0].className.replace('open','close');
				$('>ul',parent).hide();
			}

			// added by Erik Dohmen (2BinBusiness.nl) select node
			if($('span:first',dragNode_source).attr('class')=='text')
			{
				$('.active',TREE).attr('class','text');
				$('span:first',dragNode_source).attr('class','active');
			}

			if(typeof(TREE.option.afterMove) == 'function')
			{
				var pos = $(dragNode_source).prevAll(':not(.line)').size();
				TREE.option.afterMove($(node).parents('li:first'), $(dragNode_source), pos);
			}
		};

		TREE.addNode = function(id, text, callback)
		{
			var temp_node = $('<li><ul><li id="'+id+'"><span>'+text+'</span></li></ul></li>');
			TREE.setTreeNodes(temp_node);
			dragNode_destination = TREE.getSelected();
			dragNode_source = $('.doc-last',temp_node);
			TREE.moveNodeToFolder(dragNode_destination);
			temp_node.remove();
			if(typeof(callback) == 'function')
			{
				callback(dragNode_destination, dragNode_source);
			}
		};
		TREE.delNode = function(callback)
		{
			dragNode_source = TREE.getSelected();
			
			var parentNode = dragNode_source.parent().parent();
			
			TREE.checkNodeIsLast(dragNode_source[0]);
			dragNode_source.prev().remove();
			dragNode_source.remove();
			
			//add by zsy 没有子节点时转为文件
			var childNode = $('>ul:has(span)',parentNode);
			if (childNode.size() == 0) { 
				TREE.convertToDoc(parentNode);
			}
			
			if(typeof(callback) == 'function')
			{
				callback(dragNode_destination);
			}
		};

		TREE.init = function(obj)
		{
			TREE.setTreeNodes(obj, false);
		};
		TREE.init(ROOT);
		
		
		//修改节点名称//id为空时不修改 add by zsy
		TREE.modifyNode = function(id, text, callback)
		{
			var activeSpan = $('span.active', this);
			activeSpan.html(text);
			var selectedNode = activeSpan.parent();
			if (id) {
				selectedNode.attr("id", id);
			}
			if(typeof(callback) == 'function')
			{
				callback(selectedNode);
			}
		};
		
		TREE.setNodeClassName = function(className)
		{
			var activeSpan = $('span.active', this);
			if(className){
				activeSpan.removeClass();
				activeSpan.addClass("active")
				activeSpan.addClass(className);
			}
		};
		
		//取得当前节点的父节点 add by zsy
		TREE.getParentNode = function()
		{
			var node = TREE.getSelected();
			if(node.attr('class') == 'root') {
				return ROOT;
			} else {
				return node.parent().parent();
			}
		};
		
		//展开节点parm为节点ID或JQuery对像都可以 add by zsy
		TREE.openNode = function(parm, callback)
		{
			var node = (typeof(parm) == 'string') ? $('#' + parm, this) : parm;
			var obj = node[0];
			var childUl = $('>ul',obj);
			if(childUl.size() > 0 && !childUl.is(':visible')){
				obj.className = obj.className.replace('close','open');
				if(TREE.option.animate)
				{
					childUl.animate({height:"toggle"},TREE.option.speed, function(){
						if(TREE.option.autoclose)TREE.closeNearby(obj);
						if(childUl.is('.ajax')) {
							TREE.setAjaxNodes(childUl, obj.id, callback);
						} else if(typeof(callback) == 'function') {
							callback(node, true);
						}
					});
				}else{
					childUl.show();
					if(TREE.option.autoclose)TREE.closeNearby(obj);
					if(childUl.is('.ajax')) {
						TREE.setAjaxNodes(childUl, obj.id, callback);
					} else if(typeof(callback) == 'function') {
						callback(node, true);
					}
				}
			} else {
				if(typeof(callback) == 'function')
				{
					callback(node, true);
				}
			}
		};
		
		//刷新节点 add by zsy
		TREE.refreshNode = function(parentNode, ajaxUrl, callback)
		{
			if (!parentNode) {
				parentNode = TREE.getSelected();
			}
			$('>ul', parentNode).remove();
		  	parentNode.append("<ul class='ajax'><li>{url:" + ajaxUrl + "}</li></ul>");
		  	TREE.setTreeNodes(parentNode, false);
		  	$(ajaxCache).each(function(i){//移除缓存
		  	//	if (ajaxCache[i] == parentNode.attr("id")) {
		  			ajaxCache[i] = null;

		  	//	}
		  	});
		  	TREE.openNode(parentNode, callback);
		};
		
		//选中节点（只能是已加载的）节点ID路径字符串，如：id22/id33/id44 add by zsy
		TREE.selectNode = function(nodePath, firstId) 
		{
			if (firstId) {
				TREE.openNode($("#" + firstId, this),function(){
					TREE.selectNode(nodePath);
				});
			} else {
				var index = nodePath.indexOf("/");
				if (index == -1) {//只有一级节点
				 	var node = $('#'+nodePath,this);
					$('span:first',node).click();
					return;
				}
				var nodeId = nodePath.substring(0, index);
				var elseIds = nodePath.substring(index + 1, nodePath.length);
				TREE.selectNode(elseIds, nodeId);
			}
		}
		
		//当前节点同级内，向上移一位 add by zsy
		TREE.moveUp = function(callback)
		{
			var node = TREE.getSelected();
			var target = node.prevAll('li:has(span):first');
			if(target.size() == 0){//是最前面一个
				return;
			}
			var line = node.prev('li');
    		target.before(node);
			target.before(line);
			
			if(typeof(callback) == 'function')
			{
				callback(node, target);
			}
		};
		
		//当前节点同级内，移到最上面 add by zsy
		TREE.moveTop = function(callback)
		{
			var node = TREE.getSelected();
			var target = node.prevAll('li:has(span):last');
			if(target.size() == 0){//是最前面一个
				return;
			}
			var line = node.prev('li');
       		target.before(node);
			target.before(line);
			
			if(typeof(callback) == 'function')
			{
				callback(node, target);
			}
		};
		
		//当前节点同级内，向下移一位 add by zsy
		TREE.moveDown = function(callback)
		{
			var node = TREE.getSelected();
			var target = node.nextAll('li:has(span):first');
			if(target.size() == 0){//是最后一个
				return;
			}
			var line = node.prev('li');
       		target.after(node);
			target.after(line);	
			
			if(typeof(callback) == 'function')
			{
				callback(node, target);
			}
		};
		
		//当前节点同级内，移到最底下 add by zsy
		TREE.moveBottom = function(callback)
		{
			var node = TREE.getSelected();
			var target = node.nextAll('li:has(span):last');
			if(target.size() == 0){//是最后一个
				return;
			}
			var line = node.prev('li');
       		target.after(node);
			target.after(line);	
			
			if(typeof(callback) == 'function')
			{
				callback(node, target);
			}
		};
		
	});
}