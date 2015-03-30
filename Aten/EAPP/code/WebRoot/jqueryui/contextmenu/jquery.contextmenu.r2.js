/*
 * ContextMenu - jQuery plugin for right-click context menus
 *
 * Author: Chris Domigan
 * Contributors: Dan G. Switzer, II
 * Parts of this plugin are inspired by Joern Zaefferer's Tooltip plugin
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 * Version: r2
 * Date: 16 July 2007
 *
 * For documentation visit http://www.trendskitchens.co.nz/jquery/contextmenu/
 *
 */

//====================================================================
//功能描述：实现右键菜单的jQuery插件
//创建标识：
//
//修改标识：张培坤 2008/05/05
//修改描述：在不修改菜单原有功能的情况下，兼容以前的操作。创建新的样式变量，以便可以实现换肤操作。
//　　　　　当样式变量不为空的情况下，只使用样式变量，而不使用提供的样式对象。
//
//注意事项：该菜单在创建时，只拷贝提供的HTML代码，并不是直接使用，所以源菜单容器必须把CSS样式中的display设置为none
//
//修改标识：雷大干 2008/06/13
//修改描述：1)由于之前只对菜单容器设置display:none，导致想隐藏某个菜单项时变为不可能。
//			为了实现这个效果，本次修改了function display{}中的$.each(cur.bindings, function(id, func) {}处
//			增加对每个菜单项的.show()操作
//		  2)为了实现鼠标点击页面后隐藏右键菜单的效果，把原来绑定到click的事件改为mouseup
//
//注意事项：要求源菜单容器中必须把各个菜单项的display也设置为none
//        实现时，在传入的bindings中只提供需要显示的菜单项
//====================================================================

