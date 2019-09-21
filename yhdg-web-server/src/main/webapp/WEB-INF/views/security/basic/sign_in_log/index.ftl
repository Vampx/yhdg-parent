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
                url: "${contextPath}/security/basic/sign_in_log/page.htm",
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
                        {title: '客户Id', align: 'center', field: 'passportId', width: 100},
                        {title: '手机', align: 'center', field: 'mobile', width: 100},
                        {title: '签到日期', align: 'center', field: 'signDate', width: 100},
                        {title: '创建时间', align: 'center', field: 'createTime', width: 100},

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
            var mobile = $('#mobile').val();
            var signDate = $('#sign_date').val();
            datagrid.datagrid('options').queryParams = {
                mobile: mobile, signDate: signDate
            };
            datagrid.datagrid('load');
        }


        function view(id) {
            App.dialog.show({
                css: 'width:480px;height:270px;',
                title: '查看',
                href: "${contextPath}/security/basic/sign_in_log/view.htm?id=" + id
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
                            <td align="right">手机：</td>
                            <td><input type="text" class="text" id="mobile"/></td>
                            <td align="right">&nbsp;&nbsp;&nbsp;&nbsp;签到日期：</td>
                            <td><input type="text" class="text" id="sign_date"/></td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap">
                    <div class="toolbar clearfix">
                        <div class="float_right">
                        <#--
                            <button class="btn btn_green" onclick="add()">新建</button>
-->
                        </div>
                        <h3>签到</h3>
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
