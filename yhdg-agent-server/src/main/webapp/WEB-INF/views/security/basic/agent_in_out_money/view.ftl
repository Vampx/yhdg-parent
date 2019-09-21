<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right" width="80">运营商：</td>
                    <td>
                        <input name="agentId" class="easyui-combotree" editable="false"
                               disabled style="width: 184px; height: 28px;"
                               url="${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}"
                               value="${(entity.agentId)!''}">
                    </td>
                </tr>
                <tr>
                    <td align="right">业务id：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${(entity.bizId)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">业务类型：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${(entity.bizTypeName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">类型：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${(entity.typeName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">金额：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${(entity.money/100)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">剩余金额：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${(entity.balance/100)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">操作人：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="${(entity.operator)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">创建时间：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="40" readonly
                               value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>"/></td>
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