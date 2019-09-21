<div class="tab_item" style="display: block;">
    <div class="toolbar clearfix">
        <div class="float_right">
        </div>
        <h3>已绑定电池类型</h3>
    </div>
    <div class="grid" style="height:425px;">
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
            url: "${contextPath}/security/basic/agent_battery_type/page.htm?agentId=${agentId}&cabinetId=${cabinetId}",
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
                        title: '类型名称',
                        align: 'center',
                        field: 'typeName',
                        width: 80
                    },
                    {
                        title: '额定电压(V)',
                        align: 'center',
                        field: 'ratedVoltage',
                        width: 80,
                        formatter: function (val) {
                            return Number(val / 1000);
                        }
                    },
                    {
                        title: '额定容量(Ah)',
                        align: 'center',
                        field: 'ratedCapacity',
                        width: 80,
                        formatter: function (val) {
                            return Number(val / 1000);
                        }
                    }
                ]
            ]
        });
    })();
    function reload_${pid}() {
        var datagrid = $('#page_table_box_${pid}');
        datagrid.datagrid('reload');
    }


    $('#${pid}').data('ok', function() {
        return true;
    });

</script>