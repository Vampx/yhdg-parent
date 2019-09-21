<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">公告名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="title" maxlength="40" value="${entity.title}"/></td>
                </tr>
                <tr>
                    <td align="right" width="50">公告类型：</td>
                    <td>
                        <select style="width:185px;" name="noticeType">
                            <option value="1">客户公告</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">公告内容：</td>
                    <td colspan="3"><textarea style="width:400px;height: 110px;" name="content" maxlength="200">${entity.content}</textarea></td>
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

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/agent_public_notice/update.htm',
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