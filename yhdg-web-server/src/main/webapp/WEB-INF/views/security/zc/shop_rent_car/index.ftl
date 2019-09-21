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
                url: "${contextPath}/security/zc/shop_rent_car/page.htm",
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
                            title: '所属运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
                        {
                            title: '门店编号',
                            align: 'center',
                            field: 'id',
                            width: 60
                        },
                        {title: '门店名称', align: 'center', field: 'shopName', width: 60},
                        {title: '地址', align: 'center', field: 'address', width: 60},
                        {title: '收款人姓名', align: 'center', field: 'payPeopleName', width: 60},
                        {title: '收款人手机', align: 'center', field: 'payPeopleMobile', width: 60},
                        {
                            title: '是否启用',
                            align: 'center',
                            field: 'activeStatus',
                            width: 30,
                            formatter: function(row) {
                                if(row == 1) {
                                    return '<a style="color: #00FF00;">是</a>';
                                }else{
                                    return '<a style="color: #ff0000;">否</a>';
                                }
                            }
                        },
                        {title: '营业时间', align: 'center', field: 'workTime', width: 40},
                        {
                            title: '经营范围',
                            align: 'center',
                            field: 'isVehicle',
                            width: 40,
                            formatter: function(val, row) {
                                var businessScope = '';
                                if(row.isExchange == 1) {
                                    businessScope += '换电 ';
                                }
                                if (row.isRent == 1){
                                    businessScope += '租电 ';
                                }
                                if (val == 1) {
                                    businessScope += '租车'
                                }
                                return businessScope;
                            }
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 60,
                            formatter: function(val, row) {
                                    var html = '';
                                    <@app.has_oper perm_code='zc.ShopRentCar:view'>
                                        html += ' <a href="javascript:view(\'ID\')">查看</a>';
                                    </@app.has_oper>
                                    <@app.has_oper perm_code='zc.ShopRentCar:edit'>
                                        html += ' <a href="javascript:edit(ID)">修改</a>';
                                    </@app.has_oper>
                                    <@app.has_oper perm_code='zc.ShopRentCar:remove'>
                                        html += ' <a href="javascript:remove(ID)">删除</a>';
                                    </@app.has_oper>
                                return html.replace(/ID/g, row.id).replace(/EDIT_FLAG/g, 0);
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

            var shopName = $('#shop_name').val();
            var id = $('#id').val();

            datagrid.datagrid('options').queryParams = {
                shopName: shopName,
                id: id,
                agentId: agentId
            };

            datagrid.datagrid('load');
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:1050px;height:650px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/zc/shop_rent_car/edit.htm?id=" + id ,
                event: {
                    onClose: function() {
                        query();
                    },
                    onLoad: function() {
                    }
                }
            });
        }

        function add() {
            App.dialog.show({
                css: 'width:950px;height:600px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/zc/shop_rent_car/add.htm",
                event: {
                    onClose: function() {
                        query();
                    }
                }
            });
        }

        function view(id) {
            App.dialog.show({
                css: 'width:1050px;height:650px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/zc/shop_rent_car/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function(ok) {
                if(ok) {
                    $.post('${contextPath}/security/zc/shop_rent_car/delete.htm', {
                        id: id
                    }, function (json) {
                        if (json.success) {
                            $.messager.alert('info', '操作成功', 'info');
                            query();
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
                                <input id="agent_id" class="easyui-combotree" editable="true" style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:true,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                }
                            "
                                >
                            </td>
                            <td align="right" width="70">门店编号：</td>
                            <td><input type="text" class="text" id="id"  /></td>
                            <td align="right" width="70">门店名称：</td>
                            <td><input type="text" class="text" id="shop_name"  /></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='zc.ShopRentCar:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>门店信息</h3>
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





