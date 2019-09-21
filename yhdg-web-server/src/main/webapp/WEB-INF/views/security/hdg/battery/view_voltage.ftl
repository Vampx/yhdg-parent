<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>电池单体电压</h3>
    </div>
    <div class="grid" style="height:345px;">
        <table id="page_table_box_${pid}"></table>
    </div>
</div>

<script>
    $(function () {
        var win = $('#${pid}');
        var ok = function () {
            var success = true;
            return success;
        };
        win.data('ok', ok);

        $('#page_table_box_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            fitColumns: true,
            idField: 'id',
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '电池串数ID',
                        align: 'center',
                        field: 'id',
                        width: 40
                    },
                    {
                        title: '电压',
                        align: 'center',
                        field: 'voltage',
                        width: 40,
                        formatter: function (val) {
                            return Number(val / 1000).toFixed(3) + "V";
                        }
                    }
                ]
            ],
            data: [
            <#list voltages as voltage>
                {id: ${voltage_index}+1, voltage: ${voltage}},
            </#list>
            ]
        });

    });
</script>