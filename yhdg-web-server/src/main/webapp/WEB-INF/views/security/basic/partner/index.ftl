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
                url: "${contextPath}/security/basic/partner/page.htm",
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
                            title: 'ID',
                            align: 'center',
                            field: 'id',
                            width: 40
                        },
                        {
                            title: '商户名称',
                            align: 'center',
                            field: 'partnerName',
                            width: 60
                        },
                        {
                            title: '公众号appId',
                            align: 'center',
                            field: 'mpAppId',
                            width: 60
                        },
                        {
                            title: '小程序appId',
                            align: 'center',
                            field: 'maAppId',
                            width: 60
                        },
                        {
                            title: '生活号appId',
                            align: 'center',
                            field: 'fwAppId',
                            width: 60
                        },
                        {
                            title: '支付宝appId',
                            align: 'center',
                            field: 'alipayAppId',
                            width: 60
                        },
                        {
                            title: '微信appId',
                            align: 'center',
                            field: 'weixinAppId',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.Partner:view'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                    <@app.has_oper perm_code='basic.Partner:edit'>
                                        html += ' <a href="javascript:edit(ID)">修改</a>';
                                    </@app.has_oper>
                                    <@app.has_oper perm_code='basic.Partner:remove'>
                                        html += ' <a href="javascript:remove(ID)">删除</a>';
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

            var partnerName = $('#partnerName').val();

            var queryParams = {
                partnerName: partnerName
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }
        function add() {
            App.dialog.show({
                css: 'width:886px;height:620px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/basic/partner/add.htm",
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }
        function edit(id) {
            App.dialog.show({
                css: 'width:886px;height:620px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/partner/edit.htm?id=" + id,
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
                css: 'width:886px;height:620px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/partner/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function(ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/partner/delete.htm?id=" + id, function (json) {
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
                                <td align="right" >商户名称：</td>
                                <td><input type="text" class="text" id="partnerName"/></td>
                            </tr>
                        </table>
                    </div>
                    <div class="panel grid_wrap">
                        <div class="toolbar clearfix">
                            <div class="float_right">
                                <@app.has_oper perm_code='basic.Partner:add'>
                                    <button class="btn btn_green" onclick="add()">新建</button>
                                </@app.has_oper>
                            </div>
                            <h3>商户信息</h3>
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