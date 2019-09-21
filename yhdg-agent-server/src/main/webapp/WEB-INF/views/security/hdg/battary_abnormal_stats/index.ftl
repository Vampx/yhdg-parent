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
                url: "${contextPath}/security/hdg/battary_abnormal_stats/page.htm",
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
                            title: 'checkbox', filed: 'id', checkbox: true
                        },
                        {
                            title: '电池编号',
                            align: 'center',
                            field: 'id',
                            width: 80
                        },
                        {
                            title: 'IMEI',
                            align: 'center',
                            field: 'code',
                            width: 140
                        },
                        {
                            title: '外壳编号',
                            align: 'center',
                            field: 'shellCode',
                            width: 100
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 120,
                            formatter: function (val, row) {
                                return row.statusName + '/' + row.chargeStatusName;
                            }
                        },
                        {
                            title: '上线状态',
                            align: 'center',
                            field: 'upLineStatusName',
                            width: 80
                        },
                        {
                            title: '上线时间',
                            align: 'center',
                            field: 'upLineTime',
                            width: 120
                        },
                        {
                            title: '电量',
                            align: 'center',
                            field: 'volume',
                            width: 80
                        },
                        {
                            title: '在线',
                            align: 'center',
                            field: 'isOnline',
                            width: 45,
                            formatter: function (val, row) {
                                return val == 1 ? '<a style="color: #00FF00;">是</a>' : '<a style="color: #ff0000;">否</a>';
                            }
                        },
                        {
                            title: '当前压差',
                            align: 'center',
                            field: 'realVoltageDiff',
                            width: 80
                        },
                        {
                            title: '充满压差',
                            align: 'center',
                            field: 'fullVoltageDiff',
                            width: 80
                        },
                        {
                            title: '放电压差',
                            align: 'center',
                            field: 'dischargeVoltageDiff',
                            width: 80
                        },

                        {title: '客户姓名', align: 'center', field: 'customerFullname', width: 80},
                        {title: '所在站点', align: 'center', field: 'cabinetName', width: 100},
                        {title: '所在柜口', align: 'center', field: 'boxNum', width: 100},
                        {title: '标记异常人', align: 'center', field: 'operator', width: 100},
                        {title: '异常时间', align: 'center', field: 'operatorTime', width: 100},
                        {title: '异常原因', align: 'center', field: 'abnormalCause', width: 100},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 200,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='hdg.Battery:detail'>
                                    html += ' <a href="javascript:view(\'ID\')" style="color:blue;">电池详情</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                onLoadSuccess: function (request) {
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
            datagrid.datagrid('options').queryParams = {
                agentId: agentId,
                id: $('#id').val(),
                abnormalCause:$('#abnormalCause').val()
            };
            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:780px;height:512px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/battery/view.htm?id=" + id
            });
        }

        function exportExcel() {
            var agentId = $('#agent_id').combotree('getValue');
            var id = $('#id').val();
            var abnormalCause=$('#abnormalCause').val();
            window.location.href = "${contextPath}/security/hdg/battary_abnormal_stats/export_excel.htm?id="+id+"&agentId="+agentId;
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
                                       style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?agentId=${Session['SESSION_KEY_USER'].agentId}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onLoadSuccess:function() {
                                    $('#agent_id').combotree('setValue', '${Session['SESSION_KEY_USER'].agentId}');
                                },
                                onClick: function(node) {

                                }
                            "
                                >
                            </td>
                            <td align="right" style="width: 100px;">电池编号：</td>
                            <td><input type="text" class="text" id="id"  /></td>
                            <td align="right" style="width: 100px;">异常原因：</td>
                            <td><input type="text" class="text" id="abnormalCause"  /></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <button class="btn btn_green" onclick="exportExcel()">导出</button>
                        </div>
                        <h3>
                            异常电池总统计
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