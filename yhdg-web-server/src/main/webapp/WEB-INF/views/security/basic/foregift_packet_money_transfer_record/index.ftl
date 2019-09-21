<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">

        </div>
        <h3>换电转让记录</h3>
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
            url: "${contextPath}/security/basic/foregift_packet_money_transfer_record/page.htm?customerId=${customerId}",
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
                        title: '转让客户姓名',
                        align: 'center',
                        field: 'transferCustomerFullname',
                        width: 30
                    },
                    {
                        title: '转让客户手机',
                        align: 'center',
                        field: 'transferCustomerMobile',
                        width: 30
                    },
                    {
                        title: '转让押金(元)',
                        align: 'center',
                        field: 'foregiftMoney',
                        width: 30,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '押金订单编号',
                        align: 'center',
                        field: 'foregiftOrderId',
                        width: 30,
                        formatter : function(value, row) {
                            if (row.foregiftOrderId != null) {
                                return '<span title="'+row.foregiftOrderId+'">' + value + '</span>';
                            }
                        }
                    },
                    {
                        title: '租金订单编号',
                        align: 'center',
                        field: 'packetPeriodOrderId',
                        width: 30,
                        formatter : function(value, row) {
                            if (row.packetPeriodOrderId != null) {
                                return '<span title="'+row.packetPeriodOrderId+'">' + value + '</span>';
                            }
                        }
                    },
                    {
                        title: '换电订单编号',
                        align: 'center',
                        field: 'batteryOrderId',
                        width: 30,
                        formatter : function(value, row) {
                            if (row.batteryOrderId != null) {
                                return '<span title="'+row.batteryOrderId+'">' + value + '</span>';
                            }
                        }
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'id',
                        width: 30,
                        formatter: function(val, row) {
                            var html = '';
                            html += '<a href="javascript:view_transfer_record(ID)">查看</a>';
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ],
            onLoadSuccess:function() {
                $('#page_table').datagrid('clearChecked');
                $('#page_table').datagrid('clearSelections');
            }
        });
    })();

    function view_transfer_record(id) {
        App.dialog.show({
            css: 'width:360px;height:435px;overflow:visible;',
            title: '查看',
            href: "${contextPath}/security/basic/foregift_packet_money_transfer_record/view.htm?id=" + id,
        });
    }

</script>
