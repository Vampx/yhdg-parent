<@app.html>
    <@app.head>
    <script>
        $(function () {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                fitColumns: true,
                pageSize: 10,
                url: "${contextPath}/security/hdg/unregister_vehicle/page.htm",
                pageList: [10, 50, 100],
                idField: 'code',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                columns: [
                    [
                        {
                            title: '所属运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 40
                        },
                        {
                            title: '车辆编号',
                            align: 'center',
                            field: 'id',
                            width: 50
                        },
                        {
                            title: '控制器版本',
                            align: 'center',
                            field: 'controllerVersion',
                            width: 35
                        },
                        {
                            title: '通信版本',
                            align: 'center',
                            field: 'communicationVersion',
                            width: 35
                        },
                        {
                            title: '波特率',
                            align: 'center',
                            field: 'baudRate',
                            width: 35
                        },
                        {
                            title: '状态1',
                            align: 'center',
                            field: 'status1',
                            width: 35
                        },
                        {
                            title: '状态2',
                            align: 'center',
                            field: 'status2',
                            width: 35
                        },
                        {
                            title: '状态3',
                            align: 'center',
                            field: 'status3',
                            width: 35
                        },
                        {
                            title: '状态4',
                            align: 'center',
                            field: 'status4',
                            width: 35
                        },
                        {
                            title: '电压百分比',
                            align: 'center',
                            field: 'voltagePercent',
                            width: 35
                        },
                        {
                            title: '限速比率',
                            align: 'center',
                            field: 'speedLimit',
                            width: 35
                        },
                        {
                            title: '霍尔转速',
                            align: 'center',
                            field: 'holzerSpeed',
                            width: 35
                        },
                        {
                            title: '电压',
                            align: 'center',
                            field: 'voltage',
                            width: 30,
                            formatter: function (val) {
                                  return Number(val / 100).toFixed(2) + "V";
                            }
                        },
                        {
                            title: '电流',
                            align: 'center',
                            field: 'electricity',
                            width: 30,
                            formatter: function (val) {
                                  return Number(val / 100).toFixed(2) + "A";
                            }
                        },
                        {
                            title: '防拆报警',
                            align: 'center',
                            field: 'dismantleFaultLogId',
                            width: 30
                        },
                        {
                            title: '控制指令',
                            align: 'center',
                            field: 'controlInstruction',
                            width: 30
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 40,
                            formatter: function(val, row) {
                                var html = '';
                                if (row.agentId == null) {
                                    <@app.has_oper perm_code='2_5_3_2'>
                                        html += ' <a href="javascript:bound_card(\'ID\')">设置运营商</a>';
                                    </@app.has_oper>
                                } else {
                                    html +='/';
                                }
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

        function bound_card(id) {
            App.dialog.show({
                css: 'width:340px;height:170px;',
                title: '设置运营商',
                href: "${contextPath}/security/hdg/unregister_vehicle/bound_agent.htm?id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }

        function query(){
            var id = $('#id').val();
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                id: id
            };
            datagrid.datagrid('load');
        }

        $(function () {
            $('#id').keydown(function (event) {
                if (event.keyCode == 13) {
                 query();
                }
            });
        })
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
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right" width="70">车辆编号：</td>
                            <td><input type="text" class="text" id="id"/>&nbsp;&nbsp;</td>
                            <td><button class="btn btn_red" onclick="query()">查询</button></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>未识别车辆列表</h3>
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