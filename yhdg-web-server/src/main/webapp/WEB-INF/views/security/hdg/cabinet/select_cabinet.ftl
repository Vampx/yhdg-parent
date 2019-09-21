<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="query_${pid}">搜索</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td align="right">运营商：</td>
                <td >
                    <input id="agent_id_${pid}" class="easyui-combotree" editable="false" style="height: 28px;"
                           data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'auto'
                            "
                    >
                </td>
                <td align="right" width="95">换电柜名称：</td>
                <td><input type="text" class="text" id="cabinet_name_${pid}"/></td>
            </tr>
        </table>
    </div>
    <div style="width:800px; height:360px; padding-top: 6px;">
        <table id="page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red" id="ok_${pid}">确定</button>
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
            url: "${contextPath}/security/hdg/cabinet/page.htm",
            fitColumns: true,
            pageSize: 10,
            pageList: [10, 50, 100],
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: true,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '所属运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 60
                    },
                    {
                        title: '换电柜编号',
                        align: 'center',
                        field: 'id',
                        width: 60
                    },
                    {title: '换电柜名称', align: 'center', field: 'cabinetName', width: 60},
                    {title: '地址', align: 'center', field: 'address', width: 60},
                    {
                        title: '是否启用',
                        align: 'center',
                        field: 'activeStatus',
                        width: 60,
                        formatter: function(val) {
                            if (val == 1) {
                                return '是';
                            } else {
                                return '否';
                            }
                        }
                    },
                    {title: '换电套餐', align: 'center', field: 'priceSettingName', width: 60},
                    {title: '广告终端', align: 'center', field: 'terminalId', width: 60},
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 100,
                        formatter: function(val, row) {
                            var html = '<a href="javascript:view_${pid}(\'ID\')">查看</a>';
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ],
            onLoadSuccess:function() {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            }
        });

        $('#ok_${pid}').click(function() {
            var cabinet = datagrid.datagrid('getSelected');
            if(cabinet) {
                $.post('${contextPath}/security/yms/terminal/relevance_cabinet.htm', {
                    cabinetId: cabinet.id,
                    terminalId: '${(terminalId)!''}'
                }, function (json) {
                    if (json.success) {
                        $.messager.alert('info', '操作成功', 'info');
                        win.window('close');
                    } else {
                        $.messager.alert('提示消息', json.message, 'info');
                    }
                }, 'json');
            } else {
                $.messager.alert('提示信息', '请选择换电柜');
            }

        });

        $('#close_${pid}').click(function() {
            $('#${pid}').window('close');
        });

        $('#query_${pid}').click(function() {
            query();
        });

        function query() {
            datagrid.datagrid('options').queryParams = {
                cabinetName: $('#cabinet_name_${pid}').val(),
                agentId: $('#agent_id_${pid}').combotree('getValue')
            };
            datagrid.datagrid('load');
        }

    })();

    function view_${pid}(id) {
        App.dialog.show({
            css: 'width:786px;height:515px;overflow:visible;',
            title: '查看',
            href: "${contextPath}/security/hdg/cabinet/view.htm?id=" + id
        });
    }

</script>


