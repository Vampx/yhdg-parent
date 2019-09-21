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
                url: "${contextPath}/security/hdg/battery_cell_model/page.htm",
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
                            title: '序号',
                            align: 'center',
                            field: 'id',
                            width: 40
                        },
                        {
                            title: '电芯厂家',
                            align: 'center',
                            field: 'cellMfr',
                            width: 80
                        },
                        {
                            title: '电芯型号',
                            align: 'center',
                            field: 'cellModel',
                            width: 60
                        },
                        {
                            title: '创建人',
                            align: 'center',
                            field: 'operator',
                            width: 60
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 60
                        },
                        {
                            title: '备注',
                            align: 'center',
                            field: 'memo',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 80,
                            formatter: function (val, row) {
                                var html = '';
                            <@app.has_oper perm_code='hdg.BatteryCellModel:view'>
                                html += '<a href="javascript:view(\'ID\')">查看</a>';
                            </@app.has_oper>
                            <@app.has_oper perm_code='hdg.BatteryCellModel:edit'>
                                html += ' <a href="javascript:edit(\'ID\')">修改</a>';
                            </@app.has_oper>
                            <@app.has_oper perm_code='hdg.BatteryCellModel:remove'>
                                html += ' <a href="javascript:remove(\'ID\')">删除</a>';
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

            var queryParams = {
                cellMfr: $('#cell_mfr').val(),
                cellModel: $('#cell_model').val()
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:400px;height:260px;',
                title: '新建',
                href: "${contextPath}/security/hdg/battery_cell_model/add.htm",
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:400px;height:260px;',
                title: '修改',
                href: "${contextPath}/security/hdg/battery_cell_model/edit.htm?id=" + id,
                event: {
                    onClose: function () {
                        reload();
                    },
                    onLoad: function () {
                    }
                }
            });
        }

        function view(id) {
            App.dialog.show({
                css: 'width:400px;height:245px;',
                title: '查看',
                href: "${contextPath}/security/hdg/battery_cell_model/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/hdg/battery_cell_model/delete.htm?id=" + id, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            reload();
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
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
                    <table cellpadding="0" cellspacing="0" border="0" style="border-collapse:separate; border-spacing: 0px 3px;">
                        <tr>
                            <td align="right">电芯厂家：</td>
                            <td><input type="text" style="width:170px; height: 28px;" class="text"
                                       id="cell_mfr"/>
                            </td>
                            <td align="right">&nbsp;&nbsp;电芯型号：</td>
                            <td><input type="text" style="width:170px; height: 28px;" class="text"
                                       id="cell_model"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-top: 30px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        <@app.has_oper perm_code='hdg.BatteryCellModel:add'>
                            <button class="btn btn_green" onclick="add()">新建</button>
                        </@app.has_oper>
                        </div>
                        <h3>电芯型号列表</h3>
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