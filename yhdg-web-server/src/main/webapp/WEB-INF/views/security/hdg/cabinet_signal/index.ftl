<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>信号记录</h3>
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
            url: "${contextPath}/security/hdg/cabinet_signal/page.htm?cabinetId=${cabinetId}",
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
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
                        width: 40
                    },
                    {
                        title: '当前信号',
                        align: 'center',
                        field: 'currentSignal',
                        width: 30
                    },
                    {
                        title: '序号',
                        align: 'center',
                        field: 'num',
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