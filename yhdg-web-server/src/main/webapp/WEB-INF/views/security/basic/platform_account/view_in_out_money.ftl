<div style="padding-top: 5px;padding-bottom: 5px;">
    <div width="70" style="font-weight: 650;">
        <h3>平台账户流水</h3>
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
            url: "${contextPath}/security/basic/platform_account_in_out_money/page.htm?platformAccountId=${(entity.id)!''}",
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
                        title: '平台账户id',
                        align: 'center',
                        field: 'platformAccountId',
                        width: 60
                    },
                    {title: '业务id', align: 'center', field: 'bizId', width: 60},
                    {title: '业务类型', align: 'center', field: 'bizTypeName', width: 60},
                    {title: '类型', align: 'center', field: 'typeName', width: 60},
                    {
                        title: '金额',
                        align: 'center',
                        field: 'money',
                        width: 60,
                        formatter: function (val) {
                            return Number(val/100).toFixed(2) + "元";
                        }
                    },
                    {
                        title: '剩余金额',
                        align: 'center',
                        field: 'balance',
                        width: 60,
                        formatter: function (val) {
                            return Number(val/100).toFixed(2) + "元";
                        }
                    },
                    {
                        title: '操作人',
                        align: 'center',
                        field: 'operator',
                        width: 60
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
                        width: 100,
                        formatter: function (val, row) {
                            var html = '';
                            html += '<a href="javascript:view_record(ID)">查看</a>';
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


    function view_record(id) {
        App.dialog.show({
            css: 'width:400px;height:448px;overflow:visible;',
            title: '查看',
            href: "${contextPath}/security/basic/platform_account_in_out_money/view_record.htm?id=" + id,
            event: {
                onClose: function () {
                }
            }
        });
    }
</script>