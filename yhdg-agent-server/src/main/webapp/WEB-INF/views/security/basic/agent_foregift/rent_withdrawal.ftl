<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">运营商名称：</td>
                    <td><input type="text" maxlength="20" class="text easyui-validatebox" name="agentName" value="${(entity.agentName)!''}"/></td>
                </tr>
                <tr>
                    <td width="80" align="right">可转余额：</td>
                    <td><input type="text" id="zd_withdraw_money_${pid}" class="text easyui-numberspinner" name="zdWithdrawMoney" style="height: 28px;" data-options="min:${entity.zdWithdrawMoney/100},max:${entity.zdWithdrawMoney/100},precision:2,editable:false" value="<#if entity.zdWithdrawMoney??>${entity.zdWithdrawMoney/100}<#else>0</#if>"/></td>
                </tr>
                <tr>
                    <td width="80" align="right">转余额：</td>
                    <td><input type="text" id="amount_${pid}" class="text easyui-numberspinner" name="amount" style="height: 28px;" required="true" data-options="min:0,max:${entity.zdWithdrawMoney/100},precision:2" value="<#if entity.zdWithdrawMoney??>${entity.zdWithdrawMoney/100}<#else>0</#if>"/></td>
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
                form = win.find('form'),
                withdrawMoney = $('#zd_withdraw_money_${pid}'),
                amount = $('#amount_${pid}');

        win.find('button.ok').click(function() {
            $.messager.confirm('提示信息', '确认押金池余额转入运营商余额?', function(ok) {
                if(ok) {
                    form.form('submit', {
                        url: '${contextPath}/security/basic/agent_foregift/update_rent_withdrawal.htm',
                        onSubmit: function(param) {
                            if(!form.form('validate')) {
                                return false;
                            }
                            var b = parseFloat(withdrawMoney.numberspinner('getValue'));
                            var m = parseFloat(amount.numberspinner('getValue'));

                            if(b <= 0) {
                                $.messager.alert('提示信息', '余额大于0才能提现', 'info');
                                return false;
                            }
                            if(m > b) {
                                $.messager.alert('提示信息', '提现金额不能大于余额', 'info');
                                return false;
                            }

                            param.money = parseInt(parseFloat(amount.numberspinner('getValue')) * 100);
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
                }
            });
        });
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>
