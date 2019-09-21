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
                url: "${contextPath}/security/yms/terminal_online/page.htm",
                fitColumns: true,
                pageSize: 10,
                pageList: [10, 50, 100],
                idField: 'id',
                singleSelect: false,
                selectOnCheck: true,
                checkOnSelect: true,
                autoRowHeight: true,
                rowStyler: gridRowStyler,
                columns: [
                    [
                        {
                            title: 'checkbox', checkbox: true
                        },
                        {title: '终端ID', align: 'center', field: 'id', width: 40},
                        {title: '换电柜ID', align: 'center', field: 'cabinetId', width: 40},
                        {title: '换电柜名称', align: 'center', field: 'cabinetName', width: 60},
                        {title: '运营商', align: 'center', field: 'agentName', width: 40},
                        {title: '地址', align: 'center', field: 'address', width: 60},
                        {title: '版本', align: 'center', field: 'version', width: 40},
                        {title: 'CPU/内存/硬盘', align: 'center', field: 'cpu', width: 80,
                            formatter: function(val, row) {
                                var html = '<a href="javascript:" >CPU/MEMORY/DISK</a>';
                                html = html.replace(/CPU/g, Number(row.cpu*100).toFixed(0)+ '%');
                                html = html.replace(/MEMORY/g, Number(row.memory*100).toFixed(0)+ '%');
                                var disk;
                                if(row.cardCapacity == null){
                                    disk = "-";
                                }else{
                                    disk = (Number((row.cardCapacity -  row.restCapacity)*100/row.cardCapacity ).toFixed(0)) + '%';
                                }
                               return html = html.replace(/DISK/g,disk);
                            }
                        },
                        {title: '播放音量', align: 'center', field: 'playVolume', width: 30},
                        {title: '在线', align: 'center', field: 'isOnline', width: 25,
                            formatter: function(val, row) {
                                return val ? '<a style="color: #00FF00;">是</a>' : '<a style="color: #ff0000;">否</a>'
                            }
                        },
                        {title: '速度(kb/s)', align: 'center', field: 'speed', width: 40,
                            formatter: function(val, row) {
                                return (Number(row.speed).toFixed(2));
                            }
                        },
                        {title: '下载进度', align: 'center', field: 'downloadProgress', width: 40,
                            formatter: function(val, row) {
                                return (Number(row.downloadProgress*100).toFixed(0) + '%');
                            }
                        },
                        {title: '最后连接', align: 'center', field: 'heartTime', width: 60},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 50,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='6_2_4_7'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                               /* html += ' <a href="javascript:edit(\'ID\')">修改</a>'
                                html += ' <a href="javascript:remove(\'ID\')">删除</a>'*/
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
            var isOnline = $('#is_online').combobox('getValue');
            var groupId = null;
            var group = $('#cabinet_group_tree').tree("getSelected");
            if(group) {
                groupId = group.id
            }
            var agentId = $('#agent_id').combotree("getValue");
            var queryParams = {
                id: $('#id').val(),
                isOnline: isOnline,
                groupId: groupId,
                agentId: agentId
            };

            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:770px;height:510px;',
                title: '查看',
                href: "${contextPath}/security/yms/terminal_online/view.htm?id=" + id
            });
        }

        function restart(){
            $.messager.confirm('提示信息', '确认重启?', function(ok){
                if(ok){
                    var ids = [];
                    var rows= $('#page_table').datagrid('getChecked');
                    for(var i = 0; i < rows.length; i++){
                        ids.push(rows[i].id);
                    }
                    if(ids.length == 0){
                        alert('你没有选择任何内容');
                    } else{
                        $.post('${contextPath}/security/yms/terminal_command/add_restart.htm', {
                            ids: ids
                        }, function (json) {
                            if (json.success) {
                                $.messager.alert('info', '操作成功', 'info');
                                reload();
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
                    }
                }
            });
        }

        function shutdown(){
            $.messager.confirm('提示信息', '确认关机?', function(ok){
                if(ok){
                    var ids = [];
                    var rows= $('#page_table').datagrid('getChecked');
                    for(var i = 0; i < rows.length; i++){
                        ids.push(rows[i].id);
                    }
                    if(ids.length == 0){
                        alert('你没有选择任何内容');
                    } else{
                        $.post('${contextPath}/security/yms/terminal_command/add_shutdown.htm', {
                            ids: ids
                        }, function (json) {
                            if (json.success) {
                                $.messager.alert('info', '操作成功', 'info');
                                reload();
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
                    }
                }
            });
        }

        function initialize(){
            $.messager.confirm('提示信息', '确认初始化?', function(ok){
                if(ok){
                    var ids = [];
                    var rows= $('#page_table').datagrid('getChecked');
                    for(var i = 0; i < rows.length; i++){
                        ids.push(rows[i].id);
                    }
                    if(ids.length == 0){
                        alert('你没有选择任何内容');
                    } else{
                        $.post('${contextPath}/security/yms/terminal_command/add_initialize.htm', {
                            ids: ids
                        }, function (json) {
                            if (json.success) {
                                $.messager.alert('info', '操作成功', 'info');
                                reload();
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
                    }
                }
            });
        }

        function uploadErrorLog(){
            $.messager.confirm('提示信息', '确认上传失败日志?', function(ok){
                if(ok){
                    var ids = [];
                    var rows= $('#page_table').datagrid('getChecked');
                    for(var i = 0; i < rows.length; i++){
                        ids.push(rows[i].id);
                    }
                    if(ids.length == 0){
                        alert('你没有选择任何内容');
                    } else{
                        $.post('${contextPath}/security/yms/terminal_command/add_upload_error_log.htm', {
                            ids: ids
                        }, function (json) {
                            if (json.success) {
                                $.messager.alert('info', '操作成功', 'info');
                                reload();
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
                    }
                }
            });
        }


        function screenshot(){
            $.messager.confirm('提示信息', '确认截屏?', function(ok){
                if(ok){
                    var ids = [];
                    var rows= $('#page_table').datagrid('getChecked');
                    for(var i = 0; i < rows.length; i++){
                        ids.push(rows[i].id);
                    }
                    if(ids.length == 0){
                        alert('你没有选择任何内容');
                    } else{
                        $.post('${contextPath}/security/yms/terminal_command/add_screenshot.htm', {
                            ids: ids
                        }, function (json) {
                            if (json.success) {
                                $.messager.alert('info', '操作成功', 'info');
                                reload();
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
                    }
                }
            });
        }

        function online(){
            $.messager.confirm('提示信息', '确认在线播放?', function(ok){
                if(ok){
                    var ids = [];
                    var rows= $('#page_table').datagrid('getChecked');
                    for(var i = 0; i < rows.length; i++){
                        ids.push(rows[i].id);
                    }
                    if(ids.length == 0){
                        alert('你没有选择任何内容');
                    } else{
                        $.post('${contextPath}/security/yms/terminal_command/add_online.htm', {
                            ids: ids
                        }, function (json) {
                            if (json.success) {
                                $.messager.alert('info', '操作成功', 'info');
                                reload();
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
                    }
                }
            });
        }

        function upgrade(){
            $.messager.confirm('提示信息', '确认升级?', function(ok){
                if(ok){
                    var ids = [];
                    var rows= $('#page_table').datagrid('getChecked');
                    for(var i = 0; i < rows.length; i++){
                        ids.push(rows[i].id);
                    }
                    if(ids.length == 0){
                        alert('你没有选择任何内容');
                    } else{
                        $.post('${contextPath}/security/yms/terminal_command/add_upgrade.htm', {
                            ids: ids
                        }, function (json) {
                            if (json.success) {
                                $.messager.alert('info', '操作成功', 'info');
                                reload();
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
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
            var tree = $('#cabinet_group_tree');
            tree.tree({
                url:"${contextPath}/security/hdg/cabinet_group/tree.htm?agentId="+agentId+"&dummy=${'所有'?url}"
            });
            tree.tree('reload');
        }

    </script>
    </@app.head>

    <@app.body>

        <@app.container>
            <@app.banner/>

        <div class="main">
            <@app.menu/>

            <div class="content">
                <div class="panel ztree_wrap">
                    <div class="ztree" >
                        <div class="ztree_head">
                            <h3>换电柜分组</h3>
                        </div>
                        <div class="ztree_body easyui-tree" id="cabinet_group_tree" url="${contextPath}/security/hdg/cabinet_group/tree.htm?dummy=${'所有'?url}" lines="true"
                             data-options="
                                onBeforeSelect: App.tree.toggleSelect,
                                onClick: function(node) {
                                    query();
                                }
                            ">
                        </div>
                    </div>
                </div>
                <div class="panel search" style="margin-left:250px;">
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">运营商：</td>
                            <td>
                                <input name="agentId" id="agent_id" class="easyui-combotree" editable="false" style="height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                                            method:'get',
                                                            valueField:'id',
                                                            textField:'text',
                                                            editable:false,
                                                            multiple:false,
                                                            panelHeight:'auto',
                                                            onClick: function(node) {
                                                               reloadTree();
                                                            }
                                                        "
                                >
                            </td>
                            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                            <td align="right">终端id：</td>
                            <td><input type="text" class="text" id="id"/></td>
                            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                            <td align="right">在线：</td>
                            <td>
                                <select type="text" id="is_online" class="text easyui-combobox"  name="isOnline" style="width:120px;height: 28px " >
                                    <option value="">所有</option>
                                    <option value="1">是</option>
                                    <option value="0">否</option>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-left:250px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='6_2_4_2'>
                                <button class="btn btn_green" onclick="upgrade()">关机</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='6_2_4_3'>
                                <button class="btn btn_blue" onclick="restart()">重启</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='6_2_4_4'>
                                <button class="btn btn_green" onclick="screenshot()">截屏</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='6_2_4_5'>
                                <button class="btn btn_yellow" onclick="initialize()">初始化</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='6_2_4_6'>
                                <button class="btn btn_red" onclick="uploadErrorLog()">上传错误日志</button>
                            </@app.has_oper>
                        </div>
                        <h3>终端监控</h3>
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
