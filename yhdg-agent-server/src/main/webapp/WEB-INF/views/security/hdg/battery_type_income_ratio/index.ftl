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
                url: "${contextPath}/security/hdg/battery_type_income_ratio/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            title: '所属运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
                        {
                            title: '电池类型',
                            align: 'center',
                            field: 'batteryTypeName',
                            width: 60
                        },
                        {
                            title: '平台比例',
                            align: 'center',
                            field: 'platformRatio',
                            width: 60
                        },
                        {
                            title: '运营商比例',
                            align: 'center',
                            field: 'agentRatio',
                            width: 60
                        },
                        {
                            title: '省代比例',
                            align: 'center',
                            field: 'provinceAgentRatio',
                            width: 60
                        },
                        {
                            title: '市代比例',
                            align: 'center',
                            field: 'cityAgentRatio',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '';
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
                                    html += ' <a href="javascript:remove(ID)">删除</a>';
                                return html.replace(/ID/g, row.id);
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
            var datagrid = $('#page_table');
            var agentId = $('#agent_id').combotree('getValue');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId
            };

            datagrid.datagrid('load');
        }

        function reloadTree() {
            var agentId = $('#agent_id').combotree('getValue');
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId
            };
            datagrid.datagrid('reload');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:580px;height:320px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/battery_type_income_ratio/view.htm?id=" + id
            });
        }

        function add() {
            App.dialog.show({
                css: 'width:580px;height:320px;overflow:visible;',
                title: '新建',
                href: '${contextPath}/security/hdg/battery_type_income_ratio/add.htm',
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:580px;height:320px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/hdg/battery_type_income_ratio/edit.htm?id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }

        function remove(id) {
            $.messager.confirm("提示信息", "确认删除?", function(ok) {
                if(ok) {
                    $.post("${contextPath}/security/hdg/battery_type_income_ratio/delete.htm?id=" + id, function (json){
                        if(json.success) {
                            $.messager.alert("提示信息", "删除成功", "info");
                            reload();
                        } else {
                            $.messager.alert("提示信息", json.message, "info")
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
                    <table cellpadding="0" cellspacing="0" border="0">
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
                                    panelHeight:'200',
                                    onClick: function(node) {
                                       reloadTree();
                                    }
                                "
                                >
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" >
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='2_2_7_2'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                         </div>
                    <h3>电池类型收入分配</h3>
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