<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title></title>
    <link href="${contextPath}/static/css/style.css" type="text/css" rel="stylesheet"/>
    <script>
        function d() {
            var u = navigator.userAgent;
            if (u.indexOf('iPhone') > -1) {//苹果手机
                window.location.href = "${customerIosUrl}";
            } else {
                window.location.href = "${customerAndroidUrl}";
            }
        }

    </script>
</head>
<body ontouchstart="" onmouseover="">
<div class="download_wrap">
    <div class="logo"><img src="${contextPath}/static/img/logo_${(controller.appConfig.brandImageSuffix)!''}.png"></div>
    <div class="focus"><img src="${contextPath}/static/img/focus.png"></div>
    <div class="btn">
        <a onclick="d()">立即下载</a>

        <p class="tips">最新版本：${version}</p>
    </div>
</div>
</body>
</html>
<script>
    document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.5 + 'px';
    var deviceWidth = document.documentElement.clientWidth;
    if (deviceWidth > 750) deviceWidth = 750;
    document.documentElement.style.fontSize = deviceWidth / 7.5 + 'px';
</script>
