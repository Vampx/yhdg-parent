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
                url: "${contextPath}/security/basic/balance_transfer_order/page.htm",
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
                            title: 'ID',
                            align: 'center',
                            field: 'id',
                            width: 60
                        },
                        {
                            title: '运营商名称',
                            align: 'center',
                            field: 'agentName',
                            width: 40
                        },
                        {
                            title: '姓名',
                            align: 'center',
                            field: 'fullName',
                            width: 40
                        },
                        {title: '金额', align: 'center', field: 'money', width: 40,
                            formatter: function(val) {
                                return Number(val / 100).toFixed(2);
                            }
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'status',
                            width: 30,
                            formatter: function(val){
                                <#list StatusEnum as e>
                                    if(${e.getValue()} == val)
                                    return '${e.getName()}';
                                </#list>
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
                                if(row.status == 3) {
                                    <#--<@app.has_oper perm_code='5_1_2_3'>-->
                                        html += '<a href="javascript:reset(\'ID\')">重置</a>&nbsp;';
                                    <#--</@app.has_oper>-->
                                }
                                <#--<@app.has_oper perm_code='5_1_2_4'>-->
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
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
            var agentIds = $('#agent_id').combotree("getValue");
            var agent = $('#agent').val();
            if(agentIds == "" || agentIds == null){
                agentId = agent;
            }else{
                agentId = $('#agent_id').combotree("getValue");
            }
            var queryParams = {
                agentId: agentId,
                status: status
            };



            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }


        function reset(id) {
            App.dialog.show({
                css: 'width:480px;height:252px;',
                title: '重置',
                href: "${contextPath}/security/basic/balance_transfer_order/reset.htm?id=" + id,
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }


        function view(id){
            App.dialog.show({
                css:'width:1200px;height:600px;overflow:visible;',
                title:'查看',
                href:"${contextPath}/security/basic/balance_transfer_order/view.htm?id="+id
            });
        }

        function add() {
            App.dialog.show({
                options:'maximized:true',
                title: '新建',
                href: "${contextPath}/security/basic/balance_transfer_order/add.htm",
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }


        function switchAgent(agentId, descendant) {
            query();
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
                            <td width="60" align="right">运营商：</td>
                            <td>
                                <input type="text" name="agentId" id="agent_id" class="easyui-combotree" editable="false"  style=" height: 28px;width: 200px"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                            method:'get',
                                            valueField:'id',
                                            textField:'text',
                                            editable:false,
                                            multiple:false,
                                            panelHeight:'500',
                                            onClick: function(node) {
                                                   $('#agent').val(node.id);
                                                $('#demo').val('');
                                            }
                                        "
                                >&nbsp;
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
                            <td width="90" align="right">运营商名称：</td>
                            <td><input id="demo" type="text" class="text" /></td>
                            <td><input  hidden id="agent" /></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <#--<@app.has_oper perm_code='5_1_2_2'>-->
                                <button class="btn btn_green" onclick="add()">新建</button>
                            <#--</@app.has_oper>-->
                        </div>
                        <h3>结算转账</h3>
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
