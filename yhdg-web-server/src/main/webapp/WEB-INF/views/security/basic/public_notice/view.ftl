<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="80" align="right">公告名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="title" readonly value="${entity.title}"/></td>
                </tr>
                <tr>
                    <td align="right" width="50">公告类型：</td>
                    <td>
                        <select style="width:185px;" disabled name="noticeType">
                        <#list noticeTypeEnum as e>
                            <option value="${e.value!}" <#if (entity.noticeType == e.value)> selected="selected" </#if>>${e.getName()!}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">公告内容：</td>
                    <td colspan="3"><textarea style="width:400px; height: 110px;" name="content" readonly>${entity.content}</textarea></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>
