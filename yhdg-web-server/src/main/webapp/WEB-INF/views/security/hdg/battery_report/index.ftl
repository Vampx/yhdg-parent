<@app.html>
    <@app.head>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=1.5&ak=${controller.appConfig.mapKey}"></script>
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
                url: "${contextPath}/security/hdg/battery_report/page.htm",
                pageSize: 10,
                pageList: [10, 50, 100],
                idField: 'batteryId',
                frozenColumns: [[
                    {
                        title: '电池编号',
                        align: 'center',
                        field: 'batteryId',
                        width: 100
                    },
                    {
                        title: '上报时间',
                        align: 'center',
                        field: 'createTime',
                        width: 140
                    },
                ]],
                columns: [
                    [
                        {
                            title: '电压',
                            align: 'center',
                            field: 'voltage',
                            width: 50,
                            formatter: function (val) {
                                return Number(val / 1000).toFixed(2) + "V";
                            }
                        },
                        {
                            title: '电流',
                            align: 'center',
                            field: 'electricity',
                            width: 50,
                            formatter: function (val) {
                                return Number(val / 1000).toFixed(2) + "A";
                            }
                        },
                        {
                            title: '剩余电量',
                            align: 'center',
                            field: 'volume',
                            width: 60,
                            formatter: function (val) {
                                if(val == null){
                                    return "";
                                }
                                return  val  + "%";
                            }
                        },
                        {
                            title: '温度',
                            align: 'center',
                            field: 'temp',
                            width: 60
                        },
                        {
                            title: '经度/纬度',
                            align: 'center',
                            field: 'lat',
                            width: 80,
                            formatter: function(val, row) {
                                var lng = row.lng;
                                var lat = row.lat;
                                if(lng == null && lat == null){
                                    return "";
                                }
                                return Number(row.lng).toFixed(6) + "/" + Number(row.lat).toFixed(6);
                            }
                        },
                        {
                            title: '位置类型',
                            align: 'center',
                            field: 'locTypeName',
                            width: 60
                        },
                        {
                            title: '信号',
                            align: 'center',
                            field: 'currentSignal',
                            width: 50
                        },
                        {
                            title: '类型',
                            align: 'center',
                            field: 'heartTypeName',
                            width: 50
                        },
                        {
                            title: '位置',
                            align: 'center',
                            field: 'address',
                            width: 180
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 150,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='2_6_6_4'>
                                    html += '<a href="javascript:view(\'ID\',\'ReportTime\')">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='2_6_6_5'>
                                    html += ' <a style="color: blue;" href="javascript:getLocation(\'lng\',\'lat\')">获取当前位置</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.batteryId).replace(/ReportTime/g, row.createTime).replace(/lng/g, row.lng).replace(/lat/g, row.lat);
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

        var myGeo = new BMap.Geocoder();

        function getLocation(lng, lat) {
            if(lng == null || lat == null || lng == 'null'  || lat == 'null'){
                $.messager.alert('提示信息', '经纬度不能为空', 'info');
                return;
            }
            myGeo.getLocation(new BMap.Point(lng, lat), function (result) {
                if (!result) {
                    alert("获取失败");
                } else {
                    var address = result.address;
                    var datagrid = $('#page_table');
                    var row = datagrid.datagrid('getSelected');
                    if (row) {
                        var index = datagrid.datagrid('getRowIndex', row);
                        datagrid.datagrid('updateRow', {index: index, row: {address: address}});
                    }
                }
            });
        }

        function selectQuery() {
            var batteryId = $('#battery_id').val();
            $("#battery_tree").tree({
                url: '${contextPath}/security/hdg/battery_report_date/tree.htm?batteryId=' + batteryId,
                onLoadSuccess: function (data) {
                    eval(data)
                }

            });
            var datagrid = $('#page_table');

            datagrid.datagrid('options').queryParams = {
                batteryId: batteryId
            };
            datagrid.datagrid('load');
        }

        function view(batteryId, createTime) {
            App.dialog.show({
                css: 'width:886px;height:647px;overflow:scroll;',
                title: '查看',
                href: "${contextPath}/security/hdg/battery_report/view.htm?batteryId=" + batteryId + "&createTime=" + createTime
            });
        }

        function export_excel() {
            var tree = $('#battery_tree');
            var batteryId = $('#battery_id').val();
            if(batteryId == null || batteryId =='') {
                $.messager.alert('提示信息', '请选择电池', 'info');
                return false;
            }
            var createTime = tree.tree('getSelected');
            if (createTime) {
                createTime = createTime.id || '';
            } else {
                createTime = '';
            }
            if(createTime == '' || createTime.length < 10) {
                $.messager.alert('提示信息', '请选择年月日', 'info');
                return false;
            }
            $.messager.confirm('提示信息', '确认导出数据?', function (ok) {
                if (ok) {
                    var queryParams = {
                        batteryId: batteryId,
                        createTime: createTime
                    };
                    $.post('${contextPath}/security/hdg/battery_report/export_excel.htm', queryParams, function (json) {
                        $.messager.progress('close');
                        if (json.success) {
                            document.location.href = '${contextPath}/security/hdg/battery_report/download.htm?filePath=' + json.data[0] + "&formatDate=" + json.data[1] + "&batteryId=" + json.data[2];
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        }

        function showLineChart() {
            var tree = $('#battery_tree');
            var batteryId = $('#battery_id').val();
            if(batteryId == null || batteryId =='') {
                $.messager.alert('提示信息', '请选择电池', 'info');
                return false;
            };
            var createTime = tree.tree('getSelected');
            if (createTime) {
                createTime = createTime.id || '';
            } else {
                createTime = '';
            }
            if(createTime == '' || createTime.length < 10) {
                $.messager.alert('提示信息', '请选择年月日', 'info');
                return false;
            }
            App.dialog.show({
                css: 'width:1200px;height:620px;overflow:visible;',
                title: '查看',
                href: '${contextPath}/security/hdg/battery_report/show_line_chart.htm?batteryId=' + batteryId + "&createTime=" + createTime
            });
        }

        function tree_query() {
            var tree = $('#battery_tree');
            var createTime = tree.tree('getSelected');
            if (createTime) {
                createTime = createTime.id || '';
            } else {
                createTime = '';
            }
            if (createTime.length >= 10) {
                var datagrid = $('#page_table');
                var batteryId = $('#battery_id').val();
                if (batteryId != null) {
                    datagrid.datagrid('options').queryParams = {
                        batteryId: batteryId,
                        createTime: createTime + " 00:00:00"
                    };
                    datagrid.datagrid('load');
                }
            }
        }

        function selectBattery() {
            App.dialog.show({
                css: 'width:826px;height:522px;overflow:visible;',
                title: '选择电池',
                href: "${contextPath}/security/hdg/battery/select_battery.htm",
                windowData: {
                    ok: function (config) {
                        $('#battery_id').val(config.battery.id)
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
                        <div class="ztree_body easyui-tree" id="battery_tree"
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
                            <td align="right" style="color: red">请选择电池编号：</td>
                            <td>
                                <input type="text" class="text" id="battery_id" onclick="selectBattery()"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-left:230px;">

                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='2_6_6_2'>
                                <button class="btn btn_red" onclick="export_excel()">批量导出</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='2_6_6_3'>
                                <button class="btn btn_green" onclick="showLineChart()">查看折线图</button>
                            </@app.has_oper>
                        </div>
                        <h3>电池上报日志</h3>
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