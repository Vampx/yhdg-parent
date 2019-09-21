<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" id="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">客户姓名：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly
                               value="${(entity.customerFullname)!''}"/></td>
                    <td width="150" align="right">客户剩余运营商资金：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly
                               value="${(entity.agentBalance)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">离职原因：</td>
                    <td colspan="3">
                        <textarea style="width:500px;" maxlength="256" readonly>${(entity.content)!''}</textarea>
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">审批备注：</td>
                    <td colspan="3">
                        <textarea style="width:500px;" maxlength="256" id="reason">${(entity.reason)!''}</textarea>
                    </td>
                </tr>

            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red" onclick="ok(${ADOPT})">同意</button>
    <button class="btn btn_green" onclick="ok(${REFUSE})">拒绝</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    var pid = '${pid}', win = $('#' + pid);
    (function () {
        win.find('button.close').click(function () {
            win.window('close');
        });
    })();

    function ok(state) {
        var id = $("#id").val();
        var reason = $("#reason").val();
        var customerId = $("#customerId").val();
        $.post('${contextPath}/security/basic/resignation/update.htm', {
            id: id,
            reason: reason,
            state: state
        }, function (json) {
            if (json.success) {
                $.messager.alert('info', '操作成功', 'info');
                win.window('close');
            } else {
                $.messager.alert('提示消息', json.message, 'info');
            }
        }, 'json');
    }
</script>