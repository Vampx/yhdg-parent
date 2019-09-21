<#function show_temp temp >
    <#return (temp - 2731) / 10 >
</#function>
<script>
    $.extend($.fn.validatebox.defaults.rules, {
        positiveInt:{//验证整数
            validator:function(value,param){
                if (value){
                    return /^[-\+]?\d+$/.test(value);
                } else {
                    return true;
                }
            },
            message:'只能输入整数.'
        }
    });
</script>
<div class="tab_item" style="display:block;width: 770px;">
    <form>
        <input type="hidden" name="id" value="${entity.id}">
        <div style="text-align:right"><a style="color: #ff0000;">最后同步时间&nbsp;&nbsp; <#if (entity.shortReportTime)??>短心跳：${app.format_date_time(entity.shortReportTime)}</#if>&nbsp; <#if (entity.longReportTime)??>长心跳：${app.format_date_time(entity.longReportTime)}</#if></a></div>
        <fieldset style="margin-top: 10px">
            <legend>
                <span>
                    <label>锁定显示</label>
                </span>
            </legend>
            <div class="ui_table">
                <table cellpadding="0" cellspacing="0" class="times_table_list">
                    <tr>
                        <td  width="100" align="right">MOS：</td>
                        <td><input type="text" class="text"  name="mosName" value="${(entity.mosName)!''}"
                                   style="width: 100px; height: 28px;" disabled/></td>
