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
                url: "${contextPath}/security/hdg/exchange_whitelist/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            width: 25
                        },
                        {
                            title: '姓名',
                            align: 'center',
                            field: 'fullname',
                            width: 20
                        },
                        {
                            title: '电池类型',
                            align: 'center',
                            field: 'typeName',
                            width: 20
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 30
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 20,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='hdg.ExchangeWhitelist:view'>
                                    html += ' <a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='hdg.ExchangeWhitelist:eidt'>
                                    html += ' <a href="javascript:edit(\'ID\')">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='hdg.ExchangeWhitelist:delete'>
                                    html += ' <a href="javascript:remove(\'ID\')">删除</a>';
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
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();

            var queryParams = {
            };
            queryParams[queryName] = queryValue;
            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:350px;height:330px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/exchange_whitelist/view.htm?id=" + id
            });
        }

        function add() {
            App.dialog.show({
                css: 'width:350px;height:350px;',
                title: '新建',
                href: "${contextPath}/security/hdg/exchange_whitelist/add.htm",
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:350px;height:350px;',
                title: '修改',
                href: "${contextPath}/security/hdg/exchange_whitelist/edit.htm?id=" + id,
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if(ok) {
                    $.post('${contextPath}/security/hdg/exchange_whitelist/delete_whitelist_customer.htm', {
                        id: id
                    }, function(json) {
                        if(json.success) {
                            $.messager.alert('提示信息', '删除成功', 'info');
                            reload();
                        }else {
                            $.messager.alert('提示信息', json.message, 'info');
                        }
                    }, 'json');
                }
            })

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
                            <td align="right" width="115">
                                <select style="width:100px;" id="query_name">
                                    <option value="fullname">姓名</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"  style="width: 170px;"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='hdg.ExchangeWhitelist:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>运营商白名单</h3>
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

