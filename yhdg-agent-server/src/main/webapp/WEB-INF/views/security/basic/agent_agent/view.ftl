<div class="popup_body clearfix">
    <ul class="tab_nav" id="tab_container_${pid}">
        <li class="selected" url="${contextPath}/security/basic/agent_agent/view_basic_info.htm?id=${id}&pid=${pid}">基本信息</li>
        <li url="${contextPath}/security/basic/agent_agent/view_in_out_money.htm?id=${id}&pid=${pid}">运营商流水信息</li>
    </ul>
    <div class="tab_con">
        <div class="easyui-layout" style="width: 585px; height: 400px;">
            <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/basic/agent_agent/view_basic_info.htm?id=${id}&pid=${pid}'">
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


