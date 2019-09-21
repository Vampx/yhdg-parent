<div class="popup_body">
    <div class="ui_table">
        <form id="form_${pid}">
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">标题：</td>
                    <td><input type="text" maxlength="17" class="text easyui-validatebox" required="true" name="title" value="${(entity.title)!''}" maxlength="40"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">接收人：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" maxlength="10" name="receiver" value="${(entity.receiver)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">编号：</td>
                    <td><input type="text" class="text easyui-validatebox" name="code" maxlength="40" value="${(entity.code)!''}" maxlength="40"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">被叫显号：</td>
                    <td><input type="text" class="text easyui-validatebox" name="calledShowNumber" value="${(entity.calledShowNumber)!''}" maxlength="40"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">音量：</td>
                    <td>
                        <input class="easyui-numberspinner" name="volume" value="${(entity.volume)!0}" required="required" data-options="min:1,max:200" style="width:180px; height: 32px;">
                        <span style="color: red">&nbsp;&nbsp;取值范围 0 ~ 200</span>
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">播放次数：</td>
                    <td>
                        <input class="easyui-numberspinner" name="playTimes" value="${(entity.playTimes)!0}" required="required" data-options="min:1,max:200" style="width:180px; height: 32px;">
                    </td>
                </tr>
                <tr>
                    <td align="right">内容：</td>
                    <td colspan="3">
                        <textarea class="text easyui-validatebox" required="true"maxlength="160" name="content" style="width:435px; height: 32px;">${(entity.content)!''}</textarea>
                    </td>
                </tr>
                <tr>
                    <td align="right">变量：</td>
                    <td colspan="3">
                        <textarea class="text easyui-validatebox" required="true" maxlength="80" name="variable" style="width:435px; height: 32px;" readonly>${(entity.variable)!''}</textarea>
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

<script type="text/javascript">
    (function() {
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/voice_message_template/update.htm',
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