<div style="flex: 1;box-sizing: border-box;padding-left: 10px;width: 100%">
    <div style="height:520px; padding: 6px 0 12px 0; width: 100%">
        <table id="station_in_out_money" style="height: 80%;"></table>
    </div>
</div>
<script>

    function showStationInOutMoneyTable() {
        $('#station_in_out_money').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/station_in_out_money/page.htm?stationId=${(entity.id)!''}",
            fitColumns: true,
            pageSize: 10,
            pageList: [10, 50, 100],
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '日期',
                        align: 'center',
                        field: 'createTime',
                        width: 30
                    },
                    {
                        title: '业务类型',
                        align: 'center',
                        field: 'bizTypeName',
                        width: 40
                    },
                    {
                        title: '类型',
                        align: 'center',
                        field: 'typeName',
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
                        title: '剩余金额',
                        align: 'center',
                        field: 'balance',
                        width: 40,
                        formatter:function(val) {
                            return val / 100;
                        }
                    },
                    {
                        title: '操作人',
                        align: 'center',
                        field: 'operator',
                        width: 40
                    }
                ]
            ],
            onLoadSuccess: function () {
                $('#station_in_out_money').datagrid('clearChecked');
                $('#station_in_out_money').datagrid('clearSelections');
            }
        });
    }
</script>
