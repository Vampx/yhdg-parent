<div class="popup_body">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="90" align="right">运营商：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" required="true"
                               editable="false" style="width: 184px; height: 28px;"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                        swich_agent();
                                    }" value="${(entity.agentId)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">车型：</td>
                    <td>
                        <input name="modelId" id="model_id_${pid}" style="height: 28px;width: 183px;" class="easyui-combobox"  editable="false" required="true"
                               data-options="url:'${contextPath}/security/zc/vehicle_model/list.htm',
                                method:'get',
                                valueField:'id',
                                textField:'modelName',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onSelect: function(node) {
                                }"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>

    function swich_agent() {
        var agentId = $('#agent_id_${pid}').combotree('getValue');
        var modelId = $('#model_id_${pid}');
        modelId.combobox({
            url: "${contextPath}/security/zc/vehicle_model/list.htm?agentId=" + agentId + ""
        });
        modelId.combobox('reload');
    }

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');
         var windowData = win.data('windowData');

        win.find('button.ok').click(function() {
            var vehicle = {
                agentId: $('#agent_id_${pid}').combotree('getValue'),
                modelId: $('#model_id_${pid}').combotree('getValue')
            };

            if(windowData.ok(vehicle)) {
                win.window('close');
            }
        });

        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>
