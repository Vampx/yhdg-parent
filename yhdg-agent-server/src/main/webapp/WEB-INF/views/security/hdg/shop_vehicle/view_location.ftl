<div class="tab_item" style="display: block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="lng" value="${(entity.lng)!''}">
            <input type="hidden" name="lat" value="${(entity.lat)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">经度/纬度:</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="lnglat"  value="<#if entity.lng?? && entity.lat??>${(entity.lng)!''}/${(entity.lat)!''}</#if>"/></td>
                    <td width="70" align="right"></td>
                    <td align="right"><a href="javascript:void(0)" class="a_red map_location" onclick="getLocation(${(entity.lng)!''},${(entity.lat)!''})">获取当前位置</a>&nbsp;&nbsp;&nbsp;</td>
                </tr>
                <tr>
                    <td width="70" align="right">详细地址：</td>
                    <td colspan="3"><input style="width:430px;" type="text" maxlength="20" class="text" name="address" value="${(entity.address)!''}"/></td>
                </tr>
                <tr>
                    <td align="right" colspan="4">
                        <div style="width:505px; height:290px; border:1px solid #ccc;">
                            <iframe id="map_frame_${pid}"
                                    src="${contextPath}/security/basic/baidu_map/view_map.htm" width="100%"
                                    height="100%" frameborder="0" style="border:0">
                            </iframe>
                        </div>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script type="text/javascript">
    (function () {
    var win = $('#${pid}'), windowData = win.data('windowData');
        var jform = win.find('form');
        var form = jform[0];

        var ok = function() {
            if(!jform.form('validate')) {
                return false;
            }
            var success = true;

            return success;
        };

        win.data('ok', ok);
    })();
    var myGeo = new BMap.Geocoder();
    function getLocation(lng, lat) {
        myGeo.getLocation(new BMap.Point(lng, lat), function (result) {
            if (!result) {
                $.messager.alert('提示信息', "获取失败", 'info');
            } else {
                var address = result.address;
                $('input[name = "address"]').val(address);
                $.post("${contextPath}/security/hdg/shop_vehicle/update_address.htm", {
                    id: ${(entity.id)!''},
                    address: address
                }, function (json) {
                }, 'json');
            }
        });
    }

    var mapFrame = document.getElementById('map_frame_${pid}');
    setTimeout(function () {
        $.post("${contextPath}/security/basic/baidu_map/vehicle_map_data.htm", {id: ${(entity.id)!''}}, function (json) {
            var doc = mapFrame.contentDocument || mapFrame.document;
            var childWindow = mapFrame.contentWindow || mapFrame;
            childWindow.createMap(json.data);
        }, 'json');
    }, 500)
</script>