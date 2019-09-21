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
                        {
                            title: '公众名称',
                            align: 'center',
                            field: 'weixinmpName',
                            width: 60
                        },

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
                                <#--<@app.has_oper perm_code='basic.MpPushMessageTemplate:view'>-->
                                    <#--html += '<a href="javascript:view(ID, WEIXINMP_ID)">查看</a>';-->
                                <#--</@app.has_oper>-->
                                <@app.has_oper perm_code='basic.MpPushMessageTemplate:edit'>
                                    html += ' <a href="javascript:edit(ID, WEIXINMP_ID)">修改</a>'
                                </@app.has_oper>
                                return html.replace(/WEIXINMP_ID/g, row.weixinmpId).replace(/ID/g, row.id);
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
            var receiver = $('#receiver').val();
            var templateName = $('#template_name').val();
            var weixinmpId = $('#weixinmp_id').combobox('getValue');

            datagrid.datagrid('options').queryParams = {
                receiver: receiver,
                templateName : templateName,
                weixinmpId: weixinmpId
            };

            datagrid.datagrid('load');
        }

        function edit(id, weixinmpId) {
            App.dialog.show({
                css: 'width:1024px;height:605px;',
                title: '模板详情',
                href: "${contextPath}/security/basic/mp_push_message_template/edit.htm?weixinmpId="+ weixinmpId+"&id=" + id,
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

        function view(id, weixinmpId) {
            App.dialog.show({
                css: 'width:752px;height:510px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/mp_push_message_template/view.htm?weixinmpId="+weixinmpId+"&id=" + id
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
                            <td >公众号：</td>
                            <td>
                                <input name="weixinmpId" id="weixinmp_id" style="height: 28px;width: 130px;" class="easyui-combobox"  editable="false"
                                       data-options="url:'${contextPath}/security/basic/weixinmp/list.htm',
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