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
                url: "${contextPath}/security/hdg/fault_log/page.htm",
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
                            title: 'checkbox', checkbox: true
                        },
                        {
                            title: '级别',
                            align: 'center',
                            field: 'faultLevelName',
                            width: 20
                        },
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 40
                        },
                        {
                            title: '订单ID',
                            align: 'center',
                            field: 'orderId',
                            width: 40
                        },
                        {
                            title: '换电柜',
                            align: 'center',
                            field: 'cabinetId',
                            width: 40,
                            formatter: function (val, row) {
                                if (val != null) {
                                    return val + '(' + row.cabinetName + ')'
                                } else {
                                    return ''
                                }
                            }
                        },

                        {
                            title: '格口',
                            align: 'center',
                            field: 'boxNum',
                            width: 30
                        },
                        {
                            title: '电池',
                            align: 'center',
                            field: 'batteryId',
                            width: 40,
                            formatter: function (val) {
                                if (val != null) {
                                    return '<a href="#" onclick="battery_view(\''+val+'\')"><u>'+val+'</u></a>';
                                } else {
                                    return '';
                                }
                            }
                        },
                        {
                            title: '品牌',
                            align: 'center',
                            field: 'brandName',
                            width: 40
                        },
                        {
                            title: '故障类型',
                            align: 'center',
                            field: 'faultTypeName',
                            width: 40
                        },
                        {
                            title: '故障内容',
                            align: 'center',
                            field: 'faultContent',
                            width: 100
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 30
                        },
                        {
                            title: '处理时间',
                            align: 'center',
                            field: 'handleTime',
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
                            field: 'id',
                            width: 50,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='hdg.FaultLog:view'>
                                    html += '<a href="javascript:view(ID)">查看&nbsp;&nbsp;</a>';
                                </@app.has_oper>
                                if (row.status == 1) {
                                    <@app.has_oper perm_code='hdg.FaultLog:edit'>
                                        html += '<a href="javascript:edit(ID)">处理</a>';
                                    </@app.has_oper>
                                }
                                return html.replace(/ID/g, row.id);
                            }
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

        function view(id) {
            App.dialog.show({
                css: 'width:660px;height:430px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/fault_log/view.htm?id=" + id
            });
        }

        function battery_view(id) {
            App.dialog.show({
                css: 'width:780px;height:512px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/battery/view.htm?id=" + id
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:660px;height:300px;overflow:visible;',
                title: '处理',
                href: "${contextPath}/security/hdg/fault_log/edit.htm?id=" + id,
                event: {
                    onClose: function () {
                        reload();
                    },
                    onLoad: function () {
                    }
                }
            });
        }

        function edit_status() {
            var faultLogs = $('#page_table').datagrid('getChecked');
            if (faultLogs.length > 0) {
                $.messager.confirm('提示信息', '确认处理？', function (ok) {
                    if (ok) {
                        var faultLogIds = [];
                        for (var i = 0; i < faultLogs.length; i++) {
                            faultLogIds.push(faultLogs[i].id);
                        }
                        $.post('${contextPath}/security/hdg/fault_log/batch_edit_status.htm', {
                            faultLogIds: faultLogIds
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
            } else {
                $.messager.alert('提示信息', '请选择故障明细', 'info');
                return false;
            }

        }


        function query() {
            var datagrid = $('#page_table');
            var faultLevel = $('#fault_level').combobox('getValue');
            var status = $('#status').combobox('getValue');
            var faultType = $('#fault_type').val();
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();

            var queryParams = {
                faultLevel: faultLevel,
                status: status,
                faultType: faultType
            };
            queryParams[queryName] = queryValue;
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
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
                            <td align="right" width="115">
                                <select style="width:100px;" id="query_name">
                                    <option value="orderId">订单编号</option>
                                    <option value="cabinetId">换电柜编号</option>
                                    <option value="subcabinetId">换电柜编号</option>
                                    <option value="batteryId">电池编号</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="query_value" style="width: 170px;"/></td>
                            <td align="right" width="90">故障类型：</td>
                            <td>
                                <select style="width:120px;" id="fault_type">
                                    <option value="">所有</option>
                                    <#list FaultTypeEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right" width="90">状态：</td>
                            <td>
                                <select style="height: 28px;width:70px;" id="status" class="easyui-combobox">
                                    <option value="">所有</option>
                                    <#list StatusEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                                &nbsp;&nbsp;
                            </td>
                            <td align="right">级别：</td>
                            <td>
                                <select id="fault_level" class="easyui-combobox" style="height: 28px;width:70px;">
                                    <option value="">所有</option>
                                    <#if FaultLevelEnum??>
                                        <#list FaultLevelEnum as e>
                                            <option value="${(e.getValue())!''}">${(e.getName())!''}</option>
                                        </#list>
                                    </#if>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='hdg.FaultLog:editStatus'>
                                <button class="btn btn_green" onclick="edit_status()">批量处理</button>
                            </@app.has_oper>
                        </div>
                        <h3>故障明细</h3>
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