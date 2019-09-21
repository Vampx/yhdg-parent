<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">

        </div>
        <h3>优惠券</h3>
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
            url: "${contextPath}/security/basic/customer_coupon_ticket/page.htm?customerId=${customerId}",
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
                        title: '优惠券名称',
                        align: 'center',
                        field: 'ticketName',
                        width: 30
                    },
                    {
                        title: '金额',
                        align: 'center',
                        field: 'money',
                        width: 25,
                        formatter: function(val, row) {
                            return val / 100;
                        }
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'status',
                        width: 40,
                        formatter: function(val) {
                        <#list statusEnum as e>
                            if (${e.getValue()}== val)
                            return '${e.getName()}';
                        </#list>
                        }
                    },
                    {
                        title: '过期时间',
                        align: 'center',
                        field: 'expireTime',
                        width: 40
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