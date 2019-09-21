<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">设备编号：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" value="" maxlength="6" name="id" validType="unique[]"/></td>
                </tr>
                <tr>
                    <td align="right">策略：</td>
                    <td>
                        <select name="strategyId" id="terminal_strategy_id_${pid}" class="easyui-combobox" editable="false" required="true" style="width: 184px; height: 28px;">
                        <#if strategyList??>
                            <#list strategyList as e>
                                <option value="${e.id}">${(e.strategyName)!''}</option>
                            </#list>
                        </#if>
                        </select>
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
        var win = $('#${pid}'), jform = win.find('form');
        var form = jform[0];
        win.find('button.ok').click(function () {
            var agent = $('#agent_id_${pid}');
            var strategy = $('#terminal_strategy_id_${pid}');
            jform.form('submit', {
                url: '${contextPath}/security/yms/terminal/create.htm',
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


