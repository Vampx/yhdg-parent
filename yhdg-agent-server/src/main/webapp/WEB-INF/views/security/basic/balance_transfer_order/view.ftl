<div class="popup_body clearfix">
    <ul class="tab_nav" style="height: 500px" id="tab_container_${pid}">
        <li class="selected" url="${contextPath}/security/basic/balance_transfer_order/view_basic.htm?id=${(entity.id)!''}&pid=${pid}">基本信息</li>
        <li url="${contextPath}/security/basic/balance_transfer_order/view_record.htm?id=${(entity.id)!''}&pid=${pid}">结算记录</li>
        <li url="${contextPath}/security/basic/balance_transfer_order/view_log.htm?id=${(entity.id)!''}&pid=${pid}">操作日志</li>
    </ul>
    <div class="tab_con">
        <div class="easyui-layout" style="width: 1000px; height: 480px;">
            <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/basic/balance_transfer_order/view_basic.htm?id=${(entity.id)!''}&pid=${pid}'">
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
            var ok = win.data('ok')();
            if(ok) {
                list.removeClass('selected');
                me.addClass('selected');
                $('#page_${pid}').panel('refresh', me.attr('url'));
            }
        });
        win.find('.close').click(function() {
            win.window('close');
        });
    })();
</script>
