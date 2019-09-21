<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="id" value="${(entity.id)!''}">

            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">运营商：</td>
                    <td><input type="text" maxlength="40" class="text easyui-validatebox" readonly="readonly" name="agentName" value="${(entity.agentName)!''}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">设置名称：</td>
                    <td><input type="text" maxlength="40" class="text easyui-validatebox" name="settingName" value="${(entity.settingName)!''}" maxlength="40"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">优惠券金额：</td>
                    <td><input type="text" class="easyui-numberspinner" style="width: 184px; height: 28px;" id="ticket_money_${pid}"  data-options="precision:2,min:0.01" value="${(entity.ticketMoney/100)!''}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">优惠券天数：</td>
                    <td><input type="text" class="easyui-numberspinner" style="width: 184px; height: 28px;" name="ticketDayCount"  data-options="min:1" value="${(entity.ticketDayCount)!''}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">佣金方式：</td>
                    <td><input type="text" maxlength="40" class="text easyui-validatebox" readonly="readonly" name="mobile" value="${entity.incomeTypeName}"/></td>
                </tr>
                <#if entity.incomeType == 1>
                    <tr>
                        <td width="100" align="right">拉新收入：</td>
                        <td><input type="text" class="easyui-numberspinner" style="width: 184px; height: 28px;" id="laxin_money_${pid}"  data-options="precision:2,min:0.01" value="${(entity.laxinMoney/100)!''}"/></td>
                    </tr>
                <#elseif entity.incomeType == 2>
                    <tr>
                        <td width="100" align="right">按月收入：</td>
                        <td><input type="text" class="easyui-numberspinner" style="width: 184px; height: 28px;" required="true" id="packet_period_money_${pid}"  data-options="precision:2,min:0.00" value="${(entity.packetPeriodMoney/100)!''}"/></td>
                    </tr>
                    <tr>
                        <td width="100" align="right">租金收入时间：</td>
                        <td><input type="text" class="easyui-numberspinner" style="width: 184px; height: 28px;" required="true" name="packetPeriodMonth"  data-options="min:0" value="${(entity.ticketDayCount)!''}"/>月</td>
                    </tr>
                </#if>
                <tr>
                    <td width="100" align="right">再次拉新间隔：</td>
                    <td><input type="text" class="easyui-numberspinner" style="width: 184px; height: 28px;" required="true" id="interval_day_${pid}" name="intervalDay" data-options="min:30" value="${(entity.intervalDay)!''}"/>天</td>
                </tr>
                <tr>
                    <td align="right">是否启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_1" <#if entity.isActive?? && entity.isActive == 1>checked</#if> value="1"/><label for="is_active_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_0" <#if entity.isActive?? && entity.isActive == 1><#else>checked</#if> value="0"/><label for="is_active_0">禁用</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right">类型：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="type" id="type_normal" <#if entity.type?? && entity.type == 1>checked</#if> value="1"/><label for="type_normal">普通</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="type" id="type_register" <#if entity.type?? && entity.type == 2>checked</#if> value="2"/><label for="type_register">自注册</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td><textarea style="width:400px;" maxlength="256" name="memo">${(entity.memo)!''}</textarea></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn" style="margin-right: 0px;">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>
