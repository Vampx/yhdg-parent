<div class="grid" style="height: 460px;margin: 5px; ">
    <table id="page_table_${pid}"></table>
</div>

<script>
    $(function () {
        var win = $('#${pid}');
        $('#page_table_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/zc/vehicle_rent_period_order/page.htm?orderRefundId=${orderRefundId!""}&status=0",
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
                        title: '订单id',
                        align: 'center',
                        field: 'id',
                        width: 25
                    },
                    {
                        title: '运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 25
                    },
                    {
                        title: '姓名',
                        align: 'center',
                        field: 'customerFullname',
                        width: 20
                    },
                    {
                        title: '手机号',
                        align: 'center',
                        field: 'customerMobile',
                        width: 20
                    },
                    {
                        title: '电池类型',
                        align: 'center',
                        field: 'batteryTypeName',
                        width: 20
                    },
                    {
                        title: '支付类型',
                        align: 'center',
                        field: 'payTypeName',
                        width: 20
                    },
                    {
                        title: '支付金额(元)',
                        align: 'center',
                        field: 'money',
                        width: 30,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '开始时间',
                        align: 'center',
                        field: 'beginTime',
                        width: 30
                    },

                    {
                        title: '过期时间',
                        align: 'center',
                        field: 'endTime',
                        width: 30
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'statusName',
                        width: 20
                    }
                ]
            ]
        })
    });
</script>