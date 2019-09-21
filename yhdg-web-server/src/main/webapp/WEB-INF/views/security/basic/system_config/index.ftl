<@app.html>
    <@app.head>
    <script>
        function isFlag(val, row) {
            if(val == 1) {
                return "是";
            }
            return "否";
        }
        $(function() {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: false,
                url: "${contextPath}/security/basic/system_config/page.htm",
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
                        { title: '配置项', align: 'center', field: 'configName', width: 80},
                        {title: '配置值', align: 'center', field: 'configValue', width: 150},
                        {
                            title: '只读',
                            align: 'center',
                            field: 'readOnlyFlag',
                            width: 60,
                            formatter:isFlag
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 100,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.SystemConfig:view'>
                                    html += '<a href="javascript:view(\'ID\',VALUE_TYPE)">查看</a>';
                                </@app.has_oper>
                                if(row.isReadOnly == 0) {
                                    <@app.has_oper perm_code='basic.SystemConfig:edit'>
                                        html+= ' <a href="javascript:edit(\'ID\',VALUE_TYPE)">修改</a>';
                                    </@app.has_oper>
                                }
                                return html.replace(/ID/g, row.id).replace(/VALUE_TYPE/g,row.valueType);
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

            var tree = $('#category_tree');
            var category = tree.tree('getSelected');
            var category_type = '';
            if(category) {
                category_type = category.id;
            }

            datagrid.datagrid('options').queryParams = {
                configName: $("#configName").val(),
                categoryType : category_type
            };

            datagrid.datagrid('load');
        }
        
        function edit(id, type) {
            App.dialog.show({
                css: 'width:472px;height:375px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/system_config/edit_" + type + ".htm?id=" + id,
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
        function view(id, type) {
            App.dialog.show({
                css: 'width:472px;height:375px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/system_config/view_" + type + ".htm?id=" + id
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
                            <h3>分类</h3>
                        </div>
                        <div class="ztree_body easyui-tree" id="category_tree" url="${contextPath}/security/basic/system_config/tree.htm" lines="true"
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
                            <td align="right">配置项：</td>
                            <td><input type="text" class="text" id="configName"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-left:250px;">
                    <div class="toolbar clearfix">
                        <h3>配置信息</h3>
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