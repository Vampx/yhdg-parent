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
                url: "${contextPath}/security/hdg/battery_order_history/page.htm",
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
                                return takeCabinetName + '(' + takeCabinetId + ')/' + takeCabinetName + takeBoxNum;
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
                                var tree = $('#suffix_tree');
                                var suffixId = tree.tree('getSelected');
                                if (suffixId) {
                                    if (suffixId.id.length > 4) {
                                        suffixId = suffixId.id || '';
                                    } else {
                                        suffixId = '';
                                    }
                                } else {
                                    suffixId = '';
                                }
                                var html = '';
                                <@app.has_oper perm_code='2_4_5_2'>
                                    html += '<a href="javascript:view(\'ID\',\'suffix\')">查看</a>';
                                </@app.has_oper>
                                html = html.replace(/suffix/g, suffixId);
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
            var queryBeginTime = $('#begin_time').val();
            var queryEndTime = $('#end_time').val();
            var takeBeginTime = $('#take_begin_time').val();
            var takeEndTime = $('#take_end_time').val();
            var putBeginTime = $('#put_begin_time').val();
            var putEndTime = $('#put_end_time').val();
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();
            var payType = $('#pay_type').val();
            var agentId = $('#agent_id').combotree('getValue');
            if (queryBeginTime != "" && queryEndTime != "") {
                if (queryBeginTime > queryEndTime || queryEndTime < queryBeginTime) {
                    $.messager.alert('提示信息', '结束日期必须大于开始日期', 'info');
                    return;
                }
            }
            var tree = $('#suffix_tree');
            var suffixId = tree.tree('getSelected');
            if (suffixId) {
                if (suffixId.id.length > 4) {
                    suffixId = suffixId.id || '';
                } else {
                    suffixId = '';
                }
            } else {
                suffixId = '';
            }
            var queryParams = {
                payType: payType,
                agentId: agentId,
                queryBeginTime: queryBeginTime,
                queryEndTime: queryEndTime,
                takeBeginTime: takeBeginTime,
                takeEndTime: takeEndTime,
                suffix: suffixId,
                putBeginTime: putBeginTime,
                putEndTime: putEndTime
            };
            queryParams[queryName] = queryValue;
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function view(id,suffix) {
            App.dialog.show({
                css: 'width:786px;height:520px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/battery_order_history/view.htm?id=" + id + "&suffix=" + suffix
            });
        }
        function advanced_query() {
            App.dialog.show({
                css: 'width:480px;height:340px;overflow:visible;',
                title: '高级搜索',
                href: "${contextPath}/security/hdg/battery_order_history/advanced_query.htm"

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
                <div class="panel ztree_wrap" style="width: 110px">
                    <div class="ztree" style="width: 110px">
                        <div class="ztree_head">
                            <h3>月份查询</h3>
                        </div>
                        <div class="ztree_body easyui-tree" id="suffix_tree"
                             url="${contextPath}/security/hdg/battery_order_history/tree.htm" lines="true"
                             data-options="onBeforeSelect: App.tree.toggleSelect,
                                onClick: function(node) {
                                    query();
                                }">
                        </div>
                    </div>
                </div>
                <div class="panel search" style="margin-left:160px;">
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
                            <td align="right">运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="false"
                                       style="width: 180px;height: 28px;"
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
                            <td><input type="text" class="text" id="query_value" style="width: 170px;"/></td>
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
                <div class="panel grid_wrap" style="margin-left:160px;">
                    <div class="toolbar clearfix">
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

