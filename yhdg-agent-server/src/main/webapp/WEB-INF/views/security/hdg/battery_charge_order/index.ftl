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
                url: "${contextPath}/security/hdg/battery_charge_order/page.htm",
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
                            title: '订单编号',
                            align: 'center',
                            field: 'id',
                            width: 30
                        },
                        {
                            title: '电池编号',
                            align: 'center',
                            field: 'batteryId',
                            width: 30
                        },
                        {
                            title: '客户名称',
                            align: 'center',
                            field: 'customerFullname',
                            width: 30
                        },
                        {
                            title: '订单收入',
                            align: 'center',
                            field: 'money',
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
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 30,
                            formatter: function(val, row) {
                                var html = '<a href="javascript:view(\'ID\')">查看</a>';
                                if(row.status == ${status} && row.refundTime == null) {
                                    <@app.has_oper perm_code='2_3_7_2'>
                                        html += ' <a href="javascript:refund(\'ID\')">退款</a>';
                                    </@app.has_oper>
                                }
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

        function reloadTree() {
            var agentId = $('#agent_id').combotree('getValue');
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId
            };
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();
            var status = $('#status').val();
            var payType = $('#pay_type').val();
            var agentId = $('#agent_id').combotree('getValue');
            var queryParams = {
                status: status,
                payType: payType,
                agentId: agentId
            };
            queryParams[queryName] = queryValue;
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:786px;height:500px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/battery_charge_order/view.htm?id=" + id

            });
        }

        function refund(id) {
            App.dialog.show({
                css:'width:500px;height:230px;overflow:visible;',
                title:'退款',
                href:"${contextPath}/security/hdg/battery_charge_order/edit_refund.htm?id="+id,
                event: {
                    onClose: function() {
                        reload();
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
                            <td>
                                <select style="width:100px;" id="query_name">
                                    <option value="id">订单编号</option>
                                    <option value="batteryId">电池编号</option>
                                    <option value="customerFullname">客户名称</option>
                                    <option value="customerMobile">客户手机</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"/></td>
                            <td align="right" width="70">运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="false" style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                   reloadTree();
                                }
                            "
                                >
                            </td>
                            <td align="right" width="70">订单状态：</td>
                            <td>
                                <select style="width:80px;" id="status">
                                    <option value="">所有</option>
                                    <#list StatusEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right" width="70">支付方式：</td>
                            <td>
                                <select style="width:80px;" id="pay_type">
                                    <option value="">所有</option>
                                    <#list PayTypeEnum as e>
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
                        <h3>换电订单列表</h3>
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

