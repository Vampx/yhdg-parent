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
                url: "${contextPath}/security/hdg/relief_station/page.htm",
                fitColumns: true,
                pageSize: 10,
                pageList: [10, 50, 100],
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
                            title: '救助站名称',
                            align: 'center',
                            field: 'stationName',
                            width: 60,
                            formatter: function (value, row) {
                                return '<span title="' + row.stationName + '">' + value + '</span>';
                            }
                        },
//                        {
//                            title: '地址',
//                            align: 'center',
//                            field: 'street',
//                            width: 100,
//                            formatter: function (val, row) {
//                                return (row.provinceName || '') + (row.cityName || '') + (row.districtName || '') + (row.street || '');
//                            }
//                        },
                        {title: '地址', align: 'center', field: 'street', width: 90},
                        {
                            title: '联系电话',
                            align: 'center',
                            field: 'tel',
                            width: 60
                        },
                        {
                            title: '星级',
                            align: 'center',
                            field: 'starName',
                            width: 30
                        },
                        {
                            title: '价格范围',
                            align: 'center',
                            field: 'minPrice',
                            width: 40,
                            formatter: function (val, row) {
                                return Number(val / 100).toFixed(2) + "-" + Number(row.maxPrice / 100).toFixed(2);
                            }
                        },
                        {
                            title: '经营范围',
                            align: 'center',
                            field: 'introduce',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 100,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='9_4_1_3'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='9_4_1_4'>
                                    html += ' <a href="javascript:edit(ID,EDIT_FLAG)">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='9_4_1_5'>
                                    html += ' <a href="javascript:remove(ID)">删除</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id).replace(/EDIT_FLAG/g, 0);
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
            var stationName = $('#station_name').val();
            datagrid.datagrid('options').queryParams = {
                stationName: stationName
            };

            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:786px;height:630px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/relief_station/view.htm?id=" + id
            });
        }

        function add() {
            App.dialog.show({
                css: 'width:400px;height:410px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/hdg/relief_station/new_add.htm",
                windowData:{
                    ok:function (id) {
                        edit(id,1);
                    }
                },
                event: {
                    onClose: function() {
                        query();
                    }
                }
            });
        }

        function edit(id,editFlag) {
            App.dialog.show({
                css: 'width:786px;height:620px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/hdg/relief_station/new_edit.htm?id=" + id + "&editFlag=" + editFlag,
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
                    $.post("${contextPath}/security/hdg/relief_station/delete.htm?id=" + id, function (json) {
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
                            <td align="right" style="width:80px;"> 救助站名称：</td>
                            <td><input type="text" class="text" id="station_name"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='9_4_1_2'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>救助站</h3>
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