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
                url: "${contextPath}/security/yms/terminal/page.htm",
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
                            title: 'checkbox', filed: 'id', checkbox: true
                        },
                        {title: '终端ID', align: 'center', field: 'id', width: 30},
                        {title: '换电柜ID', align: 'center', field: 'cabinetId', width: 30},
                        {title: '换电柜名称', align: 'center', field: 'cabinetName', width: 30},
                        {title: '运营商', align: 'center', field: 'agentName', width: 30},
                        {title: '地址', align: 'center', field: 'address', width: 60},
                        {title: '版本', align: 'center', field: 'version', width: 30},
                        {title: '心跳时间', align: 'center', field: 'heartTime', width: 40},
                        {title: '在线', align: 'center', field: 'isOnline', width: 20,
                            formatter: function(val, row) {
                                if (val == 1) {
                                    return '<a style="color: #00FF00;">是</a>';
                                } else {
                                    return '<a style="color: #ff0000;">否</a>';
                                }
                            }
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='yms.Terminal:view'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='yms.Terminal:remove'>
                                    html += ' <a href="javascript:remove(\'ID\')">删除</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
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
            var agentId = $('#agent_id').combotree("getValue");
            var queryParams = {
                agentId: agentId
            };

            queryParams[queryName] = queryValue;

            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:770px;height:580px;',
                title: '修改',
                href: "${contextPath}/security/yms/terminal/edit.htm?id=" + id,
                event: {
                    onClose: function() {
                        var datagrid = $('#page_table');
                        datagrid.datagrid('reload');
                    },
                    onLoad: function() {
                    }
                }
            });
        }
        function view(id) {
            App.dialog.show({
                css: 'width:770px;height:500px;',
                title: '查看',
                href: "${contextPath}/security/yms/terminal/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function(ok) {
                if(ok) {
                    $.post('${contextPath}/security/yms/terminal/delete.htm', {
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

        function add() {
            App.dialog.show({
                css: 'width:340px;height:200px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/yms/terminal/add.htm",
                event: {
                    onClose: function() {
                        var datagrid = $('#page_table');
                        datagrid.datagrid('reload');
                    },
                    onLoad: function() {
                    }
                }
            });
        }

        function selectPlaylists() {
            var terminals = $('#page_table').datagrid('getChecked');
            if(terminals.length >0){
                App.dialog.show({
                    css: 'width:680px;height:530px;',
                    title: '修改',
                    href: "${contextPath}/security/yms/terminal/select_playlists.htm",
                    windowData: {
                        ok: function(rows) {
                            if(rows.length > 0) {
                                var playlistIds = [];
                                for(var i = 0; i < rows.length; i++) {
                                    playlistIds.push(rows[i].playlistId);
                                }
                                var terminalIds = [];
                                for(var j = 0; j < terminals.length; j++) {
                                    terminalIds.push(terminals[j].id);
                                }
                                $.post('${contextPath}/security/yms/terminal/update.htm',{
                                    terminalIds: terminalIds,
                                    playlistIds: playlistIds
                                }, function(json) {
                                    if(json.success) {
                                        $.messager.alert('提示信息', '操作成功', 'info');
                                        reload();
                                    }else{
                                        $.messager.alert('提示消息', json.message, 'info');
                                    }
                                }, 'json');
                                return true;
                            }else {
                                $.messager.alert('提示信息', '请选择播放列表', 'info');
                                return false;
                            }
                        }
                    }
                });
            }else {
                $.messager.alert('提示信息', '请选择终端', 'info');
                return false;
            }
        }

        function selectPlaylist(id) {
            App.dialog.show({
                css: 'width:680px;height:530px;',
                title: '修改',
                href: "${contextPath}/security/yms/terminal/select_playlist.htm?id=" + id,
                windowData: {
                    ok: function(rows) {
                        if(rows.length > 0) {
                            var playlistIds = [];
                            for(var i = 0; i < rows.length; i++) {
                                playlistIds.push(rows[i].playlistId);
                            }
                            var terminalIds = [];
                            terminalIds.push(id);
                            $.post('${contextPath}/security/yms/terminal/update.htm',{
                                terminalIds: terminalIds,
                                playlistIds: playlistIds
                            }, function(json) {
                                if(json.success) {
                                    $.messager.alert('提示信息', '操作成功', 'info');
                                    reload();
                                }else{
                                    $.messager.alert('提示消息', json.message, 'info');
                                }
                            }, 'json');
                            return true;
                        }else {
                            $.messager.alert('提示信息', '请选择播放列表', 'info');
                            return false;
                        }
                    }
                }
            });
        }

        function reloadTree() {
            var agentId = $('#agent_id').combotree('getValue');
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId
            };
            datagrid.datagrid('reload');
        }

        function view_upload_log() {
            App.dialog.show({
                css: 'width:1000px;height:680px;overflow:visible;',
                title: '终端上传日志',
                href: "${contextPath}/security/yms/terminal_upload_log/select_terminal_upload_log.htm"
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
                    <div class="panel search" >
                        <div class="float_right">
                            <button class="btn btn_yellow" onclick="query()">搜索</button>
                        </div>
                        <table cellpadding="0" cellspacing="0" border="0"">
                            <tr>
                                <td align="right">运营商：</td>
                                <td>
                                    <input name="agentId" id="agent_id" class="easyui-combotree" editable="true" style="width:200px;height: 28px;"
                                           data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                                            method:'get',
                                                            valueField:'id',
                                                            textField:'text',
                                                            editable:true,
                                                            multiple:false,
                                                            panelHeight:'200',
                                                            onClick: function(node) {
                                                               reloadTree();
                                                            }
                                                        "
                                    >
                                </td>
                                <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                <td>
                                    <select style="width:100px;" id="query_name">
                                        <option value="cabinetName">柜子名称</option>
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
                            <div class="float_right">
                                <@app.has_oper perm_code='yms.Terminal:uploadLog'>
                                    <button class="btn btn_blue" onclick="view_upload_log()">上传日志</button>
                                </@app.has_oper>
                            </div>
                            <h3>终端</h3>
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
