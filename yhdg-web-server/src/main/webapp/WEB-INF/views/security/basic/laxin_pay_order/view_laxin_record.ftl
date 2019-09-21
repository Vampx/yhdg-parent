<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">

        </div>
        <h3>拉新记录</h3>
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
            url: "${contextPath}/security/basic/laxin_record/page.htm?orderId=${entity.id}",
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
                        title: '拉新手机号',
                        align: 'center',
                        field: 'laxinMobile',
                        width: 40
                    },
                    {
                        title: '金额',
                        align: 'center',
                        field: 'laxinMoney',
                        width: 20,
                        formatter: function(val, row) {
                            return val / 100 + "元";
                        }
                    },
                    {
                        title: '客户手机号',
                        align: 'center',
                        field: 'targetMobile',
                        width: 40
                    },
                    {
                        title: '客户姓名',
                        align: 'center',
                        field: 'targetFullname',
                        width: 40
                    },
                    {
                        title: '支付类型',
                        align: 'center',
                        field: 'payTypeName',
                        width: 25
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
                        width: 40
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