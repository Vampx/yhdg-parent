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
                url: "${contextPath}/security/hdg/vehicle_order/page.htm",
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
                        { field: 'checkbox', checkbox: true },
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 30
                        },
                        {
                            title: '门店',
                            align: 'center',
                            field: 'shopName',
                            width: 30
                        },
                        {title: '客户名称', align: 'center', field: 'customerFullname', width: 30},
                        {title: '客户电话', align: 'center', field: 'customerMobile', width: 30},
                        {title: '车辆编号', align: 'center', field: 'vehicleId', width: 30},
//                        {title: '车辆型号', align: 'center', field: 'modelCode', width: 20},
                        {title: '车型名称', align: 'center', field: 'modelName', width: 30},
                        {
                            title: '开始时间',
                            align: 'center',
                            field: 'beginTime',
                            width: 40
                        },
                        {
                            title: '结束时间',
                            align: 'center',
                            field: 'endTime',
                            width: 40
                        },
                        {
                            title: '套餐',
                            align: 'center',
                            field: 'settingId',
                            width: 20,
                            formatter: function (val, row) {
                                return row.duration + "/" + row.money/ 100 ;
                            }
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
                            width: 25,
                            formatter: function(val, row) {
                                return val / 100;
                            }
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 30,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='2_4_4_2'>
                                    html += ' <a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
                            }
                        }

                    ]
                ],
                onLoadSuccess:function() {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
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
            var fullname = $('#fullname').val();
            var mobile = $('#mobile').val();
            var agentId = $('#agent_id').combotree('getValue');
            var shopId = $('#shop_id').combotree('getValue');
            var modelName = $('#model_name').val();
            var queryParams = {
                modelName:modelName,
                shopId:shopId,
                agentId:agentId,
                customerMobile:mobile,
                customerFullname:fullname
            };

            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:786px;height:520px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/shop_vehicle_order/view.htm?id=" + id

            });
        }

        function reloadShopTree() {
            var shopId = $('#shop_id').combotree('getValue');
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                shopId: shopId
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
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>

                            <td align="left">运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="false" style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                   reloadTree();
                                }
                            "
                                >
                            </td>
                            <td align="right" width="70">选择门店：</td>
                            <td>
                                <select id="shop_id" class="easyui-combotree" editable="false" style="width: 184px; height: 28px;"
                                        data-options="url:'${contextPath}/security/hdg/shop/tree.htm?dummy=${'所有'?url}',
                                    method:'get',
                                    valueField:'id',
                                    textField:'text',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onClick: function(node) {
                                        reloadShopTree();
                                    }
                                "
                                >
                            </td>
                            <td align="right" width="70">客户名称：</td>
                            <td><input type="text" class="text" id="fullname"  /></td>
                            <td align="right" width="70">手机号码：</td>
                            <td><input type="text" class="text" id="mobile"  /></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                        <h3>租车订单列表</h3>
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

