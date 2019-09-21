<@app.html>
    <@app.head>
        <script type="text/javascript">
            $(function() {

                $('#page_table').datagrid({
                    fit: true,
                    width: '100%',
                    height: '100%',
                    striped: true,
                    pagination: true,
                    url: "${contextPath}/security/basic/dict_item/page.htm",
                    fitColumns: true,
                    singleSelect: true,
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
                            {title: '字典分类', align: 'center', field: 'categoryName', width: 120},
                            {title: '字典名称', align: 'center', field: 'itemName', width: 120},
                            {title: '条目值', align: 'center', field: 'itemValue', width: 120},
                            {
                                title: '编号',
                                align: 'center',
                                field: 'orderNum',
                                width: 30,
                                formatter: function(val, row) {
                                    var html = "<input type='text' style='width: 40px; height:15px; text-align: center;' id='order_num_ID' onblur='updateOrderNum(ID)' onkeypress='if(event.keyCode==13) {updateOrderNum(ID);return false;}' value=" + val+ ">"
                                    return html.replace(/ID/g, row.id);
                                }
                            },
                            {
                                title: '操作',
                                align: 'center',
                                field: 'id',
                                width: 100,
                                formatter: function(val, row) {
                                    var html = '';
                                    <@app.has_oper perm_code='basic.DictItem:view'>
                                        html += '<a href="javascript:view(ID)">查看</a>';
                                    </@app.has_oper>
                                    <@app.has_oper perm_code='basic.DictItem:remove'>
                                        html += ' <a href="javascript:remove(ID)">删除</a>';
                                    </@app.has_oper>
                                    return html.replace(/ID/g, row.id);
                                }
                            }
                        ]
                    ]
                });

                $('#mc_tree').tree({
                    url:'${contextPath}/security/basic/dict_item/find_category.htm',
                    loadFilter: function(json){
                        <@app.json_jump/>
                        var data = [{id:'0', text:'所有分类',children :json}];
                        return data;
                    },
                    onClick:function (node) {
                        query();
                    }
                });
            });



            function reload() {
                var datagrid = $('#page_table');
                datagrid.datagrid('reload');
            }

            function query() {
                var tree = $('#mc_tree');
                var datagrid = $('#page_table');

                var item_name = $('#item_name').val();
                var category = tree.tree('getSelected');
                var category_id = '';
                if(category) {
                    category_id = category.id;
                }

                datagrid.datagrid('options').queryParams = {
                    itemName:item_name,
                    categoryId: category_id
                };

                datagrid.datagrid('load');
            }

            function add() {
                App.dialog.show({
                    css: 'width:350px;height:266px;',
                    title: '新建',
                    href: "${contextPath}/security/basic/dict_item/add.htm",
                    event: {
                        onClose: function() {
                            var datagrid = $('#page_table');
                            datagrid.datagrid('reload');
                        }
                    }
                });
            }

            function edit(id) {
                App.dialog.show({
                    css: 'width:350px;height:266px;',
                    title: '修改',
                    href: "${contextPath}/security/basic/dict_item/edit.htm?id=" + id,
                    event: {
                        onClose: function() {
                            reload();
                        }
                    }
                });
            }

            function view(id) {
                App.dialog.show({
                    css: 'width:340px;height:266px;',
                    title: '查看',
                    href: "${contextPath}/security/basic/dict_item/view.htm?id=" + id
                });
            }

            function remove(id) {
                $.messager.confirm('提示信息', '确认删除?', function(ok) {
                    if(ok) {
                        $.post('${contextPath}/security/basic/dict_item/delete.htm', {
                            id: id
                        }, function(json) {
                            <@app.json_jump />
                            if(json.success) {
                                $.messager.alert('提示信息', '操作成功', 'info');
                                reload();
                            } else {
                                $.messager.alert('提示信息', json.message, 'info');
                            }
                        }, 'json');
                    }
                });
            }

            function updateOrderNum(id) {
                $.ajax({
                    cache: false,
                    async: false,
                    type: 'POST',
                    url: '${contextPath}/security/basic/dict_item/update_order_num.htm',
                    dataType: 'json',
                    data: {id: id, orderNum: $('#order_num_' + id).val()},
                    success: function (json) {
                    <@app.json_jump/>
                        if (json.success) {
                            reload();
                        } else {
                            $.messager.alert('提示信息', json.message, 'info');
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
                <div class="panel ztree_wrap">
                    <div class="ztree">
                        <div class="ztree_head">
                            <ul class="tab_nav clearfix">
                                <li class="selected">字典分类</li>
                            </ul>
                        </div>
                        <div>
                            <div class="ztree_body easyui-tree" id="mc_tree" ></div>
                        </div>
                    </div>
                </div>
                <div class="panel search" style="margin-left:250px;">
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">字典名称: &nbsp;&nbsp;</td>
                            <td><input type="text" id="item_name" class="text" /></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-left:250px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='basic.DictItem:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>分类信息</h3>
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