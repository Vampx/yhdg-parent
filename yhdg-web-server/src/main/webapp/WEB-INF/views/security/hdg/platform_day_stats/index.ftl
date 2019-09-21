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
                url: "${contextPath}/security/hdg/platform_day_stats/page.htm",
                pageSize: 50,
                pageList: [50, 100],
                idField: 'id',
                frozenColumns: [[
                    {title: '统计日期', align: 'center', field: 'statsDate', width: 100}
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
                            title: '总收入', align: 'center', field: 'totalPlatformIncome', width: 100,
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
                            title: '总押金收入', align: 'center', field: 'totalForegiftMoney', width: 100,
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
                            title: '总换电收入', align: 'center', field: 'totalExchangeMoney', width: 100,
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
                            title: '总包时段收入', align: 'center', field: 'totalPacketPeriodMoney', width: 100,
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
                            title: '总充值收入', align: 'center', field: 'totalDepositMoney', width: 100,
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
                        {title: '总换电单量', align: 'center', field: 'totalExchangeCount', width: 100},
                        {title: '新增换电单量', align: 'center', field: 'incrementExchangeCount', width: 100},
                        {title: '总押金单量', align: 'center', field: 'totalForegiftCount', width: 100},
                        {title: '新增押金单量', align: 'center', field: 'incrementForegiftCount', width: 100},
                        {title: '总充值单量', align: 'center', field: 'totalDepositCount', width: 100},
                        {title: '新增充值单量', align: 'center', field: 'incrementDepositCount', width: 100},
                        {
                            title: '总退款', align: 'center', field: 'totalRefundMoney', width: 100,
                            formatter: function (val) {
                                return new Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {
                            title: '新增退款', align: 'center', field: 'incrementRefundMoney', width: 100,
                            formatter: function (val) {
                                return new Number(val / 100).toFixed(2)+"元";
                            }
                        },
                        {
                            title: '总押金退款', align: 'center', field: 'totalRefundForegiftMoney', width: 100,
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
                            title: '总换电退款', align: 'center', field: 'totalRefundExchangeMoney', width: 100,
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
                            title: '总包时段退款', align: 'center', field: 'totalRefundPacketPeriodMoney', width: 100,
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
                            title: '总充值退款', align: 'center', field: 'totalRefundDepositMoney', width: 100,
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
                        {title: '总换电退款单量', align: 'center', field: 'totalRefundExchangeCount', width: 100},
                        {title: '新增换电退款单量', align: 'center', field: 'incrementRefundExchangeCount', width: 100},
                        {title: '总押金退款单量', align: 'center', field: 'totalRefundForegiftCount', width: 100},
                        {title: '新增押金退款单量', align: 'center', field: 'incrementRefundForegiftCount', width: 100},
                        {title: '总充值退款单量', align: 'center', field: 'totalRefundDepositCount', width: 100},
                        {title: '新增充值退款单量', align: 'center', field: 'incrementRefundDepositCount', width: 100},
                        {title: '总设备数', align: 'center', field: 'totalCabinetCount', width: 100},
                        {title: '新增设备数', align: 'center', field: 'incrementCabinetCount', width: 100},
                        {title: '总客户人数', align: 'center', field: 'totalCustomerCount', width: 100},
                        {title: '新增客户人数', align: 'center', field: 'incrementCustomerCount', width: 100},
                        {title: '总投诉量', align: 'center', field: 'totalFeedbackCount', width: 100},
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

            var statsDate = $('#stats_date').datebox('getValue');
            datagrid.datagrid('options').queryParams = {
                statsDate: statsDate
            };
            datagrid.datagrid('load');
        }

        function exportExcel() {
            $.messager.confirm('提示信息', '确认导出数据?', function (ok) {
                if (ok) {
                    var statsDate = $('#stats_date').datebox('getValue');
                    $.messager.progress();
                    $.post('${contextPath}/security/hdg/platform_day_stats/export_excel.htm', {
                        statsDate: statsDate
                    }, function (json) {
                        $.messager.progress('close');
                        if (json.success) {
                            document.location.href = '${contextPath}/security/hdg/platform_day_stats/download.htm?filePath=' + json.data;
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
                            <td align="right">日期：</td>
                            <td><input type="text" class="easyui-datebox" id="stats_date" />&nbsp;&nbsp;
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="top: 80px">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='hdg.PlatformDayStats:exportExcel'>
                                <button class="btn btn_red" onclick="exportExcel()">导出数据</button>
                            </@app.has_oper>
                        </div>
                        <h3>平台日收入统计</h3>
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
