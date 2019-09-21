<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="90" align="right">电芯厂家：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="cellMfr" value="${(entity.cellMfr)!''}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">电芯型号：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="cellModel" value="${(entity.cellModel)!''}"/>&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">规格名称：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="cellFormatName" value="${(entity.cellFormatName)!''}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">条码规则：</td>
                    <td colspan="3">
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="barcodeRule" value="${(entity.barcodeRule)!''}"/>&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">充电截至电压：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="chgCutVol" value="${(entity.chgCutVol)!''}"/>V
                    </td>
                    <td width="90" align="right">标称电压：</td>
                    <td>
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="nominalVol" value="${(entity.nominalVol)!''}"/>V
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">充电温度：</td>
                    <td>
                        <input type="text" style="width: 66px;" class="text easyui-validatebox" readonly="readonly" name="minChgTemp" value="${(entity.minChgTemp)!''}"/>℃
                        -
                        <input type="text" style="width: 67px;" class="text easyui-validatebox" readonly="readonly" name="maxChgTemp" value="${(entity.maxChgTemp)!''}"/>℃
                    </td>
                    <td width="90" align="right">放电温度：</td>
                    <td>
                        <input type="text" style="width: 67px;" class="text easyui-validatebox" readonly="readonly" name="minDsgTemp" value="${(entity.minDsgTemp)!''}"/>℃
                        -
                        <input type="text" style="width: 67px;" class="text easyui-validatebox" readonly="readonly" name="maxDsgTemp" value="${(entity.maxDsgTemp)!''}"/>℃
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">组包容量：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="nominalCap" value="${(entity.nominalCap)!''}"/>Ah&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">标称范围：</td>
                    <td>
                        <select style="width: 33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 40px;" class="text easyui-validatebox" readonly="readonly" name="minNominalCap" value="${(entity.minNominalCap)!''}"/>
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 40px;" class="text easyui-validatebox" readonly="readonly" name="maxNominalCap" value="${(entity.maxNominalCap)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">交流内阻：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="acResistance" value="${(entity.acResistance)!''}"/>mΩ&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">交流内阻范围：</td>
                    <td>
                        <select style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 40px;" class="text easyui-validatebox" readonly="readonly" name="minAcResistance" value="${(entity.minAcResistance)!''}"/>
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 40px;" class="text easyui-validatebox" readonly="readonly" name="maxAcResistance" value="${(entity.maxAcResistance)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">回弹电压：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="resilienceVol"  value="${(entity.resilienceVol)!''}"/>V&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">回弹电压范围：</td>
                    <td>
                        <select style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 40px;" class="text easyui-validatebox" readonly="readonly" name="minResilienceVol" value="${(entity.minResilienceVol)!''}"/>
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 40px;" class="text easyui-validatebox" readonly="readonly" name="maxResilienceVol" value="${(entity.maxResilienceVol)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">静置电压：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="staticVol" value="${(entity.staticVol)!''}"/>V&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">静置电压范围：</td>
                    <td>
                        <select style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 40px;" class="text easyui-validatebox" readonly="readonly" name="minStaticVol" value="${(entity.minStaticVol)!''}"/>
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 40px;" class="text easyui-validatebox" readonly="readonly" name="maxStaticVol" value="${(entity.maxStaticVol)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">循环次数：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="circle" value="${(entity.circle)!''}"/>次&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">循环次数范围：</td>
                    <td>
                        <select style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 40px;" class="text easyui-validatebox" readonly="readonly"  name="minCircle"  value="${(entity.minCircle)!''}"/>
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 40px;" class="text easyui-validatebox" readonly="readonly" name="maxCircle" value="${(entity.maxCircle)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">充电电流倍率：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="chgRate" value="${(entity.chgRate)!''}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">充电电流：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="chgCurrent" value="${(entity.chgCurrent)!''}"/>A
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">充电时间：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="chgTime" value="${(entity.chgTime)!''}"/>小时&nbsp;
                    </td>
                    <td width="90" align="right">放电截至电压：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="dsgCutVol" value="${(entity.dsgCutVol)!''}"/>V
                    </td>
                </tr>
                <tr>
                    <td width="120" align="right">最大持续充电电流：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="maxContinueChgCurrent" value="${(entity.maxContinueChgCurrent)!''}"/>A&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="120" align="right">最大持续放电电流：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" readonly="readonly" name="maxContinueDsgCurrent" value="${(entity.maxContinueDsgCurrent)!''}"/>A
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