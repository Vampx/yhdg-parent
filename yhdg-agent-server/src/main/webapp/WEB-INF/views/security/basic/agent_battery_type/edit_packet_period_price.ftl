<div class="popup_body">
    <div class="ui_table">
        <form>
        <#if exchangeBatteryForegiftList?? && (exchangeBatteryForegiftList?size>0) >
            <#list exchangeBatteryForegiftList as exchangeBatteryForegift>
                <#if exchangeBatteryForegift_index == 0><input type="hidden" id="foregift_id_${pid}"
                                                               value="${exchangeBatteryForegift.id}"></#if>
            </#list>
        <#else>
            <input type="hidden" id="foregift_id_${pid}" value="0">
        </#if>
            <input type="hidden" name="batteryType" value="${(entity.batteryType)!''}">
            <input type="hidden" name="agentId" value="${(entity.agentId)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">运营商名称：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" editable="false"
                               <#if (entity.agentId)??>disabled</#if> style="width: 184px; height: 28px;"
                               url="${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}"
                               value="${(entity.agentId)!''}">
                    </td>
                </tr>
                <tr>
                    <td align="right">类型名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="typeName" readonly required="true" maxlength="40" value="${(entity.typeName)!''}"/></td>
                </tr>
            </table>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td colspan="2">电池押金：</td>
                </tr>
                <tr id="exchange_battery_foregift">
                <#include '../../hdg/exchange_battery_foregift/exchange_battery_foregift.ftl'>
                </tr>
                <tr>
                    <td colspan="2">电池租金：</td>
                </tr>
                <tr id="exchange_packet_period_price">
                <#include '../../hdg/packet_period_price/exchange_packet_period_price.ftl'>
                </tr>
                <#--<tr>-->
                    <#--<td colspan="2">电池保险：</td>-->
                <#--</tr>-->
                <#--<tr id="exchange_insurance">-->
                <#--<#include '../../hdg/insurance/exchange_insurance.ftl'>-->
                <#--</tr>-->
            </table>
        </form>
    </div>
</div>
<script>

    (function () {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];

        //押金
        $.post('${contextPath}/security/hdg/exchange_battery_foregift/exchange_battery_foregift.htm', {
            batteryType: ${(entity.batteryType)!''},
            agentId: ${(entity.agentId)!''}
        }, function (html) {
            $("#exchange_battery_foregift").html(html);
        }, 'html');

        //换电包时段套餐
        $.post('${contextPath}/security/hdg/packet_period_price/exchange_packet_period_price.htm', {
            foregiftId: $('#foregift_id_${pid}').val(),
            batteryType: ${(entity.batteryType)!''},
            agentId: ${(entity.agentId)!''}
        }, function (html) {
            $("#exchange_packet_period_price").html(html);
        }, 'html');

        <#--//保险-->
        <#--$.post('${contextPath}/security/hdg/insurance/exchange_insurance.htm', {-->
            <#--batteryType: ${(entity.batteryType)!''},-->
            <#--agentId: ${(entity.agentId)!''}-->
        <#--}, function (html) {-->
            <#--$("#exchange_insurance").html(html);-->
        <#--}, 'html');-->


        var ok = function () {
            return true;
        }

        win.data('ok', ok);
    })();


</script>
