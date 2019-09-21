<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="query_${pid}">搜索</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0"">
        <tr>
            <td align="right">在线：</td>
            <td>
                <select type="text" id="is_online_${pid}" class="text easyui-combobox" style="width:120px;height: 28px " >
                    <option value="">所有</option>
                    <option value="1">是</option>
                    <option value="0">否</option>
                </select>
            </td>
            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td>
                <select style="width:100px;" id="query_name_${pid}">
                    <option value="id">Id</option>
                    <option value="version">版本</option>
                </select>
            </td>
            <td><input type="text" class="text" id="query_value_${pid}"/></td>

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
    <button class="btn btn_red" id="ok_${pid}">确定</button>
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>
<script>
    (function () {
        var win = $('#${pid}'), windowData = win.data('windowData');
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/yms/not_associated_terminal/page.htm",
            fitColumns: true,
            pageSize: 10,
            pageList: [10, 50, 100],
            idField: 'id',
            singleSelect: true,
            selectOnCheck: true,
            checkOnSelect: true,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: 'checkbox', checkbox: true
                    },
                    {
                        title: '终端ID',
                        align: 'center',
                        field: 'id',
                        width: 20
                    },
                    {title: '版本', align: 'center', field: 'version', width: 30},
                    {title: 'uid', align: 'center', field: 'uid', width: 30},
                    {title: '策略', align: 'center', field: 'strategyName', width: 30},
                    {title: '播放列表', align: 'center', field: 'playlistName', width: 30},
                    {title: '心跳时间', align: 'center', field: 'heartTime', width: 40},
                    {
                        title: '在线', align: 'center', field: 'isOnline', width: 20,
                        formatter: function (val, row) {
                            return val ? '<a style="color: #00FF00;">是</a>' : '<a style="color: #ff0000;">否</a>'
                        }
                    }
                ]
            ],
            onLoadSuccess: function () {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            }
        });


        function reload() {
            var datagrid = $('#page_table_${pid}');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table_${pid}');
            var queryName = $('#query_name_${pid}').val();
            var queryValue = $('#query_value_${pid}').val();
            var isOnline = $('#is_online_${pid}').combobox('getValue');
            var queryParams = {
                isOnline: isOnline
            };

            queryParams[queryName] = queryValue;

            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }


        $('#ok_${pid}').click(function () {
            var notAssociatedTerminal = datagrid.datagrid('getSelected');
            if (notAssociatedTerminal) {
                notAssociatedTerminal = {
                    id: notAssociatedTerminal.id,
                };
                windowData.ok({
                    notAssociatedTerminal: notAssociatedTerminal
                });
                win.window('close');
            } else {
                $.messager.alert('提示信息', '请选择广告终端');
            }

        });

        $('#close_${pid}').click(function () {
            $('#${pid}').window('close');
        });

        $('#query_${pid}').click(function () {
            query();
        });

        $('#delete_${pid}').click(function () {
            var notAssociatedTerminal = datagrid.datagrid('getSelected');
            notAssociatedTerminal = {
                id: "",
            };
            windowData.ok({
                notAssociatedTerminal: notAssociatedTerminal
            });
            win.window('close');
        });
    })();


</script>


