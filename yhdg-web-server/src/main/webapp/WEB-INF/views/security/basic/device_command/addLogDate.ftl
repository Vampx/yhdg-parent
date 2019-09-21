<div class="popup_body">
    <div class="ui_table">
        <form method="post" id="form_${pid}">
            <table cellpadding="0" cellspacing="0">
                <input  id="device_id"  name="deviceId" type="hidden"  >
                <tr>
                    <td width="70" align="right">日期：</td>
                    <td>
                        <input id="begin_time" class="easyui-datebox" name="logDate" type="text" style="width:180px;;height:30px;" >
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
    (function() {
        var pid = '${pid}', win = $('#' + pid), form = $('#form_${pid}'), windowData = win.data('windowData');
        var checked = windowData.checked;
        var deviceId = [];
        for(var i=0; i<checked.length; i++){
            deviceId.push(checked[i].deviceId)
        }
        $("input[name='deviceId']").val(deviceId);
        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/device_command/create.htm',
                success: function(text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if(json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });

        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>

