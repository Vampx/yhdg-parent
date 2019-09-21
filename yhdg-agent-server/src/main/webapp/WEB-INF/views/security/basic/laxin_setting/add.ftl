<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="agentId" value="${Session['SESSION_KEY_USER'].agentId}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="100" align="right">设置名称：</td>
                    <td><input type="text" maxlength="40" class="text easyui-validatebox" required="true" name="settingName" maxlength="40"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">优惠券金额：</td>
                    <td><input type="text" class="easyui-numberspinner" style="width: 184px; height: 28px;" id="ticket_money_${pid}"  data-options="precision:2,min:1.00" value="1.00"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">优惠券天数：</td>
                    <td><input type="text" class="easyui-numberspinner" style="width: 184px; height: 28px;" name="ticketDayCount"  data-options="min:1" value="30"/></td>
                </tr>
                <tr>
                    <td align="right">佣金方式：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="incomeType" id="income_type_times" checked value="1"/><label for="income_type_times">按次</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="incomeType" id="income_type_month" value="2"/><label for="income_type_month">按月</label>
                        </span>
                    </td>
                </tr>
                <tr class="income_type_times">
                    <td width="100" align="right">按次收入：</td>
                    <td><input type="text" class="easyui-numberspinner" style="width: 184px; height: 28px;" id="laxin_money_${pid}"  data-options="precision:2,min:1.00" value="1.00"/></td>
                </tr>
                <tr class="income_type_month" style="display: none;">
                    <td width="100" align="right">按月收入：</td>
                    <td><input type="text" class="easyui-numberspinner" style="width: 184px; height: 28px;" required="true" id="packet_period_money_${pid}"  data-options="precision:2,min:1.00" value="1.00"/></td>
                </tr>
                <tr class="income_type_month" style="display: none;">
                    <td width="100" align="right">租金收入时间：</td>
                    <td><input type="text" class="easyui-numberspinner" style="width: 184px; height: 28px;" required="true" id="packet_period_month_${pid}" name="packetPeriodMonth"  data-options="min:1" value="1"/>月</td>
                </tr>
                <tr>
                    <td width="100" align="right">再次拉新间隔：</td>
                    <td><input type="text" class="easyui-numberspinner" style="width: 184px; height: 28px;" required="true" id="interval_day_${pid}" name="intervalDay" data-options="min:30" value="180"/>天</td>
                </tr>
                <tr>
                    <td align="right">是否启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_1" checked value="1"/><label for="is_active_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_0" value="0"/><label for="is_active_0">禁用</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right">类型：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="type" id="type_normal" checked value="1"/><label for="type_normal">普通</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="type" id="type_register" value="2"/><label for="type_register">自注册</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td><textarea style="width:400px;" maxlength="256" name="memo"></textarea></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn" style="margin-right: 0px;">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        $('#income_type_times').click(function() {
             $('.income_type_times').show();
            $('.income_type_month').hide();
        });
        $('#income_type_month').click(function() {
            $('.income_type_times').hide();
            $('.income_type_month').show();
        });

        win.find('button.ok').click(function() {
            form.form('submit', {
                url: '${contextPath}/security/basic/laxin_setting/create.htm',
                onSubmit: function(param) {
                    var isValid = $(this).form('validate');
                    if (!isValid) {
                        return false;
                    }


                    var laxinMoney = $('#laxin_money_${pid}').numberspinner('getValue');
                    var ticketMoney = $('#ticket_money_${pid}').numberspinner('getValue');
                    var packetPeriodMoney = $('#packet_period_money_${pid}').numberspinner('getValue');

                    param.laxinMoney = parseInt(Math.round(laxinMoney * 100));
                    param.ticketMoney = parseInt(Math.round(ticketMoney * 100));
                    param.packetPeriodMoney = parseInt(Math.round(packetPeriodMoney * 100));

                    if ($('#income_type_times').attr('checked')) {
                        if (param.laxinMoney == 0) {
                            $.messager.alert('提示信息', '按次收入必须大于0', 'info');
                            return false;
                        }
                    }

                    if ($('#income_type_month').attr('checked')) {
                        if (param.packetPeriodMoney == 0) {
                            $.messager.alert('提示信息', '按月收入必须大于0', 'info');
                            return false;
                        }
                        if ($('#packet_period_month_${pid}').numberspinner('getValue') == 0) {
                            $.messager.alert('提示信息', '按月收入过期时间必须大于0', 'info');
                            return false;
                        }
                    }

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
