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
                url: "${contextPath}/security/basic/fw_push_message_template/page.htm",
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
//                        {title: '公众号', align: 'center', field: 'mpCode', width: 80},
                        {title: '生活号', align: 'center', field: 'fwCode', width: 80},
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
                                <#--<@app.has_oper perm_code='7_4_1_3'>-->
//                                    html += '<a href="javascript:view(ID, ALIPAYFW_ID)">查看</a>';
                                <#--</@app.has_oper>-->
                                <#--<@app.has_oper perm_code='7_4_1_4'>-->
                                    html += ' <a href="javascript:edit(ID, ALIPAYFW_ID)">修改</a>'
                                <#--</@app.has_oper>-->
                                return html.replace(/ALIPAYFW_ID/g, row.alipayfwId).replace(/ID/g, row.id);
                            }
                        }
                    ]
                ],
                onLoadSuccess:function() {
                    $('#page_table').datagrid('clearChecked');
                    $('#page_table').datagrid('clearSelections');
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
            var alipayfwId = $('#alipayfw_id').combobox('getValue');
            datagrid.datagrid('options').queryParams = {
                templateName : templateName,
                alipayfwId:alipayfwId
            };

            datagrid.datagrid('load');
        }

        function edit(id, alipayfwId) {
            App.dialog.show({
                css: 'width:1024px;height:605px;',
                title: '修改',
                href: "${contextPath}/security/basic/fw_push_message_template/edit.htm?alipayfwId="+alipayfwId+"&id=" + id,
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
                href: "${contextPath}/security/basic/fw_push_message_template/add.htm",
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

        function view(id, alipayfwId) {
            App.dialog.show({
                css: 'width:752px;height:510px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/fw_push_message_template/view.htm?alipayfwId="+alipayfwId+"&id=" + id
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
                            <td>生活号：</td>
                            <td>
                                <input name="alipayfwId" id="alipayfw_id" style="height: 28px;width: 130px;" class="easyui-combobox"  editable="false"
                                       data-options="url:'${contextPath}/security/basic/alipayfw/list.htm',
                                        method:'get',
                                        valueField:'id',
                                        textField:'appName',
                                        editable:false,
                                        multiple:false,
                                        panelHeight:'200',
                                        onSelect: function(node) {
                                            query();
                                        }"
                                />
                            </td>
                            <td align="right" width="70px">模板名称：</td>
                            <td><input type="text" class="text" id="template_name"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <#--<@app.has_oper perm_code='7_4_1_2'>-->
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