<div class="popup_body clearfix">
    <ul class="tab_nav" id="tab_container_${pid}">
        <li class="selected" url="${contextPath}/security/basic/agent_agent/edit_basic_info.htm?id=${id}&pid=${pid}&agentId=${agentId!}">基本信息</li>
        <li url="${contextPath}/security/basic/agent_agent/edit_payee.htm?id=${id}&pid=${pid}&agentId=${agentId!}">设置收款人</li>
        <li url="${contextPath}/security/basic/agent_agent/shop_clearing.htm?pid=${pid}&agentId=${id}">门店转账</li>
    </ul>
    <div class="tab_con">
        <div class="easyui-layout" style="width: 585px; height: 400px;">
            <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/basic/agent_agent/edit_basic_info.htm?id=${id}&pid=${pid}&agentId=${agentId!}'">
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


