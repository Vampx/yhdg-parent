<div class="popup_body">
    <div class="ui_table">
        <form method="post">
            <input type="hidden" name="agentId" value="${(entity.agentId)!''}">
            <input type="hidden" name="estateId" value="${(entity.estateId)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">订单编号：</td>
                    <td><input type="text" class="text easyui-validatebox"  name="orderId" value="${(entity.orderId)!''}" style="width: 175px; height: 28px;"/></td>
                    <td align="right">结算日期：</td>
                    <td><input type="text" class="text easyui-validatebox"    name="balanceDate" value="${(entity.balanceDate)!''}" /></td>
                </tr>
                <tr>
                    <td align="right">类型：</td>
                    <td>
                        <select name="bizType" class="easyui-combobox" style="width: 184px; height: 28px;" editable="false">
                        <#list BizTypeEnum as e>
                            <option value="${e.getValue()}" <#if entity.bizType?? && (entity.bizType == e.value)>selected</#if>> ${(e.getName())!''}</option>
                        </#list>
                        </select>
                    </td>
                    <td align="right">状态：</td>
                    <td>
                        <select name="status" class="easyui-combobox" style="width: 184px; height: 28px;" editable="false">
                        <#list StatusEnum as e>
                            <option value="${e.getValue()}" <#if entity.status?? && (entity.status == e.value)>selected</#if>> ${(e.getName())!''}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">金额：</td>
                    <td><input  class="easyui-numberspinner"  style="width:160px;height:28px " required="required" value="${(entity.money)/ 100 !''}" data-options="min:0.00,precision:2">&nbsp;&nbsp;元</td>
                    <td align="right">确认人员：</td>
                    <td><input type="text" class="text easyui-validatebox"    name="confirmUser" value="${(entity.confirmUser)!''}" /></td>
                </tr>
                <tr>
                    <td align="right">处理时间：</td>
                    <td><input type="text" class="text easyui-validatebox"    name="handleTime" value="<#if (entity.handleTime)?? >${app.format_date_time(entity.handleTime)}</#if>" /></td>
                    <td align="right">确认时间：</td>
                    <td><input type="text" class="text easyui-validatebox"    name="confirmTime" value="<#if (entity.confirmTime)?? >${app.format_date_time(entity.confirmTime)}</#if>" /></td>
                </tr>
                <tr>
                    <td align="right">运营账号：</td>
                    <td><input type="text" class="text easyui-validatebox"    name="agentMobile" value="${(entity.agentMobile)!''}" /></td>
                    <td align="right">运营账户：</td>
                    <td><input type="text" class="text easyui-validatebox"  name="agentAccountName" value="${(entity.agentAccountName)!''}" style="width: 175px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td><textarea style="width:200px;" name="memo" maxlength="200">${(entity.memo)!''}</textarea></td>
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