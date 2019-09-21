<div class="popup_body clearfix">
    <ul class="tab_nav" style="height: 530px" id="tab_container_${pid}">
        <li class="selected" url="${contextPath}/security/hdg/battery_report/view_voltage_electricity.htm?batteryId=${batteryId}&pid=${pid}&formatDate=${formatDate}">电压/电流信息</li>
        <li  url="${contextPath}/security/hdg/battery_report/view_temp.htm?batteryId=${batteryId}&pid=${pid}&formatDate=${formatDate}">温度信息</li>
    </ul>
    <div class="tab_con">
        <div class="easyui-layout" style="width: 1000px; height: 500px;">
            <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/hdg/battery_report/view_voltage_electricity.htm?batteryId=${batteryId}&pid=${pid}&formatDate=${formatDate}'">
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


