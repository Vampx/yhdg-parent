<@app.html>
    <@app.head>
    <script>
        $.extend($.fn.validatebox.defaults.rules, {
            mobile: {
                validator: function (value) {
                    if (value != "") {
                        return /^1\d{10}$/.test(value);
                    }

                },
                message: '请正确输入手机号码'
            }
        });


        $(function () {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/basic/customer_whitelist/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                        {title: '运营商', align: 'center', field: 'agentName', width: 40},
                        {
                            title: '手机号码',
                            align: 'center',
                            field: 'mobile',
                            width: 50,
                            formatter: function (val) {
                                return val;
                            }
                        },
                        {title: '备注', align: 'center', field: 'memo', width: 50},
                        {
                            title: '创建时间', align: 'center', field: 'createTime', width: 50
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 30,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.CustomerWhitelist:view'>
                                    html += ' <a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.CustomerWhitelist:edit'>
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.CustomerWhitelist:delete'>
                                    html += ' <a href="javascript:remove(ID)">删除</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
                            }
                        }
                    ]
                ]
            });
        });


        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }


        function view(id) {
            App.dialog.show({
                css: 'width:370px;height:238px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/customer_whitelist/view.htm?id=" + id
            });
        }

        function add() {
            App.dialog.show({
                css: 'width:370px;height:263px;',
                title: '新建',
                href: "${contextPath}/security/basic/customer_whitelist/add.htm",
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:370px;height:263px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/customer_whitelist/edit.htm?id=" + id,
                event: {
                    onClose: function () {
                        reload();
                    },
                    onLoad: function () {
                    }
                }
            });
        }

        function query() {
            var datagrid = $('#page_table');
            var mobile = $('#mobile').val();
            datagrid.datagrid('options').queryParams = {
                mobile: mobile
            };
            datagrid.datagrid('load');
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/customer_whitelist/delete.htm?id=" + id, function (json) {
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
                <div class="panel search">
                    <div class="float_right">

                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0" style="border-collapse:separate; border-spacing: 0px 3px;">
                        <tr>
                            <td align="right" width="50">手机号：</td>
                            <td><input type="text" class="text" id="mobile"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-top: 30px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='basic.CustomerWhitelist:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>客户白名单</h3>
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