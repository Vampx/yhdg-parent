<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">换电柜组名称：</td>
                    <td><input type="text" maxlength="40" class="text easyui-validatebox" required="true" name="name" value="${(entity.name)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">上级分类：</td>
                    <td>
                        <input name="parentId" class="easyui-combotree" editable="false" style="width: 184px; height: 28px;" url="${contextPath}/security/basic/customer_guide/parentTree.htm?dummy=${'无'?url}" value="${(entity.parentId)!''}" >
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
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/customer_guide/update.htm',
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
