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
                url: "${contextPath}/security/hdg/cabinet_install_record/page.htm",
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
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
                        {
                            title: '换电柜编号',
                            align: 'center',
                            field: 'cabinetId',
                            width: 50
                        },
                        {
                            title: '换电柜名称',
                            align: 'center',
                            field: 'cabinetName',
                            width: 60
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'status',
                            width: 40,
                            formatter: function (val) {
                                if (val == 1) {
                                    return '待审核';
                                } else if(val == 2) {
                                    return '审核通过';
                                } else if(val == 3) {
                                    return '审核未通过';
                                }
                            }
                        },
                        {
                            title: '申请时间',
                            align: 'center',
                            field: 'createTime',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 60,
                            formatter: function (val, row) {
                                var html = '';
                                <@app.has_oper perm_code='hdg.CabinetInstallRecord:view'>
                                    html += '<a href="javascript:view(\'ID\')">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='hdg.CabinetInstallRecord:audit'>
                                if (row.status == 1){
                                    html += ' <a style="color: blue;" href="javascript:edit(\'ID\')">审核</a>';
                                }
                                </@app.has_oper>
                                return html.replace(/ID/g, row.id);
                            }
                        }
                    ]
                ]
            });
        });

        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var tree = $('#cabinet_tree');
            var datagrid = $('#page_table');
            var agentId = $('#agent_id').combotree('getValue');
            var status = $('#status').val();
            datagrid.datagrid('options').queryParams = {
                status: status,
                agentId: agentId
            };

            datagrid.datagrid('load');
        }

        function edit(id) {
            App.dialog.show({
                css: 'width:686px;height:230px;overflow:visible;',
                title: '审核',
                href: "${contextPath}/security/hdg/cabinet_install_record/edit.htm?id=" + id,
                event: {
                    onClose: function () {
                        query();
                    },
                    onLoad: function () {
                    }
                }
            });
        }

        function view(id) {
            App.dialog.show({
                css: 'width:686px;height:350px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/cabinet_install_record/view.htm?id=" + id
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
                <div class="panel search" >
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">运营商：</td>
                            <td >
                                <input id="agent_id" class="easyui-combotree" editable="true" style="width: 200px;height: 28px;"
                                       data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'所有'?url}',
                                method:'get',
                                valueField:'id',
                                textField:'text',
                                editable:true,
                                multiple:false,
                                panelHeight:'200'
                            "
                                >
                            </td>
                            <td align="right">状态：</td>
                            <td>
                                <select style="width:90px;" id="status">
                                    <option value="">所有</option>
                                    <#list statusEnum as e>
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
                        <h3>设备上线记录</h3>
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





