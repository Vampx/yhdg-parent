<div class="popup_body clearfix">
    <ul class="tab_nav" id="tab_container_${pid}">
        <li class="selected" url="${contextPath}/security/batch_weixinmp_template_message/view_basic.htm?id=${(entity.id)!''}&pid=${pid}">基本信息</li>
        <li url="${contextPath}/security/batch_weixinmp_template_message_detail/index.htm?batchId=${(entity.id)!''}&pid=${pid}">批量发送详情</li>
    </ul>
    <div class="tab_con">
        <div class="easyui-layout" style="width: 585px; height: 400px;">
            <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/batch_weixinmp_template_message/view_basic.htm?id=${(entity.id)!''}&pid=${pid}'">
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

