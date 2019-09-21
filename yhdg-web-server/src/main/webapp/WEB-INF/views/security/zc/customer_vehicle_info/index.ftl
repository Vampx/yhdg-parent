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
                url: "${contextPath}/security/zc/customer_vehicle_info/page.htm",
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
                            width: 100
                        },
                        {
                            title: '结算的门店ID',
                            align: 'center',
                            field: 'balanceShopId',
                            width: 100
                        },
                        {
                            title: '门店',
                            align: 'center',
                            field: 'shopName',
                            width: 120
                        },
                        {
                            title: '车辆型号',
                            align: 'center',
                            field: 'modelName',
                            width: 80
                        },
                        {
                            title: '车架号',
                            align: 'center',
                            field: 'vinNo',
                            width: 80
                        },
                        {
                            title: '电池',
                            align: 'center',
                            field: 'batteryId',
                            width: 80
                        },
                        {
                            title: '电池型号',
                            align: 'center',
                            field: 'batteryTypeName',
                            width: 80
                        },
                        {
                            title: '姓名',
                            align: 'center',
                            field: 'fullname',
                            width: 80
                        },
                        {
                            title: '手机',
                            align: 'center',
                            field: 'mobile',
                            width: 100
                        },
                        {
                            title: '类型',
                            align: 'center',
                            field: 'categoryName',
                            width: 80
                        },
                        {
                            title: '车辆押金',
                            align: 'center',
                            field: 'foregift',
                            width: 80,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) + " 元";
                            }
                        },
                        {
                            title: '押金订单ID',
                            align: 'center',
                            field: 'foregiftOrderId',
                            width: 80
                        },
                        {
                            title: '押金套餐ID',
                            align: 'center',
                            field: 'rentPriceId',
                            width: 80
                        },
                        {
                            title: '租车订单ID',
                            align: 'center',
                            field: 'vehicleOrderId',
                            width: 80
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 100
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
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();

            var queryParams = {
                agentId: $('#agent_id').combotree('getValue'),
                modelId: $('#model_id').combobox('getValue'),
            };
            queryParams[queryName] = queryValue;
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function reloadTree() {
            var agentId = $('#agent_id').combotree('getValue');
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId
            };
            datagrid.datagrid('reload');
        }

        function swich_vehicle_model() {
            var agentId = $('#agent_id').combotree('getValue');
            var modelId = $('#model_id');
            modelId.combobox({
                url: "${contextPath}/security/zc/vehicle_model/list.htm?agentId=" + agentId + ""
            });
            modelId.combobox('reload');
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
                        <tr> <td align="right">运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="true"
                                       style="width:142px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:true,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                    swich_vehicle_model();
                                }
                            "
                                >
                            </td>
                            <td align="right">车辆型号：</td>
                            <td>
                                <input id="model_id" style="height: 28px;width: 183px;" class="easyui-combobox"  editable="false"
                                       data-options="url:'${contextPath}/security/zc/vehicle_model/list.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'modelName',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onSelect: function(node) {
                                    }"/>
                            </td>
                            <td align="right" width="85">
                            <select style="width:80px;" id="query_name">
                                <option value="fullname">姓名</option>
                                <option value="mobile">手机</option>
                                <option value="vehicleName">车辆名称</option>
                                <option value="vinNo">车架号</option>
                            </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-top: 50px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                        <h3>
                            <div style="display: inline;">门店用户信息列表
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