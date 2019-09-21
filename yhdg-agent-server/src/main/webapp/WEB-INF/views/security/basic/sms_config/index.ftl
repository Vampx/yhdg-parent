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
                url: "${contextPath}/security/basic/sms_config/page.htm",
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
                            title: '接口名称',
                            align: 'center',
                            field: 'configName',
                            width: 60
                        },
                        {
                            title: '账户',
                            align: 'center',
                            field: 'account',
                            width: 60
                        },
                        {
                            title: '余额',
                            align: 'center',
                            field: 'balance',
                            width: 60
                        },
                        {
                            title: '签名',
                            align: 'center',
                            field: 'sign',
                            width: 60
                        },
                        {
                            title: '是否启用',
                            align: 'center',
                            field: 'isActive',
                            width: 60,
                            formatter: function(val) {
                                if (val == 1) {
                                    return '是'
                                } else {
                                    return '否'
                                }
                            }
                        },
                        {
                            title: '更新时间',
                            align: 'center',
                            field: 'updateTime',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='7_1_1_3'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='7_1_1_4'>
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='7_1_1_5'>
                                    html += ' <a href="javascript:balance(ID)">查询余额</a>';
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
            var datagrid = $('#page_table');
            var appId = $('#app_id').combotree('getValue');
            datagrid.datagrid('options').queryParams = {
                appId: appId
            };
            datagrid.datagrid('load');
        }

        function query() {
            var datagrid = $('#page_table');
            var configName = $('#config_name').val();
            var appId = $('#app_id').combotree('getValue');
            datagrid.datagrid('options').queryParams = {
                configName: configName,
                appId:appId
            };
            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:480px;height:335px;',
                title: '修改',
                href: "${contextPath}/security/basic/sms_config/add.htm",
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

        function edit(id) {
            App.dialog.show({
                css: 'width:480px;height:332px;',
                title: '修改',
                href: "${contextPath}/security/basic/sms_config/edit.htm?id=" + id + "&agentId=" + $('#app_id').combotree('getValue'),
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

        function view(id) {
            App.dialog.show({
                css: 'width:480px;height:255px;',
                title: '查看',
                href: "${contextPath}/security/basic/sms_config/view.htm?id=" + id +"&agentId=" + $('#app_id').combotree('getValue')
            });
        }
        function balance(id) {
            $.post('${contextPath}/security/basic/sms_config/balance.htm', {
                id: id
            }, function(json) {
                <@app.json_jump/>
                $.messager.alert('提示信息', json.message, 'info');
                reload();
            }, 'json');
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
                            <td>平台类型：</td>
                            <td>
                                <input name="appId" id="app_id" style="height: 28px;width: 140px;" class="easyui-combobox"  editable="false"
                                       data-options="url:'${contextPath}/security/basic/agent/self_platform_agent_list.htm',
                                            method:'get',
                                            valueField:'id',
                                            textField:'agentName',
                                            editable:false,
                                            multiple:false,
                                            panelHeight:'200',
                                            onSelect: function(node) {
                                                query();
                                            }"
                                />
                            </td>
                            <td align="right">&nbsp;&nbsp;接口名称：</td>
                            <td><input type="text" class="text" id="config_name"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='7_1_1_2'>
                                <button class="btn btn_red add" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>短信接口</h3>
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
