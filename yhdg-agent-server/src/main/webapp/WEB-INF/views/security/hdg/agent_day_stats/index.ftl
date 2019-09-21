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
                url: "${contextPath}/security/hdg/agent_day_stats/page.htm<#if Session['SESSION_KEY_USER'].agentId != 0>?agentId=${(Session['SESSION_KEY_USER'].agentId)!''}</#if>",
                pageSize: 50,
                pageList: [50, 100],
                idField: 'id',
                frozenColumns: [[
                    {title: '统计日期', align: 'center', field: 'statsDate', width: 100},
                    {title: '运营商名称', align: 'center', field: 'agentName', width: 100}
                ]],
                columns: [
                    [
                        {
                            title: '押金（元）', align: 'center', colspan: 2
                        },
                        {
                            title: '收入（元）', align: 'center', colspan: 8
                        },
                        {
                            title: '分成（元）', align: 'center', colspan: 5
                        },
                        {
                            title: '支出（元）', align: 'center', colspan: 6
                        },
                        {
                            title: '电费', align: 'center', colspan: 2
                        },
                        {
                            title: '当日订单数', align: 'center', colspan: 7
                        },

                        {
                            title: '设备数', align: 'center', colspan: 2
                        },
                        {
                            title: '更新时间', align: 'center', field: 'updateTime', width: 140, rowspan: 2
                        }

                    ],


                    [
                        {
                            title: '收入', align: 'center', field: 'foregiftMoney', width: 60, rowspan: 1,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) ;
                            }
                        },
                        {
                            title: '退款', align: 'center', field: 'foregiftRefundMoney', width: 60, rowspan: 1,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) ;
                            }
                        },
                        {
                            title: '包时段收入', align: 'center', field: 'packetPeriodMoney', width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) ;
                            }
                        },
                        {
                            title: '包时段退款', align: 'center', field: 'refundPacketPeriodMoney', width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) ;
                            }
                        },
                        {
                            title: '按次收入', align: 'center', field: 'exchangeMoney', width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) ;
                            }
                        },
                        {
                            title: '退押金剩余收入', align: 'center', field: 'foregiftRemainMoney', width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) ;
                            }
                        },
                        {
                            title: '保险收入', align: 'center', field: 'insuranceMoney', width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) ;
                            }
                        },
                        {
                            title: '保险退款', align: 'center', field: 'insuranceRefundMoney', width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) ;
                            }
                        },
                        {
                            title: '抵扣券支出', align: 'center', field: 'deductionTicketMoney', width: 70,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) ;
                            }
                        },
                        {
                            title: '合计', align: 'center', field: 'money', width: 60,
                            formatter: function (val, row) {
                                return '<a style="font-weight: bold;">'+Number(val / 100).toFixed(2) + '</a>';
                            }
                        },

                        {
                            title: '运营商收入', align: 'center', field: 'income', width: 60,
                            formatter: function (val, row) {
                                return Number(val / 100).toFixed(2) ;
                            }
                        },
                        {
                            title: '平台收入', align: 'center', field: 'platformIncome', width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '门店收入', align: 'center', field: 'shopMoney', width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '运营公司收入', align: 'center', field: 'agentCompanyMoney', width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '省代收入', align: 'center', field: 'provinceIncome', width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) ;
                            }
                        },
                        {
                            title: '市代收入', align: 'center', field: 'cityIncome', width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },

                        {
                            title: '拉新支出', align: 'center', field: 'laxinPayMoney', width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '认证费支出', align: 'center', field: 'idCardAuthMoney', width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '柜子押金支出', align: 'center', field: 'cabinetForegiftMoney', width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '柜子租金支出', align: 'center', field: 'cabinetRentMoney', width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '电池租金支出', align: 'center', field: 'batteryRentMoney', width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '支出汇总', align: 'center', field: 'sum2', width: 60,
                            formatter: function (val, row) {
                                return '<a style="font-weight: bold;">'+Number((row.idCardAuthMoney + row.cabinetForegiftMoney + row.cabinetRentMoney + row.batteryRentMoney ) / 100).toFixed(2) + '</a>';
                            }
                        },
                        {
                            title: '电量', align: 'center', field: 'electricDegree', width: 80,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '电费', align: 'center', field: 'electricPrice', width: 80,
                            formatter: function (val) {
                                return Number(val / 10000).toFixed(2);
                            }
                        },


                        {title: '押金', align: 'center', field: 'foregiftCount', width: 60},
                        {
                            title: '租金', align: 'center', field: 'packetPeriodOrderCount', width: 60
                        },
                        {title: '保险', align: 'center', field: 'insuranceCount', width: 60},
                        {
                            title: '换电', align: 'center', field: 'exchangeCount', width: 60
                        },
                        {title: '押金退款', align: 'center', field: 'foregiftRefundCount', width: 60},
                        {
                            title: '租金退款', align: 'center', field: 'refundPacketPeriodOrderCount', width: 60
                        },
                        {title: '保险退款', align: 'center', field: 'insuranceRefundCount', width: 60},


                        {
                            title: '换电柜', align: 'center', field: 'cabinetCount', width: 80
                        },
                        {
                            title: '电池', align: 'center', field: 'batteryCount', width: 80
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
            var statsDate = $('#stats_date').datebox('getValue');
            var category = $('#category').val();

            datagrid.datagrid('options').queryParams = {
                statsDate: statsDate,
               category: category
            };
            datagrid.datagrid('load');
        }
        function exportExcel() {
            $.messager.confirm('提示信息', '确认导出数据?', function (ok) {
                if (ok) {
                    var statsDate = $('#stats_date').datebox('getValue');
                    var category = $('#category').val();
                    $.messager.progress();
                    $.post('${contextPath}/security/hdg/agent_day_stats/export_excel.htm', {
                        statsDate: statsDate,
                        agentId: ${(Session['SESSION_KEY_USER'].agentId)!''},
                        category: category
                    }, function (json) {
                        $.messager.progress('close');
                        if (json.success) {
                            document.location.href = '${contextPath}/security/hdg/agent_day_stats/download.htm?filePath=' + json.data;
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
                            <td><input type="text" class="easyui-datebox" id="stats_date" style="width: 150px;height: 28px;"/>&nbsp;&nbsp;
                            </td>
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
                            <@app.has_oper perm_code='hdg.AgentDayStats:exportExcel'>
                                <button class="btn btn_red" onclick="exportExcel()">导出数据</button>
                            </@app.has_oper>
                        </div>
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
