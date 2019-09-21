<@app.html>
    <@app.head>
    <script>
        $(function () {

            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/basic/agent_day_in_out_money/page.htm",
                fitColumns: true,
                pageSize: 50,
                pageList: [50, 100],
                idField: 'id',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                columns: [
                    [
                        {title: '统计日期', align: 'center', field: 'statsDate', width: 100},
                        {title: '运营商ID', align: 'center', field: 'agentId', width: 100},
                        {title: '运营商名称', align: 'center', field: 'agentName', width: 100},
                        {
                            title: '运营商收入', align: 'center', field: 'agentInMoney', width: 100,
                            formatter: function (val, row) {
                                return '<a href="javascript:view_agent_in_money(' + row.agentId + ',\'' + row.statsDate + '\')"><u>' + Number(val / 100).toFixed(2) + "元" + '</u></a>';
                            }
                        },
                        {
                            title: '运营商支出', align: 'center', field: 'agentOutMoney', width: 100,
                            formatter: function (val, row) {
                                return '<a href="javascript:view_agent_out_money(' + row.agentId + ',\'' + row.statsDate + '\')"><u>' + Number(val / 100).toFixed(2) + "元" + '</u></a>';
                            }
                        }
                    ]
                ],
                onLoadSuccess: function () {
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
            var agentName = $('#agent_name').val();
            var statsDate = $('#stats_date').datebox('getValue');

            datagrid.datagrid('options').queryParams = {
                agentId: agentId,
                statsDate: statsDate,
                agentName: agentName
            };
            datagrid.datagrid('load');
        }

        function view_agent_in_money(agentId, statsDate) {
            App.dialog.show({
                css: 'width:920px;height:550px;overflow:visible;',
                title: '运营商收入流水',
                href: "${contextPath}/security/basic/agent_day_in_out_money/view_agent_in_money.htm?agentId="+agentId+"&statsDate='"+statsDate+"'"
            });
        }

        function view_agent_out_money(agentId, statsDate) {
            App.dialog.show({
                css: 'width:920px;height:550px;overflow:visible;',
                title: '运营商支出流水',
                href: "${contextPath}/security/basic/agent_day_in_out_money/view_agent_out_money.htm?agentId="+agentId+"&statsDate='"+statsDate+"'"
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
                            <td align="right">运营商：</td>
                            <td>
                                <input name="agentId" id="agent_id" class="easyui-combotree" editable="true"
                                       style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                                            method:'get',
                                                            valueField:'id',
                                                            textField:'text',
                                                            editable:true,
                                                            multiple:false,
                                                            panelHeight:'200',
                                                            onClick: function(node) {

                                                            }
                                                        "
                                        >
                            </td>
                            <td align="right" width="80">运营商名称：</td>
                            <td><input type="text" class="text" id="agent_name"/>&nbsp;&nbsp;</td>
                            <td align="right">日期：</td>
                            <td><input type="text" class="easyui-datebox" id="stats_date" style="width: 150px;height: 28px;"/>&nbsp;&nbsp;
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="top: 80px">
                    <div class="toolbar clearfix">
                        <h3>运营商日收入统计</h3>
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
