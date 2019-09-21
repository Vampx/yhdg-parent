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
                url: "${contextPath}/security/zc/vehicle/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            title: 'checkbox', filed: 'id', checkbox: true
                        },
                        {
                            title: '车架号',
                            align: 'center',
                            field: 'vinNo',
                            width: 120,
                            formatter: function (val, row) {
                                if(row.firstDataFlag == 1) {
                                    if(row.vehicleCommon != null) {
                                        $('#vehicle_common').html(row.vehicleCommon);
                                    }
                                    if(row.inUse != null) {
                                        $('#in_use').html(row.inUse);
                                    }
                                    if(row.inShop != null) {
                                        $('#in_shop').html(row.inShop);
                                    }
                                    if(row.leisure != null) {
                                        $('#leisure').html(row.leisure);
                                    }
                                }
                                return val;
                            }
                        },
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 100
                        },
                        {
                            title: '车辆型号',
                            align: 'center',
                            field: 'modelName',
                            width: 80
                        },
                        {
                            title: '所属门店编号',
                            align: 'center',
                            field: 'shopId',
                            width: 80
                        },
                        {
                            title: '所属门店名称',
                            align: 'center',
                            field: 'shopName',
                            width: 80
                        },
                        {title: '姓名', align: 'center', field: 'customerFullname', width: 80},
                        {title: '手机', align: 'center', field: 'customerMobile', width: 100},
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 80
                        },
                        {
                            title: '启用',
                            align: 'center',
                            field: 'isActive',
                            width: 80,
                            formatter: function (val) {
                                if(val = 0){
                                    return '否';
                                }
                                if(val = 1){
                                    return '是';
                                }
                            }
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
                            title: '上报时间',
                            align: 'center',
                            field: 'reportTime',
                            width: 100
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
                            width: 100
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 100,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='zc.Vehicle:remove'>
                                    html += ' <a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='zc.Vehicle:remove'>
                                    html += ' <a href="javascript:edit(\'ID\')">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='zc.Vehicle:remove'>
                                    html += ' <a href="javascript:remove(\'ID\')">删除</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                onLoadSuccess: function () {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                    var rows = $('#page_table').datagrid('getRows');
                    if(rows == 0) {
                        $('#vehicle_common').html(0);
                        $('#in_shop').html(0);
                        $('#in_use').html(0);
                        $('#leisure').html(0);
                    }
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
                agentId: ${Session['SESSION_KEY_USER'].agentId},
                modelId: $('#model_id').combobox('getValue'),
            };
            queryParams[queryName] = queryValue;
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:700px;height:425px;',
                title: '新建',
                href: "${contextPath}/security/zc/vehicle/add.htm",
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:700px;height:455px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/zc/vehicle/edit.htm?id=" + id,
                event: {
                    onClose: function () {
                        reload();
                    },
                    onLoad: function () {
                    }
                }
            });
        }

        function view(id) {
            App.dialog.show({
                css: 'width:870px;height:635px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/zc/vehicle/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/zc/vehicle/delete.htm?id=" + id, function (json) {
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

        function reloadTree() {
            var agentId = ${Session['SESSION_KEY_USER'].agentId};
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId
            };
            datagrid.datagrid('reload');
        }

        function batchUpdateUpLineStatus() {
            var vehicles = $('#page_table').datagrid('getChecked');
            if (vehicles.length > 0) {
                App.dialog.show({
                    css: 'width:350px;height:190px;',
                    title: '批量上线',
                    href: "${contextPath}/security/zc/vehicle/batch_edit_up_line_status.htm",
                    windowData: {
                        ok: function (vehicle) {
                            var vehicleIds = [];
                            for (var j = 0; j < vehicles.length; j++) {
                                if(vehicles[j].upLineStatus == 0) {
                                    vehicleIds.push(vehicles[j].id);
                                }
                            }
                            $.post('${contextPath}/security/zc/vehicle/batch_update_up_line_status.htm', {
                                vehicleIds: vehicleIds,
                                agentId: vehicle.agentId,
                                modelId: vehicle.modelId
                            }, function (json) {
                                if (json.success) {
                                    $.messager.alert('提示信息', json.message, 'info');
                                    reload();
                                } else {
                                    $.messager.alert('提示消息', json.message, 'info');
                                }
                            }, 'json');
                            return true;
                        }
                    }
                });
            } else {
                $.messager.alert('提示信息', '请选择车辆', 'info');
                return false;
            }
        }

        function upload_file() {
            App.dialog.show({
                css: 'width:700px;height:296px;',
                title: '批量导入',
                href: "${contextPath}/security/zc/vehicle/upload_file.htm",
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
                    $.post('${contextPath}/security/zc/vehicle/export_excel.htm',
                        function (json) {
                            $.messager.progress('close');
                            if (json.success) {
                                document.location.href = '${contextPath}/security/zc/vehicle/download.htm?filePath=' + json.data;
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json'
                    );
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
                    <table cellpadding="0" cellspacing="0" border="0" style="border-collapse:separate; border-spacing: 0px 3px;">
                        <tr>
                            <td align="right">车辆型号：</td>
                            <td>
                                <input id="model_id" style="height: 28px;width: 183px;" class="easyui-combobox"  editable="false"
                                       data-options="url:'${contextPath}/security/zc/vehicle_model/list.htm?agentId=${Session['SESSION_KEY_USER'].agentId}',
                                    method:'get',
                                    valueField:'id',
                                    textField:'modelName',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onSelect: function(node) {
                                    }"/>
                            </td>
                            <td align="right" width="85">
                            <select style="width:80px;" id="query_name">
                            <option value="vehicleName">车辆名称</option>
                            <option value="vinNo">车架号</option>
                            <option value="shopName">门店名称</option>
                            <option value="shopId">门店编号</option>
                            </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-top: 50px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='zc.VehicleModel:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='zc.VehicleModel:batchUpdateUpLineStatus'>
                                <button class="btn btn_green" onclick="batchUpdateUpLineStatus()">批量上线</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='zc.ShopStoreVehicle:exportExcel'>
                                <button class="btn btn_green" onclick="export_excel()">导出模板</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='zc.ShopStoreVehicle:uploadFile'>
                                <button class="btn btn_green" onclick="upload_file()">批量导入</button>
                            </@app.has_oper>
                        </div>
                        <h3>
                            <div style="display: inline;">车辆信息列表</div> &nbsp;&nbsp;&nbsp;  <div style="font-weight: 650;display: inline;" float="right">车辆共：<span id="vehicle_common"></span> 辆 ；使用中：<span id="in_use"></span> 辆；门店中 ：<span id="in_shop"></span> 辆；空闲 ：<span id="leisure"></span> 辆；</div>
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