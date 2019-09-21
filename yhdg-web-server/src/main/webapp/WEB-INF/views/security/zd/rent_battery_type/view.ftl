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
                    <td align="right">类型名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="typeName" readonly maxlength="40" value="${(entity.typeName)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">额定电压：</td>
                    <td><input type="text" name="ratedVoltage" readonly class="easyui-numberbox"  data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" value="${(entity.ratedVoltage/1000)!''}" >V</td>
                </tr>
                <tr>
                    <td align="right">额定容量：</td>
                    <td><input type="text" name="ratedCapacity" readonly class="easyui-numberbox"  data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" value="${(entity.ratedCapacity/1000)!''}" >Ah</td>
                </tr>
                <tr>
                    <td align="right">是否启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isActive" id="is_active_1" <#if entity.isActive?? && entity.isActive == 1>checked</#if> value="1"/><label for="is_active_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isActive" id="is_active_0" <#if entity.isActive?? && entity.isActive == 1><#else >checked</#if> value="0"/><label for="is_active_0">禁用</label>
                        </span>
                    </td>
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