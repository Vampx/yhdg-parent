<#assign toolPath='${contextPath}/static/tool' >
<#assign imagePath='${contextPath}/static/images' >
<script type="text/javascript" src="${toolPath}/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=${controller.appConfig.mapKey}"></script>
<div class="popup_box_content">
    <div><input id="is_stop" type="hidden"></div>
    <div style="width:100%;height:100%;overflow: hidden;margin: 0;font-family: '微软雅黑';" id="viewContent">
    </div>
    <script type="text/javascript">
        var map = new BMap.Map("viewContent");
        map.centerAndZoom(new BMap.Point(109.369334, 38.16197), 5);  // 初始化地图,设置中心点坐标和地图级别
        map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
        map.enableDragging();//启用地图拖拽事件，默认启用(可不写)
        map.enableScrollWheelZoom();//启用地图滚轮放大缩小
        map.enableDoubleClickZoom();//启用鼠标双击放大，默认启用(可不写)
        map.enableKeyboard();//启用键盘上下左右键移动地图
        //向地图中添加缩放控件
        var ctrl_nav = new BMap.NavigationControl({
            anchor: BMAP_ANCHOR_TOP_LEFT,
            type: BMAP_NAVIGATION_CONTROL_LARGE
        });
        map.addControl(ctrl_nav);
        //向地图中添加比例尺控件
        var ctrl_sca = new BMap.ScaleControl({anchor: BMAP_ANCHOR_BOTTOM_LEFT});

        //轨迹模式
        var polyline ;
        var options = {
            onSearchComplete: function(results){
                if (driving.getStatus() == BMAP_STATUS_SUCCESS){
                    // 获取第一条方案
                    var plan = results.getPlan(0);
                    // 获取方案的驾车线路
                    var route = plan.getRoute(0);
                    //返回路线的地理坐标点数组。（自 1.2 新增）
                    var points = route.getPath();
                    polyline = new BMap.Polyline(points);
                    map.addOverlay(polyline);          //增加折线
                }
            }
        };
        var driving = new BMap.WalkingRoute(map, options);

        //组装坐标点
        var points = new Array();
        var labels = new Array();
        function pointListInfo(list) {
            for (var i = 0; i < list.length; i++) {
                points.push(new BMap.Point(list[i].lng, list[i].lat));
                showtime = new Date(list[i].reportTime);
                labels.push("上报时间 " + showtime.getHours()+":"+showtime.getMinutes() + " 间隔距离 "+list[i].distance + "m");
            }
        }

        //停止轨迹地图
        function stopWalkingMap(obj) {
            $('#is_stop').val(1);
        }

        //还原地图
        function restoreMap() {
            $('#is_stop').val(1);
            map.clearOverlays();
            map.centerAndZoom(new BMap.Point(109.369334, 38.16197), 5);  // 初始化地图,设置中心点坐标和地图级别
        }

        //生成轨迹地图
        function createMap(obj) {
            points.length = 0;
            labels.length = 0;
            map.clearOverlays();
            if (obj.length != 0) {
                pointListInfo(obj);//放入坐标
                showLine();
            } else {
                $.messager.alert('提示信息', '该时段电池未上报信息', 'info');
            }
        }

        //生成行驶轨迹
        function createWalkingMap(obj) {
            $('#is_stop').val(0);
            points.length = 0;
            labels.length = 0;
            map.clearOverlays();
            if (obj.length != 0) {
                pointListInfo(obj);//放入坐标
                showWalking();
            } else {
                $.messager.alert('提示信息', '该时段电池未上报信息', 'info');
            }
        }

        function showLine() {
            map.centerAndZoom(points[points.length - 1], 18);
            for (var i = 0; i < points.length; i ++) {
                //绘制线
                if(i>0){
                    driving.search(points[i-1], points[i]);
                    map.addOverlay(polyline);
                }

                if (i == 0) {
                    var marker = new BMap.Marker(points[i]);  // 创建标注
                    map.addOverlay(marker);
                    marker.setLabel(new BMap.Label("起点",{offset:new BMap.Size(20,-10)}));
                    map.panTo(points[i]);
                } else if (i == points.length - 1) {
                    var marker = new BMap.Marker(points[i]);  // 创建标注
                    map.addOverlay(marker);
                    marker.setLabel(new BMap.Label("终点",{offset:new BMap.Size(20,-10)}));
                    map.panTo(points[i]);
                }
            }
        }


        var i=0;
        var marker;
        function showWalking() {
            map.centerAndZoom(points[points.length - 1], 18);
            i=0;
            playLine();
        }

        function playLine(){
            if(i==0){//第一个点 直接添加
                //开始坐标
                marker1 = new BMap.Marker(points[i]);  // 创建标注
                map.addOverlay(marker1);
                marker1.setLabel(new BMap.Label("开始",{offset:new BMap.Size(20,-10)}));
                map.panTo(points[i]);

                marker = new BMap.Marker(points[i]);  // 创建标注
                map.addOverlay(marker);
                marker.setLabel(new BMap.Label(labels[i],{offset:new BMap.Size(20,-10)}));
                map.panTo(points[i]);
                i++;
                setTimeout(function(){
                    playLine(i);
                },700)
            }else{//获取PolyLine并添加 添加点
                var isStop = $('#is_stop').val();
                if(isStop == 1) {
                    //结束坐标
                    marker2 = new BMap.Marker(points[points.length - 1]);  // 创建标注
                    map.addOverlay(marker2);
                    marker2.setLabel(new BMap.Label("结束",{offset:new BMap.Size(20,-10)}));
                    map.panTo(points[points.length - 1]);
                    return;
                }

                if(i<=points.length){
                    if(i == points.length -1){
                        //结束坐标
                        marker2 = new BMap.Marker(points[i]);  // 创建标注
                        map.addOverlay(marker2);
                        marker2.setLabel(new BMap.Label("结束",{offset:new BMap.Size(20,-10)}));
                        map.panTo(points[i]);
                    }else{
                        //经过坐标点
                        var myIcon = new BMap.Icon("http://api.map.baidu.com/img/markers.png", new BMap.Size(23, 25), {
                            offset: new BMap.Size(20, -10), // 指定定位位置
                            imageOffset: new BMap.Size(0, 0 - 10 * 25) // 设置图片偏移
                        });
                        marker2 = new BMap.Marker(points[i],{icon:myIcon});  // 创建标注
                        map.addOverlay(marker2);
                        marker2.setLabel(new BMap.Label("位置"+(i+1),{offset:new BMap.Size(20,-10)}));
                        map.panTo(points[i]);
                    }

                    driving.search(points[i-1], points[i]);
                    map.addOverlay(polyline);
                    if(marker){
                        map.removeOverlay(marker);
                    }
                    marker = new BMap.Marker(points[i]);  // 创建标注
                    map.addOverlay(marker);
                  //  marker.setAnimation(BMAP_ANIMATION_BOUNCE);
                    marker.setLabel(new BMap.Label(labels[i],{offset:new BMap.Size(20,-10)}));
                    map.panTo(points[i]);
                    i++;
                    setTimeout(function(){
                        playLine(i);
                    },700)
                }
            }
        }

        var id = "markerAnimation";
        function markerAnimation(lng, lat) {
            var overlays = map.getOverlays();
            var flag = 0;
            $.each(overlays, function (index, data) {
                if (data.id == id) {
                    map.removeOverlay(data);
                    return;
                }
            })
            var marker = new BMap.Marker(new BMap.Point(lng, lat));
            marker['id'] = id;
            map.addOverlay(marker);
            marker.setAnimation(BMAP_ANIMATION_BOUNCE);
            map.centerAndZoom(new BMap.Point(lng, lat), 18)
        }


    </script>
</div>