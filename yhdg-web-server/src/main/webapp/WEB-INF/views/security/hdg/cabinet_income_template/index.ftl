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
                url: "${contextPath}/security/hdg/cabinet_income_template/page.htm",
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
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
                        {
                            title: '押金金额（元）',
                            align: 'center',
                            field: 'foregiftMoney',
                            width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '租金金额（元）',
                            align: 'center',
                            field: 'rentMoney',
                            width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '租金周期（月）',
                            align: 'center',
                            field: 'periodType',
                            width: 60
                        },
                        {title: '租金截至时间',
                            align: 'center',
                            field: 'rentExpireTime',
                            width: 60
                        },
                        {title: '是否免审',
                            align: 'center',
                            field: 'isReview',
                            width: 60,
                            formatter: function (val) {
                                if (val == 0) {
                                    return '是';
                                } else {
                                    return '否';
                                }
                            }
                        },
                        {title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 100,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='hdg.CabinetIncomeTemplate:view'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='hdg.CabinetIncomeTemplate:edit'>
                                    html += ' <a href="javascript:edit(\'ID\')">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='hdg.CabinetIncomeTemplate:remove'>
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
            var tree = $('#cabinet_tree');
            var datagrid = $('#page_table');
            var agentId = $('#agent_id').combotree('getValue');
            var cabinetName = $('#cabinet_name').val();
            var id = $('#id').val();
            datagrid.datagrid('options').queryParams = {
                cabinetName: cabinetName,
                id: id,
                agentId: agentId
            };

            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:786px;height:515px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/hdg/cabinet_income_template/add.htm",
                event: {
                    onClose: function () {
                        query();
                    }
                }
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:686px;height:230px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/hdg/cabinet_income_template/edit.htm?id=" + id,
                event: {
                    onClose: function () {
                        query();
                    },
                    onLoad: function () {
                    }
                }
            });
        }

        function view(id) {
            App.dialog.show({
                css: 'width:686px;height:230px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/cabinet_income_template/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post('${contextPath}/security/hdg/cabinet_income_template/delete.htm', {
                        id: id
                    }, function (json) {
                        if (json.success) {
                            $.messager.alert('提示信息', '操作成功', 'info');
                            query();
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
                <div class="panel search" >
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">运营商：</td>
                            <td >
                                <input id="agent_id" class="easyui-combotree" editable="true" style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:true,
                                multiple:false,
                                panelHeight:'200'
                            "
                                >
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" >
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='hdg.CabinetIncomeTemplate:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>换电柜收入分配模板</h3>
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





