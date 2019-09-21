<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right" width="100">运营商名称：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" readonly class="easyui-combotree" editable="false"
                               <#if (entity.agentId)??>disabled</#if> style="width: 184px; height: 28px;"
                               url="${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}"
                               value="${(entity.agentId)!''}">
                    </td>
                </tr>
                <tr>
                    <td align="right">手机号码：</td>
                    <td><input type="text" id="mobile" class="text" readonly="readonly" name="mobile" maxlength="11" validType="mobile"
                               value="${(entity.mobile)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">备注：</td>
                    <td colspan="3"><textarea style="width:260px;height:50px;" readonly name="memo" maxlength="200">${(entity.memo)!''}</textarea></td>
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