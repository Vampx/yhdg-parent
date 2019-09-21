<@app.html>
    <@app.head>
    <link href="${app.toolPath}/colpick/css/colpick.css"  rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${app.toolPath}/colpick/js/colpick.js"></script>
    <script>
        function isFlag(val, row) {
            if(val == 1) {
                return "是";
            }
            return "否";
        }

        $(function() {
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/basic/mp_push_message_template/page.htm",
                fitColumns: true,
                pageSize: 20,
                pageList: [20, 50, 100],
                idField: 'id',
                singleSelect: true,
                selectOnCheck: false,
                checkOnSelect: false,
                autoRowHeight: false,
                rowStyler: gridRowStyler,
                columns: [
                    [
                        { title: '模板名称', align: 'center', field: 'templateName', width: 80},
                        {title: '公众号', align: 'center', field: 'mpCode', width: 80},
                        {title: '接收人', align: 'center', field: 'receiver', width: 80},
//                        {title: '生活号', align: 'center', field: 'fwCode', width: 80},
                        {title: '备注', align: 'center', field: 'memo', width: 120},
                        {
                            title: '启用',
                            align: 'center',
                            field: 'isActive',
                            width: 60,
                            formatter:isFlag
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='7_3_1_3'>
                                    html += '<a href="javascript:view(ID, APP_ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='7_3_1_4'>
                                    html += ' <a href="javascript:edit(ID, APP_ID)">修改</a>'
                                </@app.has_oper>
                                return html.replace(/APP_ID/g, row.appId).replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                onLoadSuccess:function() {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
                },
                queryParams: {
                    appId: 0
                }
            });
        })



        function reload() {
            var datagrid = $('#page_table');
            datagrid.datagrid('reload');
        }

        function query() {
            var datagrid = $('#page_table');
            var templateName = $('#template_name').val();
            var appId = $('#app_id').combotree('getValue');

            datagrid.datagrid('options').queryParams = {
                templateName : templateName,
                appId: appId
            };

            datagrid.datagrid('load');
        }

        function edit(id, appId) {
            App.dialog.show({
                css: 'width:752px;height:510px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/mp_push_message_template/edit.htm?appId="+appId+"&id=" + id,
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
        function add() {
            App.dialog.show({
                css: 'width:475px;height:420px;overflow:visible;',
                title: '新增',
                href: "${contextPath}/security/basic/mp_push_message_template/add.htm",
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

        function view(id, appId) {
            App.dialog.show({
                css: 'width:752px;height:510px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/mp_push_message_template/view.htm?appId="+appId+"&id=" + id
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
                            <td align="right">平台：</td>
                            <td>
                                <select name="" id="app_id" style="height: 28px;width: 185px;" class="easyui-combobox"  editable="false"
                                        data-options="url:'${contextPath}/security/basic/agent/self_platform_agent_list.htm',
                                                    method:'get',
                                                    valueField:'id',
                                                    textField:'agentName',
                                                    editable:false,
                                                    multiple:false,
                                                    panelHeight:'200',
                                                    onLoadSuccess:function() {
                                                    },
                                                    onSelect: function(node) {
                                                        query();
                                                    }"
                                >
                                </select>
                                &nbsp;&nbsp;
                            </td>
                            <td align="right">模板名称：</td>
                            <td><input type="text" class="text" id="template_name"/></td>
                            <td align="right" width="70px">接收人：</td>
                            <td><input type="text" class="text" id="receiver"/></td>
                        </tr>

                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <#--<@app.has_oper perm_code='7_3_1_2'>-->
                                <#--<button class="btn btn_green" onclick="add()">新建</button>-->
                            <#--</@app.has_oper>-->
                        </div>
                        <h3>推送消息模板</h3>
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