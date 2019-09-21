<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="templateId" value="${(templateId)!''}">
            <input type="hidden" name="alipayfwId" value="${(alipayfwId)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">id：</td>
                    <td><input type="text" class="text easyui-validatebox" name="id" required="true"  style="width: 172px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">序号：</td>
                    <td><input type="text" class="text easyui-numberspinner" name="orderNum" maxlength="3" required="true"
                               data-options="min:0, max:100" style="width: 184px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">名称：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="keywordName"  style="width: 172px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">内容：</td>
                    <td><textarea style="width:330px;height:100px;" name="keywordValue" maxlength="512"></textarea></td>
                </tr>
                <tr>
                    <td width="80" align="right">颜色：</td>
                    <td><input type="text" readonly value="#000000"style="background-color:#000000 " class="text easyui-validatebox" style="background-color:${(entity.color)!''} " name="color" required="required"  id="color_${pid}"/></td>
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

        win.find('#color_${pid}').colpick({
            onSubmit: function(hsb, color, rgb, target) {
                var jo = $(target);
                if(color) {
                    color = '#' + color;
                    jo.attr('value', color);
                    jo.css('background-color', color);
                } else {
                    jo.attr('value', '#000000');
                    jo.css('background-color', '#000000');
                }
                jo.colpickHide();
            }
        });

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/fw_push_message_template_detail/insert.htm',
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

    })()
</script>
