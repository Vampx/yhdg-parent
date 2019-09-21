<div class="popup_body">
    <div class="ui_table">
        <form id="form_${pid}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">标题：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="templateName" value="${(entity.templateName)!''}" maxlength="40" readonly/></td>
                    <td width="70" align="right"></td>
                    <td>
                    </td>
                </tr>
                <tr>
                    <td align="right">模板ID：</td>
                    <td colspan="3">
                        <textarea class="text easyui-validatebox" required="true" name="templateCode" style="width:435px; height: 32px;" readonly>${(entity.templateCode)!''}</textarea>
                    </td>
                </tr>
                <tr>
                    <td align="right">变量：</td>
                    <td colspan="3">
                        <textarea class="text easyui-validatebox" required="true" name="variable" style="width:435px; height: 32px;" readonly>${(entity.variable)!''}</textarea>
                    </td>
                </tr>
                <tr>
                    <td align="right">颜色：</td>
                    <td colspan="3">
                        <textarea class="text easyui-validatebox" required="true" name="color" style="width:435px; height: 32px;" readonly>${(entity.color)!''}</textarea>
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