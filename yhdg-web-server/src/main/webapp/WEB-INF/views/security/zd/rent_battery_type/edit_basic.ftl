<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" name="batteryType" value="${(entity.batteryType)!''}">
            <input type="hidden" name="toBatteryType" value="${(entity.batteryType)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">运营商名称：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" editable="false"
                               <#if (entity.agentId)??>disabled</#if> style="width: 184px; height: 28px;"
                               url="${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}"
                               value="${(entity.agentId)!''}">
                    </td>
                </tr>
                <tr>
                    <td align="right">类型名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="typeName" readonly required="true" maxlength="40" value="${(entity.typeName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">额定电压：</td>
                    <td><input type="text" id="rated_voltage_${pid}" readonly class="easyui-numberbox"  data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" value="${(entity.ratedVoltage/1000)!''}" >V</td>
                </tr>
                <tr>
                    <td align="right">额定容量：</td>
                    <td><input type="text" id="rated_capacity_${pid}" readonly class="easyui-numberbox"  data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" value="${(entity.ratedCapacity/1000)!''}" >Ah</td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>

    (function () {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];
        win.find('input[name=typeName]').click(function() {
            selectBatteryType();
        });

        var snapshot = $.toJSON({
            batteryType: '${(entity.batteryType)!''}',
            toBatteryType: '${(entity.batteryType)!''}',
            agentId: '${(entity.agentId)!''}',
            typeName:'${(entity.typeName)!''}'
        });

        var ok = function () {
            if (!jform.form('validate')) {
                return false;
            }

            var success = true;
            var values = {
                batteryType: '${(entity.batteryType)!''}',
                toBatteryType: win.find('input[name=toBatteryType]').val(),
                agentId: $('#agent_id_${pid}').combobox('getValue'),
                typeName:win.find('input[name=typeName]').val()
            };

            if (snapshot != $.toJSON(values)) {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/zd/rent_battery_type/update.htm',
                    dataType: 'json',
                    data: values,
                    success: function (json) {
                    <@app.json_jump/>
                        if (json.success) {
                            win.window("close");
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

    function selectBatteryType() {
        var win = $('#${pid}');
        App.dialog.show({
            css: 'width:826px;height:522px;overflow:visible;',
            title: '选择电池类型',
            href: "${contextPath}/security/basic/system_battery_type/select_battery_type.htm",
            windowData: {
                getOk: function(config) {
                    win.find('input[name=toBatteryType]').val(config.systemBatteryType.id);
                    win.find('input[name=typeName]').val(config.systemBatteryType.typeName);
                    $('#rated_voltage_${pid}').val(config.systemBatteryType.ratedVoltage/1000);
                    $('#rated_capacity_${pid}').val(config.systemBatteryType.ratedCapacity/1000);
                }
            },
            event: {
                onClose: function() {
                }
            }
        });
    }


</script>
