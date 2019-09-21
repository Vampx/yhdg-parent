<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">
        </div>
        <h3>操作日志</h3>
    </div>
    <div class="grid" style="height:370px;">
        <table id="page_table_${pid}"></table>
    </div>
</div>

<script>
    (function () {
        var win = $('#${pid}');

        $('#page_table_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            url: "${contextPath}/security/basic/balance_transfer_order_log/list.htm?orderId=${entity.id}",
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
                        title: '操作人',
                        align: 'center',
                        field: 'operatorName',
                        width: 40
                    },
                    {
                        title: '内容',
                        align: 'center',
                        field: 'content',
                        width: 300
                    },
                    {
                        title: '操作时间',
                        align: 'center',
                        field: 'createTime',
                        width: 100
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