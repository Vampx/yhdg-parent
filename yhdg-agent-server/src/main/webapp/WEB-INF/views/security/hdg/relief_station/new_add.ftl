<div class="popup_body clearfix">
    <div class="ui_table clearfix">
        <form>
            <input type="hidden" name="imagePath" id="station_image_${pid}">
            <table class="float_left" cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">救助站名称：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="stationName"
                               maxlength="20"/></td>
                </tr>
                <tr>
                    <td align="right">联系电话：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="tel" maxlength="11"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">星标：</td>
                    <td>
                        <select class="easyui-combobox" name="star" style="height: 28px; width: 182px;">
                        <#list starEnum as e>
                            <option value="${(e.getValue())!''}">${e.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">价格范围：</td>
                    <td>
                        <input class="easyui-numberspinner" id="min_price_${pid}" required="required"
                               style="width:80px;height: 28px;" data-options="min:0.01,precision:2">-
                        <input class="easyui-numberspinner" id="max_price_${pid}" required="required"
                               style="width:80px;height: 28px;" data-options="min:0.01,precision:2">
                    </td>
                </tr>
                <tr>
                    <td align="right">经营范围：</td>
                    <td colspan="3"><textarea style="width:170px;" name="introduce" maxlength="256"></textarea></td>
                </tr>
                <tr>
                    <td align="right">救助站图片：</td>
                    <td>
                        <div class="image portrait" style="float: left; margin-right: 10px;">
                            <a href="javascript:void(0)"><img id="image_${pid}" src="${app.imagePath}/user.jpg"/><span>修改图片</span></a>
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
                form = win.find('form');
        windowData = win.data('windowData');

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
                url: '${contextPath}/security/hdg/relief_station/new_create.htm',
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
//                        $.messager.alert('提示信息', '操作成功', 'info');
                        windowData.ok(json.data);
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