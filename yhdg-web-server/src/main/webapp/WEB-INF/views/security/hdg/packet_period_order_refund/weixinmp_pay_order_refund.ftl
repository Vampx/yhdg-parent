<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>公众号退款订单</h3>
    </div>
    <div class="grid" style="height:325px;">
        <table id="page_table_box_${pid}"></table>
    </div>
</div>

<script>

    (function () {
        $('#page_table_box_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            url: "${contextPath}/security/hdg/packet_period_order_refund/weixinmp_pay_order_page_refund.htm?orderRefundId=${orderRefundId}",
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
                        title: '公众号支付流水id',
                        align: 'center',
                        field: 'id',
                        width: 40
                    },
                    {
                        title: '手机号码',
                        align: 'center',
                        field: 'mobile',
                        width: 20
                    },
                    {
                        title: '支付金额',
                        align: 'center',
                        field: 'money',
                        width: 20,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '退款金额',
                        align: 'center',
                        field: 'refundMoney',
                        width: 20,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '退款时间',
                        align: 'center',
                        field: 'refundTime',
                        width: 50
                    },
                    {
                        title: '订单状态',
                        align: 'center',
                        field: 'statusName',
                        width: 20
                    },
                    {
                    title: '操作',
                    align: 'center',
                    field: 'action',
                    width: 20,
                    formatter: function(val, row) {
                        var html = '<a href="javascript:view_${pid}(\'ID\')">查看</a>';
                        return html.replace(/ID/g, row.id);
                    }
                }
                ]
            ]
        });
    })();

    function view_${pid}(id) {
        App.dialog.show({
            css: 'width:560px;height:260px;overflow:visible;',
            title: '查看',
            href: "${contextPath}/security/basic/weixinmp_pay_order/view.htm?id=" + id

        });
    }

</script>


