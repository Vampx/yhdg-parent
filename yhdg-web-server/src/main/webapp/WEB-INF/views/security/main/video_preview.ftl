<script type="text/javascript" src="${app.toolPath}/player/swfobject.js"></script>
<div class="video" id="CuPlayer"><b><img src="${app.toolPath}/player/loading.gif" /> 网页视频播放器加载中，请稍后...</b></div>
<script type="text/javascript">
    var so = new SWFObject("${app.toolPath}/player/player.swf","ply","100%","700","9","#000000");
    so.addParam("allowfullscreen","true");
    so.addParam("allowscriptaccess","always");
    so.addParam("wmode","opaque");
    so.addParam("quality","high");
    so.addParam("salign","lt");
    //播放器配置文件-----------------------------
    so.addVariable("JcScpFile","${app.toolPath}/player/CuSunV2set.xml");
    //视频文件及略缩图--------------------------
    //so.addVariable("JcScpServer","rtmp://www.yoursite.com/vod");
    so.addVariable("JcScpVideoPath","${contextPath}${path}");
    //-前置Flash广告-----------------------------
    //so.addVariable("ShowJcScpAFront","yes");
    //so.addVariable("JcScpCountDownsPosition","top-left");
    //so.addVariable("JcScpCountDowns","5");
    //so.addVariable("JcScpAFrontW","600");
    //so.addVariable("JcScpAFrontH","410");
    //so.addVariable("JcScpAFrontPath","/player/images/a650x418.swf");
    //so.addVariable("JcScpAFrontLink","http://demo.cuplayer.com/CuSunPlayer/demo1.html");
    //-视频广告参数-----------------------------
    //so.addVariable("ShowJcScpAVideo","no");
    //so.addVariable("JcScpAVideoPath","http://demo.cuplayer.com/file/up.flv");
    //so.addVariable("JcScpAVideoLink","http://demo.cuplayer.com/CuSunPlayer/demo2.html");
    //-暂停广告参数-----------------------------
    //so.addVariable("ShowJcScpAPause","yes");
    //so.addVariable("JcScpAPauseW","300");
    //so.addVariable("JcScpAPauseH","250");
    //so.addVariable("JcScpAPausePath","/player/images/a300x250.swf");
    //so.addVariable("JcScpAPauseLink","http://demo.cuplayer.com/CuSunPlayer/demo3.html");
    //-角标广告参数-----------------------------
    //so.addVariable("ShowJcScpACorner","yes");
    //so.addVariable("JcScpACornerW","85");
    //so.addVariable("JcScpACornerH","50");
    //so.addVariable("JcScpACornerPath","/player/images/a370x250.swf");
    //so.addVariable("JcScpACornerPosition","bottom-right");
    //so.addVariable("JcScpACornerLink","http://demo.cuplayer.com/CuSunPlayer/demo4.html");
    //-后置广告参数-----------------------------
    //so.addVariable("ShowJcScpAEnd","yes");
    //so.addVariable("JcScpAEndW","400");
    //so.addVariable("JcScpAEndH","300");
    //so.addVariable("JcScpAEndPath","/player/images/a400x300.swf");
    //so.addVariable("JcScpAEndLink","http://demo.cuplayer.com/CuSunPlayer/demo5.html");
    //-----------------------------------------
    //so.addVariable("JcScpSharetitle","极酷阳光播放器(CuSunPlayerV2.5)版使用官方演示实例");
    so.write("CuPlayer");
</script>

<!--极酷播放器/代码结束-->