<@app.html>
    <@app.head>
    <script>
        $(function() {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/hdg/battery_order_battery_report_log/page.htm?orderId=" + $('#order_id').val(),
                fitColumns: true,
                pageSize: 50,
                pageList: [50, 100],
                idField: 'orderId',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                columns: [
                    [
                        {
                            title: '订单id',
                            align: 'center',
                            field: 'orderId',
                            width: 25
                        },
                        {
                            title: '上报时间',
                            align: 'center',
                            field: 'reportTime',
                            width: 25
                        },
                        {
                            title: '当前电量',
                            align: 'center',
                            field: 'volume',
                            width: 25
                        },
                        {
                            title: '温度',
                            align: 'center',
                            field: 'temp',
                            width: 20
                        },
                        {
                            title: '经度',
                            align: 'center',
                            field: 'lng',
                            width: 20,
                            formatter: function (val, row) {
                                return val.toFixed(6)
                            }
                        },
                        {
                            title: '纬度',
                            align: 'center',
                            field: 'lat',
                            width: 25,
                            formatter: function (val, row) {
                                return val.toFixed(6)
                            }
                        },
                        {
                            title: '距离',
                            align: 'center',
                            field: 'distance',
                            width: 25
                        },
                        {
                            title: '当前信号',
                            align: 'center',
                            field: 'currentSignal',
                            width: 25
                        },
                        {
                            title: '地址',
                            align: 'center',
                            field: 'address',
                            width: 25
                        }
                    ]
                ],
                onLoadSuccess:function() {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                }
            });
        });

        function query() {
            var datagrid = $('#page_table');
            var queryParams = {
                orderId: $('#order_id').val()
            };
            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
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
                            <td align="right" width="80">换电订单id：</td>
                            <td><input type="text" class="text" id="order_id" maxlength="32" /></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                        <h3>换电电池上报记录</h3>
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

