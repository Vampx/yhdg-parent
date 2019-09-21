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
                url: "${contextPath}/security/basic/feedback/page.htm?today=${today}",
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
                            title: '平台',
                            align: 'center',
                            field: 'partnerName',
                            width: 60
                        },
                        {title: '客户姓名', align: 'center', field: 'customerFullname', width: 50},
                        {title: '手机号码', align: 'center', field: 'customerMobile', width: 70},
                        {
                            title: '图片', align: 'center', field: 'photoPath', width: 50,
                            formatter: function (val) {
                                if (val == null) {
                                    return '';
                                }
                                var html = '<a href="${staticUrl}URL" target="view_window"><img src="${staticUrl}URL" height="34" width="34"/></a>';
                                return html.replace(/URL/g, val);
                            }
                        },
                        {title: '反馈内容', align: 'center', field: 'content', width: 200},
                        {title: '创建时间', align: 'center', field: 'createTime', width: 80},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 60,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.Feedback:remove'>
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
            var content = $('#content').val();
            var partnerId = $('#partner_id').combobox('getValue');
            datagrid.datagrid('options').queryParams = {
                partnerId: partnerId,
                mobile: mobile,
                content: content
            };
            datagrid.datagrid('load');
        }


        function view(id) {
            App.dialog.show({
                css: 'width:480px;height:260px;',
                title: '查看',
                href: "${contextPath}/security/basic/feedback/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post('${contextPath}/security/basic/feedback/delete.htm', {
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
                            <td>商户：</td>
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
                                        query();
                                    }"
                                />&nbsp;&nbsp;&nbsp;&nbsp;
                            </td>
                            <td align="right">手机号码：</td>
                            <td><input type="text" class="text" id="mobile"/>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                            <td align="right" width="70">反馈内容：</td>
                            <td><input type="text" class="text" id="content"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                        <h3>意见反馈</h3>
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
