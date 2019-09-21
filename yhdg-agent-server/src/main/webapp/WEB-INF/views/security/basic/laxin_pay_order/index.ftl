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
                url: "${contextPath}/security/basic/laxin_pay_order/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            title: '订单编号',
                            align: 'center',
                            field: 'id',
                            width: 40
                        },
                        {
                            title: '金额',
                            align: 'center',
                            field: 'money',
                            width: 20,
                            formatter: function(val, row) {
                                return val / 100 + "元";
                            }
                        },
                        {
                            title: '记录数',
                            align: 'center',
                            field: 'recordCount',
                            width: 20
                        },
                        {
                            title: '支付类型',
                            align: 'center',
                            field: 'payTypeName',
                            width: 25
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
                            width: 30,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.LaxinPayOrder:view'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
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

            var id = $('#id').val();
            var status = $('#status').val();

            var queryParams = {
                id: id,
                status: status
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }


        function view(id) {
            App.dialog.show({
                css: 'width:850px;height:510px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/laxin_pay_order/view.htm?id=" + id

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
                            <td align="right" >订单编号：</td>
                            <td><input type="text" class="text" id="id"/></td>
                            <td align="right" >订单状态：</td>
                            <td>
                                <select id="status">
                                    <option value="">所有</option>
                                    <#list StatusEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>拉新支付订单</h3>
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
