<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>订单明细</h3>
    </div>
    <div class="grid" style="height:325px;">
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
            url: "${contextPath}/security/hdg/battery_charge_order_detail/page.htm?orderId=${(orderId)!''}",
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
                        title: '订单编号',
                        align: 'center',
                        field: 'orderId',
                        width: 30
                    },
                    {
                        title: '时长',
                        align: 'center',
                        field: 'duration',
                        width: 30
                    },
                    {
                        title: '支付类型',
                        align: 'center',
                        field: 'payType',
                        width: 40,
                        formatter: function(val){
                        <#list PayTypeEnum as e>
                            if(${e.getValue()} == val)
                            return '${e.getName()}';
                        </#list>
                        }
                    },
                    {
                        title: '付款时间',
                        align: 'center',
                        field: 'payTime',
                        width: 50
                    },
                    {
                        title: '取消时间',
                        align: 'center',
                        field: 'cancelTime',
                        width: 50
                    },
                    {
                        title: '订单状态',
                        align: 'center',
                        field: 'status',
                        width: 20,
                        formatter: function(val){
                        <#list StatusEnum as e>
                            if(${e.getValue()} == val)
                            return '${e.getName()}';
                        </#list>
                        }
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