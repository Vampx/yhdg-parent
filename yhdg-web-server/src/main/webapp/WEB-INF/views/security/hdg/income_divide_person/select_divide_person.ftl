<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="query_${pid}">搜索</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td align="right">受益人：</td>
                <td><input type="text" class="text" id="person_name_${pid}"/>&nbsp;&nbsp;</td>
            </tr>
        </table>
    </div>
    <div style="width:800px; height:360px; padding-top: 6px;">
        <table id="page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_yellow" id="delete_${pid}">清空</button>
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
            url: "${contextPath}/security/hdg/income_divide_person/page.htm?agentId=${(agentId)!''}",
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
                        width: 80
                    },
                    {
                        title: '受益人',
                        align: 'center',
                        field: 'personName',
                        width: 60
                    },
                    {
                        title: '创建时间',
                        align: 'center',
                        field: 'createTime',
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
        var dividePerson = datagrid.datagrid('getSelected');
        if(dividePerson) {
            windowData.ok({
                dividePerson: dividePerson
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
        queryPersonName();
    });
    function queryPersonName() {
        datagrid.datagrid('options').queryParams = {
            personName: $('#person_name_${pid}').val()
        };
        datagrid.datagrid('load');
    }
    $('#delete_${pid}').click(function() {
        var dividePerson = datagrid.datagrid('getSelected');
        dividePerson = {
            id: "",
            personName: ""
        };
        windowData.ok({
            dividePerson:dividePerson
        });
        win.window('close');
    });
</script>





