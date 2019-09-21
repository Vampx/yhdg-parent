<@app.html>
    <@app.head>
    <script>
        $(function(){
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/basic/partner_in_out_cash/page.htm",
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
                            title: 'ID',
                            align: 'center',
                            field: 'partnerId',
                            width: 50
                        },
                        {
                            title: '日期',
                            align: 'center',
                            field: 'statsDate',
                            width: 40
                        },
                        {
                            title: '平台',
                            align: 'center',
                            field: 'partnerName',
                            width: 60
                        },
                        {
                            title: '公众号收入',
                            align: 'center',
                            field: 'weixinmpIncome',
                            width: 40,
                            formatter: function (val, row) {
                                return '<a href="javascript:view_weixinmp_pay_order(' + row.partnerId +',\''+ row.statsDate + '\')"><u>' + Number(val / 100).toFixed(2) + "元" + '</u></a>';
                            }
                        },
                        {
                            title: '公众号支出',
                            align: 'center',
                            field: 'weixinmpRefund',
                            width: 40,
                            formatter: function (val, row) {
                                return '<a href="javascript:view_weixinmp_pay_order_refund(' + row.partnerId +',\''+ row.statsDate + '\')"><u>' + Number(val / 100).toFixed(2) + "元" + '</u></a>';
                            }
                        },
                        {
                            title: '公众号提现',
                            align: 'center',
                            field: 'weixinmpWithdraw',
                            width: 40,
                            formatter: function (val, row) {
                                return Number(val / 100).toFixed(2) + "元";
                            }
                        },
                        {
                            title: '生活号收入',
                            align: 'center',
                            field: 'alipayfwIncome',
                            width: 40,
                            formatter: function (val, row) {
                                return '<a href="javascript:view_alipayfw_pay_order(' + row.partnerId +',\''+ row.statsDate + '\')"><u>' + Number(val / 100).toFixed(2) + "元" + '</u></a>';
                            }
                        },
                        {
                            title: '生活号支出',
                            align: 'center',
                            field: 'alipayfwRefund',
                            width: 40,
                            formatter: function (val, row) {
                                return '<a href="javascript:view_alipayfw_pay_order_refund(' + row.partnerId +',\''+ row.statsDate + '\')"><u>' + Number(val / 100).toFixed(2) + "元" + '</u></a>';
                            }
                        },
                        {
                            title: '生活号提现',
                            align: 'center',
                            field: 'alipayfwWithdraw',
                            width: 40,
                            formatter: function (val, row) {
                                return Number(val / 100).toFixed(2) + "元";
                            }
                        },
                        {
                            title: '微信收入',
                            align: 'center',
                            field: 'weixinIncome',
                            width: 40,
                            formatter: function (val, row) {
                                return '<a href="javascript:view_weixin_pay_order(' + row.partnerId +',\''+ row.statsDate + '\')"><u>' + Number(val / 100).toFixed(2) + "元" + '</u></a>';
                            }
                        },
                        {
                            title: '微信支出',
                            align: 'center',
                            field: 'weixinRefund',
                            width: 40,
                            formatter: function (val, row) {
                                return '<a href="javascript:view_weixin_pay_order_refund(' + row.partnerId +',\''+ row.statsDate + '\')"><u>' + Number(val / 100).toFixed(2) + "元" + '</u></a>';
                            }
                        },
                        {
                            title: '微信提现',
                            align: 'center',
                            field: 'weixinWithdraw',
                            width: 40,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2) + "元";
                            }
                        },
                        {
                            title: '支付宝收入',
                            align: 'center',
                            field: 'alipayIncome',
                            width: 40,
                            formatter: function (val, row) {
                                return '<a href="javascript:view_alipay_pay_order(' + row.partnerId +',\''+ row.statsDate + '\')"><u>' + Number(val / 100).toFixed(2) + "元" + '</u></a>';
                            }
                        },
                        {
                            title: '支付宝支出',
                            align: 'center',
                            field: 'alipayRefund',
                            width: 40,
                            formatter: function (val, row) {
                                return '<a href="javascript:view_alipay_pay_order_refund(' + row.partnerId +',\''+ row.statsDate + '\')"><u>' + Number(val / 100).toFixed(2) + "元" + '</u></a>';
                            }
                        },
                        {
                            title: '支付宝提现',
                            align: 'center',
                            field: 'alipayWithdraw',
                            width: 40,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2) + "元";
                            }
                        },
                    ]
                ],
                onLoadSuccess:function(){
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                }
            });
        });

        function reload(){
            var datagrid=$('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var statsDate = $('#statsDate').datebox('getValue');
            var queryParams = {
                statsDate: statsDate
            };

            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function view_weixinmp_pay_order(partnerId, statsDate) {
            App.dialog.show({
                css: 'width:1000px;height:680px;overflow:visible;',
                title: '公众号收入订单',
                href: "${contextPath}/security/basic/weixinmp_pay_order/view_weixinmp_pay_order.htm?partnerId="+partnerId+"&statsDate='"+statsDate+"'"
            });
        }

        function view_weixinmp_pay_order_refund(partnerId, statsDate) {
            App.dialog.show({
                css: 'width:1000px;height:680px;overflow:visible;',
                title: '公众号支出订单',
                href: "${contextPath}/security/basic/weixinmp_pay_order_refund/view_weixinmp_pay_order_refund.htm?partnerId="+partnerId+"&statsDate='"+statsDate+"'"
            });
        }

        function view_alipayfw_pay_order(partnerId, statsDate) {
            App.dialog.show({
                css: 'width:1000px;height:680px;overflow:visible;',
                title: '生活号收入订单',
                href: "${contextPath}/security/basic/alipayfw_pay_order/view_alipayfw_pay_order.htm?partnerId="+partnerId+"&statsDate='"+statsDate+"'"
            });
        }

        function view_alipayfw_pay_order_refund(partnerId, statsDate) {
            App.dialog.show({
                css: 'width:1000px;height:680px;overflow:visible;',
                title: '生活号支出订单',
                href: "${contextPath}/security/basic/alipayfw_pay_order_refund/view_alipayfw_pay_order_refund.htm?partnerId="+partnerId+"&statsDate='"+statsDate+"'"
            });
        }

        function view_weixin_pay_order(partnerId, statsDate) {
            App.dialog.show({
                css: 'width:1000px;height:680px;overflow:visible;',
                title: '微信收入订单',
                href: "${contextPath}/security/basic/weixin_pay_order/view_weixin_pay_order.htm?partnerId="+partnerId+"&statsDate='"+statsDate+"'"
            });
        }

        function view_weixin_pay_order_refund(partnerId, statsDate) {
            App.dialog.show({
                css: 'width:1000px;height:680px;overflow:visible;',
                title: '微信支出订单',
                href: "${contextPath}/security/basic/weixin_pay_order_refund/view_weixin_pay_order_refund.htm?partnerId="+partnerId+"&statsDate='"+statsDate+"'"
            });
        }

        function view_alipay_pay_order(partnerId, statsDate) {
            App.dialog.show({
                css: 'width:1000px;height:680px;overflow:visible;',
                title: '支付宝收入订单',
                href: "${contextPath}/security/basic/alipay_pay_order/view_alipay_pay_order.htm?partnerId="+partnerId+"&statsDate='"+statsDate+"'"
            });
        }

        function view_alipay_pay_order_refund(partnerId, statsDate) {
            App.dialog.show({
                css: 'width:1000px;height:680px;overflow:visible;',
                title: '支付宝支出订单',
                href: "${contextPath}/security/basic/alipay_pay_order_refund/view_alipay_pay_order_refund.htm?partnerId="+partnerId+"&statsDate='"+statsDate+"'"
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
                            <td align="right">日期：</td>
                            <td><input type="text" class="easyui-datebox" id="statsDate" style="width: 150px;height: 28px;"/>&nbsp;&nbsp;</td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>平台现金流水</h3>
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
