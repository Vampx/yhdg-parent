<div class="popup_body clearfix">
    <ul class="tab_nav" id="tab_container_${pid}" style="height: 530px;">
        <#--<li class="selected" url="${contextPath}/security/hdg/battery_parameter/parameter_basic.htm?id=${(id)!''}&pid=${pid}">电池信息</li>-->
        <li  class="selected" url="${contextPath}/security/hdg/battery_parameter/parameter_edit.htm?id=${(id)!''}&pid=${pid}">参数设置</li>
        <li url="${contextPath}/security/hdg/battery_parameter_log/index.htm?id=${(id)!''}&pid=${pid}">参数设置记录</li>
    </ul>
    <div class="tab_con">
        <div class="easyui-layout" style="width: 840px; height: 500px;">
            <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/hdg/battery_parameter/parameter_edit.htm?id=${(id)!''}&pid=${pid}'">
            </div>
        </div>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
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
