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
                url: "${contextPath}/security/basic/mobile_message_template/page.htm",
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
                            title: '标题',
                            align: 'center',
                            field: 'title',
                            width: 30
                        },
                        {
                            title: '内容',
                            align: 'left',
                            field: 'content',
                            width: 100,
                            formatter : function(value, row) {
                                return '<span title="'+row.content+'">' + value + '</span>';
                            }
                        },
                        {
                            title: '编号',
                            align: 'center',
                            field: 'code',
                            width: 30
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 30,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='7_1_2_2'>
                                    html += '<a href="javascript:view(AGENT_ID, ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='7_1_2_2'>
                                    html += ' <a href="javascript:edit(AGENT_ID, ID)">修改</a>';
                                </@app.has_oper>
                                return html.replace(/AGENT_ID/g, row.appId).replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                onLoadSuccess:function() {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                },
                queryParams: {
                    appId: 0
                }
            });
        });



        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function reloadTree() {
            var datagrid = $('#page_table');
            var appId = $('#app_id').combotree('getValue');
            datagrid.datagrid('options').queryParams = {
                appId: appId
            };
            datagrid.datagrid('load');
        }

        function query() {
            var datagrid = $('#page_table');
            var title = $('#title').val();
            var appId = $('#app_id').combotree('getValue');
            datagrid.datagrid('options').queryParams = {
                title: title,
                appId:appId
            };
            datagrid.datagrid('load');
        }

        function edit(agentId, id) {
            App.dialog.show({
                css: 'width:550px;height:336px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/mobile_message_template/edit.htm?agentId=" + agentId + "&id=" + id,
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
        function view(agentId, id) {
            App.dialog.show({
                css: 'width:550px;height:336px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/mobile_message_template/view.htm?agentId=" + agentId + "&id=" + id,
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
                <div class="panel search">
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td>平台类型：</td>
                            <td>
                                <input name="appId" id="app_id" style="height: 28px;width: 140px;" class="easyui-combobox"  editable="false"
                                       data-options="url:'${contextPath}/security/basic/agent/self_platform_agent_list.htm',
                                            method:'get',
                                            valueField:'id',
                                            textField:'agentName',
                                            editable:false,
                                            multiple:false,
                                            panelHeight:'200',
                                            onSelect: function(node) {
                                                query();
                                            }"
                                />
                            </td>
                            <td align="right">&nbsp;&nbsp;标题：</td>
                            <td><input type="text" class="text" id="title"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>短信模板列表</h3>
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


