<@app.html>
    <@app.head>
    <script>
        $(function () {

            $('#page_table').datagrid({
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                fit: true,
                striped: true,
                singleSelect: true,
                collapsible: true,
                pagination: true,
                url: "${contextPath}/security/hdg/vehicle_report_log/page.htm",
                pageSize: 10,
                pageList: [10, 50, 100],
                idField: 'vehicleId',
                frozenColumns: [[
                    {
                        title: '车辆编号',
                        align: 'center',
                        field: 'vehicleId',
                        width: 100
                    },
                    {
                        title: '上报时间',
                        align: 'center',
                        field: 'reportTime',
                        width: 125
                    }
                ]],
                columns: [
                    [
                        {
                            title: '控制器版本',
                            align: 'center',
                            field: 'controllerVersion',
                            width: 80
                        },
                        {
                            title: '通信版本',
                            align: 'center',
                            field: 'communicationVersion',
                            width: 80
                        },
                        {
                            title: '波特率',
                            align: 'center',
                            field: 'baudRate',
                            width: 80
                        },
                        {
                            title: '状态1',
                            align: 'center',
                            field: 'status1',
                            width: 80
                        },
                        {
                            title: '状态2',
                            align: 'center',
                            field: 'status2',
                            width: 80
                        },
                        {
                            title: '状态3',
                            align: 'center',
                            field: 'status3',
                            width: 80
                        },
                        {
                            title: '状态4',
                            align: 'center',
                            field: 'status4',
                            width: 80
                        },
                        {
                            title: '电压百分比',
                            align: 'center',
                            field: 'voltagePercent',
                            width: 80
                        },
                        {
                            title: '限速比率',
                            align: 'center',
                            field: 'speedLimit',
                            width: 80
                        },
                        {
                            title: '霍尔转速',
                            align: 'center',
                            field: 'holzerSpeed',
                            width: 80
                        },
                        {
                            title: '电压',
                            align: 'center',
                            field: 'voltage',
                            width: 80,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) + "V";
                            }
                        },
                        {
                            title: '电流',
                            align: 'center',
                            field: 'electricity',
                            width: 80,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) + "A";
                            }
                        },
                        {
                            title: '防拆报警',
                            align: 'center',
                            field: 'dismantleFaultLogId',
                            width: 80
                        },
                        {
                            title: '控制指令',
                            align: 'center',
                            field: 'controlInstruction',
                            width: 80
                        }
                    ]
                ],
                onLoadSuccess: function () {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                }
            });
        });


        function selectQuery() {
            var vehicleId = $('#vehicle_id').val();
            $("#vehicle_tree").tree({
                url: '${contextPath}/security/hdg/vehicle_report_date/tree.htm?vehicleId=' + vehicleId,
                onLoadSuccess: function (data) {
                    eval(data)
                }

            });
            var datagrid = $('#page_table');

            datagrid.datagrid('options').queryParams = {
                vehicleId: vehicleId
            };
            datagrid.datagrid('load');
        }

        function view(batteryId, reportTime) {
            App.dialog.show({
                css: 'width:886px;height:647px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/vehicle_report_log/view.htm?batteryId=" + batteryId + "&reportTime=" + reportTime
            });
        }

        function tree_query() {
            var tree = $('#vehicle_tree');
            var reportTime = tree.tree('getSelected');
            if (reportTime) {
                reportTime = reportTime.id || '';
            } else {
                reportTime = '';
            }
            if (reportTime.length >= 10) {
                var datagrid = $('#page_table');
                var vehicleId = $('#vehicle_id').val();
                if (vehicleId != null) {
                    datagrid.datagrid('options').queryParams = {
                        vehicleId: vehicleId,
                        reportTime: reportTime + " 00:00:00"
                    };
                    datagrid.datagrid('load');
                }
            }
        }

        function selectVehicle() {
            App.dialog.show({
                css: 'width:826px;height:522px;overflow:visible;',
                title: '选择车辆',
                href: "${contextPath}/security/hdg/shop_vehicle/select_vehicle.htm",
                windowData: {
                    ok: function (config) {
                        $('#vehicle_id').val(config.vehicle.id)
                    }
                },
                event: {
                    onClose: function () {
                        selectQuery();
                    },
                    onLoad: function () {
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
                <div class="panel ztree_wrap">
                    <div class="ztree">
                        <div class="ztree_head">
                            <h3>上报日期</h3>
                        </div>
                        <div class="ztree_body easyui-tree" id="vehicle_tree"
                             data-options="
                                onBeforeSelect: App.tree.toggleSelect,
                                onClick: function(node) {
                                    tree_query();
                                }
                            ">
                        </div>
                    </div>
                </div>
                <div class="panel search" style="margin-left:230px;">
                    <div class="float_right">
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right" style="color: red">请选择车辆编号：</td>
                            <td>
                                <input type="text" class="text" id="vehicle_id" onclick="selectVehicle()"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-left:230px;">

                    <div class="toolbar clearfix">
                        <h3>车辆上报日志</h3>
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