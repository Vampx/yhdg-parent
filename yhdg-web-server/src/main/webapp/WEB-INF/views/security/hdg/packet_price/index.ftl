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
                url: "${contextPath}/security/hdg/packet_price/page.htm",
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
                            title: '套餐名称',
                            align: 'center',
                            field: 'priceName',
                            width: 200
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 200
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 150,
                            formatter: function(val, row) {
                                var html = '<a href="javascript:view(ID)">查看</a>';
                                html += ' <a href="javascript:edit(ID)">修改</a>';
                                html += ' <a href="javascript:del(ID)">删除</a>';
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
            var priceName = $('#price_name').val();
            datagrid.datagrid('options').queryParams = {
                priceName: priceName,
                agentId: $('#agent_id').combotree('getValue')
            };
            datagrid.datagrid('load');
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:786px;height:515px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/hdg/packet_price/edit.htm?id=" + id,
                event: {
                    onClose: function() {
                        var datagrid = $('#page_table');
                        datagrid.datagrid('reload');
                    },
                    onLoad: function() {
                    }
                }
            });
        }

        function del(id) {
            $.messager.confirm('提示信息', '确认删除?', function(ok) {
                if (ok) {
                    $.post("${contextPath}/security/hdg/packet_price/delete.htm?id=" + id, function (json) {
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
        function view(id) {
            App.dialog.show({
                css: 'width:786px;height:515px;',
                title: '查看',
                href: "${contextPath}/security/hdg/packet_price/view.htm?id=" + id,
                event: {
                    onClose: function() {
                    }
                }
            });
        }

        function add() {
            App.dialog.show({
                css: 'width:400px;height:240px;',
                title: '新建',
                href: "${contextPath}/security/hdg/packet_price/add.htm",
                event: {
                    onClose: function() {
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
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">运营商：</td>
                            <td >
                                <input id="agent_id" class="easyui-combotree" editable="false" style="height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto'
                            "
                                >
                            </td>
                            <td align="right" width="95">套餐名称：</td>
                            <td><input type="text" class="text" id="price_name"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <button class="btn btn_green" onclick="add()">新建</button>
                        </div>
                        <h3>包时段套餐</h3>
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
