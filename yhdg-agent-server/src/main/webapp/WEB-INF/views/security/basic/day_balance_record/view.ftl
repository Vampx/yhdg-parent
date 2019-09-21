<div class="popup_body clearfix" style="background: #f7f7f7;">
    <div class="ui_table" style="padding: 0 15px;">
        <table cellpadding="0" cellspacing="0" width="100%">
            <tr>
                <td>总收入：${(entity.money/100)!''}</td>
                <td>包月：${(entity.packetMoney/100)!''}</td>
                <td>换电：${(entity.exchangeMoney/100)!''}</td>
                <td>退款：${(entity.refundPacketMoney/100 + entity.refundExchangeMoney/100)!''}</td>
                <td>时间：${(entity.balanceDate)!''}</td>
            </tr>
        </table>
    </div>
    <div class="card-item" style="margin-top: 0;">
        <div class="card-item__head">
            <ul class="tabnav" id="tab_container_${pid}">
                <li class="active" style="font-size: 14px;"  url="${contextPath}/security/hdg/packet_period_order/select_packet_period_order.htm?agentId=${entity.agentId}&balanceDate=${entity.balanceDate}&pid=${pid}">包月收入(${(entity.packetMoney/100.00)!''})</li>
                <li style="font-size: 14px;" url="${contextPath}/security/hdg/battery_order/select_battery_order.htm?agentId=${entity.agentId}&balanceDate=${entity.balanceDate}&pid=${pid}">换电收入(${(entity.exchangeMoney/100.00)!''})</li>
                <li style="font-size: 14px;" url="${contextPath}/security/hdg/order_refund/select_order_refund.htm?agentId=${entity.agentId}&balanceDate=${entity.balanceDate}&pid=${pid}">退款支出(${(entity.refundPacketMoney/100 + entity.refundExchangeMoney)!''})</li>
            </ul>
        </div>



        <div class="easyui-layout" style="width: 780px; height: 380px;">
            <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/hdg/packet_period_order/select_packet_period_order.htm?agentId=${entity.agentId}&balanceDate=${entity.balanceDate}&pid=${pid}'">
            </div>
        </div>

    </div>

</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}', win = $('#' + pid);

        var list = $('#tab_container_${pid} li');
        list.click(function() {
            var me = $(this);
            if(me.attr('class') && me.attr('class').indexOf('active') >= 0) {
                return;
            }

                list.removeClass('active');
                me.addClass('active');
                $('#page_${pid}').panel('refresh', me.attr('url'));

        });

        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>