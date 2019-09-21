<div class="popup_body">
    <div class="ui_table" style="width:760px;height: 475px;">
        <form method="post" style="width: 100%;height: 100%;">
            <table cellpadding="0" cellspacing="0" style="height: 50px;">
                <tr>
                    <td align="right">电芯条码：</td>
                    <td>
                        <input type="text" required="true" class="text easyui-validatebox" name="barcode"
                               id="barcode_${pid}" maxlength="40" value="${(entity.barcode)!''}"/>
                    </td>
                </tr>
            </table>
            <table class="barcodeInfoTable" id="showBarcodeInfo" cellpadding="40" cellspacing="20" style="width: 100%;height: 80px;">
                <tr style="width: 100%">
                    <td style="width: 17%" align="right" >电芯厂家：</td>
                    <td style="width: 33%"><span id="cell_mfr_${pid}">${(entity.cellMfr)!''}</span></td>
                    <td style="width: 15%" align="right" >电芯型号：</td>
                    <td style="width: 35%"><span id="cell_model_${pid}">${(entity.cellModel)!''}</span></td>
                </tr>
                <tr style="width: 100%">
                    <td style="width: 15%" align="right">检验人：</td>
                    <td style="width: 35%"><span id="operator_${pid}"> ${(entity.operator)!''}</span></td>
                    <td style="width: 15%" align="right">检验时间：</td>
                    <td style="width: 35%"><span id="create_time_${pid}"><#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if></span></td>
                </tr>
            </table>
            <!-- Table goes in the document BODY -->
            <table class="formatInfoTable" id="showFormatInfo" style="width: 100%;height: 350px;">
                <tr style="height: 35px;">
                    <th style="width: 20%" align="center">项目</th><th style="width: 20%" align="center">标准值</th><th style="width: 20%" align="center">偏差（-,+）</th><th style="width: 20%" align="center" >记录</th><th style="width: 20%" align="center" >结果</th>
                </tr>
                <tr style="height: 45px;">
                    <td align="center" >组包容量</td><td align="center"><span id="nominal_cap_${pid}"></span></td><td align="center"><span id="nominal_cap_range_${pid}"></span></td><td><input type="text" id="real_nominal_cap_${pid}" name="nominalCap"  class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10"  value="${(entity.nominalCap/1000)!''}" readonly></td><td align="center"><span id="nominal_cap_result_${pid}"></span></td>
                </tr>
                <tr style="height: 45px;">
                    <td align="center" >交流电阻</td><td align="center"><span id="ac_resistance_${pid}"></td><td align="center"><span id="ac_resistance_range_${pid}"></td><td><input type="text" id="real_ac_resistance_${pid}" name="acResistance" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" value="${(entity.acResistance)}" readonly></td><td align="center"><span id="ac_resistance_result_${pid}"></span></td>
                </tr>
                <tr style="height: 45px;">
                    <td align="center" >回弹电压</td><td align="center"><span id="resilience_vol_${pid}"></td><td align="center"><span id="resilience_vol_range_${pid}"></td><td><input type="text" id="real_resilience_vol_${pid}" name="resilienceVol" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" value="${(entity.resilienceVol/1000)!''}" readonly></td><td align="center"><span id="resilience_vol_result_${pid}"></span></td>
                </tr>
                <tr style="height: 45px;">
                    <td align="center" >静置电压</td><td align="center"><span id="static_vol_${pid}"></td><td align="center"><span id="static_vol_range_${pid}"></td><td><input type="text" id="real_static_vol_${pid}" name="staticVol" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" value="${(entity.staticVol/1000)!''}" readonly></td><td align="center"><span id="static_vol_result_${pid}"></span></td>
                </tr>
                <tr style="height: 45px;">
                    <td align="center" >当前循环</td><td align="center"><span id="circle_${pid}"></td><td align="center"><span id="circle_range_${pid}"></td><td><input type="text" id="real_circle_${pid}" name="circle" class="easyui-numberbox" style="width: 185px;height: 28px;" value="${(entity.circle)!''}" readonly></td><td align="center"><span id="circle_result_${pid}"></span></td>
                </tr>
                <tr style="height: 45px;">
                    <td align="center">外观</td>
                    <td align="center">无</td>
                    <td align="center">无</td>
                    <td>
                        <select class="easyui-combobox" required="true" readonly="readonly" name="appearance"
                                style="width:185px;height:28px ">
                        <#list AppearanceEnum as s>
                            <option value="${s.getValue()}"
                                    <#if entity.appearance?? && entity.appearance==s.getValue()>selected</#if>>${s.getName()}</option>
                        </#list>
                        </select>
                    </td>
                    <td align="center">无</span></td>
                </tr>
            </table>
            <input type="hidden" id="cell_format_id_${pid}" value="${(cellFormatId)!''}">
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script>

    (function () {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        var cellFormatId = $('#cell_format_id_${pid}').val();
        if(cellFormatId == null || cellFormatId =='') {
            $.messager.alert('提示信息','信息错误，无法查看','info');
            win.window('close');
            return false;
        }else{
            $.ajax({
                type: 'POST',
                url: '${contextPath}/security/hdg/battery_cell_format/find_format.htm',
                dataType: 'json',
                data: {
                    id: $('#cell_format_id_${pid}').val()
                },
                success: function (json2) {
                    var data2 = json2.data;
                    $('#nominal_cap_${pid}').html(data2.nominalCap);
                    if (data2.minNominalCap != null) {
                        var nominalCapHtml1 = "-" + data2.minNominalCap;
                    } else {
                        var nominalCapHtml1 = "-" + 0;
                    }
                    if (data2.maxNominalCap != null) {
                        var nominalCapHtml2 = "+" + data2.maxNominalCap;
                    } else {
                        var nominalCapHtml2 = "+" + 0;
                    }
                    $('#nominal_cap_range_${pid}').html(nominalCapHtml1 + " , " + nominalCapHtml2);

                    $('#ac_resistance_${pid}').html(data2.acResistance);
                    if (data2.minAcResistance != null) {
                        var acResistanceHtml1 = "-" + data2.minAcResistance;
                    } else {
                        var acResistanceHtml1 = "-" + 0;
                    }
                    if (data2.maxAcResistance != null) {
                        var acResistanceHtml2 = "+" + data2.maxAcResistance;
                    } else {
                        var acResistanceHtml2 = "+" + 0;
                    }
                    $('#ac_resistance_range_${pid}').html(acResistanceHtml1 + " , " + acResistanceHtml2);

                    $('#resilience_vol_${pid}').html(data2.resilienceVol);
                    if (data2.minResilienceVol != null) {
                        var resilienceVolHtml1 = "-" + data2.minResilienceVol;
                    } else {
                        var resilienceVolHtml1 = "-" + 0;
                    }
                    if (data2.maxResilienceVol != null) {
                        var resilienceVolHtml2 = "+" + data2.maxResilienceVol;
                    } else {
                        var resilienceVolHtml2 = "+" + 0;
                    }
                    $('#resilience_vol_range_${pid}').html(resilienceVolHtml1 + " , " + resilienceVolHtml2);

                    $('#static_vol_${pid}').html(data2.staticVol);
                    if (data2.minStaticVol != null) {
                        var staticVolHtml1 = "-" + data2.minStaticVol;
                    } else {
                        var staticVolHtml1 = "-" + 0;
                    }
                    if (data2.maxStaticVol != null) {
                        var staticVolHtml2 = "+" + data2.maxStaticVol;
                    } else {
                        var staticVolHtml2 = "+" + 0;
                    }
                    $('#static_vol_range_${pid}').html(staticVolHtml1 + " , " + staticVolHtml2);

                    $('#circle_${pid}').html(data2.circle);
                    if (data2.minCircle != null) {
                        var circleHtml1 = "-" + data2.minCircle;
                    } else {
                        var circleHtml1 = "-" + 0;
                    }
                    if (data2.maxCircle != null) {
                        var circleHtml2 = "+" + data2.maxCircle;
                    } else {
                        var circleHtml2 = "+" + 0;
                    }
                    $('#circle_range_${pid}').html(circleHtml1 + " , " + circleHtml2);
                }
            });
            $('#nominal_cap_result_${pid}').html('成功');
            $('#nominal_cap_result_${pid}').css("color", "green");
            $('#ac_resistance_result_${pid}').html('成功');
            $('#ac_resistance_result_${pid}').css("color", "green");
            $('#resilience_vol_result_${pid}').html('成功');
            $('#resilience_vol_result_${pid}').css("color", "green");
            $('#static_vol_result_${pid}').html('成功');
            $('#static_vol_result_${pid}').css("color", "green");
            $('#circle_result_${pid}').html('成功');
            $('#circle_result_${pid}').css("color", "green");
        }

        win.find('button.close').click(function () {
            win.window('close');
        });
    })()
</script>