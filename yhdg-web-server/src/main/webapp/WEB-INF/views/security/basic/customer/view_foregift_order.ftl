<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">

        </div>
        <h3>会员押金记录</h3>
    </div>
    <div class="grid" style="height:345px;">
        <table id="page_table_box_${pid}"></table>
    </div>
</div>

<script>

    (function() {
        $('#page_table_box_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/customer_foregift_order/page.htm?customerId=${customerId}",
            fitColumns: true,
            pageSize: 50,
            pageList: [50, 100],
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '编号',
                        align: 'center',
                        field: 'id',
                        width: 30
                    },
                    {
                        title: '金额',
                        align: 'center',
                        field: 'money',
                        width: 20,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'statusName',
                        width: 20
                    },
                    {
                        title: '处理时间',
                        align: 'center',
                        field: 'handleTime',
                        width: 40
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
                        width: 40
                    },
                    {
                        title: '支付类型',
                        align: 'center',
                        field: 'payTypeName',
                        width: 20
                    }
                ]
            ],
            onLoadSuccess:function() {
                $('#page_table').datagrid('clearChecked');
                $('#page_table').datagrid('clearSelections');
            }
        });
    })();

</script>