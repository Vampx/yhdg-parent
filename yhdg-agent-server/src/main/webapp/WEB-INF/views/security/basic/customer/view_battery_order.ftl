<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">

        </div>
        <h3>换电记录</h3>
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
            url: "${contextPath}/security/hdg/battery_order/page.htm?customerId=${customerId}",
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
                        title: '换电订单ID',
                        align: 'center',
                        field: 'id',
                        width: 30,
                        formatter : function(value, row) {
                            if (row.id != null) {
                                return '<span title="'+row.id+'">' + value + '</span>';
                            }
                        }
                    },
                    {
                        title: '取电柜名称/箱号',
                        align: 'center',
                        field: 'takeCabinetName',
                        width: 50,
                        formatter: function(val, row) {
                            var takeCabinetName = (row.takeCabinetName == null || row.takeCabinetName == '') ? '' : row.takeCabinetName;
                            var takeBoxNum = (row.takeBoxNum == null || row.takeBoxNum == '') ? '' : row.takeBoxNum;
                            return ''+ takeCabinetName +'/' + takeBoxNum +'';
                        }
                    },
                    {
                        title: '放电柜名称/箱号',
                        align: 'center',
                        field: 'putCabinetName',
                        width: 50,
                        formatter: function(val, row) {
                            var putCabinetName = (row.putCabinetName == null || row.putCabinetName == '') ? '' : row.putCabinetName;
                            var putBoxNum = (row.putBoxNum == null || row.putBoxNum == '') ? '' : row.putBoxNum;
                            return ''+ putCabinetName +'/' + putBoxNum +'';
                        }
                    },
                    {
                        title: '金额(元)',
                        align: 'center',
                        field: 'money',
                        width: 15,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '支付类型',
                        align: 'center',
                        field: 'payTypeName',
                        width: 15
                    },
                    {
                        title: '取出时间',
                        align: 'center',
                        field: 'takeTime',
                        width: 30,
                        formatter : function(value, row) {
                            if (row.takeTime != null) {
                                return '<span title="'+row.takeTime+'">' + value + '</span>';
                            }
                        }
                    },
                ]
            ],
            onLoadSuccess:function() {
                $('#page_table').datagrid('clearChecked');
                $('#page_table').datagrid('clearSelections');
            }
        });
    })();

</script>