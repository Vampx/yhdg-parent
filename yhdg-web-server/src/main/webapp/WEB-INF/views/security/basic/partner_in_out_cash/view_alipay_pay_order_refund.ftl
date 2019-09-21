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
            url: "${contextPath}/security/basic/alipay_pay_order_refund/page.htm",
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
                        title: '订单Id',
                        align: 'center',
                        field: 'id',
                        width: 40
                    },
                    {
                        title: '运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 40
                    },
                    {
                        title: '退款金额',
                        align: 'center',
                        field: 'refundMoney',
                        width: 30,
                        formatter: function (val) {
                            return Number(val / 100).toFixed(2) + "元";
                        }
                    },
                    {
                        title: '业务类型',
                        align: 'center',
                        field: 'bizTypeName',
                        width: 40
                    },
                    {
                        title: '手机',
                        align: 'center',
                        field: 'customerMobile',
                        width: 40
                    },
                    {
                        title: '姓名',
                        align: 'center',
                        field: 'customerFullname',
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


