<div class="tab_item" style="display: block; width: 100%;height: 100%;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="lng" value="${(entity.lng)!''}">
            <input type="hidden" name="lat" value="${(entity.lat)!''}">
            <input type="hidden" name="address" value="${(entity.address)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                <tr>
                    <td align="right" colspan="2">
                        <div style="width:580px; height:440px; overflow: hidden; /*border:1px solid #ccc;*/">
                            <iframe id="map_frame_${pid}" src="${contextPath}/security/basic/baidu_map/new_location.htm?lng=${(entity.lng)!''}&lat=${(entity.lat)!''}&address=${(entity.address)!''}" width="100%" height="100%" frameborder="0" style="border:0;"></iframe>
                        </div>
                    </td>
                </tr>
                <tr><td><div id="current_location">当前位置：<span id="current_position">${(entity.address)!''}</span><br>当前经度/纬度：<span id="current_lng_lat">${(entity.lng)!''}/${(entity.lat)!''}</span> </div></td><#--<td align="right"><a href="javascript:void(0)" class="a_red map_location">修改为当前地址</a>&nbsp;&nbsp;&nbsp;</td>--></tr>
            </table>
        </form>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        function G(id) {
            return document.getElementById(id);
        }
        window.setLocation = function (pp,currentPosition) {
            win.find('input[name=lng]').val(pp.lng);
            win.find('input[name=lat]').val(pp.lat);
            win.find('input[name=address]').val(currentPosition);
            G("current_position").innerHTML=currentPosition;
            G("current_lng_lat").innerHTML=pp.lng+"/"+pp.lat;
        }

        var ok = function () {
            return true;
        };
        win.data('ok', ok);
    })();
</script>