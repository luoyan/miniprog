/*! page - v1.0.0 - 2013-11-28 */
define("js/page/jianbao",["./base","jquery","jqueryplugins","Boxy"],function(a,b,c){function d(){$("#no_show").click(function(){$.cookie("jianbao_desc","true",{expires:365,path:"/"}),$("#search_desc").fadeOut()}),$("#url").focusin(function(){$("#default_value").hide()}),$("#url").focusout(function(){""!=$("#url").val()?$("#default_value").hide():$("#default_value").show()}),$("#jianbao_btn").click(function(){var a=$.trim($("#url").val());m.test(a)?f=new k($("#product_right_ctn"),{modal:!0,title:" ",closeText:"×"}):""!=a&&(g=new k($("#product_wrong_ctn"),{modal:!0,title:" ",closeText:"×"})),j.ajaxPost("/set_click_log",{type:"jianbao_click"},function(){})}),$(".product-ctn .label").click(function(){$(this).next().trigger("focus")}),$(".product-ctn .input").focusin(function(){$(this).prev().hide()}),$(".product-ctn .input").focusout(function(){""!=$(this).val()?$(this).prev().hide():$(this).prev().show()}),$("#product_wrong_ctn button").click(function(){var a=$("#product_wrong_ctn .input").val();m.test(a)?(g.hide(),f=new k($("#product_right_ctn"),{modal:!0,title:" ",closeText:"×"})):alert("请输入正确的宝贝链接")}),$("#product_right_ctn button").click(function(){var a=$.trim($("#product_right_ctn .input").val());if(n.test(a)){var b=$("#url").val();m.test(b)||(b=$("#product_wrong_ctn .input").val());var a=$("#product_right_ctn .input").val(),c={url:b,email:a};j.ajaxPost("/ajax_add_jianbao",c,function(){f.hide(),h=new k($("#right_email_ctn"),{modal:!0,title:" ",closeText:"×"}),$("#left_time").text(5),e()})}else""!=a&&(f.hide(),i=new k($("#wrong_email_ctn"),{modal:!0,title:" ",closeText:"×"}))}),$("#wrong_email_ctn a").click(function(){i.hide(),f=new k($("#product_right_ctn"),{modal:!0,title:" ",closeText:"×"})}),$("#jianbao_report").click(function(){return new k($("#jianbao_report_ctn"),{modal:!0,title:" ",closeText:"×"}),!1})}function e(){$("#left_time").text()-0==0?h.hide():($("#left_time").text($("#left_time").text()-1),setTimeout(e,1e3))}var f,g,h,i,j=a("./base"),k=a("Boxy"),l={},m=new RegExp(/^http:\/\/[A-Za-z0-9\.-]{3,}\.[A-Za-z]{2,3}/),n=new RegExp(/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/);l.init=function(){d(),$.cookie("jianbao_desc")&&$("#search_desc").hide(),""==$("#url").val()&&$("#default_value").show()},c.exports=l});