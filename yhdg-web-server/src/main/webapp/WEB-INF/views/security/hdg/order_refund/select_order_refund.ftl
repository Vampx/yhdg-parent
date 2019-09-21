<div class="ui_table" style="padding: 5px;">
    <table cellpadding="0" cellspacing="0" border="0">
        <tbody>
        <tr>
            <td align="right" width="60">手机号：</td>
            <td><input type="text" class="text" style="width:120px" id="customer_mobile" /></td>
            <td align="right" width="50">姓名：</td>
            <td><input type="text" class="text" style="width:120px" id="customer_fullname" /></td>
            <td align="right" width="50">类型：</td>
            <td>
                <select style="width:90px;" id="type" >
                    <option value="0">所有</option>
                    <#list typeEnum as e>
                        <option value="${e.getValue()}" >${e.getName()}</option>
                    </#list>
                </select>
            </td>

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
            url: "${contextPath}/security/hdg/order_refund/page_for_balance.htm?agentId=${agentId!""}&balanceDate=${balanceDate!""}",
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
                        title: '客户名称',
                        align: 'center',
                        field: 'customerFullname',
                        width: 30
                    },
                    {
                        title: '手机',
                        align: 'center',
                        field: 'customerMobile',
                        width: 40
                    },
                    {
                        title: '退款类型',
                        align: 'center',
                        field: 'typeName',
                        width: 30
                    },
                    {
                        title: '订单',
                        align: 'center',
                        field: 'sourceId',
                        width: 40
                    },
                    {
                        title: '订单金额(元)',
                        align: 'center',
                        field: 'money',
                        width: 30,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '退款金额(元)',
                        align: 'center',
                        field: 'refundMoney',
                        width: 30,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '退款时间',
                        align: 'center',
                        field: 'refundTime',
                        width: 60
                    },
                    {
                        title: '退款人',
                        align: 'center',
                        field: 'refundOperator',
                        width: 30
                    }
                ]
            ]
        })
    });


    function query_order() {
        var datagrid = $('#page_table_${pid}');
        var queryParams = {
            customerFullname: $('#customer_fullname').val(),
            customerMobile: $('#customer_mobile').val(),
            type: $('#type').val()
        };
        datagrid.datagrid('options').queryParams = queryParams;
        datagrid.datagrid('load');
    }

</script>