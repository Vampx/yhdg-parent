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
                url: "${contextPath}/security/basic/device_report_log/page.htm",
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
                            title: '升级类型',
                            align: 'center',
                            field: 'deviceTypeName',
                            width: 60
                        },
                        {
                            title: '设备ID',
                            align: 'center',
                            field: 'deviceId',
                            width: 60
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 60
                        },
                        {title: '日志时间', align: 'center', field: 'logDate', width: 60},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 40,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.DeviceReportLog:download'>
                                    html += ' <a href="javascript:download(\'URL\')">下载</a>'
                                </@app.has_oper>
                                return html.replace(/URL/g, row.url);
                            }
                        }
                    ]
                ],
                onLoadSuccess: function () {
                    $('#table').datagrid('clearChecked');
                    $('#table').datagrid('clearSelections');
                }
            });
        });

        function reload() {
            var datagrid = $('#table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#table');
            var deviceId = $('#device_id').val();
            datagrid.datagrid('options').queryParams = {
                deviceId: deviceId
            };
            datagrid.datagrid('load');
        }

        function download(url) {
            console.log(url);
            document.location.href = "${staticUrl}" + url;
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
                        <h3>终端上报日志</h3>
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