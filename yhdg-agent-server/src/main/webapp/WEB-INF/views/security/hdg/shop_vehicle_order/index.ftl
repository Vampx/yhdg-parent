<@app.html>
    <@app.head>
    <script>
        $.extend($.fn.validatebox.defaults.rules, {
            mobile: {
                validator: function (value) {
                    if (value != "") {
                        return /^1\d{10}$/.test(value);
                    }
                },
                message: '请正确输入手机号码'
            }
        });

        $(function() {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/hdg/shop_vehicle_order/page.htm",
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
                            title: '所属运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 30
                        },
                        {
                            title: '所属门店',
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
                                <@app.has_oper perm_code='4_1_5_3'>
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

        function swich_agent() {
            var agentId = $('#agent_id').combotree('getValue');
            var shopComboTree = $('#shop_id');

            shopComboTree.combotree({
                url: "${contextPath}/security/hdg/shop/tree.htm?agentId=" + agentId + ""
            });
            shopComboTree.combotree('reload');
        }

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

        function add() {
            App.dialog.show({
                css: 'width:800px;height:420px;overflow:visible;',
                title: '绑定车辆/电池',
                href: "${contextPath}/security/hdg/vehicle_order/add.htm",
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }

        function query() {
            var datagrid = $('#page_table');
            var agentId = $('#agent_id').combotree('getValue');
            var shopId = $('#shop_id').combotree('getValue');
            var fullname = $('#fullname').val();
            var mobile = $('#mobile').val();
            var agentId = $('#agent_id').combotree('getValue');
            var queryParams = {
                agentId: agentId,
                shopId: shopId,
                customerMobile:mobile,
                customerFullname:fullname
            };

            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:786px;height:510px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/shop_vehicle_order/view.htm?id=" + id

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
                                <input name="agentId" id="agent_id" class="easyui-combotree" editable="false"
                                       style="width: 160px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                            method:'get',
                                            valueField:'id',
                                            textField:'text',
                                            editable:false,
                                            multiple:false,
                                            panelHeight:'200',
                                            onClick: function(node) {
                                               swich_agent();
                                            }
                                        "
                                >
                            </td>
                            <td width="70"  align="right">所属门店：</td>
                            <td>
                                <input name="shopId" id="shop_id" class="easyui-combotree" editable="false"
                                       style="width: 184px;height: 28px;"
                                       data-options="url:'${contextPath}/security/hdg/shop/tree.htm?dummy=${'所有'?url}',
                                            method:'get',
                                            valueField:'id',
                                            textField:'text',
                                            editable:false,
                                            multiple:false,
                                            panelHeight:'200',
                                            onClick: function(node) {
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
                            <@app.has_oper perm_code='4_1_5_2'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>门店租车订单列表</h3>
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

