<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" name="customerId" value="${(entity.customerId)!''}">
            <input type="hidden" name="batteryType" value="${(entity.batteryType)!''}">
            <table cellpadding="0" cellspacing="0">
                <td align="right">运营商：</td>
                <td>
                    <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" style="width: 184px; height: 28px;"
                           url="${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}" disabled
                           value="${(entity.agentId)!''}">
                </td>
                <tr>
                    <td width="70" align="right">手机号：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" value="${(entity.mobile)!''}"  maxlength="11" name="mobile" style="width: 174px;height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">姓名：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true"  value="${(entity.fullname)!''}" maxlength="40"  name="fullname"  style="width: 174px;height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">类型名称：</td>
                    <td><input type="text" class="text easyui-validatebox"  name="typeName" readonly required="true" maxlength="40" value="${(entity.typeName)!''}" /></td>
                </tr>
                <tr>
                    <td align="right">额定电压：</td>
                    <td><input type="text" id="rated_voltage_${pid}" readonly class="text easyui-validatebox"  data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" value="${(entity.ratedVoltage/1000)!''}" >V</td>
                </tr>
                <tr>
                    <td align="right">额定容量：</td>
                    <td><input type="text" id="rated_capacity_${pid}" readonly class="text easyui-validatebox""  data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" value="${(entity.ratedCapacity/1000)!''}" >Ah</td>
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

        win.find('input[name=fullname]').click(function() {
            var mobile = win.find('input[name=mobile]').val();
            if(mobile == null || mobile == '') {
                $.messager.alert('提示信息', '请输入手机号', 'info');
                return;
            }else{
                selectCustomer();
            }
        });

        function selectCustomer() {
            var agentId = ${Session['SESSION_KEY_USER'].agentId};
            App.dialog.show({
                css: 'width:826px;height:522px;overflow:visible;',
                title: '选择客户',
                href: "${contextPath}/security/basic/customer/select_whitelist_customer.htm?agentId=" + agentId +"&mobile=" + mobile,
                windowData: {
                    ok: function(config) {
                        win.find('input[name=customerId]').val(config.customer.id);
                        win.find('input[name=fullname]').val(config.customer.fullname);
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
            var agentId = ${Session['SESSION_KEY_USER'].agentId};
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
            var fullname = win.find('input[name=fullname]').val();
            if(fullname == '') {
                $.messager.alert('提示信息', '请输入用户信息', 'info');
                return;
            }
            var ratedVoltage = $('#rated_voltage_${pid}').val();
            var ratedCapacity = $('#rated_capacity_${pid}').val();
            if(ratedVoltage == '' || ratedCapacity == '') {
                $.messager.alert('提示信息', '请输入电池类型', 'info');
                return;
            }
            var customerId = win.find('input[name=customerId]').val();
            var batteryType = win.find('input[name=batteryType]').val();
            //如果信息没有发生改变，直接提示操作成功
            if(agentId==${(entity.agentId)!''}&&customerId==${(entity.customerId)!''}&&batteryType==${(entity.batteryType)!''}) {
                $.messager.alert('提示信息', '操作成功', 'info');
                win.window('close');
            }else{
                form.form('submit', {
                    url: '${contextPath}/security/hdg/exchange_whitelist/update.htm',
                    onSubmit: function(param) {
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
            }
        });
        win.find('button.close').click(function () {
            win.window('close');
        });
    })()
</script>