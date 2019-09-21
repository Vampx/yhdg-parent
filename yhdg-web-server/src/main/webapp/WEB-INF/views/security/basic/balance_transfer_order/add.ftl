<div class="popup_body popup_body_full">
    <div class="ui_table">
        <input type="hidden" name="transferImagePath" id="transfer_image_path_${pid}">
        <table cellpadding="0" cellspacing="0" border="0" width="100%">
            <tr>
                <td width="70" align="right">转账类型：</td>
                <td width="170" align="right">
                    <input type="text" name="agentId" id="agent_id_${pid}" class="easyui-combotree" editable="false"
                           style="width:250px; height: 28px;"
                           data-options="url:'${contextPath}/security/basic/agent/tree.htm?dummy=${'无'?url}',
                                            method:'get',
                                            valueField:'id',
                                            textField:'text',
                                            editable:false,
                                            multiple:false,
                                            panelHeight:'500',
                                            onClick: function(node) {
                                                eliminate($('#agent_id_${pid}').combotree('getValue'));
                                            }
                                        "
                            >
                </td>
                <td width="70" align="right">操作人：</td>
                <td><input type="text" class="text easyui-validatebox" id="handleUser_${pid}" name="handleUser"
                           maxlength="40" readonly value="${(Session['SESSION_KEY_USER'].username)!''}"/></td>
            </tr>
            <tr>
                <td align="right" width="75">备注：</td>
                <td colspan="6"><textarea id="memo_${pid}" name="memo" maxlength="200" style="width:99%;"></textarea>
                </td>
            </tr>
            <tr>
                <td align="right" width="75">转账记录：</td>
                <td colspan="6">
                    <button class="btn btn_blue" onclick="select_day_balance_record()">选择</button>
                    <div class="grid" style="height:305px; margin-top: 10px;">
                        <table id="day_balance_record_${pid}">
                        </table>
                    </div>
                </td>
            </tr>
            <tr>
                <td align="right" width="75"></td>
                <td colspan="6" style="text-align: right; padding-right: 20px;">共计：<input type="text" class="text easyui-numberspinner" style="width:160px;height:28px " id="totalMoney" readonly data-options="min:0.00,precision:2"/>元
                </td>
            </tr>
            <tr>
                <td align="right">转账凭证：</td>
                <td colspan="6">
                    <div class="portrait">
                        <a href="javascript:void(0)"><img id="image_${pid}" src=""><span>上传凭证</span></a>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</div>
<div class="popup_btn popup_btn_full">
    <button class="btn btn_red ok">确定</button>
    <button class="btn btn_border close">关闭</button>
