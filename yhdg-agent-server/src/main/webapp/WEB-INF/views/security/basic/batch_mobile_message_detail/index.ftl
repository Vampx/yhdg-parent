<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">
        <#--<button class="btn btn_green edit_one">1元</button>
        <button class="btn btn_green edit_two">2元</button>
        <button class="btn btn_green edit_three">3元</button>-->

        </div>
        <h3>批量短信发送详情</h3>
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
            url: "${contextPath}/security/basic/batch_mobile_message_detail/page.htm?batchId=${batchId}",
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
                        width: 50
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