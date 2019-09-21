<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>电池充电记录</h3>
    </div>
    <div class="grid" style="height:600px;">
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
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/battery_charge_record/page.htm?batteryId=" + '${(batteryId)!''}',
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
                        title: '运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 30
                    },
                    {
                        title: '电池编号',
                        align: 'center',
                        field: 'batteryId',
                        width: 30
                    },
                    {
                        title: '充电类型',
                        align: 'center',
                        field: 'typeName',
                        width: 30

                    },
                    {
                        title: '电量',
                        align: 'center',
                        field: 'beginVolume',
                        width: 30,
                        formatter: function(val, row) {
                            var beginVolume = row.beginVolume == null ? 0 : row.beginVolume;
                            var currentVolume = row.currentVolume == null ? 0 : row.currentVolume;
                            return '' + beginVolume +'->' + currentVolume + '';
                        }
                    },
                    {
                        title: '开始时间',
                        align: 'center',
                        field: 'beginTime',
                        width: 30
                    },
                    {
                        title: '结束时间',
                        align: 'center',
                        field: 'endTime',
                        width: 30
                    },
                    {
                        title: '计划时长',
                        align: 'center',
                        field: 'duration',
                        width: 30
                    },
                    {
                        title: '客户名称',
                        align: 'center',
                        field: 'customerFullname',
                        width: 30
                    },
                    {
                        title: '客户手机',
                        align: 'center',
                        field: 'customerMobile',
                        width: 30
                    },
                    {
                        title: '柜子名称/箱号',
                        align: 'center',
                        field: 'cabinetName',
                        width: 80,
                        formatter: function(val, row) {
                            var cabinetName = (row.cabinetName == null || row.cabinetName == '') ? '' : row.cabinetName;
                            var boxNum = (row.boxNum == null || row.boxNum == '') ? '' : row.boxNum;
                            return ''+ cabinetName +'/'+ boxNum +'';
                        }
                    },
                    {
                        title: '维护员',
                        align: 'center',
                        field: 'keeperName',
                        width: 30
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 30,
                        formatter: function(val, row) {
                            var html = '<a href="javascript:view(\ID\)">功率</a>';
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

        var ok = function () {
            return true;
        };

        win.data('ok', ok);
    })();

    function view(id) {
        App.dialog.show({
            css: 'width:800px;height:510px;overflow:visible;',
            title: '折线图',
            href: "${contextPath}/security/hdg/battery_charge_record/select_battery_charge_power_by_record.htm?id=" + id
        });
    }

</script>