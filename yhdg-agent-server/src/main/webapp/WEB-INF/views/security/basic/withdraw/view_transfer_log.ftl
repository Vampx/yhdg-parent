<div style="padding-top: 5px;padding-bottom: 5px;">
    <div width="70" style="font-weight: 650;">
        <h3>提现转账日志</h3>
    </div>
</div>
<div style="height: 470px; padding-top: 6px;">
    <table id="platform_money_page_${pid}"></table>
</div>
<script>

    (function () {
        var win = $('#${pid}');
        $('#platform_money_page_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/withdraw_transfer_log/page.htm?withdrawId=${id!}",
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
                    {title: '订单ID', align: 'center', field: 'withdrawId', width: 60},
                    {title: '操作人', align: 'center', field: 'operatorName', width: 60},
                    {title: '返回结果', align: 'center', field: 'content', width: 100},
                    {title: '类型', align: 'center', field: 'typeName', width: 60},
                    {title: '创建时间', align: 'center', field: 'createTime', width: 80},
                    {title: '操作', align: 'center', field: 'action', width: 50,
                        formatter: function (val, row) {
                            var html = '';
                            html += '<a href="javascript:view_transfer_log(ID)">查看</a>';
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ],
            onLoadSuccess: function () {
                $('#platform_money_page_${pid}').datagrid('clearChecked');
                $('#platform_money_page_${pid}').datagrid('clearSelections');
            }
        });

        var ok = function () {
            return true;
        };

        win.data('ok', ok);
    })();


    function view_transfer_log(id) {
        App.dialog.show({
            css: 'width:500px;height:350px;overflow:visible;',
            title: '查看',
            href: "${contextPath}/security/basic/withdraw_transfer_log/view.htm?id=" + id,
            event: {
                onClose: function () {
                }
            }
        });
    }
</script>