<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">电芯厂家：</td>
                    <td><input type="text" class="text easyui-validatebox" name="cellMfr" maxlength="40" readonly value="${(entity.cellMfr)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">电芯型号：</td>
                    <td><input type="text" class="text easyui-validatebox" name="cellModel" maxlength="40" readonly
                               value="${(entity.cellModel)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">备注：</td>
                    <td colspan="3"><textarea style="width:260px;height:50px;" readonly name="memo" maxlength="200">${(entity.memo)!''}</textarea></td>
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