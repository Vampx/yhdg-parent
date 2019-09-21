<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <input type="hidden" name="priceId" value="${(priceId)!0}">
                <input type="hidden" name="agentId" value="${(agentId)!0}">
                <tr>
                    <td align="right">客户手机：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="11" name="mobile" required="true" style="width:170px;height: 30px "/></td>
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
            var mobile = $('input[name = "mobile"]').val();
            if ($.trim(mobile) == '') {
                $.messager.alert('提示信息', '请输入客户手机', 'info');
                return;
            }
            if ($.trim(mobile).length != 11) {
                $.messager.alert('提示信息', '手机号格式错误', 'info');
                return;
            }
            form.form('submit', {
                url: '${contextPath}/security/zc/vehicle_vip_price_customer/create.htm',
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