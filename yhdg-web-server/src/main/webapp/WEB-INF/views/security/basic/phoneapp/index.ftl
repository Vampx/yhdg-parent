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
                url: "${contextPath}/security/basic/phoneapp/page.htm",
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
                            title: 'ID',
                            align: 'center',
                            field: 'id',
                            width: 60
                        },
                        {
                            title: '商户名称',
                            align: 'center',
                            field: 'partnerName',
                            width: 60
                        },
                        {
                            title: 'APP名称',
                            align: 'center',
                            field: 'appName',
                            width: 60
                        },
                        {
                            title: '认证方式',
                            align: 'center',
                            field: 'authType',
                            width: 60,
                            formatter: function (val) {
                                if (val == 1) {
                                    return '自动认证';
                                }else if (val == 2) {
                                    return '人工认证';
                                }
                            }
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.Phoneapp:view'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.Phoneapp:edit'>
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.Phoneapp:remove'>
                                    html += ' <a href="javascript:remove(ID)">删除</a>';
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

            var partnerId = $('[name=partnerId]').val();

            var queryParams = {
                partnerId: partnerId
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }
        function add() {
            App.dialog.show({
                css: 'width:900px;height:620px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/basic/phoneapp/add.htm",
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }
        function edit(id) {
            App.dialog.show({
                css: 'width:900px;height:620px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/phoneapp/edit.htm?id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    },
                    onLoad: function() {
                    }
                }
            });
        }
        function view(id) {
            App.dialog.show({
                css: 'width:900px;height:620px;soverflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/phoneapp/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function(ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/phoneapp/delete.htm?id=" + id, function (json) {
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
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td align="right" >商户名称：</td>
                                <td>
                                    <input name="partnerId" id="partner_id" style="width: 182px; height: 30px;" class="easyui-combobox"
                                           data-options="url:'${contextPath}/security/basic/partner/list.htm?dummy=${'无'?url}',
                                        method:'get',
                                        valueField:'id',
                                        textField:'partnerName',
                                        editable:false,
                                        multiple:false"
                                    />
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="panel grid_wrap">
                        <div class="toolbar clearfix">
                            <div class="float_right">
                                <@app.has_oper perm_code='basic.Phoneapp:add'>
                                    <button class="btn btn_green" onclick="add()">新建</button>
                                </@app.has_oper>
                            </div>
                            <h3>APP列表</h3>
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