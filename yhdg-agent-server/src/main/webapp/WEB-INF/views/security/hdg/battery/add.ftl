<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">电池编号：</td>
                    <td><input type="text" class="text easyui-validatebox" name="id" maxlength="8"
                               validType="unique[]" style="width: 173px; height: 28px;"/></td>
                    <td align="right">运营商：</td>
                    <td>
                        <input name="agentId" required="true" id="agent_id_${pid}" class="easyui-combotree"
                               editable="false" style="width: 184px; height: 28px;"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto'
                            "
                                >
                    </td>
                </tr>
                <tr>
                    <td align="right">&nbsp;&nbsp;品牌：</td>
                    <td>
                        <select id="brand" name="brand" class="easyui-combobox" style="width: 184px; height: 28px;">
                        <#if brandList??>
                            <#list brandList as e>
                                <option value="${(e.itemValue)!''}">${(e.itemName)!''}</option>
                            </#list>
                        </#if>
                        </select>
                    </td>
                    <td align="right">电池类型：</td>
                    <td>
                        <select name="type" class="easyui-combobox" style="width: 182px; height: 30px;" required="true"
                                editable="false" style="width: 184px; height: 28px;">
                        <#list batteryTypeList as e>
                            <option value="${(e.itemValue)!''}">${(e.itemName)!''}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">IMEI：</td>
                    <td><input maxlength="40" class="text easyui-validatebox" name="code" required="true"
                               validType="uniqueCode[]" style="width: 173px; height: 28px;"/></td>
                    <#--<td align="right" width="90">最小功率：</td>-->
                    <#--<td><input id="min_power_${pid}" name="minPower" class="easyui-numberspinner"-->
                               <#--style="width: 184px; height: 28px;" required="required" value="15"></td>-->
                    <td align="right">二维码：</td>
                    <td><input type="text" class="text easyui-validatebox" validType="uniqueQrcode[]" name="qrcode"
                               required="true" style="width: 173px; height: 28px;"/></td>
                </tr>
                <tr>

                    <td align="right">外壳编号：</td>
                    <td><input type="text" class="text easyui-validatebox" validType="uniqueShellCode[]"
                               name="shellCode"  style="width: 173px; height: 28px;"/></td>
                    <td align="right">启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_1" checked value="1"/><label
                                for="is_active_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_0" value="0"/><label
                                for="is_active_0">禁用</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right">上报单体电压：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isReportVoltage" id="is_report_voltage_1"
                                   value="1"/><label for="is_report_voltage_1">是</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isReportVoltage" id="is_report_voltage_0" checked
                                   value="0"/><label for="is_report_voltage_0">否</label>
                        </span>
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
    (function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function () {
            form.form('submit', {
                url: '${contextPath}/security/hdg/battery/create.htm',
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