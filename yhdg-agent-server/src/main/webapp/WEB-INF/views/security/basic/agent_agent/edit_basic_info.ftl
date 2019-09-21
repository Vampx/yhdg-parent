<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" name="partnerId" value="${(entity.partnerId)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">省份城市：</td>
                    <td>
                        <div class="select_city" style="width:210px;" >
                        <#assign areaText=''>
                        <#if (entity.provinceName)??>
                            <#assign provinceName=entity.provinceName>
                            <#assign areaText=entity.provinceName>
                        </#if>
                        <#if (entity.cityName)??>
                            <#assign cityName=entity.cityName>
                            <#assign areaText=areaText + ' - ' + entity.cityName>
                        </#if>
                        <#if (entity.districtName)??>
                            <#assign districtName=entity.districtName>
                            <#assign areaText=areaText + ' - ' + entity.districtName>
                        </#if>

                        <#if (entity.provinceId)??>
                            <#assign provinceId=entity.provinceId>
                        </#if>
                        <#if (entity.cityId)??>
                            <#assign cityId=entity.cityId>
                        </#if>
                        <#if (entity.districtId)??>
                            <#assign districtId=entity.districtId>
                        </#if>
                    <#include '../../basic/area/select_area_agent.ftl'>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">运营商名称：</td>
                    <td><input type="text" maxlength="20" class="text easyui-validatebox" required="true" name="agentName" value="${(entity.agentName)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">运营商级别：</td>
                    <td>
                        <select name="grade" class="easyui-combobox" style="width: 184px; height: 28px;">
                            <option value="">无</option>
                        <#list gradeEnum as e>
                            <option value="${e.getValue()}" <#if entity.grade?? && (entity.grade == e.value)>selected</#if>> ${(e.getName())!''}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr style="display: none">
                    <td align="right">上级运营商：</td>
                    <td>
                        <select name="parentId" id="parent_id_${pid}" class="easyui-combotree"   style="width: 184px; height: 28px;"
                                data-options="
                                url:'${contextPath}/security/basic/agent/tree.htm?agentId=${(agentId)!''}',
                                onLoadSuccess:function() {
                                   $('#parent_id_${pid}').combotree('setValue', '${(entity.parentId)!''}');
                                }" value="${(entity.parentId)!''}">
                    </td>
                </tr>
                <tr>
                    <td align="right">省级运营商：</td>
                    <td>
                        <select name="provinceAgentId" id="province_agent_id_${pid}" class="easyui-combotree"   style="width: 184px; height: 28px;"
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
                        <select name="cityAgentId" id="city_agent_id_${pid}" class="easyui-combotree"   style="width: 184px; height: 28px;"
                                data-options="
                                url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}',
                                onLoadSuccess:function() {
                                   $('#city_agent_id_${pid}').combotree('setValue', '${(entity.cityAgentId)!''}');
                                }" value="${(entity.cityAgentId)!''}">
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">联系人：</td>
                    <td><input type="text" maxlength="5" class="text easyui-validatebox" name="linkman" value="${(entity.linkman)!''}"/></td>
                </tr>
                <tr>
                    <td width="80" align="right">联系电话：</td>
                    <td><input type="text" maxlength="11" class="text easyui-validatebox"  name="tel" value="${(entity.tel)!''}" /></td>
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
                    <td align="right">是否支持换电：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isExchange" id="is_exchange_1" <#if entity.isExchange?? && entity.isExchange == 1>checked</#if>  value="1"/><label for="is_exchange_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isExchange" id="is_exchange_0" <#if entity.isExchange?? && entity.isExchange == 1><#else>checked</#if>  value="0"/><label for="is_exchange_0">禁用</label>
                        </span>
                    </td>
                </tr>

                <tr>
                    <td align="right">是否支持租电：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isRent" id="is_rent_1" <#if entity.isRent?? && entity.isRent == 1>checked</#if>   value="1"/><label for="is_rent_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isRent" id="is_rent_0" <#if entity.isRent?? && entity.isRent == 1><#else>checked</#if>   value="0"/><label for="is_rent_0">禁用</label>
                        </span>
                    </td>
                </tr>

                <tr>
                    <td align="right">是否支持租车：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isVehicle" id="is_vehicle_1" <#if entity.isVehicle?? && entity.isVehicle == 1>checked</#if> value="1"/><label for="is_vehicle_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isVehicle" id="is_vehicle_0" <#if entity.isVehicle?? && entity.isVehicle == 1><#else>checked</#if>  value="0"/><label for="is_vehicle_0">禁用</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right">结算周期：</td>
                    <td>
                        <select name="balanceStatus" id="balanceStatus_${pid}" style="height: 28px;width: 185px;" class="easyui-combobox"  disabled  editable="false">
                        <#list BalanceStatusEnum as e>
                            <option value="${e.getValue()}" <#if entity.balanceStatus?? && entity.balanceStatus == e.getValue()>selected</#if>> ${(e.getName())!''}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
<#--                <tr>-->
<#--                    <td align="right">序号：</td>-->
<#--                    <td><input type="text" maxlength="6" class="easyui-numberspinner" style="height: 28px;width: 185px;" name="orderNum"  data-options="min:0,max:100" value="${(entity.orderNum)!''}"/></td>-->
<#--                </tr>-->
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
<script>
    (function () {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];

        var ok = function () {
            var success = true;
            var values = {
                id:form.id.value,
                provinceId: form.provinceId.value,
                cityId: form.cityId.value,
                districtId: form.districtId.value,
                partnerId: form.partnerId.value,
                agentName: form.agentName.value,
                grade: form.grade.value,
                parentId: form.parentId.value,
                provinceAgentId: form.provinceAgentId.value,
                cityAgentId: form.cityAgentId.value,
                linkman: form.linkman.value,
                tel: form.tel.value,
                isExchange: form.isExchange.value,
                isRent: form.isRent.value,
                isVehicle: form.isVehicle.value,
                isActive: form.isActive.value,
                balanceStatus: form.balanceStatus.value,
                memo: form.memo.value
            };

            function check() {
                if(win.find('input[name="partnerId"]').val()==''){
                    $.messager.alert('提示信息', "商户不能为空", 'error');
                    return false;
                }
                if(win.find('input[name="agentName"]').val()==''){
                    $.messager.alert('提示信息', "运营商不能为空", 'error');
                    return false;
                }
                if(win.find('input[name="linkman"]').val()==''){
                    $.messager.alert('提示信息', "联系人不能为空", 'error');
                    return false;
                }
                if(win.find('input[name="tel"]').val()==''){
                    $.messager.alert('提示信息', "联系电话不能为空", 'error');
                    return false;
                }
                return true;
            }

            if(!check()){
                success = false;
            }else {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/basic/agent/update.htm',
                    dataType: 'json',
                    data: values,
                    success: function (json) {
                        <@app.json_jump/>
                        if (json.success) {
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                            success = false;
                        }
                    },
                    error: function (text) {
                        console.log(text);
                        $.messager.alert('提示信息', text, 'info');
                        success = false;
                    }
                });
            }
            return success;
        };

        win.data('ok', ok);
    })();
</script>
