<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">

            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">策略：</td>
                    <td>
                        <select class="easyui-combobox" id="terminal_strategy_id_${pid}" name="strategyId"  style="width:185px;height: 28px " >
                        <#if strategyList??>
                            <#list strategyList as e>
                                <option value="${e.id}" <#if (entity.strategyId)?? && e.id == entity.strategyId>selected="selected"</#if>>${(e.strategyName)!''}</option>
                            </#list>
                        </#if>
                        </select>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];

        <#--var agent = $('#agent_id_${pid}');-->
        var strategy = $('#terminal_strategy_id_${pid}');

        var snapshot = $.toJSON({
            id: '${entity.id}',
            strategyId: '${(entity.strategyId)!''}'
        });

        var ok = function() {
            if(!jform.form('validate')) {
                return false;
            }
            var success = true;
            var values = {
                id: '${entity.id}',
                strategyId: strategy.combobox('getValue')
            };

            if(snapshot != $.toJSON(values)) {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/yms/terminal/update_basic_info.htm',
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