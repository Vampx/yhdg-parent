<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>在线统计信息</h3>
    </div>
    <div class="grid" style="height:340px;">
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
            url: "${contextPath}/security/zc/vehicle_online_stats/page.htm?vehicleId=${(vehicleId)!''}",
            fitColumns: true,
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {field: 'checkbox', checkbox: true},
                    {
                        title: '车架号',
                        align: 'center',
                        field: 'vinNo',
                        width: 50
                    },
                    {
                        title: '开始时间',
                        align: 'center',
                        field: 'beginTime',
                        width: 40
                    },
                    {
                        title: '结束时间',
                        align: 'center',
                        field: 'endTime',
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