<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" name="cellModelId" value="${(entity.cellModelId)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="90" align="right">电芯厂家：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" maxlength="40" name="cellMfr" value="${(entity.cellMfr)!''}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">电芯型号：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" style="width: 190px;"  maxlength="40" name="cellModel" value="${(entity.cellModel)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">规格名称：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox"  name="batteryFormatName" value="${(entity.batteryFormatName)!''}" required="true"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">条码规则：</td>
                    <td colspan="3">
                        <input type="text" class="text easyui-validatebox" style="width: 190px;" placeholder="两位大写字母组成" value="${(entity.barcodeRule)!''}"  maxlength="2" name="barcodeRule" required="true" readonly/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">组包容量：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" name="nominalCap" value="${(entity.nominalCap)!''}" required="true" />Ah&nbsp;
                    </td>
                    <td width="90" align="right">标称范围：</td>
                    <td>
                        <select disabled="disabled" style="width: 33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10" name="minNominalCap" value="${(entity.minNominalCap)!''}" />
                        <select disabled="disabled" style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10" name="maxNominalCap" value="${(entity.maxNominalCap)!''}" />
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">交流内阻：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10"  name="acResistance" value="${(entity.acResistance)!''}" required="true" />mΩ
                    </td>
                    <td width="90" align="right">交流内阻范围：</td>
                    <td>
                        <select disabled="disabled" style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10"  name="minAcResistance" value="${(entity.minAcResistance)!''}" />
                        <select disabled="disabled" style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10"  name="maxAcResistance" value="${(entity.maxAcResistance)!''}" />
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">回弹电压：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10"  name="resilienceVol"  value="${(entity.resilienceVol)!''}" required="true" />V&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">回弹电压范围：</td>
                    <td>
                        <select disabled="disabled" style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10"  name="minResilienceVol" value="${(entity.minResilienceVol)!''}" />
                        <select disabled="disabled" style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10"  name="maxResilienceVol" value="${(entity.maxResilienceVol)!''}" />
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">静置电压：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" name="staticVol" value="${(entity.staticVol)!''}" required="true" />V&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">静置电压范围：</td>
                    <td>
                        <select disabled="disabled" style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10" name="minStaticVol" value="${(entity.minStaticVol)!''}" />
                        <select disabled="disabled" style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10" name="maxStaticVol" value="${(entity.maxStaticVol)!''}" />
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">循环次数：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" style="width:184px;height: 28px;" maxlength="10" name="circle" value="${(entity.circle)!''}" required="true" />次&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">循环次数范围：</td>
                    <td>
                        <select disabled="disabled" style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" maxlength="10"  name="minCircle"  value="${(entity.minCircle)!''}" />
                        <select disabled="disabled" style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" maxlength="10" name="maxCircle" value="${(entity.maxCircle)!''}" />
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">电芯串数：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" style="width:184px;height: 28px;" maxlength="10" name="cellCount" value="${(entity.cellCount)!''}" required="true" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">标称容量：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3"
                               style="width:184px;height: 28px;" maxlength="10" name="nominalPow"
                               value="${(entity.nominalPow)!''}" required="true"/>Ah&nbsp;
                    </td>
                </tr>
            </table>
            <input type="hidden" id="battery_format_id_${pid}">
            <input type="hidden" id="battery_regular_type_${pid}" value="${(regular.regularType)!''}">
            <input type="hidden" id="battery_regular_name_${pid}" value="${(regular.regularName)!''}">
            <input type="hidden" id="battery_reset_type_${pid}" value="${(regular.resetType)!''}">
        </form>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('input[name=cellMfr],input[name=cellModel]').click(function() {
            selectCellModel();
        });

        win.find('input[name=barcodeRule]').click(function() {
            var batteryFormatId = win.find('input[name=id]').val();
            editBarcodeRule(batteryFormatId);
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

        function editBarcodeRule(batteryFormatId) {
            App.dialog.show({
                css: 'width:400px;height:420px;overflow:visible;',
                title: '条码规则',
                href: "${contextPath}/security/hdg/battery_format/edit_barcode_rule.htm?batteryFormatId=" + batteryFormatId + "&regularType=" + 2 + "",
                windowData: {
                    ok: function(config) {
                        $("#battery_regular_type_${pid}").val(config.regularType);
                        win.find('input[name=barcodeRule]').val(config.regular);
                        $('#battery_regular_name_${pid}').val(config.regularName);
                        $('#battery_reset_type_${pid}').val(config.resetType);
                    }
                },
                event: {
                    onClose: function() {
                    }
                }
            });
        }

        win.find('button.ok').click(function() {
            $.ajax({
                type:'POST',
                url:'${contextPath}/security/hdg/battery_cell_regular/check_param.htm',
                dataType:'json',
                data:{
                    batteryFormatId: win.find('input[name=id]').val(),
                    regularType: 2,
                    regular: win.find('input[name=barcodeRule]').val()
                },
                success:function(json1){
                    if(json1.success) {
                        form.form('submit', {
                            url: '${contextPath}/security/hdg/battery_format/update.htm',
                            success: function(text) {
                                var json = $.evalJSON(text);
                            <@app.json_jump/>
                                if(json.success) {
                                    $.ajax({
                                        type:'POST',
                                        url:'${contextPath}/security/hdg/battery_cell_regular/update.htm',
                                        dataType:'json',
                                        data: {
                                            batteryFormatId:win.find('input[name=id]').val(),
                                            regularType: 2,
                                            regular: win.find('input[name=barcodeRule]').val(),
                                            regularName: $('#battery_regular_name_${pid}').val(),
                                            resetType: $('#battery_reset_type_${pid}').val()
                                        },
                                        success:function (json2) {
                                            if(json2.success){
                                                $.messager.alert('提示信息', '操作成功', 'info');
                                                win.window('close');
                                            }else {
                                                $.messager.alert('提示信息', json.message, 'info');
                                            }
                                        }
                                    });
                                } else {
                                    $.messager.alert('提示信息', json.message, 'info');
                                }
                            }
                        });
                    }else {
                        $.messager.alert('提示信息', json1.message, 'info');
                    }
                }
            });
        });
        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>
