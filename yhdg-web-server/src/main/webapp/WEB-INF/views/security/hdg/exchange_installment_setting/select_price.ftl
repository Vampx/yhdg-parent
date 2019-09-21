<div class="popup_body" style="min-height: 82%;">
    <div class="ui_table">
        <form>
            <input type="hidden" id="exchange_foregift_id" value="${(exchangeBatteryForegift.id)!''}">
            <input type="hidden" id="exchange_foregift_money" value="${(exchangeBatteryForegift.money/100)!0}">
            <input type="hidden" id="exchange_period_id" value="${(packetPeriodPrice.id)!''}">
            <input type="hidden" id="exchange_period_money" value="${(packetPeriodPrice.price/100)!0}">
            <input type="hidden" id="exchange_insurance_id" value="${(insurance.id)!''}">
            <input type="hidden" id="exchange_insurance_money" value="${(insurance.price/100)!0}">
        <#if exchangeBatteryForegiftList?? && (exchangeBatteryForegiftList?size>0) >
            <#list exchangeBatteryForegiftList as exchangeBatteryForegift>
                <#if exchangeBatteryForegift_index == 0><input type="hidden" id="foregift_id_${pid}"
                                                               value="${exchangeBatteryForegift.id}"></#if>
            </#list>
        <#else>
            <input type="hidden" id="foregift_id_${pid}" value="0">
        </#if>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td colspan="2">电池押金：</td>
                </tr>
                <tr id="exchange_battery_foregift">
                <#include 'exchange_battery_foregift.ftl'>
                </tr>
                <tr>
                    <td colspan="2">电池租金：</td>
                </tr>
                <tr id="exchange_packet_period_price">
                <#include 'exchange_packet_period_price.ftl'>
                </tr>
                <tr>
                    <td colspan="2">电池保险：</td>
                </tr>
                <tr id="exchange_insurance">
                <#include 'exchange_insurance.ftl'>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn" style="height: 10%">
    <table >
        <tr>
            <td align="left" id="total_money" width="200px;" style="font-size: 12px;padding-left: 12px;">
                总金额：<span id="money_detail" style="color: #FF4012;"></span>元
            </td>
            <td width="150px;"></td>
            <td width="150px;">
                <button  class="btn btn_red price_ok">确定</button>
            </td>
            <td width="105px;">
                <button class="btn btn_border price_close">关闭</button>
            </td>
        </tr>
    </table>
</div>
<script>

    function showBeginMoney() {
        var exchangeForegiftMoney = parseInt(Math.round($('#exchange_foregift_money').val()*100));
        var exchangePeriodMoney = parseInt(Math.round($('#exchange_period_money').val()*100));
        var exchangeInsuranceMoney = parseInt(Math.round($('#exchange_insurance_money').val()*100));

        var totalMoney = Number((exchangeForegiftMoney + exchangePeriodMoney + exchangeInsuranceMoney)/100);
        $('#money_detail').html(totalMoney);
    }

    function showRecentMoney() {
        var exchangeForegiftMoney = parseInt(Math.round($('#exchange_foregift_money').val()*100));
        var exchangePeriodMoney = parseInt(Math.round($('#exchange_period_money').val()*100));
        var exchangeInsuranceMoney = parseInt(Math.round($('#exchange_insurance_money').val()*100));

        var totalMoney = Number((exchangeForegiftMoney + exchangePeriodMoney + exchangeInsuranceMoney)/100);
        $('#money_detail').html(totalMoney);
    }

    (function () {
        var win = $('#${pid}');
        var windowData = win.data('windowData');

        showBeginMoney();

        //押金
        $.post('${contextPath}/security/hdg/exchange_installment_setting/exchange_battery_foregift.htm', {
            batteryType: ${(batteryType)!''},
            agentId: ${(agentId)!''}
        }, function (html) {
            $("#exchange_battery_foregift").html(html);
        }, 'html');

        //换电包时段套餐
        $.post('${contextPath}/security/hdg/exchange_installment_setting/exchange_packet_period_price.htm', {
            foregiftId: $('#foregift_id_${pid}').val(),
            batteryType: ${(batteryType)!''},
            agentId: ${(agentId)!''}
        }, function (html) {
            $("#exchange_packet_period_price").html(html);
        }, 'html');

        //保险
        $.post('${contextPath}/security/hdg/exchange_installment_setting/exchange_insurance.htm', {
            batteryType: ${(batteryType)!''},
            agentId: ${(agentId)!''}
        }, function (html) {
            $("#exchange_insurance").html(html);
        }, 'html');

        win.find('button.price_ok').click(function () {
            var exchangeForegiftId = $('#exchange_foregift_id').val();
            var exchangePeriodId = $('#exchange_period_id').val();
            if(exchangeForegiftId == null || exchangeForegiftId== '' || exchangeForegiftId == 0) {
                $.messager.alert('提示信息', '请先配置押金', 'info');
                return;
            }
            if(exchangePeriodId == null || exchangePeriodId == '' || exchangePeriodId == 0) {
                $.messager.alert('提示信息', '请先配置租金', 'info');
                return;
            }
            var exchangeForegiftMoney = parseInt(Math.round($('#exchange_foregift_money').val()*100));
            var exchangePeriodMoney = parseInt(Math.round($('#exchange_period_money').val()*100));
            var exchangeInsuranceMoney = parseInt(Math.round($('#exchange_insurance_money').val()*100));

            var totalMoney = Number((exchangeForegiftMoney + exchangePeriodMoney + exchangeInsuranceMoney)/100);
            price = {
                foregiftMoney: exchangeForegiftMoney,
                foregiftId: $('#exchange_foregift_id').val(),
                packetMoney: exchangePeriodMoney,
                packetId: $('#exchange_period_id').val(),
                insuranceMoney: exchangeInsuranceMoney,
                insuranceId: $('#exchange_insurance_id').val(),
                totalMoney: totalMoney
            };
            windowData.ok({
                price: price
            });
            win.window('close');
        })

        win.find('button.price_close').click(function () {
            win.window('close');
        });

        var ok = function () {
            return true;
        }

        win.data('ok', ok);
    })();


</script>
