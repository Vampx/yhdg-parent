<div class="delete_detail">
<div class="popup_body popup_body_full clearfix">
    <div class="playlist_wrap">
        <div class="playlist_nav" id="detail_container_${pid}">
            <ul class="playlist_select">
            <#list publishedPlaylistDetails as detail>
                <li detail_id="${(detail.id)!0}" <#if detail_index == 0>class="selected"</#if>
                    url="${contextPath}/security/yms/published_playlist/view_basic.htm?detailId=${(detail.id)!0}&pid=${pid}">${(detail.detailName)!''}
                </li>
            </#list>
            </ul>
        </div>
        <div class="playlist_con" id="detail_content_${pid}">
            <#include 'view_basic.ftl'>
        </div>
    </div>
</div>
<div class="popup_btn popup_btn_full">
    <button class="btn btn_blue ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
</div>
<script>
    (function(){
        var pid = '${pid}';
        var win = $('#${pid}');
        $('#detail_container_${pid}').delegate("li", "click", function(){
            var me = $(this);
            if (me.attr('class') && me.attr('class').indexOf('selected') >= 0) {
                return;
            }

            $('.selected').removeClass('selected');
            me.addClass('selected');

            var  detailId = $(this).attr('detail_id');
            $.post('${contextPath}/security/yms/published_playlist/view_basic.htm', {
                detailId: detailId,
                pid:pid
            }, function (html) {
                $('#detail_content_${pid}').html(html);
            }, 'html');
        });

        win.find('.close').click(function () {
            win.window('close');
        });

        //点击确定提交
        win.find('button.ok').click(function() {
            win.window('close');
        });

    })()

</script>