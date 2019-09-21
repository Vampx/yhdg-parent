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
                url: "${contextPath}/security/basic/terminal_code/page.htm",
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
                        {title: '终端码', align: 'center', field: 'id', width: 250},
                        {title: '终端编号', align: 'center', field: 'code', width: 250},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 500,
                            formatter: function(val, row) {
                                return '<a href="javascript:swap(ID)">交换</a>'.replace(/ID/g, "'"+row.id+"'");
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
            var code = $('#code').val();
            datagrid.datagrid('options').queryParams = {
                code: code
            };
            datagrid.datagrid('load');
        }

        function swap(id) {
            App.dialog.show({
                css: 'width:480px;height:190px;padding:5px;',
                title: '交换',
                href: "${contextPath}/security/basic/terminal_code/swap.htm?id=" + id,
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
                            <td align="right">终端编号：</td>
                            <td><input type="text" class="text" id="code"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>终端编号</h3>
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
