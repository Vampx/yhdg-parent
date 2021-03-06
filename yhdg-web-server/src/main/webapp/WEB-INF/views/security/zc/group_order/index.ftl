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
                url: "${contextPath}/security/zc/group_order/page.htm",
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
                            width: 60
                        },
                        {
                            title: '姓名',
                            align: 'center',
                            field: 'customerFullname',
                            width: 60
                        },
                        {
                            title: '手机号',
                            align: 'center',
                            field: 'customerMobile',
                            width: 60
                        },
                        {
                            title: '车辆配置',
                            align: 'center',
                            field: 'vehicleName',
                            width: 60
                        },
                        {
                            title: '车辆型号',
                            align: 'center',
                            field: 'modelName',
                            width: 60
                        },
                        {
                            title: '支付金额(元)',
                            align: 'center',
                            field: 'money',
                            width: 60,
                            formatter:function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '支付时间',
                            align: 'center',
                            field: 'payTime',
                            width: 60,
                            formatter:function (val,row) {
                                return val;
                            }
                        },
                        {
                            title: '支付方式',
                            align: 'center',
                            field: 'payTypeName',
                            width: 60
                        },
                        {
                            title: '支付状态',
                            align: 'center',
                            field: 'statusName',
                            width: 60
                        },
                        {
                            title: '门店编号',
                            align: 'center',
                            field: 'shopId',
                            width: 60
                        },
                        {
                            title: '门店名称',
                            align: 'center',
                            field: 'shopName',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 80,
                            formatter: function (val, row) {
                                var html = '';
                                if(row.status == 3) {
                                    <@app.has_oper perm_code='zc.GroupOrder:audit'>
                                        html += ' <a href="javascript:audit(\'ID\')"> 待审核</a>';
                                    </@app.has_oper>
                                }else if(row.status == 2) {
                                    <@app.has_oper perm_code='zc.GroupOrder:refund'>
                                        html += ' <a href="javascript:refund(\'ID\')"> 退款</a>';
                                    </@app.has_oper>
                                }
                                <@app.has_oper perm_code='zc.GroupOrder:view'>
                                    html += '<a href="javascript:view(\'ID\')"> 查看</a>';
                                </@app.has_oper>
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
        });


        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            debugger
            var datagrid = $('#page_table');
            var shopName  =$('#shop_name').val();
            var modelVehicle  =$('#model_vehicle').val();
            var queryName  =$('#query_name').val();
            var queryParams = {
                agentId: $('#agent_id').combotree('getValue'),
                status : $('#status').val()
            }
            queryParams[shopName]=$('#shop').val();
            queryParams[modelVehicle]=$('#modelVehicle_Val').val();
            queryParams[queryName]=$('#customer').val();
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }


        function audit(id) {
            App.dialog.show({
                css: 'width:800px;height:520px;',
                title: '审核',
                href: "${contextPath}/security/zc/group_order/audit.htm?id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }

        function refund(id) {
            App.dialog.show({
                css: 'width:800px;height:520px;',
                title: '退款',
                href: "${contextPath}/security/zc/group_order/refund.htm?id=" + id,
                event: {
                    onClose: function () {
                        reload();
                    },
                    onLoad: function () {
                    }
                }
            });
        }

        function view(id) {
            App.dialog.show({
                css: 'width:700px;height:480px;',
                title: '订单查看',
                href: "${contextPath}/security/zc/group_order/view.htm?id=" + id
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
                    <table cellpadding="0" cellspacing="0" border="0" style="border-collapse:separate; border-spacing: 0px 3px;">
                        <tr>

                            <td align="right" >运营商：</td>
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
                                }
                            "
                                >
                            </td>
                            <td align="right">&nbsp;&nbsp;支付状态：</td>
                            <td>
                                <select style="width: 200px;" id="status">
                                    <option value="">所有</option>
                                    <#list statusList! as status>
                                        <option value="${status.getValue()}">${status.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td align="right" width="70">
                                <select style="width:70px;" id="query_name">
                                    <option value="customerFullname">姓名</option>
                                    <option value="customerMobile">手机号</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="customer"  style="width: 188px;"/></td>
                            <td align="right" width="70">
                                <select style="width:90px;" id="shop_name">
                                    <option value="shopId">门店编号</option>
                                    <option value="shopName">门店名称</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="shop"  style="width: 188px;"/></td>
                            <td align="right" width="70">
                                <select style="width:90px;" id="model_vehicle">
                                    <option value="vehicleName">车辆名称</option>
                                    <option value="modelId">车辆型号</option>
                                </select>
                            </td>
                            <td><input type="text" style="width: 200px;" class="text"
                                       id="modelVehicle_Val"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-top: 30px;">
                    <div class="toolbar clearfix">
                       <#-- <div class="float_right">
                        <@app.has_oper perm_code='zc.GroupOrder:add'>
                            <button class="btn btn_green" onclick="add()">新建</button>
                        </@app.has_oper>
                        </div>-->
                        <h3>组合订单</h3>
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