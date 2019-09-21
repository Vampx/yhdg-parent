<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="query_${pid}">搜索</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td  width="60" align="right">运营商：</td>
                <td >
                    <input name="agentId" id="agent_id" class="easyui-combotree" editable="false" style="width: 200px;height: 28px;"
                           data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                                            method:'get',
                                                            valueField:'id',
                                                            textField:'text',
                                                            editable:false,
                                                            multiple:false,
                                                            panelHeight:'200',
                                                            onClick: function(node) {
                                                               reloadTree();
                                                            }
                                                        "
                    >
                </td>
                <td align="right" width="80">车辆编号：</td>
                <td><input type="text" class="text" id="id" /></td>
            </tr>
        </table>
    </div>
    <div style="width:800px; height:360px; padding-top: 6px;">
        <table id="page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
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
            url: "${contextPath}/security/hdg/shop_vehicle/page.htm",
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
                        width: 60
                    },
                    {
                        title: '车辆编号',
                        align: 'center',
                        field: 'id',
                        width: 60
                    },
                    {
                        title: '车型名称',
                        align: 'center',
                        field: 'modelName',
                        width: 60
                    },
                    {
                        title: '车型编号',
                        align: 'center',
                        field: 'modelCode',
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
        var vehicle = datagrid.datagrid('getSelected');
        if(vehicle) {
            windowData.ok({
                vehicle: vehicle
            });
            win.window('close');
        } else {
            $.messager.alert('提示信息', '请选择一个车辆');
        }
    }

    $('#close_${pid}').click(function() {
        $('#${pid}').window('close');
    });
    $('#query_${pid}').click(function() {
        query();
    });

    function query() {
        datagrid.datagrid('options').queryParams = {
            agentId: $('#agent_id').combotree('getValue'),
            id: $('#id').val()
        };
        datagrid.datagrid('load');
    }
</script>

