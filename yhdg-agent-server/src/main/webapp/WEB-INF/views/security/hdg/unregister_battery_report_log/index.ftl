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
                fitColumns: true,
                pageSize: 10,
                pageList: [10, 50, 100],
                idField: 'code',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                columns: [
                    [
                        {
                            title: '编号',
                            align: 'center',
                            field: 'code',
                            width: 50
                        },
                        {
                            title: '电压',
                            align: 'center',
                            field: 'voltage',
                            width: 30,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) + "V";
                            }
                        },
                        {
                            title: '电流',
                            align: 'center',
                            field: 'electricity',
                            width: 30,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) + "A";
                            }
                        },
                        {
                            title: '剩余电量',
                            align: 'center',
                            field: 'currentCapacity',
                            width: 35,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) + "AH";
                            }
                        },
                        {
                            title: '温度',
                            align: 'center',
                            field: 'temp',
                            width: 35
                        },
                        {
                            title: '经度/纬度',
                            align: 'center',
                            field: 'lng',
                            width: 40,
                            formatter: function(val, row) {
                                var lng = row.lng;
                                var lat = row.lat;
                                if(lng == null && lat == null){
                                    return (row.lng ||'') + "/" + (row.lat||'');
                                }
                                return Number(row.lng).toFixed(6) + "/" + Number(row.lat).toFixed(6);
                            }
                        },
                        {
                            title: '电池状态',
                            align: 'center',
                            field: 'fetStatusName',
                            width: 35
                        },
                        {
                            title: '信号',
                            align: 'center',
                            field: 'currentSignal',
                            width: 35
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 30,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='2_6_4_2'>
                                    html += '<a href="javascript:view(\'ID\',\'CREATETIME\')">查看</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.code).replace(/CREATETIME/g, row.createTime);
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

        function view(code,createTime) {
            App.dialog.show({
                css: 'width:800px;height:460px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/unregister_battery_report_log/view.htm?code=" + code +"&createTime=" + createTime
            });
        }

        function query(){
            var queryLogTime = $('#query_log_time').datebox('getValue');
            var code = $('#code').val();
            if(queryLogTime == null || queryLogTime == '' ){
                $.messager.alert('提示信息', '请选择时间', '提示');
                return;
            }

            var datagrid = $('#page_table');
            datagrid.datagrid('options').url = "${contextPath}/security/hdg/unregister_battery_report_log/page.htm";
            datagrid.datagrid('options').queryParams = {
                queryLogTime: queryLogTime,
                code: code
            };
            datagrid.datagrid('load');
        }
        $(function () {
            $('#code').keydown(function (event) {
                if (event.keyCode == 13) {
                 query();
                }
            });
        })
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
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right" width="80">日志日期：</td>
                            <td><input type="text" class="easyui-datebox" id="query_log_time"  style="height: 28px;"/></td>
                            <td align="right" width="50">IMEI：</td>
                            <td><input type="text" class="text" id="code"/>&nbsp;&nbsp;</td>
                            <td><button class="btn btn_red" onclick="query()">查询</button></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>未注册电池上报日志</h3>
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