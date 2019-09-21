<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>换车记录</h3>
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
            url: "${contextPath}/security/hdg/switch_vehicle_record/page.htm?orderId=${(orderId)!''}",
            fitColumns: true,
            idField: 'id',
            pagination: true,
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
                        field: 'orderId',
                        width: 40
                    },
                    {
                        title: '车辆编号',
                        align: 'center',
                        field: 'modelId',
                        width: 30
                    },
                    {
                        title: '车型编号',
                        align: 'center',
                        field: 'modelCode',
                        width: 30
                    },
                    {
                        title: '车型名称',
                        align: 'center',
                        field: 'modelName',
                        width: 30
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
                        width: 40
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