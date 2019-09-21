<@app.html>
    <@app.head>
    <script>
        $(function(){
            $('#page_table').datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/basic/id_card_auth_record/page.htm",
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
                            title: 'ID',
                            align: 'center',
                            field: 'id',
                            width: 50
                        },
                        {
                            title: '运营商',
                            align: 'center',
                            field: 'agentName',
                            width: 60
                        },
                        {
                            title: '客户id',
                            align: 'center',
                            field: 'customerId',
                            width: 40
                        },
                        {
                            title: '姓名',
                            align: 'center',
                            field: 'fullname',
                            width: 50
                        },
                        {
                            title: '手机号',
                            align: 'center',
                            field: 'mobile',
                            width: 60
                        },
                        {
                            title: '金额',
                            align: 'center',
                            field: 'money',
                            width: 40,
                            formatter: function (val) {
                                return Number(val / 100).toFixed(2) + "元";
                            }
                        },
                        {
                            title: '状态',
                            align: 'center',
                            field: 'status',
                            width: 40,
                            formatter: function (val) {
                                if (val == 1) {
                                    return '未支付';
                                } else if (val == 2) {
                                    return '已支付';
                                }
                            }
                        },
                        {
                            title: '支付时间',
                            align: 'center',
                            field: 'payTime',
                            width: 60
                        },
                        {
                            title: '认证时间',
                            align: 'center',
                            field: 'createTime',
                            width: 60
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

            var fullname = $('#fullname').val();
            var mobile = $('#mobile').val();
            var statsDate = $('#stats_date').datebox('getValue');

            var queryParams = {
                fullname: fullname,
                mobile: mobile,
                statsDate: statsDate
            };

            datagrid.datagrid('options').queryParams = queryParams;
            datagrid.datagrid('load');
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
                            <td align="right" width="70">姓名：</td>
                            <td><input type="text" class="text" id="fullname"/>&nbsp;</td>
                            <td align="right" width="50">手机号：</td>
                            <td><input type="text" class="text" id="mobile"/></td>
                            <td align="right" width="90">认证时间：</td>
                            <td>
                                <input type="text" class="easyui-datebox" id="stats_date" name="statsDate" style="width: 140px;height: 28px;"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <h3>实名认证记录</h3>
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
