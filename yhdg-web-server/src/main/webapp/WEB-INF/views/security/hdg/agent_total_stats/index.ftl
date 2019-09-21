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
                url: "${contextPath}/security/hdg/agent_total_stats/page.htm",
                pageSize: 50,
                pageList: [50, 100],
                idField: 'id',
                frozenColumns: [[
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
                            title: '总订单数', align: 'center', colspan: 7
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
                                return '<a style="font-weight: bold;">'+Number((row.laxinPayMoney + row.idCardAuthMoney + row.cabinetForegiftMoney + row.cabinetRentMoney + row.batteryRentMoney ) / 100).toFixed(2) + '</a>';
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


        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var agentId = $('#agent_id').combotree('getValue');
            var agentName = $('#agent_name').val();
            var category = $('#category').val();

            datagrid.datagrid('options').queryParams = {
                agentId: agentId,
                agentName: agentName,
                category: category
            };
            datagrid.datagrid('load');
        }
        function exportExcel() {
            $.messager.confirm('提示信息', '确认导出数据?', function (ok) {
                if (ok) {
                    var agentId = $('#agent_id').combotree('getValue');
                    var agentName = $('#agent_name').val();
                    var category = $('#category').val();
                    $.messager.progress();
                    $.post('${contextPath}/security/hdg/agent_total_stats/export_excel.htm', {
                        agentId: agentId,
                        agentName: agentName,
                        category: category
                    }, function (json) {
                        $.messager.progress('close');
                        if (json.success) {
                            document.location.href = '${contextPath}/security/hdg/agent_total_stats/download.htm?filePath=' + json.data;
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
                            <@app.has_oper perm_code='hdg.AgentTotalStats:exportExcel'>
                                <button class="btn btn_red" onclick="exportExcel()">导出数据</button>
                            </@app.has_oper>
                        </div>
                        <h3>运营商总收入统计</h3>
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
