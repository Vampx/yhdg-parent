<#assign items = [
{'url': '${contextPath}/security/yms/terminal/view_basic_info.htm?id=${id}&pid=${pid}', 'title': '终端信息'},
{'url': '${contextPath}/security/yms/terminal/edit_terminal_property.htm?id=${id}&pid=${pid}', 'title': '配置信息'}
]>
<#if item??>
    <#assign showItem=item>
<#else>
    <#assign showItem=0>
</#if>
<div class="popup_body clearfix">
    <ul class="tab_nav" style="height: 400px;" id="tab_container_${pid}">
    <#list items as item>
        <li <#if showItem=item_index>class="selected"</#if> url="${item.url}">${item.title}</li>
    </#list>
    </ul>
    <div class="tab_con" style="width: 420px;">
        <div class="easyui-layout" style="width: 550px; height: 380px;">
            <div id="page_${pid}" data-options="region:'center', border:false, href:'${items[showItem].url}'">
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




