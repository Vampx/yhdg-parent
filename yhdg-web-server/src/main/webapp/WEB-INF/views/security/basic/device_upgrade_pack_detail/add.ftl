<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_red" id="ok_${pid}">点击添加</button>
        </div>
        <h4>
            <div>
                设备编号：
                <input style="width: 100px" class="text" id="code_${pid}" type="text">&nbsp;&nbsp;
                版本号：
                <input style="width: 100px" class="text" id="version_${pid}" type="text">&nbsp;&nbsp;
                <button class="btn btn_yellow" id="query_${pid}">搜索</button>

            </div>
        </h4>
    </div>
    <div style="width:700px; height:310px; padding-top: 6px;">
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
                url: "${contextPath}/security/basic/device/page.htm?terminalDetailFlag=1",
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
                            field: 'checkbox', checkbox: true
                        },
                        {
                            title: '设备编号',
                            align: 'center',
                            field: 'deviceId',
                            width: 180
                        },
                        {
                            title: '类型',
                            align: 'center',
                            field: 'typeName',
                            width: 180
                        },
                        {
                            title: '当前版本',
                            align: 'center',
                            field: 'version',
                            width: 180
                        }
                    ]
                ],
                onLoadSuccess:function() {
                    datagrid.datagrid('clearChecked');
                    datagrid.datagrid('clearSelections');
                }
            });

            $('#query_${pid}').click(function() {
                datagrid.datagrid('options').queryParams = {
                    deviceId: $('#code_${pid}').val(),
                    version:$('#version_${pid}').val()
                };
                datagrid.datagrid('load');
            });

            $('#ok_${pid}').click(function() {
                var checked = datagrid.datagrid('getChecked');
                if(checked.length > 0) {
                    windowData.ok(checked);
                    win.window('close');
                } else {
                    $.messager.alert('提示信息', '请选择设备');
                }

            });


            $('#close_${pid}').click(function() {
                $('#${pid}').window('close');
            });

        })();

    </script>


</div>