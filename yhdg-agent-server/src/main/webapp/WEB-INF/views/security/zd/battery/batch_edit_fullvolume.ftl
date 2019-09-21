<div class="popup_body">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">充满电量%：</td>
                    <td><input type="text" class="text easyui-numberspinner" id="chargeCompleteVolume" data-options="min:1, max:100, editable:true" name="chargeCompleteVolume" value="95" style="width: 184px; height: 28px;"/></td>
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
                win = $('#' + pid),
                form = win.find('form');
         var windowData = win.data('windowData');

        win.find('button.ok').click(function() {
            var chargeCompleteVolume = $('#chargeCompleteVolume').numberspinner('getValue');
            if(windowData.ok(chargeCompleteVolume)) {
                win.window('close');
            }
        });

        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>
