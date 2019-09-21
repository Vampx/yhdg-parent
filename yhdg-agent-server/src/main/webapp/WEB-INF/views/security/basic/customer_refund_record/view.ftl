<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>退款订单</h3>
    </div>
    <div class="grid" style="height:325px;">
        <table id="page_table_box"></table>
    </div>
</div>

<script>

    (function () {
        $('#page_table_box').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            url: "${contextPath}/security/basic/customer_refund_record/page.htm?sourceId=${(sourceId)!''}&sourceType=${sourceType}",
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
                        title: '商户单号',
                        align: 'center',
                        field: 'ptPayOrderId',
                        width: 100
                    },
                    {
                        title: '订单Id',
                        align: 'center',
                        field: 'orderId',
                        width: 60
                    },
                    {
                        title: '退款方式',
                        align: 'center',
                        field: 'refundType',
                        width: 30,
                        formatter: function (val) {
                            if (val == 2) {
                                return '原路退回';
                            } else if (val == 1) {
                                return '退到余额';
                            }
                        }
                    },
                    {
                        title: '退款金额',
                        align: 'center',
                        field: 'refundMoney',
                        width: 30,
                        formatter: function (val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '退款时间',
                        align: 'center',
                        field: 'refundTime',
                        width: 55
                    },
                    {
                        title: '备注',
                        align: 'center',
                        field: 'moneyInfo',
                        width: 50
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'statusName',
                        width: 30
                    }
                ]
            ]
        });
    })();
</script>


