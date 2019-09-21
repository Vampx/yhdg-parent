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
                url: "${contextPath}/security/basic/agent_system_config/page.htm",
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
                                <@app.has_oper perm_code='2_9_1_2'>
                                    html += '<a href="javascript:view(\'ID\', VALUE_TYPE, AGENT)">查看</a>';
                                </@app.has_oper>

                                if(row.isReadOnly == 0) {
                                    <@app.has_oper perm_code='2_9_1_3'>
                                        html+= ' <a href="javascript:edit(\'ID\', VALUE_TYPE, AGENT)">修改</a>';
                                    </@app.has_oper>

                                }
                                return html.replace(/ID/g, row.id).replace(/VALUE_TYPE/g,row.valueType).replace(/AGENT/g,row.agentId);
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
            var agentId = $('#agent_id').combotree('getValue');
            var tree = $('#category_tree');
            var category = tree.tree('getSelected');
            var category_type = '';
            if(category) {
                category_type = category.id;
            }

            datagrid.datagrid('options').queryParams = {
                configName: $("#configName").val(),
                categoryType : category_type,
                agentId:agentId
            };

            datagrid.datagrid('load');
        }
        
        function edit(id, type, agentId) {
            App.dialog.show({
                css: 'width:472px;height:375px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/agent_system_config/edit_" + type + ".htm?id=" + id +"&agentId="+agentId,
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

        function view(id, type, agentId) {
            App.dialog.show({
                css: 'width:472px;height:375px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/agent_system_config/view_" + type + ".htm?id=" + id+"&agentId="+agentId,
            });
        }

        function reloadTree() {
            var agentId = $('#agent_id').combotree('getValue');
            selectQuery(agentId);
        }

        function selectQuery(agentId) {
            $("#category_tree").tree({
                url: '${contextPath}/security/basic/agent_system_config/tree.htm?agentId=' + agentId,
                onLoadSuccess: function (data) {
                    eval(data)
                }

            });
            var datagrid = $('#page_table');

            datagrid.datagrid('options').queryParams = {
                agentId: agentId
            };
            datagrid.datagrid('load');
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
                        <div class="ztree_body easyui-tree" id="category_tree" url="${contextPath}/security/basic/agent_system_config/tree.htm?agentId=1" lines="true"
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
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">请选择运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="false" style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                   reloadTree();
                                }
                            "
                                >
                            </td>
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