<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="cellModelId">
            <table cellpadding="0" cellspacing="0">
                <tr>
                <#--<span style="color: red">*&nbsp;</span>-->
                    <td width="90" align="right">电芯厂家：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" maxlength="40" name="cellMfr" required="true" readonly/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">电芯型号：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" maxlength="40" name="cellModel" required="true" readonly/>&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">规格名称：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" maxlength="40" name="cellFormatName" required="true"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">条码规则：</td>
                    <td colspan="3">
                        <input type="text" class="text easyui-validatebox" maxlength="40" name="barcodeRule" required="true" readonly/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">充电截至电压：</td>
                    <td>
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" name="chgCutVol" required="true"/>V
                    </td>
                    <td width="90" align="right">标称电压：</td>
                    <td>
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10"  name="nominalVol" required="true"/>V
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">充电温度：</td>
                    <td>
                        <input type="text" style="width: 75px;height: 28px;" class="easyui-numberbox" data-options="precision:3"  maxlength="10"  name="minChgTemp" required="true" />℃
                        -
                        <input type="text" style="width: 75px;height: 28px;" class="easyui-numberbox" data-options="precision:3"  maxlength="10"  name="maxChgTemp" required="true" />℃
                    </td>
                    <td width="90" align="right">放电温度：</td>
                    <td>
                        <input type="text" style="width: 75px;height: 28px;" class="easyui-numberbox" data-options="precision:3"  maxlength="10"  name="minDsgTemp" required="true"/>℃
                        -
                        <input type="text" style="width: 75px;height: 28px;" class="easyui-numberbox" data-options="precision:3"  maxlength="10"  name="maxDsgTemp" required="true"/>℃
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">组包容量：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" name="nominalCap" required="true" />Ah&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">标称范围：</td>
                    <td>
                        <select style="width: 33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10"  name="minNominalCap" />
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10"   name="maxNominalCap" />
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">交流内阻：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" name="acResistance" required="true"/>mΩ&nbsp;
                    </td>
                    <td width="90" align="right">交流内阻范围：</td>
                    <td>
                        <select style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10"  name="minAcResistance"/>
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10"  name="maxAcResistance"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">回弹电压：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10"  name="resilienceVol" required="true"/>V&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">回弹电压范围：</td>
                    <td>
                        <select style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10"  name="minResilienceVol" />
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10"  name="maxResilienceVol" />
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">静置电压：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10"  name="staticVol" required="true"/>V&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">静置电压范围：</td>
                    <td>
                        <select style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10"  name="minStaticVol" />
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10"  name="maxStaticVol" />
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">循环次数：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" style="width:184px;height: 28px;" maxlength="10"  name="circle" required="true"/>次&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">循环次数范围：</td>
                    <td>
                        <select style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" maxlength="10"  name="minCircle" />
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" maxlength="10"  name="maxCircle" />
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">充电电流倍率：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10"  name="chgRate" required="true" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">充电电流：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10"  name="chgCurrent" required="true" />A
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">充电时间：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10"  name="chgTime" required="true"/>小时&nbsp;
                    </td>
                    <td width="90" align="right">放电截至电压：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10"  name="dsgCutVol" required="true"/>V
                    </td>
                </tr>
                <tr>
                    <td width="120" align="right">最大持续充电电流：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10"  name="maxContinueChgCurrent" required="true"/>A&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="120" align="right">最大持续放电电流：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10"  name="maxContinueDsgCurrent" required="true" />A
                    </td>
                </tr>
            </table>
            <input type="hidden" id="cell_format_id_${pid}">
            <input type="hidden" id="cell_regular_type_${pid}">
            <input type="hidden" id="cell_regular_name_${pid}">
            <input type="hidden" id="cell_reset_type_${pid}">
        </form>
    </div>
</div>
<div class="popup_btn"  style="margin-right: 5px;">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>


    (function() {

        var pid = '${pid}', win = $('#' + pid), form = win.find('form');

        $("select").each(function () {
            $(this).attr("disabled","disabled");
        });

        win.find('input[name=cellMfr],input[name=cellModel]').click(function() {
            selectCellModel();
        });

        win.find('input[name=barcodeRule]').click(function() {
            addBarcodeRule();
        });

        function selectCellModel() {
            App.dialog.show({
                css: 'width:826px;height:522px;overflow:visible;',
                title: '选择电芯型号',
                href: "${contextPath}/security/hdg/battery_cell_model/select_battery_cell_model.htm",
                windowData: {
                    ok: function(config) {
                        win.find('input[name=cellModelId]').val(config.cellModel.id);
                        win.find('input[name=cellMfr]').val(config.cellModel.cellMfr);
                        win.find('input[name=cellModel]').val(config.cellModel.cellModel);
                    }
                },
                event: {
                    onClose: function() {
                    }
                }
            });
        }

        function addBarcodeRule() {
            App.dialog.show({
                css: 'width:400px;height:420px;overflow:visible;',
                title: '条码规则',
                href: "${contextPath}/security/hdg/battery_cell_format/add_barcode_rule.htm",
                windowData: {
                    ok: function(config) {
                        $("#cell_regular_type_${pid}").val(config.regularType);
                        win.find('input[name=barcodeRule]').val(config.regular);
                        $('#cell_regular_name_${pid}').val(config.regularName);
                        $('#cell_reset_type_${pid}').val(config.resetType);
                    }
                },
                event: {
                    onClose: function() {
                    }
                }
            });
        }


        win.find('button.ok').click(function () {
            var param1 = win.find('input[name=barcodeRule]').val();
            var param2 = $('#cell_regular_name_${pid}').val();
            var param3 = $('#cell_reset_type_${pid}').val();
            if (param1 == null || param1 == '' || param2 == null || param2 == '' || param3 == null || param3 == '') {
                $.messager.alert('提示信息', '请输入完整的条码规则信息', 'info');
                return false;
            } else {
                $.ajax({
                    type: 'POST',
                    url: '${contextPath}/security/hdg/battery_cell_regular/check_param.htm',
                    dataType: 'json',
                    data: {
                        cellFormatId: win.find('input[name=id]').val(),
                        regularType: 1,
                        regular: win.find('input[name=barcodeRule]').val()
                    },
                    success: function (json1) {
                        if (json1.success) {
                            form.form('submit', {
                                url: '${contextPath}/security/hdg/battery_cell_format/create.htm',
                                success: function (text) {
                                    var json = $.evalJSON(text);
                                <@app.json_jump/>
                                    if (json.success) {
                                        var data = json.data;
                                        $.ajax({
                                            type: 'POST',
                                            url: '${contextPath}/security/hdg/battery_cell_regular/create.htm',
                                            dataType: 'json',
                                            data: {
                                                cellFormatId: data,
                                                regularType: 1,
                                                regular: win.find('input[name=barcodeRule]').val(),
                                                regularName: $('#cell_regular_name_${pid}').val(),
                                                resetType: $('#cell_reset_type_${pid}').val()
                                            },
                                            success: function (json2) {
                                                if (json2.success) {
                                                    $.messager.alert('提示信息', '操作成功', 'info');
                                                    win.window('close');
                                                } else {
                                                    $.messager.alert('提示信息', json.message, 'info');
                                                }
                                            }
                                        });
                                    } else {
                                        $.messager.alert('提示信息', json.message, 'info');
                                    }
                                }
                            });
                        } else {
                            $.messager.alert('提示信息', json1.message, 'info');
                        }
                    }
                });
            }
        });
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>
