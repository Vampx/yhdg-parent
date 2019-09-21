<div class="popup_body">
    <div style="width:750px; height:360px; padding-top: 6px;">
        <table id="parent_page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>
<script>
    (function() {
        var win = $('#${pid}'), windowData = win.data('windowData');
        var datagrid = $('#parent_page_table_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/exchange_installment_detail/page.htm?settingId=${(settingId)!''}",
            fitColumns: true,
            pageSize: 10,
            pageList: [10, 50, 100],
            singleSelect: true,
            selectOnCheck: true,
            checkOnSelect: true,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '期数',
                        align: 'center',
                        field: 'num',
                        width: 60
                    },
                    {
                        title: '本次总金额',
                        align: 'center',
                        field: 'money',
                        width: 50,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2) + "元";
                        }
                    },
                    {
                        title: '本次押金支付金额',
                        align: 'center',
                        field: 'foregiftMoney',
                        width: 50,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2) + "元";
                        }
                    },
                    {
                        title: '本次租金支付金额',
                        align: 'center',
                        field: 'packetMoney',
                        width: 50,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2) + "元";
                        }
                    },
                    {
                        title: '本次保险支付金额',
                        align: 'center',
                        field: 'insuranceMoney',
                        width: 50,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2) + "元";
                        }
                    },
                    {title: '截至时间', align: 'center', field: 'expireTime', width: 60}
                ]
            ],
            onLoadSuccess:function() {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            }
        });


        $('#close_${pid}').click(function() {
            $('#${pid}').window('close');
        });

    })();

</script>
