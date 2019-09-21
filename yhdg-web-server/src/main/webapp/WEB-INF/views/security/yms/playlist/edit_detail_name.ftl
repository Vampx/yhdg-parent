<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">明细名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="detailName" id="detail_name_${pid}" value="${(entity.detailName)!''}" required="required" /></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {

            var detailName=$('#detail_name_${pid}').val();
            var line = '<h3 class="detail_name_class" detail_name="DETAIL_NAME">'+ detailName +'<h3>';
            var html = line.replace(/DETAIL_NAME/,detailName);
            $('.detail_name_class').replaceWith(html);
            $('li.selected').replaceWith("<li class='selected' detail_id=${(entity.id)!0} url='${contextPath}/security/yms/playlist/detail.htm?detailId=${(entity.id)!0}&pid=${pid}'>"+detailName+'</li>');
            win.window('close');
        });
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>
