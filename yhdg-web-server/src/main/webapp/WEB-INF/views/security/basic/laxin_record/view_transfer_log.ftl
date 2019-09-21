<div class="tab_item" style="display: block">
    <div class="toolbar clearfix">
        <div class="float_right">

        </div>
        <h3>转账日志</h3>
    </div>
    <div class="grid" style="height:345px;">
        <table id="page_table_box_${pid}"></table>
    </div>
</div>

<script>

    (function() {
        $('#page_table_box_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/basic/laxin_record_transfer_log/page.htm?recordId=${entity.id}",
            fitColumns: true,
            pageSize: 50,
            pageList: [50, 100],
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    {
                        title: '记录ID',
                        align: 'center',
                        field: 'recordId',
                        width: 40
                    },
                    {
                        title: '操作人',
                        align: 'center',
                        field: 'operatorName',
                        width: 40
                    },
                    {
                        title: '内容',
                        align: 'center',
                        field: 'content',
                        width: 100
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
                        width: 40
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'action',
                        width: 40,
                        formatter: function(val, row) {
                            var html = '';
                            html += '<a href="javascript:view_transfer_log_detail(\'ID\')">查看</a>&nbsp;&nbsp;';
                            return html.replace(/ID/g, row.id);
                        }
                    },
                ]
            ],
            onLoadSuccess:function() {
                $('#page_table').datagrid('clearChecked');
                $('#page_table').datagrid('clearSelections');
            }
        });
    })();
    function view_transfer_log_detail(id) {
        App.dialog.show({
            css: 'width:480px;height:500px;',
            title: '查看',
            href: "${contextPath}/security/basic/laxin_record_transfer_log/view.htm?id=" + id
        });
    }
</script>