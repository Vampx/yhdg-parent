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
                url: "${contextPath}/security/hdg/cabinet_control/page.htm",
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
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 40
                        },
                        {
                            title: '换电柜编号',
                            align: 'center',
                            field: 'cabinetId',
                            width: 40
                        },
                        {
                            title: '换电柜名称',
                            align: 'center',
                            field: 'cabinetName',
                            width: 40
                        },
                        {
                            title: '在线数量',
                            align: 'center',
                            field: 'isOnlineCount',
                            width: 40,
                            formatter: function (val, row) {
                                var html = '<a href="javascript:view_subcabinet(\'CABINET_ID\', 20)"  style="color: red">'+ val + '</a>';
                                return html.replace(/CABINET_ID/g, row.cabinetId);
                            }
                        },
                        {
                            title: '格口数量',
                            align: 'center',
                            field: 'boxCount',
                            width: 40,
                            formatter: function (val, row) {
                                var html = '<a href="javascript:view(\'CABINET_ID\', 1)"  style="color: red">'+ val + '</a>';
                                return html.replace(/CABINET_ID/g, row.cabinetId);
                            }
                        },
                        {
                            title: '空箱数',
                            align: 'center',
                            field: 'emptyCount',
                            width: 60,
                            formatter: function (val, row) {
                                var html = '<a href="javascript:view(\'CABINET_ID\', 2)"  style="color: red">'+ val + '</a>';
                                return html.replace(/CABINET_ID/g, row.cabinetId);
                            }
                        },
                        {
                            title: '满电数量',
                            align: 'center',
                            field: 'completeChargeCount',
                            width: 40,
                            formatter: function (val, row) {
                                var html = '<a href="javascript:view(\'CABINET_ID\', 3)"  style="color: red">'+ val + '</a>';
                                return html.replace(/CABINET_ID/g, row.cabinetId);
                            }
                        },
                        {
                            title: '欠压数量',
                            align: 'center',
                            field: 'notCompleteChargeCount',
                            width: 40,
                            formatter: function (val, row) {
                                var html = '<a href="javascript:view(\'CABINET_ID\', 4)"  style="color: red">'+ val + '</a>';
                                return html.replace(/CABINET_ID/g, row.cabinetId);
                            }
                        },
                        {
                            title: '待充电数量',
                            align: 'center',
                            field: 'waitChargeCount',
                            width: 40,
                            formatter: function (val, row) {
                                var html = '<a href="javascript:view(\'CABINET_ID\', 5)"  style="color: red">'+ val + '</a>';
                                return html.replace(/CABINET_ID/g, row.cabinetId);
                            }
                        },
                        {
                            title: '充电中数量',
                            align: 'center',
                            field: 'chargingCount',
                            width: 40,
                            formatter: function (val, row) {
                                var html = '<a href="javascript:view(\'CABINET_ID\', 6)"  style="color: red">'+ val + '</a>';
                                return html.replace(/CABINET_ID/g, row.cabinetId);
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
            datagrid.datagrid('options').queryParams = {
                cabinetName: $('#cabinet_name').val(),
                id: $('#id').val(),
                agentId: $('#agent_id').combotree('getValue'),
            };
            datagrid.datagrid('load');
        }

        function view_subcabinet(cabinetId, viewFlag) {
            App.dialog.show({
                css: 'width:1000px;height:515px;overflow:visible;',
                title: '分柜列表',
                href: "${contextPath}/security/hdg/cabinet_control/view_subcabinet.htm?cabinetId="+cabinetId+"&viewFlag="+viewFlag+""
            });
        }

        function view(cabinetId, viewFlag) {
            App.dialog.show({
                css: 'width:1000px;height:515px;overflow:visible;',
                title: '格子列表',
                href: "${contextPath}/security/hdg/cabinet_control/view.htm?cabinetId="+cabinetId+"&viewFlag="+viewFlag+""
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
                            <td align="right" width="80">换电柜编号：</td>
                            <td><input type="text" class="text" id="id" /></td>
                            <td align="right" width="80">换电柜名称：</td>
                            <td><input type="text" class="text" id="cabinet_name" /></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" >
                    <div class="toolbar clearfix">
                    <h3>站点监控</h3>
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