<div class="popup_body">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">省份城市：</td>
                    <td>
                        <div class="select_city" style="width:210px;" >
                        <#include '../../basic/area/select_area_agent.ftl'>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">运营商名称：</td>
                    <td><input type="text" maxlength="20" class="text easyui-validatebox" required="true" name="agentName" /></td>
                </tr>
                <tr>
                    <td  width="120" width="70" align="right">运营商级别：</td>
                    <td  width="230" >
                        <select name="grade" class="easyui-combobox" style="width: 183px;height: 28px;" editable="false">
                            <option value="">无</option>
                        <#list gradeEnum as e>
                            <option value="${e.getValue()}"> ${(e.getName())!''}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">上级运营商：</td>
                    <td>
                        <select name="parentId" class="easyui-combotree" editable="false"  style="width: 184px; height: 28px;" url="${contextPath}/security/basic/agent/tree.htm?agentId=${(agentId)!''}">
                    </td>
                </tr>
                <tr>
                    <td align="right">省级运营商：</td>
                    <td>
                        <select name="provinceAgentId" id="province_agent_id_${pid}" class="easyui-combotree"   style="width: 184px; height: 28px;"
                                data-options="
                                url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}',
                                onLoadSuccess:function() {
                                   $('#province_agent_id_${pid}').combotree('setValue', null);
                                }">
                    </td>
                </tr>
                <tr>
                    <td align="right">市级运营商：</td>
                    <td>
                        <select name="cityAgentId" id="city_agent_id_${pid}" class="easyui-combotree"   style="width: 184px; height: 28px;"
                                data-options="
                                url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}',
                                onLoadSuccess:function() {
                                   $('#city_agent_id_${pid}').combotree('setValue', null);
                                }">
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">联系人：</td>
                    <td><input type="text" maxlength="5" class="text easyui-validatebox" required="true" name="linkman" /></td>
                </tr>
                <tr>
                    <td width="80" align="right">联系电话：</td>
                    <td><input type="text" maxlength="11" class="text easyui-validatebox" required="true" name="tel" /></td>
                </tr>
                <tr>
                    <td align="right">是否启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_1" checked value="1"/><label for="is_active_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" id="is_active_0" value="0"/><label for="is_active_0">禁用</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right">是否支持换电：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isExchange" id="is_exchange_1"  value="1"/><label for="is_exchange_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isExchange" id="is_exchange_0" checked value="0"/><label for="is_exchange_0">禁用</label>
                        </span>
                    </td>
                </tr>

                <tr>
                    <td align="right">是否支持租电：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isRent" id="is_rent_1" value="1"/><label for="is_rent_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isRent" id="is_rent_0" checked value="0"/><label for="is_rent_0">禁用</label>
                        </span>
                    </td>
                </tr>

                <tr>
                    <td align="right">是否支持租车：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isVehicle" id="is_vehicle_1" value="1"/><label for="is_vehicle_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isVehicle" id="is_vehicle_0" checked value="0"/><label for="is_vehicle_0">禁用</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right">结算周期：</td>
                    <td>
                        <select name="balanceStatus" id="balanceStatus_${pid}" style="height: 28px;width: 185px;" class="easyui-combobox"   editable="false">
                        <#list BalanceStatusEnum as e>
                            <option value="${e.getValue()}" <#if (e.value == 2)>selected</#if>> ${(e.getName())!''}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <#--<tr>-->
                    <#--<td align="right">序号：</td>-->
                    <#--<td><input type="text" maxlength="6" class="easyui-numberspinner" style="height: 28px;width: 185px;" name="orderNum"  data-options="min:0,max:100"value="10"/></td>-->
                <#--</tr>-->
                <#--<tr>-->
                    <#--<td align="right">平台比例%：</td>-->
                    <#--<td><input type="text" maxlength="3" class="easyui-numberspinner" style="height: 28px;width: 185px;" name="platformRatio" required="true"  data-options="min:0,max:100" value="10"/></td>-->
                <#--</tr>-->
                <tr>
                    <td align="right" valign="top" style="padding-top:10px;">备注：</td>
                    <td><textarea style="width:400px;" maxlength="20" name="memo"></textarea></td>
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
                provinceId: form.provinceId.value,
                cityId: form.cityId.value,
                districtId: form.districtId.value,
                agentName: form.agentName.value,
                grade: form.grade.value,
                parentId: form.parentId.value,
                provinceAgentId: form.provinceAgentId.value,
                cityAgentId: form.cityAgentId.value,
                linkman: form.linkman.value,
                tel: form.tel.value,
                isActive: form.isActive.value,
                balanceStatus: form.balanceStatus.value,
                isExchange: form.isExchange.value,
                isRent: form.isRent.value,
                isVehicle: form.isVehicle.value,
                memo: form.memo.value
            };

            function check() {
                if(win.find('input[name="parentId"]').val()==''){
                    $.messager.alert('提示信息', "上级运营商不能为空", 'error');
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
                    url: '${contextPath}/security/basic/agent/create.htm',
                    dataType: 'json',
                    data: values,
                    success: function (json) {
                        <@app.json_jump/>
                        if (json.success) {
                            win.data('id', json.data.id);
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                            success = false;
                        }
                    },
                    error: function (text) {
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
