<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">

        </div>
        <h3>批量发微信推送详情</h3>
    </div>
    <div class="grid" style="height:325px;">
        <table id="page_table_point_${pid}"></table>
    </div>
</div>


<script>

    (function () {
        var win = $('#${pid}');

        $('#page_table_point_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/batch_weixinmp_template_message_detail/page.htm?batchId=${batchId}",
            fitColumns: true,
            pageSize: 50,
            pageList: [50,100],
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '手机号码',
                        align: 'center',
                        field: 'mobile',
                        width: 60
                    },
                    {
                        title: 'openId',
                        align: 'center',
                        field: 'openId',
                        width: 60
                    }

                ]
            ]
        });

        win.find('button.query').click(function () {
            var datagrid = $('#page_table_point_${pid}');
            datagrid.datagrid('load');
        });


        var ok = function () {
            return true;
        };

        win.data('ok', ok);
    })();

    function list_point_reload() {
        var datagrid = $('#page_table_point_${pid}');
        datagrid.datagrid('reload');
    }

</script>