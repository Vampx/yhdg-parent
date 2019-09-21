<div class="popup_body">
    <div style="width:800px; height:360px; padding-top: 6px;">
        <table id="page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>
<script>
    (function() {
        var win = $('#${pid}');
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/battery/page.htm",
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
                        title: '运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 60
                    },
                    {
                        title: '电池编号',
                        align: 'center',
                        field: 'id',
                        width: 60
                    },
                    {
                        title: '品牌',
                        align: 'center',
                        field: 'brandName',
                        width: 60
                    },
                    {
                        title: '电池类型',
                        align: 'center',
                        field: 'batteryType',
                        width: 60
                    }
                ]
            ],
            onLoadSuccess:function() {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            },onClickRow:  function(rowIndex, rowData) {
                select_${pid}(rowData.id);
            }
        });

        var data = ${data};
        var queryParams = $.toJSON(data);
        datagrid.datagrid('options').queryParams = queryParams;
        datagrid.datagrid('load');

        $('#close_${pid}').click(function () {
            win.window('close');
        })

        function select_${pid}(id) {
            App.dialog.show({
                css: 'width:780px;height:512px;overflow:visible;',
                title: '查看',
                href: "${contextPath}/security/hdg/battery/view.htm?id=" + id
            });
        }

    })();


</script>

