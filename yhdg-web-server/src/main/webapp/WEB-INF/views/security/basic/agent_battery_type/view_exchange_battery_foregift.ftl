<div class="deposit">
    <div class="deposit-list battery_foregift_create">
    <#if exchangeBatteryForegiftList?? && (exchangeBatteryForegiftList?size>0) >
        <#list exchangeBatteryForegiftList as exchangeBatteryForegift>
            <div <#if exchangeBatteryForegift_index == 0 >class="active battery_foregift"<#else>class="battery_foregift"</#if>
                 battery_foregift_id="${(exchangeBatteryForegift.id)!0}">
                <p>金额:<#if exchangeBatteryForegift.money??>${(exchangeBatteryForegift.money)/100}<#else>0</#if> 元</p>
            </div>
        </#list>
    </#if>
    </div>
</div>
<div class="deposit-tips" id="exchange_battery_foregift_memo">
<#include 'exchange_battery_foregift_memo.ftl'>
</div>
<script>

    var batteryType = $("#agent_battery_type_form").find('input[name=batteryType]').val();
    var agentId = $("#agent_battery_type_form").find('input[name=agentId]').val();

    $(function() {
        var foregiftId = $(".deposit-list").find(".active").attr("battery_foregift_id");
        if (foregiftId != '' && foregiftId != undefined) {
            //利用押金id去查找租金备注
            $.post('${contextPath}/security/basic/agent_battery_type/exchange_battery_foregift_memo.htm', {
                batteryForegiftId: foregiftId
            }, function (html) {
                $("#exchange_battery_foregift_memo").html(html);
            }, 'html');
        }
        if (foregiftId != '' && batteryType != '' && agentId != '') {
            //租金
            $.post('${contextPath}/security/basic/agent_battery_type/view_exchange_packet_period_price.htm', {
                foregiftId: foregiftId,
                batteryType: batteryType,
                agentId: agentId
            }, function (html) {
                $("#exchange_packet_period_price").html(html);
            }, 'html');
        }

    });

    //随点击切换 包时段套餐
    $('.battery_foregift').click(function () {
        $(this).addClass("active").siblings().removeClass("active");
        //获取押金id
        var batteryForegiftId = $(this).attr("battery_foregift_id");

        //利用押金id去查找租金备注
        $.post('${contextPath}/security/basic/agent_battery_type/exchange_battery_foregift_memo.htm', {
            batteryForegiftId: batteryForegiftId
        }, function (html) {
            $("#exchange_battery_foregift_memo").html(html);
        }, 'html');

        //利用押金id去查找租金
        $.post('${contextPath}/security/basic/agent_battery_type/view_exchange_packet_period_price.htm', {
            foregiftId: batteryForegiftId,
            batteryType: batteryType,
            agentId: agentId
        }, function (html) {
            $("#exchange_packet_period_price").html(html);
        }, 'html');

    })

</script>