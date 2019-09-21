<div class="popup_body">
    <div class="ui_table">
        <form id="form_${pid}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">标题：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="title" readonly value="${(entity.title)!''}" maxlength="40" readonly/></td>
                    <td width="70" align="right"></td>
                    <td>
                    </td>
                </tr>
                <tr>
                    <td align="right">内容：</td>
                    <td colspan="3">
                        <textarea class="text easyui-validatebox" required="true" name="stand" style="width:435px; height: 32px;" readonly>${(entity.stand)!''}</textarea>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>

<script type="text/javascript">
    (function() {
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();


</script>