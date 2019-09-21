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
                url: "${contextPath}/security/basic/voice_config/page.htm",
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
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
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
                                <@app.has_oper perm_code='basic.VoiceConfig:view'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.VoiceConfig:edit'>
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
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
            var configName = $('#config_name').val();
            var partnerId = $('#agent_id').combobox('getValue');
            datagrid.datagrid('options').queryParams = {
                configName: configName,
                partnerId: partnerId
            };
            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:350px;height:310px;',
                title: '修改',
                href: "${contextPath}/security/basic/voice_config/add.htm",
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
                css: 'width:350px;height:270px;',
                title: '修改',
                href: "${contextPath}/security/basic/voice_config/edit.htm?id=" + id,
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
                css: 'width:350px;height:270px;',
                title: '查看',
                href: "${contextPath}/security/basic/voice_config/view.htm?id=" + id
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
                            <td>运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="true" style="width: 180px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:true,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                   reloadTree();
                                }
                            "
                                >
                            </td>
                            <td align="right">&nbsp;&nbsp;接口名称：</td>
                            <td><input type="text" class="text" id="config_name"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='basic.VoiceConfig:add'>
                                <button class="btn btn_red add" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>语音接口</h3>
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
