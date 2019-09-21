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
                url: "${contextPath}/security/hdg/battery_ride_order/page.htm",
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
                            title: '订单编号',
                            align: 'center',
                            field: 'id',
                            width: 30
                        },
                        {
                            title: '电池编号',
                            align: 'center',
                            field: 'batteryId',
                            width: 30
                        },
                        {
                            title: '骑行里程',
                            align: 'center',
                            field: 'currentDistance',
                            width: 25,
                            formatter: function (val, row) {
                                if(val>=1000){
                                    return val/1000+"km"
                                }
                                return val+"m"
                            }
                        },
                        {
                            title: '取电柜',
                            align: 'center',
                            field: 'takeCabinetId',
                            width: 25
                        },
                        {
                            title: '取电柜名称/箱号',
                            align: 'center',
                            field: 'takeCabinetName',
                            width: 60,
                            formatter: function (val, row) {
                                var takeCabinetName = (row.takeCabinetName == null || row.takeCabinetName == '') ? '' : row.takeCabinetName;
                                var takeBoxNum = (row.takeBoxNum == null || row.takeBoxNum == '') ? '' : row.takeBoxNum;
                                return '' + takeCabinetName + '/' + takeBoxNum + '';
                            }
                        },
                        {
                            title: '取电时间',
                            align: 'center',
                            field: 'takeTime',
                            width: 30
                        },
                        {
                            title: '放电柜',
                            align: 'center',
                            field: 'putCabinetId',
                            width: 25
                        },
                        {
                            title: '放电柜名称/箱号',
                            align: 'center',
                            field: 'putCabinetName',
                            width: 60,
                            formatter: function (val, row) {
                                var putCabinetName = (row.putCabinetName == null || row.putCabinetName == '') ? '' : row.putCabinetName;
                                var putBoxNum = (row.putBoxNum == null || row.putBoxNum == '') ? '' : row.putBoxNum;
                                return '' + putCabinetName  + '/' + putBoxNum + '';
                            }
                        },
                        {
                            title: '放电时间',
                            align: 'center',
                            field: 'putTime',
                            width: 30
                        },
                        {
                            title: '电量',
                            align: 'center',
                            field: 'currentVolume',
                            width: 40,
                            formatter: function (val, row) {
                                return '' + row.initVolume + '->' + row.currentVolume + '';
                            }
                        },
                        {
                            title: '客户姓名',
                            align: 'center',
                            field: 'customerFullname',
                            width: 20
                        },
                        {
                            title: '手机号',
                            align: 'center',
                            field: 'customerMobile',
                            width: 35
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 40,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='2_6_2_2'>
                                    html += '<a href="javascript:view(\'ID\')">查看&nbsp;&nbsp;</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='2_6_2_3'>
                                    html += '<a href="javascript:view_map(\'ID\')">轨迹&nbsp;&nbsp;</a>';
                                </@app.has_oper>
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

            var id = $('#id').val();
            var batteryId = $('#battery_id').val();
            var customerName = $('#customer_name').val();
            var mobile = $('#mobile').val();

            var queryParams = {
                id: id,
                batteryId: batteryId,
                customerFullname: customerName,
                customerMobile: mobile
            };

            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:786px;height:520px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/battery_ride_order/view.htm?id=" + id

            });
        }
        function view_map(id) {
            $.post("${contextPath}/security/hdg/battery_order_battery_report_log/find_all_map_count.htm", {orderId: id}, function (json) {
                if (!json.success) {
                    $.messager.alert('提示信息', '该时段电池未上报信息', 'info');
                    return
                }
                App.dialog.show({
                    css: 'width:1286px;height:700px;overflow:visible;',
                    title: '运行轨迹',
                    href: "${contextPath}/security/hdg/battery_ride_order/view_map.htm?id=" + id
                });
            }, 'json');
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
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">订单编号：</td>
                            <td><input type="text" class="text" id="id"/></td>
                            <td align="right" width="70">电池编号：</td>
                            <td><input type="text" class="text" id="battery_id"/></td>
                            <td align="right" width="70">客户姓名：</td>
                            <td><input type="text" class="text" id="customer_name"/></td>
                            <td align="right" width="70">手机号码：</td>
                            <td><input type="text" class="text" id="mobile"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                        <h3>电池骑行记录列表</h3>
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

