<div class="tab_item" style="display: block; width: 100%;height: 100%;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="lng">
            <input type="hidden" name="lat" >
            <input type="hidden" name="geoHash" >
            <input type="hidden" name="address" >
            <input type="hidden" name="street" >
            <input type="hidden" name="provinceName">
            <input type="hidden" name="cityName">
            <input type="hidden" name="districtName">
            <input type="hidden" name="streetName">
            <input type="hidden" name="streetNumber">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right" colspan="2">
                        <div style="width:580px; height:440px; overflow: hidden;">
                            <iframe id="map_frame_${pid}" src="${contextPath}/security/basic/baidu_map/new_location.htm?lng=${lng}&lat=${lat}&address='' " width="100%" height="100%" frameborder="0" style="border:0;"></iframe>
                        </div>
                    </td>
                </tr>
                <tr><td><div id="current_location">当前位置：<span id="current_position"></span><br>当前经度/纬度：<span id="current_lng_lat">${(lng)!''}/${(lat)!''}</span> </div></td><td align="right"><a href="javascript:void(0)" class="a_red map_location">修改为当前地址</a>&nbsp;&nbsp;&nbsp;</td></tr>
            </table>
        </form>
    </div>
</div>
<script>
    (function () {
        var win = $('#${pid}');

        function G(id) {
            return document.getElementById(id);
        }

        window.setLocation = function (pp, currentPosition, provinceName, cityName, districtName, streetName, streetNumber) {
            win.find('input[name=lng]').val(pp.lng);
            win.find('input[name=lat]').val(pp.lat);
            win.find('input[name=address]').val(currentPosition);
            win.find('input[name=provinceName]').val(provinceName);
            win.find('input[name=cityName]').val(cityName);
            win.find('input[name=districtName]').val(districtName);
            win.find('input[name=streetName]').val(streetName);
            win.find('input[name=streetNumber]').val(streetNumber);
            G("current_position").innerHTML = currentPosition;
            G("current_lng_lat").innerHTML = pp.lng + "/" + pp.lat;
        }

        win.find('.map_location').click(function () {
            $.messager.confirm('提示信息', '确认修改为当前地址?', function (ok) {
                if (ok) {
                    var win = $('#${pid}');
                    var shop = {
                        lng: win.find('input[name=lng]').val(),
                        lat: win.find('input[name=lat]').val(),
                        address: win.find('input[name=address]').val(),
                        street: win.find('input[name=street]').val(),
                        provinceName: win.find('input[name=provinceName]').val(),
                        cityName: win.find('input[name=cityName]').val(),
                        districtName: win.find('input[name=districtName]').val(),
                        streetName: win.find('input[name=streetName]').val(),
                        streetNumber: win.find('input[name=streetNumber]').val()
                    };
                    var windowData = win.data('windowData');
                    windowData.ok({
                        shop:shop
                    });
                    win.window('close');
                }
            });
        });

    })();
</script>