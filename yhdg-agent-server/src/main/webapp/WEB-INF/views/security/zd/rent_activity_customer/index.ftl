<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <h3>活动客户</h3>
    </div>
    <div class="grid" style="height:325px;">
        <table id="page_table_scubinet_${pid}"></table>
    </div>
</div>
<script>

    (function () {
        var win = $('#${pid}');
        $('#page_table_scubinet_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/zd/rent_activity_customer/page.htm?activityId=${(activityId)!0}",
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
                        title: '客户姓名',
                        align: 'center',
                        field: 'fullname',
                        width: 60
                    },
                    {
                        title: '手机号码',
                        align: 'center',
                        field: 'mobile',
                        width: 60
                    }
                ]
            ]
        });
    })();

    $('#${pid}').data('ok', function() {
        return true;
    });
    function list_reload() {
        var datagrid = $('#page_table_scubinet_${pid}');
        datagrid.datagrid('reload');
    }

</script>
