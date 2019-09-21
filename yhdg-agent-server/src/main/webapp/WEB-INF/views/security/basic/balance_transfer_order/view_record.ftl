<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">
        </div>
        <h3>结算记录</h3>
    </div>
    <div class="grid" style="height:370px;">
        <table id="page_table_${pid}"></table>
    </div>
</div>

<script>
    (function () {
        var win = $('#${pid}');

        $('#page_table_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            url: "${contextPath}/security/basic/day_balance_record/page.htm?orderId=${entity.id}",
            fitColumns: true,
            pagination: true,
            pageSize: 50,
            pageList: [50,100,200],
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '结算日期',
                        align: 'center',
                        field: 'balanceDate',
                        width: 40
                    },
                    {
                        title: '类型',
                        align: 'center',
                        field: 'bizType',
                        width: 40,
                        formatter: function(val){
                        <#list BizTypeEnum as e>
                            if(${e.getValue()} == val)
                            return '${e.getName()}';
                        </#list>
                        }
                    },
                    {
                        title: '运营商名称',
                        align: 'center',
                        field: 'agentName',
                        width: 40
                    },
                    {
                        title: '运营账号',
                        align: 'center',
                        field: 'agentMobile',
                        width: 40
                    },
                    {
                        title: '运营账户',
                        align: 'center',
                        field: 'agentAccountName',
                        width: 40
                    },
                    {title: '总收入', align: 'center', field: 'money', width: 40,
                        formatter: function(val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'status',
                        width: 30,
                        formatter: function(val){
                        <#list StatusEnum as e>
                            if(${e.getValue()} == val)
                            return '${e.getName()}';
                        </#list>
                        }
                    },
                    {
                        title: '处理时间',
                        align: 'center',
                        field: 'handleTime',
                        width: 50
                    }
                ]
            ]
        });

        var ok = function () {
            return true;
        };

        win.data('ok', ok);
    })();

</script>