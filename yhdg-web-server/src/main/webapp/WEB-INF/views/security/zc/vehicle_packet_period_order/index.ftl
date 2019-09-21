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
                url: "${contextPath}/security/zc/vehicle_packet_period_order/page.htm",
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
                            title: '换电柜编号',
                            align: 'center',
                            field: 'cabinetId',
                            width: 25
                        },
                        {
                            title: '换电柜名称',
                            align: 'center',
                            field: 'cabinetName',
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
                            title: '支付类型',
                            align: 'center',
                            field: 'payTypeName',
                            width: 20
                        },
                        {
                            title: '支付金额(元)',
                            align: 'center',
                            field: 'money',
                            width: 30,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '开始时间',
                            align: 'center',
                            field: 'beginTime',
                            width: 30
                        },

                        {
                            title: '过期时间',
                            align: 'center',
                            field: 'endTime',
                            width: 30
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 20
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 30,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='zc.VehiclePacketPeriodOrder:view'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                                if(row.status == 3 || row.status == 4) {
                                    html += ' <a href="javascript:editExtendRent(\'ID\')">延长租期</a>';
                                }
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
                cabinetId: $('#cabinet_id').val(),
                cabinetName: $('#cabinet_name').val(),
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
                href: "${contextPath}/security/zc/vehicle_packet_period_order/view.htm?id=" + id

            });
        }

        function editExtendRent(id) {
            App.dialog.show({
                css: 'width:400px;height:160px;overflow:visible;',
                title: '延长租期',
                href: "${contextPath}/security/zc/vehicle_packet_period_order/edit_extend_rent.htm?id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }

        function refund(id) {
            App.dialog.show({
                css:'width:500px;height:220px;overflow:visible;',
                title:'退款',
                href:"${contextPath}/security/zc/vehicle_packet_period_order/edit_refund.htm?refundFlag=1&id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }

        function repulse_refund(id) {
            App.dialog.show({
                css:'width:500px;height:200px;overflow:visible;',
                title:'取消退款',
                href:"${contextPath}/security/zc/vehicle_packet_period_order/edit_repulse_refund.htm?id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    }
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
                                    <option value="customerFullname">姓名</option>
                                    <option value="customerMobile">手机号</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"  style="width: 170px;"/></td>
                            <td align="right" width="60">状态：</td>
                            <td>
                                <select style="width:90px;" id="status" >
                                    <option value="0">所有</option>
                                    <#list StatusEnum as e>
                                        <option value="${e.getValue()}" <#if e.getValue()==usedStatus>selected = "selected" </#if>>${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td align="right" width="100">换电柜编号：</td>
                            <td><input type="text" class="text" id="cabinet_id" /></td>
                            <td align="right" width="100">换电柜名称：</td>
                            <td><input type="text" class="text" id="cabinet_name" style="width: 188px;" /></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                        <h3>换电套餐记录</h3>
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

