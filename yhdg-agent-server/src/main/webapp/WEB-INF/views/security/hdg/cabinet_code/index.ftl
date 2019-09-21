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
                url: "${contextPath}/security/hdg/cabinet_code/page.htm",
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
                        {title: '机器码', align: 'center', field: 'id', width: 100},
                        {title: '换电柜编号', align: 'center', field: 'code', width: 100},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 100,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='2_3_3_2'>
                                    html += ' <a href="javascript:swap(\'ID\')">交换</a>';
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
        })



        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var code = $('#code').val();
            var id = $('#id').val();
            datagrid.datagrid('options').queryParams = {
                code: code,
                id: id
            };
            datagrid.datagrid('load');
        }

        function editCharger(id){
            App.dialog.show({
                css: 'width:786px;height:515px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/hdg/cabinet/edit.htm?id=" + id,
                event: {
                    onClose: function(){
                        var datagrid = $('#page_table');
                        datagrid.datagrid('reload');
                    },
                    onLoad:function(){
                    }
                }
            });
        }

        function swap(id) {
            App.dialog.show({
                css: 'width:350px;height:200px;padding:5px;',
                title: '交换',
                href: "${contextPath}/security/hdg/cabinet_code/swap.htm?id=" + id,
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
                                <td align="right">机器码：</td>
                                <td><input type="text" class="text" id="id"/>&nbsp;&nbsp;</td>
                                <td align="right">换电柜编号：</td>
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
