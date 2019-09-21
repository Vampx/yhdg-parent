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
                url: "${contextPath}/security/basic/laxin_customer/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            title: 'Id',
                            align: 'center',
                            field: 'id',
                            width: 50
                        },
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 25
                        },
                        {
                            title: '拉新手机号',
                            align: 'center',
                            field: 'laxinMobile',
                            width: 40
                        },
                        {
                            title: '客户手机号',
                            align: 'center',
                            field: 'targetMobile',
                            width: 40
                        },
                        {
                            title: '客户姓名',
                            align: 'center',
                            field: 'targetFullname',
                            width: 20
                        },
                        {
                            title: '佣金方式',
                            align: 'center',
                            field: 'incomeTypeName',
                            width: 20
                        },
                        {
                            title: '购买押金时间',
                            align: 'center',
                            field: 'foregiftTime',
                            width: 40
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 40
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 40,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.LaxinCustomer:view'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>&nbsp;&nbsp;';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.LaxinCustomer:cancelLaxin'>
                                    if (row.foregiftTime == null) {
                                        html += '<a href="javascript:cancelLaxin(\'ID\')">取消拉新</a>&nbsp;&nbsp;';
                                    }
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                onLoadSuccess:function() {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                },
                queryParams: {
                    appId: 0
                }
            });
        });

        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }



        function query() {
            var laxinMobile = $('#laxin_mobile').val();
            var targetMobile = $('#target_mobile').val();

            var queryParams = {
                laxinMobile: laxinMobile,
                targetMobile: targetMobile
            };
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function cancelLaxin(id) {
            App.dialog.show({
                css: 'width:470px;height:230px;overflow:visible;',
                title: '取消拉新',
                href: "${contextPath}/security/basic/laxin_customer/cancel_laxin.htm?id=" + id,
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
                css: 'width:850px;height:510px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/laxin_customer/view.htm?id=" + id

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
                            <td align="right" >拉新手机号：</td>
                            <td><input type="text" class="text" id="laxin_mobile"/></td>
                            <td align="right" >客户手机号：</td>
                            <td><input type="text" class="text" id="target_mobile"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>拉新客户</h3>
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
