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
                url: "${contextPath}/security/hdg/shop_battery_order/page.htm",
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
                            title: '所属运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 30
                        },
                        {
                            title: '所属门店',
                            align: 'center',
                            field: 'takeShopName',
                            width: 30
                        },
                        {
                            title: '订单编号',
                            align: 'center',
                            field: 'id',
                            width: 30
                        },

                        {
                            title: '电池编号',
                            align: 'center',
                            field: 'batteryId',
                            width: 20
                        },
                        {
                            title: '取电柜/箱号',
                            align: 'center',
                            field: 'takeCabinetName',
                            width: 45,
                            formatter: function (val, row) {
                                var takeCabinetName = (row.takeCabinetName == null || row.takeCabinetName == '') ? '' : row.takeCabinetName;
                                var takeCabinetId = (row.takeCabinetId == null || row.takeCabinetId == '') ? '' : row.takeCabinetId;
                                var takeBoxNum = (row.takeBoxNum == null || row.takeBoxNum == '') ? '' : row.takeBoxNum;
                                return takeCabinetName + '(' + takeCabinetId + ')/' + takeCabinetName + '(' + takeCabinetId + ')/' + takeBoxNum;
                            }
                        },
                        {
                            title: '取电时间',
                            align: 'center',
                            field: 'takeTime',
                            width: 25
                        },
                        {
                            title: '放电柜/箱号',
                            align: 'center',
                            field: 'putCabinetName',
                            width: 45,
                            formatter: function (val, row) {
                                var putCabinetName = (row.putCabinetName == null || row.putCabinetName == '') ? '' : row.putCabinetName;
                                var putCabinetId = (row.putCabinetId == null || row.putCabinetId == '') ? '' : row.putCabinetId;
                                var putBoxNum = (row.putBoxNum == null || row.putBoxNum == '') ? '' : row.putBoxNum;
                                return putCabinetName + '(' + putCabinetId + ')/' + putCabinetName + putBoxNum;
                            }
                        },
                        {
                            title: '放入时间',
                            align: 'center',
                            field: 'putTime',
                            width: 25
                        },
                        {
                            title: '支付类型',
                            align: 'center',
                            field: 'payTypeName',
                            width: 25
                        },
                        {
                            title: '换电价格',
                            align: 'center',
                            field: 'price',
                            width: 25
                        },
//                        {
//                            title: '电量',
//                            align: 'center',
//                            field: 'currentVolume',
//                            width: 20,
//                            formatter: function (val, row) {
//                                return '' + row.initVolume + ' -> ' + row.currentVolume + '';
//                            }
//                        },
                        {
                            title: '客户名称',
                            align: 'center',
                            field: 'customerFullname',
                            width: 20
                        },
                        {
                            title: '客户手机',
                            align: 'center',
                            field: 'customerMobile',
                            width: 30
                        },
