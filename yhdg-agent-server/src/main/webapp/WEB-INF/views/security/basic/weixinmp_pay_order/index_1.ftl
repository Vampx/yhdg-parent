<@app.html>
    <@app.head>
    <script>
        $(function() {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                url: "${contextPath}/security/basic/weixinmp_pay_order/page.htm?agentId=${(agentId)!0}",
                fitColumns: true,
                idField: 'id',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                columns: [
                    [
                        {
                            title: '微信订单单号',
                            align: 'center',
                            field: 'id',
                            width: 40
                        },
                        {
                            title: '客户名称',
                            align: 'center',
                            field: 'customerName',
                            width: 25
                        },
                        {
                            title: '金额',
                            align: 'center',
                            field: 'money',
                            width: 25,
                            formatter: function(val, row) {
                                return val / 100 + "元";
                            }
                        },
                        {
                            title: '订单单号',
                            align: 'center',
                            field: 'sourceId',
                            width: 40
                        },
                        {
                            title: '订单类型',
                            align: 'center',
                            field: 'sourceType',
                            width: 40,
                            formatter: function(val){
                                <#list SourceTypeEnum as e>
                                    if(${e.getValue()} == val)
                                    return '${e.getName()}';
                                </#list>
                            }
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'orderStatus',
                            width: 25,
                            formatter: function(val){
                                <#list StatusEnum as e>
                                    if(${e.getValue()} == val)
                                    return '${e.getName()}';
                                </#list>
                            }
                        },
                        {
                            title: '客户支付码',
                            align: 'center',
                            field: 'paymentId',
                            width: 50
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
                                var html = '<a href="javascript:view(\'ID\')">查看</a>';
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
                    rows: $('#rows').val()
                }
            });
        })
        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query(page) {
            var tree = $('#site_tree');
            var datagrid = $('#page_table');

            var queryName = $('#query_name').val();
            var customerName = $('#customer_name').val();
            var orderStatus = $('#order_status').val();
            var mobile = $('#mobile').val();
            var id = $('#id').val();
            var sourceType = $('#source_type').val();
            var sourceId = $('#source_id').val();
            var rowId = '';
            var rows = $('#rows').val();

            if(page == 2) {
                var data = datagrid.datagrid('getRows');
                if(data.length) {
                    rowId = data[0].id;
                }
            } else if(page == 3) {
                var data = datagrid.datagrid('getRows');
                if(data.length) {
                    rowId = data[data.length - 1].id;
                }
            }

            var queryParams = {
                orderStatus: orderStatus,
                sourceType: sourceType,
                customerName: customerName,
                sourceId: sourceId,
                id: id,
                page: page || 1,
                rowId: rowId,
                rows: rows
            };

            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }



        function view(id) {
            App.dialog.show({
                css: 'width:560px;height:260px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/weixinmp_pay_order/view.htm?id=" + id

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
                            <td align="right">客户名称：</td>
                            <td><input type="text" class="text" id="customer_name"/></td>
                            <td align="right">订单单号：</td>
                            <td><input type="text" class="text" id="source_id"/></td>
                            <td align="right">微信单号：</td>
                            <td><input type="text" class="text" id="id"/></td>
                            <td align="right" width="70">状态：</td>
                            <td>
                                <select style="width:70px;" id="order_status">
                                    <option value="">所有</option>
                                    <#list StatusEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right" width="70">订单类型：</td>
                            <td>
                                <select style="width:70px;" id="source_type">
                                    <option value="">所有</option>
                                    <#list SourceTypeEnum as e>
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
                            <#--<button class="btn btn_green" onclick="add()">充值</button>-->
                        </div>
                        <h3>公众号订单列表</h3>
                    </div>
                    <div class="grid">
                        <div class="grid_table_box">
                            <table id="page_table"></table>
                        </div>
                        <div class="paging">
                            <ul>
                                <li><a href="javascript:query(1)">首页</a></li>
                                <li><a href="javascript:query(2)">上页</a></li>
                                <li><a href="javascript:query(3)">下页</a></li>
                                <li><a href="javascript:query(4)">末页</a></li>
                            </ul>
                            <p class="float_left">每页显示：</p>
                            <select class="float_left page_list" style="width: 50px;" id="rows">
                                <option>10</option>
                                <option>50</option>
                                <option>100</option>
                            </select>
                            <p class="float_left">条</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        </@app.container>
    </@app.body>
</@app.html>

