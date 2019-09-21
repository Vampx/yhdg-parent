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
                url: "${contextPath}/security/basic/agent_foregift/page.htm",
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
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
                        {
                            title: '运营商余额',
                            align: 'center',
                            field: 'balance',
                            width: 40,
                            formatter: function (val,row) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '押金池金额',
                            align: 'center',
                            field: 'foregiftBalance',
                            width: 40,
                            formatter: function (val,row) {
                                if(row.category == 1){
                                    return Number(val / 100).toFixed(2);
                                }else {
                                    return Number(row.zdForegiftBalance / 100).toFixed(2);
                                }
                            }
                        },
                        {
                            title: '押金池剩余金额',
                            align: 'center',
                            field: 'foregiftRemainMoney',
                            width: 40,
                            formatter: function (val,row) {
                                if(row.category == 1){
                                    return Number(val / 100).toFixed(2);
                                }else {
                                    return Number(row.zdForegiftRemainMoney / 100).toFixed(2);
                                }
                            }
                        },
                        {
                            title: '押金池可转余额',
                            align: 'center',
                            field: 'hdWithdrawMoney',
                            width: 40,
                            formatter: function (val,row) {
                                if(row.category == 1){
                                    return Number(val / 100).toFixed(2);
                                }else {
                                    return Number(row.zdWithdrawMoney / 100).toFixed(2);
                                }
                            }
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'edit',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.AgentForegift:view'>
                                    html += '<a href="javascript:view(ID,CATEGORY)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.AgentForegift:edit'>
                                    if(row.category == 1){
                                        html += ' <a href="javascript:exchangeWithdrawal(ID)">换电转余额</a>';
                                    }else {
                                        html += ' <a href="javascript:rentWithdrawal(ID)">租电转余额</a>';
                                    }
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id).replace(/CATEGORY/g, row.category);
                            }
                        }
                    ]
                ],
                queryParams: {
                    parentId: <#if Session['SESSION_KEY_USER'].rootAgentId != 0>${Session['SESSION_KEY_USER'].rootAgentId}<#else>''</#if>
                },
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
            var agentId = $('#agent_id').combotree('getValue');
            var category = $('#category').val();

            datagrid.datagrid('options').queryParams = {
                partnerId: partnerId,
                id: agentId,
                category: category
            };

            datagrid.datagrid('load');
        }


        function view(id,category) {
            App.dialog.show({
                css: 'width:850px;height:510px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/agent_foregift/view.htm?id=" + id + "&category=" + category
            });
        }

        function exchangeWithdrawal(id) {
            App.dialog.show({
                css: 'width:433px;height:235px;overflow:visible;',
                title: '换电转余额',
                href: "${contextPath}/security/basic/agent_foregift/exchange_withdrawal.htm?id=" + id,
                event: {
                    onClose: function() {
                        var datagrid = $('#page_table');
                        datagrid.datagrid('reload');
                    },
                    onLoad: function() {
                    }
                }
            });
        }

        function rentWithdrawal(id) {
            App.dialog.show({
                css: 'width:433px;height:235px;overflow:visible;',
                title: '租电转余额',
                href: "${contextPath}/security/basic/agent_foregift/rent_withdrawal.htm?id=" + id,
                event: {
                    onClose: function() {
                        var datagrid = $('#page_table');
                        datagrid.datagrid('reload');
                    },
                    onLoad: function() {
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
                                <td>商户：</td>
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
                                        query();
                                    }"
                                    />&nbsp;&nbsp;&nbsp;&nbsp;
                                </td>
                                <td align="right">运营商：</td>
                                <td >
                                    <input id="agent_id" class="easyui-combotree" editable="true" style="width: 200px;height: 28px;"
                                           value = "${(agentId)!''}"
                                           data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}',
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
                                <td align="right" width="70">业务类型：</td>
                                <td>
                                    <select style="width:80px;" id="category">
                                        <#list Category as e>
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
                            <h3>运营商押金池信息</h3>
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