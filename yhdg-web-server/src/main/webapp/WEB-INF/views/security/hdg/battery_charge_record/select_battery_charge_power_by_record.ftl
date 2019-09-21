<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">
            <tr>
                <button class="btn btn_yellow" onclick="view_line_chart()">折线图</button>
            </tr>
        </div>
        <h3>订单功率详情</h3>
    </div>
    <div class="grid" style="height:430px;">
        <table id="page_table_box_${pid}"></table>
    </div>
</div>

<script>
    (function () {
        var win = $('#${pid}');
        $('#page_table_box_${pid}').datagrid({
            fit:true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/battery_charge_record_detail/page.htm?id=${(entity.id)!''}",
            fitColumns: true,
            pageSize: 10,
            pageList: [50,100],
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '编号',
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
                            return Number(val / 100).toFixed(2) + "A";
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
    })();

    function view_line_chart() {
        App.dialog.show({
            css: 'width:852px;height:520px;overflow:visible;',
            title: '折线图',
            href: "${contextPath}/security/hdg/battery_charge_record_detail/view_line_chart.htm?id=${(entity.id)!0}",
        });
    }

</script>