//                        {
//                            title: '支付方式',
//                            align: 'center',
//                            field: 'payTypeName',
//                            width: 20
//                        },
//                        {
//                            title: '金额',
//                            align: 'center',
//                            field: 'money',
//                            width: 20,
//                            formatter: function (val) {
//                                return Number(val / 100).toFixed(2) + " 元";
//                            }
//                        },
                        {
                            title: '订单状态',
                            align: 'center',
                            field: 'orderStatusName',
                            width: 20
                        },
                        {
                            title: '退款金额',
                            align: 'center',
                            field: 'refundMoney',
                            width: 20,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) + " 元";
                            }
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 20,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='2_4_1_5'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                                if (row.orderStatus == ${OrderStatus} && row.refundStatus != ${RefundStatus}) {
                                    <@app.has_oper perm_code='2_4_1_6'>
                                        html += ' <a href="javascript:refund(\'ID\')">退款</a>';
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
            var agentId = $('#agent_id').combotree('getValue');
            var takeShopId = $('#shop_id').combotree('getValue');
            var queryBeginTime = $('#begin_time').val();
            var queryEndTime = $('#end_time').val();
            var takeBeginTime = $('#take_begin_time').val();
            var takeEndTime = $('#take_end_time').val();
            var putBeginTime = $('#put_begin_time').val();
            var putEndTime = $('#put_end_time').val();
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();
            var orderStatus = $('#order_status').val();
            var payType = $('#pay_type').val();
            if(queryBeginTime != "" &&  queryEndTime != ""){
                if(queryBeginTime > queryEndTime || queryEndTime < queryBeginTime) {
                    $.messager.alert('提示信息', '结束日期必须大于开始日期', 'info');
                    return;
                }
            }
            var queryParams = {
                agentId: agentId,
                takeShopId: takeShopId,
                orderStatus: orderStatus,
                payType: payType,
                queryBeginTime: queryBeginTime,
                queryEndTime: queryEndTime,
                takeBeginTime: takeBeginTime,
                takeEndTime: takeEndTime,
                putBeginTime: putBeginTime,
                putEndTime: putEndTime
            };
            queryParams[queryName] = queryValue;
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function swich_agent() {
            var agentId = $('#agent_id').combotree('getValue');
            var shopComboTree = $('#shop_id');

            shopComboTree.combotree({
                url: "${contextPath}/security/hdg/shop/tree.htm?agentId=" + agentId + ""
            });
            shopComboTree.combotree('reload');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:786px;height:520px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/battery_order/view.htm?id=" + id

            });
        }
        function advanced_query() {
            App.dialog.show({
                css: 'width:480px;height:380px;overflow:visible;',
                title: '高级搜索',
                href: "${contextPath}/security/hdg/battery_order/advanced_query.htm"

            });
        }

        function refund(id) {
            App.dialog.show({
                css: 'width:500px;height:230px;overflow:visible;',
                title: '退款',
                href: "${contextPath}/security/hdg/battery_order/edit_refund.htm?id=" + id,
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });

        }

        function completeOrder() {
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
                    $.post("${contextPath}/security/hdg/battery_order/complete.htm?id=" + list[0].id, function (json) {
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
                    $.post("${contextPath}/security/hdg/battery_order/exchange_battery.htm?", {
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

        function toBackBatteryOrder() {
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

            $.messager.confirm('提示信息', '确定转为电池退租订单?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/hdg/battery_order/to_back_battery_order.htm?id=" + list[0].id, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功<br/><br/><br/><span style="color: green;">提示 : 请确认电池是否在箱子中、损耗情况是否完好，再进行后续退押金处理</span>', 'info');
                            reload();
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
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
                        <#--<button class="btn btn_blue" onclick="advanced_query()">高级搜索</button>-->
                    </div>
                    <input type="hidden" id="begin_time">
                    <input type="hidden" id="end_time">
                    <input type="hidden" id="take_begin_time">
                    <input type="hidden" id="take_end_time">
                    <input type="hidden" id="put_begin_time">
                    <input type="hidden" id="put_end_time">
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
                            <td align="right" width="115">
                                <select style="width:100px;" id="query_name">
                                    <option value="id">订单编号</option>
                                    <option value="takeCabinetId">取电柜子编号</option>
                                    <option value="putCabinetId">放电柜子编号</option>
                                    <option value="batteryId">电池编号</option>
                                    <option value="customerFullname">客户名称</option>
                                    <option value="customerMobile">客户手机</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"  style="width: 170px;"/></td>
                            <td align="right" width="70">订单状态：</td>
                            <td>
                                <select style="width:80px;" id="order_status">
                                    <option value="">所有</option>
                                    <#list OrderStatusEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right" width="70">支付方式：</td>
                            <td>
                                <select style="width:80px;" id="pay_type">
                                    <option value="">所有</option>
                                    <#list PayTypeEnum as e>
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
                            <@app.has_oper perm_code='2_4_1_2'>
                                <button class="btn btn_green" onclick="completeOrder()">结束订单</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='2_4_1_3'>
                                <button class="btn btn_green" onclick="exchangeBattery()">更换电池</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='2_4_1_4'>
                                <button class="btn btn_green" onclick="toBackBatteryOrder()">转电池退租</button>
                            </@app.has_oper>
                        </div>
                        <h3>换电订单列表</h3>
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

