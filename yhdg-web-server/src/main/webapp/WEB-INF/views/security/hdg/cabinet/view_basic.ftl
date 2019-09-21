<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${entity.id}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td style="font-weight: 600;height: 32px;line-height: 32px;font-size: 16px;">基础设置</td>
                </tr>
                <tr>
                    <td style="width: 70px;" align="left">*设备编号：</td>
                    <td><span>${entity.id}</span></td>
                    <td width="70" align="right">*设备SN：</td>
                    <td><span>${(entity.mac)!''}</span></td>
                </tr>
                <tr>
                    <td width="70" align="left">*设备名称：</td>
                    <td><input type="text" class="text easyui-validatebox" name="cabinetName" readonly
                               maxlength="10" value="${(entity.cabinetName)!''}"/></td>
                    <td width="140" align="right">*所属运营商：</td>
                    <td><input name="agentId" required="true" class="easyui-combotree" disabled
                               editable="false" style="width: 183px; height: 28px;" id="agent_id_${pid}"
                               data-options="url:'${contextPath}/security/basic/agent/tree.htm',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto',
                                onClick: function () {
                                       swich_agent_${pid}();
                                }"
                               value="${(entity.agentId)!''}"/>
                </tr>
                <tr>
                    <td width="70" align="left">设备位置：</td>
                    <td colspan="2"><input type="text" class="text easyui-validatebox" readonly maxlength="50" style="width: 290px;"
                                           value="${(entity.address)!''}"/>&nbsp;&nbsp;</td>
                </tr>
                <tr>
                <td align="left">工作时间：</td>
                <td><input type="text" class="text easyui-timespinner" readonly maxlength="5" value="${(beginTime)!''}" data-options="showSeconds:false" style="width:86px; height:28px;" name="beginTime"/>--<input type="text" class="text easyui-timespinner" readonly maxlength="5" value="${(endTime)!''}" data-options="showSeconds:false" style="width:86px; height:28px;" name="endTime"/></td>
                <td width="70" align="right">终端id：</td>
                <td><input type="text" class="text easyui-validatebox" name="terminalId" readonly
                value="${(entity.terminalId)!''}"/></td>
                </tr>
                <tr>
                    <td align="left">*满电电量：</td>
                    <td><input type="text" class="text easyui-numberspinner" name="chargeFullVolume" readonly
                               data-options="min:0, max:100" style="width: 183px; height: 28px;" required="true"
                               value="${(entity.chargeFullVolume)!0}"/></td>
                    <td align="right">*可换进电量：</td>
                    <td><input type="text" class="text easyui-numberspinner" name="permitExchangeVolume" readonly
                               data-options="min:0, max:100" style="width: 183px; height: 28px;" required="true"
                               value="${(entity.permitExchangeVolume)!0}"/></td>
                </tr>
                <tr>
                    <td width="70" align="left">SIM卡号：</td>
                    <td><input type="text" class="text easyui-validatebox" readonly maxlength="25" name="simMemo" value="${(entity.simMemo)!''}"/></td>
                    <td align="right">是否启用：</td>
                    <td>
                        <select name="activeStatus" id="active_status_${pid}" class="easyui-combobox" editable="false" readonly="readonly"
                                style="width: 183px; height: 28px;">
                        <#list activeStatusEnum as e>
                            <option value="${e.getValue()}"
                                    <#if entity.activeStatus?? && entity.activeStatus == e.getValue()>selected</#if>>${e.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="150" align="left">电价（元/度）：</td>
                    <td><input class="easyui-numberspinner" readonly id="price_${pid}" name="price" maxlength="5" <#if entity ??><#if entity.price ??> value="${(entity.price)!0}"</#if> </#if>  style="width:182px;height: 28px;" data-options="min:0.00,precision:2"></td>

                    <td align="right">共享类型：</td>
                    <td>
                        <select name="viewType" id="view_type_${pid}" style="width: 183px; height: 28px;">
                        <#list ViewTypeEnum as s>
                            <option value="${s.getValue()}"
                                    <#if entity.viewType==s.getValue()>selected="selected"</#if>>${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="left">联系人：</td>
                    <td><input type="text" class="text easyui-validatebox" name="linkname" maxlength="40" readonly
                               value="${(entity.linkname)!''}"/></td>
                    <td width="70" align="right">电话：</td>
                    <td><input type="text" class="text easyui-validatebox" maxlength="11" name="tel" readonly
                               value="${(entity.tel)!''}"/></td>
                </tr>
                <tr>
                    <td align="left">最低可换电量：</td>
                    <td><input type="text" class="text easyui-numberspinner" name="minExchangeVolume" readonly
                               data-options="min:0, max:100" style="width: 183px; height: 28px;"
                               value="${(entity.minExchangeVolume)!''}"/></td>
                    <td align="right">是否开启wifi：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="enableWifi" id="enable_wifi_1"
                                   <#if entity.enableWifi?? && entity.enableWifi == 1>checked</#if> value="1" /><label for="enable_wifi_1">是</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="enableWifi" id="enable_wifi_0"
                                   <#if entity.enableWifi?? && entity.enableWifi == 0>checked</#if> value="0"/><label for="enable_wifi_0">否</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="left">是否开启蓝牙：</td>
                    <td>
                          <span class="radio_box">
                            <input type="radio" class="radio" name="enableBluetooth" id="enable_bluetooth_1"
                                   <#if entity.enableBluetooth?? && entity.enableBluetooth == 1>checked</#if> value="1" /><label for="enable_bluetooth_1">是</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="enableBluetooth" id="enable_bluetooth_0"
                                   <#if entity.enableBluetooth?? && entity.enableBluetooth == 0>checked</#if> value="0"/><label for="enable_bluetooth_0">否</label>
                        </span>
                    </td>
                    <td align="right">是否开启语音播报：</td>
                    <td>
                          <span class="radio_box">
                            <input type="radio" class="radio" name="enableVoice" id="enable_voice_1"
                                   <#if entity.enableVoice?? && entity.enableVoice == 1>checked</#if> value="1" /><label for="enable_voice_1">是</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="enableVoice" id="enable_voice_0"
                                   <#if entity.enableVoice?? && entity.enableVoice == 0>checked</#if> value="0"/><label for="enable_voice_0">否</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="left" valign="top" style="padding-top:10px;">备注：</td>
                    <td colspan="3"><textarea style="width:505px;" maxlength="20" readonly name="memo">${(entity.memo)!''}</textarea></td>
                </tr>
                <tr>
                    <td style="font-weight: 600;height: 32px;line-height: 32px;font-size: 16px;">上线入口</td>
                </tr>
                <tr>
                    <td align="left">*上线状态：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="upLineStatus" id="up_line_status_2" disabled
                                   <#if entity.upLineStatus?? && entity.upLineStatus == 2>checked</#if> value="2"/><label for="up_line_status_2">上线</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="upLineStatus" id="up_line_status_0" disabled
                                   <#if entity.upLineStatus?? && entity.upLineStatus == 2><#else >checked</#if> value="0" /><label for="up_line_status_0">下线</label>
                        </span>
                    </td>
                    <td align="right">*上线时间：</td>
                    <td>
                        <input type="text" id="up_line_time_${pid}" editable="false" readonly class="text easyui-datetimebox" name="upLineTime"  value="${(entity.upLineTime?string('yyyy-MM-dd HH:mm:ss'))!''}" style="width: 183px; height: 28px;"/>
                    </td>
                </tr>
                <tr>
                    <td style="font-weight: 600;height: 32px;line-height: 32px;font-size: 16px;">安全设置</td>
                </tr>
                <tr>
                    <td width="140"  align="left">灭火器状态：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isFpOpen" disabled
                                   <#if entity.isFpOpen?? && entity.isFpOpen == 0>checked</#if> id="is_fp_open_0"
                                   value="0"/><label for="is_fp_open_0">关闭</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="isFpOpen" disabled
                                   <#if entity.isFpOpen?? && entity.isFpOpen == 1>checked</#if> id="is_fp_open_1"
                                   value="1"/><label for="is_fp_open_1">开启</label>
                        </span>
                    </td>
                    <td width="140" align="right">水位状态：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="waterLevel" id="water_level_0" disabled
                                   <#if entity.waterLevel?? && entity.waterLevel == 0>checked</#if> value="0"/><label for="water_level_0">正常</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="waterLevel" id="water_level_1" disabled
                                   <#if entity.waterLevel?? && entity.waterLevel == 1>checked</#if> value="1" /><label for="water_level_1">超水位</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td width="140"  align="left">烟雾传感器：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="smokeState"  id="smoke_state_0" disabled
                                   <#if entity.smokeState ?? && entity.smokeState == 0>checked</#if>/><label for="smoke_state_0">关闭</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="smokeState" id="smoke_state_1" disabled
                                   <#if entity.smokeState ?? && entity.smokeState == 1>checked</#if>/><label for="smoke_state_1">开启</label>
                        </span>
                    </td>
                    <td align="right">交流电输入状态：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="acVoltageState" id="ac_voltage_state_0" disabled
                                   <#if entity.acVoltageState?? && entity.acVoltageState == 0>checked</#if> value="0"/><label for="ac_voltage_state_0">正常</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="acVoltageState" id="ac_voltage_state_1" disabled
                                   <#if entity.acVoltageState?? && entity.acVoltageState == 1>checked</#if> value="1" /><label for="ac_voltage_state_1">断电</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td width="140" align="left">输入AC电压：</td>
                    <td><input type="text" class="text easyui-numberspinner" readonly name="acVoltage"  style="width: 183px; height: 28px;" required="true" value="${(entity.acVoltage)!0}" /></td>
                </tr>
                <tr>
                    <td width="140" align="left">*最大充电功率：</td>
                    <td><input type="text" class="text easyui-numberspinner" readonly name="maxChargePower"  style="width: 183px; height: 28px;" required="true" value="${(entity.maxChargePower)!0}" /></td>
                    <td width="140" align="right">*最大充电数量：</td>
                    <td><input type="text" class="text easyui-numberspinner" readonly name="maxChargeCount" data-options="min:1, max:100" style="width: 183px; height: 28px;" required="true" value="${(entity.maxChargeCount)!0}" /></td>
                </tr>
                <tr>
                    <td width="140" align="left">格口功率范围：</td>
                    <td><input type="text" class="text easyui-numberspinner" readonly name="boxMinPower" style="width: 85px; height: 28px;" required="true" value="${(entity.boxMinPower)!0}" />&nbsp;-&nbsp;<input type="text" class="text easyui-numberspinner" readonly name="boxMaxPower" style="width: 85px; height: 28px;" value="${(entity.boxMaxPower)!''}"/></td>
                    <td width="140" align="right">自停涓流时间(秒)：</td>
                    <td><input type="text" class="text easyui-numberspinner" readonly   name="boxTrickleTime" style="width: 183px; height: 28px;" value="${(entity.boxTrickleTime)!''}"/></td>
                </tr>
                <tr>
                    <td width="140" align="left">开启风扇温度：</td>
                    <td><input type="text" class="text easyui-numberspinner" readonly name="activeFanTemp" data-options="min:1, max:100" style="width: 184px; height: 28px;" required="true" value="${(entity.activeFanTemp)!0}" /></td>
                    <td align="right">回充电量：</td>
                    <td><input type="text" class="text easyui-numberspinner" name="recoilVolume" readonly
                               data-options="min:0, max:100" style="width: 183px; height: 28px;" required="true"
                               value="${(entity.recoilVolume)!0}"/></td>
                </tr>
                <tr>
                    <td width="70" align="left">二维码地址：</td>
                    <td colspan="3"><textarea style="width:505px;height:60px;" maxlength="450" readonly >${(qrCodeAddress)!''}</textarea></td>
                </tr>
                <tr>
                    <td align="left">动态码：</td>
                    <td>
                        <input name="dynamicCode" id="dynamic_code_${pid}" readonly value="${(entity.dynamicCode)!''}" maxlength="4" style="width: 185px;height: 28px;" type="text" class="easyui-numberbox">
                    </td>
                    <td align="right">子类型：</td>
                    <td>
                        <select name="subtype" id="sub_type_${pid}" disabled style="width: 182px; height: 28px;">
                        <#list SubtypeEnum as s>
                            <option value="${s.getValue()}"
                                    <#if entity.subtype==s.getValue()>selected="selected"</#if>>${s.getName()}</option>
                        </#list>
                        </select>
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