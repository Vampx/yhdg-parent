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
                        {
                            title: 'id',
                            align: 'center',
                            field: 'id',
                            width: 15,
                            formatter: function (val, row) {
                                if(row.firstDataFlag == 1) {
                                    if(row.countByUnPaidForegift != null) {
                                        $('#un_paid_foregift').html('');
                                        $('#un_paid_foregift').html(row.countByUnPaidForegift);
                                    }
                                    if(row.countByHdPaidForegift != null) {
                                        $('#hd_paid_foregift').html('');
                                        $('#hd_paid_foregift').html(row.countByHdPaidForegift);
                                    }
                                    if(row.countByZdPaidForegift != null) {
                                        $('#zd_paid_foregift').html('');
                                        $('#zd_paid_foregift').html(row.countByZdPaidForegift);
                                    }
                                    if(row.countByHdRefundedForegift != null) {
                                        $('#hd_refunded_foregift').html('');
                                        $('#hd_refunded_foregift').html(row.countByHdRefundedForegift);
                                    }
                                    if(row.countByZdRefundedForegift != null) {
                                        $('#zd_refunded_foregift').html('');
                                        $('#zd_refunded_foregift').html(row.countByZdRefundedForegift);
                                    }
                                }
                                return val;
                            }
                        },
                        {title: '商户', align: 'center', field: 'partnerName', width: 30},
                        {title: '运营商', align: 'center', field: 'agentName', width: 35},
                        {title: '姓名', align: 'center', field: 'fullname', width: 25},
                        {
                            title: '手机号码',
                            align: 'center',
                            field: 'mobile',
                            width: 40,
                            formatter: function (val) {
                                return val;
                            }
                        },
                        {
                            title: '充值/赠送',
                            align: 'center',
                            field: 'balance',
                            width: 35,
                            formatter: function (val, row) {
                                return Number(row.balance / 100).toFixed(2) + "/" + Number(row.giftBalance / 100).toFixed(2) ;
                            }
                        },
                        {title: '注册时间', align: 'center', field: 'createTime', width: 40},
                        {
                            title: '来源',
                            align: 'center',
                            field: 'registerTypeName',
                            width: 20
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'isActive',
                            width: 20,
                            formatter: function (val) {
                                if (val == 1) {
                                    return '启用';
                                } else {
                                    return '禁用';
                                }
                            }
                        },
                        {
                            title: '注册终端',
                            align: 'center',
                            field: 'belongCabinetName',
                            width: 35
                        },
                        {
                            title: '当前终端',
                            align: 'center',
                            field: 'balanceCabinetName',
                            width: 35
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 35,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.Customer:view'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.Customer:edit'>
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.Customer:remove'>
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
                    $('#page_table').datagrid('hideColumn','id');
                    var rows = $('#page_table').datagrid('getRows');
                    if(rows == 0) {
                        $('#un_paid_foregift').html(0);
                        $('#hd_paid_foregift').html(0);
                        $('#zd_paid_foregift').html(0);
                        $('#hd_refunded_foregift').html(0);
                        $('#zd_refunded_foregift').html(0);
                    }
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
            var partnerId = $('#partner_id').combobox('getValue');
            var fullname = $('#full_name').val();
            var mobile = $('#mobile').val();
            var batteryId = $('#battery_id').val();
            var belongCabinetId = $('#belong_cabinet_id').val();
            var belongCabinetName = $('#belong_cabinet_name').val();
            var agentId = $('#agent_id').combotree('getValue');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId,
                fullname: fullname,
                mobile: mobile,
                type: type,
                belongCabinetId: belongCabinetId,
                belongCabinetName: belongCabinetName,
                batteryId: batteryId,
                partnerId: partnerId
            };
            datagrid.datagrid('load');
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


        function setTransferPeople() {
            var datagrid = $('#page_table');
            var checked = datagrid.datagrid('getChecked');
            var id = [], partnerId = [];
            for(var i = 0; i < checked.length; i++) {
                id.push(checked[i].id);
                partnerId.push(checked[i].partnerId);
            }
            if(id.length == 0) {
                $.messager.alert('提示信息', '请选择记录', 'info');
                return;
            }

            if(id.length != 1) {
                $.messager.alert('提示信息', '只能选择一个骑手转让', 'info');
                return;
            }

            $.ajax({
                url: '${contextPath}/security/basic/customer_foregift_order/find_by_customer_id.htm?customerId='+id,
                success: function (text) {
                    var json = $.evalJSON(text);
                    <@app.json_jump/>
                    if (!json.success) {
                        $.messager.alert('提示信息', json.message, 'info');
                        return false;
                    } else {
                        selectTransferPeople(id, partnerId);
                    }
                }
            });

        }

        function selectTransferPeople(id, partnerId) {
            App.dialog.show({
                css: 'width:826px;height:522px;overflow:visible;',
                title: '选择转让人',
                href: "${contextPath}/security/basic/customer/select_transfer_customer.htm?partnerId=" + partnerId,
                windowData: {
                    ok: function(customer) {
                        var values = {
                            customerId: id,
                            partnerId: partnerId,
                            mobile: customer.customer.mobile
                        };
                        $.ajax({
                            cache: false,
                            async: false,
                            type: 'POST',
                            url: '${contextPath}/security/basic/customer/update_transfer_people.htm',
                            dataType: 'json',
                            data: values,
                            success: function (json) {
                                <@app.json_jump/>
                                if (json.success) {
                                    $.messager.alert('提示信息', '操作成功', 'info');
                                } else {
                                    $.messager.alert('提示信息', json.message, 'info');
                                }
                            }
                        });
                    }
                },
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }


        function clearAgent() {
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

            $.messager.confirm('提示信息', '此操作会清空客户的运营商id，确认执行?', function (ok) {
                if (ok) {
                    $.post('${contextPath}/security/basic/customer/clear_agent.htm', {
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

        function un_paid_foregift() {
            App.dialog.show({
                css: 'width:1360px;height:482px;overflow:visible;',
                title: '未交押金',
                href: "${contextPath}/security/basic/customer/un_paid_foregift.htm"
                + "?type=" + $('#type').val()
                + "&partnerId=" + $('#partner_id').combobox('getValue')
                + "&fullname=" + $('#full_name').val()
                + "&mobile=" + $('#mobile').val()
                + "&batteryId=" + $('#battery_id').val()
                + "&belongCabinetId=" + $('#belong_cabinet_id').val()
                + "&belongCabinetName=" + $('#belong_cabinet_name').val()
                + "&agentId=" + $('#agent_id').combotree('getValue')
                + "&unPaidForegiftFlag=1"
            });
        }

        function hd_paid_foregift() {
            App.dialog.show({
                css: 'width:1360px;height:482px;overflow:visible;',
                title: '换电已交押金',
                href: "${contextPath}/security/basic/customer/hd_paid_foregift.htm"
                + "?type=" + $('#type').val()
                + "&partnerId=" + $('#partner_id').combobox('getValue')
                + "&fullname=" + $('#full_name').val()
                + "&mobile=" + $('#mobile').val()
                + "&batteryId=" + $('#battery_id').val()
                + "&belongCabinetId=" + $('#belong_cabinet_id').val()
                + "&belongCabinetName=" + $('#belong_cabinet_name').val()
                + "&agentId=" + $('#agent_id').combotree('getValue')
                + "&hdPaidForegiftFlag=1"
            });
        }

        function zd_paid_foregift() {
            App.dialog.show({
                css: 'width:1360px;height:482px;overflow:visible;',
                title: '租电已交押金',
                href: "${contextPath}/security/basic/customer/zd_paid_foregift.htm"
                + "?type=" + $('#type').val()
                + "&partnerId=" + $('#partner_id').combobox('getValue')
                + "&fullname=" + $('#full_name').val()
                + "&mobile=" + $('#mobile').val()
                + "&batteryId=" + $('#battery_id').val()
                + "&belongCabinetId=" + $('#belong_cabinet_id').val()
                + "&belongCabinetName=" + $('#belong_cabinet_name').val()
                + "&agentId=" + $('#agent_id').combotree('getValue')
                + "&zdPaidForegiftFlag=1"
            });
        }

        function hd_refunded_foregift() {
            App.dialog.show({
                css: 'width:1360px;height:482px;overflow:visible;',
                title: '换电已退押金',
                href: "${contextPath}/security/basic/customer/hd_refunded_foregift.htm"
                + "?type=" + $('#type').val()
                + "&partnerId=" + $('#partner_id').combobox('getValue')
                + "&fullname=" + $('#full_name').val()
                + "&mobile=" + $('#mobile').val()
                + "&batteryId=" + $('#battery_id').val()
                + "&belongCabinetId=" + $('#belong_cabinet_id').val()
                + "&belongCabinetName=" + $('#belong_cabinet_name').val()
                + "&agentId=" + $('#agent_id').combotree('getValue')
                + "&hdRefundedForegiftFlag=1"
            });
        }

        function zd_refunded_foregift() {
            App.dialog.show({
                css: 'width:1360px;height:482px;overflow:visible;',
                title: '租电已退押金',
                href: "${contextPath}/security/basic/customer/zd_refunded_foregift.htm"
                + "?type=" + $('#type').val()
                + "&partnerId=" + $('#partner_id').combobox('getValue')
                + "&fullname=" + $('#full_name').val()
                + "&mobile=" + $('#mobile').val()
                + "&batteryId=" + $('#battery_id').val()
                + "&belongCabinetId=" + $('#belong_cabinet_id').val()
                + "&belongCabinetName=" + $('#belong_cabinet_name').val()
                + "&agentId=" + $('#agent_id').combotree('getValue')
                + "&zdRefundedForegiftFlag=1"
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
                    <table cellpadding="0" cellspacing="0" border="0" style="border-collapse:separate; border-spacing: 0px 3px;">
                        <tr>
                            <td align="right">商户：</td>
                            <td>
                                <input name="partnerId" id="partner_id" style="height: 28px;width: 130px;" class="easyui-combobox"  editable="false"
                                       data-options="url:'${contextPath}/security/basic/partner/list.htm?dummy=${'无'?url}',
                                    method:'get',
                                    valueField:'id',
                                    textField:'partnerName',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onSelect: function(node) {
                                    }"
                                />
                            </td>

                            <td align="right"  width="70">运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="true" style="width: 130px;height: 28px;"
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
                            <td align="right" width="70">骑手姓名：</td>
                            <td><input type="text" class="text" id="full_name"/></td>
                            <td align="right" width="70">手机号：</td>
                            <td><input type="text" class="text" id="mobile"/></td>
                        </tr>
                        <tr>
                            <td align="right" width="100">注册终端编号：</td>
                            <td><input type="text" class="text" style="width: 118px;" id="belong_cabinet_id"/></td>
                            <td align="right" width="100">注册终端名称：</td>
                            <td><input type="text" class="text" style="width: 118px;" id="belong_cabinet_name"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-top: 30px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='basic.Customer:setTransferPeople'>
                                <button class="btn btn_green" onclick="setTransferPeople()">换电转让</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='basic.Customer:clearAgent'>
                                <button class="btn btn_green" onclick="clearAgent()">清空运营商</button>
                            </@app.has_oper>
                            <#--<@app.has_oper perm_code='basic.Customer:appUnbindMobile'>-->
                                <#--<button class="btn btn_green" onclick="appUnbindMobile()">平台解绑</button>-->
                            <#--</@app.has_oper>-->
                            <@app.has_oper perm_code='basic.Customer:fwUnbindMobile'>
                                <button class="btn btn_green" onclick="fwUnbindMobile()">生活号解绑</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='basic.Customer:mpUnbindMobile'>
                                <button class="btn btn_green" onclick="mpUnbindMobile()">公众号解绑</button>
                            </@app.has_oper>
                            <#--<@app.has_oper perm_code='basic.Customer:batchActive'>-->
                                <#--<button class="btn btn_red" onclick="batchActive()">批量禁用/启动</button>-->
                            <#--</@app.has_oper>-->
                            <#--<@app.has_oper perm_code='basic.Customer:batchRemove'>-->
                                <#--<button class="btn btn_red" onclick="batchRemove()">批量删除</button>-->
                            <#--</@app.has_oper>-->
                            <@app.has_oper perm_code='basic.Customer:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3><div style="display: inline;"> 会员信息列表</div> &nbsp;&nbsp;&nbsp;
                            <div style="font-weight: 650;display: inline;font-size: 14px;" float="right">
                                未交：<a href="javascript:un_paid_foregift()"><span id="un_paid_foregift"></span></a> ；
                                换电已交：<a href="javascript:hd_paid_foregift()"><span id="hd_paid_foregift"></span></a>；
                                租电已交：<a href="javascript:zd_paid_foregift()"><span id="zd_paid_foregift"></span></a>；
                                换电已退 ：<a href="javascript:hd_refunded_foregift()"><span id="hd_refunded_foregift"></span></a>；
                                租电已退 ：<a href="javascript:zd_refunded_foregift()"><span id="zd_refunded_foregift"></span></a>；
                            </div></h3>
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