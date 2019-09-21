<@app.html>
    <@app.head>
    <script>
        $(function () {
            $('#page_table').datagrid({
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                fit: true,
                striped: true,
                singleSelect: true,
                collapsible: true,
                pagination: true,
                url: "${contextPath}/security/basic/agent_company_total_stats/page.htm",
                pageSize: 50,
                pageList: [50, 100],
                idField: 'id',
                frozenColumns: [[
                    {title: '运营公司名称', align: 'center', field: 'agentCompanyName', width: 120}
                ]],
                columns: [
                    [
                        {
                            title: '所属运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 120
                        },
                        {
                            title: '总收入',
                            align: 'center',
                            field: 'money',
                            width: 120,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) + "元";
                            }
                        },
                        {
                            title: '换电金额',
                            align: 'center',
                            field: 'exchangeMoney',
                            width: 120,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) + "元";
                            }
                        },
                        {
                            title: '包时段金额',
                            align: 'center',
                            field: 'packetPeriodMoney',
                            width: 120,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) + "元";
                            }
                        },
                        {
                            title: '包时段退款金额',
                            align: 'center',
                            field: 'refundPacketPeriodMoney',
                            width: 120,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) + "元";
                            }
                        },
                        {
                            title: '订单次数',
                            align: 'center',
                            field: 'orderCount',
                            width: 120
                        },
                        {
                            title: '包时段数量',
                            align: 'center',
                            field: 'packetPeriodCount',
                            width: 120
                        },
                        {
                            title: '退款包时段数量',
                            align: 'center',
                            field: 'refundPacketPeriodCount',
                            width: 120
                        },
                        {
                            title: '更新时间',
                            align: 'center',
                            field: 'updateTime',
                            width: 140
                        }
                    ]
                ],
                onLoadSuccess: function () {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                }
            });
        });

        function countTotal() {
            var rows = $('#page_table').datagrid('getRows');//获取当前的数据行
            var totalIncome = 0, totalCusteomer = 0, totalPower = 0;
            totalDegree = 0;
            for (var i = 0; i < rows.length; i++) {
                totalIncome += rows[i]['money'];
                totalPower += rows[i]['power'];
                totalCusteomer += rows[i]['orderCount'];
                totalDegree += rows[i]['degree'];
            }
            $('#totalIncome').attr('value', Number(totalIncome / 100).toFixed(2));
            $('#totalCusteomer').attr('value', totalCusteomer);
            $('#totalPower').attr('value', Number(totalPower / 120000).toFixed(5));
            $('#totalDegree').attr('value', Number(totalDegree / 100).toFixed(2));
        }


        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var agentCompanyName = $('#agent_company_name').val();
            var category = $('#category').val();
            datagrid.datagrid('options').queryParams = {
                agentCompanyName: agentCompanyName,
                category: category
            };
            datagrid.datagrid('load');
        }
        function exportExcel() {
            $.messager.confirm('提示信息', '确认导出数据?', function (ok) {
                if (ok) {
                    var agentCompanyName = $('#agent_company_name').val();
                    $.messager.progress();
                    $.post('${contextPath}/security/basic/agent_company_total_stats/export_excel.htm', {
                        agentCompanyName: agentCompanyName
                    }, function (json) {
                        $.messager.progress('close');
                        if (json.success) {
                            document.location.href = '${contextPath}/security/basic/agent_company_total_stats/download.htm?filePath=' + json.data;
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
                <div class="panel search">
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right" width="120">运营公司名称：</td>
                            <td><input type="text" class="text" id="agent_company_name"/>&nbsp;&nbsp;</td>
                            <td align="right" width="70">业务类型：</td>
                            <td>
                                <select style="width:80px;" id="category">
                                    <#list Category as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="top: 80px">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='basic.AgentCompanyTotalStats:exportExcel'>
                                <button class="btn btn_red" onclick="exportExcel()">导出数据</button>
                            </@app.has_oper>
                        </div>
                        <h3>运营公司总收入统计</h3>
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
