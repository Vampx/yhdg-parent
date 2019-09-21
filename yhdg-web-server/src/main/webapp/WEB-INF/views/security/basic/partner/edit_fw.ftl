<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="110" align="right">生活号名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="fwAppName" value="${(entity.fwAppName)!}" style="width: 200px;"/></td>
                </tr>
                <tr>
                    <td align="right">appId：</td>
                    <td><input type="text" class="text easyui-validatebox" name="fwAppId" value="${(entity.fwAppId)!}" style="width: 200px;"/></td>
                </tr>
                <tr>
                    <td align="right">生活号公钥：</td>
                    <td><textarea type="text" class="text easyui-validatebox" name="fwPubKey"
                                  style="width: 560px;height: 80px;">${(entity.fwPubKey)!}</textarea></td>
                </tr>
                <tr>
                    <td align="right">生活号私钥：</td>
                    <td><textarea type="text" class="text easyui-validatebox" name="fwPriKey"
                                  style="width: 560px;height: 80px;">${(entity.fwPriKey)!}</textarea></td>
                </tr>
                <tr>
                    <td align="right">支付宝公钥：</td>
                    <td><textarea type="text" class="text easyui-validatebox" name="fwAliKey"
                                  style="width: 560px;height: 80px;">${(entity.fwAliKey)!}</textarea></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>
    (function () {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];

        var ok = function () {
            var success = true;
            var values = {
                id:form.id.value,
                fwAppName: form.fwAppName.value,
                fwAppId: form.fwAppId.value,
                fwPubKey: form.fwPubKey.value,
                fwPriKey: form.fwPriKey.value,
                fwAliKey: form.fwAliKey.value
            };

            $.ajax({
                cache: false,
                async: false,
                type: 'POST',
                url: '${contextPath}/security/basic/partner/update.htm',
                dataType: 'json',
                data: values,
                success: function (json) {
                    <@app.json_jump/>
                    if (json.success) {
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                        success = false;
                    }
                },
                error: function (text) {
                    $.messager.alert('提示信息', test, 'info');
                    success = false;
                }
            });
            return success;
        };

        win.data('ok', ok);
    })();
</script>

