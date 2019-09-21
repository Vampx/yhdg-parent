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
                url: "${contextPath}/security/basic/mobile_message/page.htm",
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
                        {title: '手机号', align: 'center', field: 'mobile', width: 40},
                        {
                            title: '内容',
                            align: 'center',
                            field: 'content',
                            width: 100,
                            formatter : function(value, row) {
                                return '<span title="'+row.content+'">' + value + '</span>';
                            }
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 40
                        },
                        {
                            title: '发送时间',
                            align: 'center',
                            field: 'handleTime',
                            width: 40
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'messageStatusName',
                            width: 20
                        },
                        {
                            title: '回调状态',
                            align: 'center',
                            field: 'callbackStatus',
                            width: 40
                        },
                        {
                            title: '发送接口',
                            align: 'center',
                            field: 'senderName',
                            width: 40
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 40,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='7_1_3_2'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>&nbsp;';
                                </@app.has_oper>
                                if(row.status == 2 || row.status == 3) {
                                    <@app.has_oper perm_code='7_1_3_3'>
                                        html += '<a href="javascript:send(\'ID\')">重发</a>'
                                    </@app.has_oper>
                                }
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
            var mobile = $('#mobile').val();
            var content = $('#content').val();
            var senderId = $('#sender_id').val();
            var status = $('#status').val();
            var appId = $('#app_id').combotree('getValue');
            var queryParams = {
                mobile: mobile, content: content, senderId: senderId, status: status, appId:appId
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:480px;height:390px;',
                title: '查看',
                href: "${contextPath}/security/basic/mobile_message/view.htm?id=" + id
            });
        }

        function send(id) {
            $.messager.confirm('提示信息', '确认重发?', function(ok) {
                if(ok) {
                    $.post('${contextPath}/security/basic/mobile_message/send.htm', {
                        id: id
                    }, function (json) {
                        if (json.success) {
                            $.messager.alert('info', '操作成功', 'info');
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
                            <td align="right">&nbsp;&nbsp;手机：</td>
                            <td><input type="text" class="text" id="mobile"/></td>
                            <td align="right" width="50">内容：</td>
                            <td><input type="text" class="text" id="content"/></td>
                            <td align="right" width="50">状态：</td>
                            <td>
                                <select style="width:70px;" id="status">
                                    <option value="">所有</option>
                                    <#list messageStatusEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>短信列表</h3>
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
