<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">
        </div>
        <h3>租车信息</h3>
    </div>
    <div class="grid" style="height:325px;">
        <table id="page_table_vehicle_order_${pid}"></table>
    </div>
</div>


<script>

    (function () {
        var win = $('#${pid}');
        $('#page_table_vehicle_order_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/shop_vehicle_order/page.htm?vehicleId=${(vehicleId)!''}",
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
                    {title: '客户名称', align: 'center', field: 'customerMobile', width: 20},
                    {title: '客户电话', align: 'center', field: 'customerFullname', width: 20},
                    {
                        title: '开始时间',
                        align: 'center',
                        field: 'beginTime',
                        width: 30,
                        formatter: function (value) {
                            if (value) {
                                return "<span title='" + value + "'>" + value + "</span>";
                            } else {
                                return "";
                            }
                        }
                    },
                    {
                        title: '结束时间',
                        align: 'center',
                        field: 'endTime',
                        width: 30,
                        formatter: function (value) {
                            if (value) {
                                return "<span title='" + value + "'>" + value + "</span>";
                            } else {
                                return "";
                            }
                        }
                    },
                    {
                        title: '套餐',
                        align: 'center',
                        field: 'settingId',
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
                        width: 20,
                        formatter: function(val, row) {
                            return val / 100;
                        }
                    }
                ]
            ]
        });
    })();

    $('#${pid}').data('ok', function() {
        return true;
    });


</script>