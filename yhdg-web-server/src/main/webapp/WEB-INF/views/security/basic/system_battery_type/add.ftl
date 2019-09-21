<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">类型名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="typeName" required="true" maxlength="40" /></td>
                </tr>
                <tr>
                    <td align="right">额定电压：</td>
                    <td><input type="text" id="rated_voltage_${pid}" class="easyui-numberbox"  data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" >V</td>
                </tr>
                <tr>
                    <td align="right">额定容量：</td>
                    <td><input type="text" id="rated_capacity_${pid}" class="easyui-numberbox"  data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" >Ah</td>
                </tr>
                <tr>
                    <td align="right">是否启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_1" checked  value="1"/><label for="is_active_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_0"  value="0"/><label for="is_active_0">禁用</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">备注：</td>
                    <td colspan="3"><textarea style="width:260px;height:50px;" name="memo" maxlength="200"></textarea></td>
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
    (function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function () {
            form.form('submit', {
                url: '${contextPath}/security/basic/system_battery_type/create.htm',
                onSubmit: function(param) {
                    var ratedVoltage = $('#rated_voltage_${pid}').val();
                    var ratedCapacity = $('#rated_capacity_${pid}').val();
                    param.ratedVoltage = parseInt(Math.round(ratedVoltage * 1000));
                    param.ratedCapacity = parseInt(Math.round(ratedCapacity * 1000));
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