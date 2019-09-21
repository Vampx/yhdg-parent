<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="110" align="right">公众号名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="mpAppName" value="${(entity.mpAppName)!}"
                               style="width: 200px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">appId：</td>
                    <td><input type="text" class="text easyui-validatebox" name="mpAppId" value="${(entity.mpAppId)!}"
                               style="width: 200px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">appSecret：</td>
                    <td><input type="text" class="text easyui-validatebox" name="mpAppSecret" value="${(entity.mpAppSecret)!}"
                               style="width: 400px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">公众号商户：</td>
                    <td><input type="text" class="text easyui-validatebox" name="mpPartnerCode" value="${(entity.mpPartnerCode)!}"
                               style="width: 400px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">公众号商户秘钥：</td>
                    <td><input type="text" class="text easyui-validatebox" name="mpPartnerKey" value="${(entity.mpPartnerKey)!}"
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
                mpAppName: form.mpAppName.value,
                mpAppId: form.mpAppId.value,
                mpAppSecret: form.mpAppSecret.value,
                mpPartnerCode: form.mpPartnerCode.value,
                mpPartnerKey: form.mpPartnerKey.value
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

