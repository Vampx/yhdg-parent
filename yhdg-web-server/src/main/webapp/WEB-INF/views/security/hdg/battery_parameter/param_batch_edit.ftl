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
<div class="tab_item" style="display:block;width: 870px;margin-left: 70px;">
    <form>
        <#--<div style="text-align:right"><a style="color: #ff0000;">最后同步时间&nbsp;&nbsp; <#if (entity.shortReportTime)??>短心跳：${app.format_date_time(entity.shortReportTime)}</#if>&nbsp; <#if (entity.longReportTime)??>长心跳：${app.format_date_time(entity.longReportTime)}</#if></a></div>-->
        <fieldset style="margin-top: 10px">
            <legend>
                <span>
                    <label>锁定显示</label>
                </span>
            </legend>
            <div class="ui_table">
                <table cellpadding="0" cellspacing="0" class="times_table_list">
                    <tr>
                        <#--<td  width="100" align="right">MOS：</td>-->
                        <#--<td><input type="text" class="text"  name="mosName" value="${(entity.mosName)!''}"-->
                                   <#--style="width: 100px; height: 28px;" disabled/></td>-->
<#--                        <td  width="100" align="right">租期：</td>
                        <td><input type="text" class="text"  name="batteryLease" value="${(entity.batteryLease)!''}"
                                   style="width: 100px; height: 28px;" disabled/>秒</td>-->

                        <td width="170" align="right">
                            <span class="check_box">
                                <input type="checkbox" class="checkbox upBms_checkbox" /><label>立即上传BMS参数</label>
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
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="heartInterval" value=""
                                   style="width: 50px; height: 28px;"/> S</td>
                        <td  width="120" align="right">静止心跳间隔：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="motionless" value=""
                                   style="width: 50px; height: 28px;"/> S</td>
                        <td  width="120" align="right">存储心跳间隔：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="standby" value=""
                                   style="width: 50px; height: 28px;"/> S</td>
                    </tr>
                    <tr>
                        <td  width="120" align="right">休眠心跳间隔：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="dormancy" value=""
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
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="nominalCapacity" value=""
                                   style="width: 50px; height: 28px;"/> mAH</td>
                        <td  width="100" align="right">循环容量：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="circleCapacity" value=""
                                   style="width: 50px; height: 28px;"/> mAH</td>
                        <td  width="100" align="right">自放电率：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="selfDsgRate" value=""
                                   style="width: 50px; height: 28px;"/> %</td>
                    </tr>
                    <tr>
                        <td  width="100" align="right">单体充满电压：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="cellFullVol" value=""
                                   style="width: 50px; height: 28px;"/> mV</td>
                        <td  width="100" align="right">单体截止电压：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="cellCutVol" value=""
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
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="cellOvTrip" value=""
                                   style="width: 50px; height: 28px;"/> mV</td>
                        <td  width="100" align="right">释放电压：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="cellOvResume" value=""
                                   style="width: 50px; height: 28px;"/> mV</td>
                        <td  width="80" align="right">延时：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="cellOvDelay" value=""
                                   style="width: 50px; height: 28px;"/> S</td>
                    </tr>

                    <tr>
                        <td  width="100" align="right">单体欠压：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="cellUvTrip" value=""
                                   style="width: 50px; height: 28px;"/> mV</td>
                        <td  width="100" align="right">释放电压：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="cellUvResume" value=""
                                   style="width: 50px; height: 28px;"/> mV</td>
                        <td  width="80" align="right">延时：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="cellUvDelay" value=""
                                   style="width: 50px; height: 28px;"/> S</td>
                    </tr>

                    <tr>
                        <td  width="100" align="right">整组过压：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="packOvTrip" value=""
                                   style="width: 50px; height: 28px;"/> mV</td>
                        <td  width="100" align="right">释放电压：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="packOvResume" value=""
                                   style="width: 50px; height: 28px;"/> mV</td>
                        <td  width="80" align="right">延时：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="packOvDelay" value=""
                                   style="width: 50px; height: 28px;"/> S</td>
                    </tr>

                    <tr>
                        <td  width="100" align="right">整组欠压：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="packUvTrip" value=""
                                   style="width: 50px; height: 28px;"/> mV</td>
                        <td  width="100" align="right">释放电压：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="packUvResume" value=""
                                   style="width: 50px; height: 28px;"/> mV</td>
                        <td  width="80" align="right">延时：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="packUvDelay" value=""
                                   style="width: 50px; height: 28px;"/> S</td>
                    </tr>

                    <tr>
                        <td  width="100" align="right">充电高温：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="chgOtTrip" value=""
                                   style="width: 50px; height: 28px;"/> ℃</td>
                        <td  width="100" align="right">释放温度：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="chgOtResume" value=""
                                   style="width: 50px; height: 28px;"/> ℃</td>
                        <td  width="80" align="right">延时：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="chgOtDelay" value=""
                                   style="width: 50px; height: 28px;"/> S</td>
                    </tr>

                    <tr>
                        <td  width="100" align="right">充电低温：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="chgUtTrip" value=""
                                   style="width: 50px; height: 28px;"/> ℃</td>
                        <td  width="100" align="right">释放温度：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="chgUtResume" value=""
                                   style="width: 50px; height: 28px;"/> ℃</td>
                        <td  width="80" align="right">延时：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="chgUtDelay" value=""
                                   style="width: 50px; height: 28px;"/> S</td>
                    </tr>

                    <tr>
                        <td  width="100" align="right">放电高温：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="dsgOtTrip" value=""
                                   style="width: 50px; height: 28px;"/> ℃</td>
                        <td  width="100" align="right">释放温度：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="dsgOtResume" value=""
                                   style="width: 50px; height: 28px;"/> ℃</td>
                        <td  width="80" align="right">延时：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="dsgOtDelay" value=""
                                   style="width: 50px; height: 28px;"/> S</td>
                    </tr>

                    <tr>
                        <td  width="100" align="right">放电低温：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="dsgUtTrip" value=""
                                   style="width: 50px; height: 28px;"/> ℃</td>
                        <td  width="100" align="right">释放温度：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="dsgUtResume" value=""
                                   style="width: 50px; height: 28px;"/> ℃</td>
                        <td  width="80" align="right">延时：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="dsgUtDelay" value=""
                                   style="width: 50px; height: 28px;"/> S</td>
                    </tr>

                    <tr>
                        <td  width="100" align="right">充电过流：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="chgOcTrip" value=""
                                   style="width: 50px; height: 28px;"/> mA</td>
                        <td  width="100" align="right">释放时间：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="chgOcRelease" value=""
                                   style="width: 50px; height: 28px;"/> S</td>
                        <td  width="80" align="right">延时：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="chgOcDelay" value=""
                                   style="width: 50px; height: 28px;"/> S</td>
                    </tr>

                    <tr>
                        <td  width="100" align="right">放电过流：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="dsgOcTrip" value=""
                                   style="width: 50px; height: 28px;"/> mA</td>
                        <td  width="100" align="right">释放时间：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="dsgOcRelease" value=""
                                   style="width: 50px; height: 28px;"/> S</td>
                        <td  width="80" align="right">延时：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="dsgOcDelay" value=""
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
                                <input type="checkbox" class="checkbox rsns_checkbox" /><label>过流及短路保护值翻倍</label>
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
                                    <option value=""></option>
                                    <option value="${e.getValue()}">${(e.getName())!''}</option>
                                </#list>
                            </#if>
                            </select>
                        </td>
                        <td  width="120" align="right">过流保护延时：</td>
                        <td>
                            <select name="hardOcDelay" id="hardOcDelay_${pid}" class="easyui-combobox" style="width: 184px; height: 28px;">
                            <#if HardOcDelay??>
                                <#list HardOcDelay as e>
                                    <option value=""></option>
                                    <option value="${e.getValue()}">${(e.getName())!''}</option>
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
                                    <option value=""></option>
                                    <option value="${e.getValue()}">${(e.getName())!''}</option>
                                </#list>
                            </#if>
                            </select>
                        </td>
                        <td  width="120" align="right">短路保护延时：</td>
                        <td>
                            <select name="scDelay" id="scDelay_${pid}" class="easyui-combobox" style="width: 184px; height: 28px;">
                            <#if ScDelay??>
                                <#list ScDelay as e>
                                    <option value=""></option>
                                    <option value="${e.getValue()}">${(e.getName())!''}</option>
                                </#list>
                            </#if>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td  width="120" align="right">硬件单体过压：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="hardOvTrip" value=""
                                   style="width: 100px; height: 28px;"/> mV</td>
                        <td  width="120" align="right">过压保护延时：</td>
                        <td>
                            <select name="hardOvDelay" id="hardOvDelay_${pid}" class="easyui-combobox" style="width: 184px; height: 28px;">
                            <#if HardOvDelay??>
                                <#list HardOvDelay as e>
                                    <option value=""></option>
                                    <option value="${e.getValue()}">${(e.getName())!''}</option>
                                </#list>
                            </#if>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td  width="120" align="right">硬件单体欠压：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]"  name="hardUvTrip" value=""
                                   style="width: 100px; height: 28px;"/> mV</td>
                        <td  width="120" align="right">欠压保护延时：</td>
                        <td>
                            <select name="hardUvDelay" id="hardUvDelay_${pid}" class="easyui-combobox" style="width: 184px; height: 28px;">
                            <#if HardUvDelay??>
                                <#list HardUvDelay as e>
                                    <option value=""></option>
                                    <option value="${e.getValue()}">${(e.getName())!''}</option>
                                </#list>
                            </#if>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td  width="120" align="right">短路释放时间：</td>
                        <td><input type="text" class="text easyui-validatebox"  validType="positiveInt[]" name="sdRelease" value=""
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
                        <td width="100" align="right">
                            <span class="check_box">
                                <input type="checkbox" class="checkbox function_checkbox"/><label>开关功能</label>
                            </span>
                        </td>
                        <td width="100" align="right">
                                   <span class="check_box">
                                <input type="checkbox" class="checkbox function_checkbox"/><label>负载检验</label>
                            </span>
                        </td>
                        <td width="100" align="right">
                                  <span class="check_box">
                                <input type="checkbox" class="checkbox function_checkbox"/><label>均衡功能</label>
                            </span>
                        </td>
                        <td width="100" align="right">
                                <span class="check_box">
                                <input type="checkbox" class="checkbox function_checkbox"/><label>充电均衡</label>
                            </span>
                        </td>
                        <td width="100" align="right">
                                 <span class="check_box">
                                <input type="checkbox" class="checkbox function_checkbox"/><label>LED功能</label>
                            </span>
                        </td>
                        <td width="100" align="right">
                                <span class="check_box">
                                <input type="checkbox" class="checkbox function_checkbox"/><label>5个LED</label>
                            </span>
                        </td>
                        <td width="100" align="right">
                                  <span class="check_box">
                                <input type="checkbox" class="checkbox function_checkbox"/><label>预留</label>
                            </span>
                        </td>
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
                        <td width="100" align="right">
                            <span class="check_box">
                                <input type="checkbox" class="checkbox ntc_checkbox"/><label>NTC1</label>
                            </span>
                        </td>
                        <td width="100" align="right">
                            <span class="check_box">
                                <input type="checkbox" class="checkbox ntc_checkbox"/><label>NTC2</label>
                            </span>
                        </td>
                        <td width="100" align="right">
                            <span class="check_box">
                                <input type="checkbox" class="checkbox ntc_checkbox"/><label>NTC3</label>
                            </span>
                        </td>
                        <td width="100" align="right">
                            <span class="check_box">
                                <input type="checkbox" class="checkbox ntc_checkbox"/><label>NTC4</label>
                            </span>
                        </td>
                        <td width="100" align="right">
                              <span class="check_box">
                                <input type="checkbox" class="checkbox ntc_checkbox"/><label>NTC5</label>
                            </span>
                        </td>
                        <td width="100" align="right">
                             <span class="check_box">
                                <input type="checkbox" class="checkbox ntc_checkbox"/><label>NTC6</label>
                            </span>
                        </td>
                        <td width="100" align="right">

                            <span class="check_box">
                                <input type="checkbox" class="checkbox ntc_checkbox"/><label>NTC7</label>
                            </span>
                        </td>
                        <td width="100" align="right">
                            <span class="check_box">
                                <input type="checkbox" class="checkbox ntc_checkbox"/><label>NTC8</label>
                            </span>
                        </td>
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
                    <tr>
                        <td width="60" align="right">0%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                        <td width="60" align="right">5%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                        <td width="60" align="right">10%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                        <td width="60" align="right">15%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                        <td width="60" align="right">20%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                        <td width="60" align="right">25%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                    </tr>
                    <tr>
                        <td width="60" align="right">30%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                        <td width="60" align="right">35%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                        <td width="60" align="right">40%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                        <td width="60" align="right">45%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                        <td width="60" align="right">50%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                        <td width="60" align="right">55%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                    </tr>
                    <tr>
                        <td width="60" align="right">60%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                        <td width="60" align="right">65%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                        <td width="60" align="right">70%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                        <td width="60" align="right">75%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                        <td width="60" align="right">80%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                        <td width="60" align="right">85%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                    </tr>
                    <tr>
                        <td width="60" align="right">90%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                        <td width="60" align="right">95%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                        <td width="60" align="right">100%</td>
                        <td><input type="text" class="text  ocv_text" name="ocvTable" value=""
                                   style="width: 40px; height: 28px;"/></td>
                    </tr>
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
                        <td><input type="text" class="text"  name="cellModel" value=""
                                   style="width: 100px; height: 28px;"/></td>
                        <td  width="120" align="right">电芯厂家：</td>
                        <td><input type="text" class="text" name="cellMfr" value=""
                                   style="width: 100px; height: 28px;"/></td>
                        <td  width="100" align="right">电池厂家：</td>
                        <td><input type="text" class="text"  name="battMfr" value=""
                                   style="width: 100px; height: 28px;"/></td>
                    </tr>
                </table>
                <table cellpadding="0" cellspacing="0" class="times_table_list">
                    <tr>
                        <td  width="100" align="right">生产日期：</td>
                        <td><input type="text" class="easyui-numberspinner" name="mfd_yy" value=""
                                   style="width: 60px; height: 28px;"/> -
                            <input type="text" class="easyui-numberspinner" name="mfd_mm" value=""
                                   data-options="min:1, max:12, editable:true" style="width: 60px; height: 28px;"/> -
                            <input type="text" class="easyui-numberspinner" name="mfd_dd" value=""
                                   data-options="min:1, max:31, editable:true" style="width: 60px; height: 28px;"/>
                        </td>
                    </tr>
                </table>
            </div>
        </fieldset>
    </form>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function () {
        var win = $('#${pid}');
        var jform = win.find('form');
        var form = jform[0];
        var upBmsCheckbox = win.find('.upBms_checkbox');
        var rsnsCheckbox = win.find('.rsns_checkbox');
        var functionCheckbox = win.find('.function_checkbox');
        var ntcCheckbox = win.find('.ntc_checkbox');


        win.find('.ok').click(function() {
            var ok = win.data('ok')();
            if(ok) {
                win.window('close');
            }
        });
        win.find('.close').click(function() {
            win.window('close');
        });

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
                var ocvValue = $(this).val();
                if(ocvValue == null || ocvValue == '') {
                    ocvTable = ocvTable + "无" + ",";
                }else{
                    ocvTable =ocvTable + ocvValue + ",";
                }
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
                idsData: '${(idsData)!''}',
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
                url: '${contextPath}/security/hdg/battery_parameter/batch_update.htm',
                dataType: 'json',
                data: values,
                success: function (json) {
                <@app.json_jump/>
                    if (json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
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