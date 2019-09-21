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
                url: "${contextPath}/security/hdg/keep_take_order/page.htm",
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
                            title: '订单编号',
                            align: 'center',
                            field: 'id',
                            width: 60
                        },
                        {title: '换电柜编号', align: 'center', field: 'cabinetId', width: 60},
                        {title: '换电柜名称', align: 'center', field: 'cabinetName', width: 60},
                        {
                            title: '订单数量',
                            align: 'center',
                            field: 'orderCount',
                            width: 60
                        },
                        {
                            title: '最后投电时间',
                            align: 'center',
                            field: 'lastTime',
                            width: 60
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 60,
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='2_7_5_2'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
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
            var cabinetId = $('#cabinet_id').val();
            var cabinetName = $('#cabinet_name').val();
            var id = $('#id').val();
            datagrid.datagrid('options').queryParams = {
                cabinetId: cabinetId,
                agentId: agentId,
                cabinetName: cabinetName,
                id: id
            };

            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:786px;height:515px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/keep_take_order/view.htm?id=" + id
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
                        <button class="btn btn_yellow"onclick="query()">搜索</button>
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
                                panelHeight:'200'
                            "
                                >
                            </td>
                            <td align="right">&nbsp;&nbsp;订单编号：</td>
                            <td><input type="text" class="text" id="id"/></td>
                            <td align="right" width="80">换电柜编号：</td>
                            <td><input type="text" class="text" id="cabinet_id"  /></td>
                            <td align="right" width="80">换电柜名称：</td>
                            <td><input type="text" class="text" id="cabinet_name"  /></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                        <h3>收电订单列表</h3>
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