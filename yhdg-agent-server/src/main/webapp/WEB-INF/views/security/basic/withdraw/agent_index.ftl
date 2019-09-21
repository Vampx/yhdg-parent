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
                url: "${contextPath}/security/basic/withdraw/page.htm",
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
                            title: '提现机构',
                            align: 'center',
                            field: 'typeName',
                            width: 35
                        },
                        {
                            title: '机构名称',
                            align: 'center',
                            field: 'organizeName',
                            width: 60
                        },
                        {
                            title: '所属运营商',
                            align: 'center',
                            field: 'belongAgentName',
                            width: 60
                        },
                        {
                            title: '所属商户',
                            align: 'center',
                            field: 'belongPartnerName',
                            width: 60
                        },
                        {
                            title: '收款账户',
                            align: 'center',
                            field: 'accountTypeName',
                            width: 35
                        },
                        {
                            title: '收款账户名',
                            align: 'center',
                            field: 'accountName',
                            width: 35
                        },
                        {
                            title: '余额',
                            align: 'center',
                            field: 'balance',
                            width: 35,
                            formatter: function (val,row) {
                                var html = '<a href="javascript:flow(\'ID\')">'+val/100+'元</a>';
                                return html.replace(/ID/g, row.id);
                            }
                        },
                        {
                            title: '申请金额',
                            align: 'center',
                            field: 'money',
                            width: 35,
                            formatter: function (val) {
                                return val/100+'元';
                            }
                        },
                        {
                            title: '实提金额',
                            align: 'center',
                            field: 'realMoney',
                            width: 35,
                            formatter: function (val) {
                                return val/100+'元';
                            }
                        },
                        {
                            title: '手续费',
                            align: 'center',
                            field: 'serviceMoney',
                            width: 30,
                            formatter: function (val) {
                                return val/100+'元';
                            }
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'status',
                            width: 35,
                            formatter: function(val){
                                <#list StatusEnum as e>
                                if(${e.getValue()} == val){
                                    return '${e.getName()}';
                                }
                                </#list>
                            }
                        },
                        {
                            title: '申请时间',
                            align: 'center',
                            field: 'createTime',
                            width: 60
                        },
                        {
                            title: '处理时间',
                            align: 'center',
                            field: 'handleTime',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 35,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.Withdraw:view'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.Withdraw:audit'>
                                    if (row.status == 1) {
                                        html += '<a href="javascript:audit(\'ID\')" style="margin-left: 10px;">审核</a>';
                                    }
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.Withdraw:reset'>
                                    if (row.status == 5) {
                                        html += '<a href="javascript:reset(\'ID\')" style="margin-left: 10px;">重置</a>';
                                        html += '<a href="javascript:cancel(\'ID\', 3)" style="margin-left: 10px;">取消</a>';
                                    }
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

            // var partnerId = $('[name=partnerId]').val();
            var type = $('[name=type]').val();

            var queryParams = {
                // partnerId: partnerId,
                type: type
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }
        function audit(id) {
            App.dialog.show({
                css: 'width:320px;height:230px;overflow:visible;',
                title: '审核',
                href: "${contextPath}/security/basic/withdraw/audit.htm?id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    },
                    onLoad: function() {
                    }
                }
            });
        }
        function reset(id) {
            App.dialog.show({
                css: 'width:350px;height:220px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/withdraw/reset.htm?id=" + id,
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
                css: 'width:886px;height:635px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/withdraw/view.htm?id=" + id
            });
        }
        function flow(id) {
            App.dialog.show({
                css: 'width:886px;height:635px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/withdraw/flow.htm?id=" + id
            });
        }
        function cancel(id, status) {
            $.messager.confirm('提示信息', '确认取消提现?', function (ok) {
                if (ok) {
                    var values = {
                        id: id,
                        status: status,
                        auditMemo: '后台取消'
                    };
                    $.ajax({
                        cache: false,
                        async: false,
                        type: 'POST',
                        url: '${contextPath}/security/basic/withdraw/cancel.htm',
                        dataType: 'json',
                        data: values,
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
                                <td align="right">提现机构：</td>
                                <td>
                                    <select class="easyui-combobox" name="type" style="width: 182px; height: 30px;">
                                        <option value="">全部</option>
                                        <option value="1">客户</option>
                                        <option value="3">门店</option>
                                    </select>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="panel grid_wrap">
                        <div class="toolbar clearfix">
                            <div class="float_right">
                            </div>
                            <h3>申请列表</h3>
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