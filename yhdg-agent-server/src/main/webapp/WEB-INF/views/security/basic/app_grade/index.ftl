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
                url: "${contextPath}/security/basic/app_grade/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                        { field: 'checkbox', checkbox: true },
                        {title: '手机', align: 'center', field: 'mobile', width: 100},
                        {title: '评分', align: 'center', field: 'grade', width: 100,
                            formatter: function(val, row) {
                                var html = '<a href="javascript:" >GRADE</a>';
                                return html = html.replace(/GRADE/g, new Number(row.grade).toFixed(1));
                            }
                        },
                        {title: '创建时间', align: 'center', field: 'createTime', width: 100},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '<a href="javascript:view(ID)">查看</a>';
                                html += ' <a href="javascript:remove(ID)">删除</a>'
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
        })



        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var mobile = $('#mobile').val();
            datagrid.datagrid('options').queryParams = {
                mobile: mobile
            };
            datagrid.datagrid('load');
        }


        function view(id) {
            App.dialog.show({
                css: 'width:480px;height:270px;',
                title: '查看',
                href: "${contextPath}/security/basic/app_grade/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function(ok) {
                if(ok) {
                    $.post('${contextPath}/security/basic/app_grade/delete.htm', {
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
                            <td align="right">手机：</td>
                            <td><input type="text" class="text" id="mobile"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        <#--
                            <button class="btn btn_green" onclick="add()">新建</button>
-->
                        </div>
                        <h3>反馈</h3>
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
