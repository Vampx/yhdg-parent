<div class="ui_table" style="padding: 5px;">
    <table cellpadding="0" cellspacing="0" border="0">
        <tbody>
        <tr>
            <td align="right" width="60">手机号：</td>
            <td><input type="text" class="text" style="width:120px" id="customer_mobile" /></td>
            <td align="right" width="50">姓名：</td>
            <td><input type="text" class="text" style="width:120px" id="customer_fullname" /></td>

            <td align="right" width="80">
                <button class="btn btn_yellow" onclick="query_order()">搜索</button>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<div class="grid" style="height: 310px;margin: 5px; ">
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
            url: "${contextPath}/security/zd/rent_period_order/page_for_balance.htm?agentId=${agentId!""}&balanceDate=${balanceDate!""}",
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


    function query_order() {
        var datagrid = $('#page_table_${pid}');
        var queryParams = {
            customerFullname: $('#customer_fullname').val(),
            customerMobile: $('#customer_mobile').val()
        };
        datagrid.datagrid('options').queryParams = queryParams;
        datagrid.datagrid('load');
    }

</script>