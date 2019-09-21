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
                url: "${contextPath}/security/hdg/distribution_operate/page.htm",
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
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
                        {
                            title: '分层体名称',
                            align: 'center',
                            field: 'distributionName',
                            width: 60
                        },
                        {
                            title: '分层体账号',
                            align: 'center',
                            field: 'loginName',
                            width: 60
                        },
                        {title: '收款人手机', align: 'center', field: 'payPeopleMobile', width: 60},
                        {
                            title: '是否启用',
                            align: 'center',
                            field: 'isActive',
                            width: 60,
                            formatter: function(val) {
                                if(val == 1) {
                                    return '<a style="color: #00FF00;">是</a>';
                                }else{
                                    return '<a style="color: #ff0000;">否</a>';
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
                                <@app.has_oper perm_code='hdg.DistributionOperate:view'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='hdg.DistributionOperate:edit'>
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='hdg.DistributionOperate:remove'>
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

            var distributionName = $('#distribution_name').val();
            var mobile = $('#mobile').val();
            datagrid.datagrid('options').queryParams = {
                distributionName: distributionName,
                mobile: mobile,
                isActive: $('#is_active').val(),
                agentId: $('#agent_id').combotree('getValue')
            };

            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:665px;height:485px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/hdg/distribution_operate/add.htm",
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:665px;height:485px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/hdg/distribution_operate/edit.htm?id=" + id,
                event: {
                    onClose: function () {
                        reload();
                    },
                    onLoad: function () {
                    }
                }
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/hdg/distribution_operate/delete.htm?id=" + id, function (json) {
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
                css: 'width:665px;height:485px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/distribution_operate/view.htm?id=" + id
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
                                }
                            "
                                        >
                            </td>
                            <td align="right" width="80">分层体名称：</td>
                            <td><input type="text" class="text" id="distribution_name"/></td>
                            <td align="right" width="80">状态：</td>
                            <td>
                                <select  id="is_active"
                                         style="width:60px;">
                                    <option value="">所有</option>
                                    <option value="1">启用</option>
                                    <option value="0">禁用</option>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='hdg.DistributionOperate:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>分成体信息</h3>
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