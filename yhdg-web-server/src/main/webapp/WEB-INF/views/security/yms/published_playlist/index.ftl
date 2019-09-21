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
                url: "${contextPath}/security/yms/published_playlist/page.htm",
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
                            title: '播放列表ID',
                            align: 'center',
                            field: 'playlistId',
                            width: 40
                        },
                        {
                            title: '播放列表名称',
                            align: 'center',
                            field: 'playlistName',
                            width: 40
                        },
                        {
                            title: '版本',
                            align: 'center',
                            field: 'version',
                            width: 40
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 70
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='6_3_2_2'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                queryParams: {
                    agentId: $('#agent_id').combotree('getValue'),
                },
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
            var agentId = $('#agent_id').combotree("getValue");
            var playlistName = $('#playlistName').val();
            datagrid.datagrid('options').queryParams = {
                agentId: agentId,
                playListName: playlistName
            };
            datagrid.datagrid('load');
        }
        function view(id) {
            App.dialog.show({
                options:'maximized:true',
                title: '查看',
                href: "${contextPath}/security/yms/published_playlist/view.htm?id=" + id
            });
        }

    </script>
    </@app.head>
    <@app.body>
        <@app.container>
            <@app.banner/>
        <div class="main">
            <@app.menu/>
            <div class="splitter"><span class="split_left"></span></div>
            <div class="content">
                <div class="panel search">
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">运营商：</td>
                            <td>
                                <input name="agentId" id="agent_id" class="easyui-combotree" editable="false" style="width: 200px;height: 28px;"
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
                            <td align="right">&nbsp;&nbsp;&nbsp;&nbsp;名称：</td>
                            <td><input type="text" class="text" id="playlistName"/></td>
                        </tr>

                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                        <h3>播放列表</h3>
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
