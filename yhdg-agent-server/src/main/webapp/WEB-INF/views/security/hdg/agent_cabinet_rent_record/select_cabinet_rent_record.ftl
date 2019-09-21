<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_red" id="ok_${pid}">导出数据</button>
        </div>
        <h4>
            <div>
                月份：
                <input style="width: 100px" class="text" id="query_time_${pid}" type="text">&nbsp;&nbsp;
                <button class="btn btn_yellow" id="query_${pid}">搜索</button>
            </div>
        </h4>
    </div>
    <div style="width:960px; height:465px; padding-top: 6px;">
        <table id="page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>
<script>
    (function() {
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/agent_cabinet_rent_record/page.htm?materialDayStatsId=${materialDayStatsId}",
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
                        title: '运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 30
                    },
                    {
                        title: '换电柜Id',
                        align: 'center',
                        field: 'cabinetId',
                        width: 40
                    },
                    {
                        title: '换电柜名称',
                        align: 'center',
                        field: 'cabinetName',
                        width: 40
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'status',
                        width: 30,
                        formatter: function (val) {
                            if (val == 1) {
                                return '未支付';
                            } else if (val == 2) {
                                return '已支付';
                            }
                        }
                    },
                    {
                        title: '金额',
                        align: 'center',
                        field: 'money',
                        width: 40,
                        formatter: function (val) {
                            return Number(val / 100).toFixed(2) + "元";
                        }
                    },
                    {
                        title: '支付时间',
                        align: 'center',
                        field: 'payTime',
                        width: 50
                    },
                    {
                        title: '开始时间',
                        align: 'center',
                        field: 'beginTime',
                        width: 50
                    },
                    {
                        title: '结束时间',
                        align: 'center',
                        field: 'endTime',
                        width: 50
                    }
                ]
            ]
        });
        $('#close_${pid}').click(function() {
            $('#${pid}').window('close');
        });
        function reload() {
            var datagrid = $('#page_table_${pid}');
            datagrid.datagrid('reload');
        }
    })();

</script>


