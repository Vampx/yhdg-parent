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
                url: "${contextPath}/security/hdg/cabinet_box_prohibit_stats/page.htm",
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
                            width: 60,
                            formatter: function (val, row) {
                                return row.cabinet.agentName;
                            }

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
                            width: 60,
                            formatter: function (val, row) {
                                return row.cabinet.cabinetName;
                            }
                        },
                        {
                            title: '地址',
                            align: 'center',
                            field: 'address',
                            width: 60,
                            formatter: function (val, row) {
                                return row.cabinet.address;
                            }
                        },
                        {
                            title: '是否在线',
                            align: 'center',
                            field: 'isOnline',
                            width: 60,
                            formatter: function (val, row) {
                                if(val == 1) {
                                    return '<a style="color: #00FF00;">是</a>';
                                }else{
                                    return '<a style="color: #ff0000;">否</a>';
                                }

                            }
                        },
                        {
                            title: '禁用格口',
                            align: 'center',
                            field: 'boxNum',
                            width: 60
                        },
                        {
                            title: '禁用格口类型',
                            align: 'center',
                            field: 'type',
                            width: 60
                        },
                        {
                            title: '禁用人',
                            align: 'center',
                            field: 'operator',
                            width: 60
                        },
                        {
                            title: '禁用时间',
                            align: 'center',
                            field: 'operatorTime',
                            width: 60
                        },
                        {
                            title: '禁用原因',
                            align: 'center',
                            field: 'forbiddenCause',
                            width: 100
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 30,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='hdg.CabinetBoxProhibitStats:view'>
                                    html += '<a href="javascript:view(\'cabinetId\',\'boxNum\')">查看</a>';
                                </@app.has_oper>
                                html=html.replace(/cabinetId/g, row.cabinetId);
                                return html.replace(/boxNum/g, row.boxNum);
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
        function view(cabinetId,boxNum) {
            App.dialog.show({
                css: 'width:600px;height:400px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/cabinet_box_prohibit_stats/view.htm?cabinetId=" + cabinetId+"&boxNum="+boxNum

            });
        }

        function query() {
            var datagrid = $('#page_table');
            var agentId = $('#agent_id').combotree('getValue');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId,
                cabinetId: $('#cabinet_id').val()
            };
            datagrid.datagrid('load');
        }

        function exportExcel() {
            var agentId = $('#agent_id').combotree('getValue');
            var cabinetId = $('#cabinet_id').val()
            window.location.href = "${contextPath}/security/hdg/cabinet_box_prohibit_stats/export_excel.htm?agentId="+agentId+"&cabinetId="+cabinetId+"";
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
                                <input id="agent_id" class="easyui-combotree" editable="true"
                                       style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:true,
                                multiple:false,
                                panelHeight:'200'
                            "
                                >
                            </td>
                            <td align="right" style="width: 100px;">换电柜编号：</td>
                            <td><input type="text" class="text" id="cabinet_id"  /></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='hdg.CabinetBoxProhibitStats:exportExcel'>
                                <button class="btn btn_green" onclick="exportExcel()">导出</button>
                            </@app.has_oper>
                        </div>
                        <h3>
                            禁用箱门总统计
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