<div class="popup_body">
    <div style="width:650px; height:366px; padding-top: 6px;">
        <table id="page_table_detail_${pid}">
        </table>
    </div>
</div>
<div class="popup_btn">
    <button class="btn btn_red ok" onclick="confirm()">确定</button>
    <button class="btn btn_border" id="close_${pid}">关闭</button>
</div>
<script>
    (function() {

        var datagrid = $('#page_table_detail_${pid}');
        datagrid.datagrid({
            fit: true,
            width: '100%',
            height: '100%',
            striped: true,
            pagination: false,
            url: "${contextPath}/security/basic/mp_push_message_template/agent_user_page.htm?userId=${userId}",
            fitColumns: true,
            idField: 'id',
            singleSelect: true,
            selectOnCheck: false,
            checkOnSelect: false,
            autoRowHeight: false,
            rowStyler: gridRowStyler,
            columns: [
                [
                    { field: 'checkbox', checkbox: true },
                    {
                        title: '公众名称',
                        align: 'center',
                        field: 'weixinmpName',
                        width: 40
                    },
                    {
                        title: '模板名称',
                        align: 'center',
                        field: 'templateName',
                        width: 40
                    },
                    {
                        title: '接收人',
                        align: 'center',
                        field: 'receiver',
                        width: 40
                    },
                    {
                        title: '启用',
                        align: 'center',
                        field: 'isActive',
                        width: 40,
                        formatter:function (val) {
                            if (val == 1){
                                return '是';
                            }else{
                                return '否';
                            }
                        }
                    }
                ]
            ],
            onLoadSuccess: function (data) {
                if (data) {
                    $.each(data.rows, function (index, item) {
                        if (item.userId == ${userId}) {
                            $('#page_table_detail_${pid}').datagrid('checkRow', index);
                        }
                    });
                }
            }
        });
        $('#close_${pid}').click(function() {
            $('#${pid}').window('close');
        });
    })();
    
    function confirm() {
        var mpPushs = $('#page_table_detail_${pid}').datagrid('getChecked');
        var idList = [];
        for (var j = 0; j < mpPushs.length; j++) {
            idList.push(mpPushs[j].id+"_"+mpPushs[j].weixinmpId);
        }
        $.post('${contextPath}/security/basic/mp_push_message_template/update_user_mp_push.htm', {
            idList: idList,
            userId:${userId}
        }, function (json) {
            if (json.success) {
                $('#${pid}').window('close');
                $.messager.alert('提示信息', '操作成功', 'info');
            } else {
                $.messager.alert('提示消息', json.message, 'info');
            }
        }, 'json');
    }

</script>


