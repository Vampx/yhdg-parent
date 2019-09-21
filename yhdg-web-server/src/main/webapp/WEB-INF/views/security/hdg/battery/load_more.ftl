<#if batteryOperateLogList??>
    <#list batteryOperateLogList as bol>
    <li OPER_ID="${bol.id}">
        <div class="txt">
            <span>${app.format_date_time(bol.createTime)}</span>
        </div>
        <div class="txt">
            <span>类型：${(bol.operateTypeName)!''}</span>
            <#if bol.customerId??>
                <span>客户：${(bol.customerMobile)!''}</span>
            </#if>
            <#if bol.cabinetId?? && bol.subcabinetId??>
                <span>主柜：${(bol.cabinetName)!''}</span>
                <span>格口：${(bol.boxNum)!''}</span>
            </#if>
            <#if bol.keeperId??>
                <span>维护人员：${(bol.keeperMobile)!''}</span>
            </#if>
            <span>当前电量：${(bol.volume)!0}%</span>
        </div>
    </li>
    </#list>
</#if>