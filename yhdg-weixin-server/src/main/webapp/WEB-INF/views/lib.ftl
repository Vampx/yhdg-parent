<#macro html>
<!doctype html>
<html>
    <#nested>
</html>
</#macro>

<#assign v="20161220-2"/>

<#macro head>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <script type="text/javascript" src="${contextPath}/static/js/zepto.min.js?v=${v}" ></script>
    <script type="text/javascript" src="${contextPath}/static/js/zepto.data.js?v=${v}" ></script>
    <script type="text/javascript" src="${contextPath}/static/js/zepto.touch.js?v=${v}" ></script>
    <script type="text/javascript" src="${contextPath}/static/js/common.js?v=${v}" ></script>


    <script type="text/javascript">
        $.ajaxSettings.traditional = true;
    </script>
    <link href="${contextPath}/static/css/global.css?v=${v}" type="text/css" rel="stylesheet">
    <link href="${contextPath}/static/css/main.css?v=${v}" type="text/css" rel="stylesheet">
    <link href="${contextPath}/static/css/weui.min.css?v=${v}" type="text/css" rel="stylesheet">
    <title>快递柜</title>
    <#nested>
</head>
</#macro>

<#macro body>
<body>
    <#nested>
</body>
</#macro>

<#macro json_ajax_callbak json="json" show_loading=true>
    <#if show_loading>closeLoading();</#if>
    if(${json}.timeout) { document.location.href = '${controller.appConfig.wxMyOrderUrl}'; return false; }
</#macro>

<#function format_time d>
    <#return d?string('HH:mm:ss')>
</#function>

<#function format_date d>
    <#return d?string('yyyy-MM-dd')>
</#function>

<#function format_date_minute d>
    <#return d?string('yyyy-MM-dd HH:mm')>
</#function>

<#function format_date_time d>
    <#return d?string('yyyy-MM-dd HH:mm:ss')>
</#function>