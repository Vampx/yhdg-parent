<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="query_${pid}">搜索</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td align="right">电芯厂家：</td>
                <td><input type="text" class="text" id="cell_mfr_${pid}"/>&nbsp;&nbsp;</td>
                <td align="right">电芯型号：</td>
                <td><input type="text" class="text" id="cell_model_${pid}"/></td>
            </tr>
        </table>
    </div>
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
        var win = $('#${pid}'), windowData = win.data('windowData');
        var datagrid = $('#page_table_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/battery_cell_model/page.htm",
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
                        title: '序号',
                        align: 'center',
                        field: 'id',
                        width: 40
                    },
                    {
                        title: '电芯厂家',
                        align: 'center',
                        field: 'cellMfr',
                        width: 60
                    },
                    {
                        title: '电芯型号',
                        align: 'center',
                        field: 'cellModel',
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
    })();
    var win = $('#${pid}'), windowData = win.data('windowData');
    var datagrid = $('#page_table_${pid}');
    function select_${pid}() {
        var cellModel = datagrid.datagrid('getSelected');
        if(cellModel) {
            windowData.ok({
                cellModel: cellModel
            });
            win.window('close');
        } else {
            $.messager.alert('提示信息', '请选择记录');
        }
    }

    $('#close_${pid}').click(function() {
        $('#${pid}').window('close');
    });
    $('#query_${pid}').click(function() {
        queryCellModel();
    });
    function queryCellModel() {
        datagrid.datagrid('options').queryParams = {
            cellMfr: $('#cell_mfr_${pid}').val(),
            cellModel: $('#cell_model_${pid}').val()
        };
        datagrid.datagrid('load');
    }

</script>






