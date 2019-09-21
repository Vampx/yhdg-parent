<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>退款订单</h3>
    </div>
    <div class="grid" style="height:325px;">
        <table id="page_table_box_${pid}"></table>
    </div>
</div>

<script>

    (function () {
        $('#page_table_box_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            url: "${contextPath}/security/hdg/packet_period_order_refund/page.htm?id=${(id)!''}",
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
                        title: '退款金额(元)',
                        align: 'center',
                        field: 'refundMoney',
                        width: 30,
                        formatter: function (val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '客户姓名',
                        align: 'center',
                        field: 'customerFullname',
                        width: 40
                    },
                    {
                        title: '退款人',
                        align: 'center',
                        field: 'refundOperator',
                        width: 40
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'refundStatusName',
                        width: 40
                    },
                    {
                        title: '退款时间',
                        align: 'center',
                        field: 'createTime',
                        width: 40
                    },
                    {
                        title: '备注',
                        align: 'center',
                        field: 'memo',
                        width: 40
                    }
                ]
            ]
        });
    })();

    function view_detailed(id, num) {
        App.dialog.show({
            css: 'width:325px;height:377px;overflow:visible;',
            title: '查看',
            href: "${contextPath}/security/basic/customer_foregift_refund_detailed/view.htm?id=" + id + "&num=" + num

        });
    }
</script>


