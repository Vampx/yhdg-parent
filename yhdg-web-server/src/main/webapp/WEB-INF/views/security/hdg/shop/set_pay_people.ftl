<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="customerId" >
            <input type="hidden" name="id" value="${id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right" width="80">收款人姓名：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly  value="" maxlength="40" name="payPeopleName"  style="width: 184px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="90" align="right">收款人手机号：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly maxlength="11" name="payPeopleMobile" value="" style="width: 184px; height: 28px;"/></td>
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

        win.find('input[name=payPeopleName],input[name=payPeopleMobile]').click(function() {
            selectCustomer();
        });

        function selectCustomer() {
            App.dialog.show({
                css: 'width:826px;height:522px;overflow:visible;',
                title: '选择客户',
                href: "${contextPath}/security/basic/customer/select_whitelist_customer.htm?agentId=" + ${(agentId)!0},
                windowData: {
                    ok: function(config) {
                        win.find('input[name=customerId]').val(config.customer.id);
                        win.find('input[name=payPeopleName]').val(config.customer.fullname);
                        win.find('input[name=payPeopleMobile]').val(config.customer.mobile);
                    }
                },
                event: {
                    onClose: function() {
                    }
                }
            });
        }

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/hdg/shop/set_pay_people_update.htm',
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
