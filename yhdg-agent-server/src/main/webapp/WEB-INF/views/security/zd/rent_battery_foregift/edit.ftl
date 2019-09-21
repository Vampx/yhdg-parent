<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!0}">
            <input type="hidden" name="agentId" value="${(entity.agentId)!''}">
            <input type="hidden" name="batteryType" value="${(entity.batteryType)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">运营商：</td>
                    <td><input type="text" readonly maxlength="20" class="text easyui-validatebox" required="true" name="agentName" value="${(agentName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">电池类型：</td>
                    <td><input type="text" readonly maxlength="20" class="text easyui-validatebox" required="true" name="typeName" value="${(typeName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">金额：</td>
                    <td><input class="easyui-numberspinner" required="true" id="money_${pid}" maxlength="5" style="width:184px;height: 28px;" data-options="min:0.00,precision:2" value="${(entity.money/100)}"></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td colspan="3"><textarea style="width:300px; height: 110px;" name="memo" maxlength="200">${(entity.memo)!''}</textarea></td>
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

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {
            var money = $('#money_${pid}').numberspinner('getValue');
            if (money == '') {
                $.messager.alert('提示信息', "请填写金额", 'info');
                return false;
            }
            var lastMoney = parseInt(Math.round(${((entity.money)!0)/100} * 100));
            var nextMoney = parseInt(Math.round(money * 100));
            if(lastMoney == nextMoney) {
                form.form('submit', {
                    url: '${contextPath}/security/zd/rent_battery_foregift/update.htm',
                    onSubmit: function(param) {
                        param.money = parseInt(Math.round(money * 100));
                        return true;
                    },
                    success: function(text) {
                        var json = $.evalJSON(text);
                    <@app.json_jump/>
                        if(json.success) {
                            $.messager.alert('提示信息', '操作成功', 'info');
                            win.window('close');
                            $.post('${contextPath}/security/zd/rent_battery_foregift/rent_battery_foregift.htm', {
                                batteryType: ${(entity.batteryType)!''},
                                agentId: ${(entity.agentId)!''}
                            }, function (html) {
                                $("#exchange_battery_foregift").html(html);
                            }, 'html');
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                        }
                    }
                });
            } else {
                $.messager.confirm('提示信息', '修改押金金额会删除对应的分期设置，确认修改?', function(ok) {
                    if(ok) {
                        form.form('submit', {
                            url: '${contextPath}/security/zd/rent_battery_foregift/update.htm',
                            onSubmit: function(param) {
                                param.money = parseInt(Math.round(money * 100));
                                return true;
                            },
                            success: function(text) {
                                var json = $.evalJSON(text);
                            <@app.json_jump/>
                                if(json.success) {
                                    $.messager.alert('提示信息', '操作成功', 'info');
                                    win.window('close');
                                    $.post('${contextPath}/security/zd/rent_battery_foregift/rent_battery_foregift.htm', {
                                        batteryType: ${(entity.batteryType)!''},
                                        agentId: ${(entity.agentId)!''}
                                    }, function (html) {
                                        $("#exchange_battery_foregift").html(html);
                                    }, 'html');
                                } else {
                                    $.messager.alert('提示信息', json.message, 'info');
                                }
                            }
                        });
                    }
                });
            }
        });
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>