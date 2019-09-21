<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right" width="100">运营商：</td>
                    <td>
                        <input name="agentId" id="agent_id_${pid}" readonly class="easyui-combotree" editable="false"
                               <#if (entity.agentId)??>disabled</#if> style="width: 184px; height: 28px;"
                               url="${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}"
                               value="${(entity.agentId)!''}">
                    </td>
                </tr>
                <tr>
                    <td align="right">姓名：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true"  value="${(entity.fullname)!''}" maxlength="40"  name="fullname"  style="width: 174px;height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">手机号：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" value="${(entity.mobile)!''}"  maxlength="11" name="mobile" style="width: 174px;height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">类型名称：</td>
                    <td><input type="text" class="text easyui-validatebox"  name="typeName" readonly required="true" maxlength="40" value="${(entity.typeName)!''}" /></td>
                </tr>
                <tr>
                    <td align="right">额定电压：</td>
                    <td><input type="text" id="rated_voltage_${pid}" readonly class="text easyui-validatebox"  data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" value="${(entity.ratedVoltage/1000)!''}" >V</td>
                </tr>
                <tr>
                    <td align="right">额定容量：</td>
                    <td><input type="text" id="rated_capacity_${pid}" readonly class="text easyui-validatebox""  data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" value="${(entity.ratedCapacity/1000)!''}" >Ah</td>
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