<div class="popup_body clearfix">
    <div class="ui_table clearfix">
        <div style="width:450px; height:350px; float:right; border:1px solid #ccc; margin:5px 5px 10px 15px;">
            <iframe id="map_frame_${pid}"
                    src="${contextPath}/security/basic/baidu_map/location.htm?lng=${(entity.lng)!''}&lat=${(entity.lat)!''}"
                    width="100%" height="100%" frameborder="0" style="border:0"></iframe>
        </div>
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" name="lng" value="${(entity.lng)!''}">
            <input type="hidden" name="lat" value="${(entity.lat)!''}">
            <input type="hidden" name="imagePath" id="station_image_${pid}" value="${(entity.imagePath)!''}">
            <table class="float_left" cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">救助站名称：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="stationName"
                               maxlength="20" value="${(entity.stationName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">联系电话：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="tel" maxlength="11"
                               value="${(entity.tel)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">星标：</td>
                    <td>
                        <select class="easyui-combobox" name="star" style="height: 28px; width: 182px;"
                                editable="false">
                        <#list starEnum as e>
                            <option <#if (entity.star)?? && entity.star == e.getValue()>selected</#if>
                                    value="${(e.getValue())!''}">${e.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">价格(元)：</td>
                    <td>
                        <input class="easyui-numberspinner" id="min_price_${pid}" required="required"
                               value="${(entity.minPrice/100)!''}"
                               style="width:80px;height: 28px;" data-options="min:0.01,precision:2">-
                        <input class="easyui-numberspinner" id="max_price_${pid}" required="required"
                               value="${(entity.maxPrice/100)!''}"
                               style="width:80px;height: 28px;" data-options="min:0.01,precision:2">
                    </td>
                </tr>
                <tr>
                    <td align="right">省份城市：</td>
                    <td>
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
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">详细地址：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="street" maxlength="80"
                               value="${(entity.street)!''}"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td><a href="javascript:void(0)" class="a_red map_location">地图定位</a></td>
                </tr>
                <tr>
                    <td align="right">经度纬度：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="lnglat"
                               value="<#if entity.lng?? && entity.lat??>${(entity.lng)!''}/${(entity.lat)!''}</#if>"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">经营范围：</td>
                    <td colspan="3"><textarea style="width:170px;" name="introduce"
                                              maxlength="256">${(entity.introduce)!''}</textarea></td>
                </tr>
                <tr>
                    <td align="right">救助站图片：</td>
                    <td>
                        <div class="image portrait" style="float: left; margin-right: 10px;">
                            <a href="javascript:void(0)"><img
                                    id="image_${pid}" src=<#if entity.imagePath ?? && entity.imagePath != ''>'${staticUrl}${entity.imagePath}' <#else>
                                '${app.imagePath}/user.jpg'</#if> /><span>修改图片</span></a>
                        </div>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    function set_image(param) {
        $('#image_${pid}').attr('src', param.staticUrl + param.filePath);
        $('#station_image_${pid}').val(param.filePath);
        $('#' + param.pid).window('close');
    }

    (function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form'),
                mapFrame = document.getElementById('map_frame_${pid}'),
                areaText = $('#area_text_${pid}');

        window.setLngLat = function (point) {
            win.find('input[name=lnglat]').val(point.lng + '/' + point.lat);
            win.find('input[name=lng]').val(point.lng);
            win.find('input[name=lat]').val(point.lat);
        };

        win.find('.map_location').click(function () {
            var province = areaText.attr('province_text') || '';
            var city = areaText.attr('city_text') || '';
            var district = areaText.attr('district_text') || '';
            var street = win.find('input[name=street]').val();
            var address = province + city + district + street;

            var doc = mapFrame.contentDocument || mapFrame.document;
            var childWindow = mapFrame.contentWindow || mapFrame;
            childWindow.locationAddress({
                province: province,
                city: city,
                district: district,
                street: street
            });
        });

        win.find('.image').click(function () {
            App.dialog.show({
                css: 'width:558px;height:150px;overflow:visible;',
                title: '上传',
                href: "${contextPath}/security/hdg/relief_station/image.htm",
                event: {
                    onClose: function () {
                    }
                }
            });
        });

        win.find('button.ok').click(function () {
            form.form('submit', {
                url: '${contextPath}/security/hdg/relief_station/update.htm',
                onSubmit: function (param) {
                    var maxPrice = $('#max_price_${pid}').numberspinner('getValue');
                    param.maxPrice = parseInt(Math.round(maxPrice * 100));
                    var minPrice = $('#min_price_${pid}').numberspinner('getValue');
                    param.minPrice = parseInt(Math.round(minPrice * 100));
                    return true;
                },
                success: function (text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if (json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        win.find('button.close').click(function () {
            win.window('close');
        });

    })()
</script>