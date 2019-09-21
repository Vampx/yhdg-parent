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
                url: "${contextPath}/security/basic/laxin_record/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            title: '拉新手机号',
                            align: 'center',
                            field: 'laxinMobile',
                            width: 40
                        },
                        {
                            title: '金额',
                            align: 'center',
                            field: 'laxinMoney',
                            width: 40,
                            formatter: function(val, row) {
                                return val / 100 + "元/" + row.incomeTypeName;
                            }
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
                            title: '支付订单',
                            align: 'center',
                            field: 'orderId',
                            width: 40
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 25
                        },
                        {
                            title: '付款时间',
                            align: 'center',
                            field: 'payTime',
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
                                <@app.has_oper perm_code='basic.LaxinRecord:view'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>&nbsp;&nbsp;';
                                    if (row.status == 4) {
                                        html += '<a href="javascript:resetAccount(\'ID\')">重置</a>';
                                    }else if (row.status == 1) {
                                        <@app.has_oper perm_code='basic.LaxinRecord:cancel'>
                                            html += '<a href="javascript:cancel(\'ID\')">取消</a>';
                                        </@app.has_oper>
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
            var datagrid = $('#page_table');

            var laxinMobile = $('#laxin_mobile').val();
            var targetMobile = $('#target_mobile').val();

            var queryParams = {
                laxinMobile: laxinMobile,
                targetMobile: targetMobile
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }


        function view(id) {
            App.dialog.show({
                css: 'width:850px;height:510px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/laxin_record/view.htm?id=" + id

            });
        }

        function cancel(id) {
            App.dialog.show({
                css: 'width:470px;height:230px;overflow:visible;',
                title: '取消',
                href: "${contextPath}/security/basic/laxin_record/cancel.htm?id=" + id,
                event: {
                    onClose: function () {
                        reload();
                    },
                    onLoad: function () {
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
                            <td align="right" >拉新手机号：</td>
                            <td><input type="text" class="text" id="laxin_mobile"/>&nbsp;&nbsp;</td>
                            <td align="right" >客户手机号：</td>
                            <td><input type="text" class="text" id="target_mobile"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>拉新记录列表</h3>
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
