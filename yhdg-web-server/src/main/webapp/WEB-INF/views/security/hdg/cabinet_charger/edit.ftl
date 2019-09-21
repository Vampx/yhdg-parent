<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="cabinetId" value="${(entity.cabinetId)!''}">
            <input type="hidden" name="boxNum" value="${(entity.boxNum)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td align="left">是否充电：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="enableCharge" id="enable_charge_1" <#if entity.enableCharge?? && entity.enableCharge == 1>checked</#if> value="1"/><label for="enable_charge_1">是</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="enableCharge" id="enable_charge_0" <#if entity.enableCharge?? && entity.enableCharge == 1><#else >checked</#if> value="0"/><label for="enable_charge_0">否</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="left">是否开启电池数据连接：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="enableLink" id="enable_link_1" <#if entity.enableLink?? && entity.enableLink == 1>checked</#if> value="1"/><label for="enable_link_1">是</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="enableLink" id="enable_link_0" <#if entity.enableLink?? && entity.enableLink == 1><#else >checked</#if> value="0"/><label for="enable_link_0">否</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="left">自动选择充电电压：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="autoSwtichMode" id="auto_swtich_mode_1" <#if entity.autoSwtichMode?? && entity.autoSwtichMode == 1>checked</#if> value="1"/><label for="auto_swtich_mode_1">开启</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="autoSwtichMode" id="auto_swtich_mode_0" <#if entity.autoSwtichMode?? && entity.autoSwtichMode == 1><#else >checked</#if> value="0"/><label for="auto_swtich_mode_0">关闭</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="left">阶段1(预充)最大充电电压：</td>
                    <td><input type="text" class="easyui-numberspinner" style="width: 182px; height: 28px;" name="maxChargeVoltageOfStage1" required
                               data-options="min:0" value="${(entity.maxChargeVoltageOfStage1)!''}"/>mV</td>
                </tr>
                <tr>
                    <td align="left">阶段1(预充)充电电流：</td>
                    <td><input type="text" class="easyui-numberspinner" style="width: 182px; height: 28px;" name="chargeCurrentOfStage1" required
                               data-options="min:1000,max:6000" value="${(entity.chargeCurrentOfStage1)!''}"/>mA</td>
                </tr>
                <tr>
                    <td align="left">阶段2(恒流1)最大充电电压U3：</td>
                    <td><input type="text" class="easyui-numberspinner" style="width: 182px; height: 28px;" name="maxChargeVoltageOfStage2" required
                               data-options="min:0" value="${(entity.maxChargeVoltageOfStage2)!''}"/>mV</td>
                </tr>
                <tr>
                    <td align="left">阶段2(恒流1)充电电流：</td>
                    <td><input type="text" class="easyui-numberspinner" style="width: 182px; height: 28px;" name="chargeCurrentOfStage2" required
                               data-options="min:1000,max:12000" value="${(entity.chargeCurrentOfStage2)!''}"/>mA</td>
                </tr>
                <tr>
                    <td align="left">阶段3开始改变电流时电压：</td>
                    <td><input type="text" class="easyui-numberspinner" style="width: 182px; height: 28px;" name="slopeVoltage" required
                               data-options="min:0" value="${(entity.slopeVoltage)!''}"/>mV</td>
                </tr>
                <tr>
                    <td align="left">阶段3(恒流2)充电电流：</td>
                    <td><input type="text" class="easyui-numberspinner" style="width: 182px; height: 28px;" name="chargeCurrentOfStage3" required
                               data-options="min:1000,max:10000" value="${(entity.chargeCurrentOfStage3)!''}"/>mA</td>
                </tr>
                <tr>
                    <td align="left">电池满电电压：</td>
                    <td><input type="text" class="easyui-numberspinner" style="width: 182px; height: 28px;" name="fullVoltage" required
                               data-options="min:0" value="${(entity.fullVoltage)!''}"/>mV</td>
                </tr>
                <tr>
                    <td align="left">满电电压时斜率电流：</td>
                    <td><input type="text" class="easyui-numberspinner" style="width: 182px; height: 28px;" name="slopeCurrent" required
                               data-options="min:1000" value="${(entity.slopeCurrent)!''}"/>mA</td>
                </tr>
                <tr>
                    <td align="left">阶段4最小充电电流：</td>
                    <td><input type="text" class="easyui-numberspinner" style="width: 182px; height: 28px;" name="minCurrentOfStage4" required
                               data-options="min:50,max:3000" value="${(entity.minCurrentOfStage4)!''}"/>mA</td>
                </tr>
                <tr>
                    <td align="left">低温环境充电模式：</td>
                    <td>
                         <span class="radio_box">
                            <input type="radio" class="radio" name="lowTemperatureMode" id="low_temperature_mode_0" <#if entity.lowTemperatureMode?? && entity.lowTemperatureMode == 0>checked</#if> value="0"/><label for="low_temperature_mode_0">不允许充电</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="lowTemperatureMode" id="low_temperature_mode_1" <#if entity.lowTemperatureMode?? && entity.lowTemperatureMode == 1>checked</#if> value="1"/><label for="low_temperature_mode_1">以1/2的电流进行充电</label>
                        </span>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="lowTemperatureMode" id="low_temperature_mode_2" <#if entity.lowTemperatureMode?? && entity.lowTemperatureMode == 2>checked</#if> value="2"/><label for="low_temperature_mode_2">正常充电</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="left">是否异常：</td>
                    <td>
                         <span class="radio_box">
                            <input type="radio" class="radio" name="abnormal" id="abnormal_1" <#if entity.abnormal?? && entity.abnormal == 1>checked</#if> value="1"/><label for="abnormal_1">异常</label>
                        </span>
                         <span class="radio_box">
                            <input type="radio" class="radio" name="abnormal" id="abnormal_0" <#if entity.abnormal?? && entity.abnormal == 0>checked</#if> value="0"/><label for="abnormal_0">正常</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td align="left">使能NFC检测：</td>
                    <td>
                        <span class="radio_box">
                            <input type="radio" class="radio" name="enableNfc" id="enable_nfc_1" <#if entity.enableNfc?? && entity.enableNfc == 1>checked</#if> value="1"/><label for="enable_nfc_1">使能</label>
                        </span>
                         <span class="radio_box">
                            <input type="radio" class="radio" name="enableNfc" id="enable_nfc_0" <#if entity.enableNfc?? && entity.enableNfc == 0>checked</#if> value="0"/><label for="enable_nfc_0">禁用</label>
                        </span>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.ok').click(function() {
            var slopeCurrent = win.find('input[name=slopeCurrent]').val();
            var currentOfStage3 = win.find('input[name=currentOfStage3]').val();
            if(slopeCurrent > currentOfStage3) {
                $.messager.alert('提示信息', '满电电压时斜率电流不能大于阶段3(恒流2)充电电流', 'info');
                return;
            }else{
                form.form('submit', {
                    url: '${contextPath}/security/hdg/cabinet_charger/update.htm',
                    success: function(text) {
                        var json = $.evalJSON(text);
                    <@app.json_jump/>
                        if(json.success) {
                            win.window('close');
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
                        }
                    }
                });
            }
        });
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()
</script>
