<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="query_${pid}">搜索</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td align="right">运营商：</td>
                <td>
                    <input id="agentId_${pid}" class="easyui-combotree" editable="false"
                           style="width:150px;height: 28px;"
                           data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                }
                            "
                    >
                </td>
                <td align="right" style="width: 80px">换电柜编号：</td>
                <td><input id="cabinetId_${pid}" type="text" class="text"/></td>
                <td align="right" style="width: 80px">换电柜名称：</td>
                <td><input id="cabinetName_${pid}" type="text" class="text"/></td>
                <td align="right">日期：</td>
                <td><input type="text" class="easyui-datebox" id="statsDate_${pid}"/></td>
            </tr>
        </table>
    </div>
    <div style="width:960px; height:520px; padding-top: 6px;">
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
            url: "${contextPath}/security/hdg/cabinet_day_degree_stats/page.htm",
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
                        field: 'statsDate',
                        width: 40
                    },
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
                        title: '开始时间',
                        align: 'center',
                        field: 'beginTime',
                        width: 40
                    },
                    {
                        title: '结束时间',
                        align: 'center',
                        field: 'endTime',
                        width: 40
                    },
                    {
                        title: '开始度数',
                        align: 'center',
                        field: 'beginNum',
                        width: 30,
                        formatter: function (val) {
                            return Number(val/100);
                        }
                    },
                    {
                        title: '结束度数',
                        align: 'center',
                        field: 'endNum',
                        width: 30,
                        formatter: function (val) {
                            return Number(val/100);
                        }
                    },
                    {
                        title: '用电总计',
                        align: 'center',
                        field: 'num',
                        width: 30,
                        formatter: function (val) {
                            return Number(val/100);
                        }
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
        $('#query_${pid}').click(function() {
            query();
        });
        function query() {
            var datagrid = $('#page_table_${pid}');
            var cabinetName = $('#cabinetName_${pid}').val();
            var cabinetId = $('#cabinetId_${pid}').val();
            var statsDate = $('#statsDate_${pid}').datebox('getValue');
            datagrid.datagrid('options').queryParams = {
                agentId: $('#agentId_${pid}').combotree('getValue'),
                cabinetName: cabinetName,
                cabinetId: cabinetId,
                statsDate: statsDate
            };
            datagrid.datagrid('load');
        }
    })();

</script>


