<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="staffId" value="${(entity.id)!''}">
            <input type="hidden" name="agentId" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">运营商名称：</td>
                    <td><input type="text" maxlength="20" class="text easyui-validatebox" name="agentName" value="${(entity.agentName)!''}"/></td>
                </tr>
                <tr>
                    <td width="80" align="right">可用余额：</td>
                    <td><input type="text" id="balance_${pid}" class="text easyui-numberspinner" name="balance" style="height: 28px;" data-options="min:${entity.balance/100},max:${entity.balance/100},precision:2,editable:false" value="<#if entity.balance??>${entity.balance/100}<#else>0</#if>"/></td>
                </tr>
                <tr>
                    <td width="80" align="right">支付宝账户：</td>
                    <td><input type="text" class="text easyui-validatebox" name="alipay" required="true" value="${(entity.alipay)!''}"/></td>
                </tr>
                <tr>
                    <td width="80" align="right">账户姓名：</td>
                    <td><input type="text" class="text easyui-validatebox" name="alipayName" required="true" value="${(entity.alipayName)!''}"/></td>
                </tr>
                <tr>
                    <td width="80" align="right">提现金额：</td>
                    <td><input type="text" id="amount_${pid}" class="text easyui-numberspinner" name="amount" style="height: 28px;" required="true" data-options="min:0,max:${entity.balance/100},precision:2" value="<#if entity.balance??>${entity.balance/100}<#else>0</#if>"/></td>
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
                balance = $('#balance_${pid}'),
                amount = $('#amount_${pid}');

        win.find('button.ok').click(function() {
            $.messager.confirm('提示信息', '确认支付宝账户和账户名称正确?', function(ok) {
                if(ok) {
                    form.form('submit', {
                        url: '${contextPath}/security/basic/agent/withdrawal.htm',
                        onSubmit: function(param) {
                            if(!form.form('validate')) {
                                return false;
                            }
                            var b = parseFloat(balance.numberspinner('getValue'));
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
                                $.messager.alert('提示信息', '操作成功,请等待审核', 'info');
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
