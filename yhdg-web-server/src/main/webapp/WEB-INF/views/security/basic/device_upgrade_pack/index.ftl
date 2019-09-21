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
                url: "${contextPath}/security/basic/device_upgrade_pack/page.htm?moduleId=${Session['SESSION_KEY_USER'].moduleId}",
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
                            title: '升级名称',
                            align: 'center',
                            field: 'upgradeName',
                            width: 60
                        },
                        {
                            title: '文件名称',
                            align: 'center',
                            field: 'fileName',
                            width: 60
                        },
                        {
                            title: '版本',
                            align: 'center',
                            field: 'version',
                            width: 80,
                            formatter: function (val, row) {
                                return row.oldVersion + '->' + row.newVersion;
                            }
                        },
                        {
                            title: '修改时间',
                            align: 'center',
                            field: 'updateTime',
                            width: 60
                        },
                        {title: '备注', align: 'center', field: 'memo', width: 100},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 100,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.DeviceUpgradePack:view'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.DeviceUpgradePack:edit'>
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.DeviceUpgradePack:download'>
                                    html += ' <a href="javascript:download(ID)">下载</a>'
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.DeviceUpgradePack:remove'>
                                    html += ' <a href="javascript:remove(ID)">删除</a>'
                                </@app.has_oper>
                                  <@app.has_oper perm_code='basic.DeviceUpgradePack:facility'>
                                    html += ' <a href="javascript:facility(ID)">设备</a>'
                                  </@app.has_oper>
                                return html.replace(/ID/g, row.id).replace(/FILEPATH/g, "'" + row.filePath + "'");
                            }
                        }
                    ]
                ],
                onLoadSuccess: function () {
                    $('#table').datagrid('clearChecked');
                    $('#table').datagrid('clearSelections');
                }
            });
        })

        function add() {
            App.dialog.show({
                css: 'width:480px;height:430px;',
                title: '新建',
                href: "${contextPath}/security/basic/device_upgrade_pack/add.htm",
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
            var roleName = $('#upgrade_name').val();
            datagrid.datagrid('options').queryParams = {
                roleName: roleName
            };
            datagrid.datagrid('load');
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:500px;height:430px;',
                title: '修改',
                href: "${contextPath}/security/basic/device_upgrade_pack/edit.htm?id=" + id,
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

        function view(id) {
            App.dialog.show({
                css: 'width:480px;height:350px;',
                title: '查看',
                href: "${contextPath}/security/basic/device_upgrade_pack/view.htm?id=" + id
            });
        }

        function download(id) {
            document.location.href = "${contextPath}/security/basic/device_upgrade_pack/download.htm?id=" + id;
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post('${contextPath}/security/basic/device_upgrade_pack/delete.htm',
                            {
                                id: id
                            }, function (json) {
                                if (json.success) {
                                    var datagrid = $('#table');
                                    datagrid.datagrid('reload');
                                    $.messager.alert('提示消息', '操作成功', 'info');
                                } else {
                                    $.messager.alert('提示消息', json.message, 'info');
                                }
                            }, 'json');
                }
            });
        }

        function facility(id) {
            App.dialog.show({
                css: 'width:800px;height:540px;',
                title: '设备',
                href: "${contextPath}/security/basic/device_upgrade_pack_detail/index.htm?packId=" + id,
                event: {
                    onClose: function () {
                        var datagrid = $('#table');
                        datagrid.datagrid('reload');
                    },
                    onLoad: function () {
                    }
                },
                windowData: {
                    packId: id
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
                <div class="panel search">
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">名称：</td>
                            <td><input type="text" class="text" id="upgrade_name"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='basic.DeviceUpgradePack:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>终端升级</h3>
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