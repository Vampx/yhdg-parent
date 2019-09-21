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
                url: "${contextPath}/security/hdg/packet_period_order_soon_expire_stats/page.htm",
                fitColumns: true,
                pageSize: 10,
                pageList: [10, 50, 100],
                idField: 'cabinetId',
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
                            title: '电池编号',
                            align: 'center',
                            field: 'batteryId',
                            width: 20
                        },
                            /*{
                                title: '电池类型',
                                align: 'center',
                                field: 'batteryTypeName',
                                width: 20
                            },*/
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
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='hdg.PacketPeriodOrderSoonExpireStats:view'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                onLoadSuccess: function (request) {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                },
                queryParams:{
                    expireDate:3
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
            datagrid.datagrid('options').queryParams = {
                agentId: agentId,
                cabinetId: $('#cabinetId').val(),
                expireDate:$('#expireDate').combotree('getValue'),
                isBattery:$('#isBattery').combotree('getValue'),
                isDeposit:$('#isDeposit').combotree('getValue')
            };
            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:1000px;height:510px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/packet_period_order/view.htm?id=" + id

            });
        }

        function exportExcel() {
            var agentId = $('#agent_id').combotree('getValue');
            var cabinetId = $('#cabinetId').val();
            var expireDate = $('#expireDate').combotree('getValue');
            var isBattery = $('#isBattery').combotree('getValue');
            var isDeposit = $('#isDeposit').combotree('getValue');
            window.location.href = "${contextPath}/security/hdg/packet_period_order_soon_expire_stats/export_excel.htm?agentId="+agentId+"&cabinetId="+cabinetId+"&expireDate="+expireDate+"&isBattery="+isBattery+"&isDeposit="+isDeposit;
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
                                       style="width: 200px;height: 28px;"
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
                            <td align="right" style="width: 100px;">换电柜编号：</td>
                            <td><input type="text" class="text" id="cabinetId"  /></td>
                            <td align="right" style="width: 100px;">到期时间：</td>
                            <td>
                                <select id="expireDate" class="easyui-combobox" name="expireDate" editable="false" style="width: 100px;height: 28px;">
                                    <#list expireDate as e>
                                        <option value='${(e.getValue())!}'>${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right" style="width: 100px;">是否有电池：</td>
                            <td>
                                <select id="isBattery" class="easyui-combobox" name="isBattery" editable="false" style="width: 50px;height: 28px;">
                                    <option value="11">所有</option>
                                    <#list isBattery as e>
                                        <option value='${(e.getValue())!}'>${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right" style="width: 100px;">是否有押金：</td>
                            <td>
                                <select id="isDeposit" class="easyui-combobox" name="isBattery" editable="false" style="width: 50px;height: 28px;">
                                    <option value="11">所有</option>
                                    <#list isDeposit as e>
                                        <option value='${(e.getValue())!}'>${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='hdg.PacketPeriodOrderSoonExpireStats:exportExcel'>
                                <button class="btn btn_green" onclick="exportExcel()">导出</button>
                            </@app.has_oper>
                        </div>
                        <h3>
                            要到期包时总统计
                        </h3>
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