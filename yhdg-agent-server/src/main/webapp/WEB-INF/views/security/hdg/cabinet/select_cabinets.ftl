<div class="popup_body">
    <div class="search">
        <div class="float_right">
            <button class="btn btn_yellow" id="query_${pid}">搜索</button>
        </div>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td align="right">换电柜编号：</td>
                <td><input type="text" class="text" id="cabinetId_${pid}"/></td>
                <td align="right">换电柜名称：</td>
                <td><input type="text" class="text" id="cabinetName_${pid}"/></td>
            </tr>
        </table>
    </div>
    <div style="width:800px; height:360px; padding-top: 6px;">
        <table id="page_table_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red" id="ok_${pid}">确定</button>
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
            url: "${contextPath}/security/hdg/cabinet/page.htm?agentId=${(agentId)!""}",
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
                        title: 'checkbox', checkbox: true
                    },
                    {
                        title: '运营商',
                        align: 'center',
                        field: 'agentName',
                        width: 200
                    },
                    {
                        title: '换电柜编号',
                        align: 'center',
                        field: 'id',
                        width: 200
                    },
                    {
                        title: '换电柜名称',
                        align: 'center',
                        field: 'cabinetName',
                        width: 200
                    }
                ]
            ],
            onLoadSuccess:function() {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            }
        });
        $('#ok_${pid}').click(function() {
            var cabinet = datagrid.datagrid('getSelected');
            if(cabinet) {
                cabinet = {
                    id: cabinet.id,
                    cabinetName: cabinet.cabinetName
                };
                windowData.ok({
                    cabinet:cabinet
                });
                win.window('close');
            } else {
                $.messager.alert('提示信息', '请选择换电柜');
            }

        });
        $('#close_${pid}').click(function() {
            $('#${pid}').window('close');
        });
        $('#query_${pid}').click(function() {
            query();
        });
        function query() {
            datagrid.datagrid('options').queryParams = {
                agentId: ${(agentId)!''},
                cabinetName: $('#cabinetName_${pid}').val(),
                id: $('#cabinetId_${pid}').val()
            };
            datagrid.datagrid({ url: "${contextPath}/security/hdg/cabinet/page.htm" });

            datagrid.datagrid('load');
        }

    })();

</script>


