<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>电池充电记录</h3>
    </div>
    <div class="grid" style="height:340px;">
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
                        title: '柜子名称/箱号',
                        align: 'center',
                        field: 'cabinetName',
                        width: 80,
                        formatter: function(val, row) {
                            var cabinetName = (row.cabinetName == null || row.cabinetName == '') ? '' : row.cabinetName;
                            var boxNum = (row.boxNum == null || row.boxNum == '') ? '' : row.boxNum;
                            return ''+ cabinetName +'/'+ boxNum +'';
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

</script>