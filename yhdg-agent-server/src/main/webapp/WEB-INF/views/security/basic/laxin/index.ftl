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
                        return /^1\d{10}$/.test(value);
                    }

                },
                message: '请正确输入手机号码'
            }
        });
        $(function() {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/basic/laxin/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                        { field: 'checkbox', checkbox: true },
                        {
                            title: 'ID',
                            align: 'center',
                            field: 'id',
                            width: 40
                        },
                        {
                            title: '手机号',
                            align: 'center',
                            field: 'mobile',
                            width: 60
                        },
                        {
                            title: '佣金方式',
                            align: 'center',
                            field: 'incomeTypeName',
                            width: 60
                        },
                        {
                            title: '按次金额',
                            align: 'center',
                            field: 'laxinMoney',
                            width: 60,
                            formatter: function(val, row) {
                                return new Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '客户优惠券/天数',
                            align: 'center',
                            field: 'ticketMoney',
                            width: 60,
                            formatter: function(val, row) {
                                return new Number(val / 100).toFixed(2) + "/" + row.ticketDayCount;
                            }
                        },
                        {
                            title: '按月收入/过期时间(月)',
                            align: 'center',
                            field: 'packetPeriodMoney',
                            width: 120,
                            formatter: function(val, row) {
                                return new Number(val / 100).toFixed(2) + "/" + row.packetPeriodMonth;
                            }
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'isActive',
                            width: 60,
                            formatter: function(val, row) {
                                if (val == 0) {
                                    return '禁用';
                                } else if (val == 1) {
                                    return '启用';
                                }
                            }
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'edit',
                            width: 120,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.Laxin:view'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.Laxin:edit'>
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.Laxin:delete'>
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

            var mobile = $('#mobile').val();

            var queryParams = {
                mobile: mobile
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }
        function add() {
            App.dialog.show({
                css: 'width:550px;height:540px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/basic/laxin/add.htm",
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }
        function edit(id) {
            App.dialog.show({
                css: 'width:550px;height:560px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/laxin/edit.htm?id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    },
                    onLoad: function() {
                    }
                }
            });
        }
        function view(id) {
            App.dialog.show({
                css: 'width:550px;height:500px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/laxin/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function(ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/laxin/delete.htm?id=" + id, function (json) {
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
                                <td align="right" >手机号：</td>
                                <td><input type="text" class="text" id="mobile"/></td>
                            </tr>
                        </table>
                    </div>
                    <div class="panel grid_wrap">
                        <div class="toolbar clearfix">
                            <div class="float_right">
                                <@app.has_oper perm_code='basic.Laxin:add'>
                                    <button class="btn btn_green" onclick="add()">新建</button>
                                </@app.has_oper>
                            </div>
                            <h3>拉新人员信息</h3>
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