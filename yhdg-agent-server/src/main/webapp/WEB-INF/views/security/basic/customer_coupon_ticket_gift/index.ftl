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
                url: "${contextPath}/security/basic/customer_coupon_ticket_gift/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            title: '赠送类型',
                            align: 'center',
                            field: 'typeName',
                            width: 60
                        },
                        {
                            title: '购买天数',
                            align: 'center',
                            field: 'payCount',
                            width: 60,
                            formatter: function (val) {
                                return (val==""||val==null||val==0)?"----":val;
                            }
                        },
                        {
                            title: '工资日',
                            align: 'center',
                            field: 'wagesDay',
                            width: 60,
                            formatter: function (val) {
                                return (val==""||val==null)?"----":val;
                            }
                        },
                        {
                            title: '有效期(天)',
                            align: 'center',
                            field: 'dayCount',
                            width: 60
                        },
                        {
                            title: '优惠金额(元)',
                            align: 'center',
                            field: 'money',
                            width: 60,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '是否启用',
                            align: 'center',
                            field: 'isActive',
                            width: 60,
                            formatter: function (val, row) {
                                return val ? '是' : '否'
                            }
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 100,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.CustomerCouponTicketGift:view'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.CustomerCouponTicketGift:edit'>
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.CustomerCouponTicketGift:remove'>
                                    if (row.agentId != 0) {
                                        html += ' <a href="javascript:del(ID)">删除</a>';
                                    }
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.CustomerCouponTicketGift:remove'>
                                    if (row.agentId != 0&&row.type==4) {
                                        html += ' <a href="javascript:binDing(ID)">绑定骑手</a>';
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
        })
        ;

        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                agentId: $('#agent_id').combotree('getValue'),
                type: $('#type').val()
            };
            datagrid.datagrid('load');
        }
        function binDing(id) {

            App.dialog.show({
                css: 'width:825px;height:535px;',
                title: '绑定骑手',
                href: "${contextPath}/security/basic/customer_coupon_ticket_gift/binDing.htm?id=" + id,
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

        function edit(id) {
            App.dialog.show({
                css: 'width:360px;height:300px;',
                title: '修改',
                href: "${contextPath}/security/basic/customer_coupon_ticket_gift/edit.htm?id=" + id,
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

        function del(id) {
            $.messager.confirm('提示信息', '确认删除?', function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/customer_coupon_ticket_gift/delete.htm?id=" + id, function (json) {
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
                css: 'width:360px;height:330px;',
                title: '查看',
                href: "${contextPath}/security/basic/customer_coupon_ticket_gift/view.htm?id=" + id,
                event: {
                    onClose: function () {
                    }
                }
            });
        }

        function add() {
            App.dialog.show({
                css: 'width:360px;height:300px;',
                title: '新建',
                href: "${contextPath}/security/basic/customer_coupon_ticket_gift/add.htm",
                event: {
                    onClose: function () {
                        reload();
                    }
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
                            <td align="right" width="80">赠送类型：</td>
                            <td>
                                <select style="width:90px;" id="type">
                                    <option value="">所有</option>
                                    <#list TypeEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='basic.CustomerCouponTicketGift:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>优惠券配置</h3>
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
