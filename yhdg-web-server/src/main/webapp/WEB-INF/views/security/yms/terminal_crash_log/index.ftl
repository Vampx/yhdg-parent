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
                url: "${contextPath}/security/yms/terminal_crash_log/page.htm",
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
                            title: '终端编号',
                            align: 'center',
                            field: 'terminalId',
                            width: 60
                        },
                        {
                            title: '文件',
                            align: 'center',
                            field: 'filePath',
                            width: 110,
                            formatter:function(val){
                                return val.replace(/\/.*\//, '');
                            }
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 120
                        },
                        {
                            title: '报告时间',
                            align: 'center',
                            field: 'reportTime',
                            width: 120
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='6_2_7_2'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='6_2_7_3'>
                                    html += ' <a href="javascript:download(ID)">下载</a>';
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
            var queryBeginTime = $('#begin_time').datebox('getValue');
            var queryEndTime = $('#end_time').datebox('getValue');
            var terminalId = $('#terminal_id').val();
            var agentId = $('#agent_id').combotree("getValue");

            if(queryBeginTime >= queryEndTime) {
                $.messager.alert('提示信息', '结束日期必须大于开始日期', 'info');
                return;
            }

            datagrid.datagrid('options').queryParams = {
                queryBeginTime: queryBeginTime,
                queryEndTime: queryEndTime,
                terminalId: terminalId,
                agentId: agentId
            };
            datagrid.datagrid('load');
        }


        function view(id) {
            App.dialog.show({
                options:'maximized:true',
                title: '查看',
                href: "${contextPath}/security/yms/terminal_crash_log/view.htm?id=" + id
            });
        }

        function download(id) {
            document.location.href = "${contextPath}/security/yms/terminal_crash_log/download.htm?id=" + id
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
                            <td width="80" align="right">终端编号：</td>
                            <td><input type="text" class="text" id="terminal_id"/></td>
                            <td align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;开始/结束时间：</td>
                            <td>
                                <input id="begin_time" class="easyui-datebox" name="queryBeginTime" type="text" style="width:150px;height:27px;" >
                                -
                                <input id="end_time" class="easyui-datebox" name="queryEndTime" type="text" style="width:150px;height:27px;" >
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>终端崩溃日志</h3>
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
