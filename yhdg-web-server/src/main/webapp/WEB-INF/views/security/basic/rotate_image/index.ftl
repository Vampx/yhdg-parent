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
                url: "${contextPath}/security/basic/rotate_image/page.htm",
                fitColumns: true,
                pageSize: 10,
                pageList: [10, 50, 100],
                idField: 'id',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: function() { return 'height:7.0em;'; },
                columns: [
                    [
                        {
                            title: '缩略图',
                            align: 'center',
                            field: 'imagePath',
                            width: 60,
                            formatter: function (val, row) {
                                return "<img style='width:100px; height:70px;' onclick='preview(\""+ row.imagePath +"\")' src='${staticUrl}"+ val +"' />"
                            }
                        },
                        {
                            title: '链接URL',
                            align: 'center',
                            field: 'url',
                            width: 60
                        },
                        {
                            title: '编号',
                            align: 'center',
                            field: 'orderNum',
                            width: 60,
                            formatter: function(val, row) {
                                var html = "<input type='text' style='width: 40px; height:15px; text-align: center;' id='order_num_ID' onblur='updateOrderNum(ID)' onkeypress='if(event.keyCode==13) {updateOrderNum(ID);return false;}' value=" + val+ ">";
                                return html.replace(/ID/g, row.id);
                            }
                        },
                        {
                            title: '是否显示', align: 'center', field: 'isShow', width: 60,
                            formatter: function(val, row) {
                                return val == 1 ? "是" : "否";
                            }
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '<a href="javascript:view(ID)">查看</a>';
                                    <@app.has_oper perm_code='1_1_12_4'>
                                        html += ' <a href="javascript:edit(ID)">修改</a>';
                                    </@app.has_oper>
                                    <@app.has_oper perm_code='1_1_12_5'>
                                        html += ' <a href="javascript:remove(ID)">删除</a>';
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
            var isShow = $('#is_show').val();
            datagrid.datagrid('options').queryParams = {
                isShow : isShow
            };
            datagrid.datagrid('load');
        }

        function preview(path) {
            App.dialog.show({
                options:'maximized:true',
                title: '查看',
                href: "${contextPath}/security/main/preview.htm?path=" + path
            });
        }

        function updateOrderNum(id) {
            $.ajax({
                cache: false,
                async: false,
                type: 'POST',
                url: '${contextPath}/security/basic/rotate_image/update_order_num.htm',
                dataType: 'json',
                data: {id: id, orderNum: $('#order_num_' + id).val()},
                success: function (json) {
                    <@app.json_jump/>
                    if (json.success) {
                        reload();
                    } else {
                        $.messager.alert('提示信息', json.message, 'info');
                    }
                }
            });
        }

        function add() {
            App.dialog.show({
                css: 'width:380px;height:300px;',
                title: '新建',
                href: "${contextPath}/security/basic/rotate_image/add.htm",
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }
        function edit(id) {
            App.dialog.show({
                css: 'width:380px;height:300px;',
                title: '修改',
                href: "${contextPath}/security/basic/rotate_image/edit.htm?id=" + id,
                event: {
                    onClose: function() {
                        var datagrid = $('#page_table');
                        datagrid.datagrid('reload');
                    },
                    onLoad: function() {
                    }
                }
            });
        }
        function view(id) {
            App.dialog.show({
                css: 'width:380px;height:300px;',
                title: '查看',
                href: "${contextPath}/security/basic/rotate_image/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function(ok) {
                if(ok) {
                    $.post('${contextPath}/security/basic/rotate_image/delete.htm', {
                        id: id
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
    </script>
    </@app.head>
<style>
    .content .datagrid-body .datagrid-cell{
        height: 70px;
        line-height: 70px;
    }
    .content .datagrid-body .datagrid-cell input{
        margin-top: 26px;
    }
</style>
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
                            <td align="right">是否显示：</td>
                            <td>
                                <select style="width:70px;" id="is_show">
                                    <option value="">所有</option>
                                    <option value="1">显示</option>
                                    <option value="0">不显示</option>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='1_1_12_2'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>轮播图</h3>
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
