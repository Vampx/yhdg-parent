<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>客户分期付款明细</h3>
    </div>
    <div class="grid" style="height:325px;">
        <table id="page_table_box_${pid}"></table>
    </div>
</div>

<script>

    (function () {
        $('#page_table_box_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            url: "${contextPath}/security/zd/rent_foregift_order/rent_installment_page.htm?sourceId=${(sourceId)!''}",
            fitColumns: true,
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '客户名称',
                        align: 'center',
                        field: 'fullname',
                        width: 150
                    },
                    {
                        title: '期数',
                        align: 'center',
                        field: 'num',
                        width: 150
                    },
                    {
                        title: '支付租金',
                        align: 'center',
                        field: 'packetMoney',
                        width: 150,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '付款状态',
                        align: 'center',
                        field: 'statusName',
                        width: 150
                    },
                    {
                        title: '支付时间',
                        align: 'center',
                        field: 'payTime',
                        width: 150
                    }
                <#--{-->
                <#--title: '操作',-->
                <#--align: 'center',-->
                <#--field: 'action',-->
                <#--width: 20,-->
                <#--formatter: function (val, row) {-->
                <#--var html = '<a href="javascript:view_${pid}(CUSTOMERID, SOURCETYPE,\'SOURCEID\', ORDERSTATUS)">查看</a>';-->
                <#--return html.replace(/CUSTOMERID/g, row.customerId).replace(/SOURCETYPE/g, row.sourceType).replace(/SOURCEID/g, row.sourceId).replace(/ORDERSTATUS/g, row.orderStatus);-->
                <#--}-->
                <#--}-->
                ]
            ]
        });
    })();

    <#--function view_${pid}(customerId, sourceType, sourceId, orderStatus) {-->
    <#--App.dialog.show({-->
    <#--css: 'width:580px;height:310px;overflow:visible;',-->
    <#--title: '查看',-->
    <#--href: "${contextPath}/security/basic/weixin_pay_order/view.htm?customerId=" + customerId + "&sourceType=" + sourceType + "&sourceId=" +sourceId + "&orderStatus=" + orderStatus-->

    <#--});-->
    <#--}-->

</script>


