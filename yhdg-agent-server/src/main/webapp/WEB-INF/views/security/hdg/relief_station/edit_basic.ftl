<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">救助站名称：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="stationName" maxlength="20" value="${(entity.stationName)!''}"/></td>
                    <td align="right">联系电话：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="tel" maxlength="11" value="${(entity.tel)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">星标：</td>
                    <td>
                        <select class="easyui-combobox" id="star_${pid}" name="star" style="height: 28px; width: 182px;"
                                editable="false">
                        <#list starEnum as e>
                            <option <#if (entity.star)?? && entity.star == e.getValue()>selected</#if>
                                    value="${(e.getValue())!''}">${e.getName()}</option>
                        </#list>
                        </select>
                    </td>
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
                    <td align="right">经营范围：</td>
                    <td colspan="3"><textarea style="width:420px;"  name="introduce" maxlength="256">${(entity.introduce)!''}</textarea></td>
                </tr>
                <tr>
                    <td align="right">救助站图片：</td>
                    <td>
                        <div class="image portrait" style="float: left; margin-right: 10px;">
                            <a href="javascript:void(0)"><img id="image_${pid}" src="<#if entity.imagePath?? && entity.imagePath!= ''>${staticUrl}${entity.imagePath}<#else>${app.imagePath}/user.jpg</#if>" /><span>修改图片</span></a>
                            <input  type="hidden" id="image_path_${pid}" name="imagePath" value="${(entity.imagePath)!''}"/>

                            <#--<a href="javascript:void(0)"><img id="image_${pid}" name="imagePath" src=<#if entity.imagePath ?? && entity.imagePath != ''>'${staticUrl}${entity.imagePath}' <#else>'${app.imagePath}/user.jpg'</#if> /><span>修改图片</span></a>-->
                        </div>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>
    function set_image(param) {
        $('#image_${pid}').attr('src', param.staticUrl + param.filePath);
        $('#image_path_${pid}').val(param.filePath);
        $('#' + param.pid).window('close');
    }

    (function () {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];

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

        var snapshot = $.toJSON({
            id: '${entity.id}',
            stationName: '${(entity.stationName)!''}',
            tel: '${(entity.tel)!''}',
            introduce: '${(entity.introduce)!''}',
            imagePath: '${(entity.imagePath)!''}',
            minPrice: '${(entity.minPrice)!''}',
            maxPrice: '${(entity.maxPrice)!''}',
            star: '${(entity.star)!''}'
        });

        var ok = function () {
            if (!jform.form('validate')) {
                return false;
            }

            var success = true;
            var values = {
                id: '${entity.id}',
                stationName: form.stationName.value,
                tel: form.tel.value,
                introduce: form.introduce.value,
                imagePath: $("#image_path_${pid}").val(),
                minPrice: $("#min_price_${pid}").val()*100,
                maxPrice: $("#max_price_${pid}").val()*100,
                star: $("#star_${pid}").combobox('getValue')
            };

            if (snapshot != $.toJSON(values)) {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/hdg/relief_station/update_basic.htm',
                    dataType: 'json',
                    data: values,
                    success: function (json) {
                    <@app.json_jump/>
                        if (json.success) {
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                            success = false;
                        }
                    }
                });
            }

            return success;
        };

        win.data('ok', ok);
    })();
</script>
