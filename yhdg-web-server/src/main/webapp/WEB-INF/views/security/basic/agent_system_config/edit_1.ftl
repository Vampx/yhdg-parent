<div class="popup_body">
    <div class="ui_table">
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td width="70" align="right">配置名称：</td>
                <td><input type="text" class="text" maxlength="40" value="${(entity.configName)!''}"/></td>
            </tr>
            <tr>
                <td align="right" valign="top" style="padding-top:10px;">配置值：</td>
                <td><textarea style="width:330px;height: 200px;" maxlength="2048" id="config_value_${pid}">${(entity.configValue)!''}</textarea></td>
            </tr>
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script type="text/javascript">

    (function() {
        var pid = '${pid}', win = $('#' + pid);
        win.find('button.close').click(function() {
            win.window('close');
        });
        win.find('button.ok').click(function() {
            var obj = $('#config_value_${pid}');

            $.ajax({
                cache: false,
                url: '${contextPath}/security/basic/agent_system_config/update.htm',
                type: "POST",
                data:  {
                    configValue: obj.attr('attr_value') || obj.val(),
                    configKey: '${(entity.configKey)!''}',
                    id: '${entity.id}',
                    agentId: '${entity.agentId}'
                },
                dataType: 'json',
                success: function(json) {
                    <@app.json_jump/>
                    if(json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        alert(json.message);
                    }
                }
            });
        });
    })();

</script>