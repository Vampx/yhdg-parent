<@app.html>
    <@app.head>
    <script>
        $(function() {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                nowrap: false,
                striped: true,
                pagination: true,
                url: "${contextPath}/security/hdg/battery_cell_format/page.htm",
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
                            width: 13
                        },
                        {
                            title: '电芯厂家',
                            align: 'center',
                            field: 'cellMfr',
                            width: 30
                        },
                        {
                            title: '电芯型号',
                            align: 'center',
                            field: 'cellModel',
                            width: 30
                        },
                        {
                            title: '充电截至电压(V)',
                            align: 'center',
                            field: 'chgCutVol',
                            width: 30
                        },
                        {
                            title: '标称电压(V)',
                            align: 'center',
                            field: 'nominalVol',
                            width: 30
                        },
                        {
                            title: '组包容量(Ah)',
                            align: 'center',
                            field: 'nominalCap',
                            width: 30,
                            formatter: function (val, row) {
                                var nominalCap = val;
                                var minNominalCap = row.minNominalCap;
                                var maxNominalCap = row.maxNominalCap;
                                (nominalCap == null) ? nominalCap = '' : nominalCap = nominalCap;
                                (minNominalCap == null) ? minNominalCap = '-0' : minNominalCap = "-" + minNominalCap;
                                (maxNominalCap == null) ? maxNominalCap = '+0' : maxNominalCap = "+" + maxNominalCap;
                                return "标准值:"+ nominalCap + "<br>偏差:" + minNominalCap + "," + maxNominalCap;
                            }
                        },
                        {
                            title: '交流内阻(mΩ)',
                            align: 'center',
                            field: 'acResistance',
                            width: 30,
                            formatter: function (val, row) {
                                var acResistance = val;
                                var minAcResistance = row.minAcResistance;
                                var maxAcResistance = row.maxAcResistance;
                                (acResistance == null) ? acResistance = '' : acResistance = acResistance;
                                (minAcResistance == null) ? minAcResistance = '-0' : minAcResistance = "-" + minAcResistance;
                                (maxAcResistance == null) ? maxAcResistance = '+0' : maxAcResistance = "+" + maxAcResistance;
                                return "标准值:"+ acResistance + "<br>偏差:" + minAcResistance + "," + maxAcResistance;
                            }
                        },
                        {
                            title:'回弹电压(V)',
                            align: 'center',
                            field:'resilienceVol',
                            width: 30,
                            formatter: function (val, row) {
                                var resilienceVol = val;
                                var minResilienceVol = row.minResilienceVol;
                                var maxResilienceVol = row.maxResilienceVol;
                                (resilienceVol == null) ? resilienceVol = '' : resilienceVol = resilienceVol;
                                (minResilienceVol == null) ? minResilienceVol = '-0' : minResilienceVol = "-" + minResilienceVol;
                                (maxResilienceVol == null) ? maxResilienceVol = '+0' : maxResilienceVol = "+" + maxResilienceVol;
                                return "标准值:"+ resilienceVol + "<br>偏差:" + minResilienceVol + "," + maxResilienceVol;
                            }
                        },
                        {
                            title: '静置电压(V)',
                            align: 'center',
                            field: 'staticVol',
                            width: 30,
                            formatter: function (val, row) {
                                var staticVol = val;
                                var minStaticVol = row.minStaticVol;
                                var maxStaticVol = row.maxStaticVol;
                                (staticVol == null) ? staticVol = '' : staticVol = staticVol;
                                (minStaticVol == null) ? minStaticVol = '-0' : minStaticVol = "-" + minStaticVol;
                                (maxStaticVol == null) ? maxStaticVol = '+0' : maxStaticVol = "+" + maxStaticVol;
                                return "标准值:"+ staticVol + "<br>偏差:" + minStaticVol + "," + maxStaticVol;
                            }
                        },
                        {
                            title: '循环寿命',
                            align: 'center',
                            field: 'circle',
                            width: 30,
                            formatter: function (val, row) {
                                var circle = val;
                                var minCircle = row.minCircle;
                                var maxCircle = row.maxCircle;
                                (circle == null) ? circle = '' : circle = circle;
                                (minCircle == null) ? minCircle = '-0' : minCircle = "-" + minCircle;
                                (maxCircle == null) ? maxCircle = '+0' : maxCircle = "+" + maxCircle;
                                return "标准值:"+ circle + "<br>偏差:" + minCircle + "," + maxCircle;
                            }
                        },
                        {
                            title: '生成规则',
                            align: 'center',
                            field: 'barcodeRule',
                            width: 20
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 70,
                            formatter: function(val, row) {
                                var html = '';
                                    <#--<@app.has_oper perm_code='10_1_1_3'>-->
                                        html += ' <a href="javascript:view(\'ID\')">查看</a>';
                                    <#--</@app.has_oper>-->
                                    <#--<@app.has_oper perm_code='10_1_1_4'>-->
                                        html += ' <a href="javascript:edit(\'ID\')">修改</a>';
                                    <#--</@app.has_oper>-->
                                    <#--<@app.has_oper perm_code='10_1_1_5'>-->
                                        html += ' <a href="javascript:remove(\'ID\')">删除</a>';
                                    <#--</@app.has_oper>-->
                                    <#--<@app.has_oper perm_code='10_1_1_6'>-->
                                        html += ' <a href="javascript:cellBarcode(\'ID\')">生成条码</a>';
                                    <#--</@app.has_oper>-->
                                    <#--<@app.has_oper perm_code='10_1_1_7'>-->
                                        html += ' <a href="javascript:viewCellBarcode(\'ID\')">查看条码</a>';
                                    <#--</@app.has_oper>-->
                                return html.replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                onLoadSuccess:function() {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                }
            });
        });

        function cellBarcode(id) {
            App.dialog.show({
                css: 'width:750px;height:558px;',
                title: '生成条码',
                href: "${contextPath}/security/hdg/battery_cell_format/select_battery_cell_barcode.htm?id="+id,
                windowData: {
                    ok: function(rows) {
                    }
                }
            });
        }

        function viewCellBarcode(id) {
            App.dialog.show({
                css: 'width:750px;height:516px;',
                title: '查看条码',
                href: "${contextPath}/security/hdg/battery_cell_format/view_battery_cell_barcode.htm?id="+id,
                windowData: {
                    ok: function(rows) {
                    }
                }
            });
        }

        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');

            var queryParams = {
                cellMfr: $('#cell_mfr').val(),
                cellModel: $('#cell_model').val()
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }


        function view(id) {
            App.dialog.show({
                css: 'width:710px;height:570px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/battery_cell_format/view.htm?id=" + id
            });
        }

        function add() {
            App.dialog.show({
                css: 'width:710px;height:575px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/hdg/battery_cell_format/add.htm",
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:710px;height:575px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/hdg/battery_cell_format/edit.htm?id="+id,
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function(ok) {
                if(ok) {
                    $.post('${contextPath}/security/hdg/battery_cell_format/delete.htm?id=' + id , function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '删除成功', 'info');
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

                <div class="panel search" >
                    <div class="float_right">
                        <button class="btn btn_yellow"onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">电芯厂家：</td>
                            <td><input type="text" style="width:170px; height: 28px;" class="text"
                                       id="cell_mfr"/>
                            </td>
                            <td align="right">&nbsp;&nbsp;电芯型号：</td>
                            <td><input type="text" style="width:170px; height: 28px;" class="text"
                                       id="cell_model"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <#--<@app.has_oper perm_code="10_1_1_2">-->
                                <button class="btn btn_green" onclick="add()">新建</button>
                            <#--</@app.has_oper>-->
                        </div>
                        <h3>电芯规格列表</h3>
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





