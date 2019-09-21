<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="90" align="right">电芯厂家：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="cellMfr" value="${(entity.cellMfr)!''}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">电芯型号：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" style="width:175px;height: 28px;" readonly="readonly" name="cellModel" value="${(entity.cellModel)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">规格名称：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="batteryFormatName" value="${(entity.batteryFormatName)!''}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">条码规则：</td>
                    <td colspan="3">
                        <input type="text" class="text easyui-validatebox" style="width:175px;height: 28px;" readonly="readonly" name="barcodeRule" value="${(entity.barcodeRule)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">组包容量：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="nominalCap" value="${(entity.nominalCap)!''}"/>Ah&nbsp;
                    </td>
                    <td width="90" align="right">标称范围：</td>
                    <td>
                        <select style="width: 33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10" readonly="readonly" name="minNominalCap" value="${(entity.minNominalCap)!''}"/>
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10" readonly="readonly" name="maxNominalCap" value="${(entity.maxNominalCap)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">交流内阻：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" readonly="readonly" name="acResistance" value="${(entity.acResistance)!''}"/>mΩ
                    </td>
                    <td width="90" align="right">交流内阻范围：</td>
                    <td>
                        <select style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10" readonly="readonly" name="minAcResistance" value="${(entity.minAcResistance)!''}"/>
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10" readonly="readonly" name="maxAcResistance" value="${(entity.maxAcResistance)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">回弹电压：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" readonly="readonly" name="resilienceVol"  value="${(entity.resilienceVol)!''}"/>V&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    </td>
                    <td width="90" align="right">回弹电压范围：</td>
                    <td>
                        <select style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10" readonly="readonly" name="minResilienceVol" value="${(entity.minResilienceVol)!''}"/>
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10" readonly="readonly" name="maxResilienceVol" value="${(entity.maxResilienceVol)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">静置电压：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" readonly="readonly" name="staticVol" value="${(entity.staticVol)!''}"/>V&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">静置电压范围：</td>
                    <td>
                        <select style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10" readonly="readonly" name="minStaticVol" value="${(entity.minStaticVol)!''}"/>
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10" readonly="readonly" name="maxStaticVol" value="${(entity.maxStaticVol)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">循环次数：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" style="width:184px;height: 28px;" maxlength="10" readonly="readonly" name="circle" value="${(entity.circle)!''}"/>次&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">循环次数范围：</td>
                    <td>
                        <select style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 43px;" class="text easyui-validatebox" readonly="readonly"  name="minCircle"  value="${(entity.minCircle)!''}"/>
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 43px;" class="text easyui-validatebox" readonly="readonly" name="maxCircle" value="${(entity.maxCircle)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">电芯串数：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" maxlength="4" name="cellCount" value="${(entity.cellCount)!''}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">标称容量：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3"
                               style="width:184px;height: 28px;" maxlength="10" name="nominalPow" readonly
                               value="${(entity.nominalPow)!''}" required="true"/>Ah&nbsp;
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var pid = '${pid}', win = $('#' + pid), form = win.find('form');
        $("select").each(function () {
            $(this).attr("disabled","disabled");
        });
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>