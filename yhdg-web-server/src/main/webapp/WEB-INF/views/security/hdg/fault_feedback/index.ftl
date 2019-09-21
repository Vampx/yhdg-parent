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
                url: "${contextPath}/security/hdg/fault_feedback/page.htm?defaultFaultType=${defaultFaultType}&defaultHandleStatus=${defaultHandleStatus}",
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
                            title: '故障名称',
                            align: 'center',
                            field: 'faultName',
                            width: 40
                        },
                        {
                            title: '故障内容',
                            align: 'center',
                            field: 'content',
                            width: 60
                        },
                        {
                            title: '换电柜编号',
                            align: 'center',
                            field: 'cabinetId',
                            width: 60
                        },
                        {title: '换电柜名称', align: 'center', field: 'cabinetName', width: 60},
                        {title: '电池编号', align: 'center', field: 'batteryId', width: 60},
                        {title: '故障类型', align: 'center', field: 'faultTypeName', width: 60},
                        {
                            title: '客户姓名',
                            align: 'center',
                            field: 'customerName',
                            width: 40
                        },
                        {
                            title: '客户手机',
                            align: 'center',
                            field: 'customerMobile',
                            width: 40
                        },
                        {
                            title: '处理状态',
                            align: 'center',
                            field: 'handleStatusName',
                            width: 30
                        },
                        {
                            title: '处理人',
                            align: 'center',
                            field: 'handlerName',
                            width: 40
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 40
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 60,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='hdg.FaultFeedback:view'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='hdg.FaultFeedback:edit'>
                                    html += ' <a href="javascript:edit(ID)">修改</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='hdg.FaultFeedback:remove'>
                                    html += ' <a href="javascript:remove(ID)">删除</a>';
                                </@app.has_oper>
                                return html.replace(/ID/g, "'" + row.id + "'");
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
            var tree = $('#category_tree');
            tree.tree('reload');
        }


        function query() {
            var datagrid = $('#page_table');

            var customerName = $('#customer_name').val();
            var content = $('#content').val();

            var handleStatus = $('#handle_status').val();
            var faultType = $('#fault_type').val();

            datagrid.datagrid('options').queryParams = {
                agentId: $('#agent_id').combotree('getValue'),
                customerName: customerName,
                content: content,
                faultType: faultType,
                handleStatus: handleStatus
            };
            datagrid.datagrid('load');
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:752px;height:510px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/hdg/fault_feedback/edit.htm?id=" + id,
                event: {
                    onClose: function () {
                        reload();
                    }
                }
            });
        }

        function view(id) {
            App.dialog.show({
                css: 'width:752px;height:510px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/fault_feedback/view.htm?id=" + id
            });
        }

        function remove(id) {
            $.messager.confirm("提示信息", "确认删除?", function (ok) {
                if (ok) {
                    $.post("${contextPath}/security/hdg/fault_feedback/delete.htm?id=" + id, function (json) {
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
                            <td align="right">运营商：</td>
                            <td>
                                <input name="agentId" id="agent_id" class="easyui-combotree" editable="true"
                                       style="width: 200px;height: 28px;"
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
                            <td align="right" width="70">客户姓名：</td>
                            <td><input type="text" class="text" id="customer_name"/></td>
                            <td align="right" width="70">故障内容：</td>
                            <td><input type="text" class="text" id="content"/></td>
                            <td align="right" width="70">处理状态：</td>
                            <td>
                                <select style="width:80px;" id="handle_status">
                                    <option value="0">所有</option>
                                    <#list handleStatusList as status>
                                        <option value="${status.getValue()}" <#if defaultHandleStatus=status.getValue()> selected</#if>>${(status.getName())!''}</option>
                                    </#list>
                                </select>
                            </td>
                            <td align="right" width="70">故障类型：</td>
                            <td>
                                <select style="width:80px;" id="fault_type">
                                    <option value="0">所有</option>
                                    <#list typeList as type>
                                        <option value="${type.getValue()}" <#if defaultFaultType=type.getValue()> selected</#if>>${(type.getName())!''}</option>
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
                        <h3>故障反馈列表</h3>
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