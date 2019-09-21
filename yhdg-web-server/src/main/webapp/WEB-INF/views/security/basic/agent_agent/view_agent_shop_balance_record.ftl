<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">

        </div>
        <h3>运营商门店余额结算</h3>
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
            url: "${contextPath}/security/basic/agent_shop_balance_record/page.htm?agentId=${id}",
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
                        width: 30
                    },
                    {
                        title: '门店名称',
                        align: 'center',
                        field: 'shopName',
                        width: 20
                    },
                    {
                        title: '结算金额',
                        align: 'center',
                        field: 'money',
                        width: 20,
                        formatter:function(val) {
                            return val / 100;
                        }
                    },
                    {
                        title: '租金开始时间',
                        align: 'center',
                        field: 'beginTime',
                        width: 30
                    },
                    {
                        title: '租金结束时间',
                        align: 'center',
                        field: 'endTime',
                        width: 30
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
                        width: 30
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 15,
                        formatter: function(val, row) {
                            var html = '';
                            html += '<a href="javascript:view_record(ID)">查看</a>';
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ]
        });
    })();

    function view_record(id) {
        App.dialog.show({
            css: 'width:1100px;height:600px;overflow:visible;',
            title: '运营商门店余额结算明细',
            href: "${contextPath}/security/basic/agent_shop_balance_record/view_agent_shop_balance_record.htm?id=" + id,
            event: {
                onClose: function() {
                }
            }
        });
    }

</script>