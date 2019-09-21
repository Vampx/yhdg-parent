<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>在线统计信息</h3>
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
            url: "${contextPath}/security/hdg/hdg_cabinet_sim_replace_record/page.htm?subcabinetId=${(subcabinetId)!''}",
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
                        title: '换电柜编号',
                        align: 'center',
                        field: 'cabinetId',
                        width: 50
                    },
                    {
                        title: '开始时间',
                        align: 'center',
                        field: 'oldCode',
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