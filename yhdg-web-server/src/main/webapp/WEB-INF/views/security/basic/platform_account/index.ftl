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
                url: "${contextPath}/security/basic/platform_account/page.htm",
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
                            title: 'ID',
                            align: 'center',
                            field: 'id',
                            width: 40
                        },
                        {
                            title: '商户名称',
                            align: 'center',
                            field: 'partnerName',
                            width: 60
                        },
                        {
                            title: '余额',
                            align: 'center',
                            field: 'balance',
                            width: 60,
                            formatter:function (val) {
                                return Number(val / 100).toFixed(2) + "元";
                            }
                        },
                        {
                            title: '创建时间',
                            align: 'center',
                            field: 'createTime',
                            width: 60
                        },
                        {
                            title: '操作',
                            align: 'center',
                            field: 'action',
                            width: 60,
                            formatter: function(val, row) {
                                var html = '';
                                <@app.has_oper perm_code='basic.PlatformAccount:view'>
                                    html += '<a href="javascript:view(ID)">查看</a>';
                                </@app.has_oper>
                                <@app.has_oper perm_code='basic.PlatformAccount:withdraw'>
                                    if (row.balance > 0) {
                                        html += '<a href="javascript:withdraw(ID)" style="padding-left: 10px">提现</a>';
                                    }
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
            var partnerName = $('#partnerName').val();
            var queryParams = {
                partnerName: partnerName
            };
            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
        }
        function view(id) {
            App.dialog.show({
                css: 'width:886px;height:630px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/basic/platform_account/view.htm?id=" + id
            });
        }
        function withdraw(id) {
            App.dialog.show({
                css: 'width:550px;height:300px;overflow:visible;',
                title: '提现',
                href: "${contextPath}/security/basic/withdraw/add.htm?partnerId="+id,
                event: {
                    onClose: function() {
                        reload();
                    },
                    onLoad: function() {
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
                                <td align="right" >商户名称：</td>
                                <td><input type="text" class="text" id="partnerName"/></td>
                            </tr>
                        </table>
                    </div>
                    <div class="panel grid_wrap">
                        <div class="toolbar clearfix">
                            <div class="float_right">
                            </div>
                            <h3>平台账户信息</h3>
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