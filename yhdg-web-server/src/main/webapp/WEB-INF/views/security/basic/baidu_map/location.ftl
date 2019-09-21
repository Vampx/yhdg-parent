<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <style type="text/css">
        html{height:100%}
        body{height:100%;margin:0px;padding:0px}
        #container{height:100%}
    </style>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=1.5&ak=${controller.appConfig.mapKey}"></script>
</head>
<body>
<div id="container"></div>
<script type="text/javascript">
    var point = <#if lng?? && lat??>new BMap.Point(${lng}, ${lat});<#else>false;</#if>
    var address = "${(address)!''}";

    var map = new BMap.Map("container");
    var navigationControl = new BMap.NavigationControl({
        // 靠左上角位置
        anchor: BMAP_ANCHOR_TOP_LEFT,
        // LARGE类型
        type: BMAP_NAVIGATION_CONTROL_LARGE,
        // 启用显示定位
        enableGeolocation: true
    });
    map.addControl(navigationControl);
    map.enableScrollWheelZoom(true); //开启鼠标滚轮缩放

    if(point) {
        map.centerAndZoom(point, 15);
        map.addOverlay(new BMap.Marker(point));
    } else if(address) {
        map.setCenter(address);
    } else {
        map.centerAndZoom(new BMap.Point(116.404, 39.915), 15);
    }

    <#if readonly?? && readonly == 1>
    <#else>
    map.addEventListener("click", function(e) {
        var point = e.point;
        map.clearOverlays();
        map.addOverlay(new BMap.Marker(point));

        var geoc = new BMap.Geocoder();
        geoc.getLocation(point, function(rs){
            var addComp = rs.addressComponents;
            alert("你选择了\n" + addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber);
        });
        parent.setLngLat(point);
    });
    </#if>

    function locationAddress(area) {
        var address = area.province + area.city + area.district + area.street;
        var city = area.province.indexOf('市') == area.province.length - 1 ? area.province : area.city;
        // 创建地址解析器实例
        var myGeo = new BMap.Geocoder();
        // 将地址解析结果显示在地图上,并调整地图视野
        myGeo.getPoint(address, function(point){
            if (point) {
                map.centerAndZoom(point, 15);
                map.clearOverlays();
                map.addOverlay(new BMap.Marker(point));
                parent.setLngLat(point);
            } else {
                alert("选择的地址没有解析到结果!");
            }
        }, city);
    }
</script>
</body>
</html>