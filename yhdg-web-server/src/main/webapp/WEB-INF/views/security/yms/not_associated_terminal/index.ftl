<@app.html>
    <@app.head>
    <script>
        $(function() {
            $('#page_table').datagrid({
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
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                columns: [
                    [
                        {
                            title: '终端ID',
                            align: 'center',
                            field: 'id',
                            width: 20,
                            formatter: function (val) {
                                var html = '<a href="javascript:relevanceCabinet(\'ID\', 4)"  style="color: red">'+ val + '</a>';
                                return html.replace(/ID/g, val);
                            }
                        },
                        {title: '版本', align: 'center', field: 'version', width: 30},
                        {title: 'uid', align: 'center', field: 'uid', width: 30},
                        {title: '策略', align: 'center', field: 'strategyName', width: 30},
                        {title: '播放列表', align: 'center', field: 'playlistName', width: 30},
                        {title: '心跳时间', align: 'center', field: 'heartTime', width: 40},
                        {title: '在线', align: 'center', field: 'isOnline', width: 20,
                            formatter: function(val, row) {
                                return val ? '<a style="color: #00FF00;">是</a>' : '<a style="color: #ff0000;">否</a>'
                            }
                        }
                    ]
                ],
                onLoadSuccess:function() {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                }
            });
        });

        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();
            var isOnline = $('#is_online').combobox('getValue');
            var queryParams = {
                isOnline: isOnline
            };

            queryParams[queryName] = queryValue;

            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }

        function relevanceCabinet(id) {
            App.dialog.show({
                css: 'width:826px;height:522px;overflow:visible;',
                title: '关联换电柜',
                href: "${contextPath}/security/hdg/cabinet/select_cabinet.htm?terminalId=" + id,
                event: {
                    onClose: function() {
                        query();
                    }
                }
            });
        }

    </script>
    </@app.head>
    <@app.body>
        <@app.container>
            <@app.banner/>
            <div class="main">
                <@app.menu/>
                <div class="content">
                    <div class="panel search">
                        <div class="float_right">
                            <button class="btn btn_yellow" onclick="query()">搜索</button>
                        </div>
                        <table cellpadding="0" cellspacing="0" border="0"">
                            <tr>
                                <td align="right">在线：</td>
                                <td>
                                    <select type="text" id="is_online" class="text easyui-combobox"  name="isOnline" style="width:120px;height: 28px " >
                                        <option value="">所有</option>
                                        <option value="1">是</option>
                                        <option value="0">否</option>
                                    </select>
                                </td>
                                <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                <td>
                                    <select style="width:100px;" id="query_name">
                                        <option value="id">Id</option>
                                        <option value="version">版本</option>
                                    </select>
                                </td>
                                <td><input type="text" class="text" id="query_value"/></td>

                            </tr>
                        </table>
                    </div>
                    <div class="panel grid_wrap">
                        <div class="toolbar clearfix">
                            <h3>未关联终端</h3>
                        </div>
                        <div class="grid">
                            <table id="page_table"></table>
                        </div>
                    </div>
                </div>
            </div>
        </@app.container>
    </@app.body>
</@app.html>
