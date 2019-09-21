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
                url: "${contextPath}/security/hdg/export_record/page.htm",
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
//                        {
//                            title: '运营商SN',
//                            align: 'center',
//                            field: 'agentCode',
//                            width: 60
//                        },
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
                        {
                            title: '电池编号',
                            align: 'center',
                            field: 'batteryId',
                            width: 60
                        },
                        {
                            title: 'IMEI',
                            align: 'center',
                            field: 'code',
                            width: 60
                        },
                        {
                            title: '电池外壳编号',
                            align: 'center',
                            field: 'shellCode',
                            width: 60
                        },
                        {
                            title: '柜子编号',
                            align: 'center',
                            field: 'cabinetId',
                            width: 60
                        },
                        {
                            title: '柜子名称',
                            align: 'center',
                            field: 'cabinetName',
                            width: 60
                        },
//                        {
//                            title: '操作人',
//                            align: 'center',
//                            field: 'operator',
//                            width: 60
//                        },
                        {
                            title: '发货时间',
                            align: 'center',
                            field: 'createTime',
                            width: 150
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 40,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='hdg.ExportRecord:view'>
                                    html += ' <a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='hdg.ExportRecord:remove'>
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
            var batteryQueryName = $('#battery_query_name').val();
            var batteryQueryValue = $('#battery_query_value').val();
            var cabinetQueryName = $('#cabinet_query_name').val();
            var cabinetQueryValue = $('#cabinet_query_value').val();
            var queryParams = {
                agentId: $('#agent_id').combotree('getValue')
            };
            queryParams[batteryQueryName] = batteryQueryValue;
            queryParams[cabinetQueryName] = cabinetQueryValue;
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:600px;height:450px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/export_record/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/hdg/export_record/delete.htm?id=" + id, function (json) {
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

        function download_template() {
            $.messager.confirm('提示信息', '确认下载模板?', function (ok) {
                if (ok) {
                    document.location.href = '${contextPath}/static/excel/ExportRecord.xls';
                }
            });
        }

        function import_battery_cell() {
            App.dialog.show({
                css: 'width:380px;height:185px;overflow:visible;',
                title: '上传文件',
                href: "${contextPath}/security/hdg/export_record/upload_file.htm",
                event: {
                    onClose: function () {
                        reload();
                    }
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
                            <td align="right">运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="false" style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200'">
                            </td>
                            <td align="right" width="85">
                                <select style="width:80px;" id="battery_query_name">
                                    <option value="batteryId">电池编号</option>
                                    <option value="shellCode">外壳编号</option>
                                    <option value="code">IMEI</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="battery_query_value"/></td>
                            <td align="right" width="85">
                                <select style="width:70px;" id="cabinet_query_name">
                                    <option value="cabinetId">柜子编号</option>
                                    <option value="cabinetName">柜子名称</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="cabinet_query_value"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-top: 30px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='hdg.ExportRecord:download'>
                                <button class="btn btn_green" onclick="download_template()">模板下载</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='hdg.ExportRecord:batchImport'>
                                <button class="btn btn_green" onclick="import_battery_cell()">批量导入</button>
                            </@app.has_oper>
                        </div>
                        <h3>发货管理</h3>
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