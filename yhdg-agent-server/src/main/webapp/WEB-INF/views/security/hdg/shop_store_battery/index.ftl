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
                url: "${contextPath}/security/hdg/shop_store_battery/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                        { field: 'checkbox', checkbox: true },
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
                        {
                            title: '门店名称',
                            align: 'center',
                            field: 'shopName',
                            width: 60
                        },
                        {
                            title: '换电类型',
                            align: 'center',
                            field: 'category',
                            width: 60,
                            formatter: function (val) {
                                if(val == 1) {
                                    return '换电';
                                }else if(val == 2){
                                    return '租电';
                                }
                            }
                        },
                        {
                            title: '电池ID',
                            align: 'center',
                            field: 'batteryId',
                            width: 60
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 60
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
            var shopName = $('#shop_name').val();
            var batteryId = $('#battery_id').val();

            datagrid.datagrid('options').queryParams = {
                shopName: shopName,
                batteryId: batteryId,
                agentId: agentId
            };

            datagrid.datagrid('load');
        }

        function unbindBattery() {
            var datagrid = $('#page_table');
            var checked = datagrid.datagrid('getChecked');
            var idList = [];
            for (var i = 0; i < checked.length; i++) {
                idList.push(checked[i].id);
            }
            if (idList.length == 0) {
                $.messager.alert('info', '请选择记录', 'info');
                return;
            }

            $.messager.confirm('提示信息', '确认解绑?', function (ok) {
                if (ok) {
                    $.post('${contextPath}/security/hdg/shop_store_battery/unbind_battery.htm', {
                        idList: idList
                    }, function (json) {
                        if (json.success) {
                            $.messager.alert('info', json.message, 'info');
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

                <div class="panel search" >
                    <div class="float_right">
                        <button class="btn btn_yellow"onclick="query()">搜索</button>
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
                                panelHeight:'200',
                                onClick: function(node) {
                                   reloadTree();
                                }
                            "
                                >
                            </td>
                            <td align="right" width="70">电池id：</td>
                            <td><input type="text" class="text" id="battery_id"  /></td>
                            <td align="right" width="70">门店名称：</td>
                            <td><input type="text" class="text" id="shop_name"  /></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='hdg.ShopStoreBattery:unbindBattery'>
                                <button class="btn btn_green" onclick="unbindBattery()">电池解绑</button>
                            </@app.has_oper>
                        </div>
                        <h3>门店库存电池</h3>
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










