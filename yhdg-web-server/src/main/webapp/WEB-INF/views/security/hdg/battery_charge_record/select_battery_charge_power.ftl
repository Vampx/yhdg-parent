<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>功率详情</h3>
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
            url: "${contextPath}/security/hdg/battery_charge_record_detail/page.htm?id=${(entity.id)!''}",
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
                        title: '订单编号',
                        align: 'center',
                        field: 'id',
                        width: 30
                    },
                    {
                        title: '当前功率',
                        align: 'center',
                        field: 'currentPower',
                        width: 30,
                        formatter: function (val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '上报时间',
                        align: 'center',
                        field: 'reportTime',
                        width: 30
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