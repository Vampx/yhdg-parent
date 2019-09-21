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
                url: "${contextPath}/security/hdg/cabinet_box_stats/page.htm",
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
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
                        {
                            title: '换电柜编号',
                            align: 'center',
                            field: 'cabinetId',
                            width: 60
                        },
                        {
                            title: '换电柜名称',
                            align: 'center',
                            field: 'cabinetName',
                            width: 60
                        },
                        {
                            title: '格口数',
                            align: 'center',
                            field: 'boxCount',
                            width: 60,
                            formatter: function (val, row) {
                                var html = '<a href="javascript:view(\'CABINET_ID\', 1)"  style="color: red">' + val + '</a>';
                                return html.replace(/CABINET_ID/g, row.cabinetId);
                            }
                        },
                        {
                            title: '空箱数',
                            align: 'center',
                            field: 'emptyCount',
                            width: 60,
                            formatter: function (val, row) {
                                var html = '<a href="javascript:view(\'CABINET_ID\', 2)"  style="color: red">' + val + '</a>';
                                return html.replace(/CABINET_ID/g, row.cabinetId);
                            }
                        },
                        {
                            title: '开门数',
                            align: 'center',
                            field: 'openCount',
                            width: 60,
                            formatter: function (val, row) {
                                var html = '<a href="javascript:view(\'CABINET_ID\', 3)"  style="color: red">' + val + '</a>';
                                return html.replace(/CABINET_ID/g, row.cabinetId);
                            }
                        },
                        {
                            title: '电池数',
                            align: 'center',
                            field: 'batteryCount',
                            width: 60,
                            formatter: function (val, row) {
                                var html = '<a href="javascript:view(\'CABINET_ID\', 4)"  style="color: red">' + val + '</a>';
                                return html.replace(/CABINET_ID/g, row.cabinetId);
                            }
                        },
                        {
                            title: '充电中数',
                            align: 'center',
                            field: 'chargingCount',
                            width: 40,
                            formatter: function (val, row) {
                                var html = '<a href="javascript:view(\'CABINET_ID\', 5)"  style="color: red">' + val + '</a>';
                                return html.replace(/CABINET_ID/g, row.cabinetId);
                            }
                        },
                        {
                            title: '待充数',
                            align: 'center',
                            field: 'waitChargeCount',
                            width: 40,
                            formatter: function (val, row) {
                                var html = '<a href="javascript:view(\'CABINET_ID\', 6)"  style="color: red">' + val + '</a>';
                                return html.replace(/CABINET_ID/g, row.cabinetId);
                            }
                        },
                        {
                            title: '满电数',
                            align: 'center',
                            field: 'completeChargeCount',
                            width: 40,
                            formatter: function (val, row) {
                                var html = '<a href="javascript:view(\'CABINET_ID\', 7)"  style="color: red">' + val + '</a>';
                                return html.replace(/CABINET_ID/g, row.cabinetId);
                            }
                        },
                        {
                            title: '未付款数',
                            align: 'center',
                            field: 'notPayCount',
                            width: 40,
                            formatter: function (val, row) {
                                var html = '<a href="javascript:view(\'CABINET_ID\', 8)"  style="color: red">' + val + '</a>';
                                return html.replace(/CABINET_ID/g, row.cabinetId);
                            }
                        },
                        {
                            title: '未取出数',
                            align: 'center',
                            field: 'notTakeCount',
                            width: 40,
                            formatter: function (val, row) {
                                var html = '<a href="javascript:view(\'CABINET_ID\', 9)"  style="color: red">' + val + '</a>';
                                return html.replace(/CABINET_ID/g, row.cabinetId);
                            }
                        }
                    ]
                ],
                onLoadSuccess: function (request) {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                }
            });
            battery_state_count();
        });

        function battery_state_count() {
            $.post('${contextPath}/security/hdg/cabinet_box_stats/battery_state_count.htm', {
                agentId: $('#agent_id').combotree('getValue'),
                type: $('#type').val()
            }, function (json) {
                if (json.success) {
                    $("#batteryCount").html(json.data.batteryCount)
                    $("#chargeCount").html(json.data.chargeCount)
                    $("#completeCount").html(json.data.completeCount)
                }
            }, 'json');
        }

        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var agentId = $('#agent_id').combotree('getValue');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId,
                id: $('#id').val()
            };
            battery_state_count();
            datagrid.datagrid('load');
        }

        function view(cabinetId, viewFlag) {
            App.dialog.show({
                css: 'width:1000px;height:515px;overflow:visible;',
                title: '格子列表',
                href: "${contextPath}/security/hdg/cabinet_box_stats/view.htm?cabinetId=" + cabinetId + "&viewFlag=" + viewFlag + ""
            });
        }

        function exportExcel() {
            var agentId = $('#agent_id').combotree('getValue');;
            var id = $('#id').val();
            window.location.href = "${contextPath}/security/hdg/cabinet_box_stats/export_excel.htm?agentId="+agentId+"&id="+id+"";
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
                                <input id="agent_id" class="easyui-combotree" editable="false"
                                       style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200'
                            "
                                >
                            </td>
                            <td align="right" style="width: 100px;">换电柜编号：</td>
                            <td><input type="text" class="text" id="id"  /></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <button class="btn btn_green" onclick="exportExcel()">导出</button>
                        </div>
                        <h3>
                            柜子总电池数： <a id="batteryCount"></a>个 &nbsp;&nbsp;柜子充满数： <a id="chargeCount"></a>个 &nbsp;&nbsp;柜子充电中数： <a id="completeCount"></a>个
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