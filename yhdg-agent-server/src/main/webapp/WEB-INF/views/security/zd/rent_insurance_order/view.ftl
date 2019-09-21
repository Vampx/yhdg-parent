<div class="popup_body clearfix">
    <ul class="tab_nav" id="tab_container_${pid}">
        <li class="selected" url="${contextPath}/security/zd/rent_insurance_order/view_basic.htm?id=${(entity.id)!''}&pid=${pid}">基本信息</li>
    <#if entity.payType?? && entity.payType == 14>
        <li url="${contextPath}/security/zd/rent_insurance_order/rent_installment.htm?id=${(entity.id)!''}&pid=${pid}">分期信息</li>
    </#if>
        <li url="${contextPath}/security/zd/customer_refund_record/view.htm?sourceId=${(entity.id)!''}&sourceType=6&pid=${pid}">退款订单</li>
    </ul>
    <div class="tab_con">
        <div class="easyui-layout" style="width: 830px; height: 400px;">
            <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/zd/rent_insurance_order/view_basic.htm?id=${(entity.id)!''}&pid=${pid}'">
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
