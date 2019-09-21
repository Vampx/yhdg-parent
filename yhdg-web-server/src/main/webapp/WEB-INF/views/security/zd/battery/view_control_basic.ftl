<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="70" align="right">电池id：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.id)!''}" /></td>
                    <td width="70" align="right">电池类型：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.batteryType)!''}"/></td>

                </tr>
                <tr>
                    <td width="70" align="right">当前电量：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.volume)!0}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">当前电量：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.currentCapacity)!0}"/></td>
                    <td width="70" align="right">状态：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.statusName)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">换电次数：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.exchangeAmount)!0}" /></td>
                    <td width="70" align="right">上报时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox" readonly style="width:195px; height:28px " value="<#if (entity.reportTime)?? >${app.format_date_time(entity.reportTime)}</#if>" />
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">使用次数：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.useCount)!0}"/></td>
                    <td width="70" align="right">总距离：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.totalDistance)!0}" /></td>
                </tr>
                <tr>
                    <td width="70" align="right">是否启用：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled id="isActive_1" <#if entity.isActive?? && entity.isActive == 1>checked</#if>  value="1"/><label for="isActive_1">启用</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled id="isActive_0"  <#if entity.isActive?? && entity.isActive == 0>checked</#if> value="0"/><label for="isActive_0">禁用</label>
                        </span>
                    </td>
                    <td width="70" align="right">当前信号：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.currentSignal)!0}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">经度：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.lng)!''}" /></td>
                    <td width="70" align="right">纬度：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.lat)!''}" /></td>
                </tr>
                <tr>
                    <td width="70" align="right">地址：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.address)!''}"/></td>
                    <td width="70" align="right">sim卡信息：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.simMemo)!''}" /></td>

                </tr>
                <tr>
                    <td width="70" align="right">品牌：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.brandName)!''}" /></td>
                    <td width="70" align="right">版本：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.version)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">电压：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.voltage)!''}" /></td>
                    <td width="70" align="right">电流：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.electricity)!''}" /></td>
                </tr>
                <tr>
                    <td width="70" align="right">总容量：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.totalCapacity)!0}"/></td>
                    <td width="70" align="right">生产日期：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox" readonly style="width:195px; height:28px " value="<#if (entity.produceDate)?? >${app.format_date_time(entity.produceDate)}</#if>" />
                    </td>
                </tr>
                <tr>
                    <td width="70" align="right">保护状态：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.protectState)!''}" /></td>
                    <td width="70" align="right">指示状态：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.fet)!''}"/></td>
                </tr>
                <tr>
                    <td width="70" align="right">电池串数：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.strand)!''}" /></td>
                    <td width="70" align="right">温度：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly value="${(entity.temp)!''}" /></td>
                </tr>
                <tr>
                    <td width="70" align="right">是否已通电：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled id="isElectrify_1" <#if entity.isElectrify?? && entity.isElectrify == 1>checked</#if>  value="1"/><label for="isElectrify_1">是</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled id="isElectrify_0"  <#if entity.isElectrify?? && entity.isElectrify == 0>checked</#if> value="0"/><label for="isElectrify_0">否</label>
                        </span>
                    </td>
                    <td width="70" align="right">位置状态：</td>
                    <td><input type="text" class="text easyui-validatebox" disabled value="${(entity.positionStateName)!''}" /></td>
                </tr>
                <tr>
                    <td width="70" align="right">充电状态：</td>
                    <td><input type="text" class="text easyui-validatebox" disabled value="${(entity.chargeStatusName)!''}" /></td>
                    <td width="70" align="right">电池每串单体电压：</td>
                    <td><input type="text" class="text easyui-validatebox" disabled value="${(entity.singleVoltage)!''}" /></td>
                </tr>
                <tr>
                    <td width="70" align="right">停留心跳间隔：</td>
                    <td><input type="text" class="easyui-numberspinner" readonly value="${(entity.stayHeartbeat)!0}" style="width: 184px; height: 28px;" /></td>
                </tr>
                <tr>
                    <td width="70" align="right">移动心跳间隔：</td>
                    <td><input type="text" class="easyui-numberspinner" readonly value="${(entity.moveHeartbeat)!''}" style="width: 184px; height: 28px;" /></td>
                    <td width="70" align="right">通电心跳间隔：</td>
                    <td><input type="text" class="easyui-numberspinner" readonly value="${(entity.electrifyHeartbeat)!''}" style="width: 184px; height: 28px;" /></td>
                </tr>
                <tr>
                    <td align="right" width="90">是否上报单体电压：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled id="is_report_voltage_1" <#if entity.isReportVoltage?? && entity.isReportVoltage == 1>checked</#if> value="1"/><label for="is_report_voltage_1">是</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" disabled id="is_report_voltage_0" <#if entity.isReportVoltage?? && entity.isReportVoltage == 0>checked</#if> value="0"/><label for="is_report_voltage_0">否</label>
                        </span>
                    </td>
                    <td width="70" align="right">上报电压时间：</td>
                    <td>
                        <input type="text" class="text easyui-datetimebox" readonly style="width:195px; height:28px " value="<#if (entity.reportVoltageTime)?? >${app.format_date_time(entity.reportVoltageTime)}</#if>" />
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];

        var ok = function() {
            if(!jform.form('validate')) {
                return false;
            }
            var success = true;

            return success;
        };

        win.data('ok', ok);
    })();
</script>