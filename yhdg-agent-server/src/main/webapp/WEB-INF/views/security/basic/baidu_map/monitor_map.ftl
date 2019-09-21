<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <style type="text/css">
        body, html, #allmap {
            width: 100%;
            height: 100%;
            overflow: hidden;
            margin: 0;
            font-family: "微软雅黑";
        }
    </style>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=${controller.appConfig.mapKey}"></script>
    <script type="text/javascript" src="${contextPath}/static/tool/jquery/jquery-1.8.0.min.js"></script>
</head>
<body>
<div id="allmap">
    <div class="map_info_wrap" style="display: none;">

    </div>
</div>
</body>
<script type="text/javascript">

    function view(id) {
        parent.view(id);
    }

    var map = new BMap.Map("allmap");
    map.centerAndZoom(new BMap.Point(109.369334, 38.16197), 5);
    map.enableScrollWheelZoom(true);
    var convertor = new BMap.Convertor();

    var icon1 = new BMap.Icon("${app.imagePath}/battery_map_1.png", new BMap.Size(41, 45));
    var icon2 = new BMap.Icon("${app.imagePath}/battery_map_2.png", new BMap.Size(41, 45));
    var icon3 = new BMap.Icon("${app.imagePath}/battery_map_3.png", new BMap.Size(41, 45));

    function detail(id) {
        var overlays = map.getOverlays();
        var flag = 0;
        $.each(overlays, function (index, data) {
            if (data.id == id) {
                map.centerAndZoom(data.point, 17);
                data.openInfoWindow(data.infoWindow);
                flag = 1;
                return;
            }
        })
        if (flag == 0) {
            alert("该电池未上报经纬度");
        }
    }

    function addMarker(list) {
        map.clearOverlays();
            $.each(list, function (index, data) {
                    var marker = new BMap.Marker(new BMap.Point(data.lng, data.lat), {icon: new BMap.Icon("${app.imagePath}/battery_map_" + data.imageSuffix + ".png", new BMap.Size(41, 45))});
                    var html = "<div style='cursor:pointer' onclick='view(\"" + data.id + "\")'><h4>电池编号：<u>" + data.id + "</u></h4>";
                    if (data.address != null) {
                        html += "<p>电池位置：" + data.address + "</p>";
                    }
                    html += " <p>电池状态：" + data.statusName + "</p> <p>当前电量：" + data.volume + "%</p>";
                    if (data.imageSuffix == 1) {
                        if (data.keeperName != null && data.keeperMobile != null) {
                            html += "<p>维护人姓名：" + data.keeperName + "</p> <p>联系方式：" + data.keeperMobile + "</p> </div>";
                        }
                    } else if (data.imageSuffix == 2) {
                        html += " <p>换电柜：" + data.cabinetId + "/" + data.cabinetName + "</p> <p>箱号：" + data.boxNum + "</p> </div>";
                    } else {
                        html += "<p>承租人姓名：" + data.customerFullname + "</p> <p>联系方式：" + data.customerMobile + "</p> </div>";
                    }
                    var infoWindow = new BMap.InfoWindow(html);  // 创建信息窗口对象
                    marker['id'] = data.id;
                    marker['infoWindow'] = infoWindow;
                    map.addOverlay(marker);

                    marker.addEventListener("click", function () {
                        this.openInfoWindow(infoWindow);
                    });
            })
            map.centerAndZoom(new BMap.Point(109.369334, 38.16197), 5);
    }
</script>
</html>