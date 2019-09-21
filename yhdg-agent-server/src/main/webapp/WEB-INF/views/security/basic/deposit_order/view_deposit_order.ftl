<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">
        </div>
        <h3>充值记录</h3>
    </div>
    <div class="grid" style="height:345px;">
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
            url: "${contextPath}/security/basic/deposit_order/page.htm?customerId=${customerId}",
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
                        title: '金额',
                        align: 'center',
                        field: 'money',
                        width: 20,
                        formatter:function(val) {
                            return val / 100;
                        }
                    },
                    {
                        title: '类型',
                        align: 'center',
                        field: 'bizType',
                        width: 20
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
                        width: 30
                    },
                    {
                        title: '处理时间',
                        align: 'center',
                        field: 'handleTime',
                        width: 30
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'status',
                        width: 20,
                        formatter: function(val, row) {
                            if(row.failReason) {
                                return row.failReason;
                            } else {
                                return val
                            }
                        }
                    },
                ]
            ]
        });
    })();

</script>