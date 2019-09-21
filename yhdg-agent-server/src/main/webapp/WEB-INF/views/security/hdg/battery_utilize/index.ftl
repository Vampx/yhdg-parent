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
                url: "${contextPath}/security/hdg/battery_utilize/page.htm",
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
                            title: '电池编号',
                            align: 'center',
                            field: 'batteryId',
                            width: 40
                        },
                        {
                            title: '换电柜编号',
                            align: 'center',
                            field: 'cabinetId',
                            width: 40
                        },
                        {
                            title: '换电柜名称',
                            align: 'center',
                            field: 'cabinetName',
                            width: 40
                        },
                        {
                            title: '取电时间',
                            align: 'center',
                            field: 'takeTime',
                            width: 40
                        },
                        {
                            title: '还电时间',
                            align: 'center',
                            field: 'putTime',
                            width: 40
                        },
                        {
                            title: '利用率',
                            align: 'center',
                            field: 'utilize',
                            width: 40 ,
                            formatter: function (val) {
                            if (val != null && val != 0) {
                                return val+"%"
                            }
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
            var tree = $('#site_tree');
            var datagrid = $('#page_table');

            var batteryId = $('#battery_id').val();
            var cabinetId = $('#cabinet_id').val();
            var queryParams = {
                batteryId: batteryId,
                cabinetId: cabinetId
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
                            <td align="right" width="70">电池编号：</td>
                            <td><input type="text" class="text" id="battery_id"/></td>
                            <td align="right" width="70">站点编号：</td>
                            <td><input type="text" class="text" id="cabinet_id"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                        <h3>电池利用率统计</h3>
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

