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
                url: "${contextPath}/security/zd/customer_multi_order/page.htm",
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
                        {
                            title: '商户',
                            align: 'center',
                            field: 'partnerName',
                            width: 30
                        },
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 30
                        },
                        {
                            title: '手机',
                            align: 'center',
                            field: 'mobile',
                            width: 30
                        },
                        {
                            title: '姓名',
                            align: 'center',
                            field: 'fullname',
                            width: 30
                        },
                        {
                            title: '总金额(元)',
                            align: 'center',
                            field: 'totalMoney',
                            width: 30,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '待付金额(元)',
                            align: 'center',
                            field: 'debtMoney',
                            width: 30,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 30
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
                            width: 20,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='zd.CustomerMultiOrder:view'>
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
                },
                queryParams: {
                    type: 2,
                    agentId: ${Session['SESSION_KEY_USER'].agentId}
                }
            });
        });
        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();

            var queryParams = {
                queryName: queryName,
                status: $('#status').val(),
                type: 2,
                agentId: ${Session['SESSION_KEY_USER'].agentId}
            };

            queryParams[queryName] = queryValue;
            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:985px;height:520px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/zd/customer_multi_order/view.htm?id=" + id
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
                            <td align="right" width="115">
                                <select style="width:100px;" id="query_name">
                                    <option value="mobile">手机号</option>
                                    <option value="fullname">姓名</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"  style="width: 120px;"/></td>
                            <td align="right" width="60">状态：</td>
                            <td>
                                <select style="width:90px;" id="status" >
                                    <option value="">所有</option>
                                    <#list statusEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                        <h3>多通道订单</h3>
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

