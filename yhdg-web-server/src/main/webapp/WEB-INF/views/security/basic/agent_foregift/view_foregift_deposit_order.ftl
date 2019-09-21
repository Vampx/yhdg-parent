<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">

        </div>
        <h3>运营商押金充值信息</h3>
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
            url: "${contextPath}/security/basic/agent_foregift_deposit_order/page.htm?agentId=${id}&category=${category}",
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
                        title: '运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 40
                    },
                    {
                        title: '业务类型',
                        align: 'center',
                        field: 'categoryName',
                        width: 40
                    },
                    {
                        title: '金额',
                        align: 'center',
                        field: 'money',
                        width: 40,
                        formatter:function(val) {
                            return val / 100;
                        }
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'statusName',
                        width: 40
                    },
                    {
                        title: '支付方式',
                        align: 'center',
                        field: 'payTypeName',
                        width: 40
                    },
                    {
                        title: '处理时间',
                        align: 'center',
                        field: 'handleTime',
                        width: 30
                    },
                    {
                        title: '操作人',
                        align: 'center',
                        field: 'operator',
                        width: 40
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
                        width: 50
                    }
                ]
            ]
        });
    })();

</script>