<@app.html>
    <@app.head>

    <script>
        $(function () {
            $('#table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/basic/device/page.htm",
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
                            title: '类型',
                            align: 'center',
                            field: 'typeName',
                            width: 60
                        },
                        {
                            title: '设备ID',
                            align: 'center',
                            field: 'deviceId',
                            width: 60
                        },
                        {
                            title: '版本',
                            align: 'center',
                            field: 'version',
                            width: 60
                        },
                        {
                            title: '心跳时间',
                            align: 'center',
                            field: 'heartTime',
                            width: 40
                        }
                    ]
                ],
                onLoadSuccess: function () {
                    $('#table').datagrid('clearChecked');
                    $('#table').datagrid('clearSelections');
                }
            });
        });

        function query() {
            var datagrid = $('#table');
            var deviceId = $('#device_id').val();
            datagrid.datagrid('options').queryParams = {
                deviceId: deviceId
            };
            datagrid.datagrid('load');
        }

        function reload() {
            var datagrid = $('#table');
            datagrid.datagrid('reload');
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
                            <td align="right">设备ID：</td>
                            <td><input type="text" class="text" id="device_id"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>终端列表</h3>
                    </div>
                    <div class="grid">
                        <table id="table"></table>
                    </div>
                </div>
            </div>
        </div>
        </@app.container>
    </@app.body>
</@app.html>