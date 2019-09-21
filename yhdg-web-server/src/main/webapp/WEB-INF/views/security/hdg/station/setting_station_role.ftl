<div class="c-d-three-3 c-d-three-3-site" style="display: none; width: 100%">
    <div style="flex: 1;box-sizing: border-box;padding-left: 10px;width: 100%">
        <div style="height:auto; padding-top: 6px; width: 100%">
            <div class="add">
                <button class="export" onclick="role_add()">新建</button>
            </div>
            <div style="height: 520px;">
                <table id="station_role_page_table"></table>
            </div>
        </div>
    </div>
</div>
<script>

    function role_add() {
        App.dialog.show({
            css: 'width:480px;height:480px;overflow:visible;',
            title: '新建',
            href: "${contextPath}/security/basic/station_role/add.htm?stationId=${(entity.id)!''}&agentId=${(entity.agentId)!0}",
            event: {
                onClose: function() {
                    $('.c-d-three-3').show();
                    showStationRoleTable();
                }
            }
        });
    }

    function role_edit(id) {
        App.dialog.show({
            css: 'width:480px;height:480px;overflow:visible;',
            title: '修改',
            href: "${contextPath}/security/basic/station_role/edit.htm?id=" + id,
            event: {
                onClose: function() {
                    $('.c-d-three-3').show();
                    showStationRoleTable();
                },
                onLoad: function() {
                }
            }
        });
    }

    function role_view(id) {
        App.dialog.show({
            css: 'width:480px;height:480px;overflow:visible;',
            title: '查看',
            href: "${contextPath}/security/basic/station_role/view.htm?id=" + id
        });
    }

    function role_remove(id) {
        $.messager.confirm('提示信息', '确认删除?', function(ok) {
            if(ok) {
                $.post('${contextPath}/security/basic/station_role/delete.htm', {
                    id: id
                }, function (json) {
                    if (json.success) {
                        $.messager.alert('info', '操作成功', 'info');
                        $('.c-d-three-3').show();
                        showStationRoleTable();
                    } else {
                        $.messager.alert('提示消息', json.message, 'info');
                    }
                }, 'json');
            }
        });
    }


    function showStationRoleTable() {
        $('#station_role_page_table').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/station_role/page.htm?stationId=${(entity.id)!''}",
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
                        title: '运营商名称',
                        align: 'center',
                        field: 'agentName',
                        width: 60
                    },
                    {
                        title: '站点名称',
                        align: 'center',
                        field: 'stationName',
                        width: 60
                    },
                    {
                        title: '角色名称',
                        align: 'center',
                        field: 'roleName',
                        width: 60
                    },
                    {title: '备注', align: 'center', field: 'memo', width: 100},
                    {
                        title: '操作',
                        align: 'center',
                        field: 'id',
                        width: 60,
                        formatter: function(val, row) {
                            var html ='';
                        <@app.has_oper perm_code='basic.StationRole:view'>
                            html += '<a href="javascript:role_view(ID)">查看</a>';
                        </@app.has_oper>
                        <@app.has_oper perm_code='basic.StationRole:edit'>
                            html += ' <a href="javascript:role_edit(ID)">修改</a>';
                        </@app.has_oper>
                        <@app.has_oper perm_code='basic.StationRole:remove'>
                            html += ' <a href="javascript:role_remove(ID)">删除</a>';
                        </@app.has_oper>
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ],
            onLoadSuccess: function () {
                $('#station_role_page_table').datagrid('clearChecked');
                $('#station_role_page_table').datagrid('clearSelections');
            }
        });
    }
</script>