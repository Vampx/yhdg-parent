<#if packetPeriodPriceList?? && (packetPeriodPriceList?size>0) >
    <#list packetPeriodPriceList as exchange>
    <div class="setbox">
        <div class="set-1">
            <span class="font1 weight">首次</span>
            <span class="font1 margin-t-9">
                <b>租金：<#if exchange.price??>${(exchange.price/100)?string('0.00')}<#else>0</#if>元/${(exchange.dayCount)!0}天</b>
                <b style="margin-left: 18px;">备注：${(exchange.memo)!''}</b>
            </span>
        </div>
        <#if exchange.renewList?? && (exchange.renewList?size>0) >
            <#list exchange.renewList as renew>
                <div class="set-2">
                    <span class="font1 weight">续租</span>
                    <span class="font1 margin-t-9">
                        资费：${(renew.price/100)?string('0.00')}元/${(renew.dayCount)!0} 天
                        <#if renew.isTicket?? && renew.isTicket == 1>可以使用优惠券<#else>不可使用优惠券</#if>
                    </span>
                    <span class="font1 margin-t-9">
                        限制总次数：${(renew.limitCount)!0}
                        限制每日换电次数：${(renew.dayLimitCount)!0}
                    </span>
                    <span class="font1 margin-t-9">
                        备注：${(renew.memo)!''}
                    </span>
                </div>
            </#list>
        </#if>
    </div>
    </#list>
</#if>