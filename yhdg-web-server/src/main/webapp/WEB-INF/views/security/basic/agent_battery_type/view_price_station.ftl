<div class="state-list">
<#if priceStationList?? && (priceStationList?size>0) >
    <#list priceStationList as priceStation>
        <div class="price_station">
            <input type="text" readonly value="<#if priceStation.stationId??>${(priceStation.stationName)!''}<#else></#if>"/>
            <i class="station-close" price_station_id="${(priceStation.stationId)!0}"  battery_type="${(priceStation.batteryType)!0}"></i>
        </div>
    </#list>
</#if>
</div>
