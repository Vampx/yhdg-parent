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
                url: "${contextPath}/security/hdg/charge_packet_order_refund/page.htm",
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
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 40
                        },
                        {
                            title: '姓名',
                            align: 'center',
                            field: 'customerFullname',
                            width: 40
                        },
                        {
                            title: '手机号',
                            align: 'center',
                            field: 'customerMobile',
                            width: 40
                        },
                        {
                            title: '金额(元)',
                            align: 'center',
                            field: 'money',
                            width: 40,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '退款金额(元)',
                            align: 'center',
                            field: 'refundMoney',
                            width: 40,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '申请退款时间',
                            align: 'center',
                            field: 'applyRefundTime',
                            width: 60
                        },
                        {
                            title: '退款时间',
                            align: 'center',
                            field: 'refundTime',
                            width: 60
                        },
                        {
                            title: '退款状态',
                            align: 'center',
                            field: 'refundStatusName',
                            width: 40
                        },
                        {
                            title: '退款人',
                            align: 'center',
                            field: 'refundOperator',
                            width: 40
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '<a href="javascript:view(\'ID\')">查看</a>';
                                if(row.refundStatus == ${APPLY_REFUND}) {
                                    <@app.has_oper perm_code='1_2_8_3'>
                                        html += ' <a href="javascript:refund(\'ID\')">退款</a>';
                                    </@app.has_oper>
                                    <@app.has_oper perm_code='1_2_8_4'>
                                        html += ' <a href="javascript:repulse_refund(\'ID\')">拒绝退款</a>';
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

        function query() {
            var datagrid = $('#page_table');
            var queryParams = {
                agentId: $('#agent_id').combotree('getValue'),
                customerFullname: $('#customer_fullname').val(),
                customerMobile: $('#customer_mobile').val(),
                refundStatus: $('#status').val()
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

        function view(id) {
            App.dialog.show({
                css: 'width:786px;height:515px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/charge_packet_order_refund/view.htm?id=" + id

            });
        }

        function refund(id) {
            App.dialog.show({
                css:'width:500px;height:230px;overflow:visible;',
                title:'退款',
                href:"${contextPath}/security/hdg/charge_packet_order_refund/edit_refund.htm?id="+id,
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });

        }

        function repulse_refund(id) {
            App.dialog.show({
                css:'width:500px;height:230px;overflow:visible;',
                title:'退款',
                href:"${contextPath}/security/hdg/charge_packet_order_refund/edit_repulse_refund.htm?id="+id,
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
                            <td align="right">运营商：</td>
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
                            <td align="right" width="60">手机号：</td>
                            <td><input type="text" class="text" id="customer_mobile" /></td>
                            <td align="right" width="60">姓名：</td>
                            <td><input type="text" class="text" id="customer_fullname" /></td>
                            <td align="right" width="80">退款状态：</td>
                            <td>
                                <select style="width:90px;" id="status">
                                    <option value="">所有</option>
                                    <#list RefundStatus as e>
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
                        <h3>会员充电包时段套餐退款记录</h3>
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

