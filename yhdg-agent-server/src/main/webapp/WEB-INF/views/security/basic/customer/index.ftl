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
                url: "${contextPath}/security/basic/customer/page.htm",
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
                        {title: '运营商', align: 'center', field: 'agentName', width: 40},
                        {title: '姓名', align: 'center', field: 'fullname', width: 30},
                        {
                            title: '手机号码',
                            align: 'center',
                            field: 'mobile',
                            width: 50,
                            formatter: function (val) {
                                return val;
                            }
                        },
//                        {
//                            title: '身份证号码',
//                            align: 'center',
//                            field: 'idCard',
//                            width: 60
//                        },
//                        {title: '单位', align: 'center', field: 'companyName', width: 30},
//                        {
//                            title: '换电电池/租车押金',
//                            align: 'center',
//                            field: 'batteryForegift',
//                            width: 50,
//                            formatter: function (val, row) {
//                                if (val != null) {
//                                    return Number(row.batteryForegift / 100).toFixed(2) + "/" + Number(row.vehicleForegift / 100).toFixed(2);
//                                }
//                            }
//                        },
//                        {
//                            title: '租电电池押金',
//                            align: 'center',
//                            field: 'zdBatteryForegift',
//                            width: 50,
//                            formatter: function (val) {
//                                if (val != null) {
//                                    return Number(val / 100).toFixed(2);
//                                }
//                            }
//                        },
//                        {title: '会员等级', align: 'center', field: 'gradeName', width: 40},
                        {
                            title: '充值/赠送',
                            align: 'center',
                            field: 'balance',
                            width: 80,
                            formatter: function (val, row) {
                                return Number(row.balance / 100).toFixed(2) + "/" + Number(row.giftBalance / 100).toFixed(2) ;
                            }
                        },
//                        {title: '绑定换电电池', align: 'center', field: 'batteryId', width: 40},
//                        {title: '绑定租电电池', align: 'center', field: 'zdBatteryId', width: 40},
//                        {title: '所属站点', align: 'center', field: 'belongCabinetName', width: 40},
//                        {title: '积分', align: 'center', field: 'score', width: 40},
                        {title: '注册时间', align: 'center', field: 'createTime', width: 70},
                        {
                            title: '来源',
                            align: 'center',
                            field: 'registerTypeName',
                            width: 30
                        },
