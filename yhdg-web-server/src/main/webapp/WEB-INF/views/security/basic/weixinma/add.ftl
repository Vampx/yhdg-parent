<div class="popup_body clearfix">
    <ul class="tab_nav" style="height: 530px" id="tab_container_${pid}">
        <li url="${contextPath}/security/basic/weixinma/edit_basic.htm?id=${(entity.id)!}&pid=${pid}" class="selected">基本信息</li>
        <li url="${contextPath}/security/basic/weixinma/edit_rotate_image.htm?id=${(entity.id)!}&pid=${pid}">轮播图上传</li>
    </ul>
    <div class="tab_con">
        <div class="easyui-layout"  style="width: 690px; height: 500px;">
            <div id="page_${pid}" data-options="region:'center', border:false, href:'${contextPath}/security/basic/weixinma/add_basic.htm?id=${(entity.id)!}&pid=${pid}'">
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
                $('#page_${pid}').panel('refresh', me.attr('url').replace("?id=&","?id="+win.data('entityId')+"&"));
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


