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
                url: "${contextPath}/security/zd/battery/battery_control_page.htm",
                fitColumns: true,
                pageSize: 10,
                pageList: [10, 50, 100],
                idField: 'id',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                columns: [
                    [
                        {title: '电池编号', align: 'center', field: 'id', width: 50},
                        {title: 'sim卡', align: 'center', field: 'simMemo', width: 50},
                        {title: '类型', align: 'center', field: 'batteryType', width: 50},
                        {title: '使用状态', align: 'center', field: 'statusName', width: 50},
                        {title: '充电状态', align: 'center', field: 'chargeStatusName', width: 50},
                        {title: '温度', align: 'center', field: 'temp', width: 40},
                        {title: '当前电量', align: 'center', field: 'volume', width: 50},
                        {
                            title: '当前/总容量',
                            align: 'center',
                            field: 'putCabinetName',
                            width: 50,
                            formatter: function(val, row) {
                                var currentCapacity = (row.currentCapacity == null || row.currentCapacity == '') ? 0 : row.currentCapacity;

                                var totalCapacity = (row.totalCapacity == null || row.totalCapacity == '') ? 0 : row.totalCapacity;
                                return ''+ currentCapacity +'/' + totalCapacity +'';
                            }
                        },
                        {title: '当前信号', align: 'center', field: 'currentSignal', width: 50},
                        {title: '上报时间', align: 'center', field: 'reportTime', width: 80},
                        {
                            title: '行驶总里程(Km)',
                            align: 'center',
                            field: 'totalDistance',
                            width: 60,
                            formatter: function(val) {
                                return Number(val / 1000).toFixed(2);
                            }
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='2_8_3_2'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='2_8_3_3'>
                                    html += ' <a href="javascript:viewBatteryTrack(\'ID\')">轨迹</a>';
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

        function query() {
            var datagrid = $('#page_table');
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();

            var queryParams = {
                agentId: $('#agent_id').combotree('getValue'),
                status: $('#status').combobox('getValue'),
                chargeStatus: $('#charge_status').combobox('getValue')
            };

            queryParams[queryName] = queryValue;
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:780px;height:520px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/zd/battery/view_control.htm?id=" + id
            });
        }

        function viewBatteryTrack(id) {
            App.dialog.show({
                options:'maximized:true',
                title: '电池监控',
                href: "${contextPath}/security/zd/battery/view_battery_track.htm?id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    }
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
                            <td align="right">运营商：</td>
                            <td >
                                <input name="agentId" id="agent_id" class="easyui-combotree" editable="false" style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
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
                                    <option value="id">电池编号</option>
                                    <option value="simMemo">SIM卡</option>
                                    <option value="code">电池编码</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"/></td>
                            <td align="right">&nbsp;&nbsp;状态：</td>
                            <td>
                                <select id="status" class="easyui-combobox" style="height: 28px;width:90px;">
                                    <option value="">所有</option>
                                    <#if statusEnum??>
                                        <#list statusEnum as se>
                                            <option value="${(se.getValue())!''}">${(se.getName())!''}</option>
                                        </#list>
                                    </#if>
                                </select>
                            </td>
                            <td align="right">&nbsp;&nbsp;充电状态：</td>
                            <td>
                                <select id="charge_status" class="easyui-combobox" style="height: 28px;width:90px;">
                                    <option value="">所有</option>
                                    <#if chargeStatusEnum??>
                                        <#list chargeStatusEnum as cse>
                                            <option value="${(cse.getValue())!''}">${(cse.getName())!''}</option>
                                        </#list>
                                    </#if>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" >
                    <div class="toolbar clearfix">
                    <h3>电池监控</h3>
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