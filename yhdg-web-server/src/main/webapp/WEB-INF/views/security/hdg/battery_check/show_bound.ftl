<div class="popup_body">
    <div class="ui_table" style="width:760px;height: 475px;">
        <table cellpadding="0" cellspacing="0" style="height: 50px;">
            <tr>
                <td align="right">电池条码：</td>
                <td>
                    <input type="text" required="true" class="text easyui-validatebox" name="shellCode"
                           id="battery_shell_code_${pid}" readonly maxlength="40" value="${(entity.shellCode)!''}"/>
                </td>
            <#--<td width="90" align="right">外观：</td>-->
            <#--<td>-->
            <#--<select class="easyui-combobox" required="true" readonly="readonly"  name="appearance" style="width:187px;height:28px ">-->
            <#--<#list AppearanceEnum as s>-->
            <#--<option value="${s.getValue()}" <#if entity.appearance?? && entity.appearance==s.getValue()>selected="selected"</#if> > ${s.getName()} </option>-->
            <#--</#list>-->
            <#--</select>-->
            <#--</td>-->
                <td align="right"></td>
                <td style="float: right">
                    <button class="btn btn_red" id="bind_cell_${pid}">绑定电芯</button>
                </td>
            </tr>
        </table>
        <table class="batteryBarcodeInfoTable" id="showBatteryBarcodeInfo" cellpadding="40" cellspacing="20"
               style="width: 100%;height: 30px;">
            <tr style="width: 100%">
                <td style="width: 12%" align="right">电芯厂家：</td>
                <td style="width: 8%"><span id="cell_mfr_${pid}"></span></td>
                <td style="width: 12%" align="right">电芯型号：</td>
                <td style="width: 8%"><span id="cell_model_${pid}"></span></td>
                <td style="width: 12%" align="right">检验人：</td>
                <td style="width: 8%"><span id="operator_${pid}"></span></td>
                <td style="width: 12%" align="right">检验时间：</td>
                <td style="width: 28%"><span id="create_time_${pid}"></span></td>
            </tr>
        </table>
        <div class="panel grid_wrap" id="showBoundCellInfo" style="margin-top: 0px;">
            <div class="toolbar clearfix" style="margin-top: 0px;height: 30px;">
                <div class="float_right" style="margin-top: 0px;">
                </div>
                <h3>已绑定电芯</h3>
            </div>
        </div>
        <div id="batteryCellTable" style="width: 100%;height: 340px;">
            <table class="cellInfoTable" id="page_table_${pid}" style="width: 100%;height: 300px;">
            </table>
        </div>
        <input type="hidden" name="cellMfr" value="${(entity.cellMfr)!''}">
        <input type="hidden" name="cellModel" value="${(entity.cellModel)!''}">
        <input type="hidden" name="operator">
        <input type="hidden" name="createTime" value="<#if (entity.createTime)?? >${app.format_date_time(entity.createTime)}</#if>">
        <input type="hidden" name="id" value="${(entity.id)!''}">
        <input type="hidden" name="batteryFormatId" id="battery_format_id_${pid}" value="${(batteryFormatId)!''}">
        <input type="hidden" name="cellCount" id="cell_count_${pid}">
        <input type="hidden" id="to_bind_cell_${pid}">
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red add_ok">确定</button>
    <button class="btn btn_border add_close">关闭</button>
</div>

