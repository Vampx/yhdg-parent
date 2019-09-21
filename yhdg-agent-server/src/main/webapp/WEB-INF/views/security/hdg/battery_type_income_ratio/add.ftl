<div class="tab_item">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="dividePersonId">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">运营商：</td>
                    <td>
                        <input name="agentId" required="true" id="agent_id_${pid}" class="easyui-combotree" editable="false" style="width: 184px; height: 28px;"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto',
                                onClick: function(node) {
                                   swich_battery_type_${pid}();
                                }
                            ">
                    </td>
                    <td align="right">电池类型：</td>
                    <td>
                        <select name="batteryType" id="battery_type_${pid}" class="easyui-combotree" editable="false"
                                style="width: 184px; height: 28px;">
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">平台比例：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" maxlength="3" name="platformRatio" required/>
                    </td>
                    <td width="80" align="right">运营商比例：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" maxlength="3" name="agentRatio" required/>
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">省代比例：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" maxlength="3" name="provinceAgentRatio" required/>
                    </td>
                    <td width="80" align="right">市代比例：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" maxlength="3" name="cityAgentRatio" required/>
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">启用扣除金额：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="activePlatformDeduct" id="activePlatformDeduct_1" checked value="1"/><label for="activePlatformDeduct_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="activePlatformDeduct" id="activePlatformDeduct_0" value="0"/><label for="activePlatformDeduct_0">禁用</label>
                        </span>
                    </td>
                    <td width="80" align="right">运营商扣除：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" maxlength="3" name="platformDeductMoney" required/>
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">运营商扣除过期时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" editable="false" style="width:185px;height:28px " id="begin_time_${pid}" name="platformDeductExpireTime"></td>
                    <td width="80" align="right" >租金周期：</td>
                    <td>
                        <select style="width:185px;" name="rentPeriodType">
                        <#list rentPeriodTypeEnum as e>
                            <option value="${e.getValue()}">${e.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">每个周期收多少钱：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" maxlength="3" name="rentPeriodMoney"/>
                    </td>
                    <td width="80" align="right">租金周期过期时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" editable="false" style="width:185px;height:28px " id="end_time_${pid}" name="rentExpireTime"></td>
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
    function swich_battery_type_${pid}() {
        var agentId = $('#agent_id_${pid}').combotree('getValue');
        var batteryTypeCombotree = $('#battery_type_${pid}');
        batteryTypeCombotree.combotree({
            url: "${contextPath}/security/basic/agent_battery_type/tree.htm?agentId=" + agentId + ""
        });
        batteryTypeCombotree.combotree('reload');
        batteryTypeCombotree.combotree('setValue', null);
    }
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        $("#begin_time_${pid}").datetimebox().datetimebox('calendar').calendar({
            validator: function(date){
                var now = new Date();
                var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                return d1<=date;
            }
        });

        $("#end_time_${pid}").datetimebox().datetimebox('calendar').calendar({
            validator: function(date){
                var now = new Date();
                var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                return d1<=date;
            }
        });

        win.find('input[name=dividePersonName]').click(function() {
            var agentId = $('#agent_id_${pid}').combotree('getValue');
            if (agentId == '') {
                $.messager.alert('提示信息', '请选择运营商', 'info');
                return false;
            }
            selectDividePerson(agentId);
        });

        function selectDividePerson(agentId) {
            App.dialog.show({
                css: 'width:826px;height:522px;overflow:visible;',
                title: '选择受益人',
                href: "${contextPath}/security/hdg/income_divide_person/select_divide_person.htm?agentId=" + agentId,
                windowData: {
                    ok: function(config) {
                        win.find('input[name=dividePersonId]').val(config.dividePerson.id);
                        win.find('input[name=dividePersonName]').val(config.dividePerson.personName);
                    }
                },
                event: {
                    onClose: function() {
                    }
                }
            });
        }

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/hdg/battery_type_income_ratio/create.htm',
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
        });
        win.find('button.close').click(function() {
            win.window('close');
        });
    })()
</script>