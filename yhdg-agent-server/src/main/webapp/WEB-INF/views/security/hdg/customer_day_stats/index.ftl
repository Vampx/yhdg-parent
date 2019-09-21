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
                url: "${contextPath}/security/hdg/customer_day_stats/page.htm",
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
                        {title: '统计日期', align: 'center', field: 'statsDate', width: 60},
                        {title: '客户名称', align: 'center', field: 'customerName', width: 60},
                        {title: '客户手机号', align: 'center', field: 'customerMobile', width: 60},
                        {title: '充电次数', align: 'center', field: 'orderCount', width: 60},
                        {title: '充电花费(元)', align: 'center', field: 'money', width: 60,
                            formatter: function(val) {
                                return new Number(val / 100).toFixed(2);
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
            var customerName = $('#customer_name').val();
            var customerMobile = $('#customer_mobile').val();
            var statsDate = $('#stats_date').datebox('getValue');
            datagrid.datagrid('options').queryParams = {
                customerName: customerName,
                customerMobile: customerMobile,
                statsDate: statsDate
            };
            datagrid.datagrid('load');
        }

        function exportExcel() {
            $.messager.confirm('提示信息', '确认导出数据?', function(ok) {
                if(ok) {
                    var statsDate = $('#stats_date').datebox('getValue');
                    var customerName = $('#customer_name').val();
                    var customerMobile = $('#customer_mobile').val();
                    $.messager.progress();
                    $.post('${contextPath}/security/hdg/customer_day_stats/export_excel.htm', {
                        customerName: customerName,
                        customerMobile: customerMobile,
                        statsDate: statsDate
                    }, function (json) {
                        $.messager.progress('close');
                        if (json.success) {
                            document.location.href = '${contextPath}/security/hdg/customer_day_stats/download.htm?filePath=' + json.data;
                        } else {
                            $.messager.alert('提示消息', json.message, 'info');
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
                <div class="panel search" >
                    <div class="float_right">
                        <button class="btn btn_yellow" onclick="query()">搜索</button>
                    </div>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td align="right">客户名称：</td>
                            <td><input type="text" class="text" id="customer_name"/></td>
                            <td align="right" width="70px;">客户手机：</td>
                            <td><input type="text" class="text"  id="customer_mobile" maxlength="11"/></td>
                            <td align="right"width="50px;">日期：</td>
                            <td><input type="text" class="easyui-datebox" id="stats_date" required="required"/>&nbsp;&nbsp;</td>
                        </tr>
                    </table>
                </div>
                <div class="panel grid_wrap" >
                    <div class="toolbar clearfix">
                        <div class="float_right">
                            <@app.has_oper perm_code='2_8_5_2'>
                                <button class="btn btn_red" onclick="exportExcel()">导出数据</button>
                            </@app.has_oper>
                        </div>
                        <h3>客户日使用统计表</h3>
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