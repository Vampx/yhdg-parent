<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">

        </div>
        <h3>客户分期付款明细</h3>
    </div>
    <div class="grid" style="height:345px;">
        <table id="page_table_box_${pid}"></table>
    </div>
</div>

<script>

    (function () {
        $('#page_table_box_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/customer_installment_record_pay_detail/page.htm?recordId=${recordId}",
            pageSize: 10,
            pageList: [10, 50, 100],
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
                        title: '客户姓名',
                        align: 'center',
                        field: 'fullname',
                        width: 40
                    },
                    {
                        title: '手机号',
                        align: 'center',
                        field: 'mobile',
                        width: 50
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'statusName',
                        width: 30
                    },
                    {
                        title: '分期总金额',
                        align: 'center',
                        field: 'totalMoney',
                        width: 40,
                        formatter: function(val) {
                            return new Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '本次支付总金额',
                        align: 'center',
                        field: 'money',
                        width: 50,
                        formatter: function(val) {
                            return new Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '支付时间',
                        align: 'center',
                        field: 'payTime',
                        width: 70
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'id',
                        width: 30,
                        formatter: function(val, row) {
                            var html = '';
                            html += '<a href="javascript:view_detail(ID)">查看</a>';
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ]
        });
    })();


    function reload() {
        var datagrid = $('#page_table');
        datagrid.datagrid('reload');

    }

    function query() {
        var datagrid = $('#page_table');
        var partnerId = $('#partner_id').combobox('getValue');
        var fullname = $('#fullname').val();
        var mobile = $('#mobile').val();
        var agentId = $('#agent_id').combotree('getValue');
        var status = $('#status').val();
        datagrid.datagrid('options').queryParams = {
            agentId: agentId,
            fullname: fullname,
            mobile: mobile,
            partnerId: partnerId,
            status: status
        };
        datagrid.datagrid('load');
    }

    function view_detail(id) {
        App.dialog.show({
            css: 'width:720px;height:312px;overflow:visible;',
            title: '查看',
            href: "${contextPath}/security/basic/customer_installment_record_pay_detail/view.htm?id=" + id,
        });
    }
</script>