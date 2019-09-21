<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>付款明细</h3>
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
            url: "${contextPath}/security/zc/zc_customer_multi_pay_detail/page.htm?orderId=${(orderId)!''}",
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
                        title: '订单ID',
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
                        title: '支付类型',
                        align: 'center',
                        field: 'payTypeName',
                        width: 30
                    },
                    {
                        title: '支付时间',
                        align: 'center',
                        field: 'payTime',
                        width: 40
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'status',
                        width: 30,
                        formatter: function (val) {
                            if (val == 1) {
                                return "未支付";
                            }else if (val == 2) {
                                return "已支付";
                            }
                        }
                    },{
                    title: '创建时间',
                    align: 'center',
                    field: 'createTime',
                    width: 40
                },
                ]
            ]
        });
    })();
</script>


