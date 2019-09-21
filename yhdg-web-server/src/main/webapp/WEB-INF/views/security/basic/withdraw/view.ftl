<div class="popup_body clearfix">
    <ul class="tab_nav" style="height: 530px" id="tab_container_${pid}">
        <li url="${contextPath}/security/basic/withdraw/view_basic.htm?id=${entity.id}&pid=${pid}" class="selected">基本信息</li>
        <li url="${contextPath}/security/basic/withdraw/view_transfer_log.htm?id=${entity.id}&pid=${pid}">转账日志</li>
        <#if entity.type==1>
            <li url="${contextPath}/security/basic/customer_in_out_money/index.htm?customerId=${entity.customerId!}&pid=${pid}">客户流水信息</li>
        </#if>
        <#if entity.type==2>
            <li url="${contextPath}/security/basic/agent_in_out_money/index.htm?agentId=${entity.agentId!}&pid=${pid}">运营商流水信息</li>
        </#if>
        <#if entity.type==3>
            <li url="${contextPath}/security/basic/shop_in_out_money/index.htm?shopId=${entity.shopId!}&pid=${pid}">门店流水信息</li>
        </#if>
        <#if entity.type==6>
            <li url="${contextPath}/security/basic/agent_company_in_out_money/index.htm?agentCompanyId=${(entity.agentCompanyId)!''}&pid=${pid}">运营公司流水信息</li>
        </#if>
        <#if entity.type==4>
            <li url="${contextPath}/security/basic/partner/view_in_out_money.htm?id=${entity.partnerId!}&pid=${pid}">商户流水信息</li>
        </#if>
        <#if entity.type==5>
            <li url="${contextPath}/security/basic/estate_in_out_money/index.htm?estateId=${entity.estateId!}&pid=${pid}">物业流水信息</li>
        </#if>
    </ul>
    <div class="tab_con">
        <div class="easyui-layout"  style="width: 690px; height: 500px;">
            <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/basic/withdraw/view_basic.htm?id=${entity.id}&pid=${pid}'">
            </div>
        </div>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var list = $('#tab_container_${pid} li');
        list.click(function() {
            var me = $(this);
            if(me.attr('class') && me.attr('class').indexOf('selected') >= 0) {
                return;
            }

            list.removeClass('selected');
            me.addClass('selected');
            $('#page_${pid}').panel('refresh', me.attr('url'));
        });

        win.find('.close').click(function() {
            win.window('close');
        });
    })();
</script>
