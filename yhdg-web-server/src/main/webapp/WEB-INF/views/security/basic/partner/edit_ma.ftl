<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="110" align="right">小程序名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="maAppName" value="${(entity.maAppName)!}"
                               style="width: 200px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">小程序Id：</td>
                    <td><input type="text" class="text easyui-validatebox" name="maAppId" value="${(entity.maAppId)!}"
                               style="width: 200px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">appSecret：</td>
                    <td><input type="text" class="text easyui-validatebox" name="maAppSecret" value="${(entity.maAppSecret)!}"
                               style="width: 400px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">小程序商户：</td>
                    <td><input type="text" class="text easyui-validatebox" name="maPartnerCode" value="${(entity.maPartnerCode)!}"
                               style="width: 400px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">小程序商户秘钥：</td>
                    <td><input type="text" class="text easyui-validatebox" name="maPartnerKey" value="${(entity.maPartnerKey)!}"
                               style="width: 400px; height: 28px;"/></td>
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
                maAppName: form.maAppName.value,
                maAppId: form.maAppId.value,
                maAppSecret: form.maAppSecret.value,
                maPartnerCode: form.maPartnerCode.value,
                maPartnerKey: form.maPartnerKey.value
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

