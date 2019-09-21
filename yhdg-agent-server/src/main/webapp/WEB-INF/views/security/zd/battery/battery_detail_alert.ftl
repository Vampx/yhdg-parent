<style type="text/css">
    .main .left_bar {
        height: 0%;
    }
    .param-three .combo-arrow{
        margin-top: 20px;
        margin-left: 10px;
    }
</style>
    <#function show_temp temp >
        <#return (temp - 2731) / 10 >
    </#function>
    <div class="main" style="top:25px">
        <div class="batteryDetail">
            <div class="battery-c-detail">
                <div class="battery-c-detail-up">
                    <div class="c-d-tips">
                        <span><i></i>电池详情</span>
                        <button class="btn btn_blue" onclick="javascript:$('[id^=win_]').window('destroy');">关闭</button>
                    </div>
                    <div class="c-d-two">
                        <span>电池编号：${(entity.id)!''}</span>
                        <span>电池SN：${(entity.code)!''}</span>
                        <span style="margin-left: 20px;">外壳编号：${(entity.shellCode)!''}</span>
                    </div>
                    <div class="c-d-three-tabs">
                        <a href="#">
                            <span class="active" data-id="0">基本信息</span>
                        </a>
                        <a href="#">
                            <span data-id="1">参数设置</span>
                        </a>
                        <a href="#" id="ride_order_map">
                            <span data-id="2">运动轨迹</span>
                        </a>
                        <a href="#" id="fault_log">
                            <span data-id="3">告警信息</span>
                        </a>
                        <a href="#" id="battery_report">
                            <span data-id="4">上报信息</span>
                        </a>
                    </div>
                </div>


                <div class="battery-c-detail-down" style="overflow: auto;">
                    <div class="c-d-three-0">
                        <div class="cdtc-left">
                            <div class="cdtc-left-one">
                                <span>总电压：
                                    <#if entity.voltage??>
                                    ${(entity.voltage/1000)!''}
                                    </#if> V</span>
                                <span>总电流：<#if entity.electricity??>${(entity.electricity/1000)!''}</#if> A</span>
                            </div>
                            <div class="cdtc-left-two">
                                <div>
                                    <i></i>电池控制
                                </div>
                                <#--<div>-->
                                    <#--电池租期：无-->
                                <#--</div>-->
                                <div>
                                    MOS管状态：${(batteryParameter.mosName)!''}
                                </div>
                                <#--<div>-->
                                    <#--控制：-->
                                    <#--<select>-->
                                        <#--<option>控制</option>-->
                                        <#--<option>不控制</option>-->
                                    <#--</select>-->
                                <#--</div>-->
                            </div>
                            <div class="cdtc-left-box">
                                <div>
                                    <i></i>电池容量
                                </div>
                                <div class="cdtc-text">
                                    额定容量：
                                <#if batteryParameter ?? && batteryParameter.nominalCapacity ??>
                                ${(batteryParameter.nominalCapacity)!''}
                                </#if>
                                    mAH
                                </div>
                                <div class="cdtc-text">
                                    剩余容量：
                                <#if batteryParameter ?? && batteryParameter.currentCapacity ??>
                                ${(batteryParameter.currentCapacity)!''}
                                </#if>
                                    mAH
                                </div>
                                <div class="cdtc-text">
                                    soc：<#if (entity.volume)?? >${entity.volume}%</#if>
                                </div>
                                <div class="cdtc-text">
                                    循环次数：${(batteryParameter.circle)!''}次
                                </div>
                            </div>
                            <div class="cdtc-left-box">
                                <div>
                                    <i></i>网络信息
                                </div>
                                <div class="cdtc-text">
                                    网络类型：
                                    <#if entity.signalType ??>
                                        <#if entity.signalType == 0>2g</#if>
                                        <#if entity.signalType == 1>3g</#if>
                                        <#if entity.signalType == 2>4g</#if>
                                        <#if entity.signalType == 3>NBIo</#if>
                                    </#if>
                                </div>
                                <div class="cdtc-text">
                                    经度：${(batteryParameter.lng)!''}
                                </div>
                                <div class="cdtc-text">
                                    纬度：${(batteryParameter.lat)!''}
                                </div>
                                <div class="cdtc-text">
                                    信号：${(batteryParameter.currentSignal)!''}
                                </div>
                            </div>
                            <div class="cdtc-left-box">
                                <div>
                                    <i></i>温度信息
                                </div>
                                <#--<div class="cdtc-text">-->
                                    <#--BMS温度：46℃-->
                                <#--</div>-->
                                <#if tempList ?? && tempList?size &gt; 0>
                                    <#list tempList as temp>
                                        <div class="cdtc-text">
                                            电芯温度${temp_index + 1}: ${temp} ℃
                                        </div>
                                    </#list>
                                </#if>
                            </div>
                        </div>
                        <div class="cdtc-right">
                            <div class="cdtc-right-one">
                                <i></i>单体电压信息
                            </div>
                            <div class="cdtc-right-two">
                                <span>平均电压：<#if averageVoltage??>${(averageVoltage/1000)!''}</#if>V</span>
                                <span>最高电压：<#if averageVoltage??>${(maxVoltage/1000)!''}</#if>V</span>
                                <span>最低电压：<#if averageVoltage??>${(minVoltage/1000)!''}</#if>V</span>
                                <span>压差：<#if averageVoltage??>${(voltageRange)!''}</#if>mV</span>
                                <span>电芯串数：${(batteryParameter.serials)!''}</span>
                            </div>
                            <div class="cdtc-right-three">
                                <table>
                                    <thead>
                                    <tr>
                                        <td>序号</td>
                                        <td>电压</td>
                                        <td>最高压差</td>
                                        <td>最低压差</td>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        <#if mapList?? && mapList?size &gt; 0>
                                            <#list mapList as map>
                                            <tr>
                                                <td>${map["no"]!''}</td>
                                                <td>${(map["voltage"]/1000)!''}V</td>
                                                <td>${map["maxVoltageRange"]!''}mV</td>
                                                <td>${map["minVoltageRange"]!''}mV</td>
                                            </tr>
                                            </#list>
                                        </#if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <div class="c-d-three-1" style="display: none;">

                        <div class="param-one">
                            <button class="btn btn_blue ok" onclick="seeLog()">参数修改记录</button>&nbsp;
                            <button class="btn btn_red ok" id="ok_update">确定</button>
                            <#--<button class="btn btn_border close">关闭</button>-->
                            <div>
                            最后同步时间 <b><#if (batteryParameter.shortReportTime)??>短心跳：${app.format_date_time(batteryParameter.shortReportTime)}</#if>&nbsp; <#if (batteryParameter.longReportTime)??>长心跳：${app.format_date_time(batteryParameter.longReportTime)}</#if></b>
                            </div>
                        </div>
                        <form id="battery_parameter" >
                        <div class="param-two">
                            <div>
                                <i></i>锁定显示
                            </div>
                            <div>
                                MOS：
                                <td><input type="text" class="text"  name="mosName" value="${(batteryParameter.mosName)!''}"
                                           style="width: 100px; height: 28px;" disabled/></td>
                                <span>
                                    <input type="checkbox" class="checkbox upBms_checkbox" <#if batteryParameter.upBms?? && batteryParameter.upBms == 1>checked</#if> /><label>立即上传BMS参数</label>
                                </span>
                            </div>
                        </div>
                        <div class="param-three">
                            <div>
                                <i></i>心跳配置
                            </div>
                            <div>
                                <span>
                                    <b>运动心跳间隔：</b><input type="text" class="text" name="heartInterval" value="${(batteryParameter.heartInterval)!''}" style="width: 50px; height: 28px;"/>S
                                </span>
                                <span>
                                    <b>静止心跳间隔：</b><input type="text" class="text" name="motionless" value="${(batteryParameter.motionless)!''}" style="width: 50px; height: 28px;"/>S
                                </span>
                                <span>
                                    <b>存储心跳间隔：</b><input type="text" class="text" name="standby" value="${(batteryParameter.standby)!''}" style="width: 50px; height: 28px;"/>S
                                </span>
                                <span>
                                    <b>休眠心跳间隔：</b><input type="text" class="text" name="dormancy" value="${(batteryParameter.dormancy)!''}" style="width: 50px; height: 28px;"/>S
                                </span>
                            </div>
                        </div>
                        <div class="param-three">
                            <div>
                                <i></i>容量配置
                            </div>
                            <div>
                                <span>
                                    <b>标称容量：</b><input type="text" class="text"  name="nominalCapacity" value="${(batteryParameter.nominalCapacity)!''}" style="width: 50px; height: 28px;"/>mAh
                                </span>
                                <span>
                                    <b>循环容量：</b><input type="text" class="text" name="circleCapacity" value="${(batteryParameter.circleCapacity)!''}" style="width: 50px; height: 28px;"/>mAh
                                </span>
                                <span>
                                    <b>自放电率：</b><input type="text" class="text"  name="selfDsgRate" value="${(batteryParameter.selfDsgRate)!''}" style="width: 50px; height: 28px;"/>%
                                </span>
                                <span>
                                    <b>单体充满电压：</b><input type="text" class="text" name="cellFullVol" value="${(batteryParameter.cellFullVol)!''}" style="width: 50px; height: 28px;"/>mV
                                </span>
                                <span>
                                    <b>单体截止电压：</b><input type="text" class="text" name="cellCutVol" value="${(batteryParameter.cellCutVol)!''}" style="width: 50px; height: 28px;"/>mV
                                </span>

                            </div>
                        </div>
                        <div class="param-four">
                            <div>
                                <i></i>基本保护参数配置
                            </div>
                            <div>
                                <span>
                                    <b>单体过压：</b><input type="text" class="text"  name="cellOvTrip" value="${(batteryParameter.cellOvTrip)!''}" style="width: 50px; height: 28px;"/>mV
                                </span>
                                <span>
                                    <b>释放电压：</b><input type="text" class="text" name="cellOvResume" value="${(batteryParameter.cellOvResume)!''}" style="width: 50px; height: 28px;"/>mV
                                </span>
                                <span>
                                    <b>延时：</b><input type="text" class="text" name="cellOvDelay" value="${(batteryParameter.cellOvDelay)!''}" style="width: 50px; height: 28px;"/>S
                                </span>

                                <span>
                                    <b>单体欠压：</b><input type="text" class="text"  name="cellUvTrip" value="${(batteryParameter.cellUvTrip)!''}" style="width: 50px; height: 28px;"/>mV
                                </span>
                                <span>
                                    <b>释放电压：</b><input type="text" class="text" name="cellUvResume" value="${(batteryParameter.cellUvResume)!''}" style="width: 50px; height: 28px;"/>mV
                                </span>
                                <span>
                                    <b>延时：</b><input type="text" class="text" name="cellUvDelay" value="${(batteryParameter.cellUvDelay)!''}" style="width: 50px; height: 28px;"/>S
                                </span>

                                <span>
                                    <b>整组过压：</b><input type="text" class="text"  name="packOvTrip" value="${(batteryParameter.packOvTrip)!''}" style="width: 50px; height: 28px;"/>mV
                                </span>
                                <span>
                                    <b>释放电压：</b><input type="text" class="text" name="packOvResume" value="${(batteryParameter.packOvResume)!''}" style="width: 50px; height: 28px;"/>mV
                                </span>
                                <span>
                                    <b>延时：</b><input type="text" class="text" name="packOvDelay" value="${(batteryParameter.packOvDelay)!''}" style="width: 50px; height: 28px;"/>S
                                </span>

                                <span>
                                    <b>整组欠压：</b><input type="text" class="text"  name="packUvTrip" value="${(batteryParameter.packUvTrip)!''}" style="width: 50px; height: 28px;"/>mV
                                </span>
                                <span>
                                    <b>释放电压：</b><input type="text" class="text" name="packUvResume" value="${(batteryParameter.packUvResume)!''}" style="width: 50px; height: 28px;"/>mV
                                </span>
                                <span>
                                    <b>延时：</b><input type="text" class="text" name="packUvDelay" value="${(batteryParameter.packUvDelay)!''}" style="width: 50px; height: 28px;"/>S
                                </span>

                                <span>
                                    <b>充电高温：</b><input type="text" class="text"  name="chgOtTrip" value="<#if (batteryParameter.chgOtTrip)?? >${show_temp(batteryParameter.chgOtTrip)}</#if>"
                                                       style="width: 50px; height: 28px;"/> ℃
                                </span>
                                <span>
                                    <b>释放温度：</b><input type="text" class="text" name="chgOtResume" value="<#if (batteryParameter.chgOtResume)?? >${show_temp(batteryParameter.chgOtResume)}</#if>"
                                                       style="width: 50px; height: 28px;"/> ℃
                                </span>
                                <span>
                                    <b>延时：</b><input type="text" class="text" name="chgOtDelay" value="${(batteryParameter.chgOtDelay)!''}"
                                                     style="width: 50px; height: 28px;"/> S
                                </span>

                                <span>
                                    <b>充电低温：</b><input type="text" class="text"  name="chgUtTrip" value="<#if (batteryParameter.chgUtTrip)?? >${show_temp(batteryParameter.chgUtTrip)}</#if>"
                                                       style="width: 50px; height: 28px;"/> ℃
                                </span>
                                <span>
                                    <b>释放温度：</b><input type="text" class="text" name="chgUtResume" value="<#if (batteryParameter.chgUtResume)?? >${show_temp(batteryParameter.chgUtResume)}</#if>"
                                                       style="width: 50px; height: 28px;"/> ℃
                                </span>
                                <span>
                                    <b>延时：</b><input type="text" class="text" name="chgUtDelay" value="${(batteryParameter.chgUtDelay)!''}"
                                                     style="width: 50px; height: 28px;"/> S
                                </span>

                                <span>
                                    <b>放电高温：</b><input type="text" class="text"  name="dsgOtTrip" value="<#if (batteryParameter.dsgOtTrip)?? >${show_temp(batteryParameter.dsgOtTrip)}</#if>"
                                                       style="width: 50px; height: 28px;"/> ℃
                                </span>
                                <span>
                                    <b>释放温度：</b><input type="text" class="text" name="dsgOtResume" value="<#if (batteryParameter.dsgOtResume)?? >${show_temp(batteryParameter.dsgOtResume)}</#if>"
                                                       style="width: 50px; height: 28px;"/> ℃
                                </span>
                                <span>
                                    <b>延时：</b><input type="text" class="text" name="dsgOtDelay" value="${(batteryParameter.dsgOtDelay)!''}"
                                                     style="width: 50px; height: 28px;"/> S
                                </span>

                                <span>
                                    <b>放电低温：</b><input type="text" class="text"  name="dsgUtTrip" value="<#if (batteryParameter.dsgUtTrip)?? >${show_temp(batteryParameter.dsgUtTrip)}</#if>"
                                                       style="width: 50px; height: 28px;"/> ℃
                                </span>
                                <span>
                                    <b>释放电压：</b><input type="text" class="text" name="dsgUtResume" value="<#if (batteryParameter.dsgUtResume)?? >${show_temp(batteryParameter.dsgUtResume)}</#if>"
                                                       style="width: 50px; height: 28px;"/> ℃
                                </span>
                                <span>
                                    <b>延时：</b><input type="text" class="text" name="dsgUtDelay" value="${(batteryParameter.dsgUtDelay)!''}"
                                                     style="width: 50px; height: 28px;"/> S
                                </span>

                                <span>
                                    <b>充电过流：</b><input type="text" class="text"  name="chgOcTrip" value="${(batteryParameter.chgOcTrip)!''}"
                                                       style="width: 50px; height: 28px;"/> mA
                                </span>
                                <span>
                                    <b>释放时间：</b><input type="text" class="text" name="chgOcRelease" value="${(batteryParameter.chgOcRelease)!''}"
                                                       style="width: 50px; height: 28px;"/> S
                                </span>
                                <span>
                                    <b>延时：</b><input type="text" class="text" name="chgOcDelay" value="${(batteryParameter.chgOcDelay)!''}"
                                                     style="width: 50px; height: 28px;"/> S
                                </span>

                                <span>
                                    <b>放电过流：</b><input type="text" class="text"  name="dsgOcTrip" value="${(batteryParameter.dsgOcTrip)!''}"
                                                       style="width: 50px; height: 28px;"/> mA
                                </span>
                                <span>
                                    <b>释放时间：</b><input type="text" class="text" name="dsgOcRelease" value="${(batteryParameter.dsgOcRelease)!''}"
                                                       style="width: 50px; height: 28px;"/> S
                                </span>
                                <span>
                                    <b>延时：</b><input type="text" class="text" name="dsgOcDelay" value="${(batteryParameter.dsgOcDelay)!''}"
                                                     style="width: 50px; height: 28px;"/> S
                                </span>
                            </div>
                        </div>

                        <div class="param-three">
                            <div>
                                <i></i>高级保护
                            </div>
                            <div>
                                <span>
										<input type="checkbox" class="checkbox rsns_checkbox" <#if batteryParameter.rsns?? && batteryParameter.rsns == 1>checked</#if> /><label>过流及短路保护值翻倍</label>
									</span>
                                <span></span>
                                <span>
										<b>硬件过流保护值：</b>
                                         <select name="hardOcTrip" id="hardOcTrip"  class="easyui-combobox" style="width: 170px; height: 30px;" >
                                         <#if HardOcTrip??>
                                             <#list HardOcTrip as e>
                                                 <option <#if batteryParameter.hardOcTrip?? && batteryParameter.hardOcTrip == e.getValue()>selected</#if>
                                                         value="${e.getValue()}">${(e.getName())!''}</option>
                                             </#list>
                                         </#if>
                                        </select>
									</span>
                                <span>
										<b>过流保护延时：</b>
                                          <select name="hardOcDelay" id="hardOcDelay" class="easyui-combobox" style="width: 170px; height: 30px;">
                                          <#if HardOcDelay??>
                                              <#list HardOcDelay as e>
                                                  <option <#if batteryParameter.hardOcDelay?? && batteryParameter.hardOcDelay == e.getValue()>selected</#if>
                                                          value="${e.getValue()}">${(e.getName())!''}</option>
                                              </#list>
                                          </#if>
                                          </select>
									</span>
                                <span>
										<b>短路保护值：</b>
                                          <select name="scTrip" id="scTrip" class="easyui-combobox" style="width: 170px; height: 30px;" >
                                          <#if ScTrip??>
                                              <#list ScTrip as e>
                                                  <option <#if batteryParameter.scTrip?? && batteryParameter.scTrip == e.getValue()>selected</#if>
                                                          value="${e.getValue()}">${(e.getName())!''}</option>
                                              </#list>
                                          </#if>
                                          </select>
									</span>
                                <span>
										<b>短路保护延时：</b>
                                          <select name="scDelay" id="scDelay"  class="easyui-combobox" style="width: 170px; height: 30px;" >
                                          <#if ScDelay??>
                                              <#list ScDelay as e>
                                                  <option <#if batteryParameter.scDelay?? && batteryParameter.scDelay == e.getValue()>selected</#if>
                                                          value="${e.getValue()}">${(e.getName())!''}</option>
                                              </#list>
                                          </#if>
                                          </select>
									</span>
                                <span>
										<b>硬件单体过压：</b><input type="text" class="text"  name="hardOvTrip" value="${(batteryParameter.hardOvTrip)!''}" style="width: 100px; height: 28px;"/> mV
									</span>
                                <span>
										<b>过压保护延时：</b>
                                          <select name="hardOvDelay" id="hardOvDelay"  class="easyui-combobox" style="width: 170px; height: 30px;" >
                                          <#if HardOvDelay??>
                                              <#list HardOvDelay as e>
                                                  <option <#if batteryParameter.hardOvDelay?? && batteryParameter.hardOvDelay == e.getValue()>selected</#if>
                                                          value="${e.getValue()}">${(e.getName())!''}</option>
                                              </#list>
                                          </#if>
                                          </select>
									</span>
                                <span>
										<b>硬件单体欠压：</b><input type="text" class="text"  name="hardUvTrip" value="${(batteryParameter.hardUvTrip)!''}" style="width: 100px; height: 28px;"/> mV
									</span>
                                <span>
										<b>欠压保护延时：</b>
                                         <select name="hardUvDelay" id="hardUvDelay"  class="easyui-combobox" style="width: 170px; height: 30px;" >
                                         <#if HardUvDelay??>
                                             <#list HardUvDelay as e>
                                                 <option <#if batteryParameter.hardUvDelay?? && batteryParameter.hardUvDelay == e.getValue()>selected</#if>
                                                         value="${e.getValue()}">${(e.getName())!''}</option>
                                             </#list>
                                         </#if>
                                         </select>
									</span>
                                <span>
										<b>短路释放时间：</b><input type="text" class="text" name="sdRelease" value="${(batteryParameter.sdRelease)!''}" style="width: 100px; height: 28px;"/> S
									</span>
                            </div>
                        </div>
                        <div class="param-five">
                            <div>
                                <i></i>功能配置
                            </div>
                            <div>
                                <#if FunctionList??>
                                    <#list FunctionList as e>
                                        <#if e.name??>
                                            <td width="100" align="right">
                                    <span>
                                        <input type="checkbox" class="checkbox function_checkbox" <#if e.value == 1>checked</#if> /><label>${(e.name)!''}</label>
                                    </span>
                                            </td>
                                        </#if>
                                    </#list>
                                </#if>
                            </div>
                        </div>
                        <div class="param-five">
                            <div>
                                <i></i>NTC配置
                            </div>
                            <div>
                                <#if NtcList??>
                                    <#list NtcList as e>
                                        <#if e.name??>
                                            <td width="100" align="right">
                                    <span class="check_box">
                                        <input type="checkbox" class="checkbox ntc_checkbox" <#if e.value == 1>checked</#if> /><label>${(e.name)!''}</label>
                                    </span>
                                            </td>
                                        </#if>
                                    </#list>
                                </#if>
                            </div>
                        </div>
                        <div class="param-six">
                            <div>
                                <i></i>OCV配置
                            </div>
                            <div>
                                <#if OCVTableList??>
                                    <#list OCVTableList as e>
                                    <span>${(e.name)!''}</span>
                                    <span><input type="text" class="text  ocv_text"  name="ocvTable" value="${(e.value)!''}" style="width: 40px; height: 28px;"/></span>
                                    </#list>
                                </#if>
                            </div>
                        </div>
                        <div class="param-four">
                            <div>
                                <i></i>其他信息配置
                            </div>
                            <div>
                                <span>
                                    <b>电芯型号：</b><input type="text" class="text"  name="cellModel" value="${(batteryParameter.cellModel)!''}" />
                                </span>
                                <span>
                                    <b>电芯厂家：</b><input type="text" class="text" name="cellMfr" value="${(batteryParameter.cellMfr)!''}" />
                                </span>
                                <span>
                                    <b>电池厂家：</b><input type="text" class="text"  name="battMfr" value="${(batteryParameter.battMfr)!''}" />
                                </span>
                            </div>
                            <div>
                                <span>
                                    <b>生产日期：</b>
                                    <input type="text" class="easyui-numberspinner" name="mfd_yy" value="${(mfd_yy)!''}" style="width: 60px; height: 28px;"/> -
                                    <input type="text" class="easyui-numberspinner" name="mfd_mm" value="${(mfd_mm)!''}" data-options="min:1, max:12, editable:true" style="width: 60px; height: 28px;"/> -
                                    <input type="text" class="easyui-numberspinner" name="mfd_dd" value="${(mfd_dd)!''}" data-options="min:1, max:31, editable:true" style="width: 60px; height: 28px;"/>
                                </span>
                            </div>
                        </div>
                        </form>
                    </div>

                    <div class="c-d-three-2" style="display: none;">
                        <div class="path-one">
                                请选择日期：
                                <input id="begin_time" class="easyui-datetimebox" type="text"
                                              style="width:150px;height:27px;">
                                -
                                <input id="end_time" class="easyui-datetimebox" type="text"
                                       style="width:150px;height:27px;">
                            <div>
                                订单号码：<span><input class="text" type="text" id="order_id" readonly name="orderId" required="true"></span>
                                <a href="javascript:selectOrder('${entity.id}')" style="color: #1852ff;">选择订单</a>
                                <button class="btn btn_blue ok" id="ok_play" onclick="pay_index()" >播放</button>
                                <button class="btn btn_blue ok" id="ok_line" onclick="select_index()">轨迹列表</button>
                                <button class="btn btn_blue ok" id="ok_stop" onclick="stop_index()">停止</button>
                            </div>
                        </div>
                        <div class="path-two">
                            <div style="width: 100%;height:620px;padding-top: 10px;display: flex;">
                                <iframe id="map_frame" src="${contextPath}/security/basic/baidu_map/view_map.htm" width="65%" height="100%" frameborder="0" style="border:0"></iframe>
                                <div style="display: inline-block;width:35%;height:100%">
                                    <table id="report_log_page_table">
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="c-d-three-3" style="display: none;">
                        <table id="fault_log_page_table"></table>
                    </div>
                    <div class="c-d-three-4" style="display: none;">
                        <div class="report-left">
                            <div class="log-tips">
                                <i></i>上报日期
                            </div>
                            <div class="ztree_body easyui-tree" id="battery_tree"
                                 data-options="
                            onBeforeSelect: App.tree.toggleSelect,
                            onClick: function(node) {
                                tree_query();
                            }
                        ">
                            </div>
                        </div>

                        <div class="report-right">
                            <table id="battery_report_page_table"></table>
                        </div>

                        <div style="position: absolute;right: 20px;top: 55px;">
                            <button class="btn btn_red" onclick="export_excel()">批量导出</button>
                            <button class="btn btn_green" onclick="showLineChart()">查看折线图</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<#--<div class="popup_btn">-->
