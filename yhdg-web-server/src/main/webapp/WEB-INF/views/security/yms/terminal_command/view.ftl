<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">

            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">终端id：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="terminalId" value="${(entity.terminalId)!''}" /></td>
                    <td width="70" align="right">内容：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="content" value="${(entity.content)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">类型：</td>
                    <td>
                        <select class="easyui-combobox" required="true" name="type" style="width:182px;height:30px;" editable="false" disabled>
                        <#list TypeEnum as t>
                            <option value="${t.getValue()}" <#if entity.type?? && entity.type==t.getValue()>selected</#if> >${t.getName()}</option>
                        </#list>
                        </select>
                    </td>
                    <td align="right">状态：</td>
                    <td>
                        <select class="easyui-combobox" required="true" name="status" style="width:182px;height:30px;" editable="false" disabled>
                        <#list StatusEnum as s>
                            <option value="${s.getValue()}" <#if entity.status?? && entity.status==s.getValue()>selected</#if> >${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script type="text/javascript">

    (function() {
        var pid = '${pid}', win = $('#' + pid);
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();

</script>