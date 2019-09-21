<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">

            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="150" align="right">运营商名称：</td>
                    <td><input type="text" maxlength="20" readonly="readonly" class="text easyui-validatebox" required="true" name="agentName" value="${(entity.agentName)!''}"/></td>
                </tr>
                <#if category?? && category ==1 >
                    <tr>
                        <td width="100" align="right">换电押金池金额（元）：</td>
                        <td><input type="text" class="text easyui-validatebox" readonly="readonly"
                                   value="${(entity.foregiftBalance/100)!}"/></td>
                    </tr>
                    <tr>
                        <td width="100" align="right">押金池剩余金额（元）：</td>
                        <td><input type="text" class="text easyui-validatebox" readonly="readonly"
                                   value="${(entity.foregiftRemainMoney/100)!}"/></td>
                    </tr>
                    <tr>
                        <td width="100" align="right">押金池可转余额（元）：</td>
                        <td><input type="text" class="text easyui-validatebox" readonly="readonly"
                                   value="${(entity.hdWithdrawMoney/100)!}"/></td>
                    </tr>
                </#if>

            <#if category?? && category ==2 >
                <tr>
                    <td width="100" align="right">租电押金池金额（元）：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly="readonly"
                               value="${(entity.zdForegiftBalance/100)!}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">押金池剩余金额（元）：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly="readonly"
                               value="${(entity.zdForegiftRemainMoney/100)!}"/></td>
                </tr>
                <tr>
                    <td width="100" align="right">押金池可提金额（元）：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly="readonly"
                               value="${(entity.zdWithdrawMoney/100)!}"/></td>
                </tr>
            </#if>

            </table>
        </form>
    </div>
</div>
