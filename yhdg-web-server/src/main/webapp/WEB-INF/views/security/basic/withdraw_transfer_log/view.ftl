<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">订单ID：</td>
                    <td style="width: 300px"><input type="text" class="text" name="withdrawId" readonly value="${(entity.withdrawId)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">操作人：</td>
                    <td><input type="text" class="text" name="operatorName" readonly value="${(entity.operatorName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">返回结果：</td>
                    <td><textarea type="text" class="text" name="content" readonly style="width: 350px;height: 80px">${(entity.content)!''}</textarea></td>
                </tr>
                <tr>
                    <td align="right">创建时间：</td>
                    <td><input type="text" class="text" name="createTime" readonly value="${((entity.createTime)?string('yyyy-MM-dd HH:mm:ss'))!}"/></td>
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
        var pid = '${pid}',
            win = $('#' + pid),
            form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>