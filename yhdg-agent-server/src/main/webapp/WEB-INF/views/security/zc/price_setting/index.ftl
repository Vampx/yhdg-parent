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
                url: "${contextPath}/security/zc/price_setting/page.htm",
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
                            title: '套餐名称',
                            align: 'center',
                            field: 'settingName',
                            width: 40
                        },
                        {
                            title: '业务类型',
                            align: 'center',
                            field: 'categoryName',
                            width: 40
                        },
                        {
                            title: '车辆配置',
                            align: 'center',
                            field: 'vehicleName',
                            width: 40
                        },
                        {
                            title: '车辆型号',
                            align: 'center',
                            field: 'modelName',
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
                                <@app.has_oper perm_code='zc.PriceSetting:edit'>
                                    html += '<a href="javascript:edit(\ID\)">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='zc.PriceSetting:remove'>
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
            var settingName = $('#setting_name').val();
            var queryParams = {
                agentId: $('#agent_id').combotree('getValue'),
                settingName: settingName
            };

            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:560px;height:460px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/zc/price_setting/add.htm",
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
                css: 'width:850px;height:655px;',
                title: '修改',
                href: "${contextPath}/security/zc/price_setting/edit.htm?id=" + id,
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
                    $.post("${contextPath}/security/zc/price_setting/delete.htm?id=" + id, function (json) {
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
                        <tr>
                            <td align="right">运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="false"
                                       style="width: 130px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200'
                            "
                                >
                            </td>
                            <td width="80" align="right">套餐名称：</td>
                            <td><input type="text" class="text" id="setting_name"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='zc.PriceSetting:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>租车套餐</h3>
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

