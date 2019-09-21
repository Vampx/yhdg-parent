<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>电池充电记录详情</h3>
    </div>
    <div class="grid" style="height:325px;">
        <table id="page_table_detail_${pid}"></table>
    </div>
</div>
<script>
    (function () {
        $('#page_table_detail_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            url: "${contextPath}/security/hdg/battery_charge_record/detail_list_page.htm?id=${(id)!0}",
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
                        title: 'id',
                        align: 'center',
                        field: 'id',
                        width: 30
                    },
                    {
                        title: '上报时间',
                        align: 'center',
                        field: 'reportTime',
                        width: 40
                    },
                    {
                        title: '当前电量',
                        align: 'center',
                        field: 'currentVolume',
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
                    }
                ]
            ]
        });
    })();
</script>