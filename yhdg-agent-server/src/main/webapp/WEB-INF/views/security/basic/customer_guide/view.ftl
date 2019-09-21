<div class="popup_body">
    <div class="ztree" style="height:412px;">
        <div class="ztree_body" id="customer_guide_${pid}">
        </div>
    </div>
    <div class="toolbar clearfix" style="margin-left:210px; margin-top:-10px;">
        <div class="float_right">
            <button class="btn btn_green" id="add_${pid}">新建</button>
        </div>
    </div>
    <div class="grid" style="width:600px; height:360px; margin-left:210px;">
        <table id="page_table_${pid}"></table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>
<script>
    (function () {
        var tree = $('#customer_guide_${pid}');
        var datagrid = $('#page_table_${pid}');

        tree.tree({
            url: "${contextPath}/security/basic/customer_guide/tree.htm?dummy=${'所有'?url}",
            lines: true,
            onBeforeSelect: App.tree.toggleSelect,
            onClick: function (node) {
                query();
            }
        });

        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/customer_guide/page.htm",
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
                    {field: 'checkbox', checkbox: true},
                    {
                        title: '分类名称',
                        align: 'center',
                        field: 'name',
                        width: 60
                    },
                    {title: '上级分类', align: 'center', field: 'parentName', width: 60},
                    {
                        title: '操作',
                        align: 'center',
                        field: 'id',
                        width: 100,
                        formatter: function (val, row) {
                            var html = ' <a href="javascript:edit_${pid}(ID)">修改</a>';
                            html += ' <a href="javascript:remove_${pid}(ID)">删除</a>';
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ],
            onLoadSuccess: function () {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            }
        });
    })()

    $('#close_${pid}').click(function () {
        $('#${pid}').window('close');
    })

    $('#add_${pid}').click(function () {
        add_${pid}();
    })

    function reload_${pid}() {
        var tree = $('#customer_guide_${pid}');
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid('reload');
        tree.tree('reload')
    }

    function query() {
        var parentId = tree.tree('getSelected');
        if (parentId) {
            parentId = parentId.id || '';
        } else {
            parentId = '';
        }

        datagrid.datagrid('options').queryParams = {
            parentId: parentId
        };

        datagrid.datagrid('load');
    }

    function edit_${pid}(id) {
        App.dialog.show({
            css: 'width:310px;height:180px;overflow:visible;',
            title: '修改',
            href: "${contextPath}/security/basic/customer_guide/edit.htm?id=" + id,
            event: {
                onClose: function () {
                    reload_${pid}();
                },
                onLoad: function () {
                }
            }
        });
    }

    function remove_${pid}(id) {
        $.messager.confirm('提示信息', '确认删除?', function (ok) {
            if (ok) {
                $.post('${contextPath}/security/basic/customer_guide/delete.htm', {
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

    function add_${pid}() {
        App.dialog.show({
            css: 'width:310px;height:180px;overflow:visible;',
            title: '新建',
            href: "${contextPath}/security/basic/customer_guide/add.htm",
            event: {
                onClose: function () {
                    reload_${pid}();
                }
            }
        });
    }
</script>