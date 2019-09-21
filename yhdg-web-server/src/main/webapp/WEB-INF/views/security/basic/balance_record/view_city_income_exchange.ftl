<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_red" id="ok_${pid}">导出数据</button>
        </div>
        <h4>
            <div>
                月份：
                <input style="width: 100px" class="text" id="query_time_${pid}" type="text">&nbsp;&nbsp;
                <button class="btn btn_yellow" id="query_${pid}">搜索</button>
            </div>
        </h4>
    </div>
    <div style="width:1030px; height:350px; padding-top: 6px;">
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
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            fit: true,
            striped: true,
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            collapsible: true,
            pagination: true,
            url: "${contextPath}/security/basic/balance_record/view_city_income_exchange_page.htm?balanceRecordId=${entity.id}",
            pageSize: 10,
            pageList: [10, 50, 100],
            idField: 'id',
            frozenColumns: [[
                {
                    title: 'ID',
                    align: 'center',
                    field: 'id',
                    width: 50
                },
                {
                    title: '平台',
                    align: 'center',
                    field: 'partnerName',
                    width: 60
                },
                {
                    title: '运营商',
                    align: 'center',
                    field: 'agentName',
                    width: 80
                },
                {
                    title: '分配类型',
                    align: 'center',
                    field: 'orgTypeName',
                    width: 100
                }
            ]],
            columns: [
                [
                    {
                        title: '分配单位名称',
                        align: 'center',
                        field: 'orgName',
                        width: 120
                    },
                    {
                        title: '客户名称',
                        align: 'center',
                        field: 'customerName',
                        width: 100
                    },
                    {
                        title: '客户手机号',
                        align: 'center',
                        field: 'customerMobile',
                        width: 120
                    },
                    {
                        title: '天数',
                        align: 'center',
                        field: 'dayCount',
                        width: 60
                    },
                    {
                        title: '服务类型',
                        align: 'center',
                        field: 'serviceTypeName',
                        width: 100
                    },
                    {
                        title: '订单金额',
                        align: 'center',
                        field: 'orderMoney',
                        width: 100,
                        formatter: function (val) {
                            return Number(val / 100).toFixed(2) + "元";
                        }
                    },
                    {
                        title: '分配金额',
                        align: 'center',
                        field: 'money',
                        width: 100,
                        formatter: function (val) {
                            return Number(val / 100).toFixed(2) + "元";
                        }
                    },
                    {
                        title: '统计时间',
                        align: 'center',
                        field: 'statsDate',
                        width: 100
                    },
                    {
                        title: '付款时间',
                        align: 'center',
                        field: 'payTime',
                        width: 150
                    }
                ]
            ]
        });
        $('#close_${pid}').click(function() {
            $('#${pid}').window('close');
        });
        function reload() {
            var datagrid = $('#page_table_${pid}');
            datagrid.datagrid('reload');
        }
    })();

</script>


