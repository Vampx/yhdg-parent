<div class="popup_body clearfix">
    <ul class="tab_nav" id="tab_container_${pid}" style="height: 450px;">
        <li <#if editFlag == 0>class="selected"</#if> url="${contextPath}/security/zd/rent_battery_type/edit_basic.htm?batteryType=${(entity.batteryType)!''}&agentId=${(entity.agentId)!''}&pid=${pid}">基本信息</li>
        <li <#if foregiftColourFlag?? && foregiftColourFlag == 0>class="disabled"</#if> url="${contextPath}/security/zd/rent_battery_type/edit_rent_period_price.htm?batteryType=${(entity.batteryType)!''}&agentId=${(entity.agentId)!''}&pid=${pid}">套餐信息</li>
        <li <#if insuranceColourFlag?? && insuranceColourFlag == 0>class="disabled"</#if> url="${contextPath}/security/zd/rent_battery_type/edit_rent_insurance.htm?batteryType=${(entity.batteryType)!''}&agentId=${(entity.agentId)!''}&pid=${pid}">保险</li>
    </ul>
    <div class="tab_con">
        <#if editFlag == 1>
            <div class="easyui-layout" style="width: 590px; height: 380px;">
                <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/zd/rent_battery_type/edit_rent_period_price.htm?batteryType=${(entity.batteryType)!''}&agentId=${(entity.agentId)!''}&pid=${pid}'">
                </div>
            </div>
        <#else>
            <div class="easyui-layout" style="width: 590px; height: 380px;">
                <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/zd/rent_battery_type/edit_basic.htm?batteryType=${(entity.batteryType)!''}&agentId=${(entity.agentId)!''}&pid=${pid}'">
                </div>
            </div>
        </#if>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<style>
    .popup_body .tab_nav li {
        height: 45px;
        line-height: 40px;
    }
</style>
<script>
    (function() {
        var win = $('#${pid}');
        var list = $('#tab_container_${pid} li');
        list.click(function() {
            var me = $(this);
            if(me.attr('class') && me.attr('class').indexOf('selected') >= 0) {
                return;
            }

            var ok = win.data('ok')();
            if(ok) {
                list.removeClass('selected');
                me.addClass('selected');
                $('#page_${pid}').panel('refresh', me.attr('url'));
            }
        });

        win.find('.ok').click(function() {
            var ok = win.data('ok')();
            if(ok) {
                win.window('close');
            }
        });
        win.find('.close').click(function() {
            win.window('close');
        });
    })();
</script>


