<@app.html>
    <@app.head>
    </@app.head>
    <@app.body>
    <div class="page msg_page">
        <table width="100%" height="100%"><tr><td align="center" valign="middle">${message}</td></tr></table>

    </div>
    </@app.body>
</@app.html>
<script  type="text/javascript">
    (function (_D){
        var _self = {};
        _self.Html = _D.getElementsByTagName("html")[0];
        _self.widthProportion = function(){var p = (_D.body&&_D.body.clientWidth||_self.Html.offsetWidth)*0.16/320;return p>1?1:p;};
        _self.changePage = function(){_self.Html.setAttribute("style","font-size:"+_self.widthProportion()*100+"px !important");}
        _self.changePage();
        setInterval(_self.changePage,100);
    })(document);
</script>

