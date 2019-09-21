<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">

        </div>
        <h3>设备信号记录</h3>
    </div>
    <div class="grid" style="height:340px;">
        <table id="page_table_box_${pid}"></table>
    </div>
</div>


<script>

    (function () {
        $('#page_table_box_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/battery_signal/page.htm?batteryId=${batteryId}",
            pageSize: 10,
            pageList: [10, 50, 100],
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
                        title: '电池编号',
                        align: 'center',
                        field: 'batteryId',
                        width: 40
                    },
                    {
                        title: '开始时间',
                        align: 'center',
                        field: 'createTime',
                        width: 40
                    },
                    {
                        title: '信号',
                        align: 'center',
                        field: 'currentSignal',
                        width: 30
                    }
                ]
            ]
        });
    })();

</script>