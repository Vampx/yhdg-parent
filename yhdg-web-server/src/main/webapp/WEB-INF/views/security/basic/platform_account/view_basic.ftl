<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">ID：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly value="${(entity.id)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">商户名称：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${(entity.partnerName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">余额：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${(entity.balance/100)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">创建时间：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');

        var ok = function() {
            return true;
        };

        win.data('ok', ok);
    })();
</script>