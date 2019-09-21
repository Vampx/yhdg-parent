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
                url: "${contextPath}/security/zc/vehicle_battery_order/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                                return takeCabinetName + '(' + takeCabinetId + ')/'   + takeBoxNum;
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
                                return putCabinetName + '(' + putCabinetId + ')/' + putBoxNum;
                            }
                        },
                        {
                            title: '放电时间',
                            align: 'center',
                            field: 'putTime',
                            width: 25
                        },
                        {
                            title: '电量',
                            align: 'center',
                            field: 'currentVolume',
                            width: 20,
                            formatter: function (val, row) {
                                return '' + row.initVolume + ' -> ' + row.currentVolume + '';
                            }
                        },
                        {
                            title: '骑行里程',
                            align: 'center',
                            field: 'currentDistance',
                            width: 25,
                            formatter: function (val, row) {
                                if(val>=1000){
                                    var html=  '<a href="javascript:view_map(\'ID\')">'+val / 1000 + "km"+'</a>';
                                    return html.replace(/ID/g, row.id);
                                }else {
                                    var html=  '<a href="javascript:view_map(\'ID\')">'+val + "m"+'</a>';
                                    return html.replace(/ID/g, row.id);
                                }
                            }
                        },
                        {
                            title: '客户名称',
                            align: 'center',
                            field: 'customerFullname',
                            width: 20
                        },
                        {
                            title: '支付方式',
                            align: 'center',
                            field: 'payTypeName',
                            width: 20
                        },
                        {
                            title: '金额',
                            align: 'center',
                            field: 'money',
                            width: 20,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) + " 元";
                            }
                        },
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
                                <@app.has_oper perm_code='zc.VehicleBatteryOrder:view'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                onLoadSuccess: function () {
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

        function view(id) {
            App.dialog.show({
                css: 'width:786px;height:570px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/zc/vehicle_battery_order/view.htm?id=" + id

            });
        }

        function view_map(id) {
            $.post("${contextPath}/security/zc/vehicle_battery_order_battery_report_log/find_all_map_count.htm", {orderId: id}, function (json) {
                if (!json.success) {
                    $.messager.alert('提示信息', '该时段电池未上报信息', 'info');
                    return
                }
                App.dialog.show({
                    css: 'width:1286px;height:700px;overflow:visible;',
                    title: '运行轨迹',
                    href: "${contextPath}/security/hdg/battery_ride_order/view_map.htm?id=" + id
                });
            }, 'json');
        }

        function advanced_query() {
            App.dialog.show({
                css: 'width:480px;height:380px;overflow:visible;',
                title: '高级搜索',
                href: "${contextPath}/security/zc/vehicle_battery_order/advanced_query.htm"

            });
        }

        function refund(id) {
            App.dialog.show({
                css: 'width:500px;height:230px;overflow:visible;',
                title: '退款',
                href: "${contextPath}/security/zc/vehicle_battery_order/edit_refund.htm?id=" + id,
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });

        }

        function oneclickSelfhelp() {
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

            $.messager.confirm('提示信息', '确认启用自救?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/hdg/battery/rescue.htm?id=" + list[0].id, function (json) {
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
                    $.post("${contextPath}/security/zc/vehicle_battery_order/complete.htm?id=" + list[0].id, function (json) {
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
                    $.post("${contextPath}/security/zc/vehicle_battery_order/exchange_battery.htm?", {
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
                    $.post("${contextPath}/security/zc/vehicle_battery_order/to_back_battery_order.htm?id=" + list[0].id, function (json) {
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
                        <button class="btn btn_blue" onclick="advanced_query()">高级搜索</button>
                    </div>
                    <input type="hidden" id="begin_time">
                    <input type="hidden" id="end_time">
                    <input type="hidden" id="take_begin_time">
                    <input type="hidden" id="take_end_time">
                    <input type="hidden" id="put_begin_time">
                    <input type="hidden" id="put_end_time">
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right" width="110">
                                <select style="width:100px;" id="query_name">
                                    <option value="id">订单编号</option>
                                    <option value="takeCabinetId">取电柜子编号</option>
                                    <option value="putCabinetId">放电柜子编号</option>
                                    <option value="batteryId">电池编号</option>
                                    <option value="customerFullname">客户名称</option>
                                    <option value="customerMobile">客户手机号</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"  style="width: 150px;"/></td>
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
                            <#--<@app.has_oper perm_code='hdg.BatteryOrder:SelfhelpOrder'>
                                <button class="btn btn_red" onclick="oneclickSelfhelp()">一键自救</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='hdg.BatteryOrder:completeOrder'>
                                <button class="btn btn_green" onclick="completeOrder()">结束订单</button>
                            </@app.has_oper>
                            -->
                            <@app.has_oper perm_code='hdg.BatteryOrder:exchangeBattery'>
                                <button class="btn btn_green" onclick="exchangeBattery()">更换电池</button>
                            </@app.has_oper>
                            <#--
                        <@app.has_oper perm_code='hdg.BatteryOrder:toBackBatteryOrder'>
                            <button class="btn btn_green" onclick="toBackBatteryOrder()">转电池退租</button>
                        </@app.has_oper>-->
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

