<@app.html>
    <@app.head>
    <style type="text/css">

        table.formatInfoTable {
            font-family: verdana,arial,sans-serif;
            font-size:15px;
            color:#333333;
            border-width: 1px;
            border-color: #999999;
            border-collapse: collapse;
        }
        table.formatInfoTable th {
            background:#b5cfd2;
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: #999999;
        }
        table.formatInfoTable td {
            background:#dcddc0;
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: #999999;
        }
        table.batteryBarcodeInfoTable {
            font-family: verdana,arial,sans-serif;
            font-size:15px;
            -webkit-text-fill-color: #999999;
            border-collapse: collapse;
        }
        table.batteryBarcodeInfoTable th {
            padding: 8px;
        }
        table.batteryBarcodeInfoTable td {
            padding: 8px;
        }
        table.barcodeInfoTable {
            font-family: verdana,arial,sans-serif;
            font-size:15px;
            -webkit-text-fill-color: #999999;
            border-collapse: collapse;
        }
        table.barcodeInfoTable th {
            padding: 8px;
        }
        table.barcodeInfoTable td {
            padding: 8px;
        }
    </style>

    <script>
        $(function () {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                nowrap: false,
                striped: true,
                pagination: true,
                url: "${contextPath}/security/hdg/battery_check/page.htm",
                fitColumns: true,
                pageSize: 50,
                pageList: [50, 100],
                idField: 'id',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                columns: [
                    [
                        {
                            title: '序号',
                            align: 'center',
                            field: 'id',
                            width: 40
                        },
                        {
                            title: 'IMEI',
                            align: 'center',
                            field: 'code',
                            width: 60,
                            formatter: function (val, row) {
                                if(val == null || val == '') {
                                    var html = '<a style="color: #ff0000" href="javascript:updateCode(\'ID\')">完善电池信息</a>';
                                    return html.replace(/ID/g, row.id);
                                }else {
                                    return val;
                                }
                            }
                        },
                        {
                            title: '电芯厂家',
                            align: 'center',
                            field: 'cellMfr',
                            width: 40
                        },
                        {
                            title: '电芯型号',
                            align: 'center',
                            field: 'cellModel',
                            width: 40
                        },
                        {
                            title: '条码编号',
                            align: 'center',
                            field: 'shellCode',
                            width: 50
                        },
                        {
                            title: '已绑定电芯',
                            align: 'center',
                            field: 'cellCount',
                            width: 40,
                            formatter: function (val, row) {
                                if(val != null){
                                    if(val == row.expectCellCount) {
                                        var html = '<a href="javascript:showBound(\'ID\',\'BARCODE\')" style="color: #00FF00">' + new Number(val).toFixed(0) + '</a>';
                                        return html.replace(/ID/g, row.id).replace(/BARCODE/g, row.shellCode);
                                    }else{
                                        var html = '<a href="javascript:showBound(\'ID\',\'BARCODE\')" style="color: #ff0000">' + new Number(val).toFixed(0) + '</a>';
                                        return html.replace(/ID/g, row.id).replace(/BARCODE/g, row.shellCode);
                                    }
                                }
                            }
                        },
                        {
                            title: '组包容量',
                            align: 'center',
                            field: 'batteryFormat1',
                            width: 50,
                            formatter: function (val) {
                                if(val != null){
                                    var nominalCap = val.nominalCap;;
                                    var minNominalCap = val.minNominalCap;
                                    var maxNominalCap = val.maxNominalCap;
                                    (nominalCap == null) ? nominalCap = '' : nominalCap = nominalCap;
                                    (minNominalCap == null) ? minNominalCap = '-0' : minNominalCap = "-" + minNominalCap;
                                    (maxNominalCap == null) ? maxNominalCap = '+0' : maxNominalCap = "+" + maxNominalCap;
                                    return "标准值:"+ nominalCap + "<br>偏差:" + minNominalCap + "," + maxNominalCap;
                                }
                            }
                        },
                        {
                            title: '交流内阻',
                            align: 'center',
                            field: 'batteryFormat2',
                            width: 50,
                            formatter: function (val) {
                                if(val != null) {
                                    var acResistance = val.acResistance;;
                                    var minAcResistance = val.minAcResistance;
                                    var maxAcResistance = val.maxAcResistance;
                                    (acResistance == null) ? acResistance = '' : acResistance = acResistance;
                                    (minAcResistance == null) ? minAcResistance = '-0' : minAcResistance = "-" + minAcResistance;
                                    (maxAcResistance == null) ? maxAcResistance = '+0' : maxAcResistance = "+" + maxAcResistance;
                                    return "标准值:"+ acResistance + "<br>偏差:" + minAcResistance + "," + maxAcResistance;
                                }
                            }
                        },
                        {
                            title: '回弹电压',
                            align: 'center',
                            field: 'batteryFormat3',
                            width: 50,
                            formatter: function (val) {
                                if(val != null) {
                                    var resilienceVol = val.resilienceVol;;
                                    var minResilienceVol = val.minResilienceVol;
                                    var maxResilienceVol = val.maxResilienceVol;
                                    (resilienceVol == null) ? resilienceVol = '' : resilienceVol = resilienceVol;
                                    (minResilienceVol == null) ? minResilienceVol = '-0' : minResilienceVol = "-" + minResilienceVol;
                                    (maxResilienceVol == null) ? maxResilienceVol = '+0' : maxResilienceVol = "+" + maxResilienceVol;
                                    return "标准值:"+ resilienceVol + "<br>偏差:" + minResilienceVol + "," + maxResilienceVol;
                                }
                            }
                        },
                        {
                            title: '静置电压',
                            align: 'center',
                            field: 'batteryFormat4',
                            width: 50,
                            formatter: function (val) {
                                if(val != null) {
                                    var staticVol = val.staticVol;
                                    var minStaticVol = val.minStaticVol;
                                    var maxStaticVol = val.maxStaticVol;
                                    (staticVol == null) ? staticVol = '' : staticVol = staticVol;
                                    (minStaticVol == null) ? minStaticVol = '-0' : minStaticVol = "-" + minStaticVol;
                                    (maxStaticVol == null) ? maxStaticVol = '+0' : maxStaticVol = "+" + maxStaticVol;
                                    return "标准值:"+ staticVol + "<br>偏差:" + minStaticVol + "," + maxStaticVol;
                                }
                            }
                        },
                        {
                            title: '循环寿命',
                            align: 'center',
                            field: 'batteryFormat5',
                            width: 50,
                            formatter: function (val) {
                                if(val != null) {
                                    var circle = val.circle;
                                    var minCircle = val.minCircle;
                                    var maxCircle = val.maxCircle;
                                    (circle == null) ? circle = '' : circle = circle;
                                    (minCircle == null) ? minCircle = '-0' : minCircle = "-" + minCircle;
                                    (maxCircle == null) ? maxCircle = '+0' : maxCircle = "+" + maxCircle;
                                    return "标准值:"+ circle + "<br>偏差:" + minCircle + "," + maxCircle;
                                }
                            }
                        },
                        {
                            title: '外观',
                            align: 'center',
                            field: 'appearance',
                            width: 40,
                            formatter: function (val) {
                                if(val != null) {
                                    var result = "";
                                    if(val == 1) {
                                        result = "R";
                                    }else if(val == 2) {
                                        result = "M";
                                    }else if(val == 3) {
                                        result = "Y";
                                    }
                                    return result;
                                }
                            }
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 50,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='2_6_1_9'>
                                    html += ' <a href="javascript:remove(\'ID\')">删除</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='2_6_1_8'>
                                    html += ' <a href="javascript:exchangeQrcode(\'ID\')">交换二维码</a>';
                                </@app.has_oper>
                                if(row.code == null || row.code == '') {
                                    html += ' <a style="color: #ff0000" href="javascript:updateCode(\'ID\')">绑定BMS编号</a>';
                                }
                                html += '<a href="javascript:showBound(\'ID\',\'BARCODE\')" > 修改</a>';
                                return html.replace(/ID/g, row.id).replace(/BARCODE/g, row.shellCode);
                            }
                        }
                    ]
                ],
                onLoadSuccess: function () {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                }
            });
        });


        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();

            var queryParams = {
                cellMfr: $('#cell_mfr').val(),
                cellModel: $('#cell_model').val(),
//                shellCode: $('#shell_code').val()
            };
            queryParams[queryName] = queryValue;
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function showBound(batteryId, barcode) {
            $.ajax({
                type: 'POST',
                url: '${contextPath}/security/hdg/battery_barcode/find_barcode.htm',
                dataType: 'json',
                data: {
                    barcode: barcode
                },
                success: function (json) {
                    if(json.success) {
                        App.dialog.show({
                            css: 'width:800px;height:590px;',
                            title: '已绑定电芯',
                            href: "${contextPath}/security/hdg/battery_check/show_bound.htm?batteryId=" + batteryId,
                            event: {
                                onClose: function () {
                                    reload();
                                }
                            }
                        });
                    }else{
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        }

        function add() {
            App.dialog.show({
                css: 'width:800px;height:590px;',
                title: '电池检验',
                href: "${contextPath}/security/hdg/battery_check/add.htm",
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function exchangeQrcode(id) {
            App.dialog.show({
                css: 'width:400px;height:195px;',
                title: '交换二维码',
                href: "${contextPath}/security/hdg/battery_check/exchange_qrcode.htm?id=" + id,
                event: {
                    onClose: function () {
                        reload();
                    },
                    onLoad: function () {
                    }
                }
            });
        }

        function updateCode(id) {
            App.dialog.show({
                css: 'width:400px;height:155px;',
                title: '绑定BMS编号',
                href: "${contextPath}/security/hdg/battery_check/edit_code.htm?id=" + id,
                event: {
                    onClose: function () {
                        reload();
                    },
                    onLoad: function () {
                    }
                }
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '删除该电池会解绑其中的电芯，确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/hdg/battery_check/delete.htm?id=" + id, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            reload();
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        }

    </script>
    </@app.head>
    <@app.body>
        <@app.container>
            <@app.banner/>
        <div class="main">
            <@app.menu/>

            <div class="content">
                <div class="panel search">
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0" style="border-collapse:separate; border-spacing: 0px 3px;">
                        <tr>
                            <td align="right">电芯厂家：</td>
                            <td><input type="text" style="width:170px; height: 28px;" class="text"
                                       id="cell_mfr"/>
                            </td>
                            <td align="right">&nbsp;&nbsp;电芯型号：</td>
                            <td><input type="text" style="width:170px; height: 28px;" class="text"
                                       id="cell_model"/>
                            </td>
                            <#--<td align="right">&nbsp;&nbsp;条码编号：</td>-->
                            <#--<td><input type="text" style="width:170px; height: 28px;" class="text"-->
                                       <#--id="shell_code"/>-->
                            <#--</td>-->
                            <td align="right" width="85">
                                <select style="width:80px;" id="query_name">
                                    <option value="id">序号</option>
                                    <option value="code">IMEI</option>
                                    <option value="shellCode">条码编号</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-top: 30px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <#--<@app.has_oper perm_code='2_6_1_6'>-->
                                <button class="btn btn_green" onclick="add()">新建</button>
                            <#--</@app.has_oper>-->
                        </div>
                        <h3>电池信息列表</h3>
                    </div>
                    <div class="grid">
                        <table id="page_table"></table>
                    </div>
                </div>
            </div>
        </div>
        </@app.container>
    </@app.body>
</@app.html>