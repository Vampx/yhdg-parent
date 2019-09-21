<div class="tab_item" style="display: block; width: 100%;height: 100%;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="lng" value="${(entity.lng)!''}">
            <input type="hidden" name="lat" value="${(entity.lat)!''}">
            <input type="hidden" name="geoHash" value="${(entity.geoHash)!''}">
            <input type="hidden" name="address" value="${(entity.address)!''}">
            <input type="hidden" name="street" value="${(entity.street)!''}">
            <input type="hidden" name="priceGroupId" value="${(entity.priceGroupId)!''}">
            <input type="hidden" name="workTime" value="${(entity.workTime)!''}">
            <input type="hidden" name="provinceName">
            <input type="hidden" name="cityName">
            <input type="hidden" name="districtName">
            <input type="hidden" name="streetName">
            <input type="hidden" name="streetNumber">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <#--<td width="70" align="right">省份城市：</td>-->
                    <#--<td>-->
                        <#--<div class="select_city">-->
                            <#--<#assign areaText=''>-->
                                <#--<#if (entity.provinceName)??>-->
                                <#--<#assign provinceName=entity.provinceName>-->
                                <#--<#assign areaText=entity.provinceName>-->
                            <#--</#if>-->
                                <#--<#if (entity.cityName)??>-->
                                <#--<#assign cityName=entity.cityName>-->
                                <#--<#assign areaText=areaText + ' - ' + entity.cityName>-->
                            <#--</#if>-->
                                <#--<#if (entity.districtName)??>-->
                                <#--<#assign districtName=entity.districtName>-->
                                <#--<#assign areaText=areaText + ' - ' + entity.districtName>-->
                            <#--</#if>-->

                            <#--<#if (entity.provinceId)??>-->
                                <#--<#assign provinceId=entity.provinceId>-->
                            <#--</#if>-->
                            <#--<#if (entity.cityId)??>-->
                                <#--<#assign cityId=entity.cityId>-->
                            <#--</#if>-->
                            <#--<#if (entity.districtId)??>-->
                                <#--<#assign districtId=entity.districtId>-->
                            <#--</#if>-->
                            <#--<#include '../../basic/area/select_area_agent.ftl'>-->
                        <#--</div>-->
                    <#--</td>-->
                    <#--<td width="70" align="right">详细地址：</td>-->
                    <#--<td><input type="text" maxlength="20" class="text easyui-validatebox" required="true" name="street" value="${(entity.street)!''}"/></td>-->
                <#--</tr>-->
                <#--<tr>-->
                    <#--<td width="70" align="right">经度/纬度:</td>-->
                    <#--<td><input type="text" class="text easyui-validatebox" required="true" name="lnglat" readonly="readonly" value="<#if entity.lng?? && entity.lat??>${(entity.lng)!''}/${(entity.lat)!''}</#if>"/></td>-->
                    <#--<td width="70" align="right"></td>-->
                    <#--<td align="right"><a href="javascript:void(0)" class="a_red map_location">生成地图</a>&nbsp;&nbsp;&nbsp;</td>-->
                <#--</tr>-->
                <tr>
                    <td align="right" colspan="2">
                        <div style="width:580px; height:440px; overflow: hidden; /*border:1px solid #ccc;*/">
                            <iframe id="map_frame_${pid}" src="${contextPath}/security/basic/baidu_map/new_location.htm?lng=${(entity.lng)!''}&lat=${(entity.lat)!''}&address=${(entity.address)!''}" width="100%" height="100%" frameborder="0" style="border:0;"></iframe>
                        </div>
                    </td>
                </tr>
                <tr><td><div id="current_location">当前位置：<span id="current_position">${(entity.address)!''}</span><br>当前经度/纬度：<span id="current_lng_lat">${(entity.lng)!''}/${(entity.lat)!''}</span> </div></td><td align="right"><a href="javascript:void(0)" class="a_red map_location">修改为当前地址</a>&nbsp;&nbsp;&nbsp;</td></tr>
                <#--<tr><td><div id="current_location">当前位置：<span id="current_position">${(entity.provinceName)!''}${(entity.cityName)!''}${(entity.districtName)!''}${(entity.street)!''}</span><br>当前经度/纬度：<span id="current_lng_lat">${(entity.lng)!''}/${(entity.lat)!''}</span> </div></td><td align="right"><a href="javascript:void(0)" class="a_red map_location">修改为当前地址</a>&nbsp;&nbsp;&nbsp;</td></tr>-->
            </table>
        </form>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        <#--var jform = win.find('form')-->
        <#--var form = jform[0];-->
        <#--var lng = '${(entity.lng)!''}', lat = '${(entity.lat)!''}';-->
        <#--var areaText = $('#area_text_${pid}');-->
        <#--var mapFrame = document.getElementById('map_frame_${pid}');-->

