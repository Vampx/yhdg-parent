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
                url: "${contextPath}/security/basic/device_command/page.htm",
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
                            title: '类型',
                            align: 'center',
                            field: 'typeName',
                            width: 60
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 60
                        },
                        {
                            title: '下发时间',
                            align: 'center',
                            field: 'dispatchTime',
                            width: 60
                        },
                        {title: '日志时间', align: 'center', field: 'logDate', width: 60}
                    ]
                ],
                onLoadSuccess: function () {
                    $('#table').datagrid('clearChecked');
                    $('#table').datagrid('clearSelections');
                }
            });
        });

        function add() {
            App.dialog.show({
                css: 'width:850px;height:580px;',
                title: '新建',
                href: "${contextPath}/security/basic/device_command/add.htm",
                event: {
                    onClose: function () {
                        var datagrid = $('#table');
                        datagrid.datagrid('reload');
                    },
                    onLoad: function () {
                    }
                }
            });
        }

        function reload() {
            var datagrid = $('#table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#table');
            datagrid.datagrid('options').queryParams = {
                deviceId: $('#device_id').val(),
                type: $('#type').val(),
                status: $('#status').val()
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
                <div class="panel search">
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">设备ID：</td>
                            <td><input type="text" class="text" id="device_id"/></td>
                            <td align="right">类型：</td>
                            <td>
                                <select style="width:70px;" id="type">
                                    <option value="">所有</option>
                                    <#list typeList as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right">状态：</td>
                            <td>
                            <td>
                                <select style="width:70px;" id="status">
                                    <option value="">所有</option>
                                    <#list statusList as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='basic.DeviceCommand:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>终端命令</h3>
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