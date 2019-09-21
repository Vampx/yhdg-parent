<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>支付宝退款订单</h3>
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
            url: "${contextPath}/security/zc/vehicle_customer_foregift_order/alipay_pay_order_page_refund.htm?sourceId=${(sourceId)!''}",
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
                        title: '支付宝流水id',
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
                        title: '支付金额(元)',
                        align: 'center',
                        field: 'money',
                        width: 20,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2);
                        }
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
                        var html = '<a href="javascript:view_${pid}(CUSTOMERID, SOURCETYPE,\'SOURCEID\', ORDERSTATUS)">查看</a>';
                        return html.replace(/CUSTOMERID/g, row.customerId).replace(/SOURCETYPE/g, row.sourceType).replace(/SOURCEID/g, row.sourceId).replace(/ORDERSTATUS/g, row.orderStatus);
                    }
                }
                ]
            ]
        });
    })();

    function view_${pid}(customerId, sourceType, sourceId, orderStatus) {
        App.dialog.show({
            css: 'width:580px;height:310px;overflow:visible;',
            title: '查看',
            href: "${contextPath}/security/basic/alipay_pay_order/view.htm?customerId=" + customerId + "&sourceType=" + sourceType + "&sourceId=" +sourceId + "&orderStatus=" + orderStatus

        });
    }

</script>