(function($) {

  var menu, shadow, trigger, content, hash, currentTarget;
  var defaults = {
    menuStyle: {
      listStyle: 'none',
      padding: '1px',
      margin: '0px',
      backgroundColor: '#fff',
      border: '1px solid #999',
      width: '100px'
    },
    itemStyle: {
      margin: '0px',
      color: '#000',
      display: 'block',
      cursor: 'default',
      padding: '3px',
      border: '1px solid #fff',
      backgroundColor: 'transparent'
    },
    itemHoverStyle: {
      border: '1px solid #0a246a',
      backgroundColor: '#b6bdd2'
    },
    /*-------为适应换肤操作，而新增加三个外部全局变量-----------*/
    menuStyleClass : "",
    itemStyleClass : "",
    itemHoverStyleClass : "",
    /*----------------------END-------------------------*/
    eventPosX: 'pageX',
    eventPosY: 'pageY',
    shadow : true,
    onContextMenu: null,
    onShowMenu: null
 	}
    ;

  $.fn.contextMenu = function(id, options) {
    if (!menu) {                                      // Create singleton menu
      menu = $('<div id="jqContextMenu"></div>')
               .hide()
               .css({position:'absolute', zIndex:'500'})
               .appendTo('body')
               .bind('click', function(e) {
                 e.stopPropagation();
               });
    }
    if (!shadow) {
      shadow = $('<div></div>')
                 .css({backgroundColor:'#000',position:'absolute',opacity:0.2,zIndex:499})
                 .appendTo('body')
                 .hide();
    }
    hash = hash || [];
    hash.push({
      id : id,
      menuStyle: $.extend({}, defaults.menuStyle, options.menuStyle || {}),
      itemStyle: $.extend({}, defaults.itemStyle, options.itemStyle || {}),
      itemHoverStyle: $.extend({}, defaults.itemHoverStyle, options.itemHoverStyle || {}),
      /*-------为适应换肤操作，而新增加样式变量-----------*/
      menuStyleClass : options.menuStyleClass || defaults.menuStyleClass || "",
      itemStyleClass : options.itemStyleClass || defaults.itemStyleClass || "",
      itemHoverStyleClass : options.itemHoverStyleClass || defaults.itemHoverStyleClass || "",
      /*---------------------END--------------------*/
      bindings: options.bindings || {},
      shadow: options.shadow || options.shadow === false ? options.shadow : defaults.shadow,
      onContextMenu: options.onContextMenu || defaults.onContextMenu,
      onShowMenu: options.onShowMenu || defaults.onShowMenu,
      eventPosX: options.eventPosX || defaults.eventPosX,
      eventPosY: options.eventPosY || defaults.eventPosY
    });

    var index = hash.length - 1;
    $(this).bind('contextmenu', function(e) {
      // Check if onContextMenu() defined
      var bShowContext = (!!hash[index].onContextMenu) ? hash[index].onContextMenu(e) : true;
      if (bShowContext) display(index, this, e, options);
      return false;
    });
    return this;
  };

  function display(index, trigger, e, options) {
    var cur = hash[index];
    var lis = null;
    content = $('#'+cur.id).find('ul:first').clone(true);

    if($.trim(cur.menuStyleClass).length > 0){
        content.addClass(cur.menuStyleClass);
        lis = content.find('li').addClass(cur.itemStyleClass);
    }
    else{
        content.css(cur.menuStyle);
        lis = content.find('li').css(cur.itemStyle);
    };
    
    lis.hover(
      function() {
          if($.trim(cur.menuStyleClass).length > 0){
              $(this).addClass(cur.itemHoverStyleClass);  
          }
          else{
              $(this).css(cur.itemHoverStyle);
          };
      },
      function(){
        if($.trim(cur.menuStyleClass).length > 0){
              $(this).removeClass(cur.itemHoverStyleClass);  
          }
          else{
              $(this).css(cur.itemStyle);
          };
      }
    ).find('img').css({verticalAlign:'middle',paddingRight:'2px'});

    // Send the content to the menu
    menu.html(content);

    // if there's an onShowMenu, run it now -- must run after content has been added
		// if you try to alter the content variable before the menu.html(), IE6 has issues
		// updating the content
    if (!!cur.onShowMenu) menu = cur.onShowMenu(e, menu);

	//top.hideContextMenu = hide;	//不支持跨域
	
    $.each(cur.bindings, function(id, func) {
    	$('#'+id, menu).show();		//modified by richfans 2008-6-13
    	$('#'+id, menu).bind(		//在FireFox下，click事件会先被document捕捉，从而使这里的click失效。为了比document更早捕捉到这个事件，只好用mousedown了。
    		'mousedown', 
    		function(e) {
    			hide();
    			if ($.isFunction(func)){
	    			func(trigger, currentTarget);
	    		} else {
	    			eval(func + "(trigger, currentTarget)");
	    		}
	    	}
      	);
    });
    
    var oPos = {
        left : e[cur.eventPosX],
        top  : e[cur.eventPosY]
    };
    
    var body = $(document.body);

    if(e[cur.eventPosX] + menu.width() + 2  > body.width()){
        oPos.left = e[cur.eventPosX] - (e[cur.eventPosX] + menu.width() - body.width()) - 2;
    };
    
    if(e[cur.eventPosY] + menu.height() + 2 > body.height()){
        oPos.top = e[cur.eventPosY] - (e[cur.eventPosY] + menu.height() - body.height()) - 2;
    };
    
    menu.css({'left':oPos.left,'top':oPos.top}).show();
    if (cur.shadow) shadow.css({width:menu.width(),height:menu.height(),left:oPos.left+2,top:oPos.top+2}).show();
    //$(document).one('click', hide);
    $(document).one('mouseup',hide);	//这里不能是mousedown，否则会提前把右键菜单关闭，导致菜单的点击事件无效。modified by richfans  2008-6-13
  }

  function hide() {
    menu.hide();
    shadow.hide();
  }

  // Apply defaults
  $.contextMenu = {
    defaults : function(userDefaults) {
      $.each(userDefaults, function(i, val) {
        if (typeof val == 'object' && defaults[i]) {
          $.extend(defaults[i], val);
        }
        else defaults[i] = val;
      });
    }
  };

})(jQuery);

$(function() {
  $('div.contextMenu').hide();
});