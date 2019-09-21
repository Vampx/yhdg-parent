<div class="popup_body">
    <div class="search">
        <h4>
            <div>
                手机号码：
                <input style="width: 100px" class="text" id="code_${pid}" type="text">&nbsp;&nbsp;
                <button class="btn btn_yellow" id="query_${pid}">搜索</button>
            </div>
        </h4>
    </div>
    <div style="width:700px; height:310px; padding-top: 6px;">
        <table id="page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>

    <script>
        (function() {
            var datagrid = $('#page_table_${pid}');
            datagrid.datagrid({
                fit: true,
                width: '100%',
                height: '100%',
                striped: true,
                pagination: true,
                url: "${contextPath}/security/hdg/exchange_installment_customer/page.htm?settingId=${(settingId)!''}",
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
                            title: '分期设置ID',
                            align: 'center',
                            field: 'settingId',
                            width: 180
                        },
                        {
                            title: '骑手姓名',
                            align: 'center',
                            field: 'customerFullname',
                            width: 180
                        },
                        {
                            title: '骑手手机号',
                            align: 'center',
                            field: 'customerMobile',
                            width: 180
                        },

                    ]
                ],
                onLoadSuccess:function() {
                    datagrid.datagrid('clearChecked');
                    datagrid.datagrid('clearSelections');
                }
            });
            $('#query_${pid}').click(function() {
                datagrid.datagrid('options').queryParams = {
                    customerMobile: $('#code_${pid}').val()
                };
                datagrid.datagrid('load');
            });
            $('#close_${pid}').click(function() {
                $('#${pid}').window('close');
            });

        })();

    </script>