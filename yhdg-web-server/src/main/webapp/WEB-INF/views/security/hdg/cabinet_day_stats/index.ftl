<@app.html>
    <@app.head>
    <script>
        $(function() {
            $('#page_table').datagrid({
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                fit: true,
                striped: true,
                singleSelect: true,
                collapsible: true,
                pagination: true,
                url: "${contextPath}/security/hdg/cabinet_day_stats/page.htm",
                pageSize: 50,
                pageList: [50, 100],
                idField: 'id',
                frozenColumns: [[
                    {title: '统计日期', align: 'center', field: 'statsDate', width: 90},
                    {title: '运营商名称', align: 'center', field: 'agentName', width: 90},
                    {title: '换电柜编号', align: 'center', field: 'cabinetId', width: 120},
                    {title: '换电柜名称', align: 'center', field: 'cabinetName', width: 120}
                ]],
                columns: [
                    [
                        {
                            title: '当日总金额', align: 'center', field: 'money', width: 90,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {
                            title: '押金金额', align: 'center', field: 'foregiftMoney', width: 80,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {
                            title: '押金退款金额', align: 'center', field: 'refundForegiftMoney', width: 100,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {
                            title: '包时段金额', align: 'center', field: 'packetPeriodMoney', width: 80,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {
                            title: '包时段退款金额', align: 'center', field: 'agentRefundPacketPeriodMoney', width: 110,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {
                            title: '换电金额', align: 'center', field: 'exchangeMoney', width: 80,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {title: '押金数量', align: 'center', field: 'foregiftCount', width: 80},
                        {title: '包时段数量', align: 'center', field: 'packetPeriodCount', width: 100},
                        {title: '订单次数', align: 'center', field: 'orderCount', width: 80},
                        {title: '活跃人数', align: 'center', field: 'activeCustomerCount', width: 80},
                        {
                            title: '用电量', align: 'center', field: 'electricDegree', width: 80,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '单价', align: 'center', field: 'unitPrice', width: 80,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {
                            title: '用电费用', align: 'center', field: 'electricPrice', width: 80,
                            formatter: function (val) {
                                return Number(val / 10000).toFixed(2)+"元";
                            }
                        },
                        {
                            title: '更新时间', align: 'center', field: 'updateTime', width: 140
                        }
                    ]
                ],
                onLoadSuccess:function() {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                }
            });
        });

        function countTotal() {
            var rows = $('#page_table').datagrid('getRows');//获取当前的数据行
            var totalIncome = 0 ,totalCusteomer = 0, totalPower = 0; totalDegree = 0;
            for (var i = 0; i < rows.length; i++) {
                totalIncome += rows[i]['money'];
                totalPower += rows[i]['power'];
                totalCusteomer += rows[i]['orderCount'];
                totalDegree += rows[i]['degree'];
            }
            $('#totalIncome').attr('value', Number(totalIncome / 100).toFixed(2));
            $('#totalCusteomer').attr('value', totalCusteomer);
            $('#totalPower').attr('value', Number(totalPower/120000).toFixed(5));
            $('#totalDegree').attr('value', Number(totalDegree/100).toFixed(2));
        }


        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var agentId =$('#agent_id').combotree('getValue');
            var cabinetName = $('#cabinet_name').val();
            var cabinetId = $('#cabinet_id').val();
            var statsDate = $('#stats_date').datebox('getValue');
            datagrid.datagrid('options').queryParams = {
                    agentId: agentId,
                    cabinetId: cabinetId,
                    statsDate: statsDate,
                    cabinetName: cabinetName
            };
            datagrid.datagrid('load');
        }
        function exportExcel() {
            $.messager.confirm('提示信息', '确认导出数据?', function(ok) {
                if(ok) {
                    var agentId =$('#agent_id').combotree('getValue');
                    var cabinetName = $('#cabinet_name').val();
                    var cabinetId = $('#cabinet_id').val();
                    var statsDate = $('#stats_date').datebox('getValue');
                    $.messager.progress();
                    $.post('${contextPath}/security/hdg/cabinet_day_stats/export_excel.htm', {
                            agentId: agentId,
                            cabinetId: cabinetId,
                            statsDate: statsDate,
                            cabinetName: cabinetName
                    }, function (json) {
                        $.messager.progress('close');
                        if (json.success) {
                            document.location.href = '${contextPath}/security/hdg/cabinet_day_stats/download.htm?filePath=' + json.data;
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
                            <td align="right">运营商：</td>
                            <td>
                                <input name="agentId" id="agent_id" class="easyui-combotree" editable="true" style="width: 200px;height: 28px;"
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
                            <td align="right">换电柜编号：</td>
                            <td><input type="text" class="text" id="cabinet_id"/>&nbsp;&nbsp;</td>
                            <td align="right">换电柜名称：</td>
                            <td><input type="text" class="text" id="cabinet_name"/>&nbsp;&nbsp;</td>
                            <td align="right">日期：</td>
                            <td><input type="text" class="easyui-datebox" id="stats_date" />&nbsp;&nbsp;</td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap"style="top: 80px">
                    <div class="toolbar clearfix" >
                        <div class="float_right">
                            <@app.has_oper perm_code='hdg.CabinetDayStats:exportExcel'>
                                <button class="btn btn_red" onclick="exportExcel()">导出数据</button>
                            </@app.has_oper>
                        </div>
                        <h3>换电柜日收入统计</h3>
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
