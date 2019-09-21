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
                url: "${contextPath}/security/zc/shop_price_setting/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            title: '门店编号',
                            align: 'center',
                            field: 'shopId',
                            width: 60
                        },
                        {
                            title: '门店名称',
                            align: 'center',
                            field: 'shopName',
                            width: 60
                        },
                        {
                            title: '套餐名称',
                            align: 'center',
                            field: 'settingName',
                            width: 60
                        },
                        {
                            title: '类型',
                            align: 'center',
                            field: 'category',
                            width: 60,
                            formatter: function (val) {
                                if(val == 1) {
                                    return '换电';
                                }else if(val == 2){
                                    return '租电';
                                }else if(val == 3){
                                    return '不租电';
                                }
                            }
                        },
                        {
                            title: '车辆配置',
                            align: 'center',
                            field: 'vehicleName',
                            width: 60
                        },
                        {
                            title: '车辆型号',
                            align: 'center',
                            field: 'modelName',
                            width: 60
                        },
                        {
                            title: '启用',
                            align: 'center',
                            field: 'isActive',
                            width: 60,
                            formatter:function (val) {
                                if(val == 0) {
                                    return '否';
                                }else if(val == 1){
                                    return '是';
                                }
                            }
                        },
                        {
                            title: '电池数',
                            align: 'center',
                            field: 'batteryCount',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 60,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='zc.ShopPriceSetting:remove'>
                                    html += ' <a href="javascript:remove(\'ID\')">解绑</a>';
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
            var agentId = ${Session['SESSION_KEY_USER'].agentId};
            var priceName = $('#price_name').val();

            datagrid.datagrid('options').queryParams = {
                priceName: priceName,
                agentId: agentId
            };

            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:600px;height:245px;',
                title: '新建',
                href: "${contextPath}/security/zc/shop_price_setting/add.htm",
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认解绑?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/zc/shop_price_setting/delete.htm?id=" + id, function (json) {
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

                <div class="panel search" >
                    <div class="float_right">
                        <button class="btn btn_yellow"onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right" width="70">套餐名称：</td>
                            <td><input type="text" class="text" id="price_name"  /></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='zc.ShopPriceSetting:add'>
                                <button class="btn btn_green" onclick="add()">新增</button>
                            </@app.has_oper>
                        </div>
                        <h3>门店套餐</h3>
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





