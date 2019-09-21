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
                url: "${contextPath}/security/basic/batch_mobile_message/page.htm",
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
                            title: '模板名称',
                            align: 'center',
                            field: 'templateName',
                            width: 40
                        },
                        {
                            title: '标题',
                            align: 'center',
                            field: 'title',
                            width: 60
                        },
                        {
                            title: '内容',
                            align: 'center',
                            field: 'content',
                            width: 150
                        },
                        {
                            title: '发送条数',
                            align: 'center',
                            field: 'mobileCount',
                            width: 40
                        },
                        {
                            title: '操作人',
                            align: 'center',
                            field: 'operatorName',
                            width: 40
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 50
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 40,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.BatchMobileMessage:view'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
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


            var templateName = $('#template_name').val();

            var queryParams = {
                templateName: templateName
            };

            datagrid.datagrid('options').queryParams = queryParams;

            datagrid.datagrid('load');
        }

        function add() {
            App.dialog.show({
                options:'maximized:true',
                title: '新建',
                href: "${contextPath}/security/basic/batch_mobile_message/add.htm",
                event: {
                    onClose: function() {
                        reload();
                    }
                }
            });
        }

        function view(id) {
            App.dialog.show({
                css: 'width:786px;height:515px;',
                title: '查看',
                href: "${contextPath}/security/basic/batch_mobile_message/view.htm?id=" + id,
                event: {
                    onClose: function() {
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
                            <td align="right">模板名称：</td>
                            <td><input type="text" class="text" id="template_name"/>&nbsp;&nbsp;</td>

                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='basic.BatchMobileMessage:add'>
                                <button class="btn btn_green" onclick="add()">新建</button>
                            </@app.has_oper>
                        </div>
                        <h3>批量短信发送</h3>
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