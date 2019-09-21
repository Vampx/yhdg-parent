<div class="popup_body clearfix">
    <ul class="tab_nav" id="tab_container_${pid}">
        <li class="selected" url="${contextPath}/security/basic/customer/view_basic_info.htm?id=${id}&pid=${pid}">基本信息</li><#--基本信息-->
        <li url="${contextPath}/security/basic/customer/view_id_card.htm?id=${id}&pid=${pid}">实名照片</li>
        <li url="${contextPath}/security/basic/customer_in_out_money/index.htm?customerId=${id}&pid=${pid}">账户历史</li>
        <li url="${contextPath}/security/basic/customer_foregift_order/view_foregift_order.htm?customerId=${id}&pid=${pid}">会员押金记录</li>
        <li url="${contextPath}/security/basic/customer_coupon_ticket/view_coupon_ticket.htm?customerId=${id}&pid=${pid}">优惠券</li>
        <li url="${contextPath}/security/hdg/battery_order/view_battery_order.htm?customerId=${id}&pid=${pid}">换电记录</li>
    </ul>
    <div class="tab_con">
        <div class="easyui-layout" style="width: 680px; height: 400px;">
            <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/basic/customer/view_basic_info.htm?id=${id}&pid=${pid}'">
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


