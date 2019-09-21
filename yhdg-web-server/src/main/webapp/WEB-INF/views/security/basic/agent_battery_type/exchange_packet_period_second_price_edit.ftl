<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!0}">
            <input type="hidden" name="foregiftId" value="${(entity.foregiftId)!0}">
            <input type="hidden" name="agentId" value="${(entity.agentId)!0}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">价格：</td>
                    <td><input type="text" class="text easyui-numberspinner" required="true" maxlength="5" id="price_${pid}"
                               data-options="min:0, precision:2" style="width: 184px; height: 28px;" value="${((entity.price)/100)!0}"/></td>
                </tr>
                <tr>
                    <td align="right">天数：</td>
                    <td><input type="text" class="text easyui-numberspinner" required="true" id="day_count_${pid}" name="dayCount" maxlength="3"
                               data-options="min:0, max:365" style="width: 184px; height: 28px;" value="${(entity.dayCount)!0}"/></td>
                </tr>
                <tr>
                    <td align="right">限制次数：</td>
                    <td><input type="text" class="easyui-numberbox" name="limitCount" id="limit_count_${pid}" style="width: 185px;height: 28px;"
                               maxlength="4" value="${(entity.limitCount)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">每日限制次数：</td>
                    <td><input type="text" class="easyui-numberbox" name="dayLimitCount" id="day_Limit_count_${pid}" style="width: 185px;height: 28px;"
                               maxlength="4" value="${(entity.dayLimitCount)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">使用优惠券：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isTicket" <#if entity.isTicket?? && entity.isTicket == 1>checked</#if>
                                   value="1"/><label for="is_ticket_1">可以使用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isTicket"  <#if entity.isTicket?? && entity.isTicket == 1><#else >checked</#if>
                                   value="0"/><label for="is_ticket_0">不可使用</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td colspan="3"><textarea style="width:300px; height: 110px;" name="memo" maxlength="200">${(entity.memo)!''}</textarea></td>
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

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

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

           // var lastPrice = parseInt(Math.round(${((entity.price)!0)/100} * 100));
          //  var nextPrice = parseInt(Math.round(price * 100));
            if(true) {
                form.form('submit', {
                    url: '${contextPath}/security/basic/agent_battery_type/exchange_packet_period_second_price_update.htm',
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
            } else {
               /* $.messager.confirm('提示信息', '修改租金价格会删除对应的分期设置，确认修改?', function(ok) {
                    if(ok) {
                        form.form('submit', {
                            url: '${contextPath}/security/basic/agent_battery_type/exchange_packet_period_price_update.htm',
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
                    }
                });*/
            }
        });
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>