<td colspan="2">
    <div class="zj_list">
    <#if exchangeBatteryForegiftList?? && (exchangeBatteryForegiftList?size>0) >
        <#list exchangeBatteryForegiftList as exchangeBatteryForegift>
            <div <#if index == exchangeBatteryForegift_index >class="zj_item battery_foregift selected"<#else>class="zj_item battery_foregift"</#if>
                    battery_foregift_id="${(exchangeBatteryForegift.foregiftId)!0}"
                    battery_reduce_money="${(exchangeBatteryForegift.reduceMoney)!0}">
                <span class="icon">
                    <i class="fa fa-fw fa-edit" vip_index = ${exchangeBatteryForegift_index} vip_exchange_id="${(exchangeBatteryForegift.vipPriceId)!0}" vip_foregift_id="${(exchangeBatteryForegift.foregiftId)!0}"></i>
                </span>
                <p class="num">金额:<#if exchangeBatteryForegift.money??>${(exchangeBatteryForegift.money)/100}<#else>0</#if> 元</p>
                <p class="num">减免:<#if exchangeBatteryForegift.reduceMoney??>${(exchangeBatteryForegift.reduceMoney)/100}<#else>0</#if> 元</p>
            </div>
        </#list>
    </#if>
    </div>
</td>
<script>


    $(function() {
        var batteryForegiftId = $(".zj_list").find(".selected").attr("battery_foregift_id");
        $.post('${contextPath}/security/hdg/vip_packet_period_price/vip_exchange_packet_period_price.htm', {
            foregiftId: batteryForegiftId,
            batteryType: ${(batteryType)!0},
            agentId: ${(agentId)!0},
            priceId: ${(priceId)!0}
        }, function (html) {
            $("#exchange_packet_period_price").html(html);
        }, 'html');

        $(".zj_list .battery_foregift .fa-edit").click(function () {
            var index = $(this).attr("vip_index");
            var exchangeId = $(this).attr("vip_exchange_id");
            var foregiftId = $(this).attr("vip_foregift_id");
            App.dialog.show({
                css: 'width:400px;height:398px;overflow:visible;',
                title: '修改减免金额',
                href: "${contextPath}/security/hdg/vip_exchange_battery_foregift/vip_exchange_battery_foregift_edit.htm?exchangeId=" + exchangeId + "&priceId=" +${(priceId)!0} + "&foregiftId="+ foregiftId,
                windowData:{
                },
                event: {
                    onClose: function() {
                        $.post('${contextPath}/security/hdg/vip_exchange_battery_foregift/vip_exchange_battery_foregift.htm', {
                            index:index,
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
        $('.battery_foregift').click(function () {
            $(this).addClass("selected").siblings().removeClass("selected");
            //获取押金id
            var batteryForegiftId = $(this).attr("battery_foregift_id");
            //利用押金id去查找租金
            $.post('${contextPath}/security/hdg/vip_packet_period_price/vip_exchange_packet_period_price.htm', {
                foregiftId: batteryForegiftId,
                batteryType: ${(batteryType)!0},
                agentId: ${(agentId)!0},
                priceId: ${(priceId)!0}
            }, function (html) {
                $("#exchange_packet_period_price").html(html);
            }, 'html');
        });
    })
</script>
