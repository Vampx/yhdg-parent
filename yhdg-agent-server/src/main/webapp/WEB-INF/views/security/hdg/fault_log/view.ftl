<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="90" align="right">运营商：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="agentName"value="${(entity.agentName)!''}" style="width: 172px; height: 28px;"/></td>
                    <td width="90" align="right">换电柜编号：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" readonly="readonly" name="cabinetId" value="${(entity.cabinetId)!''}" style="width: 172px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="90" align="right">换电柜名称：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="cabinetName"value="${(entity.cabinetName)!''}" style="width: 172px; height: 28px;"/></td>
                    <td width="90" align="right">格子编号：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="boxNum"value="${(entity.boxNum)!''}" style="width: 172px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="90" align="right">电池编号：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" name="batteryId"value="${(entity.batteryId)!''}" style="width: 172px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="90" align="right">故障类型：</td>
                    <td>
                        <select class="easyui-combobox" required="true" id="faultType_${pid}" disabled name="faultType" style="width:180px;height: 30px ">
                        <#list FaultTypeEnum as s>
                            <option value="${s.getValue()}" <#if entity.faultType?? && entity.faultType==s.getValue()>selected</#if> >${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                    <td width="90" align="right">处理状态：</td>
                    <td>
                        <select class="easyui-combobox" required="true" id="status_${pid}" disabled name="status" style="width:180px;height: 30px ">
                        <#list StatusEnum as s>
                            <option value="${s.getValue()}" <#if entity.status?? && entity.status==s.getValue()>selected</#if> >${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">处理人：</td>
                    <td><input type="text" class="text easyui-validatebox" required="true" readonly name="handlerName"value="${(entity.handlerName)!''}" style="width: 172px; height: 28px;"/></td>
                    <td width="90" align="right">故障等级：</td>
                    <td>
                        <select class="easyui-combobox" required="true" id="faultLevel_${pid}" disabled name="faultLevel" style="width:180px;height: 30px ">
                        <#list FaultLevelEnum as s>
                            <option value="${s.getValue()}" <#if entity.faultLevel?? && entity.faultLevel==s.getValue()>selected</#if> >${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">故障内容：</td>
                    <td colspan="3"><textarea style="width:455px;" name="faultContent" readonly maxlength="400">${(entity.faultContent)!''}</textarea></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">处理备注：</td>
                    <td colspan="3" ><textarea style="width:455px;" name="handleMemo" readonly maxlength="200">${(entity.handleMemo)!''}</textarea></td>
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