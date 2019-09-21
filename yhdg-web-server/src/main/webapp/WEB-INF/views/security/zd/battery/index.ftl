<@app.html>
    <@app.head>
    <script>
        $.extend($.fn.validatebox.defaults.rules, {
            unique: {
                validator: function (value, param) {
                    var success;
                    if (value != "") {
                        success = /^[A-F0-9]{8}$/.test(value);
                    }
                    if (success == false) {
                        return success;
                    }
                    $.ajax({
                        cache: false,
                        async: false,
                        type: 'POST',
                        url: '${contextPath}/security/hdg/battery/unique.htm',
                        data: {
                            id: value
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
                message: '电池编号重复或不符合规则'
            },
            uniqueCode: {
                validator: function (value, param) {
                    var success;
                    $.ajax({
                        cache: false,
                        async: false,
                        type: 'POST',
                        url: '${contextPath}/security/hdg/battery/unique_code.htm',
                        data: {
                            code: value,
                            id: param[0]
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
                message: '电池唯一码重复'
            },
            uniqueQrcode: {
                validator: function (value, param) {
                    var success;
                    $.ajax({
                        cache: false,
                        async: false,
                        type: 'POST',
                        url: '${contextPath}/security/hdg/battery/unique_qrcode.htm',
                        data: {
                            qrcode: value,
                            id: param[0]
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
                message: '电池二维码重复'
            },
            uniqueShellCode: {
                validator: function (value, param) {
                    var success;
                    $.ajax({
                        cache: false,
                        async: false,
                        type: 'POST',
                        url: '${contextPath}/security/hdg/battery/unique_shell_code.htm',
                        data: {
                            shellCode: value,
                            id: param[0]
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
                message: '电池外壳编号重复'
            }
        });

        $(function () {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/zd/battery/page.htm",
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
                            title: '电池编号',
                            align: 'center',
                            field: 'id',
                            width: 40
                        },
                        {
                            title: 'IMEI',
                            align: 'center',
                            field: 'code',
                            width: 80
                        },
                        {
                            title: '外壳编号',
                            align: 'center',
                            field: 'shellCode',
                            width: 60
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 60,
                            formatter: function (val, row) {
                                return row.statusName ;
                            }

                        },
                        {
                            title: '上线状态',
                            align: 'center',
                            field: 'upLineStatusName',
                            width: 60
                        },
                        {
                            title: '上线时间',
                            align: 'center',
                            field: 'upLineTime',
                            width: 120
                        },
                        {
                            title: '当前电量',
                            align: 'center',
                            field: 'volume',
                            width: 50
                        },
                        {
                            title: '版本',
                            align: 'center',
                            field: 'version',
                            width: 30
                        },
                        {
                            title: '在线',
                            align: 'center',
                            field: 'isOnline',
                            width: 30,
                            formatter: function (val, row) {
                                return val == 1 ? '<a style="color: #00FF00;">是</a>' : '<a style="color: #ff0000;">否</a>';
                            }
                        },
                        {
                            title: '正常',
                            align: 'center',
                            field: 'isNormal',
                            width: 30,
                            formatter: function (val, row) {
                                var html = (val == 1 ? '<a style="color: #00FF00;">是</a>' : '<a href="javascript:viewAbnormalCause(\'ID\')" style="color: #ff0000;">否</a>');
                                return html.replace(/ID/g, row.id);
                            }
                        },
                        {
                            title: '电池类型',
                            align: 'center',
                            field: 'batteryType',
                            width: 30
                        },
                        {
                            title: '上报时间',
                            align: 'center',
                            field: 'reportTime',
                            width: 60
                        },
                        {
                            title: '当前信号',
                            align: 'center',
                            field: 'currentSignal',
                            width: 40
                        },
                        {
                            title: '品牌',
                            align: 'center',
                            field: 'brandName',
                            width: 40
                        },
                        {title: '客户姓名', align: 'center', field: 'customerFullname', width: 40},
                        {title: '客户手机', align: 'center', field: 'customerMobile', width: 40},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 100,
                            formatter: function (val, row) {
                                var html = '';
                                 <@app.has_oper perm_code='zd.Battery:edit'>
                                    html += ' <a href="javascript:edit(\'ID\')">修改</a>';
                                 </@app.has_oper>
                                <@app.has_oper perm_code='zd.Battery:remove'>
                                    html += ' <a href="javascript:remove(\'ID\')">删除</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='zd.Battery:parameter'>
                                    html += ' <a href="javascript:parameter(\'ID\')" style="color:blue;">参数</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='zd.Battery:detail'>
                                    html += ' <a href="${contextPath}/security/main/module.htm?moduleId=8&url=${contextPath}/security/zd/battery/battery_detail.htm?id=ID" style="color:blue;" target="view_window" >电池详情</a>';
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
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();
            var datagrid = $('#page_table');
            var cabinetId = $('#cabinet_id').val();

            var queryParams = {
                isOnline: $('#is_online').val(),
                isNormal: $('#is_normal').val(),
                agentId: $('#agent_id').combotree('getValue'),
                cabinetId: cabinetId,
                minVolume: $('#min_volume').val(),
                maxVolume: $('#max_volume').val(),
                queryBeginTime: $('#begin_time').datetimebox('getValue'),
                queryEndTime: $('#end_time').datetimebox('getValue'),
                type: $('#type').val()
            };
            queryParams[queryName] = queryValue;
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function selectCabinet() {
            App.dialog.show({
                css: 'width:826px;height:522px;overflow:visible;',
                title: '选择换电柜',
                href: "${contextPath}/security/hdg/cabinet/select_cabinets.htm",
                windowData: {
                    ok: function (config) {
                        $("#cabinet_id").val(config.cabinet.id);
                        $("#cabinet_name").val(config.cabinet.cabinetName);
                    }
                },
                event: {
                    onClose: function () {
                    }
                }
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:700px;height:455px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/zd/battery/edit.htm?id=" + id,
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
                css: 'width:780px;height:512px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/zd/battery/view.htm?id=" + id
            });
        }

        function parameter(id) {
            App.dialog.show({
                css: 'width:1050px;height:626px;',
                title: '电池参数管理',
                href: "${contextPath}/security/hdg/battery_parameter/index.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/zd/battery/delete.htm?id=" + id, function (json) {
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
            var agentId = $('#agent_id').combotree('getValue');
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId
            };
            datagrid.datagrid('reload');
        }

        function update_fullvolume() {
            var batterys = $('#page_table').datagrid('getChecked');
            if (batterys.length > 0) {
                App.dialog.show({
                    css: 'width:350px;height:190px;',
                    title: '批量修改',
                    href: "${contextPath}/security/hdg/battery/batch_edit_fullvolume.htm",
                    windowData: {
                        ok: function (chargeCompleteVolume) {
                            var batteryIds = [];
                            for (var j = 0; j < batterys.length; j++) {
                                batteryIds.push(batterys[j].id);
                            }

                            $.post('${contextPath}/security/zd/battery/update_fullvolumes.htm', {
                                batteryIds: batteryIds,
                                chargeCompleteVolume: chargeCompleteVolume
                            }, function (json) {
                                if (json.success) {
                                    $.messager.alert('提示信息', '操作成功', 'info');
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
                $.messager.alert('提示信息', '请选择电池', 'info');
                return false;
            }
        }

        function param_batch_edit() {
            var datagrid = $('#page_table');
            var checked = datagrid.datagrid('getChecked');
            if(checked.length > 0) {
                var list = [];
                for (var i = 0; i < checked.length; i++) {
                    list.push(checked[i].id);
                }
                App.dialog.show({
                    css: 'width:1050px;height:626px;',
                    title: '批量批量修改',
                    href: "${contextPath}/security/hdg/battery_parameter/param_batch_edit.htm?ids=" + list
                });
            }else {
                $.messager.alert('提示信息', '请选择电池', 'info');
                return false;
            }
        }

        function changeToAbnormal() {
            var datagrid = $('#page_table');
            var checked = datagrid.datagrid('getChecked');
            if (checked.length == 0) {
                $.messager.alert('提示信息', '请选择电池', 'info');
                return;
            }else{
                App.dialog.show({
                    css: 'width:320px;height:185px;overflow:visible;',
                    title: '异常标识原因',
                    href: "${contextPath}/security/hdg/cabinet_box/abnormal.htm"
                });
            }
        }

        function confirmAbnormal() {
            var datagrid = $('#page_table');
            var checked = datagrid.datagrid('getChecked');
            var idList = [];
            var isNormalList = [];
            var abnormalCause = $('#abnormal_cause').val();
            for (var i = 0; i < checked.length; i++) {
                idList.push(checked[i].id);
                isNormalList.push(checked[i].isNormal);
            }
            if(abnormalCause==null||abnormalCause==""||abnormalCause==undefined){
                $.messager.alert('提示信息', '请输入禁用原因', 'info');
                return;
            }
            $.messager.confirm('提示信息', '正常电池标识为异常，确认操作?', function (ok) {
                if (ok) {
                    $.post('${contextPath}/security/hdg/battery/change_to_abnormal.htm', {
                        idList:idList,
                        isNormalList:isNormalList,
                        abnormalCause:abnormalCause
                    }, function (json) {
                        if (json.success) {
                            $.messager.alert('提示消息', '操作成功', 'info');
                            win.window('close');
                            query();
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
                        }
                    }, 'json');
                }
            });
        };

        function viewAbnormalCause(batteryId) {
            App.dialog.show({
                css: 'width:320px;height:265px;overflow:visible;',
                title: '异常标识原因',
                href: "${contextPath}/security/hdg/cabinet_box/abnormal_cause.htm?batteryId=" + batteryId + ""
            });
        }

        function changeToNormal() {
            var datagrid = $('#page_table');
            var checked = datagrid.datagrid('getChecked');
            if (checked.length == 0) {
                $.messager.alert('提示信息', '请选择电池', 'info');
                return;
            }else{
                var idList = [];
                var isNormalList = [];
                for (var i = 0; i < checked.length; i++) {
                    idList.push(checked[i].id);
                    isNormalList.push(checked[i].isNormal);
                }
                $.messager.confirm('提示信息', '异常电池解除异常，确认操作?', function (ok) {
                    if (ok) {
                        $.post('${contextPath}/security/hdg/battery/change_to_normal.htm', {
                            idList:idList,
                            isNormalList:isNormalList
                        }, function (json) {
                            if (json.success) {
                                $.messager.alert('提示消息', '操作成功', 'info');
                                query();
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
                    }
                });

            }
        }

        function export_excel() {
            $.messager.confirm('提示信息', '确认导出数据?', function (ok) {
                if (ok) {
                    App.dialog.show({
                        css: 'width:552px;height:460px;overflow:visible;',
                        title: '选择导出字段',
                        href: "${contextPath}/security/zd/battery/select_column.htm",
                        windowData: {
                            ok: function (config) {
                                $.messager.progress();
                                var columns = config.columns;
                                var queryName = $('#query_name').val();
                                var queryValue = $('#query_value').val();

                                var queryParams = {
                                    isOnline: $('#is_online').val(),
                                    isNormal: $('#is_normal').val(),
                                    agentId: $('#agent_id').combotree('getValue'),
                                    brand: $('#brand').val(),
                                    cabinetId: $('#cabinet_id').val(),
                                    type: $('#type').val(),
                                    columns: columns
                                };
                                queryParams[queryName] = queryValue;
                                $.post('${contextPath}/security/zd/battery/export_excel.htm', queryParams, function (json) {
                                    $.messager.progress('close');
                                    if (json.success) {
                                        document.location.href = '${contextPath}/security/zd/battery/download.htm?filePath=' + json.data;
                                    } else {
                                        $.messager.alert('提示消息', json.message, 'info');
                                    }
                                }, 'json');
                            }
                        },
                        event: {
                            onClose: function () {
                            }
                        }
                    });


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
                        <tr> <td align="right">运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="true"
                                       style="width:180px;height: 28px;"
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
                            <td align="right" width="85">
                                <select style="width:80px;" id="query_name">
                                    <option value="id">电池编号</option>
                                    <option value="simMemo">SIM卡</option>
                                    <option value="code">IMEI</option>
                                    <option value="shellCode">外壳编号</option>
                                    <option value="qrcode">二维码</option>
                                    <option value="customerMobile">手机号</option>
                                    <option value="version">版本</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"/></td>
                            <td align="right" width="80">所属站点：</td>
                            <td>
                                <input onclick="selectCabinet()" readonly type="text" class="text" id="cabinet_name"/>
                                <input type="hidden" id="cabinet_id"/>
                            </td>
                            <td align="right" width="80">正常：</td>
                            <td>
                                <select  id="is_normal"  name="isNormal"
                                         style="width:60px;">
                                    <option value="">所有</option>
                                    <option value="1">是</option>
                                    <option value="0">否</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td align="right">当前电量：</td>
                            <td><input type="text"style="width:55px;" class="text" id="min_volume"/> -
                                <input type="text"style="width:55px;" class="text" id="max_volume"/>
                            </td>
                            <td align="right" width="40">在线：</td>
                            <td>
                                <select id="is_online"  name="isOnline" style="width:60px;">
                                    <option value="">所有</option>
                                    <option value="1">是</option>
                                    <option value="0">否</option>
                                </select>
                            </td>
                            <td align="right">离柜时间：</td>
                            <td>
                                <input id="begin_time" class="easyui-datetimebox" type="text" style="width:150px;height:27px;"> -
                            </td>
                            <td>
                                <input id="end_time" class="easyui-datetimebox" type="text" style="width:150px;height:27px;">
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-top: 50px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <button class="btn btn_red" onclick="changeToAbnormal()">标识异常</button>
                            <button class="btn btn_red" onclick="changeToNormal()">解除异常</button>
                            <@app.has_oper perm_code='zd.Battery:paramBatchEdit'>
                                <button class="btn btn_red" onclick="param_batch_edit()">参数批量修改</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='zd.Battery:batchEdit'>
                                <button class="btn btn_red" onclick="update_fullvolume()">批量修改</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='zd.Battery:exportExcel'>
                                <button class="btn btn_red" onclick="export_excel()">批量导出</button>
                            </@app.has_oper>
                        </div>
                        <h3>电池信息列表</h3>
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