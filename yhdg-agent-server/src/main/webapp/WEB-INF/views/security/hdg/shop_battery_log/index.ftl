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
                url: "${contextPath}/security/hdg/shop_battery_log/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
                fitColumns: true,
                pageSize: 10,
                pageList: [10, 50, 100],
                idField: 'id',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                columns: [
                    [
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
                        {
                            title: '门店名称',
                            align: 'center',
                            field: 'shopName',
                            width: 60
                        },
                        {
                            title: '换电类型',
                            align: 'center',
                            field: 'category',
                            width: 60,
                            formatter: function (val) {
                                if(val == 1) {
                                    return '换电';
                                }else if(val == 2){
                                    return '租电';
                                }
                            }
                        },
                        {
                            title: '电池ID',
                            align: 'center',
                            field: 'batteryId',
                            width: 50
                        },
                        {
                            title: '类型',
                            align: 'center',
                            field: 'action',
                            width: 60,
                            formatter: function (val) {
                                if (val == 1) {
                                    return '收入';
                                } else if (val == 2) {
                                    return '支出';
                                }
                            }
                        },
                        {
                            title: '操作说明',
                            align: 'center',
                            field: 'memo',
                            width: 100
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
            var shopName = $('#shop_name').val();
            var batteryId = $('#battery_id').val();

            datagrid.datagrid('options').queryParams = {
                shopName: shopName,
                batteryId: batteryId
            };

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

                <div class="panel search" >
                    <div class="float_right">
                        <button class="btn btn_yellow"onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right" width="70">电池id：</td>
                            <td><input type="text" class="text" id="battery_id"  /></td>
                            <td align="right" width="70">门店名称：</td>
                            <td><input type="text" class="text" id="shop_name"  /></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>门店电池日志</h3>
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





