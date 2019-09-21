<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="query_${pid}">搜索</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td align="right">类型名称：</td>
                <td><input type="text" class="text" id="type_name_${pid}"/>&nbsp;&nbsp;</td>
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
    (function() {
        var win = $('#${pid}'), windowData = win.data('windowData');
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/agent_battery_type/page.htm?agentId=${(agentId)!''}",
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
                        width: 40
                    },
                    {
                        title: '类型名称',
                        align: 'center',
                        field: 'typeName',
                        width: 80
                    },
                    {
                        title: '额定电压(V)',
                        align: 'center',
                        field: 'ratedVoltage',
                        width: 80,
                        formatter: function (val) {
                            return Number(val / 1000);
                        }
                    },
                    {
                        title: '额定容量(Ah)',
                        align: 'center',
                        field: 'ratedCapacity',
                        width: 80,
                        formatter: function (val) {
                            return Number(val / 1000);
                        }
                    },
                    {
                        title: '是否激活',
                        align: 'center',
                        field: 'isActive',
                        width: 60,
                        formatter: function(row) {
                            if(row == 1) {
                                return '<a style="color: #00FF00;">是</a>';
                            }else{
                                return '<a style="color: #ff0000;">否</a>';
                            }
                        }
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
                        width: 60
                    }
                ]
            ],
            onLoadSuccess:function() {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            },onClickRow:  function(rowIndex, rowData) {
                select_${pid}(rowData.id);
            }
        });
    })();

    var win = $('#${pid}'), windowData = win.data('windowData');
    var datagrid = $('#page_table_${pid}');
    function select_${pid}() {
        var agentBatteryType = datagrid.datagrid('getSelected');
        if(agentBatteryType) {
            windowData.getOk({
                agentBatteryType: agentBatteryType
            });
            win.window('close');
        } else {
            $.messager.alert('提示信息', '请选择电池类型');
        }
    }

    $('#close_${pid}').click(function() {
        $('#${pid}').window('close');
    });
    $('#query_${pid}').click(function() {
        queryBatteryType();
    });
    function queryBatteryType() {
        datagrid.datagrid('options').queryParams = {
            typeName: $('#type_name_${pid}').val()
        };
        datagrid.datagrid('load');
    }

    $('#delete_${pid}').click(function() {
        var agentBatteryType = datagrid.datagrid('getSelected');
        agentBatteryType = {
            batteryType: "",
            typeName: ""
        };
        windowData.getOk({
            agentBatteryType:agentBatteryType
        });
        win.window('close');
    });
</script>






