<div class="popup_body clearfix">
    <ul class="tab_nav" id="tab_container_${pid}">
        <li class="selected" url="${contextPath}/security/hdg/battery/view_basic.htm?id=${(id)!''}&pid=${pid}">基本信息</li>
        <#--<li url="${contextPath}/security/hdg/battery_signal/index.htm?batteryId=${(id)!''}&pid=${pid}">设备信号记录</li>-->
        <#--<li url="${contextPath}/security/hdg/battery/view_voltage.htm?id=${(id)!''}&pid=${pid}">电池单体电压</li>-->
        <li url="${contextPath}/security/hdg/battery/view_status_info.htm?id=${(id)!''}&pid=${pid}">电池状态信息</li>
        <li url="${contextPath}/security/hdg/battery/view_heartbeat.htm?id=${(id)!''}&pid=${pid}">电池心跳时长</li>
        <#--<li url="${contextPath}/security/hdg/battery_charge_record/select_by_battery_info.htm?batteryId=${(id)!''}&pid=${pid}">电池充电记录</li>-->
    </ul>
    <div class="tab_con">
        <div class="easyui-layout" style="width: 635px; height: 385px;">
            <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/hdg/battery/view_basic.htm?id=${(id)!''}&pid=${pid}'">
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
