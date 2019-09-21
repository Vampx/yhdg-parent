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
                url: "${contextPath}/security/basic/customer_coupon_ticket/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                        {
                            title: '优惠券名称',
                            align: 'center',
                            field: 'ticketName',
                            width: 40
                        },
                        {
                            title: '优惠券类型',
                            align: 'center',
                            field: 'ticketTypeName',
                            width: 60
                        },
                        {
                            title: '赠送类型',
                            align: 'center',
                            field: 'giveTypeName',
                            width: 60
                        },
                        {
                            title: '金额(元)',
                            align: 'center',
                            field: 'money',
                            width: 40,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '客户手机',
                            align: 'center',
                            field: 'customerMobile',
                            width: 40
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 40
                        },
                        {
                            title: '开始时间',
                            align: 'center',
                            field: 'beginTime',
                            width: 60
                        },
                        {
                            title: '失效时间',
                            align: 'center',
                            field: 'expireTime',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 40,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.CustomerCouponTicket:view'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.CustomerCouponTicket:delete'>
                                    if (row.status != ${usedStatus}) {
                                        html += ' <a href="javascript:remove(\'ID\')">删除</a>';
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
            var ticketName = $('#ticket_name').val();
            var status = $('#status').val();
            var queryParams = {
                ticketName: ticketName,
                status: status,
                customerMobile :$('#customer_mobile').val(),
                ticketType: $('#ticket_type').val()
            };

            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                css: 'width:456px;height:423px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/basic/customer_coupon_ticket/add.htm",
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function view(id) {
            App.dialog.show({
                css: 'width:456px;height:503px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/customer_coupon_ticket/view.htm?id=" + id

            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:456px;height:455px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/customer_coupon_ticket/edit.htm?id=" + id,
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function remove(id) {
            $.messager.confirm("提示信息", "确认删除?", function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/customer_coupon_ticket/delete.htm?id=" + id, function (json) {
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
                            <td width="80" align="right">优惠券名称：</td>
                            <td><input type="text" class="text" id="ticket_name"/></td>
                            <td width="80" align="right">客户手机：</td>
                            <td><input type="text" class="text" id="customer_mobile"/></td>
                            <td align="right" width="80">优惠券类型：</td>
                            <td>
                                <select style="width:90px;" id="ticket_type">
                                    <option value="">所有</option>
                                    <#list TicketTypeEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right" width="60">状态：</td>
                            <td>
                                <select style="width:70px;" id="status">
                                    <option value="">所有</option>
                                    <#list StatusEnum as e>
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
                            <@app.has_oper perm_code='basic.CustomerCouponTicket:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>优惠券订单</h3>
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

