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
                url: "${contextPath}/security/basic/mobile_message_template/page.htm",
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
                            title: '商户',
                            align: 'center',
                            field: 'partnerName',
                            width: 60
                        },
                        {
                            title: '标题',
                            align: 'center',
                            field: 'title',
                            width: 30
                        },
                        {
                            title: '内容',
                            align: 'left',
                            field: 'content',
                            width: 100,
                            formatter : function(value, row) {
                                return '<span title="'+row.content+'">' + value + '</span>';
                            }
                        },
                        {
                            title: '编号',
                            align: 'center',
                            field: 'code',
                            width: 30
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'id',
                            width: 30,
                            formatter: function(val, row) {
                                var html = '';
                                <#--<@app.has_oper perm_code='7_1_2_2'>-->
                                    html += '<a href="javascript:view(PARTNER_ID, ID)">查看</a>';
                                <#--</@app.has_oper>-->
                                <#--<@app.has_oper perm_code='7_1_2_2'>-->
                                    html += ' <a href="javascript:edit(PARTNER_ID, ID)">修改</a>';
                                <#--</@app.has_oper>-->
                                return html.replace(/PARTNER_ID/g, row.partnerId).replace(/ID/g, row.id);
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
            var partnerId = $('#partner_id').combobox('getValue');
            datagrid.datagrid('options').queryParams = {
                title: title,
                partnerId:partnerId
            };
            datagrid.datagrid('load');
        }

        function edit(partnerId, id) {
            App.dialog.show({
                css: 'width:550px;height:336px;overflow:visible;',
                title: '修改',
                href: "${contextPath}/security/basic/mobile_message_template/edit.htm?partnerId=" + partnerId + "&id=" + id,
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
        function view(partnerId, id) {
            App.dialog.show({
                css: 'width:550px;height:336px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/mobile_message_template/view.htm?partnerId=" + partnerId + "&id=" + id,
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
                                />
                            </td>
                            <td align="right">&nbsp;&nbsp;标题：</td>
                            <td><input type="text" class="text" id="title"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>短信模板列表</h3>
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


