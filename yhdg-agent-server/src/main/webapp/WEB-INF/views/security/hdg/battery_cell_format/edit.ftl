<div class="popup_body">
    <div class="ui_table">
        <form>
            <input type="hidden" name="id" value="${(entity.id)!''}">
            <input type="hidden" name="cellModelId" value="${(entity.cellModelId)!''}">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td width="90" align="right">电芯厂家：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" maxlength="40" name="cellMfr" value="${(entity.cellMfr)!''}" required="true" readonly/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">电芯型号：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" maxlength="40" name="cellModel" value="${(entity.cellModel)!''}" required="true" readonly/>&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">规格名称：</td>
                    <td align="right">
                        <input type="text" class="text easyui-validatebox" maxlength="40" name="cellFormatName" value="${(entity.cellFormatName)!''}" required="true"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">条码规则：</td>
                    <td colspan="3">
                        <input type="text" class="text easyui-validatebox" value="${(entity.barcodeRule)!''}" name="barcodeRule" required="true" readonly/>&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">充电截至电压：</td>
                    <td>
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" name="chgCutVol" value="${(entity.chgCutVol)!''}" required="true"/>V
                    </td>
                    <td width="90" align="right">标称电压：</td>
                    <td>
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" name="nominalVol" value="${(entity.nominalVol)!''}" required="true"/>V
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">充电温度：</td>
                    <td>

                        <input type="text" style="width: 75px;height: 28px;" class="easyui-numberbox" data-options="precision:3"  maxlength="10" name="minChgTemp" value="${(entity.minChgTemp)!''}" required="true"/>℃
                        -
                        <input type="text" style="width: 75px;height: 28px;" class="easyui-numberbox" data-options="precision:3"  maxlength="10" name="maxChgTemp" value="${(entity.maxChgTemp)!''}" required="true"/>℃
                    </td>
                    <td width="90" align="right">放电温度：</td>
                    <td>
                        <input type="text" style="width: 75px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10"  name="minDsgTemp" value="${(entity.minDsgTemp)!''}" required="true"/>℃
                        -
                        <input type="text" style="width: 75px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10"  name="maxDsgTemp" value="${(entity.maxDsgTemp)!''}" required="true"/>℃
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">组包容量：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" name="nominalCap" value="${(entity.nominalCap)!''}" required="true"/>Ah&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">标称范围：</td>
                    <td>
                        <select style="width: 33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10" name="minNominalCap" value="${(entity.minNominalCap)!''}"/>
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10" name="maxNominalCap" value="${(entity.maxNominalCap)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">交流内阻：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" name="acResistance" value="${(entity.acResistance)!''}" required="true"/>mΩ&nbsp;
                    </td>
                    <td width="90" align="right">交流内阻范围：</td>
                    <td>
                        <select style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10" name="minAcResistance" value="${(entity.minAcResistance)!''}"/>
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10" name="maxAcResistance" value="${(entity.maxAcResistance)!''}"/>
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">回弹电压：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" name="resilienceVol"  value="${(entity.resilienceVol)!''}" required="true"/>V&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">回弹电压范围：</td>
                    <td>
                        <select style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10" name="minResilienceVol" value="${(entity.minResilienceVol)!''}" />
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10" name="maxResilienceVol" value="${(entity.maxResilienceVol)!''}" />
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">静置电压：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" name="staticVol" value="${(entity.staticVol)!''}" required="true"/>V&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">静置电压范围：</td>
                    <td>
                        <select style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" data-options="precision:3" maxlength="10" name="minStaticVol" value="${(entity.minStaticVol)!''}" />
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;"" class="easyui-numberbox" data-options="precision:3" maxlength="10" name="maxStaticVol" value="${(entity.maxStaticVol)!''}" />
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">循环次数：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" style="width:184px;height: 28px;" maxlength="10" name="circle" value="${(entity.circle)!''}" required="true"/>次&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">循环次数范围：</td>
                    <td>
                        <select style="width:33px;">
                            <option>-</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" maxlength="10" name="minCircle"  value="${(entity.minCircle)!''}" />
                        <select style="width:33px;">
                            <option>+</option>
                        </select>
                        <input type="text" style="width: 55px;height: 28px;" class="easyui-numberbox" maxlength="10" name="maxCircle" value="${(entity.maxCircle)!''}" />
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">充电电流倍率：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" name="chgRate" value="${(entity.chgRate)!''}" required="true"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="90" align="right">充电电流：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" name="chgCurrent" value="${(entity.chgCurrent)!''}" required="true" />A
                    </td>
                </tr>
                <tr>
                    <td width="90" align="right">充电时间：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" name="chgTime" value="${(entity.chgTime)!''}" required="true"/>小时&nbsp;
                    </td>
                    <td width="90" align="right">放电截至电压：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" name="dsgCutVol" value="${(entity.dsgCutVol)!''}" required="true"/>V
                    </td>
                </tr>
                <tr>
                    <td width="120" align="right">最大持续充电电流：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" name="maxContinueChgCurrent" value="${(entity.maxContinueChgCurrent)!''}" required="true"/>A&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td width="120" align="right">最大持续放电电流：</td>
                    <td align="right">
                        <input type="text" class="easyui-numberbox" data-options="precision:3" style="width:184px;height: 28px;" maxlength="10" name="maxContinueDsgCurrent" value="${(entity.maxContinueDsgCurrent)!''}" required="true" />A
                    </td>
                </tr>
            </table>
            <input type="hidden" id="cell_format_id_${pid}">
            <input type="hidden" id="cell_regular_type_${pid}" value="${(regular.regularType)!''}">
            <input type="hidden" id="cell_regular_name_${pid}" value="${(regular.regularName)!''}">
            <input type="hidden" id="cell_reset_type_${pid}" value="${(regular.resetType)!''}">
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

        $("select").each(function () {
            $(this).attr("disabled","disabled");
        });

        win.find('input[name=cellMfr],input[name=cellModel]').click(function() {
            selectCellModel();
        });

        win.find('input[name=barcodeRule]').click(function() {
            var cellFormatId = win.find('input[name=id]').val();
            editBarcodeRule(cellFormatId);
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

        function editBarcodeRule(cellFormatId) {
            App.dialog.show({
                css: 'width:400px;height:420px;overflow:visible;',
                title: '条码规则',
                href: "${contextPath}/security/hdg/battery_cell_format/edit_barcode_rule.htm?cellFormatId=" + cellFormatId + "&regularType=" + 1 + "",
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

        win.find('button.ok').click(function() {
            $.ajax({
                type:'POST',
                url:'${contextPath}/security/hdg/battery_cell_regular/check_param.htm',
                dataType:'json',
                data:{
                    cellFormatId:win.find('input[name=id]').val(),
                    regularType: 1,
                    regular: win.find('input[name=barcodeRule]').val()
                },
                success:function(json1) {
                    if(json1.success) {
                        form.form('submit', {
                            url: '${contextPath}/security/hdg/battery_cell_format/update.htm',
                            success: function(text) {
                                var json = $.evalJSON(text);
                            <@app.json_jump/>
                                if(json.success) {
                                    $.ajax({
                                        type:'POST',
                                        url:'${contextPath}/security/hdg/battery_cell_regular/update.htm',
                                        dataType:'json',
                                        data: {
                                            cellFormatId:win.find('input[name=id]').val(),
                                            regularType: 1,
                                            regular: win.find('input[name=barcodeRule]').val(),
                                            regularName: $('#cell_regular_name_${pid}').val(),
                                            resetType: $('#cell_reset_type_${pid}').val()
                                        },
                                        success:function (json2) {
                                            if(json2.success){
                                                $.messager.alert('提示信息', '操作成功', 'info');
                                                win.window('close');
                                            }else {
                                                $.messager.alert('提示信息', json2.message, 'info');
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
