<div class="popup_body">
    <div class="search" style="margin:0 0; padding:2px 0 0 0; border-bottom:none;">
        <table cellspacing="0" cellpadding="0" border="0">
            <tr>
                <td align="right">电芯条码号：</td>
                <td><input type="text" style="width: 200px" class="text" id="barcode_code_${pid}"/>&nbsp;&nbsp;</td>
                <td>
                    <button class="btn btn_blue" id="query_${pid}">查询</button>
                </td>
                <td align="right">&nbsp;&nbsp;&nbsp;</td>
                <td>
                    <button class="btn btn_green" id="export_excel_${pid}">导出</button>
                </td>
            </tr>
        </table>
    </div>
    <div class="select_routes" >
        <div class="select_body" style="margin-right: 20px; top:29px; overflow: hidden;">
            <div class="grid" style="width: 685px; height:360px; margin-right: 20px;">
                <table id="table_${pid}"></table>
            </div>
        </div>
    </div>
</div>
<div class="popup_btn">
<#--  <button class="btn btn_red ok">确定</button>-->
    <button class="btn btn_border close">关闭</button>
</div>

<script>

    (function() {
        var pid = '${pid}', win = $('#' + pid), windowData = win.data('windowData');
        var datagrid = $('#table_${pid}');

        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: true,
            url: "${contextPath}/security/hdg/battery_barcode/page.htm?batteryFormatId=${(id)!''}",
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
                    },
                    {
                        title: '条码号',
                        align: 'center',
                        field: 'barcode',
                        width: 60
                    }
                ]
            ],
            onLoadSuccess:function() {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            }
        });

        win.find('#query_${pid}').click(function() {
            query();
        });

        win.find('#export_excel_${pid}').click(function() {
            exportExcel();
        });

        var exportExcel = function(){
            window.location.href = "${contextPath}/security/hdg/battery_barcode/export_excel.htm?batteryFormatId=${(id)!''}";
        };

        var query = function() {
            datagrid.datagrid('options').queryParams = {
                barcode: $('#barcode_code_${pid}').val()
            };
            datagrid.datagrid('load');
        };

        win.find('button.ok').click(function() {
            win.window('close');
        });

        win.find('button.close').click(function() {
            win.window('close');
        });

    })();

</script>