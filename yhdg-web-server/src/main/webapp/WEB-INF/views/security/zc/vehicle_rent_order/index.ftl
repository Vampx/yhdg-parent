<@app.html>
    <@app.head>
    <script>
        $(function() {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/zc/vehicle_rent_order/page.htm",
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
                            title: '订单id',
                            align: 'center',
                            field: 'id',
                            width: 25
                        },
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 25
                        },
                        {
                            title: '门店名称',
                            align: 'center',
                            field: 'shopName',
                            width: 25
                        },
                        {
                            title: '姓名',
                            align: 'center',
                            field: 'customerFullname',
                            width: 20
                        },
                        {
                            title: '手机号',
                            align: 'center',
                            field: 'customerMobile',
                            width: 20
                        },
                        {
                            title: '电池类型',
                            align: 'center',
                            field: 'batteryTypeName',
                            width: 20
                        },
                        {
                            title: '电池编号',
                            align: 'center',
                            field: 'batteryId',
                            width: 20
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 20
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 20
                        },
                        {
                            title: '还电时间',
                            align: 'center',
                            field: 'backTime',
                            width: 20
                        },
                        {
                            title: '还电人',
                            align: 'center',
                            field: 'backOperator',
                            width: 20
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 30,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='zd.VehicleRentOrder:view'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
//                                if(row.status == 3 || row.status == 4) {
//                                    html += ' <a href="javascript:editExtendRent(\'ID\')">延长租期</a>';
//                                }
                                return html.replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                onLoadSuccess:function() {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                },
                queryParams: {
                    appId: 0
                }
            });
        });

        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function reloadTree() {
            var agentId = $('#agent_id').combotree('getValue');
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId
            };
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var partnerId = $('#partner_id').combobox('getValue');
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();

            var queryParams = {
                queryName: queryName,
                agentId: $('#agent_id').combotree('getValue'),
                status: $('#status').val(),
                partnerId: partnerId
            };
            queryParams[queryName] = queryValue;
            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:1000px;height:510px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/zc/vehicle_rent_order/view.htm?id=" + id

            });
        }

        function updateStatus() {
            var datagrid = $('#page_table');
            var list = datagrid.datagrid('getSelections');
            if (list.length == 0) {
                $.messager.alert('提示消息', '请选择记录', 'info');
                return;
            }
            if (list.length > 1) {
                $.messager.alert('提示消息', '只能选择一条记录', 'info');
                return;
            }

            $.messager.confirm('提示信息', '是否结束订单?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/zc/vehicle_rent_order/updateStatus.htm?id=" + list[0].id, function (json) {
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
        function exchangeBattery() {
            var datagrid = $('#page_table');
            var list = datagrid.datagrid('getSelections');
            if (list.length == 0) {
                $.messager.alert('提示消息', '请选择记录', 'info');
                return;
            }
            if (list.length > 1) {
                $.messager.alert('提示消息', '只能选择一条记录', 'info');
                return;
            }

            $.messager.prompt('提示信息', '请输入电池编号：', function (batteryId) {
                batteryId = batteryId.trim();
                if (batteryId) {
                    $.post("${contextPath}/security/zc/vehicle_rent_order/exchange_battery.htm?", {
                        id: list[0].id,
                        batteryId: batteryId
                    }, function (json) {
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
        <#--function editExtendRent(id) {-->
            <#--App.dialog.show({-->
                <#--css: 'width:400px;height:160px;overflow:visible;',-->
                <#--title: '延长租期',-->
                <#--href: "${contextPath}/security/zd/rent_period_order/edit_extend_rent.htm?id=" + id,-->
                <#--event: {-->
                    <#--onClose: function() {-->
                        <#--reload();-->
                    <#--}-->
                <#--}-->
            <#--});-->
        <#--}-->
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
                            <td align="right">商户：</td>
                            <td>
                                <input name="partnerId" id="partner_id" style="height: 28px;width: 142px;" class="easyui-combobox"  editable="false"
                                       data-options="url:'${contextPath}/security/basic/partner/list.htm?dummy=${'无'?url}',
                                        method:'get',
                                        valueField:'id',
                                        textField:'partnerName',
                                        editable:false,
                                        multiple:false,
                                        panelHeight:'200',
                                        onSelect: function(node) {
                                            query();
                                        }"
                                />
                            </td>
                            <td align="right" style="width: 60px;">运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="true" style="width: 200px;height: 28px;"
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
                            <td align="right" width="115">
                                <select style="width:100px;" id="query_name">
                                    <option value="customerMobile">手机号</option>
                                    <option value="customerFullname">姓名</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"  style="width: 170px;"/></td>
                            <td align="right" width="60">状态：</td>
                            <td>
                                <select style="width:90px;" id="status" >
                                    <option value=""selected = "selected">所有</option>
                                    <#list StatusEnum as e>
                                        <option value="${e.getValue()}" >${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                           <#-- <@app.has_oper perm_code='zd.RentOrder:updateStatus'>
                                <button class="btn btn_green" onclick="updateStatus()">结束订单</button>
                            </@app.has_oper>-->
                            <@app.has_oper perm_code='zd.RentOrder:exchangeBattery'>
                                <button class="btn btn_green" onclick="exchangeBattery()">更换电池</button>
                            </@app.has_oper>
                        </div>
                        <h3>租电订单记录</h3>
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

