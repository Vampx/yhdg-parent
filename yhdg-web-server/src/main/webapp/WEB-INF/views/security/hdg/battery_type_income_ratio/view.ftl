<div class="tab_item">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">运营商：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" required="true" readonly
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
                               value="${(entity.agentId)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">电池类型：</td>
                    <td>
                        <input name="batteryType" readonly id="battery_type_${pid}" class="easyui-combotree" editable="false"
                               style="width: 184px; height: 28px;"
                               url="${contextPath}/security/basic/agent_battery_type/tree.htm?agentId=${entity.agentId}"
                               value="${(entity.batteryType)!''}">
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">租金周期（月）：</td>
                    <td><input type="text" class="text easyui-numberspinner" readonly data-options="min:0" maxlength="20" name="rentPeriodType" value="${(entity.rentPeriodType)!''}"
                               style="width: 184px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="80" align="right">每个周期收多少钱（元）：</td>
                    <td>
                        <input type="text" class="text easyui-numberspinner" data-options="min:0.00, precision:2" readonly maxlength="3" name="rentPeriodMoney" value="${(entity.rentPeriodMoney/100)!''}" style="width: 184px; height: 28px;"/>
                    </td>
                </tr>
                <tr>
                    <td width="150" align="right">租金周期过期时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" readonly editable="false" style="width:185px;height:28px " id="end_time_${pid}" name="rentExpireTime" value="${(entity.rentExpireTime?string('yyyy-MM-dd HH:mm:ss'))!''}"></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>