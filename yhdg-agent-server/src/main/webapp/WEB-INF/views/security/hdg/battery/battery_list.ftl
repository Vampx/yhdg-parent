<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>电池列表</h3>
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
            url: "${contextPath}/security/hdg/battery/page.htm?subcabinetId=${(subcabinetId)!''}",
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
                        title: '电池编号',
                        align: 'center',
                        field: 'id',
                        width: 60
                    },
                    {
                        title: '品牌',
                        align: 'center',
                        field: 'brandName',
                        width: 60
                    },
                    {
                        title: '电池类型',
                        align: 'center',
                        field: 'batteryType',
                        width: 60
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