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
                url: "${contextPath}/security/basic/weixin_template_code/page.htm",
                fitColumns: true,
                pageSize: 10,
                pageList: [50, 100, 150],
                idField: 'id',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                columns: [
                    [
                        { field: 'checkbox', checkbox: true },
                        {
                            title: '标题',
                            align: 'center',
                            field: 'templateName',
                            width: 30
                        },
                        {
                            title: '模板ID',
                            align: 'center',
                            field: 'templateCode',
                            width: 100
                        },
                        {
                            title: '变量',
                            align: 'center',
                            field: 'variable',
                            width: 100
                        },
                        {
                            title: '颜色',
                            align: 'center',
                            field: 'color',
                            width: 100
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 30,
                            formatter: function(val, row) {
                                var html = '<a href="javascript:view(ID)">查看</a>';
                                html += ' <a href="javascript:edit(ID)">修改</a>';
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
            var templateName = $('#template_name').val();
            datagrid.datagrid('options').queryParams = {
                templateName: templateName
            };
            datagrid.datagrid('load');
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:550px;height:336px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/weixin_template_code/edit.htm?id=" + id,
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
                css: 'width:550px;height:336px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/weixin_template_code/view.htm?id=" + id,
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
                            <td align="right" width="60">标题：</td>
                            <td><input type="text" class="text" id="template_name"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>微信模板列表</h3>
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


