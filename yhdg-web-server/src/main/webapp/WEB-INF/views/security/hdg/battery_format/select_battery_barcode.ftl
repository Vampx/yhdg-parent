<div class="popup_body">
    <div class="search" style="margin:0 0; padding:2px 0 0 0; border-bottom:none;">
        <table cellspacing="0" cellpadding="0" border="0">
            <tr>
                <td align="right">电芯厂家：</td>
                <td>
                ${(entity.cellMfr)!''}
                </td>
                <td align="right">&nbsp;&nbsp;电芯型号：</td>
                <td>
                ${(entity.cellModel)!''}
                </td>
                <td align="right">&nbsp;&nbsp;当前条码：</td>
                <td>
                    <span style="color: red" class="cell_code">
                   <#-- <#if maxCode??>
                        ${(maxCode)!''}
                    <#else>
                        ${(defaultCode)!''}
                    </#if>-->
                         ${(defaultCode)!''}
                    </span>
                </td>
                <td align="right">&nbsp;&nbsp;生成条码数：</td>
                <td>
                    <input type="text" style="width: 100px" class="text" id="code_count_${pid}"/>
                </td>
                <td align="right">&nbsp;</td>
                <td>
                    <button class="btn btn_red" id="barcode_${pid}">生成</button>
                </td>
            </tr>
        </table>
    </div>
    <div class="search" style="margin:0 0; padding:2px 0 0 0; border-bottom:none;">
        <table cellspacing="0" cellpadding="0" border="0">
            <tr>
                <td align="right">电芯条码号：</td>
                <td><input type="text" class="text" style="width: 200px;" id="barcode_code_${pid}"/>&nbsp;&nbsp;</td>
                <td>
                    <button class="btn btn_blue" id="query_${pid}">查询</button>
                    <button class="btn btn_red" onclick="batchRemove()">批量删除</button>
                </td>
            </tr>
        </table>
    </div>
    <div class="select_routes" >
        <div class="select_body" style="margin-right: 20px; top:29px; overflow: hidden;">
            <div class="grid" style="width: 785px; height:360px; margin-right: 20px;">
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
            url: "${contextPath}/security/hdg/battery_barcode/page.htm?batteryFormatId=${(entity.id)!''}",
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
                    {field: 'checkbox', checkbox: true},
                    {
                        title: '序号',
                        align: 'center',
                        field: 'id',
                        width: 60
                    },
                    {
                        title: '条码号',
                        align: 'center',
                        field: 'barcode',
                        width: 60
                    }, {
                    title: '操作',
                    align: 'center',
                    field: 'action',
                    width: 70,
                    formatter: function (val, row) {
                        var html = '';
                        html += ' <a href="javascript:removeBarcode(\'ID\')">删除</a>';
                        return html.replace(/ID/g, row.id);
                    }
                }
                ]
            ],
            onLoadSuccess: function () {
                datagrid.datagrid('clearChecked');
                datagrid.datagrid('clearSelections');
            }
        });


        win.find('#barcode_${pid}').click(function() {
            var codeCount = $('#code_count_${pid}').val();
            if (codeCount == '') {
                $.messager.alert('提示信息', '请输入生成条码数', 'info');
                return false;
            }
            $('#barcode_${pid}').attr("disabled","disabled");
            $.post('${contextPath}/security/hdg/battery_barcode/create.htm',{
                barcodeRule:"${(barcodeRule)!''}",
                codeCount: codeCount,
                batteryFormatId: "${(entity.id)!''}",
                cellMfr: "${(entity.cellMfr)!''}",
                cellModel: "${(entity.cellModel)!''}"
            }, function(json) {
                if(json.success) {
                    $.messager.alert('提示信息', '操作成功', 'info');
                    datagrid.datagrid('load');
                    $.ajax({
                        type: 'POST',
                        url: '${contextPath}/security/hdg/battery_barcode/find_max_code.htm',
                        dataType: 'json',
                        data:{
                            batteryFormatId:${(entity.id)!''}
                        },
                        success: function (json) {
                            if (json.success) {
                                $('.cell_code').html(json.message);
                            }
                        }
                    });
                } else {
                    $.messager.alert('提示消息', json.message, 'info');
                }
            }, 'json');
            datagrid.datagrid('load');
        });

        win.find('#query_${pid}').click(function() {
            query();
        });

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

    function removeBarcode(id) {
        $.messager.confirm('提示信息', '确认删除?', function(ok) {
            if(ok) {
                $.post('${contextPath}/security/hdg/battery_barcode/delete.htm?id=' + id , function (json) {
                    if (json.success) {
                        $.messager.alert('提示消息', '删除成功', 'info');
                        reloadBarcode();
                    } else {
                        $.messager.alert('提示消息', json.message, 'info');
                    }
                }, 'json');
            }
        });
    }

    function reloadBarcode() {
        var datagrid = $('#table_${pid}');
        datagrid.datagrid('reload');
    }

    function batchRemove() {
        var datagrid = $('#table_${pid}');
        var checked = datagrid.datagrid('getChecked');
        var list = [];
        for (var i = 0; i < checked.length; i++) {
            list.push(checked[i].id);
        }
        if (list.length == 0) {
            $.messager.alert('info', '请选择记录', 'info');
            return;
        }

        $.messager.confirm('提示信息', '确认删除?', function (ok) {
            if (ok) {
                $.post('${contextPath}/security/hdg/battery_barcode/batch_remove.htm', {
                    idList: list
                }, function (json) {
                    if (json.success) {
                        $.messager.alert('提示信息', json.message, 'info');
                        reloadBarcode();
                    } else {
                        $.messager.alert('提示消息', json.message, 'info');
                    }
                }, 'json');
            }
        });

    }


</script>