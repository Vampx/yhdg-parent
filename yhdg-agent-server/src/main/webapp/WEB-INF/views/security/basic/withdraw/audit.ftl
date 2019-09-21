<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">

            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="100" align="right">提现金额：</td>
                    <td>${(entity.money/100)!''}元</td>
                </tr>
                <tr>
                    <td width="100" align="right">审核原因：</td>
                    <td>
                        <textarea id="audit_memo"></textarea>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn" style="margin-right: 0px;">
    <button class="btn btn_red" onclick="updateStatus(2)">通过</button>
    <button class="btn btn_red" onclick="updateStatus(3)">不通过</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    var win = $('#${pid}');
    var jform = win.find('form');
    var form = jform[0];

    function updateStatus(status) {
        var values = {
            id: form.id.value,
            status: status,
            auditMemo: $('#audit_memo').html()
        };
        $.ajax({
            cache: false,
            async: false,
            type: 'POST',
            url: '${contextPath}/security/basic/withdraw/audit.htm',
            dataType: 'json',
            data: values,
            success: function (json) {
                <@app.json_jump/>
                if (json.success) {
                    win.window('close');
                } else {
                    $.messager.alert('提示信息', json.message, 'info');
                }
            }
        });
    };
    win.find('button.close').click(function() {
        win.window('close');
    });

</script>