//                        {title: '资费组', align: 'center', field: 'priceGroupName', width: 40},
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
//                        {
//                            title: '类型',
//                            align: 'center',
//                            field: 'typeName',
//                            width: 30
//                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 80,
                            formatter: function (val, row) {
                                var html = '';
                                <#--<@app.has_oper perm_code='1_1_2_7'>-->
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                <#--</@app.has_oper>-->
                                <#--<@app.has_oper perm_code='1_1_2_8'>-->
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
                                <#--</@app.has_oper>-->
                                <#--<@app.has_oper perm_code='1_1_2_9'>-->
//                                    html += ' <a href="javascript:refund(ID)">扣款</a>';
                                <#--</@app.has_oper>-->
                                <#--<@app.has_oper perm_code='1_1_2_10'>-->
                                    html += ' <a href="javascript:remove(ID)">删除</a>';
                                <#--</@app.has_oper>-->
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
                },
                queryParams: {
                    appId: 0
                }
            });
        });


        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');

        }

        function query() {
            var datagrid = $('#page_table');
            var type = $('#type').val();
            var appId = $('#app_id').combotree('getValue');
            var fullname = $('#full_name').val();
            var mobile = $('#mobile').val();
            var batteryId = $('#battery_id').val();
            var belongCabinetId = $('#belong_cabinet_id').val();
            var agentId = $('#agent_id').combotree('getValue');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId,
                fullname: fullname,
                mobile: mobile,
                type: type,
                belongCabinetId: belongCabinetId,
                batteryId: batteryId,
                appId: appId
            };
            datagrid.datagrid('load');
        }

        function refund(id) {

            App.dialog.show({
                css: 'width:500px;height:250px;overflow:visible;',
                title: '扣款',
                href: "${contextPath}/security/basic/customer/edit_refund.htm?id=" + id,
                event: {
                    onClose: function () {
                        reload();
                    },
                    onLoad: function () {
                    }
                }
            });

        }

        function add() {
            App.dialog.show({
                css: 'width:600px;height:310px;',
                title: '新建',
                href: "${contextPath}/security/basic/customer/add.htm",
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function download_template() {
            $.messager.confirm('提示信息', '确认下载模板?', function (ok) {
                if (ok) {
                    document.location.href = '${contextPath}/static/excel/Customer.xls';
                }
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:600px;height:340px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/customer/edit.htm?id=" + id,
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
                css: 'width:850px;height:510px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/customer/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/customer/delete.htm?id=" + id, function (json) {
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

        function resignation(id) {
            $.messager.confirm('提示信息', '确认离职?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/customer/resignation.htm?id=" + id, function (json) {
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

        function batchActive() {
            var datagrid = $('#page_table');
            var checked = datagrid.datagrid('getChecked');
            var list = [];
            for (var i = 0; i < checked.length; i++) {
                list.push(checked[i].id);
            }
            if (list.length == 0) {
                $.messager.alert('info', '请选择记录', 'info');
                return;
            }

            $.messager.confirm('提示信息', '确认禁用/启动?', function (ok) {
                if (ok) {
                    $.post('${contextPath}/security/basic/customer/batch_active.htm', {
                        id: list
                    }, function (json) {
                        if (json.success) {
                            $.messager.alert('info', '操作成功', 'info');
                            reload();
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });


        }

        function batchRemove() {
            var datagrid = $('#page_table');
            var checked = datagrid.datagrid('getChecked');
            var list = [];
            for (var i = 0; i < checked.length; i++) {
                list.push(checked[i].id);
            }
            if (list.length == 0) {
                $.messager.alert('info', '请选择记录', 'info');
                return;
            }

            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post('${contextPath}/security/basic/customer/batch_remove.htm', {
                        id: list
                    }, function (json) {
                        if (json.success) {
                            $.messager.alert('info', '操作成功', 'info');
                            reload();
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });

        }

        function appUnbindMobile() {
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

            $.messager.confirm('提示信息', '此操作会结束用户未用完的套餐！确认解绑?', function (ok) {
                if (ok) {
                    $.post('${contextPath}/security/basic/customer/app_unbind_mobile.htm', {
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

        function fwUnbindMobile() {
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
                    $.post('${contextPath}/security/basic/customer/fw_unbind_mobile.htm', {
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


        function mpUnbindMobile() {
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
                    $.post('${contextPath}/security/basic/customer/mp_unbind_mobile.htm', {
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

        function import_customer() {
            App.dialog.show({
                css: 'width:380px;height:240px;overflow:visible;',
                title: '上传文件',
                href: "${contextPath}/security/basic/customer/upload_file.htm",
                event: {
                    onClose: function () {
                        reload();
                    }
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

        function reloadTree() {
            var agentId = $('#agent_id').combotree('getValue');
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId
            };
            datagrid.datagrid('reload');
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
                    <table cellpadding="0" cellspacing="0" border="0" style="border-collapse:separate; border-spacing: 0px 3px;">
                        <tr>
                            <td>平台类型：</td>
                            <td>
                                <input name="appId" id="app_id" style="height: 28px;width: 130px;" class="easyui-combobox"  editable="false"
                                       data-options="url:'${contextPath}/security/basic/agent/self_platform_agent_list.htm',
                                    method:'get',
                                    valueField:'id',
                                    textField:'agentName',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onSelect: function(node) {
                                        query();
                                    }"
                                />
                            </td>

                            <td align="right"  width="70">运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="false" style="width: 130px;height: 28px;"
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

                            <td align="right" width="70">电池编号：</td>
                            <td><input type="text" class="text" id="battery_id"/></td>
                            <td align="right" width="70">骑手姓名：</td>
                            <td><input type="text" class="text" id="full_name"/></td>
                            <td align="right" width="50">手机号：</td>
                            <td><input type="text" class="text" id="mobile"/></td>
                        <#-- <td align="right" width="60">姓名：</td>
                         <td><input type="text" class="text" id="full_name"/>&nbsp;&nbsp;</td>-->
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-top: 30px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <#--<@app.has_oper perm_code='1_1_2_14'>-->
                                <button class="btn btn_green" onclick="appUnbindMobile()">平台解绑</button>
                            <#--</@app.has_oper>-->
                            <#--<@app.has_oper perm_code='1_1_2_12'>-->
                                <button class="btn btn_green" onclick="fwUnbindMobile()">生活号解绑</button>
                            <#--</@app.has_oper>-->
                            <#--<@app.has_oper perm_code='1_1_2_13'>-->
                                <button class="btn btn_green" onclick="mpUnbindMobile()">公众号解绑</button>
                            <#--</@app.has_oper>-->
                            <#--<@app.has_oper perm_code='1_1_2_2'>-->
                                <button class="btn btn_red" onclick="batchActive()">批量禁用/启动</button>
                            <#--</@app.has_oper>-->
                            <#--<@app.has_oper perm_code='1_1_2_3'>-->
                                <button class="btn btn_red" onclick="batchRemove()">批量删除</button>
                            <#--</@app.has_oper>-->
                            <#--<@app.has_oper perm_code='1_1_2_6'>-->
                                <button class="btn btn_green" onclick="add()">新建</button>
                            <#--</@app.has_oper>-->
                        </div>
                        <h3>会员信息列表</h3>
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