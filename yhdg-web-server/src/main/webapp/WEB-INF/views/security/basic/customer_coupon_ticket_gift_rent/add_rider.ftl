<script>
    $(function () {
        var win = $('#${pid}');
        win.find('.close').click(function () {
            win.window('close');
        });
        win.find('button.ok').click(function () {
            var mobile = $("#mobile_${pid}").val();
            var ticketGiftId =${ticketGiftId};
            $.post("${contextPath}/security/basic/customer_coupon_ticket_gift_rent/end_binding.htm?ticketGiftId=" + ticketGiftId + "&mobile=" + mobile, function (json) {
                if (json.success) {
                    $.messager.alert('提示消息', json.message, 'info');
                    win.window('close');
                } else {
                    $.messager.alert('提示消息', json.message, 'info');
                }
            }, 'json');

        });
    });
</script>
    <div class="popup_body">
        <div class="search">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td align="right">手机号：</td>
                    <td><input type="text" name="mobile" maxlength="11" id="mobile_${pid}" class="text easyui-validatebox" required="true" validType="mobile[]" /></td>
                </tr>
            </table>
        </div>
    </div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
