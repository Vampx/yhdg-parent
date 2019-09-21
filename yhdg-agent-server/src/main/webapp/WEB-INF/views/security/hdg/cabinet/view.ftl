<div class="popup_body clearfix">
    <ul class="tab_nav" style="height: 530px" id="tab_container_${pid}">
        <li class="selected" url="${contextPath}/security/hdg/cabinet/view_basic.htm?id=${entity.id}&pid=${pid}">基本信息</li>
        <#--<li url="${contextPath}/security/hdg/cabinet/view_new_location.htm?id=${entity.id}&pid=${pid}">地图定位</li>-->
        <li url="${contextPath}/security/hdg/cabinet_box/index.htm?cabinetId=${entity.id}&editFlag=0&pid=${pid}">箱体信息</li>
        <#--<li url="${contextPath}/security/hdg/cabinet/view_property.htm?id=${entity.id}&pid=${pid}">配置信息</li>-->
        <li url="${contextPath}/security/hdg/cabinet/view_online.htm?id=${entity.id}&pid=${pid}">收费方式</li>
        <li url="${contextPath}/security/hdg/cabinet/view_battery_type.htm?cabinetId=${entity.id}&agentId=${entity.agentId}&pid=${pid}">电池类型</li>
        <li url="${contextPath}/security/hdg/cabinet/view_ratio.htm?cabinetId=${entity.id}&agentId=${entity.agentId}&pid=${pid}">分成方式</li>
        <li url="${contextPath}/security/hdg/battery/battery_list.htm?cabinetId=${entity.id}&editFlag=0&pid=${pid}">电池列表</li>
        <li url="${contextPath}/security/hdg/cabinet_online_stats/index.htm?cabinetId=${entity.id}&pid=${pid}">在线统计</li>
        <#--<li url="${contextPath}/security/hdg/cabinet/view_online.htm?id=${entity.id}&pid=${pid}">上线信息</li>-->
        <li url="${contextPath}/security/hdg/cabinet/view_image.htm?id=${entity.id}&pid=${pid}">实景图片</li>
    </ul>
    <div class="tab_con">
        <div class="easyui-layout"  style="width: 690px; height: 500px;">
            <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/hdg/cabinet/view_basic.htm?id=${entity.id}&pid=${pid}'">
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


