<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">

        </div>
        <h3>客户充值记录</h3>
    </div>
    <div class="grid" style="height:345px;">
        <table id="page_table_box_${pid}"></table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border close">关闭</button>
</div>
<script>

    (function () {
        $('#page_table_box_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/customer_deposit_order/page.htm?customerId=${customerId}",
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
                        title: '订单Id',
                        align: 'center',
                        field: 'id',
                        width: 40
                    },
                    {
                        title: '充值金额(元)',
                        align: 'center',
                        field: 'money',
                        width: 40,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '客户',
                        align: 'center',
                        field: 'customerFullname',
                        width: 40
                    },
                    {
                        title: '手机',
                        align: 'center',
                        field: 'customerMobile',
                        width: 40
                    },
                    {
                        title: '充值类型',
                        align: 'center',
                        field: 'payTypeName',
                        width: 40
                    },
                    {
                        title: '充值状态',
                        align: 'center',
                        field: 'statusName',
                        width: 40
                    },
                    {
                        title: '处理时间',
                        align: 'center',
                        field: 'handleTime',
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
                        width: 30,
                        formatter: function(val, row) {
                            var html = '<a href="javascript:view_${pid}(ID)">查看&nbsp;&nbsp;</a>';
                            return html.replace(/ID/g, "'" + row.id + "'");
                        }
                    }
                ]
            ]
        });
    })();




    function view_${pid}(id) {
        App.dialog.show({
            css: 'width:530px;height:260px;overflow:visible;',
            title: '查看',
            href: "${contextPath}/security/basic/customer_deposit_order/view.htm?id=" + id
        });
    }

    (function() {
        var pid = '${pid}',
                win = $('#' + pid),
                form = win.find('form');

        win.find('button.close').click(function() {
            win.window('close');
        });
    })();
</script>