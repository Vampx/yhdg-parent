<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>维护订单</h3>
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
            url: "${contextPath}/security/hdg/keep_order/page.htm?putOrderId=${(putOrderId)!''}",
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
                        title: '电池编号',
                        align: 'center',
                        field: 'batteryId',
                        width: 40
                    },
                    {
                        title: '取电柜',
                        align: 'center',
                        field: 'takeCabinetName',
                        width: 25
                    },
                    {
                        title: '投电柜',
                        align: 'center',
                        field: 'putCabinetName',
                        width: 25
                    },
                    {
                        title: '电量',
                        align: 'center',
                        field: 'takeTimeAndCurrentVolume',
                        width: 20,
                        formatter: function(val, row) {
                            return row.initVolume + '/' + row.currentVolume;
                        }
                    },
                    {
                        title: '订单状态',
                        align: 'center',
                        field: 'orderStatus',
                        width: 20,
                        formatter: function(val){
                        <#list OrderStatusEnum as e>
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