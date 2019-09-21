<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">

            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">uid：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly  name="uid" value="${(entity.uid)!''}" /></td>
                    <td align="right">版本：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly  name="version" value="${(entity.version)!''}" /></td>
                </tr>
                <tr>
                    <td width="70" align="right">策略：</td>
                    <td>
                        <select class="easyui-combobox"  name="strategyId" readonly="readonly" style="width:185px;height: 28px " editable="false" disabled>
                        <#if strategyList??>
                            <#list strategyList as e>
                                <option value="${e.id}" <#if (entity.strategyId)?? && e.id == entity.strategyId>selected="selected"</#if>>${(e.strategyName)!''}</option>
                            </#list>
                        </#if>
                        </select>
                    </td>
                    <td width="70" align="right">是否在线：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isOnline" disabled id="is_online_1" <#if entity.isOnline?? && entity.isOnline == 1>checked</#if> value="1"/><label for="is_online_1">是</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isOnline" disabled id="is_online_0" <#if entity.isOnline?? && entity.isOnline == 1><#else>checked</#if> value="0"/><label for="is_online_0">否</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">心跳时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" readonly name="heartTime" style="width:184px;height:28px" value="<#if (entity.heartTime)?? >${app.format_date_time(entity.heartTime)}</#if>" /></td>
                </tr>
            </table>
        </form>
    </div>
</div>