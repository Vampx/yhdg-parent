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
                url: "${contextPath}/security/yms/playlist/page.htm",
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
                            field: 'id',
                            width: 40
                        },
                        {
                            title: '播放列表名称',
                            align: 'center',
                            field: 'playlistName',
                            width: 40
                        },
                        {
                            title: '版本号',
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
                            field: 'work',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='yms.Playlist:detail'>
                                    html += ' <a href="javascript:detail(ID)">列表明细</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='yms.Playlist:terminal'>
                                    html += ' <a href="javascript:playlist_terminal(ID)">关联终端</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='yms.Playlist:remove'>
                                    html += ' <a href="javascript:remove(ID)">删除</a>';
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
//            var programName = $('#program_name').val();
            var agentId = $('#agent_id').combotree("getValue");
            datagrid.datagrid('options').queryParams = {
//                programName: programName,
                agentId: agentId
            };
            datagrid.datagrid('load');
        }

        function detail(id) {
            App.dialog.show({
                css: 'width:800px;height:530px;',
                title: '套餐明细',
                href: "${contextPath}/security/yms/playlist_detail/index.htm?playlistId="+id,
                event: {
                    onClose: function() {
                        var datagrid = $('#page_table');
                        datagrid.datagrid('reload');
                    }
                },
                windowData: {
                    playlistId: id
                }
            });
        }

        function playlist_terminal(id) {
            App.dialog.show({
                css: 'width:800px;height:530px;',
                title: '关联终端',
                href: "${contextPath}/security/yms/playlist/playlist_terminal.htm?playlistId="+id,
                event: {
                    onClose: function() {
                        var datagrid = $('#page_table');
                        datagrid.datagrid('reload');
                    }
                },
                windowData: {
                    playlistId: id,

                }
            });
        }

        function add() {
            App.dialog.show({
                css: 'width:380px;height:200px;',
                title: '新建',
                href: "${contextPath}/security/yms/playlist/add.htm",
                event: {
                    onClose: function() {
                        var datagrid = $('#page_table');
                        datagrid.datagrid('reload');
                    }
                }
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function(ok) {
                if(ok) {
                    $.post('${contextPath}/security/yms/playlist/delete.htm', {
                        id: id
                    }, function (json) {
                        if (json.success) {
                            $.messager.alert('info', '操作成功', 'info');
                            reload();
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        }


        function download(id, version) {
            $.messager.progress();
            $.post('${controller.appConfig.staticUrl}/security/playlist/download.htm', {
                playlistId: id,
                version: version
            }, function (json) {
                $.messager.progress('close');
                if (json.success) {
                    document.location.href = '${controller.appConfig.staticUrl}' + json.data;
                } else {
                    $.messager.alert('提示消息', json.message, 'info');
                }
            }, 'json');
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
                                <input name="agentId" id="agent_id" class="easyui-combotree" editable="true" style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                                            method:'get',
                                                            valueField:'id',
                                                            textField:'text',
                                                            editable:true,
                                                            multiple:false,
                                                            panelHeight:'200',
                                                            onClick: function(node) {
                                                            }
                                                        "
                                >
                            </td>
                        </tr>

                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='yms.Playlist:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
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
