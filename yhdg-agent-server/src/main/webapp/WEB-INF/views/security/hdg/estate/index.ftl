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
                url: "${contextPath}/security/hdg/estate/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            title: '所属运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
                        {title: '物业名称', align: 'center', field: 'estateName', width: 60},
                        {title: '地址', align: 'center', field: 'address', width: 60},
                        {title: '物业联系人', align: 'center', field: 'linkname', width: 60},
                        {title: '联系电话', align: 'center', field: 'tel', width: 60},
                        {
                            title: '是否启用',
                            align: 'center',
                            field: 'isActive',
                            width: 30,
                            formatter: function(row) {
                                if(row == 1) {
                                    return '<a style="color: #00FF00;">是</a>';
                                }else{
                                    return '<a style="color: #ff0000;">否</a>';
                                }
                            }
                        },
                        {title: '营业时间', align: 'center', field: 'workTime', width: 40},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 60,
                            formatter: function(val, row) {
                                    var html = '';
                                    <@app.has_oper perm_code='hdg.Estate:view'>
                                        html += ' <a href="javascript:view(\'ID\')">查看</a>';
                                    </@app.has_oper>
                                    <@app.has_oper perm_code='hdg.Estate:edit'>
                                        html += ' <a href="javascript:edit(ID)">修改</a>';
                                    </@app.has_oper>
                                    <@app.has_oper perm_code='hdg.Estate:remove'>
                                        html += ' <a href="javascript:remove(ID)">删除</a>';
                                    </@app.has_oper>
                                return html.replace(/ID/g, row.id).replace(/EDIT_FLAG/g, 0);
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

            var estateName = $('#estate_name').val();

            datagrid.datagrid('options').queryParams = {
                estateName: estateName
            };

            datagrid.datagrid('load');
        }


        function add() {
            App.dialog.show({
                css: 'width:950px;height:600px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/hdg/estate/add.htm",
                event: {
                    onClose: function() {
                        query();
                    }
                }
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:1050px;height:650px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/hdg/estate/edit.htm?id=" + id ,
                event: {
                    onClose: function() {
                        query();
                    },
                    onLoad: function() {
                    }
                }
            });
        }

        function view(id) {
            App.dialog.show({
                css: 'width:1050px;height:650px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/estate/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function(ok) {
                if(ok) {
                    $.post('${contextPath}/security/hdg/estate/delete.htm', {
                        id: id
                    }, function (json) {
                        if (json.success) {
                            $.messager.alert('info', '操作成功', 'info');
                            query();
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

                <div class="panel search" >
                    <div class="float_right">
                        <button class="btn btn_yellow"onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right" width="70">物业名称：</td>
                            <td><input type="text" class="text" id="estate_name"  /></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='hdg.Estate:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>物业信息</h3>
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





