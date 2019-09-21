<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">客户姓名：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly name="mobile" value="${(entity.customerName)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">手机号码：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly name="mobile" value="${(entity.mobile)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">反馈内容：</td>
                    <td colspan="3">
                        <textarea style="width:355px;" maxlength="256" readonly name="content">${(entity.content)!''}</textarea>
                    </td>
                </tr>

            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>