<script>

    //查询电池条码信息
    function showBatteryBarcodeInfo(shellCode) {
        $.ajax({
            type:'POST',
            url:'${contextPath}/security/hdg/battery_barcode/find_barcode.htm',
            dataType:'json',
            data:{
                //电池条码就是电池的外壳编号
                barcode:shellCode
            },
            success:function (json) {
                var win = $('#${pid}');
                if(json.success){
                    var data = json.data;
                    if($('#cell_mfr_${pid}').html() == '' || $('#cell_mfr_${pid}').html == null) {
                        $('#cell_mfr_${pid}').html(data.cellMfr);
                        win.find('input[name=cellMfr]').val(data.cellMfr);
                    }else{
                        $('#cell_mfr_${pid}').html(win.find('input[name=cellMfr]').val());
                    }
                    if($('#cell_model_${pid}').html() == '' || $('#cell_model_${pid}').html == null) {
                        $('#cell_model_${pid}').html(data.cellModel);
                        win.find('input[name=cellModel]').val(data.cellModel);
                    }else{
                        $('#cell_model_${pid}').html(win.find('input[name=cellModel]').val());
                    }
                    $('#operator_${pid}').html(data.operator);
                    $('#create_time_${pid}').html(win.find('input[name=createTime]').val());
                }else{
                    $.messager.alert('提示信息', json.message, 'info');
                    var win = $('#${pid}');
                    win.window('close');
                    return;
                }
            }
        })
    }

    function showTableAgain(batteryId) {
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/battery_cell/bound_page.htm?batteryId=" + batteryId,
            pageSize: 10,
            pageList: [10, 50, 100],
            fitColumns: true,
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '条码编号',
                        align: 'center',
                        field: 'barcode',
                        width: 100
                    },
                    {title: '组包容量', align: 'center', field: 'nominalCap', width: 60, formatter:function (val) {
                        var html = "" + new Number(val/1000).toFixed(3) + "";
                        return html;
                    }},
                    {title: '交流内阻', align: 'center', field: 'acResistance', width: 60},
                    {title: '回弹电压', align: 'center', field: 'resilienceVol', width: 60, formatter:function (val) {
                        var html = "" + new Number(val/1000).toFixed(3) + "";
                        return html;
                    }},
                    {title: '静置电压', align: 'center', field: 'staticVol', width: 60, formatter:function (val) {
                        var html = "" + new Number(val/1000).toFixed(3) + "";
                        return html;
                    }},
                    {title: '当前循环', align: 'center', field: 'circle', width: 60},
//                    {title: '外观', align: 'center', field: 'appearance', width: 60, formatter:function (val) {
//                        var appearance = new Number(val).toFixed(0);
//                        var result = "";
//                        if(appearance == 1) {
//                            result = "R";
//                        }else if(appearance == 2) {
//                            result = "M";
//                        }else if(appearance == 3) {
//                            result = "Y";
//                        }
//                        return result;
//                    }},
                <#if editFlag?? && editFlag== 1>
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 80,
                        formatter: function (val, row) {
                            var html = '';
                            <#--<@app.has_oper perm_code='2_6_1_9'>-->
                                html += ' <a href="javascript:unbind(\'ID\')">解绑</a>';
                            <#--</@app.has_oper>-->
                            return html.replace(/ID/g, row.id);
                        }
                    }
                </#if>
                ]
            ],
            onLoadSuccess: function () {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            }
        });
    }

    //查询电芯信息
    function showCellTable(batteryId) {
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/battery_cell/bound_page.htm?batteryId=" + batteryId,
            pageSize: 10,
            pageList: [10, 50, 100],
            fitColumns: true,
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '条码编号',
                        align: 'center',
                        field: 'barcode',
                        width: 100
                    },
                    {title: '组包容量', align: 'center', field: 'nominalCap', width: 60, formatter:function (val) {
                        var html = "" + new Number(val/1000).toFixed(3) + "";
                        return html;
                    }},
                    {title: '交流内阻', align: 'center', field: 'acResistance', width: 60},
                    {title: '回弹电压', align: 'center', field: 'resilienceVol', width: 60, formatter:function (val) {
                        var html = "" + new Number(val/1000).toFixed(3) + "";
                        return html;
                    }},
                    {title: '静置电压', align: 'center', field: 'staticVol', width: 60, formatter:function (val) {
                        var html = "" + new Number(val/1000).toFixed(3) + "";
                        return html;
                    }},
                    {title: '当前循环', align: 'center', field: 'circle', width: 60},
