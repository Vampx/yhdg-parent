<div class="popup_body">
    <div class="ztree" style="height:412px;">
        <div class="ztree_body" id="dept_tree_${pid}">
        </div>
    </div>
    <div class="toolbar clearfix" style="margin-left:210px; margin-top:-10px;">
        <div class="float_right">
            <@app.has_oper perm_code='1_1_5_2'>
                <button class="btn btn_green" id="add_${pid}">新建</button>
            </@app.has_oper>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
          <#--  <tr>
                <td align="right">运营商：</td>
                <td>
                    <input name="agentId" id="agent_id_${pid}" class="easyui-combotree" editable="false" style="width: 184px; height: 28px;"
                           data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'系统'?url}',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                       reload_${pid}();
                                    }
                                "
                    >
                </td>
            </tr>-->
        </table>
    </div>
    <div class="grid" style="width:600px; height:360px; margin-left:210px;">
        <table id="page_table_${pid}"></table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>
<script>
function edit_${pid}(id) {
    App.dialog.show({
        css: 'width:528px;height:220px;overflow:visible;',
        title: '修改',
        href: "${contextPath}/security/basic/dept/edit.htm?id=" + id,
        event: {
            onClose: function() {
                reload_${pid}();
            },
            onLoad: function() {
            }
        }
    });
}
function view_${pid}(id) {
    App.dialog.show({
        css: 'width:528px;height:290px;overflow:visible;',
        title: '查看',
        href: "${contextPath}/security/basic/dept/view.htm?id=" + id,
        event: {
            onClose: function() {
                reload_${pid}();
            },
            onLoad: function() {
            }
        }
    });
}

function reload_${pid}() {
    var datagrid = $('#page_table_${pid}');
    datagrid.datagrid('reload');
    var tree = $('#dept_tree_${pid}');
    tree.tree({
        url:"${contextPath}/security/basic/dept/tree.htm?dummy=${'所有'?url}"
    });
    tree.tree('reload');
}

function remove_${pid}(id) {
    $.messager.confirm('提示信息', '确认删除?', function(ok) {
        if(ok) {
            $.post('${contextPath}/security/basic/dept/delete.htm', {
                id: id
            }, function (json) {
                if (json.success) {
                    $.messager.alert('info', '操作成功', 'info');
                    reload_${pid}();
                } else {
                    $.messager.alert('提示消息', json.message, 'info');
                }
            }, 'json');
        }
    });
}

(function() {
    var tree = $('#dept_tree_${pid}');
    var datagrid = $('#page_table_${pid}');
    tree.tree({
        url:"${contextPath}/security/basic/dept/tree.htm?dummy=${'所有'?url}",
        lines: true,
        onBeforeSelect: App.tree.toggleSelect,
        onClick: function(node) {
            query();
        }
    });

    datagrid.datagrid({
        fit: true,
        width: '100%',
        height: '100%',
        striped: true,
        pagination: true,
        url: "${contextPath}/security/basic/dept/page.htm",
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
                { field: 'checkbox', checkbox: true },
                {
                    title: '部门',
                    align: 'center',
                    field: 'deptName',
                    width: 60
                },
                {title: '备注', align: 'center', field: 'memo', width: 60},
                {
                    title: '操作',
                    align: 'center',
                    field: 'id',
                    width: 100,
                    formatter: function(val, row) {
                        var html = '<a href="javascript:view_${pid}(ID)">查看</a>';
                        <@app.has_oper perm_code='1_1_5_4'>
                            html += ' <a href="javascript:edit_${pid}(ID)">修改</a>';
                        </@app.has_oper>
                        <@app.has_oper perm_code='1_1_5_5'>
                            html += ' <a href="javascript:remove_${pid}(ID)">删除</a>';
                        </@app.has_oper>
                        return html.replace(/ID/g, row.id);
                    }
                }
            ]
        ],
        onLoadSuccess:function() {
            datagrid.datagrid('clearChecked');
            datagrid.datagrid('clearSelections');
        }
    });
    $('#close_${pid}').click(function() {
        $('#${pid}').window('close');
    });

    $('#add_${pid}').click(function() {
        add();
    });

    function reload() {
        datagrid.datagrid('reload');
        tree.tree('reload');
    }

    function add() {
        App.dialog.show({
            css: 'width:528px;height:250px;overflow:visible;',
            title: '新建',
            href: "${contextPath}/security/basic/dept/add.htm",
            event: {
                onClose: function() {
                    reload();
                }
            }
        });
    }

    function query() {
        var parentId = tree.tree('getSelected');
        var agentId = $('#agent_id_${pid}').combotree('getValue');
        if(parentId) {
            parentId = parentId.id || '';
        } else {
            parentId = '';
        }

        datagrid.datagrid('options').queryParams = {
            parentId: parentId,
            agentId: agentId
        };

        datagrid.datagrid('load');
    }
})()
</script>