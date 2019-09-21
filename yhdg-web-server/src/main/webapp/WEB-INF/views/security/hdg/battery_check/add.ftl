<div class="popup_body">
    <div class="ui_table" style="width:760px;height: 475px;">
        <table cellpadding="0" cellspacing="0" style="height: 50px;">
            <tr>
                <td align="right">电池条码：</td>
                <td>
                    <input type="text" required="true" class="text easyui-validatebox" name="shellCode" style="width: 200px;"
                           id="battery_shell_code_${pid}" maxlength="40" value=""/>
                </td>
            <#--<td align="right">外观：</td>-->
            <#--<td>-->
            <#--<select class="easyui-combobox" required="true" id="battery_appearance_${pid}"  name="appearance" style="width:100px;height:28px ">-->
            <#--<#list AppearanceEnum as s>-->
            <#--<option value="${s.getValue()}"> ${s.getName()} </option>-->
            <#--</#list>-->
            <#--</select>-->
            <#--</td>-->
            <#--<td align="right">BMS编号：</td>-->
            <#--<td>-->
            <#--<input type="text" class="text easyui-validatebox" name="code"-->
            <#--id="battery_code_${pid}" maxlength="40" value=""/>-->
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
        <input type="hidden" name="cellMfr">
        <input type="hidden" name="cellModel">
        <input type="hidden" name="operator">
        <input type="hidden" name="createTime">
        <input type="hidden" name="id">
        <input type="hidden" name="batteryFormatId" id="battery_format_id_${pid}">
        <input type="hidden" name="cellCount" id="cell_count_${pid}">
        <input type="hidden" id="to_bind_cell_${pid}">
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red add_ok">确定</button>
    <button class="btn btn_border add_close">关闭</button>
</div>

