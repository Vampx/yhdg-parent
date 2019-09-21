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
                url: "${contextPath}/security/hdg/back_battery_order/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            width: 40
                        },
                        {
                            title: '电池编号',
                            align: 'center',
                            field: 'batteryId',
                            width: 40
                        },
                        {
                            title: '所属换电柜',
                            align: 'center',
                            field: 'cabinetName',
                            width: 40
                        },
                        {
                            title: '客户名称',
                            align: 'center',
                            field: 'customerFullname',
                            width: 40
                        },
                        {
                            title: '电池类型',
                            align: 'center',
                            width: 40,
                            field: 'batteryType'
                        },
                        {
                            title: '电池箱号',
                            align: 'center',
                            field: 'boxNum',
                            width: 40
                        },
                        {
                            title: '订单状态',
                            align: 'center',
                            field: 'orderStatusName',
                            width: 40
                        },
                        {
                            title: '还电时间',
                            align: 'center',
                            field: 'backTime',
                            width: 40
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 40,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='hdg.BackBatteryOrder:view'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
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
        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');

            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();
            var orderStatus = $('#order_status').val();
            var id = $('#id').val();
            var batteryId = $('#battery_id').val();
            var queryParams = {
                orderStatus: orderStatus,
                id: id,
                batteryId: batteryId
            };
            queryParams[queryName] = queryValue;
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:786px;height:510px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/back_battery_order/view.htm?id=" + id

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
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">&nbsp;&nbsp;订单编号：</td>
                            <td><input type="text" class="text" id="id"/></td>
                            <td align="right" width="70">电池编号：</td>
                            <td><input type="text" class="text" id="battery_id"/></td>
                            <td align="right" width="50">状态：</td>
                            <td>
                                <select style="width:85px;" id="order_status">
                                    <option value="">所有</option>
                                    <#list OrderStatusEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>&nbsp;&nbsp;
                            </td>
                            <td align="right" width="90">
                                <select style="width:100px;" id="query_name">
                                    <option value="customerFullname">姓名</option>
                                    <option value="customerMobile">手机号</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"  style="width: 150px;"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                        <h3>退租电池订单</h3>
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

