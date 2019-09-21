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
                url: "${contextPath}/security/basic/push_message/page.htm",
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
                            title: '推送目标',
                            align: 'center',
                            field: 'target',
                            width: 40
                        },
                        {
                            title: '推送内容',
                            align: 'center',
                            field: 'content',
                            width: 40
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
                            title: '发送条数',
                            align: 'center',
                            field: 'resendNum',
                            width: 40
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'messageStatusName',
                            width: 20
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 40,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='7_2_2_2'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>&nbsp;';
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
            var content = $('#content').val();
            var senderId = $('#sender_id').val();
            var sendStatus = $('#status').val();
            var target = $('#target').val();
            var queryParams = {
                target: target, content: content, senderId: senderId, sendStatus: sendStatus
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:480px;height:385px;',
                title: '查看',
                href: "${contextPath}/security/basic/push_message/view.htm?id=" + id
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
                            <td align="right" width="70">推送目标：</td>
                            <td><input type="text" class="text" id="target"/></td>
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
                        <h3>消息推送</h3>
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
