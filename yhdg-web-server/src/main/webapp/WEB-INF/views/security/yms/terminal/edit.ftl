<div class="popup_body clearfix">
    <ul class="tab_nav" style="height: 480px;" id="tab_container_${pid}">
        <li class="selected" url="${contextPath}/security/yms/terminal/edit_basic_info.htm?id=${id}&pid=${pid}">基本信息</li>
        <li url="${contextPath}/security/yms/terminal/edit_terminal_property.htm?id=${id}&pid=${pid}">配置信息</li>
    </ul>
    <div class="tab_con" style="width:500px;">
        <div class="easyui-layout" style="width: 550px; height: 450px;">
            <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/yms/terminal/edit_basic_info.htm?id=${id}&pid=${pid}'">
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

        win.find('button.ok').click(function() {
            var ok = win.data('ok')();
            if(ok) {
                win.window('close');
            }
        });

        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>