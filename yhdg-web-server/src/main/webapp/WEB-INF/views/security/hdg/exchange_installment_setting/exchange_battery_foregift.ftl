<td colspan="2">
    <div class="zj_list">
    <#if exchangeBatteryForegiftList?? && (exchangeBatteryForegiftList?size>0) >
        <#list exchangeBatteryForegiftList as exchangeBatteryForegift>
            <div  <#if exchangeBatteryForegift_index == 0>class="zj_item battery_foregift selected"<#else>class="zj_item battery_foregift" </#if>
                    battery_foregift_id="${(exchangeBatteryForegift.id)!0}"
                    battery_foregift_money="<#if exchangeBatteryForegift.money??>${(exchangeBatteryForegift.money)/100}<#else>0</#if>">
                <p class="num"><#if exchangeBatteryForegift.money??>${(exchangeBatteryForegift.money)/100}<#else>
                    0</#if> 元</p>
            </div>
        </#list>
    </#if>
    </div>
</td>

<script>
    (function() {

        //随点击切换 包时段套餐
        $('.battery_foregift').click(function () {
            $(this).addClass("selected").siblings().removeClass("selected");
            //获取押金id
            var batteryForegiftId = $(this).attr("battery_foregift_id");
            var batteryForegiftMoney = $(this).attr("battery_foregift_money");
            $('#foregift_id').val('');
            $('#foregift_money').val('');
            $('#foregift_id').val(batteryForegiftId);
            $('#foregift_money').val(batteryForegiftMoney);

            //更新租金信息
            $('#packet_id').val('');
            $('#packet_money').val('');
            showRecentMoney();
            var agentId = $('#page_agent_id').combotree('getValue');
            var batteryType = $('#page_battery_type').combotree('getValue');
            if(agentId != null && agentId != '' && batteryType != null && batteryType != '') {
                $.post("${contextPath}/security/hdg/exchange_installment_setting/find_packet_info.htm?foregiftId=" + batteryForegiftId + "&batteryType=" + batteryType +"&agentId=" + agentId, function (json) {
                    if (json.success) {
                        var data = json.data;
                        var periodMoney = data.price;
                        var periodMoneyNum = new Number(periodMoney / 100).toFixed(2);
                        $('#packet_id').val(data.id);
                        $('#packet_money').val(periodMoneyNum);
                        showRecentMoney();
                    }
                }, 'json');

                //利用押金id去查找租金
                $.post('${contextPath}/security/hdg/exchange_installment_setting/exchange_packet_period_price.htm', {
                    foregiftId: batteryForegiftId,
                    batteryType: batteryType,
                    agentId: agentId
                }, function (html) {
                    $("#exchange_packet_period_price").html(html);
                }, 'html');
            }
        });

    })();



</script>