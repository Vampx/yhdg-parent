<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>订单明细</h3>
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
            url: "${contextPath}/security/zc/zc_customer_multi_order_detail/page.htm?orderId=${(orderId)!''}",
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
                        title: '订单Id',
                        align: 'center',
                        field: 'orderId',
                        width: 30
                    },
                    {
                        title: '金额',
                        align: 'center',
                        field: 'money',
                        width: 30,
                        formatter: function (val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '订单类型',
                        align: 'center',
                        field: 'sourceTypeName',
                        width: 30
                    },
                    {
                        title: 'num',
                        align: 'center',
                        field: 'num',
                        width: 30
                    }
                ]
            ]
        });
    })();
</script>


