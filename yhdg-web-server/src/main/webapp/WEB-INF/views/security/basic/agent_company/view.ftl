<div class="popup_body clearfix">
    <ul class="tab_nav" style="height: 560px" id="tab_container_${pid}">
        <li class="selected" url="${contextPath}/security/basic/agent_company/view_basic.htm?id=${(entity.id)!''}&pid=${pid}&agentId=${(entity.agentId)!''}">基本信息</li>
        <li url="${contextPath}/security/basic/agent_company/view_payee.htm?id=${(entity.id)!''}&pid=${pid}&agentId=${(entity.agentId)!''}">设置收款人</li>
        <li url="${contextPath}/security/basic/agent_company/view_ratio.htm?id=${(entity.id)!''}&pid=${pid}&agentId=${(entity.agentId)!''}">分成方式</li>
        <li url="${contextPath}/security/basic/agent_company_customer/bind_customer.htm?agentCompanyId=${entity.id}&pid=${pid}&agentId=${(entity.agentId)!''}&editFlag=0">运营公司骑手</li>
    </ul>
    <div class="tab_con" style="margin:0">
        <div class="easyui-layout"  style="width: 925px; height: 530px;">
            <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/basic/agent_company/view_basic.htm?id=${(entity.id)!''}&pid=${pid}&agentId=${(entity.agentId)!''}'">
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


