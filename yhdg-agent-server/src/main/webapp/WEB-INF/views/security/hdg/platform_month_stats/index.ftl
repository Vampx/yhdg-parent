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
                url: "${contextPath}/security/hdg/platform_month_stats/page.htm",
                pageSize: 50,
                pageList: [50, 100],
                idField: 'id',
                frozenColumns: [[
                    {title: '统计日期', align: 'center', field: 'statsMonth', width: 80}
                ]],
                columns: [
                    [
                        {
                            title: '运营商总收入', align: 'center', field: 'agentIncome', width: 100,
                            formatter: function (val) {
                                return new Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {
                            title: '新增收入', align: 'center', field: 'incrementPlatformIncome', width: 100,
                            formatter: function (val) {
                                return new Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {
                            title: '新增押金收入', align: 'center', field: 'incrementForegiftMoney', width: 100,
                            formatter: function (val) {
                                return new Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {
                            title: '新增换电收入', align: 'center', field: 'incrementExchangeMoney', width: 100,
                            formatter: function (val) {
                                return new Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {
                            title: '新增包时段收入', align: 'center', field: 'incrementPacketPeriodMoney', width: 100,
                            formatter: function (val) {
                                return new Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {
                            title: '新增充值收入', align: 'center', field: 'incrementDepositMoney', width: 100,
                            formatter: function (val) {
                                return new Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {title: '新增换电单量', align: 'center', field: 'incrementExchangeCount', width: 100},
                        {title: '新增押金单量', align: 'center', field: 'incrementForegiftCount', width: 100},
                        {title: '新增充值单量', align: 'center', field: 'incrementDepositCount', width: 100},
                        {
                            title: '新增退款', align: 'center', field: 'incrementRefundMoney', width: 100,
                            formatter: function (val) {
                                return new Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {
                            title: '新增押金退款', align: 'center', field: 'incrementRefundForegiftMoney', width: 100,
                            formatter: function (val) {
                                return new Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {
                            title: '新增换电退款', align: 'center', field: 'incrementRefundExchangeMoney', width: 100,
                            formatter: function (val) {
                                return new Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {
                            title: '新增包时段退款', align: 'center', field: 'incrementRefundPacketPeriodMoney', width: 100,
                            formatter: function (val) {
                                return new Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {
                            title: '新增充值退款', align: 'center', field: 'incrementRefundDepositMoney', width: 100,
                            formatter: function (val) {
                                return new Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {title: '新增换电退款单量', align: 'center', field: 'incrementRefundExchangeCount', width: 100},
                        {title: '新增押金退款单量', align: 'center', field: 'incrementRefundForegiftCount', width: 100},
                        {title: '新增充值退款单量', align: 'center', field: 'incrementRefundDepositCount', width: 100},
                        {title: '新增设备数', align: 'center', field: 'incrementCabinetCount', width: 100},
                        {title: '新增客户人数', align: 'center', field: 'incrementCustomerCount', width: 100},
                        {title: '新增投诉量', align: 'center', field: 'incrementFeedbackCount', width: 100},
                        {title: '未使用人数统计', align: 'center', field: 'notUseCount', width: 100},
                        {
                            title: '更新时间', align: 'center', field: 'updateTime', width: 140
                        }
                    ]
                ],
                onLoadSuccess: function () {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                }
            });
        })

        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var year = $('#year').combobox("getValue");
            var month = $('#month').combobox("getValue");
            if ((year != null && year != '') || (month != null && month != '')) {
                var statsMonth = year + "-" + month;
            } else {
                var statsMonth = '';
            }
            datagrid.datagrid('options').queryParams = {
                statsMonth: statsMonth
            };
            datagrid.datagrid('load');
        }

        function exportExcel() {

            $.messager.confirm('提示信息', '确认导出数据?', function (ok) {
                if (ok) {
                    var year = $('#year').combobox("getValue");
                    var month = $('#month').combobox("getValue");
                    if ((year != null && year != '') || (month != null && month != '')) {
                        var statsMonth = year + "-" + month;
                    } else {
                        var statsMonth = '';
                    }
                    $.messager.progress();
                    $.post('${contextPath}/security/hdg/platform_month_stats/export_excel.htm', {
                        statsMonth: statsMonth
                    }, function (json) {
                        $.messager.progress('close');
                        if (json.success) {
                            document.location.href = '${contextPath}/security/hdg/platform_month_stats/download.htm?filePath=' + json.data;
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
                            <td align="right">年份：</td>
                            <td>
                                <select name="year" id="year" class="easyui-combobox"
                                        style="width; 60px; height: 28px;">
                                    <option value="">无</option>
                                    <#list yearList as e>
                                        <option value="${e}">${e}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right" width="50px;">月份：</td>
                            <td>
                                <select name="month" id="month" class="easyui-combobox"
                                        style="width; 60px; height: 28px;">
                                    <option value="">无</option>
                                    <#list monthList as e>
                                        <option value="${e}">${e}</option>
                                    </#list>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="top: 80px">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='2_8_7_2'>
                                <button class="btn btn_red" onclick="exportExcel()">导出数据</button>
                            </@app.has_oper>
                        </div>
                        <h3>平台月收入统计</h3>
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
