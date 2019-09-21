<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" name="templateId" value="${(entity.templateId)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">序号：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true"  value="${(entity.orderNum)!''}" style="width: 172px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">名称：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" value="${(entity.keywordName)!''}" style="width: 172px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">内容：</td>
                    <td><textarea style="width:330px; height:100px;" name="keywordValue" readonly maxlength="512">${(entity.keywordValue)!''}</textarea></td>
                </tr>
                <tr>
                    <td width="80" align="right">颜色：</td>
                    <td><input type="text" style="background-color: ${(entity.color)!''}" class="text easyui-validatebox" readonly name="color" /></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button').click(function() {
            win.window('close');
        });
    })()
</script>