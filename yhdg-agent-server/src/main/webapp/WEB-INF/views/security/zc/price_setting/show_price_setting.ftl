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
            url: "${contextPath}/security/zc/price_setting/find_not_shop_price_page.htm?agentId=${agentId}",
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
                        width: 60
                    },
                    {
                        title: '套餐名称',
                        align: 'center',
                        field: 'settingName',
                        width: 40
                    },
                    {
                        title: '类型',
                        align: 'center',
                        field: 'categoryName',
                        width: 40
                    },
                    {
                        title: '车辆名称',
                        align: 'center',
                        field: 'vehicleName',
                        width: 40
                    },
                    {
                        title: '类型',
                        align: 'center',
                        field: 'modelName',
                        width: 40
                    },
                    {
                        title: '电池型号',
                        align: 'center',
                        field: 'batteryTypeName',
                        width: 40
                    },
                    {
                        title: '电池数',
                        align: 'center',
                        field: 'batteryCount',
                        width: 40
                    }
                ]
            ],
            onLoadSuccess:function() {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            },onClickRow:  function(rowIndex, rowData) {
                select_${pid}(rowData);
            }
        });
    })();

    function select_${pid}(priceSetting) {
        var win = $('#${pid}');
        $('#setting_name').val(priceSetting.settingName);
        $('#vehicle_name').val(priceSetting.vehicleName);
        $('#model_name').val(priceSetting.modelName);
        $('#model_id').val(priceSetting.modelId);
        $('#battery_count').val(priceSetting.batteryCount);
        $('#category').val(priceSetting.category);
        $('#price_setting_id').val(priceSetting.id);
        $('#price_setting_name').val(priceSetting.settingName);
        win.window('close');
    }

    $('#close_${pid}').click(function() {
        $('#${pid}').window('close');
    });

    $('#query_${pid}').click(function() {
        queryPriceSetting();
    });

    function queryPriceSetting() {
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid('options').queryParams = {
            vehicleName: $('#vehicle_name_${pid}').val()
        };
        datagrid.datagrid('load');
    }
    var win = $('#${pid}');
    $('#delete_${pid}').click(function() {
        $('#setting_name').val("");
        $('#vehicle_name').val("");
        $('#model_name').val("");
        $('#model_id').val("");
        $('#battery_count').val("");
        $('#category').val("");
        $('#price_setting_id').val("");
        $('#price_setting_name').val("");
        win.window('close');
    });
</script>