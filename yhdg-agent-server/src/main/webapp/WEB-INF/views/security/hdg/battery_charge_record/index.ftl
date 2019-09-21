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
                url: "${contextPath}/security/hdg/battery_charge_record/page.htm",
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
                            width: 30
                        },
                        {
                            title: '电池编号',
                            align: 'center',
                            field: 'batteryId',
                            width: 30
                        },
                        {
                            title: '充电类型',
                            align: 'center',
                            field: 'typeName',
                            width: 30

                        },
                        {
                            title: '电量',
                            align: 'center',
                            field: 'beginVolume',
                            width: 30,
                            formatter: function(val, row) {
                                var beginVolume = row.beginVolume == null ? 0 : row.beginVolume;
                                var currentVolume = row.currentVolume == null ? 0 : row.currentVolume;
                                return '' + beginVolume +'->' + currentVolume + '';
                            }
                        },
                        {
                            title: '开始时间',
                            align: 'center',
                            field: 'beginTime',
                            width: 30
                        },
                        {
                            title: '结束时间',
                            align: 'center',
                            field: 'endTime',
                            width: 30
                        },
                        {
                            title: '计划时长',
                            align: 'center',
                            field: 'duration',
                            width: 30
                        },
                        {
                            title: '客户名称',
                            align: 'center',
                            field: 'customerFullname',
                            width: 30
                        },
                        {
                            title: '客户手机',
                            align: 'center',
                            field: 'customerMobile',
                            width: 30
                        },
                        {
                            title: '柜子名称/箱号',
                            align: 'center',
                            field: 'cabinetName',
                            width: 80,
                            formatter: function(val, row) {
                                var cabinetName = (row.cabinetName == null || row.cabinetName == '') ? '' : row.cabinetName;
                                var boxNum = (row.boxNum == null || row.boxNum == '') ? '' : row.boxNum;
                                return ''+ cabinetName +'/'+ boxNum +'';
                            }
                        },
                        {
                            title: '维护员',
                            align: 'center',
                            field: 'keeperName',
                            width: 30
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 30,
                            formatter: function(val, row) {
                                var html = '<a href="javascript:view(\'ID\')">查看</a>';
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
            var agentId = $('#agent_id').combotree('getValue');
            var batteryId = $('#battery_id').val();
            var queryParams = {
                batteryId: batteryId,
                agentId: agentId
            };

            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:786px;height:500px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/battery_charge_record/view.htm?id=" + id

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
                                <input id="agent_id" class="easyui-combotree" editable="false" style="width: 200px;height: 28px;"
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
                            <td align="right" width="80">电池编号：</td>
                            <td><input type="text" class="text" id="battery_id"  /></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                        <h3>电池充电记录列表</h3>
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

