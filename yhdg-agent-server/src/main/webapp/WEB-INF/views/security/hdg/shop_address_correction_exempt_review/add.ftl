<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" >
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">客户姓名：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly  value="" maxlength="40" name="nickname"  style="width: 184px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">手机号：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly maxlength="11" name="mobile" value="" style="width: 184px; height: 28px;"/></td>
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

        win.find('input[name=nickname],input[name=mobile]').click(function() {
            selectCustomer();
        });

        function selectCustomer() {
            App.dialog.show({
                css: 'width:826px;height:522px;overflow:visible;',
                title: '选择客户',
                href: "${contextPath}/security/basic/customer/select_customer.htm",
                windowData: {
                    ok: function(config) {
                        win.find('input[name=id]').val(config.customer.id);
                        win.find('input[name=nickname]').val(config.customer.fullname);
                        win.find('input[name=mobile]').val(config.customer.mobile);
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
                url: '${contextPath}/security/hdg/shop_address_correction_exempt_review/create.htm',
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
