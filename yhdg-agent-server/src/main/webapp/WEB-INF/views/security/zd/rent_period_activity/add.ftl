<div class="popup_body">
    <div class="ui_table">
        <form method="post">
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
                    <td width="70" align="right">活动名称：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" maxlength="40" name="activityName" required="true"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">电池类型：</td>
                    <td>
                        <select name="batteryType" id="battery_type_${pid}" class="easyui-combotree" editable="false" style="width: 184px; height: 28px;">
                    </td>
                    <td align="right">价格：</td>
                    <td><input class="easyui-numberspinner" id="price_${pid}" maxlength="5" style="width:184px;height: 28px;" data-options="min:0.01,precision:2"></td>
                </tr>
                <tr>
                    <td align="right">天数：</td>
                    <td><input type="text" class="text easyui-numberspinner" name="dayCount" id="day_count_${pid}" maxlength="3"
                               data-options="min:0, max:365" style="width: 184px; height: 28px;"/></td>
                    <td align="right">次数限制：</td>
                    <td><input class="easyui-numberspinner" data-options="min:0" name="limitCount" id="limit_count_${pid}" maxlength="5" style="width:184px;height: 28px;"></td>
                </tr>
                <tr>
                    <td align="right">每天次数限制：</td>
                    <td><input type="text" class="text easyui-numberspinner" name="dayLimitCount" id="day_limit_count_${pid}" maxlength="3"
                               data-options="min:0" style="width: 184px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">开始时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" editable="false" style="width:185px;height:28px " id="begin_time_${pid}" name="beginTime" required="true"></td>
                    <td align="right">结束时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" editable="false" style="width:185px;height:28px " id="end_time_${pid}" name="endTime" required="true"></td>
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
            url: "${contextPath}/security/zd/rent_battery_type/tree.htm?agentId=" + agentId + ""
        });
        batteryTypeCombotree.combotree('reload');
        batteryTypeCombotree.combotree('setValue', null);
    }

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                windowData = win.data('windowData'),
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

        win.find('button.ok').click(function() {
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
            form.form('submit', {
                url: '${contextPath}/security/zd/rent_period_activity/create.htm',
                onSubmit: function(param) {
                    param.price = parseInt(Math.round(price * 100));
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