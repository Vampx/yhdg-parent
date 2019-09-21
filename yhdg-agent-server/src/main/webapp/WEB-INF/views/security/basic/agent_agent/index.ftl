<@app.html>
    <@app.head>
    <script>
        $(function() {

            $(function() {
                $('#page_table_tree').treegrid({
                    fit: true,
                    width: '100%',
                    height: '100%',
                    striped: true,
                    pagination: true,
                    url: "${contextPath}/security/basic/agent_agent/page_tree.htm",
                    fitColumns: true,
                    pageSize: 10,
                    pageList: [10, 50, 100],
                    idField: 'id',
                    treeField : "id",
                    singleSelect: true,
                    selectOnCheck: false,
                    checkOnSelect: false,
                    autoRowHeight: false,
                    rowStyler: gridRowStyler,
                    columns: [
                        [
                            { field: 'checkbox', checkbox: true },
                            {
                                title: 'ID',
                                align: 'center',
                                field: 'id',
                                width: 40
                            },
                            {
                                title: '运营商',
                                align: 'center',
                                field: 'agentName',
                                width: 60
                            },
                            {
                                title: '级别', align: 'center', field: 'grade', width:40,
                                formatter: function (val) {
                                    <#list gradeEnum as e>
                                        if (${e.getValue()} == val)
                                        return '${e.getName()}';
                                    </#list>
                                }
                            },
                            {
                                title: '状态',
                                align: 'center',
                                field: 'isActive',
                                width: 60,
                                formatter: function(val, row) {
                                    return val == 1 ? '启用' : '禁用'
                                }
                            },
                            {
                                title: '结算周期',
                                align: 'center',
                                field: 'balanceStatusName',
                                width: 60
                            },
                            {
                                title: '余额',
                                align: 'center',
                                field: 'balance',
                                width: 40,
                                formatter: function (val) {
                                    return Number(val / 100).toFixed(2);
                                }
                            },
                            {
                                title: '操作',
                                align: 'center',
                                field: 'edit',
                                width: 150,
                                formatter: function(val, row) {
                                    var html = '';
                                    <@app.has_oper perm_code='basic.Agent:view'>
                                        html += '<a href="javascript:view(ID)">查看</a>';
                                    </@app.has_oper>
                                    <@app.has_oper perm_code='basic.Agent:edit'>
                                        html += ' <a href="javascript:edit(ID)">修改</a>';
                                    </@app.has_oper>
                                    <@app.has_oper perm_code='basic.Agent:delete'>
                                        html += ' <a href="javascript:remove(ID)">删除</a>';
                                    </@app.has_oper>
                                    return html.replace(/ID/g, row.id);
                                }
                            }
                        ]
                    ],
                    onBeforeExpand: function (row, param) {
                        if(row) {
                            $(this).treegrid('options').url = "${contextPath}/security/basic/agent_agent/childPage.htm?parentId=" + row.id;
                            return true;
                        }
                    },
                    onLoadSuccess:function() {
                        $('#page_table_tree').treegrid('expandAll');
                        $('#page_table_tree').treegrid('clearChecked');
                        $('#page_table_tree').treegrid('clearSelections');
                    }
                });
            });
        });


        function pageTable() {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
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
                            title: 'ID',
                            align: 'center',
                            field: 'id',
                            width: 40
                        },
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
                        {
                            title: '级别', align: 'center', field: 'grade', width:40,
                            formatter: function (val) {
                                <#list gradeEnum as e>
                                    if (${e.getValue()} == val)
                                    return '${e.getName()}';
                                </#list>
                            }
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'isActive',
                            width: 60,
                            formatter: function(val, row) {
                                return val == 1 ? '启用' : '禁用'
                            }
                        },
                        {
                            title: '结算周期',
                            align: 'center',
                            field: 'balanceStatusName',
                            width: 60
                        },
                        {
                            title: '余额',
                            align: 'center',
                            field: 'balance',
                            width: 40,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'edit',
                            width: 150,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.Agent:view'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.Agent:edit'>
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.Agent:delete'>
                                    html += ' <a href="javascript:remove(ID)">删除</a>';
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

        }

        function reload() {
            var treegrid = $('#page_table_tree');
            treegrid.treegrid('reload');
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        var hasTable = false;
        function query() {
            $("#page_table_tree_div").hide();//隐藏树型表格div
            $("#page_table_div").show();//显示datagrid表格div

            var agentId = $('#agent_id').combotree('getValue');
            var grade = $('#grade').val();
            var parentId = null;
            if (!hasTable) {
                pageTable();
                hasTable = true;
            }

            setTimeout(function() {
                var datagrid = $('#page_table');
                datagrid.datagrid('options').queryParams = {
                    id: (agentId ||''),
                    parentId: (parentId || ''),
                    balanceStatus:$('#balance_status').val(),
                    grade: grade
                };
                datagrid.datagrid('options').url = '${contextPath}/security/basic/agent_agent/page.htm';
                datagrid.datagrid('load');
            }, 1);

        }
        <#--function query() {-->
        <#--    var agentId = $('#agent_id').combotree('getValue');-->
        <#--    var grade = $('#grade').val();-->
        <#--    var parentId = null;-->
        <#--    setTimeout(function() {-->
        <#--        var datagrid = $('#page_table_tree');-->
        <#--        datagrid.treegrid('options').queryParams = {-->
        <#--            id: (agentId ||''),-->
        <#--            parentId: (parentId || ''),-->
        <#--            balanceStatus:$('#balance_status').val(),-->
        <#--            grade: grade-->
        <#--        };-->
        <#--        datagrid.treegrid('options').url = '${contextPath}/security/basic/agent_agent/page_tree.htm';-->
        <#--        datagrid.treegrid('load');-->
        <#--    }, 1);-->

        <#--}-->

        function add() {
            App.dialog.show({
                css: 'width:755px;height:520px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/basic/agent_agent/add.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
                event: {
                    onClose: function() {
                        location.href = "${contextPath}/security/basic/agent_agent/index.htm";
                    }
                }
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:755px;height:520px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/agent_agent/edit.htm?id=" + id + "&agentId=${Session['SESSION_KEY_USER'].agentId}",
                event: {
                    onClose: function() {
                        // reload();
                        location.href = "${contextPath}/security/basic/agent_agent/index.htm";
                    },
                    onLoad: function() {
                    }
                }
            });
        }

        function view(id) {
            App.dialog.show({
                css: 'width:755px;height:520px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/agent_agent/view.htm?id=" + id,
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function(ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/agent_agent/delete.htm?id=" + id, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            location.href = "${contextPath}/security/basic/agent_agent/index.htm";
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
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
                    <div class="panel search" >
                        <div class="float_right">
                            <button class="btn btn_yellow" onclick="query()">搜索</button>
                        </div>
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td align="right">运营商：</td>
                                <td>
                                    <input name="agentId" id="agent_id" class="easyui-combotree" editable="false"
                                           style="width: 200px;height: 28px;"
                                           data-options="url:'${contextPath}/security/basic/agent/tree.htm?agentId=${(agentId)!''}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onLoadSuccess:function() {
                                    $('#agent_id').combotree('setValue', '${(agentId)!''}');
                                },
                                onClick: function(node) {

                                }
                            "
                                    >&nbsp;&nbsp;&nbsp;&nbsp;
                                </td>
                                <td align="right" >运营商级别：</td>
                                <td>
                                    <select style="width:70px;" id="grade">
                                        <option value="">所有</option>
                                        <#list gradeEnum as e>
                                            <option value="${e.getValue()}">${e.getName()}</option>
                                        </#list>
                                    </select>
                                </td>
                                <td align="right" style="width:90px;">结算周期：</td>
                                <td>
                                    <select style="width:80px;" id="balance_status">
                                        <option value="">所有</option>
                                        <#list BalanceStatusEnum as e>
                                            <option id="${e.getValue()}" value="${e.getValue()}">${e.getName()}</option>
                                        </#list>
                                    </select>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="panel grid_wrap">
                        <div class="toolbar clearfix">
                            <div class="float_right">
                                <@app.has_oper perm_code='basic.Agent:add'>
                                    <button class="btn btn_green" onclick="add()">新建</button>
                                </@app.has_oper>
                            </div>
                            <h3>运营商信息</h3>
                        </div>
                        <div class="grid">
                            <style>
                                .tree-folder{
                                    display: none;
                                }
                                .tree-file{
                                    display: none
                                }
                            </style>

                            <div class="easyui-layout">
                                <div id="page" data-options="region:'center',href:''" ></div>
                            </div>
                            <div id="page_table_tree_div" style="height: 100%">
                                <table id="page_table_tree"></table>
                            </div>
                            <div  id="page_table_div" style="display:none;height: 100%;width: 100%">
                                <table id="page_table" ></table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </@app.container>
    </@app.body>
</@app.html>