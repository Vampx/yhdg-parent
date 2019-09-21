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
                url: "${contextPath}/security/basic/weixinmp_pay_order/page.htm",
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
                            title: '客户手机号',
                            align: 'center',
                            field: 'mobile',
                            width: 40
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
                                var html = '';
                                <@app.has_oper perm_code='basic.WeixinmpPayOrder:view'>
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

            var customerName = $('#customer_name').val();
            var orderStatus = $('#order_status').val();
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();
            var sourceType = $('#source_type').val();
            var sourceId = $('#source_id').val();
            var agentId = $('#agent_id').combotree('getValue');
            var partnerId = $('#partner_id').combobox('getValue');

            var queryParams = {
                orderStatus: orderStatus,
                sourceType: sourceType,
                customerName: customerName,
                sourceId: sourceId,
                queryName: queryName,
                agentId: agentId,
                partnerId: partnerId
            };
            queryParams[queryName] = queryValue;
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

        function reloadTree() {
            var agentId = $('#agent_id').combotree('getValue');
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId
            };
            datagrid.datagrid('reload');
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
                            <td>商户：</td>
                            <td>
                                <input name="partnerId" id="partner_id" style="height: 28px;width: 130px;" class="easyui-combobox"  editable="false"
                                       data-options="url:'${contextPath}/security/basic/partner/list.htm?dummy=${'无'?url}',
                                        method:'get',
                                        valueField:'id',
                                        textField:'partnerName',
                                        editable:false,
                                        multiple:false,
                                        panelHeight:'200',
                                        onSelect: function(node) {
                                            query();
                                        }"
                                />
                            </td>
                            <td align="right">&nbsp;&nbsp;运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="true"
                                       style="width: 180px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:true,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                   reloadTree();
                                }
                            "
                                >
                            </td>
                            <td align="right" width="115">
                                <select style="width:100px;" id="query_name">
                                    <option value="mobile">手机号</option>
                                    <option value="id">微信单号</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"  style="width: 170px;"/></td>
                            <td align="right" width="60">状态：</td>
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
                        <h3>公众号订单列表</h3>
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
