<div class="popup_body">
    <div style="width:1070px; height:470px; padding-top: 6px;">
        <table id="page_table_detail_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>
<script>
    (function() {

        var datagrid = $('#page_table_detail_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/agent_shop_balance_record_detail/page.htm?recordId=${id}",
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
                        title: '订单Id',
                        align: 'center',
                        field: 'orderId',
                        width: 30
                    },
                    {
                        title: '运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 40
                    },
                    {
                        title: '门店',
                        align: 'center',
                        field: 'shopName',
                        width: 40
                    },
                    {
                        title: '柜子Id',
                        align: 'center',
                        field: 'cabinetId',
                        width: 30
                    },
                    {
                        title: '订单天数',
                        align: 'center',
                        field: 'dayCount',
                        width: 25
                    },
                    {
                        title: '开始时间',
                        align: 'center',
                        field: 'beginTime',
                        width: 30
                    },
                    {
                        title: '结束时间',
                        align: 'center',
                        field: 'endTime',
                        width: 30
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'statusName',
                        width: 25
                    },
                    {
                        title: '客户手机',
                        align: 'center',
                        field: 'customerMobile',
                        width: 35
                    },
                    {
                        title: '客户名称',
                        align: 'center',
                        field: 'customerFullname',
                        width: 25
                    },
                    {
                        title: '支付类型',
                        align: 'center',
                        field: 'payTypeName',
                        width: 25
                    },
                    {
                        title: '支付时间',
                        align: 'center',
                        field: 'payTime',
                        width: 30
                    },
                    {
                        title: '订单金额',
                        align: 'center',
                        field: 'orderMoney',
                        width: 25,
                        formatter:function(val) {
                            return val / 100;
                        }
                    },
                    {
                        title: '门店比例',
                        align: 'center',
                        field: 'shopRatio',
                        width: 23,
                        formatter:function(val) {
                            return val+'%';
                        }
                    },
                    {
                        title: '门店固定分成金额',
                        align: 'center',
                        field: 'shopFixedMoney',
                        width: 23,
                        formatter:function(val) {
                            return val / 100;
                        }
                    },
                    {
                        title: '分配金额',
                        align: 'center',
                        field: 'money',
                        width: 23,
                        formatter:function(val) {
                            return val / 100;
                        }
                    }
                ]
            ]
        });
        $('#close_${pid}').click(function() {
            $('#${pid}').window('close');
        });
    })();

</script>


