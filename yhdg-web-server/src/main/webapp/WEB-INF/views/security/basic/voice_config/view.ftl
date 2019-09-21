<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" name="isActive" value="${(entity.isActive)!''}" id="is_active_${pid}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">短信接口：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="configName" value="${(entity.configName)!''}"/></td>
                </tr>
                <tr>
                    <td width="80" align="right">账号：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly name="account" value="${(entity.account)!''}"/></td>
                </tr>
                <tr>
                    <td width="80" align="right">密码：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly name="password" value="${(entity.password)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">是否启用：</td>
                    <td><input type="checkbox" class="checkbox" disabled name="active" <#if entity.isActive?? && entity.isActive == 1>checked</#if> value="1" onclick="$('#is_active_${pid}').val(this.checked ? 1 : 0)"></td>
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
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>
