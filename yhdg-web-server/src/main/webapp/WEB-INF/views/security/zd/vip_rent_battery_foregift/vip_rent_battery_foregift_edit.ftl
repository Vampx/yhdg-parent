<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="foregiftId" value="${(foregiftId)!0}">
            <input type="hidden" name="priceId" value="${(priceId)!0}">
            <input type="hidden" name="agentId" value="${(agentId)!0}">
            <input type="hidden" name="id" value="${(id)!0}">
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
                    <td align="right">押金：</td>
                    <td><input type="text" class="text easyui-numberspinner" required="true" maxlength="10" id="money_${pid}" readonly
                               data-options="min:0, precision:2" style="width: 184px; height: 28px;" value="${((entity.money)/100)!0}"/></td>
                </tr>
                <tr>
                    <td align="right">减免：</td>
                    <td><input type="text" class="text easyui-numberspinner" required="true" maxlength="10" id="reduce_money_${pid}"
                               data-options="min:0, precision:2" style="width: 184px; height: 28px;" value="${((reduceMoney)/100)!0}"/></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td colspan="3"><textarea style="width:300px; height: 110px;" name="memo" maxlength="200">${(memo)!''}</textarea></td>
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

            var reduceMoney = $('#reduce_money_${pid}').numberspinner('getValue');
            if (reduceMoney == '') {
                $.messager.alert('提示信息', "请填写减免金额", 'info');
                return false;
            }
            form.form('submit', {
                url: '${contextPath}/security/zd/vip_rent_battery_foregift/vip_rent_battery_foregift_update.htm',
                onSubmit: function(param) {
                    param.reduceMoney = parseInt(Math.round(reduceMoney * 100));
                    param.money = parseInt(Math.round(money * 100));
                    return true;
                },
                success: function(text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if(json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>