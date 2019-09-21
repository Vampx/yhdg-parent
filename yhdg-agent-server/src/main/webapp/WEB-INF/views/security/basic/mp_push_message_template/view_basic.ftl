<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">平台：</td>
                    <td><select name="toAppId" id="app_id_${pid}" style="height: 28px;width: 185px;" readonly="readonly" class="easyui-combobox" editable="false">
                    <#list agentList as s>
                        <option value="${s.getId()}" <#if entity.appId?? && entity.appId == s.getId()>selected</#if> >${s.getAgentName()}</option>
                    </#list>
                    </select></td>
                </tr>
                <tr>
                    <td width="70" align="right">模板名称：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly required="true" value="${(entity.templateName)!''}" style="width: 172px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">公众号：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly  value="${(entity.mpCode)!''}" style="width: 172px; height: 28px;"/></td>
                </tr>
                <#--<tr>-->
                    <#--<td width="70" align="right">生活号：</td>-->
                    <#--<td><input type="text" class="text easyui-validatebox"  value="${(entity.fwCode)!''}" style="width: 172px; height: 28px;"/></td>-->
                <#--</tr>-->
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">变量：</td>
                    <td><textarea style="width:330px;height: 100px;" readonly name="variable" maxlength="512">${(entity.variable)!''}</textarea></td>
                </tr>
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td><textarea style="width:330px;" name="memo" readonly maxlength="512">${(entity.memo)!''}</textarea></td>
                </tr>
                <tr>
                    <td align="right">启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isActive" id="isActive_1" <#if entity.isActive?? && entity.isActive == 1>checked</#if>  value="1"/><label for="isActive_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isActive" id="isActive_0"  <#if entity.isActive?? && entity.isActive == 0>checked</#if> value="0"/><label for="isActive_0">禁用</label>
                        </span>
                    </td>
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
        };
        win.data('ok', ok);
    })();
</script>