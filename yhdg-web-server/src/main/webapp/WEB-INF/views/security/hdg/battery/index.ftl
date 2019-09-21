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
                url: "${contextPath}/security/hdg/battery/page.htm",
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
                            width: 80,
                            formatter: function (val, row) {
                                if(row.firstDataFlag == 1) {
                                    if(row.countByCustomerUse != null) {
                                        $('#customer_use').html('');
                                        $('#customer_use').html(row.countByCustomerUse);
                                    }
                                    if(row.countByInCabinet != null) {
                                        $('#in_cabinet').html('');
                                        $('#in_cabinet').html(row.countByInCabinet);
                                    }
                                    if(row.countByNotUse != null) {
                                        $('#not_use').html('');
                                        $('#not_use').html(row.countByNotUse);
                                    }
                                }
                                return val;
                            }
                        },
                        {
                            title: 'IMEI',
                            align: 'center',
                            field: 'code',
                            width: 140
                        },
                        {
                            title: '外壳编号',
                            align: 'center',
                            field: 'shellCode',
                            width: 100
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 120,
                            formatter: function (val, row) {
                                return row.statusName + '/' + row.chargeStatusName;
                            }
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
                            width: 120
                        },
                        {
                            title: '电量',
                            align: 'center',
                            field: 'volume',
                            width: 80
                        },
                        {
                            title: '版本',
                            align: 'center',
                            field: 'version',
                            width: 50
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
                            title: '正常',
                            align: 'center',
                            field: 'isNormal',
                            width: 45,
                            formatter: function (val, row) {
                                var html = (val == 1 ? '<a style="color: #00FF00;">是</a>' : '<a href="javascript:viewAbnormalCause(\'ID\')" style="color: #ff0000;">否</a>');
                                return html.replace(/ID/g, row.id);
                            }
                        },
                        {
                            title: '电池类型',
                            align: 'center',
                            field: 'batteryType',
                            width: 85
                        },
                        {
                            title: '当前压差',
                            align: 'center',
                            field: 'realVoltageDiff',
                            width: 80
                        },
                        {
                            title: '充满压差',
                            align: 'center',
                            field: 'fullVoltageDiff',
                            width: 80
                        },
                        {
                            title: '放电压差',
                            align: 'center',
                            field: 'dischargeVoltageDiff',
                            width: 80
                        },
                        {
                            title: '上报时间',
                            align: 'center',
                            field: 'reportTime',
                            width: 150
                        },
                        {
                            title: '信号',
                            align: 'center',
                            field: 'currentSignal',
                            width: 80
                        },
                        {title: '客户姓名', align: 'center', field: 'customerFullname', width: 80},
                        {title: '客户手机', align: 'center', field: 'customerMobile', width: 100},
                        {title: '所在站点', align: 'center', field: 'cabinetName', width: 140},
                        {title: '所在柜口', align: 'center', field: 'boxNum', width: 80},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 250,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='hdg.Battery:edit'>
                                    html += ' <a href="javascript:edit(\'ID\')">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='hdg.Battery:remove'>
                                    html += ' <a href="javascript:remove(\'ID\')">删除</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='hdg.Battery:parameter'>
                                    html += ' <a href="javascript:parameter(\'ID\')" style="color:blue;">参数</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='hdg.Battery:detail'>
                                    html += ' <a href="${contextPath}/security/main/module.htm?moduleId=3&url=${contextPath}/security/hdg/battery/battery_detail.htm?id=ID" style="color:blue;"  target="view_window">电池详情</a>';
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
                        $('#customer_use').html(0);
                        $('#in_cabinet').html(0);
                        $('#not_use').html(0);
                    }
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
            var queryVersionName = $('#query_version_name').val();
            var queryVersionValue = $('#query_version_value').val();
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
                status: $('#status').combobox('getValue'),
                chargeStatus: $('#charge_status').combobox('getValue'),
                type: $('#type').val()
            };
            queryParams[queryName] = queryValue;
            queryParams[queryVersionName] = queryVersionValue;
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:600px;height:300px;',
                title: '新建',
                href: "${contextPath}/security/hdg/battery/add.htm",
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function import_battery() {
            App.dialog.show({
                css: 'width:380px;height:210px;overflow:visible;',
                title: '上传文件',
                href: "${contextPath}/security/hdg/battery/upload_file.htm",
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
                href: "${contextPath}/security/hdg/battery/edit.htm?id=" + id,
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
                href: "${contextPath}/security/hdg/battery/view.htm?id=" + id
            });
        }

        function parameter(id) {
            App.dialog.show({
                css: 'width:1050px;height:626px;',
                title: '电池参数管理',
                href: "${contextPath}/security/hdg/battery_parameter/index.htm?id=" + id
            });
        }

        <#--function detail(id) {-->
            <#--App.dialog.show({-->
                <#--css: 'width:1200px;height:750px;',-->
                <#--title: '查看',-->
                <#--href: "${contextPath}/security/hdg/battery/battery_detail.htm?id=" + id-->
            <#--});-->
        <#--}-->

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/hdg/battery/delete.htm?id=" + id, function (json) {
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

        function download_template() {
            $.messager.confirm('提示信息', '确认下载模板?', function (ok) {
                if (ok) {
                    document.location.href = '${contextPath}/static/excel/Battery.xls';
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

                            $.post('${contextPath}/security/hdg/battery/update_fullvolumes.htm', {
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
                    title: '参数批量修改',
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
                $.messager.alert('提示信息', '请输入异常原因', 'info');
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

        function edit_up_line_status() {
            App.dialog.show({
                css: 'width:346px;height:232px;overflow:visible;',
                title: '电池上线',
                href: "${contextPath}/security/hdg/battery/edit_up_line_status.htm?"
            });
        }

        function export_excel() {
            $.messager.confirm('提示信息', '确认导出数据?', function (ok) {
                if (ok) {
                    App.dialog.show({
                        css: 'width:552px;height:310px;overflow:visible;',
                        title: '选择导出字段',
                        href: "${contextPath}/security/hdg/battery/select_column.htm",
                        windowData: {
                            ok: function (config) {
                                $.messager.progress();
                                var columns = config.columns;
                                var queryName = $('#query_name').val();
                                var queryValue = $('#query_value').val();
                                var queryVersionName = $('#query_version_name').val();
                                var queryVersionValue = $('#query_version_value').val();

                                var queryParams = {
                                    isOnline: $('#is_online').val(),
                                    isNormal: $('#is_normal').val(),
                                    agentId: $('#agent_id').combotree('getValue'),
                                    cabinetId: $('#cabinet_id').val(),
                                    minVolume: $('#min_volume').val(),
                                    maxVolume: $('#max_volume').val(),
                                    queryBeginTime: $('#begin_time').datetimebox('getValue'),
                                    queryEndTime: $('#end_time').datetimebox('getValue'),
                                    status: $('#status').combobox('getValue'),
                                    chargeStatus: $('#charge_status').combobox('getValue'),
                                    type: $('#type').val(),
                                    columns: columns
                                };
                                queryParams[queryName] = queryValue;
                                queryParams[queryVersionName] = queryVersionValue;
                                $.post('${contextPath}/security/hdg/battery/export_excel.htm', queryParams, function (json) {
                                    $.messager.progress('close');
                                    if (json.success) {
                                        document.location.href = '${contextPath}/security/hdg/battery/download.htm?filePath=' + json.data;
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
        
        function customer_use() {
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();
            var queryVersionName = $('#query_version_name').val();
            var queryVersionValue = $('#query_version_value').val();

            App.dialog.show({
                css: 'width:1360px;height:482px;overflow:visible;',
                title: '客户使用中',
                href: "${contextPath}/security/hdg/battery/customer_use.htm"
                + "?isOnline=" + $('#is_online').val()
                + "&agentId=" + $('#agent_id').combotree('getValue')
                + "&cabinetId=" + $('#cabinet_id').val()
                + "&minVolume=" + $('#min_volume').val()
                + "&maxVolume=" + $('#max_volume').val()
                + "&queryBeginTime=" + $('#begin_time').datetimebox('getValue')
                + "&queryEndTime=" + $('#end_time').datetimebox('getValue')
                + "&status=" + $('#status').combobox('getValue')
                + "&chargeStatus=" + $('#charge_status').combobox('getValue')
                + "&customerUseFlag=1"
                + "&" + queryName + "=" + queryValue
                + "&" + queryVersionName + "=" + queryVersionValue
            });
        }
        
        function in_cabinet() {
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();
            var queryVersionName = $('#query_version_name').val();
            var queryVersionValue = $('#query_version_value').val();

            App.dialog.show({
                css: 'width:1360px;height:482px;overflow:visible;',
                title: '柜子中',
                href: "${contextPath}/security/hdg/battery/in_cabinet.htm"
                +"?isOnline=" + $('#is_online').val()
                +"&agentId=" + $('#agent_id').combotree('getValue')
                +"&cabinetId=" + $('#cabinet_id').val()
                +"&minVolume=" + $('#min_volume').val()
                +"&maxVolume=" + $('#max_volume').val()
                +"&queryBeginTime=" + $('#begin_time').datetimebox('getValue')
                +"&queryEndTime=" + $('#end_time').datetimebox('getValue')
                +"&status=" + $('#status').combobox('getValue')
                +"&chargeStatus=" + $('#charge_status').combobox('getValue')
                +"&inCabinetFlag=1"
                +"&"+ queryName + "=" + queryValue
                +"&"+ queryVersionName + "=" + queryVersionValue
            });
        }
        
        function not_use() {
            var queryName = $('#query_name').val();
            var queryValue = $('#query_value').val();
            var queryVersionName = $('#query_version_name').val();
            var queryVersionValue = $('#query_version_value').val();

            App.dialog.show({
                css: 'width:1360px;height:482px;overflow:visible;',
                title: '未使用',
                href: "${contextPath}/security/hdg/battery/not_use.htm"
                +"?isOnline=" + $('#is_online').val()
                +"&agentId=" + $('#agent_id').combotree('getValue')
                +"&cabinetId=" + $('#cabinet_id').val()
                +"&minVolume=" + $('#min_volume').val()
                +"&maxVolume=" + $('#max_volume').val()
                +"&queryBeginTime=" + $('#begin_time').datetimebox('getValue')
                +"&queryEndTime=" + $('#end_time').datetimebox('getValue')
                +"&status=" + $('#status').combobox('getValue')
                +"&chargeStatus=" + $('#charge_status').combobox('getValue')
                +"&notUseFlag=1"
                +"&"+ queryName + "=" + queryValue
                +"&"+ queryVersionName + "=" + queryVersionValue
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
                                       style="width:142px;height: 28px;"
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
                                    <option value="customerFullname">客户姓名</option>
                                    <option value="customerMobile">客户手机</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="query_value"/></td>
                            <td align="right" width="85">
                                <select style="width:70px;" id="query_version_name">
                                    <option value="version">版本=</option>
                                    <option value="queryAntiVersion">版本≠</option>
                                </select>
                            </td>
                            <td><input type="text" class="text" id="query_version_value"/></td>
                            <td align="right">状态：</td>
                            <td>
                                <select id="status" class="easyui-combobox" style="width: 150px; height: 28px;" editable="false">
                                    <option value="">所有</option>
                                    <#list StatusEnum as e>
                                        <option value="${e.getValue()}"> ${(e.getName())!''}</option>
                                    </#list>
                                </select>
                                /
                                <select id="charge_status" class="easyui-combobox" style="width: 150px; height: 28px;" editable="false">
                                    <option value="">所有</option>
                                    <#list ChargeStatusEnum as e>
                                        <option value="${e.getValue()}"> ${(e.getName())!''}</option>
                                    </#list>
                                </select>
                            </td>
                            <#--<td align="right" width="40">型号：</td>-->
                            <#--<td>-->
                                <#--<select style="width:60px;" id="type">-->
                                    <#--<option value="">所有</option>-->
                                    <#--<#list batteryTypeList as c>-->
                                        <#--<option value="${(c.itemValue)!''}">${(c.itemName)!''}</option>-->
                                    <#--</#list>-->
                                <#--</select>-->
                            <#--</td>-->
                        </tr>
                        <tr>
                            <td align="right">所属站点：</td>
                            <td><input onclick="selectCabinet()" readonly type="text" class="text"
                                       id="cabinet_name"/><input
                                    type="hidden" id="cabinet_id"/></td>

                            <td align="right">当前电量：</td>
                            <td><input type="text"style="width:55px;" class="text"
                                       id="min_volume"/>-
                                <input type="text"style="width:55px;" class="text"
                                       id="max_volume"/></td>
                            <td align="right" width="40">在线：</td>
                            <td>
                                <select  id="is_online"  name="isOnline"
                                        style="width:60px;">
                                    <option value="">所有</option>
                                    <option value="1">是</option>
                                    <option value="0">否</option>
                                </select>
                            </td>
                            <td align="right">离柜时间：</td>
                            <td>
                                <input id="begin_time" class="easyui-datetimebox"
                                       type="text"
                                       style="width:150px;height:27px;">
                                -
                                <input id="end_time" class="easyui-datetimebox" type="text"
                                       style="width:150px;height:27px;">
                            </td>
                        </tr>
                        <tr>
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
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-top: 50px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <button class="btn btn_red" onclick="changeToAbnormal()">标识异常</button>
                            <button class="btn btn_red" onclick="changeToNormal()">解除异常</button>
                            <button class="btn btn_red" onclick="edit_up_line_status()">电池上线</button>
                            <button class="btn btn_red" onclick="param_batch_edit()">参数批量修改</button>
                            <@app.has_oper perm_code='hdg.Battery:batchEdit'>
                                <button class="btn btn_red" onclick="update_fullvolume()">批量修改</button>
                            </@app.has_oper>
                            <@app.has_oper perm_code='hdg.Battery:exportExcel'>
                                <button class="btn btn_red" onclick="export_excel()">批量导出</button>
                            </@app.has_oper>
                            <#--<@app.has_oper perm_code='2_6_1_4'>-->
                                <#--<button class="btn btn_green" onclick="download_template()">模板下载</button>-->
                            <#--</@app.has_oper>-->
                            <#--<@app.has_oper perm_code='2_6_1_5'>-->
                                <#--<button class="btn btn_green" onclick="import_battery()">批量导入</button>-->
                            <#--</@app.has_oper>-->
                            <#--<@app.has_oper perm_code='2_6_1_6'>-->
                                <#--<button class="btn btn_green" onclick="add()">新建</button>-->
                            <#--</@app.has_oper>-->
                        </div>
                        <h3>
                            <div style="display: inline;"> 电池信息列表</div> &nbsp;&nbsp;&nbsp;  <div style="font-weight: 650;display: inline;" float="right">客户使用：<a href="javascript:customer_use()"><span id="customer_use"></span></a> ；柜子中：<a href="javascript:in_cabinet()"><span id="in_cabinet"></span></a>；未使用 ：<a href="javascript:not_use()"><span id="not_use"></span></a>；</div>
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