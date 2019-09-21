<div style="padding-top: 5px;padding-bottom: 5px;">
    <div width="70" style="font-weight: 650;">
        <h3>商户流水</h3>
    </div>
</div>
<div style="height: 470px; padding-top: 6px;">
    <table id="partner_money_page_${pid}"></table>
</div>
<script>

    (function () {
        var win = $('#${pid}');
        $('#partner_money_page_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/partner/in_out_money_page.htm?partnerId=${(entity.id)!''}",
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
//                    {
//                        title: 'ID',
//                        align: 'center',
//                        field: 'id',
//                        width: 60
//                    },
                    {
                        title: '商户账户类型',
                        align: 'center',
                        field: 'partnerTypeName',
                        width: 60
                    },
//                    {title: '业务id', align: 'center', field: 'bizId', width: 60},
                    {title: '业务类型', align: 'center', field: 'bizTypeName', width: 90},
                    {title: '收入类型', align: 'center', field: 'typeName', width: 60},
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
                        title: '操作人',
                        align: 'center',
                        field: 'operator',
                        width: 60
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
                        width: 90
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 50,
                        formatter: function (val, row) {
                            var html = '';
                            html += '<a href="javascript:view_record(ID)">查看</a>';
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ],
            onLoadSuccess: function () {
                $('#partner_money_page_${pid}').datagrid('clearChecked');
                $('#partner_money_page_${pid}').datagrid('clearSelections');
            }
        });

        var ok = function () {
            return true;
        };

        win.data('ok', ok);
    })();


    function view_record(id) {
        App.dialog.show({
            css: 'width:400px;height:418px;overflow:visible;',
            title: '商户流水信息',
            href: "${contextPath}/security/basic/partner_in_out_money/view_record.htm?id=" + id,
            event: {
                onClose: function () {
                }
            }
        });
    }
</script>