//                    {title: '外观', align: 'center', field: 'appearance', width: 60, formatter:function (val) {
//                        var appearance = new Number(val).toFixed(0);
//                        var result = "";
//                        if(appearance == 1) {
//                            result = "R";
//                        }else if(appearance == 2) {
//                            result = "M";
//                        }else if(appearance == 3) {
//                            result = "Y";
//                        }
//                        return result;
//                    }},
                <#if editFlag?? && editFlag== 1>
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 80,
                        formatter: function (val, row) {
                            var html = '';
                        <#--<@app.has_oper perm_code='2_6_1_9'>-->
                            html += ' <a href="javascript:unbind(\'ID\')">解绑</a>';
                        <#--</@app.has_oper>-->
                            return html.replace(/ID/g, row.id);
                        }
                    }
                </#if>
                ]
            ],
            onLoadSuccess: function () {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            }
        });
    }

    //绑定电芯
    function toBindCell() {
        var win = $('#${pid}');
        var batteryFormatId = $('#battery_format_id_${pid}').val();
        var batteryId = win.find('input[name=id]').val();
        var cellMfr = win.find('input[name=cellMfr]').val();
        var cellModel = win.find('input[name=cellModel]').val();
        var batteryBarcode = $('#battery_shell_code_${pid}').val();
        var batteryCode = $('#battery_code_${pid}').val();
//        var appearance = win.find('input[name=appearance]').val();

        App.dialog.show({
            css: 'width:700px;height:500px;',
            title: '绑定电芯',
            href: "${contextPath}/security/hdg/battery_cell/bind.htm?batteryFormatId=" + batteryFormatId + "&batteryId=" + batteryId + "&cellMfr=" + cellMfr + "&cellModel=" + cellModel + "&shellCode=" + batteryBarcode + "&code=" + batteryCode  + "&appearance=" + 1,
            windowData:{
                ok:function (batteryId) {
                    win.find('input[name=id]').val(batteryId);
                    showTableAgain(batteryId);
                }
            },
            event: {
                onClose: function () {
                    var batteryId = win.find('input[name=id]').val();
                    showTableAgain(batteryId);
                    pageTableReload();
                }
            }
        });
    }

    //电芯解绑
    function unbind(id) {
        $.messager.confirm("提示信息", "确认解绑?", function (ok) {
            if (ok) {
                var batteryId = win.find('input[name=id]').val();
                $.post("${contextPath}/security/hdg/battery_cell/unbind.htm?id=" + id +"&batteryId=" + batteryId, function (json) {
                    if (json.success) {
                        $.messager.alert("提示信息", "解绑成功", "info");
                        pageTableReload();
                    } else {
                        $.messager.alert("提示信息", json.message, "info")
                    }
                }, 'json');
            }
        });
    }

    //刷新
    function pageTableReload() {
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid('reload');
    }

    (function () {
        var win = $('#${pid}'), windowData = win.data('windowData');

        var shellCode = $('#battery_shell_code_${pid}').val();
        showBatteryBarcodeInfo(shellCode);

        var batteryId = win.find('input[name=id]').val();
        showCellTable(batteryId);

        //点击绑定电芯按钮
        $('#bind_cell_${pid}').click(function () {
            toBindCell();
        })
        //点击确定
        win.find('button.add_ok').click(function () {
            var batteryId = win.find('input[name=id]').val();
            var cellMfr = win.find('input[name=cellMfr]').val();
            var cellModel = win.find('input[name=cellModel]').val();

            $.ajax({
                type: 'POST',
                url: '${contextPath}/security/hdg/battery_check/check_battery.htm',
                dataType: 'json',
                data: {
                    id: batteryId,
                    cellMfr: cellMfr,
                    cellModel: cellModel
                },
                success: function (json) {
                    if (json.success) {
                        $.messager.alert('提示信息', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            })
        });
        //点击关闭
        win.find('button.add_close').click(function () {
            win.window('close');
        });

    })();

    var win = $('#${pid}'), windowData = win.data('windowData');
    var datagrid = $('#page_table_${pid}');


</script>