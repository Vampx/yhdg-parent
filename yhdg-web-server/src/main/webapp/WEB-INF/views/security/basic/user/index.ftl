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
                url: "${contextPath}/security/basic/user/page.htm",
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
                            title: '登录名称',
                            align: 'center',
                            field: 'loginName',
                            width: 60
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
                        {title: '角色', align: 'center', field: 'roleName', width: 60},
                        {title: '部门', align: 'center', field: 'deptName', width: 60},
                        {title: '手机', align: 'center', field: 'mobile', width: 60},
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 100,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.User:view'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.User:edit'>
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
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

        function dept() {
            App.dialog.show({
                css: 'width:860px;height:512px;overflow:visible;',
                title: '部门',
                href: "${contextPath}/security/basic/dept/index.htm",
                event: {
                    onClose: function () {
                        var tree = $('#dept_tree');
                        tree.tree('reload')
                    }
                }
            });
        }

        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var tree = $('#dept_tree');
            var datagrid = $('#page_table');

            var loginName = $('#loginName').val();
            var mobile = $('#mobile').val();
            var deptId = tree.tree('getSelected');
            if (deptId) {
                deptId = deptId.id || '';
            } else {
                deptId = '';
            }
            datagrid.datagrid('options').queryParams = {
                loginName: loginName,
                mobile: mobile,
                deptId: deptId,
                agentId: $('#agent_id').combotree('getValue')
            };

            datagrid.datagrid('load');
        }
        function add() {
            $.ajax({
                url: "${contextPath}/security/basic/user/add_precondition.htm",
                success: function (text) {
                    var json = $.evalJSON(text);
                    <@app.json_jump/>
                    if (!json.success) {
                        $.messager.alert('提示信息', json.message, 'info');
                        return false;
                    }else{
                        App.dialog.show({
                            css: 'width:580px;height:380px;overflow:visible;',
                            title: '新建',
                            href: "${contextPath}/security/basic/user/add.htm",
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
                css: 'width:580px;height:380px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/user/edit.htm?id=" + id,
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
                css: 'width:590px;height:380px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/user/view.htm?id=" + id
            });
        }

        function reloadTree() {
            var agentId = $('#agent_id').combotree('getValue');
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId
            };
            datagrid.datagrid('reload');
            var tree = $('#dept_tree');
            tree.tree({
                url: "${contextPath}/security/basic/dept/tree.htm?agentId=" + agentId + "&dummy=${'所有'?url}"
            });
            tree.tree('reload');
        }
    </script>
    </@app.head>
    <@app.body>
        <@app.container>
            <@app.banner/>
        <div class="main">
            <@app.menu/>

            <div class="content">
                <div class="panel ztree_wrap">
                    <div class="ztree">
                        <div class="ztree_head">
                            <a class="a_red" href="javascript:dept()">部门管理</a>

                            <h3>部门</h3>
                        </div>
                        <div class="ztree_body easyui-tree" id="dept_tree"
                             url="${contextPath}/security/basic/dept/tree.htm?dummy=${'所有'?url}" lines="true"
                             data-options="
                                onBeforeSelect: App.tree.toggleSelect,
                                onClick: function(node) {
                                    query();
                                }
                            ">
                        </div>
                    </div>
                </div>
                <div class="panel search" style="margin-left:250px;">
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td width="60" align="right">运营商：</td>
                            <td>
                                <input name="agentId" id="agent_id" class="easyui-combotree" editable="true"
                                       style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'系统'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:true,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {
                                   reloadTree();
                                }
                            "
                                        >
                            </td>
                            <td align="right" width="80">登录名称：</td>
                            <td><input type="text" class="text" id="loginName"/></td>
                            <td align="right" width="60">手机：</td>
                            <td><input type="text" class="text" id="mobile"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-left:250px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='basic.User:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>用户信息</h3>
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