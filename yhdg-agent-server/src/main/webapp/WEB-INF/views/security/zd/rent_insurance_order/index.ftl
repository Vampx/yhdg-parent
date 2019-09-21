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
                url: "${contextPath}/security/zd/rent_insurance_order/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
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
                            title: '商户',
                            align: 'center',
                            field: 'partnerName',
                            width: 30
                        },
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 30
                        },
                        {
                            title: '姓名',
                            align: 'center',
                            field: 'customerFullname',
                            width: 30
                        },
                        {
                            title: '手机号',
                            align: 'center',
                            field: 'customerMobile',
                            width: 30
                        },
                        {
                            title: '支付方式',
                            align: 'center',
                            field: 'payTypeName',
                            width: 20
                        },
                        {
                            title: '支付金额',
                            align: 'center',
                            field: 'money',
                            width: 20,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) + " 元";
                            }
                        },
                        {
                            title: '电池类型',
                            align: 'center',
                            field: 'batteryTypeName',
                            width: 30
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 30
                        },
                        {
                            title: '开始时间',
                            align: 'center',
                            field: 'beginTime',
                            width: 30
                        },
                        {
                            title: '结束时间',
                            align: 'center',
                            field: 'endTime',
                            width: 30
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 30
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 30,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='zd.RentInsuranceOrder:view'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
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

        function reloadTree() {
            var agentId = $('#agent_id').combotree('getValue');
            var datagrid = $('#page_table');
            datagrid.datagrid('options').queryParams = {
                agentId: agentId
            };
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var customerFullname = $('#customer_fullname').val();
            var customerMobile = $('#customer_mobile').val();
            var status = $('#status').val();
            var queryParams = {
                customerFullname:customerFullname,
                status:status,
                customerMobile:customerMobile
            };

            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:1000px;height:515px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/zd/rent_insurance_order/view.htm?id="+id
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
                            <td align="right" width="70">手机：</td>
                            <td><input type="text" class="text" name="customerMobile" id="customer_mobile" /></td>
                            <td align="right" width="70">姓名：</td>
                            <td><input type="text" class="text" name="customerFullname" id="customer_fullname" /></td>
                            <td align="right" width="70">状态：</td>
                            <td>
                                <select id="status" style="width: 184px; height: 28px;">
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
                        </div>
                        <h3>保险订单</h3>
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

