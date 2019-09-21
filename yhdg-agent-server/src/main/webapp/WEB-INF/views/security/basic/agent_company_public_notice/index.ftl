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
                url: "${contextPath}/security/basic/agent_company_public_notice/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            title: '所属运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 40
                        },
                        {
                            title: '公告名称',
                            align: 'center',
                            field: 'title',
                            width: 60
                        },
                        {
                            title: '类型',
                            align: 'center',
                            field: 'noticeTypeName',
                            width: 60
                        },
                        {
                            title: '内容',
                            align: 'center',
                            field: 'content',
                            width: 60,
                            formatter : function(value, row) {
                                return '<span title="'+row.content+'">' + value + '</span>';
                            }
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.AgentComopanyPublicNotice:view'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.AgentComopanyPublicNotice:edit'>
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.AgentComopanyPublicNotice:delete'>
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
        });



        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var title = $('#title').val();
            datagrid.datagrid('options').queryParams = {
                title: title
            };

            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:550px;height:360px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/agent_company_public_notice/view.htm?id=" + id
            });
        }


        function add() {
            App.dialog.show({
                css: 'width:550px;height:320px;overflow:visible;',
                title: '新建',
                href: '${contextPath}/security/basic/agent_company_public_notice/add.htm',
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:550px;height:320px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/agent_company_public_notice/edit.htm?id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }

        function remove(id) {
            $.messager.confirm("提示信息", "确认删除?", function(ok) {
                if(ok) {
                    $.post("${contextPath}/security/basic/agent_company_public_notice/delete.htm?id=" + id, function (json){
                        if(json.success) {
                            $.messager.alert("提示信息", "删除成功", "info");
                            reload();
                        } else {
                            $.messager.alert("提示信息", json.message, "info")
                        }
                    }, 'json');
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
    </script>
    </@app.head>
    <@app.body>
        <@app.container>
            <@app.banner/>
        <div class="main">
            <@app.menu/>

            <div class="content">
                <div class="panel search">
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right" width="80">公告名称：</td>
                            <td><input type="text" class="text" id="title"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" >
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='basic.AgentComopanyPublicNotice:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                    </div>
                    <h3>运营商公告</h3>
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