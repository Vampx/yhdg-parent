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
            url: "${contextPath}/security/zc/vehicle_battery_order/page_for_balance.htm?agentId=${agentId!""}&balanceDate=${balanceDate!""}",
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
                        width: 20
                    },
                    {
                        title: '手机',
                        align: 'center',
                        field: 'customerMobile',
                        width: 30
                    },
                    {
                        title: '电池编号',
                        align: 'center',
                        field: 'batteryId',
                        width: 20
                    },
                    {
                        title: '取电柜/箱号',
                        align: 'center',
                        field: 'takeCabinetName',
                        width: 45,
                        formatter: function (val, row) {
                            var takeCabinetName = (row.takeCabinetName == null || row.takeCabinetName == '') ? '' : row.takeCabinetName;
                            var takeCabinetId = (row.takeCabinetId == null || row.takeCabinetId == '') ? '' : row.takeCabinetId;
                            var takeBoxNum = (row.takeBoxNum == null || row.takeBoxNum == '') ? '' : row.takeBoxNum;
                            return takeCabinetName + '(' + takeCabinetId + ')/' + takeCabinetName + takeBoxNum;
                        }
                    },
                    {
                        title: '放电柜/箱号',
                        align: 'center',
                        field: 'putCabinetName',
                        width: 45,
                        formatter: function (val, row) {
                            var putCabinetName = (row.putCabinetName == null || row.putCabinetName == '') ? '' : row.putCabinetName;
                            var putCabinetId = (row.putCabinetId == null || row.putCabinetId == '') ? '' : row.putCabinetId;
                            var putBoxNum = (row.putBoxNum == null || row.putBoxNum == '') ? '' : row.putBoxNum;
                            return putCabinetName + '(' + putCabinetId + ')/' + putCabinetName + putBoxNum;
                        }
                    },
                    {
                        title: '金额',
                        align: 'center',
                        field: 'money',
                        width: 20,
                        formatter: function (val) {
                            return Number(val / 100).toFixed(2) + " 元";
                        }
                    },
                    {
                        title: '支付时间',
                        align: 'center',
                        field: 'payTime',
                        width: 40
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