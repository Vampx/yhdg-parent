<div class="deposit">
    <div class="deposit-list vip_battery_foregift_list">
    <#if exchangeBatteryForegiftList?? && (exchangeBatteryForegiftList?size>0) >
        <#list exchangeBatteryForegiftList as exchangeBatteryForegift>
            <div <#if index == exchangeBatteryForegift_index >class="active battery_foregift"<#else>class="battery_foregift"</#if>
                 battery_foregift_id="${(exchangeBatteryForegift.id)!0}">
                <p>金额:<#if exchangeBatteryForegift.money??>${(exchangeBatteryForegift.money)/100}<#else>0</#if> 元</p>
                <p class="battery_reduce">减免:<#if exchangeBatteryForegift.reduceMoney??>${(exchangeBatteryForegift.reduceMoney)/100}<#else></#if> 元</p>
                <div>
                    <img class="edit_foregift" vip_exchange_id="${(exchangeBatteryForegift.vipExchangeId)!0}"
                         vip_index = ${exchangeBatteryForegift_index} foregift_id="${(exchangeBatteryForegift.foregiftId)!0}"
                         src="${app.imagePath}/edit2.png" />
                </div>
            </div>
        </#list>
    <#else>

    </#if>
    </div>
</div>
<div class="deposit-tips">
   <#include 'vip_exchange_battery_foregift_memo.ftl'>
</div>
<script>


    $(function() {

        var batteryForegiftId = $(".deposit-list").find(".active").attr("battery_foregift_id");

        $.post('${contextPath}/security/hdg/vip_packet_period_price/vip_exchange_packet_period_price_edit.htm', {
            foregiftId: batteryForegiftId,
            batteryType: ${(batteryType)!0},
            agentId: ${(agentId)!0},
            priceId: ${(priceId)!0},
            vipForegiftId: ${(vipForegiftId)!0}
        }, function (html) {
            $("#exchange_packet_period_price").html(html);
        }, 'html');

        if (${(vipForegiftId)!0} != 0) {
            //利用押金id去查找押金备注
            $.post('${contextPath}/security/hdg/vip_exchange_battery_foregift/vip_exchange_battery_foregift_memo.htm', {
                vipExchangeId: ${(vipForegiftId)!0}
            }, function (html) {
                $(".deposit-tips").html(html);
            }, 'html');
        }


        $(".edit_foregift").click(function () {
            var index = $(this).attr("vip_index");
            var exchangeId = $(this).attr("vip_exchange_id");
            var foregiftId = $(this).attr("foregift_id");
            App.dialog.show({
                css: 'width:400px;height:335px;overflow:visible;',
                title: '修改减免金额',
                href: "${contextPath}/security/hdg/vip_exchange_battery_foregift/vip_exchange_battery_foregift_edit.htm?exchangeId=" + exchangeId + "&priceId=" +${(priceId)!0} + "&foregiftId="+ foregiftId + "&index=" + index,
                windowData:{
                },
                event: {
                    onClose: function() {
                        $.post('${contextPath}/security/hdg/vip_exchange_battery_foregift/vip_exchange_battery_foregift.htm', {
                            index: index,
                            batteryType: ${(batteryType)!0},
                            agentId: ${(agentId)!0},
                            priceId:${(priceId)!0}
                        }, function (html) {
                            $("#exchange_battery_foregift").html(html);
                        }, 'html');
                    }
                }
            });


        });

        //随点击切换 包时段套餐
        $('.vip_battery_foregift_list .battery_foregift').click(function () {
            $(this).addClass("active").siblings().removeClass("active");
            //获取押金id
            var batteryForegiftId = $(this).attr("battery_foregift_id");
            var vipExchangeId = $(this).find(".edit_foregift").attr("vip_exchange_id");

            //利用押金id去查找租金
            $.post('${contextPath}/security/hdg/vip_packet_period_price/vip_exchange_packet_period_price.htm', {
                foregiftId: batteryForegiftId,
                batteryType: ${(batteryType)!0},
                agentId: ${(agentId)!0},
                vipForegiftId: vipExchangeId
            }, function (html) {
                $("#exchange_packet_period_price").html(html);
            }, 'html');

            if (vipExchangeId != 0) {
                //利用押金id去查找押金备注
                $.post('${contextPath}/security/hdg/vip_exchange_battery_foregift/vip_exchange_battery_foregift_memo.htm', {
                    vipExchangeId: vipExchangeId
                }, function (html) {
                    $(".deposit-tips").html(html);
                }, 'html');
            } else {
                var html = '押金备注：';
                $(".deposit-tips").html(html);
            }

        });
    })

</script>
