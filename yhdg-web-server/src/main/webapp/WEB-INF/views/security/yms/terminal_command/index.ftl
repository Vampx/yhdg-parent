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
                url: "${contextPath}/security/yms/terminal_command/page.htm",
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
                        {title: '终端Id', align: 'center', field: 'terminalId', width: 30},
                        {title: '类型', align: 'center', field: 'type', width: 40,
                            formatter:function(val,row){
                                <#list TypeEnum as t>
                                    if(val == ${t.getValue()})
                                        return '${t.getName()}';
                                </#list>
                            }
                        },
                        {title: '状态', align: 'center', field: 'status', width: 40,
                            formatter:function(val,row){
                                <#list StatusEnum as s>
                                    if(val == ${s.getValue()})
                                        return '${s.getName()}';
                                </#list>
                            }
                        },
                        {title: '内容', align: 'center', field: 'content', width: 100},
                        {title: '创建时间', align: 'center', field: 'createTime', width: 60},
                        {title: '执行时间', align: 'center', field: 'execTime', width: 60},
                        {title: '失败原因', align: 'center', field: 'failureReason', width: 60},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 40,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='6_2_5_2'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
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
            var terminalId = $('#terminal_id').val();
            var type = $('#type_id').val();
            var status =  $('#status_id').val();
            var routeId = null;
            var route = $('#route_id').tree('getSelected');
            var agentId = $('#agent_id').combotree("getValue");
            if(route){
                routeId = route.id
            }
            datagrid.datagrid('options').queryParams = {
                routeId: routeId,
                terminalId: terminalId,
                type: type,
                status: status,
                agentId: agentId
            };
            datagrid.datagrid('load');
        }

       <#--/* function edit(id) {-->
            <#--App.dialog.show({-->
                <#--css: 'width:770px;height:530px;',-->
                <#--title: '修改',-->
                <#--href: "${contextPath}/security/terminal/edit.htm?id=" + id,-->
                <#--event: {-->
                    <#--onClose: function() {-->
                        <#--var datagrid = $('#page_table');-->
                        <#--datagrid.datagrid('reload');-->
                    <#--},-->
                    <#--onLoad: function() {-->
                    <#--}-->
                <#--}-->
            <#--});-->
        <#--}*/-->

        function view(id) {
            App.dialog.show({
                css: 'width:550px;height:170px;',
                title: '查看',
                href: "${contextPath}/security/yms/terminal_command/view.htm?id=" + id
            });
        }

       <#--/* function remove(id) {-->
            <#--$.messager.confirm('提示信息', '确认删除?', function(ok) {-->
                <#--if(ok) {-->
                    <#--$.post('${contextPath}/security/terminal/delete.htm', {-->
                        <#--id: id-->
                    <#--}, function (json) {-->
                        <#--if (json.success) {-->
                            <#--$.messager.alert('info', '操作成功', 'info');-->
                            <#--reload();-->
                        <#--} else {-->
                            <#--$.messager.alert('提示消息', json.message, 'info');-->
                        <#--}-->
                    <#--}, 'json');-->
                <#--}-->
            <#--});-->
        <#--}*/-->

        function switchAgent(agentId, descendant) {
            reload();
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
                                <input name="agentId" id="agent_id" class="easyui-combotree" editable="false" style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                                            method:'get',
                                                            valueField:'id',
                                                            textField:'text',
                                                            editable:false,
                                                            multiple:false,
                                                            panelHeight:'200',
                                                            onClick: function(node) {
                                                            }
                                                        "
                                >
                            </td>
                            <td width="70" align="right">终端ID：</td>
                            <td><input type="text" class="text" id="terminal_id"/></td>
                            <td align="right" width="50">类型：</td>
                            <td>
                                <select style="width:60px;" id="type_id">
                                    <option value="">所有</option>
                                    <#list TypeEnum as t>
                                        <option value="${t.getValue()}">${t.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right" width="50">状态：</td>
                            <td>
                                <select style="width:70px;" id="status_id">
                                    <option value="">所有</option>
                                    <#list StatusEnum as s>
                                        <option value="${s.getValue()}">${s.getName()}</option>
                                    </#list>
                                </select>
                            </td>

                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" >
                    <div class="toolbar clearfix">
                        <h3>终端命令</h3>
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