<#--    <button class="btn btn_red ok">确定</button>-->
<#--    <button class="btn btn_border close">关闭</button>-->
<#--</div>-->
        <script>
            //    $('.tr_right').on('mouseover', () = > {
            //        $('#login'
            //    ).show();
            //    });
            //    $('.tr_right').on('mouseout', () = > {
            //        $('#login'
            //    ).hide()
            //    })

            $('.c-d-three-tabs').on('click', 'span', (e) => {
                $('.c-d-three-tabs span').attr('class', '')
                $('.c-d-three-tabs span').eq(e.currentTarget.dataset.id).attr('class', 'active')
                $('.c-d-three-0').hide()
                $('.c-d-three-1').hide()
                $('.c-d-three-2').hide()
                $('.c-d-three-3').hide()
                $('.c-d-three-4').hide()
                $('.c-d-three-' + e.currentTarget.dataset.id).show()

            })

        </script>
        <script type="text/javascript">
            $(function () {

                var upBmsCheckbox = $('.upBms_checkbox');
                var rsnsCheckbox = $('.rsns_checkbox');
                var functionCheckbox = $('.function_checkbox');
                var ntcCheckbox = $('.ntc_checkbox');

                var jform = $('form');
                var form = jform[0];
                var ok = function () {
                    var functionStr = '';
                    for (var i = 8; i > 0; i--) {
                        if (functionCheckbox.eq(i - 1) == null) {
                            functionStr += "0";
                        } else {
                            functionStr += functionCheckbox.eq(i - 1).attr('checked') ? '1' : '0';
                        }
                    }
                    functionStr = parseInt(functionStr, 2);

                    var ntcStr = '';
                    for (var i = 8; i > 0; i--) {
                        if (ntcCheckbox.eq(i - 1) == null) {
                            functionStr += "0";
                        } else {
                            ntcStr += ntcCheckbox.eq(i - 1).attr('checked') ? '1' : '0';
                        }
                    }
                    ntcStr = parseInt(ntcStr, 2);

                    var ocvTable = "";
                    $(".ocv_text").each(function (i, v) {
                        ocvTable = ocvTable + $(this).val() + ",";
                    });
                    if (ocvTable.length > 0) {
                        ocvTable = ocvTable.substr(0, ocvTable.length - 1);
                    }

                    //温度处理
                    chgOtTrip = dealTemp(form.chgOtTrip.value);
                    chgOtResume = dealTemp(form.chgOtResume.value);
                    chgUtTrip = dealTemp(form.chgUtTrip.value);
                    chgUtResume = dealTemp(form.chgUtResume.value);
                    dsgOtTrip = dealTemp(form.dsgOtTrip.value);
                    dsgOtResume = dealTemp(form.dsgOtResume.value);
                    dsgUtTrip = dealTemp(form.dsgUtTrip.value);
                    dsgUtResume = dealTemp(form.dsgUtResume.value);

                    var success = true;
                    var values = {
                        id: '${entity.id}',
                        heartInterval: form.heartInterval.value,
                        motionless: form.motionless.value,
                        standby: form.standby.value,
                        dormancy: form.dormancy.value,

                        nominalCapacity: form.nominalCapacity.value,
                        circleCapacity: form.circleCapacity.value,
                        selfDsgRate: form.selfDsgRate.value,
                        cellFullVol: form.cellFullVol.value,
                        cellCutVol: form.cellCutVol.value,
                        cellOvTrip: form.cellOvTrip.value,
                        cellOvResume: form.cellOvResume.value,
                        cellOvDelay: form.cellOvDelay.value,
                        cellUvTrip: form.cellUvTrip.value,
                        cellUvResume: form.cellUvResume.value,
                        cellUvDelay: form.cellUvDelay.value,
                        packOvTrip: form.packOvTrip.value,
                        packOvResume: form.packOvResume.value,
                        packOvDelay: form.packOvDelay.value,
                        packUvTrip: form.packUvTrip.value,
                        packUvResume: form.packUvResume.value,
                        packUvDelay: form.packUvDelay.value,

                        chgOtTrip: chgOtTrip,
                        chgOtResume: chgOtResume,
                        chgOtDelay: form.chgOtDelay.value,
                        chgUtTrip: chgUtTrip,
                        chgUtResume: chgUtResume,
                        chgUtDelay: form.chgUtDelay.value,
                        dsgOtTrip: dsgOtTrip,
                        dsgOtResume: dsgOtResume,
                        dsgOtDelay: form.dsgOtDelay.value,
                        dsgUtTrip: dsgUtTrip,
                        dsgUtResume: dsgUtResume,
                        dsgUtDelay: form.dsgUtDelay.value,
                        chgOcTrip: form.chgOcTrip.value,
                        chgOcRelease: form.chgOcRelease.value,
                        chgOcDelay: form.chgOcDelay.value,
                        dsgOcTrip: form.dsgOcTrip.value,
                        dsgOcRelease: form.dsgOcRelease.value,
                        dsgOcDelay: form.dsgOcDelay.value,
                        upBms: upBmsCheckbox.eq(0).attr('checked') ? 1 : 0,
                        rsns: rsnsCheckbox.eq(0).attr('checked') ? 1 : 0,
                        hardOcTrip: $('#hardOcTrip').combobox('getValue'),
                        hardOcDelay: $('#hardOcDelay').combobox('getValue'),
                        scTrip: $('#scTrip').combobox('getValue'),
                        scDelay: $('#scDelay').combobox('getValue'),
                        hardOvTrip: form.hardOvTrip.value,
                        hardOvDelay: $('#hardOvDelay').combobox('getValue'),
                        hardUvTrip: form.hardUvTrip.value,
                        hardUvDelay: $('#hardUvDelay').combobox('getValue'),
                        sdRelease: form.sdRelease.value,
                        function: functionStr,
                        ntcConfig: ntcStr,
                        ocvTable: ocvTable,
                        cellModel: form.cellModel.value,
                        cellMfr: form.cellMfr.value,
                        battMfr: form.battMfr.value,
                        mfd: form.mfd_yy.value + "/" + form.mfd_mm.value + "/" + form.mfd_dd.value,
                    };

                    $.ajax({
                        cache: false,
                        async: false,
                        type: 'POST',
                        url: '${contextPath}/security/hdg/battery_parameter/update.htm',
                        dataType: 'json',
                        data: values,
                        success: function (json) {
                            <@app.json_jump/>
                            if (json.success) {
                            } else {
                                $.messager.alert('提示信息', json.message, 'info');
                                success = false;
                            }
                        }
                    });

                    return success;
                };

                $('#battery_parameter').data('ok', ok);

                $('#ok_update').click(function () {
                    var ok = $('#battery_parameter').data('ok')();
                    if(ok) {
                        //刷新或后退
                        window.location.href = "${contextPath}/security/zd/battery/index.htm";
                    }
                });

//        //加载运动轨迹列表
//        showEmptyReportTable();
                //加载告警信息
                showFaultLogTable();
                <#--$('.close').click(function () {-->
                <#--window.location.href = "${contextPath}/security/hdg/battery/index.htm?";-->
                <#--});-->

            })

            $('.nav').hide();
            $('.main .index_con').css("left", 0);

            $('#back').click(function () {
                //刷新或后退
                window.location.href = "${contextPath}/security/zd/battery/index.htm";
            })


            function dealTemp(temp) {
                if (temp != null && temp != '') {
                    return Number(temp) * 10 + 2731;
                }
                return '';
            }

            function seeLog() {
                App.dialog.show({
                    css: 'width:1300px;height:500px;',
                    title: '参数修改记录',
                    href: "${contextPath}/security/hdg/battery_parameter_log/index.htm?id=${(entity.id)!''}"
                });
            }

            function selectOrder(id) {
                App.dialog.show({
                    css: 'width:850px;height:525px;',
                    title: '选择订单',
                    href: "${contextPath}/security/hdg/battery_ride_order/select_order.htm?batteryId=" + id,
                    windowData: {
                        ok: function (config) {
                            $("input[name='orderId']").val(config.order.id);
                            var orderId = $("input[name='orderId']").val();
                            $.post("${contextPath}/security/hdg/battery_order_battery_report_log/find_all_map_count.htm", {orderId: orderId}, function (json) {
                                if (!json.success) {
                                    $.messager.alert('提示信息', '该时段电池未上报信息', 'info');
                                    $("input[name='orderId']").val('');
                                    //还原地图
                                    var mapFrame = document.getElementById('map_frame');
                                    var doc = mapFrame.contentDocument || mapFrame.document;
                                    var childWindow = mapFrame.contentWindow || mapFrame;
                                    childWindow.restoreMap();
                                    showEmptyReportTable();
                                    return;
                                }else {
                                    //显示地图
                                    var orderId = $("input[name='orderId']").val();
                                    $.post("${contextPath}/security/basic/baidu_map/map_data.htm", {orderId: orderId}, function (json) {
                                        var mapFrame = document.getElementById('map_frame');
                                        var doc = mapFrame.contentDocument || mapFrame.document;
                                        var childWindow = mapFrame.contentWindow || mapFrame;
                                        if(json.data != null) {
                                            childWindow.createMap(json.data);
                                        }else {
                                            $.messager.alert('提示信息', '换电订单不存在', 'info');
                                        }
                                    }, 'json');

                                    //显示轨迹数据
                                    var orderId = $("input[name='orderId']").val();
                                    if (orderId != null && orderId != '') {
                                        var datagrid = $('#report_log_page_table');
                                        datagrid.datagrid({
                                            fit: true,
                                            width: '100%',
                                            height: '100%',
                                            striped: true,
                                            pagination: true,
                                            url: "${contextPath}/security/hdg/battery_order_battery_report_log/select_page.htm?orderId=" + orderId,
                                            fitColumns: true,
                                            pageSize: 50,
                                            pageList: [50, 100],
                                            idField: 'orderId',
                                            singleSelect: true,
                                            selectOnCheck: false,
                                            checkOnSelect: false,
                                            autoRowHeight: false,
                                            rowStyler: gridRowStyler,
                                            columns: [
                                                [
                                                    {
                                                        title: 'ID',
                                                        align: 'center',
                                                        field: 'id',
                                                        width: 10,
                                                        formatter: function (val, row) {
                                                            var options = datagrid.datagrid('getPager').data("pagination").options;
                                                            var page = options.pageNumber;//当前页数
                                                            var pageSize = options.pageSize;
                                                            var total = datagrid.datagrid('getData').total;
                                                            var id = (options.pageNumber - 1) * options.pageSize + datagrid.datagrid('getRowIndex', row) + 1;
                                                            if (id == 1) {
                                                                return "起点"
                                                            } else if (id == total) {
                                                                return "终点"
                                                            } else {
                                                                return id;
                                                            }
                                                        }
                                                    },
                                                    {
                                                        title: '上报时间',
                                                        align: 'center',
                                                        field: 'reportTime',
                                                        width: 25
                                                    },
                                                    {
                                                        title: '地址',
                                                        align: 'center',
                                                        field: 'address',
                                                        width: 25
                                                    },
                                                    {
                                                        title: '操作',
                                                        align: 'center',
                                                        field: 'action',
                                                        width: 15,
                                                        formatter: function (val, row) {
                                                            var html = ' <a style="color: blue;" href="javascript:getLocation(\'lng\',\'lat\')">获取当前位置</a>';
                                                            html = html.replace(/lng/g, row.lng);
                                                            return html.replace(/lat/g, row.lat);
                                                        }
                                                    }
                                                ]
                                            ],
                                            onClickRow: function (index, row) {
                                                markerPoint(row);
                                            },
                                            onLoadSuccess: function () {
                                                datagrid.datagrid('clearChecked');
                                                datagrid.datagrid('clearSelections');
                                            }
                                        });
                                    }

                                    $('#begin_time').datetimebox({
                                        onChange: function (date) {
                                            var startDate = $('#begin_time').datetimebox('getValue');
                                            var endDate = $('#end_time').datetimebox('getValue');
                                            var orderId = $("input[name='orderId']").val();
                                            var mapFrame = document.getElementById('map_frame');
                                            if (endDate != '') {
                                                if (startDate > endDate) {
                                                    $.messager.alert('提示消息', '结束日期必须大于等于开始日期', 'info');
                                                    return;
                                                }
                                                $.post("${contextPath}/security/basic/baidu_map/map_data.htm", {
                                                    orderId: orderId,
                                                    startDate: startDate,
                                                    endDate: endDate
                                                }, function (json) {
                                                    var doc = mapFrame.contentDocument || mapFrame.document;
                                                    var childWindow = mapFrame.contentWindow || mapFrame;
                                                    if(json.data != null) {
                                                        childWindow.createMap(json.data);
                                                    }else {
                                                        $.messager.alert('提示信息', '换电订单不存在', 'info');
                                                    }
                                                }, 'json');
                                            }
                                        }
                                    })

                                    $('#end_time').datetimebox({
                                        onChange: function (date) {
                                            var startDate = $('#begin_time').datetimebox('getValue');
                                            var endDate = $('#end_time').datetimebox('getValue');
                                            var orderId = $("input[name='orderId']").val();
                                            var mapFrame = document.getElementById('map_frame');
                                            if (startDate != '' && startDate > endDate) {
                                                $.messager.alert('提示消息', '结束日期必须大于等于开始日期', 'info');
                                                return;
                                            }
                                            $.post("${contextPath}/security/basic/baidu_map/map_data.htm", {
                                                orderId: orderId,
                                                startDate: startDate,
                                                endDate: endDate
                                            }, function (json) {
                                                var doc = mapFrame.contentDocument || mapFrame.document;
                                                var childWindow = mapFrame.contentWindow || mapFrame;
                                                if(json.data != null) {
                                                    childWindow.createMap(json.data);
                                                }else {
                                                    $.messager.alert('提示信息', '换电订单不存在', 'info');
                                                }
                                            }, 'json');
                                        }
                                    })

                                    setTimeout(function () {
                                        var orderId = $("input[name='orderId']").val();
                                        var mapFrame = document.getElementById('map_frame');
                                        if (orderId == null || orderId == '') {
                                            return;
                                        }
                                        $.post("${contextPath}/security/basic/baidu_map/map_data.htm", {orderId: orderId}, function (json) {
                                            var doc = mapFrame.contentDocument || mapFrame.document;
                                            var childWindow = mapFrame.contentWindow || mapFrame;
                                            if(json.data != null) {
                                                childWindow.createMap(json.data);
                                            }else {
                                                $.messager.alert('提示信息', '换电订单不存在', 'info');
                                            }
                                        }, 'json');
                                    }, 500)

                                }
                            }, 'json');
                        }
                    }
                });
            }

            function getLocation(lng, lat) {
                var myGeo = new BMap.Geocoder();
                myGeo.getLocation(new BMap.Point(lng, lat), function (result) {
                    if (!result) {
                        alert("获取失败");
                    } else {
                        var address = result.address;
                        var datagrid = $('#report_log_page_table');
                        var row = datagrid.datagrid('getSelected');
                        if (row) {
                            var mapFrame = document.getElementById('map_frame');
                            var doc = mapFrame.contentDocument || mapFrame.document;
                            var childWindow = mapFrame.contentWindow || mapFrame;
                            childWindow.markerAnimation(row.lng,row.lat);
                            var index = datagrid.datagrid('getRowIndex', row);
                            datagrid.datagrid('updateRow', {index: index, row: {address: address}});
                            $.post("${contextPath}/security/hdg/battery_order_battery_report_log/update_address.htm?", {
                                orderId: row.orderId,
                                reportTime: row.reportTime,
                                address: address,
                            }, function (json) {
                            }, 'json');
                        }
                    }
                });
            }

            function markerPoint(row){
                var mapFrame = document.getElementById('map_frame');
                var childWindow = mapFrame.contentWindow || mapFrame;
                childWindow.markerAnimation(row.lng,row.lat);
            }

            function select_index() {
                var orderId = $("input[name='orderId']").val();
                if(orderId == null || orderId == '') {
                    $.messager.alert('提示信息', '请选择订单', 'info');
                    return
                }
                var startDate = $('#begin_time').datetimebox('getValue');
                var endDate = $('#end_time').datetimebox('getValue');

                if (startDate != '' && startDate > endDate) {
                    $.messager.alert('提示消息', '结束日期必须大于等于开始日期', 'info');
                    return;
                }
                App.dialog.show({
                    css: 'width:854px;height:470px;',
                    title: '查看',
                    href: "${contextPath}/security/hdg/battery_order_battery_report_log/select_index.htm?orderId=" + orderId +"&startDate=" + startDate + "&endDate=" + endDate
                });
            }

            function pay_index() {
                var orderId = $("input[name='orderId']").val();
                var mapFrame = document.getElementById('map_frame');
                if(orderId == null || orderId == '') {
                    $.messager.alert('提示信息', '请选择订单', 'info');
                    return
                }
                var startDate = $('#begin_time').datetimebox('getValue');
                var endDate = $('#end_time').datetimebox('getValue');

                if (startDate != '' && startDate > endDate) {
                    $.messager.alert('提示消息', '结束日期必须大于等于开始日期', 'info');
                    return;
                }
                $.post("${contextPath}/security/basic/baidu_map/map_data.htm", {
                    orderId: orderId,
                    startDate: startDate,
                    endDate: endDate
                }, function (json) {
                    var doc = mapFrame.contentDocument || mapFrame.document;
                    var childWindow = mapFrame.contentWindow || mapFrame;
                    if(json.data != null) {
                        childWindow.createWalkingMap(json.data);
                    }else {
                        $.messager.alert('提示信息', '换电订单不存在', 'info');
                    }
                }, 'json');
            }

            //    $('#ride_order_map').click(function () {
            //        document.getElementById('map_frame').style.width('100%');
            //    })

            //点击上报信息
            $('#battery_report').click(function () {
                $('.c-d-three-4').show();
                var batteryId = '${entity.id}';
                $("#battery_tree").tree({
                    url: '${contextPath}/security/hdg/battery_report_date/tree.htm?batteryId=' + batteryId,
                    onLoadSuccess: function (data) {
                        eval(data);
                    }
                });

                $('#battery_report_page_table').datagrid({
                    autoRowHeight: false,
                    rowStyler: gridRowStyler,
                    fit: true,
                    striped: true,
                    singleSelect: true,
                    collapsible: true,
                    pagination: true,
                    url: "${contextPath}/security/hdg/battery_report/page.htm",
                    pageSize: 10,
                    pageList: [10, 50, 100],
                    idField: 'batteryId',
                    frozenColumns: [[
                        {
                            title: '电池编号',
                            align: 'center',
                            field: 'batteryId',
                            width: 100
                        },
                        {
                            title: '上报时间',
                            align: 'center',
                            field: 'createTime',
                            width: 140
                        },
                    ]],
                    columns: [
                        [
                            {
                                title: '电压',
                                align: 'center',
                                field: 'voltage',
                                width: 50,
                                formatter: function (val) {
                                    return Number(val / 1000).toFixed(2) + "V";
                                }
                            },
                            {
                                title: '电流',
                                align: 'center',
                                field: 'electricity',
                                width: 50,
                                formatter: function (val) {
                                    return Number(val / 1000).toFixed(2) + "A";
                                }
                            },
                            {
                                title: '剩余电量',
                                align: 'center',
                                field: 'volume',
                                width: 60,
                                formatter: function (val) {
                                    if(val == null){
                                        return "";
                                    }
                                    return  val  + "%";
                                }
                            },
                            {
                                title: '温度',
                                align: 'center',
                                field: 'temp',
                                width: 80
                            },
                            {
                                title: '经度/纬度',
                                align: 'center',
                                field: 'lat',
                                width: 140,
                                formatter: function(val, row) {
                                    var lng = row.lng;
                                    var lat = row.lat;
                                    if(lng == null && lat == null){
                                        return "";
                                    }
                                    return Number(row.lng).toFixed(6) + "/" + Number(row.lat).toFixed(6);
                                }
                            },
                            {
                                title: '位置类型',
                                align: 'center',
                                field: 'locTypeName',
                                width: 60
                            },
                            {
                                title: '信号',
                                align: 'center',
                                field: 'currentSignal',
                                width: 50
                            },
                            {
                                title: '类型',
                                align: 'center',
                                field: 'heartTypeName',
                                width: 110
                            },
                            {
                                title: '位置',
                                align: 'center',
                                field: 'address',
                                width: 180
                            },
                            {
                                title: '操作',
                                align: 'center',
                                field: 'action',
                                width: 150,
                                formatter: function (val, row) {
                                    var html = '';
                                    html += '<a href="javascript:viewReport(\'ID\',\'ReportTime\')">查看</a>';
                                    html += ' <a style="color: blue;" href="javascript:seeLocation(\'lng\',\'lat\')">获取当前位置</a>';
                                    return html.replace(/ID/g, row.batteryId).replace(/ReportTime/g, row.createTime).replace(/lng/g, row.lng).replace(/lat/g, row.lat);
                                }
                            }
                        ]
                    ],
                    onLoadSuccess: function () {
                        $('#battery_report_page_table').datagrid('clearChecked');
                        $('#battery_report_page_table').datagrid('clearSelections');
                    }
                });

                var batteryReportDatagrid = $('#battery_report_page_table');

                batteryReportDatagrid.datagrid('options').queryParams = {
                    batteryId: batteryId
                };
                batteryReportDatagrid.datagrid('load');
            })

            function tree_query() {
                var tree = $('#battery_tree');
                var createTime = tree.tree('getSelected');
                if (createTime) {
                    createTime = createTime.id || '';
                } else {
                    createTime = '';
                }
                if (createTime.length >= 10) {
                    var batteryReportDatagrid = $('#battery_report_page_table');
                    var batteryId = '${entity.id}';
                    if (batteryId != null) {
                        batteryReportDatagrid.datagrid('options').queryParams = {
                            batteryId: batteryId,
                            createTime: createTime + " 00:00:00"
                        };
                        batteryReportDatagrid.datagrid('load');
                    }
                }
            }

            function seeLocation(lng, lat) {
                if(lng == null || lat == null || lng == 'null'  || lat == 'null'){
                    $.messager.alert('提示信息', '经纬度不能为空', 'info');
                    return;
                }
                var myGeo = new BMap.Geocoder();
                myGeo.getLocation(new BMap.Point(lng, lat), function (result) {
                    if (!result) {
                        alert("获取失败");
                    } else {
                        var address = result.address;
                        var batteryReportDatagrid = $('#battery_report_page_table');
                        var row = batteryReportDatagrid.datagrid('getSelected');
                        if (row) {
                            var index = batteryReportDatagrid.datagrid('getRowIndex', row);
                            batteryReportDatagrid.datagrid('updateRow', {index: index, row: {address: address}});
                        }
                    }
                });
            }

            function viewReport(batteryId, createTime) {
                App.dialog.show({
                    css: 'width:886px;height:647px;overflow:scroll;',
                    title: '查看',
                    href: "${contextPath}/security/hdg/battery_report/view.htm?batteryId=" + batteryId + "&createTime=" + createTime
                });
            }

            function export_excel() {
                var tree = $('#battery_tree');
                var batteryId = '${entity.id}';
                var createTime = tree.tree('getSelected');
                if (createTime) {
                    createTime = createTime.id || '';
                } else {
                    createTime = '';
                }
                if(createTime == '' || createTime.length < 10) {
                    $.messager.alert('提示信息', '请选择年月日', 'info');
                    return false;
                }
                $.messager.confirm('提示信息', '确认导出数据?', function (ok) {
                    if (ok) {
                        var queryParams = {
                            batteryId: batteryId,
                            createTime: createTime
                        };
                        $.post('${contextPath}/security/hdg/battery_report/export_excel.htm', queryParams, function (json) {
                            $.messager.progress('close');
                            if (json.success) {
                                document.location.href = '${contextPath}/security/hdg/battery_report/download.htm?filePath=' + json.data[0] + "&formatDate=" + json.data[1] + "&batteryId=" + json.data[2];
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
                    }
                });
            }

            function showLineChart() {
                var tree = $('#battery_tree');
                var batteryId = '${entity.id}';
                var createTime = tree.tree('getSelected');
                if (createTime) {
                    createTime = createTime.id || '';
                } else {
                    createTime = '';
                }
                if(createTime == '' || createTime.length < 10) {
                    $.messager.alert('提示信息', '请选择年月日', 'info');
                    return false;
                }
                App.dialog.show({
                    css: 'width:1200px;height:620px;overflow:visible;',
                    title: '查看',
                    href: '${contextPath}/security/hdg/battery_report/show_line_chart.htm?batteryId=' + batteryId + "&createTime=" + createTime
                });
            }

            function stop_index() {
                //停止播放
                var orderId = $("input[name='orderId']").val();
                var mapFrame = document.getElementById('map_frame');
                if (orderId == null || orderId == '') {
                    return;
                }
                $.post("${contextPath}/security/basic/baidu_map/map_data.htm", {orderId: orderId}, function (json) {
                    var doc = mapFrame.contentDocument || mapFrame.document;
                    var childWindow = mapFrame.contentWindow || mapFrame;
                    childWindow.stopWalkingMap(json.data);
                }, 'json');
            }

            function showEmptyReportTable() {
                var datagrid = $('#report_log_page_table');
                datagrid.datagrid({
                    fit: true,
                    width: '100%',
                    height: '100%',
                    striped: true,
                    pagination: true,
                    url: "${contextPath}/security/hdg/battery_order_battery_report_log/select_page.htm?orderId=" + "emptyData",
                    fitColumns: true,
                    pageSize: 50,
                    pageList: [50, 100],
                    idField: 'orderId',
                    singleSelect: true,
                    selectOnCheck: false,
                    checkOnSelect: false,
                    autoRowHeight: false,
                    rowStyler: gridRowStyler,
                    columns: [
                        [
                            {
                                title: 'ID',
                                align: 'center',
                                field: 'id',
                                width: 10,
                                formatter: function (val, row) {
                                    var options = datagrid.datagrid('getPager').data("pagination").options;
                                    var page = options.pageNumber;//当前页数
                                    var pageSize = options.pageSize;
                                    var total = datagrid.datagrid('getData').total;
                                    var id = (options.pageNumber - 1) * options.pageSize + datagrid.datagrid('getRowIndex', row) + 1;
                                    if (id == 1) {
                                        return "起点"
                                    } else if (id == total) {
                                        return "终点"
                                    } else {
                                        return id;
                                    }
                                }
                            },
                            {
                                title: '上报时间',
                                align: 'center',
                                field: 'reportTime',
                                width: 25
                            },
                            {
                                title: '地址',
                                align: 'center',
                                field: 'address',
                                width: 25
                            },
                            {
                                title: '操作',
                                align: 'center',
                                field: 'action',
                                width: 15,
                                formatter: function (val, row) {
                                    var html = ' <a style="color: blue;" href="javascript:getLocation(\'lng\',\'lat\')">获取当前位置</a>';
                                    html = html.replace(/lng/g, row.lng);
                                    return html.replace(/lat/g, row.lat);
                                }
                            }
                        ]
                    ],
                    onClickRow: function (index, row) {
                        markerPoint(row);
                    },
                    onLoadSuccess: function () {
                        datagrid.datagrid('clearChecked');
                        datagrid.datagrid('clearSelections');
                    }
                });
            }

            function battery_view(id) {
                App.dialog.show({
                    css: 'width:780px;height:512px;overflow:visible;',
                    title: '查看',
                    href: "${contextPath}/security/zd/battery/view.htm?id=" + id
                });
            }

            function viewFaultLog() {

            }

            function editFaultLog() {

            }

            $('#fault_log').click(function () {
                showFaultLogTable();
            })

            function showFaultLogTable() {
                $('#fault_log_page_table').datagrid({
                    fit: true,
                    width: '100%',
                    height: '100%',
                    striped: true,
                    pagination: true,
                    url: "${contextPath}/security/hdg/fault_log/page.htm?batteryId=${entity.id}",
                    fitColumns: true,
                    pageSize: 50,
                    pageList: [50, 100],
                    idField: 'id',
                    singleSelect: true,
                    selectOnCheck: false,
                    checkOnSelect: false,
                    autoRowHeight: false,
                    rowStyler: gridRowStyler,
                    columns: [
                        [
                            {
                                title: 'checkbox', checkbox: true
                            },
                            {
                                title: '级别',
                                align: 'center',
                                field: 'faultLevelName',
                                width: 20
                            },
                            {
                                title: '运营商',
                                align: 'center',
                                field: 'agentName',
                                width: 40
                            },
                            {
                                title: '订单ID',
                                align: 'center',
                                field: 'orderId',
                                width: 40
                            },
                            {
                                title: '换电柜',
                                align: 'center',
                                field: 'cabinetId',
                                width: 40,
                                formatter: function (val, row) {
                                    if (val != null) {
                                        return val + '(' + row.cabinetName + ')'
                                    } else {
                                        return ''
                                    }
                                }
                            },

                            {
                                title: '格口',
                                align: 'center',
                                field: 'boxNum',
                                width: 30
                            },
                            {
                                title: '电池',
                                align: 'center',
                                field: 'batteryId',
                                width: 40,
                                formatter: function (val) {
                                    if (val != null) {
                                        return '<a href="#" onclick="battery_view(\''+val+'\')"><u>'+val+'</u></a>';
                                    } else {
                                        return '';
                                    }
                                }
                            },
                            {
                                title: '品牌',
                                align: 'center',
                                field: 'brandName',
                                width: 20
                            },
                            {
                                title: '故障类型',
                                align: 'center',
                                field: 'faultTypeName',
                                width: 40
                            },
                            {
                                title: '故障内容',
                                align: 'center',
                                field: 'faultContent',
                                width: 40
                            },
                            {
                                title: '状态',
                                align: 'center',
                                field: 'statusName',
                                width: 30
                            },
                            {
                                title: '处理时间',
                                align: 'center',
                                field: 'handleTime',
                                width: 60
                            },
                            {
                                title: '创建时间',
                                align: 'center',
                                field: 'createTime',
                                width: 60
                            },
                        ]
                    ],
                    queryParams: {
                        agentId: '${entity.agentId}'
                    }
                    ,
                    onLoadSuccess: function () {
                        $('#fault_log_page_table').datagrid('clearChecked');
                        $('#fault_log_page_table').datagrid('clearSelections');
                    }
                });
            }

        </script>
