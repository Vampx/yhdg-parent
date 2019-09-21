<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form >
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">模板名称：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="templateName" value="${(entity.templateName)!''}" maxlength="40" readonly/></td>
                </tr>
                <tr>
                    <td width="70" align="right">标题：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" name="title" value="${(entity.title)!''}" maxlength="40" readonly/></td>
                </tr>
                <tr>
                    <td align="right">内容：</td>
                    <td colspan="3">
                        <textarea class="text easyui-validatebox" required="true" maxlength="160" name="content" style="width:435px; height: 32px;" readonly>${(entity.content)!''}</textarea>
                    </td>
                </tr>
                <tr>
                    <td align="right">变量：</td>
                    <td colspan="3">
                        <textarea class="text easyui-validatebox" required="true" maxlength="80" name="variable" style="width:435px; height: 32px;" readonly>${(entity.variable)!''}</textarea>
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">操作人：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="operatorName" value="${(entity.operatorName)!''}" maxlength="40"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">数量：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="customerCount" value="${(entity.customerCount)!''}" maxlength="40"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var ok = function() {
            var success = true;
            return success;
        }
        win.data('ok', ok);
    })();
</script>

