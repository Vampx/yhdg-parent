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
                url: "${contextPath}/security/hdg/agent_material_day_stats/page.htm",
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
                        {field: 'checkbox', checkbox: true},
                        {
                            title: '所属运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 40
                        },
                        {
                            title: '结算日期',
                            align: 'center',
                            field: 'statsDate',
                            width: 30
                        },
                        {
                            title: '总金额',
                            align: 'center',
                            field: 'money',
                            width: 30,
                            formatter: function (val, row) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
//                        {
//                            title: '设备押金数量',
//                            align: 'center',
//                            field: 'cabinetForegiftCount',
//                            width: 50,
//                            formatter: function (val, row) {
//                                if (val != null) {
//                                    return '<a href="javascript:view_cabinet_foregift('+row.id+')"><u>'+val+'</u></a>';
//                                } else {
//                                    return '';
//                                }
//                            }
//                        },
                        {
                            title: '设备押金金额',
                            align: 'center',
                            field: 'cabinetForegiftMoney',
                            width: 30,
                            formatter: function (val, row) {
                                if(val != null) {
                                    return '<a href="javascript:view_cabinet_foregift('+row.id+')"><u>'+Number(val / 100).toFixed(2)+'</u></a>';
                                }
                            }
                        },
//                        {
//                            title: '设备租金数量',
//                            align: 'center',
//                            field: 'cabinetRentCount',
//                            width: 50,
//                            formatter: function (val, row) {
//                                if (val != null) {
//                                    return '<a href="javascript:view_cabinet_rent('+row.id+')"><u>'+val+'</u></a>';
//                                } else {
//                                    return '';
//                                }
//                            }
//                        },
                        {
                            title: '设备租金金额',
                            align: 'center',
                            field: 'cabinetRentMoney',
                            width: 30,
                            formatter: function (val, row) {
                                if (val != null) {
                                    return '<a href="javascript:view_cabinet_rent('+row.id+')"><u>'+Number(val / 100).toFixed(2)+'</u></a>';
                                } else {
                                    return '';
                                }
                            }
                        },
//                        {
//                            title: '电池租金数量',
//                            align: 'center',
//                            field: 'batteryRentCount',
//                            width: 50,
//                            formatter: function (val, row) {
//                                if (val != null) {
//                                    return '<a href="javascript:view_battery_rent('+row.id+')"><u>'+val+'</u></a>';
//                                } else {
//                                    return '';
//                                }
//                            }
//                        },
                        {
                            title: '电池租金金额',
                            align: 'center',
                            field: 'batteryRentMoney',
                            width: 30,
                            formatter: function (val, row) {
                                if (val != null) {
                                    return '<a href="javascript:view_battery_rent('+row.id+')"><u>'+Number(val / 100).toFixed(2)+'</u></a>';
                                } else {
                                    return '';
                                }
                            }
                        },
                        {
                            title: '客户认证金额',
                            align: 'center',
                            field: 'idCardAuthMoney',
                            width: 30,
                            formatter: function (val, row) {
                                if (val != null) {
                                    return '<a href="javascript:view_id_card_auth('+row.id+')"><u>'+Number(val / 100).toFixed(2)+'</u></a>';
                                } else {
                                    return '';
                                }
                            }
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 30
                        },
                        {
                            title: '支付时间',
                            align: 'center',
                            field: 'payTime',
                            width: 60
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 60
                        }
                    ]
                ],
                onLoadSuccess: function () {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                }
            });
        })
        ;

        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');

        }
        //柜子押金详情
        function view_cabinet_foregift(id) {
            App.dialog.show({
                css: 'width:1000px;height:630px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/agent_cabinet_foregift_record/view_cabinet_foregift.htm?materialDayStatsId=" + id
            });
        }
        //柜子租金详情
        function view_cabinet_rent(id) {
            App.dialog.show({
                css: 'width:1000px;height:630px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/agent_cabinet_rent_record/view_cabinet_rent.htm?materialDayStatsId=" + id
            });
        }
        //电池租金详情
        function view_battery_rent(id) {
            App.dialog.show({
                css: 'width:1000px;height:630px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/agent_battery_rent_record/view_battery_rent.htm?materialDayStatsId=" + id
            });
        }
        //认证金额
        function view_id_card_auth(id) {
            App.dialog.show({
                css: 'width:1000px;height:630px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/id_card_auth_record/view_id_card_auth_record.htm?materialDayStatsId=" + id
            });
        }

        function query() {
            var datagrid = $('#page_table');
            var agentId = $('#agent_id').combotree("getValue");
            var status = $('#status').val();
            var category = $('#category').val();
            var queryBeginTime = $('#begin_time').datebox('getValue');
            var queryEndTime = $('#end_time').datebox('getValue');
/*            if (queryBeginTime != null && queryEndTime != null && queryBeginTime >= queryEndTime) {
                $.messager.alert('提示信息', '结束日期必须大于开始日期', 'info');
                return;
            }*/

            var queryParams = {
                queryBeginTime: queryBeginTime,
                queryEndTime: queryEndTime,
                category: category,
                agentId: agentId,
                status: status
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function pay_money() {
            var datagrid = $('#page_table');
            var checked = datagrid.datagrid('getChecked');
            var list = [];
            for (var i = 0; i < checked.length; i++) {
                list.push(checked[i].id);
            }
            if (list.length == 0) {
                $.messager.alert('info', '请选择记录', 'info');
                return;
            }

            $.messager.confirm('提示信息', '确认支付?', function (ok) {
                if (ok) {
                    $.post('${contextPath}/security/hdg/agent_material_day_stats/pay_money.htm', {
                        ids: list
                    }, function (json) {
                        if (json.success) {
                            $.messager.alert('info', '操作成功', 'info');
                            reload();
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });

        }

        function exportExcel() {
            $.messager.confirm('提示信息', '确认导出数据?', function (ok) {
                if (ok) {
                    var agentId = $('#agent_id').combotree("getValue");
                    var status = $('#status').val();
                    var category = $('#category').val();
                    var queryBeginTime = $('#begin_time').datebox('getValue');
                    var queryEndTime = $('#end_time').datebox('getValue');
                    $.messager.progress();
                    $.post('${contextPath}/security/hdg/agent_material_day_stats/export_excel.htm', {
                        agentId: agentId,
                        status: status,
                        category: category,
                        queryBeginTime: queryBeginTime,
                        queryEndTime: queryEndTime
                    }, function (json) {
                        $.messager.progress('close');
                        if (json.success) {
                            document.location.href = '${contextPath}/security/hdg/agent_material_day_stats/download.htm?filePath=' + json.data;
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
                                <input id="agent_id" class="easyui-combotree" editable="true"
                                       style="width: 150px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:true,
                                multiple:false,
                                panelHeight:'200'
                            "
                                >
                            </td>
                            <td align="right" width="70">业务类型：</td>
                            <td>
                                <select style="width:80px;" id="category">
                                    <#list Category as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right" width="50">状态：</td>
                            <td>
                                <select style="width:70px;" id="status">
                                    <option value="">所有</option>
                                    <#list StatusEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right" width="90">查询时间段：</td>
                            <td>
                                <input type="text" class="easyui-datebox" id="begin_time" name="queryBeginTime" style="width: 140px;height: 28px;"/>
                                至
                                <input type="text" class="easyui-datebox" id="end_time" name="queryEndTime" style="width: 140px;height: 28px;"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='basic.BalanceRecord:payMoney'>
                                <button class="btn btn_green" onclick="pay_money()">支付</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='basic.BalanceRecord:exportExcel'>
                                <button class="btn btn_red" onclick="exportExcel()">导出数据</button>
                            </@app.has_oper>
                        </div>
                        <h3>运营商支出</h3>
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