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
                url: "${contextPath}/security/basic/agent_deposit_order/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 60
                        },
                        {
                            title: '创建人',
                            align: 'center',
                            field: 'operator',
                            width: 60
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'status',
                            width: 60,
                            formatter: function (val) {
                                <#list StatusEnum as e>
                                if (${e.getValue()} == val){
                                    return '${e.getName()}';
                                }
                                </#list>
                            }
                        },
                        {
                            title: '充值类型',
                            align: 'center',
                            field: 'payTypeName',
                            width: 60
                        },
                        {
                            title: '充值金额(元)',
                            align: 'center',
                            field: 'money',
                            width: 60,
                            formatter: function (val) {
                                return val/100;
                            }
                        },
                        {
                            title: '充值时间',
                            align: 'center',
                            field: 'handleTime',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 35,
                            formatter: function(val, row) {
                                <@app.has_oper perm_code='basic.AgentDepositOrder:view'>
                                    var html = '<a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                                // if(row.status==1){
                                //     html += '<a href="javascript:audit(\'ID\')" style="margin-left: 10px;">充值</a>';
                                // }
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
            var status = $('[name=status]').val();

            var queryParams = {
                // partnerId: partnerId,
                status: status
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }
        function add() {
            App.dialog.show({
                css: 'width:320px;height:170px;overflow:visible;',
                title: '充值',
                href: "${contextPath}/security/basic/agent_deposit_order/add.htm",
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
                css: 'width:800px;height:280px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/agent_deposit_order/view.htm?id=" + id
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
                                <td align="right">状态：</td>
                                <td>
                                    <select class="easyui-combobox" name="status" style="width: 182px; height: 30px;">
                                        <option value="">全部</option>
                                        <option value="1">未支付</option>
                                        <option value="2">已支付</option>
                                    </select>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="panel grid_wrap">
                        <div class="toolbar clearfix">
                            <div class="float_right">
                                <@app.has_oper perm_code='basic.AgentDepositOrder:add'>
                                    <button class="btn btn_green" onclick="add()">充值</button>
                                </@app.has_oper>
                            </div>
                            <h3>充值列表</h3>
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