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
                        return /^1[3|4|5|8][0-9]\d{4,8}$/.test(value);
                    }

                },
                message: '请正确输入手机号码'
            },
            phone: {
                validator: function (value) {
                    if (value != "") {
                        return /^((\(\d{3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}$/.test(value);
                    }
                },
                message: '请正确输入手机号码'
            },
            unique: {
                validator: function (value, param) {
                    var success;
                    $.ajax({
                        cache: false,
                        async: false,
                        type: 'POST',
                        url: '${contextPath}/security/basic/user/unique.htm',
                        data: {
                            loginName: value,
                            id: param.length ? param[0] : ''
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
                message: '用户名重复'
            }
        });

        $(function () {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/basic/agent_user/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            title: '所属运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
                        {
                            title: '登录名称',
                            align: 'center',
                            field: 'loginName',
                            width: 60
                        },
                        {title: '手机', align: 'center', field: 'mobile', width: 60},
                        {title: '角色', align: 'center', field: 'roleName', width: 60},
                        {
                            title: '是否核心管理员',
                            align: 'center',
                            field: 'isAdmin',
                            width: 60,
                            formatter: function(val) {
                                if(val == 1) {
                                    return '<a style="color: #00FF00;">是</a>';
                                }else{
                                    return '<a style="color: #ff0000;">否</a>';
                                }
                            }
                        },
                        {title: '类型', align: 'center', field: 'isProtected', width: 60,
                            formatter: function(val, row) {
                                if(val == 1){
                                    return "管理员";
                                }else{
                                    return "普通账户";
                                }
                            }
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 100,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.AgentUser:view'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.AgentUser:edit'>
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.AgentUser:delete'>
                                if(row.isProtected == 0 && row.isAdmin == 0){
                                    html += ' <a href="javascript:remove(ID)">删除</a>';
                                }
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

            var loginName = $('#loginName').val();
            var mobile = $('#mobile').val();

            datagrid.datagrid('options').queryParams = {
                loginName: loginName,
                mobile: mobile
            };

            datagrid.datagrid('load');
        }

        function add() {
            $.ajax({
                url: "${contextPath}/security/basic/agent_user/add_precondition.htm",
                success: function (text) {
                    var json = $.evalJSON(text);
                    <@app.json_jump/>
                    if (!json.success) {
                        $.messager.alert('提示信息', json.message, 'info');
                        return false;
                    }else{
                        App.dialog.show({
                            css: 'width:580px;height:305px;overflow:visible;',
                            title: '新建',
                            href: "${contextPath}/security/basic/agent_user/add.htm",
                            event: {
                                onClose: function () {
                                    reload();
                                }
                            }
                        });
                    }
                }
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:580px;height:333px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/agent_user/edit.htm?id=" + id,
                event: {
                    onClose: function () {
                        var datagrid = $('#page_table');
                        datagrid.datagrid('reload');
                    },
                    onLoad: function () {
                    }
                }
            });
        }

        function view(id) {
            App.dialog.show({
                css: 'width:590px;height:253px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/agent_user/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/agent_user/delete.htm?id=" + id, function (json) {
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
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right" width="80">登录名称：</td>
                            <td><input type="text" class="text" id="loginName"/></td>
                            <td align="right" width="60">手机：</td>
                            <td><input type="text" class="text" id="mobile"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='basic.AgentUser:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>运营商账户</h3>
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