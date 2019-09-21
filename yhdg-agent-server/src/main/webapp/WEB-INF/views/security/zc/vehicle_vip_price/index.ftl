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
                url: "${contextPath}/security/zc/vehicle_vip_price/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 40
                        },
                        {
                            title: 'vip套餐名称',
                            align: 'center',
                            field: 'priceName',
                            width: 40
                        },
                        {
                            title: '类型',
                            align: 'center',
                            field: 'categoryName',
                            width: 40
                        },
                        {
                            title: '车辆型号',
                            align: 'center',
                            field: 'modelName',
                            width: 40
                        },
                        {
                            title: '车辆名称',
                            align: 'center',
                            field: 'vehicleName',
                            width: 40
                        },
                        {
                            title: '是否启用',
                            align: 'center',
                            field: 'isActive',
                            width: 40,
                            formatter: function(row) {
                                if(row == 1) {
                                    return '<a style="color: #00FF00;">是</a>';
                                }else{
                                    return '<a style="color: #ff0000;">否</a>';
                                }
                            }
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 40,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='zc.VehicleVipPrice:edit'>
                                    html += '<a href="javascript:edit(\ID\)">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='zc.VehicleVipPrice:remove'>
                                    html += ' <a href="javascript:remove(\ID\)">删除</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
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

        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var queryParams = {
            };

            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:850px;height:540px;',
                title: '新建',
                href: "${contextPath}/security/zc/vehicle_vip_price/add.htm",
                windowData:{
                    ok:function (id) {
                        edit(id);
                    }
                },
                event: {
                    onClose: function() {
                        query();
                    }
                }
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:850px;height:540px;',
                title: '修改',
                href: "${contextPath}/security/zc/vehicle_vip_price/edit.htm?id=" + id,
                event: {
                    onClose: function() {
                        query();
                    },
                    onLoad: function() {
                    }
                }
            });
        }

        function remove(id) {
            $.messager.confirm("提示信息", "确认删除?", function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/zc/vehicle_vip_price/delete.htm?id=" + id, function (json) {
                        if (json.success) {
                            $.messager.alert("提示信息", "删除成功", "info");
                            reload();
                        } else {
                            $.messager.alert("提示信息", json.message, "info")
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
                    <table cellpadding="0" cellspacing="0" border="0">
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='zc.VehicleVipPirce:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>租车VIP套餐</h3>
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

