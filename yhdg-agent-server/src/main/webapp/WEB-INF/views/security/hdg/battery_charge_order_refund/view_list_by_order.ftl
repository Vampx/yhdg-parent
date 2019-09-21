<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>订单退款</h3>
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
            url: "${contextPath}/security/hdg/battery_charge_order_refund/page.htm?id=${(orderId)!''}",
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
                        field: 'id',
                        width: 30
                    },
                    {
                        title: '姓名',
                        align: 'center',
                        field: 'customerFullname',
                        width: 30
                    },
                    {
                        title: '手机',
                        align: 'center',
                        field: 'customerMobile',
                        width: 40
                    },
                    {
                        title: '退款状态',
                        align: 'center',
                        field: 'refundStatus',
                        width: 30,
                        formatter: function(val, row){
                        <#list faultTypeEnum as e>
                            if(${e.getValue()} == val)
                            return '${e.getName()}';
                        </#list>
                        }
                    },
                    {
                        title: '退款金额',
                        align: 'center',
                        field: 'money',
                        width: 30,
                        formatter: function(val, row) {
                            return new Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '退款人',
                        align: 'center',
                        field: 'refundOperator',
                        width: 40
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