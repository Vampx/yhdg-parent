<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="batteryType" value="${(agentBatteryType.batteryType)!''}">
            <input type="hidden" id="price_${pid}" <#if entity ??><#if entity.timesPrice ??> value="${((entity.timesPrice)/100)!0}"<#else>value="0"</#if><#else >value="0"</#if>  >
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">运营商名称：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" editable="false"
                               <#if (agentBatteryType.agentId)??>disabled</#if> style="width: 184px; height: 28px;"
                               url="${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}"
                               value="${(agentBatteryType.agentId)!''}">
                    </td>
                </tr>
                <tr>
                    <td align="right">类型名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="typeName" readonly required="true" maxlength="40" value="${(agentBatteryType.typeName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">是否开启单次换电：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="activeSingleExchange" id="active_single_exchange_1" <#if entity ??><#if entity.activeSingleExchange?? && entity.activeSingleExchange == 1>checked</#if></#if>  value="1"/><label for="active_single_exchange_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="activeSingleExchange" id="active_single_exchange_0" <#if entity ??><#if entity.activeSingleExchange?? && entity.activeSingleExchange == 1><#else >checked</#if></#if> value="0"/><label for="active_single_exchange_0">禁用</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right">单次价格：</td>
                    <td><input class="easyui-numberspinner" id="times_price_${pid}" maxlength="5" <#if entity ??><#if entity.timesPrice ??> value="${((entity.timesPrice)/100)!0}"</#if> </#if>  style="width:184px;height: 28px;" data-options="min:0.00,precision:2"></td>
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


        var snapshot = $.toJSON({
            batteryType: '${(agentBatteryType.batteryType)!''}',
            agentId: '${(agentBatteryType.agentId)!''}',
            activeSingleExchange: '${(entity.activeSingleExchange)!''}',
            timesPrice: $('#price_${pid}').val()
        });

        var ok = function () {
            if (!jform.form('validate')) {
                return false;
            }

            var success = true;

            var values = {
                batteryType: '${(agentBatteryType.batteryType)!''}',
                agentId: '${(agentBatteryType.agentId)!''}',
                activeSingleExchange: form.activeSingleExchange.value,
                timesPrice: $('#times_price_${pid}').val()
            };

            if (snapshot != $.toJSON(values)) {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/hdg/exchange_price_time/update.htm',
                    dataType: 'json',
                    data: {
                        batteryType: '${(agentBatteryType.batteryType)!''}',
                        agentId: '${(agentBatteryType.agentId)!''}',
                        activeSingleExchange: form.activeSingleExchange.value,
                        timesPrice: $('#times_price_${pid}').val()*100
                    },
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
