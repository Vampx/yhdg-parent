<div class="popup_body">
    <div class="ui_table" style="width:660px;height: 375px;">
        <table cellpadding="0" cellspacing="0" style="height: 40px;">
            <tr>
                <td align="right">电芯条码：</td>
                <td>
                    <input type="text" required="true" style="width: 200px;" class="text easyui-validatebox" name="barcode"
                           id="barcode_${pid}" maxlength="40" value=""/>
                </td>
            </tr>
        </table>
        <table class="barcodeInfoTable" id="showBarcodeInfo" cellpadding="40" cellspacing="20"
               style="width: 100%;height: 50px;">
            <tr style="width: 100%">
                <td style="width: 15%" align="right">电芯厂家：</td>
                <td style="width: 35%"><span id="bind_cell_mfr_${pid}"></span></td>
                <td style="width: 15%" align="right">电芯型号：</td>
                <td style="width: 35%"><span id="bind_cell_model_${pid}"></span></td>
            </tr>
        </table>
        <!-- Table goes in the document BODY -->
        <table class="formatInfoTable" id="showFormatInfo" style="width: 100%;height: 240px;">
            <tr style="height: 30px;">
                <th style="width: 20%" align="center">项目</th>
                <th style="width: 20%" align="center">标准值</th>
                <th style="width: 20%" align="center">偏差（-,+）</th>
                <th style="width: 20%" align="center">记录（电芯串数${(formatCellCount)!''}）</th>
                <th style="width: 20%" align="center">结果</th>
            </tr>
            <tr style="height: 30px;">
                <td align="center">组包容量</td>
                <td align="center"><span id="nominal_cap_${pid}"></span></td>
                <td align="center"><span id="nominal_cap_range_${pid}"></span></td>
                <td><input type="text" id="real_nominal_cap_${pid}" name="nominalCap" class="easyui-numberbox"
                           style="width: 185px;height: 25px;" disabled></td>
                <td align="center"><span id="nominal_cap_result_${pid}"></span></td>
            </tr>
            <tr style="height: 30px;">
                <td align="center">交流电阻</td>
                <td align="center"><span id="ac_resistance_${pid}"></td>
                <td align="center"><span id="ac_resistance_range_${pid}"></td>
                <td><input type="text" id="real_ac_resistance_${pid}" name="acResistance" class="easyui-numberbox"
                           data-options="precision:2" style="width: 185px;height: 25px;" disabled></td>
                <td align="center"><span id="ac_resistance_result_${pid}"></span></td>
            </tr>
            <tr style="height: 30px;">
                <td align="center">回弹电压</td>
                <td align="center"><span id="resilience_vol_${pid}"></td>
                <td align="center"><span id="resilience_vol_range_${pid}"></td>
                <td><input type="text" id="real_resilience_vol_${pid}" name="resilienceVol" class="easyui-numberbox"
                           style="width: 185px;height: 25px;" disabled></td>
                <td align="center"><span id="resilience_vol_result_${pid}"></span></td>
            </tr>
            <tr style="height: 30px;">
                <td align="center">静置电压</td>
                <td align="center"><span id="static_vol_${pid}"></td>
                <td align="center"><span id="static_vol_range_${pid}"></td>
                <td><input type="text" id="real_static_vol_${pid}" name="staticVol" class="easyui-numberbox"
                           style="width: 185px;height: 25px;" disabled></td>
                <td align="center"><span id="static_vol_result_${pid}"></span></td>
            </tr>
            <tr style="height: 30px;">
                <td align="center">当前循环</td>
                <td align="center"><span id="circle_${pid}"></td>
                <td align="center"><span id="circle_range_${pid}"></td>
                <td><input type="text" id="real_circle_${pid}" name="circle" class="easyui-numberbox"
                           style="width: 185px;height: 25px;" disabled></td>
                <td align="center"><span id="circle_result_${pid}"></span></td>
            </tr>
        <#--<tr style="height: 25px;">-->
        <#--<td align="center">外观</td>-->
        <#--<td align="center"><span id="appearance_${pid}"></td>-->
        <#--<td align="center"><span id="appearance_range_${pid}"></td>-->
        <#--<td><input type="text" id="real_appearance_${pid}" name="appearance" class="easyui-numberbox"-->
        <#--style="width: 185px;height: 20px;" disabled></td>-->
        <#--<td align="center"><span id="appearance_result_${pid}"></span></td>-->
        <#--</tr>-->
        </table>
        <input type="hidden" id="check_nominal_cap_${pid}"><input type="hidden" id="check_min_nominal_cap_${pid}"><input
            type="hidden" id="check_max_nominal_cap_${pid}">
        <input type="hidden" id="check_ac_resistance_${pid}"><input type="hidden"
                                                                    id="check_min_ac_resistance_${pid}"><input
            type="hidden" id="check_max_ac_resistance_${pid}">
        <input type="hidden" id="check_resilience_vol_${pid}"><input type="hidden" id="check_min_resilience_vol_${pid}"><input
            type="hidden" id="check_max_resilience_vol_${pid}">
        <input type="hidden" id="check_static_vol_${pid}"><input type="hidden" id="check_min_static_vol_${pid}"><input
            type="hidden" id="check_max_static_vol_${pid}">
        <input type="hidden" id="check_circle_${pid}"><input type="hidden" id="check_min_circle_${pid}"><input
            type="hidden" id="check_max_circle_${pid}">
    <#--<input type="hidden" id="check_appearance_${pid}">-->
        <input type="hidden" id="cell_id_${pid}">
        <input type="hidden" id="battery_id_${pid}" value="${(batteryId)!''}">
        <input type="hidden" id="check_format_id_${pid}" value="${(batteryFormatId)!''}">
        <input type="hidden" id="battery_cell_mfr_${pid}" value="${(cellMfr)!''}">
        <input type="hidden" id="battery_cell_model_${pid}" value="${(cellModel)!''}">
        <input type="hidden" id="to_bind_cell_mfr_${pid}">
        <input type="hidden" id="to_bind_cell_model_${pid}">
        <input type="hidden" id="submit_type_${pid}">
        <input type="hidden" id="code_${pid}" value="${(code)!''}">
        <input type="hidden" id="shell_code_${pid}" value="${(shellCode)!''}">
        <input type="hidden" name="batteryId">
        <input type="hidden" name="formatCellCount" id="format_cell_count_${pid}" value="${(formatCellCount)!''}">
        <input type="hidden" name="toInsertNominalCap">
        <input type="hidden" name="toInsertAcResistance">
        <input type="hidden" name="toInsertResilienceVol">
        <input type="hidden" name="toInsertStaticVol">
        <input type="hidden" name="toInsertCircle">
    <#--<input type="hidden" name="toInsertAppearance">-->
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_blue ok_clear">提交并清空</button>
    <button class="btn btn_red ok_close">提交并关闭</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    function checkNominalCap() {
        var win = $('#${pid}');
        var nominalCap = Number($('#check_nominal_cap_${pid}').val());
        var minNominalCap = Number($('#check_min_nominal_cap_${pid}').val());
        var maxNominalCap = Number($('#check_max_nominal_cap_${pid}').val());
        var checkValue = Number(win.find('input[name=toInsertNominalCap]').val());
        if (checkValue <= (nominalCap + maxNominalCap) && checkValue >= (nominalCap - minNominalCap) && checkValue != null && checkValue !== '') {
            $('#nominal_cap_result_${pid}').html('成功');
            $('#nominal_cap_result_${pid}').css("color", "green");
        } else {
            $('#nominal_cap_result_${pid}').html('失败');
            $('#nominal_cap_result_${pid}').css("color", "red");
        }
    }

    function checkAcResistance() {
        var win = $('#${pid}');
        var acResistance = Number($('#check_ac_resistance_${pid}').val());
        var minAcResistance = Number($('#check_min_ac_resistance_${pid}').val());
        var maxAcResistance = Number($('#check_max_ac_resistance_${pid}').val());
        var checkValue = Number(win.find('input[name=toInsertAcResistance]').val());
//        alert("checkValue=" + checkValue + "acResistance=" + acResistance + "min=" + minAcResistance + "max=" + maxAcResistance);
        if (checkValue <= (acResistance + maxAcResistance) && checkValue >= (acResistance - minAcResistance) && checkValue != null && checkValue !== '') {

            $('#ac_resistance_result_${pid}').html('成功');
            $('#ac_resistance_result_${pid}').css("color", "green");
        } else {
            $('#ac_resistance_result_${pid}').html('失败');
            $('#ac_resistance_result_${pid}').css("color", "red");
        }
    }

    function checkResilienceVol() {
        var win = $('#${pid}');
        var resilienceVol = Number($('#check_resilience_vol_${pid}').val());
        var minResilienceVol = Number($('#check_min_resilience_vol_${pid}').val());
        var maxResilienceVol = Number($('#check_max_resilience_vol_${pid}').val());
        var checkValue = Number(win.find('input[name=toInsertResilienceVol]').val());
//        alert("checkValue=" + checkValue + "resilienceVol=" + resilienceVol + "min=" + minResilienceVol + "max=" + maxResilienceVol);
        if (checkValue <= (resilienceVol + maxResilienceVol) && checkValue >= (resilienceVol - minResilienceVol) && checkValue != null && checkValue !== '') {
            $('#resilience_vol_result_${pid}').html('成功');
            $('#resilience_vol_result_${pid}').css("color", "green");
        } else {
            $('#resilience_vol_result_${pid}').html('失败');
            $('#resilience_vol_result_${pid}').css("color", "red");
        }
    }

    function checkStaticVol() {
        var win = $('#${pid}');
        var staticVol = Number($('#check_static_vol_${pid}').val());
        var minStaticVol = Number($('#check_min_static_vol_${pid}').val());
        var maxStaticVol = Number($('#check_max_static_vol_${pid}').val());
        var checkValue = Number(win.find('input[name=toInsertStaticVol]').val());
        if (checkValue <= (staticVol + maxStaticVol) && checkValue >= (staticVol - minStaticVol) && checkValue != null && checkValue !== '') {
            $('#static_vol_result_${pid}').html('成功');
            $('#static_vol_result_${pid}').css("color", "green");
        } else {
            $('#static_vol_result_${pid}').html('失败');
            $('#static_vol_result_${pid}').css("color", "red");
        }
    }

    function checkCircle() {
        var win = $('#${pid}');
        var circle = Number($('#check_circle_${pid}').val());
        var minCircle = Number($('#check_min_circle_${pid}').val());
        var maxCircle = Number($('#check_max_circle_${pid}').val());
        var checkValue = Number(win.find('input[name=toInsertCircle]').val());
        if (checkValue <= (circle + maxCircle) && checkValue >= (circle - minCircle) && checkValue != null && checkValue !== '') {
            $('#circle_result_${pid}').html('成功');
            $('#circle_result_${pid}').css("color", "green");
        } else {
            $('#circle_result_${pid}').html('失败');
            $('#circle_result_${pid}').css("color", "red");
        }
    }

    <#--function checkAppearance() {-->
    <#--var win = $('#${pid}');-->
    <#--var appearance = Number($('#check_appearance_${pid}').val());-->
    <#--var checkValue = Number(win.find('input[name=toInsertAppearance]').val());-->
    <#--if(appearance == checkValue) {-->
    <#--$('#appearance_result_${pid}').html('成功');-->
    <#--$('#appearance_result_${pid}').css("color", "green");-->
    <#--} else {-->
    <#--$('#appearance_result_${pid}').html('失败');-->
    <#--$('#appearance_result_${pid}').css("color", "red");-->
    <#--}-->
    <#--}-->

    function clearParam() {
        var win = $('#${pid}');
        $('#real_nominal_cap_${pid}').val('');
        $('#real_ac_resistance_${pid}').val('');
        $('#real_resilience_vol_${pid}').val('');
        $('#real_static_vol_${pid}').val('');
        $('#real_circle_${pid}').val('');
    <#--$('#real_appearance_${pid}').val('');-->
        $('#cell_id_${pid}').val('');
        $('#bind_cell_mfr_${pid}').html('');
        $('#bind_cell_model_${pid}').html('');
        $('#to_bind_cell_mfr_${pid}').val('');
        $('#to_bind_cell_model_${pid}').val('');
        $('#nominal_cap_result_${pid}').html('');
        $('#ac_resistance_result_${pid}').html('');
        $('#resilience_vol_result_${pid}').html('');
        $('#static_vol_result_${pid}').html('');
        $('#circle_result_${pid}').html('');
    <#--$('#appearance_result_${pid}').html('');-->
        win.find('input[name=toInsertNominalCap]').val('');
        win.find('input[name=toInsertAcResistance]').val('');
        win.find('input[name=toInsertResilienceVol]').val('');
        win.find('input[name=toInsertStaticVol]').val('');
        win.find('input[name=toInsertCircle]').val('');
//        win.find('input[name=toInsertAppearance]').val('');
    }

    function bindCell() {
        var code = $('#code_${pid}').val();
        var shellCode = $('#shell_code_${pid}').val();
    <#--var appearance = ${appearance};-->
        if ($('#battery_id_${pid}').val() == 'undefined' || $('#battery_id_${pid}').val() == '' ) {
            //创建一个电池
            $.ajax({
                type: 'POST',
                url: '${contextPath}/security/hdg/battery_check/create.htm',
                dataType: 'json',
                data: {
                    code: code,
                    shellCode: shellCode,
                    appearance: 1
                },
                success: function (json3) {
                    if (json3.success) {
                        var data3 = json3.data;
                        var newBatteryId = data3.id;
                        if (newBatteryId == null || newBatteryId == '') {
                            $.messager.alert('提示信息', '电池生成失败，无法绑定', 'info');
                            return false;
                        } else {

                            $.ajax({
                                type: 'POST',
                                url: '${contextPath}/security/hdg/battery_cell/bind_battery.htm',
                                dataType: 'json',
                                data: {
                                    id: $('#cell_id_${pid}').val(),
                                    batteryId: newBatteryId,
                                    formatId: $('#check_format_id_${pid}').val()
                                },
                                success: function (json4) {
                                    if (json4.success) {
//                                        $.messager.alert('提示信息', '绑定成功', 'info');
                                        $.messager.show({
                                            title:'提示信息',
                                            msg:'绑定成功',
                                            timeout:500,
                                            showType:'slide',
                                            style:{
                                                height: 100
                                            }
                                        });
                                        var type = $('#submit_type_${pid}').val();
                                        if (type == 1) {
                                            $('#barcode_${pid}').val('');
                                            $('#battery_id_${pid}').val(newBatteryId);
                                            var win = $('#${pid}');
                                            var windowData = win.data('windowData');
                                            windowData.ok(json4.data.id);
                                            clearParam();
                                            var oInput = document.getElementById("barcode_${pid}");
                                            oInput.focus();
                                        }
                                        if (type == 2) {
                                            var win = $('#${pid}');
                                            var windowData = win.data('windowData');
                                            windowData.ok(json4.data.id);
                                            var win = $('#${pid}');
                                            win.window('close');
                                        }
                                    } else {
                                        $.messager.alert('提示信息', json4.message, 'info');
                                    }
                                }
                            })
                        }

                    } else {
                        $.messager.alert('提示信息', json3.message, 'info');
                    }
                }
            });

        } else {
            $.ajax({
                type: 'POST',
                url: '${contextPath}/security/hdg/battery_cell/bind_battery.htm',
                dataType: 'json',
                data: {
                    id: $('#cell_id_${pid}').val(),
                    batteryId: $('#battery_id_${pid}').val(),
                    formatId: $('#check_format_id_${pid}').val()
                },
                success: function (json) {
                    if (json.success) {
//                        $.messager.alert('提示信息', '绑定成功', 'info');
                        $.messager.show({
                            title:'提示信息',
                            msg:'绑定成功',
                            timeout:500,
                            showType:'slide',
                            style:{
                                height: 100
                            }
                        });
                        var type = $('#submit_type_${pid}').val();
                        if (type == 1) {
                            $('#barcode_${pid}').val('');
                            clearParam();
                        }
                        if (type == 2) {
                            var win = $('#${pid}');
                            win.window('close');
                        }
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            })
        }
    }

    window.onload = function(){
        var oInput = document.getElementById("barcode_${pid}");
        oInput.focus();
    }

    window.setTimeout( function(){ document.getElementById('barcode_${pid}').focus(); }, 0);

    (function () {
        var pid = '${pid}',
                win = $('#' + pid), windowData = win.data('windowData'),
                form = win.find('form');

        //直接加载电池规格信息
        $.ajax({
            type: 'POST',
            url: '${contextPath}/security/hdg/battery_format/find_format.htm',
            dataType: 'json',
            data: {
                id: $('#check_format_id_${pid}').val()
            },
            success: function (json) {
                if (json.success) {

                } else {
                    $.messager.alert('提示信息', json.message, 'info');
                    return;
                }

                var data = json.data;
                //获取串数
                var formatCellCount = $('#format_cell_count_${pid}').val();
                $('#nominal_cap_${pid}').html(data.nominalCap);
                $('#check_nominal_cap_${pid}').val(data.nominalCap);
                if (data.minNominalCap != null) {
                    var nominalCapHtml1 = "-" + data.minNominalCap;
                } else {
                    var nominalCapHtml1 = "-" + 0;
                }
                if (data.maxNominalCap != null) {
                    var nominalCapHtml2 = "+" + data.maxNominalCap;
                } else {
                    var nominalCapHtml2 = "+" + 0;
                }
                $('#nominal_cap_range_${pid}').html(nominalCapHtml1 + " , " + nominalCapHtml2);
                $('#check_min_nominal_cap_${pid}').val(data.minNominalCap);
                $('#check_max_nominal_cap_${pid}').val(data.maxNominalCap);


                //在页面上显示标准有相乘
//                new Number().toFixed(3);
                $('#ac_resistance_${pid}').html(data.acResistance + "  *  " + formatCellCount + "  =  " + new Number(data.acResistance * formatCellCount).toFixed(3) );
                //把相乘后的数值存起来
                $('#check_ac_resistance_${pid}').val(data.acResistance * formatCellCount);
                if (data.minAcResistance != null) {
                    //偏差也会相乘
                    var acResistanceHtml1 = "-" + new Number(data.minAcResistance * formatCellCount).toFixed(3);
                } else {
                    var acResistanceHtml1 = "-" + 0;
                }
                if (data.maxAcResistance != null) {
                    var acResistanceHtml2 = "+" + new Number(data.maxAcResistance * formatCellCount).toFixed(3);
                } else {
                    var acResistanceHtml2 = "+" + 0;
                }
                //显示相乘后的偏差
                $('#ac_resistance_range_${pid}').html(acResistanceHtml1 + " , " + acResistanceHtml2);
                //把相乘后的变差存起来
                $('#check_min_ac_resistance_${pid}').val(data.minAcResistance  * formatCellCount);
                $('#check_max_ac_resistance_${pid}').val(data.maxAcResistance  * formatCellCount);

//                new Number(data.resilienceVol * formatCellCount).toFixed(3);
                $('#resilience_vol_${pid}').html(data.resilienceVol + "  *  " + formatCellCount + "  =  " + new Number(data.resilienceVol * formatCellCount).toFixed(3));
                $('#check_resilience_vol_${pid}').val(data.resilienceVol * formatCellCount);
                if (data.minResilienceVol != null) {
                    var resilienceVolHtml1 = "-" + data.minResilienceVol * formatCellCount;
                } else {
                    var resilienceVolHtml1 = "-" + 0;
                }
                if (data.maxResilienceVol != null) {
                    var resilienceVolHtml2 = "+" + data.maxResilienceVol * formatCellCount;
                } else {
                    var resilienceVolHtml2 = "+" + 0;
                }
                $('#resilience_vol_range_${pid}').html(resilienceVolHtml1 + " , " + resilienceVolHtml2);
                $('#check_min_resilience_vol_${pid}').val(data.minResilienceVol * formatCellCount);
                $('#check_max_resilience_vol_${pid}').val(data.maxResilienceVol * formatCellCount);

//                new Number(data.staticVol * formatCellCount).toFixed(3);
                $('#static_vol_${pid}').html(data.staticVol + "  *  " + formatCellCount + "  =  " + new Number(data.staticVol * formatCellCount).toFixed(3));
                $('#check_static_vol_${pid}').val(data.staticVol * formatCellCount);
                if (data.minStaticVol != null) {
                    var staticVolHtml1 = "-" + data.minStaticVol * formatCellCount;
                } else {
                    var staticVolHtml1 = "-" + 0;
                }
                if (data.maxStaticVol != null) {
                    var staticVolHtml2 = "+" + data.maxStaticVol * formatCellCount;
                } else {
                    var staticVolHtml2 = "+" + 0;
                }
                $('#static_vol_range_${pid}').html(staticVolHtml1 + " , " + staticVolHtml2);
                $('#check_min_static_vol_${pid}').val(data.minStaticVol * formatCellCount);
                $('#check_max_static_vol_${pid}').val(data.maxStaticVol * formatCellCount);

                $('#circle_${pid}').html(data.circle);
                $('#check_circle_${pid}').val(data.circle);
                if (data.minCircle != null) {
                    var circleHtml1 = "-" + data.minCircle;
                } else {
                    var circleHtml1 = "-" + 0;
                }
                if (data.maxCircle != null) {
                    var circleHtml2 = "+" + data.maxCircle;
                } else {
                    var circleHtml2 = "+" + 0;
                }
                $('#circle_range_${pid}').html(circleHtml1 + " , " + circleHtml2);
                $('#check_min_circle_${pid}').val(data.minCircle);
                $('#check_max_circle_${pid}').val(data.maxCircle);
            <#--<#if appearance==1>-->
            <#--$('#appearance_${pid}').html("R");-->
            <#--<#elseif appearance==2>-->
            <#--$('#appearance_${pid}').html("M");-->
            <#--<#elseif appearance==3>-->
            <#--$('#appearance_${pid}').html("Y");-->
            <#--</#if>-->
            <#--$('#check_appearance_${pid}').val(${(appearance)!0});-->
            <#--if(data.appearance==1) {-->
            <#--$('#appearance_${pid}').html("R");-->
            <#--}else if(data.appearance==2) {-->
            <#--$('#appearance_${pid}').html("M");-->
            <#--}else if(data.appearance==3) {-->
            <#--$('#appearance_${pid}').html("Y");-->
            <#--}-->
            <#--$('#check_appearance_${pid}').val(data.appearance);-->
            <#--$('#appearance_range_${pid}').html("无");-->
            }
        });

        $('#barcode_${pid}').bind('keyup', function (event) {
            if (event.keyCode == "13") {
                var barcode = $('#barcode_${pid}').val();
                //显示电芯信息
                showBatteryCell(barcode);
            }
        });

        $('#barcode_${pid}').change(function () {
            clearParam();
        });

        function showBatteryCell(barcode) {
            $.ajax({
                type: 'POST',
                url: '${contextPath}/security/hdg/battery_cell_barcode/find_barcode.htm',
                dataType: 'json',
                data: {
                    barcode: barcode
                },
                success: function (json5) {
                    if (json5.success) {
                        $.ajax({
                            type: 'POST',
                            url: '${contextPath}/security/hdg/battery_cell/find_cell.htm',
                            dataType: 'json',
                            data: {
                                barcode: barcode
                            },
                            success: function (json2) {
                                if (json2.success) {
                                    var data2 = json2.data;
                                    $('#bind_cell_mfr_${pid}').html(data2.cellMfr);
                                    $('#to_bind_cell_mfr_${pid}').val(data2.cellMfr);
                                    $('#bind_cell_model_${pid}').html(data2.cellModel);
                                    $('#to_bind_cell_model_${pid}').val(data2.cellModel);
                                    //先检验电芯厂家和电芯型号
                                <#--if ($('#to_bind_cell_mfr_${pid}').val() === $('#battery_cell_mfr_${pid}').val() && $('#to_bind_cell_model_${pid}').val() === $('#battery_cell_model_${pid}').val()) {-->

                                <#--} else {-->
                                    if (trim($('#to_bind_cell_model_${pid}').val()) === trim($('#battery_cell_model_${pid}').val())) {

                                    } else {
                                        $.messager.alert('提示信息', '电芯型号不符合，无法绑定', 'info');
                                        return false;
                                    }
                                    var formatCellCount = $('#format_cell_count_${pid}').val();
                                    $('#real_nominal_cap_${pid}').val(data2.nominalCap / 1000);
                                    win.find('input[name=toInsertNominalCap]').val(Number(data2.nominalCap / 1000));
//                                    new Number(data2.acResistance * formatCellCount).toFixed(3);
                                    $('#real_ac_resistance_${pid}').val(data2.acResistance + "  *  " + formatCellCount + "  =  " + new Number(data2.acResistance * formatCellCount).toFixed(3));
                                    win.find('input[name=toInsertAcResistance]').val(Number(data2.acResistance * formatCellCount));
                                    $('#real_resilience_vol_${pid}').val(data2.resilienceVol / 1000 + "  *  " + formatCellCount + "  =  " + new Number(data2.resilienceVol / 1000 * formatCellCount).toFixed(3));
                                    win.find('input[name=toInsertResilienceVol]').val(Number(data2.resilienceVol / 1000 * formatCellCount));
                                    $('#real_static_vol_${pid}').val(data2.staticVol / 1000 + "  *  " + formatCellCount + "  =  " + new Number(data2.staticVol / 1000 * formatCellCount).toFixed(3));
                                    win.find('input[name=toInsertStaticVol]').val(Number(data2.staticVol / 1000 * formatCellCount));
                                    $('#real_circle_${pid}').val(data2.circle);
                                    win.find('input[name=toInsertCircle]').val(Number(data2.circle));
//                                    var realAppearance = '';
//                                    if(Number(data2.appearance)==1) {
//                                        realAppearance = "R";
//                                    }else if(Number(data2.appearance)==2) {
//                                        realAppearance = "M";
//                                    }else if(Number(data2.appearance)==3) {
//                                        realAppearance = "Y";
//                                    }
                                <#--$('#real_appearance_${pid}').val(realAppearance);-->
//                                    win.find('input[name=toInsertAppearance]').val(Number(data2.appearance));
                                    $('#cell_id_${pid}').val(data2.id);
                                    //检验项目值
                                    checkItems();
                                    var nominalCapResult = $('#nominal_cap_result_${pid}').html();
                                    var acResistanceResult = $('#ac_resistance_result_${pid}').html();
                                    var resilienceVolResult = $('#resilience_vol_result_${pid}').html();
                                    var staticVolResult = $('#static_vol_result_${pid}').html();
                                    var circleResult = $('#circle_result_${pid}').html();
                                <#--var appearanceReulst = $('#appearance_result_${pid}').html();-->
                                    //如果检测项目都通过了，直接提交
                                <#--if ($('#to_bind_cell_mfr_${pid}').val() === $('#battery_cell_mfr_${pid}').val() && $('#to_bind_cell_model_${pid}').val() === $('#battery_cell_model_${pid}').val()) {-->
                                <#--if (nominalCapResult != "成功" || acResistanceResult != "成功" || resilienceVolResult != "成功" || staticVolResult != "成功" || circleResult != "成功") {-->
                                <#--&lt;#&ndash;if ($('#to_bind_cell_mfr_${pid}').val() === $('#battery_cell_mfr_${pid}').val() && $('#to_bind_cell_model_${pid}').val() === $('#battery_cell_model_${pid}').val()) {&ndash;&gt;-->
                                <#--&lt;#&ndash;if (nominalCapResult != "成功" || acResistanceResult != "成功" || resilienceVolResult != "成功" || staticVolResult != "成功" || circleResult != "成功" || appearanceReulst != "成功") {&ndash;&gt;-->
                                <#--}else{-->
                                    if ($('#to_bind_cell_model_${pid}').val() === $('#battery_cell_model_${pid}').val()) {
                                        if (nominalCapResult != "成功" || acResistanceResult != "成功" || resilienceVolResult != "成功" || staticVolResult != "成功" || circleResult != "成功") {
                                        <#--if ($('#to_bind_cell_mfr_${pid}').val() === $('#battery_cell_mfr_${pid}').val() && $('#to_bind_cell_model_${pid}').val() === $('#battery_cell_model_${pid}').val()) {-->
                                        <#--if (nominalCapResult != "成功" || acResistanceResult != "成功" || resilienceVolResult != "成功" || staticVolResult != "成功" || circleResult != "成功" || appearanceReulst != "成功") {-->
                                        }else{
                                            if ($('#real_nominal_cap_${pid}').val() == null || $('#real_ac_resistance_${pid}').val() == null || $('#real_resilience_vol_${pid}').val() == null || $('#real_static_vol_${pid}').val() == null || $('#real_circle_${pid}').val() == null ||
                                                    $('#real_nominal_cap_${pid}').val() === '' || $('#real_ac_resistance_${pid}').val() === '' || $('#real_resilience_vol_${pid}').val() === '' || $('#real_static_vol_${pid}').val() === '' || $('#real_circle_${pid}').val() === '') {
                                            <#--if ($('#real_nominal_cap_${pid}').val() == null || $('#real_ac_resistance_${pid}').val() == null || $('#real_resilience_vol_${pid}').val() == null || $('#real_static_vol_${pid}').val() == null || $('#real_circle_${pid}').val() == null || $('#real_appearance_${pid}').val() == null ||-->
                                            <#--$('#real_nominal_cap_${pid}').val() === '' || $('#real_ac_resistance_${pid}').val() === '' || $('#real_resilience_vol_${pid}').val() === '' || $('#real_static_vol_${pid}').val() === '' || $('#real_circle_${pid}').val() === '' || $('#real_appearance_${pid}').val() === '') {-->
                                            }else{
                                                //提交并清空
                                                $('#submit_type_${pid}').val(1);
                                                bindCell();
                                            }
                                        }
                                    }
                                } else {
                                    $.messager.alert('提示信息', json2.message, 'info');
                                    $('#to_bind_cell_mfr_${pid}').val('');
                                    $('#bind_cell_mfr_${pid}').html('');
                                    $('#to_bind_cell_model_${pid}').val('');
                                    $('#bind_cell_model_${pid}').html('');
                                    $('#barcode_${pid}').val('');
                                    return false;
                                }
                            }
                        });
                    } else {
                        $.messager.alert('提示信息', json5.message, 'info');
                        $('#to_bind_cell_mfr_${pid}').val('');
                        $('#bind_cell_mfr_${pid}').html('');
                        $('#to_bind_cell_model_${pid}').val('');
                        $('#bind_cell_model_${pid}').html('');
                        $('#barcode_${pid}').val('');
                        return false;
                    }
                }
            })
        }

        function checkItems() {
            checkNominalCap();
            checkAcResistance();
            checkResilienceVol();
            checkStaticVol();
            checkCircle();
//            checkAppearance();
        }

        function submitData() {
            var nominalCapResult = $('#nominal_cap_result_${pid}').html();
            var acResistanceResult = $('#ac_resistance_result_${pid}').html();
            var resilienceVolResult = $('#resilience_vol_result_${pid}').html();
            var staticVolResult = $('#static_vol_result_${pid}').html();
            var circleResult = $('#circle_result_${pid}').html();
        <#--var appearanceReulst = $('#appearance_result_${pid}').html();-->
            if ($('#barcode_${pid}').val() == null || $('#barcode_${pid}').val() == '') {
                $.messager.alert('提示信息', '请输入电芯条码，并按一下回车键', 'info');
                return false;
            }
            if ($('#to_bind_cell_mfr_${pid}').val() == null || $('#to_bind_cell_mfr_${pid}').val() == '' || $('#to_bind_cell_model_${pid}').val() == null || $('#to_bind_cell_model_${pid}').val() == '') {
                $.messager.alert('提示信息', '请在电芯条码输入框按一下回车键', 'info');
                return false;
            }
        <#--if ($('#to_bind_cell_mfr_${pid}').val() === $('#battery_cell_mfr_${pid}').val() && $('#to_bind_cell_model_${pid}').val() === $('#battery_cell_model_${pid}').val()) {-->
        <#--} else {-->
            if ($(trim('#to_bind_cell_model_${pid}').val()) === trim($('#battery_cell_model_${pid}').val())) {
            } else {
                $.messager.alert('提示信息', '电芯型号不符合，无法绑定', 'info');
                return false;
            }
            if (nominalCapResult != "成功" || acResistanceResult != "成功" || resilienceVolResult != "成功" || staticVolResult != "成功" || circleResult != "成功") {
                $.messager.alert('提示信息', '检验项目有失败项，无法通过', 'info');
                return false;
            }
//            if (nominalCapResult != "成功" || acResistanceResult != "成功" || resilienceVolResult != "成功" || staticVolResult != "成功" || circleResult != "成功" || appearanceReulst != "成功") {
//                $.messager.alert('提示信息', '检验项目有失败项，无法通过', 'info');
//                return false;
//            }
        <#--if ($('#real_nominal_cap_${pid}').val() == null || $('#real_ac_resistance_${pid}').val() == null || $('#real_resilience_vol_${pid}').val() == null || $('#real_static_vol_${pid}').val() == null || $('#real_circle_${pid}').val() == null || $('#real_appearance_${pid}').val() == null ||-->
        <#--$('#real_nominal_cap_${pid}').val() === '' || $('#real_ac_resistance_${pid}').val() === '' || $('#real_resilience_vol_${pid}').val() === '' || $('#real_static_vol_${pid}').val() === '' || $('#real_circle_${pid}').val() === '' || $('#real_appearance_${pid}').val() === '') {-->
        <#--$.messager.alert('提示信息', '检验项目数据不完整，无法通过', 'info');-->
        <#--return false;-->
        <#--}-->
            if ($('#real_nominal_cap_${pid}').val() == null || $('#real_ac_resistance_${pid}').val() == null || $('#real_resilience_vol_${pid}').val() == null || $('#real_static_vol_${pid}').val() == null || $('#real_circle_${pid}').val() == null ||
                    $('#real_nominal_cap_${pid}').val() === '' || $('#real_ac_resistance_${pid}').val() === '' || $('#real_resilience_vol_${pid}').val() === '' || $('#real_static_vol_${pid}').val() === '' || $('#real_circle_${pid}').val() === '') {
                $.messager.alert('提示信息', '检验项目数据不完整，无法通过', 'info');
                return false;
            }
            bindCell();
        }

        win.find('button.ok_close').click(function () {
            $('#submit_type_${pid}').val(2);
            submitData();
        });

        win.find('button.ok_clear').click(function () {
            $('#submit_type_${pid}').val(1);
            submitData();
        })

        win.find('button.close').click(function () {
            win.window('close');
        });

        function trim(str){
            return str.replace(/(^\s*)|(\s*$)/g, "");
        }
    })()
</script>