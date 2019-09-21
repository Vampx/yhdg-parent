<div class="popup_body clearfix">
    <ul class="tab_nav" id="tab_container_${pid}">
        <li url="${contextPath}/security/zd/rent_period_activity/edit_basic.htm?id=${entity.id}&pid=${pid}">基本信息</li>
    </ul>
    <div class="tab_con">
        <div class="easyui-layout" style="width: 590px; height: 380px;">
            <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/zd/rent_period_activity/edit_basic.htm?id=${entity.id}&pid=${pid}'">
            </div>
        </div>
    </div>

</div>
<div class="popup_btn">
    <button class="btn btn_red activity_ok">确定</button>
    <button class="btn btn_border activity_close">关闭</button>
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

        win.find('.activity_ok').click(function() {
            var ok = win.data('ok')();
            if(ok) {
                win.window('close');
            }
        });
        win.find('.activity_close').click(function() {
            win.window('close');
        });
    })();
</script>


