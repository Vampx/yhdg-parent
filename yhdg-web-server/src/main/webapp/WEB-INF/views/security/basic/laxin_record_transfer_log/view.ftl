<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">操作人：</td>
                    <td><input type="text" maxlength="20" readonly class="text" required="true" name="roleName" value="${(entity.operatorName)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">操作时间：</td>
                    <td><input type="text" maxlength="20" readonly class="text" required="true" name="roleName" value="<#if entity.createTime??>${app.format_date_time(entity.createTime)}</#if>"/></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">操作内容：</td>
                    <td><textarea style="width:330px;height:250px;" readonly>${(entity.content)!''}</textarea></td>
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