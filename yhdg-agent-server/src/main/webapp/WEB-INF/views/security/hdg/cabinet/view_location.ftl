<div class="tab_item" style="display: block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="lng" value="${(entity.lng)!''}">
            <input type="hidden" name="lat" value="${(entity.lat)!''}">
            <input type="hidden" name="geoHash" value="${(entity.geoHash)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">省份城市：</td>
                    <td>
                        <div class="select_city">
                            <#assign areaText=''>
                                <#if (entity.provinceName)??>
                                <#assign provinceName=entity.provinceName>
                                <#assign areaText=entity.provinceName>
                            </#if>
                                <#if (entity.cityName)??>
                                <#assign cityName=entity.cityName>
                                <#assign areaText=areaText + ' - ' + entity.cityName>
                            </#if>
                                <#if (entity.districtName)??>
                                <#assign districtName=entity.districtName>
                                <#assign areaText=areaText + ' - ' + entity.districtName>
                            </#if>

                            <#if (entity.provinceId)??>
                                <#assign provinceId=entity.provinceId>
                            </#if>
                            <#if (entity.cityId)??>
                                <#assign cityId=entity.cityId>
                            </#if>
                            <#if (entity.districtId)??>
                                <#assign districtId=entity.districtId>
                            </#if>
                            <#include '../../basic/area/select_area.ftl'>
                        </div>
                    </td>
                    <td width="70" align="right">详细地址：</td>
                    <td><input type="text" maxlength="20" class="text easyui-validatebox"  name="street" value="${(entity.street)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">经度/纬度:</td>
                    <td><input type="text" class="text easyui-validatebox" name="lnglat" value="<#if entity.lng?? && entity.lat??>${(entity.lng)!''}/${(entity.lat)!''}</#if>"/></td>
                    <td width="70" align="right"></td>
                    <td align="right"><a href="javascript:void(0)" class="a_red map_location">生成地图</a>&nbsp;&nbsp;&nbsp;</td>
                </tr>
                <tr>
                    <td align="right" colspan="4">
                        <div style="width:505px; height:290px; border:1px solid #ccc;">
                            <iframe id="map_frame_${pid}" src="${contextPath}/security/basic/baidu_map/location.htm?lng=${(entity.lng)!''}&lat=${(entity.lat)!''}" width="100%" height="100%" frameborder="0" style="border:0"></iframe>
                        </div>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var jform = win.find('form')
        var form = jform[0];
        var lng = '${(entity.lng)!''}', lat = '${(entity.lat)!''}';
        var areaText = $('#area_text_${pid}');
        var mapFrame = document.getElementById('map_frame_${pid}');

        window.setLngLat = function(point) {
            win.find('input[name=lnglat]').val(point.lng + '/' + point.lat);
            win.find('input[name=lng]').val(point.lng);
            win.find('input[name=lat]').val(point.lat);

            if(point.lng != lng || point.lat != lat) {
                win.find('input[name=geoHash]').val('');
            }
        }

        win.find('.map_location').click(function() {

            var province = areaText.attr('province_text') || '';
            var city = areaText.attr('city_text') || '';
            var district = areaText.attr('district_text') || ''
            var street = win.find('input[name=street]').val();
            var address =  province + city + district + street;

            var doc = mapFrame.contentDocument || mapFrame.document;
            var childWindow = mapFrame.contentWindow || mapFrame;
            childWindow.locationAddress({
                province: province,
                city: city,
                district: district,
                street: street
            });
        });

        var ok = function() {
            if(!jform.form('validate')) {
                return false;
            }
            var success = true;
            var values = {
                id: '${entity.id}',
                provinceId: form.provinceId.value,
                cityId: form.cityId.value,
                districtId: form.districtId.value,
                lng: form.lng.value,
                lat: form.lat.value,
                street: form.street.value,
                geoHash: form.geoHash.value
            };

            return success;
        }

        win.data('ok', ok);
    })();
</script>