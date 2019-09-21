<div class="popup_body">
    <div style="width:960px; height:520px; padding-top: 6px;">
        <table id="page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>
<script>
    (function() {
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/weixinmp_pay_order/page.htm",
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
                        title: '支付流水id',
                        align: 'center',
                        field: 'id',
                        width: 40
                    },
                    {
                        title: '订单单号',
                        align: 'center',
                        field: 'sourceId',
                        width: 40
                    },
                    {
                        title: '客户支付码',
                        align: 'center',
                        field: 'paymentId',
                        width: 50
                    },
                    {
                        title: '客户名称',
                        align: 'center',
                        field: 'customerName',
                        width: 25
                    },
                    {
                        title: '客户手机号',
                        align: 'center',
                        field: 'mobile',
                        width: 40
                    },
                    {
                        title: '支付金额(元)',
                        align: 'center',
                        field: 'money',
                        width: 25,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '订单类型',
                        align: 'center',
                        field: 'sourceTypeName',
                        width: 40
                    },
                    {
                        title: '订单状态',
                        align: 'center',
                        field: 'statusName',
                        width: 20
                    },
                    {
                        title: '备注',
                        align: 'center',
                        field: 'memo',
                        width: 40
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
                        width: 40
                    }
                ]
            ],
            queryParams: {
                partnerId: ${partnerId},
                statsDate: ${statsDate}
            }
        });
        $('#close_${pid}').click(function() {
            $('#${pid}').window('close');
        });
        function reload() {
            var datagrid = $('#page_table_${pid}');
            datagrid.datagrid('reload');
        }
    })();

</script>


