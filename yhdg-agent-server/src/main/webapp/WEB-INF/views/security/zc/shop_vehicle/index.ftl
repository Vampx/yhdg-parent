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
                url: "${contextPath}/security/zc/shop_vehicle/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            title: '车辆编号',
                            align: 'center',
                            field: 'id',
                            width: 80,
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
                            title: '所属门店名称',
                            align: 'center',
                            field: 'shopName',
                            width: 80
                        },
                        {
                            title: '车架号',
                            align: 'center',
                            field: 'vinNo',
                            width: 120
                        },
                        {
                            title: '车辆型号',
                            align: 'center',
                            field: 'modelName',
                            width: 80
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
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
                        {title: '姓名', align: 'center', field: 'customerFullname', width: 80},
                        {title: '手机', align: 'center', field: 'customerMobile', width: 100},
                        {
                            title: '上报时间',
                            align: 'center',
                            field: 'reportTime',
                            width: 150
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
                            title: '上线状态',
                            align: 'center',
                            field: 'upLineStatus',
                            width: 80,
                            formatter: function (val) {
                                if(val = 0){
                                    return '未上线';
                                }
                                if(val = 1){
                                    return '已上线';
                                }
                            }
                        },
                        {
                            title: '上线时间',
                            align: 'center',
                            field: 'upLineTime',
                            width: 100
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

        function reloadTree() {
            var agentId = ${Session['SESSION_KEY_USER'].agentId};
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId
            };
            datagrid.datagrid('reload');
        }

        function swich_vehicle_model() {
            var agentId = ${Session['SESSION_KEY_USER'].agentId};
            var modelId = $('#model_id');
            modelId.combobox({
                url: "${contextPath}/security/zc/vehicle_model/list.htm?agentId=" + agentId + ""
            });
            modelId.combobox('reload');
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认解绑?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/zc/shop_vehicle/delete.htm?id=" + id, function (json) {
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
                <div class="panel search">
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0" style="border-collapse:separate; border-spacing: 0px 3px;">
                        <tr>
                            </td>
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
                                <option value="customerFullname">姓名</option>
                                <option value="customerMobile">手机号</option>
                            </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-top: 50px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
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