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
                url: "${contextPath}/security/basic/customer_balance_deduct/page.htm",
                fitColumns: true,
                pageSize: 50,
                pageList: [50, 100],
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
                            title: '手机号码',
                            align: 'center',
                            field: 'mobile',
                            width: 60,
                            formatter: function(val) {
                                return val;
                            }
                        },
                        {title: '客户昵称', align: 'center', field: 'fullname', width: 60},
                        {
                            title: '金额(元)',
                            align: 'center',
                            field: 'money',
                            width: 40,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {title: '操作人', align: 'center', field: 'handlerName', width: 40},
                        {title: '备注', align: 'center', field: 'memo', width: 120},
                        {title: '创建时间', align: 'center', field: 'createTime', width: 60}
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

            var fullname = $('#fullname').val();
            var mobile = $('#mobile').val();

            datagrid.datagrid('options').queryParams = {
                fullname: fullname,
                mobile: mobile
            };
            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:600px;height:360px;',
                title: '新建',
                href: "${contextPath}/security/basic/customer/add.htm",
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }


        function edit(id) {
            App.dialog.show({
                css: 'width:600px;height:360px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/customer/edit.htm?id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    },
                    onLoad: function() {
                    }
                }
            });
        }
        function view(id) {
            App.dialog.show({
                css: 'width:752px;height:520px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/customer/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function(ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/customer/delete.htm?id=" + id, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            reload();
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        }

        function batchActive() {
            var datagrid = $('#page_table');
            var checked = datagrid.datagrid('getChecked');
            var list = [];
            for(var i = 0; i < checked.length; i++) {
                list.push(checked[i].id);
            }
            if(list.length == 0) {
                $.messager.alert('info', '请选择记录', 'info');
                return;
            }

            $.messager.confirm('提示信息', '确认禁用/启动?', function(ok) {
                if(ok) {
                    $.post('${contextPath}/security/basic/customer/batch_active.htm', {
                        id: list
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

        function batchRemove() {
            var datagrid = $('#page_table');
            var checked = datagrid.datagrid('getChecked');
            var list = [];
            for(var i = 0; i < checked.length; i++) {
                list.push(checked[i].id);
            }
            if(list.length == 0) {
                $.messager.alert('info', '请选择记录', 'info');
                return;
            }

            $.messager.confirm('提示信息', '确认删除?', function(ok) {
                if(ok) {
                    $.post('${contextPath}/security/basic/customer/batch_remove.htm', {
                        id: list
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
                <div class="panel search" >
                    <div class="float_right">

                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">手机号：</td>
                            <td><input type="text" class="text"  id="mobile"/></td>
                            <td align="right" width="60">客户昵称：</td>
                            <td><input type="text" class="text" id="fullname"/>&nbsp;&nbsp;</td>

                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" >
                    <div class="toolbar clearfix">
                        <div class="float_right">

                        </div>
                        <h3>会员余额扣款记录</h3>
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