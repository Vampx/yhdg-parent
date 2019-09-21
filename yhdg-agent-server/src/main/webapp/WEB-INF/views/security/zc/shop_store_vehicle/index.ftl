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
                url: "${contextPath}/security/zc/shop_store_vehicle/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            field: 'categoryName',
                            width: 60
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
                            title: '电池类型',
                            align: 'center',
                            field: 'batteryCategory',
                            width: 60
                        },
                        {
                            title: '电池型号',
                            align: 'center',
                            field: 'batteryTypeName',
                            width: 60
                        },
                        {
                            title: '车架号',
                            align: 'center',
                            field: 'vinNo',
                            width: 60
                        },
                        {
                            title: '电池数量',
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
                                <@app.has_oper perm_code='zc.ShopStoreVehicle:edit'>
                                    html += " <a href='javascript:edit(\"ID\","+row.category+")'>修改</a>";
                                </@app.has_oper>
                                <@app.has_oper perm_code='zc.ShopStoreVehicle:remove'>
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
            var settingName = $('#settingName').val();

            datagrid.datagrid('options').queryParams = {
                settingName: settingName,
                agentId: agentId
            };

            datagrid.datagrid('load');
        }

        function upload_file() {
            App.dialog.show({
                css: 'width:600px;height:275px;',
                title: '导入',
                href: "${contextPath}/security/zc/shop_store_vehicle/upload_file.htm",
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function export_excel() {
            $.messager.confirm('提示信息', '确认导出模板?', function (ok) {
                if (ok) {
                    $.messager.progress();
                    $.post('${contextPath}/security/zc/shop_store_vehicle/export_excel.htm',
                    function (json) {
                        $.messager.progress('close');
                        if (json.success) {
                            document.location.href = '${contextPath}/security/zc/shop_store_vehicle/download.htm?filePath=' + json.data;
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        }

        function add() {
            App.dialog.show({
                css: 'width:800px;height:470px;',
                title: '新建',
                href: "${contextPath}/security/zc/shop_store_vehicle/add.htm",
                windowData:{
                    ok:function (id) {
                        edit(id);
                    }
                },
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function edit(id, category) {
            if (category == 3) {
                App.dialog.show({
                    css: 'width:800px;height:320px;',
                    title: '修改',
                    href: "${contextPath}/security/zc/shop_store_vehicle/edit.htm?id="+id,
                    event: {
                        onClose: function () {
                            reload();
                        }
                    }
                });
            }else {
                App.dialog.show({
                    css: 'width:800px;height:470px;',
                    title: '修改',
                    href: "${contextPath}/security/zc/shop_store_vehicle/edit.htm?id=" + id,
                    event: {
                        onClose: function () {
                            reload();
                        }
                    }
                });
            }
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认解绑?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/zc/shop_store_vehicle/delete.htm?id=" + id, function (json) {
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
                            <td><input type="text" class="text" id="settingName"  /></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='zc.ShopStoreVehicle:exportExcel'>
                                <button class="btn btn_green" onclick="export_excel()">导出模板</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='zc.ShopStoreVehicle:uploadFile'>
                                <button class="btn btn_green" onclick="upload_file()">批量导入</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='zc.ShopStoreVehicle:add'>
                                <button class="btn btn_green" onclick="add()">新增</button>
                            </@app.has_oper>
                        </div>
                        <h3>门店库存套餐</h3>
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





