<@app.html>
    <@app.head>
    <script>
        $.extend($.fn.validatebox.defaults.rules, {
            equals: {
                validator: function (value, param) {
                    return value == $(param[0]).val();
                },
                message: '密码不匹配'
            },
            mobile: {
                validator: function (value) {
                    if (value != "") {
                        return /^1\d{10}$/.test(value);
                    }

                },
                message: '请正确输入手机号码'
            },
            unique: {
                validator: function (value) {
                    var success;
                    $.ajax({
                        cache: false,
                        async: false,
                        type: 'POST',
                        url: '${contextPath}/security/basic/customer/unique.htm',
                        data: {
                            mobile: value
                        },
                        dataType: 'json',
                        success: function (json) {
                            <@app.json_jump/>
                            if (json.success) {
                                success = true;
                            } else {
                                success = false;
                            }
                        }
                    });
                    return success;
                },
                message: '手机号重复'
            }
        });

        $(function () {
            $('#page_table').datagrid({

                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/basic/customer_active_whitelist/page.htm",
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
                        {field: 'checkbox', checkbox: true},
                        {
                            title: '手机号码',
                            align: 'center',
                            field: 'mobile',
                            width: 50,
                            formatter: function (val) {
                                return val;
                            }
                        },
                        {
                            title: '姓名',
                            align: 'center',
                            field: 'fullname',
                            width: 30},
                        {
                            title: '单位',
                            align: 'center',
                            field: 'companyName',
                            width: 30
                        },
                        {
                            title: '电池/租车押金',
                            align: 'center',
                            field: 'batteryForegift',
                            width: 50,
                            formatter: function (val, row) {
                                if (val != null) {
                                    return Number(row.batteryForegift / 100).toFixed(2) + "/" + Number(row.vehicleForegift / 100).toFixed(2);
                                }
                            }
                        },
                        {
                            title: '充值/赠送/运营商余额',
                            align: 'center',
                            field: 'balance',
                            width: 80,
                            formatter: function (val, row) {
                                return Number(row.balance / 100).toFixed(2) + "/" + Number(row.giftBalance / 100).toFixed(2) + "/" + Number(row.agentBalance / 100).toFixed(2);
                            }
                        },
                        {
                            title: '绑定电池',
                            align: 'center',
                            field: 'batteryId',
                            width: 40
                        },
                        {
                            title: '注册时间',
                            align: 'center',
                            field: 'createTime',
                            width: 70
                        },
                        {
                            title: '来源',
                            align: 'center',
                            field: 'registerTypeName',
                            width: 30
                        },
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 40
                        },
                        {
                            title: '资费组',
                            align: 'center',
                            field: 'priceGroupName',
                            width: 40
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'isActive',
                            width: 30,
                            formatter: function (val) {
                                if (val == 1) {
                                    return '启用';
                                } else {
                                    return '禁用';
                                }
                            }
                        },
                        {
                            title: '类型',
                            align: 'center',
                            field: 'typeName',
                            width: 30
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 80,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='1_1_6_3'>
                                    html += ' <a href="javascript:remove(ID)">删除</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                onLoadSuccess: function () {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                }, onDblClickRow: function (rowIndex, rowData) {
                    view(rowData.id);
                }
            });
        });


        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');

        }

        function query() {
            var datagrid = $('#page_table');

            var mobile = $('#mobile').val();
            datagrid.datagrid('options').queryParams = {
                mobile: mobile
            };
            datagrid.datagrid('load');
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/customer_active_whitelist/delete.htm?id=" + id, function (json) {
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


        function selectCabinet() {
            App.dialog.show({
                css: 'width:826px;height:522px;overflow:visible;',
                title: '选择换电柜',
                href: "${contextPath}/security/hdg/cabinet/select_cabinets.htm",
                windowData: {
                    ok: function (config) {
                        $("#belong_cabinet_id").val(config.cabinet.id);
                        $("#belong_cabinet_name").val(config.cabinet.cabinetName);
                    }
                },
                event: {
                    onClose: function () {
                    }
                }
            });
        }

        function add() {
            App.dialog.show({
                css: 'width:926px;height:552px;overflow:visible;',
                title: '选择客户',
                href: "${contextPath}/security/basic/customer_active_whitelist/select_not_whitelist_customer.htm",
                windowData: {
                },
                event: {
                    onClose: function () {
                    }
                }
            });
        }

        function cleanCabinet() {
            $("#belong_cabinet_id").val('');
            $("#belong_cabinet_name").val('');
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
                            <td align="right" width="60">手机号：</td>
                            <td><input type="text" class="text" id="mobile"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='1_1_6_2'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>白名单客户列表</h3>
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