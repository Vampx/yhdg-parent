<#if batteryTypeList?? && (batteryTypeList?size>0) >
<td colspan="2">
    <div class="zj_list">
        <#list batteryTypeList as batteryType>
            <div
                <#if oneBatteryType?? && oneBatteryType== 0 && batteryType_index = 0>
                        class="zj_item battery_foregift selected"
                <#else>
                    <#if oneBatteryType?? && oneBatteryType == batteryType.batteryType >
                        class="zj_item battery_foregift selected"
                    <#else>
                        class="zj_item battery_foregift"
                    </#if>
                </#if>
                        battery_type="${(batteryType.batteryType)!0}"
            <p class="num">型号:<#if batteryType.typeName??>${(batteryType.typeName)}<#else>0</#if></p>
            <p class="num">电压:<#if batteryType.typeName??>${(batteryType.ratedVoltage)/1000}V${(batteryType.ratedCapacity)/1000}AH<#else>0</#if></p>
        </div>
        </#list>
    </div>
</td>
<#else>
    无
</#if>
<script>


    $(function() {
        var batteryType = $(".zj_list").find(".selected").attr("battery_type");
        if (batteryType != undefined) {
            $.post('${contextPath}/security/zc/price_setting/setting_rent_price.htm', {
                batteryType: batteryType,
                agentId: ${(agentId)!0},
                priceSettingId: ${(priceSettingId)!0}
            }, function (html) {
                $("#setting_rent_price").html(html);
            }, 'html');
        }


        //随点击切换
        $('.battery_foregift').click(function () {
            $(this).addClass("selected").siblings().removeClass("selected");
            var batteryType = $(this).attr("battery_type");
            $.post('${contextPath}/security/zc/price_setting/setting_rent_price.htm', {
                batteryType: batteryType,
                agentId: ${(agentId)!0},
                priceSettingId: ${(priceSettingId)!0}
            }, function (html) {
                $("#setting_rent_price").html(html);
            }, 'html');

        });
    })
</script>
