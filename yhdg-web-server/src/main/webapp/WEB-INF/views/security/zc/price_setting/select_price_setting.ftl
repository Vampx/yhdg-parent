<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="query_price_setting" >搜索</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td align="right">运营商：</td>
                <td>
                    <input id="agent_id_${pid}" class="easyui-combotree" editable="false"
                           style="width: 130px;height: 28px;"
                           data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200'
                            "
                    >
                </td>
                <td width="80" align="right">车辆名称：</td>
                <td><input type="text" class="text" id="vehicle_name_${pid}"/></td>
            </tr>
        </table>
    </div>
    <div style="width:960px; height:520px; padding-top: 6px;">
        <table id="page_table_price_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    (function() {
        var datagrid = $('#page_table_price_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/zc/price_setting/page.htm?agentId=${agentId}",
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
                    { field: 'checkbox', checkbox: true },
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
                        title: '电池型号',
                        align: 'center',
                        field: 'batteryTypeName',
                        width: 40
                    }
                ]
            ],
            onLoadSuccess:function() {
                $('#page_table_price_${pid}').datagrid('clearChecked');
                $('#page_table_price_${pid}').datagrid('clearSelections');
            }
        });
        function reload() {
            var datagrid = $('#page_table_price_${pid}');
            datagrid.datagrid('reload');
        }
        $('#query_price_setting').click(function() {
            query_price_setting();
        });
        function query_price_setting() {
            var datagrid = $('#page_table_price_${pid}');
            var vehicleName = $('#vehicle_name_${pid}').val();
            datagrid.datagrid('options').queryParams = {
                agentId: $('#agent_id_${pid}').combotree('getValue'),
                vehicleName: vehicleName
            };
            datagrid.datagrid('load');
        }
        (function() {
            var pid = '${pid}',
                    win = $('#' + pid);
            win.find('button.ok').click(function () {
                var priceSettings = $('#page_table_price_${pid}').datagrid('getChecked');
                if (priceSettings.length == 0){
                    $.messager.alert("提示信息", '请选择套餐');
                }else {
                    var ids = [];
                    for (var i = 0; i < priceSettings.length; i++) {
                        ids.push(priceSettings[i].id);
                    }
                    $.post("${contextPath}/security/zc/shop_price_setting/create.htm",{
                        ids: ids,
                        shopId: ${shopId}
                    }, function (json) {
                        if (json.success) {
                            win.window('close');
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
            win.find('button.close').click(function() {
                win.window('close');
            });
        })();
    })();

</script>


