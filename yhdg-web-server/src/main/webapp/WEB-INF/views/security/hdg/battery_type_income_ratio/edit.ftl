<div class="tab_item">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" name="dividePersonId" value="${(entity.dividePersonId)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">运营商：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" readonly class="easyui-combotree" required="true"
                               editable="false" style="width: 184px; height: 28px;"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                      swich_battery_type_${pid}();
                                    }"
                               value="${(entity.agentId)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">电池类型：</td>
                    <td>
                        <input name="batteryType" id="battery_type_${pid}" class="easyui-combotree" editable="false"
                               style="width: 184px; height: 28px;"
                               url="${contextPath}/security/basic/agent_battery_type/tree.htm?agentId=${entity.agentId}"
                               value="${(entity.batteryType)!''}">
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">租金周期（月）：</td>
                    <td><input type="text" class="text easyui-numberspinner" data-options="min:0" maxlength="20" name="rentPeriodType" value="${(entity.rentPeriodType)!''}"
                               style="width: 184px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="80" align="right">每个周期收多少钱（元）：</td>
                    <td>
                        <input type="text" class="text easyui-numberspinner" data-options="min:0.00, precision:2" maxlength="10"  id="rent_period_money_${pid}" value="${(entity.rentPeriodMoney/100)!''}"  style="width: 184px; height: 28px;"/>
                    </td>
                </tr>
                <tr>
                    <td width="150" align="right">租金周期过期时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" editable="false" style="width:185px;height:28px " id="end_time_${pid}" name="rentExpireTime" value="${(entity.rentExpireTime?string('yyyy-MM-dd HH:mm:ss'))!''}"></td>
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

        $("#end_time_${pid}").datetimebox().datetimebox('calendar').calendar({
            validator: function(date){
                var now = new Date();
                var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                return d1<=date;
            }
        });

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/hdg/battery_type_income_ratio/update.htm',
                onSubmit: function (param) {
                    var rentPeriodMoney = $('#rent_period_money_${pid}').numberspinner('getValue');
                    param.rentPeriodMoney = parseInt(Math.round(rentPeriodMoney * 100));
                    return true;
                },
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