<script>

    function showEmptyCellTable() {
        var win = $('#${pid}');
        var batteryId = win.find('input[name=id]').val();
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
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 80,
                        formatter: function (val, row) {
                            var html = '';
                        <@app.has_oper perm_code='2_6_1_9'>
                            html += ' <a href="javascript:unbind(\'ID\')">解绑</a>';
                        </@app.has_oper>
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ],
            onLoadSuccess: function () {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            }
        });
    }

    //查询电池条码信息
    function showBatteryBarcodeInfo(shellCode) {
        var win = $('#${pid}');
        $.ajax({
            type:'POST',
            url:'${contextPath}/security/hdg/battery_barcode/find_barcode.htm',
            dataType:'json',
            data:{
                //电池条码就是电池的外壳编号
                barcode:shellCode
            },
            success:function (json) {
                if(json.success){
                    var data = json.data;
                    $('#cell_mfr_${pid}').html(data.cellMfr);
                    win.find('input[name=cellMfr]').val(data.cellMfr);
                    $('#cell_model_${pid}').html(data.cellModel);
                    win.find('input[name=cellModel]').val(data.cellModel);
                    $('#operator_${pid}').html(data.operator);
                    win.find('input[name=operator]').val(data.operator);
                    $('#create_time_${pid}').html(data.createTime);
                    win.find('input[name=createTime]').val(data.createTime);
                    $('#battery_format_id_${pid}').val(data.batteryFormatId);

                    var oInput = document.getElementById("battery_code_${pid}");
                    oInput.focus();
                }else{
                    $.messager.alert('提示信息', json.message, 'info');
                    $('#battery_shell_code_${pid}').val('');
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
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 80,
                        formatter: function (val, row) {
                            var html = '';
                        <@app.has_oper perm_code='2_6_1_9'>
                            html += ' <a href="javascript:unbind(\'ID\')">解绑</a>';
                        </@app.has_oper>
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ],
            onLoadSuccess: function () {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            }
        });
    }

    function showCellTableByBarcode(shellCode) {
        $.ajax({
            type: 'POST',
            url: '${contextPath}/security/hdg/battery_check/find_battery_by_barcode.htm',
            dataType: 'json',
            data: {
                shellCode: shellCode
            },
            success: function (json6) {
                if(json6.success) {
                    var win = $('#${pid}');
                    var data6 = json6.data;
                    if(data6.id == '' || data6.id == null) {
                        //创建新电池的逻辑
                        $('#to_bind_cell_${pid}').val("toBindCell");
                    }else{
                        win.find('input[name=id]').val(data6.id);
                    <#--//将外观和BMS编号直接显示在框中-->
                    <#--$('#battery_appearance_${pid}').val(data6.appearance);-->
                    <#--win.find('input[name=code]').val(data6.code);-->
                    <#--//设置为不可编辑-->
                    <#--$('#battery_appearance_${pid}').combo('readonly', true);-->
                    <#--win.find('input[name=code]').combo('readonly', true);-->
                    }
                    var batteryId = win.find('input[name=id]').val();

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
//                                {title: '外观', align: 'center', field: 'appearance', width: 60, formatter:function (val) {
//                                    var appearance = new Number(val).toFixed(0);
//                                    var result = "";
//                                    if(appearance == 1) {
//                                        result = "R";
//                                    }else if(appearance == 2) {
//                                        result = "M";
//                                    }else if(appearance == 3) {
//                                        result = "Y";
//                                    }
//                                    return result;
//                                }},
                                {
                                    title: '操作',
                                    align: 'center',
                                    field: 'action',
                                    width: 80,
                                    formatter: function (val, row) {
                                        var html = '';
                                    <@app.has_oper perm_code='2_6_1_9'>
                                        html += ' <a href="javascript:unbind(\'ID\')">解绑</a>';
                                    </@app.has_oper>
                                        return html.replace(/ID/g, row.id);
                                    }
                                }
                            ]
                        ],
                        onLoadSuccess: function () {
                            datagrid.datagrid('clearChecked');
                            datagrid.datagrid('clearSelections');
                        }
                    });
                }else {
                    //创建一个新电池，把id存起来

                    showEmptyCellTable();
                }
            }
        });
    }

    //查询电芯信息
    function showCellTable(code) {
        var win = $('#${pid}');
        var shellCode = $('#battery_shell_code_${pid}').val();
        //根据code和shellCode去查找电池
        //检验code和shellCode是否匹配
        $.ajax({
            type:'POST',
            url:'${contextPath}/security/hdg/battery_check/check_bind_param.htm',
            dataType:'json',
            data:{
                code:code,
                shellCode:shellCode
            },
            success:function (json) {
                if(json.success) {
                    var data = json.data;
                    if(data.id == null || data.id == '') {
                        //创建新电池的逻辑
                        $('#to_bind_cell_${pid}').val("toBindCell");
                    }else{
                        win.find('input[name=id]').val(data.id);
                    }
                    //页面一加载就显示空表格
//                    document.getElementById("batteryCellTable").style.display = 'block';
                    var batteryId = win.find('input[name=id]').val();
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
//                                {title: '外观', align: 'center', field: 'appearance', width: 60, formatter:function (val) {
//                                    var appearance = new Number(val).toFixed(0);
//                                    var result = "";
//                                    if(appearance == 1) {
//                                        result = "R";
//                                    }else if(appearance == 2) {
//                                        result = "M";
//                                    }else if(appearance == 3) {
//                                        result = "Y";
//                                    }
//                                    return result;
//                                }},
                                {
                                    title: '操作',
                                    align: 'center',
                                    field: 'action',
                                    width: 80,
                                    formatter: function (val, row) {
                                        var html = '';
                                    <@app.has_oper perm_code='2_6_1_9'>
                                        html += ' <a href="javascript:unbind(\'ID\')">解绑</a>';
                                    </@app.has_oper>
                                        return html.replace(/ID/g, row.id);
                                    }
                                }
                            ]
                        ],
                        onLoadSuccess: function () {
                            datagrid.datagrid('clearChecked');
                            datagrid.datagrid('clearSelections');
                        }
                    });
                }else {
                    $.messager.alert('提示信息', json.message, 'info');
                    $('#battery_code_${pid}').val('');
                    return false;
                }
            }
        })
    }

    //绑定电芯
    function toBindCell() {
        var win = $('#${pid}');
        var batteryBarcode = $('#battery_shell_code_${pid}').val();
        if(batteryBarcode == null || batteryBarcode == '') {
            $.messager.alert('提示信息', '请输入电池条码，并按一下回车键', 'info');
            $('#battery_code_${pid}').val('');
            return false;
        }
        var batteryFormatId = win.find('input[name=batteryFormatId]').val();
        if(batteryFormatId == null || batteryFormatId == '') {
            $.messager.alert('提示信息', '请在电池条码输入框按一下回车键', 'info');
            $('#battery_code_${pid}').val('');
            return false;
        }
    <#--var batteryCode = $('#battery_code_${pid}').val();-->
    <#--if (batteryCode == null || batteryCode == '') {-->
    <#--$.messager.alert('提示信息', '请输入BMS编号，并按一下回车键', 'info');-->
    <#--return false;-->
    <#--}-->
    <#--var bindFlag = $('#to_bind_cell_${pid}').val();-->
    <#--if(bindFlag != "toBindCell") {-->
    <#--var batteryId = win.find('input[name=id]').val();-->
    <#--if(batteryId == null || batteryId == '') {-->
    <#--$.messager.alert('提示信息', '请在BMS编号输入框按一下回车键', 'info');-->
    <#--return false;-->
    <#--}-->
    <#--}-->
        var batteryCode = $('#battery_code_${pid}').val();
        if(batteryCode !=null && batteryCode != '') {
            if(/^\w+$/.test(batteryCode)) {

            }else{
                $.messager.alert('提示信息', 'BMS编号只能是数字或字母', 'info');
                $('#battery_code_${pid}').val('');
                return;
            }
        }

        var batteryId = win.find('input[name=id]').val();
        var batteryCode = $('#battery_code_${pid}').val();
        if(batteryCode == '' || batteryCode == null) {
            batteryCode = '';
        }

        var cellMfr = win.find('input[name=cellMfr]').val();
        var cellModel = win.find('input[name=cellModel]').val();
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

    window.onload = function(){
        var oInput = document.getElementById("battery_shell_code_${pid}");
        oInput.focus();
    }

    window.setTimeout( function(){ document.getElementById('battery_shell_code_${pid}').focus(); }, 0);

    //刷新
    function pageTableReload() {
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid('reload');
    }

    (function () {
        var win = $('#${pid}'), windowData = win.data('windowData');

        showEmptyCellTable();

        $('#battery_code_${pid}').focus(function () {
            var batteryBarcode = $('#battery_shell_code_${pid}').val();
            if(batteryBarcode == null || batteryBarcode == '') {
                $.messager.alert('提示信息', '请输入电池条码，并按一下回车键', 'info');
                $('#battery_code_${pid}').val('');
            }else{
                var batteryFormatId = win.find('input[name=batteryFormatId]').val();
                if(batteryFormatId == null || batteryFormatId == '') {
                    $.messager.alert('提示信息', '请在电池条码输入框按一下回车键', 'info');
                    $('#battery_code_${pid}').val('');
                }
            }
        });

        //点击绑定电芯按钮
        $('#bind_cell_${pid}').click(function () {
            toBindCell();
        })
        //点击确定
        win.find('button.add_ok').click(function () {
            var batteryBarcode = $('#battery_shell_code_${pid}').val();
            if(batteryBarcode == null || batteryBarcode == '') {
                $.messager.alert('提示信息', '请输入电池条码，并按一下回车键', 'info');
                $('#battery_code_${pid}').val('');
                return false;
            }
            var batteryFormatId = win.find('input[name=batteryFormatId]').val();
            if(batteryFormatId == null || batteryFormatId == '') {
                $.messager.alert('提示信息', '请在电池条码输入框按一下回车键', 'info');
                $('#battery_code_${pid}').val('');
                return false;
            }
        <#--var batteryCode = $('#battery_code_${pid}').val();-->
        <#--if (batteryCode == null || batteryCode == '') {-->
        <#--$.messager.alert('提示信息', '请输入BMS编号，并按一下回车键', 'info');-->
        <#--return false;-->
        <#--}-->
        <#--var bindFlag = $('#to_bind_cell_${pid}').val();-->
        <#--if(bindFlag != "toBindCell") {-->
        <#--var batteryId = win.find('input[name=id]').val();-->
        <#--if(batteryId == null || batteryId == '') {-->
        <#--$.messager.alert('提示信息', '请在BMS编号输入框按一下回车键', 'info');-->
        <#--return false;-->
        <#--}-->
        <#--}-->
            var batteryId = win.find('input[name=id]').val();
            if(batteryId == null || batteryId == '') {
                $.messager.alert('提示信息', '请先绑定电芯', 'info');
                return false;
            }else{
                var cellMfr = win.find('input[name=cellMfr]').val();
                var cellModel = win.find('input[name=cellModel]').val();

                $.ajax({
                    type:'POST',
                    url:'${contextPath}/security/hdg/battery_check/find_battery.htm',
                    dataType:'json',
                    data:{
                        id:batteryId
                    },
                    success:function (json) {
                        if(json.success) {
                            var data = json.data;
                            if(data == 0) {
                                $.messager.alert('提示信息', '请先绑定电芯', 'info');
                                return false;
                            }
                        }else {
                            $.messager.alert('提示信息', json.message, 'info');
                        }
                    }
                })

                $.ajax({
                    type:'POST',
                    url:'${contextPath}/security/hdg/battery_check/check_battery.htm',
                    dataType:'json',
                    data:{
                        id:batteryId,
                        cellMfr:cellMfr,
                        cellModel:cellModel
                    },
                    success:function (json) {
                        if(json.success) {
                            $.messager.alert('提示信息', '操作成功', 'info');
                            win.window('close');
                        }else {
                            $.messager.alert('提示信息', json.message, 'info');
                        }
                    }
                })
            }
        });
        //点击关闭
        win.find('button.add_close').click(function () {
            win.window('close');
        });

        //电池条码改变，条码信息清空，batteryFormatId、电池id清空
        $('#battery_shell_code_${pid}').change(function () {
            win.find('input[name=id]').val('');
            $('#to_bind_cell_${pid}').val('');
            $('#cell_mfr_${pid}').html('');
            $('#cell_model_${pid}').html('');
            $('#operator_${pid}').html('');
            $('#create_time_${pid}').html('');
            $('#battery_format_id_${pid}').val('');
        <#--//设置为可编辑-->
        <#--$('#battery_appearance_${pid}').combo('readonly', false);-->
        <#--win.find('input[name=code]').combo('readonly', false);-->
//            document.getElementById("batteryCellTable").style.display = 'none';
        });

        //BMS编号改变，电池条码非空判断，电池id隐藏框清空
        $('#battery_code_${pid}').change(function () {
            var batteryBarcode = $('#battery_shell_code_${pid}').val();
            if(batteryBarcode == null || batteryBarcode == '') {
                $.messager.alert('提示信息', '请输入电池条码，并按一下回车键', 'info');
                $('#battery_code_${pid}').val('');
                $('#battery_shell_code_${pid}').focus();
            }
            var batteryFormatId = win.find('input[name=batteryFormatId]').val();
            if(batteryFormatId == null || batteryFormatId == '') {
                $.messager.alert('提示信息', '请在电池条码输入框按一下回车键', 'info');
                $('#battery_code_${pid}').val('');
                $('#battery_shell_code_${pid}').focus();
            }
            win.find('input[name=id]').val('');
            $('#to_bind_cell_${pid}').val('');
//            document.getElementById("batteryCellTable").style.display = 'none';
        });


    <#--在电池条码输入框按回车键-->
        $('#battery_shell_code_${pid}').bind('keyup', function(event) {
            if (event.keyCode == "13") {
                var shellCode = $('#battery_shell_code_${pid}').val();
                showBatteryBarcodeInfo(shellCode);
                showCellTableByBarcode(shellCode);
            }
        });

    <#--BMS编号输入框按回车键-->
        $('#battery_code_${pid}').bind('keyup', function(event) {
            if (event.keyCode == "13") {
                var batteryCode = $('#battery_code_${pid}').val();
                showCellTable(batteryCode);
            }
        });

    })();

    var win = $('#${pid}'), windowData = win.data('windowData');
    var datagrid = $('#page_table_${pid}');


</script>