<#--                        <td  width="100" align="right">租期：</td>
                        <td><input type="text" class="text"  name="batteryLease" value="${(entity.batteryLease)!''}"
                                   style="width: 100px; height: 28px;" disabled/>秒</td>-->

                        <td width="170" align="right">
                            <span class="check_box">
                                <input type="checkbox" class="checkbox upBms_checkbox" <#if entity.upBms?? && entity.upBms == 1>checked</#if> /><label>立即上传BMS参数</label>
                            </span>
                        </td>
                    </tr>
                </table>
            </div>
        </fieldset>

        <fieldset style="margin-top: 10px">
            <legend>
                <span>
                    <label>心跳配置</label>
                </span>
            </legend>
            <div class="ui_table">
                <table cellpadding="0" cellspacing="0" class="times_table_list">
                    <tr>
                        <td  width="120" align="right">运动心跳间隔：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="heartInterval" value="${(entity.heartInterval)!''}"
                                   style="width: 50px; height: 28px;"/> S</td>
                        <td  width="120" align="right">静止心跳间隔：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="motionless" value="${(entity.motionless)!''}"
                                   style="width: 50px; height: 28px;"/> S</td>
                        <td  width="120" align="right">存储心跳间隔：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="standby" value="${(entity.standby)!''}"
                                   style="width: 50px; height: 28px;"/> S</td>
                    </tr>
                    <tr>
                        <td  width="120" align="right">休眠心跳间隔：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="dormancy" value="${(entity.dormancy)!''}"
                                   style="width: 50px; height: 28px;"/> S</td>
                    </tr>
                </table>
            </div>
        </fieldset>

        <fieldset style="margin-top: 10px">
            <legend>
                <span>
                    <label>容量配置</label>
                </span>
            </legend>
            <div class="ui_table">
                <table cellpadding="0" cellspacing="0" class="times_table_list">
                    <tr>
                        <td  width="100" align="right">标称容量：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="nominalCapacity" value="${(entity.nominalCapacity)!''}"
                                   style="width: 50px; height: 28px;"/> mAH</td>
                        <td  width="100" align="right">循环容量：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="circleCapacity" value="${(entity.circleCapacity)!''}"
                                   style="width: 50px; height: 28px;"/> mAH</td>
                        <td  width="100" align="right">自放电率：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="selfDsgRate" value="${(entity.selfDsgRate)!''}"
                                   style="width: 50px; height: 28px;"/> %</td>
                    </tr>
                    <tr>
                        <td  width="100" align="right">单体充满电压：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="cellFullVol" value="${(entity.cellFullVol)!''}"
                                   style="width: 50px; height: 28px;"/> mV</td>
                        <td  width="100" align="right">单体截止电压：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="cellCutVol" value="${(entity.cellCutVol)!''}"
                                   style="width: 50px; height: 28px;"/> mV</td>
                    </tr>
                </table>
            </div>
        </fieldset>

        <fieldset style="margin-top: 10px">
            <legend>
                <span>
                    <label>基本保护参数配置</label>
                </span>
            </legend>
            <div class="ui_table">
                    <table cellpadding="0" cellspacing="0" class="times_table_list">
                        <tr>
                            <td  width="100" align="right">单体过压：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="cellOvTrip" value="${(entity.cellOvTrip)!''}"
                                       style="width: 50px; height: 28px;"/> mV</td>
                            <td  width="100" align="right">释放电压：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="cellOvResume" value="${(entity.cellOvResume)!''}"
                                       style="width: 50px; height: 28px;"/> mV</td>
                            <td  width="80" align="right">延时：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="cellOvDelay" value="${(entity.cellOvDelay)!''}"
                                       style="width: 50px; height: 28px;"/> S</td>
                        </tr>

                        <tr>
                            <td  width="100" align="right">单体欠压：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="cellUvTrip" value="${(entity.cellUvTrip)!''}"
                                       style="width: 50px; height: 28px;"/> mV</td>
                            <td  width="100" align="right">释放电压：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="cellUvResume" value="${(entity.cellUvResume)!''}"
                                       style="width: 50px; height: 28px;"/> mV</td>
                            <td  width="80" align="right">延时：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="cellUvDelay" value="${(entity.cellUvDelay)!''}"
                                       style="width: 50px; height: 28px;"/> S</td>
                        </tr>

                        <tr>
                            <td  width="100" align="right">整组过压：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="packOvTrip" value="${(entity.packOvTrip)!''}"
                                       style="width: 50px; height: 28px;"/> mV</td>
                            <td  width="100" align="right">释放电压：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="packOvResume" value="${(entity.packOvResume)!''}"
                                       style="width: 50px; height: 28px;"/> mV</td>
                            <td  width="80" align="right">延时：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="packOvDelay" value="${(entity.packOvDelay)!''}"
                                       style="width: 50px; height: 28px;"/> S</td>
                        </tr>

                        <tr>
                            <td  width="100" align="right">整组欠压：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="packUvTrip" value="${(entity.packUvTrip)!''}"
                                       style="width: 50px; height: 28px;"/> mV</td>
                            <td  width="100" align="right">释放电压：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="packUvResume" value="${(entity.packUvResume)!''}"
                                       style="width: 50px; height: 28px;"/> mV</td>
                            <td  width="80" align="right">延时：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="packUvDelay" value="${(entity.packUvDelay)!''}"
                                       style="width: 50px; height: 28px;"/> S</td>
                        </tr>

                        <tr>
                            <td  width="100" align="right">充电高温：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="chgOtTrip" value="<#if (entity.chgOtTrip)?? >${show_temp(entity.chgOtTrip)}</#if>"
                                       style="width: 50px; height: 28px;"/> ℃</td>
                            <td  width="100" align="right">释放温度：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="chgOtResume" value="<#if (entity.chgOtResume)?? >${show_temp(entity.chgOtResume)}</#if>"
                                       style="width: 50px; height: 28px;"/> ℃</td>
                            <td  width="80" align="right">延时：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="chgOtDelay" value="${(entity.chgOtDelay)!''}"
                                       style="width: 50px; height: 28px;"/> S</td>
                        </tr>

                        <tr>
                            <td  width="100" align="right">充电低温：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="chgUtTrip" value="<#if (entity.chgUtTrip)?? >${show_temp(entity.chgUtTrip)}</#if>"
                                       style="width: 50px; height: 28px;"/> ℃</td>
                            <td  width="100" align="right">释放温度：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="chgUtResume" value="<#if (entity.chgUtResume)?? >${show_temp(entity.chgUtResume)}</#if>"
                                       style="width: 50px; height: 28px;"/> ℃</td>
                            <td  width="80" align="right">延时：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="chgUtDelay" value="${(entity.chgUtDelay)!''}"
                                       style="width: 50px; height: 28px;"/> S</td>
                        </tr>

                        <tr>
                            <td  width="100" align="right">放电高温：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="dsgOtTrip" value="<#if (entity.dsgOtTrip)?? >${show_temp(entity.dsgOtTrip)}</#if>"
                                       style="width: 50px; height: 28px;"/> ℃</td>
                            <td  width="100" align="right">释放温度：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="dsgOtResume" value="<#if (entity.dsgOtResume)?? >${show_temp(entity.dsgOtResume)}</#if>"
                                       style="width: 50px; height: 28px;"/> ℃</td>
                            <td  width="80" align="right">延时：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="dsgOtDelay" value="${(entity.dsgOtDelay)!''}"
                                       style="width: 50px; height: 28px;"/> S</td>
                        </tr>

                        <tr>
                            <td  width="100" align="right">放电低温：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="dsgUtTrip" value="<#if (entity.dsgUtTrip)?? >${show_temp(entity.dsgUtTrip)}</#if>"
                                       style="width: 50px; height: 28px;"/> ℃</td>
                            <td  width="100" align="right">释放温度：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="dsgUtResume" value="<#if (entity.dsgUtResume)?? >${show_temp(entity.dsgUtResume)}</#if>"
                                       style="width: 50px; height: 28px;"/> ℃</td>
                            <td  width="80" align="right">延时：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="dsgUtDelay" value="${(entity.dsgUtDelay)!''}"
                                       style="width: 50px; height: 28px;"/> S</td>
                        </tr>

                        <tr>
                            <td  width="100" align="right">充电过流：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="chgOcTrip" value="${(entity.chgOcTrip)!''}"
                                       style="width: 50px; height: 28px;"/> mA</td>
                            <td  width="100" align="right">释放时间：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="chgOcRelease" value="${(entity.chgOcRelease)!''}"
                                       style="width: 50px; height: 28px;"/> S</td>
                            <td  width="80" align="right">延时：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="chgOcDelay" value="${(entity.chgOcDelay)!''}"
                                       style="width: 50px; height: 28px;"/> S</td>
                        </tr>

                        <tr>
                            <td  width="100" align="right">放电过流：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="dsgOcTrip" value="${(entity.dsgOcTrip)!''}"
                                       style="width: 50px; height: 28px;"/> mA</td>
                            <td  width="100" align="right">释放时间：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="dsgOcRelease" value="${(entity.dsgOcRelease)!''}"
                                       style="width: 50px; height: 28px;"/> S</td>
                            <td  width="80" align="right">延时：</td>
                            <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="dsgOcDelay" value="${(entity.dsgOcDelay)!''}"
                                       style="width: 50px; height: 28px;"/> S</td>
                        </tr>
                    </table>
            </div>
        </fieldset>

        <fieldset style="margin-top: 10px">
            <legend>
                <span>
                    <label>高级保护</label>
                </span>
            </legend>
            <div class="ui_table">
                <table cellpadding="0" cellspacing="0" class="times_table_list">
                    <tr>
                        <td width="170" align="right">
                            <span class="check_box">
                                <input type="checkbox" class="checkbox rsns_checkbox" <#if entity.rsns?? && entity.rsns == 1>checked</#if> /><label>过流及短路保护值翻倍</label>
                            </span>
                        </td>
                    </tr>
                </table>
                <table cellpadding="0" cellspacing="0" class="times_table_list">
                    <tr>
                        <td  width="120" align="right">硬件过流保护值：</td>
                        <td>
                            <select name="hardOcTrip" id="hardOcTrip_${pid}" class="easyui-combobox" style="width: 184px; height: 28px;">
                            <#if HardOcTrip??>
                                <#list HardOcTrip as e>
                                    <option <#if entity.hardOcTrip?? && entity.hardOcTrip == e.getValue()>selected</#if>
                                            value="${e.getValue()}">${(e.getName())!''}</option>
                                </#list>
                            </#if>
                            </select>
                        </td>
                        <td  width="120" align="right">过流保护延时：</td>
                        <td>
                            <select name="hardOcDelay" id="hardOcDelay_${pid}" class="easyui-combobox" style="width: 184px; height: 28px;">
                            <#if HardOcDelay??>
                                <#list HardOcDelay as e>
                                    <option <#if entity.hardOcDelay?? && entity.hardOcDelay == e.getValue()>selected</#if>
                                            value="${e.getValue()}">${(e.getName())!''}</option>
                                </#list>
                            </#if>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td  width="120" align="right">短路保护值：</td>
                        <td>
                            <select name="scTrip" id="scTrip_${pid}" class="easyui-combobox" style="width: 184px; height: 28px;">
                            <#if ScTrip??>
                                <#list ScTrip as e>
                                    <option <#if entity.scTrip?? && entity.scTrip == e.getValue()>selected</#if>
                                            value="${e.getValue()}">${(e.getName())!''}</option>
                                </#list>
                            </#if>
                            </select>
                        </td>
                        <td  width="120" align="right">短路保护延时：</td>
                        <td>
                            <select name="scDelay" id="scDelay_${pid}" class="easyui-combobox" style="width: 184px; height: 28px;">
                            <#if ScDelay??>
                                <#list ScDelay as e>
                                    <option <#if entity.scDelay?? && entity.scDelay == e.getValue()>selected</#if>
                                            value="${e.getValue()}">${(e.getName())!''}</option>
                                </#list>
                            </#if>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td  width="120" align="right">硬件单体过压：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="hardOvTrip" value="${(entity.hardOvTrip)!''}"
                                   style="width: 100px; height: 28px;"/> mV</td>
                        <td  width="120" align="right">过压保护延时：</td>
                        <td>
                            <select name="hardOvDelay" id="hardOvDelay_${pid}" class="easyui-combobox" style="width: 184px; height: 28px;">
                            <#if HardOvDelay??>
                                <#list HardOvDelay as e>
                                    <option <#if entity.hardOvDelay?? && entity.hardOvDelay == e.getValue()>selected</#if>
                                            value="${e.getValue()}">${(e.getName())!''}</option>
                                </#list>
                            </#if>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td  width="120" align="right">硬件单体欠压：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="hardUvTrip" value="${(entity.hardUvTrip)!''}"
                                   style="width: 100px; height: 28px;"/> mV</td>
                        <td  width="120" align="right">欠压保护延时：</td>
                        <td>
                            <select name="hardUvDelay" id="hardUvDelay_${pid}" class="easyui-combobox" style="width: 184px; height: 28px;">
                            <#if HardUvDelay??>
                                <#list HardUvDelay as e>
                                    <option <#if entity.hardUvDelay?? && entity.hardUvDelay == e.getValue()>selected</#if>
                                            value="${e.getValue()}">${(e.getName())!''}</option>
                                </#list>
                            </#if>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td  width="120" align="right">短路释放时间：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="sdRelease" value="${(entity.sdRelease)!''}"
                                   style="width: 100px; height: 28px;"/> S</td>
                    </tr>
                </table>
            </div>
        </fieldset>

        <fieldset style="margin-top: 10px">
            <legend>
                <span>
                    <label>功能配置</label>
                </span>
            </legend>
            <div class="ui_table">
                <table cellpadding="0" cellspacing="0" class="times_table_list">
                    <tr>
                        <#if FunctionList??>
                            <#list FunctionList as e>
                                <#if e.name??>
                                    <td width="100" align="right">
                                        <span class="check_box">
                                            <input type="checkbox" class="checkbox function_checkbox" <#if e.value == 1>checked</#if> /><label>${(e.name)!''}</label>
                                        </span>
                                    </td>
                                </#if>
                            </#list>
                        </#if>
                    </tr>
                </table>
            </div>
        </fieldset>

        <fieldset style="margin-top: 10px">
            <legend>
                <span>
                    <label>NTC配置</label>
                </span>
            </legend>
            <div class="ui_table">
                <table cellpadding="0" cellspacing="0" class="times_table_list">
                    <tr>
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
                    </tr>
                </table>
            </div>
        </fieldset>

        <fieldset style="margin-top: 10px">
            <legend>
        <span>
            <label>OCV配置</label>
        </span>
            </legend>
            <div class="ui_table">
                <table cellpadding="0" cellspacing="0" class="times_table_list">
                <#if OCVTableList??>
                    <#assign num=0 />
                    <#list OCVTableList as e>
                        <#if num%6==0>
                        <tr>
                        </#if>
                        <td  width="60" align="right">${(e.name)!''}</td>
                        <td><input type="text" class="text  ocv_text"  name="ocvTable" value="${(e.value)!''}"
                                   style="width: 40px; height: 28px;"/></td>
                        <#if (num+1)%6==0 || num ==20>
                        </tr>
                        </#if>
                        <#assign num=num+1 />
                    </#list>
                </#if>
                </table>
            </div>
        </fieldset>

        <fieldset style="margin-top: 10px">
            <legend>
                <span>
                    <label>其他信息配置</label>
                </span>
            </legend>
            <div class="ui_table">
                <table cellpadding="0" cellspacing="0" class="times_table_list">
                    <tr>
                        <td  width="100" align="right">电芯型号：</td>
                        <td><input type="text" class="text"  name="cellModel" value="${(entity.cellModel)!''}"
                                   style="width: 100px; height: 28px;"/></td>
                        <td  width="120" align="right">电芯厂家：</td>
                        <td><input type="text" class="text" name="cellMfr" value="${(entity.cellMfr)!''}"
                                   style="width: 100px; height: 28px;"/></td>
                        <td  width="100" align="right">电池厂家：</td>
                        <td><input type="text" class="text"  name="battMfr" value="${(entity.battMfr)!''}"
                                   style="width: 100px; height: 28px;"/></td>
                    </tr>
                </table>
                <table cellpadding="0" cellspacing="0" class="times_table_list">
                    <tr>
                        <td  width="100" align="right">生产日期：</td>
                        <td><input type="text" class="easyui-numberspinner" name="mfd_yy" value="${(mfd_yy)!''}"
                                   style="width: 60px; height: 28px;"/> -
                            <input type="text" class="easyui-numberspinner" name="mfd_mm" value="${(mfd_mm)!''}"
                                   data-options="min:1, max:12, editable:true" style="width: 60px; height: 28px;"/> -
                            <input type="text" class="easyui-numberspinner" name="mfd_dd" value="${(mfd_dd)!''}"
                                   data-options="min:1, max:31, editable:true" style="width: 60px; height: 28px;"/>
                        </td>
                    </tr>
                </table>
            </div>
        </fieldset>
    </form>
