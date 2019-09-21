<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_red" id="ok_${pid}">点击添加</button>
        </div>
        <h4>
            <div>
                设备编号：
                <input style="width: 100px" class="text" id="cabinet_id_${pid}" type="text">&nbsp;&nbsp;
                设备名称：
                <input style="width: 100px" class="text" id="cabinet_name_${pid}" type="text">&nbsp;&nbsp;
                <#--<a class="btn_yellow" id="query_win_5">搜 索</a>-->
                <button class="btn btn_yellow" onclick="page_query()">搜 索</button>
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
                url: "${contextPath}/security/hdg/cabinet/page.htm?terminalDetailFlag=1",
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
                            title: '终端ID',
                            align: 'center',
                            field: 'terminalId',
                            width: 180
                        },
                        {
                            title: '换电柜ID',
                            align: 'center',
                            field: 'id',
                            width: 180
                        },
                        {
                            title: '换电柜名称',
                            align: 'center',
                            field: 'cabinetName',
                            width: 180
                        },
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 180
                        },
                        {
                            title: '地址',
                            align: 'center',
                            field: 'address',
                            width: 180
                        },
                        {
                            title: '在线',
                            align: 'center',
                            field: 'isOnline',
                            width: 180,
                            formatter: function (val, row) {
                                return val == 1 ? '<a style="color: #00FF00;">是</a>' : '<a style="color: #ff0000;">否</a>';
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

        function page_query() {
            var datagrid = $('#page_table_${pid}');
            var cabinetName = $('#cabinet_name_${pid}').val();
            var id = $('#cabinet_id_${pid}').val();
            datagrid.datagrid('options').queryParams = {
                cabinetName: cabinetName,
                id: id
            };
            datagrid.datagrid('load');
        }
    </script>


</div>