<@app.html>
    <@app.head>
    <script>
        $(function(){
            $('#page_table').datagrid({
                fit:true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/basic/day_balance_record/page.htm?agentId=${Session['SESSION_KEY_USER'].agentId}",
                fitColumns: true,
                pageSize: 50,
                pageList: [50,100],
                idField: 'id',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                columns: [
                    [
                        {
                            title: 'checkbox', checkbox: true
                        },
                        {
                            title: '结算日期',
                            align: 'center',
                            field: 'balanceDate',
                            width: 40
                        },
                        {
                            title: '类型',
                            align: 'center',
                            field: 'bizType',
                            width: 40,
                            formatter: function(val){
                                <#list BizTypeEnum as e>
                                    if(${e.getValue()} == val)
                                    return '${e.getName()}';
                                </#list>
                            }
                        },
                        {
                            title: '运营商名称',
                            align: 'center',
                            field: 'agentName',
                            width: 40
                        },
                        {title: '总收入', align: 'center', field: 'money', width: 40,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {title: '充电收入', align: 'center', field: 'income', width: 40,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'status',
                            width: 50,
                            formatter: function(val){
                                <#list StatusEnum as e>
                                    if(${e.getValue()} == val)
                                    return '${e.getName()}';
                                </#list>
                            }
                        },
                        {
                            title: '订单号',
                            align: 'center',
                            field: 'orderId',
                            width: 40,
                            formatter: function(val, row) {
                                if(val) {
                                    var html = '<a style="color: red;" href="javascript:viewOrder(\'ORDER_ID\')">'+ val + '</a>';
                                    return html.replace(/ORDER_ID/g, row.orderId);
                                }
                            }
                        },
                        {
                            title: '处理时间',
                            align: 'center',
                            field: 'handleTime',
                            width: 50
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 40,
                            formatter: function(val, row) {
                                var html = '';
                                <#--<@app.has_oper perm_code='5_1_1_4'>-->
                                    html += '<a href="javascript:view(ID)">查看明细</a>';
                                <#--</@app.has_oper>-->
                                return html.replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                onLoadSuccess:function(){
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                }
            });
        });

        function reload(){
            var datagrid=$('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');

            var agentId = null;
            var status = $('#status').val();
            var bizType = $('#biz_type').val();
//            var agent = $('#agent').val();
//            var demo = $('#demo').val();
//            var agentIds = $('#agent_id').combotree("getValue");
//            if(agentIds == "" || agentIds == null){
//                agentId = agent;
//            }else{
//                agentId = $('#agent_id').combotree("getValue");
//            }

            var queryParams = {
//                agentId: agentId,
                bizType: bizType,
                status: status
            };



            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }


        function viewOrder(id){
            App.dialog.show({
                css:'width:1200px;height:570px;overflow:visible;',
                title:'查看',
                href:"${contextPath}/security/basic/balance_transfer_order/view.htm?id="+id
            });
        }


        function view(id){
            App.dialog.show({
                css:'width:860px;height:580px;overflow:visible;',
                title:'查看',
                href:"${contextPath}/security/basic/day_balance_record/view.htm?id="+id
            });
        }

         function edit_status(){
             $.messager.confirm('提示信息', '确认提交?', function(ok){
                 if(ok){
                     var ids = [];
                     var rows= $('#page_table').datagrid('getChecked');
                     for(var i = 0; i < rows.length; i++){
                         ids.push(rows[i].id);
                     }
                     if(ids.length == 0){
                         alert('你没有选择任何内容');
                     } else{
                         $.post('${contextPath}/security/basic/day_balance_record/confirm.htm', {
                            ids: ids
                        }, function (json) {
                            if (json.success) {
                                $.messager.alert('info', '操作成功', 'info');
                                reload();
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
                    }
                }
            });
        }


        function edit_status_offline(){
            $.messager.confirm('提示信息', '确认提交?', function(ok){
                if(ok){
                    var ids = [];
                    var rows= $('#page_table').datagrid('getChecked');
                    for(var i = 0; i < rows.length; i++){
                        ids.push(rows[i].id);
                    }
                    if(ids.length == 0){
                        alert('你没有选择任何内容');
                    } else{
                        $.post('${contextPath}/security/basic/day_balance_record/confirmStatus.htm', {
                            ids: ids
                        }, function (json) {
                            if (json.success) {
                                $.messager.alert('info', '操作成功', 'info');
                                reload();
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
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
                            <#--<#if Session['SESSION_KEY_USER'].agentId == 0>-->
                                <#--<td width="60"  align="right">运营商：</td>-->
                                <#--<td>-->
                                    <#--<input type="text" name="agentId" id="agent_id" class="easyui-combotree" editable="false"  style=" height: 28px;width: 200px"-->
                                           <#--data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',-->
                                            <#--method:'get',-->
                                            <#--valueField:'id',-->
                                            <#--textField:'text',-->
                                            <#--editable:false,-->
                                            <#--multiple:false,-->
                                            <#--panelHeight:'500',-->
                                            <#--onClick: function(node) {-->
                                                 <#--$('#agent').val(node.id);-->
                                                <#--$('#demo').val('');-->
                                            <#--}-->
                                        <#--"-->
                                            <#-->&nbsp;-->
                                <#--</td>-->
                            <#--</#if>-->
                        <td align="right" width="60">类型：</td>
                        <td>
                            <select style="width:70px;" id="biz_type">
                                <option value="">所有</option>
                                <#list BizTypeEnum as e>
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
                            <#--<td width="90" align="right">运营商名称：</td>-->
                            <#--<td><input id="demo" type="text" class="text" /></td>-->
                            <#--<td><input  hidden id="agent" /></td>-->
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <#--<@app.has_oper perm_code='5_1_1_2'>-->
                                <#--<button class="btn btn_green" onclick="edit_status()">确认(公众号)</button>-->
                            <#--</@app.has_oper>-->
                            <#--<@app.has_oper perm_code='5_1_1_3'>-->
                                <#--<button class="btn btn_green" onclick="edit_status_offline()">确认(线下)</button>-->
                            <#--</@app.has_oper>-->
                        </div>
                        <h3>结算记录</h3>
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
