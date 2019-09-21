$(function(){
	

	//节目编辑器正文左侧工具栏
	function toolbar(){
		$(".editor_main .toolbar_left .tool_button:has(div)").click(function(event){
			event.stopPropagation();
			
			var Li = $(this).children(".tool_second").children("ul").children("li");
			var Width = Li.width() * Li.length;
			$(this).children(".button_click").addClass("selected").siblings(".tool_second").animate({ width:Width},200).show();
			$(this).siblings().children(".button_click").removeClass("selected").siblings(".tool_second").animate({ width:0},50).hide();
		})
		
		$(".editor_main .toolbar_left .tool_button .tool_second ").click(function(event){ event.stopPropagation() })
		
	}
	toolbar();

	
	$(".editor_header .btn_box .alignment_wrap").hover(function(){
		$(".editor_header .btn_box .alignment_con").show();
	},function(){
		$(".editor_header .btn_box .alignment_con").hide();
	})

	$(".editor_header .btn_box .save_wrap").hover(function(){
		$(this).children('a').addClass("selected");
		$(".editor_header .btn_box .save_con").show();
	},function(){
		$(this).children('a').removeClass("selected");
		$(".editor_header .btn_box .save_con").hide();
	})
	
			

	$(document).click(function(){ 
		$(".toolbar .button .float_button").hide();

		$(".editor_main .toolbar_left .tool_button .button_click").removeClass("selected");
		$(".editor_main .toolbar_left .tool_button .tool_second").animate({ width:0},50).hide();
		
	})
	


});
