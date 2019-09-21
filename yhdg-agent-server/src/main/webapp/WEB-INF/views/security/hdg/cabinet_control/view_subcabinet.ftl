<div class="tab_item" style="display: block">
    <div class="grid" style="height:480px;">
        <table id="page_table_box_${pid}"></table>
    </div>
</div>
<script>
    (function () {
        $('#page_table_box_${pid}').datagrid({
            fit: true,
            url: "${contextPath}/security/hdg/cabinet_control/sbucabinet_page.htm?cabinetId=${cabinetId}&viewFlag=${viewFlag}",
            fitColumns: true,
            pagination: false,
            idField: 'cabinetId',
            columns: [
                [
                    {
                        title: '换电柜编号',
                        align: 'center',
                        field: 'cabinetId',
                        width: 60
                    },
                    {
                        title: '换电柜编号',
                        align: 'center',
                        field: 'id',
                        width: 60
                    },
                    {
                        title: '换电柜名称',
                        align: 'center',
                        field: 'subcabinetName',
                        width: 60
                    },
                    {
                        title: '版本',
                        align: 'center',
                        field: 'version',
                        width: 60
                    },
                    {
                        title: '类型',
                        align: 'center',
                        field: 'subtype',
                        width: 60,
                        formatter: function(val) {
                            if (val == 1) {
                                return '换电';
                            } else if (val == 2) {
                                return '换/充电';
                            }
                        }
                    },
                    {
                        title: '启用/在线',
                        align: 'center',
                        field: 'isActive',
                        width: 60,
                        formatter: function(val, row) {
                            var isActive = row.activeStatus == 1 ? '是' : '否';
                            var isOnline = row.isOnline == 1 ? '<a style="color: #00FF00;">是</a>' : '<a style="color: #ff0000;">否</a>';
                            return isActive + "/" + isOnline;
                        }
                    },
                    {
                        title: '最后连接',
                        align: 'center',
                        field: 'heartTime',
                        width: 60
                    },
                    {
                        title: '信号',
                        align: 'center',
                        field: 'currentSignal',
                        width: 40
                    }
                ]
            ]
        });
    })();

</script>