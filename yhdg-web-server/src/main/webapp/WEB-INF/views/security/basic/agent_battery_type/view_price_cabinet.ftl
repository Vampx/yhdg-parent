<div class="state-list">
<#if priceCabinetList?? && (priceCabinetList?size>0) >
    <#list priceCabinetList as priceCabinet>
        <div class="price_cabinet">
            <input type="text" readonly value="<#if priceCabinet.cabinetId??>${(priceCabinet.cabinetName)!''}<#else></#if>"/>
            <i class="cabinet-close" price_cabinet_id="${(priceCabinet.cabinetId)!0}"  battery_type="${(priceCabinet.batteryType)!0}"></i>
        </div>
    </#list>
</#if>
</div>
