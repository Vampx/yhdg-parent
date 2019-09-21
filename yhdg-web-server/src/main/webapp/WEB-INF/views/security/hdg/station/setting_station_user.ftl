<div class="c-d-three-4 c-d-three-4-site" style="display: none; width: 100%">
    <div style="flex: 1;box-sizing: border-box;padding-left: 10px;width: 100%">
        <div style="height:auto; padding-top: 6px; width: 100%">
            <div class="add">
                <button class="export" onclick="user_add()">新建</button>
            </div>
            <div style="height: 520px;">
                <table id="station_user_page_table" style="height: 80%;"></table>
            </div>
        </div>
    </div>
</div>
<script>

    function user_add() {
        $.ajax({
            url: "${contextPath}/security/basic/user/add_precondition.htm",
            success: function (text) {
                var json = $.evalJSON(text);
            <@app.json_jump/>
                if (!json.success) {
                    $.messager.alert('提示信息', json.message, 'info');
                    return false;
                }else{
                    App.dialog.show({
                        css: 'width:665px;height:485px;overflow:visible;',
                        title: '新建',
                        href: "${contextPath}/security/hdg/station_user/add.htm?stationId=${(entity.id)!''}&agentId=${(entity.agentId)!0}",
                        event: {
                            onClose: function () {
                                $('.c-d-three-4').show();
                                showStationUserTable();
                            }
                        }
                    });
                }
            }
        });
    }

    function user_edit(id) {
        App.dialog.show({
            css: 'width:665px;height:485px;overflow:visible;',
            title: '修改',
            href: "${contextPath}/security/hdg/station_user/edit.htm?id=" + id,
            event: {
                onClose: function () {
                    $('.c-d-three-4').show();
                    showStationUserTable();
                },
                onLoad: function () {
                }
            }
        });
    }

    function user_remove(id) {
        $.messager.confirm('提示信息', '确认删除?', function (ok) {
            if (ok) {
                $.post("${contextPath}/security/basic/user/delete.htm?id=" + id, function (json) {
                    if (json.success) {
                        $.messager.alert('提示消息', '操作成功', 'info');
                        $('.c-d-three-4').show();
                        showStationUserTable();
                    } else {
                        $.messager.alert('提示消息', json.message, 'info');
                    }
                }, 'json');
            }
        });
    }

    function user_view(id) {
        App.dialog.show({
            css: 'width:665px;height:485px;overflow:visible;',
            title: '查看',
            href: "${contextPath}/security/hdg/station_user/view.htm?id=" + id
        });
    }

    function showStationUserTable() {
        $('#station_user_page_table').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/station_user/page.htm?stationId=${(entity.id)!''}&stationFlag=1",
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
                        title: '站点编号',
                        align: 'center',
                        field: 'stationId',
                        width: 60
                    },
                    {
                        title: '站点名称',
                        align: 'center',
                        field: 'stationName',
                        width: 60
                    },
                    {
                        title: '站点账号',
                        align: 'center',
                        field: 'loginName',
                        width: 60
                    },
                    {title: '手机号码', align: 'center', field: 'mobile', width: 60},
                    {title: '角色', align: 'center', field: 'stationRoleName', width: 60},
                    {
                        title: '是否启用',
                        align: 'center',
                        field: 'isActive',
                        width: 60,
                        formatter: function(val) {
                            if(val == 1) {
                                return '<a style="color: #00FF00;">是</a>';
                            }else{
                                return '<a style="color: #ff0000;">否</a>';
                            }
                        }
                    },
                    {
                        title: '是否核心管理员',
                        align: 'center',
                        field: 'isAdmin',
                        width: 60,
                        formatter: function(val) {
                            if(val == 1) {
                                return '<a style="color: #00FF00;">是</a>';
                            }else{
                                return '<a style="color: #ff0000;">否</a>';
                            }
                        }
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'id',
                        width: 100,
                        formatter: function (val, row) {
                            var html = '';
                        <@app.has_oper perm_code='hdg.StationUser:view'>
                            html += '<a href="javascript:user_view(ID)">查看</a>';
                        </@app.has_oper>
                        <@app.has_oper perm_code='hdg.StationUser:edit'>
                            html += ' <a href="javascript:user_edit(ID)">修改</a>';
                        </@app.has_oper>
                        <@app.has_oper perm_code='hdg.StationUser:remove'>
                            html += ' <a href="javascript:user_remove(ID)">删除</a>';
                        </@app.has_oper>
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ],
            onLoadSuccess: function () {
                $('#station_user_page_table').datagrid('clearChecked');
                $('#station_user_page_table').datagrid('clearSelections');
            }
        });
    }
</script>