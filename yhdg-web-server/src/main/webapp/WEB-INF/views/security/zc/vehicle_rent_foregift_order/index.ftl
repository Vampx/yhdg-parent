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
                url: "${contextPath}/security/zc/vehicle_rent_foregift_order/page.htm",
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
                            title: '手机号',
                            align: 'center',
                            field: 'customerMobile',
                            width: 30
                        },
                        {
                            title: '姓名',
                            align: 'center',
                            field: 'customerFullname',
                            width: 30
                        },
                        {
                            title: '金额(元)',
                            align: 'center',
                            field: 'money',
                            width: 20,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '退款金额(元)',
                            align: 'center',
                            field: 'refundMoney',
                            width: 25,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '支付时间',
                            align: 'center',
                            field: 'payTime',
                            width: 40
                        },
                        {
                            title: '支付方式',
                            align: 'center',
                            field: 'payTypeName',
                            width: 20
                        },
                        {
                            title: '申请退款时间',
                            align: 'center',
                            field: 'applyRefundTime',
                            width: 40
                        },
                        {
                            title: '退款时间',
                            align: 'center',
                            field: 'refundTime',
                            width: 40
                        },
                        {
                            title: '退款人',
                            align: 'center',
                            field: 'refundOperator',
                            width: 25
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 25
                        },
                        {
                            title: '门店编号',
                            align: 'center',
                            field: 'shopId',
                            width: 30
                        },
                        {
                            title: '门店名称',
                            align: 'center',
                            field: 'shopName',
                            width: 30
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 20,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='zc.VehicleRentForegiftOrder:view'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
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

        function query() {
            var datagrid = $('#page_table');
            var queryBeginTime = $('#begin_time').datetimebox('getValue');
            var queryEndTime = $('#end_time').datetimebox('getValue');
            var partnerId = $('#partner_id').combobox('getValue');
            var agentId = $('#agent_id').combotree('getValue');
            var cabinetId = $('#cabinet_id').val();
            var cabinetName = $('#cabinet_name').val();
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();


            if(queryBeginTime != "" &&  queryEndTime != ""){
                if(queryBeginTime > queryEndTime || queryEndTime < queryBeginTime) {
                    $.messager.alert('提示信息', '结束日期必须大于开始日期', 'info');
                    return;
                }
            }else{
                queryEndTime = "";
                queryBeginTime = "";
            }

            var queryParams = {
                queryName: queryName,
                status: $('#status').val(),
                queryBeginTime: queryBeginTime,
                queryEndTime: queryEndTime,
                partnerId: partnerId,
                agentId: agentId
            };

            queryParams[queryName] = queryValue;
            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:1000px;height:510px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/zc/vehicle_rent_foregift_order/view.htm?id=" + id

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
                                <input name="partnerId" id="partner_id" style="height: 28px;width: 130px;" class="easyui-combobox"  editable="false"
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
                            <td align="right"  style="width:60px;">运营商：</td>
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
                                }
                            "
                                >
                            <td align="right"  width="115">&nbsp;&nbsp;开始/结束日期：</td>
                            <td >
                                <input id="begin_time" class="easyui-datetimebox" name="queryBeginTime" type="text" style="width:125px;;height:27px;" >
                                -
                                <input id="end_time" class="easyui-datetimebox" name="queryEndTime" type="text" style="width:125px;;height:27px;" >
                            </td>
                            <td align="right" width="115">
                                <select style="width:100px;" id="query_name">
                                    <option value="customerMobile">手机号</option>
                                    <option value="customerFullname">姓名</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"  style="width: 120px;"/></td>
                        </tr>
                        <tr>
                            <td align="right" width="60">&nbsp;&nbsp;状态：</td>
                            <td>
                                <select style="width:125px;" id="status">
                                    <option value="0">所有</option>
                                    <#list statusEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                        <h3>用户押金记录</h3>
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

