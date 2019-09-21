<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="query_${pid}">搜索</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td align="right" width="70">套餐名称：</td>
                <td><input type="text" class="text" id="price_setting_name_${pid}"  /></td>
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
            url: "${contextPath}/security/zc/shop_price_setting/find_by_shop_page.htm?shopId=${shopId}",
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
                        title: '门店名称',
                        align: 'center',
                        field: 'shopName',
                        width: 60
                    },
                    {
                        title: '套餐名称',
                        align: 'center',
                        field: 'settingName',
                        width: 60
                    },
                    {
                        title: '类型',
                        align: 'center',
                        field: 'category',
                        width: 60,
                        formatter: function (val) {
                            if(val == 1) {
                                return '换电';
                            }else if(val == 2){
                                return '租电';
                            }else if(val == 3){
                                return '不租电';
                            }
                        }
                    },
                    {
                        title: '车辆名称',
                        align: 'center',
                        field: 'vehicleName',
                        width: 60
                    },
                    {
                        title: '车辆型号',
                        align: 'center',
                        field: 'modelName',
                        width: 60
                    },
                    {
                        title: '启用',
                        align: 'center',
                        field: 'isActive',
                        width: 60,
                        formatter:function (val) {
                            if(val == 0) {
                                return '否';
                            }else if(val == 1){
                                return '是';
                            }
                        }
                    },
                    {
                        title: '电池数',
                        align: 'center',
                        field: 'batteryCount',
                        width: 60
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
        $('#price_setting_id').val(priceSetting.priceSettingId);
        $('#price_setting_name').val(priceSetting.settingName);
        $('#vin_no').val("");
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
            settingName: $('#price_setting_name_${pid}').val()
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
        $('#vin_no').val("");
        $('#price_setting_id').val("");
        $('#price_setting_name').val("");
        win.window('close');
    });
</script>