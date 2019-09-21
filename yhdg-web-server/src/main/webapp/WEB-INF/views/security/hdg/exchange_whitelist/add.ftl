<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="customerId">
            <input type="hidden" name="batteryType">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">运营商：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" required="true"
                               style="width: 184px; height: 28px;"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                    }"
                    </td>
                </tr>
                <tr>
                    <td align="right">姓名：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true"  value="" maxlength="40" name="fullname"  style="width: 174px;height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">手机号：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" maxlength="11" name="mobile" value="" style="width: 174px;height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">类型名称：</td>
                    <td><input type="text" class="text easyui-validatebox"  name="typeName" readonly required="true" maxlength="40" /></td>
                </tr>
                <tr>
                    <td align="right">额定电压：</td>
                    <td><input type="text" id="rated_voltage_${pid}" readonly class="text easyui-validatebox"  data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" >V</td>
                </tr>
                <tr>
                    <td align="right">额定容量：</td>
                    <td><input type="text" id="rated_capacity_${pid}" readonly class="text easyui-validatebox""  data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" >Ah</td>
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
                windowData = win.data('windowData'),
                form = win.find('form');

        win.find('input[name=fullname],input[name=mobile]').click(function() {
            selectCustomer();
        });

        function selectCustomer() {
            var agentId = $('#agent_id_${pid}').combobox('getValue');
            App.dialog.show({
                css: 'width:826px;height:522px;overflow:visible;',
                title: '选择客户',
                href: "${contextPath}/security/basic/customer/select_whitelist_customer.htm?agentId=" + agentId,
                windowData: {
                    ok: function(config) {
                        win.find('input[name=customerId]').val(config.customer.id);
                        win.find('input[name=fullname]').val(config.customer.fullname);
                        win.find('input[name=mobile]').val(config.customer.mobile);
                    }
                },
                event: {
                    onClose: function() {
                    }
                }
            });
        }

        win.find('input[name=typeName]').click(function() {
            selectBatteryType();
        });

        function selectBatteryType() {
            var agentId = $('#agent_id_${pid}').combobox("getValue");
            App.dialog.show({
                css: 'width:826px;height:522px;overflow:visible;',
                title: '选择电池类型',
                href: "${contextPath}/security/basic/agent_battery_type/select_battery_type.htm?agentId=" + agentId,
                windowData: {
                    getOk: function(config) {
                        win.find('input[name=batteryType]').val(config.agentBatteryType.batteryType);
                        win.find('input[name=typeName]').val(config.agentBatteryType.typeName);
                        $('#rated_voltage_${pid}').val(config.agentBatteryType.ratedVoltage/1000);
                        $('#rated_capacity_${pid}').val(config.agentBatteryType.ratedCapacity/1000);
                    }
                },
                event: {
                    onClose: function() {
                    }
                }
            });
        }

        win.find('button.ok').click(function () {
            var agentId = $('#agent_id_${pid}').combobox('getValue');
            if(agentId == '') {
                $.messager.alert('提示信息', '请选择运营商', 'info');
                return;
            }
            var mobile = win.find('input[name=mobile]').val();
            if(mobile == '') {
                $.messager.alert('提示信息', '请输入用户信息', 'info');
                return;
            }
            var ratedVoltage = $('#rated_voltage_${pid}').val();
            var ratedCapacity = $('#rated_capacity_${pid}').val();
            if(ratedVoltage == '' || ratedCapacity == '') {
                $.messager.alert('提示信息', '请输入电池类型', 'info');
                return;
            }

            form.form('submit', {
                url: '${contextPath}/security/hdg/exchange_whitelist/create.htm',
                onSubmit: function (param) {
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