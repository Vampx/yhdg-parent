<div class="tab_item" style="display: block">
    <div class="grid" style="height:480px;">
        <table id="page_table_box_${pid}"></table>
    </div>
</div>
<script>
    (function () {
        $('#page_table_box_${pid}').datagrid({
            fit: true,
            url: "${contextPath}/security/hdg/cabinet_control/stats_page.htm?cabinetId=${cabinetId}&viewFlag=${viewFlag}",
            fitColumns: true,
            pagination: false,
            idField: 'cabinetId',
            columns: [
                [
                    {title: '编号', align: 'center', field: 'id', width: 40},
                    {title: '格口编号', align: 'center', field: 'boxNum', width: 40},
                    {
                        title: '格口类型',
                        align: 'center',
                        field: 'batteryTypeName',
                        width: 40,
                        formatter: function (val) {
                            if (val == null) {
                                return "不限";
                            } else {
                                return val;
                            }
                        }
                    },
                    {
                        title: '是否启用',
                        align: 'center',
                        field: 'isActive',
                        width: 40,
                        formatter: function (val) {
                            if (val == 1) {
                                return '启用'
                            } else {
                                return '禁用'
                            }
                        }
                    },
                    {
                        title: '格口状态',
                        align: 'center',
                        field: 'boxStatusName',
                        width: 40
                    },
                    {
                        title: '充电状态',
                        align: 'center',
                        field: 'chargeStatusName',
                        width: 40
                    },
                    {title: '电池编号', align: 'center', field: 'batteryId', width: 60},
                    {
                        title: '是否开箱',
                        align: 'center',
                        field: 'isOpen',
                        width: 40,
                        formatter: function (val) {
                            if (val == 1) {
                                return '是'
                            } else {
                                return '否'
                            }
                        }
                    },
                    {title: '开箱时间', align: 'center', field: 'openTime', width: 60},
                ]
            ]
        });
    })();

</script>