//        window.setLngLat = function(point) {
////            win.find('input[name=lnglat]').val(point.lng + '/' + point.lat);
//            win.find('input[name=lng]').val(point.lng);
//            win.find('input[name=lat]').val(point.lat);
//
////            if(point.lng != lng || point.lat != lat) {
////                win.find('input[name=geoHash]').val('');
////            }
//        }
        function G(id) {
            return document.getElementById(id);
        }
        window.setLocation = function (pp,currentPosition,provinceName,cityName,districtName,streetName,streetNumber) {
            win.find('input[name=lng]').val(pp.lng);
            win.find('input[name=lat]').val(pp.lat);
            win.find('input[name=address]').val(currentPosition);
            win.find('input[name=provinceName]').val(provinceName);
            win.find('input[name=cityName]').val(cityName);
            win.find('input[name=districtName]').val(districtName);
            win.find('input[name=streetName]').val(streetName);
            win.find('input[name=streetNumber]').val(streetNumber);
            G("current_position").innerHTML=currentPosition;
            G("current_lng_lat").innerHTML=pp.lng+"/"+pp.lat;
            var lng = '${(entity.lng)!''}', lat = '${(entity.lat)!''}';
            if (pp.lng != lng || pp.lat != lat) {
                win.find('input[name=geoHash]').val('');
            }
        }

        win.find('.map_location').click(function() {

//            var province = areaText.attr('province_text') || '';
//            var city = areaText.attr('city_text') || '';
//            var district = areaText.attr('district_text') || '';
//            var street = win.find('input[name=street]').val();
//            var address =  province + city + district + street;

//            var doc = mapFrame.contentDocument || mapFrame.document;
//            var childWindow = mapFrame.contentWindow || mapFrame;
//            childWindow.locationAddress({
//                province: province,
//                city: city,
//                district: district,
//                street: street
//            });
            $.messager.confirm('提示信息', '确认修改为当前地址?', function(ok) {
                if (ok) {
            var values = {
                id: '${entity.id}',
                lng: win.find('input[name=lng]').val(),
                lat: win.find('input[name=lat]').val(),
                priceGroupId: win.find('input[name=priceGroupId]').val(),
                workTime: win.find('input[name=workTime]').val(),
                address:win.find('input[name=address]').val(),
                street:win.find('input[name=street]').val(),
                provinceName:win.find('input[name=provinceName]').val(),
                cityName:win.find('input[name=cityName]').val(),
                districtName:win.find('input[name=districtName]').val(),
                streetName:win.find('input[name=streetName]').val(),
                streetNumber:win.find('input[name=streetNumber]').val()
            };

//            if(snapshot != $.toJSON(values)) {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/hdg/shop/update_new_location.htm',
                    dataType: 'json',
                    data: values,
                    success: function (json) {
                    <@app.json_jump/>
                        if (json.success) {
//                            $.messager.alert('提示信息', json.message, 'info');
                            win.window('close');
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                        }
                    }
                });
            }
        });
//            }
    });

        <#--var snapshot = $.toJSON({-->
            <#--id: '${entity.id}',-->
            <#--&lt;#&ndash;provinceId: '${(entity.provinceId)!''}',&ndash;&gt;-->
            <#--&lt;#&ndash;cityId: '${(entity.cityId)!''}',&ndash;&gt;-->
            <#--&lt;#&ndash;districtId: '${(entity.districtId)!''}',&ndash;&gt;-->
            <#--lng: '${(entity.lng)!''}',-->
            <#--lat: '${(entity.lat)!''}',-->
            <#--address:'${(entity.address)!''}'-->
            <#--&lt;#&ndash;street: '${(entity.street)!''}',&ndash;&gt;-->
            <#--&lt;#&ndash;geoHash: '${(entity.geoHash)!''}'&ndash;&gt;-->

        <#--});-->

        <#--var ok = function() {-->
            <#--if(!jform.form('validate')) {-->
                <#--return false;-->
            <#--}-->
            <#--var success = true;-->
            <#--var values = {-->
                <#--id: '${entity.id}',-->
                <#--provinceId: form.provinceId.value,-->
                <#--cityId: form.cityId.value,-->
                <#--districtId: form.districtId.value,-->
                <#--lng: form.lng.value,-->
                <#--lat: form.lat.value,-->
                <#--street: form.street.value,-->
                <#--geoHash: form.geoHash.value-->
            <#--};-->

            <#--if(snapshot != $.toJSON(values)) {-->
                <#--$.ajax({-->
                    <#--cache: false,-->
                    <#--async: false,-->
                    <#--type: 'POST',-->
                    <#--url: '${contextPath}/security/hdg/shop/update_location.htm',-->
                    <#--dataType: 'json',-->
                    <#--data: values,-->
                    <#--success: function (json) {-->
                    <#--<@app.json_jump/>-->
                        <#--if (json.success) {-->
                        <#--} else {-->
                            <#--$.messager.alert('提示信息', json.message, 'info');-->
                            <#--success = false;-->
                        <#--}-->
                    <#--}-->
                <#--});-->
            <#--}-->

            <#--return success;-->
        <#--}-->
        <#--win.data('ok', ok);-->

        var ok = function () {
            return true;
        };
        win.data('ok', ok);
    })();
</script>