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
                url: "${contextPath}/security/basic/agent/page.htm",
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
                            title: '商户',
                            align: 'center',
                            field: 'partnerName',
                            width: 60
                        },
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
                        {
                            title: '级别', align: 'center', field: 'grade', width:40,
                            formatter: function (val) {
                                <#list gradeEnum as e>
                                    if (${e.getValue()} == val)
                                    return '${e.getName()}';
                                </#list>
                            }
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'isActive',
                            width: 60,
                            formatter: function(val, row) {
                                return val == 1 ? '启用' : '禁用'
                            }
                        },
                        {
                            title: '结算周期',
                            align: 'center',
                            field: 'balanceStatusName',
                            width: 60
                        },
                        {
                            title: '余额',
                            align: 'center',
                            field: 'balance',
                            width: 40,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
//                        {
//                            title: '编号',
//                            align: 'center',
//                            field: 'orderNum',
//                            width: 30,
//                            formatter: function(val, row) {
//                                var html = "<input type='text' style='width: 40px; height:13px; text-align: center;' id='order_num_ID' onblur='updateOrderNum(ID)' onkeypress='if(event.keyCode==13) {updateOrderNum(ID);return false;}' value=" + val+ ">";
//                                return html.replace(/ID/g, row.id);
//                            }
//                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'edit',
                            width: 150,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.Agent:view'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.Agent:edit'>
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.Agent:delete'>
                                    html += ' <a href="javascript:remove(ID)">删除</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
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
            var weixinmpId = $('#weixinmp_id').combobox('getValue');
            var alipayfwId = $('#alipayfw_id').combobox('getValue');
            var balanceStatus = $('#balanceStatus').val();
            var grade = $('#grade').val();

            datagrid.datagrid('options').queryParams = {
                partnerId: partnerId,
                weixinmpId: weixinmpId,
                alipayfwId: alipayfwId,
                id: agentId,
                balanceStatus: balanceStatus,
                grade: grade
            };

            datagrid.datagrid('load');
        }
        function add() {
            App.dialog.show({
                css: 'width:550px;height:640px;overflow:visible;',
                title: '新建',
                href: "${contextPath}/security/basic/agent/add.htm",
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }
        function edit_recharge_agio(id) {
            App.dialog.show({
                css: 'width:306px;height:143px;overflow:visible;',
                title: '修改充值折扣',
                href: "${contextPath}/security/basic/agent/edit_recharge_agio.htm?id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }
        function edit(id) {
            App.dialog.show({
                css: 'width:550px;height:650px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/agent/edit.htm?id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    },
                    onLoad: function() {
                    }
                }
            });
        }
        function frozen_balance(id) {
            App.dialog.show({
                css: 'width:528px;height:500px;overflow:visible;',
                title: '冻结资金',
                href: "${contextPath}/security/basic/agent/frozen_balance.htm?id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    },
                    onLoad: function() {
                    }
                }
            });
        }
        function thaw_balance(id) {
            App.dialog.show({
                css: 'width:528px;height:500px;overflow:visible;',
                title: '解冻资金',
                href: "${contextPath}/security/basic/agent/thaw_balance.htm?id=" + id,
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
                css: 'width:750px;height:510px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/agent/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm('提示信息', '确认删除?', function(ok) {
                if (ok) {
                    $.post("${contextPath}/security/basic/agent/delete.htm?id=" + id, function (json) {
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

        function updateOrderNum(id) {
            $.ajax({
                cache: false,
                async: false,
                type: 'POST',
                url: '${contextPath}/security/basic/agent/update_order_num.htm',
                dataType: 'json',
                data: {id: id, orderNum: $('#order_num_' + id).val()},
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

        <#--function withdrawal(id) {-->
            <#--App.dialog.show({-->
                <#--css: 'width:433px;height:304px;overflow:visible;',-->
                <#--title: '提现',-->
                <#--href: "${contextPath}/security/basic/agent/withdrawal.htm?id=" + id,-->
                <#--event: {-->
                    <#--onClose: function() {-->
                        <#--var datagrid = $('#page_table');-->
                        <#--datagrid.datagrid('reload');-->
                    <#--},-->
                    <#--onLoad: function() {-->
                    <#--}-->
                <#--}-->
            <#--});-->
        <#--}-->
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
                                <td>公众号：</td>
                                <td>
                                    <input name="weixinmpId" id="weixinmp_id" style="height: 28px;width: 130px;" class="easyui-combobox"  editable="false"
                                           data-options="url:'${contextPath}/security/basic/weixinmp/list.htm',
                                            method:'get',
                                            valueField:'id',
                                            textField:'appName',
                                            editable:false,
                                            multiple:false,
                                            panelHeight:'200'
                                            "
                                           <#if weixinmpId?? >value="${weixinmpId}"</#if>
                                    />&nbsp;&nbsp;&nbsp;&nbsp;
                                </td>
                                <td>生活号：</td>
                                <td>
                                    <input name="alipayfwId" id="alipayfw_id" style="height: 28px;width: 130px;" class="easyui-combobox"  editable="false"
                                           data-options="url:'${contextPath}/security/basic/alipayfw/list.htm',
                                        method:'get',
                                        valueField:'id',
                                        textField:'appName',
                                        editable:false,
                                        multiple:false,
                                        panelHeight:'200'
                                        "
                                           <#if alipayfwId?? >value="${alipayfwId}"</#if>
                                    />&nbsp;&nbsp;&nbsp;&nbsp;
                                </td>
                                <td align="right">运营商：</td>
                                <td >
                                    <input id="agent_id" class="easyui-combotree" editable="false" style="width: 200px;height: 28px;"
                                           value = "${(agentId)!''}"
                                           data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:false,
                                multiple:false,
                                panelHeight:'200',
                                onClick: function(node) {

                                }
                            "
                                    >&nbsp;&nbsp;&nbsp;&nbsp;
                                </td>
                                <td align="right" >运营商级别：</td>
                                <td>
                                    <select style="width:70px;" id="grade">
                                        <option value="">所有</option>
                                        <#list gradeEnum as e>
                                            <option value="${e.getValue()}">${e.getName()}</option>
                                        </#list>
                                    </select>
                                </td>
                                <td align="right" style="width:90px;">结算周期：</td>
                                <td>
                                    <select style="width:80px;" id="balanceStatus">
                                        <option value="">所有</option>
                                        <#list BalanceStatusEnum as e>
                                            <option id="${e.getValue()}" value="${e.getValue()}">${e.getName()}</option>
                                        </#list>
                                    </select>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="panel grid_wrap">
                        <div class="toolbar clearfix">
                            <div class="float_right">
                                <@app.has_oper perm_code='basic.Agent:add'>
                                    <button class="btn btn_green" onclick="add()">新建</button>
                                </@app.has_oper>
                            </div>
                            <h3>运营商信息</h3>
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