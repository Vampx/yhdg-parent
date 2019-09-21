<div class="popup_body clearfix">
    <ul class="tab_nav" style="height: 530px" id="tab_container_${pid}">
        <li <#if editFlag == 0>class="selected"</#if>url="${contextPath}/security/hdg/relief_station/edit_basic.htm?id=${entity.id}&pid=${pid}">基本信息</li>
        <li <#if editFlag == 1>class="selected"</#if>url="${contextPath}/security/hdg/relief_station/edit_new_location.htm?id=${entity.id}&pid=${pid}">地图定位</li>
    </ul>
    <div class="tab_con">
    <#if editFlag == 1>
        <div class="easyui-layout" style="width: 590px; height: 500px;">
            <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/hdg/relief_station/edit_new_location.htm?id=${entity.id}&pid=${pid}'">
            </div>
        </div>
    <#else>
        <div class="easyui-layout" style="width: 590px; height: 500px;">
            <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/hdg/relief_station/edit_basic.htm?id=${entity.id}&pid=${pid}'">
            </div>
        </div>
    </#if>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red shop_ok">确定</button>
    <button class="btn btn_border shop_close">关闭</button>
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

        win.find('.shop_ok').click(function() {
            var ok = win.data('ok')();
            if(ok) {
                win.window('close');
            }
        });
        win.find('.shop_close').click(function() {
            win.window('close');
        });
    })();
</script>


