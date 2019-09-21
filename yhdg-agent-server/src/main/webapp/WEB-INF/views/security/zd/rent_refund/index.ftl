<@app.html>
    <@app.head>
    <script>
        $(function () {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/zd/rent_refund/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 50
                        },
                        {title: '姓名', align: 'center', field: 'fullname', width: 50},
                        {
                            title: '手机',
                            align: 'center',
                            field: 'mobile',
                            width: 60
                        },
                        {
                            title: '电池类型',
                            align: 'center',
                            field: 'batteryTypeName',
                            width: 40
                        },
                        {
                            title: '押金',
                            align: 'center',
                            field: 'foregiftMoney',
                            width: 40,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) + " 元";
                            }
                        },
                        {
                            title: '租金',
                            align: 'center',
                            field: 'packetPeriodMoney',
                            width: 40,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) + " 元";
                            }
                        },
                        {
                            title: '保险',
                            align: 'center',
                            field: 'insuranceMoney',
                            width: 40,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) + " 元";
                            }
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'zdRefundStatus',
                            width: 40,
                            formatter: function (val) {
                                if(val == 1){
                                    return '申请退款中';
                                }else {
                                    return '正常';
                                }
                            }
                        },
                        {
                            title: '申请退款时间',
                            align: 'center',
                            field: 'applyRefundTime',
                            width: 40
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 100,
                            formatter: function (val, row) {
                                var html = '';
                                if(row.zdRefundStatus == 1) {
                                    <@app.has_oper perm_code='zd.RentRefund:audit'>
                                        html += ' <a href="javascript:audit(ID)">待审核</a>';
                                    </@app.has_oper>
                                }else {
                                    <@app.has_oper perm_code='zd.RentRefund:refund'>
                                        html += ' <a href="javascript:refund(ID)">退款</a>';
                                    </@app.has_oper>
                                }
                                <@app.has_oper perm_code='zd.RentRefund:refundRecord'>
                                    html += ' <a href="javascript:refund_record(ID)">退款记录</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                onLoadSuccess: function () {
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
            var agentId = ${Session['SESSION_KEY_USER'].agentId};
            datagrid.datagrid('options').queryParams = {
                agentId: agentId,
                mobile: $('#mobile').val(),
                fullname: $('#full_name').val()
            };

            datagrid.datagrid('load');
        }

        function audit(id) {
            App.dialog.show({
                css: 'width:800px;height:520px;',
                title: '审核',
                href: "${contextPath}/security/zd/rent_refund/audit.htm?id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }

        function refund(id) {
            App.dialog.show({
                css: 'width:800px;height:515px;',
                title: '退款',
                href: "${contextPath}/security/zd/rent_refund/refund.htm?id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }

        function refund_record(id) {
            App.dialog.show({
                css: 'width:1200px;height:680px;',
                title: '退款记录',
                href: "${contextPath}/security/zd/customer_refund_record/index.htm?customerId=" + id
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
                            <td align="right" width="80">手机号码：</td>
                            <td><input type="text" class="text" id="mobile"/></td>
                            <td align="right" width="70">姓名：</td>
                            <td><input type="text" class="text" id="full_name"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>退款管理</h3>
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