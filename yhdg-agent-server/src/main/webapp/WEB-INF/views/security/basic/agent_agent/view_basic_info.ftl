<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">

            <table cellpadding="0" cellspacing="0">
                <tr>
                <tr>
                    <td align="right">省份城市：</td>
                    <td>
                    <#assign areaText=''>
                    <#if (entity.provinceName)??>
                        <#assign areaText=entity.provinceName>
                    </#if>
                    <#if (entity.cityName)??>
                        <#assign areaText=areaText + ' - ' + entity.cityName>
                    </#if>
                    <#if (entity.districtName)??>
                        <#assign areaText=areaText + ' - ' + entity.districtName>
                    </#if>
                        <input type="text" class="text easyui-validatebox" value="${(areaText)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">运营商名称：</td>
                    <td><input type="text" maxlength="20" class="text easyui-validatebox" required="true" name="agentName" value="${(entity.agentName)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">运营商级别：</td>
                    <td>
                        <select name="grade" class="easyui-combobox" style="width: 184px; height: 28px;" editable="false">
                            <option value="">无</option>
                        <#list gradeEnum as e>
                            <option value="${e.getValue()}" <#if entity.grade?? && (entity.grade == e.value)>selected</#if>> ${(e.getName())!''}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">上级运营商：</td>
                    <td>
                        <select name="parentId" id="parent_id_${pid}" class="easyui-combotree" readonly   style="width: 184px; height: 28px;"
                                data-options="
                                url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}',
                                onLoadSuccess:function() {
                                   $('#parent_id_${pid}').combotree('setValue', '${(entity.parentId)!''}');
                                }" value="${(entity.parentId)!''}">
                    </td>
                </tr>
                <tr>
                    <td align="right">省级运营商：</td>
                    <td>
                        <select name="provinceAgentId" id="province_agent_id_${pid}" class="easyui-combotree" readonly   style="width: 184px; height: 28px;"
                                data-options="
                                url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}',
                                onLoadSuccess:function() {
                                   $('#province_agent_id_${pid}').combotree('setValue', '${(entity.provinceAgentId)!''}');
                                }" value="${(entity.provinceAgentId)!''}">
                    </td>
                </tr>
                <tr>
                    <td align="right">市级运营商：</td>
                    <td>
                        <select name="cityAgentId" id="city_agent_id_${pid}" class="easyui-combotree" readonly   style="width: 184px; height: 28px;"
                                data-options="
                                url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}',
                                onLoadSuccess:function() {
                                   $('#city_agent_id_${pid}').combotree('setValue', '${(entity.cityAgentId)!''}');
                                }" value="${(entity.cityAgentId)!''}">
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">联系人：</td>
                    <td><input type="text" maxlength="5" class="text easyui-validatebox" required="true" name="linkman" value="${(entity.linkman)!''}"/></td>
                </tr>
                <tr>
                    <td width="80" align="right">联系电话：</td>
                    <td><input type="text" maxlength="11" class="text easyui-validatebox" required="true" name="tel" value="${(entity.tel)!''}" /></td>
                </tr>
                <tr>
                    <td align="right">是否启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_1" <#if entity.isActive?? && entity.isActive == 1>checked</#if> value="1"/><label for="is_active_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_0" <#if entity.isActive?? && entity.isActive == 1><#else>checked</#if> value="0"/><label for="is_active_0">禁用</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right">结算周期：</td>
                    <td>
                        <select name="balanceStatus" id="balanceStatus_${pid}" style="height: 28px;width: 185px;" class="easyui-combobox"   editable="false">
                        <#list BalanceStatusEnum as e>
                            <option value="${e.getValue()}" <#if entity.balanceStatus?? && entity.balanceStatus == e.getValue()>selected</#if>> ${(e.getName())!''}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">序号：</td>
                    <td><input type="text" maxlength="6" class="easyui-numberspinner" style="height: 28px;width: 185px;" name="orderNum"  data-options="min:0,max:100" value="${(entity.orderNum)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">是否支持换电：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isExchange" id="is_exchange_1" <#if entity.isExchange?? && entity.isExchange == 1>checked</#if>  value="1"/><label for="is_exchange_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isExchange" id="is_exchange_0" <#if entity.isExchange?? && entity.isExchange == 1><#else>checked</#if>  value="0"/><label for="is_exchange_0">禁用</label>
                        </span>
                    </td>
                </tr>

                <tr>
                    <td align="right">是否支持租电：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isRent" id="is_rent_1" <#if entity.isRent?? && entity.isRent == 1>checked</#if> value="1"/><label for="is_rent_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isRent" id="is_rent_0" <#if entity.isRent?? && entity.isRent == 1><#else>checked</#if> value="0"/><label for="is_rent_0">禁用</label>
                        </span>
                    </td>
                </tr>

                <tr>
                    <td align="right">是否支持租车：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isVehicle" id="is_vehicle_1" <#if entity.isVehicle?? && entity.isVehicle == 1>checked</#if>  value="1"/><label for="is_vehicle_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled name="isVehicle" id="is_vehicle_0" <#if entity.isVehicle?? && entity.isVehicle == 1><#else>checked</#if>  value="0"/><label for="is_vehicle_0">禁用</label>
                        </span>
                    </td>
                </tr>
                <#--<tr>-->
                    <#--<td align="right">平台比例%：</td>-->
                    <#--<td><input type="text" maxlength="6" class="easyui-numberspinner" style="height: 28px;width: 185px;" name="platformRatio" required="true"  data-options="min:0,max:100"value="10"/></td>-->
                <#--</tr>-->
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td><textarea style="width:400px;" maxlength="20" name="memo">${(entity.memo)!''}</textarea></td>
                </tr>
            </table>
        </form>
    </div>
</div>