</div>
<script>
    (function () {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];
        var upBmsCheckbox = win.find('.upBms_checkbox')
        var rsnsCheckbox = win.find('.rsns_checkbox');
        var functionCheckbox = win.find('.function_checkbox');
        var ntcCheckbox = win.find('.ntc_checkbox');

        var ok = function () {
            if (!jform.form('validate')) {
                return false;
            }

            var functionStr = '';
            for(var i = 8; i > 0 ; i--) {
                if(functionCheckbox.eq(i-1) == null ){
                    functionStr += "0";
                }else{
                    functionStr += functionCheckbox.eq(i-1).attr('checked') ? '1' : '0';
                }
            }
            functionStr  = parseInt( functionStr,2);

            var ntcStr = '';
            for(var i = 8; i > 0 ; i--) {
                if(ntcCheckbox.eq(i-1) == null){
                    functionStr += "0";
                }else{
                    ntcStr += ntcCheckbox.eq(i-1).attr('checked') ? '1' : '0';
                }
            }
            ntcStr  = parseInt( ntcStr,2);

            var ocvTable = "";
            $(".ocv_text").each(function(i,v){
                ocvTable =ocvTable +  $(this).val() + ",";
            });
            if(ocvTable.length > 0){
                ocvTable = ocvTable.substr(0,ocvTable.length-1);
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
                hardOcTrip: $('#hardOcTrip_${pid}').combobox('getValue'),
                hardOcDelay: $('#hardOcDelay_${pid}').combobox('getValue'),
                scTrip: $('#scTrip_${pid}').combobox('getValue'),
                scDelay: $('#scDelay_${pid}').combobox('getValue'),
                hardOvTrip: form.hardOvTrip.value,
                hardOvDelay: $('#hardOvDelay_${pid}').combobox('getValue'),
                hardUvTrip: form.hardUvTrip.value,
                hardUvDelay: $('#hardUvDelay_${pid}').combobox('getValue'),
                sdRelease: form.sdRelease.value,
                function: functionStr,
                ntcConfig: ntcStr,
                ocvTable:ocvTable,
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

        win.data('ok', ok);

    })();

    function dealTemp(temp) {
        if (temp != null && temp != '') {
            return Number(temp) * 10 + 2731;
        }
        return '';
    }
</script>