
<#if list?? && (list?size>0) >
<ul class="battery_state_list clearfix">
    <#list list as l>
        <#if l.monitorStatus ??>
            <#if l.isOnline == 0>
            <#--离线-->
            <li class="battery_item battery_item_default" onclick="queryBatteryChargeOrder('${(l.id)!''}')">
            <#elseif  l.monitorStatus == 1>
            <#--未使用-->
            <li class="battery_item battery_item_warning" onclick="queryBatteryChargeOrder('${(l.id)!''}')">
            <#elseif  l.monitorStatus == 2>
            <#--客户使用-->
            <li class="battery_item battery_item_danger" onclick="queryBatteryChargeOrder('${(l.id)!''}')">
            <#elseif  l.monitorStatus == 3>
            <#--充电中-->
            <li class="battery_item battery_item_warning" onclick="queryBatteryChargeOrder('${(l.id)!''}')">
            <#elseif  l.monitorStatus == 4>
            <#--充电完成-->
            <li class="battery_item battery_item_success" onclick="queryBatteryChargeOrder('${(l.id)!''}')">
            </#if>
            <div class="battery_item_box">
                <div class="battery_text">
                    <p>电池：${(l.id)!''}</p>
                    <p>电量：${(l.volume)!''}%</p>
                    <p>温度：${(l.temp)!''}</p>
                </div>
                <div class="battery_inner">
                    <span class="battery_bar" style="height: ${l.volume}%;"></span>
                    <span class="battery_text">${(l.volume)!''}%</span>
                </div>
            </div>
            <h3 class="battery_item_title">${(l.monitorStatusName)!''}</h3>
        </li>
        </#if>
    </#list>
</ul>
<#else>
<h1 align="center">电池记录为空</h1>
</#if>