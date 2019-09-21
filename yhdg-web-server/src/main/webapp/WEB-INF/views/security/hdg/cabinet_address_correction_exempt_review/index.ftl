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
                url: "${contextPath}/security/hdg/cabinet_address_correction_exempt_review/page.htm",
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
                        {title: '免审人姓名', align: 'center', field: 'nickname', width: 90},
                        {title: '手机号', align: 'center', field: 'mobile', width: 90},
                        {title: '创建时间', align: 'center', field: 'createTime', width: 50},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 80,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='hdg.CabinetAddressCorrectionExemptReview:remove'>
                                    html += '<a href="javascript:remove(ID)">删除</a>';
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

            var nickname = $('#nick_name').val();
            var mobile = $('#mobile').val();

            datagrid.datagrid('options').queryParams = {
                nickname: nickname,
                mobile: mobile
            };
            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:456px;height:190px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/hdg/cabinet_address_correction_exempt_review/add.htm",
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function(ok) {
                if (ok) {
                    $.post("${contextPath}/security/hdg/cabinet_address_correction_exempt_review/delete.htm?id=" + id, function (json) {
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
                            <td align="right">免审人姓名：</td>
                            <td><input type="text" class="text" id="nick_name"/>&nbsp;&nbsp;</td>
                            <td align="right">手机号：</td>
                            <td><input type="text" class="text" id="mobile"/>&nbsp;&nbsp;</td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" >
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='hdg.CabinetAddressCorrectionExemptReview:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>纠错免审人员</h3>
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