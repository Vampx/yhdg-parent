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
                url: "${contextPath}/security/basic/user_notice_message/page.htm",
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
                            title: '标题',
                            align: 'center',
                            field: 'title',
                            width: 50
                        },
                        {
                            title: '内容',
                            align: 'center',
                            field: 'content',
                            width: 80
                        },
                        {
                            title: '客户姓名',
                            align: 'center',
                            field: 'userFullname',
                            width: 40
                        },
                        {
                            title: '手机号码',
                            align: 'center',
                            field: 'userMobile',
                            width: 40
                        },
                        {
                            title: '通知类型',
                            align: 'center',
                            field: 'typeName',
                            width: 20
                        },
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
                            width: 40,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='7_2_4_2'>
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
            var userFullname = $('#user_fullname').val();
            var userMobile = $('#user_mobile').val();
            var type = $('#type').val();
            var title = $('#title').val();
            var queryParams = {
                title: title,
                userMobile: userMobile,
                userFullname: userFullname,
                type: type
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:480px;height:425px;',
                title: '查看',
                href: "${contextPath}/security/basic/user_notice_message/view.htm?id=" + id
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
                            <td align="right">标题：</td>
                            <td><input type="text" class="text" id="title"/></td>
                            <td align="right" width="70">用户姓名：</td>
                            <td><input type="text" class="text" id="user_fullname"/></td>
                            <td align="right" width="70">手机号码：</td>
                            <td><input type="text" class="text" id="user_mobile"/></td>
                            <td align="right" width="70">通知类型：</td>
                            <td>
                                <select style="width:70px;" id="type">
                                    <option value="">所有</option>
                                    <#list typeEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>用户通知消息</h3>
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
