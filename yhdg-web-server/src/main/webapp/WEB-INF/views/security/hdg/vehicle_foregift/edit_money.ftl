<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">押金：</td>
                    <td><input type="text" class="text easyui-numberspinner" name="money"
                               data-options="min:0, precision:2" style="width: 184px; height: 28px;"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid), windowData = win.data('windowData'),
                form = win.find('form');

        win.find('button.ok').click(function() {
            var money = $('input[name = "money"]').val();
            if(windowData.ok(money)) {
                win.window('close');
            }
        });
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>