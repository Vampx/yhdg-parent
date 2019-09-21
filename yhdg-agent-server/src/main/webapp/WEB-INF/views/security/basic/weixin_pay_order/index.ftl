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
                url: "${contextPath}/security/basic/weixin_pay_order/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            title: '微信支付流水id',
                            align: 'center',
                            field: 'id',
                            width: 40
                        },
                        {
                            title: '订单单号',
                            align: 'center',
                            field: 'sourceId',
                            width: 40
                        },
                        {
                            title: '客户支付码',
                            align: 'center',
                            field: 'paymentId',
                            width: 50
                        },
                        {
                            title: '客户名称',
                            align: 'center',
                            field: 'customerName',
                            width: 25
                        },
                        {
                            title: '支付金额(元)',
                            align: 'center',
                            field: 'money',
                            width: 25,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '订单类型',
                            align: 'center',
                            field: 'sourceTypeName',
                            width: 40
                        },
                        {
                            title: '订单状态',
                            align: 'center',
                            field: 'statusName',
                            width: 20
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
                                <@app.has_oper perm_code='basic.WeixinPayOrder:view'>
                                    html += '<a href="javascript:view(CUSTOMERID, SOURCETYPE,\'SOURCEID\', ORDERSTATUS)">查看</a>';
                                </@app.has_oper>
                                return html.replace(/CUSTOMERID/g, row.customerId).replace(/SOURCETYPE/g, row.sourceType).replace(/SOURCEID/g, row.sourceId).replace(/ORDERSTATUS/g, row.orderStatus);
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
            var tree = $('#site_tree');
            var datagrid = $('#page_table');
//            var agentId = $('#agent_id').combotree('getValue');

            var queryName = $('#query_name').val();
            var customerName = $('#customer_name').val();
            var orderStatus = $('#order_status').val();
            var sourceType = $('#source_type').val();
            var sourceId = $('#source_id').val();
            var queryParams = {
//                agentId: agentId,
                orderStatus: orderStatus,
                sourceType: sourceType,
                customerName: customerName,
                sourceId: sourceId
            };


            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }

        function reloadTree() {
            var agentId = $('#agent_id').combotree('getValue');
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId
            };
            datagrid.datagrid('reload');
        }

        function view(customerId, sourceType, sourceId, orderStatus) {
            App.dialog.show({
                css: 'width:580px;height:310px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/weixin_pay_order/view.htm?customerId=" + customerId + "&sourceType=" + sourceType + "&sourceId=" +sourceId + "&orderStatus=" + orderStatus

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
                            <#--<td align="right">运营商：</td>-->
                            <#--<td>-->
                                <#--<input id="agent_id" class="easyui-combotree" editable="false" style="width: 200px;height: 28px;"-->
                                       <#--data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',-->
                                <#--method:'get',-->
                                <#--valueField:'id',-->
                                <#--textField:'text',-->
                                <#--editable:false,-->
                                <#--multiple:false,-->
                                <#--panelHeight:'200',-->
                                <#--onClick: function(node) {-->
                                   <#--reloadTree();-->
                                <#--}-->
                            <#--"-->
                                <#-->-->
                            <#--</td>-->
                            <td align="right" width="70">客户名称：</td>
                            <td><input type="text" class="text" id="customer_name"/></td>
                            <td align="right" width="70">订单单号：</td>
                            <td><input type="text" class="text" id="source_id"/></td>
                            <td align="right" width="70">订单类型：</td>
                            <td>
                                <select style="width:120px;" id="source_type">
                                    <option value="">所有</option>
                                    <#list SourceTypeEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right" width="70">支付状态：</td>
                            <td>
                                <select style="width:80px;" id="order_status">
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
                        <div class="float_right">
                        </div>
                        <h3>微信订单列表</h3>
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

