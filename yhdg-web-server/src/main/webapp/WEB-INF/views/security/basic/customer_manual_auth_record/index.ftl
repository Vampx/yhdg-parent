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
                url: "${contextPath}/security/basic/customer_manual_auth_record/page.htm",
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
                            title: '认证id',
                            align: 'center',
                            field: 'id',
                            width: 25
                        },
                        {
                            title: '姓名',
                            align: 'center',
                            field: 'fullname',
                            width: 25
                        },
                        {
                            title: '手机号',
                            align: 'center',
                            field: 'mobile',
                            width: 20
                        },
                        {
                            title: '审核用户',
                            align: 'center',
                            field: 'auditUser',
                            width: 20
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 20
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 20
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 30,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.CustomerManualAuthRecord:view'>
                                    html += ' <a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.CustomerManualAuthRecord:auth'>
                                if(row.status==1){
                                    html += ' <a href="javascript:Auth(\'ID\')">审核</a>';
                                }
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                onLoadSuccess:function() {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                },
                queryParams: {
                    appId: 0
                }
            });
        });

        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function reloadTree() {
            var agentId = $('#agent_id').combotree('getValue');
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId
            };
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();

            var queryParams = {
                queryName: queryName,
                status: $('#status').val(),
            };
            queryParams[queryName] = queryValue;
            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:1000px;height:510px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/customer_manual_auth_record/view.htm?id=" + id

            });
        }
        function Auth(id) {
            App.dialog.show({
                css: 'width:755px;height:510px;overflow:visible;',
                title: '审核',
                href: "${contextPath}/security/basic/customer_manual_auth_record/audit.htm?id="+id,
                event: {
                    onClose: function() {
                        reload();
                    },
                    onLoad: function() {
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
                            <td align="right" width="115">
                                <select style="width:100px;" id="query_name">
                                    <option value="mobile">手机号</option>
                                    <option value="fullname">姓名</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"  style="width: 170px;"/></td>
                            <td align="right" width="60">状态：</td>
                            <td>
                                <select style="width:90px;" id="status" >
                                    <option value=""selected = "selected">所有</option>
                                    <#list StatusEnum as e>
                                        <option value="${e.getValue()}" >${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                        <h3>人工认证审核</h3>
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

