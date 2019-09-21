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
                url: "${contextPath}/security/basic/image_convert_size/page.htm",
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
                        {
                            title: '标题',
                            align: 'center',
                            field: 'title',
                            width: 30
                        },
                        {
                            title: '内容',
                            align: 'left',
                            field: 'stand',
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
            var title = $('#title').val();
            datagrid.datagrid('options').queryParams = {
                title: title
            };
            datagrid.datagrid('load');
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:550px;height:336px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/image_convert_size/edit.htm?id=" + id,
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
                href: "${contextPath}/security/basic/image_convert_size/view.htm?id=" + id,
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
                            <td><input type="text" class="text" id="title"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>图片标准列表</h3>
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


