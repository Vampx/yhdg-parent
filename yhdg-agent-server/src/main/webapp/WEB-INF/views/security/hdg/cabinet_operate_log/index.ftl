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
                url: "${contextPath}/security/hdg/cabinet_operate_log/page.htm",
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
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 40
                        },
                        {
                            title: '换电柜名称',
                            align: 'center',
                            field: 'cabinetName',
                            width: 40
                        },
                        {
                            title: '日志内容',
                            align: 'center',
                            field: 'content',
                            width: 80,
                            formatter : function(value, row) {
                                return '<span title="'+row.content+'">' + value + '</span>';
                            }
                        },
                        {
                            title: '箱号',
                            align: 'center',
                            field: 'boxNum',
                            width: 30
                        },
                        {
                            title: '操作类型',
                            align: 'center',
                            field: 'operateTypeName',
                            width: 30
                        },
                        {
                            title: '操作人',
                            align: 'center',
                            field: 'operator',
                            width: 30
                        },
                        {
                            title: '日志类型',
                            align: 'center',
                            field: 'operatorTypeName',
                            width: 30
                        },
                        {
                            title: '操作时间',
                            align: 'center',
                            field: 'createTime',
                            width: 50
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 30,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='2_3_5_2'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
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
            var title = $('#title').val();
            var cabinetName = $('#cabinet_name').val();
            var operateType = $('#operate_type').val();
            var operatorType = $('#operator_type').val();
            datagrid.datagrid('options').queryParams = {
                agentId: $('#agent_id').combotree('getValue'),
                title: title,
                cabinetName: cabinetName,
                operateType: operateType,
                operatorType: operatorType

            };

            datagrid.datagrid('load');
        }

        function view(id) {
            App.dialog.show({
                css: 'width:550px;height:360px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/cabinet_operate_log/view.htm?id=" + id
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
                            <td align="right">运营商：</td>
                            <td>
                                <input name="agentId" id="agent_id" class="easyui-combotree" editable="false" style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                                            method:'get',
                                                            valueField:'id',
                                                            textField:'text',
                                                            editable:false,
                                                            multiple:false,
                                                            panelHeight:'200',
                                                            onClick: function(node) {

                                                            }
                                                        "
                                >
                            </td>
                            <td align="right" style="width: 70px">换电柜名称：</td>
                            <td><input type="text" class="text" id="cabinet_name"/></td>
                            <td align="right" width="70">操作类型：</td>
                            <td>
                                <select style="width:70px;" id="operate_type">
                                    <option value="">所有</option>
                                    <#list operateTypeEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right" width="70">日志类型：</td>
                            <td>
                                <select style="width:70px;" id="operator_type">
                                    <option value="">所有</option>
                                    <#list operatorTypeEnum as e>
                                        <option value="${e.getValue()}">${e.getName()}</option>
                                    </#list>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" >
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        </div>
                    <h3>设备操作日志</h3>
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