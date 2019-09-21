<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="query_${pid}">搜索</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td width="80" align="right">车辆名称：</td>
                <td><input type="text" class="text" id="vehicle_name_${pid}"/></td>
            </tr>
        </table>
    </div>
    <div style="width:800px; height:360px; padding-top: 6px;">
        <table id="page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_yellow" id="delete_${pid}">清空</button>
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>

<script>
    (function () {
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/zc/vehicle/page.htm",
            pageSize: 10,
            pageList: [10, 50, 100],
            fitColumns: true,
            idField: 'vinNo',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '车架号',
                        align: 'center',
                        field: 'vinNo',
                        width: 120
                    },
                    {
                        title: '运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 100
                    },
                    {
                        title: '车辆型号',
                        align: 'center',
                        field: 'modelName',
                        width: 80
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'statusName',
                        width: 80
                    },
                    {
                        title: '在线',
                        align: 'center',
                        field: 'isOnline',
                        width: 45,
                        formatter: function (val, row) {
                            return val == 1 ? '<a style="color: #00FF00;">是</a>' : '<a style="color: #ff0000;">否</a>';
                        }
                    }
                ]
            ],
            onLoadSuccess:function() {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            },onClickRow:  function(rowIndex, rowData) {
                select_${pid}(rowData);
            },queryParams: {
                agentId:${agentId},
                modelId:${modelId},
                status:1
            }
        });
    })();

    function select_${pid}(vehicle) {
        var win = $('#${pid}');
        $('#vin_no').val(vehicle.vinNo);
        win.window('close');
    }

    $('#close_${pid}').click(function() {
        $('#${pid}').window('close');
    });

    $('#query_${pid}').click(function() {
        queryVehicle();
    });

    function queryVehicle() {
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid('options').queryParams = {
            agentId: ${agentId},
            vehicleName: $('#vehicle_name_${pid}').val()
        };
        datagrid.datagrid('load');
    }
    var win = $('#${pid}');
    $('#delete_${pid}').click(function() {
        $('#vin_no').val("");
        win.window('close');
    });
</script>