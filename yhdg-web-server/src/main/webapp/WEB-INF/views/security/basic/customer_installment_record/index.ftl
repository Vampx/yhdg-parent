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
                url: "${contextPath}/security/basic/customer_installment_record/page.htm?category=1",
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
                            width: 40
                        },
                        /*{
                            title: '电池类型',
                            align: 'center',
                            field: 'batteryTypeName',
                            width: 40
                        },*/
                        {
                            title: '客户姓名',
                            align: 'center',
                            field: 'fullname',
                            width: 40
                        },
                        {
                            title: '手机号',
                            align: 'center',
                            field: 'mobile',
                            width: 40
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'statusName',
                            width: 40
                        },
                        {
                            title: '押金金额（元）',
                            align: 'center',
                            field: 'foregiftMoney',
                            width: 50,
                            formatter: function(val) {
                                return new Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '租金金额（元）',
                            align: 'center',
                            field: 'packetMoney',
                            width: 50,
                            formatter: function(val) {
                                return new Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '手续费金额（元）',
                            align: 'center',
                            field: 'feeMoney',
                            width: 50,
                            formatter: function(val) {
                                return new Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '总金额（元）',
                            align: 'center',
                            field: 'totalMoney',
                            width: 50,
                            formatter: function(val) {
                                return new Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '已支付金额（元）',
                            align: 'center',
                            field: 'paidMoney',
                            width: 50,
                            formatter: function(val) {
                                return new Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 70
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 30,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.CustomerInstallmentRecord:view'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <#if row.status ==1 || row.status ==2>/*1.未支付2.分期中*/
                                <@app.has_oper perm_code='basic.CustomerInstallmentRecord:edit'>
                                    html += '<a href="javascript:edit(ID)">修改</a>';
                                </@app.has_oper>
                                </#if>
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
            var partnerId = $('#partner_id').combobox('getValue');
            var fullname = $('#fullname').val();
            var mobile = $('#mobile').val();
            var agentId = $('#agent_id').combotree('getValue');
            var status = $('#status').val();
            datagrid.datagrid('options').queryParams = {
                agentId: agentId,
                fullname: fullname,
                mobile: mobile,
                partnerId: partnerId,
                status: status
            };
            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:850px;height:520px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/customer_installment_record/view.htm?id=" + id,
            });
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:850px;height:520px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/customer_installment_record/view.htm?id=" + id,
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
                            <td align="right">商户：</td>
                            <td>
                                <input name="partnerId" id="partner_id" style="height: 28px;width: 130px;" class="easyui-combobox"  editable="false"
                                       data-options="url:'${contextPath}/security/basic/partner/list.htm?dummy=${'无'?url}',
                                    method:'get',
                                    valueField:'id',
                                    textField:'partnerName',
                                    editable:false,
                                    multiple:false,
                                    panelHeight:'200',
                                    onSelect: function(node) {
                                    }"
                                />
                            </td>

                            <td align="right"  width="70">运营商：</td>
                            <td>
                                <input id="agent_id" class="easyui-combotree" editable="true" style="width: 130px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
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
                            <td align="right" width="70">姓名：</td>
                            <td><input type="text" class="text" id="fullname"/></td>
                            <td align="right" width="70">手机号：</td>
                            <td><input type="text" class="text" id="mobile"/></td>
                            <td align="right" width="70">状态：</td>
                            <td>
                                <select id="status" style="width: 150px; height: 28px;">
                                    <option value="">所有</option>
                                    <#list StatusEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" style="margin-top: 30px;">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                        <h3>客户分期记录</h3>
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