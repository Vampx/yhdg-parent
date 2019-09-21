<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" id="id_${pid}" value="${(entity.id)!''}">
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
                                        swich_model();
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
                                }" value="${(entity.modelId)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">车架号：</td>
                    <td><input id="vin_no" type="text" class="text easyui-validatebox" readonly name="vinNo" value="${entity.vinNo}" style="height: 28px;width: 185px;" onkeyup="var reg = /^[\d\_a-zA-Z]+$/;if(!reg.test(this.value)) this.value='';" maxlength="100" required="true"></td>
                </tr>
                <tr>
                    <td width="90" align="right">是否上线：</td>
                    <td>
                        <select id="up_line_status" name="upLineStatus" style="width: 184px; height: 28px;">
                            <option value="0"  <#if entity.upLineStatus == 0>selected</#if>>下线</option>
                            <option value="1" <#if entity.upLineStatus == 1>selected</#if>>上线</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">上线时间：</td>
                    <td>
                        <input class="easyui-datetimebox" type="text"style="width:150px;height:27px;" readonly value="${(entity.upLineTime?string('yyyy-MM-dd HH:mm:ss'))!''}">
                    </td>
                </tr>
                <tr>
                    <td align="right">是否启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_1"
                                   <#if entity.isActive == 1>checked</#if> value="1"/>启用
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_0"
                                   <#if entity.isActive == 0>checked</#if> value="0" />禁用
                        </span>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">备注：</td>
                    <td><textarea id="memo" name="memo" style="width:405px;height:60px;" maxlength="450">${(entity.memo)!''}</textarea></td>
                </tr>
                <tr>
                    <td align="right">锁车状态：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="lockStatus" id="lockStatus_0" disabled
                                   <#if entity.lockStatus == 0>checked</#if> value="0"/>开锁
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="lockStatus" id="lockStatus_1"  disabled
                                   <#if entity.lockStatus == 1>checked</#if> value="1" />锁车
                        </span>

                    </td>

                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red" onclick="lock(0)">远程开锁</button>
    <button class="btn btn_red" onclick="lock(1)">远程锁车</button>
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>

    function swich_model() {
        var agentId = $('#agent_id_${pid}').combotree('getValue');
        var model = $('#model_id_${pid}');
        model.combobox({
            url: "${contextPath}/security/zc/vehicle_model/list.htm?agentId=" + agentId + ""
        });
        model.combobox('reload');
        var data = model.combobox('getData');//获取所有下拉框数据
        model.combobox('select',data[0].value);
    }

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function () {
            var modelId = $('#model_id_${pid}').combobox('getValue');
            if (modelId == '' || modelId == null){
                $.messager.alert("提示信息", '请选择车型');
            }else {
                form.form('submit', {
                    url: '${contextPath}/security/zc/vehicle/update.htm',
                    success: function (text) {
                        var json = $.evalJSON(text);
                    <@app.json_jump/>
                        if (json.success) {
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

    function lock(lockSwitch) {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        var id = $('#id_${pid}').val();

        var values = {
            id: id,
            lockSwitch: lockSwitch
        };

        $.ajax({
            cache: false,
            async: false,
            type: 'POST',
            url: '${contextPath}/security/zc/vehicle/lock.htm',
            dataType: 'json',
            data: values,
            success: function (json) {
            <@app.json_jump/>
                if (json.success) {
                    $.messager.alert('提示信息', '操作成功', 'info');

                    win.window('close');
                } else {
                    $.messager.alert('提示信息', json.message, 'info');
                }
            }
        });
    }
</script>
