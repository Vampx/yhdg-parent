<div class="popup_body" style="padding-left:50px;padding-top: 40px;font-size: 14px;">
    <div class="tab_item" style="display:block;">
        <div class="ui_table">
            <form method="post" action="javascript:void(0);">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td width="90" align="left">运营商：</td>
                        <td>
                            <input name="agentId" id="agentId" class="easyui-combotree" required="true" readonly value="${Session['SESSION_KEY_USER'].agentId}"
                                   editable="false" style="width: 184px; height: 28px;"
                                   data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                    }"
                            >
                        </td>
                    </tr>
                    <tr>
                        <td width="70" align="left">套餐名称：</td>
                        <td><input type="text" class="text easyui-validatebox" maxlength="40" name="settingName" required="true" style="width:170px;height: 30px "/></td>
                    </tr>
                    <tr>
                        <td width="90" align="left">车辆型号：</td>
                        <td>
                            <input name="modelId" id="model_id" class="easyui-combotree" editable="false" required="true"
                                   style="width: 185px; height: 28px;"
                                   data-options="url:'${contextPath}/security/zc/vehicle_model/tree.htm?agentId=${Session['SESSION_KEY_USER'].agentId}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto',
                                onClick: function(node) {
                                  swich_agent();
                                }
                            "/>
                        </td>
                    </tr>
                    <tr>
                        <td width="80" align="left">业务类型：</td>
                        <td>
                            <select class="easyui-combobox"  data-options="editable:false" name="category" id="category_${pid}" style="width:180px;height: 30px ">
                            <#list CategoryEnum as s>
                                <option value="${s.getValue()}">${s.getName()}</option>
                            </#list>
                            </select>
                        </td>
                    </tr>
                    <tr id="battery_count_${pid}">
                        <td width="70" align="left">电池数量：</td>
                        <td><input type="text" class="text easyui-validatebox number" maxlength="40" readonly name="batteryCount" id="battery_num_${pid}" style="width:170px;height: 30px "/></td>
                    </tr>
                    <tr>
                        <td align="left">是否启用：</td>
                        <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" checked
                                   value="1"/><label for="is_active_1">启用</label>
                        </span>
                            <span class="radio_box">
                            <input type="radio" class="radio" name="isActive"
                                   value="0"/><label for="is_active_0">禁用</label>
                        </span>
                        </td>
                    </tr>
                    <tr>
                        <td align="left">车辆配置：</td>
                        <td colspan="2"><textarea style="width:250px;height:60px;" maxlength="450" name="vehicleName"></textarea></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>

    function swich_agent() {
        var agentId = $('#agentId').combotree('getValue');
        find_hd_battery_count(agentId);
    }


    $('#category_${pid}').combobox({
        onSelect:function(){
            if($('#category_${pid}').combobox('getValue') == 3){
                $("#battery_count_${pid}").hide();
            } else {
                $("#battery_count_${pid}").show();
            }
            var agentId = $('#agentId').combotree('getValue');
            if ($('#category_${pid}').combobox('getValue') == 1) {
                find_hd_battery_count(agentId);
            } else if ($('#category_${pid}').combobox('getValue') == 2) {
                find_zd_battery_count(agentId);
            }
        }
    });

    function find_hd_battery_count(agentId) {
        $.ajax({
            type: 'POST',
            url: '${contextPath}/security/zc/price_setting/find_hd_battery_count.htm',
            dataType: 'json',
            data: {agentId: agentId},
            success: function (json) {
                if (json.success) {
                    var data = json.data;
                    $('#battery_num_${pid}').val(data);
                }
            }
        });
    };

    function find_zd_battery_count(agentId) {
        $.ajax({
            type: 'POST',
            url: '${contextPath}/security/zc/price_setting/find_zd_battery_count.htm',
            dataType: 'json',
            data: {agentId: agentId},
            success: function (json) {
                if (json.success) {
                    var data = json.data;
                    $('#battery_num_${pid}').val(data);
                }
            }
        });
    };

    $(function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form'),
                windowData = win.data('windowData');

        win.find('button.ok').click(function () {

            form.form('submit', {
                url: '${contextPath}/security/zc/price_setting/create.htm',
                success: function(text) {
                    var json = $.evalJSON(text);
                <@app.json_jump/>
                    if(json.success) {
                        windowData.ok(json.data);
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        win.find('button.close').click(function () {
            win.window('close');
        });

    })
</script>
