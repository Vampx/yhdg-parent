<div class="tab_item" style="display:block;">
    <div class="ui_table" style="height: 530px">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="right">卡号：</td>
                    <td><input type="text" class="text" maxlength="40" readonly name="id" value="${(entity.id)!''}"
                               style="width: 173px; height: 28px;"/></td>
                    <td align="right">运营商：</td>
                    <td><input type="text" class="text" maxlength="40" readonly name="agentName" value="${(entity.agentName)!''}"
                               style="width: 173px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">&nbsp;&nbsp;品牌：</td>
                    <td>
                        <select id="brand" name="brand" disabled="disabled" class="easyui-combobox" style="width: 184px; height: 28px;">
                            <option value="">无</option>
                        <#if brandList??>
                            <#list brandList as e>
                                <option <#if entity.brand?? && entity.brand == e.itemValue>selected</#if>
                                        value="${(e.itemValue)!''}">${(e.itemName)!''}</option>
                            </#list>
                        </#if>
                        </select>
                    </td>
                    <td align="right">换电次数：</td>
                    <td><input type="text" class="text easyui-numberspinner" readonly name="exchangeAmount" required="true"
                               value="${(entity.exchangeAmount)!''}" style="width: 184px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">电池类型：</td>
                    <td>
                        <input name="batteryType" id="battery_type_${pid}" class="easyui-combotree" editable="false"
                               style="width: 184px; height: 28px;"
                               url="${contextPath}/security/basic/agent_battery_type/tree.htm?agentId=${entity.agentId}"
                               value="${(entity.type)!''}">
                    </td>
                    <td align="right">状态：</td>
                    <td>
                        <select name="status" disabled="disabled" class="easyui-combobox" style="width: 184px; height: 28px;">
                        <#list StatusEnum as e>
                            <option <#if entity.status?? && entity.status == e.getValue()>selected</#if>
                                    value="${e.getValue()}">${(e.getName())!''}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">当前电量：</td>
                    <td><input type="text" class="text easyui-numberspinner" readonly name="volume" value="${(entity.volume)!''}"
                               style="width: 184px; height: 28px;"/></td>
                    <td align="right">充满电量：</td>
                    <td><input type="text" class="text" readonly value="${(entity.chargeCompleteVolume)!''}"
                               style="width: 173px; height: 28px;"/></td>
                </tr>
                <tr>

                    <td align="right">订单距离：</td>
                    <td><input type="text" maxlength="40" class="text" name="orderDistance" readonly
                               value="${(entity.orderDistance)!''}"/></td>
                    <td align="right">总距离：</td>
                    <td><input type="text" class="text" maxlength="40" name="totalDistance" readonly
                               value="${(entity.totalDistance)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">经度：</td>
                    <td><input type="text" maxlength="40" class="text" readonly name="lng" value="${(entity.lng)!''}"/></td>
                    <td align="right">纬度：</td>
                    <td><input type="text" class="text" maxlength="40" readonly name="lat" value="${(entity.lat)!''}"/></td>
                </tr>
                <tr>
                    <td align="right">IMEI：</td>
                    <td><input maxlength="40" class="text easyui-validatebox" name="code" required="true" readonly
                               value="${(entity.code)!''}" style="width: 173px; height: 28px;"/></td>
                    <td align="right">上报时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" name="reportTime" readonly
                               style="width:184px;height:28px"
                               value="<#if (entity.reportTime)?? >${app.format_date_time(entity.reportTime)}</#if>"/>
                    </td>
                </tr>
                <#--<tr>-->
                    <#--<td align="right" width="90">最小功率：</td>-->
                    <#--<td><input id="min_power_${pid}" name="minPower" class="easyui-numberspinner" readonly-->
                               <#--style="width: 184px; height: 28px;" required="required" value="${(entity.minPower)!''}"></td>-->

                <#--</tr>-->
                <tr>
                    <td align="right">启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" readonly
                                   <#if entity.isActive?? && entity.isActive == 1>checked</#if> id="is_active_1" disabled
                                   value="1"/><label for="is_active_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isActive" readonly
                                   <#if entity.isActive?? && entity.isActive == 0>checked</#if> id="is_active_0" disabled
                                   value="0"/><label for="is_active_0">禁用</label>
                        </span>
                    </td>
                    <td align="right" width="100">上报单体电压：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isReportVoltage" disabled
                                   <#if entity.isReportVoltage?? && entity.isReportVoltage == 1>checked</#if>
                                   id="is_report_voltage_1" value="1"/><label for="is_report_voltage_1">是</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isReportVoltage" disabled
                                   <#if entity.isReportVoltage?? && entity.isReportVoltage == 0>checked</#if>
                                   id="is_report_voltage_0" value="0"/><label for="is_report_voltage_0">否</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="right">二维码：</td>
                    <td><input type="text" class="text" maxlength="40" value="${(entity.qrcode)!''}" readonly
                               style="width: 173px; height: 28px;"/></td>
                    <td align="right">外壳编号：</td>
                    <td><input type="text" class="text" value="${(entity.shellCode)!''}" readonly
                               style="width: 173px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td align="right">sim卡号：</td>
                    <td><input type="text" class="text" value="${(entity.simMemo)!''}" readonly
                               style="width: 173px; height: 28px;"/></td>
                    <td align="right">所属站点：</td>
                    <td><input value="${(entity.cabinetName)!''}"  type="text" class="text" readonly
                               name="belongCabinetName"/></td>
                </tr>
                <tr>
                    <td align="right">返修状态：</td>
                    <td><input type="text" class="text" value="${(entity.repairStatusName)!''}" readonly
                               style="width: 173px; height: 28px;"/></td>
                    <td align="right">离柜时间：</td>
                    <td><input type="text" class="text easyui-datetimebox" name="freeOutTime" readonly
                               style="width:184px;height:28px"
                               value="<#if (entity.freeOutTime)?? >${app.format_date_time(entity.freeOutTime)}</#if>"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>
    (function () {
        var win = $('#${pid}');
        var ok = function () {
            var success = true;
            return success;
        };
        win.data('ok', ok);
    })();
</script>