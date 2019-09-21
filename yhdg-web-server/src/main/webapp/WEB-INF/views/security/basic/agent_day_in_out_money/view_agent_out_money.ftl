<div class="popup_body">
    <div style="width:900px; height:420px; padding-top: 6px;">
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
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/agent_in_out_money/page.htm",
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
                        title: '运营商名称',
                        align: 'center',
                        field: 'agentName',
                        width: 40
                    },
                    {
                        title: '发生时间',
                        align: 'center',
                        field: 'createTime',
                        width: 40
                    },
                    {
                        title: '业务类型',
                        align: 'center',
                        field: 'bizTypeName',
                        width: 30
                    },
                    {
                        title: '订单类型',
                        align: 'center',
                        field: 'typeName',
                        width: 30
                    },
                    {
                        title: '金额',
                        align: 'center',
                        field: 'money',
                        width: 30,
                        formatter: function (val) {
                            return new Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '剩余金额',
                        align: 'center',
                        field: 'balance',
                        width: 30,
                        formatter: function (val) {
                            return new Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '操作人',
                        align: 'center',
                        field: 'operator',
                        width: 40
                    }
                ]
            ],
            queryParams: {
                agentId: ${agentId},
                statsDate: ${statsDate},
                type: 2
            }
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


