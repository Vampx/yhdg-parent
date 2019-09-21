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
                striped: true,
                pagination: true,
                url: "${contextPath}/security/hdg/battery_cell/page.htm",
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
                            title: '电芯厂家',
                            align: 'center',
                            field: 'cellMfr',
                            width: 60
                        },
                        {
                            title: '电芯型号',
                            align: 'center',
                            field: 'cellModel',
                            width: 60
                        },
                        {
                            title: '条码编号',
                            align: 'center',
                            field: 'barcode',
                            width: 100
                        },
                        {
                            title: '组包容量',
                            align: 'center',
                            field: 'nominalCap',
                            width: 60,
                            formatter: function (val, row) {
                                return val/1000;
                            }
                        },
                        {
                            title: '交流内阻',
                            align: 'center',
                            field: 'acResistance',
                            width: 60
                        },
                        {
                            title: '回弹电压',
                            align: 'center',
                            field: 'resilienceVol',
                            width: 60,
                            formatter: function (val, row) {
                                return val/1000;
                            }
                        },
                        {
                            title: '静置电压',
                            align: 'center',
                            field: 'staticVol',
                            width: 60,
                            formatter: function (val, row) {
                                return val/1000;
                            }
                        },
                        {
                            title: '外观',
                            align: 'center',
                            field: 'appearance',
                            width: 60,
                            formatter: function (val) {
                                <#list AppearanceEnum as e>
                                    if (${e.getValue()} == val)
                                    return '${e.getName()}';
                                </#list>
                            }
                        },
                        {
                            title: '当前循环',
                            align: 'center',
                            field: 'circle',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 40,
                            formatter: function (val, row) {
                                var html = '';
                                <#--<@app.has_oper perm_code='2_6_1_7'>-->
                                    html += ' <a href="javascript:view(\'ID\')">查看</a>';
                                <#--</@app.has_oper>-->
                                    html += ' <a href="javascript:edit(\'ID\')">修改</a>';
                                return html.replace(/ID/g, row.id);
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

            var queryParams = {
                cellMfr: $('#cell_mfr').val(),
                cellModel: $('#cell_model').val(),
                barcode: $('#barcode').val()
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:800px;height:590px;',
                title: '电芯检验',
                href: "${contextPath}/security/hdg/battery_cell/add.htm",
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function view(id) {
            App.dialog.show({
                css: 'width:800px;height:590px;',
                title: '查看',
                href: "${contextPath}/security/hdg/battery_cell/view.htm?id=" + id
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:800px;height:590px;',
                title: '修改',
                href: "${contextPath}/security/hdg/battery_cell/edit.htm?id=" + id,
                event: {
                    onClose: function () {
                        reload();
                    }
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
                            <td align="right">&nbsp;&nbsp;条码编号：</td>
                            <td><input type="text" style="width:170px; height: 28px;" class="text"
                                       id="barcode"/>
                            </td>
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
                        <h3>电芯信息列表</h3>
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