<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <div class="popup_body">
                <div class="report_table">
                    <table>
                        <tbody>
                        <tr>
                            <th colspan="2">换电柜${(entity.cabinetId)!''}格口${(entity.boxNum)!''}的充电器信息</th>
                        </tr>
                        <tr>
                            <td width="25%">充电器版本:</td>
                            <td width="75%">${(entity.chargerVersion)!''}</td>
                        </tr>
                        <tr>
                            <td width="20%">充电器型号:</td>
                            <td width="80%">${(entity.chargerModule)!''}</td>
                        </tr>
                        <tr>
                            <td width="20%">充电状态:</td>
                            <td width="80%"><#if (entity.chargeState)?? >${(entity.chargeStateName)!''}</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">充电阶段:</td>
                            <td width="80%"><#if (entity.chargeStage)?? >${(entity.chargeStageName)!''}</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">充电时长:</td>
                            <td width="80%">${(entity.chargeTime)!''}</td>
                        </tr>
                        <tr>
                            <td width="20%">充电器输出电压:</td>
                            <td width="80%"><#if (entity.chargeVoltage)?? >${(entity.chargeVoltage)!0}mV</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">电池端检测电压:</td>
                            <td width="80%"><#if (entity.batteryVoltage)?? >${(entity.batteryVoltage)!0}mV</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">充电器输出电流:</td>
                            <td width="80%"><#if (entity.chargeCurrent)?? >${(entity.chargeCurrent)!0}mA</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">变压器温度:</td>
                            <td width="80%"><#if (entity.transformerTemp)?? >${entity.transformerTemp}℃</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">散热片温度:</td>
                            <td width="80%"><#if (entity.heatsinkTemp)?? >${entity.heatsinkTemp}℃</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">环境温度:</td>
                            <td width="80%"><#if (entity.ambientTemp)?? >${entity.ambientTemp}℃</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">充电器故障:</td>
                            <td width="80%"><#if (entity.chargerFault)?? >${(entity.chargerFaultName)!''}</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">BMS停止原因:</td>
                            <td width="80%"><#if (entity.chargerFault)?? >${(entity.bmsStopFaultName)!''}</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">创建时间:</td>
                            <td width="80%"><#if (entity.createTime)?? >${app.format_date_time(entity.createTime)!''}</#if></td>
                        </tr>
                        <tr>
                            <th colspan="2">其他参数信息</th>
                        </tr>
                        <tr>
                            <td width="20%">是否充电:</td>
                            <td width="80%"><#if (entity.enableCharge)?? >${(entity.enableChargeName)!''}</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">是否开启电池数据连接：</td>
                            <td width="80%"><#if (entity.enableLink)?? >${(entity.enableLinkName)!''}</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">自动选择充电电压：</td>
                            <td width="80%"><#if (entity.autoSwtichMode)?? >${(entity.autoSwtichModeName)!''}</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">阶段1(预充)最大充电电压U2：</td>
                            <td width="80%"><#if (entity.maxChargeVoltageOfStage1)?? >${(entity.maxChargeVoltageOfStage1)!0}mV</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">阶段1(预充)充电电流：</td>
                            <td width="80%"><#if (entity.chargeCurrentOfStage1)?? >${((entity.chargeCurrentOfStage1)!0)}mA</#if></td>
                        </tr>
                        <tr>
                            <td width="25%">阶段2(恒流1)最大充电电压U3：</td>
                            <td width="75%"><#if (entity.maxChargeVoltageOfStage2)?? >${(entity.maxChargeVoltageOfStage2)!0}mV</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">阶段2(恒流1)充电电流：</td>
                            <td width="80%"><#if (entity.chargeCurrentOfStage2)?? >${(entity.chargeCurrentOfStage2)!0}mA</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">阶段3开始改变电流时电压：</td>
                            <td width="80%"><#if (entity.slopeVoltage)?? >${(entity.slopeVoltage)!0}mV</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">阶段3(恒流2)充电电流：</td>
                            <td width="80%"><#if (entity.chargeCurrentOfStage3)?? >${(entity.chargeCurrentOfStage3)!0}mA</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">电池满电电压：</td>
                            <td width="80%"><#if (entity.fullVoltage)?? >${(entity.fullVoltage)!0}mV</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">满电电压时斜率电流：</td>
                            <td width="80%"><#if (entity.slopeCurrent)?? >${(entity.slopeCurrent)!0}mA</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">阶段4最小充电电流：</td>
                            <td width="80%"><#if (entity.minCurrentOfStage4)?? >${(entity.minCurrentOfStage4)!''}mA</#if></td>
                        </tr>
                        <tr>
                            <td width="20%">低温环境充电模式：</td>
                            <td width="80%"><#if (entity.lowTemperatureMode)?? >${(entity.lowTemperatureModeName)!''}</#if></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');
        win.find('button.close').click(function() {
            win.window('close');
        });

    })()

</script>