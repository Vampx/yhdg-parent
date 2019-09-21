<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="110" align="right">应用名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="alipayName" value="${(entity.alipayName)!}" style="width: 200px;"/></td>
                </tr>
                <tr>
                    <td align="right">appId：</td>
                    <td><input type="text" class="text easyui-validatebox" name="alipayAppId" value="${(entity.alipayAppId)!}" style="width: 200px;"/></td>
                </tr>
                <tr>
                    <td align="right">应用公钥：</td>
                    <td><textarea type="text" class="text easyui-validatebox" name="alipayPubKey"
                                  style="width: 560px;height: 80px;">${(entity.alipayPubKey)!}</textarea></td>
                </tr>
                <tr>
                    <td align="right">应用私钥：</td>
                    <td><textarea type="text" class="text easyui-validatebox" name="alipayPriKey"
                                  style="width: 560px;height: 80px;">${(entity.alipayPriKey)!}</textarea></td>
                </tr>
                <tr>
                    <td align="right">支付宝公钥：</td>
                    <td><textarea type="text" class="text easyui-validatebox" name="alipayAliKey"
                                  style="width: 560px;height: 80px;">${(entity.alipayAliKey)!}</textarea></td>
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
                alipayName: form.alipayName.value,
                alipayAppId: form.alipayAppId.value,
                alipayPubKey: form.alipayPubKey.value,
                alipayPriKey: form.alipayPriKey.value,
                alipayAliKey: form.alipayAliKey.value
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
