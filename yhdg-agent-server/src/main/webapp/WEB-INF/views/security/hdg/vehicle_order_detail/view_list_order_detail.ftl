<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>订单明细</h3>
    </div>
    <div class="grid" style="height:325px;">
        <table id="page_table_box_${pid}"></table>
    </div>
</div>

<script>
    (function () {
        var win = $('#${pid}');
        $('#page_table_box_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            url: "${contextPath}/security/hdg/vehicle_order_detail/page.htm?orderId=${(orderId)!''}",
            fitColumns: true,
            idField: 'id',
            pagination: true,
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
                        field: 'orderId',
                        width: 30
                    },
                    {
                        title: '时长',
                        align: 'center',
                        field: 'duration',
                        width: 30
                    },
                    {
                        title: '支付类型',
                        align: 'center',
                        field: 'payTypeName',
                        width: 40
                    },
                    {
                        title: '付款时间',
                        align: 'center',
                        field: 'payTime',
                        width: 50
                    },
                    {
                        title: '订单状态',
                        align: 'center',
                        field: 'statusName',
                        width: 20
                    }
                ]
            ]
        });

        var ok = function () {
            return true;
        };

        win.data('ok', ok);
    })();

</script>