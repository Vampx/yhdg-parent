<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="110" align="right">商户名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="partnerName" value="${(entity.partnerName)!}" required="true"
                               style="width: 200px; height: 28px;"/></td>
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
                partnerName: form.partnerName.value
            };

            $.ajax({
                cache: false,
                async: false,
                type: 'POST',
                url: '${contextPath}/security/basic/partner/create.htm',
                dataType: 'json',
                data: values,
                success: function (json) {
                    <@app.json_jump/>
                    if (json.success) {
                        win.data('entityId', json.message);
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                        success = false;
                    }
                }
            });
            return success;
        };

        win.data('ok', ok);
    })();
</script>

