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
                url: "${contextPath}/security/hdg/bespeak_order/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            width: 50
                        },
                        {
                            title: '预约换电柜/箱号/电池',
                            align: 'center',
                            field: 'bespeakCabinetName',
                            width: 90,
                            formatter: function (val, row) {
                                var bespeakCabinetName = (row.bespeakCabinetName == null || row.bespeakCabinetName == '') ? '' : row.bespeakCabinetName;
                                var bespeakCabinetId = (row.bespeakCabinetId == null || row.bespeakCabinetId == '') ? '' : row.bespeakCabinetId;
                                var bespeakBoxNum = (row.bespeakBoxNum == null || row.bespeakBoxNum == '') ? '' : row.bespeakBoxNum;
                                var bespeakBatteryId = (row.bespeakBatteryId == null || row.bespeakBatteryId == '') ? '' : row.bespeakBatteryId;

                                return bespeakCabinetName + '(' + bespeakCabinetId + ')/'   + bespeakBoxNum + '/' + bespeakBatteryId;
                            }
                        },
                        {
                            title: '取出换电柜/箱号/电池',
                            align: 'center',
                            field: 'takeCabinetName',
                            width: 90,
                            formatter: function (val, row) {
                                var takeCabinetName = (row.takeCabinetName == null || row.takeCabinetName == '') ? '' : row.takeCabinetName;
                                var takeCabinetId = (row.takeCabinetId == null || row.takeCabinetId == '') ? '' : row.takeCabinetId;
                                var takeBoxNum = (row.takeBoxNum == null || row.takeBoxNum == '') ? '' : row.takeBoxNum;
                                var takeBatteryId = (row.takeBatteryId == null || row.takeBatteryId == '') ? '' : row.takeBatteryId;

                                return takeCabinetName + '(' + takeCabinetId + ')/'   + takeBoxNum + '/' + takeBatteryId;
                            }
                        },
                        {
                            title: '客户名称',
                            align: 'center',
                            field: 'customerFullname',
                            width: 40
                        },
                        {
                            title: '手机号',
                            align: 'center',
                            field: 'customerMobile',
                            width: 40
                        },
                        {
                            title: '订单状态',
                            align: 'center',
                            field: 'statusName',
                            width: 40
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 40,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='hdg.BackBatteryOrder:view'>
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
            var status = $('#status').val();
            var id = $('#id').val();
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();
            var bespeakCabinetId = $('#bespeak_cabinet_id').val();
            var queryParams = {
                status: status,
                id: id,
                bespeakCabinetId: bespeakCabinetId
            };
            queryParams[queryName] = queryValue;
            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:786px;height:520px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/bespeak_order/view.htm?id=" + id

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
                    $.post("${contextPath}/security/hdg/bespeak_order/complete.htm?id=" + list[0].id, function (json) {
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
                            <td align="right">订单编号：</td>
                            <td><input type="text" class="text" id="id"/>&nbsp;&nbsp;</td>
                            <td align="right" width="80">预约柜编号：</td>
                            <td><input type="text" class="text" id="bespeak_cabinet_id"/></td>
                            <td align="right" width="50">状态：</td>
                            <td>
                                <select style="width:85px;" id="status">
                                    <option value="">所有</option>
                                    <#list StatusEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>&nbsp;&nbsp;
                            </td>
                            <td align="right" width="90">
                                <select style="width:100px;" id="query_name">
                                    <option value="customerFullname">姓名</option>
                                    <option value="customerMobile">手机号</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"  style="width: 150px;"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='hdg.BackBatteryOrder:completeOrder'>
                                <button class="btn btn_green" onclick="completeOrder()">结束订单</button>
                            </@app.has_oper>
                        </div>
                        <h3>预约订单</h3>
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

