<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">

        </div>
        <h3>客户流水信息</h3>
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
            url: "${contextPath}/security/basic/customer_in_out_money/page.htm?customerId=${customerId}",
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
                        title: '客户名称',
                        align: 'center',
                        field: 'customerName',
                        width: 40
                    },
                    {
                        title: '业务类型',
                        align: 'center',
                        field: 'bizTypeName',
                        width: 70
                    },
                    {
                        title: '收入类型',
                        align: 'center',
                        field: 'typeName',
                        width: 40
                    },
                    {
                        title: '金额',
                        align: 'center',
                        field: 'money',
                        width: 40,
                        formatter: function(val) {
                            return new Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '剩余金额',
                        align: 'center',
                        field: 'balance',
                        width: 40,
                        formatter: function(val) {
                            return new Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '状态信息',
                        align: 'center',
                        field: 'statusInfo',
                        width: 40
                    },
                    {
                        title: '时间',
                        align: 'center',
                        field: 'createTime',
                        width: 70
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'id',
                        width: 30,
                        formatter: function(val, row) {
                            var html = '';
                            html += '<a href="javascript:view_money(ID)">查看</a>';
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ]
        });
    })();

    function view_money(id) {
        App.dialog.show({
            css: 'width:340px;height:425px;overflow:visible;',
            title: '查看',
            href: "${contextPath}/security/basic/customer_in_out_money/view.htm?id=" + id,
        });
    }
</script>