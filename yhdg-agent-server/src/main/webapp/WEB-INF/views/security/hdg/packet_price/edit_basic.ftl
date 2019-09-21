<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">套餐名称：</td>
                    <td><input class="text easyui-validatebox" name="priceName" value="${(entity.priceName)!''}" style="width:182px;height:28px " required="required" ></td>
                </tr>
            </table>
        </form>
    </div>
</div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];

        var snapshot = $.toJSON({
            id: '${(entity.id)!''}',
            agentId: '${(entity.agentId)!''}',
            priceName: "${(entity.priceName)!''}",
        });
        var ok = function() {
            if(!jform.form('validate')) {
                return false;
            }
            var success = true;

            var values = {
                id: '${(entity.id)!''}',
                agentId: '${(entity.agentId)!''}',
                priceName: form.priceName.value,
        };

            if(snapshot != $.toJSON(values)) {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/hdg/packet_price/update_basic.htm',
                    dataType: 'json',
                    data: values,
                    success: function (json) {
                    <@app.json_jump/>
                        if (json.success) {
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                            success = false;
                        }
                    }
                });
            }

            return success;
        };

        win.data('ok', ok);
    })();
</script>
