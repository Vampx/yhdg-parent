<div class="popup_body">
    <div class="ui_table">
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td align="right">异常标识原因：</td>
                <td>
                    <textarea id="abnormal_cause" maxlength="1024"></textarea>
                </td>
            </tr>
        </table>
    </div>
</div>
<div class="popup_btn" style="margin-right: 0px;">
    <button class="btn btn_red" onclick="confirmAbnormal()">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    var win = $('#${pid}');
    win.find('button.close').click(function() {
        win.window('close');
    });
</script>