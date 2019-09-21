<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">手机：</td>
                    <td><input type="text" maxlength="11" class="text easyui-validatebox" readonly name="mobile" value="${(entity.mobile)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">评分：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly name="grade" value="<#if (entity.grade)?? >${entity.grade?string("#.0")}</#if>"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">创建时间：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly name="createTime" value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>" /></td>
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
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>