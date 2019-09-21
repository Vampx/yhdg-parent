<td colspan="2">
    <div class="zj_list">
    <#if packetPeriodPriceList?? && (packetPeriodPriceList?size>0) >
        <#list packetPeriodPriceList as exchangePeriod>
            <div <#if exchangePeriod.getId() == packetId>class="zj_item exchange_rent selected"<#else>class="zj_item exchange_rent"</#if>
                    exchange_rent_id="${(exchangePeriod.id)!0}"
                    exchange_rent_money="<#if exchangePeriod.price??>${(exchangePeriod.price)/100}<#else>0</#if>">
                <p class="num"><#if exchangePeriod.price??>${(exchangePeriod.price)/100}<#else>0</#if> 元/${(exchangePeriod.dayCount)!0} 天</p>
                <p class="text"></p>
            </div>
        </#list>
    </#if>
    </div>
</td>

<script>
    (function() {
        $('.exchange_rent').click(function () {
            $(this).addClass("selected").siblings().removeClass("selected");
            //获取租金id
            var exchangePeriodId = $(this).attr("exchange_rent_id");
            var exchangeRentMoney = $(this).attr("exchange_rent_money");
            $('#packet_id').val('');
            $('#packet_money').val('');
            $('#packet_id').val(exchangePeriodId);
            $('#packet_money').val(exchangeRentMoney);
            showRecentMoney();
        });
    })();
</script>