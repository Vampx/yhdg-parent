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
                url: "${contextPath}/security/basic/agent_company/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            width: 80
                        },
                        {
                            title: '运营公司编号',
                            align: 'center',
                            field: 'id',
                            width: 80
                        },
                        {
                            title: '运营公司名称',
                            align: 'center',
                            field: 'companyName',
                            width: 60
                        },
                        {title: '地址', align: 'center', field: 'address', width: 60},
                        {title: '收款人姓名', align: 'center', field: 'payPeopleName', width: 60},
                        {title: '收款人手机', align: 'center', field: 'payPeopleMobile', width: 60},
                        {
                            title: '是否启用',
                            align: 'center',
                            field: 'activeStatus',
                            width: 30,
                            formatter: function(row) {
                                if(row == 1) {
                                    return '<a style="color: #00FF00;">是</a>';
                                }else{
                                    return '<a style="color: #ff0000;">否</a>';
                                }
                            }
                        },
                        {title: '营业时间', align: 'center', field: 'workTime', width: 40},
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
                            width: 80,
                            formatter: function (val, row) {
                                var html = '';
                            <@app.has_oper perm_code='basic.AgentCompany:view'>
                                html += '<a href="javascript:view(\'ID\')">查看</a>';
                            </@app.has_oper>
                            <@app.has_oper perm_code='basic.AgentCompany:edit'>
                                html += ' <a href="javascript:edit(\'ID\')">修改</a>';
                            </@app.has_oper>
                            <@app.has_oper perm_code='basic.AgentCompany:remove'>
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
                companyName: $('#company_name').val()
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:950px;height:600px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/basic/agent_company/add.htm",
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:1050px;height:650px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/agent_company/edit.htm?id=" + id,
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
                css: 'width:1050px;height:650px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/agent_company/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/agent_company/delete.htm?id=" + id, function (json) {
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
                            <td align="right" width="100">运营公司名称：</td>
                            <td><input type="text" class="text" id="company_name"  /></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-top: 30px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        <@app.has_oper perm_code='basic.AgentCompany:add'>
                            <button class="btn btn_green" onclick="add()">新建</button>
                        </@app.has_oper>
                        </div>
                        <h3>运营公司列表</h3>
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