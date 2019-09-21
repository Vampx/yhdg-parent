<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
            <#list BatteryColumnEnum as batteryColumn>
                <#if batteryColumn.getValue()%2 ==1>
                <tr>
                </#if>
                <td><input type="checkbox" name="batteryColumn" checked value="${batteryColumn.getValue()}"/></td>
                <td><input type="text" class="text easyui-validatebox" maxlength="80"
                           value="${batteryColumn.getName()}"/></td>
                <#if batteryColumn.getValue()%2==0>
                </tr>
                </#if>
            </#list>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red" id="ok_${pid}">确定</button>
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>
<script>

    (function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form'),
                windowData = win.data('windowData');

        $('#ok_${pid}').click(function () {
            obj = $("input:checkbox[name='batteryColumn']");
            var columns = [];
            for (k in obj) {
                if (obj[k].checked)
                    columns.push(obj[k].value);
            }
            if (columns) {
                windowData.ok({
                    columns: columns
                });
                win.window('close');
            } else {
                $.messager.alert('提示信息', '请选择字段');
            }
        });

        $('#close_${pid}').click(function () {
            $('#${pid}').window('close');
        });
    })()
</script>