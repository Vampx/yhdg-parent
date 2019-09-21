<@app.html>
    <@app.head>
    <script>
        $.extend($.fn.validatebox.defaults.rules, {
            unique: {
                validator: function(value, param) {
                    var success;
                    if (value != "") {
                        success= /^[A-F0-9]{8}$/.test(value);
                    }
                    if(success==false){
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
                        success: function(json) {
                            <@app.json_jump/>
                            if(json.success) {
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
                validator: function(value, param) {
                    var success;
                    $.ajax({
                        cache: false,
                        async: false,
                        type: 'POST',
                        url: '${contextPath}/security/hdg/battery/unique_code.htm',
                        data: {
                            code: value
                        },
                        dataType: 'json',
                        success: function(json) {
                            <@app.json_jump/>
                            if(json.success) {
                                success = true;
                            } else {
                                success = false;
                            }
                        }
                    });

                    return success;
                },
                message: '电池唯一码重复'
            }
        });


        $(function () {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                fitColumns: true,
                pageSize: 10,
                url: "${contextPath}/security/hdg/unregister_battery/page.htm",
                pageList: [10, 50, 100],
                idField: 'code',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                columns: [
                    [
                        {
                            title: '编号',
                            align: 'center',
                            field: 'id',
                            width: 50
                        },
                        {
                            title: '电压',
                            align: 'center',
                            field: 'voltage',
                            width: 30,
                            formatter: function (val) {
                                return Number(val / 1000).toFixed(2) + "V";
                            }
                        },
                        {
                            title: '电流',
                            align: 'center',
                            field: 'electricity',
                            width: 30,
                            formatter: function (val) {
                                return Number(val / 1000).toFixed(2) + "A";
                            }
                        },
                        {
                            title: '剩余电量',
                            align: 'center',
                            field: 'currentCapacity',
                            width: 35,
                            formatter: function (val) {
                                return Number(val / 1000).toFixed(2) + "AH";
                            }
                        },
                        {
                            title: '温度',
                            align: 'center',
                            field: 'temp',
                            width: 35
                        },
                        {
                            title: '经度/纬度',
                            align: 'center',
                            field: 'lng',
                            width: 40,
                            formatter: function(val, row) {
                                var lng = row.lng;
                                var lat = row.lat;
                                if(lng == null && lat == null){
                                    return "";
                                }
                                return Number(row.lng).toFixed(6) + "/" + Number(row.lat).toFixed(6);
                            }
                        },
                        {
                            title: '电池状态',
                            align: 'center',
                            field: 'fetStatusName',
                            width: 35
                        },
                        {
                            title: '信号',
                            align: 'center',
                            field: 'currentSignal',
                            width: 35
                        },
                        {
                            title: '上报时间',
                            align: 'center',
                            field: 'reportTime',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 30,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='2_6_5_2'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='2_6_5_3'>
                                    html += ' <a href="javascript:bound_card(\'ID\')">绑卡</a>';
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

        function view(id) {
            App.dialog.show({
                css: 'width:800px;height:470px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/unregister_battery/view.htm?id=" + id
            });
        }

        function bound_card(id) {
            App.dialog.show({
                css: 'width:340px;height:270px;',
                title: '绑卡',
                href: "${contextPath}/security/hdg/unregister_battery/bound_card.htm?id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }

        function query(){
            var id = $('#id').val();
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                id: id
            };
            datagrid.datagrid('load');
        }
        $(function () {
            $('#id').keydown(function (event) {
                if (event.keyCode == 13) {
                 query();
                }
            });
        })
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
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right" width="50">IMEI：</td>
                            <td><input type="text" class="text" id="id"/>&nbsp;&nbsp;</td>
                            <td><button class="btn btn_red" onclick="query()">查询</button></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>未注册电池</h3>
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