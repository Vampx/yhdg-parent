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
                url: "${contextPath}/security/hdg/insurance/page.htm",
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
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 40
                        },
                        {
                            title: '保险名称',
                            align: 'center',
                            field: 'insuranceName',
                            width: 40
                        },
                        {
                            title: '价格',
                            align: 'center',
                            field: 'price',
                            width: 40,
                            formatter: function (val) {
                                return val/100;
                            }
                        },
                        {
                            title: '保额',
                            align: 'center',
                            field: 'paid',
                            width: 40,
                            formatter: function (val) {
                                return val/100;
                            }
                        },
                        {
                            title: '时长',
                            align: 'center',
                            field: 'monthCount',
                            width: 40,
                            formatter: function (val) {
                                return val + "月";
                            }
                        },
                        {
                            title: '是否有效',
                            align: 'center',
                            field: 'isActive',
                            width: 40,
                            formatter: function(val) {
                                if(val == 1) {
                                    return '<a style="color: #00FF00;">是</a>';
                                }else{
                                    return '<a style="color: #ff0000;">否</a>';
                                }
                            }
                        },
                        {
                            title: '备注',
                            align: 'center',
                            field: 'memo',
                            width: 60
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 60,
                            formatter: function (val, row) {
                                var html = '';
                                    html += ' <a href="javascript:view(\'ID\')">查看</a>';
                                    html += ' <a href="javascript:edit(\'ID\')">修改</a>';
                                    html += ' <a href="javascript:remove(\'ID\')">删除</a>';
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
                agentId: $('#agent_id').combotree('getValue'),
                insuranceName: $('#insurance_name').val()
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:500px;height:462px;',
                title: '新建',
                href: "${contextPath}/security/hdg/insurance/add.htm",
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function view(id) {
            App.dialog.show({
                css: 'width:500px;height:465px;',
                title: '查看',
                href: "${contextPath}/security/hdg/insurance/view.htm?id=" + id
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:500px;height:462px;',
                title: '修改',
                href: "${contextPath}/security/hdg/insurance/edit.htm?id=" + id,
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }


        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/hdg/insurance/delete.htm?id=" + id, function (json) {
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
                            <td align="right">运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="false" style="width: 130px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                }
                            "
                                >
                            </td>
                            <td align="right">&nbsp;&nbsp;保险名称：</td>
                            <td><input type="text" style="width:170px; height: 28px;" class="text"
                                       id="insurance_name"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-top: 30px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <#--<@app.has_oper perm_code='2_6_1_6'>-->
                                <button class="btn btn_green" onclick="add()">新建</button>
                            <#--</@app.has_oper>-->
                        </div>
                        <h3>保险信息列表</h3>
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