</div>
<script>
    function set_transfer_image_path(param) {
        $('#image_${pid}').attr('src', '${(controller.appConfig.staticUrl)!''}' + param.filePath);
        $('#transfer_image_path_${pid}').val(param.filePath);
        $('#' + param.pid).window('close');
    }

    function eliminate() {
        $('#day_balance_record_${pid}').datagrid('loadData', []);//清空
    }


    function select_day_balance_record() {
        var agentId = $('#agent_id_${pid}').combotree("getValue");
        var bizType = 1;
        var hash = {};
        var row = $('#day_balance_record_${pid}').datagrid('getRows');//获取当前的数据行
        for (var i = 0; i < row.length; i++) {
            var key = '' + row[i].id;
            hash[key] = key;
        }
        //alert(hash.size);
        //alert(hash1.length);
        if (agentId == '') {
            $.messager.alert('提示信息', '转账类型不能为空!', 'info');
            return false;
        }
        App.dialog.show({
            css: 'width:970px;height:480px;',
            title: '结算记录表',
            href: "${contextPath}/security/basic/day_balance_record/select_day_balance_record.htm?agentId=" + agentId + "&bizType=" + bizType,
            windowData: {
                ok: function (checked) {
                    var totalMoney = 0;
                    for (var i = 0; i < checked.length; i++) {
                        var key = '' + checked[i].id;
                        if (!hash[key]) {
                            //hash[key] = key;
                            //alert(key);
                        } else {
                            $.messager.alert('提示信息', '记录重复', 'info');
                            return;
                        }
                        $('#day_balance_record_${pid}').datagrid('appendRow',
                                checked[i],
                                totalMoney += (checked[i].money),
                                hash[key] = key
                        );
                        //alert(hash);
                    }
                    var row = $('#day_balance_record_${pid}').datagrid('getRows');//获取当前的数据行
                    var Money = 0;
                    for (var i = 0; i < row.length; i++) {
                        Money += row[i]['money'];
                    }
                    $('#totalMoney').attr('value', Number(Money / 100).toFixed(2));

                }
            },
            event: {
                onClose: function () {
                    reload();
                }
            }
        });
    }

    (function () {
        $('#day_balance_record_${pid}').datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: false,

            fitColumns: true,
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
                        title: '结算日期',
                        align: 'center',
                        field: 'balanceDate',
                        width: 40
                    },
                    {
                        title: '类型',
                        align: 'center',
                        field: 'bizType',
                        width: 40,
                        formatter: function (val) {
                        <#list BizTypeEnum as e>
                            if (${e.getValue()} == val
                            )
                            return '${e.getName()}';
                        </#list>
                        }
                    },
                    {
                        title: '运营商名称',
                        align: 'center',
                        field: 'agentName',
                        width: 40
                    },
                    {
                        title: '运营账号',
                        align: 'center',
                        field: 'agentMobile',
                        width: 40
                    },
                    {
                        title: '运营账户',
                        align: 'center',
                        field: 'agentAccountName',
                        width: 40
                    },
                    {
                        title: '总收入', align: 'center', field: 'money', width: 40,
                        formatter: function (val) {
                            return Number(val / 100).toFixed(2);
                        }
                    },
                    {
                        title: '状态',
                        align: 'center',
                        field: 'status',
                        width: 50,
                        formatter: function (val) {
                        <#list StatusEnum as e>
                            if (${e.getValue()} == val
                            )
                            return '${e.getName()}';
                        </#list>
                        }
                    },
                    {
                        title: '操作',
                        align: 'center',
                        field: 'id',
                        width: 30,
                        formatter: function (val, row) {
                            var html = '<a href="javascript:deleteRow(ID)">移除</a>';
                            return html.replace(/ID/g, row.id);
                        }
                    }
                ]
            ]
        });
    })();

    function deleteRow(id) {
        var rows = $('#day_balance_record_${pid}').datagrid('getSelections');
        var selectRows = [];
        for (var i = 0; i < rows.length; i++) {
            selectRows.push(rows[i]);
        }
        for (var j = 0; j < selectRows.length; j++) {
            var index = $('#day_balance_record_${pid}').datagrid('getRowIndex', selectRows[j]);
            $('#day_balance_record_${pid}').datagrid('deleteRow', index);
        }
        var row = $('#day_balance_record_${pid}').datagrid('getRows');//获取当前的数据行
        var totalMoney = 0;
        for (var i = 0; i < row.length; i++) {
            totalMoney += row[i]['money'];
        }
        $('#totalMoney').attr('value', Number(totalMoney / 100).toFixed(2));
    }


    (function () {
        var pid = '${pid}',
                win = $('#' + pid);
        win.find('button.ok').click(function () {
            var checked = $('#day_balance_record_${pid}').datagrid('getRows');
            if (checked.length > 0) {
                $.messager.confirm('提示信息', '确认提交？', function (ok) {
                    if (ok) {
                        var ids = [];
                        var memo = $('#memo_${pid}').val();
                        var transferImagePath = $('#transfer_image_path_${pid}').val();
                        for (var i = 0; i < checked.length; i++) {
                            ids.push(checked[i].id);
                        }
                        $.post('${contextPath}/security/basic/balance_transfer_order/create.htm', {
                            ids: ids,
                            memo: memo,
                            transferImagePath: transferImagePath
                        }, function (json) {
                            if (json.success) {
                                $.messager.alert('info', '操作成功', 'info');
                                win.window('close');
                            } else {
                                $.messager.alert('提示消息', json.message, 'info');
                            }
                        }, 'json');
                    }
                });
            } else {
                $.messager.alert('提示信息', '数据不能为空', 'info');
                return false;
            }

        });

        win.find('button.close').click(function () {
            win.window('close');
        });
        win.find('.portrait').click(function () {
            App.dialog.show({
                css: 'width:558px;height:150px;overflow:visible;',
                title: '上传图片',
                href: "${contextPath}/security/basic/balance_transfer_order/image_path.htm",
                event: {
                    onClose: function () {
                    }
                }
            });
        });
    })()


</script>
