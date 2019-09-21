<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">原密码：</td>
                    <td><input type="password" class="text easyui-validatebox" required="true" name="oldPassword" /></td>
                </tr>
                <tr>
                    <td align="right">新密码：</td>
                    <td><input type="password" class="text easyui-validatebox" required="true" name="password" id="password"/></td>
                </tr>
                <tr>
                    <td width="80" align="right">确认密码：</td>
                    <td><input type="password" class="text easyui-validatebox" required="true" name="confirmPassword" validType="equals['#password']" /></td>
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

    $.extend($.fn.validatebox.defaults.rules, {
        equals: {
            validator: function(value,param){
                return value == $(param[0]).val();
            },
            message: '两次密码不一致'
        }
    });

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/main/password.htm',
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
