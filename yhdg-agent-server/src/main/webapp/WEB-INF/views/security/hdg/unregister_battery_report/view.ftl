<div class="tab_item" style="display:block;">
    <div class="ui_table">
        <form>
            <div class="popup_body">
                    <div class="report_table">
                        <table>
                            <tbody>
                            <tr>
                                <th colspan="2">${(entity.code)!''}的信息</th>
                            </tr>
                            <tr>
                                <td width="20%">BMS版本:</td>
                                <td width="80%">${(entity.version)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">位置类型:</td>
                                <td width="80%">${(entity.locTypeName)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">当前信号:</td>
                                <td width="80%">${(entity.currentSignal)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">信号类型:</td>
                                <td width="80%">${(entity.signalTypeName)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">经纬度:</td>
                                <td width="80%"><#if (entity.lng)?? >${entity.lng}/</#if>${(entity.lat)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">总电压:</td>
                                <td width="80%"><#if (entity.voltage)?? >${entity.voltage/1000}V</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">电流:</td>
                                <td width="80%"><#if (entity.electricity)?? >${entity.electricity/1000}A</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">电芯串数:</td>
                                <td width="80%">${(entity.serials)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">单体电压(mv):</td>
                                <td width="80%">${(entity.singleVoltage)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">电芯均衡状态:</td>
                                <td width="80%">${(entity.balanceName)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">温度（℃）:</td>
                                <td width="80%">${(entity.temp)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">当前容量：</td>
                                <td width="80%"><#if (entity.currentCapacity)?? >${entity.currentCapacity/1000}AH</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">电量：</td>
                                <td width="80%"><#if (entity.volume)?? >${entity.volume}%</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">循环次数：</td>
                                <td width="80%">${(entity.circle)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">MOS：</td>
                                <td width="80%">${(entity.mosName)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">故障类型：</td>
                                <td width="80%">${(entity.faultName)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">心跳间隔：</td>
                                <td width="80%"><#if (entity.heartInterval)?? >${entity.heartInterval}秒</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">运动值：</td>
                                <td width="80%">${(entity.isMotion)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">开盖状态：</td>
                                <td width="80%"><#if (entity.uncapState)?? && (entity.uncapState == 1) >开盖<#else >未开盖</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">工作状态(0正常1待机)：</td>
                                <td width="80%"><#if (entity.energyState)?? && (entity.energyState == 1) >待机<#else >正常</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">保护次数：</td>
                                <td width="80%">${(entity.protectName)!''}</td>
                            </tr>

                            <tr>
                                <th colspan="2">其他参数信息</th>
                            </tr>
                            <tr>
                                <td width="20%">Sim卡CCID号:</td>
                                <td width="80%"><#if (entity.simCode)?? >${entity.simCode}</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">电池型号：</td>
                                <td width="80%">${(entity.cellModel)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">电芯厂家：</td>
                                <td width="80%">${(entity.cellMfr)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">电池厂家：</td>
                                <td width="80%">${(entity.battMfr)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">生产日期：</td>
                                <td width="80%">${(entity.mfd)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">BMS型号:</td>
                                <td width="80%">${(entity.bmsModel)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">材质:</td>
                                <td width="80%">${(entity.materialName)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">电池类型:</td>
                                <td width="80%">${(entity.battTypeName)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">标准容量:</td>
                                <td width="80%"><#if (entity.nominalCapacity)?? >${entity.nominalCapacity/1000}AH</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">循环容量：</td>
                                <td width="80%"><#if (entity.circleCapacity)?? >${entity.circleCapacity/1000}AH</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">单体充满电压：</td>
                                <td width="80%"><#if (entity.cellFullVol)?? >${entity.cellFullVol/1000}V</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">单体截止电压：</td>
                                <td width="80%"><#if (entity.cellCutVol)?? >${entity.cellCutVol/1000}V</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">自放电率：</td>
                                <td width="80%"><#if (entity.selfDsgRate)?? >${entity.selfDsgRate/10}%</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">开路电压（mV）：</td>
                                <td width="80%"><#if (entity.ocvTable)?? >${entity.ocvTable}</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">单体过压保护阈值：</td>
                                <td width="80%"><#if (entity.cellOvTrip)?? >${entity.cellOvTrip/1000}V</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">单体过压恢复阈值：</td>
                                <td width="80%"><#if (entity.cellOvResume)?? >${entity.cellOvResume/1000}V</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">单体过压延时：</td>
                                <td width="80%"><#if (entity.cellOvDelay)?? >${entity.cellOvDelay}S</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">单体欠压保护阈值：</td>
                                <td width="80%"><#if (entity.cellUvTrip)?? >${entity.cellUvTrip/1000}V</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">单体欠压恢复阈值：</td>
                                <td width="80%"><#if (entity.cellUvResume)?? >${entity.cellUvResume/1000}V</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">单体欠压延时：</td>
                                <td width="80%"><#if (entity.cellUvDelay)?? >${entity.cellUvDelay}S</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">整体过压保护阈值：</td>
                                <td width="80%"><#if (entity.packOvTrip)?? >${entity.packOvTrip/1000}V</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">整体过压恢复阈值：</td>
                                <td width="80%"><#if (entity.packOvResume)?? >${entity.packOvResume/1000}V</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">整体过压延时：</td>
                                <td width="80%"><#if (entity.packOvDelay)?? >${entity.packOvDelay}S</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">整体欠压保护阈值：</td>
                                <td width="80%"><#if (entity.packUvTrip)?? >${entity.packUvTrip/1000}V</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">整体欠压恢复阈值：</td>
                                <td width="80%"><#if (entity.packUvResume)?? >${entity.packUvResume/1000}V</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">整体欠压延时：</td>
                                <td width="80%"><#if (entity.packUvDelay)?? >${entity.packUvDelay}S</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">充电高温保护阈值：</td>
                                <td width="80%"><#if (entity.chgOtTrip)?? >${entity.chgOtTrip}K</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">充电高温恢复阈值：</td>
                                <td width="80%"><#if (entity.chgOtResume)?? >${entity.chgOtResume}K</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">充电高温延时：</td>
                                <td width="80%"><#if (entity.chgOtDelay)?? >${entity.chgOtDelay}S</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">充电低温保护阈值：</td>
                                <td width="80%"><#if (entity.chgUtTrip)?? >${entity.chgUtTrip}K</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">充电低温恢复阈值：</td>
                                <td width="80%"><#if (entity.chgUtResume)?? >${entity.chgUtResume}K</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">充电低温延时：</td>
                                <td width="80%"><#if (entity.chgUtDelay)?? >${entity.chgUtDelay}S</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">放电高温保护阈值：</td>
                                <td width="80%"><#if (entity.dsgOtTrip)?? >${entity.dsgOtTrip}K</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">放电高温恢复阈值：</td>
                                <td width="80%"><#if (entity.dsgOtResume)?? >${entity.dsgOtResume}K</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">放电高温延时：</td>
                                <td width="80%"><#if (entity.dsgOtDelay)?? >${entity.dsgOtDelay}S</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">放电低温保护阈值：</td>
                                <td width="80%"><#if (entity.dsgUtTrip)?? >${entity.dsgUtTrip}K</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">放电低温恢复阈值：</td>
                                <td width="80%"><#if (entity.dsgUtResume)?? >${entity.dsgUtResume}K</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">放电低温延时：</td>
                                <td width="80%"><#if (entity.dsgUtDelay)?? >${entity.dsgUtDelay}S</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">充电过流保护阈值：</td>
                                <td width="80%"><#if (entity.chgOcTrip)?? >${entity.chgOcTrip/1000}A</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">充电过流延时：</td>
                                <td width="80%"><#if (entity.chgOcDelay)?? >${entity.chgOcDelay}S</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">充电过流释放时间：</td>
                                <td width="80%"><#if (entity.chgOcRelease)?? >${entity.chgOcRelease}S</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">放电过流保护阈值：</td>
                                <td width="80%"><#if (entity.dsgOcTrip)?? >${entity.dsgOcTrip/1000}A</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">放电过流延时：</td>
                                <td width="80%"><#if (entity.dsgOcDelay)?? >${entity.dsgOcDelay}S</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">放电过流释放时间：</td>
                                <td width="80%"><#if (entity.dsgOcRelease)?? >${entity.dsgOcRelease}S</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">硬件过流：</td>
                                <td width="80%">${(entity.rsns)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">硬件过流保护阈值：</td>
                                <td width="80%">${(entity.hardOcTripName)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">硬件过流保护延时：</td>
                                <td width="80%">${(entity.hardOcDelayName)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">短路保护阈值：</td>
                                <td width="80%">${(entity.SCTripName)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">短路保护延时：</td>
                                <td width="80%">${(entity.SCDelayName)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">硬件单体过压保护阈值：</td>
                                <td width="80%"><#if (entity.hardOvTrip)?? >${entity.hardOvTrip/1000}V</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">硬件单体过压保护延时：</td>
                                <td width="80%">${(entity.hardOvDelayName)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">硬件单体欠压保护阈值：</td>
                                <td width="80%"><#if (entity.hardUvTrip)?? >${entity.hardUvTrip/1000}V</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">硬件单体欠压保护延时：</td>
                                <td width="80%">${(entity.hardUvDelayName)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">短路释放时间：</td>
                                <td width="80%">${(entity.sdRelease)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">功能配置：</td>
                                <td width="80%">${(entity.functionName)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">NTC配置：</td>
                                <td width="80%">${(entity.ntcConfigName)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">电流采样电阻值：</td>
                                <td width="80%">${(entity.sampleR)!''}</td>
                            </tr>
                            <tr>
                                <td width="20%">待机时心跳间隔：</td>
                                <td width="80%"><#if (entity.stdInterval)?? >${entity.stdInterval}秒</#if></td>
                            </tr>
                            <tr>
                                <td width="20%">总容量：</td>
                                <td width="80%"><#if (entity.totalCapacity)?? >${entity.totalCapacity/1000}AH</#if></td>
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