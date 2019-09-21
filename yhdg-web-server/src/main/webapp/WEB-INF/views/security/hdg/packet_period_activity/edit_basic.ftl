<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">运营商：</td>
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
                                       swich_battery_type_${pid}();
                                    }"
                              value="${(entity.agentId)!''}"/>
                    </td>
                    <td width="70" align="right">活动名称：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" maxlength="40" name="activityName" required="true" value="${(entity.activityName)!''}"/>
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
                    <td align="right">价格：</td>
                    <td><input type="text" class="text easyui-numberspinner" maxlength="5" id="price_${pid}"
                               data-options="min:0.01, precision:2" style="width: 184px; height: 28px;" value="${((entity.price)/100)!0}"/></td>
                </tr>
                <tr>
                    <td align="right">天数：</td>
                    <td><input type="text" class="text easyui-numberspinner" name="dayCount" id="day_count_${pid}" maxlength="3"
                               data-options="min:0, max:365" style="width: 184px; height: 28px;" value="${(entity.dayCount)!0}"/></td>
                    <td align="right">次数限制：</td>
                    <td><input class="easyui-numberspinner" data-options="min:0" value="${(entity.limitCount)!0}" name="limitCount" id="limit_count_${pid}" maxlength="5" style="width:184px;height: 28px;"></td>
                </tr>
                <tr>
                    <td align="right">每天次数限制：</td>
                    <td><input type="text" class="text easyui-numberspinner" name="dayLimitCount" id="day_limit_count_${pid}" maxlength="3"
                               data-options="min:0" style="width: 184px; height: 28px;" value="${(entity.dayLimitCount)!0}"/></td>
                </tr>
                <tr>
                    <td align="right">开始时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox" style="width:185px;height:28px " id="begin_time_${pid}"
                               value="<#if (entity.beginTime)?? >${app.format_date_time(entity.beginTime)}</#if>" required="true" name="beginTime" >
                    </td>
                    <td align="right">结束时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox"style="width:185px;height:28px " id="end_time_${pid}"
                               value="<#if (entity.endTime)?? >${app.format_date_time(entity.endTime)}</#if>" required="true" name="endTime">
                    </td>
                </tr>
            </table>
        </form>
    </div>
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

    (function () {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];

        var snapshot = $.toJSON({
            id: '${entity.id}',
            activityName: '${(entity.activityName)!''}',
            agentId: '${(entity.agentId)!''}',
            batteryType: '${(entity.batteryType)!''}',
            dayCount:'${(entity.dayCount)!''}',
            limitCount:'${(entity.limitCount)!''}',
            dayLimitCount:'${(entity.dayLimitCount)!''}',
            beginTime: '${app.format_date_time(entity.beginTime)}',
            endTime: '${app.format_date_time(entity.endTime)}'
        });

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

        var ok = function () {
            var price = $('#price_${pid}').numberspinner('getValue');
            if (price == '') {
                $.messager.alert('提示信息', "请填写价格", 'info');
                return false;
            }
            var dayCount = $('#day_count_${pid}').numberspinner('getValue');
            if (dayCount == '') {
                $.messager.alert('提示信息', "请填写天数", 'info');
                return false;
            }
            var beginTime = $('#begin_time_${pid}').datetimebox('getValue');
            var endTime = $('#end_time_${pid}').datetimebox('getValue');
            if(beginTime != '' && endTime != '' && beginTime > endTime) {
                $.messager.alert('提示信息', '结束时间必须大于开始时间', 'info');
                return;
            }
            if (!jform.form('validate')) {
                return false;
            }

            var success = true;
            var values = {
                id: '${entity.id}',
                activityName: form.activityName.value,
                batteryType: $('#battery_type_${pid}').combotree('getValue'),
                price: parseInt(Math.round(price * 100)),
                agentId: form.agentId.value,
                dayCount: form.dayCount.value,
                limitCount: form.limitCount.value,
                dayLimitCount: form.dayLimitCount.value,
                beginTime: $('#begin_time_${pid}').datetimebox('getValue'),
                endTime: $('#end_time_${pid}').datetimebox('getValue')
            };

            if (snapshot != $.toJSON(values)) {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/hdg/packet_period_activity/update.htm',
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