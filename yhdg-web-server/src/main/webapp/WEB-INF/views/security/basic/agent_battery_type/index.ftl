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
                url: "${contextPath}/security/basic/agent_battery_type/page.htm",
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
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 40
                        },
                        {
                            title: '类型名称',
                            align: 'center',
                            field: 'typeName',
                            width: 80
                        },
                        {
                            title: '额定电压(V)',
                            align: 'center',
                            field: 'ratedVoltage',
                            width: 80,
                            formatter: function (val) {
                                return Number(val / 1000);
                            }
                        },
                        {
                            title: '额定容量(Ah)',
                            align: 'center',
                            field: 'ratedCapacity',
                            width: 80,
                            formatter: function (val) {
                                return Number(val / 1000);
                            }
                        },
                        {
                            title: '是否激活',
                            align: 'center',
                            field: 'isActive',
                            width: 60,
                            formatter: function(val) {
                                if(val == 1) {
                                    return '<a style="color: #00FF00;">是</a>';
                                }else{
                                    return '<a style="color: #ff0000;">否</a>';
                                }
                            }
                        },
                        {
                            title: '备注',
                            align: 'center',
                            field: 'memo',
                            width: 60
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 80,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.AgentBatteryType:view'>
                                    html += '<a href="javascript:view(\'BATTERY_TYPE\', AGENT_ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.AgentBatteryType:edit'>
                                    html += ' <a href="javascript:edit(\'BATTERY_TYPE\', AGENT_ID)">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.AgentBatteryType:remove'>
                                    html += ' <a href="javascript:remove(\'BATTERY_TYPE\', AGENT_ID)">删除</a>';
                                </@app.has_oper>
                                return html.replace(/BATTERY_TYPE/g, row.batteryType).replace(/AGENT_ID/g, row.agentId);
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
            var queryParams = {
                agentId: agentId,
                typeName: $('#type_name').val()
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function add() {
            window.open("${contextPath}/security/main/module.htm?moduleId=3&url=${contextPath}/security/basic/agent_battery_type/add.htm");
        }

        function edit(batteryType,agentId) {
            window.open('${contextPath}/security/basic/agent_battery_type/edit.htm?agentId=' + agentId+"&batteryType=" + batteryType);
        }

        function view(batteryType,agentId) {
            window.open('${contextPath}/security/basic/agent_battery_type/view.htm?agentId=' + agentId+"&batteryType=" + batteryType)
        }

        function remove(batteryType,agentId) {
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/agent_battery_type/delete.htm?batteryType=" + batteryType + "&agentId=" + agentId, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            reload();
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        }

        function reloadTree() {
            var agentId = $('#agent_id').combotree('getValue');
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId
            };
            datagrid.datagrid('reload');
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
                    <table cellpadding="0" cellspacing="0" border="0" style="border-collapse:separate; border-spacing: 0px 3px;">
                            <td align="right">运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="true" style="width: 130px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:true,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                   reloadTree();
                                }
                            "
                                >
                            </td>
                            <td align="right">类型名称：</td>
                            <td><input type="text" style="width:170px; height: 28px;" class="text"
                                       id="type_name"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-top: 30px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='basic.AgentBatteryType:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>电池类型</h3>
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