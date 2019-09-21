<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                </tr>
                    <td>角色名称：</td>
                    <td>
                        <input type="text" name="partName" id="part_name_${pid}" class="text easyui-validatebox" required="true" />
                    </td>
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


    $('#close_${pid}').click(function() {
        $('#${pid}').window('close');
    });

    (function () {
        var pid = '${pid}',
                win = $('#' + pid),
                 windowData = win.data('windowData');

        win.find('button.ok').click(function () {
            var partName = win.find('input[name=partName]').val();
            windowData.ok(partName);
            win.window('close');
        })

        win.find('button.close').click(function () {
            win.window('close');
        });
    })()

</script>






