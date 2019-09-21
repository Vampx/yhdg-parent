<div class="step_item step_item_1" style="display:block;">
    <form>
        <div class="ui_table" style="height:100%; width:100%;">
            <table cellpadding="0" cellspacing="0" height="100%" width="100%">
                <tr>
                    <td align="right">运营商：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" required="true"  style="width: 184px; height: 28px;"
                               data-options="
                                       url:'${contextPath}/security/basic/agent/tree.htm'
                                       " value="">
                    </td>
                </tr>
                <tr>
                    <td width="170" align="right">策略名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="strategyName" required="true" style="width:300px;" /></td>
                </tr>
            </table>
        </div>
    </form>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var container = win.find('.step_item_1');
        var form = win.find('form');
        var showValue = function() {
            var values = win.data('values');
            container.find('input[name=strategyName]').val(values.strategyName);
            container.find('input[name=agentId]').val(values.agentId);
        }
        var collectValue = function() {
            var values = win.data('values');
            values.strategyName = container.find('input[name=strategyName]').val();
            values.agentId = container.find('input[name=agentId]').val();
            return form.form('validate');
        }

        win.data('showValue1', showValue);
        win.data('collectValue1', collectValue);
    })();
</script>