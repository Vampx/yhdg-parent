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
            url: "${contextPath}/security/basic/balance_record/view_foregift_remain_money_page.htm?balanceRecordId=${entity.id}",
            pageSize: 10,
            pageList: [10, 50, 100],
            idField: 'id',
            frozenColumns: [[
                {
                    title: 'ID',
                    align: 'center',
                    field: 'id',
                    width: 150
                },
                {
                    title: '商户',
                    align: 'center',
                    field: 'partnerName',
                    width: 50
                },
                {
                    title: '运营商',
                    align: 'center',
                    field: 'agentName',
                    width: 80
                }
            ]],
            columns: [
                [
                    {
                        title: '姓名',
                        align: 'center',
                        field: 'customerFullname',
                        width: 80
                    },
                    {
                        title: '手机号',
                        align: 'center',
                        field: 'customerMobile',
                        width: 120
                    },
                    {
                        title: '支付方式',
                        align: 'center',
                        field: 'payTypeName',
                        width: 100
                    },
                    {
                        title: '支付金额',
                        align: 'center',
                        field: 'money',
                        width: 100,
                        formatter: function (val) {
                            return Number(val / 100).toFixed(2) + " 元";
                        }
                    },
                    {
                        title: '电池类型',
                        align: 'center',
                        field: 'batteryTypeName',
                        width: 100
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'statusName',
                        width: 80
                    },
                    {
                        title: '开始时间',
                        align: 'center',
                        field: 'beginTime',
                        width: 180
                    },
                    {
                        title: '结束时间',
                        align: 'center',
                        field: 'endTime',
                        width: 180
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
                        width: 180
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


