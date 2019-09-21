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
                url: "${contextPath}/security/hdg/cabinet_address_correction/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            title: '换电柜编号',
                            align: 'center',
                            field: 'cabinetId',
                            width: 40
                        },
                        {title: '客户名称', align: 'center', field: 'customerFullname', width: 40},
                        {title: '客户手机号', align: 'center', field: 'customerMobile', width: 50},
                        {
                            title: '名称(纠错后)',
                            align: 'center',
                            field: 'cabinetName',
                            width: 60
                        },
                        {
                            title: '经度/纬度(纠错后)',
                            align: 'center',
                            field: 'lng',
                            width: 90,
                            formatter: function(val, row) {
                                var lng = row.lng;
                                var lat = row.lat;
                                if(lng == null && lat == null){
                                    return (row.lng ||'') + "/" + (row.lat||'');
                                }
                                return Number(row.lng).toFixed(6) + "/" + Number(row.lat).toFixed(6);
                            }
                        },
                        {
                            title: '地址(纠错后)',
                            align: 'center',
                            field: 'address',
                            width: 100,
                            formatter: function(val, row) {
                                return (row.provinceName || '') + (row.cityName || '') + (row.districtName || '') + (row.street || '');
                            }
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 50
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 70,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='hdg.CabinetAddressCorrection:view'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                                if(row.status == 1){
                                        <@app.has_oper perm_code='hdg.CabinetAddressCorrection:pass'>
                                            html += ' <a href="javascript:pass(\'ID\')">通过</a>';
                                        </@app.has_oper>
                                        <@app.has_oper perm_code='hdg.CabinetAddressCorrection:fail'>
                                            html += ' <a href="javascript:fail(\'ID\')">不通过</a>';
                                        </@app.has_oper>
                                    return html.replace(/ID/g, row.id);
                                }
                                <@app.has_oper perm_code='hdg.CabinetAddressCorrection:delete'>
                                    html += ' <a href="javascript:remove(\'ID\')">删除</a>';
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

            var customerName = $('#customer_fullname').val();
            datagrid.datagrid('options').queryParams = {
                customerFullname: customerName
            };
            datagrid.datagrid('load');
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function(ok) {
                if (ok) {
                    $.post("${contextPath}/security/hdg/cabinet_address_correction/delete.htm?id=" + id, function (json) {
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

        function pass(id) {
            $.messager.confirm('提示信息', '确认通过?', function(ok) {
                if (ok) {
                    $.post("${contextPath}/security/hdg/cabinet_address_correction/update.htm?id=" + id + "&status=2", function (json) {
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

        function fail(id) {
            $.messager.confirm('提示信息', '确认不通过?', function(ok) {
                if (ok) {
                    $.post("${contextPath}/security/hdg/cabinet_address_correction/update.htm?id=" + id + "&status=3", function (json) {
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

        function view(id) {
            App.dialog.show({
                css: 'width:600px;height:330px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/cabinet_address_correction/view.htm?id=" + id
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
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right" width="80">客户名称：</td>
                            <td><input type="text" class="text" id="customer_fullname"/>&nbsp;&nbsp;</td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" >
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                        <h3>设备位置纠错</h3>
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