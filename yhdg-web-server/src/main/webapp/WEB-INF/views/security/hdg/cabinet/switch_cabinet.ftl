<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="query_${pid}">搜索</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td align="right">运营商：</td>
                <td>
                    <input id="agent_id" class="easyui-combotree" editable="false"
                           style="width: 200px;height: 28px;"
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
                <td align="right" width="100">设备SN：</td>
                <td><input type="text" class="text" id="mac" /></td>
                <td align="right" width="100">换电柜编号：</td>
                <td><input type="text" class="text" id="id" /></td>
                <td align="right" width="100">换电柜名称：</td>
                <td><input type="text" class="text" id="cabinet_name" /></td>
            </tr>
        </table>
    </div>
    <div style="width:1350px; height:360px; padding-top: 6px;">
        <table id="page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>
<script>
    $(function () {
        var win = $('#${pid}'), windowData = win.data('windowData');
        var datagrid = $('#page_table_${pid}');
        $('#page_table_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/cabinet/page.htm",
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
                        title: '所属运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 50
                    },
                    {
                        title: '换电柜编号',
                        align: 'center',
                        field: 'id',
                        width: 50
                    },
                    {title: '换电柜名称', align: 'center', field: 'cabinetName', width: 50},
                    {
                        title: '版本',
                        align: 'center',
                        field: 'version',
                        width: 20
                    },
                    {
                        title: '地址', align: 'center', field: 'address', width: 120
                    },
                    {
                        title: 'SIM卡号',
                        align: 'center',
                        field: 'simMemo',
                        width: 40
                    },
                    {
                        title: '温度',
                        align: 'center',
                        field: 'temp1',
                        width: 30,
                        formatter: function (val, row) {
                            var temp1 = row.temp1;
                            var temp2 = row.temp2;
                            if (temp1 == null) {
                                temp1 = "--"
                            }
                            if (temp2 == null) {
                                temp2 = "--"
                            }
                            return temp1 + "/" + temp2
                        }
                    },
                    {
                        title: '启用/在线',
                        align: 'center',
                        field: 'activeStatus',
                        width: 40,
                        formatter: function (val, row) {
                            var activeStatus = row.activeStatus == 1 ? '是' : '否';
                            var isOnline = row.isOnline == 1 ? '<a style="color: #00FF00;">是</a>' : '<a style="color: #ff0000;">否</a>';
                            return activeStatus + "/" + isOnline;
                        }
                    },
                    {
                        title: '最后连接',
                        align: 'center',
                        field: 'heartTime',
                        width: 70
                    },
                    {
                        title: '上线状态',
                        align: 'center',
                        field: 'upLineStatusName',
                        width: 40
                    },
                    {
                        title: '上线时间',
                        align: 'center',
                        field: 'upLineTime',
                        width: 70
                    }
                ]
            ],
            onLoadSuccess: function () {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            },onClickRow:  function(rowIndex, rowData) {
                select_${pid}(rowData.id);
            }
        });
    });

    function select_${pid}() {
        var win = $('#${pid}'), windowData = win.data('windowData');
        var datagrid = $('#page_table_${pid}');
        var cabinet = datagrid.datagrid('getSelected');
        if(cabinet) {
            windowData.ok({
                cabinet: cabinet
            });
            win.window('close');
        } else {
            $.messager.alert('提示信息', '请选择换电柜');
        }
    }

    $('#close_${pid}').click(function() {
        $('#${pid}').window('close');
    });
    $('#query_${pid}').click(function() {
        query();
    });

    function query() {
        var datagrid = $('#page_table_${pid}');
        var agentId = $('#agent_id').combotree('getValue');
        var mac = $('#mac').val();
        var cabinetName = $('#cabinet_name').val();
        var id = $('#id').val();
        var groupId  = '';
        var queryParams = {
            cabinetName: cabinetName,
            id: id,
            groupId: groupId,
            mac: mac,
            agentId: agentId
        };
        datagrid.datagrid('options').queryParams = queryParams;
        datagrid.datagrid('load');
    }

</script>


