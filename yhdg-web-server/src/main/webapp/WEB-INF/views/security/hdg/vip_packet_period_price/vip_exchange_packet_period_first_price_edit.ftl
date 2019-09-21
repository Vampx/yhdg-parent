<div class="popup_body" style="height: 600px;">
    <div style="border-bottom: 2px solid #eaeaea; display: flex;height: 28px;align-items: center">
        <div style="width: 2px; background: blueviolet;margin-right: 2px; height: 15px;"></div>
        <p style="font-weight: bold;font-size: 13px">首次支付</p>
    </div>
    <form method="post" id="form_first">
        <div class="ui_table" style="margin-top: 10px;">
            <table cellpadding="0" cellspacing="0">
                <input type="hidden" name="id" value="${(entity.id)!0}">
                <input type="hidden" name="foregiftId" value="${(entity.foregiftId)!0}">
                <input type="hidden" name="agentId" value="${(entity.agentId)!0}">
                <input type="hidden" name="vipForegiftId" value="${(vipForegiftId)!0}">

                <td align="left">价格：</td>
                <td><input class="easyui-numberspinner" required="true" id="price" maxlength="5" value="${((entity.price)/100)!0}" style="width:184px;height: 28px;" data-options="min:0.01,precision:2"></td>
                <td align="right">限制次数：</td>
                <td><input type="text"  class="easyui-numberbox" name="limitCount" maxlength="4" value="${(entity.limitCount)!''}" style="width: 185px;height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="left">天数：</td>
                    <td><input type="text" class="text easyui-numberspinner" required="true" value="${(entity.dayCount)!0}" id="day_count" name="dayCount" maxlength="3"
                               data-options="min:0, max:365" style="width: 184px; height: 28px;"/></td>
                    <td align="right">每日限制次数：</td>
                    <td><input type="text"  class="easyui-numberbox" name="dayLimitCount" maxlength="4" value="${(entity.dayLimitCount)!''}" style="width: 185px;height: 28px;"/></td>
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
                    <td align="left" valign="top" style="padding-top:10px;">备注：</td>
                    <td colspan="3"><textarea style="width:500px; height: 60px;" name="memo" maxlength="200">${(entity.memo)!''}</textarea></td>
                </tr>
            </table>
        </div>
    </form>

    <div style="border-bottom: 2px solid #eaeaea; display: flex;height: 28px;align-items: center">
        <div style="width: 2px; background: blueviolet;margin-right: 2px; height: 15px;"></div>
        <p style="font-weight: bold;font-size: 13px">续租支付</p>
    </div>

    <div class="zj_list" style="margin-top: 10px;  width: 590px;" id="exchange_packet_period_second_price">
        <#include 'vip_exchange_packet_period_second_price.ftl'>
    </div>

</div>
<div class="popup_btn">
    <button class="btn btn_red first_edit_ok">确定</button>
    <button class="btn btn_border first_edit_close">关闭</button>
</div>
<script>

    $.post('${contextPath}/security/hdg/vip_packet_period_price/vip_exchange_packet_period_second_price.htm', {
        packetPriceId: ${(entity.id)!0},
        agentId: ${(agentId)!0},
        batteryType: ${(batteryType)!0},
        foregiftId: ${(foregiftId)!0}
    }, function (html) {
        $("#exchange_packet_period_second_price").html(html);
    }, 'html');

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form'),
                windowData = win.data('windowData');

        win.find('button.first_edit_ok').click(function() {
            var price = $('#price').numberspinner('getValue');
            if (price == '') {
                $.messager.alert('提示信息', "请填写价格", 'info');
                return false;
            }
            var dayCount = $('#day_count').numberspinner('getValue');
            if (dayCount == '') {
                $.messager.alert('提示信息', "请填写天数", 'info');
                return false;
            }

            form.form('submit', {
                url: '${contextPath}/security/hdg/vip_packet_period_price/vip_exchange_packet_period_price_update.htm',
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
                        windowData.ok({
                            vipForegiftId : ${(vipForegiftId)!0}
                        });
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        });
        win.find('button.first_edit_close').click(function() {
            win.window('close');
        });

    })()
</script>