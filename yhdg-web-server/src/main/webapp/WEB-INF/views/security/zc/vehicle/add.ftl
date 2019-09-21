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
                                        swich_vehicle_model();
                                    }"
                        >
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
                                }" />
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">车架号：</td>
                    <td><input id="vin_no" class="text easyui-validatebox" name="vinNo" style="height: 28px;width: 185px;" onkeyup="var reg = /^[\d\_a-zA-Z]+$/;if(!reg.test(this.value)) this.value='';" maxlength="100" required="true"></td>
                </tr>
                <tr>
                    <td width="90" align="right">是否上线：</td>
                    <td>
                        <select id="up_line_status" name="upLineStatus" style="width: 184px; height: 28px;">
                            <option value="1">上线</option>
                            <option value="0">下线</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">是否启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_1"
                                   checked value="1"/>启用
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_0"
                                   value="0" />禁用
                        </span>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">备注：</td>
                    <td><textarea id="memo" name="memo" style="width:405px;height:60px;" maxlength="450"></textarea></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn"  style="margin-right: 5px;">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>

    function swich_vehicle_model() {
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

        win.find('button.ok').click(function() {
            var modelId = $('#model_id_${pid}').combobox('getValue');
            if (modelId == '' || modelId == null){
                $.messager.alert("提示信息", '请选择车型');
            }else {
                form.form('submit', {
                    url: '${contextPath}/security/zc/vehicle/create.htm',
                    success: function(text) {
                        var json = $.evalJSON(text);
                        <@app.json_jump/>
                        if(json.success) {
                            $.messager.alert('提示信息', '操作成功', 'info');
                            win.window('close');
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                        }
                    }
                });
            }
        });
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>
