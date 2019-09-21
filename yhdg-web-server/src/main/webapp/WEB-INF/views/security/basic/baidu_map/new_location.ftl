<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
        body, html{width: 100%;height: 100%;margin:0;font-family:"微软雅黑";font-size:14px;}
        #l-map{height:388px;width:100%;}
        #r-result{width:100%;}
    </style>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=1.5&ak=${controller.appConfig.mapKey}"></script>
    <title>关键字输入提示词条</title>
</head>
<body>
<div id="r-result" style="margin-bottom: 10px;"><input type="text" class="text" id="suggestId" size="30" placeholder="请输入地址" style="position: relative;width: 575px; height: 32px; z-index: 99999;border: 1px solid #0089f0;box-shadow:0px 0px 5px #0089f0;"/></div>
<div id="l-map"></div>
<div id="searchResultPanel" style="border:1px solid #C0C0C0;width:150px;height:auto; display:none;"></div>
<div id="searchLocationName" style="border:1px solid #C0C0C0;width:150px;height:auto; display:none;"></div>
<#--<div id="current_location">当前位置：<span id="current_position">${(address)!''}</span><br>当前经度/纬度：<span id="current_lng_lat">${(lng)!''}/${(lat)!''}</span> </div>-->
</body>
</html>
<script type="text/javascript">

    // 百度地图API功能
    function G(id) {
        return document.getElementById(id);
    }

    var map = new BMap.Map("l-map");
    var point = <#if lng?? && lat??>new BMap.Point(${lng}, ${lat});<#else>false;</#if>
    //    if(point!=null && point!=false) {
    map.centerAndZoom(point, 18);
    //    }else{
    //        point=new BMap.Point(116.404, 39.915);
    //        map.centerAndZoom(point, 18);
    //    }
    map.addOverlay(new BMap.Marker(point));

    var geoc = new BMap.Geocoder();

    map.addEventListener("click", function(e){
        map.clearOverlays();
        //得到经纬度
        var pp = e.point;

        var clickPoint = new BMap.Point(e.point.lng, e.point.lat);
        map.addOverlay(new BMap.Marker(clickPoint));
        geoc.getLocation(pp, function(rs){
            //得到地址名称
            var addComp = rs.addressComponents;
            var currentPosition = addComp.province + "" + addComp.city + "" + addComp.district + "" + addComp.street + "" + addComp.streetNumber;
            var provinceName = addComp.province;
            var cityName = addComp.city;
            var districtName = addComp.district;
            var streetName = addComp.street;
            var streetNumber = addComp.streetNumber;
//            G("current_position").innerHTML=currentPosition;
//            G("current_lng_lat").innerHTML=pp.lng+"/"+pp.lat;
            parent.setLocation(pp,currentPosition,provinceName,cityName,districtName,streetName,streetNumber);
        });


    });

    //    map.centerAndZoom("杭州",12);                   // 初始化地图,设置城市和地图级别。
    map.addControl(new BMap.NavigationControl());
    map.addControl(new BMap.ScaleControl());
    map.addControl(new BMap.OverviewMapControl());
    map.addControl(new BMap.MapTypeControl());
    map.enableScrollWheelZoom(true); //开启鼠标滚轮缩放

    var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
            {"input" : "suggestId"
                ,"location" : map
            });

    ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
        var str = "";
        var _value = e.fromitem.value;
        var value = "";
        if (e.fromitem.index > -1) {
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

        value = "";
        if (e.toitem.index > -1) {
            _value = e.toitem.value;
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
        G("searchResultPanel").innerHTML = str;
    });

    var myValue;
    ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
        var _value = e.item.value;
        myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
        G("searchLocationName").innerHTML = myValue;
        setPlace();
    });

    function setPlace(){
        map.clearOverlays();    //清除地图上所有覆盖物
        function myFun(){
            var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果

//            geoc.getLocation(pp, function(rs){
//                //得到地址名称
//                var addComp = rs.addressComponents;
//                var currentPosition = addComp.province + "" + addComp.city + "" + addComp.district + "" + addComp.street + "" + addComp.streetNumber;
////                G("current_position").innerHTML=currentPosition;
////                G("current_lng_lat").innerHTML=pp.lng+"/"+pp.lat;
//                parent.setLocation(pp,currentPosition);
//            });
            var currentPosition = G("searchLocationName").innerHTML;

            geoc.getLocation(pp, function(rs){
                //得到地址名称
                var addComp = rs.addressComponents;
                var currentPosition = addComp.province + "" + addComp.city + "" + addComp.district + "" + addComp.street + "" + addComp.streetNumber;
//            G("current_position").innerHTML=currentPosition;
//            G("current_lng_lat").innerHTML=pp.lng+"/"+pp.lat;
                var provinceName = addComp.province;
                var cityName = addComp.city;
                var districtName = addComp.district;
                var streetName = addComp.street;
                var streetNumber = addComp.streetNumber;
                parent.setLocation(pp,currentPosition,provinceName,cityName,districtName,streetName,streetNumber);
            });

            map.centerAndZoom(pp, 18);
            map.addOverlay(new BMap.Marker(pp));    //添加标注
        }
        var local = new BMap.LocalSearch(map, { //智能搜索
            onSearchComplete: myFun
        });
        local.search(myValue);
    }



</script>
