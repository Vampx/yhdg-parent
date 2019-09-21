<@app.html>
    <@app.head>
    <script>
        function gridRowStyler() { return 'height:7.0em;'; }
        $(function() {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/yms/terminal_screen_snapshot/page.htm",
                fitColumns: true,
                pageSize: 5,
                pageList: [5, 10, 20],
                idField: 'id',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                columns: [
                    [
                        {
                            title: '缩略图',
                            align: 'center',
                            field: 'snapshotPath',
                            width: 60,
                            formatter: function (val, row) {
                                return "<img style='width:100px; height:70px;' onclick='preview(\""+ row.snapshotPath +"\")' src='${controller.appConfig.staticUrl}"+ val +"' />"
                            }
                        },
                        {
                            title: '终端编号',
                            align: 'center',
                            field: 'terminalId',
                            width: 60
                        },
                        {
                            title: '截屏时间',
                            align: 'center',
                            field: 'snapTime',
                            width: 70
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 70
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

        function preview(path) {
            App.dialog.show({
                options:'maximized:true',
                title: '查看',
                href: "${contextPath}/security/main/preview.htm?path=" + path
            });
        }

        function query() {
            var datagrid = $('#page_table');
            var terminalId = $('#terminal_id').val();
            var agentId = $('#agent_id').combotree("getValue");

            datagrid.datagrid('options').queryParams = {
                terminalId: terminalId,
                agentId: agentId
            };
            datagrid.datagrid('load');
        }

        /*
                function view(terminalId, now, num) {
                    App.dialog.show({
                        css: 'width:590px;height:260px;',
                        title: '查看',
                        href: "<#--${contextPath}-->/security/terminal_run_log/view.htm?terminalId=" + terminalId + "&now=" + now + "&num=" + num
            });
        }

*/

        function switchAgent() {
            query();
        }

    </script>
    </@app.head>
<style>
    .content .datagrid-body .datagrid-cell{
        height: 70px;
        line-height: 70px;
    }
    .content .datagrid-body .datagrid-cell input{
        margin-top: 26px;
    }
    datagrid-cell datagrid-cell-c2-position
</style>
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
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>终端截屏</h3>
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
