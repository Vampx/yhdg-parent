<div class="popup_body">
    <div class="ui_table">
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td align="right">禁用人：</td>
                <td>
                    <input type="text" class="text easyui-validatebox" name="operator" readonly value="${(entity.operator)!}"/>
                </td>
            </tr>
            <tr>
                <td align="right">禁用时间：</td>
                <td>
                    <input type="text" class="text easyui-datetimebox" style="width: 184px; height: 28px;" readonly name="operatorTime" value="${(entity.operatorTime?string('yyyy-MM-dd HH:mm:ss'))!''}">
                </td>
            </tr>
            <tr>
                <td align="right">禁用原因：</td>
                <td>
                    <textarea id="forbidden_cause" style="width: 210px;height: 50px">${(entity.forbiddenCause)!''}</textarea>
                </td>
            </tr>
        </table>
    </div>
</div>
<div class="popup_btn" style="margin-right: 0px;">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    var win = $('#${pid}');
    win.find('button.close').click(function() {
        win.window('close');
    });
</script>