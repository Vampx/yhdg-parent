<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td colspan="3">
                        <textarea style="width: 1380px;height: 700px;" readonly id="log_${pid}"></textarea>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<#--<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>-->
<script>

    (function() {
        $.post('${contextPath}/security/yms/terminal_crash_log/load.htm', {
            id: ${id}
        }, function(text) {
            $('#log_${pid}').html(text);
        }, 'text');

        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>